package com.google.common.hash;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import com.google.common.primitives.UnsignedBytes;
import com.google.errorprone.annotations.Immutable;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@Immutable
@ElementTypesAreNonnullByDefault
final class Murmur3_32HashFunction extends AbstractHashFunction implements Serializable {

    /* JADX INFO: renamed from: C1 */
    private static final int f132C1 = -862048943;

    /* JADX INFO: renamed from: C2 */
    private static final int f133C2 = 461845907;
    private static final int CHUNK_SIZE = 4;
    static final HashFunction GOOD_FAST_HASH_32 = null;
    static final HashFunction MURMUR3_32 = null;
    static final HashFunction MURMUR3_32_FIXED = null;
    private static final long serialVersionUID = 0;
    private final int seed;
    private final boolean supplementaryPlaneFix;

    private static final class Murmur3_32Hasher extends AbstractHasher {
        private long buffer;

        /* JADX INFO: renamed from: h1 */
        private int f134h1;
        private boolean isDone;
        private int length;
        private int shift;

        Murmur3_32Hasher(int r2) {
            this.f134h1 = r2;
            this.length = 0;
            this.isDone = false;
        }

        private void update(int r6, long r7) {
            this.buffer |= (4294967295L & r7) << this.shift;
            this.shift += r6 * 8;
            this.length += r6;
            if (this.shift < 32) goto L6;
            this.f134h1 = Murmur3_32HashFunction.access$100(this.f134h1, Murmur3_32HashFunction.access$000((int) this.buffer));
            this.buffer >>>= 32;
            this.shift -= 32;
            return;
        }

        @Override // com.google.common.hash.Hasher
        public HashCode hash() {
            Preconditions.checkState(!this.isDone);
            this.isDone = true;
            this.f134h1 ^= Murmur3_32HashFunction.access$000((int) this.buffer);
            return Murmur3_32HashFunction.access$600(this.f134h1, this.length);
        }

        @Override // com.google.common.hash.AbstractHasher, com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
        public Hasher putByte(byte r4) {
            update(1, r4 & 255);
            return this;
        }

        @Override // com.google.common.hash.AbstractHasher, com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
        public /* bridge */ /* synthetic */ PrimitiveSink putByte(byte r1) {
            return putByte(r1);
        }

        @Override // com.google.common.hash.AbstractHasher, com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
        public Hasher putBytes(ByteBuffer r4) {
            ByteOrder r0 = r4.order();
            r4.order(ByteOrder.LITTLE_ENDIAN);
        L4:
            if (r4.remaining() < 4) goto L7;
            putInt(r4.getInt());
        L7:
            if (r4.hasRemaining() == false) goto L9;
            putByte(r4.get());
            goto L7
        L9:
            r4.order(r0);
            return this;
        }

        @Override // com.google.common.hash.AbstractHasher, com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
        public Hasher putBytes(byte[] r5, int r6, int r7) {
            Preconditions.checkPositionIndexes(r6, r6 + r7, r5.length);
            int r0 = 0;
        L4:
            if ((r0 + 4) > r7) goto L6;
            update(4, Murmur3_32HashFunction.access$200(r5, r6 + r0));
            r0 = r0 + 4;
        L6:
            if (r0 >= r7) goto L8;
            putByte(r5[r6 + r0]);
            r0 = r0 + 1;
            goto L6
        L8:
            return this;
        }

        @Override // com.google.common.hash.AbstractHasher, com.google.common.hash.PrimitiveSink
        public /* bridge */ /* synthetic */ PrimitiveSink putBytes(ByteBuffer r1) {
            return putBytes(r1);
        }

        @Override // com.google.common.hash.AbstractHasher, com.google.common.hash.PrimitiveSink
        public /* bridge */ /* synthetic */ PrimitiveSink putBytes(byte[] r1, int r2, int r3) {
            return putBytes(r1, r2, r3);
        }

        @Override // com.google.common.hash.AbstractHasher, com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
        public Hasher putChar(char r4) {
            update(2, r4);
            return this;
        }

        @Override // com.google.common.hash.AbstractHasher, com.google.common.hash.PrimitiveSink
        public /* bridge */ /* synthetic */ PrimitiveSink putChar(char r1) {
            return putChar(r1);
        }

        @Override // com.google.common.hash.AbstractHasher, com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
        public Hasher putInt(int r4) {
            update(4, r4);
            return this;
        }

        @Override // com.google.common.hash.AbstractHasher, com.google.common.hash.PrimitiveSink
        public /* bridge */ /* synthetic */ PrimitiveSink putInt(int r1) {
            return putInt(r1);
        }

        @Override // com.google.common.hash.AbstractHasher, com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
        public Hasher putLong(long r4) {
            update(4, (int) r4);
            update(4, r4 >>> 32);
            return this;
        }

        @Override // com.google.common.hash.AbstractHasher, com.google.common.hash.PrimitiveSink
        public /* bridge */ /* synthetic */ PrimitiveSink putLong(long r1) {
            return putLong(r1);
        }

        @Override // com.google.common.hash.AbstractHasher, com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
        public Hasher putString(CharSequence r11, Charset r12) {
            if (StandardCharsets.UTF_8.equals(r12) == false) goto L34;
            int r0 = r11.length();
            int r1 = 0;
        L6:
            if ((r1 + 4) > r0) goto L13;
            char r2 = r11.charAt(r1);
            char r5 = r11.charAt(r1 + 1);
            char r6 = r11.charAt(r1 + 2);
            char r7 = r11.charAt(r1 + 3);
            if (r2 >= 128) goto L13;
            if (r5 >= 128) goto L13;
            if (r6 >= 128) goto L13;
            if (r7 >= 128) goto L13;
            update(4, (((r5 << '\b') | r2) | (r6 << 16)) | (r7 << 24));
            r1 = r1 + 4;
        L13:
            if (r1 >= r0) goto L32;
            char r3 = r11.charAt(r1);
            if (r3 >= 128) goto L18;
            update(1, r3);
        L31:
            r1 = r1 + 1;
            goto L13
        L18:
            if (r3 >= 2048) goto L21;
            update(2, Murmur3_32HashFunction.access$300(r3));
            goto L31
        L21:
            if (r3 >= 55296) goto L23;
        L30:
            update(3, Murmur3_32HashFunction.access$400(r3));
            goto L31
        L23:
            if (r3 > 57343) goto L30;
            int r8 = Character.codePointAt(r11, r1);
            if (r8 == r3) goto L27;
            r1 = r1 + 1;
            update(4, Murmur3_32HashFunction.access$500(r8));
            goto L31
        L27:
            putBytes(r11.subSequence(r1, r0).toString().getBytes(r12));
            return this;
        L32:
            return this;
        L34:
            return super.putString(r11, r12);
        }

        @Override // com.google.common.hash.AbstractHasher, com.google.common.hash.PrimitiveSink
        public /* bridge */ /* synthetic */ PrimitiveSink putString(CharSequence r1, Charset r2) {
            return putString(r1, r2);
        }
    }

    static {
        MURMUR3_32 = new Murmur3_32HashFunction(0, false);
        MURMUR3_32_FIXED = new Murmur3_32HashFunction(0, true);
        GOOD_FAST_HASH_32 = new Murmur3_32HashFunction(Hashing.GOOD_FAST_HASH_SEED, true);
    }

    Murmur3_32HashFunction(int r1, boolean r2) {
        this.seed = r1;
        this.supplementaryPlaneFix = r2;
    }

    static /* synthetic */ int access$000(int r1) {
        return mixK1(r1);
    }

    static /* synthetic */ int access$100(int r1, int r2) {
        return mixH1(r1, r2);
    }

    static /* synthetic */ int access$200(byte[] r1, int r2) {
        return getIntLittleEndian(r1, r2);
    }

    static /* synthetic */ long access$300(char r2) {
        return charToTwoUtf8Bytes(r2);
    }

    static /* synthetic */ long access$400(char r2) {
        return charToThreeUtf8Bytes(r2);
    }

    static /* synthetic */ long access$500(int r2) {
        return codePointToFourUtf8Bytes(r2);
    }

    static /* synthetic */ HashCode access$600(int r1, int r2) {
        return fmix(r1, r2);
    }

    private static long charToThreeUtf8Bytes(char r4) {
        return ((((long) (r4 >>> '\f')) | 224) | ((long) ((((r4 >>> 6) & 63) | 128) << 8))) | ((long) (((r4 & '?') | 128) << 16));
    }

    private static long charToTwoUtf8Bytes(char r4) {
        return (((long) (r4 >>> 6)) | 192) | ((long) (((r4 & '?') | 128) << 8));
    }

    private static long codePointToFourUtf8Bytes(int r7) {
        return (((((long) (r7 >>> 18)) | 240) | ((((long) ((r7 >>> 12) & 63)) | 128) << 8)) | ((((long) ((r7 >>> 6) & 63)) | 128) << 16)) | ((((long) (r7 & 63)) | 128) << 24);
    }

    private static HashCode fmix(int r1, int r2) {
        int r3 = r1 ^ r2;
        int r4 = (r3 ^ (r3 >>> 16)) * (-2048144789);
        int r5 = (r4 ^ (r4 >>> 13)) * (-1028477387);
        return HashCode.fromInt(r5 ^ (r5 >>> 16));
    }

    private static int getIntLittleEndian(byte[] r4, int r5) {
        return Ints.fromBytes(r4[r5 + 3], r4[r5 + 2], r4[r5 + 1], r4[r5]);
    }

    private static int mixH1(int r2, int r3) {
        return (Integer.rotateLeft(r2 ^ r3, 13) * 5) - 430675100;
    }

    private static int mixK1(int r1) {
        return Integer.rotateLeft(r1 * f132C1, 15) * f133C2;
    }

    @Override // com.google.common.hash.HashFunction
    public int bits() {
        return 32;
    }

    public boolean equals(@CheckForNull Object r5) {
        if ((r5 instanceof Murmur3_32HashFunction) == false) goto L10;
        Murmur3_32HashFunction r0 = (Murmur3_32HashFunction) r5;
        if (this.seed == r0.seed) goto L7;
        return false;
    L7:
        if (this.supplementaryPlaneFix != r0.supplementaryPlaneFix) goto L12;
        return true;
    L12:
        return false;
    L10:
        return false;
    }

    @Override // com.google.common.hash.AbstractHashFunction, com.google.common.hash.HashFunction
    public HashCode hashBytes(byte[] r6, int r7, int r8) {
        Preconditions.checkPositionIndexes(r7, r7 + r8, r6.length);
        int r0 = this.seed;
        int r1 = 0;
    L4:
        if ((r1 + 4) > r8) goto L6;
        r0 = mixH1(r0, mixK1(getIntLittleEndian(r6, r7 + r1)));
        r1 = r1 + 4;
        goto L4
    L6:
        int r2 = 0;
        int r3 = 0;
    L7:
        if (r1 >= r8) goto L10;
        r2 = r2 ^ (UnsignedBytes.toInt(r6[r7 + r1]) << r3);
        r1 = r1 + 1;
        r3 = r3 + 8;
        goto L7
    L10:
        return fmix(r0 ^ mixK1(r2), r8);
    }

    public int hashCode() {
        return getClass().hashCode() ^ this.seed;
    }

    @Override // com.google.common.hash.AbstractHashFunction, com.google.common.hash.HashFunction
    public HashCode hashInt(int r4) {
        int r0 = mixK1(r4);
        return fmix(mixH1(this.seed, r0), 4);
    }

    @Override // com.google.common.hash.AbstractHashFunction, com.google.common.hash.HashFunction
    public HashCode hashLong(long r6) {
        int r2 = mixK1((int) r6);
        return fmix(mixH1(mixH1(this.seed, r2), mixK1((int) (r6 >>> 32))), 8);
    }

    @Override // com.google.common.hash.AbstractHashFunction, com.google.common.hash.HashFunction
    public HashCode hashString(CharSequence r13, Charset r14) {
        if (StandardCharsets.UTF_8.equals(r14) == false) goto L42;
        int r0 = r13.length();
        int r1 = this.seed;
        int r2 = 0;
        int r3 = 0;
    L6:
        if ((r2 + 4) > r0) goto L13;
        char r4 = r13.charAt(r2);
        char r6 = r13.charAt(r2 + 1);
        char r7 = r13.charAt(r2 + 2);
        char r8 = r13.charAt(r2 + 3);
        if (r4 >= 128) goto L13;
        if (r6 >= 128) goto L13;
        if (r7 >= 128) goto L13;
        if (r8 >= 128) goto L13;
        r1 = mixH1(r1, mixK1((((r6 << '\b') | r4) | (r7 << 16)) | (r8 << 24)));
        r2 = r2 + 4;
        r3 = r3 + 4;
    L13:
        long r9 = 0;
        int r5 = 0;
    L14:
        if (r2 >= r0) goto L40;
        char r10 = r13.charAt(r2);
        if (r10 >= 128) goto L19;
        r9 = r9 | (((long) r10) << r5);
        r5 = r5 + 8;
        r3 = r3 + 1;
    L36:
        if (r5 < 32) goto L38;
        r1 = mixH1(r1, mixK1((int) r9));
        r9 = r9 >>> 32;
        r5 = r5 - 32;
    L38:
        r2 = r2 + 1;
        goto L14
    L19:
        if (r10 >= 2048) goto L22;
        r9 = r9 | (charToTwoUtf8Bytes(r10) << r5);
        r5 = r5 + 16;
        r3 = r3 + 2;
        goto L36
    L22:
        if (r10 >= 55296) goto L24;
    L34:
        r9 = r9 | (charToThreeUtf8Bytes(r10) << r5);
        r5 = r5 + 24;
        r3 = r3 + 3;
        goto L36
    L24:
        if (r10 > 57343) goto L34;
        int r11 = Character.codePointAt(r13, r2);
        if (r11 == r10) goto L29;
        r2 = r2 + 1;
        r9 = r9 | (codePointToFourUtf8Bytes(r11) << r5);
        if (this.supplementaryPlaneFix == false) goto L33;
        r5 = r5 + 32;
    L33:
        r3 = r3 + 4;
        goto L36
    L29:
        return hashBytes(r13.toString().getBytes(r14));
    L40:
        return fmix(r1 ^ mixK1((int) r9), r3);
    L42:
        return hashBytes(r13.toString().getBytes(r14));
    }

    @Override // com.google.common.hash.AbstractHashFunction, com.google.common.hash.HashFunction
    public HashCode hashUnencodedChars(CharSequence r5) {
        int r0 = this.seed;
        int r1 = 1;
    L4:
        if (r1 >= r5.length()) goto L7;
        r0 = mixH1(r0, mixK1(r5.charAt(r1 - 1) | (r5.charAt(r1) << 16)));
        r1 = r1 + 2;
        goto L4
    L7:
        if ((r5.length() & 1) != 1) goto L10;
        r0 = r0 ^ mixK1(r5.charAt(r5.length() - 1));
    L10:
        return fmix(r0, r5.length() * 2);
    }

    @Override // com.google.common.hash.HashFunction
    public Hasher newHasher() {
        return new Murmur3_32Hasher(this.seed);
    }

    public String toString() {
        return "Hashing.murmur3_32(" + this.seed + ")";
    }
}
