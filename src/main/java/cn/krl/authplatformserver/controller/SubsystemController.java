package cn.krl.authplatformserver.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import cn.krl.authplatformserver.common.response.ResponseWrapper;
import cn.krl.authplatformserver.common.utils.RegexUtil;
import cn.krl.authplatformserver.model.dto.SubsystemDTO;
import cn.krl.authplatformserver.model.dto.SubsystemRegisterDTO;
import cn.krl.authplatformserver.model.dto.SubsystemUpdateDTO;
import cn.krl.authplatformserver.service.ISubsystemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 子系统管理的前端控制器
 *
 * @author kuang
 * @since 2021-11-22
 */
@RestController
@RequestMapping("/api/subsystem")
@Slf4j
@Api(tags = "子系统管理")
public class SubsystemController {
    private final String ADMIN = "admin";
    private final String USER = "user";
    @Autowired private ISubsystemService subsystemService;
    @Autowired private RegexUtil regexUtil;

    /**
     * @description: 注册一条子系统的记录
     * @param: registerDTO 需要提交的表单
     * @author kuang
     * @date: 2021/11/22
     */
    @SaCheckRole(value = ADMIN)
    @PostMapping("/register")
    @ApiOperation(value = "注册一条子系统记录(管理员使用)")
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
    @SaCheckRole(value = ADMIN)
    @PutMapping("/update")
    @ApiOperation(value = "更新一条子系统记录(管理员使用)")
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

    /**
     * @description: 根据id删除一条子系统记录 不对id做检查 id对应记录不存在也不抛异常
     * @param: id 子系统记录的id
     * @author kuang
     * @date: 2021/11/22
     */
    @SaCheckRole(value = ADMIN)
    @DeleteMapping("/delete/id")
    @ApiOperation(value = "通过id删除一条子系统记录(管理员使用)")
    @ResponseBody
    ResponseWrapper deleteSubsystemById(@RequestParam Integer id) {
        subsystemService.removeById(id);
        return ResponseWrapper.markSuccess();
    }

    /**
     * @description: 通过id获得一条子系统记录
     * @param: id 子系统的id
     * @author kuang
     * @date: 2021/11/22
     */
    @SaCheckRole(value = ADMIN)
    @GetMapping("/list/id")
    @ApiOperation(value = "通过id获得一条子系统记录(管理员使用)")
    @ResponseBody
    ResponseWrapper listSubsystemById(@RequestParam Integer id) {
        ResponseWrapper responseWrapper;
        if (subsystemService.getById(id) == null) {
            log.error("没找到id=" + id + "的子系统记录");
            return ResponseWrapper.markNoData();
        }
        SubsystemDTO subsystemDTO = subsystemService.listById(id);
        responseWrapper = ResponseWrapper.markSuccess();
        responseWrapper.setExtra("subsystem", subsystemDTO);
        return responseWrapper;
    }

    /**
     * @description: 获得所有子系统记录 包括过期的和禁用的
     * @author kuang
     * @date: 2021/11/22
     */
    @SaCheckRole(value = ADMIN)
    @GetMapping("/list/all")
    @ApiOperation(value = "获得所以子系统记录（包括禁用和过期的，管理员使用）")
    @ResponseBody
    ResponseWrapper listAllSubsystem() {
        ResponseWrapper responseWrapper;
        List<SubsystemDTO> subsystemDTOs = subsystemService.listAll();
        if (subsystemDTOs.size() == 0) {
            log.error("数据库中不存在子系统记录");
            responseWrapper = ResponseWrapper.markNoData();
        } else {
            responseWrapper = ResponseWrapper.markSuccess();
            responseWrapper.setExtra("subsystems", subsystemDTOs);
        }

        return responseWrapper;
    }

    /**
     * @description: 获得正在工作的子系统记录 不包括过期的和禁用的
     * @author kuang
     * @date: 2021/11/22
     */
    @SaCheckRole(
            value = {ADMIN, USER},
            mode = SaMode.OR)
    @GetMapping("/list/workable")
    @ApiOperation(value = "获得正在工作的子系统记录（不包括禁用和过期的，用户使用）")
    @ResponseBody
    ResponseWrapper listWorkableSubsystem() {
        ResponseWrapper responseWrapper;
        List<SubsystemDTO> subsystemDTOs = subsystemService.listWorkable();
        if (subsystemDTOs.size() == 0) {
            log.error("数据库中不存在可工作的子系统记录");
            responseWrapper = ResponseWrapper.markNoData();
        } else {
            responseWrapper = ResponseWrapper.markSuccess();
            responseWrapper.setExtra("subsystems", subsystemDTOs);
        }
        return responseWrapper;
    }
}
