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
public class ProblemListVO {

    private Long id;

    private String title;

    /** 难度 (取值见 DifficultyEnum) */
    private String difficulty;

    private LocalDateTime createTime;
}
