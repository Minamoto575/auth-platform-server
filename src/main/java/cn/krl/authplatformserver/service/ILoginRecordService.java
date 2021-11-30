package cn.krl.authplatformserver.service;

import cn.krl.authplatformserver.model.dto.LoginRecordDTO;
import cn.krl.authplatformserver.model.dto.LoginRecordPageDTO;
import cn.krl.authplatformserver.model.po.LoginRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 服务类
 *
 * @author kuang
 * @since 2021-11-30
 */
public interface ILoginRecordService extends IService<LoginRecord> {
    /**
     * @description: 插入一条记录
     * @param: uid 用户id
     * @param: ip ip地址
     * @author kuang
     * @date: 2021/11/30
     */
    void insert(Integer uid, String ip);

    /**
     * @description: 获取某个用户的登录记录
     * @param: uid
     * @author kuang
     * @date: 2021/11/30
     */
    List<LoginRecordDTO> listAllByUid(Integer uid);

    /**
     * @description: 获取某个用户的登录记录
     * @param: uid
     * @author kuang
     * @date: 2021/11/30
     */
    LoginRecordPageDTO listPageByUid(Integer uid, Integer cur, Integer size);

    /**
     * @description: 列出所有记录
     * @author kuang
     * @date: 2021/11/30
     */
    List<LoginRecordDTO> listAll();

    /**
     * @description: 分页列出登录记录
     * @param: cur
     * @param: size
     * @author kuang
     * @date: 2021/11/30
     */
    LoginRecordPageDTO listPage(Integer cur, Integer size);
}
