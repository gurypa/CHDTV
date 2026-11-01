package androidx.media3.datasource;

import android.content.Context;

/* JADX INFO: loaded from: classes.dex */
@Deprecated
public final class DefaultDataSourceFactory implements DataSource.Factory {
    private final DataSource.Factory baseDataSourceFactory;
    private final Context context;
    private final TransferListener listener;

    public DefaultDataSourceFactory(Context r3) {
        String r1 = (String) null;
        this(r3, null, null);
    }

    public DefaultDataSourceFactory(Context r2, DataSource.Factory r3) {
        this(r2, null, r3);
    }

    public DefaultDataSourceFactory(Context r2, TransferListener r3, DataSource.Factory r4) {
        this.context = r2.getApplicationContext();
        this.listener = r3;
        this.baseDataSourceFactory = r4;
    }

    public DefaultDataSourceFactory(Context r2, String r3) {
        this(r2, r3, null);
    }

    public DefaultDataSourceFactory(Context r2, String r3, TransferListener r4) {
        this(r2, r4, new DefaultHttpDataSource.Factory().setUserAgent(r3));
    }

    @Override // androidx.media3.datasource.DataSource.Factory
    public /* bridge */ /* synthetic */ DataSource createDataSource() {
        return createDataSource();
    }

    @Override // androidx.media3.datasource.DataSource.Factory
    public DefaultDataSource createDataSource() {
        DefaultDataSource r0 = new DefaultDataSource(this.context, this.baseDataSourceFactory.createDataSource());
        if (this.listener == null) goto L5;
        r0.addTransferListener(this.listener);
    L5:
        return r0;
    }
}
