package androidx.media3.common;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import com.google.common.collect.ImmutableList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class BundleListRetriever extends Binder {
    private static final int REPLY_BREAK = 2;
    private static final int REPLY_CONTINUE = 1;
    private static final int REPLY_END_OF_LIST = 0;
    private static final int SUGGESTED_MAX_IPC_SIZE = 0;
    private final ImmutableList<Bundle> list;

    static {
        if (Util.SDK_INT < 30) goto L5;
        int r0 = IBinder.getSuggestedMaxIpcSizeBytes();
    L6:
        SUGGESTED_MAX_IPC_SIZE = r0;
        return;
    L5:
        r0 = 65536;
        goto L6
    }

    public BundleListRetriever(List<Bundle> r2) {
        this.list = ImmutableList.copyOf(r2);
    }

    public static ImmutableList<Bundle> getList(IBinder r1) {
        if ((r1 instanceof BundleListRetriever) == false) goto L7;
        return ((BundleListRetriever) r1).list;
    L7:
        return getListFromRemoteBinder(r1);
    }

    static ImmutableList<Bundle> getListFromRemoteBinder(IBinder r7) {
        ImmutableList.Builder r0 = ImmutableList.builder();
        int r1 = 0;
        int r2 = 1;
    L3:
        if (r2 == 0) goto L20;
        Parcel r3 = Parcel.obtain();
        Parcel r4 = Parcel.obtain();
        r3.writeInt(r1);     // Catch: Throwable -> L16
        r7.transact(1, r3, r4, 0);     // Catch: RemoteException -> L13 Throwable -> L16
    L8:
        int r5 = r4.readInt();     // Catch: Throwable -> L16
        r2 = r5;
        if (r5 != 1) goto L12;
        r0.add((Bundle) Assertions.checkNotNull(r4.readBundle()));     // Catch: Throwable -> L16
        r1 = r1 + 1;
        goto L8
    L12:
        r4.recycle();
        r3.recycle();
        goto L3
    L13:
        e = move-exception;
        throw new RuntimeException(e);     // Catch: Throwable -> L16
    L16:
        th = move-exception;
        r4.recycle();
        r3.recycle();
        throw th;
    L20:
        return r0.build();
    }

    @Override // android.os.Binder
    protected boolean onTransact(int r7, Parcel r8, Parcel r9, int r10) throws RemoteException {
        if (r7 != 1) goto L5;
        int r1 = 0;
        if (r9 != null) goto L9;
        return false;
    L9:
        int r2 = this.list.size();
        int r3 = r8.readInt();
    L10:
        if (r3 >= r2) goto L14;
        if (r9.dataSize() >= SUGGESTED_MAX_IPC_SIZE) goto L14;
        r9.writeInt(1);
        r9.writeBundle(this.list.get(r3));
        r3 = r3 + 1;
    L14:
        if (r3 >= r2) goto L16;
        r1 = 2;
    L16:
        r9.writeInt(r1);
        return true;
    L5:
        return super.onTransact(r7, r8, r9, r10);
    }
}
