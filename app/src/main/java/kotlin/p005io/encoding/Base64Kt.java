package kotlin.p005io.encoding;

import kotlin.Metadata;
import kotlin.collections.ArraysKt;

/* JADX INFO: compiled from: Base64.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\u001e\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\u001a\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0001\"\u0016\u0010\u0000\u001a\u00020\u00018\u0002X\u0083\u0004¢\u0006\b\n\u0000\u0012\u0004\b\u0002\u0010\u0003\"\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000\"\u0016\u0010\u0006\u001a\u00020\u00018\u0002X\u0083\u0004¢\u0006\b\n\u0000\u0012\u0004\b\u0007\u0010\u0003\"\u000e\u0010\b\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\r"}, m421d2 = {"base64DecodeMap", "", "getBase64DecodeMap$annotations", "()V", "base64EncodeMap", "", "base64UrlDecodeMap", "getBase64UrlDecodeMap$annotations", "base64UrlEncodeMap", "isInMimeAlphabet", "", "symbol", "", "kotlin-stdlib"}, m422k = 2, m423mv = {1, 9, 0}, m425xi = 48)
public final class Base64Kt {
    private static final int[] base64DecodeMap = null;
    private static final byte[] base64EncodeMap = null;
    private static final int[] base64UrlDecodeMap = null;
    private static final byte[] base64UrlEncodeMap = null;

    static {
        base64EncodeMap = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};
        int[] r0 = new int[256];
        ArraysKt.fill$default(r0, -1, 0, 0, 6, null);
        r0[61] = -2;
        byte[] r2 = base64EncodeMap;
        int r4 = 0;
        int r5 = r2.length;
        int r6 = 0;
    L3:
        if (r6 >= r5) goto L5;
        r0[r2[r6]] = r4;
        r6 = r6 + 1;
        r4 = r4 + 1;
        goto L3
    L5:
        base64DecodeMap = r0;
        base64UrlEncodeMap = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95};
        int[] r1 = new int[256];
        ArraysKt.fill$default(r1, -1, 0, 0, 6, null);
        r1[61] = -2;
        byte[] r3 = base64UrlEncodeMap;
        int r7 = 0;
        int r8 = r3.length;
        int r9 = 0;
    L6:
        if (r9 >= r8) goto L8;
        r1[r3[r9]] = r7;
        r9 = r9 + 1;
        r7 = r7 + 1;
        goto L6
    L8:
        base64UrlDecodeMap = r1;
    }

    public static final /* synthetic */ int[] access$getBase64DecodeMap$p() {
        return base64DecodeMap;
    }

    public static final /* synthetic */ byte[] access$getBase64EncodeMap$p() {
        return base64EncodeMap;
    }

    public static final /* synthetic */ int[] access$getBase64UrlDecodeMap$p() {
        return base64UrlDecodeMap;
    }

    public static final /* synthetic */ byte[] access$getBase64UrlEncodeMap$p() {
        return base64UrlEncodeMap;
    }

    private static /* synthetic */ void getBase64DecodeMap$annotations() {
    }

    private static /* synthetic */ void getBase64UrlDecodeMap$annotations() {
    }

    public static final boolean isInMimeAlphabet(int r4) {
        if (r4 >= 0) goto L5;
    L7:
        boolean r2 = false;
    L8:
        if (r2 == false) goto L13;
        if (base64DecodeMap[r4] == (-1)) goto L13;
        return true;
    L13:
        return false;
    L5:
        if (r4 >= base64DecodeMap.length) goto L7;
        r2 = true;
        goto L8
    }
}
