package com.waiter.common.constant;

/**
 * 邮箱枚举类型
 */
public enum EmailType {
    QQ("qq.com"),
    X163("163.com"),
    SINA("sina.com"),
    GMAIL("gmail.com"),
    YAHOO("yahoo.com");

    private String name;

    EmailType(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public static EmailType fromEmailName(String name){
        for(EmailType emailType:EmailType.values()){
            if(emailType.getName().equals(name)){
                return emailType;
            }
        }
        return null;
    }
}
