package hk.ljx.fishoj.common.constant;

/**
 * 提交/判题状态
 */
public enum SubmitStatus {

    PENDING("pending"),
    JUDGING("judging"),
    ACCEPTED("accepted"),
    WRONG_ANSWER("wrong_answer"),
    TIME_LIMIT_EXCEEDED("time_limit_exceeded"),
    MEMORY_LIMIT_EXCEEDED("memory_limit_exceeded"),
    COMPILE_ERROR("compile_error"),
    RUNTIME_ERROR("runtime_error");

    private final String value;

    SubmitStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
