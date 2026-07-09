package hk.ljx.fishoj;

import cn.dev33.satoken.secure.BCrypt;

public class GenPwd {
    public static void main(String[] args) {
        System.out.println("admin123 -> " + BCrypt.hashpw("admin123"));
    }
}