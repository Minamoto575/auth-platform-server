package cn.krl.authplatformserver.controller;

import cn.krl.authplatformserver.model.pojo.VerifyCode;
import cn.krl.authplatformserver.service.IVerifyCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author kuang
 * @description 验证码控制器
 * @date 2021/11/12  16:59
 */
@RestController
@RequestMapping("/verifyCode")
@Api(tags = "验证码的api")
@Slf4j
public class VerifyCodeController {
    @Autowired
    private IVerifyCodeService verifyCodeService;

    @ApiOperation(value = "验证码")
    @GetMapping("/")
    public void verifyCode(HttpServletRequest request, HttpServletResponse response) {
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
        } catch (IOException e) {
            log.info("获取登录验证码失败：" + e.getMessage());
        }
    }
}
