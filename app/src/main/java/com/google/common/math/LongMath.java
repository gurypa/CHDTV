package com.google.common.math;

import androidx.media3.common.C0180C;
import androidx.media3.exoplayer.MediaPeriodQueue;
import androidx.media3.exoplayer.Renderer;
import androidx.media3.exoplayer.audio.SilenceSkippingAudioProcessor;
import androidx.media3.exoplayer.upstream.CmcdData;
import androidx.media3.extractor.p001ts.TsExtractor;
import com.google.common.base.Ascii;
import com.google.common.base.Preconditions;
import java.math.RoundingMode;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class LongMath {
    static final long FLOOR_SQRT_MAX_LONG = 3037000499L;
    static final long MAX_POWER_OF_SQRT2_UNSIGNED = -5402926248376769404L;
    static final long MAX_SIGNED_POWER_OF_TWO = 4611686018427387904L;
    private static final int SIEVE_30 = -545925251;
    static final int[] biggestBinomials = null;
    static final int[] biggestSimpleBinomials = null;
    static final long[] factorials = null;
    static final long[] halfPowersOf10 = null;
    static final byte[] maxLog10ForLeadingZeros = null;
    private static final long[][] millerRabinBaseSets = null;
    static final long[] powersOf10 = null;

    /* JADX INFO: renamed from: com.google.common.math.LongMath$1 */
    static /* synthetic */ class C08621 {
        static final /* synthetic */ int[] $SwitchMap$java$math$RoundingMode = null;

        static {
            $SwitchMap$java$math$RoundingMode = new int[RoundingMode.values().length];
            $SwitchMap$java$math$RoundingMode[RoundingMode.UNNECESSARY.ordinal()] = 1;     // Catch: NoSuchFieldError -> L5
        L40:
            $SwitchMap$java$math$RoundingMode[RoundingMode.DOWN.ordinal()] = 2;     // Catch: NoSuchFieldError -> L8
        L30:
            $SwitchMap$java$math$RoundingMode[RoundingMode.FLOOR.ordinal()] = 3;     // Catch: NoSuchFieldError -> L11
        L28:
            $SwitchMap$java$math$RoundingMode[RoundingMode.UP.ordinal()] = 4;     // Catch: NoSuchFieldError -> L14
        L32:
            $SwitchMap$java$math$RoundingMode[RoundingMode.CEILING.ordinal()] = 5;     // Catch: NoSuchFieldError -> L17
        L36:
            $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_DOWN.ordinal()] = 6;     // Catch: NoSuchFieldError -> L20
        L42:
            $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_UP.ordinal()] = 7;     // Catch: NoSuchFieldError -> L23
        L38:
            $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_EVEN.ordinal()] = 8;     // Catch: NoSuchFieldError -> L26
            return;
        }
    }

    private static abstract enum MillerRabinTester extends Enum<MillerRabinTester> {
        private static final /* synthetic */ MillerRabinTester[] $VALUES = null;
        public static final MillerRabinTester LARGE = null;
        public static final MillerRabinTester SMALL = null;

        private static /* synthetic */ MillerRabinTester[] $values() {
            return new MillerRabinTester[]{SMALL, LARGE};
        }

        static {
            final String r1 = "SMALL";
            final int r2 = 0;
            SMALL = new C08631(r1, r2);
            final String r3 = "LARGE";
            final int r4 = 1;
            LARGE = new C08642(r3, r4);
            $VALUES = $values();
        }

        private MillerRabinTester(String r1, int r2) {
            super(r1, r2);
        }

        /* synthetic */ MillerRabinTester(String r1, int r2, C08621 r3) {
            this(r1, r2);
        }

        private long powMod(long r10, long r12, long r14) {
            long r5 = r10;
            long r3 = 1;
        L4:
            if (r12 == 0) goto L10;
            if ((1 & r12) == 0) goto L8;
            long r7 = r14;
            r3 = mulMod(r3, r5, r7);
        L9:
            r5 = squareMod(r5, r7);
            r12 = r12 >> 1;
            r14 = r7;
            goto L4
        L8:
            r7 = r14;
            goto L9
        L10:
            return r3;
        }

        static boolean test(long r2, long r4) {
            if (r4 > LongMath.FLOOR_SQRT_MAX_LONG) goto L5;
            MillerRabinTester r0 = SMALL;
        L7:
            return r0.testWitness(r2, r4);
        L5:
            r0 = LARGE;
            goto L7
        }

        private boolean testWitness(long r16, long r18) {
            int r9 = Long.numberOfTrailingZeros(r18 - 1);
            long r3 = (r18 - 1) >> r9;
            long r1 = r16 % r18;
            if (r1 != 0) goto L5;
            return true;
        L5:
            long r11 = powMod(r1, r3, r18);
            if (r11 != 1) goto L8;
            return true;
        L8:
            int r0 = 0;
        L10:
            if (r11 == (r18 - 1)) goto L16;
            r0 = r0 + 1;
            if (r0 == r9) goto L13;
            r11 = squareMod(r11, r18);
            goto L10
        L13:
            return false;
        L16:
            return true;
        }

        public static MillerRabinTester valueOf(String r1) {
            return (MillerRabinTester) Enum.valueOf(MillerRabinTester.class, r1);
        }

        public static MillerRabinTester[] values() {
            return (MillerRabinTester[]) $VALUES.clone();
        }

        abstract long mulMod(long r1, long r3, long r5);

        abstract long squareMod(long r1, long r3);
    }

    static {
        maxLog10ForLeadingZeros = new byte[]{19, Ascii.DC2, Ascii.DC2, Ascii.DC2, Ascii.DC2, 17, 17, 17, Ascii.DLE, Ascii.DLE, Ascii.DLE, Ascii.f88SI, Ascii.f88SI, Ascii.f88SI, Ascii.f88SI, Ascii.f89SO, Ascii.f89SO, Ascii.f89SO, Ascii.f79CR, Ascii.f79CR, Ascii.f79CR, Ascii.f81FF, Ascii.f81FF, Ascii.f81FF, Ascii.f81FF, Ascii.f92VT, Ascii.f92VT, Ascii.f92VT, 10, 10, 10, 9, 9, 9, 9, 8, 8, 8, 7, 7, 7, 6, 6, 6, 6, 5, 5, 5, 4, 4, 4, 3, 3, 3, 3, 2, 2, 2, 1, 1, 1, 0, 0, 0};
        powersOf10 = new long[]{1, 10, 100, 1000, Renderer.DEFAULT_DURATION_TO_PROGRESS_US, SilenceSkippingAudioProcessor.DEFAULT_MINIMUM_SILENCE_DURATION_US, 1000000, 10000000, 100000000, C0180C.NANOS_PER_SECOND, 10000000000L, 100000000000L, MediaPeriodQueue.INITIAL_RENDERER_POSITION_OFFSET_US, 10000000000000L, 100000000000000L, 1000000000000000L, 10000000000000000L, 100000000000000000L, 1000000000000000000L};
        halfPowersOf10 = new long[]{3, 31, 316, 3162, 31622, 316227, 3162277, 31622776, 316227766, 3162277660L, 31622776601L, 316227766016L, 3162277660168L, 31622776601683L, 316227766016837L, 3162277660168379L, 31622776601683793L, 316227766016837933L, 3162277660168379331L};
        factorials = new long[]{1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880, 3628800, 39916800, 479001600, 6227020800L, 87178291200L, 1307674368000L, 20922789888000L, 355687428096000L, 6402373705728000L, 121645100408832000L, 2432902008176640000L};
        biggestBinomials = new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 3810779, 121977, 16175, 4337, 1733, 887, 534, 361, 265, 206, 169, 143, 125, 111, 101, 94, 88, 83, 79, 76, 74, 72, 70, 69, 68, 67, 67, 66, 66, 66, 66};
        biggestSimpleBinomials = new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 2642246, 86251, 11724, 3218, 1313, 684, 419, 287, 214, 169, TsExtractor.TS_STREAM_TYPE_DTS_UHD, 119, 105, 95, 87, 81, 76, 73, 70, 68, 66, 64, 63, 62, 62, 61, 61, 61};
        millerRabinBaseSets = new long[][]{new long[]{291830, 126401071349994536L}, new long[]{885594168, 725270293939359937L, 3569819667048198375L}, new long[]{273919523040L, 15, 7363882082L, 992620450144556L}, new long[]{47636622961200L, 2, 2570940, 211991001, 3749873356L}, new long[]{7999252175582850L, 2, 4130806001517L, 149795463772692060L, 186635894390467037L, 3967304179347715805L}, new long[]{585226005592931976L, 2, 123635709730000L, 9233062284813009L, 43835965440333360L, 761179012939631437L, 1263739024124850375L}, new long[]{Long.MAX_VALUE, 2, 325, 9375, 28178, 450775, 9780504, 1795265022}};
    }

    private LongMath() {
    }

    public static long binomial(int r13, int r14) {
        MathPreconditions.checkNonNegative("n", r13);
        MathPreconditions.checkNonNegative("k", r14);
        if (r14 > r13) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0, "k (%s) > n (%s)", r14, r13);
        if (r14 <= (r13 >> 1)) goto L9;
        r14 = r13 - r14;
    L9:
        switch(r14) {
            case 0: goto L16;
            case 1: goto L15;
            default: goto L11;
        };
    L16:
        return 1;
    L11:
        if (r13 >= factorials.length) goto L19;
        return factorials[r13] / (factorials[r14] * factorials[r13 - r14]);
    L19:
        if (r14 < biggestBinomials.length) goto L21;
        return Long.MAX_VALUE;
    L21:
        if (r13 <= biggestBinomials[r14]) goto L24;
        return Long.MAX_VALUE;
    L24:
        if (r14 < biggestSimpleBinomials.length) goto L26;
    L31:
        int r1 = log2(r13, RoundingMode.CEILING);
        long r2 = 1;
        int r3 = r13 - 1;
        long r4 = r13;
        int r15 = r1;
        int r8 = 2;
        int r7 = r3;
        long r5 = r4;
        long r6 = 1;
    L32:
        if (r8 > r14) goto L39;
        if ((r15 + r1) >= 63) goto L36;
        r5 = r5 * ((long) r7);
        r6 = r6 * ((long) r8);
        r15 = r15 + r1;
    L37:
        r8 = r8 + 1;
        r7 = r7 - 1;
        goto L32
    L36:
        r2 = multiplyFraction(r2, r5, r6);
        r5 = r7;
        r6 = r8;
        r15 = r1;
        goto L37
    L39:
        return multiplyFraction(r2, r5, r6);
    L26:
        if (r13 > biggestSimpleBinomials[r14]) goto L31;
        int r9 = r13 - 1;
        long r10 = r13;
        int r16 = 2;
    L28:
        if (r16 > r14) goto L30;
        r10 = (r10 * ((long) r9)) / ((long) r16);
        r9 = r9 - 1;
        r16 = r16 + 1;
        goto L28
    L30:
        return r10;
    L15:
        return r13;
    L5:
        r0 = false;
        goto L6
    }

    public static long ceilingPowerOfTwo(long r4) {
        MathPreconditions.checkPositive("x", r4);
        if (r4 > 4611686018427387904L) goto L7;
        return 1 << (-Long.numberOfLeadingZeros(r4 - 1));
    L7:
        throw new ArithmeticException("ceilingPowerOfTwo(" + r4 + ") is not representable as a long");
    }

    public static long checkedAdd(long r10, long r12) {
        long r0 = r10 + r12;
        boolean r3 = true;
        if ((r10 ^ r12) >= 0) goto L5;
        boolean r2 = true;
    L7:
        if ((r10 ^ r0) >= 0) goto L10;
        r3 = false;
    L10:
        MathPreconditions.checkNoOverflow(r2 | r3, "checkedAdd", r10, r12);
        return r0;
    L5:
        r2 = false;
        goto L7
    }

    public static long checkedMultiply(long r12, long r14) {
        int r0 = ((Long.numberOfLeadingZeros(r12) + Long.numberOfLeadingZeros(~r12)) + Long.numberOfLeadingZeros(r14)) + Long.numberOfLeadingZeros(~r14);
        if (r0 <= 65) goto L7;
        return r12 * r14;
    L7:
        if (r0 < 64) goto L9;
        boolean r4 = true;
    L10:
        MathPreconditions.checkNoOverflow(r4, "checkedMultiply", r12, r14);
        if (r12 < 0) goto L13;
        boolean r15 = true;
    L15:
        if (r14 == Long.MIN_VALUE) goto L17;
        boolean r16 = true;
    L18:
        MathPreconditions.checkNoOverflow(r15 | r16, "checkedMultiply", r12, r14);
        long r17 = r12 * r14;
        if (r12 != 0) goto L21;
    L24:
        boolean r6 = true;
    L25:
        MathPreconditions.checkNoOverflow(r6, "checkedMultiply", r12, r14);
        return r17;
    L21:
        if ((r17 / r12) == r14) goto L24;
        r6 = false;
        goto L25
    L17:
        r16 = false;
        goto L18
    L13:
        r15 = false;
        goto L15
    L9:
        r4 = false;
        goto L10
    }

    public static long checkedPow(long r11, int r13) {
        MathPreconditions.checkNonNegative("exponent", r13);
        if (r11 < (-2)) goto L5;
        boolean r0 = true;
    L7:
        if (r11 > 2) goto L9;
        boolean r5 = true;
    L11:
        if ((r0 & r5) == true) goto L13;
        long r2 = 1;
        long r7 = r11;
        int r6 = r13;
    L41:
        switch(r6) {
            case 0: goto L47;
            case 1: goto L46;
            default: goto L43;
        };
    L43:
        if ((r6 & 1) == 0) goto L48;
        r2 = checkedMultiply(r2, r7);
    L48:
        r6 = r6 >> 1;
        if (r6 <= 0) goto L41;
        if ((-3037000499L) <= r7) goto L53;
    L55:
        boolean r8 = false;
    L56:
        MathPreconditions.checkNoOverflow(r8, "checkedPow", r7, r6);
        r7 = r7 * r7;
        goto L41
    L53:
        if (r7 > FLOOR_SQRT_MAX_LONG) goto L55;
        r8 = true;
        goto L56
    L46:
        return checkedMultiply(r2, r7);
    L47:
        return r2;
    L13:
        switch(((int) r11)) {
            case -2: goto L32;
            case -1: goto L28;
            case 0: goto L23;
            case 1: goto L22;
            case 2: goto L17;
            default: goto L15;
        };
    L22:
        return 1;
    L23:
        if (r13 == 0) goto L65;
        return 0;
    L65:
        return 1;
    L15:
        throw new AssertionError();
    L17:
        if (r13 >= 63) goto L19;
        boolean r1 = true;
    L20:
        MathPreconditions.checkNoOverflow(r1, "checkedPow", r11, r13);
        return 1 << r13;
    L19:
        r1 = false;
        goto L20
    L28:
        if ((r13 & 1) == 0) goto L30;
        return -1;
    L30:
        return 1;
    L32:
        if (r13 >= 64) goto L34;
        boolean r3 = true;
    L35:
        MathPreconditions.checkNoOverflow(r3, "checkedPow", r11, r13);
        if ((r13 & 1) != 0) goto L39;
        return 1 << r13;
    L39:
        return (-1) << r13;
    L34:
        r3 = false;
        goto L35
    L9:
        r5 = false;
        goto L11
    L5:
        r0 = false;
        goto L7
    }

    public static long checkedSubtract(long r10, long r12) {
        long r0 = r10 - r12;
        boolean r3 = true;
        if ((r10 ^ r12) < 0) goto L5;
        boolean r2 = true;
    L7:
        if ((r10 ^ r0) >= 0) goto L10;
        r3 = false;
    L10:
        MathPreconditions.checkNoOverflow(r2 | r3, "checkedSubtract", r10, r12);
        return r0;
    L5:
        r2 = false;
        goto L7
    }

    public static long divide(long r17, long r19, RoundingMode r21) {
        Preconditions.checkNotNull(r21);
        long r1 = r17 / r19;
        long r3 = r17 - (r19 * r1);
        if (r3 != 0) goto L5;
        return r1;
    L5:
        boolean r8 = true;
        int r7 = ((int) ((r17 ^ r19) >> 63)) | 1;
        switch(C08621.$SwitchMap$java$math$RoundingMode[r21.ordinal()]) {
            case 1: goto L35;
            case 2: goto L39;
            case 3: goto L30;
            case 4: goto L29;
            case 5: goto L25;
            case 6: goto L9;
            case 7: goto L9;
            case 8: goto L9;
            default: goto L8;
        };
    L9:
        long r11 = Math.abs(r3);
        long r13 = r11 - (Math.abs(r19) - r11);
        if (r13 != 0) goto L21;
        if (r21 != RoundingMode.HALF_UP) goto L14;
    L40:
        if (r8 == false) goto L43;
        return ((long) r7) + r1;
    L43:
        return r1;
    L14:
        if (r21 == RoundingMode.HALF_EVEN) goto L16;
    L18:
        r8 = false;
        goto L40
    L16:
        if ((1 & r1) == 0) goto L18;
    L21:
        if (r13 > 0) goto L40;
        r8 = false;
        goto L40
    L25:
        if (r7 > 0) goto L40;
        r8 = false;
        goto L40
    L29:
        r8 = true;
        goto L40
    L30:
        if (r7 < 0) goto L40;
        r8 = false;
    L39:
        r8 = false;
        goto L40
    L8:
        throw new AssertionError();
    L35:
        if (r3 == 0) goto L38;
        r8 = false;
    L38:
        MathPreconditions.checkRoundingUnnecessary(r8);
        goto L39
    }

    public static long factorial(int r3) {
        MathPreconditions.checkNonNegative("n", r3);
        if (r3 < factorials.length) goto L7;
        return Long.MAX_VALUE;
    L7:
        return factorials[r3];
    }

    static boolean fitsInInt(long r2) {
        if (((int) r2) != r2) goto L5;
        return true;
    L5:
        return false;
    }

    public static long floorPowerOfTwo(long r3) {
        MathPreconditions.checkPositive("x", r3);
        return 1 << (63 - Long.numberOfLeadingZeros(r3));
    }

    public static long gcd(long r8, long r10) {
        MathPreconditions.checkNonNegative(CmcdData.Factory.OBJECT_TYPE_AUDIO_ONLY, r8);
        MathPreconditions.checkNonNegative("b", r10);
        if (r8 != 0) goto L6;
        return r10;
    L6:
        if (r10 != 0) goto L8;
        return r8;
    L8:
        int r0 = Long.numberOfTrailingZeros(r8);
        long r9 = r8 >> r0;
        int r1 = Long.numberOfTrailingZeros(r10);
        long r11 = r10 >> r1;
    L10:
        if (r9 == r11) goto L13;
        long r2 = r9 - r11;
        long r4 = (r2 >> 63) & r2;
        long r6 = (r2 - r4) - r4;
        r11 = r11 + r4;
        r9 = r6 >> Long.numberOfTrailingZeros(r6);
        goto L10
    L13:
        return r9 << Math.min(r0, r1);
    }

    public static boolean isPowerOfTwo(long r7) {
        boolean r3 = true;
        if (r7 <= 0) goto L5;
        boolean r2 = true;
    L7:
        if (((r7 - 1) & r7) == 0) goto L11;
        r3 = false;
    L11:
        return r2 & r3;
    L5:
        r2 = false;
        goto L7
    }

    public static boolean isPrime(long r11) {
        if (r11 >= 2) goto L7;
        MathPreconditions.checkNonNegative("n", r11);
        return false;
    L7:
        if (r11 >= 66) goto L13;
        if (((722865708377213483 >> (((int) r11) - 2)) & 1) != 0) goto L11;
        return false;
    L11:
        return true;
    L13:
        if (((1 << ((int) (r11 % 30))) & SIEVE_30) == 0) goto L16;
        return false;
    L16:
        if ((r11 % 7) != 0) goto L18;
    L40:
        return false;
    L18:
        if ((r11 % 11) == 0) goto L40;
        if ((r11 % 13) == 0) goto L40;
        if (r11 >= 289) goto L25;
        return true;
    L25:
        long[][] r0 = millerRabinBaseSets;
        int r2 = r0.length;
        int r3 = 0;
    L26:
        if (r3 >= r2) goto L39;
        long[] r5 = r0[r3];
        if (r11 <= r5[0]) goto L29;
        r3 = r3 + 1;
        goto L26
    L29:
        int r1 = 1;
    L31:
        if (r1 >= r5.length) goto L36;
        if (MillerRabinTester.test(r5[r1], r11) == false) goto L34;
        r1 = r1 + 1;
        goto L31
    L34:
        return false;
    L36:
        return true;
    L39:
        throw new AssertionError();
    }

    static int lessThanBranchFree(long r3, long r5) {
        return (int) ((~(~(r3 - r5))) >>> 63);
    }

    public static int log10(long r6, RoundingMode r8) {
        MathPreconditions.checkPositive("x", r6);
        int r0 = log10Floor(r6);
        long r2 = powersOf10[r0];
        switch(C08621.$SwitchMap$java$math$RoundingMode[r8.ordinal()]) {
            case 1: goto L11;
            case 2: goto L15;
            case 3: goto L15;
            case 4: goto L9;
            case 5: goto L9;
            case 6: goto L7;
            case 7: goto L7;
            case 8: goto L7;
            default: goto L5;
        };
    L15:
        return r0;
    L5:
        throw new AssertionError();
    L7:
        return lessThanBranchFree(halfPowersOf10[r0], r6) + r0;
    L9:
        return lessThanBranchFree(r2, r6) + r0;
    L11:
        if (r6 != r2) goto L13;
        boolean r1 = true;
    L14:
        MathPreconditions.checkRoundingUnnecessary(r1);
        goto L15
    L13:
        r1 = false;
        goto L14
    }

    static int log10Floor(long r4) {
        byte r0 = maxLog10ForLeadingZeros[Long.numberOfLeadingZeros(r4)];
        return r0 - lessThanBranchFree(r4, powersOf10[r0]);
    }

    public static int log2(long r5, RoundingMode r7) {
        MathPreconditions.checkPositive("x", r5);
        switch(C08621.$SwitchMap$java$math$RoundingMode[r7.ordinal()]) {
            case 1: goto L10;
            case 2: goto L12;
            case 3: goto L12;
            case 4: goto L9;
            case 5: goto L9;
            case 6: goto L6;
            case 7: goto L6;
            case 8: goto L6;
            default: goto L5;
        };
    L6:
        int r0 = Long.numberOfLeadingZeros(r5);
        int r4 = lessThanBranchFree(MAX_POWER_OF_SQRT2_UNSIGNED >>> r0, r5);
        return r4 + (63 - r0);
    L10:
        MathPreconditions.checkRoundingUnnecessary(isPowerOfTwo(r5));
        goto L12
    L5:
        throw new AssertionError("impossible");
    L9:
        return 64 - Long.numberOfLeadingZeros(r5 - 1);
    L12:
        return 63 - Long.numberOfLeadingZeros(r5);
    }

    public static long mean(long r5, long r7) {
        return (r5 & r7) + ((r5 ^ r7) >> 1);
    }

    public static int mod(long r2, int r4) {
        return (int) mod(r2, r4);
    }

    public static long mod(long r4, long r6) {
        if (r6 <= 0) goto L10;
        long r2 = r4 % r6;
        if (r2 < 0) goto L8;
        return r2;
    L8:
        return r2 + r6;
    L10:
        throw new ArithmeticException("Modulus must be positive");
    }

    static long multiplyFraction(long r4, long r6, long r8) {
        if (r4 == 1) goto L5;
        long r0 = gcd(r4, r8);
        long r2 = r6 / (r8 / r0);
        return r2 * (r4 / r0);
    L5:
        return r6 / r8;
    }

    public static long pow(long r7, int r9) {
        MathPreconditions.checkNonNegative("exponent", r9);
        if ((-2) <= r7) goto L5;
    L30:
        long r3 = 1;
    L31:
        switch(r9) {
            case 0: goto L37;
            case 1: goto L36;
            default: goto L33;
        };
    L33:
        if ((r9 & 1) != 0) goto L38;
        long r5 = 1;
    L39:
        r3 = r3 * r5;
        r7 = r7 * r7;
        r9 = r9 >> 1;
        goto L31
    L38:
        r5 = r7;
        goto L39
    L36:
        return r3 * r7;
    L37:
        return r3;
    L5:
        if (r7 > 2) goto L30;
        switch(((int) r7)) {
            case -2: goto L23;
            case -1: goto L19;
            case 0: goto L14;
            case 1: goto L13;
            case 2: goto L10;
            default: goto L9;
        };
    L10:
        if (r9 < 64) goto L12;
        return 0;
    L12:
        return 1 << r9;
    L13:
        return 1;
    L14:
        if (r9 != 0) goto L17;
        return 1;
    L17:
        return 0;
    L23:
        if (r9 < 64) goto L25;
        return 0;
    L25:
        if ((r9 & 1) != 0) goto L28;
        return 1 << r9;
    L28:
        return -(1 << r9);
    L9:
        throw new AssertionError();
    L19:
        if ((r9 & 1) == 0) goto L46;
        return -1;
    L46:
        return 1;
    }

    public static double roundToDouble(long r25, RoundingMode r27) {
        double r2 = r25;
        long r4 = (long) r2;
        if (r4 != Long.MAX_VALUE) goto L5;
        int r8 = -1;
    L7:
        switch(C08621.$SwitchMap$java$math$RoundingMode[r27.ordinal()]) {
            case 1: goto L72;
            case 2: goto L62;
            case 3: goto L57;
            case 4: goto L47;
            case 5: goto L42;
            case 6: goto L10;
            case 7: goto L10;
            case 8: goto L10;
            default: goto L9;
        };
    L10:
        if (r8 < 0) goto L12;
        double r13 = r2;
        double r17 = Math.nextUp(r2);
        long r6 = (long) Math.ceil(r17);
        long r11 = r4;
        long r15 = 0;
    L13:
        long r0 = r25 - r11;
        long r21 = r6 - r25;
        if (r6 != 9223372036854775807(0x7fffffffffffffff, double:NaN)) goto L16;
        long r3 = r21 + 1;
    L17:
        int r9 = Long.compare(r0, r3);
        if (r9 >= 0) goto L20;
        return r13;
    L20:
        if (r9 <= 0) goto L23;
        return r17;
    L23:
        switch(C08621.$SwitchMap$java$math$RoundingMode[r27.ordinal()]) {
            case 6: goto L37;
            case 7: goto L32;
            case 8: goto L27;
            default: goto L25;
        };
    L25:
        throw new AssertionError("impossible");
    L27:
        if ((DoubleUtils.getSignificand(r13) & 1) != r15) goto L30;
        return r13;
    L30:
        return r17;
    L32:
        if (r25 < r15) goto L35;
        return r17;
    L35:
        return r13;
    L37:
        if (r25 < r15) goto L40;
        return r13;
    L40:
        return r17;
    L16:
        r3 = r21;
        goto L17
    L12:
        r17 = r2;
        r6 = r4;
        r13 = DoubleUtils.nextDown(r2);
        r15 = 0;
        r11 = (long) Math.floor(r13);
        goto L13
    L9:
        throw new AssertionError("impossible");
    L42:
        if (r8 > 0) goto L45;
        return r2;
    L45:
        return Math.nextUp(r2);
    L47:
        if (r25 < 0) goto L52;
        if (r8 > 0) goto L51;
        return r2;
    L51:
        return Math.nextUp(r2);
    L52:
        if (r8 < 0) goto L55;
        return r2;
    L55:
        return DoubleUtils.nextDown(r2);
    L57:
        if (r8 < 0) goto L60;
        return r2;
    L60:
        return DoubleUtils.nextDown(r2);
    L62:
        if (r25 < 0) goto L67;
        if (r8 < 0) goto L66;
        return r2;
    L66:
        return DoubleUtils.nextDown(r2);
    L67:
        if (r8 > 0) goto L70;
        return r2;
    L70:
        return Math.nextUp(r2);
    L72:
        if (r8 != 0) goto L74;
        boolean r1 = true;
    L75:
        MathPreconditions.checkRoundingUnnecessary(r1);
        return r2;
    L74:
        r1 = false;
        goto L75
    L5:
        r8 = Long.compare(r25, r4);
        goto L7
    }

    public static long saturatedAdd(long r9, long r11) {
        long r0 = r9 + r11;
        boolean r3 = true;
        if ((r9 ^ r11) >= 0) goto L5;
        boolean r2 = true;
    L7:
        if ((r9 ^ r0) >= 0) goto L11;
        r3 = false;
    L11:
        if ((r2 | r3) == false) goto L14;
        return r0;
    L14:
        return ((r0 >>> 63) ^ 1) + Long.MAX_VALUE;
    L5:
        r2 = false;
        goto L7
    }

    public static long saturatedMultiply(long r11, long r13) {
        int r0 = ((Long.numberOfLeadingZeros(r11) + Long.numberOfLeadingZeros(~r11)) + Long.numberOfLeadingZeros(r13)) + Long.numberOfLeadingZeros(~r13);
        if (r0 > 65) goto L5;
        long r1 = ((r11 ^ r13) >>> 63) + Long.MAX_VALUE;
        boolean r4 = true;
        if (r0 >= 64) goto L9;
        boolean r3 = true;
    L11:
        if (r11 >= 0) goto L13;
        boolean r8 = true;
    L15:
        if (r13 == Long.MIN_VALUE) goto L19;
        r4 = false;
    L19:
        if ((r3 | (r4 & r8)) == false) goto L21;
        return r1;
    L21:
        long r5 = r11 * r13;
        if (r11 != 0) goto L24;
    L27:
        return r5;
    L24:
        if ((r5 / r11) == r13) goto L27;
        return r1;
    L13:
        r8 = false;
        goto L15
    L9:
        r3 = false;
        goto L11
    L5:
        return r11 * r13;
    }

    public static long saturatedPow(long r12, int r14) {
        MathPreconditions.checkNonNegative("exponent", r14);
        if (r12 < (-2)) goto L5;
        boolean r0 = true;
    L7:
        if (r12 > 2) goto L9;
        boolean r3 = true;
    L11:
        if ((r0 & r3) == true) goto L13;
        long r6 = 1;
        long r8 = ((r12 >>> 63) & ((long) (r14 & 1))) + Long.MAX_VALUE;
    L39:
        switch(r14) {
            case 0: goto L45;
            case 1: goto L44;
            default: goto L41;
        };
    L41:
        if ((r14 & 1) == 0) goto L46;
        r6 = saturatedMultiply(r6, r12);
    L46:
        r14 = r14 >> 1;
        if (r14 <= 0) goto L39;
        if ((-3037000499L) <= r12) goto L51;
        boolean r1 = true;
    L53:
        if (r12 <= FLOOR_SQRT_MAX_LONG) goto L55;
        boolean r4 = true;
    L57:
        if ((r1 | r4) == true) goto L58;
        r12 = r12 * r12;
        goto L39
    L58:
        return r8;
    L55:
        r4 = false;
        goto L57
    L51:
        r1 = false;
        goto L53
    L44:
        return saturatedMultiply(r6, r12);
    L45:
        return r6;
    L13:
        switch(((int) r12)) {
            case -2: goto L30;
            case -1: goto L26;
            case 0: goto L21;
            case 1: goto L20;
            case 2: goto L16;
            default: goto L15;
        };
    L16:
        if (r14 < 63) goto L19;
        return Long.MAX_VALUE;
    L19:
        return 1 << r14;
    L20:
        return 1;
    L21:
        if (r14 == 0) goto L67;
        return 0;
    L67:
        return 1;
    L15:
        throw new AssertionError();
    L26:
        if ((r14 & 1) == 0) goto L28;
        return -1;
    L28:
        return 1;
    L30:
        if (r14 < 64) goto L34;
        return ((long) (r14 & 1)) + Long.MAX_VALUE;
    L34:
        if ((r14 & 1) != 0) goto L37;
        return 1 << r14;
    L37:
        return (-1) << r14;
    L9:
        r3 = false;
        goto L11
    L5:
        r0 = false;
        goto L7
    }

    public static long saturatedSubtract(long r9, long r11) {
        long r0 = r9 - r11;
        boolean r3 = true;
        if ((r9 ^ r11) < 0) goto L5;
        boolean r2 = true;
    L7:
        if ((r9 ^ r0) >= 0) goto L11;
        r3 = false;
    L11:
        if ((r2 | r3) == false) goto L14;
        return r0;
    L14:
        return ((r0 >>> 63) ^ 1) + Long.MAX_VALUE;
    L5:
        r2 = false;
        goto L7
    }

    public static long sqrt(long r10, RoundingMode r12) {
        MathPreconditions.checkNonNegative("x", r10);
        if (fitsInInt(r10) == true) goto L5;
        long r0 = (long) Math.sqrt(r10);
        long r2 = r0 * r0;
        boolean r7 = true;
        int r8 = 1;
        switch(C08621.$SwitchMap$java$math$RoundingMode[r12.ordinal()]) {
            case 1: goto L27;
            case 2: goto L22;
            case 3: goto L22;
            case 4: goto L17;
            case 5: goto L17;
            case 6: goto L11;
            case 7: goto L11;
            case 8: goto L11;
            default: goto L9;
        };
    L9:
        throw new AssertionError();
    L11:
        if (r10 < r2) goto L14;
        r8 = 0;
    L14:
        long r4 = r0 - ((long) r8);
        return ((long) lessThanBranchFree((r4 * r4) + r4, r10)) + r4;
    L17:
        if (r10 > r2) goto L19;
        return r0;
    L19:
        return 1 + r0;
    L22:
        if (r10 < r2) goto L24;
        return r0;
    L24:
        return r0 - 1;
    L27:
        if (r2 == r10) goto L30;
        r7 = false;
    L30:
        MathPreconditions.checkRoundingUnnecessary(r7);
        return r0;
    L5:
        return IntMath.sqrt((int) r10, r12);
    }
}
