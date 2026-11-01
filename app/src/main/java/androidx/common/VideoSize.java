package androidx.media3.common;

import android.os.Bundle;
import androidx.media3.common.util.Util;

/* JADX INFO: loaded from: classes.dex */
public final class VideoSize {
    private static final int DEFAULT_HEIGHT = 0;
    private static final float DEFAULT_PIXEL_WIDTH_HEIGHT_RATIO = 1.0f;
    private static final int DEFAULT_WIDTH = 0;
    private static final String FIELD_HEIGHT = null;
    private static final String FIELD_PIXEL_WIDTH_HEIGHT_RATIO = null;
    private static final String FIELD_WIDTH = null;
    public static final VideoSize UNKNOWN = null;
    public final int height;
    public final float pixelWidthHeightRatio;

    @Deprecated
    public final int unappliedRotationDegrees;
    public final int width;

    static {
        UNKNOWN = new VideoSize(0, 0);
        FIELD_WIDTH = Util.intToStringMaxRadix(0);
        FIELD_HEIGHT = Util.intToStringMaxRadix(1);
        FIELD_PIXEL_WIDTH_HEIGHT_RATIO = Util.intToStringMaxRadix(3);
    }

    public VideoSize(int r2, int r3) {
        this(r2, r3, 1.0f);
    }

    public VideoSize(int r2, int r3, float r4) {
        this.width = r2;
        this.height = r3;
        this.unappliedRotationDegrees = 0;
        this.pixelWidthHeightRatio = r4;
    }

    @Deprecated
    public VideoSize(int r1, int r2, int r3, float r4) {
        this(r1, r2, r4);
    }

    public static VideoSize fromBundle(Bundle r4) {
        return new VideoSize(r4.getInt(FIELD_WIDTH, 0), r4.getInt(FIELD_HEIGHT, 0), r4.getFloat(FIELD_PIXEL_WIDTH_HEIGHT_RATIO, 1.0f));
    }

    public boolean equals(Object r6) {
        if (this != r6) goto L6;
        return true;
    L6:
        if ((r6 instanceof VideoSize) == false) goto L16;
        VideoSize r1 = (VideoSize) r6;
        if (this.width != r1.width) goto L15;
        if (this.height != r1.height) goto L15;
        if (this.pixelWidthHeightRatio != r1.pixelWidthHeightRatio) goto L15;
        return true;
    L15:
        return false;
    L16:
        return false;
    }

    public int hashCode() {
        return (((((7 * 31) + this.width) * 31) + this.height) * 31) + Float.floatToRawIntBits(this.pixelWidthHeightRatio);
    }

    public Bundle toBundle() {
        Bundle r0 = new Bundle();
        if (this.width == 0) goto L6;
        r0.putInt(FIELD_WIDTH, this.width);
    L6:
        if (this.height == 0) goto L9;
        r0.putInt(FIELD_HEIGHT, this.height);
    L9:
        if (this.pixelWidthHeightRatio == 1.0f) goto L11;
        r0.putFloat(FIELD_PIXEL_WIDTH_HEIGHT_RATIO, this.pixelWidthHeightRatio);
    L11:
        return r0;
    }
}
