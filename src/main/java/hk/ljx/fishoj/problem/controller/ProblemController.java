package hk.ljx.fishoj.problem.controller;

import hk.ljx.fishoj.common.response.Result;
import hk.ljx.fishoj.problem.dto.ProblemQuery;
import hk.ljx.fishoj.problem.service.ProblemService;
import hk.ljx.fishoj.problem.vo.ProblemDetailVO;
import hk.ljx.fishoj.problem.vo.ProblemListVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;

@RestController
@RequestMapping("/problem")
public class ProblemController {

    @Resource
    private ProblemService problemService;

    /**
     * 前台题目列表 (分页, 仅看已发布题目)
     * @param query 分页参数 + 可选 tagId 标签筛选
     * @return 题目列表 VO 分页结果 (不含 description 长文本)
     */
    @GetMapping("/list")
    public Result<IPage<ProblemListVO>> list(ProblemQuery query) {
        return Result.success(problemService.pageList(query));
    }

    /**
     * 获取题目详情 (含 description + 关联标签)
     * @param id 题目 id
     * @return 题目详情 VO
     */
    @GetMapping("/{id}")
    public Result<ProblemDetailVO> get(@PathVariable Long id) {
        return Result.success(problemService.getDetail(id));
    }
}