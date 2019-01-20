package com.waiter.utils.export;

import com.itextpdf.text.pdf.BaseFont;
import com.lowagie.text.DocumentException;
import com.waiter.utils.FreeMarkerUtils;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName PDFExportUtils
 * @Description PDF文件导出工具类，主要提供的功能如下：
 * 1.将PDF文件导出到特定输出流
 * 2.将PDF文件导出到指定目录下
 * @Author lizhihui
 * @Date 2019/1/20 16:28
 * @Version 1.0
 */
public class PDFExportUtils {
    private static final String TEMPLATE_PATH = "/template/freemarker/";

    /**
     * 导出PDF文件
     * @param dataMap 需要渲染的数据
     * @param templateName 使用到的导出模板
     * @param outputFile 导出的文件路径
     * @throws IOException
     * @throws DocumentException
     * @throws ClassNotFoundException
     */
    public static void export(Map<String,Object> dataMap, String templateName,String outputFile) throws IOException, DocumentException, ClassNotFoundException {
        OutputStream outputStream = new FileOutputStream(outputFile);
        export(dataMap,templateName,outputStream);
    }

    /**
     * 导出PDF文件
     * @param dataMap 需要渲染的数据
     * @param templateName 使用到的导出模板
     * @param out 输出流
     * PDF文件导出用的是freemarker+iText，导出原理如下：
     * 1.利用freemarker将需要导出的数据渲染进模板生成一个html文件
     * 2.利用flying-saucer-pdf这个包中的方法将这个html的body部分提取出来转换成PDF文件然后导出
     * @throws ClassNotFoundException
     * @throws IOException
     * @throws DocumentException
     */
    public static void export(Map<String,Object> dataMap, String templateName, OutputStream out) throws ClassNotFoundException, IOException, DocumentException {
        //获取freemarker模板的绝对路径
        String templateAbsolutePath = Class.forName(PDFExportUtils.class.getName()).getResource(TEMPLATE_PATH).getPath();
        //由于导出的时候需要先用freemarker生成一个临时的html文件，这里指定一个临时文件的路径（之所以不直接复用freemarker返回的输出流是因为renderer.setDocument只支持路径参数，不支持流参数）
        String htmlFilePath = templateAbsolutePath + "pdf.html";

        //利用freemarker工具类生成一个临时的html文件
        FreeMarkerUtils.generateFile(dataMap,templateName,htmlFilePath);

        //初始化renderer对象
        ITextRenderer renderer = new ITextRenderer();
        //指定需要进行PDF转换的文件
        renderer.setDocument(htmlFilePath);

        //如果没有这个字体的话，生成的PDF文件不能显示汉字
        ITextFontResolver fontResolver = renderer.getFontResolver();
        fontResolver.addFont(templateAbsolutePath + "/simsun.ttc", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

        renderer.layout();
        renderer.createPDF(out);
    }

    public static void main(String[] args) throws ClassNotFoundException, IOException, DocumentException {
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("name","李志慧");
        String templateAbsolutePath = Class.forName(PDFExportUtils.class.getName()).getResource(TEMPLATE_PATH).getPath();
        File outPutFile = new File(templateAbsolutePath+"test.pdf");
        FileOutputStream out = new FileOutputStream(outPutFile);
        export(dataMap,"htmlTemp.ftl",out);
    }
}
