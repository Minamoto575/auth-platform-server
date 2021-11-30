package cn.krl.authplatformserver.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author kuang
 * @description 登陆记录传输对象
 * @date 2021/11/30 9:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "登录记录展示的表单", description = "")
public class LoginRecordDTO {

    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty(value = "用户id")
    private Integer uid;

    @ApiModelProperty(value = "登录时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty(value = "登录ip")
    private String ip;
}
