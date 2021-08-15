package com.cjiajiagame.sound.main;

import com.cjiajiagame.sound.base.MP3Player;
import com.mpatric.mp3agic.ID3v2;

import java.io.File;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConsoleMain {
    private static final Logger logger = Logger.getLogger("ConsoleMain.class");
    public static void main(String[] args) {
        ////////////////以下是参数载入
        String filepath;
        boolean debug = false;
        boolean circle = false;
        boolean info = false;
        logger.setLevel(Level.OFF);
        try{
            filepath = args[0];
            if(filepath.equalsIgnoreCase("-h")){
                help();
                return;
            }
            int i = 1;
            while(args.length > i){
                if(args[i].equalsIgnoreCase("-c")){
                    circle = true;
                }else if(args[i].equalsIgnoreCase("-d")){
                    debug = true;
                }else if(args[i].equalsIgnoreCase("-i")){
                    info = true;
                }else if(args[i].equalsIgnoreCase("-h")){
                    System.out.println("其实你应该把-h参数写到第一个参数里的。不过程序依然会执行你的-h命令。");
                    help();
                    return;
                }else{
                    System.out.println("你可能输入了一个无效参数。不过程序依然会继续运行。(" + args[i] + ")");
                }
                i++;
            }
            init(debug);
            logger.fine("args = " + Arrays.deepToString(args));
            ///////////////////以下是播放实现
            File file = new File(filepath);
            if (!file.exists() || !file.isFile()){
                System.err.println("你输入的是一个无效文件！请重新输入。");
                return;
            }
            if(!getExFilename(file.getPath()).equalsIgnoreCase("mp3")){
                System.err.println("你输入的不是MP3格式文件！请重新输入。");
                return;
            }
            MP3Player player = new MP3Player(file);
            System.out.println("已成功载入音乐。");
            ID3v2 id = player.getSoundInfo();
            if(info){
                Info(id);
                return;
            }
            System.out.println("音乐标题: \t" + get(id.getTitle()));
            System.out.println("音乐专辑: \t" + get(id.getAlbum()));
            System.out.println("音乐作者: \t" + get(id.getArtist()));
            System.out.println("音乐时长(帧): \t" + id.getLength());
            if(circle){
                System.out.println("单曲循环已启用。");
            }
            System.out.println("现在开始播放......");
            player.play(circle);

        }catch(Exception e){
            if(args.length == 0){
                logger.info("args is null.");
                System.err.println("错误：你没有传入任何参数。");
                System.err.println("用法：java -jar 文件名.jar <音乐文件路径> [选项]");
                System.err.println("更多用法请输入'java -jar 文件名.jar -h'来获取更多帮助。");
            }else{
                System.err.println("OH NO，也许...是出了bug。若是你愿意的话，就复制下面的错误信息然后去我的仓库里提个issue吧...我会尽快修复的。");
                e.printStackTrace();
            }
        }

    }
    private static void help(){
        System.out.println("用法：java -jar 文件名.jar <音乐文件路径> [选项]");
        System.out.println("本程序会从头播放音乐文件，结束后自动退出(除非启用了单曲循环)");
        System.out.println("目前只支持播放MP3格式的音乐文件。其他格式暂无法播放。");
        System.out.println("可选选项：");
        System.out.println("-d          启用调试模式（没啥用）");
        System.out.println("-c          使用单曲循环模式播放");
        System.out.println("-h          输出帮助并退出(应写在第一个参数里)");
        System.out.println("-i          输出音乐信息而不播放");
        System.out.println("如果参数中出现了-h选项，那么程序将会输出帮助而不执行其他命令。");
        System.out.println("如果参数中出现了-i且没有-h选项，那么程序只会执行-i的命令。");
        System.out.println("直接ctrl+c终止程序即可停止播放。");
        System.out.println("如果你遇到了bug，请在github上的cjiajiaGame/MusicPlayer仓库里提交issue。我会尽快解决的。");
        System.out.println("感谢您的使用！");
    }
    private static void Info(ID3v2 id){
        System.out.println("音乐标题: \t" + get(id.getTitle()));
        System.out.println("音乐专辑: \t" + get(id.getAlbum()));
        System.out.println("音乐作者: \t" + get(id.getArtist()));
        System.out.println("音乐时长(帧): \t" + id.getLength());
        System.out.println("音乐BPM（-1则代表没标记） \t" + id.getBPM());
        System.out.println("音乐版权: \t" + get(id.getCopyright()));
    }
    private static String get(String x){
        if(x == null || x.equals("")){
            return "无";
        }else{
            return x;
        }
    }
    private static String getExFilename(String filename){
        String fe = "";
        int i = filename.lastIndexOf('.');
        if (i > 0) {
            fe = filename.substring(i+1);
        }
        return fe;
    }
    private static void init(boolean debug){
        if(debug){
            logger.setLevel(Level.ALL);
        }
    }
}
