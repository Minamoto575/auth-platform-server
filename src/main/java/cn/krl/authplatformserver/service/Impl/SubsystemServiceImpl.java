package cn.krl.authplatformserver.service.Impl;

import cn.krl.authplatformserver.mapper.SubsystemMapper;
import cn.krl.authplatformserver.model.dto.SubsystemRegisterDTO;
import cn.krl.authplatformserver.model.dto.SubsystemUpdateDTO;
import cn.krl.authplatformserver.model.po.Subsystem;
import cn.krl.authplatformserver.service.ISubsystemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 服务实现类
 *
 * @author kuang
 * @since 2021-11-22
 */
@Service
public class SubsystemServiceImpl extends ServiceImpl<SubsystemMapper, Subsystem>
        implements ISubsystemService {

    @Autowired private SubsystemMapper subsystemMapper;

    @Override
    public void register(SubsystemRegisterDTO registerDTO) {
        Subsystem subsystem = new Subsystem();
        BeanUtils.copyProperties(registerDTO, subsystem);
        subsystem.setGmtCreate(System.currentTimeMillis());
        subsystem.setGmtModified(System.currentTimeMillis());
        subsystemMapper.insert(subsystem);
    }

    @Override
    public void update(SubsystemUpdateDTO updateDTO) {
        int id = updateDTO.getId();
        Subsystem subsystem = subsystemMapper.selectById(id);
        BeanUtils.copyProperties(updateDTO, subsystem);
        subsystem.setGmtModified(System.currentTimeMillis());
        subsystemMapper.updateById(subsystem);
    }
}
