package hk.ljx.fishoj.problem.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import hk.ljx.fishoj.common.exception.ErrorCode;
import hk.ljx.fishoj.common.response.Result;
import hk.ljx.fishoj.problem.entity.ProblemTestCase;
import hk.ljx.fishoj.problem.service.ProblemTestCaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/test-case")
@SaCheckRole("admin")
@RequiredArgsConstructor
public class AdminTestCaseController {

    private final ProblemTestCaseService testCaseService;

    @GetMapping("/list")
    public Result<List<ProblemTestCase>> list(@RequestParam Long problemId) {
        return Result.success(testCaseService.listByProblem(problemId));
    }

    @PostMapping
    public Result<Void> create(@RequestBody ProblemTestCase testCase) {
        testCaseService.save(testCase);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody ProblemTestCase testCase) {
        testCase.setId(id);
        testCaseService.updateById(testCase);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        testCaseService.removeById(id);
        return Result.success();
    }
}