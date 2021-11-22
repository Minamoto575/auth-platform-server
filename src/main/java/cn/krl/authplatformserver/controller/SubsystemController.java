package cn.krl.authplatformserver.controller;

import cn.krl.authplatformserver.common.response.ResponseWrapper;
import cn.krl.authplatformserver.common.utils.RegexUtil;
import cn.krl.authplatformserver.model.dto.SubsystemRegisterDTO;
import cn.krl.authplatformserver.model.dto.SubsystemUpdateDTO;
import cn.krl.authplatformserver.service.ISubsystemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 子系统管理的前端控制器
 *
 * @author kuang
 * @since 2021-11-22
 */
@RestController
@RequestMapping("/api/subsystem")
@Slf4j
@Api(tags = "子系统记录的api")
public class SubsystemController {

    @Autowired private ISubsystemService subsystemService;
    @Autowired private RegexUtil regexUtil;

    /**
     * @description: 注册一条子系统的记录
     * @param: registerDTO 需要提交的表单
     * @author kuang
     * @date: 2021/11/22
     */
    @PostMapping("/register")
    @ApiOperation(value = "注册一条子系统记录")
    @ResponseBody
    ResponseWrapper registerSubsystem(@RequestBody @Validated SubsystemRegisterDTO registerDTO) {
        ResponseWrapper responseWrapper;
        try {
            subsystemService.register(registerDTO);
            responseWrapper = ResponseWrapper.markSuccess();
            log.info("子系统记录注册成功");
        } catch (Exception e) {
            responseWrapper = ResponseWrapper.markSubsystemRegisterError();
            e.printStackTrace();
            log.error("子系统记录注册失败");
        }
        return responseWrapper;
    }
    /**
     * @description: 更新一条子系统的记录
     * @param: updateDTO 需要提交的表单
     * @author kuang
     * @date: 2021/11/22
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新一条子系统记录")
    @ResponseBody
    ResponseWrapper registerSubsystem(@RequestBody SubsystemUpdateDTO updateDTO) {
        ResponseWrapper responseWrapper;
        Integer id = updateDTO.getId();
        String webUrl = updateDTO.getWebUrl();
        String iconUrl = updateDTO.getIconUrl();
        if (id == null || subsystemService.getById(id) == null) {
            log.error("没找到子系统记录，id=" + id);
            return ResponseWrapper.markSubsystemNotFoundError();
        }

        // 要求改web和图片时 检查其url是否合法
        if (!regexUtil.isBlank(webUrl) && !regexUtil.isLegalUrl(webUrl)) {
            log.error("错误的网站url");
            return ResponseWrapper.markUrlError();
        }
        if (!regexUtil.isBlank(iconUrl) && !regexUtil.isLegalUrl(iconUrl)) {
            log.error("错误的图标url");
            return ResponseWrapper.markUrlError();
        }

        try {
            subsystemService.update(updateDTO);
            responseWrapper = ResponseWrapper.markSuccess();
            log.info("子系统记录更新成功");
        } catch (Exception e) {
            responseWrapper = ResponseWrapper.markSubsystemUpdateError();
            e.printStackTrace();
            log.error("子系统记录更新失败");
        }
        return responseWrapper;
    }

    @DeleteMapping("/delete/id")
    @ApiOperation(value = "通过id删除一条子系统记录")
    @ResponseBody
    ResponseWrapper deleteSubsystemById(@RequestParam Integer id) {
        subsystemService.removeById(id);
        return ResponseWrapper.markSuccess();
    }
}
