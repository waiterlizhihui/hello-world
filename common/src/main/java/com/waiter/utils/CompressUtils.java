package com.waiter.utils;

import net.jpountz.lz4.*;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;

import java.io.*;
import java.nio.Buffer;
import java.util.zip.*;

/**
 * @ClassName ZipUtils
 * @Description 文件压缩工具类，主要提供了以下功能：
 * 1.文件的zip压缩与解压缩
 * 2.对象的zip压缩于解压缩
 * @Author lizhihui
 * @Date 2018/12/11 13:40
 * @Version 1.0
 */
public class CompressUtils {
    /**
     * 缓存区大小，这里设置的是1024byte，即1M
     */
    private static final int BUFFER = 1024;

    /**
     * UTF-8字符编码
     */
    private static final String ENCODE_UTF_8 = "UTF-8";

    /**
     * ISO-8859-1字符编码
     */
    private static final String ENCODE_ISO_8859_1 = "ISO-8859-1";

    /**
     * 将指定文件夹下的所有文件压缩到同一目录下面
     *
     * @param file
     */
    public static void zip(File file) {
        //如果不传压缩后的文件名称及路径，默认压缩到与待压缩文件同一级目录下，并且压缩文件名称与待压缩文件名称相同
        String basePath = new StringBuilder(file.toString()).append(".zip").toString();
        File zipFile = new File(basePath);
        zip(file, zipFile);
    }

    /**
     * 将文件夹unzipFile下面的所有文件压缩到zipFile里面
     *
     * @param unzipFile 待压缩文件夹
     * @param zipFile   压缩后的文件存储路径及名称
     * @throws IOException
     */
    public static void zip(File unzipFile, File zipFile) {
        try (FileOutputStream zipFileStream = new FileOutputStream(zipFile);
             ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(zipFileStream));) {
            setZipDirTree("", unzipFile, zipOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 对一个输入流转换成压缩输出流
     *
     * @param inputStream     输入流
     * @param zipOutputStream 输出流
     * @throws IOException
     */
    public static void zip(InputStream inputStream, ZipOutputStream zipOutputStream) throws IOException {
        int ret;
        byte[] buffer = new byte[BUFFER];
        while ((ret = inputStream.read(buffer, 0, BUFFER)) != -1) {
            zipOutputStream.write(buffer, 0, ret);
        }
    }


    /**
     * 因为待压缩文件可能存在多级目录进行压缩，这个方法主要使完成待压缩文件的目录结构到压缩文件内的目录结构的映射
     *
     * @param parentPath      压缩文件的父级目录路径
     * @param unzipFile       待压缩文件
     * @param zipOutputStream zip输出流
     */
    public static void setZipDirTree(String parentPath, File unzipFile, ZipOutputStream zipOutputStream) {
        //如果待压缩的文件是一个目录的话，则对目录进行遍历，获取里面的所有文件，然后进行压缩
        if (unzipFile.isDirectory()) {
            File files[] = unzipFile.listFiles();
            //对目录里的文件进行遍历
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                //如果遍历到的是一个目录的话
                if (file.isDirectory()) {
                    //将遍历到的目录添加到父目录路径之中
                    String newOppositePath = parentPath + file.getName() + "/";
                    //在压缩文件内构建这个目录
                    ZipEntry entry = new ZipEntry(newOppositePath);
                    try {
                        zipOutputStream.putNextEntry(entry);
                        zipOutputStream.closeEntry();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //对遍历到的目录进行递归处理
                    setZipDirTree(newOppositePath, file, zipOutputStream);
                } else {
                    //如果遍历到的是一个文件的话，则直接对这个文件进行压缩处理
                    zipFile(zipOutputStream, parentPath, file);
                }
            }
        } else {
            zipFile(zipOutputStream, parentPath, unzipFile);
        }
    }

    /**
     * 将一个文件转换成一个Zip输出流
     *
     * @param zipOutputStream
     * @param oppositPath     在压缩后的文件里的父路径
     * @param file            待压缩文件名称
     */
    public static void zipFile(ZipOutputStream zipOutputStream, String oppositPath, File file) {
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));) {
            ZipEntry zipEntry = new ZipEntry(oppositPath + file.getName());
            zipOutputStream.putNextEntry(zipEntry);
            zip(bufferedInputStream, zipOutputStream);
            zipOutputStream.closeEntry();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 对文件进行解压缩
     *
     * @param zipFile 要锁
     */
    public static void unzip(File zipFile) {
        String basePath = zipFile.getParent();
        File unzipFile = new File(basePath);
        unzip(zipFile, unzipFile);
    }

    /**
     * 将压缩文件解压到指定目录下面
     *
     * @param zipFile   压缩文件
     * @param unzipFile 解压路径
     */
    public static void unzip(File zipFile, File unzipFile) {
        try (FileInputStream fileInputStream = new FileInputStream(zipFile);
             BufferedInputStream buffered = new BufferedInputStream(fileInputStream);
             ZipInputStream zipInputStream = new ZipInputStream(buffered)) {
            unzip(unzipFile, zipInputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将文件zip输出流输出到指定目录下面
     *
     * @param unzipFile
     * @param zipInputStream
     * @throws IOException
     */
    public static void unzip(File unzipFile, ZipInputStream zipInputStream) throws IOException {
        ZipEntry zipEntry = null;
        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
            String dir = unzipFile.getPath() + File.separator + zipEntry.getName();
            File dirFile = new File(dir);
            //如果文件的目录不存在的话，创建目录
            fileProber(dirFile);

            if (zipEntry.isDirectory()) {
                dirFile.mkdirs();
            } else {
                try (FileOutputStream fileOutputStream = new FileOutputStream(dirFile);
                     BufferedOutputStream buffered = new BufferedOutputStream(fileOutputStream);) {
                    unzip(zipInputStream, buffered);
                }
            }

        }
    }

    /**
     * 将zip输入流转换成输出流
     *
     * @param zipInputStream
     * @param outputStream
     * @throws IOException
     */
    public static void unzip(ZipInputStream zipInputStream, OutputStream outputStream) throws IOException {
        int ret;
        byte[] buffer = new byte[BUFFER];
        while ((ret = zipInputStream.read(buffer, 0, BUFFER)) != -1) {
            outputStream.write(buffer, 0, ret);
        }
        outputStream.flush();
    }

    /**
     * 当父目录不存在的话，递归创建父级目录
     *
     * @param dirFile
     */
    private static void fileProber(File dirFile) {
        File parentFile = dirFile.getParentFile();
        if (!parentFile.exists()) {
            fileProber(parentFile);
            parentFile.mkdir();
        }
    }

    /**
     * 对字符串进行UTF-8编码GZIP压缩
     *
     * @param str 待压缩字符串
     * @return 压缩后的字节数组
     */
    public static byte[] GZip(String str) {
        return GZip(str, ENCODE_UTF_8);
    }

    /**
     * 对字符串进行指定编码格式的GZIP压缩
     *
     * @param str    待压缩字符串
     * @param encode 压缩的编码格式
     * @return 压缩后的字节数组
     */
    public static byte[] GZip(String str, String encode) {
        byte[] gzipByte = null;
        if (StringUtils.isEmpty(str) || StringUtils.isEmpty(encode)) {
            return null;
        }
        try {
            byte[] strByte = str.getBytes(encode);
            gzipByte = GZip(strByte);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return gzipByte;
    }

    /**
     * 对字节数组进行GZIP压缩
     *
     * @param data 待压缩的字节数组
     * @return 压缩后的字节数组
     */
    public static byte[] GZip(byte[] data) {
        byte[] gzipByte = null;
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);) {
            gzipOutputStream.write(data);
            gzipOutputStream.finish();
            gzipByte = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return gzipByte;
    }

    /**
     * 将GZIP压缩后的字节数组还原成UTF-8编码的字符串
     *
     * @param data
     * @return
     */
    public static String unGZipStr(byte[] data) {
        if (data.length == 0) {
            return null;
        }
        byte[] unGzipByte = unGZip(data);
        String unGzipStr = null;
        try {
            unGzipStr = new String(unGzipByte, ENCODE_UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return unGzipStr;
    }

    /**
     * 将GZIP压缩后的字节数组还原成指定编码格式的字符串
     *
     * @param data
     * @param encode
     * @return
     */
    public static String unGzipStr(byte[] data, String encode) {
        if (data.length == 0 || StringUtils.isEmpty(encode)) {
            return null;
        }
        byte[] unGzipByte = unGZip(data);
        String unGzipStr = null;
        try {
            unGzipStr = new String(unGzipByte, encode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return unGzipStr;
    }

    /**
     * 对字节数组进行解压缩操作
     *
     * @param data 待解压缩的字节数组
     * @return
     */
    public static byte[] unGZip(byte[] data) {
        if (data.length == 0) {
            return null;
        }
        byte[] unGzipByte = null;
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream);) {
            int ret = -1;
            byte[] buffer = new byte[BUFFER];
            while ((ret = gzipInputStream.read(buffer, 0, BUFFER)) != -1) {
                byteArrayOutputStream.write(buffer, 0, ret);
            }
            byteArrayOutputStream.flush();
            unGzipByte = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return unGzipByte;
    }

    /**
     * 对字节数组进行BZIP2压缩(压缩方法调用的是apache的commons-compress包)
     * @param data
     * @return
     */
    public static byte[] BZip2(byte[] data){
        byte[] bzip2Byte = null;
        try(ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            BZip2CompressorOutputStream bzipOutStream = new BZip2CompressorOutputStream(byteArrayOutputStream);){
            byte[] buffer = new byte[BUFFER];
            int ret = -1;
            while ((ret = byteArrayInputStream.read(buffer,0,BUFFER))!=-1){
                bzipOutStream.write(buffer,0,ret);
            }
            bzipOutStream.finish();
            bzipOutStream.flush();
            byteArrayOutputStream.flush();
            bzip2Byte = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bzip2Byte;
    }

    /**
     * 对字节数组进行BZIP2解压缩(解压缩方法调用的是apache的commons-compress包)
     * @param data
     * @return
     */
    public static byte[] unBZip2(byte[] data){
        byte[] unBzip2Byte = null;
        try(ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            BZip2CompressorInputStream bzipInStream = new BZip2CompressorInputStream(byteArrayInputStream);) {
            byte[] buffer = new byte[BUFFER];
            int ret = -1;
            while ((ret = bzipInStream.read(buffer,0,BUFFER)) != -1){
                byteArrayOutputStream.write(buffer,0,ret);
            }
            byteArrayOutputStream.flush();
            unBzip2Byte = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return unBzip2Byte;
    }

    //lz4压缩方法有问题，等研究清楚了这个压缩方法之后再来补充修复问题
//    public static byte[] lz4(byte[] data,int blockSize){
//        byte[] lz4Byte = null;
//        LZ4Factory factory = LZ4Factory.fastestInstance();
//        LZ4Compressor compressor = factory.fastCompressor();
//        try(ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            LZ4BlockOutputStream blockOutputStream = new LZ4BlockOutputStream(byteArrayOutputStream,blockSize,compressor);){
//            blockOutputStream.write(data);
//            lz4Byte = byteArrayOutputStream.toByteArray();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return lz4Byte;
//    }
//
//    public static byte[] unLz4(byte[] data,int blockSize){
//        byte[] unLz4Byte = null;
//        LZ4Factory factory = LZ4Factory.fastestInstance();
//        LZ4FastDecompressor decompressor = factory.fastDecompressor();
//        try(ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(blockSize);
//            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
//            LZ4BlockInputStream blockInputStream = new LZ4BlockInputStream(byteArrayInputStream,decompressor)){
//            byte[] buffer = new byte[blockSize];
//            int ret = -1;
//            while ((ret = blockInputStream.read(buffer)) != -1){
//                byteArrayOutputStream.write(buffer,0,ret);
//            }
//            byteArrayOutputStream.flush();
//            unLz4Byte = byteArrayOutputStream.toByteArray();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return unLz4Byte;
//    }

    public static void main(String[] args) throws Exception {

    }
}
