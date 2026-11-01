package com.arthenica.ffmpegkit;

import java.util.LinkedList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class FFmpegSession extends AbstractSession implements Session {
    private final FFmpegSessionCompleteCallback completeCallback;
    private final List<Statistics> statistics;
    private final StatisticsCallback statisticsCallback;
    private final Object statisticsLock;

    private FFmpegSession(String[] r2, FFmpegSessionCompleteCallback r3, LogCallback r4, StatisticsCallback r5, LogRedirectionStrategy r6) {
        super(r2, r4, r6);
        this.completeCallback = r3;
        this.statisticsCallback = r5;
        this.statistics = new LinkedList();
        this.statisticsLock = new Object();
    }

    public static FFmpegSession create(String[] r6) {
        return new FFmpegSession(r6, null, null, null, FFmpegKitConfig.getLogRedirectionStrategy());
    }

    public static FFmpegSession create(String[] r6, FFmpegSessionCompleteCallback r7) {
        return new FFmpegSession(r6, r7, null, null, FFmpegKitConfig.getLogRedirectionStrategy());
    }

    public static FFmpegSession create(String[] r6, FFmpegSessionCompleteCallback r7, LogCallback r8, StatisticsCallback r9) {
        return new FFmpegSession(r6, r7, r8, r9, FFmpegKitConfig.getLogRedirectionStrategy());
    }

    public static FFmpegSession create(String[] r6, FFmpegSessionCompleteCallback r7, LogCallback r8, StatisticsCallback r9, LogRedirectionStrategy r10) {
        return new FFmpegSession(r6, r7, r8, r9, r10);
    }

    public void addStatistics(Statistics r3) {
        Object r0 = this.statisticsLock;
        monitor-enter(r0);
        this.statistics.add(r3);     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return;
    L7:
        th = move-exception;
        throw th;
    }

    public List<Statistics> getAllStatistics() {
        return getAllStatistics(5000);
    }

    public List<Statistics> getAllStatistics(int r4) {
        waitForAsynchronousMessagesInTransmit(r4);
        if (thereAreAsynchronousMessagesInTransmit() == false) goto L6;
        android.util.Log.i("ffmpeg-kit", String.format("getAllStatistics was called to return all statistics but there are still statistics being transmitted for session id %d.", new Object[]{Long.valueOf(this.sessionId)}));
    L6:
        return getStatistics();
    }

    public FFmpegSessionCompleteCallback getCompleteCallback() {
        return this.completeCallback;
    }

    public Statistics getLastReceivedStatistics() {
        Object r0 = this.statisticsLock;
        monitor-enter(r0);
    L12:
        th = move-exception;
        throw th;
    L5:
        if (this.statistics.size() <= 0) goto L9;
        Statistics r1 = this.statistics.get(this.statistics.size() - 1);     // Catch: Throwable -> L12
        monitor-exit(r0);     // Catch: Throwable -> L12
        return r1;
    L9:
        monitor-exit(r0);     // Catch: Throwable -> L12
        return null;
    }

    public List<Statistics> getStatistics() {
        Object r0 = this.statisticsLock;
        monitor-enter(r0);
        List<Statistics> r1 = this.statistics;     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return r1;
    L7:
        th = move-exception;
        throw th;
    }

    public StatisticsCallback getStatisticsCallback() {
        return this.statisticsCallback;
    }

    @Override // com.arthenica.ffmpegkit.Session
    public boolean isFFmpeg() {
        return true;
    }

    @Override // com.arthenica.ffmpegkit.Session
    public boolean isFFprobe() {
        return false;
    }

    @Override // com.arthenica.ffmpegkit.Session
    public boolean isMediaInformation() {
        return false;
    }

    public String toString() {
        return "FFmpegSession{sessionId=" + this.sessionId + ", createTime=" + this.createTime + ", startTime=" + this.startTime + ", endTime=" + this.endTime + ", arguments=" + FFmpegKitConfig.argumentsToString(this.arguments) + ", logs=" + getLogsAsString() + ", state=" + this.state + ", returnCode=" + this.returnCode + ", failStackTrace='" + this.failStackTrace + "'}";
    }
}
