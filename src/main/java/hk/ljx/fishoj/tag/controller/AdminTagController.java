package hk.ljx.fishoj.tag.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import hk.ljx.fishoj.common.exception.ErrorCode;
import hk.ljx.fishoj.common.response.Result;
import hk.ljx.fishoj.tag.entity.Tag;
import hk.ljx.fishoj.tag.service.TagService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/tag")
@SaCheckRole("admin")
public class AdminTagController {

    @Resource
    private TagService tagService;

    /**
     * 创建标签
     * @param tag 标签实体
     */
    @PostMapping
    public Result<Void> create(@Valid @RequestBody Tag tag) {
        tagService.save(tag);
        return Result.success();
    }

    /**
     * 更新标签
     * @param id 标签 id
     * @param tag 标签实体
     */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Tag tag) {
        tag.setId(id);
        tagService.updateById(tag);
        return Result.success();
    }

    /**
     * 删除标签
     * @param id 标签 id
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        tagService.removeById(id);
        return Result.success();
    }

    /**
     * 给题目绑定标签 (整组替换, 不做差量)
     * @param problemId 题目 id
     * @param tagIds 标签 id 列表, 空列表表示清空
     */
    @PostMapping("/bind")
    public Result<Void> bind(@RequestParam Long problemId,
                             @RequestBody List<Long> tagIds) {
        // 整组替换, 前端传啥就存啥, 不做差量
        tagService.bindProblemTags(problemId, tagIds);
        return Result.success();
    }
}