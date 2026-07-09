package hk.ljx.fishoj.problem.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hk.ljx.fishoj.problem.entity.ProblemTestCase;
import hk.ljx.fishoj.problem.mapper.ProblemTestCaseMapper;
import hk.ljx.fishoj.problem.service.ProblemTestCaseService;
import hk.ljx.fishoj.problem.vo.AdminTestCaseVO;
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

    /**
     * 获取指定题目的全部测试用例 (返回 VO)
     */
    @Override
    public List<AdminTestCaseVO> listVoByProblem(Long problemId) {
        return BeanUtil.copyToList(listByProblem(problemId), AdminTestCaseVO.class);
    }

    /**
     * 管理员更新测试用例
     * @param testCase 待更新字段 (id/input/output/score)
     */
    @Override
    public void updateByAdmin(ProblemTestCase testCase) {
        updateById(testCase);
    }

    /**
     * 管理员创建或更新测试用例 (id 为空则新增, 有值则更新)
     */
    @Override
    public void saveOrUpdateByAdmin(ProblemTestCase testCase) {
        if (testCase.getId() == null) {
            save(testCase);
        } else {
            updateByAdmin(testCase);
        }
    }
}