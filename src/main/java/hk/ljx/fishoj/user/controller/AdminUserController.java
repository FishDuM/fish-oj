package hk.ljx.fishoj.user.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import hk.ljx.fishoj.common.response.Result;
import hk.ljx.fishoj.user.dto.AdminCreateUserRequest;
import hk.ljx.fishoj.user.dto.AdminUpdateUserRequest;
import hk.ljx.fishoj.user.dto.AdminUserQuery;
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
     * @param request 创建请求, 含用户名/密码/昵称/邮箱
     */
    @PostMapping
    public Result<Void> create(@Valid @RequestBody AdminCreateUserRequest request) {
        userService.createByAdmin(request);
        return Result.success();
    }

    /**
     * 管理员更新用户资料 (不含角色, 密码非空才重置)
     * @param id 用户 id
     * @param request 更新请求
     */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody AdminUpdateUserRequest request) {
        userService.updateByAdmin(id, request);
        return Result.success();
    }

    /**
     * 管理员修改用户角色 (改动后强制该用户下线)
     * @param id 用户 id
     * @param role 新角色
     */
    @PutMapping("/{id}/role")
    public Result<Void> updateRole(@PathVariable Long id, @RequestParam String role) {
        userService.updateRole(id, role);
        return Result.success();
    }

    /**
     * 删除指定用户 (不能删除自己, 由 service 内部校验)
     * @param id 用户 id
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        userService.deleteById(id);
        return Result.success();
    }
}