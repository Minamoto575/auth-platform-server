package cn.krl.authplatformserver.common.utils;

/**
 * @author kuang
 * @description 阿里短信服务工具类
 * @date 2021/11/20  15:44
 */

import com.alibaba.fastjson.JSONObject;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.aliyun.teaopenapi.models.Config;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
public class AliMessageUtil {

    private static final String AccessKey_ID = "LTAI5tCfNFitSipbM1jpuGSn";
    private static final String AccessKey_Secret = "c9f5QIeAK1oyi2QCyEeCQ2SmkYc3cW";
    private static final String signName = "梦想珈";
    private static final String templateCode = "SMS_217030149";
    private static final String endpoint = "dysmsapi.aliyuncs.com";

    /**
     * 使用AK&SK初始化账号Client
     *
     * @return Client
     * @throws Exception
     */
    public static com.aliyun.dysmsapi20170525.Client createClient() throws Exception {
        Config config = new Config()
            // 您的AccessKey ID
            .setAccessKeyId(AccessKey_ID)
            // 您的AccessKey Secret
            .setAccessKeySecret(AccessKey_Secret);
        // 访问的域名
        config.endpoint = endpoint;
        return new com.aliyun.dysmsapi20170525.Client(config);
    }

    /**
     * @param phone:       电话
     * @param messageCode: 验证码
     * @description 发送短信的工具方法
     * @return: void
     * @data 2021/11/20
     */
    public static boolean sendMessage(String phone, String messageCode) throws Exception {
        Client client = AliMessageUtil.createClient();
        JSONObject json = new JSONObject();
        json.put("code", messageCode);

        SendSmsRequest sendSmsRequest = new SendSmsRequest()
            .setPhoneNumbers(phone)
            .setSignName(signName)
            .setTemplateCode(templateCode)
            .setTemplateParam(json.toString());
        // 复制代码运行请自行打印 API 的返回值
        SendSmsResponse response = client.sendSms(sendSmsRequest);
        SendSmsResponseBody body = response.getBody();
        String resultCode = body.getCode();
        if (!"OK".equals(resultCode)) {
            log.error("阿里API调用失败，响应码:" + body.getCode());
            log.error("错误信息" + body.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 随机生成6位验证码
     *
     * @return
     */
    public static String getRandomCode(Integer code) {
        Random random = new Random();
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < code; i++) {
            result.append(random.nextInt(10));
        }
        return result.toString();
    }
}
