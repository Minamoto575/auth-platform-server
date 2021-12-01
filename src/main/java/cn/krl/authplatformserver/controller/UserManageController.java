package cn.krl.authplatformserver.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.krl.authplatformserver.common.response.ResponseWrapper;
import cn.krl.authplatformserver.common.utils.RegexUtil;
import cn.krl.authplatformserver.model.dto.UserDTO;
import cn.krl.authplatformserver.model.dto.UserPageDTO;
import cn.krl.authplatformserver.model.dto.UserUpdateDTO;
import cn.krl.authplatformserver.model.po.User;
import cn.krl.authplatformserver.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author kuang
 * @description 管理员控制器
 * @date 2021/11/25 10:27
 */
@RestController
@RequestMapping("/api/user")
@Api(tags = "用户管理")
@Slf4j
@CrossOrigin
public class UserManageController {
    private final String ADMIN = "admin";
    private final IUserService userService;
    private final RegexUtil regexUtil;

    public UserManageController(IUserService userService, RegexUtil regexUtil) {
        this.userService = userService;
        this.regexUtil = regexUtil;
    }

    /**
     * @param updateDTO: 用户更新的数据表单 要求详情见UserUpdateDTO
     * @description 用户更新方法，不做任何检验，开发给管理员
     * @return: ResponseWrapper
     * @date 2021/11/14
     */
    @SaCheckRole(value = ADMIN)
    @PutMapping("/update")
    @ApiOperation(value = "用户更新(管理员使用)")
    @ResponseBody
    public ResponseWrapper updateUser(@RequestBody @Validated UserUpdateDTO updateDTO) {
        Integer id = updateDTO.getId();
        String phone = updateDTO.getPhone();
        String email = updateDTO.getEmail();
        if (id == null || userService.getById(id) == null) {
            log.error("不存在用户id=" + id);
            return ResponseWrapper.markUserNotFoundError();
        }
        if (!regexUtil.isBlank(phone) && !regexUtil.isLegalPhone(phone)) {
            log.warn("用户:" + updateDTO.getId() + "更新失败,错误的电话格式");
            return ResponseWrapper.markPhoneError();
        }
        if (!regexUtil.isBlank(email) && !regexUtil.isLegalPhone(email)) {
            log.warn("用户:" + updateDTO.getId() + "更新失败,错误的邮箱格式");
            return ResponseWrapper.markEmailError();
        }
        // 更新用户
        try {
            userService.updateUser(updateDTO);
            log.info("用户:" + updateDTO.getId() + "更新成功");
            return ResponseWrapper.markSuccess();
        } catch (Exception e) {
            log.error("用户:" + updateDTO.getId() + "更新失败");
            return ResponseWrapper.markUpdateUserError();
        }
    }

    /**
     * @description: 删除用户
     * @param: id 用户id
     * @author kuang
     * @date: 2021/11/25
     */
    @SaCheckRole(value = ADMIN)
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除用户")
    @ResponseBody
    public ResponseWrapper deleteById(@PathVariable("id") String id) {
        User user = userService.getById(id);
        List<String> roles = user.getRoles();
        if (roles.contains(ADMIN)) {
            log.error(id + "是管理员,不能删除");
            return ResponseWrapper.markDeleteAdminError();
        }
        if (userService.removeById(id)) {
            return ResponseWrapper.markSuccess();
        } else {
            log.info("删除用户失败，用户id:" + id);
            return ResponseWrapper.markError();
        }
    }

    /**
     * @description: 查询所有用户
     * @author kuang
     * @date: 2021/11/25
     */
    @SaCheckRole(value = ADMIN)
    @GetMapping("/list/all")
    @ApiOperation(value = "获取用户列表")
    @ResponseBody
    public ResponseWrapper listAll() {
        ResponseWrapper responseWrapper;
        try {
            List<UserDTO> users = userService.listAll();
            responseWrapper = ResponseWrapper.markSuccess();
            responseWrapper.setExtra("users", users);
            log.info("查询所有用户成功");
        } catch (Exception e) {
            log.error("查询所有用户失败");
            responseWrapper = ResponseWrapper.markError();
            e.printStackTrace();
        }
        return responseWrapper;
    }

    /**
     * @description: 分页查询用户
     * @param: cur 当前页
     * @param: size 每页记录数
     * @author kuang
     * @date: 2021/11/25
     */
    @SaCheckRole(value = ADMIN)
    @GetMapping("/list/page")
    @ApiOperation(value = "分页获取用户")
    @ResponseBody
    public ResponseWrapper listPage(@RequestParam int cur, @RequestParam int size) {
        ResponseWrapper responseWrapper;
        try {
            UserPageDTO users = userService.listPage(cur, size);
            responseWrapper = ResponseWrapper.markSuccess();
            responseWrapper.setExtra("users", users);
            log.info("分页查询所有用户成功");
        } catch (Exception e) {
            log.error("分页查询所有用户失败");
            responseWrapper = ResponseWrapper.markError();
            e.printStackTrace();
        }
        return responseWrapper;
    }
}
