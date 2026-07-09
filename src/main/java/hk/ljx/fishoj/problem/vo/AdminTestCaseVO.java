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
public class AdminTestCaseVO {

    private Long id;

    private Long problemId;

    private String input;

    private String output;

    private Integer score;

    private LocalDateTime createTime;
}
