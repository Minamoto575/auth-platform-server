package cn.krl.authplatformserver.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaCheckSafe;
import cn.dev33.satoken.annotation.SaMode;
import cn.dev33.satoken.config.SaTokenConfig;
import cn.dev33.satoken.sso.SaSsoHandle;
import cn.dev33.satoken.sso.SaSsoUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.krl.authplatformserver.common.response.ResponseWrapper;
import cn.krl.authplatformserver.common.utils.AliMessageUtil;
import cn.krl.authplatformserver.common.utils.IpUtil;
import cn.krl.authplatformserver.common.utils.RegexUtil;
import cn.krl.authplatformserver.model.dto.LoginRecordDTO;
import cn.krl.authplatformserver.model.dto.LoginRecordPageDTO;
import cn.krl.authplatformserver.model.dto.UserDTO;
import cn.krl.authplatformserver.model.dto.UserRegisterDTO;
import cn.krl.authplatformserver.model.po.User;
import cn.krl.authplatformserver.service.IEmailService;
import cn.krl.authplatformserver.service.ILoginRecordService;
import cn.krl.authplatformserver.service.IUserService;
import com.ejlchina.okhttps.OkHttps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    private final IUserService userService;
    private final IEmailService emailService;
    private final RegexUtil regexUtil;
    private final AliMessageUtil aliMessageUtil;
    private final IpUtil ipUtil;
    private final ILoginRecordService loginRecordService;

    @Value("${sa-token.sso.secretkey}")
    private String secretkey;

    public UserController(
            IUserService userService,
            IEmailService emailService,
            ILoginRecordService loginRecordService,
            RegexUtil regexUtil,
            AliMessageUtil aliMessageUtil,
            IpUtil ipUtil) {
        this.userService = userService;
        this.emailService = emailService;
        this.loginRecordService = loginRecordService;
        this.regexUtil = regexUtil;
        this.aliMessageUtil = aliMessageUtil;
        this.ipUtil = ipUtil;
    }

    /** 配置SSO相关参数 */
    @Autowired
    private void configSso(SaTokenConfig cfg) {
        // 配置 Http 请求处理器 （在模式三的单点注销功能下用到，如不需要可以注释掉）
        cfg.sso.setSendHttp(
                url -> {
                    return OkHttps.sync(url).get().getBody().toString();
                });
    }

    /**
     * @description 判断用户是否已经登录
     * @return: cn.krl.authplatformserver.common.response.ResponseWrapper
     * @date 2021/11/16
     */
    @CrossOrigin
    @GetMapping("/auth")
    @ApiOperation(value = "用户登录状态查询")
    @ResponseBody
    public ResponseWrapper isLogin(@RequestParam String redirect) {
        ResponseWrapper responseWrapper;
        String ticket = null;
        if (!regexUtil.isLegalUrl(redirect)) {
            log.error("无效的URL格式");
            return ResponseWrapper.markUrlError();
        }
        if (StpUtil.isLogin()) {
            log.info("用户登录状态查询：用户已成功登录");
            ticket = SaSsoUtil.createTicket(StpUtil.getLoginId());
            responseWrapper = ResponseWrapper.markRedirect();
        } else {
            log.info("用户登录状态查询：用户未登录");
            responseWrapper = ResponseWrapper.markNotLoginError();
        }
        if (ticket != null) {
            redirect = redirect + "?ticket=" + ticket;
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
            // 更新最近登录的ip地址
            String ip = ipUtil.getIpAddress(request);
            userService.updateStatus(phone, ip);

            // 插入登录记录
            loginRecordService.insert(user.getId(), ip);

            // 封装携带的参数
            responseWrapper = ResponseWrapper.markSuccess();
            String ticket = SaSsoUtil.createTicket(user.getId());
            responseWrapper.setExtra("token", StpUtil.getTokenValue());
            responseWrapper.setExtra("ticket", ticket);
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
            userDTO.setLastIp(ip);
            responseWrapper.setExtra("userInfo", userDTO);

            // 用户电话存入redis
            StpUtil.getSession().set("phone", phone);
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
     * @description: 已经登录的用户获取ticket
     * @author kuang
     * @date: 2021/12/8
     */
    @SaCheckRole(
            value = {ADMIN, USER},
            mode = SaMode.OR)
    @GetMapping("/ticket")
    @ApiOperation(value = "已登录用户获取临时的ticket")
    @ResponseBody
    public ResponseWrapper getTicket() {
        ResponseWrapper responseWrapper = ResponseWrapper.markSuccess();
        String ticket = SaSsoUtil.createTicket(StpUtil.getLoginId());
        responseWrapper.setExtra("ticket", ticket);
        return responseWrapper;
    }

    /**
     * @description 通过签发的ticket进行登录
     * @param ticket 票据
     * @param ssoLogoutCall 单点注销时的回调通知地址，只在SSO模式三单点注销时需要携带此参数
     * @author kuang
     * @date 2021/12/10
     */
    @GetMapping("/doLoginByTicket")
    @ApiOperation(value = "检查ticket")
    @ResponseBody
    public ResponseWrapper checkTicket(
            @RequestParam String ticket, @RequestParam(required = false) String ssoLogoutCall) {
        ResponseWrapper responseWrapper;
        Object loginId = SaSsoHandle.checkTicket(ticket, "/doLoginByTicket");
        if (loginId != null) {
            StpUtil.login(loginId);
            responseWrapper = ResponseWrapper.markSuccess();
            responseWrapper.setExtra("id", loginId);
            responseWrapper.setExtra("satoken", StpUtil.getTokenValue());
        } else {
            responseWrapper = ResponseWrapper.markInvalidTicketError();
            log.error("ticket无效：" + ticket);
        }
        return responseWrapper;
    }

    /**
     * @description: 获取用户信息
     * @author kuang
     * @date: 2021/11/29
     */
    @GetMapping("/info")
    @ApiOperation(value = "获取用户信息")
    @ResponseBody
    public ResponseWrapper info() {
        ResponseWrapper responseWrapper;
        Integer id = Integer.parseInt(StpUtil.getLoginId().toString());
        if (!userService.userExists(id)) {
            log.error("用户不存在");
            return ResponseWrapper.markUserNotFoundError();
        }

        // 封装携带的参数
        UserDTO userInfo = userService.getInfo(id);
        responseWrapper = ResponseWrapper.markSuccess();
        responseWrapper.setExtra("userInfo", userInfo);
        return responseWrapper;
    }

    /**
     * @description 用户退出的接口
     * @param loginId
     * @param secretkey
     * @author kuang
     * @date 2021/12/10
     */
    @SaCheckRole(
            value = {ADMIN, USER},
            mode = SaMode.OR)
    @GetMapping("/logout")
    @ApiOperation(value = "用户退出（单点注销）")
    @ResponseBody
    public ResponseWrapper logout(
            @RequestParam(required = false) String loginId,
            @RequestParam(required = false) String secretkey) {
        ResponseWrapper responseWrapper;
        if (!secretkey.equals(this.secretkey)) {
            log.error("sso密钥不匹配");
            return ResponseWrapper.markSsoSecretkeyError();
        }
        if (loginId == null) {
            StpUtil.logout();
        } else {
            StpUtil.logout(loginId);
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
            userService.updateStatus(phone, ip);
            responseWrapper = ResponseWrapper.markSuccess();
        } catch (Exception e) {
            responseWrapper = ResponseWrapper.markRegisterError();
            log.error(phone + "注册失败");
            e.printStackTrace();
        }
        return responseWrapper;
    }

    /**
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
            @RequestParam String oldPwd, @RequestParam String newPwd) {
        ResponseWrapper responseWrapper;
        Integer id = Integer.parseInt(StpUtil.getLoginId().toString());
        // 验证旧密码
        if (!userService.loginCheckById(id, oldPwd)) {
            log.warn(id + "旧密码验证失败");
            return ResponseWrapper.markAccountError();
        }
        // 检查新密码
        if (!regexUtil.isLegalPassword(newPwd)) {
            log.error("新密码格式不合法");
            return ResponseWrapper.markPasswordIllegalError();
        }
        try {
            userService.changePwd(id, newPwd);
            responseWrapper = ResponseWrapper.markSuccess();
        } catch (Exception e) {
            log.error(id + "更改密码失败");
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
            @RequestParam String messageCode, @RequestParam String newPwd) {
        ResponseWrapper responseWrapper;
        Integer id = Integer.parseInt(StpUtil.getLoginId().toString());
        String phone = userService.getById(id).getPhone();
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
            userService.changePwd(id, newPwd);
            responseWrapper = ResponseWrapper.markSuccess();
        } catch (Exception e) {
            log.error(id + "更改密码失败");
            responseWrapper = ResponseWrapper.markChangePwdError();
        }
        return responseWrapper;
    }

    /**
     * @description: 用户更改绑定的电话号码
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
            @RequestParam String phone,
            @RequestParam String pwd,
            @RequestParam String messageCode) {
        ResponseWrapper responseWrapper;
        Integer id = Integer.parseInt(StpUtil.getLoginId().toString());
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

    /**
     * @description: 绑定邮箱
     * @param: email
     * @param: pwd
     * @param: emailCode
     * @author kuang
     * @date: 2021/11/29
     */
    @SaCheckRole(
            value = {ADMIN, USER},
            mode = SaMode.OR)
    @PutMapping("/bindEmail")
    @ApiOperation(value = "用户绑定邮箱")
    @ResponseBody
    public ResponseWrapper bindEmail(
            @RequestParam String email, @RequestParam String pwd, @RequestParam String emailCode) {
        ResponseWrapper responseWrapper;
        Integer id = Integer.parseInt(StpUtil.getLoginId().toString());
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

    /**
     * @description: 修改用户名
     * @param: name
     * @author kuang
     * @date: 2021/11/29
     */
    @SaCheckRole(
            value = {ADMIN, USER},
            mode = SaMode.OR)
    @PutMapping("/changeName")
    @ApiOperation(value = "用户修改用户名")
    @ResponseBody
    public ResponseWrapper changeName(@RequestParam String name) {
        Integer id = Integer.parseInt(StpUtil.getLoginId().toString());
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

    /**
     * @description: 获取当前登录用户的登录记录
     * @author kuang
     * @date: 2021/11/30
     */
    @SaCheckRole(
            value = {ADMIN, USER},
            mode = SaMode.OR)
    @GetMapping("/loginRecord")
    @ApiOperation(value = "获取当前登录用户的登录记录")
    @ResponseBody
    public ResponseWrapper listLoginRecord() {
        ResponseWrapper responseWrapper;
        Integer uid = Integer.parseInt(StpUtil.getLoginId().toString());
        List<LoginRecordDTO> records = loginRecordService.listAllByUid(uid);
        responseWrapper = ResponseWrapper.markSuccess();
        responseWrapper.setExtra("loginRecords", records);
        return responseWrapper;
    }

    /**
     * @description: 分页获取当前登录用户的登录记录
     * @param: cur
     * @param: size
     * @author kuang
     * @date: 2021/11/30
     */
    @SaCheckRole(
            value = {ADMIN, USER},
            mode = SaMode.OR)
    @GetMapping("/loginRecord/page")
    @ApiOperation(value = "分页获取当前登录用户的登录记录")
    @ResponseBody
    public ResponseWrapper listLoginRecord(@RequestParam Integer cur, @RequestParam Integer size) {
        ResponseWrapper responseWrapper;
        Integer uid = Integer.parseInt(StpUtil.getLoginId().toString());
        LoginRecordPageDTO loginRecordPageDTO = loginRecordService.listPageByUid(uid, cur, size);
        responseWrapper = ResponseWrapper.markSuccess();
        responseWrapper.setExtra("page", loginRecordPageDTO);
        return responseWrapper;
    }
}
