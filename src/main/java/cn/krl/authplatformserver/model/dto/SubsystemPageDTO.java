package cn.krl.authplatformserver.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author kuang
 * @description 子系统分页传输对象 考虑到子系统数量不会很多 暂时不用
 * @date 2021/11/30 11:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "子系统分页展示的表单", description = "")
public class SubsystemPageDTO {
    @ApiModelProperty(value = "总数")
    private Integer total;

    @ApiModelProperty(value = "当前页")
    private Integer cur;

    @ApiModelProperty(value = "页的大小")
    private Integer size;

    @ApiModelProperty(value = "子系统记录")
    private List<SubsystemDTO> subsystems;
}
