package hk.ljx.fishoj.common.constant;

import lombok.Getter;

/**
 * 提交/判题状态
 */
@Getter
public enum SubmitStatus {

    PENDING("pending", "等待判题"),
    JUDGING("judging", "判题中"),
    ACCEPTED("accepted", "通过"),
    WRONG_ANSWER("wrong_answer", "答案错误"),
    TIME_LIMIT_EXCEEDED("time_limit_exceeded", "时间超限"),
    MEMORY_LIMIT_EXCEEDED("memory_limit_exceeded", "内存超限"),
    COMPILE_ERROR("compile_error", "编译错误"),
    RUNTIME_ERROR("runtime_error", "运行错误");

    ;

    final String value;

    final String label;

    SubmitStatus(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public static SubmitStatus getEnum(String value) {
        for (SubmitStatus status : SubmitStatus.values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        return null;
    }
}