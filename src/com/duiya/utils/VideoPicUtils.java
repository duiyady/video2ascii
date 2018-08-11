package com.duiya.utils;

import com.duiya.start.Start;
import org.jim2mov.core.*;
import org.jim2mov.utils.MovieUtils;

import java.io.*;
import java.util.ArrayList;

public class VideoPicUtils {
    private final static String base_dir = System.getProperty("user.dir");

    /**
     * change video to frame picture
     * @param videoPath
     * @param resoulution
     * @param frame
     * @return
     */
    public static boolean video2Pic(String videoPath, String resoulution, String frame) {
        //use ffmpeg.exe
        StringBuilder cmd = new StringBuilder();
        cmd.append(base_dir+ File.separator + "ffmpeg.exe");
        cmd.append(" -i "+ videoPath);
        cmd.append(" -r "+ frame);
        cmd.append(" -f image2");
        cmd.append(" -s " + resoulution);
        cmd.append(" frame_img/%d.jpg");
        boolean result = true;
        try{
            Process proc = Runtime.getRuntime().exec(cmd.toString());
            System.out.println("cmd=" + cmd.toString());
            PrintStream error = new PrintStream(proc.getErrorStream());
            PrintStream output = new PrintStream(proc.getInputStream());
            error.start();
            output.start();
            proc.waitFor();
        }catch(Exception e){
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    /**
     * change frame picture to ascii txt
     * @return
     */
    public static boolean pic2Text() {
        boolean result = true;
        File file = new File(Start.FRAME_IMG);
        if (file.isDirectory() && file.exists()) {
            File[] files = file.listFiles();
            int length = files.length;
            if (files != null && length > 0) {
                for (int i = 0; i < files.length; i++) {
                    try {
                        System.out.println(String.format("start converting the image to ASCII code...%s/%s ", (i + 1), length));
                        File file1 = new File(Start.FRAME_IMG + File.separator + (i + 1) + ".jpg");
                        if (!file1.isFile()) {
                            System.out.println("this file is not a file..." + (i + 1));
                            continue;
                        }
                        AsciiPic asciiPic = new AsciiPic();
                        asciiPic.createAsciiPic(file1);
                        asciiPic.saveAsTxt(Start.FRAME_TXT + File.separator + (i + 1) + ".txt");
                    } catch (Exception e) {
                        e.printStackTrace();
                        result = false;
                    }
                }
            }

        }
        return result;
    }

    /**
     * change ascii txt to picture
     * @return
     */
    public static boolean text2Pic(){
        boolean result = true;
        File file = new File(Start.FRAME_TXT);
        if (file.isDirectory() && file.exists()) {
            File[] files = file.listFiles();
            int length = files.length;
            if (files != null && length > 0) {
                for(int i = 0; i < files.length; i++){
                    try {
                        System.out.println(String.format("start converting the ascii txt to picture...%s/%s ", (i + 1), length));
                        File file1 = new File(Start.FRAME_TXT + File.separator + (i + 1) + ".txt");
                        if (!file1.isFile()) {
                            System.out.println("this file is not a file" + (i + 1));
                            continue;
                        }
                        PicAscii.convert(file1, new File(Start.FRAME_PIC + File.separator + (i + 1) + ".jpg"));
                    } catch (Exception e) {
                        e.printStackTrace();
                        result = false;
                    }
                }
            }
        }
        return result;
    }

    /**
     * change picture to video
     * @return
     */
    public static boolean pic2Video(){
        boolean result = true;
        ArrayList<String> fileArray = new ArrayList<String>();
        File[] listFiles = new File(Start.FRAME_PIC).listFiles();
        for (int i = 1; i <= listFiles.length; i++) {
            fileArray.add(Start.FRAME_PIC + File.separator + i + ".jpg");
        }
        try {
            new FilesToMov(fileArray, MovieInfoProvider.TYPE_QUICKTIME_JPEG, "duiya.mp4");
        } catch (MovieSaveException e) {
            e.printStackTrace();
            return false;
        }
        return result;
    }
}

class PrintStream extends  Thread {
    private InputStream is;

    public PrintStream(InputStream is) {
        this.is = is;
    }

    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                System.out.println(line.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class FilesToMov implements ImageProvider, FrameSavedListener {
    private int type = MovieInfoProvider.TYPE_QUICKTIME_JPEG;
    private ArrayList<String> fileArray = null;
    public FilesToMov(ArrayList<String> fileArray, int type, String path) throws MovieSaveException {
        this.fileArray = fileArray;
        this.type = type;
        DefaultMovieInfoProvider dmip = new DefaultMovieInfoProvider(path);
        dmip.setFPS(Start.VIDEO_FRAME);
        dmip.setNumberOfFrames(fileArray.size());
        dmip.setMWidth(Start.VIDEO_WIDTH);
        dmip.setMHeight(Start.VIDEO_HEIGHT);
        new Jim2Mov(this, dmip, this).saveMovie(this.type);
    }
    @Override
    public void frameSaved(int frameNumber) {
        System.out.println("Saved frame: " + frameNumber);
    }
    @Override
    public byte[] getImage(int frame) {
        try {
            return MovieUtils.convertImageToJPEG(new File(fileArray.get(frame)), 1.0f);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

