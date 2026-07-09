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
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
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
    public Result<Map<String, String>> login(@Valid @RequestBody LoginRequest request) {
        // 登录成功直接返回 token, 前端存到 localStorage
        String token = userService.login(request);
        return Result.success(Map.of("token", token));
    }

    /**
     * 退出登录, 清掉当前 session + token
     */
    @PostMapping("/logout")
    @SaCheckLogin
    public Result<Void> logout() {
        // 清掉 session + token
        StpUtil.logout();
        return Result.success();
    }

    /**
     * 获取当前登录用户信息
     * @return 当前用户的 VO (不含密码字段)
     */
    @GetMapping("/me")
    @SaCheckLogin
    public Result<UserVO> me() {
        // 用 VO 而不是直接返 Entity, 防止 password 字段漏出去
        User user = userService.getCurrentUser(StpUtil.getLoginIdAsLong());
        UserVO vo = new UserVO();
        BeanUtil.copyProperties(user, vo);
        return Result.success(vo);
    }
}