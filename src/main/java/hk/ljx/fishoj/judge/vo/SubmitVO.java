package hk.ljx.fishoj.judge.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SubmitVO {

    private Long id;

    private Long userId;

    private Long problemId;

    private String language;

    private String status;

    private Integer totalScore;

    private Integer timeUsedMs;

    private Integer memoryUsedKb;

    private String errorMessage;

    private LocalDateTime createTime;
}
