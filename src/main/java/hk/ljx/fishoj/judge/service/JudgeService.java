package hk.ljx.fishoj.judge.service;

/**
 * 判题服务接口（骨架）。
 *
 * <p>职责：拿到一条 pending 的 submit，用 {@code CodeSandBox} 执行代码，回写 submit 状态 +
 * 写入 judge_case 明细 + 调用 userProblemService.recordSubmit 累计统计。</p>
 *
 * <p>TODO: 沙箱（codesendbox）就绪后实现 {@link #judgeAsync(Long)}。</p>
 */
public interface JudgeService {

    /**
     * 异步执行一次判题。
     *
     * <p>实现要点：</p>
     * <ol>
     *   <li>读 submit, 调用 {@code CodeSandBox.execute(...)} 跑用例</li>
     *   <li>回写 submit 状态/得分/时间/内存/errorMessage</li>
     *   <li>写 {@code judge_case} 明细（JudgeCaseService 写入）</li>
     *   <li>调 {@code userProblemService.recordSubmit(userId, problemId, status, totalScore)}</li>
     * </ol>
     *
     * @param submitId 提交 id
     */
    void judgeAsync(Long submitId);
}