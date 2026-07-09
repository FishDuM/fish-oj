package hk.ljx.fishoj.problem.dto;

import hk.ljx.fishoj.common.page.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProblemQuery extends PageQuery {

    private Long tagId;

    /**
     * 难度过滤 (easy/medium/hard), 与 problem.difficulty 字符串字段对应
     */
    private String difficulty;
}