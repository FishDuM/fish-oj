package hk.ljx.fishoj.problem.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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