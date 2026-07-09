package hk.ljx.fishoj.user.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import hk.ljx.fishoj.common.response.Result;
import hk.ljx.fishoj.user.entity.UserProblem;
import hk.ljx.fishoj.user.service.UserProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/problem")
@SaCheckLogin
@RequiredArgsConstructor
public class UserProblemController {

    private final UserProblemService userProblemService;

    @GetMapping("/list")
    public Result<List<UserProblem>> list() {
        long userId = StpUtil.getLoginIdAsLong();
        return Result.success(userProblemService.list(
                new LambdaQueryWrapper<UserProblem>()
                        .eq(UserProblem::getUserId, userId)
                        .orderByDesc(UserProblem::getLastSubmitTime)));
    }

    @GetMapping("/{problemId}")
    public Result<UserProblem> get(@PathVariable Long problemId) {
        long userId = StpUtil.getLoginIdAsLong();
        UserProblem up = userProblemService.getOne(
                new LambdaQueryWrapper<UserProblem>()
                        .eq(UserProblem::getUserId, userId)
                        .eq(UserProblem::getProblemId, problemId));
        if (up == null) {
            up = new UserProblem();
            up.setUserId(userId);
            up.setProblemId(problemId);
            up.setStatus("none");
            up.setBestScore(0);
            up.setSubmitCount(0);
            up.setAcCount(0);
        }
        return Result.success(up);
    }
}