package hk.ljx.fishoj.problem.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import hk.ljx.fishoj.common.exception.BusinessException;
import hk.ljx.fishoj.common.exception.ErrorCode;
import hk.ljx.fishoj.common.response.Result;
import hk.ljx.fishoj.problem.entity.Problem;
import hk.ljx.fishoj.problem.service.ProblemService;
import hk.ljx.fishoj.problem.vo.ProblemDetailVO;
import hk.ljx.fishoj.problem.vo.ProblemListVO;
import hk.ljx.fishoj.problem.vo.ProblemVO;
import hk.ljx.fishoj.tag.entity.Tag;
import hk.ljx.fishoj.tag.service.TagService;
import hk.ljx.fishoj.tag.vo.TagVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/problem")
@RequiredArgsConstructor
public class ProblemController {

    private final ProblemService problemService;
    private final TagService tagService;

    @GetMapping("/list")
    public Result<Page<ProblemListVO>> list(@RequestParam(required = false) Long tagId,
                                            @RequestParam(defaultValue = "1") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        LambdaQueryWrapper<Problem> wrapper = new LambdaQueryWrapper<Problem>()
                .eq(Problem::getStatus, 1)
                .select(Problem::getId, Problem::getTitle, Problem::getDifficulty,
                        Problem::getCreateTime)
                .orderByDesc(Problem::getCreateTime);
        if (tagId != null) {
            // 通过子查询关联 problem_tag, 避免先加载所有 ID 到内存
            wrapper.inSql(Problem::getId,
                    "SELECT problem_id FROM problem_tag WHERE tag_id = " + tagId);
        }
        Page<Problem> p = problemService.page(new Page<>(page, size), wrapper);
        Page<ProblemListVO> voPage = new Page<>(p.getCurrent(), p.getSize(), p.getTotal());
        List<ProblemListVO> voList = p.getRecords().stream().map(problem -> {
            ProblemListVO vo = new ProblemListVO();
            BeanUtil.copyProperties(problem, vo);
            return vo;
        }).toList();
        voPage.setRecords(voList);
        return Result.success(voPage);
    }

    @GetMapping("/{id}")
    public Result<ProblemDetailVO> get(@PathVariable Long id) {
        Problem problem = problemService.getById(id);
        if (problem == null) {
            throw new BusinessException(ErrorCode.PROBLEM_NOT_FOUND);
        }
        List<Long> tagIds = tagService.listTagIdsByProblem(id);
        List<Tag> tags = tagService.listTagsByIds(tagIds);
        ProblemVO problemVO = new ProblemVO();
        BeanUtil.copyProperties(problem, problemVO);
        List<TagVO> tagVOs = tags.stream().map(tag -> {
            TagVO tagVO = new TagVO();
            BeanUtil.copyProperties(tag, tagVO);
            return tagVO;
        }).toList();
        ProblemDetailVO vo = new ProblemDetailVO();
        vo.setProblem(problemVO);
        vo.setTags(tagVOs);
        return Result.success(vo);
    }
}