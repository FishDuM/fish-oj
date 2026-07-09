package hk.ljx.fishoj.user.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hk.ljx.fishoj.common.exception.BusinessException;
import hk.ljx.fishoj.common.exception.ErrorCode;
import hk.ljx.fishoj.user.dto.AdminUserQuery;
import hk.ljx.fishoj.user.dto.LoginRequest;
import hk.ljx.fishoj.user.dto.RegisterRequest;
import hk.ljx.fishoj.user.entity.User;
import hk.ljx.fishoj.user.mapper.UserMapper;
import hk.ljx.fishoj.user.service.UserService;
import hk.ljx.fishoj.user.vo.UserVO;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * 用户注册 (BCrypt 加密密码, 唯一索引兜底防重名)
     * @param request 注册请求
     */
    @Override
    public void register(RegisterRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .password(BCrypt.hashpw(request.getPassword()))
                .nickname(request.getNickname() != null ? request.getNickname() : request.getUsername())
                .email(request.getEmail())
                .role("user")
                .build();
        try {
            save(user);
        } catch (DuplicateKeyException e) {
            // 唯一索引兜底, 业务层重复提示
            throw new BusinessException(ErrorCode.USER_ALREADY_EXISTS);
        }
    }

    /**
     * 用户登录 (用户名不存在/密码错误统一抛同一个错, 避免暴露用户名是否存在)
     * @param request 登录请求
     * @return 登录成功后的 token
     */
    @Override
    public String login(LoginRequest request) {
        User user = getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, request.getUsername()));
        // 用户名或密码错都抛同一个错, 避免暴露用户名是否存在
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_PASSWORD_ERROR);
        }
        if (!BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.USER_PASSWORD_ERROR);
        }
        StpUtil.login(user.getId());
        // 把角色写入 session, StpInterfaceImpl 直接读 session 不用每次查库
        StpUtil.getSession().set("role", user.getRole());
        return StpUtil.getTokenValue();
    }

    /**
     * 按 id 获取当前用户实体
     * @param currentUserId 当前登录用户 id
     * @return 用户实体
     */
    @Override
    public User getCurrentUser(Long currentUserId) {
        User user = getById(currentUserId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        return user;
    }

    /**
     * 管理端用户分页 (VO 已脱敏 password 字段)
     * @param query 分页参数
     * @return 用户 VO 分页结果
     */
    @Override
    public IPage<UserVO> pageAdmin(AdminUserQuery query) {
        // 管理后台的列表, VO 把 password 剥掉
        Page<User> p = page(new Page<>(query.getPage(), query.getSize()),
                new LambdaQueryWrapper<User>().orderByDesc(User::getCreateTime));
        Page<UserVO> voPage = new Page<>(p.getCurrent(), p.getSize(), p.getTotal());
        List<UserVO> voList = p.getRecords().stream().map(u -> {
            UserVO vo = new UserVO();
            BeanUtil.copyProperties(u, vo);
            return vo;
        }).toList();
        voPage.setRecords(voList);
        return voPage;
    }

    /**
     * 按 id 获取用户 VO (已脱敏 password 字段)
     * @param id 用户 id
     * @return 用户 VO
     */
    @Override
    public UserVO getVoById(Long id) {
        User user = getById(id);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        UserVO vo = new UserVO();
        BeanUtil.copyProperties(user, vo);
        return vo;
    }

    /**
     * 管理员创建用户 (强制 role=user, BCrypt 加密密码)
     * @param user 用户实体
     * @return 创建后的用户实体
     */
    @Override
    public User createByAdmin(User user) {
        // 强制 user 角色, 防止前台传 role="admin" 越权
        user.setRole("user");
        user.setPassword(BCrypt.hashpw(user.getPassword()));
        save(user);
        return user;
    }

    /**
     * 管理员更新用户 (密码非空才重置, 空字符串视为不动密码)
     * @param id 用户 id
     * @param user 用户实体
     */
    @Override
    public void updateByAdmin(Long id, User user) {
        user.setId(id);
        // 密码非空才重置, 空字符串视为不动密码
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(BCrypt.hashpw(user.getPassword()));
        } else {
            user.setPassword(null);
        }
        updateById(user);
    }

    /**
     * 按 id 删除用户
     * @param id 用户 id
     */
    @Override
    public void deleteById(Long id) {
        removeById(id);
    }
}