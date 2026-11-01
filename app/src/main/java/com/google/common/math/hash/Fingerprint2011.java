package com.google.common.hash;

import com.google.common.base.Preconditions;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class Fingerprint2011 extends AbstractNonStreamingHashFunction {
    static final HashFunction FINGERPRINT_2011 = null;

    /* JADX INFO: renamed from: K0 */
    private static final long f124K0 = -6505348102511208375L;

    /* JADX INFO: renamed from: K1 */
    private static final long f125K1 = -8261664234251669945L;

    /* JADX INFO: renamed from: K2 */
    private static final long f126K2 = -4288712594273399085L;

    /* JADX INFO: renamed from: K3 */
    private static final long f127K3 = -4132994306676758123L;

    static {
        FINGERPRINT_2011 = new Fingerprint2011();
    }

    Fingerprint2011() {
    }

    static long fingerprint(byte[] r9, int r10, int r11) {
        if (r11 > 32) goto L6;
        long r0 = murmurHash64WithSeed(r9, r10, r11, -1397348546323613475L);
    L9:
        long r2 = f124K0;
        if (r11 < 8) goto L12;
        long r5 = LittleEndianByteArray.load64(r9, r10);
    L14:
        if (r11 < 9) goto L16;
        r2 = LittleEndianByteArray.load64(r9, (r10 + r11) - 8);
    L16:
        long r1 = hash128to64(r0 + r2, r5);
        if (r1 == 0) goto L23;
        if (r1 == 1) goto L23;
        return r1;
    L23:
        return (-2) + r1;
    L12:
        r5 = -6505348102511208375L;
        goto L14
    L6:
        if (r11 > 64) goto L8;
        r0 = hashLength33To64(r9, r10, r11);
        goto L9
    L8:
        r0 = fullFingerprint(r9, r10, r11);
        goto L9
    }

    private static long fullFingerprint(byte[] r22, int r23, int r24) {
        long r8 = LittleEndianByteArray.load64(r22, r23);
        long r4 = LittleEndianByteArray.load64(r22, (r23 + r24) - 16) ^ f125K1;
        long r12 = f124K0 ^ LittleEndianByteArray.load64(r22, (r23 + r24) - 56);
        long[] r6 = new long[2];
        long[] r14 = new long[2];
        weakHashLength32WithSeeds(r22, (r23 + r24) - 64, r24, r4, r6);
        byte[] r0 = r22;
        weakHashLength32WithSeeds(r0, (r23 + r24) - 32, ((long) r24) * f125K1, f124K0, r14);
        long r13 = r12 + (shiftMix(r6[1]) * f125K1);
        long r1 = Long.rotateRight(r13 + r8, 39) * f125K1;
        int r7 = (r24 - 1) & (-64);
        long r5 = Long.rotateRight(r4, 33) * f125K1;
        long r2 = r1;
        int r3 = r23;
    L3:
        long r9 = Long.rotateRight(((r2 + r5) + r6[0]) + LittleEndianByteArray.load64(r0, r3 + 16), 37) * f125K1;
        long r10 = Long.rotateRight((r6[1] + r5) + LittleEndianByteArray.load64(r0, r3 + 48), 42) * f125K1;
        long r11 = r9 ^ r14[1];
        long r20 = r10 ^ r6[0];
        long r15 = Long.rotateRight(r14[0] ^ r13, 33);
        weakHashLength32WithSeeds(r0, r3, r6[1] * f125K1, r14[0] + r11, r6);
        int r17 = r3;
        long[] r16 = r14;
        r5 = r20;
        weakHashLength32WithSeeds(r22, r17 + 32, r14[1] + r15, r5, r16);
        r13 = r11;
        r2 = r15;
        int r18 = r17 + 64;
        r7 = r7 - 64;
        if (r7 == 0) goto L6;
        r0 = r22;
        r14 = r16;
        r3 = r18;
        goto L3
    L6:
        return hash128to64((hash128to64(r6[0], r16[0]) + (shiftMix(r5) * f125K1)) + r13, hash128to64(r6[1], r16[1]) + r2);
    }

    static long hash128to64(long r9, long r11) {
        long r0 = (r11 ^ r9) * f127K3;
        long r5 = (r9 ^ (r0 ^ (r0 >>> 47))) * f127K3;
        return (r5 ^ (r5 >>> 47)) * f127K3;
    }

    private static long hashLength33To64(byte[] r27, int r28, int r29) {
        long r2 = LittleEndianByteArray.load64(r27, r28 + 24);
        long r4 = LittleEndianByteArray.load64(r27, r28) + ((((long) r29) + LittleEndianByteArray.load64(r27, (r28 + r29) - 16)) * f124K0);
        long r6 = Long.rotateRight(r4 + r2, 52);
        long r12 = Long.rotateRight(r4, 37);
        long r5 = r4 + LittleEndianByteArray.load64(r27, r28 + 8);
        long r13 = r12 + Long.rotateRight(r5, 7);
        long r7 = r5 + LittleEndianByteArray.load64(r27, r28 + 16);
        long r15 = r7 + r2;
        long r19 = (r6 + Long.rotateRight(r7, 31)) + r13;
        long r8 = LittleEndianByteArray.load64(r27, r28 + 16) + LittleEndianByteArray.load64(r27, (r28 + r29) - 32);
        long r3 = LittleEndianByteArray.load64(r27, (r28 + r29) - 8);
        long r9 = Long.rotateRight(r8 + r3, 52);
        long r10 = Long.rotateRight(r8, 37);
        long r11 = r8 + LittleEndianByteArray.load64(r27, (r28 + r29) - 24);
        long r14 = r10 + Long.rotateRight(r11, 7);
        long r16 = r11 + LittleEndianByteArray.load64(r27, (r28 + r29) - 16);
        return shiftMix((f124K0 * shiftMix(((r15 + ((Long.rotateRight(r16, 31) + r9) + r14)) * f126K2) + (((r16 + r3) + r19) * f124K0))) + r19) * f126K2;
    }

    static long murmurHash64WithSeed(byte[] r12, int r13, int r14, long r15) {
        int r3 = (~7) & r14;
        int r4 = r14 & 7;
        long r5 = (((long) r14) * f127K3) ^ r15;
        int r7 = 0;
    L3:
        if (r7 >= r3) goto L5;
        r5 = (r5 ^ (shiftMix(LittleEndianByteArray.load64(r12, r13 + r7) * f127K3) * f127K3)) * f127K3;
        r7 = r7 + 8;
        goto L3
    L5:
        if (r4 == 0) goto L8;
        r5 = (r5 ^ LittleEndianByteArray.load64Safely(r12, r13 + r3, r4)) * f127K3;
    L8:
        return shiftMix(shiftMix(r5) * f127K3);
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
        long r11 = Long.rotateRight((r22 + r9) + r7, 51);
        long r10 = (r9 + r3) + r5;
        long r12 = r11 + Long.rotateRight(r10, 23);
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
        return "Hashing.fingerprint2011()";
    }
}
