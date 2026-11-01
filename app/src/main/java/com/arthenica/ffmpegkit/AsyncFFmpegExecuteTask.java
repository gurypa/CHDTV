package com.arthenica.ffmpegkit;

import com.arthenica.smartexception.java.Exceptions;

/* JADX INFO: loaded from: classes.dex */
public class AsyncFFmpegExecuteTask implements Runnable {
    private final FFmpegSessionCompleteCallback completeCallback;
    private final FFmpegSession ffmpegSession;

    public AsyncFFmpegExecuteTask(FFmpegSession r2) {
        this.ffmpegSession = r2;
        this.completeCallback = r2.getCompleteCallback();
    }

    @Override // java.lang.Runnable
    public void run() {
        FFmpegKitConfig.ffmpegExecute(this.ffmpegSession);
        if (this.completeCallback != null) goto L15;
    L8:
        FFmpegSessionCompleteCallback r0 = FFmpegKitConfig.getFFmpegSessionCompleteCallback();
        if (r0 == null) goto L19;
        r0.apply(this.ffmpegSession);     // Catch: Exception -> L12
        return;
    L12:
        e = move-exception;
        android.util.Log.e("ffmpeg-kit", String.format("Exception thrown inside global complete callback.%s", new Object[]{Exceptions.getStackTraceString(e)}));
        return;
    L19:
        return;
    L15:
        this.completeCallback.apply(this.ffmpegSession);     // Catch: Exception -> L6
    L6:
        e = move-exception;
        android.util.Log.e("ffmpeg-kit", String.format("Exception thrown inside session complete callback.%s", new Object[]{Exceptions.getStackTraceString(e)}));
        goto L8
    }
}
