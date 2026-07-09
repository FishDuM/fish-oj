package hk.ljx.fishoj.problem.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProblemVO {

    private Long id;

    private String title;

    private String description;

    private String inputDesc;

    private String outputDesc;

    private String sampleInput;

    private String sampleOutput;

    private String difficulty;

    private Integer timeLimitMs;

    private Integer memoryLimitKb;

    private Integer status;

    private LocalDateTime createTime;
}
