package hk.ljx.fishoj.problem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hk.ljx.fishoj.problem.entity.ProblemTestCase;
import hk.ljx.fishoj.problem.mapper.ProblemTestCaseMapper;
import hk.ljx.fishoj.problem.service.ProblemTestCaseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProblemTestCaseServiceImpl
        extends ServiceImpl<ProblemTestCaseMapper, ProblemTestCase>
        implements ProblemTestCaseService {

    @Override
    public List<ProblemTestCase> listByProblem(Long problemId) {
        return list(new LambdaQueryWrapper<ProblemTestCase>()
                .eq(ProblemTestCase::getProblemId, problemId)
                .orderByAsc(ProblemTestCase::getId));
    }
}