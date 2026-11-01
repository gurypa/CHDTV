package androidx.media3.exoplayer;

import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import androidx.media3.common.Format;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.analytics.AnalyticsListener;
import androidx.media3.exoplayer.source.MediaSource;
import java.io.IOException;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* JADX INFO: loaded from: classes.dex */
public final class ExoPlaybackException extends PlaybackException {
    private static final String FIELD_IS_RECOVERABLE = null;
    private static final String FIELD_RENDERER_FORMAT = null;
    private static final String FIELD_RENDERER_FORMAT_SUPPORT = null;
    private static final String FIELD_RENDERER_INDEX = null;
    private static final String FIELD_RENDERER_NAME = null;
    private static final String FIELD_TYPE = null;
    public static final int TYPE_REMOTE = 3;
    public static final int TYPE_RENDERER = 1;
    public static final int TYPE_SOURCE = 0;
    public static final int TYPE_UNEXPECTED = 2;
    final boolean isRecoverable;
    public final MediaSource.MediaPeriodId mediaPeriodId;
    public final Format rendererFormat;
    public final int rendererFormatSupport;
    public final int rendererIndex;
    public final String rendererName;
    public final int type;

    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
    }

    static {
        FIELD_TYPE = Util.intToStringMaxRadix(1001);
        FIELD_RENDERER_NAME = Util.intToStringMaxRadix(1002);
        FIELD_RENDERER_INDEX = Util.intToStringMaxRadix(1003);
        FIELD_RENDERER_FORMAT = Util.intToStringMaxRadix(1004);
        FIELD_RENDERER_FORMAT_SUPPORT = Util.intToStringMaxRadix(AnalyticsListener.EVENT_UPSTREAM_DISCARDED);
        FIELD_IS_RECOVERABLE = Util.intToStringMaxRadix(1006);
    }

    private ExoPlaybackException(int r11, Throwable r12, int r13) {
        this(r11, r12, null, r13, null, -1, null, 4, false);
    }

    private ExoPlaybackException(int r14, Throwable r15, String r16, int r17, String r18, int r19, Format r20, int r21, boolean r22) {
        this(deriveMessage(r14, r16, r18, r19, r20, r21), r15, r17, r14, r18, r19, r20, r21, null, SystemClock.elapsedRealtime(), r22);
    }

    private ExoPlaybackException(Bundle r5) {
        super(r5);
        this.type = r5.getInt(FIELD_TYPE, 2);
        this.rendererName = r5.getString(FIELD_RENDERER_NAME);
        this.rendererIndex = r5.getInt(FIELD_RENDERER_INDEX, -1);
        Bundle r0 = r5.getBundle(FIELD_RENDERER_FORMAT);
        if (r0 != null) goto L5;
        Format r2 = null;
    L6:
        this.rendererFormat = r2;
        this.rendererFormatSupport = r5.getInt(FIELD_RENDERER_FORMAT_SUPPORT, 4);
        this.isRecoverable = r5.getBoolean(FIELD_IS_RECOVERABLE, false);
        this.mediaPeriodId = null;
        return;
    L5:
        r2 = Format.fromBundle(r0);
        goto L6
    }

    private ExoPlaybackException(String r9, Throwable r10, int r11, int r12, String r13, int r14, Format r15, int r16, MediaSource.MediaPeriodId r17, long r18, boolean r20) {
        super(r9, r10, r11, Bundle.EMPTY, r18);
        boolean r2 = false;
        if (r20 == false) goto L7;
        if (r12 == 1) goto L7;
        boolean r4 = false;
    L8:
        Assertions.checkArgument(r4);
        if (r10 == null) goto L11;
    L12:
        r2 = true;
    L13:
        Assertions.checkArgument(r2);
        this.type = r12;
        this.rendererName = r13;
        this.rendererIndex = r14;
        this.rendererFormat = r15;
        this.rendererFormatSupport = r16;
        this.mediaPeriodId = r17;
        this.isRecoverable = r20;
        return;
    L11:
        if (r12 != 3) goto L13;
    L7:
        r4 = true;
        goto L8
    }

    public static ExoPlaybackException createForRemote(String r10) {
        return new ExoPlaybackException(3, null, r10, 1001, null, -1, null, 4, false);
    }

    public static ExoPlaybackException createForRenderer(Throwable r10, String r11, int r12, Format r13, int r14, boolean r15, int r16) {
        if (r13 != null) goto L5;
        int r8 = 4;
    L7:
        return new ExoPlaybackException(1, r10, null, r16, r11, r12, r13, r8, r15);
    L5:
        r8 = r14;
        goto L7
    }

    public static ExoPlaybackException createForSource(IOException r2, int r3) {
        return new ExoPlaybackException(0, r2, r3);
    }

    @Deprecated
    public static ExoPlaybackException createForUnexpected(RuntimeException r1) {
        return createForUnexpected(r1, 1000);
    }

    public static ExoPlaybackException createForUnexpected(RuntimeException r2, int r3) {
        return new ExoPlaybackException(2, r2, r3);
    }

    private static String deriveMessage(int r3, String r4, String r5, int r6, Format r7, int r8) {
        switch(r3) {
            case 0: goto L6;
            case 1: goto L5;
            case 2: goto L3;
            case 3: goto L4;
            default: goto L3;
        };
    L3:
        String r0 = "Unexpected runtime error";
    L8:
        if (TextUtils.isEmpty(r4) == false) goto L10;
        return r0;
    L10:
        return r0 + ": " + r4;
    L4:
        r0 = "Remote error";
        goto L8
    L5:
        r0 = r5 + " error, index=" + r6 + ", format=" + r7 + ", format_supported=" + Util.getFormatSupportString(r8);
        goto L8
    L6:
        r0 = "Source error";
        goto L8
    }

    public static ExoPlaybackException fromBundle(Bundle r1) {
        return new ExoPlaybackException(r1);
    }

    ExoPlaybackException copyWithMediaPeriodId(MediaSource.MediaPeriodId r14) {
        return new ExoPlaybackException((String) Util.castNonNull(getMessage()), getCause(), this.errorCode, this.type, this.rendererName, this.rendererIndex, this.rendererFormat, this.rendererFormatSupport, r14, this.timestampMs, this.isRecoverable);
    }

    @Override // androidx.media3.common.PlaybackException
    public boolean errorInfoEquals(PlaybackException r5) {
        if (super.errorInfoEquals(r5) == true) goto L5;
        return false;
    L5:
        ExoPlaybackException r0 = (ExoPlaybackException) Util.castNonNull(r5);
        if (this.type == r0.type) goto L8;
        return false;
    L8:
        if (Util.areEqual(this.rendererName, r0.rendererName) == true) goto L10;
        return false;
    L10:
        if (this.rendererIndex == r0.rendererIndex) goto L12;
        return false;
    L12:
        if (Util.areEqual(this.rendererFormat, r0.rendererFormat) == true) goto L14;
        return false;
    L14:
        if (this.rendererFormatSupport == r0.rendererFormatSupport) goto L16;
        return false;
    L16:
        if (Util.areEqual(this.mediaPeriodId, r0.mediaPeriodId) == true) goto L18;
        return false;
    L18:
        if (this.isRecoverable != r0.isRecoverable) goto L28;
        return true;
    L28:
        return false;
    }

    public Exception getRendererException() {
        boolean r1 = true;
        if (this.type == 1) goto L6;
        r1 = false;
    L6:
        Assertions.checkState(r1);
        return (Exception) Assertions.checkNotNull(getCause());
    }

    public IOException getSourceException() {
        if (this.type != 0) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkState(r0);
        return (IOException) Assertions.checkNotNull(getCause());
    L5:
        r0 = false;
        goto L6
    }

    public RuntimeException getUnexpectedException() {
        if (this.type != 2) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkState(r0);
        return (RuntimeException) Assertions.checkNotNull(getCause());
    L5:
        r0 = false;
        goto L6
    }

    @Override // androidx.media3.common.PlaybackException
    public Bundle toBundle() {
        Bundle r0 = super.toBundle();
        r0.putInt(FIELD_TYPE, this.type);
        r0.putString(FIELD_RENDERER_NAME, this.rendererName);
        r0.putInt(FIELD_RENDERER_INDEX, this.rendererIndex);
        if (this.rendererFormat == null) goto L5;
        r0.putBundle(FIELD_RENDERER_FORMAT, this.rendererFormat.toBundle(false));
    L5:
        r0.putInt(FIELD_RENDERER_FORMAT_SUPPORT, this.rendererFormatSupport);
        r0.putBoolean(FIELD_IS_RECOVERABLE, this.isRecoverable);
        return r0;
    }
}
