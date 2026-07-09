package hk.ljx.fishoj.judge.submit.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import hk.ljx.fishoj.common.response.Result;
import hk.ljx.fishoj.judge.submit.dto.SubmitQuery;
import hk.ljx.fishoj.judge.submit.dto.SubmitRequest;
import hk.ljx.fishoj.judge.submit.service.SubmitService;
import hk.ljx.fishoj.judge.submit.vo.SubmitDetailVO;
import hk.ljx.fishoj.judge.submit.vo.SubmitVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 提交相关接口。路径前缀 /api/submit。
 *
 * <p>用例明细（judge_case）由 {@code JudgeCaseController} 负责，路径仍为
 * {@code /api/submit/{id}/cases}。</p>
 */
@RestController
@RequestMapping("/submit")
public class SubmitController {

    @Resource
    private SubmitService submitService;

    /**
     * 提交代码 (仅插入 pending 记录, 真实判题异步执行)
     * @return 提交记录 id, 供前端轮询
     */
    @PostMapping
    @SaCheckLogin
    public Result<Long> submit(@Valid @RequestBody SubmitRequest request) {
        return Result.success(submitService.submit(request));
    }

    /**
     * 获取提交详情 (含代码, 越权直接抛 NO_PERMISSION; 登录用户/角色由 service 从上下文读取)
     */
    @GetMapping("/{id}")
    @SaCheckLogin
    public Result<SubmitDetailVO> getSubmit(@PathVariable Long id) {
        return Result.success(submitService.getDetailVo(id));
    }

    /**
     * 当前用户的提交记录列表 (分页, 仅看自己的; 用户 id 由 service 从上下文读取)
     */
    @GetMapping("/list")
    @SaCheckLogin
    public Result<IPage<SubmitVO>> list(@Valid SubmitQuery query) {
        return Result.success(submitService.pageMy(query));
    }
}