package hk.ljx.fishoj.judge.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import hk.ljx.fishoj.common.response.Result;
import hk.ljx.fishoj.judge.dto.SubmitRequest;
import hk.ljx.fishoj.judge.entity.Submit;
import hk.ljx.fishoj.judge.service.SubmitService;
import hk.ljx.fishoj.judge.vo.JudgeCaseVO;
import hk.ljx.fishoj.judge.vo.SubmitDetailVO;
import hk.ljx.fishoj.judge.vo.SubmitVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/judge")
@RequiredArgsConstructor
public class JudgeController {

    private final SubmitService submitService;

    @PostMapping("/submit")
    @SaCheckLogin
    public Result<Map<String, Long>> submit(@Valid @RequestBody SubmitRequest request) {
        Long id = submitService.submit(request);
        return Result.success(Map.of("id", id));
    }

    @GetMapping("/submit/{id}")
    @SaCheckLogin
    public Result<SubmitDetailVO> getSubmit(@PathVariable Long id) {
        long userId = StpUtil.getLoginIdAsLong();
        boolean isAdmin = StpUtil.hasRole("admin");
        Submit submit = submitService.getDetail(id, userId, isAdmin);
        SubmitDetailVO vo = new SubmitDetailVO();
        BeanUtil.copyProperties(submit, vo);
        return Result.success(vo);
    }

    @GetMapping("/submit/{id}/cases")
    @SaCheckLogin
    public Result<List<JudgeCaseVO>> getCases(@PathVariable Long id) {
        long userId = StpUtil.getLoginIdAsLong();
        boolean isAdmin = StpUtil.hasRole("admin");
        return Result.success(submitService.listCases(id, userId, isAdmin).stream().map(jc -> {
            JudgeCaseVO vo = new JudgeCaseVO();
            BeanUtil.copyProperties(jc, vo);
            return vo;
        }).toList());
    }

    @GetMapping("/submit/list")
    @SaCheckLogin
    public Result<Page<SubmitVO>> list(@RequestParam(required = false) Long problemId,
                                       @RequestParam(defaultValue = "1") int page,
                                       @RequestParam(defaultValue = "10") int size) {
        long userId = StpUtil.getLoginIdAsLong();
        LambdaQueryWrapper<Submit> wrapper = new LambdaQueryWrapper<Submit>()
                .eq(Submit::getUserId, userId)
                .orderByDesc(Submit::getCreateTime);
        if (problemId != null) {
            wrapper.eq(Submit::getProblemId, problemId);
        }
        Page<Submit> p = submitService.page(new Page<>(page, size), wrapper);
        Page<SubmitVO> voPage = new Page<>(p.getCurrent(), p.getSize(), p.getTotal());
        List<SubmitVO> voList = p.getRecords().stream().map(submit -> {
            SubmitVO vo = new SubmitVO();
            BeanUtil.copyProperties(submit, vo);
            return vo;
        }).toList();
        voPage.setRecords(voList);
        return Result.success(voPage);
    }
}