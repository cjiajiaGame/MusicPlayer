package com.cjiajiagame.sound.base;

import java.util.TimerTask;

public class Timer {
    private int second = 0;
    private int minute = 0;
    private int hour = 0;
    private java.util.Timer timer;

    /**
     * 初始化一个时间归零的计时器。
     */
    public Timer(){
        this(0);
    }

    /**
     * 初始化一个已经过去特定时间的计时器。
     * @param nowTime 已过去的秒数。
     */
    public Timer(int nowTime){
        second = nowTime;
        Update();
    }

    /**
     * 立即启动计时器。
     */
    public void start(){
        start(0);
    }
    /**
     * 经过特定的延迟(毫秒)后启动计时器。
     * @param delay 毫秒延迟
     */
    public void start(int delay){
        this.timer = new java.util.Timer();
        timer.schedule(new TimeTask(), 0, 1000);
    }
    /**
     * 停止计时器，并释放内存。
     */
    public void stop(){
        timer.cancel();
        timer.purge();
    }

    /**
     * 返回已过的秒数。
     * @return 整数
     */
    public int getSecond() {
        int second = this.second + minute*60 + hour*60*60;
        return second;
    }

    /**
     * 返回已过的分钟数。
     * @return 小数
     */
    public double getMinute() {
        double minute = this.minute + hour*60 + second/60.0;
        return minute;
    }

    /**
     * 返回已过的小时数。
     * @return 小数
     */
    public double getHour() {
        double hour = this.hour + minute/60.0 + second/60.0/60.0;
        return hour;
    }

    /**
     * 以xx:xx:xx的格式格式化输出已过时间的字符串。
     * @return 字符串
     */
    public String formatOutput(){
        String second = String.format("%02d", this.second);
        String minute = String.format("%02d", this.minute);
        String hour = String.format("%02d", this.hour);
        return hour + ":" + minute + ":" + second;
    }


    void Update(){
        if(second >= 60){
            int c = second/60;
            minute += c;
            second -= c*60;
        }
        if(minute >= 60){
            int c = minute/60;
            hour += c;
            minute -= c*60;
        }
    }

    /**
     * 计时事件类。
     */
    private class TimeTask extends TimerTask {
        @Override
        public void run() {
            second++;
            Update();
        }
    }
}
