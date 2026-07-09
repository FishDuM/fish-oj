package hk.ljx.fishoj.judge.codesendbox.service;

import hk.ljx.fishoj.judge.codesendbox.enums.SandBoxEnum;
import hk.ljx.fishoj.judge.codesendbox.service.impl.ExampleCodeSandBox;
import hk.ljx.fishoj.judge.codesendbox.service.impl.RemoteCodeSandBox;
import hk.ljx.fishoj.judge.codesendbox.service.impl.ThirdPartyCodeSandBox;

/**
 * 代码沙箱工厂
 */
public class CodeSandBoxFactory {

    public static CodeSandBox createCodeSandBox(SandBoxEnum sandBoxEnum) {

        switch (sandBoxEnum) {
            case REMOTE:
                return new RemoteCodeSandBox();
            case THIRD_PARTY:
                return new ThirdPartyCodeSandBox();
            case EXAMPLE:
            default:
                return new ExampleCodeSandBox();
        }
    }
}
