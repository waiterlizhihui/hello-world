package com.waiter.utils.encryption;

import com.waiter.utils.EncodesUtils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @ClassName AESUtils
 * @Description AES对称加密工具类
 * 注意：AES算法支持128、192、256三种密钥长度，由于美国对加密软件出口的控制，
 * 如果想生成192和256位的密钥，则需要下载无政策和司法限制的文件，否则运行时会报java secret illegal key size or default错误
 * 关于该问题的解决：https://blog.csdn.net/wangjunjun2008/article/details/50847426
 * @Author lizhihui
 * @Date 2019/2/14 21:15
 * @Version 1.0
 */
public class AESUtils {
    /**
     * 生成密钥，并进行base64编码
     * @param length
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String genKeyAES(int length) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(length);
        SecretKey key = keyGenerator.generateKey();
        return EncodesUtils.encodeBase64(key.getEncoded());
    }

    /**
     * 对密钥进行base64解码
     * @param base64Key
     * @return
     */
    public static SecretKey loadKeyAES(String base64Key){
        byte[] bytes = EncodesUtils.decodeBase64(base64Key);
        return new SecretKeySpec(bytes,"AES");
    }

    /**
     * 加密
     * @param source 待加密的内容
     * @param key 加密密钥
     * @return
     * @throws Exception
     */
    public static byte[] encryptAES(byte[] source,SecretKey key) throws Exception{
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE,key);
        byte[] bytes = cipher.doFinal(source);
        return bytes;
    }

    /**
     * 解密
     * @param source 待解密的内容
     * @param key 密钥
     * @return
     * @throws Exception
     */
    public static byte[] decryptAES(byte[] source,SecretKey key) throws Exception{
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE,key);
        byte[] bytes = cipher.doFinal(source);
        return bytes;
    }

    public static void main(String[] args)throws Exception{
        String key = genKeyAES(256);
        System.out.println(key);

        String content = "hello,this is a encrypt demo";
        byte[] bytes1 = encryptAES(content.getBytes("UTF-8"),loadKeyAES(key));
        byte[] bytes2 = decryptAES(bytes1,loadKeyAES(key));
        System.out.println(new String(bytes2,"UTF-8"));
    }
}
