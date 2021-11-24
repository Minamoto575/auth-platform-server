package cn.krl.authplatformserver.service.Impl;

import cn.dev33.satoken.stp.StpInterface;
import cn.krl.authplatformserver.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author kuang
 * @description 权限获取实现类
 * @date 2021/11/24 10:10
 */
@Component // 保证此类被SpringBoot扫描，完成Sa-Token的自定义权限验证扩展
public class StpInterfaceImpl implements StpInterface {

    @Autowired private IUserService userService;

    /** 返回一个账号所拥有的权限码集合 */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return null;
    }

    /** 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验) 俺也不知道这个loginType干啥的 */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        String id = (String) loginId;
        return userService.getRoleList(Integer.parseInt(id));
    }
}
