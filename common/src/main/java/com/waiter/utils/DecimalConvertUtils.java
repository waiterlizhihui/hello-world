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
}
