package cn.krl.authplatformserver.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author kuang
 * @description 用户更新表单
 * @date 2021/11/12 15:24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "用户更新的表单")
public class UserUpdateDTO {

    @ApiModelProperty(value = "用户ID")
    @NotBlank
    private Integer id;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "统一登录密码")
    private String password;

    @ApiModelProperty(value = "是否过期")
    private Boolean expired;

    @ApiModelProperty(value = "禁用")
    private Boolean disabled;

    @ApiModelProperty(value = "邮箱")
    private String email;
}
