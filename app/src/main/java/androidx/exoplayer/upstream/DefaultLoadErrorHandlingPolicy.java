package androidx.media3.exoplayer.upstream;

import androidx.media3.common.C0180C;
import androidx.media3.common.ParserException;
import androidx.media3.datasource.DataSourceException;
import androidx.media3.datasource.HttpDataSource;
import java.io.FileNotFoundException;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public class DefaultLoadErrorHandlingPolicy implements LoadErrorHandlingPolicy {
    private static final int DEFAULT_BEHAVIOR_MIN_LOADABLE_RETRY_COUNT = -1;
    public static final long DEFAULT_LOCATION_EXCLUSION_MS = 300000;
    public static final int DEFAULT_MIN_LOADABLE_RETRY_COUNT = 3;
    public static final int DEFAULT_MIN_LOADABLE_RETRY_COUNT_PROGRESSIVE_LIVE = 6;

    @Deprecated
    public static final long DEFAULT_TRACK_BLACKLIST_MS = 60000;
    public static final long DEFAULT_TRACK_EXCLUSION_MS = 60000;
    private final int minimumLoadableRetryCount;

    public DefaultLoadErrorHandlingPolicy() {
        this(-1);
    }

    public DefaultLoadErrorHandlingPolicy(int r1) {
        this.minimumLoadableRetryCount = r1;
    }

    @Override // androidx.media3.exoplayer.upstream.LoadErrorHandlingPolicy
    public LoadErrorHandlingPolicy.FallbackSelection getFallbackSelectionFor(LoadErrorHandlingPolicy.FallbackOptions r5, LoadErrorHandlingPolicy.LoadErrorInfo r6) {
        if (isEligibleForFallback(r6.exception) == true) goto L6;
        return null;
    L6:
        if (r5.isFallbackAvailable(1) == false) goto L10;
        return new LoadErrorHandlingPolicy.FallbackSelection(1, 300000);
    L10:
        if (r5.isFallbackAvailable(2) == true) goto L12;
        return null;
    L12:
        return new LoadErrorHandlingPolicy.FallbackSelection(2, 60000);
    }

    @Override // androidx.media3.exoplayer.upstream.LoadErrorHandlingPolicy
    public int getMinimumLoadableRetryCount(int r3) {
        if (this.minimumLoadableRetryCount != (-1)) goto L10;
        if (r3 != 7) goto L7;
        return 6;
    L7:
        return 3;
    L10:
        return this.minimumLoadableRetryCount;
    }

    @Override // androidx.media3.exoplayer.upstream.LoadErrorHandlingPolicy
    public long getRetryDelayMsFor(LoadErrorHandlingPolicy.LoadErrorInfo r4) {
        IOException r0 = r4.exception;
        if ((r0 instanceof ParserException) == false) goto L5;
    L14:
        return C0180C.TIME_UNSET;
    L5:
        if ((r0 instanceof FileNotFoundException) == true) goto L14;
        if ((r0 instanceof HttpDataSource.CleartextNotPermittedException) == true) goto L14;
        if ((r0 instanceof Loader.UnexpectedLoaderException) == true) goto L14;
        if (DataSourceException.isCausedByPositionOutOfRange(r0) == true) goto L14;
        return Math.min((r4.errorCount - 1) * 1000, 5000);
    }

    protected boolean isEligibleForFallback(IOException r5) {
        if ((r5 instanceof HttpDataSource.InvalidResponseCodeException) == true) goto L5;
        return false;
    L5:
        HttpDataSource.InvalidResponseCodeException r0 = (HttpDataSource.InvalidResponseCodeException) r5;
        if (r0.responseCode != 403) goto L8;
    L17:
        return true;
    L8:
        if (r0.responseCode == 404) goto L17;
        if (r0.responseCode == 410) goto L17;
        if (r0.responseCode == 416) goto L17;
        if (r0.responseCode == 500) goto L17;
        if (r0.responseCode == 503) goto L17;
        return false;
    }

    @Override // androidx.media3.exoplayer.upstream.LoadErrorHandlingPolicy
    public /* synthetic */ void onLoadTaskConcluded(long r1) {
        LoadErrorHandlingPolicy.CC.$default$onLoadTaskConcluded(this, r1);
    }
}
