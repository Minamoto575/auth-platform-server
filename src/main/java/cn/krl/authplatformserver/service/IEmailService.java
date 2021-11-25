package cn.krl.authplatformserver.service;

/**
 * @author kuang
 * @description 邮箱服务层接口
 * @date 2021/11/25 10:45
 */
public interface IEmailService {
    /**
     * 发送普通文本邮件
     *
     * @param theRecipient 收件人
     * @param subject 主题
     * @param content 内容
     */
    void sendSimpleMail(String theRecipient, String subject, String content);
    /**
     * 发送HTML邮件
     *
     * @param theRecipient 收件人
     * @param subject 主题
     * @param content 内容（可以包含<html>等标签）
     */
    void sendHtmlMail(String theRecipient, String subject, String content);
    /**
     * 发送带附件的邮件
     *
     * @param theRecipient 收件人
     * @param subject 主题
     * @param content 内容
     * @param filePath 附件路径
     */
    void sendAttachmentMail(String theRecipient, String subject, String content, String filePath);
    /**
     * 发送带图片的邮件
     *
     * @param theRecipient 收件人
     * @param subject 主题
     * @param content 文本
     * @param rscPath 图片路径
     * @param rscId 图片ID，用于在<img>标签中使用，从而显示图片
     */
    void sendInlineResourceMail(
            String theRecipient, String subject, String content, String rscPath, String rscId);

    /**
     * @description: 随机生成邮箱验证码
     * @param: length 长度
     * @author kuang
     * @date: 2021/11/25
     */
    String getRandomCode(Integer length);

    /**
     * @description: 检查邮箱验证码
     * @param: email 邮箱
     * @param: code 验证码
     * @author kuang
     * @date: 2021/11/25
     */
    boolean checkEmailCode(String email, String inp);
}
