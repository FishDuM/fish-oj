package hk.ljx.fishoj.tag.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hk.ljx.fishoj.tag.entity.ProblemTag;
import hk.ljx.fishoj.tag.entity.Tag;
import hk.ljx.fishoj.tag.mapper.ProblemTagMapper;
import hk.ljx.fishoj.tag.mapper.TagMapper;
import hk.ljx.fishoj.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    private final ProblemTagMapper problemTagMapper;

    @Override
    @Transactional
    public void bindProblemTags(Long problemId, List<Long> tagIds) {
        problemTagMapper.delete(new LambdaQueryWrapper<ProblemTag>()
                .eq(ProblemTag::getProblemId, problemId));
        if (tagIds == null || tagIds.isEmpty()) {
            return;
        }
        for (Long tagId : tagIds) {
            ProblemTag pt = new ProblemTag();
            pt.setProblemId(problemId);
            pt.setTagId(tagId);
            problemTagMapper.insert(pt);
        }
    }

    @Override
    public List<Long> listTagIdsByProblem(Long problemId) {
        return problemTagMapper.selectList(new LambdaQueryWrapper<ProblemTag>()
                        .eq(ProblemTag::getProblemId, problemId))
                .stream().map(ProblemTag::getTagId).toList();
    }

    @Override
    public List<Tag> listTagsByIds(List<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return List.of();
        }
        return listByIds(tagIds);
    }
}