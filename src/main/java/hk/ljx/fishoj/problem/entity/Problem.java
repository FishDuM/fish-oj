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
@TableName("problem")
public class Problem {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    private String description;

    private String inputDesc;

    private String outputDesc;

    private String sampleInput;

    private String sampleOutput;

    /** 难度 (取值见 DifficultyEnum, DB 存 value 字符串) */
    private String difficulty;

    private Integer timeLimitMs;

    private Integer memoryLimitKb;

    private Long createUserId;

    /** 逻辑删除: 1-正常, 0-已删除 */
    @TableLogic(value = "1", delval = "0")
    private Integer status;

    /** 乐观锁版本号, DB 需有 version INT DEFAULT 0 列 */
    @Version
    private Integer version;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
