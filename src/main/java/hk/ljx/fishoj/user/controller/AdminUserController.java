package hk.ljx.fishoj.user.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import hk.ljx.fishoj.common.exception.ErrorCode;
import hk.ljx.fishoj.common.response.Result;
import hk.ljx.fishoj.user.entity.User;
import hk.ljx.fishoj.user.service.UserService;
import hk.ljx.fishoj.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/user")
@SaCheckRole("admin")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    @GetMapping("/list")
    public Result<Page<UserVO>> list(@RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "10") int size) {
        Page<User> p = userService.page(new Page<>(page, size),
                new LambdaQueryWrapper<User>().orderByDesc(User::getCreateTime));
        Page<UserVO> voPage = new Page<>(p.getCurrent(), p.getSize(), p.getTotal());
        List<UserVO> voList = p.getRecords().stream().map(u -> {
            UserVO vo = new UserVO();
            BeanUtil.copyProperties(u, vo);
            return vo;
        }).toList();
        voPage.setRecords(voList);
        return Result.success(voPage);
    }

    @GetMapping("/{id}")
    public Result<UserVO> get(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null) {
            return Result.error(ErrorCode.USER_NOT_FOUND);
        }
        UserVO vo = new UserVO();
        BeanUtil.copyProperties(user, vo);
        return Result.success(vo);
    }

    @PostMapping
    public Result<Void> create(@RequestBody User user) {
        user.setRole("user");
        user.setPassword(cn.dev33.satoken.secure.BCrypt.hashpw(user.getPassword()));
        userService.save(user);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(cn.dev33.satoken.secure.BCrypt.hashpw(user.getPassword()));
        } else {
            user.setPassword(null);
        }
        userService.updateById(user);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        userService.removeById(id);
        return Result.success();
    }
}
