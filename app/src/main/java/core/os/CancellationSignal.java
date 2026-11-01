package androidx.core.os;

/* JADX INFO: loaded from: classes.dex */
public final class CancellationSignal {
    private boolean mCancelInProgress;
    private Object mCancellationSignalObj;
    private boolean mIsCanceled;
    private OnCancelListener mOnCancelListener;

    static class Api16Impl {
        private Api16Impl() {
        }

        static void cancel(Object r1) {
            ((android.os.CancellationSignal) r1).cancel();
        }

        static android.os.CancellationSignal createCancellationSignal() {
            return new android.os.CancellationSignal();
        }
    }

    public interface OnCancelListener {
        void onCancel();
    }

    public CancellationSignal() {
    }

    private void waitForCancelFinishedLocked() {
    L3:
        if (this.mCancelInProgress == false) goto L8;
        wait();     // Catch: InterruptedException -> L6
        goto L3
    }

    public void cancel() {
        monitor-enter(this);
    L31:
        th = move-exception;
        throw th;
    L4:
        if (this.mIsCanceled == false) goto L7;
        monitor-exit(this);     // Catch: Throwable -> L31
        return;
    L7:
        this.mIsCanceled = true;     // Catch: Throwable -> L31
        this.mCancelInProgress = true;     // Catch: Throwable -> L31
        OnCancelListener r0 = this.mOnCancelListener;     // Catch: Throwable -> L31
        Object r1 = this.mCancellationSignalObj;     // Catch: Throwable -> L31
        monitor-exit(this);     // Catch: Throwable -> L31
        if (r0 != null) goto L39;
    L14:
        if (r1 == null) goto L24;
        Api16Impl.cancel(r1);     // Catch: Throwable -> L12
    L12:
        th = move-exception;
        monitor-enter(this);
        this.mCancelInProgress = false;     // Catch: Throwable -> L21
        notifyAll();     // Catch: Throwable -> L21
        throw th;
    L21:
        th = move-exception;
        throw th;
    L24:
        monitor-enter(this);
        this.mCancelInProgress = false;     // Catch: Throwable -> L28
        notifyAll();     // Catch: Throwable -> L28
        monitor-exit(this);     // Catch: Throwable -> L28
        return;
    L28:
        th = move-exception;
        throw th;
    L39:
        r0.onCancel();     // Catch: Throwable -> L12
        goto L14
    }

    public Object getCancellationSignalObject() {
        monitor-enter(this);
    L11:
        th = move-exception;
        throw th;
    L4:
        if (this.mCancellationSignalObj != null) goto L8;
        this.mCancellationSignalObj = Api16Impl.createCancellationSignal();     // Catch: Throwable -> L11
        if (this.mIsCanceled == false) goto L8;
        Api16Impl.cancel(this.mCancellationSignalObj);     // Catch: Throwable -> L11
    L8:
        Object r0 = this.mCancellationSignalObj;     // Catch: Throwable -> L11
        monitor-exit(this);     // Catch: Throwable -> L11
        return r0;
    }

    public boolean isCanceled() {
        monitor-enter(this);
        boolean r0 = this.mIsCanceled;     // Catch: Throwable -> L6
        monitor-exit(this);     // Catch: Throwable -> L6
        return r0;
    L6:
        th = move-exception;
        throw th;
    }

    public void setOnCancelListener(OnCancelListener r2) {
        monitor-enter(this);
        waitForCancelFinishedLocked();     // Catch: Throwable -> L16
        if (this.mOnCancelListener != r2) goto L7;
        monitor-exit(this);     // Catch: Throwable -> L16
        return;
    L7:
        this.mOnCancelListener = r2;     // Catch: Throwable -> L16
        if (this.mIsCanceled == false) goto L14;
        if (r2 == null) goto L14;
        monitor-exit(this);     // Catch: Throwable -> L16
        r2.onCancel();
        return;
    L14:
        monitor-exit(this);     // Catch: Throwable -> L16
        return;
    L16:
        th = move-exception;
        throw th;
    }

    public void throwIfCanceled() {
        if (isCanceled() == true) goto L6;
        return;
    L6:
        throw new OperationCanceledException();
    }
}
