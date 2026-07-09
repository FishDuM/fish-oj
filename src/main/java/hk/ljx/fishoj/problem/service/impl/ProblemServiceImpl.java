package hk.ljx.fishoj.problem.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hk.ljx.fishoj.common.exception.BusinessException;
import hk.ljx.fishoj.common.exception.ErrorCode;
import hk.ljx.fishoj.problem.dto.AdminProblemQuery;
import hk.ljx.fishoj.problem.dto.ProblemDTO;
import hk.ljx.fishoj.problem.dto.ProblemQuery;
import hk.ljx.fishoj.problem.entity.Problem;
import hk.ljx.fishoj.problem.mapper.ProblemMapper;
import hk.ljx.fishoj.problem.service.ProblemService;
import hk.ljx.fishoj.problem.vo.AdminProblemVO;
import hk.ljx.fishoj.problem.vo.ProblemDetailVO;
import hk.ljx.fishoj.problem.vo.ProblemListVO;
import hk.ljx.fishoj.problem.vo.ProblemVO;
import hk.ljx.fishoj.tag.entity.ProblemTag;
import hk.ljx.fishoj.tag.entity.Tag;
import hk.ljx.fishoj.tag.mapper.ProblemTagMapper;
import hk.ljx.fishoj.tag.service.TagService;
import hk.ljx.fishoj.tag.vo.TagVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class ProblemServiceImpl extends ServiceImpl<ProblemMapper, Problem> implements ProblemService {

    @Resource
    private TagService tagService;

    @Resource
    private ProblemTagMapper problemTagMapper;

    /**
     * 管理端题目分页 (看全部状态, 含草稿/隐藏)
     * @param query 分页参数
     * @return 题目 VO 分页结果
     */
    @Override
    public IPage<AdminProblemVO> pageAdmin(AdminProblemQuery query) {
        // 管理端能看草稿/隐藏题, 不过滤 status
        Page<Problem> p = page(new Page<>(query.getPage(), query.getSize()),
                new LambdaQueryWrapper<Problem>().orderByDesc(Problem::getCreateTime));
        Page<AdminProblemVO> voPage = new Page<>(p.getCurrent(), p.getSize(), p.getTotal());
        List<AdminProblemVO> voList = p.getRecords().stream().map(problem -> {
            AdminProblemVO vo = new AdminProblemVO();
            BeanUtil.copyProperties(problem, vo);
            return vo;
        }).toList();
        voPage.setRecords(voList);
        return voPage;
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
            // 通过 problem_tag 关联取题目 id, 走 mybatis-plus 参数化查询, 避免 SQL 注入
            List<Long> problemIds = problemTagMapper.selectList(new LambdaQueryWrapper<ProblemTag>()
                            .eq(ProblemTag::getTagId, query.getTagId()))
                    .stream().map(ProblemTag::getProblemId).toList();
            if (problemIds.isEmpty()) {
                // 没有任何题目, 直接返空 (in(emptyList) 不会报错, 但显式返空更清晰)
                Page<ProblemListVO> empty = new Page<>(query.getPage(), query.getSize(), 0);
                empty.setRecords(Collections.emptyList());
                return empty;
            }
            wrapper.in(Problem::getId, problemIds);
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
     * 获取题目详情 (含 description + 关联标签; 仅查已发布题目)
     * @param id 题目 id
     * @return 题目详情 VO
     */
    @Override
    public ProblemDetailVO getDetail(Long id) {
        Problem problem = getOne(new LambdaQueryWrapper<Problem>()
                .eq(Problem::getId, id)
                .eq(Problem::getStatus, 1));
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
     * 按 id 获取管理端题目 VO
     */
    @Override
    public AdminProblemVO getVoById(Long id) {
        Problem problem = getById(id);
        if (problem == null) {
            throw new BusinessException(ErrorCode.PROBLEM_NOT_FOUND);
        }
        AdminProblemVO vo = new AdminProblemVO();
        BeanUtil.copyProperties(problem, vo);
        return vo;
    }

    /**
     * 管理员创建题目 (强制 createUserId=当前管理员, createTime=now, 忽略客户端传入;
     * 当前管理员 id 由 service 从 StpUtil 读取)
     * @param dto 题目入参
     * @return 创建后的题目实体
     */
    @Override
    public Problem createByAdmin(ProblemDTO dto) {
        Problem problem = new Problem();
        BeanUtil.copyProperties(dto, problem);
        // 创建人=当前管理员 id, 客户端无权指定
        problem.setCreateUserId(StpUtil.getLoginIdAsLong());
        // 兜底: 即使 MyMetaObjectHandler 未生效也强制写入创建时间
        problem.setCreateTime(LocalDateTime.now());
        save(problem);
        return problem;
    }

    /**
     * 管理员更新题目 (仅动 title/description/难度/时间/内存, 不碰 createUserId/status)
     * 先查后更新以便乐观锁 @Version 生效, 并发安全
     * @param dto 题目入参 (含 id)
     */
    @Override
    public void updateByAdmin(ProblemDTO dto) {
        Problem problem = getById(dto.getId());
        if (problem == null) {
            throw new BusinessException(ErrorCode.PROBLEM_NOT_FOUND);
        }
        BeanUtil.copyProperties(dto, problem);
        updateById(problem);
    }

    /**
     * 管理员创建或更新题目 (id 为空则创建, 有值则更新)
     */
    @Override
    public void saveOrUpdateByAdmin(ProblemDTO dto) {
        if (dto.getId() == null) {
            createByAdmin(dto);
        } else {
            updateByAdmin(dto);
        }
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