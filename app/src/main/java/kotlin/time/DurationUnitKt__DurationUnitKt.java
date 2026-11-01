package kotlin.time;

import androidx.media3.exoplayer.upstream.CmcdData;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: DurationUnit.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\u001c\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\f\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\u001a\u0018\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0001\u001a\u0010\u0010\u0006\u001a\u00020\u00012\u0006\u0010\u0007\u001a\u00020\bH\u0001\u001a\f\u0010\u0007\u001a\u00020\b*\u00020\u0001H\u0001¨\u0006\t"}, m421d2 = {"durationUnitByIsoChar", "Lkotlin/time/DurationUnit;", "isoChar", "", "isTimeComponent", "", "durationUnitByShortName", "shortName", "", "kotlin-stdlib"}, m422k = 5, m423mv = {1, 9, 0}, m425xi = 49, m426xs = "kotlin/time/DurationUnitKt")
class DurationUnitKt__DurationUnitKt extends DurationUnitKt__DurationUnitJvmKt {

    /* JADX INFO: compiled from: DurationUnit.kt */
    @Metadata(m422k = 3, m423mv = {1, 9, 0}, m425xi = 48)
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0 = null;

        static {
            int[] r0 = new int[DurationUnit.values().length];
            r0[DurationUnit.NANOSECONDS.ordinal()] = 1;     // Catch: NoSuchFieldError -> L5
        L26:
            r0[DurationUnit.MICROSECONDS.ordinal()] = 2;     // Catch: NoSuchFieldError -> L8
        L32:
            r0[DurationUnit.MILLISECONDS.ordinal()] = 3;     // Catch: NoSuchFieldError -> L11
        L36:
            r0[DurationUnit.SECONDS.ordinal()] = 4;     // Catch: NoSuchFieldError -> L14
        L28:
            r0[DurationUnit.MINUTES.ordinal()] = 5;     // Catch: NoSuchFieldError -> L17
        L30:
            r0[DurationUnit.HOURS.ordinal()] = 6;     // Catch: NoSuchFieldError -> L20
        L38:
            r0[DurationUnit.DAYS.ordinal()] = 7;     // Catch: NoSuchFieldError -> L23
        L24:
            $EnumSwitchMapping$0 = r0;
        }
    }

    public DurationUnitKt__DurationUnitKt() {
    }

    public static final DurationUnit durationUnitByIsoChar(char r3, boolean r4) {
        if (r4 == true) goto L9;
        if (r3 != 'D') goto L7;
        return DurationUnit.DAYS;
    L7:
        throw new IllegalArgumentException("Invalid or unsupported duration ISO non-time unit: " + r3);
    L9:
        if (r3 != 'H') goto L12;
        return DurationUnit.HOURS;
    L12:
        if (r3 != 'M') goto L15;
        return DurationUnit.MINUTES;
    L15:
        if (r3 != 'S') goto L19;
        return DurationUnit.SECONDS;
    L19:
        throw new IllegalArgumentException("Invalid duration ISO time unit: " + r3);
    }

    public static final DurationUnit durationUnitByShortName(String r3) {
        Intrinsics.checkNotNullParameter(r3, "shortName");
        switch(r3.hashCode()) {
            case 100: goto L24;
            case 104: goto L21;
            case 109: goto L18;
            case 115: goto L15;
            case 3494: goto L12;
            case 3525: goto L9;
            case 3742: goto L6;
            default: goto L28;
        };
    L6:
        if (r3.equals("us") == false) goto L28;
        return DurationUnit.MICROSECONDS;
    L9:
        if (r3.equals("ns") == false) goto L28;
        return DurationUnit.NANOSECONDS;
    L12:
        if (r3.equals("ms") == false) goto L28;
        return DurationUnit.MILLISECONDS;
    L15:
        if (r3.equals(CmcdData.Factory.STREAMING_FORMAT_SS) == false) goto L28;
        return DurationUnit.SECONDS;
    L18:
        if (r3.equals("m") == false) goto L28;
        return DurationUnit.MINUTES;
    L21:
        if (r3.equals(CmcdData.Factory.STREAMING_FORMAT_HLS) == false) goto L28;
        return DurationUnit.HOURS;
    L24:
        if (r3.equals("d") == false) goto L28;
        return DurationUnit.DAYS;
    L28:
        throw new IllegalArgumentException("Unknown duration unit short name: " + r3);
    }

    public static final String shortName(DurationUnit r3) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        switch(WhenMappings.$EnumSwitchMapping$0[r3.ordinal()]) {
            case 1: goto L12;
            case 2: goto L11;
            case 3: goto L10;
            case 4: goto L9;
            case 5: goto L8;
            case 6: goto L7;
            case 7: goto L6;
            default: goto L5;
        };
    L6:
        return "d";
    L7:
        return CmcdData.Factory.STREAMING_FORMAT_HLS;
    L8:
        return "m";
    L9:
        return CmcdData.Factory.STREAMING_FORMAT_SS;
    L10:
        return "ms";
    L11:
        return "us";
    L12:
        return "ns";
    L5:
        throw new IllegalStateException(("Unknown unit: " + r3).toString());
    }
}
