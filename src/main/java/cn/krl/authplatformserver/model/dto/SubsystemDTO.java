package cn.krl.authplatformserver.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/***
 * @description: 子系统传输对象
 * @author: kuang
 * @date: 2021/11/22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "子系统展示的表单", description = "")
public class SubsystemDTO {

    @ApiModelProperty(value = "id")
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
    private Long gmtCreate;

    @ApiModelProperty(value = "最新的修改时间")
    private Long gmtModified;

    @ApiModelProperty(value = "禁用 （1禁用，0不禁用）")
    private Boolean disabled;

    @ApiModelProperty(value = "过期（1过期，0未过期）")
    private Boolean expired;

    @ApiModelProperty(value = "备注")
    private String remark;
}
