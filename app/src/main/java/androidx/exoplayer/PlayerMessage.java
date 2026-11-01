package androidx.media3.exoplayer;

import android.os.Looper;
import androidx.media3.common.C0180C;
import androidx.media3.common.IllegalSeekPositionException;
import androidx.media3.common.Timeline;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Clock;
import java.util.concurrent.TimeoutException;

/* JADX INFO: loaded from: classes.dex */
public final class PlayerMessage {
    private final Clock clock;
    private boolean deleteAfterDelivery;
    private boolean isCanceled;
    private boolean isDelivered;
    private boolean isProcessed;
    private boolean isSent;
    private Looper looper;
    private int mediaItemIndex;
    private Object payload;
    private long positionMs;
    private final Sender sender;
    private final Target target;
    private final Timeline timeline;
    private int type;

    public interface Sender {
        void sendMessage(PlayerMessage r1);
    }

    public interface Target {
        void handleMessage(int r1, Object r2) throws ExoPlaybackException;
    }

    public PlayerMessage(Sender r3, Target r4, Timeline r5, int r6, Clock r7, Looper r8) {
        this.sender = r3;
        this.target = r4;
        this.timeline = r5;
        this.looper = r8;
        this.clock = r7;
        this.mediaItemIndex = r6;
        this.positionMs = C0180C.TIME_UNSET;
        this.deleteAfterDelivery = true;
    }

    public synchronized boolean blockUntilDelivered() throws InterruptedException {
        monitor-enter(this);
        Assertions.checkState(this.isSent);     // Catch: Throwable -> L14
        if (this.looper.getThread() == Thread.currentThread()) goto L6;
        boolean r0 = true;
    L7:
        Assertions.checkState(r0);     // Catch: Throwable -> L14
    L9:
        if (this.isProcessed == true) goto L11;
        wait();     // Catch: Throwable -> L14
        goto L9
    L11:
        boolean r1 = this.isDelivered;     // Catch: Throwable -> L14
        monitor-exit(this);
        return r1;
    L6:
        r0 = false;
    L14:
        th = move-exception;
        throw th;
    }

    public synchronized boolean blockUntilDelivered(long r7) throws InterruptedException, TimeoutException {
        monitor-enter(this);
        Assertions.checkState(this.isSent);     // Catch: Throwable -> L20
        if (this.looper.getThread() == Thread.currentThread()) goto L6;
        boolean r0 = true;
    L7:
        Assertions.checkState(r0);     // Catch: Throwable -> L20
        long r1 = this.clock.elapsedRealtime() + r7;     // Catch: Throwable -> L20
        long r2 = r7;
    L9:
        if (this.isProcessed == true) goto L14;
        if (r2 <= 0) goto L14;
        this.clock.onThreadBlocked();     // Catch: Throwable -> L20
        wait(r2);     // Catch: Throwable -> L20
        r2 = r1 - this.clock.elapsedRealtime();     // Catch: Throwable -> L20
    L14:
        if (this.isProcessed == false) goto L19;
        boolean r4 = this.isDelivered;     // Catch: Throwable -> L20
        monitor-exit(this);
        return r4;
    L19:
        throw new TimeoutException("Message delivery timed out.");     // Catch: Throwable -> L20
    L6:
        r0 = false;
    L20:
        th = move-exception;
        throw th;
    }

    public synchronized PlayerMessage cancel() {
        monitor-enter(this);
        Assertions.checkState(this.isSent);     // Catch: Throwable -> L6
        this.isCanceled = true;     // Catch: Throwable -> L6
        markAsProcessed(false);     // Catch: Throwable -> L6
        monitor-exit(this);
        return this;
    L6:
        th = move-exception;
        throw th;
    }

    public boolean getDeleteAfterDelivery() {
        return this.deleteAfterDelivery;
    }

    public Looper getLooper() {
        return this.looper;
    }

    public int getMediaItemIndex() {
        return this.mediaItemIndex;
    }

    public Object getPayload() {
        return this.payload;
    }

    public long getPositionMs() {
        return this.positionMs;
    }

    public Target getTarget() {
        return this.target;
    }

    public Timeline getTimeline() {
        return this.timeline;
    }

    public int getType() {
        return this.type;
    }

    public synchronized boolean isCanceled() {
        monitor-enter(this);
        boolean r0 = this.isCanceled;     // Catch: Throwable -> L6
        monitor-exit(this);
        return r0;
    L6:
        th = move-exception;
        throw th;
    }

    public synchronized void markAsProcessed(boolean r2) {
        monitor-enter(this);
        this.isDelivered |= r2;     // Catch: Throwable -> L6
        this.isProcessed = true;     // Catch: Throwable -> L6
        notifyAll();     // Catch: Throwable -> L6
        monitor-exit(this);
        return;
    L6:
        th = move-exception;
        throw th;
    }

    public PlayerMessage send() {
        Assertions.checkState(!this.isSent);
        if (this.positionMs != C0180C.TIME_UNSET) goto L5;
        Assertions.checkArgument(this.deleteAfterDelivery);
    L5:
        this.isSent = true;
        this.sender.sendMessage(this);
        return this;
    }

    public PlayerMessage setDeleteAfterDelivery(boolean r2) {
        Assertions.checkState(!this.isSent);
        this.deleteAfterDelivery = r2;
        return this;
    }

    public PlayerMessage setLooper(Looper r2) {
        Assertions.checkState(!this.isSent);
        this.looper = r2;
        return this;
    }

    public PlayerMessage setPayload(Object r2) {
        Assertions.checkState(!this.isSent);
        this.payload = r2;
        return this;
    }

    public PlayerMessage setPosition(int r5, long r6) {
        boolean r1 = true;
        Assertions.checkState(!this.isSent);
        if (r6 != C0180C.TIME_UNSET) goto L6;
        r1 = false;
    L6:
        Assertions.checkArgument(r1);
        if (r5 < 0) goto L15;
        if (this.timeline.isEmpty() == false) goto L11;
    L12:
        this.mediaItemIndex = r5;
        this.positionMs = r6;
        return this;
    L11:
        if (r5 < this.timeline.getWindowCount()) goto L12;
    L15:
        throw new IllegalSeekPositionException(this.timeline, r5, r6);
    }

    public PlayerMessage setPosition(long r2) {
        Assertions.checkState(!this.isSent);
        this.positionMs = r2;
        return this;
    }

    public PlayerMessage setType(int r2) {
        Assertions.checkState(!this.isSent);
        this.type = r2;
        return this;
    }
}
