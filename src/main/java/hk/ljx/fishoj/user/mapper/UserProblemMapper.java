package hk.ljx.fishoj.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import hk.ljx.fishoj.user.entity.UserProblem;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserProblemMapper extends BaseMapper<UserProblem> {
}