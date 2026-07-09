package hk.ljx.fishoj.tag.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import hk.ljx.fishoj.common.exception.ErrorCode;
import hk.ljx.fishoj.common.response.Result;
import hk.ljx.fishoj.tag.entity.Tag;
import hk.ljx.fishoj.tag.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/tag")
@SaCheckRole("admin")
@RequiredArgsConstructor
public class AdminTagController {

    private final TagService tagService;

    @PostMapping
    public Result<Void> create(@Valid @RequestBody Tag tag) {
        tagService.save(tag);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Tag tag) {
        tag.setId(id);
        tagService.updateById(tag);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        tagService.removeById(id);
        return Result.success();
    }

    @PostMapping("/bind")
    public Result<Void> bind(@RequestParam Long problemId,
                             @RequestBody List<Long> tagIds) {
        tagService.bindProblemTags(problemId, tagIds);
        return Result.success();
    }
}