package cn.krl.authplatformserver.mapper;

import cn.krl.authplatformserver.model.po.LoginRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * Mapper 接口
 *
 * @author kuang
 * @since 2021-11-30
 */
@Mapper
public interface LoginRecordMapper extends BaseMapper<LoginRecord> {}
