package hk.ljx.fishoj.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import hk.ljx.fishoj.user.entity.UserProblem;

public interface UserProblemService extends IService<UserProblem> {

    /**
     * 提交判题完成后调用, 更新用户做题统计
     * @param userId    提交用户ID
     * @param problemId 题目ID
     * @param status    本次判题最终状态 (accepted, wrong_answer, ...)
     * @param score     本次得分
     */
    void recordSubmit(Long userId, Long problemId, String status, int score);
}