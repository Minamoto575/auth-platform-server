package cn.krl.authplatformserver.controller;

import cn.krl.authplatformserver.common.response.ResponseWrapper;
import cn.krl.authplatformserver.common.utils.MessageUtil;
import cn.krl.authplatformserver.common.utils.RegexUtil;
import cn.krl.authplatformserver.model.pojo.VerifyCode;
import cn.krl.authplatformserver.service.IUserService;
import cn.krl.authplatformserver.service.IVerifyCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author kuang
 * @description 验证码控制器
 * @date 2021/11/12 16:59
 */
@RestController
@RequestMapping("/api/verify")
@Api(tags = "验证码的api")
@Slf4j
public class VerifyCodeController {
    @Autowired private IVerifyCodeService verifyCodeService;
    @Autowired private IUserService userService;

    /**
     * @param request:
     * @param response:
     * @description 获取图形验证码 登录或者注册请求发送短信用
     * @return: void
     * @data 2021/11/14
     */
    @ApiOperation(value = "验证码（登录或者注册请求发送短信用）")
    @GetMapping("/imageCode/get")
    public void sendImageCode(HttpServletRequest request, HttpServletResponse response) {
        try {
            VerifyCode imageCode = verifyCodeService.generate(80, 28);
            String code = imageCode.getCode();
            log.info("生成登录图形验证码：" + code);
            request.getSession().setAttribute("ImageVerifyCode", code);
            // 设置响应头
            response.setHeader("Pragma", "no-cache");
            // 设置响应头
            response.setHeader("Cache-Control", "no-cache");
            // 在代理服务器端防止缓冲
            response.setDateHeader("Expires", System.currentTimeMillis() + 300000);
            // 设置响应内容类型
            response.setContentType("image/jpeg");
            response.getOutputStream().write(imageCode.getImgBytes());
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.info("生成登录验证码失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * @description 校验输入的图形验证码是否正确
     * @param input: 输入的四位图形验证码
     * @param session: 会话
     * @return: cn.krl.authplatformserver.common.response.ResponseWrapper
     * @data 2021/11/19
     */
    @ApiOperation(value = "验证图形验证码")
    @GetMapping("/imageCode/check")
    public ResponseWrapper checkImageCode(@RequestParam String input, HttpSession session) {
        try {
            // 从session中获取随机数
            String imageCode = (String) session.getAttribute("ImageVerifyCode");
            if (RegexUtil.isBlank(imageCode)) {
                log.error("session中不存在对应的图形验证码");
                return ResponseWrapper.markImageCodeNotFound();
            }
            if (imageCode.equalsIgnoreCase(input)) {
                log.info("图形验证码校验一致");
                return ResponseWrapper.markSuccess();
            } else {
                log.warn("图形验证码校验不一致 " + input + " " + imageCode);
                return ResponseWrapper.markImageCodeNotConsistent();
            }
        } catch (Exception e) {
            log.error("图形验证码校验失败" + e.getMessage());
            e.printStackTrace();
            return ResponseWrapper.markImageCodeCheckError();
        }
    }

    /**
     * @param phone: 电话
     * @param request:
     * @description 获取短信验证码 注册时使用
     * @return: cn.krl.authplatformserver.common.response.ResponseWrapper
     * @data 2021/11/14
     */
    @ApiOperation(value = "手机短信，用于注册")
    @GetMapping("/messageCode/get")
    public ResponseWrapper sendMessageCode(@RequestParam String phone, HttpServletRequest request) {
        ResponseWrapper responseWrapper;
        if (!RegexUtil.isLegalPhone(phone)) {
            log.error(phone + "错误的电话格式");
            return ResponseWrapper.markPhoneError();
        }
        if (userService.phoneExists(phone)) {
            log.error(phone + "已被注册");
            return ResponseWrapper.markPhoneExist();
        }
        String messageCode = MessageUtil.getRandomCode(6);
        int resultCode = MessageUtil.send(phone, "验证码：" + messageCode + "。您正在注册，请勿告知他人此验证码。");
        String message = MessageUtil.getMessage(resultCode);
        if (resultCode >= 0) {
            log.info("短信发送成功," + message);
            responseWrapper = ResponseWrapper.markSuccess();
            request.getSession().setAttribute("MessageVerifyCode", messageCode);
        } else {
            log.info("短信发送失败," + message);
            responseWrapper = ResponseWrapper.markMessageCodeGenerateError();
        }
        return responseWrapper;
    }
    /**
     * @description 校验输入的短信验证码是否正确
     * @param input: 输入的六位短信验证码
     * @param session: 会话
     * @return: cn.krl.authplatformserver.common.response.ResponseWrapper
     * @data 2021/11/19
     */
    @ApiOperation(value = "验证短信验证码")
    @GetMapping("/messageCode/check")
    public ResponseWrapper checkMessageCode(@RequestParam String input, HttpSession session) {
        try {
            // 从session中获取随机数
            String messageCode = (String) session.getAttribute("MessageVerifyCode");
            if (RegexUtil.isBlank(messageCode)) {
                log.error("session中不存在对应的短信验证码");
                return ResponseWrapper.markMessageCodeNotFound();
            }
            if (messageCode.equalsIgnoreCase(input)) {
                log.info("短信验证码校验一致");
                return ResponseWrapper.markSuccess();
            } else {
                log.warn("短信验证码校验不一致 " + input + " " + messageCode);
                return ResponseWrapper.markMessageCodeNotConsistent();
            }
        } catch (Exception e) {
            log.error("短信验证码校验失败 " + e.getMessage());
            e.printStackTrace();
            return ResponseWrapper.markMessageCodeCheckError();
        }
    }
}
