package cn.krl.authplatformserver.service;

import cn.krl.authplatformserver.model.dto.*;
import cn.krl.authplatformserver.model.po.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 用户服务类
 *
 * @author kuang
 * @since 2021-11-11
 */
public interface IUserService extends IService<User> {
    /**
     * @description 进行登录检查
     * @param phone: 用户名电话
     * @param password: 密码
     * @return: boolean
     * @date 2021/11/11
     */
    boolean loginCheckByPhone(String phone, String password);

    /**
     * @description 进行登录检查
     * @param Id: 用户名id
     * @param password: 密码
     * @return: boolean
     * @date 2021/11/11
     */
    boolean loginCheckById(Integer Id, String password);

    /**
     * @description 对密码进行单向散列 带盐
     * @param password: 密码明文
     * @param salt: 盐
     * @return: java.lang.String 密码散列值
     * @date 2021/11/11
     */
    String hashPassword(String password, String salt);

    /**
     * @description 根据电话号码获取对应用户
     * @param phone: 电话号码
     * @return: User
     * @date 2021/11/11
     */
    User getUserByPhone(String phone);

    /**
     * @description 用户注册
     * @param userRegisterDTO: 注册表单
     * @return: boolean
     * @date 2021/11/11
     */
    void registerUser(UserRegisterDTO userRegisterDTO);

    /**
     * @description 检查电话号码是否被注册
     * @param phone: 电话号码
     * @return: boolean
     * @date 2021/11/12
     */
    boolean phoneExists(String phone);

    /**
     * @param email: 邮箱
     * @description 检查email是否被使用
     * @return: boolean
     * @date 2021/11/12
     */
    boolean emailExists(String email);

    /**
     * @param userUpdateDTO: 信息表单
     * @description 更新用户信息 不做验证 开放给管理员用
     * @return: void
     * @date 2021/11/12
     */
    void updateUser(UserUpdateDTO userUpdateDTO);

    /**
     * @param id: 用户id
     * @param newPwd: 新密码
     * @description 修改密码
     * @return: void
     * @date 2021/11/12
     */
    void changePwd(Integer id, String newPwd);

    /**
     * @param id: 用户id
     * @param phone: 新的电话
     * @description 修改绑定的电话号码
     * @return: void
     * @date 2021/11/15
     */
    void changePhone(Integer id, String phone);

    /**
     * @description 列出所以的用户
     * @return: java.util.List<cn.krl.authplatformserver.model.dto.UserDTO>
     * @date 2021/11/18
     */
    List<UserDTO> listAll();

    /**
     * @description 分页列出用户
     * @param cur: 当前页码
     * @param size: 页的大小
     * @return: java.util.List<cn.krl.authplatformserver.model.dto.UserDTO>
     * @date 2021/11/18
     */
    UserPageDTO listPage(int cur, int size);

    /**
     * @description: 更新用户登录相关的信息 ip、时间等
     * @param: phone 电话
     * @param: ip ip地址
     * @author kuang
     * @date: 2021/11/23
     */
    void updateStatus(String phone, String ip);

    /**
     * @description: 获取用户的角色列表
     * @param: id 用户id
     * @author kuang
     * @date: 2021/11/24
     */
    List<String> getRoleList(int id);
    /**
     * @description: 修改邮箱
     * @param: id 用户id
     * @param: email 邮箱地址
     * @author kuang
     * @date: 2021/11/25
     */
    void changeEmail(Integer id, String email);

    /**
     * @description: 用户修改用户名
     * @param: id 用户id
     * @param: name 新的用户名称
     * @author kuang
     * @date: 2021/11/26
     */
    void changeName(Integer id, String name);

    /**
     * @description: 获取用户信息
     * @param: id 用户id
     * @author kuang
     * @date: 2021/11/29
     */
    UserDTO getInfo(Integer id);

    /**
     * @description: 判断当前用户是否存在
     * @param: id 用户id
     * @author kuang
     * @date: 2021/11/29
     */
    boolean userExists(Integer id);

    /**
     * @description 用户插入
     * @param insertDTO
     * @author kuang
     * @date 2022/2/25
     */
    void insertUser(UserInsertDTO insertDTO);
}
