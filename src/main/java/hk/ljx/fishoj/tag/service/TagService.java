package hk.ljx.fishoj.tag.service;

import com.baomidou.mybatisplus.extension.service.IService;
import hk.ljx.fishoj.tag.entity.Tag;

import java.util.List;

public interface TagService extends IService<Tag> {

    void bindProblemTags(Long problemId, List<Long> tagIds);

    List<Long> listTagIdsByProblem(Long problemId);

    List<Tag> listTagsByIds(List<Long> tagIds);

    /** 管理员更新标签 (id 在 body 中, 不使用路径参数) */
    void updateByAdmin(Tag tag);

    /** 管理员创建或更新标签 (id 为空则新增, 有值则更新) */
    void saveOrUpdateByAdmin(Tag tag);
}