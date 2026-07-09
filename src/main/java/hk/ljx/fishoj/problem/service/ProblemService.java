package hk.ljx.fishoj.problem.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import hk.ljx.fishoj.problem.dto.AdminProblemQuery;
import hk.ljx.fishoj.problem.dto.ProblemDTO;
import hk.ljx.fishoj.problem.dto.ProblemQuery;
import hk.ljx.fishoj.problem.entity.Problem;
import hk.ljx.fishoj.problem.vo.ProblemDetailVO;
import hk.ljx.fishoj.problem.vo.ProblemListVO;

public interface ProblemService extends IService<Problem> {

    IPage<Problem> pageAdmin(AdminProblemQuery query);

    IPage<ProblemListVO> pageList(ProblemQuery query);

    ProblemDetailVO getDetail(Long id);

    Problem createByAdmin(ProblemDTO dto);

    void updateByAdmin(Long id, ProblemDTO dto);

    void deleteById(Long id);
}