package androidx.media3.datasource;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import androidx.media3.common.MediaMetadata;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.BitmapLoader;
import androidx.media3.common.util.Util;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import java.io.IOException;
import java.util.concurrent.Executors;

/* JADX INFO: loaded from: classes.dex */
public final class DataSourceBitmapLoader implements BitmapLoader {
    public static final Supplier<ListeningExecutorService> DEFAULT_EXECUTOR_SERVICE = null;
    private final DataSource.Factory dataSourceFactory;
    private final ListeningExecutorService listeningExecutorService;
    private final int maximumOutputDimension;
    private final BitmapFactory.Options options;

    static {
        DEFAULT_EXECUTOR_SERVICE = Suppliers.memoize(new DataSourceBitmapLoader$$ExternalSyntheticLambda0());
    }

    public DataSourceBitmapLoader(Context r3) {
        this((ListeningExecutorService) Assertions.checkStateNotNull(DEFAULT_EXECUTOR_SERVICE.get()), new DefaultDataSource.Factory(r3));
    }

    public DataSourceBitmapLoader(ListeningExecutorService r2, DataSource.Factory r3) {
        this(r2, r3, null);
    }

    public DataSourceBitmapLoader(ListeningExecutorService r2, DataSource.Factory r3, BitmapFactory.Options r4) {
        this(r2, r3, r4, -1);
    }

    public DataSourceBitmapLoader(ListeningExecutorService r1, DataSource.Factory r2, BitmapFactory.Options r3, int r4) {
        this.listeningExecutorService = r1;
        this.dataSourceFactory = r2;
        this.options = r3;
        this.maximumOutputDimension = r4;
    }

    static /* synthetic */ ListeningExecutorService lambda$static$0() {
        return MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor());
    }

    private static Bitmap load(DataSource r3, Uri r4, BitmapFactory.Options r5, int r6) throws IOException {
        r3.open(new DataSpec(r4));     // Catch: Throwable -> L5
        byte[] r1 = DataSourceUtil.readToEnd(r3);     // Catch: Throwable -> L5
        Bitmap r2 = BitmapUtil.decode(r1, r1.length, r5, r6);     // Catch: Throwable -> L5
        r3.close();
        return r2;
    L5:
        th = move-exception;
        r3.close();
        throw th;
    }

    @Override // androidx.media3.common.util.BitmapLoader
    public ListenableFuture<Bitmap> decodeBitmap(final byte[] r3) {
        return this.listeningExecutorService.submit(new DataSourceBitmapLoader$$ExternalSyntheticLambda1(this, r3));
    }

    /* JADX INFO: renamed from: lambda$decodeBitmap$1$androidx-media3-datasource-DataSourceBitmapLoader */
    /* synthetic */ Bitmap m56xcc09b2d8(byte[] r4) throws Exception {
        return BitmapUtil.decode(r4, r4.length, this.options, this.maximumOutputDimension);
    }

    /* JADX INFO: renamed from: lambda$loadBitmap$2$androidx-media3-datasource-DataSourceBitmapLoader */
    /* synthetic */ Bitmap m57x731908d1(Uri r4) throws Exception {
        return load(this.dataSourceFactory.createDataSource(), r4, this.options, this.maximumOutputDimension);
    }

    @Override // androidx.media3.common.util.BitmapLoader
    public ListenableFuture<Bitmap> loadBitmap(final Uri r3) {
        return this.listeningExecutorService.submit(new DataSourceBitmapLoader$$ExternalSyntheticLambda2(this, r3));
    }

    @Override // androidx.media3.common.util.BitmapLoader
    public /* synthetic */ ListenableFuture loadBitmapFromMetadata(MediaMetadata r1) {
        return BitmapLoader.CC.$default$loadBitmapFromMetadata(this, r1);
    }

    @Override // androidx.media3.common.util.BitmapLoader
    public boolean supportsMimeType(String r2) {
        return Util.isBitmapFactorySupportedMimeType(r2);
    }
}
