package com.waiter.utils;

/**
 * @ClassName NumberConversionUtils
 * @Description 进制转换工具
 * @Author lizhihui
 * @Date 2018/12/1 17:27
 * @Version 1.0
 */
public class DecimalConvertUtils {
    /**
     * 十进制转换成十六进制
     * @param decimal
     * @return
     */
    public static String decimalToHex(Integer decimal){
        if(decimal == null){
            return null;
        }
        return Integer.toHexString(decimal);
    }

    /**
     * 十六进制转换成十进制
     * @param hex
     * @return
     */
    public static Integer hexToDecimal(String hex){
        if(hex == null){
            return null;
        }
        return Integer.parseInt(hex,16);
    }

    /**
     * 十进制转换成八进制
     * @param decimal
     * @return
     */
    public static String decimalToOctal(Integer decimal){
        if(decimal == null){
            return null;
        }
        return Integer.toOctalString(decimal);
    }

    /**
     * 八进制转换成是十进制
     * @param octal
     * @return
     */
    public static Integer octalToDecimal(String octal){
        if(octal == null){
            return null;
        }
        return Integer.parseInt(octal,8);
    }

    /**
     * 十进制转换成二进制
     * @param decimal
     * @return
     */
    public static String decimalToBinary(Integer decimal){
        if(decimal == null){
            return null;
        }
        return Integer.toBinaryString(decimal);
    }

    /**
     * 二进制转换成十进制
     * @param binary
     * @return
     */
    public static Integer binaryToDecimal(String binary){
        if(binary == null){
            return null;
        }
        return Integer.parseInt(binary,2);
    }

    /**
     * 将十六进制转换成八进制
     * @param hex
     * @return
     */
    public static String hexToOctal(String hex){
        Integer deciaml = hexToDecimal(hex);
        return decimalToOctal(deciaml);
    }

    /**
     * 将八进制转换成十六进制
     * @param octal
     * @return
     */
    public static String octalToHex(String octal){
        Integer decimal = octalToDecimal(octal);
        return decimalToHex(decimal);
    }

    /**
     * 将十六进制转换成二进制
     * @param hex
     * @return
     */
    public static String hexToBinary(String hex){
        Integer decimal = hexToDecimal(hex);
        return decimalToBinary(decimal);
    }

    /**
     * 将二进制转换成十六进制
     * @param binary
     * @return
     */
    public static String binaryToHex(String binary){
        Integer decimal = binaryToDecimal(binary);
        return decimalToHex(decimal);
    }

    /**
     * 八进制转换成二进制
     * @param octal
     * @return
     */
    public static String octalToBinary(String octal){
        Integer decimal = octalToDecimal(octal);
        return decimalToBinary(decimal);
    }

    /**
     * 二进制转换成八进制
     * @param binary
     * @return
     */
    public static String binaryToOctal(String binary){
        Integer decimal = binaryToDecimal(binary);
        return decimalToOctal(decimal);
    }

    /**
     * 将加密后生成的bytes数组转换成十六进制表示的字符串
     * 转换原理：由于一个byte长8位，可以由两个十六进制表示，所以生成的十六进制的字符串的长度为byte数组长度的两倍
     * @param bytes
     * @return
     */
    public static String byteToHex(byte[] bytes){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i=0;i<bytes.length;i++){
            //byte是有符号数，而bytes[i] & 0xff是将byte转换成无符号int类型的方式（0xff默认为int类型，一个byte只有八位，和32位的0xff进行与运算之后，前24位都被置成了0，byte中的符号位到了int中之后直接没用了）
            String hex = Integer.toHexString(bytes[i] & 0xff);
            if(hex.length()<2){
                stringBuilder.append(0);
            }
            stringBuilder.append(hex);
        }
        return stringBuilder.toString();
    }
}
