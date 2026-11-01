package kotlin.time;

import com.arthenica.ffmpegkit.MediaInformation;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: longSaturatedMath.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000 \n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0010\u000b\n\u0000\u001a'\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0001H\u0002¢\u0006\u0004\b\u0006\u0010\u0007\u001a\u0015\u0010\b\u001a\u00020\u00042\u0006\u0010\u0002\u001a\u00020\u0001H\u0002¢\u0006\u0002\u0010\t\u001a'\u0010\n\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0003\u001a\u00020\u0004H\u0000¢\u0006\u0004\b\r\u0010\u000e\u001a'\u0010\u000f\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0003\u001a\u00020\u0004H\u0002¢\u0006\u0004\b\u0010\u0010\u000e\u001a%\u0010\u0011\u001a\u00020\u00042\u0006\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\fH\u0000¢\u0006\u0002\u0010\u0014\u001a%\u0010\u0015\u001a\u00020\u00042\u0006\u0010\u0016\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\fH\u0002¢\u0006\u0002\u0010\u0014\u001a%\u0010\u0018\u001a\u00020\u00042\u0006\u0010\u0019\u001a\u00020\u00012\u0006\u0010\u001a\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\fH\u0000¢\u0006\u0002\u0010\u0014\u001a\r\u0010\u001b\u001a\u00020\u001c*\u00020\u0001H\u0080\b¨\u0006\u001d"}, m421d2 = {"checkInfiniteSumDefined", "", "value", MediaInformation.KEY_DURATION, "Lkotlin/time/Duration;", "durationInUnit", "checkInfiniteSumDefined-PjuGub4", "(JJJ)J", "infinityOfSign", "(J)J", "saturatingAdd", "unit", "Lkotlin/time/DurationUnit;", "saturatingAdd-NuflL3o", "(JLkotlin/time/DurationUnit;J)J", "saturatingAddInHalves", "saturatingAddInHalves-NuflL3o", "saturatingDiff", "valueNs", "origin", "(JJLkotlin/time/DurationUnit;)J", "saturatingFiniteDiff", "value1", "value2", "saturatingOriginsDiff", "origin1", "origin2", "isSaturated", "", "kotlin-stdlib"}, m422k = 2, m423mv = {1, 9, 0}, m425xi = 48)
public final class LongSaturatedMathKt {
    /* JADX INFO: renamed from: checkInfiniteSumDefined-PjuGub4, reason: not valid java name */
    private static final long m2273checkInfiniteSumDefinedPjuGub4(long r4, long r6, long r8) {
        if (Duration.m2177isInfiniteimpl(r6) == true) goto L5;
    L9:
        return r4;
    L5:
        if ((r4 ^ r8) >= 0) goto L9;
        throw new IllegalArgumentException("Summing infinities of different signs");
    }

    private static final long infinityOfSign(long r2) {
        if (r2 >= 0) goto L6;
        return Duration.Companion.m2247getNEG_INFINITEUwyO8pc$kotlin_stdlib();
    L6:
        return Duration.Companion.m2246getINFINITEUwyO8pc();
    }

    public static final boolean isSaturated(long r5) {
        if ((1 | (r5 - 1)) != Long.MAX_VALUE) goto L5;
        return true;
    L5:
        return false;
    }

    /* JADX INFO: renamed from: saturatingAdd-NuflL3o, reason: not valid java name */
    public static final long m2274saturatingAddNuflL3o(long r15, DurationUnit r17, long r18) {
        Intrinsics.checkNotNullParameter(r17, "unit");
        long r6 = Duration.m2191toLongimpl(r18, r17);
        boolean r11 = true;
        if (((r15 - 1) | 1) != Long.MAX_VALUE) goto L5;
        boolean r1 = true;
    L6:
        if (r1 == false) goto L10;
        return m2273checkInfiniteSumDefinedPjuGub4(r15, r18, r6);
    L10:
        if (((r6 - 1) | 1) == Long.MAX_VALUE) goto L13;
        r11 = false;
    L13:
        if (r11 == true) goto L15;
        long r2 = r15 + r6;
        if (((r15 ^ r2) & (r6 ^ r2)) < 0) goto L19;
        return r2;
    L19:
        if (r15 >= 0) goto L23;
        return Long.MIN_VALUE;
    L23:
        return Long.MAX_VALUE;
    L15:
        return m2275saturatingAddInHalvesNuflL3o(r15, r17, r18);
    L5:
        r1 = false;
        goto L6
    }

    /* JADX INFO: renamed from: saturatingAddInHalves-NuflL3o, reason: not valid java name */
    private static final long m2275saturatingAddInHalvesNuflL3o(long r11, DurationUnit r13, long r14) {
        long r0 = Duration.m2148divUwyO8pc(r14, 2);
        long r2 = Duration.m2191toLongimpl(r0, r13);
        if ((1 | (r2 - 1)) != Long.MAX_VALUE) goto L5;
        boolean r7 = true;
    L6:
        if (r7 == false) goto L9;
        return r2;
    L9:
        return m2274saturatingAddNuflL3o(m2274saturatingAddNuflL3o(r11, r13, r0), r13, Duration.m2180minusLRDsOJo(r14, r0));
    L5:
        r7 = false;
        goto L6
    }

    public static final long saturatingDiff(long r7, long r9, DurationUnit r11) {
        Intrinsics.checkNotNullParameter(r11, "unit");
        if ((1 | (r9 - 1)) != Long.MAX_VALUE) goto L5;
        boolean r3 = true;
    L6:
        if (r3 == false) goto L10;
        return Duration.m2198unaryMinusUwyO8pc(infinityOfSign(r9));
    L10:
        return saturatingFiniteDiff(r7, r9, r11);
    L5:
        r3 = false;
        goto L6
    }

    private static final long saturatingFiniteDiff(long r13, long r15, DurationUnit r17) {
        long r1 = r13 - r15;
        if (((r1 ^ r13) & (~(r1 ^ r15))) >= 0) goto L11;
        if (r17.compareTo(DurationUnit.MILLISECONDS) >= 0) goto L9;
        long r3 = DurationUnitKt.convertDurationUnit(1, DurationUnit.MILLISECONDS, r17);
        long r5 = (r13 / r3) - (r15 / r3);
        long r7 = (r13 % r3) - (r15 % r3);
        Duration.Companion r9 = Duration.Companion;
        return Duration.m2181plusLRDsOJo(DurationKt.toDuration(r5, DurationUnit.MILLISECONDS), DurationKt.toDuration(r7, r17));
    L9:
        return Duration.m2198unaryMinusUwyO8pc(infinityOfSign(r1));
    L11:
        return DurationKt.toDuration(r1, r17);
    }

    public static final long saturatingOriginsDiff(long r13, long r15, DurationUnit r17) {
        Intrinsics.checkNotNullParameter(r17, "unit");
        boolean r7 = true;
        if (((r15 - 1) | 1) != Long.MAX_VALUE) goto L5;
        boolean r0 = true;
    L6:
        if (r0 == false) goto L14;
        if (r13 != r15) goto L12;
        return Duration.Companion.m2248getZEROUwyO8pc();
    L12:
        return Duration.m2198unaryMinusUwyO8pc(infinityOfSign(r15));
    L14:
        if ((1 | (r13 - 1)) == Long.MAX_VALUE) goto L17;
        r7 = false;
    L17:
        if (r7 == false) goto L21;
        return infinityOfSign(r13);
    L21:
        return saturatingFiniteDiff(r13, r15, r17);
    L5:
        r0 = false;
        goto L6
    }
}
