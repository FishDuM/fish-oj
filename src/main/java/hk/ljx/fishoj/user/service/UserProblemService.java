package hk.ljx.fishoj.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import hk.ljx.fishoj.user.entity.UserProblem;

import java.util.List;

public interface UserProblemService extends IService<UserProblem> {

    /**
     * 提交判题完成后调用, 更新用户做题统计
     */
    void recordSubmit(Long userId, Long problemId, String status, int score);

    List<UserProblem> listMy(Long userId);

    /**
     * 没提交过的题返一个空骨架, 前端不用判 null
     */
    UserProblem getOrEmpty(Long userId, Long problemId);
}