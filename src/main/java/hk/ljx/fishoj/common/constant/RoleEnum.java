package hk.ljx.fishoj.common.constant;

import lombok.Getter;

/**
 * 用户角色 (DB 存 value 字符串, 跟 Sa-Token 的 role 字符串一致)
 */
@Getter
public enum RoleEnum {

    ADMIN("admin", "管理员"),
    USER("user", "普通用户");

    ;

    final String value;

    final String label;

    RoleEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    /**
     * 按 value 反查枚举, 找不到返 null (调用方负责抛业务异常)
     */
    public static RoleEnum getEnum(String value) {
        if (value == null) {
            return null;
        }
        for (RoleEnum role : RoleEnum.values()) {
            if (role.value.equals(value)) {
                return role;
            }
        }
        return null;
    }
}