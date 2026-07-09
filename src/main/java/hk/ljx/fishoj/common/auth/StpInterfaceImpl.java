package hk.ljx.fishoj.common.auth;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import hk.ljx.fishoj.user.entity.User;
import hk.ljx.fishoj.user.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
public class StpInterfaceImpl implements StpInterface {

    private static final String ROLE_KEY = "role";

    @Resource
    private UserMapper userMapper;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return Collections.emptyList();
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        Long userId = Long.valueOf(loginId.toString());
        // 1. 优先从 session 读 (登录时已写入)
        Object role = StpUtil.getSessionByLoginId(userId).get(ROLE_KEY);
        if (role != null) {
            return List.of(Objects.toString(role));
        }
        // 2. fallback: 直接查 DB, 保证权限正确性
        User user = userMapper.selectById(userId);
        if (user == null) {
            return Collections.emptyList();
        }
        return List.of(user.getRole());
    }
}