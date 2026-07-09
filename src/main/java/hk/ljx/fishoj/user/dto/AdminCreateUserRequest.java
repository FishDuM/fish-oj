package hk.ljx.fishoj.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 管理员创建用户请求 (不含 role/id/createTime, role 由后端强制为 user)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminCreateUserRequest {

    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度3-50")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 8, max = 255, message = "密码长度8-255")
    private String password;

    private String nickname;

    @Email(message = "邮箱格式不正确")
    private String email;
}
