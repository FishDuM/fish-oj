package hk.ljx.fishoj.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import hk.ljx.fishoj.user.entity.UserProblem;

import java.util.List;

public interface UserProblemService extends IService<UserProblem> {

    /**
     * 提交判题完成后调用, 更新用户做题统计
     */
    void recordSubmit(Long userId, Long problemId, String status, int score);

    /**
     * 获取当前登录用户的全部做题记录
     */
    List<UserProblem> listMy();

    /**
     * 获取当前登录用户在某题上的做题进度 (没提交过则返回空骨架)
     */
    UserProblem getOrEmpty(Long problemId);
}