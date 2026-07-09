package hk.ljx.fishoj.tag.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import hk.ljx.fishoj.common.response.Result;
import hk.ljx.fishoj.tag.entity.Tag;
import hk.ljx.fishoj.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tag")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping("/list")
    public Result<List<Tag>> list() {
        return Result.success(tagService.list());
    }
}