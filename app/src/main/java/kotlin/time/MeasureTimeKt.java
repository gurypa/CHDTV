package kotlin.time;

import androidx.exifinterface.media.ExifInterface;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: measureTime.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000(\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a,\u0010\u0000\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u0087\bø\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001¢\u0006\u0002\u0010\u0005\u001a3\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\b0\u0007\"\u0004\b\u0000\u0010\b2\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u0002H\b0\u0003H\u0087\bø\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001\u001a0\u0010\u0000\u001a\u00020\u0001*\u00020\t2\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u0087\bø\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001¢\u0006\u0002\u0010\n\u001a0\u0010\u0000\u001a\u00020\u0001*\u00020\u000b2\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u0087\bø\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001¢\u0006\u0002\u0010\f\u001a7\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\b0\u0007\"\u0004\b\u0000\u0010\b*\u00020\t2\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u0002H\b0\u0003H\u0087\bø\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001\u001a7\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\b0\u0007\"\u0004\b\u0000\u0010\b*\u00020\u000b2\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u0002H\b0\u0003H\u0087\bø\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\r"}, m421d2 = {"measureTime", "Lkotlin/time/Duration;", "block", "Lkotlin/Function0;", "", "(Lkotlin/jvm/functions/Function0;)J", "measureTimedValue", "Lkotlin/time/TimedValue;", ExifInterface.GPS_DIRECTION_TRUE, "Lkotlin/time/TimeSource;", "(Lkotlin/time/TimeSource;Lkotlin/jvm/functions/Function0;)J", "Lkotlin/time/TimeSource$Monotonic;", "(Lkotlin/time/TimeSource$Monotonic;Lkotlin/jvm/functions/Function0;)J", "kotlin-stdlib"}, m422k = 2, m423mv = {1, 9, 0}, m425xi = 48)
public final class MeasureTimeKt {
    public static final long measureTime(Function0<Unit> r5) {
        Intrinsics.checkNotNullParameter(r5, "block");
        long r3 = TimeSource.Monotonic.INSTANCE.m2284markNowz9LOYto();
        r5.invoke();
        return TimeSource.Monotonic.ValueTimeMark.m2289elapsedNowUwyO8pc(r3);
    }

    public static final long measureTime(TimeSource.Monotonic r5, Function0<Unit> r6) {
        Intrinsics.checkNotNullParameter(r5, "<this>");
        Intrinsics.checkNotNullParameter(r6, "block");
        long r1 = r5.m2284markNowz9LOYto();
        r6.invoke();
        return TimeSource.Monotonic.ValueTimeMark.m2289elapsedNowUwyO8pc(r1);
    }

    public static final long measureTime(TimeSource r4, Function0<Unit> r5) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        Intrinsics.checkNotNullParameter(r5, "block");
        TimeMark r1 = r4.markNow();
        r5.invoke();
        return r1.mo2135elapsedNowUwyO8pc();
    }

    public static final <T> TimedValue<T> measureTimedValue(Function0<? extends T> r10) {
        Intrinsics.checkNotNullParameter(r10, "block");
        long r3 = TimeSource.Monotonic.INSTANCE.m2284markNowz9LOYto();
        return new TimedValue(r10.invoke(), TimeSource.Monotonic.ValueTimeMark.m2289elapsedNowUwyO8pc(r3), null);
    }

    public static final <T> TimedValue<T> measureTimedValue(TimeSource.Monotonic r8, Function0<? extends T> r9) {
        Intrinsics.checkNotNullParameter(r8, "<this>");
        Intrinsics.checkNotNullParameter(r9, "block");
        long r1 = r8.m2284markNowz9LOYto();
        return new TimedValue(r9.invoke(), TimeSource.Monotonic.ValueTimeMark.m2289elapsedNowUwyO8pc(r1), null);
    }

    public static final <T> TimedValue<T> measureTimedValue(TimeSource r7, Function0<? extends T> r8) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        Intrinsics.checkNotNullParameter(r8, "block");
        TimeMark r1 = r7.markNow();
        return new TimedValue(r8.invoke(), r1.mo2135elapsedNowUwyO8pc(), null);
    }
}
