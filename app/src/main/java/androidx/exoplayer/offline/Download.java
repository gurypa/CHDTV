package androidx.media3.exoplayer.offline;

import androidx.media3.common.util.Assertions;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* JADX INFO: loaded from: classes.dex */
public final class Download {
    public static final int FAILURE_REASON_NONE = 0;
    public static final int FAILURE_REASON_UNKNOWN = 1;
    public static final int STATE_COMPLETED = 3;
    public static final int STATE_DOWNLOADING = 2;
    public static final int STATE_FAILED = 4;
    public static final int STATE_QUEUED = 0;
    public static final int STATE_REMOVING = 5;
    public static final int STATE_RESTARTING = 7;
    public static final int STATE_STOPPED = 1;
    public static final int STOP_REASON_NONE = 0;
    public final long contentLength;
    public final int failureReason;
    final DownloadProgress progress;
    public final DownloadRequest request;
    public final long startTimeMs;
    public final int state;
    public final int stopReason;
    public final long updateTimeMs;

    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface FailureReason {
    }

    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
    }

    public Download(DownloadRequest r13, int r14, long r15, long r17, long r19, int r21, int r22) {
        this(r13, r14, r15, r17, r19, r21, r22, new DownloadProgress());
    }

    public Download(DownloadRequest r5, int r6, long r7, long r9, long r11, int r13, int r14, DownloadProgress r15) {
        Assertions.checkNotNull(r15);
        boolean r0 = true;
        if (r14 != 0) goto L5;
        boolean r2 = true;
    L7:
        if (r6 == 4) goto L9;
        boolean r3 = true;
    L10:
        if (r2 != r3) goto L12;
        boolean r4 = true;
    L13:
        Assertions.checkArgument(r4);
        if (r13 != 0) goto L16;
    L21:
        this.request = r5;
        this.state = r6;
        this.startTimeMs = r7;
        this.updateTimeMs = r9;
        this.contentLength = r11;
        this.stopReason = r13;
        this.failureReason = r14;
        this.progress = r15;
        return;
    L16:
        if (r6 == 2) goto L19;
        if (r6 == 0) goto L19;
    L20:
        Assertions.checkArgument(r0);
    L19:
        r0 = false;
        goto L20
    L12:
        r4 = false;
        goto L13
    L9:
        r3 = false;
        goto L10
    L5:
        r2 = false;
        goto L7
    }

    public long getBytesDownloaded() {
        return this.progress.bytesDownloaded;
    }

    public float getPercentDownloaded() {
        return this.progress.percentDownloaded;
    }

    public boolean isTerminalState() {
        if (this.state != 3) goto L5;
    L8:
        return true;
    L5:
        if (this.state == 4) goto L8;
        return false;
    }
}
