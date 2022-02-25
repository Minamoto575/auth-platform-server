package cn.krl.authplatformserver.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author kuang
 * @description 用户插入对象
 * @date 2022/2/25 16:52
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "用户插入的表单")
public class UserInsertDTO {
    @ApiModelProperty(value = "用户名")
    @Pattern(regexp = "^[a-zA-Z0-9_]{4,12}$", message = "用户名不合法")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @ApiModelProperty(value = "电话")
    @Pattern(regexp = "^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$", message = "不是一个正确的电话格式")
    @NotBlank(message = "电话不能为空")
    private String phone;

    @ApiModelProperty(value = "统一登录密码")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-zA-Z]).{8,20}$", message = "密码不合法")
    @NotBlank(message = "登录密码不能为空")
    private String password;
}
