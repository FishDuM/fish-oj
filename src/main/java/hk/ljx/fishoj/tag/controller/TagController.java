package hk.ljx.fishoj.tag.controller;

import hk.ljx.fishoj.common.response.Result;
import hk.ljx.fishoj.tag.entity.Tag;
import hk.ljx.fishoj.tag.service.TagService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tag")
public class TagController {

    @Resource
    private TagService tagService;

    /**
     * 获取全部标签 (数据量小, 全量返回)
     * @return 标签列表
     */
    @GetMapping("/list")
    public Result<List<Tag>> list() {
        // 标签数据少, 全量返即可
        return Result.success(tagService.list());
    }
}