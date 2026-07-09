package hk.ljx.fishoj.judge.service;

import com.baomidou.mybatisplus.extension.service.IService;
import hk.ljx.fishoj.judge.dto.SubmitRequest;
import hk.ljx.fishoj.judge.entity.JudgeCase;
import hk.ljx.fishoj.judge.entity.Submit;

import java.util.List;

public interface SubmitService extends IService<Submit> {

    Long submit(SubmitRequest request);

    /**
     * 查询提交详情, 自动校验权限 (非管理员只能查自己)
     */
    Submit getDetail(Long id, Long currentUserId, boolean isAdmin);

    /**
     * 查询用例明细, 自动校验权限
     */
    List<JudgeCase> listCases(Long submitId, Long currentUserId, boolean isAdmin);
}