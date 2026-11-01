package kotlin;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class UByte$$ExternalSyntheticBackport2 {
    /* JADX INFO: renamed from: m */
    public static /* synthetic */ long m431m(long r7, long r9) {
        if (r9 >= 0) goto L10;
        if ((r7 ^ Long.MIN_VALUE) >= (Long.MIN_VALUE ^ r9)) goto L8;
        return r7;
    L8:
        return r7 - r9;
    L10:
        if (r7 >= 0) goto L12;
        long r8 = r7 - ((((r7 >>> 1) / r9) << 1) * r9);
        if ((r8 ^ Long.MIN_VALUE) >= (Long.MIN_VALUE ^ r9)) goto L18;
        r9 = 0;
    L18:
        return r8 - r9;
    L12:
        return r7 % r9;
    }
}
