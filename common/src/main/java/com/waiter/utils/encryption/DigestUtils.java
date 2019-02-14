package com.waiter.utils.encryption;

import com.sun.corba.se.impl.orb.ParserTable;
import com.waiter.common.constant.Encrypt;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @ClassName EncryptionUtils
 * @Description 摘要算法工具类，主要提供了如下摘要算法
 * 1.MD5
 * 2.SHA-1
 * @Author lizhihui
 * @Date 2019/2/14 16:53
 * @Version 1.0
 */
public class DigestUtils {
    /**
     * 获取进行MD5摘要后的字符串
     * @param content
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    public static String MD5Str(String content) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] bytes = MD5(content);
        return tranBytesToStr(bytes);
    }
    /**
     * 进行MD5摘要
     * @param content
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static byte[] MD5(String content) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return encrypt(content,Encrypt.MD5);
    }

    /**
     * 获取SHA-1摘要后的字符串
     * @param content
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    public static String SHA1Str(String content) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] bytes = SHA1(content);
        return tranBytesToStr(bytes);
    }

    /**
     * 进行SHA-1摘要
     * @param content
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static byte[] SHA1(String content) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return encrypt(content,Encrypt.SHA1);
    }

    /**
     * 调用java中封装的摘要方法
     * @param content 需要进行摘要的内容
     * @param encrypt 选择的摘要方法(枚举类)
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    private static byte[] encrypt(String content, Encrypt encrypt) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String encryptName = encrypt.getName();
        MessageDigest md = MessageDigest.getInstance(encryptName);
        return md.digest(content.getBytes("UTF-8"));
    }

    /**
     * 将加密后生成的bytes数组转换成十六进制表示的字符串
     * 转换原理：由于一个byte长8位，可以由两个十六进制表示，所以生成的十六进制的字符串的长度为byte数组长度的两倍
     * @param bytes
     * @return
     */
    private static String tranBytesToStr(byte[] bytes){
        StringBuffer stringBuffer = new StringBuffer();
        for (int i=0;i<bytes.length;i++){
            //byte是有符号数，而bytes[i] & 0xff是将byte转换成无符号int类型的方式（0xff默认为int类型，一个byte只有八位，和32位的0xff进行与运算之后，前24位都被置成了0，byte中的符号位到了int中之后直接没用了）
            String hex = Integer.toHexString(bytes[i] & 0xff);
            if(hex.length()<2){
                stringBuffer.append(0);
            }
            stringBuffer.append(hex);
        }
        return stringBuffer.toString();
    }
}
