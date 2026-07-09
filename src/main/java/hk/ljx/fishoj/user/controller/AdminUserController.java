package hk.ljx.fishoj.user.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import hk.ljx.fishoj.common.response.Result;
import hk.ljx.fishoj.user.dto.AdminCreateUserRequest;
import hk.ljx.fishoj.user.dto.AdminUpdateUserRequest;
import hk.ljx.fishoj.user.dto.AdminUserQuery;
import hk.ljx.fishoj.user.dto.UserRoleRequest;
import hk.ljx.fishoj.user.service.UserService;
import hk.ljx.fishoj.user.vo.UserVO;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.metadata.IPage;

@RestController
@RequestMapping("/admin/user")
@SaCheckRole("admin")
public class AdminUserController {

    @Resource
    private UserService userService;

    /**
     * 管理端用户列表 (分页)
     */
    @GetMapping("/list")
    public Result<IPage<UserVO>> list(@Valid AdminUserQuery query) {
        return Result.success(userService.pageAdmin(query));
    }

    /**
     * 获取指定用户详情
     */
    @GetMapping("/{id}")
    public Result<UserVO> get(@PathVariable Long id) {
        return Result.success(userService.getVoById(id));
    }

    /**
     * 管理员创建用户 (强制 role=user, 防止前台传 admin 越权)
     */
    @PostMapping("/create")
    public Result<Void> create(@Valid @RequestBody AdminCreateUserRequest request) {
        userService.createByAdmin(request);
        return Result.success();
    }

    /**
     * 管理员更新用户资料 (不含角色, 密码非空才重置)
     */
    @PostMapping("/update")
    public Result<Void> update(@Valid @RequestBody AdminUpdateUserRequest request) {
        userService.updateByAdmin(request);
        return Result.success();
    }

    /**
     * 管理员修改用户角色
     */
    @PostMapping("/update-role")
    public Result<Void> updateRole(@Valid @RequestBody UserRoleRequest request) {
        userService.updateRole(request);
        return Result.success();
    }

    /**
     * 删除指定用户 (不能删除自己, 由 service 内部校验)
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        userService.deleteById(id);
        return Result.success();
    }
}
