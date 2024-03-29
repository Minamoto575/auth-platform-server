package cn.krl.authplatformserver.model.po;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author kuang
 * @since 2021-11-11
 */
@Data
@TableName(autoResultMap = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "User对象", description = "统一登陆的用户实体类")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户ID，保证唯一性（系统内部使用，防止主键id在数据库迁移或者合并之后因为自增会发生变动）")
    private String phone;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "统一登录密码")
    private String password;

    @ApiModelProperty(value = "是否过期")
    private Boolean expired;

    @ApiModelProperty(value = "盐")
    private String salt;

    @ApiModelProperty(value = "注册时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @ApiModelProperty(value = "最新修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;

    @ApiModelProperty(value = "最近登录时间")
    private LocalDateTime gmtLogin;

    @ApiModelProperty(value = "禁用")
    private Boolean disabled;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "最新的登录ip")
    private String lastIp;

    @TableField(typeHandler = FastjsonTypeHandler.class)
    @ApiModelProperty(value = "角色数组")
    private List<String> roles;
}
