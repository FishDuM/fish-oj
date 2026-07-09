package hk.ljx.fishoj.user.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import hk.ljx.fishoj.common.response.Result;
import hk.ljx.fishoj.user.dto.AdminUserQuery;
import hk.ljx.fishoj.user.entity.User;
import hk.ljx.fishoj.user.service.UserService;
import hk.ljx.fishoj.user.vo.UserVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.metadata.IPage;

@RestController
@RequestMapping("/api/admin/user")
@SaCheckRole("admin")
public class AdminUserController {

    @Resource
    private UserService userService;

    /**
     * 管理端用户列表 (分页)
     * @param query 分页参数
     * @return 用户 VO 分页结果, 已脱敏 password 字段
     */
    @GetMapping("/list")
    public Result<IPage<UserVO>> list(AdminUserQuery query) {
        return Result.success(userService.pageAdmin(query));
    }

    /**
     * 获取指定用户详情
     * @param id 用户 id
     * @return 用户 VO, 已脱敏 password 字段
     */
    @GetMapping("/{id}")
    public Result<UserVO> get(@PathVariable Long id) {
        return Result.success(userService.getVoById(id));
    }

    /**
     * 管理员创建用户 (强制 role=user, 防止前台传 admin 越权)
     * @param user 用户实体, 含用户名/密码/昵称/邮箱
     */
    @PostMapping
    public Result<Void> create(@RequestBody User user) {
        userService.createByAdmin(user);
        return Result.success();
    }

    /**
     * 管理员更新用户 (密码非空才重置, 空字符串视为不动密码)
     * @param id 用户 id
     * @param user 用户实体
     */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody User user) {
        userService.updateByAdmin(id, user);
        return Result.success();
    }

    /**
     * 删除指定用户
     * @param id 用户 id
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        userService.deleteById(id);
        return Result.success();
    }
}