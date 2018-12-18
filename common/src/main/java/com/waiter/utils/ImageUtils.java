package com.waiter.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @ClassName ImageUtils
 * @Description 图片工具类，主要提供了以下功能：
 * 1.将Image对象转换成BufferedImage对象
 * 2.对图片进行缩放
 * @Author lizhihui
 * @Date 2018/12/9 16:09
 * @Version 1.0
 */
public class ImageUtils {
    private static final Logger logger = LoggerFactory.getLogger(ImageUtils.class);

    /**
     * 将image转换成bufferdImage
     *
     * @param image
     * @return
     */
    public static BufferedImage imageToBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }
        //这个方法保证了图片的所有像素都会被加载进来
        image = new ImageIcon(image).getImage();

        BufferedImage bufferedImage = null;

        //尝试生成一个和当前设备屏幕适配的bufferedImage
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        int transparency = Transparency.OPAQUE;
        //获取设备信息
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gs.getDefaultConfiguration();
        //尝试生成适配设备的bufferedImage
        bufferedImage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);

        //如果设备适配的bufferedImage生成失败的话，则直接new一个bufferedImage对象出来
        if (bufferedImage == null) {
            int type = BufferedImage.TYPE_INT_RGB;
            bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }

        //获取bufferedImage的画板引用
        Graphics graphics = bufferedImage.createGraphics();
        //将image画到bufferedImage上面
        graphics.drawImage(image, 0, 0, null);
        graphics.dispose();

        return bufferedImage;
    }

    /**
     * 对图片进行缩放
     * @param image 待缩放的图片
     * @param width 缩放的宽度
     * @param heigth 缩放的高度
     * @return
     */
    public static Image scaleImage(Image image,int width,int heigth){
        Image imagex = image.getScaledInstance(width, heigth, Image.SCALE_SMOOTH);
        BufferedImage tag = new BufferedImage(width, heigth, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = tag.getGraphics();
        graphics.drawImage(imagex, 0, 0, null);
        graphics.dispose();
        return imagex;
    }
}
