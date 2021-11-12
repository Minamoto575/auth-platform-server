package cn.krl.authplatformserver.service;

import cn.krl.authplatformserver.model.dto.RegisterDTO;
import cn.krl.authplatformserver.model.vo.User;
import com.baomidou.mybatisplus.extension.service.IService;

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
     * @data 2021/11/11
     */
    boolean loginCheck(String phone, String password);

    /**
     * @description 对密码进行单向散列 带盐
     * @param password: 密码明文
     * @param salt: 盐
     * @return: java.lang.String 密码散列值
     * @data 2021/11/11
     */
    String hashPassword(String password, String salt);

    /**
     * @description 根据电话号码获取对应用户
     * @param phone: 电话号码
     * @return: User
     * @data 2021/11/11
     */
    User getUserByPhone(String phone);

    /**
     * @description 用户注册
     * @param registerDTO: 注册表单
     * @return: boolean
     * @data 2021/11/11
     */
    void registerUser(RegisterDTO registerDTO);

    /**
     * @description 检查电话号码是否被注册
     * @param phone: 电话号码
     * @return: boolean
     * @data 2021/11/12
     */
    boolean phoneExists(String phone);

    /**
     * @description 检查email是否被使用
     * @param email: 邮箱
     * @return: boolean
     * @data 2021/11/12
     */
    boolean emailExists(String email);
}
