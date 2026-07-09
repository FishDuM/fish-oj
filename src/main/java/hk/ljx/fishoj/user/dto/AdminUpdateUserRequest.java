package hk.ljx.fishoj.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 管理员更新用户请求 (不含 role; id 在 body 中, role 由 /role 端点单独处理)
 * password 为空表示不改密码, 故不做长度限制
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUpdateUserRequest {

    private Long id;

    /** username 不参与更新, 仅当传入时校验长度 (不能改成空白) */
    @Size(min = 3, max = 50, message = "用户名长度3-50")
    private String username;

    /** 为空表示不修改密码 */
    private String password;

    private String nickname;

    @Email(message = "邮箱格式不正确")
    private String email;
}
