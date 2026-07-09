package hk.ljx.fishoj.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("user_problem")
public class UserProblem {

    private Long userId;

    private Long problemId;

    /** 见 UserProblemStatus 枚举 */
    private String status;

    private Integer bestScore;

    private Integer submitCount;

    private Integer acCount;

    @TableField("last_submit_time")
    private LocalDateTime lastSubmitTime;
}