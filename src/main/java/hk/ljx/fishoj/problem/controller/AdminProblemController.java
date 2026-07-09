package hk.ljx.fishoj.problem.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.core.metadata.IPage;
import hk.ljx.fishoj.common.response.Result;
import hk.ljx.fishoj.problem.dto.AdminProblemQuery;
import hk.ljx.fishoj.problem.dto.ProblemDTO;
import hk.ljx.fishoj.problem.service.ProblemService;
import hk.ljx.fishoj.problem.vo.AdminProblemVO;
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
     */
    @GetMapping("/list")
    public Result<IPage<AdminProblemVO>> list(@Valid AdminProblemQuery query) {
        return Result.success(problemService.pageAdmin(query));
    }

    /**
     * 获取指定题目详情 (管理端)
     */
    @GetMapping("/{id}")
    public Result<AdminProblemVO> get(@PathVariable Long id) {
        return Result.success(problemService.getVoById(id));
    }

    /**
     * 创建或更新题目 (dto.id 为空则创建, 有值则更新)
     */
    @PostMapping
    public Result<Void> save(@Valid @RequestBody ProblemDTO dto) {
        problemService.saveOrUpdateByAdmin(dto);
        return Result.success();
    }

    /**
     * 删除指定题目
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        problemService.deleteById(id);
        return Result.success();
    }
}
