package hk.ljx.fishoj.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import hk.ljx.fishoj.user.dto.AdminUserQuery;
import hk.ljx.fishoj.user.dto.LoginRequest;
import hk.ljx.fishoj.user.dto.RegisterRequest;
import hk.ljx.fishoj.user.entity.User;
import hk.ljx.fishoj.user.vo.UserVO;

public interface UserService extends IService<User> {

    void register(RegisterRequest request);

    String login(LoginRequest request);

    User getCurrentUser(Long currentUserId);

    IPage<UserVO> pageAdmin(AdminUserQuery query);

    UserVO getVoById(Long id);

    User createByAdmin(User user);

    void updateByAdmin(Long id, User user);

    void deleteById(Long id);
}