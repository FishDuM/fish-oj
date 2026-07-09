package hk.ljx.fishoj.judge.codesendbox.enums;

import lombok.Getter;

@Getter
public enum SandBoxEnum {

    EXAMPLE("example"),
    REMOTE("remote"),
    THIRD_PARTY("thirdParty");

    ;

    final String type;

    SandBoxEnum(String type) {
        this.type = type;
    }

    public static SandBoxEnum getEnum(String type) {
        for (SandBoxEnum sandBoxEnum : SandBoxEnum.values()) {
            if (sandBoxEnum.type.equals(type)) {
                return sandBoxEnum;
            }
        }
        return null;
    }
}
