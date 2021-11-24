package cn.krl.authplatformserver.common.enums;

/**
 * @author kuang
 * @description 角色枚举
 * @date 2021/11/24 10:24
 */
public enum Role {
    ADMIN(0, "admin"),
    USER(1, "user");

    private Integer code;
    private String role;

    Role(int code, String role) {
        this.code = code;
        this.role = role;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return role;
    }
}
