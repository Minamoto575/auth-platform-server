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

/**
 * @author kuang
 * @description 验证码控制器
 * @date 2021/11/12  16:59
 */
@RestController
@RequestMapping("/verify")
@Api(tags = "验证码的api")
@Slf4j
public class VerifyCodeController {
    @Autowired
    private IVerifyCodeService verifyCodeService;
    @Autowired
    private IUserService userService;

    /**
     * @param request:
     * @param response:
     * @description 获取图形验证码 登录或者注册请求发送短信用
     * @return: void
     * @data 2021/11/14
     */
    @ApiOperation(value = "验证码（登录或者注册请求发送短信用）")
    @GetMapping("/code")
    public void verifyCode(HttpServletRequest request, HttpServletResponse response) {
        ResponseWrapper responseWrapper;
        try {
            //设置长宽
            VerifyCode verifyCode = verifyCodeService.generate(80, 28);
            String code = verifyCode.getCode();
            log.info("生成登录验证码：" + code);
            //将VerifyCode绑定session
            request.getSession().setAttribute("VerifyCode", code);
            //设置响应头
            response.setHeader("Pragma", "no-cache");
            //设置响应头
            response.setHeader("Cache-Control", "no-cache");
            //在代理服务器端防止缓冲
            response.setDateHeader("Expires", System.currentTimeMillis() + 3600000);
            //设置响应内容类型
            response.setContentType("image/jpeg");
            response.getOutputStream().write(verifyCode.getImgBytes());
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.info("获取登录验证码失败：" + e.getMessage());
        }

    }

    /**
     * @param phone: 电话
     * @description 获取短信验证码 注册时使用
     * @return: cn.krl.authplatformserver.common.response.ResponseWrapper
     * @data 2021/11/14
     */
    @ApiOperation(value = "手机短信，用于注册")
    @GetMapping("/message")
    public ResponseWrapper sendMessage(@RequestParam String phone) {
        ResponseWrapper responseWrapper;
        if (!RegexUtil.isLegalPhone(phone)) {
            return ResponseWrapper.markPhoneError();
        }
        if (userService.phoneExists(phone)) {
            return ResponseWrapper.markPhoneExist();
        }
        String messageCode = MessageUtil.getRandomCode(6);
        int resultCode = MessageUtil.send(phone, "验证码：" + messageCode + "。您正在注册，请勿告知他人此验证码。");
        String message = MessageUtil.getMessage(resultCode);
        if (resultCode >= 0) {
            log.info(message);
            responseWrapper = ResponseWrapper.markSuccess();
            responseWrapper.setExtra("messageCode", messageCode);
        } else {
            log.info("短信发送失败" + message);
            responseWrapper = ResponseWrapper.markMessageError();
        }
        return responseWrapper;
    }
}
