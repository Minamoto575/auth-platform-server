package cn.krl.authplatformserver.service.Impl;

import cn.krl.authplatformserver.common.utils.SaltUtil;
import cn.krl.authplatformserver.mapper.UserMapper;
import cn.krl.authplatformserver.model.dto.RegisterDTO;
import cn.krl.authplatformserver.model.vo.User;
import cn.krl.authplatformserver.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;

/**
 * 用户服务实现类
 *
 * @author kuang
 * @since 2021-11-11
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired private UserMapper userMapper;

    @Override
    public boolean loginCheck(String phone, String password) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("phone", phone);
        User user = userMapper.selectOne(queryWrapper);
        System.out.println(user);
        log.info(user.toString());
        String hashedPassword = hashPassword(password, user.getSalt());
        return hashedPassword.equals(user.getPassword());
    }

    @Override
    public String hashPassword(String password, String salt) {
        String pwdStr = DigestUtils.md5DigestAsHex(password.getBytes());
        String mixedStr = pwdStr + salt;
        String hashedPassword = DigestUtils.md5DigestAsHex(mixedStr.getBytes());
        return hashedPassword;
    }

    @Override
    public User getUserByPhone(String phone) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("phone", phone);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public void registerUser(RegisterDTO registerDTO) {
        User user = new User();
        BeanUtils.copyProperties(registerDTO, user);
        String salt = SaltUtil.getSalt(10);
        user.setSalt(salt);
        String hashedPassword = hashPassword(registerDTO.getPassword(), salt);
        user.setPassword(hashedPassword);
        user.setGmtCreate(System.currentTimeMillis());
        user.setGmtModifed(System.currentTimeMillis());
        user.setDisabled(false);
        user.setExpired(false);
        userMapper.insert(user);
    }

    @Override
    public boolean phoneExists(String phone) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("phone", phone);
        List<User> users = userMapper.selectList(queryWrapper);
        return !users.isEmpty();
    }

    @Override
    public boolean emailExists(String email) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("email", email);
        List<User> users = userMapper.selectList(queryWrapper);
        return !users.isEmpty();
    }
}
