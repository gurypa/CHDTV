package es.antonio.m3u;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class TvHlsParser$$ExternalSyntheticBackport5 {
    /* JADX INFO: renamed from: m */
    public static /* synthetic */ long m417m(long r5, long r7) {
        long r0 = r5 - r7;
        boolean r8 = true;
        if ((r7 ^ r5) < 0) goto L5;
        boolean r9 = true;
    L7:
        if ((r5 ^ r0) >= 0) goto L11;
        r8 = false;
    L11:
        if ((r9 | r8) == false) goto L14;
        return r0;
    L14:
        throw new ArithmeticException();
    L5:
        r9 = false;
        goto L7
    }
}
