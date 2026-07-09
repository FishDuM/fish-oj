package hk.ljx.fishoj.tag.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hk.ljx.fishoj.common.exception.BusinessException;
import hk.ljx.fishoj.common.exception.ErrorCode;
import hk.ljx.fishoj.tag.entity.ProblemTag;
import hk.ljx.fishoj.tag.entity.Tag;
import hk.ljx.fishoj.tag.mapper.ProblemTagMapper;
import hk.ljx.fishoj.tag.mapper.TagMapper;
import hk.ljx.fishoj.tag.service.TagService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Resource
    private ProblemTagMapper problemTagMapper;

    /**
     * 给题目绑定标签 (整组覆盖, 先清后插, 标签 id 需真实存在)
     * @param problemId 题目 id
     * @param tagIds 标签 id 列表, 空/null 表示清空
     */
    @Override
    @Transactional
    public void bindProblemTags(Long problemId, List<Long> tagIds) {
        // 先清后插, 整组覆盖, 避免旧关联残留
        problemTagMapper.delete(new LambdaQueryWrapper<ProblemTag>()
                .eq(ProblemTag::getProblemId, problemId));
        if (tagIds == null || tagIds.isEmpty()) {
            return;
        }
        // 校验 tagIds 都真实存在, 避免脏外键
        Set<Long> existing = listByIds(tagIds).stream().map(Tag::getId).collect(Collectors.toSet());
        Set<Long> inputSet = new HashSet<>(tagIds);
        if (!existing.containsAll(inputSet)) {
            throw new BusinessException(ErrorCode.TAG_NOT_FOUND);
        }
        for (Long tagId : tagIds) {
            ProblemTag pt = ProblemTag.builder()
                    .problemId(problemId)
                    .tagId(tagId)
                    .build();
            problemTagMapper.insert(pt);
        }
    }

    /**
     * 获取指定题目关联的全部标签 id
     * @param problemId 题目 id
     * @return 标签 id 列表
     */
    @Override
    public List<Long> listTagIdsByProblem(Long problemId) {
        // 只取 id 减少一次回表, 前端不直接用这个, 是给详情拼标签用的中间步骤
        return problemTagMapper.selectList(new LambdaQueryWrapper<ProblemTag>()
                        .eq(ProblemTag::getProblemId, problemId))
                .stream().map(ProblemTag::getTagId).toList();
    }

    /**
     * 按 id 列表批量查询标签 (id 列表为空时返回空列表)
     * @param tagIds 标签 id 列表
     * @return 标签实体列表
     */
    @Override
    public List<Tag> listTagsByIds(List<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return List.of();
        }
        return listByIds(tagIds);
    }

    /**
     * 管理员更新标签 (id 由路径参数强制注入, 防止 body 里的 id 字段越权改其他记录)
     * @param id 标签 id (来自路径参数)
     * @param tag 待更新字段 (只动 name)
     */
    @Override
    public void updateByAdmin(Long id, Tag tag) {
        tag.setId(id);
        updateById(tag);
    }
}