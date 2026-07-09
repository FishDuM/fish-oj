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
public class AdminProblemVO {

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

    private Long createUserId;

    /** 逻辑删除: 1-正常, 0-已删除 */
    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
