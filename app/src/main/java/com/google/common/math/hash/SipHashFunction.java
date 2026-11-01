package com.google.common.hash;

import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.Immutable;
import java.io.Serializable;
import java.nio.ByteBuffer;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@Immutable
@ElementTypesAreNonnullByDefault
final class SipHashFunction extends AbstractHashFunction implements Serializable {
    static final HashFunction SIP_HASH_24 = null;
    private static final long serialVersionUID = 0;

    /* JADX INFO: renamed from: c */
    private final int f135c;

    /* JADX INFO: renamed from: d */
    private final int f136d;

    /* JADX INFO: renamed from: k0 */
    private final long f137k0;

    /* JADX INFO: renamed from: k1 */
    private final long f138k1;

    private static final class SipHasher extends AbstractStreamingHasher {
        private static final int CHUNK_SIZE = 8;

        /* JADX INFO: renamed from: b */
        private long f139b;

        /* JADX INFO: renamed from: c */
        private final int f140c;

        /* JADX INFO: renamed from: d */
        private final int f141d;
        private long finalM;

        /* JADX INFO: renamed from: v0 */
        private long f142v0;

        /* JADX INFO: renamed from: v1 */
        private long f143v1;

        /* JADX INFO: renamed from: v2 */
        private long f144v2;

        /* JADX INFO: renamed from: v3 */
        private long f145v3;

        SipHasher(int r3, int r4, long r5, long r7) {
            super(8);
            this.f142v0 = 8317987319222330741L;
            this.f143v1 = 7237128888997146477L;
            this.f144v2 = 7816392313619706465L;
            this.f145v3 = 8387220255154660723L;
            this.f139b = 0;
            this.finalM = 0;
            this.f140c = r3;
            this.f141d = r4;
            this.f142v0 ^= r5;
            this.f143v1 ^= r7;
            this.f144v2 ^= r5;
            this.f145v3 ^= r7;
        }

        private void processM(long r3) {
            this.f145v3 ^= r3;
            sipRound(this.f140c);
            this.f142v0 ^= r3;
        }

        private void sipRound(int r7) {
            int r0 = 0;
        L3:
            if (r0 >= r7) goto L5;
            this.f142v0 += this.f143v1;
            this.f144v2 += this.f145v3;
            this.f143v1 = Long.rotateLeft(this.f143v1, 13);
            this.f145v3 = Long.rotateLeft(this.f145v3, 16);
            this.f143v1 ^= this.f142v0;
            this.f145v3 ^= this.f144v2;
            this.f142v0 = Long.rotateLeft(this.f142v0, 32);
            this.f144v2 += this.f143v1;
            this.f142v0 += this.f145v3;
            this.f143v1 = Long.rotateLeft(this.f143v1, 17);
            this.f145v3 = Long.rotateLeft(this.f145v3, 21);
            this.f143v1 ^= this.f144v2;
            this.f145v3 ^= this.f142v0;
            this.f144v2 = Long.rotateLeft(this.f144v2, 32);
            r0 = r0 + 1;
            goto L3
        }

        @Override // com.google.common.hash.AbstractStreamingHasher
        protected HashCode makeHash() {
            this.finalM ^= this.f139b << 56;
            processM(this.finalM);
            this.f144v2 ^= 255;
            sipRound(this.f141d);
            return HashCode.fromLong(((this.f142v0 ^ this.f143v1) ^ this.f144v2) ^ this.f145v3);
        }

        @Override // com.google.common.hash.AbstractStreamingHasher
        protected void process(ByteBuffer r5) {
            this.f139b += 8;
            processM(r5.getLong());
        }

        @Override // com.google.common.hash.AbstractStreamingHasher
        protected void processRemaining(ByteBuffer r8) {
            this.f139b += (long) r8.remaining();
            int r0 = 0;
        L4:
            if (r8.hasRemaining() == false) goto L6;
            this.finalM ^= (((long) r8.get()) & 255) << r0;
            r0 = r0 + 8;
            goto L4
        }
    }

    static {
        SIP_HASH_24 = new SipHashFunction(2, 4, 506097522914230528L, 1084818905618843912L);
    }

    SipHashFunction(int r5, int r6, long r7, long r9) {
        boolean r0 = true;
        if (r5 <= 0) goto L5;
        boolean r2 = true;
    L6:
        Preconditions.checkArgument(r2, "The number of SipRound iterations (c=%s) during Compression must be positive.", r5);
        if (r6 > 0) goto L10;
        r0 = false;
    L10:
        Preconditions.checkArgument(r0, "The number of SipRound iterations (d=%s) during Finalization must be positive.", r6);
        this.f135c = r5;
        this.f136d = r6;
        this.f137k0 = r7;
        this.f138k1 = r9;
        return;
    L5:
        r2 = false;
        goto L6
    }

    @Override // com.google.common.hash.HashFunction
    public int bits() {
        return 64;
    }

    public boolean equals(@CheckForNull Object r7) {
        if ((r7 instanceof SipHashFunction) == false) goto L14;
        SipHashFunction r0 = (SipHashFunction) r7;
        if (this.f135c == r0.f135c) goto L7;
        return false;
    L7:
        if (this.f136d == r0.f136d) goto L9;
        return false;
    L9:
        if (this.f137k0 == r0.f137k0) goto L11;
        return false;
    L11:
        if (this.f138k1 != r0.f138k1) goto L18;
        return true;
    L18:
        return false;
    L14:
        return false;
    }

    public int hashCode() {
        return (int) ((((long) ((getClass().hashCode() ^ this.f135c) ^ this.f136d)) ^ this.f137k0) ^ this.f138k1);
    }

    @Override // com.google.common.hash.HashFunction
    public Hasher newHasher() {
        return new SipHasher(this.f135c, this.f136d, this.f137k0, this.f138k1);
    }

    public String toString() {
        return "Hashing.sipHash" + this.f135c + "" + this.f136d + "(" + this.f137k0 + ", " + this.f138k1 + ")";
    }
}
