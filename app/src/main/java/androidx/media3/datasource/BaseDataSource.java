package androidx.media3.datasource;

import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import java.util.ArrayList;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public abstract class BaseDataSource implements DataSource {
    private DataSpec dataSpec;
    private final boolean isNetwork;
    private int listenerCount;
    private final ArrayList<TransferListener> listeners;

    protected BaseDataSource(boolean r3) {
        this.isNetwork = r3;
        this.listeners = new ArrayList(1);
    }

    @Override // androidx.media3.datasource.DataSource
    public final void addTransferListener(TransferListener r2) {
        Assertions.checkNotNull(r2);
        if (this.listeners.contains(r2) == true) goto L6;
        this.listeners.add(r2);
        this.listenerCount++;
        return;
    }

    protected final void bytesTransferred(int r5) {
        DataSpec r0 = (DataSpec) Util.castNonNull(this.dataSpec);
        int r1 = 0;
    L4:
        if (r1 >= this.listenerCount) goto L6;
        this.listeners.get(r1).onBytesTransferred(this, r0, this.isNetwork, r5);
        r1 = r1 + 1;
        goto L4
    }

    @Override // androidx.media3.datasource.DataSource
    public /* synthetic */ Map getResponseHeaders() {
        return DataSource.CC.$default$getResponseHeaders(this);
    }

    protected final void transferEnded() {
        DataSpec r0 = (DataSpec) Util.castNonNull(this.dataSpec);
        int r1 = 0;
    L4:
        if (r1 >= this.listenerCount) goto L6;
        this.listeners.get(r1).onTransferEnd(this, r0, this.isNetwork);
        r1 = r1 + 1;
        goto L4
    L6:
        this.dataSpec = null;
    }

    protected final void transferInitializing(DataSpec r4) {
        int r0 = 0;
    L4:
        if (r0 >= this.listenerCount) goto L6;
        this.listeners.get(r0).onTransferInitializing(this, r4, this.isNetwork);
        r0 = r0 + 1;
        goto L4
    }

    protected final void transferStarted(DataSpec r4) {
        this.dataSpec = r4;
        int r0 = 0;
    L4:
        if (r0 >= this.listenerCount) goto L6;
        this.listeners.get(r0).onTransferStart(this, r4, this.isNetwork);
        r0 = r0 + 1;
        goto L4
    }
}
