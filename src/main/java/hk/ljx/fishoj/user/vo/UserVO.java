package hk.ljx.fishoj.user.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserVO {

    private Long id;

    private String username;

    private String nickname;

    private String email;

    private String role;

    private LocalDateTime createTime;
}
