package hk.ljx.fishoj.judge.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SubmitDetailVO extends SubmitVO {

    private String code;
}
