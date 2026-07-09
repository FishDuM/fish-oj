package hk.ljx.fishoj.judge.codesendbox.service.impl;

import hk.ljx.fishoj.judge.codesendbox.service.CodeSandBox;
import hk.ljx.fishoj.judge.codesendbox.vo.ExecuteCodeResponse;
import hk.ljx.fishoj.judge.submit.dto.SubmitRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * 测试代码沙箱
 */
@Slf4j
public class ExampleCodeSandBox implements CodeSandBox {
    /**
     * 执行代码
     *
     * @param submitRequest
     * @return
     */
    @Override
    public ExecuteCodeResponse executeCode(SubmitRequest submitRequest) {
        log.info("ExampleCodeSandBox");
        return null;
    }
}
