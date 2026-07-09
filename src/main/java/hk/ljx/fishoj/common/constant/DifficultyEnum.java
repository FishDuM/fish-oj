package hk.ljx.fishoj.common.constant;

import lombok.Getter;

/**
 * 题目难度 (DB 存 value 字符串)
 */
@Getter
public enum DifficultyEnum {

    EASY("easy", "简单"),
    MEDIUM("medium", "中等"),
    HARD("hard", "困难");

    ;

    final String value;

    final String label;

    DifficultyEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    /**
     * 按 value 反查枚举, 找不到返 null
     */
    public static DifficultyEnum getEnum(String value) {
        if (value == null) {
            return null;
        }
        for (DifficultyEnum difficulty : DifficultyEnum.values()) {
            if (difficulty.value.equals(value)) {
                return difficulty;
            }
        }
        return null;
    }
}