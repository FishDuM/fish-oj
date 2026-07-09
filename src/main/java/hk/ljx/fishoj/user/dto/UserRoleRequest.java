package hk.ljx.fishoj.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 管理员修改用户角色请求
 */
@Data
public class UserRoleRequest {

    @NotNull(message = "用户 id 不能为空")
    private Long id;

    @NotBlank(message = "角色不能为空")
    private String role;
}
