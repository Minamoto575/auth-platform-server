package cn.krl.authplatformserver.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @description: 子系统注册的表单
 * @author: kuang
 * @date: 2021/11/22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "子系统注册的表单", description = "")
public class SubsystemRegisterDTO {

    @ApiModelProperty(value = "子系统名称")
    @NotBlank(message = "子系统名称不能为空")
    private String name;

    @ApiModelProperty(value = "简要描述")
    private String description;

    @ApiModelProperty(value = "网址的url")
    @Pattern(
            regexp = "(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]",
            message = "不是一个正确的网址url")
    @NotBlank(message = "网址不能为空")
    private String webUrl;

    @ApiModelProperty(value = "图标的url")
    @Pattern(
            regexp = "(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]",
            message = "不是一个正确的图片url")
    @NotBlank(message = "图标不能为空")
    private String iconUrl;

    @ApiModelProperty(value = "禁用 （1禁用，0不禁用）")
    private Boolean disabled = false;

    @ApiModelProperty(value = "过期（1过期，0未过期）")
    private Boolean expired = false;

    @ApiModelProperty(value = "备注")
    private String remark;
}
