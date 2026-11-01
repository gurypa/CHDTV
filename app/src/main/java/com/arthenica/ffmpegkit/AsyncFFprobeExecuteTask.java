package com.arthenica.ffmpegkit;

import com.arthenica.smartexception.java.Exceptions;

/* JADX INFO: loaded from: classes.dex */
public class AsyncFFprobeExecuteTask implements Runnable {
    private final FFprobeSessionCompleteCallback completeCallback;
    private final FFprobeSession ffprobeSession;

    public AsyncFFprobeExecuteTask(FFprobeSession r2) {
        this.ffprobeSession = r2;
        this.completeCallback = r2.getCompleteCallback();
    }

    @Override // java.lang.Runnable
    public void run() {
        FFmpegKitConfig.ffprobeExecute(this.ffprobeSession);
        if (this.completeCallback != null) goto L15;
    L8:
        FFprobeSessionCompleteCallback r0 = FFmpegKitConfig.getFFprobeSessionCompleteCallback();
        if (r0 == null) goto L19;
        r0.apply(this.ffprobeSession);     // Catch: Exception -> L12
        return;
    L12:
        e = move-exception;
        android.util.Log.e("ffmpeg-kit", String.format("Exception thrown inside global complete callback.%s", new Object[]{Exceptions.getStackTraceString(e)}));
        return;
    L19:
        return;
    L15:
        this.completeCallback.apply(this.ffprobeSession);     // Catch: Exception -> L6
    L6:
        e = move-exception;
        android.util.Log.e("ffmpeg-kit", String.format("Exception thrown inside session complete callback.%s", new Object[]{Exceptions.getStackTraceString(e)}));
        goto L8
    }
}
