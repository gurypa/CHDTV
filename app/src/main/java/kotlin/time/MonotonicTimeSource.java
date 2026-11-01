package kotlin.time;

import com.arthenica.ffmpegkit.MediaInformation;
import kotlin.Metadata;

/* JADX INFO: compiled from: MonoTimeSource.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0010\u000e\n\u0000\bÁ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u001d\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\t¢\u0006\u0004\b\n\u0010\u000bJ\u001d\u0010\f\u001a\u00020\t2\u0006\u0010\r\u001a\u00020\u00062\u0006\u0010\u000e\u001a\u00020\u0006¢\u0006\u0004\b\u000f\u0010\u000bJ\u0015\u0010\u0010\u001a\u00020\t2\u0006\u0010\u0007\u001a\u00020\u0006¢\u0006\u0004\b\u0011\u0010\u0012J\u0012\u0010\u0013\u001a\u00020\u0006H\u0016ø\u0001\u0000¢\u0006\u0004\b\u0014\u0010\u0015J\b\u0010\u0016\u001a\u00020\u0004H\u0002J\b\u0010\u0017\u001a\u00020\u0018H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b!¨\u0006\u0019"}, m421d2 = {"Lkotlin/time/MonotonicTimeSource;", "Lkotlin/time/TimeSource$WithComparableMarks;", "()V", "zero", "", "adjustReading", "Lkotlin/time/TimeSource$Monotonic$ValueTimeMark;", "timeMark", MediaInformation.KEY_DURATION, "Lkotlin/time/Duration;", "adjustReading-6QKq23U", "(JJ)J", "differenceBetween", "one", "another", "differenceBetween-fRLX17w", "elapsedFrom", "elapsedFrom-6eNON_k", "(J)J", "markNow", "markNow-z9LOYto", "()J", "read", "toString", "", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public final class MonotonicTimeSource implements TimeSource.WithComparableMarks {
    public static final MonotonicTimeSource INSTANCE = null;
    private static final long zero = 0;

    static {
        INSTANCE = new MonotonicTimeSource();
        zero = System.nanoTime();
    }

    private MonotonicTimeSource() {
    }

    private final long read() {
        return System.nanoTime() - zero;
    }

    /* JADX INFO: renamed from: adjustReading-6QKq23U, reason: not valid java name */
    public final long m2276adjustReading6QKq23U(long r3, long r5) {
        return TimeSource.Monotonic.ValueTimeMark.m2288constructorimpl(LongSaturatedMathKt.m2274saturatingAddNuflL3o(r3, DurationUnit.NANOSECONDS, r5));
    }

    /* JADX INFO: renamed from: differenceBetween-fRLX17w, reason: not valid java name */
    public final long m2277differenceBetweenfRLX17w(long r3, long r5) {
        return LongSaturatedMathKt.saturatingOriginsDiff(r3, r5, DurationUnit.NANOSECONDS);
    }

    /* JADX INFO: renamed from: elapsedFrom-6eNON_k, reason: not valid java name */
    public final long m2278elapsedFrom6eNON_k(long r4) {
        return LongSaturatedMathKt.saturatingDiff(read(), r4, DurationUnit.NANOSECONDS);
    }

    @Override // kotlin.time.TimeSource.WithComparableMarks, kotlin.time.TimeSource
    public /* bridge */ /* synthetic */ ComparableTimeMark markNow() {
        return TimeSource.Monotonic.ValueTimeMark.m2285boximpl(m2279markNowz9LOYto());
    }

    @Override // kotlin.time.TimeSource
    public /* bridge */ /* synthetic */ TimeMark markNow() {
        return TimeSource.Monotonic.ValueTimeMark.m2285boximpl(m2279markNowz9LOYto());
    }

    /* JADX INFO: renamed from: markNow-z9LOYto, reason: not valid java name */
    public long m2279markNowz9LOYto() {
        return TimeSource.Monotonic.ValueTimeMark.m2288constructorimpl(read());
    }

    public String toString() {
        return "TimeSource(System.nanoTime())";
    }
}
