package cn.krl.authplatformserver.controller;

/**
 * @author kuang
 * @description
 * @date 2021/11/11 14:18
 */
import cn.dev33.satoken.config.SaTokenConfig;
import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.sso.SaSsoHandle;
import cn.dev33.satoken.stp.StpUtil;
import cn.krl.authplatformserver.common.response.ResponseWrapper;
import cn.krl.authplatformserver.model.po.User;
import cn.krl.authplatformserver.service.IUserService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Sa-Token-SSO Server端 Controller */
@RestController
@Api(tags = "单点登录")
@Slf4j
public class SsoServerController {

    @Autowired private IUserService userService;

    /*
     * SSO-Server端：处理所有SSO相关请求
     */
    @RequestMapping("/sso/*")
    public Object ssoRequest() {
        return SaSsoHandle.serverRequest();
    }

    /** 配置SSO相关参数 */
    @Autowired
    private void configSso(SaTokenConfig cfg) {
        // 配置：未登录时返回的View
        // cfg.sso.setNotLoginView(
        //     () -> {
        //         String msg =
        //             "当前会话在SSO-Server端尚未登录，请先访问"
        //                 + "<a href='/sso/doLogin?phone=17388888888&pwd=123456' target='_blank'> doLogin登录</a > "
        //                 + "进行登录之后，刷新页面开始授权";
        //         return msg;
        //     });

        // 配置：登录处理函数
        cfg.sso.setDoLoginHandle(
            (name, pwd) -> {
                ResponseWrapper responseWrapper;
                String phone = SaHolder.getRequest().getParam("phone");
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
            });

        // http://{host}:{port}/sso/logout
        // 参数	是否必填	说明
        // loginId	否	要注销的账号id
        // secretkey	否	接口通信秘钥
        // back	否	注销成功后的重定向地址


        // 配置 Http 请求处理器 （在模式三的单点注销功能下用到，如不需要可以注释掉）
        // cfg.sso.setSendHttp(
        //         url -> {
        //             return OkHttps.sync(url).get().getBody().toString();
        //         });
    }

}
