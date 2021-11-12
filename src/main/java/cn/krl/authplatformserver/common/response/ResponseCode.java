package cn.krl.authplatformserver.common.response;

/**
 * @description 自定义的响应码
 * @author kuang
 * @data 2021/10/24
 */
public enum ResponseCode {

    /** 200 ~ 399 判定为正常、合法的操作 */
    SUCCESS(200, "操作成功"),
    NODATA(201, "操作成功，但无记录"),
    DATA_EXISTED(202, "操作成功，但记录已存在"),
    REDIRECT(302, "重定向"),

    /** 400 ~ 599 判定为前端可自定义异常处理的普通异常 */
    FAILED(400, "操作失败"),
    ACCOUNT_ERROR(401, "账户或密码错误"),
    ADMIN_ACCOUNT_ERROR(402, "超级管理员必须使用超级狗登录"),
    PARAMS_ERROR(403, "请求参数为空或格式错误"),
    API_NOT_EXISTS(404, "请求的接口不存在"),
    URL_ERROR(405, "无效的URL链接"),
    UNKNOWN_IP(406, "非法IP请求"),
    PHONE_EXIST(407, "该电话已被使用"),
    PASSWORD_ERROR(408, "密码错误"),
    EMAIL_EXIST(409, "该邮箱已被使用"),
    EMAIL_ERROR(410, "非法的邮箱格式"),
    PHONE_ERROR(411, "非法的电话格式"),
    SYSTEM_ERROR(500, "系统异常"),
    /**
     * 900以上，判定为前端同一处理的异常
     */
    API_NOT_PER(900, "没有该接口的访问权限");
    private int code;
    private String msg;

    ResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
