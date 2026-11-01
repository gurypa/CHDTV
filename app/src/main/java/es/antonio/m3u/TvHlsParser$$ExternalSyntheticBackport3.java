package es.antonio.m3u;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class TvHlsParser$$ExternalSyntheticBackport3 {
    /* JADX INFO: renamed from: m */
    public static /* synthetic */ long m415m(long r6, long r8) {
        int r0 = ((Long.numberOfLeadingZeros(r6) + Long.numberOfLeadingZeros(~r6)) + Long.numberOfLeadingZeros(r8)) + Long.numberOfLeadingZeros(~r8);
        if (r0 <= 65) goto L7;
        return r6 * r8;
    L7:
        if (r0 < 64) goto L24;
        boolean r1 = true;
        if (r6 < 0) goto L11;
        boolean r3 = true;
    L13:
        if (r8 != Long.MIN_VALUE) goto L17;
        r1 = false;
    L17:
        if ((r1 | r3) == false) goto L24;
        long r2 = r6 * r8;
        if (r6 != 0) goto L21;
    L22:
        return r2;
    L21:
        if ((r2 / r6) != r8) goto L24;
    L11:
        r3 = false;
    L24:
        throw new ArithmeticException();
    }
}
