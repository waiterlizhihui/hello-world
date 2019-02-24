package com.waiter.utils.file;

import com.waiter.utils.DecimalConvertUtils;
import com.waiter.utils.PropertiesLoaderUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemHeaders;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName UploadUtils
 * @Description 文件上传工具类
 * @Author lizhihui
 * @Date 2019/2/17 9:54
 * @Version 1.0
 */
@Service("uploadUtils")
public class UploadUtils {
    private static final Logger logger = LoggerFactory.getLogger(UploadUtils.class);

    /**
     * 允许上传的文件最大大小(单位为字节)
     */
    private static final long MAX_SIZE;

    /**
     * 允许上传的文件绝对路径
     */
    private static final String SAVE_PATH;

    /**
     * 文件上传临时缓存路径
     */
    private static final String TEMP_PATH;

    /**
     * 允许上传的文件类型
     */
    private static final String[] FILE_TYPES;

    static {
        //加载配置文件里面的配置属性值
        PropertiesLoaderUtils loaderUtils = new PropertiesLoaderUtils("config.properties");
        MAX_SIZE = loaderUtils.getInteger("upload.maxSize","");
        SAVE_PATH = loaderUtils.getProperties("upload.savePath","");
        TEMP_PATH = loaderUtils.getProperties("upload.tempPath","");
        String fileTypesStr = loaderUtils.getProperties("upload.fileType");
        FILE_TYPES = fileTypesStr.split(";");
    }

    /**
     * 上传文件
     * @param request
     * @return infos[0]代表文件的验证错误；infos[1]代表文件上传时的错误；infos[2]代表文件的保存路径
     */
    public String[] upload(HttpServletRequest request){
        String[] infos = new String[5];
        infos[0] = this.validateFile(request);
        List<FileItem> fileItemList = null;
        if(infos[0].equals("true")){
            fileItemList = this.parseRequest(request);
        }
        if(fileItemList != null){
            for(FileItem fileItem : fileItemList){
                infos[1] = this.saveFile(fileItem);
            }
            infos[2] = SAVE_PATH;
        }
        return infos;
    }

    /**
     * 对文件进行相关验证
     * @param request
     * @return
     */
    private String validateFile(HttpServletRequest request){
        String errorInfo = "true";
        String contentType = request.getContentType();
        int contentLength = request.getContentLength();
        File uploadDir = new File(SAVE_PATH);
        if(contentType == null || !contentType.startsWith("multipart")){
            errorInfo = "请求不包含multipart/form-data流";
        } else if(MAX_SIZE <contentLength){
            errorInfo = "上传文件大小超出文件最大大小[" + MAX_SIZE + "]";
        } else if(!ServletFileUpload.isMultipartContent(request)){
            errorInfo = "请选择文件";
        } else if(!uploadDir.isDirectory()){
            errorInfo = "上传目录[" + SAVE_PATH + "]不存在";
        } else if(!uploadDir.canWrite()){
            errorInfo = "上传目录[" + SAVE_PATH + "]没有写权限";
        } else{
            File saveDirFile = new File(SAVE_PATH);
            if(!saveDirFile.exists()){
                saveDirFile.mkdirs();
            }

            File file = new File(TEMP_PATH);
            if(!file.exists()){
                file.mkdirs();
            }
        }

        return errorInfo;
    }

    /**
     * 解析request请求，获取上传的文件对象
     * @param request
     * @return
     */
    private List<FileItem> parseRequest(HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        boolean isMultiPart = ServletFileUpload.isMultipartContent(request);
        List<FileItem> fileItemList = null;
        if(isMultiPart){
            DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setSizeThreshold(1024*1024*10);
            factory.setRepository(new File(TEMP_PATH));
            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setHeaderEncoding("UTF-8");
            upload.setSizeMax(MAX_SIZE);

            try{
                fileItemList = upload.parseRequest(request);
            } catch (FileUploadException e) {
                e.printStackTrace();
            }
        }
        return fileItemList;
    }

    /**
     * 将获取到的文件对象保存到指定地方
     * @param fileItem
     * @return
     */
    private String saveFile(FileItem fileItem){
        String error = "true";
        String fileName = fileItem.getName();
        String fileExt = FileTypeUtils.getFileSuffix(fileName);

        if(fileItem.getSize() > MAX_SIZE){
            error = "上传文件大小超过限制";
        } else if (!checkFileType(fileItem)){
            error = "该类型的文件不允许上传";
        } else{
            checkFileType(fileItem);
            String newFileName;
            if("".equals(fileName.trim())){
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
            }else {
                newFileName = fileName;
            }
            try{
                File uploadFile = new File(SAVE_PATH,newFileName);
                fileItem.write(uploadFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return error;
    }

    /**
     * 检查文件类型是否允许上传(根据文件的魔数进行校验)
     * @param fileItem
     * @return
     */
    private boolean checkFileType(FileItem fileItem){
        byte[] bytes = fileItem.get();
        byte[] bytes1 = new byte[28];
        for(int i=0;i<28;i++){
            bytes1[i] = bytes[i];
        }
        String magicNumber = DecimalConvertUtils.byteToHex(bytes1).toUpperCase();
        for(String fileType:FILE_TYPES){
            if(magicNumber.startsWith(fileType))
                return true;
        }
        return false;
    }

}
