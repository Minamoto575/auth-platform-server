package cn.krl.authplatformserver.service.Impl;

import cn.krl.authplatformserver.common.utils.RedisUtil;
import cn.krl.authplatformserver.service.IEmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Random;

/**
 * @author kuang
 * @description 邮箱服务层实现类
 * @date 2021/11/25 10:46
 */
@Slf4j
@Service
public class EmailServiceImpl implements IEmailService {

    private final JavaMailSender mailSender;
    private final RedisUtil redisUtil;

    @Value("${spring.mail.username}")
    private String username;

    public EmailServiceImpl(JavaMailSender mailSender, RedisUtil redisUtil) {
        super();
        this.mailSender = mailSender;
        this.redisUtil = redisUtil;
    }

    /**
     * @param theRecipient 收件人
     * @param subject 主题
     * @param content 内容
     */
    @Override
    public void sendSimpleMail(String theRecipient, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(theRecipient);
        message.setSubject(subject);
        message.setText(content);
        message.setFrom(username);
        mailSender.send(message);
    }

    /**
     * 【发送HTML文件】HTML文件就是指在文件内容中可以添加<html>等标签，收件人收到邮件后显示内容也和网页一样，比较丰富多彩
     *
     * @param theRecipient 收件人
     * @param subject 主题
     * @param content 内容（可以包含<html>等标签）
     */
    @Override
    public void sendHtmlMail(String theRecipient, String subject, String content) {

        // 使用MimeMessage，MIME协议
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper;
        // MimeMessageHelper帮助我们设置更丰富的内容
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(username);
            helper.setTo(theRecipient);
            helper.setSubject(subject);
            // true代表支持html
            helper.setText(content, true);
            mailSender.send(message);
        } catch (Exception e) {
            log.error("发送html邮件失败");
        }
    }

    /**
     * 发送带附件的邮件 带附件的邮件在HTML邮件上添加一些参数即可
     *
     * @param theRecipient 收件人
     * @param subject 主题
     * @param content 内容
     * @param filePath 附件路径
     */
    @Override
    public void sendAttachmentMail(
            String theRecipient, String subject, String content, String filePath) {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true);
            // true代表支持多组件，如附件，图片等
            helper.setFrom(username);
            helper.setTo(theRecipient);
            helper.setSubject(subject);
            helper.setText(content, true);
            FileSystemResource file = new FileSystemResource(new File(filePath));
            String fileName = file.getFilename();
            helper.addAttachment(fileName, file); // 添加附件，可多次调用该方法添加多个附件
            mailSender.send(message);
        } catch (Exception e) {
            log.error("发送html邮件失败");
        }
    }

    /**
     * 【发送带图片的邮件】带图片即在正文中使用<img>标签，并设置我们需要发送的图片，也是在HTML基础上添加一些参数
     *
     * @param theRecipient 收件人
     * @param subject 主题
     * @param content 文本
     * @param rscPath 图片路径
     * @param rscId 图片ID，用于在<img>标签中使用，从而显示图片
     */
    @Override
    public void sendInlineResourceMail(
            String theRecipient, String subject, String content, String rscPath, String rscId) {
        // logger.info("发送带图片邮件开始：{},{},{},{},{}", to, subject, content, rscPath, rscId);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(username);
            helper.setTo(theRecipient);
            helper.setSubject(subject);
            helper.setText(content, true);
            FileSystemResource res = new FileSystemResource(new File(rscPath));
            helper.addInline(rscId, res); // 重复使用添加多个图片
            mailSender.send(message);
            log.info("发送带图片邮件成功");
        } catch (Exception e) {
            log.error("发送带图片邮件失败", e);
        }
    }

    /**
     * 随机生成6位验证码
     *
     * @return
     */
    @Override
    public String getRandomCode(Integer length) {
        Random random = new Random();
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < length; i++) {
            result.append(random.nextInt(10));
        }
        return result.toString();
    }

    @Override
    public boolean checkEmailCode(String email, String input) {
        // 从session中获取随机数
        String key = "EmailVerifyCode?email=" + email;
        if (!redisUtil.hasKey(key)) {
            log.error("短信验证码过期");
            return false;
        }
        String emailCode = (String) redisUtil.get(key);
        if (emailCode.equalsIgnoreCase(input)) {
            redisUtil.delete(key);
            log.info("邮箱验证码校验一致");
            return true;
        } else {
            log.warn("邮箱验证码校验不一致 " + input + " " + emailCode);
            return false;
        }
    }
}
