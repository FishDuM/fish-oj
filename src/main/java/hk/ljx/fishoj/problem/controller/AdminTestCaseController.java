package hk.ljx.fishoj.problem.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import hk.ljx.fishoj.common.response.Result;
import hk.ljx.fishoj.problem.entity.ProblemTestCase;
import hk.ljx.fishoj.problem.service.ProblemTestCaseService;
import hk.ljx.fishoj.problem.vo.AdminTestCaseVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/test-case")
@SaCheckRole("admin")
public class AdminTestCaseController {

    @Resource
    private ProblemTestCaseService testCaseService;

    /**
     * 获取指定题目的全部测试用例
     */
    @GetMapping("/list")
    public Result<List<AdminTestCaseVO>> list(@RequestParam Long problemId) {
        return Result.success(testCaseService.listVoByProblem(problemId));
    }

    /**
     * 创建或更新测试用例 (testCase.id 为空则新增, 有值则更新)
     */
    @PostMapping
    public Result<Void> save(@RequestBody ProblemTestCase testCase) {
        testCaseService.saveOrUpdateByAdmin(testCase);
        return Result.success();
    }

    /**
     * 删除测试用例
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        testCaseService.removeById(id);
        return Result.success();
    }
}
