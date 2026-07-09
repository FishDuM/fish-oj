package hk.ljx.fishoj.problem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hk.ljx.fishoj.problem.entity.Problem;
import hk.ljx.fishoj.problem.mapper.ProblemMapper;
import hk.ljx.fishoj.problem.service.ProblemService;
import org.springframework.stereotype.Service;

@Service
public class ProblemServiceImpl extends ServiceImpl<ProblemMapper, Problem> implements ProblemService {
}
