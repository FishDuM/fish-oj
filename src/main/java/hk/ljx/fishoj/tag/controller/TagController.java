package hk.ljx.fishoj.tag.controller;

import hk.ljx.fishoj.common.response.Result;
import hk.ljx.fishoj.tag.service.TagService;
import hk.ljx.fishoj.tag.vo.TagVO;
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
    public Result<List<TagVO>> list() {
        // 标签数据少, 全量返即可
        return Result.success(tagService.list().stream().map(tag -> {
            TagVO vo = new TagVO();
            vo.setId(tag.getId());
            vo.setName(tag.getName());
            return vo;
        }).toList());
    }
}