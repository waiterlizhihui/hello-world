package com.waiter.utils.encryption;

import com.waiter.common.constant.SignatureType;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

/**
 * @ClassName SignatureUtils
 * @Description 数字签名工具类，提供了MD5withRSA与SHA1withRSA两种方法
 * 数字签名是用来防篡改的，信息发送者对信息进行摘要，然后用私钥对摘要进行加密，然后将信息与加密后的摘要及公钥一起发送给信息接收者
 * 信息接受者接受到上述内容之后，先对接收到的内容进行相同的摘要获得摘要1，然后再用公钥对接收到的私钥加密的摘要进行解密获得摘要2，最后将摘要1与摘要2进行对比，如果一直则说明内容没被篡改，否则被篡改过了
 * @Author lizhihui
 * @Date 2019/2/14 22:48
 * @Version 1.0
 */
public class SignatureUtils {
    /**
     * 对内容进行签名
     * @param signatureType 使用的签名类型(枚举类型)
     * @param content 待签名的内容
     * @param privateKey 生成签名的私钥
     * @return
     * @throws Exception
     */
    public static byte[] sign(SignatureType signatureType, byte[] content, PrivateKey privateKey) throws Exception{
        Signature signature = Signature.getInstance(signatureType.getName());
        signature.initSign(privateKey);
        signature.update(content);
        return signature.sign();
    }

    /**
     * 对签名进行校验
     * @param signatureType 使用的签名类型(枚举类型)
     * @param content 待校验的内容
     * @param sign 待校验的签名
     * @param publicKey 对摘要进行解密的公钥
     * @return
     * @throws Exception
     */
    public static boolean verify(SignatureType signatureType,byte[] content, byte[] sign, PublicKey publicKey) throws Exception{
        Signature signature = Signature.getInstance(signatureType.getName());
        signature.initVerify(publicKey);
        signature.update(content);
        return signature.verify(sign);
    }

    public static void main(String[] args) throws Exception {
        String content = "hello,where are you from?";
        KeyPair keyPair = RSAUtils.getKeyPair();
        byte[] signature = sign(SignatureType.MD5withRSA,content.getBytes("UTF-8"),keyPair.getPrivate());
        System.out.println(verify(SignatureType.MD5withRSA,"hello,where are you from?".getBytes("UTF-8"),signature,keyPair.getPublic()));
    }
}
