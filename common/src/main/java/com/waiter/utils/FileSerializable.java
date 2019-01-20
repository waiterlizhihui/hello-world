package com.waiter.utils;

import java.io.*;

/**
 * @ClassName FileSerializableUtils
 * @Description 文件序列化工具，貌似是没什么用的东西，Java已经有文件流了
 * @Author lizhihui
 * @Date 2018/12/5 18:03
 * @Version 1.0
 */
public class  FileSerializable implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 反序列化后的文件名称
     */
    private String fileName;

    /**
     * 输入流（据说是不能够进行序列化，所以用transient修饰）,用这个将文件读入内存，然后进行序列化
     * 文件序列化的原理：由于inputstream不能进行序列化，所以将文件的输入流写进序列化的输出流里面实现文件的序列化
     */
    private transient InputStream inputStream;

    /**
     * 构造函数
     * @param fileName
     * @param inputStream
     */
    public FileSerializable(String fileName,InputStream inputStream){
        if(fileName == null || fileName.trim().length() == 0 || inputStream == null){
            throw  new IllegalArgumentException("fileName:" + fileName + "| inputStream:" + inputStream);
        }
        this.fileName = fileName;
        this.inputStream = inputStream;
    }

    /**
     * 这个方法应该是对象进行反序列化的时候会自动调用
     * @param objectInputStream
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        //通过这个方法读取已经序列化了的内容
        objectInputStream.defaultReadObject();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int ret = -1;
        //利用字节数组缓存流对序列化了的内容进行缓冲读取
        //疑问，为什么使用ByteArrayInputStream而不使用FileInputStream进行读取,这样不就省了saveFile这一步吗？
        while ((ret = objectInputStream.read(buffer,0,1024)) != -1){
            byteArrayOutputStream.write(buffer,0,ret);
        }
        //将流转换成字节数组
        byte[] data = byteArrayOutputStream.toByteArray();
        this.inputStream = new ByteArrayInputStream(data);
    }

    /**
     * 这个方法应该是对象进行序列化的时候自动调用
     * @param objectOutputStream
     * @throws IOException
     */
    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        //将可以序列化的内容写出到流
        objectOutputStream.defaultWriteObject();

        byte[] buffer = new byte[1024];
        int ret = -1;
        //通过手动的方法将文件流写入到序列化流当中
        while ((ret = inputStream.read(buffer,0,1024)) != -1){
            objectOutputStream.write(buffer,0,ret);
        }
        objectOutputStream.flush();
    }

    /**
     * 在反序列化完成之后，文件已经加载到输入流中，此时通过文件输出流将文件还原成文件
     * @throws IOException
     */
    public void saveFile() throws IOException {
        FileOutputStream fileOutputStream = null;
        try{
            fileOutputStream = new FileOutputStream(fileName);

            byte[] buffer = new byte[1024];
            int ret = -1;

            while ((ret = inputStream.read(buffer,0,1024))!= -1){
                fileOutputStream.write(buffer,0,ret);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if(fileOutputStream != null){
                fileOutputStream.close();
            }
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}
