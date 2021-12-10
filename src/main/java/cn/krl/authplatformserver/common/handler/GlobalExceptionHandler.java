package cn.krl.authplatformserver.common.handler;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.krl.authplatformserver.common.response.ResponseWrapper;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author kuang
 * @description 全局异常拦截
 * @date 2021/12/9 10:33
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    // 全局异常拦截（拦截项目中的所有异常）
    @ResponseBody
    @ExceptionHandler
    public ResponseWrapper handlerException(
            Exception e, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // 打印堆栈，以供调试
        System.out.println("----------全局异常----------");
        e.printStackTrace();

        // 不同异常返回不同状态码
        ResponseWrapper rw;
        if (e instanceof NotLoginException) { // 如果是未登录异常 402
            rw = ResponseWrapper.markNotLoginError();
        } else if (e instanceof NotRoleException) { // 如果是角色异常 429
            rw = ResponseWrapper.markNoRoleError();
        } else if (e instanceof NotPermissionException) { // 如果是权限异常 430
            rw = ResponseWrapper.markNoPermissionError();
        } else { // 系统异常 返回500
            rw = ResponseWrapper.markSystemError();
        }

        return rw;
    }
}
