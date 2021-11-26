package cn.krl.authplatformserver.common.utils;

import org.springframework.stereotype.Component;

/**
 * @author kuang
 * @description 正则表达式工具类
 * @date 2021/11/12 15:45
 */
@Component
public class RegexUtil {
    private final String PHONE_REGEX = "^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$";
    private final String EMAIL_REGEX =
            "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\" + ".[a-zA-Z]+\\s*$";
    private final String URL_REGEX =
            "(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]";
    private final String USERNAME_REGEX = "^[a-zA-Z0-9_]{4,12}$";
    private final String PASSWORD_REGEX = "^(?=.*\\d)(?=.*[a-zA-Z]).{8,20}$";

    /**
     * @param phone: 被检查的电话
     * @description 检查电话格式是否合法
     * @return: boolean
     * @date 2021/11/12
     */
    public boolean isLegalPhone(String phone) {
        return phone.matches(PHONE_REGEX);
    }

    /**
     * @param email: 被检查的邮箱
     * @description 检查邮箱格式是否合法
     * @return: boolean
     * @date 2021/11/12
     */
    public boolean isLegalEmail(String email) {
        return email.matches(EMAIL_REGEX);
    }

    /**
     * @description: 检查是否是合法的url
     * @param: url 输入的url
     * @author kuang
     * @date: 2021/11/26
     */
    public boolean isLegalUrl(String url) {
        return url.matches(URL_REGEX);
    }

    /**
     * @description: 检查用户名是否合法
     * @param: username 输出的用户名
     * @author kuang
     * @date: 2021/11/26
     */
    public boolean isLegalUsername(String username) {
        return username.matches(USERNAME_REGEX);
    }

    /**
     * @description: 检查密码是否合法
     * @param: password 输入的密码
     * @author kuang
     * @date: 2021/11/26
     */
    public boolean isLegalPassword(String password) {
        return password.matches(password);
    }
    /**
     * @param str: 被检查的字符
     * @description 检查字符串为null或者为空
     * @return: boolean
     * @date 2021/11/12
     */
    public boolean isBlank(String str) {
        return str == null || str.isEmpty();
    }
}
