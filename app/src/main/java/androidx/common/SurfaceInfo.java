package androidx.media3.common;

import android.view.Surface;
import androidx.media3.common.util.Assertions;

/* JADX INFO: loaded from: classes.dex */
public final class SurfaceInfo {
    public final int height;
    public final boolean isEncoderInputSurface;
    public final int orientationDegrees;
    public final Surface surface;
    public final int width;

    public SurfaceInfo(Surface r2, int r3, int r4) {
        this(r2, r3, r4, 0);
    }

    public SurfaceInfo(Surface r7, int r8, int r9, int r10) {
        this(r7, r8, r9, r10, false);
    }

    public SurfaceInfo(Surface r3, int r4, int r5, int r6, boolean r7) {
        if (r6 != 0) goto L5;
    L12:
        boolean r0 = true;
    L13:
        Assertions.checkArgument(r0, "orientationDegrees must be 0, 90, 180, or 270");
        this.surface = r3;
        this.width = r4;
        this.height = r5;
        this.orientationDegrees = r6;
        this.isEncoderInputSurface = r7;
        return;
    L5:
        if (r6 == 90) goto L12;
        if (r6 == 180) goto L12;
        if (r6 == 270) goto L12;
        r0 = false;
        goto L13
    }

    public boolean equals(Object r6) {
        if (this != r6) goto L6;
        return true;
    L6:
        if ((r6 instanceof SurfaceInfo) == true) goto L8;
        return false;
    L8:
        SurfaceInfo r1 = (SurfaceInfo) r6;
        if (this.width != r1.width) goto L20;
        if (this.height != r1.height) goto L20;
        if (this.orientationDegrees != r1.orientationDegrees) goto L20;
        if (this.isEncoderInputSurface != r1.isEncoderInputSurface) goto L20;
        if (this.surface.equals(r1.surface) == false) goto L20;
        return true;
    L20:
        return false;
    }

    public int hashCode() {
        return (((((((this.surface.hashCode() * 31) + this.width) * 31) + this.height) * 31) + this.orientationDegrees) * 31) + (this.isEncoderInputSurface ? 1 : 0);
    }
}
