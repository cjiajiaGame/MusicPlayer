package com.cjiajiagame.sound.base;

import com.mpatric.mp3agic.*;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 针对于MP3格式音乐文件的播放类。
 */
public class MP3Player {
    /**
     * 使用File对象创建播放器，并读取标签数据。
     * @param SoundFile 文件File
     * @throws IOException IO问题
     * @throws InvalidDataException 非法标签
     * @throws UnsupportedTagException 未支持标签
     * @throws NoSuchTagException 无标签
     */
    public MP3Player(File SoundFile) throws IOException, InvalidDataException, UnsupportedTagException, NoSuchTagException {
        this.SoundFile = SoundFile;
        this.FileStream = new BufferedInputStream(new FileInputStream(this.SoundFile));
        this.SoundInfo = new Mp3File(this.SoundFile).getId3v2Tag();
    }

    /**
     * 使用文件的位置字符串创建播放器，并读取标签数据。
     * @param Filepath 文件完全地址
     * @throws IOException IO问题
     * @throws InvalidDataException 非法标签
     * @throws UnsupportedTagException 未支持标签
     * @throws NoSuchTagException 无标签
     */
    public MP3Player(String Filepath) throws IOException, UnsupportedTagException, NoSuchTagException, InvalidDataException {
        this(new File(Filepath));
    }

    /**
     * 获取标签信息类。
     * @return ID3v2标签类。
     */
    public ID3v2 getSoundInfo(){return SoundInfo;}

    /**
     * 返回JLayer的Player播放器类。<br/>
     * !警告! 未开始播放前该返回参数将为null。
     * @return Player播放器类。
     */
    public Player getPlayer(){return player;}

    /**
     * 开始同步播放音乐。需要数秒时间进行加载。<br/>
     * !警告! 该方法将阻塞执行该方法的线程。
     * @throws JavaLayerException 播放器类错误
     * @param circle 是否单曲循环
     */
    public void play(boolean circle) throws JavaLayerException {
        if(!circle) {
            this.player = new Player(this.FileStream);
            this.player.play();
        }else{
            while(true){
                this.player = new Player(this.FileStream);
                this.player.play();
            }
        }
    }

    /**
     * 开始异步播放音乐。需要数秒时间进行加载。<br/>
     * 请注意，该方法通过创建新线程来承担播放任务，故无法截获错误。
     * @param circle 是否单曲循环
     */
    public void playAs(boolean circle){
        this.AsynchronousThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(!circle) {
                        player = new Player(FileStream);
                        player.play();
                    }else{
                        while(true){
                            player = new Player(FileStream);
                            player.play();
                        }
                    }
                }catch (JavaLayerException e){

                }
            }
        });
        this.AsynchronousThread.start();
        this.isAsynchronous = true;
    }

    /**
     * 停止播放异步音乐。<br/>
     * !警告! 本方法使用已过时的Thread.stop()方法来终止线程。
     */
    public void stopAs(){
        if(this.AsynchronousThread == null)
            throw new RuntimeException("现在没有异步播放音乐！");
        if(isAsynchronous){
            this.AsynchronousThread.stop();
            this.isAsynchronous = false;
        }else{
            throw new RuntimeException("无法停止同步播放的音乐！");
        }
    }

    /**
     * 暂停/恢复正在播放的异步音乐。<br/>
     * !警告! 本方法使用已过时的Thread.suspend()和Thread.resume()来实现控制音乐暂停的效果。
     */
    public void pauseAs(){
        if(this.AsynchronousThread == null)
            throw new RuntimeException("现在没有异步播放音乐！");
        if(isAsynchronous){
            if(!isPaused) {
                this.AsynchronousThread.suspend();
                this.isPaused = true;
            }
            else {
                this.AsynchronousThread.resume();
                this.isPaused = false;
            }
        }else{
            throw new RuntimeException("无法暂停/恢复同步播放的音乐！");
        }
    }

    /**
     * 测试正在播放的音乐是否为异步。也可判断是否在播放异步音乐。
     * @return
     */
    public boolean isAsynchronous(){
        return isAsynchronous;
    }

    private Player player;
    private boolean isAsynchronous = false;
    private boolean isPaused = false;
    private Thread AsynchronousThread = null;
    private File SoundFile;
    private BufferedInputStream FileStream;
    private ID3v2 SoundInfo;
}
