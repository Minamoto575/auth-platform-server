package cn.krl.authplatformserver.controller;

import cn.dev33.satoken.annotation.SaCheckSafe;
import cn.dev33.satoken.sso.SaSsoUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.krl.authplatformserver.common.response.ResponseWrapper;
import cn.krl.authplatformserver.common.utils.AliMessageUtil;
import cn.krl.authplatformserver.common.utils.ImageCodeUtil;
import cn.krl.authplatformserver.common.utils.RegexUtil;
import cn.krl.authplatformserver.model.dto.UserDTO;
import cn.krl.authplatformserver.model.dto.UserRegisterDTO;
import cn.krl.authplatformserver.model.dto.UserUpdateDTO;
import cn.krl.authplatformserver.model.po.User;
import cn.krl.authplatformserver.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@Api(tags = "用户的api")
@Slf4j
@CrossOrigin
public class UserController {
    @Autowired private IUserService userService;
    @Autowired private RegexUtil regexUtil;
    @Autowired private AliMessageUtil aliMessageUtil;
    @Autowired private ImageCodeUtil imageCodeUtil;

    /**
     * @description 判断用户是否已经登录
     * @return: cn.krl.authplatformserver.common.response.ResponseWrapper
     * @date 2021/11/16
     */
    @GetMapping("/auth")
    @ApiOperation("用户登录状态查询")
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
    @ApiOperation("用户登录")
    @ResponseBody
    public ResponseWrapper login(
            HttpServletRequest request,
            @RequestParam String phone,
            @RequestParam String pwd,
            @RequestParam String imageCode,
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
            responseWrapper = ResponseWrapper.markSuccess();
            responseWrapper.setExtra("token", StpUtil.getTokenValue());
            String ticket = SaSsoUtil.createTicket(user.getId());
            responseWrapper.setExtra("ticket", ticket);
            responseWrapper.setExtra("phone", phone);
            responseWrapper.setExtra("uid", user.getId());
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
    @GetMapping("/logout")
    @ApiOperation("用户退出")
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
    @ApiOperation("用户注册")
    @ResponseBody
    public ResponseWrapper registerUser(@RequestBody @Validated UserRegisterDTO userRegisterDTO) {
        ResponseWrapper responseWrapper;
        String messageCode = userRegisterDTO.getMessageCode();
        String phone = userRegisterDTO.getPhone();
        String email = userRegisterDTO.getEmail();

        // 短信验证码检查
        boolean check = aliMessageUtil.checkMessageCode(messageCode, phone);
        if (!check) {
            return ResponseWrapper.markMessageCodeCheckError();
        }

        if (userService.phoneExists(phone)) {
            log.warn(phone + "电话已被注册，注册失败！");
            return ResponseWrapper.markPhoneExist();
        }
        if (userService.emailExists(email)) {
            log.warn(email + "邮箱已被使用，注册失败！");
            return ResponseWrapper.markEmailExist();
        }

        // 注册
        try {
            userService.registerUser(userRegisterDTO);
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
     * @date 2021/11/14
     */
    @PutMapping("/update")
    @ApiOperation("用户更新")
    @ResponseBody
    public ResponseWrapper updateUser(@RequestBody @Validated UserUpdateDTO updateDTO) {
        Integer id = updateDTO.getId();
        String phone = updateDTO.getPhone();
        String email = updateDTO.getEmail();
        if (id == null || userService.getById(id) == null) {
            log.error("不存在用户id=" + id);
            return ResponseWrapper.markUserNotFoundError();
        }
        if (!regexUtil.isBlank(phone) && !regexUtil.isLegalPhone(phone)) {
            log.warn("用户:" + updateDTO.getId() + "更新失败,错误的电话格式");
            return ResponseWrapper.markPhoneError();
        }
        if (!regexUtil.isBlank(email) && !regexUtil.isLegalPhone(email)) {
            log.warn("用户:" + updateDTO.getId() + "更新失败,错误的邮箱格式");
            return ResponseWrapper.markEmailError();
        }

        // 更新用户
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
     * @date 2021/11/14
     */
    @PutMapping("/changePwd/oldPwd")
    @ApiOperation("用户更改密码(通过旧密码修改)")
    @ResponseBody
    public ResponseWrapper changePwdByOldPwd(
            @RequestParam String phone, @RequestParam String oldPwd, @RequestParam String newPwd) {
        ResponseWrapper responseWrapper;
        if (!userService.loginCheckByPhone(phone, oldPwd)) {
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

    /**
     * @description: 用户通过绑定的电话号码和验证码修改登录密码
     * @param: phone 电话
     * @param: messageCode 验证码
     * @param: newPwd 新密码
     * @author kuang
     * @date: 2021/11/23
     */
    @PutMapping("/changePwd/phone")
    @ApiOperation("用户更改密码(通过电话号码和验证码修改)")
    @ResponseBody
    public ResponseWrapper changePwdByPhone(
            @RequestParam String phone,
            @RequestParam String messageCode,
            @RequestParam String newPwd) {
        ResponseWrapper responseWrapper;
        boolean checkMessageCode = aliMessageUtil.checkMessageCode(messageCode, phone);
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
    @SaCheckSafe
    @PutMapping("/changePhone")
    @ApiOperation("用户更改电话")
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
    public ResponseWrapper listPage(@RequestParam int cur, @RequestParam int size) {
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
