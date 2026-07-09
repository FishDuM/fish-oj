package hk.ljx.fishoj.problem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import hk.ljx.fishoj.problem.entity.ProblemTestCase;

import java.util.List;

public interface ProblemTestCaseService extends IService<ProblemTestCase> {

    List<ProblemTestCase> listByProblem(Long problemId);

    /** 管理员更新测试用例 (id 由路径参数注入, 不接受 body 里的 id 覆盖) */
    void updateByAdmin(Long id, ProblemTestCase testCase);
}