package com.arthenica.ffmpegkit;

import com.arthenica.smartexception.java.Exceptions;

/* JADX INFO: loaded from: classes.dex */
public class AsyncGetMediaInformationTask implements Runnable {
    private final MediaInformationSessionCompleteCallback completeCallback;
    private final MediaInformationSession mediaInformationSession;
    private final Integer waitTimeout;

    public AsyncGetMediaInformationTask(MediaInformationSession r2) {
        this(r2, 5000);
    }

    public AsyncGetMediaInformationTask(MediaInformationSession r2, Integer r3) {
        this.mediaInformationSession = r2;
        this.completeCallback = r2.getCompleteCallback();
        this.waitTimeout = r3;
    }

    @Override // java.lang.Runnable
    public void run() {
        FFmpegKitConfig.getMediaInformationExecute(this.mediaInformationSession, this.waitTimeout.intValue());
        if (this.completeCallback != null) goto L15;
    L8:
        MediaInformationSessionCompleteCallback r0 = FFmpegKitConfig.getMediaInformationSessionCompleteCallback();
        if (r0 == null) goto L19;
        r0.apply(this.mediaInformationSession);     // Catch: Exception -> L12
        return;
    L12:
        e = move-exception;
        android.util.Log.e("ffmpeg-kit", String.format("Exception thrown inside global complete callback.%s", new Object[]{Exceptions.getStackTraceString(e)}));
        return;
    L19:
        return;
    L15:
        this.completeCallback.apply(this.mediaInformationSession);     // Catch: Exception -> L6
    L6:
        e = move-exception;
        android.util.Log.e("ffmpeg-kit", String.format("Exception thrown inside session complete callback.%s", new Object[]{Exceptions.getStackTraceString(e)}));
        goto L8
    }
}
