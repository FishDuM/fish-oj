package hk.ljx.fishoj.judge.submit.dto;

import hk.ljx.fishoj.common.page.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SubmitQuery extends PageQuery {

    private Long problemId;
}