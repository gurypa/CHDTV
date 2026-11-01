package androidx.media3.common.util;

/* JADX INFO: loaded from: classes.dex */
public final class GlRect {
    public int bottom;
    public int left;
    public int right;
    public int top;

    public GlRect(int r2, int r3) {
        this(0, 0, r2, r3);
    }

    public GlRect(int r2, int r3, int r4, int r5) {
        if (r2 > r4) goto L6;
        if (r3 > r5) goto L6;
        boolean r0 = true;
    L7:
        Assertions.checkArgument(r0);
        this.left = r2;
        this.bottom = r3;
        this.right = r4;
        this.top = r5;
        return;
    L6:
        r0 = false;
        goto L7
    }
}
