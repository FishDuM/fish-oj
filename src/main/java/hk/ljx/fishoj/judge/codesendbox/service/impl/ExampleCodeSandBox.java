package hk.ljx.fishoj.judge.codesendbox.service.impl;

import hk.ljx.fishoj.judge.codesendbox.service.CodeSandBox;
import hk.ljx.fishoj.judge.codesendbox.vo.ExecuteCodeResponse;
import hk.ljx.fishoj.judge.submit.dto.SubmitRequest;
import hk.ljx.fishoj.judge.judgeCase.entity.JudgeCase;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static hk.ljx.fishoj.common.constant.SubmitStatus.ACCEPTED;
import static hk.ljx.fishoj.common.constant.UserProblemStatus.AC;

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
        return ExecuteCodeResponse.builder()
                .message("测试成功").judgeCase(List.of(new JudgeCase())).status(ACCEPTED.getValue()).build();
    }
}
