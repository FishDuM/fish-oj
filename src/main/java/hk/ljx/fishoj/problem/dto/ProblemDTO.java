package hk.ljx.fishoj.problem.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 管理端题目创建/更新入参 (不含 createUserId/status/id, 由后端强制)
 */
@Data
public class ProblemDTO {

    @NotBlank(message = "标题不能为空")
    @Size(max = 200, message = "标题最长200")
    private String title;

    private String description;

    private String inputDesc;

    private String outputDesc;

    private String sampleInput;

    private String sampleOutput;

    @Pattern(regexp = "easy|medium|hard", message = "难度只能是 easy/medium/hard")
    private String difficulty;

    @Min(value = 1, message = "时间限制最小1ms")
    private Integer timeLimitMs;

    @Min(value = 1, message = "内存限制最小1KB")
    private Integer memoryLimitKb;
}
