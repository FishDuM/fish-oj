package hk.ljx.fishoj.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hk.ljx.fishoj.common.constant.SubmitStatus;
import hk.ljx.fishoj.common.constant.UserProblemStatus;
import hk.ljx.fishoj.user.entity.UserProblem;
import hk.ljx.fishoj.user.mapper.UserProblemMapper;
import hk.ljx.fishoj.user.service.UserProblemService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserProblemServiceImpl
        extends ServiceImpl<UserProblemMapper, UserProblem>
        implements UserProblemService {

    /**
     * 提交判题完成后调用, 更新用户做题统计 (首次走 insert, 后续走 update)
     * @param userId 用户 id
     * @param problemId 题目 id
     * @param status 本次判题最终状态 (accepted, wrong_answer, ...)
     * @param score 本次得分
     */
    @Override
    public void recordSubmit(Long userId, Long problemId, String status, int score) {
        // 读旧记录, 没有就当首次提交
        UserProblem record = getOne(new LambdaQueryWrapper<UserProblem>()
                .eq(UserProblem::getUserId, userId)
                .eq(UserProblem::getProblemId, problemId));

        if (record == null) {
            record = UserProblem.builder()
                    .userId(userId)
                    .problemId(problemId)
                    .submitCount(1)
                    .acCount(SubmitStatus.ACCEPTED.getValue().equals(status) ? 1 : 0)
                    .bestScore(score)
                    .status(determineStatus(null, status))
                    .lastSubmitTime(LocalDateTime.now())
                    .build();
        } else {
            // 累加提交次数, AC 数, 最高分
            record.setSubmitCount(record.getSubmitCount() + 1);
            if (SubmitStatus.ACCEPTED.getValue().equals(status)) {
                record.setAcCount(record.getAcCount() + 1);
            }
            if (score > record.getBestScore()) {
                record.setBestScore(score);
            }
            record.setStatus(determineStatus(record.getStatus(), status));
            record.setLastSubmitTime(LocalDateTime.now());
        }
        // 首次走 insert, 后续走 update, MyBatis-Plus 自己判断
        saveOrUpdate(record);
    }

    /**
     * 获取指定用户的全部做题记录
     * @param userId 用户 id
     * @return 做题记录列表, 按最近提交时间倒序
     */
    @Override
    public List<UserProblem> listMy(Long userId) {
        // 只看自己的做题记录
        return list(new LambdaQueryWrapper<UserProblem>()
                .eq(UserProblem::getUserId, userId)
                .orderByDesc(UserProblem::getLastSubmitTime));
    }

    /**
     * 获取用户在某题上的做题进度 (没提交过则返回空骨架)
     * @param userId 用户 id
     * @param problemId 题目 id
     * @return 做题记录, 若不存在则为空骨架
     */
    @Override
    public UserProblem getOrEmpty(Long userId, Long problemId) {
        UserProblem up = getOne(new LambdaQueryWrapper<UserProblem>()
                .eq(UserProblem::getUserId, userId)
                .eq(UserProblem::getProblemId, problemId));
        if (up == null) {
            // 没提交过的题返一个空骨架, 前端不用判 null
            up = UserProblem.builder()
                    .userId(userId)
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