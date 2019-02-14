package com.waiter.common.constant;

/**
 * 数字签名方法枚举类
 */
public enum SignatureType {
    MD5withRSA("MD5withRSA"),
    SHA1withRSA("SHA1withRSA");

    private String name;

    SignatureType(String name){ this.name = name;}

    public String getName(){
        return name;
    }
}
