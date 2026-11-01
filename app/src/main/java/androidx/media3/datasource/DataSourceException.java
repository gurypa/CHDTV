package androidx.media3.datasource;

import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public class DataSourceException extends IOException {

    @Deprecated
    public static final int POSITION_OUT_OF_RANGE = 2008;
    public final int reason;

    public DataSourceException(int r1) {
        this.reason = r1;
    }

    public DataSourceException(String r1, int r2) {
        super(r1);
        this.reason = r2;
    }

    public DataSourceException(String r1, Throwable r2, int r3) {
        super(r1, r2);
        this.reason = r3;
    }

    public DataSourceException(Throwable r1, int r2) {
        super(r1);
        this.reason = r2;
    }

    public static boolean isCausedByPositionOutOfRange(IOException r3) {
        Throwable r0 = r3;
    L3:
        if (r0 == null) goto L11;
        if ((r0 instanceof DataSourceException) == false) goto L10;
        if (((DataSourceException) r0).reason != 2008) goto L10;
        return true;
    L10:
        r0 = r0.getCause();
        goto L3
    L11:
        return false;
    }
}
