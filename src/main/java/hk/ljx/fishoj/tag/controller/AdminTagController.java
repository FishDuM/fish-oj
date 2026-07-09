package hk.ljx.fishoj.tag.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import hk.ljx.fishoj.common.response.Result;
import hk.ljx.fishoj.tag.entity.Tag;
import hk.ljx.fishoj.tag.service.TagService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/tag")
@SaCheckRole("admin")
public class AdminTagController {

    @Resource
    private TagService tagService;

    /**
     * 创建或更新标签 (tag.id 为空则新增, 有值则更新)
     */
    @PostMapping
    public Result<Void> save(@Valid @RequestBody Tag tag) {
        tagService.saveOrUpdateByAdmin(tag);
        return Result.success();
    }

    /**
     * 删除标签
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        tagService.removeById(id);
        return Result.success();
    }

    /**
     * 给题目绑定标签 (整组替换, 不做差量)
     */
    @PostMapping("/bind")
    public Result<Void> bind(@RequestParam Long problemId,
                             @RequestBody List<Long> tagIds) {
        tagService.bindProblemTags(problemId, tagIds);
        return Result.success();
    }
}
