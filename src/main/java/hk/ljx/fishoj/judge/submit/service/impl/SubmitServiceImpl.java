package hk.ljx.fishoj.judge.submit.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hk.ljx.fishoj.common.constant.RoleEnum;
import hk.ljx.fishoj.common.constant.SubmitStatus;
import hk.ljx.fishoj.common.exception.BusinessException;
import hk.ljx.fishoj.common.exception.ErrorCode;
import hk.ljx.fishoj.judge.submit.dto.SubmitQuery;
import hk.ljx.fishoj.judge.submit.dto.SubmitRequest;
import hk.ljx.fishoj.judge.submit.entity.Submit;
import hk.ljx.fishoj.judge.submit.mapper.SubmitMapper;
import hk.ljx.fishoj.judge.submit.service.SubmitService;
import hk.ljx.fishoj.judge.submit.vo.SubmitDetailVO;
import hk.ljx.fishoj.judge.submit.vo.SubmitVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubmitServiceImpl extends ServiceImpl<SubmitMapper, Submit> implements SubmitService {

    /**
     * 提交代码 (仅插入 pending 记录, 真实判题由 JudgeService 异步执行)
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
        // TODO: 沙箱就绪后, 在此调用 judgeService.judgeAsync(submit.getId())
        return submit.getId();
    }

    /**
     * 获取提交详情 (越权直接抛 NO_PERMISSION, 不区分 404 vs 403; 权限上下文从 StpUtil 读)
     */
    @Override
    public SubmitDetailVO getDetailVo(Long id) {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        boolean isAdmin = StpUtil.hasRole(RoleEnum.ADMIN.getValue());
        Submit submit = getById(id);
        // 先做权限判断: 无论记录是否存在, 越权一律抛 NO_PERMISSION
        if (submit != null && !isAdmin && !submit.getUserId().equals(currentUserId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }
        if (submit == null) {
            throw new BusinessException(ErrorCode.SUBMIT_NOT_FOUND);
        }
        SubmitDetailVO vo = new SubmitDetailVO();
        BeanUtil.copyProperties(submit, vo);
        return vo;
    }

    /**
     * 当前用户的提交记录分页 (仅查自己的, 按时间倒序; 用户 id 从 StpUtil 读)
     */
    @Override
    public IPage<SubmitVO> pageMy(SubmitQuery query) {
        Long currentUserId = StpUtil.getLoginIdAsLong();
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