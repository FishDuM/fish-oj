package hk.ljx.fishoj.problem.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import hk.ljx.fishoj.common.response.Result;
import hk.ljx.fishoj.problem.entity.ProblemTestCase;
import hk.ljx.fishoj.problem.service.ProblemTestCaseService;
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
     * @param problemId 题目 id
     * @return 测试用例列表
     */
    @GetMapping("/list")
    public Result<List<ProblemTestCase>> list(@RequestParam Long problemId) {
        // 测试用例只管理员能看, 按 problemId 过滤
        return Result.success(testCaseService.listByProblem(problemId));
    }

    /**
     * 新增测试用例
     * @param testCase 测试用例实体 (problemId/input/output/score)
     */
    @PostMapping
    public Result<Void> create(@RequestBody ProblemTestCase testCase) {
        testCaseService.save(testCase);
        return Result.success();
    }

    /**
     * 更新测试用例 (id 由路径参数注入, 不接受 body 里的 id 覆盖)
     * @param id 测试用例 id
     * @param testCase 待更新字段 (input/output/score)
     */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody ProblemTestCase testCase) {
        testCaseService.updateByAdmin(id, testCase);
        return Result.success();
    }

    /**
     * 删除测试用例
     * @param id 测试用例 id
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        testCaseService.removeById(id);
        return Result.success();
    }
}