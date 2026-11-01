package androidx.media3.decoder;

import androidx.media3.common.Format;
import androidx.media3.common.MediaLibraryInfo;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.nio.ByteBuffer;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;

/* JADX INFO: loaded from: classes.dex */
public class DecoderInputBuffer extends Buffer {
    public static final int BUFFER_REPLACEMENT_MODE_DIRECT = 2;
    public static final int BUFFER_REPLACEMENT_MODE_DISABLED = 0;
    public static final int BUFFER_REPLACEMENT_MODE_NORMAL = 1;
    private final int bufferReplacementMode;
    public final CryptoInfo cryptoInfo;
    public ByteBuffer data;
    public Format format;
    private final int paddingSize;
    public ByteBuffer supplementalData;
    public long timeUs;
    public boolean waitingForKeys;

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface BufferReplacementMode {
    }

    public static final class InsufficientCapacityException extends IllegalStateException {
        public final int currentCapacity;
        public final int requiredCapacity;

        public InsufficientCapacityException(int r3, int r4) {
            super("Buffer too small (" + r3 + " < " + r4 + ")");
            this.currentCapacity = r3;
            this.requiredCapacity = r4;
        }
    }

    static {
        MediaLibraryInfo.registerModule("media3.decoder");
    }

    public DecoderInputBuffer(int r2) {
        this(r2, 0);
    }

    public DecoderInputBuffer(int r2, int r3) {
        this.cryptoInfo = new CryptoInfo();
        this.bufferReplacementMode = r2;
        this.paddingSize = r3;
    }

    private ByteBuffer createReplacementByteBuffer(int r3) {
        if (this.bufferReplacementMode != 1) goto L7;
        return ByteBuffer.allocate(r3);
    L7:
        if (this.bufferReplacementMode != 2) goto L11;
        return ByteBuffer.allocateDirect(r3);
    L11:
        if (this.data != null) goto L13;
        int r0 = 0;
    L15:
        throw new InsufficientCapacityException(r0, r3);
    L13:
        r0 = this.data.capacity();
        goto L15
    }

    public static DecoderInputBuffer newNoDataInstance() {
        return new DecoderInputBuffer(0);
    }

    @Override // androidx.media3.decoder.Buffer
    public void clear() {
        super.clear();
        if (this.data == null) goto L6;
        this.data.clear();
    L6:
        if (this.supplementalData == null) goto L8;
        this.supplementalData.clear();
    L8:
        this.waitingForKeys = false;
    }

    @EnsuresNonNull({"data"})
    public void ensureSpaceForWrite(int r7) {
        int r8 = r7 + this.paddingSize;
        ByteBuffer r0 = this.data;
        if (r0 != null) goto L6;
        this.data = createReplacementByteBuffer(r8);
        return;
    L6:
        int r1 = r0.capacity();
        int r2 = r0.position();
        int r3 = r2 + r8;
        if (r1 < r3) goto L10;
        this.data = r0;
        return;
    L10:
        ByteBuffer r4 = createReplacementByteBuffer(r3);
        r4.order(r0.order());
        if (r2 <= 0) goto L13;
        r0.flip();
        r4.put(r0);
    L13:
        this.data = r4;
    }

    public final void flip() {
        if (this.data == null) goto L6;
        this.data.flip();
    L6:
        if (this.supplementalData == null) goto L9;
        this.supplementalData.flip();
        return;
    }

    public final boolean isEncrypted() {
        return getFlag(1073741824);
    }

    @EnsuresNonNull({"supplementalData"})
    public void resetSupplementalData(int r2) {
        if (this.supplementalData != null) goto L5;
    L8:
        this.supplementalData = ByteBuffer.allocate(r2);
        return;
    L5:
        if (this.supplementalData.capacity() < r2) goto L8;
        this.supplementalData.clear();
    }
}
