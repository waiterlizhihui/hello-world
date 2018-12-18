package com.waiter.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @ClassName ExcelExportUtils
 * @Description Excel导出工具类，主要提供了以下功能：
 * 1.将excel表导出到指定输出流
 * @Author lizhihui
 * @Date 2018/12/5 17:46
 * @Version 1.0
 */
public class ExcelExportUtils {
    /**
     * 导出excel表
     * @param modelPath excel格式模板的路径(建议放在resouces文件夹下面，比如resouces下面的testModel模板，传入的模板路径就为/excelModel/testModel.xls)
     * @param dataList 导出到excel表中的数据集合
     * @param out 导出的输出流
     * @param offset 导出的偏移值，因为excel模板的表头可能占据多行，我们不知道应该从那一行开始填充数据，所以必须传入一个偏移量说明从第几行开始填充数据。注意：这个值从0开始
     * @param <T>
     * @throws IOException
     */
    public static <T> void export(String modelPath, List<T> dataList, OutputStream out,int offset) throws IOException {
        InputStream inputStream = null;
        HSSFWorkbook workbook = null;
        try{
            //读取resouces目录下的模板
            inputStream = Class.forName(ExcelExportUtils.class.getName()).getResourceAsStream(modelPath);

            workbook = new HSSFWorkbook(inputStream);
            //目前只支持生成一个excel sheet，如果有一次性导出多个sheet的需求的话，可以进行重载拓展
            HSSFSheet sheet = workbook.getSheetAt(0);

            // 遍历集合数据，产生数据行
            Iterator<T> it = dataList.iterator();
            HSSFRow row;
            while(it.hasNext()){
                offset++;
                row = sheet.createRow(offset);
                T t = (T) it.next();
                Class clazz = t.getClass();
                //利用反射获取JavaBean中的属性
                Field[] fields = t.getClass().getDeclaredFields();
                for(int i=0;i<fields.length;i++){
                    Field field = fields[i];
                    //获取属性名称，以此构造getter方法，然后通过get方法获取属性的值
                    String property = field.getName();
                    String methodName = generateGetMethod(property);
                    Method method = clazz.getMethod(methodName);
                    //通过反射执行getter方法获取属性值
                    Object value = method.invoke(t);

                    HSSFCell cell = row.createCell(i);
                    setCellValue(cell,value);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(inputStream != null){
                inputStream.close();
            }
        }

        try{
            workbook.write(out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(out != null){
                out.close();
            }
        }
    }

    /**
     * 根据JavaBean的属性名称生成getter方法
     * @param property
     * @return
     */
    private static String generateGetMethod(String property) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(property);
        //当首字母是小写时
        if (Character.isLowerCase(stringBuilder.charAt(0))) {
            //如果属性名称只有一个字母或者当属性名的第二个字母大写的时候，转换成的get/set方法属性名称大小写不变，比如uName转换成方法名称为getuName而不是getUName
            if (stringBuilder.length() == 1 || !Character.isUpperCase(stringBuilder.charAt(1))) {
                stringBuilder.setCharAt(0, Character.toUpperCase(stringBuilder.charAt(0)));
            }
        }
        stringBuilder.insert(0, "get");

        return stringBuilder.toString();
    }

    /**
     * 将反射得到的对象的值填入到HSSFCell中
     * @param cell
     * @param value
     * @throws Exception
     */
    private static void setCellValue(HSSFCell cell,Object value) throws Exception {
        if(value instanceof Integer){
            int intValue = (Integer)value;
            cell.setCellValue(intValue);
        }else if(value instanceof Float){
            float floatValue = (Float)value;
            cell.setCellValue(floatValue);
        }else if(value instanceof Long){
            long longValue = (Long)value;
            cell.setCellValue(longValue);
        }else if(value instanceof Double){
            double doubleValue = (Double)value;
            cell.setCellValue(doubleValue);
        }else if(value instanceof Date){
            Date date = (Date)value;
            String dateValue = DateUtils.formatDate(date);
            cell.setCellValue(dateValue);
        }else{
            if(value != null){
                String strValue = value.toString();
                cell.setCellValue(strValue);
            }else{
                cell.setCellValue("");
            }
        }
    }
}
