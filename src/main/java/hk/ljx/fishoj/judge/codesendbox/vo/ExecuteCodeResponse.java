package hk.ljx.fishoj.judge.codesendbox.vo;

import hk.ljx.fishoj.judge.submit.entity.JudgeCase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExecuteCodeResponse {

    private String message;

    private List<JudgeCase> judgeCase;

    private String status;
}
