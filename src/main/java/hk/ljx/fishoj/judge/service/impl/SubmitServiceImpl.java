package hk.ljx.fishoj.judge.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hk.ljx.fishoj.common.exception.BusinessException;
import hk.ljx.fishoj.common.exception.ErrorCode;
import hk.ljx.fishoj.judge.dto.SubmitRequest;
import hk.ljx.fishoj.judge.entity.JudgeCase;
import hk.ljx.fishoj.judge.entity.Submit;
import hk.ljx.fishoj.judge.mapper.JudgeCaseMapper;
import hk.ljx.fishoj.judge.mapper.SubmitMapper;
import hk.ljx.fishoj.judge.service.SubmitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubmitServiceImpl extends ServiceImpl<SubmitMapper, Submit> implements SubmitService {

    private final JudgeCaseMapper judgeCaseMapper;

    @Override
    public Long submit(SubmitRequest request) {
        Submit submit = new Submit();
        submit.setUserId(StpUtil.getLoginIdAsLong());
        submit.setProblemId(request.getProblemId());
        submit.setLanguage(request.getLanguage());
        submit.setCode(request.getSubmitCode());
        submit.setStatus("pending");
        save(submit);
        // TODO: 沙箱就绪后, 在异步判题完成时调用:
        //   1. 更新 submit 状态/得分/时间/内存/errorMessage
        //   2. 写入 judge_case 明细
        //   3. 调用 userProblemService.recordSubmit(userId, problemId, status, totalScore)
        return submit.getId();
    }

    @Override
    public Submit getDetail(Long id, Long currentUserId, boolean isAdmin) {
        Submit submit = getById(id);
        if (submit == null) {
            throw new BusinessException(ErrorCode.SUBMIT_NOT_FOUND);
        }
        if (!isAdmin && !submit.getUserId().equals(currentUserId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }
        return submit;
    }

    @Override
    public List<JudgeCase> listCases(Long submitId, Long currentUserId, boolean isAdmin) {
        Submit submit = getById(submitId);
        if (submit == null) {
            throw new BusinessException(ErrorCode.SUBMIT_NOT_FOUND);
        }
        if (!isAdmin && !submit.getUserId().equals(currentUserId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }
        return judgeCaseMapper.selectList(new LambdaQueryWrapper<JudgeCase>()
                .eq(JudgeCase::getSubmitId, submitId)
                .orderByAsc(JudgeCase::getId));
    }
}