package hk.ljx.fishoj.judge.submit.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import hk.ljx.fishoj.judge.submit.dto.SubmitQuery;
import hk.ljx.fishoj.judge.submit.dto.SubmitRequest;
import hk.ljx.fishoj.judge.submit.entity.Submit;
import hk.ljx.fishoj.judge.submit.vo.SubmitDetailVO;
import hk.ljx.fishoj.judge.submit.vo.SubmitVO;

/**
 * 提交服务。
 *
 * <p>职责：写入 pending 提交记录、查询提交。判题由 {@code JudgeService} 异步执行，
 * 不在本服务范围内。</p>
 */
public interface SubmitService extends IService<Submit> {

    /**
     * 提交代码：插入一条 pending 记录 (用户 id 从当前登录上下文读取)。
     *
     * <p>TODO: 沙箱就绪后, 在此处或通过事件触发 JudgeService.judgeAsync(id)。</p>
     */
    Long submit(SubmitRequest request);

    /**
     * 获取提交详情（自动校验登录用户/管理员权限，非本人/非管理员抛 NO_PERMISSION）。
     */
    SubmitDetailVO getDetailVo(Long id);

    /**
     * 当前用户的提交记录分页 (用户 id 从当前登录上下文读取)。
     */
    IPage<SubmitVO> pageMy(SubmitQuery query);
}