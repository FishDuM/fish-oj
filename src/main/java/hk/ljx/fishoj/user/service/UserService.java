package hk.ljx.fishoj.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import hk.ljx.fishoj.user.dto.LoginRequest;
import hk.ljx.fishoj.user.dto.RegisterRequest;
import hk.ljx.fishoj.user.entity.User;

public interface UserService extends IService<User> {

    void register(RegisterRequest request);

    String login(LoginRequest request);

    User getCurrentUser();
}
