package hk.ljx.fishoj.judge.judgeCase.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("judge_case")
public class JudgeCase {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long submitId;

    private Long testCaseId;

    /** 判题用例状态, 取值复用 SubmitStatus (PENDING/JUDGING/ACCEPTED/WRONG_ANSWER/...) */
    private String status;

    private Integer timeUsedMs;

    private Integer memoryUsedKb;

    private Integer score;
}