package cn.krl.authplatformserver.service.Impl;

import cn.krl.authplatformserver.mapper.LoginRecordMapper;
import cn.krl.authplatformserver.model.dto.LoginRecordDTO;
import cn.krl.authplatformserver.model.dto.LoginRecordPageDTO;
import cn.krl.authplatformserver.model.po.LoginRecord;
import cn.krl.authplatformserver.service.ILoginRecordService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务实现类
 *
 * @author kuang
 * @since 2021-11-30
 */
@Service
@Slf4j
public class LoginRecordServiceImpl extends ServiceImpl<LoginRecordMapper, LoginRecord>
        implements ILoginRecordService {

    private final LoginRecordMapper loginRecordMapper;

    public LoginRecordServiceImpl(LoginRecordMapper loginRecordMapper) {
        this.loginRecordMapper = loginRecordMapper;
    }

    @Override
    public void insert(Integer uid, String ip) {
        LoginRecord loginRecord = new LoginRecord();
        loginRecord.setIp(ip);
        loginRecord.setUid(uid);
        loginRecordMapper.insert(loginRecord);
    }

    @Override
    public List<LoginRecordDTO> listAllByUid(Integer uid) {
        QueryWrapper<LoginRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", uid);
        List<LoginRecord> loginRecords = loginRecordMapper.selectList(queryWrapper);
        if (loginRecords != null) {
            return toDTOList(loginRecords);
        }
        log.warn(uid + "不存在登录记录");
        return new ArrayList<>();
    }

    @Override
    public LoginRecordPageDTO listPageByUid(Integer uid, Integer cur, Integer size) {
        // 查询
        QueryWrapper<LoginRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", uid);
        Page<LoginRecord> page = new Page<>(cur, size);
        loginRecordMapper.selectPage(page, queryWrapper);
        // 封装返回对象
        LoginRecordPageDTO loginRecordPageDTO = new LoginRecordPageDTO();
        loginRecordPageDTO.setCur(cur);
        loginRecordPageDTO.setSize(size);
        loginRecordPageDTO.setTotal(Math.toIntExact(page.getTotal()));
        List<LoginRecord> loginRecords = page.getRecords();
        if (loginRecords == null) {
            log.warn(uid + "不存在登录记录" + "cur:" + cur + "size:" + size);
        } else {
            loginRecordPageDTO.setLoginRecords(toDTOList(loginRecords));
        }
        return loginRecordPageDTO;
    }

    @Override
    public List<LoginRecordDTO> listAll() {
        QueryWrapper<LoginRecord> queryWrapper = new QueryWrapper<>();
        List<LoginRecord> loginRecords = loginRecordMapper.selectList(queryWrapper);
        if (loginRecords != null) {
            return toDTOList(loginRecords);
        }
        log.warn("不存在登录记录");
        return new ArrayList<>();
    }

    @Override
    public LoginRecordPageDTO listPage(Integer cur, Integer size) {
        // 查询
        Page<LoginRecord> page = new Page<>(cur, size);
        loginRecordMapper.selectPage(page, new QueryWrapper<>());

        // 封装返回对象
        LoginRecordPageDTO loginRecordPageDTO = new LoginRecordPageDTO();
        loginRecordPageDTO.setCur(cur);
        loginRecordPageDTO.setSize(size);
        loginRecordPageDTO.setTotal(Math.toIntExact(page.getTotal()));
        List<LoginRecord> loginRecords = page.getRecords();
        if (loginRecords == null) {
            log.warn("不存在登录记录" + "cur:" + cur + "size:" + size);
        } else {
            loginRecordPageDTO.setLoginRecords(toDTOList(loginRecords));
        }
        return loginRecordPageDTO;
    }

    /**
     * @description: 转换为DTO对象
     * @param: records
     * @author kuang
     * @date: 2021/11/30
     */
    public List<LoginRecordDTO> toDTOList(List<LoginRecord> records) {
        List<LoginRecordDTO> loginRecordDTOs = new ArrayList<>();
        for (LoginRecord record : records) {
            LoginRecordDTO loginRecordDTO = new LoginRecordDTO();
            BeanUtils.copyProperties(record, loginRecordDTO);
            loginRecordDTOs.add(loginRecordDTO);
        }
        return loginRecordDTOs;
    }
}
