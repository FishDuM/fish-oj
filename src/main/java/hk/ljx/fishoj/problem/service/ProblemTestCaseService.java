package hk.ljx.fishoj.problem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import hk.ljx.fishoj.problem.entity.ProblemTestCase;

import java.util.List;

public interface ProblemTestCaseService extends IService<ProblemTestCase> {

    List<ProblemTestCase> listByProblem(Long problemId);
}