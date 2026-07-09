package hk.ljx.fishoj.tag.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("problem_tag")
public class ProblemTag {

    private Long problemId;
    private Long tagId;
}