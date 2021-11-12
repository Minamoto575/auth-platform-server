package cn.krl.authplatformserver.service.Impl;

import cn.krl.authplatformserver.common.utils.SaltUtil;
import cn.krl.authplatformserver.mapper.UserMapper;
import cn.krl.authplatformserver.model.dto.RegisterDTO;
import cn.krl.authplatformserver.model.dto.UserUpdateDTO;
import cn.krl.authplatformserver.model.po.User;
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
        String hashedPwd = hashPassword(password, user.getSalt());
        return hashedPwd.equals(user.getPassword());
    }

    @Override
    public String hashPassword(String password, String salt) {
        String pwdStr = DigestUtils.md5DigestAsHex(password.getBytes());
        String mixedStr = pwdStr + salt;
        String hashedPwd = DigestUtils.md5DigestAsHex(mixedStr.getBytes());
        return hashedPwd;
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
        String hashedPwd = hashPassword(registerDTO.getPassword(), salt);
        user.setPassword(hashedPwd);
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

    @Override
    public void updateUser(UserUpdateDTO userUpdateDTO) {
        int id = userUpdateDTO.getId();
        String password = userUpdateDTO.getPassword();
        User user = userMapper.selectById(id);
        String salt = user.getSalt();
        String hashedPwd = hashPassword(password, salt);

        BeanUtils.copyProperties(userUpdateDTO, user);
        user.setPassword(hashedPwd);
        user.setGmtModifed(System.currentTimeMillis());

        userMapper.updateById(user);
    }

    @Override
    public void changePwd(String phone, String newPwd) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("phone", phone);
        User user = userMapper.selectOne(queryWrapper);
        String salt = user.getSalt();
        String hashedPwd = hashPassword(newPwd, salt);
        user.setPassword(hashedPwd);
        user.setGmtModifed(System.currentTimeMillis());
        userMapper.updateById(user);
    }
}
