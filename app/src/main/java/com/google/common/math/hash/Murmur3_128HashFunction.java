package com.google.common.hash;

import com.google.common.primitives.UnsignedBytes;
import com.google.errorprone.annotations.Immutable;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@Immutable
@ElementTypesAreNonnullByDefault
final class Murmur3_128HashFunction extends AbstractHashFunction implements Serializable {
    static final HashFunction GOOD_FAST_HASH_128 = null;
    static final HashFunction MURMUR3_128 = null;
    private static final long serialVersionUID = 0;
    private final int seed;

    private static final class Murmur3_128Hasher extends AbstractStreamingHasher {

        /* JADX INFO: renamed from: C1 */
        private static final long f128C1 = -8663945395140668459L;

        /* JADX INFO: renamed from: C2 */
        private static final long f129C2 = 5545529020109919103L;
        private static final int CHUNK_SIZE = 16;

        /* JADX INFO: renamed from: h1 */
        private long f130h1;

        /* JADX INFO: renamed from: h2 */
        private long f131h2;
        private int length;

        Murmur3_128Hasher(int r3) {
            super(16);
            this.f130h1 = r3;
            this.f131h2 = r3;
            this.length = 0;
        }

        private void bmix64(long r7, long r9) {
            this.f130h1 ^= mixK1(r7);
            this.f130h1 = Long.rotateLeft(this.f130h1, 27);
            this.f130h1 += this.f131h2;
            this.f130h1 = (this.f130h1 * 5) + 1390208809;
            this.f131h2 ^= mixK2(r9);
            this.f131h2 = Long.rotateLeft(this.f131h2, 31);
            this.f131h2 += this.f130h1;
            this.f131h2 = (this.f131h2 * 5) + 944331445;
        }

        private static long fmix64(long r3) {
            long r4 = (r3 ^ (r3 >>> 33)) * (-49064778989728563L);
            long r5 = (r4 ^ (r4 >>> 33)) * (-4265267296055464877L);
            return r5 ^ (r5 >>> 33);
        }

        private static long mixK1(long r2) {
            return Long.rotateLeft(r2 * f128C1, 31) * f129C2;
        }

        private static long mixK2(long r2) {
            return Long.rotateLeft(r2 * f129C2, 33) * f128C1;
        }

        @Override // com.google.common.hash.AbstractStreamingHasher
        protected HashCode makeHash() {
            this.f130h1 ^= (long) this.length;
            this.f131h2 ^= (long) this.length;
            this.f130h1 += this.f131h2;
            this.f131h2 += this.f130h1;
            this.f130h1 = fmix64(this.f130h1);
            this.f131h2 = fmix64(this.f131h2);
            this.f130h1 += this.f131h2;
            this.f131h2 += this.f130h1;
            return HashCode.fromBytesNoCopy(ByteBuffer.wrap(new byte[16]).order(ByteOrder.LITTLE_ENDIAN).putLong(this.f130h1).putLong(this.f131h2).array());
        }

        @Override // com.google.common.hash.AbstractStreamingHasher
        protected void process(ByteBuffer r6) {
            bmix64(r6.getLong(), r6.getLong());
            this.length += 16;
        }

        @Override // com.google.common.hash.AbstractStreamingHasher
        protected void processRemaining(ByteBuffer r14) {
            long r0 = 0;
            long r2 = 0;
            this.length += r14.remaining();
            switch(r14.remaining()) {
                case 1: goto L20;
                case 2: goto L19;
                case 3: goto L18;
                case 4: goto L17;
                case 5: goto L16;
                case 6: goto L15;
                case 7: goto L14;
                case 8: goto L13;
                case 9: goto L12;
                case 10: goto L11;
                case 11: goto L10;
                case 12: goto L9;
                case 13: goto L8;
                case 14: goto L7;
                case 15: goto L6;
                default: goto L5;
            };
        L6:
            r2 = 0 ^ (((long) UnsignedBytes.toInt(r14.get(14))) << 48);
        L7:
            r2 = r2 ^ (((long) UnsignedBytes.toInt(r14.get(13))) << 40);
        L8:
            r2 = r2 ^ (((long) UnsignedBytes.toInt(r14.get(12))) << 32);
        L9:
            r2 = r2 ^ (((long) UnsignedBytes.toInt(r14.get(11))) << 24);
        L10:
            r2 = r2 ^ (((long) UnsignedBytes.toInt(r14.get(10))) << 16);
        L11:
            r2 = r2 ^ (((long) UnsignedBytes.toInt(r14.get(9))) << 8);
        L12:
            r2 = r2 ^ ((long) UnsignedBytes.toInt(r14.get(8)));
        L13:
            long r1 = 0 ^ r14.getLong();
        L21:
            this.f130h1 ^= mixK1(r1);
            this.f131h2 ^= mixK2(r2);
            return;
        L14:
            r0 = 0 ^ (((long) UnsignedBytes.toInt(r14.get(6))) << 48);
        L15:
            r0 = r0 ^ (((long) UnsignedBytes.toInt(r14.get(5))) << 40);
        L16:
            r0 = r0 ^ (((long) UnsignedBytes.toInt(r14.get(4))) << 32);
        L17:
            r0 = r0 ^ (((long) UnsignedBytes.toInt(r14.get(3))) << 24);
        L18:
            r0 = r0 ^ (((long) UnsignedBytes.toInt(r14.get(2))) << 16);
        L19:
            r0 = r0 ^ (((long) UnsignedBytes.toInt(r14.get(1))) << 8);
        L20:
            r1 = r0 ^ ((long) UnsignedBytes.toInt(r14.get(0)));
            goto L21
        L5:
            throw new AssertionError("Should never get here.");
        }
    }

    static {
        MURMUR3_128 = new Murmur3_128HashFunction(0);
        GOOD_FAST_HASH_128 = new Murmur3_128HashFunction(Hashing.GOOD_FAST_HASH_SEED);
    }

    Murmur3_128HashFunction(int r1) {
        this.seed = r1;
    }

    @Override // com.google.common.hash.HashFunction
    public int bits() {
        return 128;
    }

    public boolean equals(@CheckForNull Object r5) {
        if ((r5 instanceof Murmur3_128HashFunction) == true) goto L5;
        return false;
    L5:
        if (this.seed != ((Murmur3_128HashFunction) r5).seed) goto L9;
        return true;
    L9:
        return false;
    }

    public int hashCode() {
        return getClass().hashCode() ^ this.seed;
    }

    @Override // com.google.common.hash.HashFunction
    public Hasher newHasher() {
        return new Murmur3_128Hasher(this.seed);
    }

    public String toString() {
        return "Hashing.murmur3_128(" + this.seed + ")";
    }
}
