package kotlin;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class UByte$$ExternalSyntheticBackport3 {
    /* JADX INFO: renamed from: m */
    public static /* synthetic */ long m432m(long r7, long r9) {
        if (r9 >= 0) goto L10;
        if ((r7 ^ Long.MIN_VALUE) >= (r9 ^ Long.MIN_VALUE)) goto L7;
        return 0;
    L7:
        return 1;
    L10:
        if (r7 >= 0) goto L12;
        int r0 = 1;
        long r1 = ((r7 >>> 1) / r9) << 1;
        if (((r7 - (r1 * r9)) ^ Long.MIN_VALUE) >= (r9 ^ Long.MIN_VALUE)) goto L18;
        r0 = 0;
    L18:
        return r1 + ((long) r0);
    L12:
        return r7 / r9;
    }
}
