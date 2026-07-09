package hk.ljx.fishoj.user.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hk.ljx.fishoj.common.exception.BusinessException;
import hk.ljx.fishoj.common.exception.ErrorCode;
import hk.ljx.fishoj.user.dto.LoginRequest;
import hk.ljx.fishoj.user.dto.RegisterRequest;
import hk.ljx.fishoj.user.entity.User;
import hk.ljx.fishoj.user.mapper.UserMapper;
import hk.ljx.fishoj.user.service.UserService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public void register(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(BCrypt.hashpw(request.getPassword()));
        user.setNickname(request.getNickname() != null ? request.getNickname() : request.getUsername());
        user.setEmail(request.getEmail());
        user.setRole("user");
        try {
            save(user);
        } catch (DuplicateKeyException e) {
            throw new BusinessException(ErrorCode.USER_ALREADY_EXISTS);
        }
    }

    @Override
    public String login(LoginRequest request) {
        User user = getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, request.getUsername()));
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_PASSWORD_ERROR);
        }
        if (!BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.USER_PASSWORD_ERROR);
        }
        StpUtil.login(user.getId());
        // 把角色写入 session, StpInterfaceImpl 直接读取避免每次查库
        StpUtil.getSession().set("role", user.getRole());
        return StpUtil.getTokenValue();
    }

    @Override
    public User getCurrentUser() {
        long userId = StpUtil.getLoginIdAsLong();
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        return user;
    }
}