package hk.ljx.fishoj.problem.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hk.ljx.fishoj.common.exception.BusinessException;
import hk.ljx.fishoj.common.exception.ErrorCode;
import hk.ljx.fishoj.problem.dto.AdminProblemQuery;
import hk.ljx.fishoj.problem.dto.ProblemQuery;
import hk.ljx.fishoj.problem.entity.Problem;
import hk.ljx.fishoj.problem.mapper.ProblemMapper;
import hk.ljx.fishoj.problem.service.ProblemService;
import hk.ljx.fishoj.problem.vo.ProblemDetailVO;
import hk.ljx.fishoj.problem.vo.ProblemListVO;
import hk.ljx.fishoj.problem.vo.ProblemVO;
import hk.ljx.fishoj.tag.entity.Tag;
import hk.ljx.fishoj.tag.service.TagService;
import hk.ljx.fishoj.tag.vo.TagVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProblemServiceImpl extends ServiceImpl<ProblemMapper, Problem> implements ProblemService {

    @Resource
    private TagService tagService;

    /**
     * 管理端题目分页 (看全部状态, 含草稿/隐藏)
     * @param query 分页参数
     * @return 题目实体分页结果
     */
    @Override
    public IPage<Problem> pageAdmin(AdminProblemQuery query) {
        // 管理端能看草稿/隐藏题, 不过滤 status
        return page(new Page<>(query.getPage(), query.getSize()),
                new LambdaQueryWrapper<Problem>().orderByDesc(Problem::getCreateTime));
    }

    /**
     * 前台题目列表分页 (仅查已发布, description 长文本不进列表)
     * @param query 分页参数 + 可选 tagId 标签筛选
     * @return 题目列表 VO 分页结果
     */
    @Override
    public IPage<ProblemListVO> pageList(ProblemQuery query) {
        // 只查列表需要的几个字段, description 这种长文本不进列表
        LambdaQueryWrapper<Problem> wrapper = new LambdaQueryWrapper<Problem>()
                .eq(Problem::getStatus, 1)
                .select(Problem::getId, Problem::getTitle, Problem::getDifficulty,
                        Problem::getCreateTime)
                .orderByDesc(Problem::getCreateTime);
        if (query.getTagId() != null) {
            // 通过子查询关联 problem_tag, 避免先加载所有 ID 到内存
            wrapper.inSql(Problem::getId,
                    "SELECT problem_id FROM problem_tag WHERE tag_id = " + query.getTagId());
        }
        if (query.getDifficulty() != null && !query.getDifficulty().isBlank()) {
            wrapper.eq(Problem::getDifficulty, query.getDifficulty());
        }
        Page<Problem> p = page(new Page<>(query.getPage(), query.getSize()), wrapper);
        Page<ProblemListVO> voPage = new Page<>(p.getCurrent(), p.getSize(), p.getTotal());
        List<ProblemListVO> voList = p.getRecords().stream().map(problem -> {
            ProblemListVO vo = new ProblemListVO();
            BeanUtil.copyProperties(problem, vo);
            return vo;
        }).toList();
        voPage.setRecords(voList);
        return voPage;
    }

    /**
     * 获取题目详情 (含 description + 关联标签)
     * @param id 题目 id
     * @return 题目详情 VO
     */
    @Override
    public ProblemDetailVO getDetail(Long id) {
        Problem problem = getById(id);
        if (problem == null) {
            throw new BusinessException(ErrorCode.PROBLEM_NOT_FOUND);
        }
        // 详情才出 description 和 tags, 列表不带
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
        return vo;
    }

    /**
     * 管理员创建题目 (自动写入当前管理员为创建人)
     * @param problem 题目实体
     * @param currentUserId 当前管理员 id
     * @return 创建后的题目实体
     */
    @Override
    public Problem createByAdmin(Problem problem, Long currentUserId) {
        // 创建人=当前管理员 id, 后续可能按这个字段做权限
        problem.setCreateUserId(currentUserId);
        save(problem);
        return problem;
    }

    /**
     * 管理员更新题目
     * @param id 题目 id
     * @param problem 题目实体
     */
    @Override
    public void updateByAdmin(Long id, Problem problem) {
        problem.setId(id);
        updateById(problem);
    }

    /**
     * 按 id 删除题目
     * @param id 题目 id
     */
    @Override
    public void deleteById(Long id) {
        removeById(id);
    }
}