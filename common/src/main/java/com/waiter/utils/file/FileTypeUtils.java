package com.waiter.utils.file;

import com.waiter.common.constant.FileType;
import com.waiter.utils.DecimalConvertUtils;

import java.io.*;

/**
 * @ClassName FileTypeUtils
 * @Description 获取文件类型的工具类
 * @Author lizhihui
 * @Date 2019/2/17 10:49
 * @Version 1.0
 */
public class FileTypeUtils {
    public static FileType getFileType(File file){
        String magicNumber = getMagicNumber(file);
        if(magicNumber != null && magicNumber.length()>0){
            magicNumber = magicNumber.toUpperCase();
            FileType[] fileTypes = FileType.values();
            for(FileType type:fileTypes){
                //注意：有的文件的魔数可能前半部分与别的文件的重合，为了防止判断出错，需要将两个前半部分重合的魔数中的长魔数在枚举类中放在短魔数的前面
                if(magicNumber.startsWith(type.getMagicNumber())){
                    return type;
                }
            }
        }
        return null;
    }
    /**
     * 获取文件的魔数
     * @param file
     * @return
     */
    public static String getMagicNumber(File file){
        byte[] bytes = new byte[28];
        try (InputStream inputStream = new FileInputStream(file)){
            inputStream.read(bytes,0,28);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return DecimalConvertUtils.byteToHex(bytes);
    }

    /**
     * 根据文件名获取文件后缀
     * @param fileName
     * @return
     */
    public static String getFileSuffix(String fileName){
        if(fileName == null){
            return null;
        }
        int index = fileName.lastIndexOf(".");
        if(index != -1){
            return fileName.substring(index + 1);
        }else {
            return null;
        }
    }

    public static void main(String[] args){
        File file = new File("D:/testx.jpg");
        String magicNumber = getMagicNumber(file);
        System.out.println(magicNumber);
    }
}
