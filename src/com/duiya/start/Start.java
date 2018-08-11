package com.duiya.start;

import com.duiya.utils.VideoPicUtils;
import java.io.File;
import java.util.Scanner;

public class Start {
    public static int VIDEO_HEIGHT = 960;
    public static int VIDEO_WIDTH = 540;
    public static float VIDEO_FRAME = 29.97f;
    public static String FRAME_IMG = System.getProperty("user.dir") + File.separator + "frame_img";
    public static String FRAME_TXT = System.getProperty("user.dir") + File.separator + "frame_txt";
    public static String FRAME_PIC = System.getProperty("user.dir") + File.separator + "frame_pic";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("input the path of video:");
        String videoPath = scanner.nextLine();

        /*if you don't know the following information, you can right click to view the properties of the video*/
        System.out.println("input resolution of the video, the default is 540*960");
        String resoulution = scanner.nextLine();
        if(resoulution == null || resoulution.length() == 0){
            resoulution = "540*960";
        }else{
            String temp[] = resoulution.split("\\*");
            VIDEO_WIDTH = Integer.valueOf(temp[0]);
            VIDEO_HEIGHT = Integer.valueOf(temp[1]);
        }

        System.out.println("input frame rate of the video, the defalult is 29.97");
        String frame = scanner.nextLine();
        if(frame == null || frame.length() == 0){
            frame = "29.97";
        }else{
            VIDEO_FRAME = Float.valueOf(frame);
        }

        File file1 = new File(FRAME_IMG);
        if(file1.exists()){
            file1.delete();
            file1.mkdirs();
        }else{
            file1.mkdirs();
        }

        File file2 = new File(FRAME_TXT);
        if(file2.exists()){
            file2.delete();
            file2.mkdirs();
        }else{
            file2.mkdirs();
        }

        File file3 = new File(FRAME_PIC);
        if(file3.exists()){
            file3.delete();
            file3.mkdirs();
        }else{
            file3.mkdirs();
        }

        System.out.println("start converting video to frame image");
        if(!VideoPicUtils.video2Pic(videoPath,resoulution,frame)){
            System.out.println("video2Pic Error");
            return;
        }

        System.out.println("start converting pic to ascii txt");
        if (!VideoPicUtils.pic2Text()) {
            System.out.println("pic2Text Error");
            return;
        }

        System.out.println("start converting ascii txt to picture");
        if(!VideoPicUtils.text2Pic()){
            System.out.println("text2Pic Error");
            return;
        }

        System.out.println("start converting picture to video");
        if(!VideoPicUtils.pic2Video()){
            System.out.println("pic2Video Error");
            return;
    }

    }
}
