package hk.ljx.fishoj.user.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hk.ljx.fishoj.common.constant.RoleEnum;
import hk.ljx.fishoj.common.exception.BusinessException;
import hk.ljx.fishoj.common.exception.ErrorCode;
import hk.ljx.fishoj.user.dto.AdminCreateUserRequest;
import hk.ljx.fishoj.user.dto.AdminUpdateUserRequest;
import hk.ljx.fishoj.user.dto.AdminUserQuery;
import hk.ljx.fishoj.user.dto.LoginRequest;
import hk.ljx.fishoj.user.dto.RegisterRequest;
import hk.ljx.fishoj.user.dto.UserRoleRequest;
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
                .role(RoleEnum.USER.getValue())
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
     * 退出登录 (清掉当前 session + token, 由 StpUtil 自动定位当前登录用户)
     */
    @Override
    public void logout() {
        StpUtil.logout();
    }

    /**
     * 获取当前登录用户信息 (返回 VO 脱敏 password)
     */
    @Override
    public UserVO getCurrentUserVO() {
        User user = getById(StpUtil.getLoginIdAsLong());
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        UserVO vo = new UserVO();
        BeanUtil.copyProperties(user, vo);
        return vo;
    }

    /**
     * 获取当前登录用户实体 (仅限 service 内部调用, 对外暴露用 getCurrentUserVO)
     * @return 用户实体
     */
    public User getCurrentUser() {
        User user = getById(StpUtil.getLoginIdAsLong());
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
     * @param request 创建请求
     * @return 创建后的用户实体
     */
    @Override
    public User createByAdmin(AdminCreateUserRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                // 强制 user 角色, 防止前台传 admin 越权
                .role(RoleEnum.USER.getValue())
                .password(BCrypt.hashpw(request.getPassword()))
                .nickname(request.getNickname() != null ? request.getNickname() : request.getUsername())
                .email(request.getEmail())
                .build();
        try {
            save(user);
        } catch (DuplicateKeyException e) {
            throw new BusinessException(ErrorCode.USER_ALREADY_EXISTS);
        }
        return user;
    }

    /**
     * 管理员更新用户 (仅动 nickname/email/password, 不碰 username/role/createTime/id)
     * 密码非空才重置, 空字符串视为不动密码
     * @param request 更新请求 (含 id)
     */
    @Override
    public void updateByAdmin(AdminUpdateUserRequest request) {
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<User>()
                .eq(User::getId, request.getId());
        if (StrUtil.isNotBlank(request.getNickname())) {
            wrapper.set(User::getNickname, request.getNickname());
        }
        // email 始终更新 (允许清空为 null)
        wrapper.set(User::getEmail, request.getEmail());
        // 密码非空才重置, 空字符串视为不动密码
        if (StrUtil.isNotBlank(request.getPassword())) {
            wrapper.set(User::getPassword, BCrypt.hashpw(request.getPassword()));
        }
        update(wrapper);
    }

    /**
     * 管理员修改用户角色 (改动后强制该用户下线, 下次请求从库刷新角色)
     * @param request 角色变更请求 (含 id + role), role 仅接受 RoleEnum 中定义的值
     */
    @Override
    public void updateRole(UserRoleRequest request) {
        if (RoleEnum.getEnum(request.getRole()) == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "非法角色: " + request.getRole());
        }
        UpdateWrapper<User> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", request.getId()).set("role", request.getRole());
        update(wrapper);
        // 让该用户会话失效, 避免 session 里的旧角色一直有效
        StpUtil.logout(request.getId());
    }

    /**
     * 按 id 删除用户 (不能删除自己, 自删除校验由 service 从登录上下文读取)
     * @param id 用户 id
     */
    @Override
    public void deleteById(Long id) {
        if (id.equals(StpUtil.getLoginIdAsLong())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "不能删除自己");
        }
        removeById(id);
    }
}