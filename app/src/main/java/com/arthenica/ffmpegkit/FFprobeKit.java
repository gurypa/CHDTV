package com.arthenica.ffmpegkit;

import java.util.List;
import java.util.concurrent.ExecutorService;

/* JADX INFO: loaded from: classes.dex */
public class FFprobeKit {
    static {
        AbiDetect.class.getName();
        FFmpegKitConfig.class.getName();
    }

    private FFprobeKit() {
    }

    private static String[] defaultGetMediaInformationCommandArguments(String r10) {
        return new String[]{"-v", "error", "-hide_banner", "-print_format", "json", "-show_format", "-show_streams", "-show_chapters", "-i", r10};
    }

    public static FFprobeSession execute(String r1) {
        return executeWithArguments(FFmpegKitConfig.parseArguments(r1));
    }

    public static FFprobeSession executeAsync(String r1, FFprobeSessionCompleteCallback r2) {
        return executeWithArgumentsAsync(FFmpegKitConfig.parseArguments(r1), r2);
    }

    public static FFprobeSession executeAsync(String r1, FFprobeSessionCompleteCallback r2, LogCallback r3) {
        return executeWithArgumentsAsync(FFmpegKitConfig.parseArguments(r1), r2, r3);
    }

    public static FFprobeSession executeAsync(String r1, FFprobeSessionCompleteCallback r2, LogCallback r3, ExecutorService r4) {
        FFprobeSession r0 = FFprobeSession.create(FFmpegKitConfig.parseArguments(r1), r2, r3);
        FFmpegKitConfig.asyncFFprobeExecute(r0, r4);
        return r0;
    }

    public static FFprobeSession executeAsync(String r1, FFprobeSessionCompleteCallback r2, ExecutorService r3) {
        FFprobeSession r0 = FFprobeSession.create(FFmpegKitConfig.parseArguments(r1), r2);
        FFmpegKitConfig.asyncFFprobeExecute(r0, r3);
        return r0;
    }

    public static FFprobeSession executeWithArguments(String[] r1) {
        FFprobeSession r0 = FFprobeSession.create(r1);
        FFmpegKitConfig.ffprobeExecute(r0);
        return r0;
    }

    public static FFprobeSession executeWithArgumentsAsync(String[] r1, FFprobeSessionCompleteCallback r2) {
        FFprobeSession r0 = FFprobeSession.create(r1, r2);
        FFmpegKitConfig.asyncFFprobeExecute(r0);
        return r0;
    }

    public static FFprobeSession executeWithArgumentsAsync(String[] r1, FFprobeSessionCompleteCallback r2, LogCallback r3) {
        FFprobeSession r0 = FFprobeSession.create(r1, r2, r3);
        FFmpegKitConfig.asyncFFprobeExecute(r0);
        return r0;
    }

    public static FFprobeSession executeWithArgumentsAsync(String[] r1, FFprobeSessionCompleteCallback r2, LogCallback r3, ExecutorService r4) {
        FFprobeSession r0 = FFprobeSession.create(r1, r2, r3);
        FFmpegKitConfig.asyncFFprobeExecute(r0, r4);
        return r0;
    }

    public static FFprobeSession executeWithArgumentsAsync(String[] r1, FFprobeSessionCompleteCallback r2, ExecutorService r3) {
        FFprobeSession r0 = FFprobeSession.create(r1, r2);
        FFmpegKitConfig.asyncFFprobeExecute(r0, r3);
        return r0;
    }

    public static MediaInformationSession getMediaInformation(String r2) {
        MediaInformationSession r0 = MediaInformationSession.create(defaultGetMediaInformationCommandArguments(r2));
        FFmpegKitConfig.getMediaInformationExecute(r0, 5000);
        return r0;
    }

    public static MediaInformationSession getMediaInformation(String r1, int r2) {
        MediaInformationSession r0 = MediaInformationSession.create(defaultGetMediaInformationCommandArguments(r1));
        FFmpegKitConfig.getMediaInformationExecute(r0, r2);
        return r0;
    }

    public static MediaInformationSession getMediaInformationAsync(String r2, MediaInformationSessionCompleteCallback r3) {
        MediaInformationSession r0 = MediaInformationSession.create(defaultGetMediaInformationCommandArguments(r2), r3);
        FFmpegKitConfig.asyncGetMediaInformationExecute(r0, 5000);
        return r0;
    }

    public static MediaInformationSession getMediaInformationAsync(String r1, MediaInformationSessionCompleteCallback r2, LogCallback r3, int r4) {
        MediaInformationSession r0 = MediaInformationSession.create(defaultGetMediaInformationCommandArguments(r1), r2, r3);
        FFmpegKitConfig.asyncGetMediaInformationExecute(r0, r4);
        return r0;
    }

    public static MediaInformationSession getMediaInformationAsync(String r1, MediaInformationSessionCompleteCallback r2, LogCallback r3, ExecutorService r4, int r5) {
        MediaInformationSession r0 = MediaInformationSession.create(defaultGetMediaInformationCommandArguments(r1), r2, r3);
        FFmpegKitConfig.asyncGetMediaInformationExecute(r0, r4, r5);
        return r0;
    }

    public static MediaInformationSession getMediaInformationAsync(String r2, MediaInformationSessionCompleteCallback r3, ExecutorService r4) {
        MediaInformationSession r0 = MediaInformationSession.create(defaultGetMediaInformationCommandArguments(r2), r3);
        FFmpegKitConfig.asyncGetMediaInformationExecute(r0, r4, 5000);
        return r0;
    }

    public static MediaInformationSession getMediaInformationFromCommand(String r2) {
        MediaInformationSession r0 = MediaInformationSession.create(FFmpegKitConfig.parseArguments(r2));
        FFmpegKitConfig.getMediaInformationExecute(r0, 5000);
        return r0;
    }

    private static MediaInformationSession getMediaInformationFromCommandArgumentsAsync(String[] r1, MediaInformationSessionCompleteCallback r2, LogCallback r3, int r4) {
        MediaInformationSession r0 = MediaInformationSession.create(r1, r2, r3);
        FFmpegKitConfig.asyncGetMediaInformationExecute(r0, r4);
        return r0;
    }

    public static MediaInformationSession getMediaInformationFromCommandAsync(String r1, MediaInformationSessionCompleteCallback r2, LogCallback r3, int r4) {
        return getMediaInformationFromCommandArgumentsAsync(FFmpegKitConfig.parseArguments(r1), r2, r3, r4);
    }

    public static List<FFprobeSession> listFFprobeSessions() {
        return FFmpegKitConfig.getFFprobeSessions();
    }

    public static List<MediaInformationSession> listMediaInformationSessions() {
        return FFmpegKitConfig.getMediaInformationSessions();
    }
}
