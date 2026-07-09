package hk.ljx.fishoj.common.constant;

/**
 * 用户做题进度
 */
public enum UserProblemStatus {

    NONE("none"),
    ATTEMPTED("attempted"),
    AC("ac");

    private final String value;

    UserProblemStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
