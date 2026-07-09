package hk.ljx.fishoj.problem.vo;

import hk.ljx.fishoj.tag.vo.TagVO;
import lombok.Data;

import java.util.List;

@Data
public class ProblemDetailVO {

    private ProblemVO problem;

    private List<TagVO> tags;
}
