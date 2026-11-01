package androidx.recyclerview.widget;

import android.view.View;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* JADX INFO: loaded from: classes.dex */
class ViewBoundsCheck {
    static final int CVE_PVE_POS = 12;
    static final int CVE_PVS_POS = 8;
    static final int CVS_PVE_POS = 4;
    static final int CVS_PVS_POS = 0;

    /* JADX INFO: renamed from: EQ */
    static final int f75EQ = 2;
    static final int FLAG_CVE_EQ_PVE = 8192;
    static final int FLAG_CVE_EQ_PVS = 512;
    static final int FLAG_CVE_GT_PVE = 4096;
    static final int FLAG_CVE_GT_PVS = 256;
    static final int FLAG_CVE_LT_PVE = 16384;
    static final int FLAG_CVE_LT_PVS = 1024;
    static final int FLAG_CVS_EQ_PVE = 32;
    static final int FLAG_CVS_EQ_PVS = 2;
    static final int FLAG_CVS_GT_PVE = 16;
    static final int FLAG_CVS_GT_PVS = 1;
    static final int FLAG_CVS_LT_PVE = 64;
    static final int FLAG_CVS_LT_PVS = 4;

    /* JADX INFO: renamed from: GT */
    static final int f76GT = 1;

    /* JADX INFO: renamed from: LT */
    static final int f77LT = 4;
    static final int MASK = 7;
    BoundFlags mBoundFlags;
    final Callback mCallback;

    static class BoundFlags {
        int mBoundFlags;
        int mChildEnd;
        int mChildStart;
        int mRvEnd;
        int mRvStart;

        BoundFlags() {
            this.mBoundFlags = 0;
        }

        void addFlags(int r2) {
            this.mBoundFlags |= r2;
        }

        boolean boundsMatch() {
            if ((this.mBoundFlags & 7) == 0) goto L8;
            if ((this.mBoundFlags & (compare(this.mChildStart, this.mRvStart) << 0)) != 0) goto L8;
            return false;
        L8:
            if ((this.mBoundFlags & 112) == 0) goto L13;
            if ((this.mBoundFlags & (compare(this.mChildStart, this.mRvEnd) << 4)) != 0) goto L13;
            return false;
        L13:
            if ((this.mBoundFlags & 1792) == 0) goto L18;
            if ((this.mBoundFlags & (compare(this.mChildEnd, this.mRvStart) << 8)) != 0) goto L18;
            return false;
        L18:
            if ((this.mBoundFlags & 28672) != 0) goto L20;
            return true;
        L20:
            if ((this.mBoundFlags & (compare(this.mChildEnd, this.mRvEnd) << 12)) != 0) goto L24;
            return false;
        L24:
            return true;
        }

        int compare(int r2, int r3) {
            if (r2 <= r3) goto L5;
            return 1;
        L5:
            if (r2 != r3) goto L8;
            return 2;
        L8:
            return 4;
        }

        void resetFlags() {
            this.mBoundFlags = 0;
        }

        void setBounds(int r1, int r2, int r3, int r4) {
            this.mRvStart = r1;
            this.mRvEnd = r2;
            this.mChildStart = r3;
            this.mChildEnd = r4;
        }
    }

    interface Callback {
        View getChildAt(int r1);

        int getChildEnd(View r1);

        int getChildStart(View r1);

        int getParentEnd();

        int getParentStart();
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ViewBounds {
    }

    ViewBoundsCheck(Callback r2) {
        this.mCallback = r2;
        this.mBoundFlags = new BoundFlags();
    }

    View findOneViewWithinBoundFlags(int r10, int r11, int r12, int r13) {
        int r0 = this.mCallback.getParentStart();
        int r1 = this.mCallback.getParentEnd();
        if (r11 <= r10) goto L5;
        int r2 = 1;
    L6:
        View r3 = null;
        int r4 = r10;
    L7:
        if (r4 == r11) goto L18;
        View r5 = this.mCallback.getChildAt(r4);
        int r6 = this.mCallback.getChildStart(r5);
        int r7 = this.mCallback.getChildEnd(r5);
        this.mBoundFlags.setBounds(r0, r1, r6, r7);
        if (r12 == 0) goto L13;
        this.mBoundFlags.resetFlags();
        this.mBoundFlags.addFlags(r12);
        if (this.mBoundFlags.boundsMatch() == false) goto L13;
        return r5;
    L13:
        if (r13 == 0) goto L17;
        this.mBoundFlags.resetFlags();
        this.mBoundFlags.addFlags(r13);
        if (this.mBoundFlags.boundsMatch() == false) goto L17;
        r3 = r5;
    L17:
        r4 = r4 + r2;
        goto L7
    L18:
        return r3;
    L5:
        r2 = -1;
        goto L6
    }

    boolean isViewWithinBoundFlags(View r6, int r7) {
        this.mBoundFlags.setBounds(this.mCallback.getParentStart(), this.mCallback.getParentEnd(), this.mCallback.getChildStart(r6), this.mCallback.getChildEnd(r6));
        if (r7 == 0) goto L6;
        this.mBoundFlags.resetFlags();
        this.mBoundFlags.addFlags(r7);
        return this.mBoundFlags.boundsMatch();
    L6:
        return false;
    }
}
