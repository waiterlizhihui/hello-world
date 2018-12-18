package com.waiter.utils;


import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName QRCodeUtils
 * @Description 二维码和条形码工具类，主要提供了以下功能：
 * 1.生成条形码和对条形码进行解码
 * 2.生成二维码并进行解码
 * 3.生成中间插入了图片的二维码
 * @Author lizhihui
 * @Date 2018/12/9 9:43
 * @Version 1.0
 */
public class ZxingCodeUtils {
    private static final Logger logger = LoggerFactory.getLogger(ZxingCodeUtils.class);
    //生成的码的编码格式
    private static final String CHARSET = "utf-8";

    //生成的码导出为图片时图片的格式
    private static final String IMAGE_FORMAT = "png";

    //生成的条形码最小宽度，这个值为什么这样设置和条形码生成的原理有关，有待探究
    private static final int BAR_CODE_WIDTH = 3 + (7 * 6) + 5 + (7 * 6) + 3;

    //生成的二维码的默认尺寸
    private static final int QRCODE_SIZE = 300;

    //生成的二维码中间的插入的图片的默认尺寸
    private static final int IMAGE_SIZE = 60;


    /**
     * 生成条形码对象
     *
     * @param content
     * @param width
     * @param height
     * @return
     */
    public static BitMatrix genBarCode(String content, int width, int height) {
        //通过比较条形码传入的宽度值与默认的宽度值的大小得出条形码的宽度，只有传入的宽度大于默认值时，传入的宽度才起作用
        int codeWidth = Math.max(BAR_CODE_WIDTH, width);
        BitMatrix bitMatrix = null;
        //调用google的zxing工具包生成条形码,MultiFormatWriter是一个生成各种码的集合类，通过BarcodeFormat.XXX这个参数判断生成何种码
        try {
            bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.EAN_13, codeWidth, height, null);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitMatrix;
    }

    /**
     * 生成二维码对象
     *
     * @param content
     * @param qrCodeSize
     * @return
     */
    public static BitMatrix genQRCode(String content, int qrCodeSize) {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = null;
        //只有传入的二维码尺寸大于默认值时才起作用
        qrCodeSize = Math.max(qrCodeSize, QRCODE_SIZE);
        try {
            bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize, hints);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitMatrix;
    }

    /**
     * 生成条形码到指定路径
     *
     * @param content  条形码内容(这个内容不能随便填写的，要遵循条形码生成的规则，关于条形码生成规则，可自行查阅相关文档)
     * @param width    条形码宽度，存在一个默认值，只有传入的宽度大于默认值时，传入的宽度才起作用
     * @param height   条形码高度
     * @param savePath 条形码输出的路径
     */
    public static void genBarCode(String content, int width, int height, String savePath) {
        BitMatrix bitMatrix = genBarCode(content, width, height);
        Path path = Paths.get(savePath);
        try {
            MatrixToImageWriter.writeToPath(bitMatrix, IMAGE_FORMAT, path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成条形码到输出流
     *
     * @param content 条形码内容
     * @param width   条形码宽度，存在一个默认值，只有传入的宽度大于默认值时，传入的宽度才起作用
     * @param height  条形码高度
     * @param out     条形码输出流
     */
    public static void genBarCode(String content, int width, int height, OutputStream out) {
        BitMatrix bitMatrix = genBarCode(content, width, height);
        try {
            MatrixToImageWriter.writeToStream(bitMatrix, IMAGE_FORMAT, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 对条形码进行解码
     * 没有想到使用场景，一般条形码都是通过扫码器进行解码的，将解码方法写出来在实际中又该怎么使用呢？可能是单纯地用来测试生成的条形码有没有问题吧
     *
     * @param barCodePath 条形码图片路径
     * @return
     * @throws Exception
     */
    public static String decodeBarCode(String barCodePath) throws Exception {
        BinaryBitmap bitmap = decode(barCodePath);
        Result result = new MultiFormatReader().decode(bitmap);
        return result.getText();
    }

    /**
     * 对生成的二维码进行解码
     *
     * @param qrCodePath
     * @return
     * @throws Exception
     */
    public static String decodeQRCode(String qrCodePath) throws Exception {
        BinaryBitmap bitmap = decode(qrCodePath);
        Map<DecodeHintType, Object> hints = new HashMap<>();
        hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
        Result result = new MultiFormatReader().decode(bitmap, hints);
        return result.getText();
    }

    /**
     * 对条形码进行解码
     *
     * @param in
     * @return
     * @throws Exception
     */
    public static String decodeBarCode(InputStream in) throws Exception {
        BinaryBitmap bitmap = decode(in);
        Result result = new MultiFormatReader().decode(bitmap);
        return result.getText();
    }

    /**
     * 对二维码进行解码
     * @param in
     * @return
     * @throws Exception
     */
    public static String decodeQRCode(InputStream in) throws Exception {
        BinaryBitmap bitmap = decode(in);
        Map<DecodeHintType, Object> hints = new HashMap<>();
        hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
        Result result = new MultiFormatReader().decode(bitmap, hints);
        return result.getText();
    }

    /**
     * 获取解码对象
     * @param imagePath
     * @return
     * @throws Exception
     */
    public static BinaryBitmap decode(String imagePath) throws Exception {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(imagePath));
            if (image == null) {
                throw new Exception("图片不存在!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        return bitmap;
    }

    /**
     * 获取解码对象
     * @param in
     * @return
     * @throws Exception
     */
    public static BinaryBitmap decode(InputStream in) throws Exception {
        BufferedImage image = null;
        try {
            image = ImageIO.read(in);
            if (image == null) {
                throw new Exception("图片不存在!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        return bitmap;
    }

    /**
     * 生成二维码保存到指定路径
     *
     * @param content    二维码内容
     * @param qrCodeSize 二维码尺寸
     * @param savePath   二维码保存路径
     */
    public static void genQRCode(String content, int qrCodeSize, String savePath) {
        BitMatrix bitMatrix = genQRCode(content, qrCodeSize);
        try {
            Path path = Paths.get(savePath);
            MatrixToImageWriter.writeToPath(bitMatrix, IMAGE_FORMAT, path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成二维码输出到指定输出流
     *
     * @param content    二维码内容
     * @param qrCodeSize 二维码尺寸
     * @param out        输出流
     */
    public static void genQRcode(String content, int qrCodeSize, OutputStream out) {
        BitMatrix bitMatrix = genQRCode(content, qrCodeSize);
        try {
            MatrixToImageWriter.writeToStream(bitMatrix, IMAGE_FORMAT, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 在生成的二维码中间插入图片(比如logo什么的)
     *
     * @param bitMatrix 二维码对象
     * @param imagePath 插入的图片路径
     * @return 插入了图片的二维码
     * @throws Exception
     */
    public static BufferedImage insertImgIntoQRCode(BitMatrix bitMatrix, String imagePath) throws Exception {
        //将生成的二维码对象转换成bufferedImage，以便可以在上面插入图片
        int qrCodeSize = bitMatrix.getWidth();
        BufferedImage qrCodeImage = new BufferedImage(qrCodeSize, qrCodeSize, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < qrCodeSize; x++) {
            for (int y = 0; y < qrCodeSize; y++) {
                qrCodeImage.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }

        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            throw new Exception("插入二维码的图片不存在!");
        }
        Image image = ImageIO.read(imageFile);
        int width = image.getWidth(null);
        int heigth = image.getHeight(null);
        //如果插入的图片的尺寸大于默认的插入图片尺寸的话，则取默认值，否则取插入图片的尺寸,
        width = Math.min(width, IMAGE_SIZE);
        heigth = Math.min(heigth, IMAGE_SIZE);

        //对插入的图片进行压缩
        image = ImageUtils.scaleImage(image, width, heigth);

        int x = (qrCodeSize - width) / 2;
        int y = (qrCodeSize - heigth) / 2;

        //将图片绘制到二维码中心
        Graphics2D graphics2D = qrCodeImage.createGraphics();
        graphics2D.drawImage(image, x, y, width, heigth, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, heigth, 6, 6);
        graphics2D.setStroke(new BasicStroke(3f));
        graphics2D.draw(shape);
        graphics2D.dispose();

        return qrCodeImage;
    }
}
