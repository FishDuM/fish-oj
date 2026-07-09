package hk.ljx.fishoj.judge.codesendbox.service.impl;

import hk.ljx.fishoj.judge.codesendbox.service.CodeSandBox;
import hk.ljx.fishoj.judge.codesendbox.vo.ExecuteCodeResponse;
import hk.ljx.fishoj.judge.submit.dto.SubmitRequest;

/**
 * 远程代码沙箱
 */
public class RemoteCodeSandBox implements CodeSandBox {
    /**
     * 执行代码
     *
     * @param submitRequest
     * @return
     */
    @Override
    public ExecuteCodeResponse executeCode(SubmitRequest submitRequest) {
        return null;
    }
}
