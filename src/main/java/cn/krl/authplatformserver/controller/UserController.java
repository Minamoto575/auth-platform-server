package cn.krl.authplatformserver.controller;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.sso.SaSsoHandle;
import cn.dev33.satoken.sso.SaSsoUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.krl.authplatformserver.common.response.ResponseWrapper;
import cn.krl.authplatformserver.common.utils.RegexUtil;
import cn.krl.authplatformserver.model.dto.RegisterDTO;
import cn.krl.authplatformserver.model.dto.UserUpdateDTO;
import cn.krl.authplatformserver.model.po.User;
import cn.krl.authplatformserver.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户前端控制器
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
    @Autowired
    private IUserService userService;

    /**
     * @description 判断用户是否已经登录
     * @return: cn.krl.authplatformserver.common.response.ResponseWrapper
     * @data 2021/11/16
     */
    @GetMapping("/auth")
    @ApiOperation("用户登录状态查询")
    @ResponseBody
    public ResponseWrapper isLogin(@RequestParam String redirect) {
        ResponseWrapper responseWrapper;
        if(StpUtil.isLogin()){
            log.info("用户登录状态查询：用户已成功登录");
            responseWrapper=ResponseWrapper.markRedirect();
            responseWrapper.setExtra("redirect",redirect);
        }else{
            log.info("用户登录状态查询：用户未登录");
            return ResponseWrapper.markNOTLOGINError();
        }
        return responseWrapper;
    }

    /**
     * @description 用户登录
     * @param phone:  电话
     * @param pwd:  密码
     * @return: cn.krl.authplatformserver.common.response.ResponseWrapper
     * @data 2021/11/16
     */
    @GetMapping("/login")
    @ApiOperation("用户登录")
    @ResponseBody
    public ResponseWrapper login(@RequestParam String phone, @RequestParam String pwd) {
        ResponseWrapper responseWrapper;
        if(!userService.phoneExists(phone)){
            log.info(phone + "该账号未注册");
            return ResponseWrapper.markAccountError();
        }
        if (userService.loginCheck(phone, pwd)) {
            User user = userService.getUserByPhone(phone);
            StpUtil.login(user.getId());
            responseWrapper = ResponseWrapper.markSuccess();
            responseWrapper.setExtra("token", StpUtil.getTokenValue());
            log.info(phone + "登录成功");
            return responseWrapper;
        }
        log.info(phone + "登录失败，电话号码或者密码错误");
        return ResponseWrapper.markAccountError();
    }

    /**
     * @description 用户退出
     * @param id 用户id
     * @return: cn.krl.authplatformserver.common.response.ResponseWrapper
     * @data 2021/11/16
     */
    @GetMapping("/logout")
    @ApiOperation("用户退出")
    @ResponseBody
    public ResponseWrapper logout(@RequestParam String id) {
        StpUtil.logout(id);
        return ResponseWrapper.markSuccess();
    }


    /**
     * @param registerDTO: 用户注册提交的表单 数据的要求详情见RegisterDTO
     * @description 用户注册方法
     * @return: cn.krl.authplatformserver.common.response.ResponseWrapper
     * @data 2021/11/14
     */
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
            responseWrapper = ResponseWrapper.markRegisterError();
            log.error(phone + "注册失败");
            e.printStackTrace();
        }
        return responseWrapper;
    }

    /**
     * @param updateDTO: 用户更新的数据表单 要求详情见UserUpdateDTO
     * @description 用户更新方法，不做任何检验，开发给管理员
     * @return: cn.krl.authplatformserver.common.response.ResponseWrapper
     * @data 2021/11/14
     */
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
            return ResponseWrapper.markUpdateUserError();
        }
    }

    /**
     * @param phone:  电话号码 当作用户的账号
     * @param oldPwd: 验证旧密码
     * @param newPwd: 新密码
     * @description 用户更改密码的方法
     * @return: cn.krl.authplatformserver.common.response.ResponseWrapper
     * @data 2021/11/14
     */
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
            responseWrapper = ResponseWrapper.markSuccess();
        } catch (Exception e) {
            log.error(phone + "更改密码失败");
            responseWrapper = ResponseWrapper.markChangePwdError();
        }
        return responseWrapper;
    }

    @PutMapping("/changePhone")
    @ApiOperation("用户更改电话")
    @ResponseBody
    public ResponseWrapper changePwd(@RequestParam String id, @RequestParam String phone) {
        ResponseWrapper responseWrapper;
        try {
            userService.changePhone(id, phone);
            log.info("用户id：" + id + "成功修改新的电话：" + phone);
            responseWrapper = ResponseWrapper.markSuccess();
        } catch (Exception e) {
            log.error("用户id：" + id + "修改新的电话：" + phone + "失败");
            responseWrapper = ResponseWrapper.markChangePhoneError();
        }
        return responseWrapper;
    }


}
