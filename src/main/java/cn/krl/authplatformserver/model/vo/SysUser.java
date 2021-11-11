package cn.krl.authplatformserver.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author kuang
 * @since 2021-11-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SysUser对象", description="")
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户ID，保证唯一性（系统内部使用，防止主键id在数据库迁移或者合并之后因为自增会发生变动）")
    private String userId;

    @ApiModelProperty(value = "用户名(手机号)")
    private String username;

    @ApiModelProperty(value = "统一登录密码")
    private String password;

    @ApiModelProperty(value = "是否过期")
    private Boolean expired;

    @ApiModelProperty(value = "盐")
    private String salt;

    @ApiModelProperty(value = "注册时间")
    private String gmtCreate;

    @ApiModelProperty(value = "最新修改时间")
    private String gmtModifed;

    @ApiModelProperty(value = "禁用")
    private String disabled;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "最新的登录ip")
    private String lastIp;

}
