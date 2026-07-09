package hk.ljx.fishoj.user.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import hk.ljx.fishoj.common.response.Result;
import hk.ljx.fishoj.user.dto.LoginRequest;
import hk.ljx.fishoj.user.dto.RegisterRequest;
import hk.ljx.fishoj.user.entity.User;
import hk.ljx.fishoj.user.service.UserService;
import hk.ljx.fishoj.user.vo.UserVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody RegisterRequest request) {
        userService.register(request);
        return Result.success();
    }

    @PostMapping("/login")
    public Result<Map<String, String>> login(@Valid @RequestBody LoginRequest request) {
        String token = userService.login(request);
        return Result.success(Map.of("token", token));
    }

    @PostMapping("/logout")
    @SaCheckLogin
    public Result<Void> logout() {
        StpUtil.logout();
        return Result.success();
    }

    @GetMapping("/me")
    @SaCheckLogin
    public Result<UserVO> me() {
        User user = userService.getCurrentUser();
        UserVO vo = new UserVO();
        BeanUtil.copyProperties(user, vo);
        return Result.success(vo);
    }
}
