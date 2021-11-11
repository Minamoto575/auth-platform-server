package cn.krl.authplatformserver.common.utils;

import java.util.Random;

/**
 * @author kuang
 * @description 随机生成盐工具
 * @date 2021/11/11  11:19
 */
public class SaltUtil {
    /**
     * 生成长度为n的随机字符串
     *
     * @param n 长度n
     * @return
     */
    public static String getSalt(int n) {
        char[] chars =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz01234567890!@#$%^&*()"
                .toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            char c = chars[new Random().nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }
}
