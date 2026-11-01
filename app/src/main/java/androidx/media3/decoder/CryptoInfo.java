package androidx.media3.decoder;

import android.media.MediaCodec;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;

/* JADX INFO: loaded from: classes.dex */
public final class CryptoInfo {
    public int clearBlocks;
    public int encryptedBlocks;
    private final MediaCodec.CryptoInfo frameworkCryptoInfo;

    /* JADX INFO: renamed from: iv */
    public byte[] f34iv;
    public byte[] key;
    public int mode;
    public int[] numBytesOfClearData;
    public int[] numBytesOfEncryptedData;
    public int numSubSamples;
    private final PatternHolderV24 patternHolder;

    /* JADX INFO: renamed from: androidx.media3.decoder.CryptoInfo$1 */
    static /* synthetic */ class C02191 {
    }

    private static final class PatternHolderV24 {
        private final MediaCodec.CryptoInfo frameworkCryptoInfo;
        private final MediaCodec.CryptoInfo.Pattern pattern;

        private PatternHolderV24(MediaCodec.CryptoInfo r3) {
            this.frameworkCryptoInfo = r3;
            this.pattern = new MediaCodec.CryptoInfo.Pattern(0, 0);
        }

        /* synthetic */ PatternHolderV24(MediaCodec.CryptoInfo r1, C02191 r2) {
            this(r1);
        }

        static /* synthetic */ void access$100(PatternHolderV24 r0, int r1, int r2) {
            r0.set(r1, r2);
        }

        private void set(int r3, int r4) {
            this.pattern.set(r3, r4);
            this.frameworkCryptoInfo.setPattern(this.pattern);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public CryptoInfo() {
        this.frameworkCryptoInfo = new MediaCodec.CryptoInfo();
        PatternHolderV24 r2 = null;
        Object[] r3 = 0;
        if (Util.SDK_INT < 24) goto L5;
        r2 = new PatternHolderV24(this.frameworkCryptoInfo, r3 == true ? 1 : 0);
    L5:
        this.patternHolder = r2;
    }

    public MediaCodec.CryptoInfo getFrameworkCryptoInfo() {
        return this.frameworkCryptoInfo;
    }

    public void increaseClearDataFirstSubSampleBy(int r4) {
        if (r4 != 0) goto L5;
        return;
    L5:
        if (this.numBytesOfClearData != null) goto L7;
        this.numBytesOfClearData = new int[1];
        MediaCodec.CryptoInfo r0 = this.frameworkCryptoInfo;
        r0.numBytesOfClearData = this.numBytesOfClearData;
    L7:
        int[] r1 = this.numBytesOfClearData;
        r1[0] = r1[0] + r4;
    }

    public void set(int r3, int[] r4, int[] r5, byte[] r6, byte[] r7, int r8, int r9, int r10) {
        this.numSubSamples = r3;
        this.numBytesOfClearData = r4;
        this.numBytesOfEncryptedData = r5;
        this.key = r6;
        this.f34iv = r7;
        this.mode = r8;
        this.encryptedBlocks = r9;
        this.clearBlocks = r10;
        this.frameworkCryptoInfo.numSubSamples = r3;
        this.frameworkCryptoInfo.numBytesOfClearData = r4;
        this.frameworkCryptoInfo.numBytesOfEncryptedData = r5;
        this.frameworkCryptoInfo.key = r6;
        this.frameworkCryptoInfo.iv = r7;
        this.frameworkCryptoInfo.mode = r8;
        if (Util.SDK_INT < 24) goto L6;
        PatternHolderV24.access$100((PatternHolderV24) Assertions.checkNotNull(this.patternHolder), r9, r10);
        return;
    }
}
