package com.arthenica.ffmpegkit;

/* JADX INFO: loaded from: classes.dex */
public class MediaInformationSession extends AbstractSession implements Session {
    private final MediaInformationSessionCompleteCallback completeCallback;
    private MediaInformation mediaInformation;

    private MediaInformationSession(String[] r2, MediaInformationSessionCompleteCallback r3, LogCallback r4) {
        super(r2, r4, LogRedirectionStrategy.NEVER_PRINT_LOGS);
        this.completeCallback = r3;
    }

    public static MediaInformationSession create(String[] r2) {
        return new MediaInformationSession(r2, null, null);
    }

    public static MediaInformationSession create(String[] r2, MediaInformationSessionCompleteCallback r3) {
        return new MediaInformationSession(r2, r3, null);
    }

    public static MediaInformationSession create(String[] r1, MediaInformationSessionCompleteCallback r2, LogCallback r3) {
        return new MediaInformationSession(r1, r2, r3);
    }

    public MediaInformationSessionCompleteCallback getCompleteCallback() {
        return this.completeCallback;
    }

    public MediaInformation getMediaInformation() {
        return this.mediaInformation;
    }

    @Override // com.arthenica.ffmpegkit.Session
    public boolean isFFmpeg() {
        return false;
    }

    @Override // com.arthenica.ffmpegkit.Session
    public boolean isFFprobe() {
        return false;
    }

    @Override // com.arthenica.ffmpegkit.Session
    public boolean isMediaInformation() {
        return true;
    }

    public void setMediaInformation(MediaInformation r1) {
        this.mediaInformation = r1;
    }

    public String toString() {
        return "MediaInformationSession{sessionId=" + this.sessionId + ", createTime=" + this.createTime + ", startTime=" + this.startTime + ", endTime=" + this.endTime + ", arguments=" + FFmpegKitConfig.argumentsToString(this.arguments) + ", logs=" + getLogsAsString() + ", state=" + this.state + ", returnCode=" + this.returnCode + ", failStackTrace='" + this.failStackTrace + "'}";
    }
}
