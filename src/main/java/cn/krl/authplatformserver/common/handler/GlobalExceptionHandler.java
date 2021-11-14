package cn.krl.authplatformserver.common.handler;

import cn.krl.authplatformserver.common.response.ResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author kuang
 * @description 全局异常捕获
 * @date 2021/11/11 17:29
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    // 全局异常捕获
    @ExceptionHandler
    @ResponseBody
    public ResponseWrapper handlerException(Exception e) {
        // code=999为未考虑的响应码，具体看异常信息
        log.error(e.getMessage());
        e.printStackTrace();
        return ResponseWrapper.markDefault(999, e.getMessage());
    }
}
