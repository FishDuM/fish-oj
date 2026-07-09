package hk.ljx.fishoj.problem.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProblemVO {

    private Long id;

    private String title;

    private String description;

    private String inputDesc;

    private String outputDesc;

    private String sampleInput;

    private String sampleOutput;

    /** 难度 (取值见 DifficultyEnum) */
    private String difficulty;

    private Integer timeLimitMs;

    private Integer memoryLimitKb;

    private Integer status;

    private LocalDateTime createTime;
}
