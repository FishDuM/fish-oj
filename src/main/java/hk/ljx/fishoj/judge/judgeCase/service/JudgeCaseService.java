package hk.ljx.fishoj.judge.judgeCase.service;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import hk.ljx.fishoj.common.constant.RoleEnum;
import hk.ljx.fishoj.common.exception.BusinessException;
import hk.ljx.fishoj.common.exception.ErrorCode;
import hk.ljx.fishoj.judge.judgeCase.entity.JudgeCase;
import hk.ljx.fishoj.judge.judgeCase.mapper.JudgeCaseMapper;
import hk.ljx.fishoj.judge.submit.entity.Submit;
import hk.ljx.fishoj.judge.submit.service.SubmitService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 判题结果（用例明细）相关服务。
 *
 * 判题结果由 JudgeService 在沙箱执行完成后写入 judge_case；
 * 这里只负责按权限读取。
 */
@Service
public class JudgeCaseService {

    @Resource
    private JudgeCaseMapper judgeCaseMapper;

    @Resource
    private SubmitService submitService;

    /**
     * 获取提交的判题用例明细 (越权直接抛 NO_PERMISSION, 不区分 404 vs 403)
     * @param submitId 提交 id
     * @return 判题用例列表
     */
    public List<JudgeCase> listCases(Long submitId) {
        // 复用 submit 的存在性 + 权限校验, 保证不能绕过详情直接拿明细
        Submit submit = submitService.getById(submitId);
        Long currentUserId = StpUtil.getLoginIdAsLong();
        boolean isAdmin = StpUtil.hasRole(RoleEnum.ADMIN.getValue());
        // 先做权限判断: 无论记录是否存在, 越权一律抛 NO_PERMISSION
        if (submit != null && !isAdmin && !submit.getUserId().equals(currentUserId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }
        if (submit == null) {
            throw new BusinessException(ErrorCode.SUBMIT_NOT_FOUND);
        }
        return judgeCaseMapper.selectList(new LambdaQueryWrapper<JudgeCase>()
                .eq(JudgeCase::getSubmitId, submitId)
                .orderByAsc(JudgeCase::getId));
    }
}