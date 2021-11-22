package cn.krl.authplatformserver.service;

import cn.krl.authplatformserver.model.dto.SubsystemRegisterDTO;
import cn.krl.authplatformserver.model.dto.SubsystemUpdateDTO;
import cn.krl.authplatformserver.model.po.Subsystem;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 子系统管理的服务类
 *
 * @author kuang
 * @since 2021-11-22
 */
public interface ISubsystemService extends IService<Subsystem> {
    /**
     * @description: 注册一个子系统的记录
     * @param: subsystemDTO
     * @author kuang
     * @date: 2021/11/22
     */
    void register(SubsystemRegisterDTO registerDTO);

    /**
     * @description: 更新一个子系统的记录
     * @param: updateDTO
     * @author kuang
     * @date: 2021/11/22
     */
    void update(SubsystemUpdateDTO updateDTO);
}
