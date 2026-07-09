package hk.ljx.fishoj.problem.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import hk.ljx.fishoj.common.exception.ErrorCode;
import hk.ljx.fishoj.common.response.Result;
import hk.ljx.fishoj.problem.entity.Problem;
import hk.ljx.fishoj.problem.service.ProblemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/problem")
@SaCheckRole("admin")
@RequiredArgsConstructor
public class AdminProblemController {

    private final ProblemService problemService;

    @GetMapping("/list")
    public Result<Page<Problem>> list(@RequestParam(defaultValue = "1") int page,
                                      @RequestParam(defaultValue = "10") int size) {
        Page<Problem> p = problemService.page(new Page<>(page, size),
                new LambdaQueryWrapper<Problem>().orderByDesc(Problem::getCreateTime));
        return Result.success(p);
    }

    @GetMapping("/{id}")
    public Result<Problem> get(@PathVariable Long id) {
        Problem problem = problemService.getById(id);
        if (problem == null) {
            return Result.error(ErrorCode.PROBLEM_NOT_FOUND);
        }
        return Result.success(problem);
    }

    @PostMapping
    public Result<Void> create(@Valid @RequestBody Problem problem) {
        problem.setCreateUserId(StpUtil.getLoginIdAsLong());
        problemService.save(problem);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody Problem problem) {
        problem.setId(id);
        problemService.updateById(problem);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        problemService.removeById(id);
        return Result.success();
    }
}
