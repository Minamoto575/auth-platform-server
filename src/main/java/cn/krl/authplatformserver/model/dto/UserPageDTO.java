package cn.krl.authplatformserver.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author kuang
 * @description 用户分页传输对象
 * @date 2021/11/30 11:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "用户分页展示的表单", description = "")
public class UserPageDTO {
    @ApiModelProperty(value = "总数")
    private Integer total;

    @ApiModelProperty(value = "当前页")
    private Integer cur;

    @ApiModelProperty(value = "页的大小")
    private Integer size;

    @ApiModelProperty(value = "用户列表")
    private List<UserDTO> users;
}
