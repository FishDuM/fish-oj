package hk.ljx.fishoj.judge.submit.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hk.ljx.fishoj.common.constant.SubmitStatus;
import hk.ljx.fishoj.common.exception.BusinessException;
import hk.ljx.fishoj.common.exception.ErrorCode;
import hk.ljx.fishoj.judge.submit.dto.SubmitQuery;
import hk.ljx.fishoj.judge.submit.dto.SubmitRequest;
import hk.ljx.fishoj.judge.submit.entity.JudgeCase;
import hk.ljx.fishoj.judge.submit.entity.Submit;
import hk.ljx.fishoj.judge.submit.mapper.JudgeCaseMapper;
import hk.ljx.fishoj.judge.submit.mapper.SubmitMapper;
import hk.ljx.fishoj.judge.submit.service.SubmitService;
import hk.ljx.fishoj.judge.submit.vo.SubmitDetailVO;
import hk.ljx.fishoj.judge.submit.vo.SubmitVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubmitServiceImpl extends ServiceImpl<SubmitMapper, Submit> implements SubmitService {

    @Resource
    private JudgeCaseMapper judgeCaseMapper;

    /**
     * 提交代码 (仅插入 pending 记录, 真实判题异步执行)
     * @param request 提交请求, 含题目 id/语言/代码
     * @return 新建的提交记录 id
     */
    @Override
    public Long submit(SubmitRequest request) {
        Submit submit = Submit.builder()
                .userId(StpUtil.getLoginIdAsLong())
                .problemId(request.getProblemId())
                .language(request.getLanguage())
                .code(request.getSubmitCode())
                .status(SubmitStatus.PENDING.getValue())
                .build();
        save(submit);
        // TODO: 沙箱就绪后, 在异步判题完成时调用:
        //   1. 更新 submit 状态/得分/时间/内存/errorMessage
        //   2. 写入 judge_case 明细
        //   3. 调用 userProblemService.recordSubmit(userId, problemId, status, totalScore)
        return submit.getId();
    }

    /**
     * 获取题目详情
     * @param id 题目 id
     * @param currentUserId 登录用户 id
     * @param isAdmin 是否为管理员
     * @return 提交的代码
     */
    @Override
    public SubmitDetailVO getDetailVo(Long id, Long currentUserId, boolean isAdmin) {
        Submit submit = getById(id);
        if (submit == null) {
            throw new BusinessException(ErrorCode.SUBMIT_NOT_FOUND);
        }
        // 非管理员只能看自己的提交, code 字段属于敏感数据
        if (!isAdmin && !submit.getUserId().equals(currentUserId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }
        SubmitDetailVO vo = new SubmitDetailVO();
        BeanUtil.copyProperties(submit, vo);
        return vo;
    }

    /**
     * 获取提交的判题用例明细 (越权直接抛 NO_PERMISSION)
     * @param submitId 提交 id
     * @param currentUserId 当前登录用户 id
     * @param isAdmin 是否为管理员
     * @return 判题用例列表
     */
    @Override
    public List<JudgeCase> listCases(Long submitId, Long currentUserId, boolean isAdmin) {
        // 用例明细跟详情共用同一套权限判断, 避免绕过详情直接拿明细
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

    /**
     * 当前用户的提交记录分页 (仅查自己的, 按时间倒序)
     * @param query 分页参数 + 可选 problemId 题目筛选
     * @param currentUserId 当前登录用户 id
     * @return 提交 VO 分页结果
     */
    @Override
    public IPage<SubmitVO> pageMy(SubmitQuery query, Long currentUserId) {
        // 只查自己的提交, 默认按时间倒序, 可选按题目筛选
        LambdaQueryWrapper<Submit> wrapper = new LambdaQueryWrapper<Submit>()
                .eq(Submit::getUserId, currentUserId)
                .orderByDesc(Submit::getCreateTime);
        if (query.getProblemId() != null) {
            wrapper.eq(Submit::getProblemId, query.getProblemId());
        }
        Page<Submit> p = page(new Page<>(query.getPage(), query.getSize()), wrapper);
        Page<SubmitVO> voPage = new Page<>(p.getCurrent(), p.getSize(), p.getTotal());
        List<SubmitVO> voList = p.getRecords().stream().map(submit -> {
            SubmitVO vo = new SubmitVO();
            BeanUtil.copyProperties(submit, vo);
            return vo;
        }).toList();
        voPage.setRecords(voList);
        return voPage;
    }
}