package com.waiter.utils.encryption;

import com.sun.corba.se.impl.orb.ParserTable;
import com.waiter.common.constant.Encrypt;
import com.waiter.utils.DecimalConvertUtils;

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
        return DecimalConvertUtils.byteToHex(bytes);
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
        return DecimalConvertUtils.byteToHex(bytes);
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
}
