package hk.ljx.fishoj.user.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import hk.ljx.fishoj.common.response.Result;
import hk.ljx.fishoj.user.entity.UserProblem;
import hk.ljx.fishoj.user.service.UserProblemService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/problem")
@SaCheckLogin
public class UserProblemController {

    @Resource
    private UserProblemService userProblemService;

    /**
     * 获取当前用户的全部做题记录
     * @return 做题记录列表, 按最近提交时间倒序
     */
    @GetMapping("/list")
    public Result<List<UserProblem>> list() {
        return Result.success(userProblemService.listMy());
    }

    /**
     * 获取当前用户在某题上的做题进度
     * @param problemId 题目 id
     * @return 做题记录, 若没提交过则返回空骨架
     */
    @GetMapping("/{problemId}")
    public Result<UserProblem> get(@PathVariable Long problemId) {
        return Result.success(userProblemService.getOrEmpty(problemId));
    }
}