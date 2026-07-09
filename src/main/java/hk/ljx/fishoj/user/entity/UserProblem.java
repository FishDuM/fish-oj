package hk.ljx.fishoj.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user_problem")
public class UserProblem {

    private Long userId;

    private Long problemId;

    private String status;

    private Integer bestScore;

    private Integer submitCount;

    private Integer acCount;

    @TableField("last_submit_time")
    private LocalDateTime lastSubmitTime;
}