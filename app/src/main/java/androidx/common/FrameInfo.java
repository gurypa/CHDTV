package androidx.media3.common;

import androidx.media3.common.util.Assertions;

/* JADX INFO: loaded from: classes.dex */
public class FrameInfo {
    public final ColorInfo colorInfo;
    public final int height;
    public final long offsetToAddUs;
    public final float pixelWidthHeightRatio;
    public final int width;

    /* JADX INFO: renamed from: androidx.media3.common.FrameInfo$1 */
    static /* synthetic */ class C01881 {
    }

    public static final class Builder {
        private ColorInfo colorInfo;
        private int height;
        private long offsetToAddUs;
        private float pixelWidthHeightRatio;
        private int width;

        public Builder(ColorInfo r2, int r3, int r4) {
            this.colorInfo = r2;
            this.width = r3;
            this.height = r4;
            this.pixelWidthHeightRatio = 1.0f;
        }

        public Builder(FrameInfo r3) {
            this.colorInfo = r3.colorInfo;
            this.width = r3.width;
            this.height = r3.height;
            this.pixelWidthHeightRatio = r3.pixelWidthHeightRatio;
            this.offsetToAddUs = r3.offsetToAddUs;
        }

        public FrameInfo build() {
            return new FrameInfo(this.colorInfo, this.width, this.height, this.pixelWidthHeightRatio, this.offsetToAddUs, null);
        }

        public Builder setColorInfo(ColorInfo r1) {
            this.colorInfo = r1;
            return this;
        }

        public Builder setHeight(int r1) {
            this.height = r1;
            return this;
        }

        public Builder setOffsetToAddUs(long r1) {
            this.offsetToAddUs = r1;
            return this;
        }

        public Builder setPixelWidthHeightRatio(float r1) {
            this.pixelWidthHeightRatio = r1;
            return this;
        }

        public Builder setWidth(int r1) {
            this.width = r1;
            return this;
        }
    }

    private FrameInfo(ColorInfo r6, int r7, int r8, float r9, long r10) {
        boolean r0 = true;
        if (r7 <= 0) goto L5;
        boolean r2 = true;
    L6:
        Assertions.checkArgument(r2, "width must be positive, but is: " + r7);
        if (r8 > 0) goto L10;
        r0 = false;
    L10:
        Assertions.checkArgument(r0, "height must be positive, but is: " + r8);
        this.colorInfo = r6;
        this.width = r7;
        this.height = r8;
        this.pixelWidthHeightRatio = r9;
        this.offsetToAddUs = r10;
        return;
    L5:
        r2 = false;
        goto L6
    }

    /* synthetic */ FrameInfo(ColorInfo r1, int r2, int r3, float r4, long r5, C01881 r7) {
        this(r1, r2, r3, r4, r5);
    }
}
