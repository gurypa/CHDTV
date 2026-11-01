package androidx.media3.exoplayer;

import android.os.HandlerThread;
import android.os.Looper;
import androidx.media3.common.util.Assertions;

/* JADX INFO: loaded from: classes.dex */
public final class PlaybackLooperProvider {
    private HandlerThread internalPlaybackThread;
    private final Object lock;
    private Looper playbackLooper;
    private int referenceCount;

    public PlaybackLooperProvider() {
        this(null);
    }

    public PlaybackLooperProvider(Looper r2) {
        this.lock = new Object();
        this.playbackLooper = r2;
        this.internalPlaybackThread = null;
        this.referenceCount = 0;
    }

    public Looper obtainLooper() {
        Object r0 = this.lock;
        monitor-enter(r0);
    L16:
        th = move-exception;
        throw th;
    L5:
        if (this.playbackLooper == null) goto L7;
    L13:
        this.referenceCount++;     // Catch: Throwable -> L16
        Looper r1 = this.playbackLooper;     // Catch: Throwable -> L16
        monitor-exit(r0);     // Catch: Throwable -> L16
        return r1;
    L7:
        if (this.referenceCount == 0) goto L9;
    L11:
        boolean r2 = false;
    L12:
        Assertions.checkState(r2);     // Catch: Throwable -> L16
        this.internalPlaybackThread = new HandlerThread("ExoPlayer:Playback", -16);     // Catch: Throwable -> L16
        this.internalPlaybackThread.start();     // Catch: Throwable -> L16
        this.playbackLooper = this.internalPlaybackThread.getLooper();     // Catch: Throwable -> L16
        goto L13
    L9:
        if (this.internalPlaybackThread != null) goto L11;
        r2 = true;
        goto L12
    }

    public void releaseLooper() {
        Object r0 = this.lock;
        monitor-enter(r0);
    L15:
        th = move-exception;
        throw th;
    L5:
        if (this.referenceCount <= 0) goto L7;
        boolean r1 = true;
    L8:
        Assertions.checkState(r1);     // Catch: Throwable -> L15
        this.referenceCount--;     // Catch: Throwable -> L15
        if (this.referenceCount == 0) goto L11;
    L13:
        monitor-exit(r0);     // Catch: Throwable -> L15
        return;
    L11:
        if (this.internalPlaybackThread == null) goto L13;
        this.internalPlaybackThread.quit();     // Catch: Throwable -> L15
        this.internalPlaybackThread = null;     // Catch: Throwable -> L15
        this.playbackLooper = null;     // Catch: Throwable -> L15
        goto L13
    L7:
        r1 = false;
        goto L8
    }
}
