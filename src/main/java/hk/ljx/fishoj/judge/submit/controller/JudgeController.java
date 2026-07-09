package hk.ljx.fishoj.judge.submit.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import hk.ljx.fishoj.common.response.Result;
import hk.ljx.fishoj.judge.submit.dto.SubmitQuery;
import hk.ljx.fishoj.judge.submit.dto.SubmitRequest;
import hk.ljx.fishoj.judge.submit.entity.JudgeCase;
import hk.ljx.fishoj.judge.submit.service.SubmitService;
import hk.ljx.fishoj.judge.submit.vo.JudgeCaseVO;
import hk.ljx.fishoj.judge.submit.vo.SubmitDetailVO;
import hk.ljx.fishoj.judge.submit.vo.SubmitVO;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;

@RestController
@RequestMapping("/api/judge")
public class JudgeController {

    @Resource
    private SubmitService submitService;

    /**
     * 提交代码 (仅插入 pending 记录, 真实判题异步执行)
     * @param request 提交请求, 含题目 id/语言/代码
     * @return 提交记录 id, 供前端轮询
     */
    @PostMapping("/submit")
    @SaCheckLogin
    public Result<Map<String, Long>> submit(@Valid @RequestBody SubmitRequest request) {
        // 只插入 pending 记录, 真实判题异步跑, 这里返回 id 供前端轮询
        Long id = submitService.submit(request);
        return Result.success(Map.of("id", id));
    }

    /**
     * 获取提交详情 (含代码, 越权直接抛 NO_PERMISSION)
     * @param id 提交 id
     * @return 提交详情 VO
     */
    @GetMapping("/submit/{id}")
    @SaCheckLogin
    public Result<SubmitDetailVO> getSubmit(@PathVariable Long id) {
        // service 内部校验权限, 越权直接抛 NO_PERMISSION
        return Result.success(submitService.getDetailVo(id,
                StpUtil.getLoginIdAsLong(), StpUtil.hasRole("admin")));
    }

    /**
     * 获取提交的全部判题用例明细 (走 service 权限校验)
     * @param id 提交 id
     * @return 判题用例 VO 列表
     */
    @GetMapping("/submit/{id}/cases")
    @SaCheckLogin
    public Result<List<JudgeCaseVO>> getCases(@PathVariable Long id) {
        // 用例明细也走 service 校验, 不在前端做权限判断
        List<JudgeCase> cases = submitService.listCases(id,
                StpUtil.getLoginIdAsLong(), StpUtil.hasRole("admin"));
        return Result.success(cases.stream().map(jc -> {
            JudgeCaseVO vo = new JudgeCaseVO();
            BeanUtil.copyProperties(jc, vo);
            return vo;
        }).toList());
    }

    /**
     * 当前用户的提交记录列表 (分页, 仅看自己的)
     * @param query 分页参数 + 可选 problemId 题目筛选
     * @return 提交 VO 分页结果
     */
    @GetMapping("/submit/list")
    @SaCheckLogin
    public Result<IPage<SubmitVO>> list(SubmitQuery query) {
        return Result.success(submitService.pageMy(query, StpUtil.getLoginIdAsLong()));
    }
}