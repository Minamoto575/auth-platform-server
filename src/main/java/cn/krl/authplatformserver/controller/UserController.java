package cn.krl.authplatformserver.controller;

import cn.krl.authplatformserver.common.response.ResponseWrapper;
import cn.krl.authplatformserver.common.utils.RegexUtil;
import cn.krl.authplatformserver.model.dto.RegisterDTO;
import cn.krl.authplatformserver.model.dto.UserUpdateDTO;
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
@CrossOrigin
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

    @PostMapping("/update")
    @ApiOperation("用户更新")
    @ResponseBody
    public ResponseWrapper updateUser(@RequestBody @Validated UserUpdateDTO updateDTO) {
        String phone = updateDTO.getPhone();
        String email = updateDTO.getEmail();
        if (!RegexUtil.isBlank(phone) && !RegexUtil.isLegalPhone(phone)) {
            log.warn("用户:" + updateDTO.getId() + "更新失败,错误的电话格式");
            return ResponseWrapper.markPhoneError();
        }
        if (!RegexUtil.isBlank(email) && !RegexUtil.isLegalPhone(email)) {
            log.warn("用户:" + updateDTO.getId() + "更新失败,错误的邮箱格式");
            return ResponseWrapper.markEmailError();
        }
        try {
            userService.updateUser(updateDTO);
            log.info("用户:" + updateDTO.getId() + "更新成功");
            return ResponseWrapper.markSuccess();
        } catch (Exception e) {
            log.error("用户:" + updateDTO.getId() + "更新失败");
            return ResponseWrapper.markError();
        }
    }

    @PutMapping("/changePwd")
    @ApiOperation("用户更改密码")
    @ResponseBody
    public ResponseWrapper changePwd(@RequestParam String phone, @RequestParam String oldPwd,
                                     @RequestParam String newPwd) {
        ResponseWrapper responseWrapper;
        if (!userService.loginCheck(phone, oldPwd)) {
            log.warn(phone + "旧密码验证失败");
            return ResponseWrapper.markAccountError();
        }
        try {
            userService.changePwd(phone, newPwd);
            return ResponseWrapper.markSuccess();
        } catch (Exception e) {
            log.error(phone + "更改密码失败");
            return ResponseWrapper.markError();
        }

    }
}
