package cn.krl.authplatformserver.mapper;

import cn.krl.authplatformserver.model.vo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * Mapper 接口
 *
 * @author kuang
 * @since 2021-11-11
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {}
