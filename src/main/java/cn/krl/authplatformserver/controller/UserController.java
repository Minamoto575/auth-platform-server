package cn.krl.authplatformserver.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.krl.authplatformserver.common.response.ResponseWrapper;
import cn.krl.authplatformserver.common.utils.RegexUtil;
import cn.krl.authplatformserver.model.dto.RegisterDTO;
import cn.krl.authplatformserver.model.dto.UserDTO;
import cn.krl.authplatformserver.model.dto.UserUpdateDTO;
import cn.krl.authplatformserver.model.po.User;
import cn.krl.authplatformserver.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户前端控制器
 *
 * @author kuang
 * @since 2021-11-11
 */
@RestController
@RequestMapping("/api/user")
@Api(tags = "用户的api")
@Slf4j
@CrossOrigin
public class UserController {
    @Autowired private IUserService userService;

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
        if (!RegexUtil.isLegalUrl(redirect)) {
            log.error("无效的URL格式");
            return ResponseWrapper.markUrlError();
        }
        if (StpUtil.isLogin()) {
            log.info("用户登录状态查询：用户已成功登录");
            responseWrapper = ResponseWrapper.markRedirect();
        } else {
            log.info("用户登录状态查询：用户未登录");
            responseWrapper = ResponseWrapper.markNOTLOGINError();
        }
        responseWrapper.setExtra("redirect", redirect);
        return responseWrapper;
    }

    /**
     * @param phone: 电话
     * @param pwd: 密码
     * @description 用户登录
     * @return: cn.krl.authplatformserver.common.response.ResponseWrapper
     * @data 2021/11/16
     */
    @GetMapping("/login")
    @ApiOperation("用户登录")
    @ResponseBody
    public ResponseWrapper login(
            @RequestParam String phone,
            @RequestParam String pwd,
            @RequestParam(required = false) String redirect) {
        ResponseWrapper responseWrapper;
        if (!RegexUtil.isBlank(redirect)) {
            if (!RegexUtil.isLegalUrl(redirect)) {
                log.error("无效的URL格式");
                return ResponseWrapper.markUrlError();
            }
        }
        if (!userService.phoneExists(phone)) {
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
        } else {
            log.info(phone + "登录失败，电话号码或者密码错误");
            responseWrapper = ResponseWrapper.markAccountError();
        }
        responseWrapper.setExtra("redirect", redirect);
        return responseWrapper;
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
     * @param phone: 电话号码 当作用户的账号
     * @param oldPwd: 验证旧密码
     * @param newPwd: 新密码
     * @description 用户更改密码的方法
     * @return: cn.krl.authplatformserver.common.response.ResponseWrapper
     * @data 2021/11/14
     */
    @PutMapping("/changePwd")
    @ApiOperation("用户更改密码")
    @ResponseBody
    public ResponseWrapper changePwd(
            @RequestParam String phone, @RequestParam String oldPwd, @RequestParam String newPwd) {
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

    @DeleteMapping("/delete/id")
    @ApiOperation("删除用户")
    @ResponseBody
    public ResponseWrapper deleteById(@RequestParam String id) {
        if (userService.removeById(id)) {
            log.info("删除用户成功，用户id:" + id);
            return ResponseWrapper.markSuccess();
        } else {
            log.info("删除用户失败，用户id:" + id);
            return ResponseWrapper.markError();
        }
    }

    @GetMapping("/list/all")
    @ApiOperation("获取用户列表")
    @ResponseBody
    public ResponseWrapper listAll() {
        ResponseWrapper responseWrapper;
        try {
            List<UserDTO> users = userService.listAll();
            responseWrapper = ResponseWrapper.markSuccess();
            responseWrapper.setExtra("users", users);
            log.info("查询所有用户成功");
        } catch (Exception e) {
            log.error("查询所有用户失败");
            responseWrapper = ResponseWrapper.markError();
            e.printStackTrace();
        }
        return responseWrapper;
    }

    @GetMapping("/list/page")
    @ApiOperation("分页获取用户")
    @ResponseBody
    public ResponseWrapper listPage(int cur, int size) {
        ResponseWrapper responseWrapper;
        try {
            List<UserDTO> users = userService.listPage(cur, size);
            responseWrapper = ResponseWrapper.markSuccess();
            responseWrapper.setExtra("users", users);
            log.info("分页查询所有用户成功");
        } catch (Exception e) {
            log.error("分页查询所有用户失败");
            responseWrapper = ResponseWrapper.markError();
            e.printStackTrace();
        }
        return responseWrapper;
    }
}
