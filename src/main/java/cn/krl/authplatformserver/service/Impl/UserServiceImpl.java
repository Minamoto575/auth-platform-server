package cn.krl.authplatformserver.service.Impl;

import cn.krl.authplatformserver.common.enums.Role;
import cn.krl.authplatformserver.common.utils.SaltUtil;
import cn.krl.authplatformserver.mapper.UserMapper;
import cn.krl.authplatformserver.model.dto.UserDTO;
import cn.krl.authplatformserver.model.dto.UserRegisterDTO;
import cn.krl.authplatformserver.model.dto.UserUpdateDTO;
import cn.krl.authplatformserver.model.po.User;
import cn.krl.authplatformserver.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
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
    @Autowired private SaltUtil saltUtil;

    @Override
    public boolean loginCheckByPhone(String phone, String password) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("phone", phone);
        User user = userMapper.selectOne(queryWrapper);
        String hashedPwd = hashPassword(password, user.getSalt());
        return hashedPwd.equals(user.getPassword());
    }

    @Override
    public boolean loginCheckById(Integer id, String password) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", id);
        User user = userMapper.selectOne(queryWrapper);
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
    public void registerUser(UserRegisterDTO userRegisterDTO) {
        User user = new User();
        BeanUtils.copyProperties(userRegisterDTO, user);
        String salt = saltUtil.getSalt(10);
        user.setSalt(salt);
        String hashedPwd = hashPassword(userRegisterDTO.getPassword(), salt);
        user.setPassword(hashedPwd);
        user.setGmtCreate(System.currentTimeMillis());
        user.setGmtModified(System.currentTimeMillis());
        user.setDisabled(false);
        user.setExpired(false);
        List<String> roles = new ArrayList<String>();
        roles.add(Role.USER.getName());
        user.setRoles(roles);
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
        user.setGmtModified(System.currentTimeMillis());

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
        user.setGmtModified(System.currentTimeMillis());
        userMapper.updateById(user);
    }

    @Override
    public void changePhone(Integer id, String phone) {
        User user = userMapper.selectById(id);
        user.setPhone(phone);
        user.setGmtModified(System.currentTimeMillis());
        userMapper.updateById(user);
    }

    @Override
    public List<UserDTO> listAll() {
        QueryWrapper queryWrapper = new QueryWrapper();
        List<User> users = userMapper.selectList(queryWrapper);
        return users2UserDTOs(users);
    }

    @Override
    public List<UserDTO> listPage(int cur, int size) {
        QueryWrapper queryWrapper = new QueryWrapper();
        Page page = new Page();
        page.setCurrent(cur);
        page.setSize(size);
        List<User> users = userMapper.selectPage(page, queryWrapper).getRecords();
        return users2UserDTOs(users);
    }

    @Override
    public void updateIp(String phone, String ip) {
        User user = this.getUserByPhone(phone);
        user.setLastIp(ip);
        userMapper.updateById(user);
    }

    @Override
    public List<String> getRoleList(int id) {
        User user = userMapper.selectById(id);
        return user.getRoles();
    }

    @Override
    public void changeEmail(Integer id, String email) {
        User user = userMapper.selectById(id);
        user.setEmail(email);
        user.setGmtModified(System.currentTimeMillis());
        userMapper.updateById(user);
    }

    @Override
    public void changeName(Integer id, String name) {
        User user = userMapper.selectById(id);
        user.setUsername(name);
        user.setGmtModified(System.currentTimeMillis());
        userMapper.updateById(user);
    }

    /**
     * @description: 封装成DTOList返回给前端
     * @param: users
     * @author kuang
     * @date: 2021/11/23
     */
    public List<UserDTO> users2UserDTOs(List<User> users) {
        List<UserDTO> userDTOS = new ArrayList<>();
        for (User user : users) {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
            userDTOS.add(userDTO);
        }
        return userDTOS;
    }
}
