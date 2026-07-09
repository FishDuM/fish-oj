package hk.ljx.fishoj.judge.judgeCase.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.hutool.core.bean.BeanUtil;
import hk.ljx.fishoj.common.response.Result;
import hk.ljx.fishoj.judge.judgeCase.service.JudgeCaseService;
import hk.ljx.fishoj.judge.judgeCase.vo.JudgeCaseVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 判题结果（用例明细）相关接口。
 * 路径全部在 /api/submit 命名空间下，与提交归属同一个业务域。
 */
@RestController
@RequestMapping("/submit")
public class JudgeCaseController {

    @Resource
    private JudgeCaseService judgeCaseService;

    /**
     * 获取提交的全部判题用例明细 (走 service 权限校验)
     */
    @GetMapping("/{id}/cases")
    @SaCheckLogin
    public Result<List<JudgeCaseVO>> getCases(@PathVariable Long id) {
        return Result.success(judgeCaseService.listCases(id).stream().map(jc -> {
            JudgeCaseVO vo = new JudgeCaseVO();
            BeanUtil.copyProperties(jc, vo);
            return vo;
        }).toList());
    }
}