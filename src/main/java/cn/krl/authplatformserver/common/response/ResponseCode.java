package cn.krl.authplatformserver.common.response;

/**
 * @description 自定义的响应码
 * @author kuang
 * @date 2021/10/24
 */
public enum ResponseCode {

    /** 200 ~ 399 判定为正常、合法的操作 */
    SUCCESS(200, "操作成功"),
    USER_ISLOGIN(200, "用户已登录"),
    NODATA(201, "操作成功，但无记录"),
    DATA_EXISTED(202, "操作成功，但记录已存在"),
    IMAGECODE_NOT_CONSISTENT(203, "图形验证码检查成功，但校验不一致"),
    MESSAGECODE_NOT_CONSISTENT(204, "短信验证码检查成功，但校验不一致"),
    REDIRECT(302, "重定向"),

    /** 400 ~ 599 判定为前端可自定义异常处理的普通异常 */
    FAILED(400, "操作失败"),
    ACCOUNT_ERROR(401, "账户或密码错误"),
    NOTLOGIN_ERROR(402, "用户未登录"),
    PARAMS_ERROR(403, "请求参数为空或格式错误"),
    API_NOT_EXISTS(404, "请求的接口不存在"),
    URL_ERROR(405, "无效的URL链接"),
    UNKNOWN_IP(406, "非法IP请求"),
    PHONE_EXIST(407, "该电话已被使用"),
    PASSWORD_ERROR(408, "密码错误"),
    EMAIL_EXIST(409, "该邮箱已被使用"),
    EMAIL_ERROR(410, "非法的邮箱格式"),
    PHONE_ERROR(411, "非法的电话格式"),
    IMAGECODE_GENERATE_ERROR(412, "图形验证码生成失败"),
    IMAGECODE_NOT_FOUND(412, "session中未找到图形验证码"),
    IMAGECODE_CHECK_ERROR(412, "图形验证码检查失败"),
    CHANGEPHONE_ERROR(413, "修改电话失败"),
    CHANGEPWD_ERROR(414, "修改密码失败"),
    UPDATEUSER_ERROR(415, "更新用户失败"),
    MESSAGECODE_GENERATE_ERROR(416, "短信验证码发送失败"),
    MESSAGECODE_NOT_FOUND(416, "session中未找到短信验证码"),
    MESSAGECODE_CHECK_ERROR(416, "短信验证码检查失败"),
    REGISTER_ERROR(417, "注册失败"),
    UPLOAD_IMAGE_ERROR(418, "上传图片失败"),
    SUBSYSTEM_REGISTER_ERROR(419, "子系统记录注册失败"),
    SUBSYSTEM_UPDATE_ERROR(420, "子系统记录更新失败"),
    SUBSYSTEM_NOT_FOUND_ERROR(421, "该子系统不存在"),
    USER_NOT_FOUND_ERROR(422, "该用户不存在"),
    SYSTEM_ERROR(500, "系统异常"),
    /** 900以上，判定为前端同一处理的异常 */
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
