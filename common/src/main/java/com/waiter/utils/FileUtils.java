package com.waiter.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * @ClassName FileUtils
 * @Description 文件工具类，主要提供了以下功能：
 * 1.文件和字节数组之间的互相转换
 * @Author lizhihui
 * @Date 2018/12/10 18:00
 * @Version 1.0
 */
public class FileUtils {
    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    /**
     * 将文件转换成字节数组
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static byte[] getBytesFromFile(File file) throws Exception {
        byte[] ret = null;
        FileInputStream in = null;
        ByteArrayOutputStream out = null;
        try {
            if (file == null) {
                throw new Exception("文件为空!");
            }
            in = new FileInputStream(file);
            out = new ByteArrayOutputStream(4096);
            byte[] b = new byte[4096];
            int n;
            while ((n = in.read(b)) != -1) {
                System.out.println(n);
                out.write(b, 0, n);
            }
            out.close();
            ret = out.toByteArray();
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
        return ret;
    }

    /**
     * 将字节数组转换成文件
     *
     * @param b
     * @param outputFile
     * @return
     * @throws IOException
     */
    public static File getFileFromBytes(byte[] b, String outputFile) throws IOException {
        File ret = null;
        BufferedOutputStream stream = null;
        FileOutputStream fileOutputStream = null;
        try {
            ret = new File(outputFile);
            fileOutputStream = new FileOutputStream(ret);
            stream = new BufferedOutputStream(fileOutputStream);
            stream.write(b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                stream.close();
            }
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        }
        return ret;
    }

    public static void main(String[] args) throws Exception {
        File file = new File("/label.txt");
        getBytesFromFile(file);
    }
}
