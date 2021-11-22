package cn.krl.authplatformserver.service.Impl;

import cn.krl.authplatformserver.mapper.SubsystemMapper;
import cn.krl.authplatformserver.model.po.Subsystem;
import cn.krl.authplatformserver.service.ISubsystemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 服务实现类
 *
 * @author kuang
 * @since 2021-11-22
 */
@Service
public class SubsystemServiceImpl extends ServiceImpl<SubsystemMapper, Subsystem>
        implements ISubsystemService {}
