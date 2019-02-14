package com.waiter.utils.encryption;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import com.waiter.utils.EncodesUtils;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @ClassName RSAUtils
 * @Description 基于RSA的非对称加密工具类
 * 非对称加密是指生成一个密钥对，一个公钥一个私钥，然后将其中的一个公布出去，其他人就使用公布出去的那个密钥对需要发送的信息进行加密，接收者就用未公布的密钥对接受到的信息进行解密
 * 非对称加密虽然安全，但是加密速度没有对称加密快，所以一般将对象加密与非对称加密结合起来使用，即用对称加密加密大的文本，然后用非对称加密对对称加密的密钥进行加密分发
 * @Author lizhihui
 * @Date 2019/2/14 21:49
 * @Version 1.0
 */
public class RSAUtils {
    /**
     * 生成密钥对
     * @return
     * @throws Exception
     */
    public static KeyPair getKeyPair() throws Exception{
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        //initialize里面代表的是加密后的密文的长度为1024位，即128字节，此时明文的最大长度不能超过117字节，
        //超过117字节需要使用2048的keysize来初始化keyPairGenerator
        //超过245个字节则需要使用更高位数的keysize。RSA的keysize位越高，其产生的密钥对及加密解密的速度越慢，这是基于大素数非对称加密算法的缺陷
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair;
    }

    /**
     * 获取base64编码后的公钥
     * @param keyPair
     * @return
     */
    public static String getPublicKey(KeyPair keyPair){
        PublicKey publicKey = keyPair.getPublic();
        byte[] bytes = publicKey.getEncoded();
        return EncodesUtils.encodeBase64(bytes);
    }

    /**
     * 获取base64编码后的私钥
     * @param keyPair
     * @return
     */
    public static String getPrivateKey(KeyPair keyPair){
        PrivateKey privateKey = keyPair.getPrivate();
        byte[] bytes = privateKey.getEncoded();
        return EncodesUtils.encodeBase64(bytes);
    }

    /**
     * 将base64字符串转换成公钥
     * @param publicKeyStr
     * @return
     * @throws Exception
     */
    public static PublicKey str2PublicKey(String publicKeyStr) throws Exception {
        byte[] keyBytes = EncodesUtils.decodeBase64(publicKeyStr);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    /**
     * 将base64字符串转换成私钥
     * @param privateKeyStr
     * @return
     * @throws Exception
     */
    public static PrivateKey str2PrivateKey(String privateKeyStr) throws Exception{
        byte[] keyBytes = EncodesUtils.decodeBase64(privateKeyStr);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    /**
     * 利用公公钥进行加密
     * @param content 待加密的内容
     * @param publicKey 公钥
     * @return
     * @throws Exception
     */
    public static byte[] publicEncrypt(byte[] content,PublicKey publicKey) throws Exception{
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE,publicKey);
        byte[] bytes = cipher.doFinal(content);
        return bytes;
    }

    /**
     * 利用私钥进行解密
     * @param content 待解密的内容
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    public static byte[] privateDecrypt(byte[] content,PrivateKey privateKey) throws Exception{
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE,privateKey);
        byte[] bytes = cipher.doFinal(content);
        return bytes;
    }

    public static void main(String[] args) throws Exception {
        String content = "hello,how are you?";
        KeyPair keyPair = getKeyPair();
        String publicKey = getPublicKey(keyPair);
        System.out.println("公钥:\n"+publicKey);
        String privateKey = getPrivateKey(keyPair);
        System.out.println("私钥:\n"+privateKey);
        byte[] bytes1 = publicEncrypt(content.getBytes("UTF-8"),keyPair.getPublic());
        byte[] bytes2 = privateDecrypt(bytes1,keyPair.getPrivate());
        System.out.println(new String(bytes2,"UTF-8"));
    }
}
