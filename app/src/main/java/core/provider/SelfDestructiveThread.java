package androidx.core.provider;

import android.os.Handler;
import android.os.HandlerThread;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/* JADX INFO: loaded from: classes.dex */
@Deprecated
public class SelfDestructiveThread {
    private static final int MSG_DESTRUCTION = 0;
    private static final int MSG_INVOKE_RUNNABLE = 1;
    private Handler.Callback mCallback;
    private final int mDestructAfterMillisec;
    private int mGeneration;
    private Handler mHandler;
    private final Object mLock;
    private final int mPriority;
    private HandlerThread mThread;
    private final String mThreadName;

    public interface ReplyCallback<T> {
        void onReply(T r1);
    }

    public SelfDestructiveThread(String r2, int r3, int r4) {
        this.mLock = new Object();
        this.mCallback = new C00891(this);
        this.mThreadName = r2;
        this.mPriority = r3;
        this.mDestructAfterMillisec = r4;
        this.mGeneration = 0;
    }

    private void post(Runnable r6) {
        Object r0 = this.mLock;
        monitor-enter(r0);
    L10:
        th = move-exception;
        throw th;
    L5:
        if (this.mThread != null) goto L7;
        this.mThread = new HandlerThread(this.mThreadName, this.mPriority);     // Catch: Throwable -> L10
        this.mThread.start();     // Catch: Throwable -> L10
        this.mHandler = new Handler(this.mThread.getLooper(), this.mCallback);     // Catch: Throwable -> L10
        this.mGeneration++;     // Catch: Throwable -> L10
    L7:
        this.mHandler.removeMessages(0);     // Catch: Throwable -> L10
        this.mHandler.sendMessage(this.mHandler.obtainMessage(1, r6));     // Catch: Throwable -> L10
        monitor-exit(r0);     // Catch: Throwable -> L10
    }

    public int getGeneration() {
        Object r0 = this.mLock;
        monitor-enter(r0);
        int r1 = this.mGeneration;     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return r1;
    L7:
        th = move-exception;
        throw th;
    }

    public boolean isRunning() {
        Object r0 = this.mLock;
        monitor-enter(r0);
    L10:
        th = move-exception;
        throw th;
    L5:
        if (this.mThread == null) goto L7;
        boolean r1 = true;
    L8:
        monitor-exit(r0);     // Catch: Throwable -> L10
        return r1;
    L7:
        r1 = false;
        goto L8
    }

    void onDestruction() {
        Object r0 = this.mLock;
        monitor-enter(r0);
    L11:
        th = move-exception;
        throw th;
    L5:
        if (this.mHandler.hasMessages(1) == false) goto L8;
        monitor-exit(r0);     // Catch: Throwable -> L11
        return;
    L8:
        this.mThread.quit();     // Catch: Throwable -> L11
        this.mThread = null;     // Catch: Throwable -> L11
        this.mHandler = null;     // Catch: Throwable -> L11
        monitor-exit(r0);     // Catch: Throwable -> L11
    }

    void onInvokeRunnable(Runnable r6) {
        r6.run();
        Object r0 = this.mLock;
        monitor-enter(r0);
        this.mHandler.removeMessages(0);     // Catch: Throwable -> L7
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(0), this.mDestructAfterMillisec);     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return;
    L7:
        th = move-exception;
        throw th;
    }

    public <T> void postAndReply(final Callable<T> r3, final ReplyCallback<T> r4) {
        final Handler r0 = CalleeHandler.create();
        post(new RunnableC00902(this, r3, r0, r4));
    }

    public <T> T postAndWait(final Callable<T> r11, int r12) throws InterruptedException {
        final ReentrantLock r0 = new ReentrantLock();
        final Condition r7 = r0.newCondition();
        final AtomicReference r3 = new AtomicReference();
        final AtomicBoolean r6 = new AtomicBoolean(true);
        post(new RunnableC00913(this, r3, r11, r0, r6, r7));
        r0.lock();
    L23:
        th = move-exception;
        r0.unlock();
        throw th;
    L4:
        if (r6.get() == true) goto L9;
        T r13 = (T) r3.get();
        r0.unlock();
        return r13;
    L9:
        long r8 = TimeUnit.MILLISECONDS.toNanos(r12);     // Catch: Throwable -> L23
    L26:
        r8 = r7.awaitNanos(r8);     // Catch: InterruptedException -> L12 Throwable -> L23
    L14:
        if (r6.get() == false) goto L15;
        if (r8 > 0) goto L26;
        throw new InterruptedException("timeout");     // Catch: Throwable -> L23
    L15:
        T r14 = (T) r3.get();
        r0.unlock();
        return r14;
    }
}
