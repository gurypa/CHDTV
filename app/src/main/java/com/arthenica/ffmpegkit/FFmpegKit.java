package com.arthenica.ffmpegkit;

import java.util.List;
import java.util.concurrent.ExecutorService;

/* JADX INFO: loaded from: classes.dex */
public class FFmpegKit {
    static {
        AbiDetect.class.getName();
        FFmpegKitConfig.class.getName();
    }

    private FFmpegKit() {
    }

    public static void cancel() {
        FFmpegKitConfig.nativeFFmpegCancel(0);
    }

    public static void cancel(long r0) {
        FFmpegKitConfig.nativeFFmpegCancel(r0);
    }

    public static FFmpegSession execute(String r1) {
        return executeWithArguments(FFmpegKitConfig.parseArguments(r1));
    }

    public static FFmpegSession executeAsync(String r1, FFmpegSessionCompleteCallback r2) {
        return executeWithArgumentsAsync(FFmpegKitConfig.parseArguments(r1), r2);
    }

    public static FFmpegSession executeAsync(String r1, FFmpegSessionCompleteCallback r2, LogCallback r3, StatisticsCallback r4) {
        return executeWithArgumentsAsync(FFmpegKitConfig.parseArguments(r1), r2, r3, r4);
    }

    public static FFmpegSession executeAsync(String r1, FFmpegSessionCompleteCallback r2, LogCallback r3, StatisticsCallback r4, ExecutorService r5) {
        FFmpegSession r0 = FFmpegSession.create(FFmpegKitConfig.parseArguments(r1), r2, r3, r4);
        FFmpegKitConfig.asyncFFmpegExecute(r0, r5);
        return r0;
    }

    public static FFmpegSession executeAsync(String r1, FFmpegSessionCompleteCallback r2, ExecutorService r3) {
        FFmpegSession r0 = FFmpegSession.create(FFmpegKitConfig.parseArguments(r1), r2);
        FFmpegKitConfig.asyncFFmpegExecute(r0, r3);
        return r0;
    }

    public static FFmpegSession executeWithArguments(String[] r1) {
        FFmpegSession r0 = FFmpegSession.create(r1);
        FFmpegKitConfig.ffmpegExecute(r0);
        return r0;
    }

    public static FFmpegSession executeWithArgumentsAsync(String[] r1, FFmpegSessionCompleteCallback r2) {
        FFmpegSession r0 = FFmpegSession.create(r1, r2);
        FFmpegKitConfig.asyncFFmpegExecute(r0);
        return r0;
    }

    public static FFmpegSession executeWithArgumentsAsync(String[] r1, FFmpegSessionCompleteCallback r2, LogCallback r3, StatisticsCallback r4) {
        FFmpegSession r0 = FFmpegSession.create(r1, r2, r3, r4);
        FFmpegKitConfig.asyncFFmpegExecute(r0);
        return r0;
    }

    public static FFmpegSession executeWithArgumentsAsync(String[] r1, FFmpegSessionCompleteCallback r2, LogCallback r3, StatisticsCallback r4, ExecutorService r5) {
        FFmpegSession r0 = FFmpegSession.create(r1, r2, r3, r4);
        FFmpegKitConfig.asyncFFmpegExecute(r0, r5);
        return r0;
    }

    public static FFmpegSession executeWithArgumentsAsync(String[] r1, FFmpegSessionCompleteCallback r2, ExecutorService r3) {
        FFmpegSession r0 = FFmpegSession.create(r1, r2);
        FFmpegKitConfig.asyncFFmpegExecute(r0, r3);
        return r0;
    }

    public static List<FFmpegSession> listSessions() {
        return FFmpegKitConfig.getFFmpegSessions();
    }
}
