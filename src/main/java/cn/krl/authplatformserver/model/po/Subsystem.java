package cn.krl.authplatformserver.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author kuang
 * @since 2021-11-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Subsystem对象", description="")
public class Subsystem implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "子系统名称")
    private String name;

    @ApiModelProperty(value = "简要描述")
    private String description;

    @ApiModelProperty(value = "网址的url")
    private String webUrl;

    @ApiModelProperty(value = "图标的url")
    private String iconUrl;

    @ApiModelProperty(value = "创建时间")
    private String gmtCreate;

    @ApiModelProperty(value = "最新的修改时间")
    private String gmtModified;

    @ApiModelProperty(value = "禁用 （1禁用，0不禁用）")
    private Boolean disabled;

    @ApiModelProperty(value = "过期（1过期，0未过期）")
    private Boolean expired;

    @ApiModelProperty(value = "备注")
    private String remark;


}
