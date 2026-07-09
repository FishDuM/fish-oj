package hk.ljx.fishoj.problem.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import hk.ljx.fishoj.problem.dto.AdminProblemQuery;
import hk.ljx.fishoj.problem.dto.ProblemDTO;
import hk.ljx.fishoj.problem.dto.ProblemQuery;
import hk.ljx.fishoj.problem.entity.Problem;
import hk.ljx.fishoj.problem.vo.AdminProblemVO;
import hk.ljx.fishoj.problem.vo.ProblemDetailVO;
import hk.ljx.fishoj.problem.vo.ProblemListVO;

public interface ProblemService extends IService<Problem> {

    IPage<AdminProblemVO> pageAdmin(AdminProblemQuery query);

    IPage<ProblemListVO> pageList(ProblemQuery query);

    ProblemDetailVO getDetail(Long id);

    AdminProblemVO getVoById(Long id);

    Problem createByAdmin(ProblemDTO dto);

    void updateByAdmin(ProblemDTO dto);

    /** 管理员创建或更新 (id 为空则创建, 有值则更新) */
    void saveOrUpdateByAdmin(ProblemDTO dto);

    void deleteById(Long id);
}