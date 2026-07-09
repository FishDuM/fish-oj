package hk.ljx.fishoj.judge.submit.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import hk.ljx.fishoj.judge.submit.dto.SubmitQuery;
import hk.ljx.fishoj.judge.submit.dto.SubmitRequest;
import hk.ljx.fishoj.judge.submit.entity.JudgeCase;
import hk.ljx.fishoj.judge.submit.entity.Submit;
import hk.ljx.fishoj.judge.submit.vo.SubmitDetailVO;
import hk.ljx.fishoj.judge.submit.vo.SubmitVO;

import java.util.List;

public interface SubmitService extends IService<Submit> {

    Long submit(SubmitRequest request);

    SubmitDetailVO getDetailVo(Long id, Long currentUserId, boolean isAdmin);

    /**
     * 查询用例明细, 自动校验权限
     */
    List<JudgeCase> listCases(Long submitId, Long currentUserId, boolean isAdmin);

    IPage<SubmitVO> pageMy(SubmitQuery query, Long currentUserId);
}