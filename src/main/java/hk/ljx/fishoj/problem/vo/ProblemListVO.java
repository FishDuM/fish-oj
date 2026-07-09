package hk.ljx.fishoj.problem.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProblemListVO {

    private Long id;

    private String title;

    private String difficulty;

    private LocalDateTime createTime;
}
