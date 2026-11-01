package kotlin;

/* JADX INFO: compiled from: CharCode.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\b\n\u0002\u0010\f\n\u0002\b\u0006\u001a\u0011\u0010\u0007\u001a\u00020\u00022\u0006\u0010\u0000\u001a\u00020\u0001H\u0087\b\"\u001f\u0010\u0000\u001a\u00020\u0001*\u00020\u00028Æ\u0002X\u0087\u0004¢\u0006\f\u0012\u0004\b\u0003\u0010\u0004\u001a\u0004\b\u0005\u0010\u0006¨\u0006\b"}, m421d2 = {"code", "", "", "getCode$annotations", "(C)V", "getCode", "(C)I", "Char", "kotlin-stdlib"}, m422k = 2, m423mv = {1, 9, 0}, m425xi = 48)
public final class CharCodeKt {
    private static final char Char(int r3) {
        if (r3 < 0) goto L8;
        if (r3 > 65535) goto L8;
        return (char) r3;
    L8:
        throw new IllegalArgumentException("Invalid Char code: " + r3);
    }

    private static final int getCode(char r0) {
        return r0;
    }

    public static /* synthetic */ void getCode$annotations(char r0) {
    }
}
