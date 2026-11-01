package com.arthenica.ffmpegkit;

/* JADX INFO: loaded from: classes.dex */
public class FFprobeSession extends AbstractSession implements Session {
    private final FFprobeSessionCompleteCallback completeCallback;

    private FFprobeSession(String[] r1, FFprobeSessionCompleteCallback r2, LogCallback r3, LogRedirectionStrategy r4) {
        super(r1, r3, r4);
        this.completeCallback = r2;
    }

    public static FFprobeSession create(String[] r3) {
        return new FFprobeSession(r3, null, null, FFmpegKitConfig.getLogRedirectionStrategy());
    }

    public static FFprobeSession create(String[] r3, FFprobeSessionCompleteCallback r4) {
        return new FFprobeSession(r3, r4, null, FFmpegKitConfig.getLogRedirectionStrategy());
    }

    public static FFprobeSession create(String[] r2, FFprobeSessionCompleteCallback r3, LogCallback r4) {
        return new FFprobeSession(r2, r3, r4, FFmpegKitConfig.getLogRedirectionStrategy());
    }

    public static FFprobeSession create(String[] r1, FFprobeSessionCompleteCallback r2, LogCallback r3, LogRedirectionStrategy r4) {
        return new FFprobeSession(r1, r2, r3, r4);
    }

    public FFprobeSessionCompleteCallback getCompleteCallback() {
        return this.completeCallback;
    }

    @Override // com.arthenica.ffmpegkit.Session
    public boolean isFFmpeg() {
        return false;
    }

    @Override // com.arthenica.ffmpegkit.Session
    public boolean isFFprobe() {
        return true;
    }

    @Override // com.arthenica.ffmpegkit.Session
    public boolean isMediaInformation() {
        return false;
    }

    public String toString() {
        return "FFprobeSession{sessionId=" + this.sessionId + ", createTime=" + this.createTime + ", startTime=" + this.startTime + ", endTime=" + this.endTime + ", arguments=" + FFmpegKitConfig.argumentsToString(this.arguments) + ", logs=" + getLogsAsString() + ", state=" + this.state + ", returnCode=" + this.returnCode + ", failStackTrace='" + this.failStackTrace + "'}";
    }
}
