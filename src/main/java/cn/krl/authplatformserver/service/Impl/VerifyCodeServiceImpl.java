package cn.krl.authplatformserver.service.Impl;

import cn.krl.authplatformserver.common.utils.ImageCodeUtil;
import cn.krl.authplatformserver.model.pojo.VerifyCode;
import cn.krl.authplatformserver.service.IVerifyCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

/**
 * @author kuang
 * @description
 * @date 2021/11/12 17:01
 */
@Slf4j
@Service
public class VerifyCodeServiceImpl implements IVerifyCodeService {

    private final String[] FONT_TYPES = {
        "\u5b8b\u4f53", "\u65b0\u5b8b\u4f53", "\u9ed1\u4f53", "\u6977\u4f53", "\u96b6\u4e66"
    };
    /** @description 验证码的长度 */
    private final int VALICATE_CODE_LENGTH = 4;

    private final ImageCodeUtil imageCodeUtil;

    public VerifyCodeServiceImpl(ImageCodeUtil imageCodeUtil) {
        this.imageCodeUtil = imageCodeUtil;
    }

    /**
     * 设置背景颜色及大小，干扰线
     *
     * @param graphics
     * @param width
     * @param height
     */
    private void fillBackground(Graphics graphics, int width, int height) {
        // 填充背景
        graphics.setColor(Color.WHITE);
        // 设置矩形坐标x y 为0
        graphics.fillRect(0, 0, width, height);

        // 加入干扰线条
        for (int i = 0; i < 8; i++) {
            // 设置随机颜色算法参数
            graphics.setColor(imageCodeUtil.randomColor(40, 150));
            Random random = new Random();
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int x1 = random.nextInt(width);
            int y1 = random.nextInt(height);
            graphics.drawLine(x, y, x1, y1);
        }
    }

    /**
     * 生成随机字符
     *
     * @param width
     * @param height
     * @param os
     * @return
     * @throws IOException
     */
    @Override
    public String generate(int width, int height, OutputStream os) throws IOException {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();
        fillBackground(graphics, width, height);
        String randomStr = imageCodeUtil.randomString(VALICATE_CODE_LENGTH);
        createCharacter(graphics, randomStr);
        graphics.dispose();
        // 设置JPEG格式
        ImageIO.write(image, "JPEG", os);
        return randomStr;
    }

    /**
     * 验证码生成
     *
     * @param width
     * @param height
     * @return
     */
    @Override
    public VerifyCode generate(int width, int height) {
        VerifyCode verifyCode = null;
        try (
        // 将流的初始化放到这里就不需要手动关闭流
        ByteArrayOutputStream baos = new ByteArrayOutputStream(); ) {
            String code = generate(width, height, baos);
            verifyCode = new VerifyCode();
            verifyCode.setCode(code);
            verifyCode.setImgBytes(baos.toByteArray());
        } catch (IOException e) {
            log.error("生成登录验证码出错");
            e.printStackTrace();
            verifyCode = null;
        }
        return verifyCode;
    }

    /**
     * 设置字符颜色大小
     *
     * @param g
     * @param randomStr
     */
    private void createCharacter(Graphics g, String randomStr) {
        char[] charArray = randomStr.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            // 设置RGB颜色算法参数
            g.setColor(
                    new Color(
                            50 + imageCodeUtil.nextInt(100),
                            50 + imageCodeUtil.nextInt(100),
                            50 + imageCodeUtil.nextInt(100)));
            // 设置字体大小，类型
            g.setFont(
                    new Font(FONT_TYPES[imageCodeUtil.nextInt(FONT_TYPES.length)], Font.BOLD, 26));
            // 设置x y 坐标
            g.drawString(String.valueOf(charArray[i]), 15 * i + 5, 19 + imageCodeUtil.nextInt(8));
        }
    }
}
