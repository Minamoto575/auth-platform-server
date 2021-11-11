package cn.krl.authplatformserver.common.handler;

import cn.krl.authplatformserver.common.response.ResponseWrapper;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author kuang
 * @description 全局异常拦截
 * @date 2021/11/11 17:29
 */
public class GlobalExceptionHandler {
    // 全局异常拦截
    @ExceptionHandler
    public ResponseWrapper handlerException(Exception e) {
        // code=999为未考虑的响应码，具体看异常信息
        e.printStackTrace();
        return ResponseWrapper.markDefault(999, e.getMessage());
    }
}
