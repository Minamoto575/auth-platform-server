package cn.krl.authplatformserver.model.dto;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author kuang
 * @description
 * @date 2021/11/18 15:30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "用户展示的表单")
public class UserDTO {

    @TableId(value = "id")
    private Integer id;

    @ApiModelProperty(value = "用户电话")
    private String phone;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "是否过期")
    private Boolean expired;

    @ApiModelProperty(value = "注册时间")
    private DateTime gmtCreate;

    @ApiModelProperty(value = "最新修改时间")
    private DateTime gmtModified;

    @ApiModelProperty(value = "禁用")
    private Boolean disabled;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "最新的登录ip")
    private String lastIp;

    @ApiModelProperty(value = "角色数组")
    private List<String> roles;
}
