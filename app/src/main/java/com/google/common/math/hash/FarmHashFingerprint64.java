package com.google.common.hash;

import com.google.common.base.Preconditions;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class FarmHashFingerprint64 extends AbstractNonStreamingHashFunction {
    static final HashFunction FARMHASH_FINGERPRINT_64 = null;

    /* JADX INFO: renamed from: K0 */
    private static final long f121K0 = -4348849565147123417L;

    /* JADX INFO: renamed from: K1 */
    private static final long f122K1 = -5435081209227447693L;

    /* JADX INFO: renamed from: K2 */
    private static final long f123K2 = -7286425919675154353L;

    static {
        FARMHASH_FINGERPRINT_64 = new FarmHashFingerprint64();
    }

    FarmHashFingerprint64() {
    }

    static long fingerprint(byte[] r2, int r3, int r4) {
        if (r4 > 32) goto L11;
        if (r4 > 16) goto L9;
        return hashLength0to16(r2, r3, r4);
    L9:
        return hashLength17to32(r2, r3, r4);
    L11:
        if (r4 > 64) goto L15;
        return hashLength33To64(r2, r3, r4);
    L15:
        return hashLength65Plus(r2, r3, r4);
    }

    private static long hashLength0to16(byte[] r13, int r14, int r15) {
        if (r15 < 8) goto L7;
        long r9 = (((long) r15) * 2) + f123K2;
        long r3 = LittleEndianByteArray.load64(r13, r14) + f123K2;
        long r0 = LittleEndianByteArray.load64(r13, (r14 + r15) - 8);
        return hashLength16((Long.rotateRight(r0, 37) * r9) + r3, (Long.rotateRight(r3, 25) + r0) * r9, r9);
    L7:
        if (r15 >= 4) goto L9;
        if (r15 > 0) goto L12;
        return f123K2;
    L12:
        return shiftMix((((long) ((r13[r14] & 255) + ((r13[(r15 >> 1) + r14] & 255) << 8))) * f123K2) ^ (((long) (((r13[(r15 - 1) + r14] & 255) << 2) + r15)) * f121K0)) * f123K2;
    L9:
        return hashLength16(((long) r15) + ((((long) LittleEndianByteArray.load32(r13, r14)) & 4294967295L) << 3), ((long) LittleEndianByteArray.load32(r13, (r14 + r15) - 4)) & 4294967295L, ((long) (r15 * 2)) + f123K2);
    }

    private static long hashLength16(long r7, long r9, long r11) {
        long r0 = (r7 ^ r9) * r11;
        long r3 = (r9 ^ (r0 ^ (r0 >>> 47))) * r11;
        return (r3 ^ (r3 >>> 47)) * r11;
    }

    private static long hashLength17to32(byte[] r18, int r19, int r20) {
        long r10 = (((long) r20) * 2) + f123K2;
        long r2 = LittleEndianByteArray.load64(r18, r19) * f122K1;
        long r12 = LittleEndianByteArray.load64(r18, r19 + 8);
        long r14 = LittleEndianByteArray.load64(r18, (r19 + r20) - 8) * r10;
        return hashLength16((Long.rotateRight(r2 + r12, 43) + Long.rotateRight(r14, 30)) + (LittleEndianByteArray.load64(r18, (r19 + r20) - 16) * f123K2), (Long.rotateRight(f123K2 + r12, 18) + r2) + r14, r10);
    }

    private static long hashLength33To64(byte[] r30, int r31, int r32) {
        long r10 = (((long) r32) * 2) + f123K2;
        long r2 = LittleEndianByteArray.load64(r30, r31) * f123K2;
        long r12 = LittleEndianByteArray.load64(r30, r31 + 8);
        long r14 = LittleEndianByteArray.load64(r30, (r31 + r32) - 8) * r10;
        long r6 = (Long.rotateRight(r2 + r12, 43) + Long.rotateRight(r14, 30)) + (LittleEndianByteArray.load64(r30, (r31 + r32) - 16) * f123K2);
        long r18 = hashLength16(r6, (Long.rotateRight(f123K2 + r12, 18) + r2) + r14, r10);
        long r22 = LittleEndianByteArray.load64(r30, r31 + 16) * r10;
        long r24 = LittleEndianByteArray.load64(r30, r31 + 24);
        long r7 = (r6 + LittleEndianByteArray.load64(r30, (r31 + r32) - 32)) * r10;
        return hashLength16((Long.rotateRight(r22 + r24, 43) + Long.rotateRight(r7, 30)) + ((r18 + LittleEndianByteArray.load64(r30, (r31 + r32) - 24)) * r10), (r22 + Long.rotateRight(r24 + r2, 18)) + r7, r10);
    }

    private static long hashLength65Plus(byte[] r31, int r32, int r33) {
        long r8 = f122K1;
        long r3 = (((long) 81) * f122K1) + 113;
        long r13 = shiftMix((r3 * f123K2) + 113) * f123K2;
        long[] r6 = new long[2];
        long[] r12 = new long[2];
        long r10 = (f123K2 * ((long) 81)) + LittleEndianByteArray.load64(r31, r32);
        int r15 = r32 + (((r33 - 1) / 64) * 64);
        int r16 = (((r33 - 1) & 63) + r15) - 63;
        int r1 = r32;
    L3:
        long r22 = r8;
        long r9 = Long.rotateRight(((r10 + r3) + r6[0]) + LittleEndianByteArray.load64(r31, r1 + 8), 37) * r22;
        long r2 = Long.rotateRight((r3 + r6[1]) + LittleEndianByteArray.load64(r31, r1 + 48), 42) * r22;
        long r11 = r9 ^ r12[1];
        long r20 = r2 + (r6[0] + LittleEndianByteArray.load64(r31, r1 + 40));
        long r14 = Long.rotateRight(r12[0] + r13, 33) * r22;
        weakHashLength32WithSeeds(r31, r1, r6[1] * r22, r12[0] + r11, r6);
        int r25 = r1;
        long[] r24 = r6;
        weakHashLength32WithSeeds(r31, r25 + 32, r12[1] + r14, r20 + LittleEndianByteArray.load64(r31, r25 + 16), r12);
        r13 = r11;
        r1 = r25 + 64;
        if (r1 == r15) goto L5;
        r10 = r14;
        r3 = r20;
        r8 = r22;
        r6 = r24;
        goto L3
    L5:
        long r29 = ((255 & r13) << 1) + r22;
        r12[0] = r12[0] + ((long) ((r33 - 1) & 63));
        r24[0] = r24[0] + r12[0];
        r12[0] = r12[0] + r24[0];
        long r5 = Long.rotateRight(((r14 + r20) + r24[0]) + LittleEndianByteArray.load64(r31, r16 + 8), 37) * r29;
        long r4 = Long.rotateRight((r20 + r24[1]) + LittleEndianByteArray.load64(r31, r16 + 48), 42) * r29;
        long r17 = (r12[1] * 9) ^ r5;
        long r18 = r4 + ((r24[0] * 9) + LittleEndianByteArray.load64(r31, r16 + 40));
        long r19 = Long.rotateRight(r12[0] + r13, 33) * r29;
        weakHashLength32WithSeeds(r31, r16, r24[1] * r29, r12[0] + r17, r24);
        weakHashLength32WithSeeds(r31, r16 + 32, r12[1] + r19, LittleEndianByteArray.load64(r31, r16 + 16) + r18, r12);
        return hashLength16((hashLength16(r24[0], r12[0], r29) + (shiftMix(r18) * f121K0)) + r17, hashLength16(r24[1], r12[1], r29) + r19, r29);
    }

    private static long shiftMix(long r2) {
        return (r2 >>> 47) ^ r2;
    }

    private static void weakHashLength32WithSeeds(byte[] r18, int r19, long r20, long r22, long[] r24) {
        long r1 = LittleEndianByteArray.load64(r18, r19);
        long r3 = LittleEndianByteArray.load64(r18, r19 + 8);
        long r5 = LittleEndianByteArray.load64(r18, r19 + 16);
        long r7 = LittleEndianByteArray.load64(r18, r19 + 24);
        long r9 = r20 + r1;
        long r11 = Long.rotateRight((r22 + r9) + r7, 21);
        long r10 = (r9 + r3) + r5;
        long r12 = r11 + Long.rotateRight(r10, 44);
        r24[0] = r10 + r7;
        r24[1] = r12 + r9;
    }

    @Override // com.google.common.hash.HashFunction
    public int bits() {
        return 64;
    }

    @Override // com.google.common.hash.AbstractNonStreamingHashFunction, com.google.common.hash.AbstractHashFunction, com.google.common.hash.HashFunction
    public HashCode hashBytes(byte[] r3, int r4, int r5) {
        Preconditions.checkPositionIndexes(r4, r4 + r5, r3.length);
        return HashCode.fromLong(fingerprint(r3, r4, r5));
    }

    public String toString() {
        return "Hashing.farmHashFingerprint64()";
    }
}
