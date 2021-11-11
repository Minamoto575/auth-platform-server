package cn.krl.authplatformserver.controller;

/**
 * @author kuang
 * @description
 * @date 2021/11/11 14:18
 */
import cn.dev33.satoken.config.SaTokenConfig;
import cn.dev33.satoken.sso.SaSsoHandle;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.krl.authplatformserver.model.vo.User;
import cn.krl.authplatformserver.service.IUserService;
import com.ejlchina.okhttps.OkHttps;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Sa-Token-SSO Server端 Controller */
@RestController
@Api(tags = "单点登录")
public class SsoServerController {

    @Autowired private IUserService userService;

    /*
     * SSO-Server端：处理所有SSO相关请求 (下面的章节我们会详细列出开放的接口)
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
        //         () -> {
        //             String msg =
        //                     "当前会话在SSO-Server端尚未登录，请先访问"
        //                             + "<a href='/sso/doLogin?name=sa&pwd=123456' target='_blank'>
        // doLogin登录 </a>"
        //                             + "进行登录之后，刷新页面开始授权";
        //             return msg;
        //         });

        // 配置：登录处理函数
        cfg.sso.setDoLoginHandle(
                (name, pwd) -> {
                    System.out.println(name + " " + pwd);
                    // 此处仅做模拟登录，真实环境应该查询数据进行登录
                    if (userService.loginCheck(name, pwd)) {
                        User user = userService.getUserByPhone(name);
                        StpUtil.login(user.getId());
                        return SaResult.ok("登录成功！").setData(StpUtil.getTokenValue());
                    }
                    return SaResult.error("登录失败！");
                });

        // 配置 Http 请求处理器 （在模式三的单点注销功能下用到，如不需要可以注释掉）
        cfg.sso.setSendHttp(
                url -> {
                    return OkHttps.sync(url).get().getBody().toString();
                });
    }

    private class UserService {}
}
