package androidx.core.graphics;

import android.graphics.Rect;

/* JADX INFO: loaded from: classes.dex */
public final class Insets {
    public static final Insets NONE = null;
    public final int bottom;
    public final int left;
    public final int right;
    public final int top;

    static class Api29Impl {
        private Api29Impl() {
        }

        /* JADX INFO: renamed from: of */
        static android.graphics.Insets m10of(int r1, int r2, int r3, int r4) {
            return android.graphics.Insets.of(r1, r2, r3, r4);
        }
    }

    static {
        NONE = new Insets(0, 0, 0, 0);
    }

    private Insets(int r1, int r2, int r3, int r4) {
        this.left = r1;
        this.top = r2;
        this.right = r3;
        this.bottom = r4;
    }

    public static Insets add(Insets r5, Insets r6) {
        return m8of(r5.left + r6.left, r5.top + r6.top, r5.right + r6.right, r5.bottom + r6.bottom);
    }

    public static Insets max(Insets r5, Insets r6) {
        return m8of(Math.max(r5.left, r6.left), Math.max(r5.top, r6.top), Math.max(r5.right, r6.right), Math.max(r5.bottom, r6.bottom));
    }

    public static Insets min(Insets r5, Insets r6) {
        return m8of(Math.min(r5.left, r6.left), Math.min(r5.top, r6.top), Math.min(r5.right, r6.right), Math.min(r5.bottom, r6.bottom));
    }

    /* JADX INFO: renamed from: of */
    public static Insets m8of(int r1, int r2, int r3, int r4) {
        if (r1 != 0) goto L9;
        if (r2 != 0) goto L9;
        if (r3 != 0) goto L9;
        if (r4 != 0) goto L9;
        return NONE;
    L9:
        return new Insets(r1, r2, r3, r4);
    }

    /* JADX INFO: renamed from: of */
    public static Insets m9of(Rect r4) {
        return m8of(r4.left, r4.top, r4.right, r4.bottom);
    }

    public static Insets subtract(Insets r5, Insets r6) {
        return m8of(r5.left - r6.left, r5.top - r6.top, r5.right - r6.right, r5.bottom - r6.bottom);
    }

    public static Insets toCompatInsets(android.graphics.Insets r4) {
        return m8of(r4.left, r4.top, r4.right, r4.bottom);
    }

    @Deprecated
    public static Insets wrap(android.graphics.Insets r1) {
        return toCompatInsets(r1);
    }

    public boolean equals(Object r6) {
        if (this != r6) goto L6;
        return true;
    L6:
        if (r6 != null) goto L8;
    L23:
        return false;
    L8:
        if (getClass() != r6.getClass()) goto L23;
        Insets r2 = (Insets) r6;
        if (this.bottom == r2.bottom) goto L14;
        return false;
    L14:
        if (this.left == r2.left) goto L17;
        return false;
    L17:
        if (this.right == r2.right) goto L20;
        return false;
    L20:
        if (this.top == r2.top) goto L22;
        return false;
    L22:
        return true;
    }

    public int hashCode() {
        return (((((this.left * 31) + this.top) * 31) + this.right) * 31) + this.bottom;
    }

    public android.graphics.Insets toPlatformInsets() {
        return Api29Impl.m10of(this.left, this.top, this.right, this.bottom);
    }

    public String toString() {
        return "Insets{left=" + this.left + ", top=" + this.top + ", right=" + this.right + ", bottom=" + this.bottom + '}';
    }
}
