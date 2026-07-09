package hk.ljx.fishoj.judge.judgeCase.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JudgeCaseVO {

    private Long id;

    private Long submitId;

    private Long testCaseId;

    private String status;

    private Integer timeUsedMs;

    private Integer memoryUsedKb;

    private Integer score;
}