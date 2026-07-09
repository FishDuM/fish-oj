package hk.ljx.fishoj.judge.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("judge_case")
public class JudgeCase {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long submitId;

    private Long testCaseId;

    private String status;

    private Integer timeUsedMs;

    private Integer memoryUsedKb;

    private Integer score;
}