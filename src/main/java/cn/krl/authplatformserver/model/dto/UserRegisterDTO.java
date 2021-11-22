package cn.krl.authplatformserver.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author kuang
 * @description 注册提交的表单
 * @date 2021/11/11 15:17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("用户注册需要提交的表单")
public class UserRegisterDTO {

    @ApiModelProperty(value = "用户名")
    @Length(min = 1, max = 10, message = "用户名长度为1-10个字符")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @ApiModelProperty(value = "电话")
    @Pattern(regexp = "^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$", message = "不是一个正确的电话格式")
    @NotBlank(message = "电话不能为空")
    private String phone;

    @ApiModelProperty(value = "统一登录密码")
    @Length(min = 6, max = 30, message = "密码长度为6-30个字符")
    @NotBlank(message = "登录密码不能为空")
    private String password;

    @ApiModelProperty(value = "邮箱")
    @NotBlank(message = "邮箱不能为空")
    @Email
    private String email;
}
