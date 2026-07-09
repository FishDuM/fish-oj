package hk.ljx.fishoj.judge.codesendbox.enums;

import lombok.Getter;

@Getter
public enum SandBoxEnum {

    EXAMPLE("example"),
    REMOTE("remote"),
    THIRD_PARTY("thirdParty");

    ;

    String type;

    SandBoxEnum(String type) {
        this.type = type;
    }
}
