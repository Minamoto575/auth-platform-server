package cn.krl.authplatformserver.model.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author kuang
 * @description
 * @date 2021/11/18 15:30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("用户列表信息")
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
    private Long gmtCreate;

    @ApiModelProperty(value = "最新修改时间")
    private Long gmtModifed;

    @ApiModelProperty(value = "禁用")
    private Boolean disabled;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "最新的登录ip")
    private String lastIp;
}