package com.waiter.common.constant;

import sun.security.rsa.RSASignature;

/**
 * 加密方法枚举类
 */
public enum Encrypt {
    MD5("MD5"),
    SHA1("SHA-1");

    private String name;

    Encrypt(String name){ this.name = name;}

    public String getName(){
        return name;
    }
}
