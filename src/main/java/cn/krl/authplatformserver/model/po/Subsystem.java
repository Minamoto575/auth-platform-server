package cn.krl.authplatformserver.model.po;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author kuang
 * @since 2021-11-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "子系统对象", description = "")
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
    @TableField(fill = FieldFill.INSERT)
    private DateTime gmtCreate;

    @ApiModelProperty(value = "最新的修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private DateTime gmtModified;

    @ApiModelProperty(value = "禁用 （1禁用，0不禁用）")
    private Boolean disabled;

    @ApiModelProperty(value = "过期（1过期，0未过期）")
    private Boolean expired;

    @ApiModelProperty(value = "备注")
    private String remark;
}
