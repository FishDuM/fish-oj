package hk.ljx.fishoj.tag.service;

import com.baomidou.mybatisplus.extension.service.IService;
import hk.ljx.fishoj.tag.entity.Tag;

import java.util.List;

public interface TagService extends IService<Tag> {

    void bindProblemTags(Long problemId, List<Long> tagIds);

    List<Long> listTagIdsByProblem(Long problemId);

    List<Tag> listTagsByIds(List<Long> tagIds);
}