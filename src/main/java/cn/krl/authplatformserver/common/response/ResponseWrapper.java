package cn.krl.authplatformserver.common.response;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @description 响应包装类 返回给前端
 * @author kuang
 * @date 2021/10/24
 */
@Data
@ApiModel(value = "响应")
@AllArgsConstructor
public class ResponseWrapper {

    private int code;
    private String msg;
    private Map<String, Object> extra;

    /** 私有的构造函数 */
    private ResponseWrapper() {}

    /** 自定义返回结果 */
    public static ResponseWrapper markDefault(int code, String msg) {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(code);
        responseWrapper.setMsg(msg);
        return responseWrapper;
    }

    /** 查询成功且有数据 */
    public static ResponseWrapper markSuccess() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.SUCCESS.getCode());
        responseWrapper.setMsg(ResponseCode.SUCCESS.getMsg());
        return responseWrapper;
    }

    /** 操作成功，但无记录 */
    public static ResponseWrapper markNoData() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.NODATA.getCode());
        responseWrapper.setMsg(ResponseCode.NODATA.getMsg());
        return responseWrapper;
    }

    /** 操作成功，但记录已存在 */
    public static ResponseWrapper markDataExisted() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.DATA_EXISTED.getCode());
        responseWrapper.setMsg(ResponseCode.DATA_EXISTED.getMsg());
        return responseWrapper;
    }
    /** 操作成功，但记录已存在 */
    public static ResponseWrapper markUserIsLogin() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.USER_ISLOGIN.getCode());
        responseWrapper.setMsg(ResponseCode.USER_ISLOGIN.getMsg());
        return responseWrapper;
    }
    /** 图形验证码检查成功 但是不一致 */
    public static ResponseWrapper markImageCodeNotConsistent() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.IMAGECODE_NOT_CONSISTENT.getCode());
        responseWrapper.setMsg(ResponseCode.IMAGECODE_NOT_CONSISTENT.getMsg());
        return responseWrapper;
    }
    /** 短信验证码检查成功 但是不一致 */
    public static ResponseWrapper markMessageCodeNotConsistent() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.MESSAGECODE_NOT_CONSISTENT.getCode());
        responseWrapper.setMsg(ResponseCode.MESSAGECODE_NOT_CONSISTENT.getMsg());
        return responseWrapper;
    }
    /** 重定向 */
    public static ResponseWrapper markRedirect() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.REDIRECT.getCode());
        responseWrapper.setMsg(ResponseCode.REDIRECT.getMsg());
        return responseWrapper;
    }

    /** 参数为空或者参数格式错误 */
    public static ResponseWrapper markParamError() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.PARAMS_ERROR.getCode());
        responseWrapper.setMsg(ResponseCode.PARAMS_ERROR.getMsg());
        return responseWrapper;
    }
    /** 参数为空或者参数格式错误 */
    public static ResponseWrapper markNotLoginError() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.NOTLOGIN_ERROR.getCode());
        responseWrapper.setMsg(ResponseCode.NOTLOGIN_ERROR.getMsg());
        return responseWrapper;
    }

    /** 查询失败 */
    public static ResponseWrapper markError() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.FAILED.getCode());
        responseWrapper.setMsg(ResponseCode.FAILED.getMsg());
        return responseWrapper;
    }

    /** 无效URL链接 */
    public static ResponseWrapper markUrlError() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.URL_ERROR.getCode());
        responseWrapper.setMsg(ResponseCode.URL_ERROR.getMsg());
        return responseWrapper;
    }

    /** 系统异常 */
    public static ResponseWrapper markSystemError() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.SYSTEM_ERROR.getCode());
        responseWrapper.setMsg(ResponseCode.SYSTEM_ERROR.getMsg());
        return responseWrapper;
    }

    /** 没有该API访问权限 */
    public static ResponseWrapper markApiNotPermission() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.API_NOT_PER.getCode());
        responseWrapper.setMsg(ResponseCode.API_NOT_PER.getMsg());
        return responseWrapper;
    }

    /** 账号或者密码错误 */
    public static ResponseWrapper markAccountError() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.ACCOUNT_ERROR.getCode());
        responseWrapper.setMsg(ResponseCode.ACCOUNT_ERROR.getMsg());
        return responseWrapper;
    }

    /** 电话已被使用 */
    public static ResponseWrapper markPhoneExist() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.PHONE_EXIST.getCode());
        responseWrapper.setMsg(ResponseCode.PHONE_EXIST.getMsg());
        return responseWrapper;
    }

    /** 邮箱已被使用 */
    public static ResponseWrapper markEmailExist() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.EMAIL_EXIST.getCode());
        responseWrapper.setMsg(ResponseCode.EMAIL_EXIST.getMsg());
        return responseWrapper;
    }

    /** 非法的电话格式 */
    public static ResponseWrapper markPhoneError() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.PHONE_ERROR.getCode());
        responseWrapper.setMsg(ResponseCode.PHONE_ERROR.getMsg());
        return responseWrapper;
    }

    /** 非法的邮箱格式 */
    public static ResponseWrapper markEmailError() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.EMAIL_ERROR.getCode());
        responseWrapper.setMsg(ResponseCode.EMAIL_ERROR.getMsg());
        return responseWrapper;
    }

    /** 图形验证码生成失败 */
    public static ResponseWrapper markImageCodeGenerateError() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.IMAGECODE_GENERATE_ERROR.getCode());
        responseWrapper.setMsg(ResponseCode.IMAGECODE_GENERATE_ERROR.getMsg());
        return responseWrapper;
    }
    /** session中未找到图形验证码 */
    public static ResponseWrapper markImageCodeNotFound() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.IMAGECODE_NOT_FOUND.getCode());
        responseWrapper.setMsg(ResponseCode.IMAGECODE_NOT_FOUND.getMsg());
        return responseWrapper;
    }
    /** 图形验证码检查失败 */
    public static ResponseWrapper markImageCodeCheckError() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.IMAGECODE_CHECK_ERROR.getCode());
        responseWrapper.setMsg(ResponseCode.IMAGECODE_CHECK_ERROR.getMsg());
        return responseWrapper;
    }

    /** 短信验证码生成失败 */
    public static ResponseWrapper markMessageCodeGenerateError() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.MESSAGECODE_GENERATE_ERROR.getCode());
        responseWrapper.setMsg(ResponseCode.MESSAGECODE_GENERATE_ERROR.getMsg());
        return responseWrapper;
    }
    /** session中未找到短信验证码 */
    public static ResponseWrapper markMessageCodeNotFound() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.MESSAGECODE_NOT_FOUND.getCode());
        responseWrapper.setMsg(ResponseCode.MESSAGECODE_NOT_FOUND.getMsg());
        return responseWrapper;
    }
    /** 短信验证码检查失败 */
    public static ResponseWrapper markMessageCodeCheckError() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.MESSAGECODE_CHECK_ERROR.getCode());
        responseWrapper.setMsg(ResponseCode.MESSAGECODE_CHECK_ERROR.getMsg());
        return responseWrapper;
    }

    /** 密码错误 */
    public static ResponseWrapper markPasswordError() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.PASSWORD_ERROR.getCode());
        responseWrapper.setMsg(ResponseCode.PASSWORD_ERROR.getMsg());
        return responseWrapper;
    }

    /** 修改密码失败 */
    public static ResponseWrapper markChangePwdError() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.CHANGEPWD_ERROR.getCode());
        responseWrapper.setMsg(ResponseCode.CHANGEPWD_ERROR.getMsg());
        return responseWrapper;
    }

    /** 修改电话失败 */
    public static ResponseWrapper markChangePhoneError() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.CHANGEPHONE_ERROR.getCode());
        responseWrapper.setMsg(ResponseCode.CHANGEPHONE_ERROR.getMsg());
        return responseWrapper;
    }

    /** 更新用户失败 */
    public static ResponseWrapper markUpdateUserError() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.UPDATEUSER_ERROR.getCode());
        responseWrapper.setMsg(ResponseCode.UPDATEUSER_ERROR.getMsg());
        return responseWrapper;
    }

    /** 注册·失败 */
    public static ResponseWrapper markRegisterError() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.REGISTER_ERROR.getCode());
        responseWrapper.setMsg(ResponseCode.REGISTER_ERROR.getMsg());
        return responseWrapper;
    }

    /** 上传图片失败 */
    public static ResponseWrapper markUploadImageError() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.UPLOAD_IMAGE_ERROR.getCode());
        responseWrapper.setMsg(ResponseCode.UPLOAD_IMAGE_ERROR.getMsg());
        return responseWrapper;
    }
    /** 子系统记录注册失败 */
    public static ResponseWrapper markSubsystemRegisterError() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.SUBSYSTEM_REGISTER_ERROR.getCode());
        responseWrapper.setMsg(ResponseCode.SUBSYSTEM_REGISTER_ERROR.getMsg());
        return responseWrapper;
    }
    /** 子系统记录更新失败 */
    public static ResponseWrapper markSubsystemUpdateError() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.SUBSYSTEM_UPDATE_ERROR.getCode());
        responseWrapper.setMsg(ResponseCode.SUBSYSTEM_UPDATE_ERROR.getMsg());
        return responseWrapper;
    }
    /** 子系统记录不存在 */
    public static ResponseWrapper markSubsystemNotFoundError() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.SUBSYSTEM_NOT_FOUND_ERROR.getCode());
        responseWrapper.setMsg(ResponseCode.SUBSYSTEM_NOT_FOUND_ERROR.getMsg());
        return responseWrapper;
    }
    /** 用户不存在 */
    public static ResponseWrapper markUserNotFoundError() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.USER_NOT_FOUND_ERROR.getCode());
        responseWrapper.setMsg(ResponseCode.USER_NOT_FOUND_ERROR.getMsg());
        return responseWrapper;
    }
    /** 邮箱验证码检查出错 */
    public static ResponseWrapper markEmailCodeCheckError() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.EMAILCODE_CHECK_ERROR.getCode());
        responseWrapper.setMsg(ResponseCode.EMAILCODE_CHECK_ERROR.getMsg());
        return responseWrapper;
    }

    public static ResponseWrapper markEmailBindError() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.EMAIL_BIND_ERROR.getCode());
        responseWrapper.setMsg(ResponseCode.EMAIL_BIND_ERROR.getMsg());
        return responseWrapper;
    }
    /** 用户名格式不合法 */
    public static ResponseWrapper markUsernameIllegalError() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.USERNAME_ILLEGAL_ERROR.getCode());
        responseWrapper.setMsg(ResponseCode.USERNAME_ILLEGAL_ERROR.getMsg());
        return responseWrapper;
    }

    /** 密码格式不合法 */
    public static ResponseWrapper markPasswordIllegalError() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.PASSWORD_ILLEGAL_ERROR.getCode());
        responseWrapper.setMsg(ResponseCode.PASSWORD_ILLEGAL_ERROR.getMsg());
        return responseWrapper;
    }

    /** 子系统名称为空 */
    public static ResponseWrapper markSubsystemNameEmptyError() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.SUBSYSTEM_NAME_EMPTY_ERROR.getCode());
        responseWrapper.setMsg(ResponseCode.SUBSYSTEM_NAME_EMPTY_ERROR.getMsg());
        return responseWrapper;
    }

    /** 子系统名称为空 */
    public static ResponseWrapper markDeleteAdminError() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.DELETE_ADMIN_ERROR.getCode());
        responseWrapper.setMsg(ResponseCode.DELETE_ADMIN_ERROR.getMsg());
        return responseWrapper;
    }

    /** 没有对应角色 */
    public static ResponseWrapper markNoRoleError() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.NO_ROLE_ERROR.getCode());
        responseWrapper.setMsg(ResponseCode.NO_ROLE_ERROR.getMsg());
        return responseWrapper;
    }
    /** 没有对应权限 */
    public static ResponseWrapper markNoPermissionError() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.NO_PERMISSION.getCode());
        responseWrapper.setMsg(ResponseCode.NO_PERMISSION.getMsg());
        return responseWrapper;
    }

    /** 无效的ticket */
    public static ResponseWrapper markInvalidTicketError() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.INVALID_TICKET.getCode());
        responseWrapper.setMsg(ResponseCode.INVALID_TICKET.getMsg());
        return responseWrapper;
    }

    /** Sso密钥错误 */
    public static ResponseWrapper markSsoSecretkeyError() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.SSO_SECRETKEY_ERROR.getCode());
        responseWrapper.setMsg(ResponseCode.SSO_SECRETKEY_ERROR.getMsg());
        return responseWrapper;
    }

    /**
     * 包装ResponseWrapper
     *
     * @param tag Tag名称
     * @param jsonArray JSON数据
     * @return ResponseWrapper
     */
    public static ResponseWrapper getResponseWrapperFromJSONArray(String tag, JSONArray jsonArray) {
        ResponseWrapper responseWrapper;
        if (jsonArray != null && jsonArray.size() > 0) {
            responseWrapper = ResponseWrapper.markSuccess();
            responseWrapper.setExtra(tag, jsonArray);
        } else if (jsonArray != null) {
            responseWrapper = ResponseWrapper.markNoData();
        } else {
            responseWrapper = ResponseWrapper.markError();
        }
        return responseWrapper;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map<String, Object> getExtra() {
        return extra;
    }

    public void setExtra(String key, Object value) {
        if (this.extra == null) {
            this.extra = new HashMap<>();
        }
        this.extra.put(key, value);
    }

    public Object getExtra(String key) {
        return this.extra.get(key);
    }

    public Object removeExtra(String key) {
        return this.extra.remove(key);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
