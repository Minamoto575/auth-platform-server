package cn.krl.authplatformserver.controller;

import cn.krl.authplatformserver.common.response.ResponseWrapper;
import cn.krl.authplatformserver.common.utils.AliMessageUtil;
import cn.krl.authplatformserver.common.utils.RedisUtil;
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
 * @date 2021/11/12 16:59
 */
@RestController
@RequestMapping("/api/verify")
@Api(tags = "验证码的api")
@Slf4j
public class VerifyCodeController {
    private final String ADMIN = "admin";
    private final String USER = "user";
    @Autowired private IVerifyCodeService verifyCodeService;
    @Autowired private IUserService userService;
    @Autowired private RegexUtil regexUtil;
    @Autowired private AliMessageUtil messageUtil;
    @Autowired private RedisUtil redisUtil;

    /**
     * @param request:
     * @param response:
     * @description 获取图形验证码 登录或者注册请求发送短信用
     * @return: void
     * @date 2021/11/14
     */
    @ApiOperation(value = "验证码（登录或者注册请求发送短信用）")
    @GetMapping("/imageCode/get")
    public void sendImageCode(HttpServletRequest request, HttpServletResponse response) {
        try {
            VerifyCode imageCode = verifyCodeService.generate(80, 28);
            String code = imageCode.getCode();
            log.info("生成登录图形验证码：" + code);
            // 将验证码存入redis
            String sessionId = request.getSession().getId();
            String key = "ImageVerifyCode?sessionId=" + sessionId;
            redisUtil.set(key, code, 10 * 60);
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
     * @param phone: 电话
     * @description 获取短信验证码 注册时使用
     * @return: cn.krl.authplatformserver.common.response.ResponseWrapper
     * @date 2021/11/14
     */
    @ApiOperation(value = "手机短信，用于注册")
    @GetMapping("/messageCode/get")
    public ResponseWrapper sendMessageCode(@RequestParam String phone) {
        ResponseWrapper responseWrapper;
        if (!regexUtil.isLegalPhone(phone)) {
            log.error(phone + "错误的电话格式");
            return ResponseWrapper.markPhoneError();
        }
        if (userService.phoneExists(phone)) {
            log.error(phone + "已被注册");
            return ResponseWrapper.markPhoneExist();
        }
        String messageCode = messageUtil.getRandomCode(6);
        try {
            if (messageUtil.sendMessage(phone, messageCode)) {
                log.info(phone + "短信发送成功" + messageCode);
                responseWrapper = ResponseWrapper.markSuccess();
                String key = "MessageVerifyCode?phone=" + phone;
                redisUtil.set(key, messageCode, 10 * 60);
            } else {
                log.error(phone + "短信发送失败" + messageCode);
                responseWrapper = ResponseWrapper.markMessageCodeGenerateError();
            }
        } catch (Exception e) {
            log.error(phone + "短信发送失败" + messageCode);
            e.printStackTrace();
            responseWrapper = ResponseWrapper.markMessageCodeGenerateError();
        }
        return responseWrapper;
    }
}
