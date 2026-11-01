package androidx.core.util;

import android.util.SizeF;
import com.arthenica.ffmpegkit.StreamInformation;

/* JADX INFO: loaded from: classes.dex */
public final class SizeFCompat {
    private final float mHeight;
    private final float mWidth;

    private static final class Api21Impl {
        private Api21Impl() {
        }

        static SizeF toSizeF(SizeFCompat r3) {
            Preconditions.checkNotNull(r3);
            return new SizeF(r3.getWidth(), r3.getHeight());
        }

        static SizeFCompat toSizeFCompat(SizeF r3) {
            Preconditions.checkNotNull(r3);
            return new SizeFCompat(r3.getWidth(), r3.getHeight());
        }
    }

    public SizeFCompat(float r2, float r3) {
        this.mWidth = Preconditions.checkArgumentFinite(r2, StreamInformation.KEY_WIDTH);
        this.mHeight = Preconditions.checkArgumentFinite(r3, StreamInformation.KEY_HEIGHT);
    }

    public static SizeFCompat toSizeFCompat(SizeF r1) {
        return Api21Impl.toSizeFCompat(r1);
    }

    public boolean equals(Object r6) {
        if (this != r6) goto L6;
        return true;
    L6:
        if ((r6 instanceof SizeFCompat) == true) goto L8;
        return false;
    L8:
        SizeFCompat r1 = (SizeFCompat) r6;
        if (r1.mWidth != this.mWidth) goto L14;
        if (r1.mHeight != this.mHeight) goto L14;
        return true;
    L14:
        return false;
    }

    public float getHeight() {
        return this.mHeight;
    }

    public float getWidth() {
        return this.mWidth;
    }

    public int hashCode() {
        return Float.floatToIntBits(this.mWidth) ^ Float.floatToIntBits(this.mHeight);
    }

    public SizeF toSizeF() {
        return Api21Impl.toSizeF(this);
    }

    public String toString() {
        return this.mWidth + "x" + this.mHeight;
    }
}
