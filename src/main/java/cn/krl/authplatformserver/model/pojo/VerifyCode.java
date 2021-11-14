package cn.krl.authplatformserver.model.pojo;

import lombok.Data;

/**
 * @author kuang
 * @description 图形验证码
 * @date 2021/11/12  16:56
 */
@Data
public class VerifyCode {
    private String code;

    private byte[] imgBytes;

    private long expireTime;
}
