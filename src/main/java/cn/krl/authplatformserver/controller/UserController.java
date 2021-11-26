package cn.krl.authplatformserver.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaCheckSafe;
import cn.dev33.satoken.annotation.SaMode;
import cn.dev33.satoken.sso.SaSsoUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.krl.authplatformserver.common.response.ResponseWrapper;
import cn.krl.authplatformserver.common.utils.AliMessageUtil;
import cn.krl.authplatformserver.common.utils.IpUtil;
import cn.krl.authplatformserver.common.utils.RegexUtil;
import cn.krl.authplatformserver.model.dto.UserRegisterDTO;
import cn.krl.authplatformserver.model.po.User;
import cn.krl.authplatformserver.service.IEmailService;
import cn.krl.authplatformserver.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户的前端控制器
 *
 * @author kuang
 * @since 2021-11-11
 */
@RestController
@RequestMapping("/api/user")
@Api(tags = "用户操作")
@Slf4j
@CrossOrigin
public class UserController {
    private final String ADMIN = "admin";
    private final String USER = "user";
    @Autowired private IUserService userService;
    @Autowired private IEmailService emailService;
    @Autowired private RegexUtil regexUtil;
    @Autowired private AliMessageUtil aliMessageUtil;
    @Autowired private IpUtil ipUtil;

    /**
     * @description 判断用户是否已经登录
     * @return: cn.krl.authplatformserver.common.response.ResponseWrapper
     * @date 2021/11/16
     */
    @GetMapping("/auth")
    @ApiOperation(value = "用户登录状态查询")
    @ResponseBody
    public ResponseWrapper isLogin(@RequestParam String redirect) {
        ResponseWrapper responseWrapper;
        if (!regexUtil.isLegalUrl(redirect)) {
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
     * @date 2021/11/16
     */
    @GetMapping("/login")
    @ApiOperation(value = "用户登录")
    @ResponseBody
    public ResponseWrapper login(
            HttpServletRequest request,
            @RequestParam String phone,
            @RequestParam String pwd,
            @RequestParam(required = false) String imageCode,
            @RequestParam(required = false) String redirect) {
        ResponseWrapper responseWrapper;
        if (!regexUtil.isBlank(redirect)) {
            if (!regexUtil.isLegalUrl(redirect)) {
                log.error("无效的URL格式");
                return ResponseWrapper.markUrlError();
            }
        }
        if (!regexUtil.isLegalPhone(phone)) {
            log.error("错误的电话格式");
            return ResponseWrapper.markPhoneError();
        }
        if (!userService.phoneExists(phone)) {
            log.info(phone + "该账号未注册");
            return ResponseWrapper.markAccountError();
        }
        // //登录的验证码检查
        // String sessionId = request.getSession().getId();
        // boolean checkCode = imageCodeUtil.checkImageCode(imageCode, sessionId);
        // if (!checkCode) {
        //     log.error("验证码检查出错");
        //     return ResponseWrapper.markImageCodeCheckError();
        // }
        if (userService.loginCheckByPhone(phone, pwd)) {
            User user = userService.getUserByPhone(phone);
            StpUtil.login(user.getId());
            // 封装携带的参数
            responseWrapper = ResponseWrapper.markSuccess();
            responseWrapper.setExtra("token", StpUtil.getTokenValue());
            String ticket = SaSsoUtil.createTicket(user.getId());
            responseWrapper.setExtra("ticket", ticket);
            responseWrapper.setExtra("phone", phone);
            responseWrapper.setExtra("uid", user.getId());
            // 更新最新登录的ip地址
            String ip = ipUtil.getIpAddress(request);
            log.info("ip:" + ip);
            userService.updateIp(phone, ip);

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
     * @date 2021/11/16
     */
    @SaCheckRole(
            value = {ADMIN, USER},
            mode = SaMode.OR)
    @GetMapping("/logout")
    @ApiOperation(value = "用户退出")
    @ResponseBody
    public ResponseWrapper logout(@RequestParam(required = false) String id) {
        if (regexUtil.isBlank(id)) {
            StpUtil.logout(id);
        } else {
            StpUtil.logout();
        }
        return ResponseWrapper.markSuccess();
    }

    /**
     * @param userRegisterDTO: 用户注册提交的表单 数据的要求详情见RegisterDTO
     * @description 用户注册方法
     * @return: cn.krl.authplatformserver.common.response.ResponseWrapper
     * @date 2021/11/14
     */
    @PostMapping("/register")
    @ApiOperation(value = "用户注册")
    @ResponseBody
    public ResponseWrapper registerUser(
            HttpServletRequest request, @RequestBody @Validated UserRegisterDTO userRegisterDTO) {
        ResponseWrapper responseWrapper;
        String messageCode = userRegisterDTO.getMessageCode();
        String phone = userRegisterDTO.getPhone();

        // 检查电话
        if (!regexUtil.isLegalPhone(phone)) {
            log.warn(phone + "电话格式错误");
            return ResponseWrapper.markPhoneError();
        }
        if (userService.phoneExists(phone)) {
            log.warn(phone + "电话已被注册，注册失败！");
            return ResponseWrapper.markPhoneExist();
        }
        // 短信验证码检查
        boolean check = aliMessageUtil.checkMessageCode(messageCode, phone);
        if (!check) {
            return ResponseWrapper.markMessageCodeCheckError();
        }

        // 注册
        try {
            userService.registerUser(userRegisterDTO);
            log.info(phone + "注册成功");
            // 更新最新登录的ip地址
            String ip = ipUtil.getIpAddress(request);
            userService.updateIp(phone, ip);
            responseWrapper = ResponseWrapper.markSuccess();
        } catch (Exception e) {
            responseWrapper = ResponseWrapper.markRegisterError();
            log.error(phone + "注册失败");
            e.printStackTrace();
        }
        return responseWrapper;
    }

    /**
     * @param phone: 电话号码 当作用户的账号
     * @param oldPwd: 验证旧密码
     * @param newPwd: 新密码
     * @description 用户更改密码的方法
     * @return: cn.krl.authplatformserver.common.response.ResponseWrapper
     * @date 2021/11/14
     */
    @SaCheckRole(
            value = {ADMIN, USER},
            mode = SaMode.OR)
    @PutMapping("/changePwd/oldPwd")
    @ApiOperation(value = "用户更改密码(通过旧密码修改)")
    @ResponseBody
    public ResponseWrapper changePwdByOldPwd(
            @RequestParam String phone, @RequestParam String oldPwd, @RequestParam String newPwd) {
        ResponseWrapper responseWrapper;
        // 验证旧密码
        if (!userService.loginCheckByPhone(phone, oldPwd)) {
            log.warn(phone + "旧密码验证失败");
            return ResponseWrapper.markAccountError();
        }
        // 检查新密码
        if (!regexUtil.isLegalPassword(newPwd)) {
            log.error("新密码格式不合法");
            return ResponseWrapper.markPasswordIllegalError();
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

    /**
     * @description: 用户通过绑定的电话号码和验证码修改登录密码
     * @param: phone 电话
     * @param: messageCode 验证码
     * @param: newPwd 新密码
     * @author kuang
     * @date: 2021/11/23
     */
    @SaCheckRole(
            value = {ADMIN, USER},
            mode = SaMode.OR)
    @PutMapping("/changePwd/phone")
    @ApiOperation(value = "用户更改密码(通过电话号码和验证码修改)")
    @ResponseBody
    public ResponseWrapper changePwdByPhone(
            @RequestParam String phone,
            @RequestParam String messageCode,
            @RequestParam String newPwd) {
        ResponseWrapper responseWrapper;
        boolean checkMessageCode = aliMessageUtil.checkMessageCode(messageCode, phone);
        // 检查新密码
        if (!regexUtil.isLegalPassword(newPwd)) {
            log.error("新密码格式不合法");
            return ResponseWrapper.markPasswordIllegalError();
        }
        // 检查验证码
        if (!checkMessageCode) {
            log.error("验证码检查出错");
            return ResponseWrapper.markMessageCodeCheckError();
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

    /**
     * @description:用户更改绑定的电话号码
     * @param: id 用户id
     * @param: phone 新电话
     * @param: messageCode 短信验证码
     * @author kuang
     * @date: 2021/11/23
     */
    @SaCheckRole(
            value = {ADMIN, USER},
            mode = SaMode.OR)
    @SaCheckSafe
    @PutMapping("/changePhone")
    @ApiOperation(value = "用户更改电话")
    @ResponseBody
    public ResponseWrapper changePhone(
            @RequestParam Integer id,
            @RequestParam String phone,
            @RequestParam String pwd,
            @RequestParam String messageCode) {
        ResponseWrapper responseWrapper;
        // 检查电话
        if (userService.phoneExists(phone)) {
            log.error("该电话已经被注册");
            return ResponseWrapper.markPhoneExist();
        }
        // 检查密码
        if (!userService.loginCheckById(id, pwd)) {
            log.error(id + "密码错误");
            return ResponseWrapper.markAccountError();
        }
        // 检查验证码
        boolean checkMessageCode = aliMessageUtil.checkMessageCode(messageCode, phone);
        if (!checkMessageCode) {
            log.error("验证码检查出错");
            return ResponseWrapper.markMessageCodeCheckError();
        }
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

    @SaCheckRole(
            value = {ADMIN, USER},
            mode = SaMode.OR)
    @PutMapping("/bindEmail")
    @ApiOperation(value = "用户绑定邮箱")
    @ResponseBody
    public ResponseWrapper bindEmail(
            @RequestParam Integer id,
            @RequestParam String email,
            @RequestParam String pwd,
            @RequestParam String emailCode) {
        ResponseWrapper responseWrapper;
        if (!regexUtil.isLegalEmail(email)) {
            log.error("邮箱格式错误");
            return ResponseWrapper.markEmailError();
        }
        // 检查邮箱
        if (userService.emailExists(email)) {
            log.error("该邮箱已经被注册");
            return ResponseWrapper.markEmailExist();
        }
        // 检查密码
        if (!userService.loginCheckById(id, pwd)) {
            log.error(id + "密码错误");
            return ResponseWrapper.markAccountError();
        }
        // 检查验证码
        boolean checkEmailCode = emailService.checkEmailCode(email, emailCode);
        if (!checkEmailCode) {
            log.error("验证码检查出错");
            return ResponseWrapper.markEmailCodeCheckError();
        }
        try {
            userService.changeEmail(id, email);
            log.info("用户id：" + id + "成功修改新的邮箱：" + email);
            responseWrapper = ResponseWrapper.markSuccess();
        } catch (Exception e) {
            log.error("用户id：" + id + "修改新的邮箱：" + email + "失败");
            responseWrapper = ResponseWrapper.markEmailBindError();
        }
        return responseWrapper;
    }

    @SaCheckRole(
            value = {ADMIN, USER},
            mode = SaMode.OR)
    @PutMapping("/changeName")
    @ApiOperation(value = "用户修改用户名")
    @ResponseBody
    public ResponseWrapper changeName(@RequestParam Integer id, @RequestParam String name) {
        // 检查输入的用户名
        if (!regexUtil.isLegalUsername(name)) {
            return ResponseWrapper.markUsernameIllegalError();
        }
        ResponseWrapper responseWrapper;
        try {
            userService.changeName(id, name);
            responseWrapper = ResponseWrapper.markSuccess();
        } catch (Exception e) {
            log.error("修改用户名失败");
            e.printStackTrace();
            responseWrapper = ResponseWrapper.markError();
        }
        return responseWrapper;
    }
}
