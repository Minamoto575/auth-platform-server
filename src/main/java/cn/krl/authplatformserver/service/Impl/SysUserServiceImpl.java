package cn.krl.authplatformserver.service.impl;

import cn.krl.authplatformserver.model.vo.SysUser;
import cn.krl.authplatformserver.mapper.SysUserMapper;
import cn.krl.authplatformserver.service.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kuang
 * @since 2021-11-11
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

}
