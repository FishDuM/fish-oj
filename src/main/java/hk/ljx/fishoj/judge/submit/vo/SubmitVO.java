package hk.ljx.fishoj.judge.submit.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
