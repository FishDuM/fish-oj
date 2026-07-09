package hk.ljx.fishoj.problem.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.core.metadata.IPage;
import hk.ljx.fishoj.common.response.Result;
import hk.ljx.fishoj.problem.dto.AdminProblemQuery;
import hk.ljx.fishoj.problem.dto.ProblemDTO;
import hk.ljx.fishoj.problem.entity.Problem;
import hk.ljx.fishoj.problem.service.ProblemService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/problem")
@SaCheckRole("admin")
public class AdminProblemController {

    @Resource
    private ProblemService problemService;

    /**
     * 管理端题目列表 (分页, 看全部状态含草稿/隐藏)
     * @param query 分页参数
     * @return 题目实体分页结果
     */
    @GetMapping("/list")
    public Result<IPage<Problem>> list(AdminProblemQuery query) {
        return Result.success(problemService.pageAdmin(query));
    }

    /**
     * 获取指定题目详情 (管理端)
     * @param id 题目 id
     * @return 题目实体
     */
    @GetMapping("/{id}")
    public Result<Problem> get(@PathVariable Long id) {
        return Result.success(problemService.getById(id));
    }

    /**
     * 创建题目 (创建人 = 当前管理员, 由 service 内部从登录上下文读取)
     * @param dto 题目入参
     */
    @PostMapping
    public Result<Void> create(@Valid @RequestBody ProblemDTO dto) {
        problemService.createByAdmin(dto);
        return Result.success();
    }

    /**
     * 更新题目 (仅动 title/description/难度/时间/内存)
     * @param id 题目 id
     * @param dto 题目入参
     */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody ProblemDTO dto) {
        problemService.updateByAdmin(id, dto);
        return Result.success();
    }

    /**
     * 删除指定题目
     * @param id 题目 id
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        problemService.deleteById(id);
        return Result.success();
    }
}