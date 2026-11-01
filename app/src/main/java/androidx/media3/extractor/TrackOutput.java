package androidx.media3.extractor;

import androidx.media3.common.DataReader;
import androidx.media3.common.Format;
import androidx.media3.common.util.ParsableByteArray;
import java.io.IOException;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public interface TrackOutput {
    public static final int SAMPLE_DATA_PART_ENCRYPTION = 1;
    public static final int SAMPLE_DATA_PART_MAIN = 0;
    public static final int SAMPLE_DATA_PART_SUPPLEMENTAL = 2;

    /* JADX INFO: renamed from: androidx.media3.extractor.TrackOutput$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static int $default$sampleData(TrackOutput r1, DataReader r2, int r3, boolean r4) throws IOException {
            return r1.sampleData(r2, r3, r4, 0);
        }

        public static void $default$sampleData(TrackOutput r1, ParsableByteArray r2, int r3) {
            r1.sampleData(r2, r3, 0);
        }
    }

    public static final class CryptoData {
        public final int clearBlocks;
        public final int cryptoMode;
        public final int encryptedBlocks;
        public final byte[] encryptionKey;

        public CryptoData(int r1, byte[] r2, int r3, int r4) {
            this.cryptoMode = r1;
            this.encryptionKey = r2;
            this.encryptedBlocks = r3;
            this.clearBlocks = r4;
        }

        public boolean equals(Object r6) {
            if (this != r6) goto L6;
            return true;
        L6:
            if (r6 != null) goto L8;
        L21:
            return false;
        L8:
            if (getClass() != r6.getClass()) goto L21;
            CryptoData r2 = (CryptoData) r6;
            if (this.cryptoMode != r2.cryptoMode) goto L20;
            if (this.encryptedBlocks != r2.encryptedBlocks) goto L20;
            if (this.clearBlocks != r2.clearBlocks) goto L20;
            if (Arrays.equals(this.encryptionKey, r2.encryptionKey) == false) goto L20;
            return true;
        L20:
            return false;
        }

        public int hashCode() {
            return (((((this.cryptoMode * 31) + Arrays.hashCode(this.encryptionKey)) * 31) + this.encryptedBlocks) * 31) + this.clearBlocks;
        }
    }

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface SampleDataPart {
    }

    void format(Format r1);

    int sampleData(DataReader r1, int r2, boolean r3) throws IOException;

    int sampleData(DataReader r1, int r2, boolean r3, int r4) throws IOException;

    void sampleData(ParsableByteArray r1, int r2);

    void sampleData(ParsableByteArray r1, int r2, int r3);

    void sampleMetadata(long r1, int r3, int r4, int r5, CryptoData r6);
}
