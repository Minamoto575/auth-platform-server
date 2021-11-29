package cn.krl.authplatformserver.service.Impl;

import cn.krl.authplatformserver.mapper.SubsystemMapper;
import cn.krl.authplatformserver.model.dto.SubsystemDTO;
import cn.krl.authplatformserver.model.dto.SubsystemRegisterDTO;
import cn.krl.authplatformserver.model.dto.SubsystemUpdateDTO;
import cn.krl.authplatformserver.model.po.Subsystem;
import cn.krl.authplatformserver.service.ISubsystemService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        subsystemMapper.insert(subsystem);
    }

    @Override
    public void update(SubsystemUpdateDTO updateDTO) {
        int id = updateDTO.getId();
        Subsystem subsystem = subsystemMapper.selectById(id);
        BeanUtils.copyProperties(updateDTO, subsystem);
        subsystemMapper.updateById(subsystem);
    }

    @Override
    public SubsystemDTO listById(Integer id) {
        Subsystem subsystem = subsystemMapper.selectById(id);
        SubsystemDTO subsystemDTO = new SubsystemDTO();
        BeanUtils.copyProperties(subsystem, subsystemDTO);
        return subsystemDTO;
    }

    @Override
    public List<SubsystemDTO> listAll() {
        QueryWrapper queryWrapper = new QueryWrapper();
        List<Subsystem> subsystems = subsystemMapper.selectList(queryWrapper);
        return toDTOList(subsystems);
    }

    @Override
    public List<SubsystemDTO> listWorkable() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("disabled", false);
        queryWrapper.eq("expired", false);
        List<Subsystem> subsystems = subsystemMapper.selectList(queryWrapper);
        return toDTOList(subsystems);
    }

    /**
     * @description: 把实体类List封装成 DTO List
     * @param: subsystems 实体类List
     * @author kuang
     * @date: 2021/11/22
     */
    public List<SubsystemDTO> toDTOList(List<Subsystem> subsystems) {
        List<SubsystemDTO> subsystemDTOs = new ArrayList<>();
        for (Subsystem subsystem : subsystems) {
            SubsystemDTO subsystemDTO = new SubsystemDTO();
            BeanUtils.copyProperties(subsystem, subsystemDTO);
            subsystemDTOs.add(subsystemDTO);
        }
        return subsystemDTOs;
    }
}
