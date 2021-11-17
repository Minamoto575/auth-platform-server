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
 * @data 2021/10/24
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
    public static ResponseWrapper markNOTLOGINError() {
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

    /**
     * 账号或者密码错误
     *
     * @return
     */
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

    /**
     * 邮箱已被使用
     */
    public static ResponseWrapper markEmailExist() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.EMAIL_EXIST.getCode());
        responseWrapper.setMsg(ResponseCode.EMAIL_EXIST.getMsg());
        return responseWrapper;
    }

    /**
     * 非法的电话格式
     */
    public static ResponseWrapper markPhoneError() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.PHONE_ERROR.getCode());
        responseWrapper.setMsg(ResponseCode.PHONE_ERROR.getMsg());
        return responseWrapper;
    }

    /**
     * 非法的邮箱格式
     */
    public static ResponseWrapper markEmailError() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.EMAIL_ERROR.getCode());
        responseWrapper.setMsg(ResponseCode.EMAIL_ERROR.getMsg());
        return responseWrapper;
    }

    /**
     * 验证码获取失败
     */
    public static ResponseWrapper markVerifyCodeError() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.VERIFYCODE_ERROR.getCode());
        responseWrapper.setMsg(ResponseCode.VERIFYCODE_ERROR.getMsg());
        return responseWrapper;
    }

    /**
     * 密码错误
     *
     * @return
     */
    public static ResponseWrapper markPasswordError() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.PASSWORD_ERROR.getCode());
        responseWrapper.setMsg(ResponseCode.PASSWORD_ERROR.getMsg());
        return responseWrapper;
    }

    /**
     * 修改密码失败
     *
     * @return
     */
    public static ResponseWrapper markChangePwdError() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.CHANGEPWD_ERROR.getCode());
        responseWrapper.setMsg(ResponseCode.CHANGEPWD_ERROR.getMsg());
        return responseWrapper;
    }

    /**
     * 修改电话失败
     *
     * @return
     */
    public static ResponseWrapper markChangePhoneError() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.CHANGEPHONE_ERROR.getCode());
        responseWrapper.setMsg(ResponseCode.CHANGEPHONE_ERROR.getMsg());
        return responseWrapper;
    }

    /**
     * 更新用户失败
     *
     * @return
     */
    public static ResponseWrapper markUpdateUserError() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.UPDATEUSER_ERROR.getCode());
        responseWrapper.setMsg(ResponseCode.UPDATEUSER_ERROR.getMsg());
        return responseWrapper;
    }

    /**
     * 短信发送失败
     *
     * @return
     */
    public static ResponseWrapper markMessageError() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.MESSAGE_ERROR.getCode());
        responseWrapper.setMsg(ResponseCode.MESSAGE_ERROR.getMsg());
        return responseWrapper;
    }

    /**
     * 注册·失败
     *
     * @return
     */
    public static ResponseWrapper markRegisterError() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(ResponseCode.REGISTER_ERROR.getCode());
        responseWrapper.setMsg(ResponseCode.REGISTER_ERROR.getMsg());
        return responseWrapper;
    }


    /**
     * 包装ResponseWrapper
     *
     * @param tag       Tag名称
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
