package com.duiya.utils;

import com.duiya.start.Start;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class PicAscii {
    private static File textFile;
    private static File imageFile;
    private static BufferedImage image;
    private static final int IMAGE_TYPE = BufferedImage.TYPE_INT_RGB;


    public static boolean convert(File textFile, File imageFile) {
        PicAscii.textFile = textFile;
        PicAscii.imageFile = imageFile;
        PicAscii.image = new BufferedImage(Start.VIDEO_WIDTH, Start.VIDEO_HEIGHT, PicAscii.IMAGE_TYPE);

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(textFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        Graphics g = createGraphics(PicAscii.image);
        int size = Start.VIDEO_HEIGHT/100;
        Font font = new Font("宋体",Font.PLAIN,size);
        g.setFont(font);
        String line;

        int Y_LINEHEIGHT = Start.VIDEO_HEIGHT/100;
        if(Y_LINEHEIGHT*100 != Start.VIDEO_HEIGHT){
            Y_LINEHEIGHT += 1;
        }
        int lineNum = 1;
        try {
            while((line = reader.readLine()) != null){
                g.drawString(line, 0, lineNum * Y_LINEHEIGHT);
                lineNum++;
            }
            g.dispose();
            FileOutputStream fos = new FileOutputStream(imageFile);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(fos);
            encoder.encode(image);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static Graphics createGraphics(BufferedImage image){
        Graphics g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, Start.VIDEO_WIDTH, Start.VIDEO_HEIGHT);
        g.setColor(Color.BLACK);
        int size = Start.VIDEO_HEIGHT/100;
        Font font = new Font("宋体",Font.PLAIN,size);
        g.setFont(font);
        return g;
    }
}
