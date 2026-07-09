package hk.ljx.fishoj.problem.vo;

import hk.ljx.fishoj.tag.vo.TagVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProblemDetailVO {

    private ProblemVO problem;

    private List<TagVO> tags;
}
