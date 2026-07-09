package hk.ljx.fishoj.user.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hk.ljx.fishoj.common.constant.SubmitStatus;
import hk.ljx.fishoj.common.constant.UserProblemStatus;
import hk.ljx.fishoj.user.entity.UserProblem;
import hk.ljx.fishoj.user.mapper.UserProblemMapper;
import hk.ljx.fishoj.user.service.UserProblemService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserProblemServiceImpl
        extends ServiceImpl<UserProblemMapper, UserProblem>
        implements UserProblemService {

    /**
     * 提交判题完成后调用, CAS 累加更新用户做题统计 (首次走 insert, 后续走 update)
     * @param userId 用户 id
     * @param problemId 题目 id
     * @param status 本次判题最终状态 (accepted, wrong_answer, ...)
     * @param score 本次得分
     */
    @Override
    @Transactional
    public void recordSubmit(Long userId, Long problemId, String status, int score) {
        boolean isAc = SubmitStatus.ACCEPTED.getValue().equals(status);
        // 读旧 status (用于锁死 ac)
        UserProblem existing = getOne(new LambdaQueryWrapper<UserProblem>()
                .eq(UserProblem::getUserId, userId)
                .eq(UserProblem::getProblemId, problemId));
        String currentStatus = existing == null ? null : existing.getStatus();
        String nextStatus = determineStatus(currentStatus, status);

        // CAS 累加: submitCount/acCount 用 setIncrBy, best_score 用 setSql + 安全格式
        LambdaUpdateWrapper<UserProblem> cas = new LambdaUpdateWrapper<UserProblem>()
                .eq(UserProblem::getUserId, userId)
                .eq(UserProblem::getProblemId, problemId)
                .setIncrBy(UserProblem::getSubmitCount, 1)
                .setIncrBy(UserProblem::getAcCount, isAc ? 1 : 0)
                .setSql("best_score = GREATEST(best_score, " + score + ")")
                .set(true, UserProblem::getStatus, nextStatus, null)
                .set(true, UserProblem::getLastSubmitTime, LocalDateTime.now(), null);
        boolean affected = update(cas);

        if (!affected) {
            // 首次提交: 插入新记录, 重复插入则重试 CAS 更新
            UserProblem record = UserProblem.builder()
                    .userId(userId)
                    .problemId(problemId)
                    .submitCount(1)
                    .acCount(isAc ? 1 : 0)
                    .bestScore(score)
                    .status(nextStatus)
                    .lastSubmitTime(LocalDateTime.now())
                    .build();
            try {
                save(record);
            } catch (DuplicateKeyException e) {
                // 并发场景: 另一线程已插入, 退回去执行累加
                update(cas);
            }
        }
    }

    /**
     * 获取当前登录用户的全部做题记录 (按最近提交时间倒序)
     * @return 做题记录列表
     */
    @Override
    public List<UserProblem> listMy() {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        return list(new LambdaQueryWrapper<UserProblem>()
                .eq(UserProblem::getUserId, currentUserId)
                .orderByDesc(UserProblem::getLastSubmitTime));
    }

    /**
     * 获取当前登录用户在某题上的做题进度 (没提交过则返回空骨架)
     * @param problemId 题目 id
     * @return 做题记录, 若不存在则为空骨架
     */
    @Override
    public UserProblem getOrEmpty(Long problemId) {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        UserProblem up = getOne(new LambdaQueryWrapper<UserProblem>()
                .eq(UserProblem::getUserId, currentUserId)
                .eq(UserProblem::getProblemId, problemId));
        if (up == null) {
            // 没提交过的题返一个空骨架, 前端不用判 null
            up = UserProblem.builder()
                    .userId(currentUserId)
                    .problemId(problemId)
                    .status(UserProblemStatus.NONE.getValue())
                    .bestScore(0)
                    .submitCount(0)
                    .acCount(0)
                    .build();
        }
        return up;
    }

    /**
     * 状态机: 一旦 AC 过就锁死 ac, 不再退回 attempted
     */
    private String determineStatus(String current, String newStatus) {
        if (UserProblemStatus.AC.getValue().equals(current) || SubmitStatus.ACCEPTED.getValue().equals(newStatus)) {
            return UserProblemStatus.AC.getValue();
        }
        return UserProblemStatus.ATTEMPTED.getValue();
    }
}