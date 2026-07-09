package hk.ljx.fishoj.judge.vo;

import lombok.Data;

@Data
public class JudgeCaseVO {

    private Long id;

    private Long submitId;

    private Long testCaseId;

    private String status;

    private Integer timeUsedMs;

    private Integer memoryUsedKb;

    private Integer score;
}
