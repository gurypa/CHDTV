package androidx.media3.common.util;

/* JADX INFO: loaded from: classes.dex */
public final class Size {
    public static final Size UNKNOWN = null;
    public static final Size ZERO = null;
    private final int height;
    private final int width;

    static {
        UNKNOWN = new Size(-1, -1);
        ZERO = new Size(0, 0);
    }

    public Size(int r2, int r3) {
        if (r2 == (-1)) goto L5;
        if (r2 >= 0) goto L5;
    L8:
        boolean r0 = false;
    L10:
        Assertions.checkArgument(r0);
        this.width = r2;
        this.height = r3;
        return;
    L5:
        if (r3 == (-1)) goto L9;
        if (r3 < 0) goto L8;
    L9:
        r0 = true;
        goto L10
    }

    public boolean equals(Object r6) {
        if (r6 != null) goto L6;
        return false;
    L6:
        if (this != r6) goto L9;
        return true;
    L9:
        if ((r6 instanceof Size) == false) goto L16;
        Size r2 = (Size) r6;
        if (this.width == r2.width) goto L13;
        return false;
    L13:
        if (this.height == r2.height) goto L15;
        return false;
    L15:
        return true;
    L16:
        return false;
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    public int hashCode() {
        return this.height ^ ((this.width << 16) | (this.width >>> 16));
    }

    public String toString() {
        return this.width + "x" + this.height;
    }
}
