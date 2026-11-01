package kotlin.time;

import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: DurationUnitJvm.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000 \n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a \u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0001\u001a \u0010\u0000\u001a\u00020\u00062\u0006\u0010\u0002\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0001\u001a \u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0002\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0001\u001a\f\u0010\b\u001a\u00020\u0004*\u00020\tH\u0007\u001a\f\u0010\n\u001a\u00020\t*\u00020\u0004H\u0007¨\u0006\u000b"}, m421d2 = {"convertDurationUnit", "", "value", "sourceUnit", "Lkotlin/time/DurationUnit;", "targetUnit", "", "convertDurationUnitOverflow", "toDurationUnit", "Ljava/util/concurrent/TimeUnit;", "toTimeUnit", "kotlin-stdlib"}, m422k = 5, m423mv = {1, 9, 0}, m425xi = 49, m426xs = "kotlin/time/DurationUnitKt")
class DurationUnitKt__DurationUnitJvmKt {

    /* JADX INFO: compiled from: DurationUnitJvm.kt */
    @Metadata(m422k = 3, m423mv = {1, 9, 0}, m425xi = 48)
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0 = null;

        static {
            int[] r0 = new int[TimeUnit.values().length];
            r0[TimeUnit.NANOSECONDS.ordinal()] = 1;     // Catch: NoSuchFieldError -> L5
        L26:
            r0[TimeUnit.MICROSECONDS.ordinal()] = 2;     // Catch: NoSuchFieldError -> L8
        L32:
            r0[TimeUnit.MILLISECONDS.ordinal()] = 3;     // Catch: NoSuchFieldError -> L11
        L36:
            r0[TimeUnit.SECONDS.ordinal()] = 4;     // Catch: NoSuchFieldError -> L14
        L28:
            r0[TimeUnit.MINUTES.ordinal()] = 5;     // Catch: NoSuchFieldError -> L17
        L30:
            r0[TimeUnit.HOURS.ordinal()] = 6;     // Catch: NoSuchFieldError -> L20
        L38:
            r0[TimeUnit.DAYS.ordinal()] = 7;     // Catch: NoSuchFieldError -> L23
        L24:
            $EnumSwitchMapping$0 = r0;
        }
    }

    public DurationUnitKt__DurationUnitJvmKt() {
    }

    public static final double convertDurationUnit(double r6, DurationUnit r8, DurationUnit r9) {
        Intrinsics.checkNotNullParameter(r8, "sourceUnit");
        Intrinsics.checkNotNullParameter(r9, "targetUnit");
        long r0 = r9.getTimeUnit$kotlin_stdlib().convert(1, r8.getTimeUnit$kotlin_stdlib());
        if (r0 <= 0) goto L7;
        return r0 * r6;
    L7:
        return r6 / r8.getTimeUnit$kotlin_stdlib().convert(1, r9.getTimeUnit$kotlin_stdlib());
    }

    public static final long convertDurationUnit(long r2, DurationUnit r4, DurationUnit r5) {
        Intrinsics.checkNotNullParameter(r4, "sourceUnit");
        Intrinsics.checkNotNullParameter(r5, "targetUnit");
        return r5.getTimeUnit$kotlin_stdlib().convert(r2, r4.getTimeUnit$kotlin_stdlib());
    }

    public static final long convertDurationUnitOverflow(long r2, DurationUnit r4, DurationUnit r5) {
        Intrinsics.checkNotNullParameter(r4, "sourceUnit");
        Intrinsics.checkNotNullParameter(r5, "targetUnit");
        return r5.getTimeUnit$kotlin_stdlib().convert(r2, r4.getTimeUnit$kotlin_stdlib());
    }

    public static final DurationUnit toDurationUnit(TimeUnit r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        switch(WhenMappings.$EnumSwitchMapping$0[r2.ordinal()]) {
            case 1: goto L13;
            case 2: goto L19;
            case 3: goto L18;
            case 4: goto L17;
            case 5: goto L16;
            case 6: goto L15;
            case 7: goto L14;
            default: goto L5;
        };
    L5:
        throw new NoWhenBranchMatchedException();
    L14:
        return DurationUnit.DAYS;
    L15:
        return DurationUnit.HOURS;
    L16:
        return DurationUnit.MINUTES;
    L17:
        return DurationUnit.SECONDS;
    L18:
        return DurationUnit.MILLISECONDS;
    L19:
        return DurationUnit.MICROSECONDS;
    L13:
        return DurationUnit.NANOSECONDS;
    }

    public static final TimeUnit toTimeUnit(DurationUnit r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return r1.getTimeUnit$kotlin_stdlib();
    }
}
