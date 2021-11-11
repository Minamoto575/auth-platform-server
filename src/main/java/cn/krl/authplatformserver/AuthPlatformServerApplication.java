package cn.krl.authplatformserver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class AuthPlatformServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthPlatformServerApplication.class, args);
        log.info("------统一认证平台服务启动成功！------");
    }
}
