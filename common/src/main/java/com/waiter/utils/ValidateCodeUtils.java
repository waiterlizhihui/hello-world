package com.waiter.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Random;

/**
 * @ClassName ValidateCodeUtils
 * @Description 验证码生成工具类，主要提供了以下功能:
 * TOOD
 * 1.简单的随机字符串验证码
 * 2.滑动验证码
 * @Author lizhihui
 * @Date 2018/12/9 17:25
 * @Version 1.0
 */
public class ValidateCodeUtils {
    private static final Random random = new Random();

    /**
     * 生成
     *
     * @param length
     * @return
     */
    public static String genRandomCode(boolean numberFlag, int length) {
        String code = "";
        String strTable = numberFlag ? "1234567890" : "1234567890abcdefghijkmnpqrstuvwxyz";
        int strTableLen = strTable.length();
        boolean success = false;
        do {
            code = "";
            int count = 0;
            for (int i = 0; i < length; i++) {
                //生成一个随机数，然后去strTable里面取值
                double dblR = Math.random() * strTableLen;
                int intR = (int) Math.floor(dblR);
                char c = strTable.charAt(intR);
                if (('0' <= c) && (c <= '9')) {
                    count++;
                }
                code += strTable.charAt(intR);
            }
            //如果生成的随机码中数字的个数超过了两个，则重新生成
            if (count >= 2) {
                success = false;
            }
        } while (success);
        return code;
    }

    /**
     * 生成二维码图片输出流
     *
     * @param code
     * @param out
     * @param width
     * @param height
     * @throws IOException
     */
    public static void genValidateImage(String code, OutputStream out, int width, int height) throws IOException {
        int verifySize = code.length();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Random rand = new Random();
        Graphics2D graphics2D = image.createGraphics();
        //设置抗锯齿(抗锯齿是啥有时间去了解一下)
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color[] colors = new Color[5];
        Color[] colorSpaces = new Color[]{Color.WHITE, Color.CYAN,
                Color.GRAY, Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE,
                Color.PINK, Color.YELLOW};
        float[] fractions = new float[colors.length];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = colorSpaces[rand.nextInt(colorSpaces.length)];
            fractions[i] = rand.nextFloat();
        }
        Arrays.sort(fractions);

        //设置边框颜色
        graphics2D.setColor(Color.GRAY);
        graphics2D.fillRect(0, 0, width, height);

        //设置背景色
        Color c = getRandColor(200, 250);
        graphics2D.setColor(c);
        graphics2D.fillRect(0, 2, width, height - 4);

        //绘制干扰线
        graphics2D.setColor(getRandColor(160, 200));
        for (int i = 0; i < 20; i++) {
            int x = random.nextInt(width - 1);
            int y = random.nextInt(height - 1);
            int x1 = random.nextInt(6) + 1;
            int y1 = random.nextInt(12) + 1;
            graphics2D.drawLine(x, y, x + x1 + 40, y + y1 + 20);
        }

        //添加噪点
        //噪声率
        float yawRate = 0.05f;
        int area = (int) (yawRate * width * height);
        for (int i = 0; i < area; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            //rgb会是一个特别大的随机值，但是不知道这样生成有什么意义,难道rgb值就是三原色的三个数值相乘得出的？
            int rgb = getRandomIntColor();
            image.setRGB(x, y, rgb);
        }
        //使图片x轴扭曲
        shearX(graphics2D, width, height, c);
        //使图片y轴扭曲
        shearY(graphics2D, width, height, c);

        graphics2D.setColor(getRandColor(100, 160));
        int fontSize = height - 4;
        //使用的字体是Algerian，如果操作系统没有这种字体的话需要单独安装
        Font font = new Font("Algerian", Font.ITALIC, fontSize);
        graphics2D.setFont(font);

        //将验证码绘制进图片里面
        char[] chars = code.toCharArray();
        for (int i = 0; i < verifySize; i++) {
            AffineTransform affineTransform = new AffineTransform();
            affineTransform.setToRotation(Math.PI / 4 * rand.nextDouble() * (rand.nextBoolean() ? 1 : -1), (width / verifySize) * i + fontSize / 2, height / 2);
            graphics2D.setTransform(affineTransform);
            graphics2D.drawChars(chars, i, 1, ((width - 10) / verifySize) * i + 5, height / 2 + fontSize / 2 - 10);
        }
        graphics2D.dispose();
        ImageIO.write(image, "jpg", out);
    }

    /**
     * 通过随机获取某一范围之内的三原色数值产生随机颜色
     *
     * @param fc 三原色数值最小值
     * @param bc 三原色数值最大值
     * @return
     */
    private static Color getRandColor(int fc, int bc) {
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        //随机获取颜色之所以这样写，是为了保证生成的三原色的三个数值在fc到bc区间内
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);

        return new Color(r, g, b);
    }

    /**
     * 生成随机RGB值
     *
     * @return
     */
    private static int getRandomIntColor() {
        int[] rgb = getRandomRgb();
        int color = 0;
        for (int c : rgb) {
            color = color << 8;
            color = color | c;
        }
        return color;
    }

    /**
     * 随机获取某个三原色的数值
     *
     * @return
     */
    private static int[] getRandomRgb() {
        int[] rgb = new int[3];
        for (int i = 0; i < 3; i++) {
            rgb[i] = random.nextInt(255);
        }
        return rgb;
    }

    /**
     * 将x轴上的坐标进行偏移
     *
     * @param graphics
     * @param width
     * @param height
     * @param color
     */
    private static void shearX(Graphics graphics, int width, int height, Color color) {
        int period = random.nextInt(2);
        boolean borderGap = true;
        int frames = 1;
        int phase = random.nextInt(2);

        for (int i = 0; i < height; i++) {
            double d = (double) (period >> 1) * Math.sin((double) i / (double) period + (6.2831853071795862D * (double) phase) / (double) frames);
            graphics.copyArea(0, i, width, height, (int) d, 0);
            if (borderGap) {
                graphics.setColor(color);
                graphics.drawLine((int) d, i, 0, i);
                graphics.drawLine((int) d + width, i, width, i);
            }
        }
    }

    /**
     * 将Y轴上的坐标进行偏移
     *
     * @param graphics
     * @param width
     * @param height
     * @param color
     */
    private static void shearY(Graphics graphics, int width, int height, Color color) {
        int period = random.nextInt(40) + 10;
        boolean borderGap = true;
        int frames = 20;
        int phase = 7;
        for (int i = 0; i < width; i++) {
            double d = (double) (period >> 1) * Math.sin((double) i / (double) period + (6.2831853071795862D * (double) phase) / (double) frames);
            graphics.copyArea(i, 0, 1, height, 0, (int) d);
            if (borderGap) {
                graphics.setColor(color);
                graphics.drawLine(i, (int) d, i, 0);
                graphics.drawLine(i, (int) d + height, i, height);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        int a = 1545062400;
        int b = a*1000;
        System.out.println(b);
    }
}
