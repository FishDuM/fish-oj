package hk.ljx.fishoj.problem.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("problem_test_case")
public class ProblemTestCase {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long problemId;

    private String input;

    private String output;

    private Integer score;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}