package hk.ljx.fishoj.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import hk.ljx.fishoj.user.dto.AdminCreateUserRequest;
import hk.ljx.fishoj.user.dto.AdminUpdateUserRequest;
import hk.ljx.fishoj.user.dto.AdminUserQuery;
import hk.ljx.fishoj.user.dto.LoginRequest;
import hk.ljx.fishoj.user.dto.RegisterRequest;
import hk.ljx.fishoj.user.dto.UserRoleRequest;
import hk.ljx.fishoj.user.entity.User;
import hk.ljx.fishoj.user.vo.UserVO;

public interface UserService extends IService<User> {

    void register(RegisterRequest request);

    String login(LoginRequest request);

    /** 退出登录 (清掉当前 session + token, 由 service 从上下文读取登录态) */
    void logout();

    /** 获取当前登录用户信息 (从 StpUtil 读 id, 返回 VO 脱敏 password) */
    UserVO getCurrentUserVO();

    IPage<UserVO> pageAdmin(AdminUserQuery query);

    UserVO getVoById(Long id);

    User createByAdmin(AdminCreateUserRequest request);

    void updateByAdmin(AdminUpdateUserRequest request);

    void updateRole(UserRoleRequest request);

    /** 按 id 删除用户 (由 service 校验不能删除自己) */
    void deleteById(Long id);
}