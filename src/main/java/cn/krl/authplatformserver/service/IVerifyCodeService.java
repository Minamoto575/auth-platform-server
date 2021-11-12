package cn.krl.authplatformserver.service;

import cn.krl.authplatformserver.model.pojo.VerifyCode;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author kuang
 * @description 验证码实现层接口
 * @date 2021/11/12  16:57
 */
public interface IVerifyCodeService {
    /**
     * 生成验证码并返回code，将图片写的os中
     *
     * @param width
     * @param height
     * @param os
     * @return
     * @throws IOException
     */
    String generate(int width, int height, OutputStream os) throws IOException;

    /**
     * 生成验证码对象
     *
     * @param width
     * @param height
     * @return
     * @throws IOException
     */
    VerifyCode generate(int width, int height) throws IOException;
}
