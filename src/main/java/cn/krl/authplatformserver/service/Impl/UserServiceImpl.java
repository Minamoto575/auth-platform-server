package cn.krl.authplatformserver.service.Impl;

import cn.krl.authplatformserver.common.enums.Role;
import cn.krl.authplatformserver.common.utils.SaltUtil;
import cn.krl.authplatformserver.mapper.UserMapper;
import cn.krl.authplatformserver.model.dto.*;
import cn.krl.authplatformserver.model.po.User;
import cn.krl.authplatformserver.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
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

    private final UserMapper userMapper;
    private final SaltUtil saltUtil;

    public UserServiceImpl(UserMapper userMapper, SaltUtil saltUtil) {
        this.userMapper = userMapper;
        this.saltUtil = saltUtil;
    }

    @Override
    public boolean loginCheckByPhone(String phone, String password) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        User user = userMapper.selectOne(queryWrapper);
        String hashedPwd = hashPassword(password, user.getSalt());
        return hashedPwd.equals(user.getPassword());
    }

    @Override
    public boolean loginCheckById(Integer id, String password) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        User user = userMapper.selectOne(queryWrapper);
        String hashedPwd = hashPassword(password, user.getSalt());
        return hashedPwd.equals(user.getPassword());
    }

    @Override
    public String hashPassword(String password, String salt) {
        String pwdStr = DigestUtils.md5DigestAsHex(password.getBytes());
        String mixedStr = pwdStr + salt;
        return DigestUtils.md5DigestAsHex(mixedStr.getBytes());
    }

    @Override
    public User getUserByPhone(String phone) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
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
        user.setDisabled(false);
        user.setExpired(false);
        List<String> roles = new ArrayList<>();
        roles.add(Role.USER.getName());
        user.setRoles(roles);
        userMapper.insert(user);
    }

    @Override
    public boolean phoneExists(String phone) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        List<User> users = userMapper.selectList(queryWrapper);
        return !users.isEmpty();
    }

    @Override
    public boolean emailExists(String email) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        List<User> users = userMapper.selectList(queryWrapper);
        return !users.isEmpty();
    }

    @Override
    public void updateUser(UserUpdateDTO userUpdateDTO) {
        int id = userUpdateDTO.getId();
        User user = userMapper.selectById(id);

        String password = userUpdateDTO.getPassword();
        if (password != null) {
            String salt = user.getSalt();
            String hashedPwd = hashPassword(password, salt);
            userUpdateDTO.setPassword(hashedPwd);
        }
        BeanUtils.copyProperties(userUpdateDTO, user);
        userMapper.updateById(user);
    }

    @Override
    public void changePwd(Integer id, String newPwd) {
        User user = userMapper.selectById(id);
        String salt = user.getSalt();
        String hashedPwd = hashPassword(newPwd, salt);
        user.setPassword(hashedPwd);
        userMapper.updateById(user);
    }

    @Override
    public void changePhone(Integer id, String phone) {
        User user = userMapper.selectById(id);
        user.setPhone(phone);
        userMapper.updateById(user);
    }

    @Override
    public List<UserDTO> listAll() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        List<User> users = userMapper.selectList(queryWrapper);
        return users2UserDTOs(users);
    }

    @Override
    public UserPageDTO listPage(int cur, int size) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        Page<User> page = new Page<>(cur, size);
        UserPageDTO userPageDTO = new UserPageDTO();
        userMapper.selectPage(page, queryWrapper);
        userPageDTO.setCur(cur);
        userPageDTO.setSize(size);
        userPageDTO.setTotal(Math.toIntExact(page.getTotal()));
        userPageDTO.setUsers(users2UserDTOs(page.getRecords()));
        return userPageDTO;
    }

    @Override
    public void updateStatus(String phone, String ip) {
        User user = this.getUserByPhone(phone);
        user.setLastIp(ip);
        user.setGmtLogin(LocalDateTime.now());
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
        userMapper.updateById(user);
    }

    @Override
    public void changeName(Integer id, String name) {
        User user = userMapper.selectById(id);
        user.setUsername(name);
        userMapper.updateById(user);
    }

    @Override
    public UserDTO getInfo(Integer id) {
        User user = userMapper.selectById(id);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }

    @Override
    public boolean userExists(Integer id) {
        User user = userMapper.selectById(id);
        return user != null;
    }

    @Override
    public void insertUser(UserInsertDTO insertDTO) {
        User user = new User();
        BeanUtils.copyProperties(insertDTO, user);
        String salt = saltUtil.getSalt(10);
        user.setSalt(salt);
        String hashedPwd = hashPassword(insertDTO.getPassword(), salt);
        user.setPassword(hashedPwd);
        user.setDisabled(false);
        user.setExpired(false);
        List<String> roles = new ArrayList<>();
        roles.add(Role.USER.getName());
        user.setRoles(roles);
        userMapper.insert(user);
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
