package hk.ljx.fishoj.judge.codesendbox.service;

import hk.ljx.fishoj.judge.codesendbox.vo.ExecuteCodeResponse;
import hk.ljx.fishoj.judge.submit.dto.SubmitRequest;

/**
 * 代码沙箱
 */
public interface CodeSandBox {

    /**
     * 执行代码
     * @param submitRequest
     * @return
     */
    ExecuteCodeResponse executeCode(SubmitRequest submitRequest);
}
