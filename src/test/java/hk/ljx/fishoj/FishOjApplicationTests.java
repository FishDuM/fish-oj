package hk.ljx.fishoj;

import hk.ljx.fishoj.judge.codesendbox.enums.SandBoxEnum;
import hk.ljx.fishoj.judge.codesendbox.service.CodeSandBox;
import hk.ljx.fishoj.judge.codesendbox.service.CodeSandBoxFactory;
import hk.ljx.fishoj.judge.submit.dto.SubmitRequest;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FishOjApplicationTests {

    @Test
    void contextLoads() {
    }

    @Value("${codesandbox.type}")
    private String type;

    @Test
    void codesandboxType() {
        SandBoxEnum boxType = SandBoxEnum.getEnum(this.type);
        if (boxType != null) {
            CodeSandBox codeSandBox = CodeSandBoxFactory.createCodeSandBox(boxType);
            codeSandBox.executeCode(new SubmitRequest());
        }
    }

}
