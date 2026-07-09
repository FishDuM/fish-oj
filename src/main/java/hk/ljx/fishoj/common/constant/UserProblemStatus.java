package hk.ljx.fishoj.common.constant;

import lombok.Getter;

/**
 * 用户做题进度
 */
@Getter
public enum UserProblemStatus {

    NONE("none", "未做"),
    ATTEMPTED("attempted", "尝试过"),
    AC("ac", "已通过");

    ;

    final String value;

    final String label;

    UserProblemStatus(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public static UserProblemStatus getEnum(String value) {
        for (UserProblemStatus status : UserProblemStatus.values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        return null;
    }
}