package cn.krl.authplatformserver.common.config;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

/**
 * @author kuang
 * @description 更新时间的处理类，数据库发送插入和更新 自动更新gmt_modified和gmt_create字段
 * @date 2021/11/29 15:19
 */
@Component
public class UpdateTimeHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("gmtCreate", new DateTime(), metaObject);
        this.setFieldValByName("gmtModified", new DateTime(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("gmtModified", new DateTime(), metaObject);
    }
}
