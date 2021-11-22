package cn.krl.authplatformserver.common.utils;

import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Random;

/**
 * @author kuang
 * @description 图像验证码生成的随机工具类
 * @date 2021/11/12 17:04
 */
@Component
public class RandomUtil extends org.apache.commons.lang3.RandomUtils {

    private static final char[] CODE_SEQ = {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T',
        'U', 'V', 'W', 'X', 'Y', 'Z', '2', '3', '4', '5', '6', '7', '8', '9'
    };

    private final char[] NUMBER_ARRAY = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    private Random random = new Random();

    public String randomString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(String.valueOf(CODE_SEQ[random.nextInt(CODE_SEQ.length)]));
        }
        return sb.toString();
    }

    public String randomNumberString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(String.valueOf(NUMBER_ARRAY[random.nextInt(NUMBER_ARRAY.length)]));
        }
        return sb.toString();
    }

    public Color randomColor(int fc, int bc) {
        int f = fc;
        int b = bc;
        Random random = new Random();
        if (f > 255) {
            f = 255;
        }
        if (b > 255) {
            b = 255;
        }
        return new Color(
                f + random.nextInt(b - f), f + random.nextInt(b - f), f + random.nextInt(b - f));
    }

    public int nextInt(int bound) {
        return random.nextInt(bound);
    }
}
