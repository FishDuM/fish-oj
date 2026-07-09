package hk.ljx.fishoj.judge.submit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmitRequest {

    @NotNull(message = "题目ID不能为空")
    private Long problemId;

    @NotBlank(message = "代码不能为空")
    @Size(max = 65535, message = "代码长度不能超过65535")
    private String submitCode;

    @NotBlank(message = "语言不能为空")
    private String language;
}
