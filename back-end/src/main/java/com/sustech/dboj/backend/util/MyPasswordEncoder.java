package com.sustech.dboj.backend.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 加密工具类
 * 为了实现先把前端加密代码解密再非对称加密
 *
 *
 * 突然发现没必要，先放着，看看有需求再写
 *
 */
public class MyPasswordEncoder extends BCryptPasswordEncoder {
}
