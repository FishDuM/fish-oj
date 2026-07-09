package hk.ljx.fishoj.tag.service;

import com.baomidou.mybatisplus.extension.service.IService;
import hk.ljx.fishoj.tag.entity.Tag;

import java.util.List;

public interface TagService extends IService<Tag> {

    void bindProblemTags(Long problemId, List<Long> tagIds);

    List<Long> listTagIdsByProblem(Long problemId);

    List<Tag> listTagsByIds(List<Long> tagIds);

    /** 管理员更新标签 (id 由路径参数注入, 不接受 body 里的 id 覆盖) */
    void updateByAdmin(Long id, Tag tag);
}