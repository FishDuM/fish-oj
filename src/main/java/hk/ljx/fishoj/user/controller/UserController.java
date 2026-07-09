package hk.ljx.fishoj.user.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import hk.ljx.fishoj.common.response.Result;
import hk.ljx.fishoj.user.dto.LoginRequest;
import hk.ljx.fishoj.user.dto.RegisterRequest;
import hk.ljx.fishoj.user.service.UserService;
import hk.ljx.fishoj.user.vo.UserVO;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     * @param request 注册请求, 含用户名/密码/昵称/邮箱
     */
    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody RegisterRequest request) {
        userService.register(request);
        return Result.success();
    }

    /**
     * 用户登录
     * @param request 登录请求, 含用户名/密码
     * @return 登录成功后的 token
     */
    @PostMapping("/login")
    public Result<String> login(@Valid @RequestBody LoginRequest request) {
        return Result.success(userService.login(request));
    }

    /**
     * 退出登录 (清掉 session + token, 由 service 内部处理)
     */
    @PostMapping("/logout")
    @SaCheckLogin
    public Result<Void> logout() {
        userService.logout();
        return Result.success();
    }

    /**
     * 获取当前登录用户信息 (用户 id 由 service 从上下文读取)
     * @return 当前用户的 VO (不含密码字段)
     */
    @GetMapping("/me")
    @SaCheckLogin
    public Result<UserVO> me() {
        return Result.success(userService.getCurrentUserVO());
    }
}
