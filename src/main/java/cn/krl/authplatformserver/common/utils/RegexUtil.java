package cn.krl.authplatformserver.common.utils;

/**
 * @author kuang
 * @description 正则表达式工具类
 * @date 2021/11/12  15:45
 */
public class RegexUtil {
    private static final String PHONE_REGEX = "^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$";
    private static final String EMAIL_REGEX = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\" +
        ".[a-zA-Z]+\\s*$";

    /**
     * @param phone: 被检查的电话
     * @description 检查电话格式是否合法
     * @return: boolean
     * @data 2021/11/12
     */
    public static boolean isLegalPhone(String phone) {
        return phone.matches(PHONE_REGEX);
    }

    ;

    /**
     * @param email: 被检查的邮箱
     * @description 检查邮箱格式是否合法
     * @return: boolean
     * @data 2021/11/12
     */
    public static boolean isLegalEmail(String email) {
        return email.matches(EMAIL_REGEX);
    }

    /**
     * @param str: 被检查的字符
     * @description 检查字符串为null或者为空
     * @return: boolean
     * @data 2021/11/12
     */
    public static boolean isBlank(String str) {
        return str == null || str.isEmpty();
    }
}
