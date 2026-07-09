package hk.ljx.fishoj.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hk.ljx.fishoj.user.entity.UserProblem;
import hk.ljx.fishoj.user.mapper.UserProblemMapper;
import hk.ljx.fishoj.user.service.UserProblemService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserProblemServiceImpl
        extends ServiceImpl<UserProblemMapper, UserProblem>
        implements UserProblemService {

    @Override
    public void recordSubmit(Long userId, Long problemId, String status, int score) {
        UserProblem record = getOne(new LambdaQueryWrapper<UserProblem>()
                .eq(UserProblem::getUserId, userId)
                .eq(UserProblem::getProblemId, problemId));

        if (record == null) {
            record = new UserProblem();
            record.setUserId(userId);
            record.setProblemId(problemId);
            record.setSubmitCount(1);
            record.setAcCount("accepted".equals(status) ? 1 : 0);
            record.setBestScore(score);
            record.setStatus(determineStatus(null, status));
            record.setLastSubmitTime(LocalDateTime.now());
        } else {
            record.setSubmitCount(record.getSubmitCount() + 1);
            if ("accepted".equals(status)) {
                record.setAcCount(record.getAcCount() + 1);
            }
            if (score > record.getBestScore()) {
                record.setBestScore(score);
            }
            record.setStatus(determineStatus(record.getStatus(), status));
            record.setLastSubmitTime(LocalDateTime.now());
        }
        saveOrUpdate(record);
    }

    private String determineStatus(String current, String newStatus) {
        if ("accepted".equals(current) || "accepted".equals(newStatus)) {
            return "ac";
        }
        return "attempted";
    }
}