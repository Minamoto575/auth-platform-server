package cn.krl.authplatformserver.service;

import cn.krl.authplatformserver.model.dto.SubsystemDTO;
import cn.krl.authplatformserver.model.dto.SubsystemRegisterDTO;
import cn.krl.authplatformserver.model.dto.SubsystemUpdateDTO;
import cn.krl.authplatformserver.model.po.Subsystem;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

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

    /**
     * @description: 根据id获取一条子系统的记录
     * @param: id
     * @author kuang
     * @date: 2021/11/22
     */
    SubsystemDTO listById(Integer id);

    /**
     * @description: 获取所有子系统的记录(管理员使用)
     * @author kuang
     * @date: 2021/11/22
     */
    List<SubsystemDTO> listAll();

    /**
     * @description: 获取所有可使用子系统的记录(用户使用)
     * @author kuang
     * @date: 2021/11/22
     */
    List<SubsystemDTO> listWorkable();
}
