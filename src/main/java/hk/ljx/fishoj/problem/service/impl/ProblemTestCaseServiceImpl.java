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

    /**
     * 获取指定题目的全部测试用例 (按 id 升序, 判题时按此顺序跑)
     * @param problemId 题目 id
     * @return 测试用例列表
     */
    @Override
    public List<ProblemTestCase> listByProblem(Long problemId) {
        // 按 id 升序, 跟出题顺序一致, 判题时按这个顺序跑
        return list(new LambdaQueryWrapper<ProblemTestCase>()
                .eq(ProblemTestCase::getProblemId, problemId)
                .orderByAsc(ProblemTestCase::getId));
    }
}