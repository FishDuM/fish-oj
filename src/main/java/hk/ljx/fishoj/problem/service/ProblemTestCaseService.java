package hk.ljx.fishoj.problem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import hk.ljx.fishoj.problem.entity.ProblemTestCase;
import hk.ljx.fishoj.problem.vo.AdminTestCaseVO;

import java.util.List;

public interface ProblemTestCaseService extends IService<ProblemTestCase> {

    List<ProblemTestCase> listByProblem(Long problemId);

    /** 获取指定题目的全部测试用例 (返回 VO) */
    List<AdminTestCaseVO> listVoByProblem(Long problemId);

    /** 管理员更新测试用例 (id 在 body 中, 不使用路径参数) */
    void updateByAdmin(ProblemTestCase testCase);

    /** 管理员创建或更新测试用例 (id 为空则新增, 有值则更新) */
    void saveOrUpdateByAdmin(ProblemTestCase testCase);
}