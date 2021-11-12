package cn.krl.authplatformserver.controller;

import cn.krl.authplatformserver.common.response.ResponseWrapper;
import cn.krl.authplatformserver.model.dto.RegisterDTO;
import cn.krl.authplatformserver.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 前端控制器
 *
 * @author kuang
 * @since 2021-11-11
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户的api")
@Slf4j
public class UserController {
    @Autowired private IUserService userService;

    @PostMapping("/register")
    @ApiOperation("用户注册")
    @ResponseBody
    public ResponseWrapper registerUser(@RequestBody @Validated RegisterDTO registerDTO) {
        ResponseWrapper responseWrapper;
        String phone = registerDTO.getPhone();
        String email = registerDTO.getEmail();
        if (userService.phoneExists(phone)) {
            log.warn(phone + "电话已被注册，注册失败！");
            return ResponseWrapper.markPhoneExist();
        }
        if (userService.emailExists(email)) {
            log.warn(email + "邮箱已被使用，注册失败！");
            return ResponseWrapper.markEmailExist();
        }
        try {
            userService.registerUser(registerDTO);
            log.info(phone + "注册成功");
            responseWrapper = ResponseWrapper.markSuccess();
        } catch (Exception e) {
            responseWrapper = ResponseWrapper.markError();
            log.error(phone + "注册失败");
            e.printStackTrace();
        }
        return responseWrapper;
    }
}
