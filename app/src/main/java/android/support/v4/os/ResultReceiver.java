package android.support.v4.os;

import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public class ResultReceiver implements Parcelable {
    public static final Parcelable.Creator<ResultReceiver> CREATOR = null;
    final Handler mHandler;
    final boolean mLocal;
    IResultReceiver mReceiver;

    class MyResultReceiver extends IResultReceiver.Stub {
        final /* synthetic */ ResultReceiver this$0;

        MyResultReceiver(ResultReceiver r1) {
            this.this$0 = r1;
        }

        @Override // android.support.v4.os.IResultReceiver
        public void send(int r4, Bundle r5) {
            if (this.this$0.mHandler == null) goto L5;
            this.this$0.mHandler.post(new MyRunnable(this.this$0, r4, r5));
            return;
        L5:
            this.this$0.onReceiveResult(r4, r5);
        }
    }

    class MyRunnable implements Runnable {
        final int mResultCode;
        final Bundle mResultData;
        final /* synthetic */ ResultReceiver this$0;

        MyRunnable(ResultReceiver r1, int r2, Bundle r3) {
            this.this$0 = r1;
            this.mResultCode = r2;
            this.mResultData = r3;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.this$0.onReceiveResult(this.mResultCode, this.mResultData);
        }
    }

    static {
        CREATOR = new C00291();
    }

    public ResultReceiver(Handler r2) {
        this.mLocal = true;
        this.mHandler = r2;
    }

    ResultReceiver(Parcel r2) {
        this.mLocal = false;
        this.mHandler = null;
        this.mReceiver = IResultReceiver.Stub.asInterface(r2.readStrongBinder());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    protected void onReceiveResult(int r1, Bundle r2) {
    }

    public void send(int r3, Bundle r4) {
        if (this.mLocal == false) goto L10;
        if (this.mHandler == null) goto L7;
        this.mHandler.post(new MyRunnable(this, r3, r4));
        return;
    L7:
        onReceiveResult(r3, r4);
        return;
    L10:
        if (this.mReceiver == null) goto L18;
        this.mReceiver.send(r3, r4);     // Catch: RemoteException -> L13
        return;
    L14:
        return;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel r2, int r3) {
        monitor-enter(this);
    L9:
        th = move-exception;
        throw th;
    L4:
        if (this.mReceiver != null) goto L6;
        this.mReceiver = new MyResultReceiver(this);     // Catch: Throwable -> L9
    L6:
        r2.writeStrongBinder(this.mReceiver.asBinder());     // Catch: Throwable -> L9
        monitor-exit(this);     // Catch: Throwable -> L9
    }
}
