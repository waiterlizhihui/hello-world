package com.waiter.utils;

import java.io.*;

/**
 * @ClassName FileSerializableUtils
 * @Description 文件序列化工具，貌似是没什么用的东西，Java已经有文件流了
 * @Author lizhihui
 * @Date 2018/12/5 18:03
 * @Version 1.0
 */
public class FileSerializable implements Serializable{
    private static final long serialVersionUID = 1L;

    private String fileName;

    private transient InputStream inputStream;

    public FileSerializable(String fileName,InputStream inputStream){
        if(fileName == null || fileName.trim().length() == 0 || inputStream == null){
            throw  new IllegalArgumentException("fileName:" + fileName + "| inputStream:" + inputStream);
        }
        this.fileName = fileName;
        this.inputStream = inputStream;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int ret = -1;
        while ((ret = objectInputStream.read(buffer,0,1024)) != -1){
            byteArrayOutputStream.write(buffer,0,ret);
        }

        byte[] data = byteArrayOutputStream.toByteArray();
        this.inputStream = new ByteArrayInputStream(data);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();

        byte[] buffer = new byte[1024];
        int ret = -1;
        while ((ret = inputStream.read(buffer,0,1024)) != -1){
            objectOutputStream.write(buffer,0,ret);
        }
        objectOutputStream.flush();
    }

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
