package hk.ljx.fishoj.judge.codesendbox.service;

import hk.ljx.fishoj.judge.codesendbox.vo.ExecuteCodeResponse;
import hk.ljx.fishoj.judge.submit.dto.SubmitRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CodeSandBoxProxy implements CodeSandBox {

    private final CodeSandBox codeSandBox;

    public CodeSandBoxProxy(CodeSandBox codeSandBox) {
        this.codeSandBox = codeSandBox;
    }

    /**
     * 执行代码
     *
     * @param submitRequest
     * @return
     */
    @Override
    public ExecuteCodeResponse executeCode(SubmitRequest submitRequest) {
        log.info("=====>代码沙箱输入: {}", submitRequest.toString());
        ExecuteCodeResponse executeCodeResponse = codeSandBox.executeCode(submitRequest);
        log.info("=====>代码沙箱输出: {}", executeCodeResponse.toString());
        return executeCodeResponse;
    }
}
