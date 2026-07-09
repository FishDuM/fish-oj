package hk.ljx.fishoj.problem.dto;

import hk.ljx.fishoj.common.page.PageQuery;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProblemQuery extends PageQuery {

    private Long tagId;

    /**
     * 难度过滤 (取值见 DifficultyEnum), 与 problem.difficulty 字符串字段对应
     */
    @Pattern(regexp = "easy|medium|hard", message = "难度只能是 easy/medium/hard")
    private String difficulty;
}