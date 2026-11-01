package com.arthenica.ffmpegkit;

/* JADX INFO: loaded from: classes.dex */
public class Statistics {
    private double bitrate;
    private long sessionId;
    private long size;
    private double speed;
    private double time;
    private float videoFps;
    private int videoFrameNumber;
    private float videoQuality;

    public Statistics(long r1, int r3, float r4, float r5, long r6, double r8, double r10, double r12) {
        this.sessionId = r1;
        this.videoFrameNumber = r3;
        this.videoFps = r4;
        this.videoQuality = r5;
        this.size = r6;
        this.time = r8;
        this.bitrate = r10;
        this.speed = r12;
    }

    public double getBitrate() {
        return this.bitrate;
    }

    public long getSessionId() {
        return this.sessionId;
    }

    public long getSize() {
        return this.size;
    }

    public double getSpeed() {
        return this.speed;
    }

    public double getTime() {
        return this.time;
    }

    public float getVideoFps() {
        return this.videoFps;
    }

    public int getVideoFrameNumber() {
        return this.videoFrameNumber;
    }

    public float getVideoQuality() {
        return this.videoQuality;
    }

    public void setBitrate(double r1) {
        this.bitrate = r1;
    }

    public void setSessionId(long r1) {
        this.sessionId = r1;
    }

    public void setSize(long r1) {
        this.size = r1;
    }

    public void setSpeed(double r1) {
        this.speed = r1;
    }

    public void setTime(double r1) {
        this.time = r1;
    }

    public void setVideoFps(float r1) {
        this.videoFps = r1;
    }

    public void setVideoFrameNumber(int r1) {
        this.videoFrameNumber = r1;
    }

    public void setVideoQuality(float r1) {
        this.videoQuality = r1;
    }

    public String toString() {
        return "Statistics{sessionId=" + this.sessionId + ", videoFrameNumber=" + this.videoFrameNumber + ", videoFps=" + this.videoFps + ", videoQuality=" + this.videoQuality + ", size=" + this.size + ", time=" + this.time + ", bitrate=" + this.bitrate + ", speed=" + this.speed + '}';
    }
}
