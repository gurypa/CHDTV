package androidx.media3.extractor.mp4;

import androidx.media3.common.C0180C;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.extractor.TrackOutput;

/* JADX INFO: loaded from: classes.dex */
public final class TrackEncryptionBox {
    private static final String TAG = "TrackEncryptionBox";
    public final TrackOutput.CryptoData cryptoData;
    public final byte[] defaultInitializationVector;
    public final boolean isEncrypted;
    public final int perSampleIvSize;
    public final String schemeType;

    public TrackEncryptionBox(boolean r4, String r5, int r6, byte[] r7, int r8, int r9, byte[] r10) {
        boolean r0 = true;
        if (r6 != 0) goto L5;
        boolean r2 = true;
    L6:
        if (r10 == null) goto L9;
        r0 = false;
    L9:
        Assertions.checkArgument(r0 ^ r2);
        this.isEncrypted = r4;
        this.schemeType = r5;
        this.perSampleIvSize = r6;
        this.defaultInitializationVector = r10;
        this.cryptoData = new TrackOutput.CryptoData(schemeToCryptoMode(r5), r7, r8, r9);
        return;
    L5:
        r2 = false;
        goto L6
    }

    private static int schemeToCryptoMode(String r3) {
        if (r3 != null) goto L6;
        return 1;
    L6:
        switch(r3.hashCode()) {
            case 3046605: goto L18;
            case 3046671: goto L15;
            case 3049879: goto L12;
            case 3049895: goto L9;
            default: goto L20;
        };
    L20:
        byte r1 = -1;
    L21:
        switch(r1) {
            case 0: goto L25;
            case 1: goto L25;
            case 2: goto L24;
            case 3: goto L24;
            default: goto L22;
        };
    L22:
        Log.m48w(TAG, "Unsupported protection scheme type '" + r3 + "'. Assuming AES-CTR crypto mode.");
        return 1;
    L24:
        return 2;
    L25:
        return 1;
    L9:
        if (r3.equals(C0180C.CENC_TYPE_cens) == false) goto L20;
        r1 = 1;
        goto L21
    L12:
        if (r3.equals(C0180C.CENC_TYPE_cenc) == false) goto L20;
        r1 = 0;
        goto L21
    L15:
        if (r3.equals(C0180C.CENC_TYPE_cbcs) == false) goto L20;
        r1 = 3;
        goto L21
    L18:
        if (r3.equals(C0180C.CENC_TYPE_cbc1) == false) goto L20;
        r1 = 2;
        goto L21
    }
}
