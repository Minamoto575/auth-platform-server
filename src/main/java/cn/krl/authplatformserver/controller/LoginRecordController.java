package cn.krl.authplatformserver.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.krl.authplatformserver.common.response.ResponseWrapper;
import cn.krl.authplatformserver.model.dto.LoginRecordDTO;
import cn.krl.authplatformserver.model.dto.LoginRecordPageDTO;
import cn.krl.authplatformserver.service.ILoginRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 前端控制器
 *
 * @author kuang
 * @since 2021-11-30
 */
@RestController
@RequestMapping("/api/loginRecord")
@Api(tags = "登录记录管理")
public class LoginRecordController {
    private final ILoginRecordService loginRecordService;
    private final String ADMIN = "admin";
    private final String USER = "user";

    public LoginRecordController(ILoginRecordService loginRecordService) {
        this.loginRecordService = loginRecordService;
    }

    /**
     * @description: 获取指定用户的登录记录
     * @param: id
     * @author kuang
     * @date: 2021/11/30
     */
    @SaCheckRole(value = ADMIN)
    @GetMapping("/list/{id}")
    @ApiOperation(value = "获取指定用户的登录记录")
    public ResponseWrapper listById(@PathVariable("id") Integer id) {
        ResponseWrapper responseWrapper;
        List<LoginRecordDTO> records = loginRecordService.listAllByUid(id);
        responseWrapper = ResponseWrapper.markSuccess();
        responseWrapper.setExtra("loginRecords", records);
        return responseWrapper;
    }

    /**
     * @description: 获取所有用户的登录记录
     * @author kuang
     * @date: 2021/11/30
     */
    @SaCheckRole(value = ADMIN)
    @GetMapping("/list/all")
    @ApiOperation(value = "获取所有用户的登录记录")
    public ResponseWrapper listAll() {
        ResponseWrapper responseWrapper;
        List<LoginRecordDTO> records = loginRecordService.listAll();
        responseWrapper = ResponseWrapper.markSuccess();
        responseWrapper.setExtra("loginRecords", records);
        return responseWrapper;
    }

    /**
     * @description: 分页获取指定用户的登录记录
     * @param: uid
     * @param: cur
     * @param: size
     * @author kuang
     * @date: 2021/11/30
     */
    @SaCheckRole(value = ADMIN)
    @GetMapping("/list/page/{uid}")
    @ApiOperation(value = "分页获取指定用户的登录记录")
    public ResponseWrapper listPageByUid(
            @PathVariable("uid") Integer uid,
            @RequestParam Integer cur,
            @RequestParam Integer size) {
        ResponseWrapper responseWrapper;
        LoginRecordPageDTO loginRecordPageDTO = loginRecordService.listPageByUid(uid, cur, size);
        responseWrapper = ResponseWrapper.markSuccess();
        responseWrapper.setExtra("page", loginRecordPageDTO);
        return responseWrapper;
    }

    /**
     * @description: 分页获取所有用户的登录记录
     * @param: cur
     * @param: size
     * @author kuang
     * @date: 2021/11/30
     */
    @SaCheckRole(value = ADMIN)
    @GetMapping("/list/page")
    @ApiOperation(value = "分页获取所有用户的登录记录")
    public ResponseWrapper listPage(@RequestParam Integer cur, @RequestParam Integer size) {
        ResponseWrapper responseWrapper;
        LoginRecordPageDTO loginRecordPageDTO = loginRecordService.listPage(cur, size);
        responseWrapper = ResponseWrapper.markSuccess();
        responseWrapper.setExtra("page", loginRecordPageDTO);
        return responseWrapper;
    }
}
