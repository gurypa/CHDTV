package kotlin.time;

import com.arthenica.ffmpegkit.MediaInformation;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.ULong$$ExternalSyntheticBackport0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.math.MathKt;

/* JADX INFO: compiled from: TimeSources.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\b'\u0018\u00002\u00020\u0001:\u0001\u0011B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\b\u0010\r\u001a\u00020\bH\u0002J\b\u0010\u000e\u001a\u00020\u000fH\u0016J\b\u0010\u0010\u001a\u00020\bH$R\u0014\u0010\u0002\u001a\u00020\u0003X\u0084\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u001b\u0010\u0007\u001a\u00020\b8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u000b\u0010\f\u001a\u0004\b\t\u0010\n¨\u0006\u0012"}, m421d2 = {"Lkotlin/time/AbstractLongTimeSource;", "Lkotlin/time/TimeSource$WithComparableMarks;", "unit", "Lkotlin/time/DurationUnit;", "(Lkotlin/time/DurationUnit;)V", "getUnit", "()Lkotlin/time/DurationUnit;", "zero", "", "getZero", "()J", "zero$delegate", "Lkotlin/Lazy;", "adjustedRead", "markNow", "Lkotlin/time/ComparableTimeMark;", "read", "LongTimeMark", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public abstract class AbstractLongTimeSource implements TimeSource.WithComparableMarks {
    private final DurationUnit unit;
    private final Lazy zero$delegate;

    /* JADX INFO: compiled from: TimeSources.kt */
    @Metadata(m420d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010\u000e\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u0012\u0010\n\u001a\u00020\u0007H\u0016ø\u0001\u0000¢\u0006\u0004\b\u000b\u0010\fJ\u0013\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0096\u0002J\b\u0010\u0011\u001a\u00020\u0012H\u0016J\u001b\u0010\u0013\u001a\u00020\u00072\u0006\u0010\u000f\u001a\u00020\u0001H\u0096\u0002ø\u0001\u0000¢\u0006\u0004\b\u0014\u0010\u0015J\u0018\u0010\u0016\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0007H\u0096\u0002¢\u0006\u0004\b\u0018\u0010\u0019J\b\u0010\u001a\u001a\u00020\u001bH\u0016R\u0013\u0010\u0006\u001a\u00020\u0007X\u0082\u0004ø\u0001\u0000¢\u0006\u0004\n\u0002\u0010\tR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b!¨\u0006\u001c"}, m421d2 = {"Lkotlin/time/AbstractLongTimeSource$LongTimeMark;", "Lkotlin/time/ComparableTimeMark;", "startedAt", "", "timeSource", "Lkotlin/time/AbstractLongTimeSource;", "offset", "Lkotlin/time/Duration;", "(JLkotlin/time/AbstractLongTimeSource;JLkotlin/jvm/internal/DefaultConstructorMarker;)V", "J", "elapsedNow", "elapsedNow-UwyO8pc", "()J", "equals", "", "other", "", "hashCode", "", "minus", "minus-UwyO8pc", "(Lkotlin/time/ComparableTimeMark;)J", "plus", MediaInformation.KEY_DURATION, "plus-LRDsOJo", "(J)Lkotlin/time/ComparableTimeMark;", "toString", "", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
    private static final class LongTimeMark implements ComparableTimeMark {
        private final long offset;
        private final long startedAt;
        private final AbstractLongTimeSource timeSource;

        private LongTimeMark(long r2, AbstractLongTimeSource r4, long r5) {
            Intrinsics.checkNotNullParameter(r4, "timeSource");
            this.startedAt = r2;
            this.timeSource = r4;
            this.offset = r5;
        }

        public /* synthetic */ LongTimeMark(long r1, AbstractLongTimeSource r3, long r4, DefaultConstructorMarker r6) {
            this(r1, r3, r4);
        }

        @Override // java.lang.Comparable
        public /* bridge */ /* synthetic */ int compareTo(ComparableTimeMark r2) {
            return compareTo2(r2);
        }

        @Override // kotlin.time.ComparableTimeMark
        /* JADX INFO: renamed from: compareTo, reason: avoid collision after fix types in other method */
        public int compareTo2(ComparableTimeMark r2) {
            return ComparableTimeMark.DefaultImpls.compareTo(this, r2);
        }

        @Override // kotlin.time.TimeMark
        /* JADX INFO: renamed from: elapsedNow-UwyO8pc */
        public long mo2135elapsedNowUwyO8pc() {
            return Duration.m2180minusLRDsOJo(LongSaturatedMathKt.saturatingOriginsDiff(AbstractLongTimeSource.access$adjustedRead(this.timeSource), this.startedAt, this.timeSource.getUnit()), this.offset);
        }

        @Override // kotlin.time.ComparableTimeMark
        public boolean equals(Object r5) {
            if ((r5 instanceof LongTimeMark) == true) goto L5;
        L9:
            return false;
        L5:
            if (Intrinsics.areEqual(this.timeSource, ((LongTimeMark) r5).timeSource) == false) goto L9;
            if (Duration.m2150equalsimpl0(mo2137minusUwyO8pc((ComparableTimeMark) r5), Duration.Companion.m2248getZEROUwyO8pc()) == false) goto L9;
            return true;
        }

        @Override // kotlin.time.TimeMark
        public boolean hasNotPassedNow() {
            return ComparableTimeMark.DefaultImpls.hasNotPassedNow(this);
        }

        @Override // kotlin.time.TimeMark
        public boolean hasPassedNow() {
            return ComparableTimeMark.DefaultImpls.hasPassedNow(this);
        }

        @Override // kotlin.time.ComparableTimeMark
        public int hashCode() {
            return (Duration.m2173hashCodeimpl(this.offset) * 37) + ULong$$ExternalSyntheticBackport0.m434m(this.startedAt);
        }

        @Override // kotlin.time.ComparableTimeMark, kotlin.time.TimeMark
        /* JADX INFO: renamed from: minus-LRDsOJo */
        public ComparableTimeMark mo2136minusLRDsOJo(long r2) {
            return ComparableTimeMark.DefaultImpls.m2140minusLRDsOJo(this, r2);
        }

        @Override // kotlin.time.TimeMark
        /* JADX INFO: renamed from: minus-LRDsOJo */
        public /* bridge */ /* synthetic */ TimeMark mo2136minusLRDsOJo(long r2) {
            return mo2136minusLRDsOJo(r2);
        }

        @Override // kotlin.time.ComparableTimeMark
        /* JADX INFO: renamed from: minus-UwyO8pc */
        public long mo2137minusUwyO8pc(ComparableTimeMark r7) {
            Intrinsics.checkNotNullParameter(r7, "other");
            if ((r7 instanceof LongTimeMark) == false) goto L9;
            if (Intrinsics.areEqual(this.timeSource, ((LongTimeMark) r7).timeSource) == false) goto L9;
            return Duration.m2181plusLRDsOJo(LongSaturatedMathKt.saturatingOriginsDiff(this.startedAt, ((LongTimeMark) r7).startedAt, this.timeSource.getUnit()), Duration.m2180minusLRDsOJo(this.offset, ((LongTimeMark) r7).offset));
        L9:
            throw new IllegalArgumentException("Subtracting or comparing time marks from different time sources is not possible: " + this + " and " + r7);
        }

        @Override // kotlin.time.ComparableTimeMark, kotlin.time.TimeMark
        /* JADX INFO: renamed from: plus-LRDsOJo */
        public ComparableTimeMark mo2138plusLRDsOJo(long r24) {
            DurationUnit r3 = this.timeSource.getUnit();
            if (Duration.m2177isInfiniteimpl(r24) == true) goto L5;
            long r4 = Duration.m2197truncateToUwyO8pc$kotlin_stdlib(r24, r3);
            long r6 = Duration.m2181plusLRDsOJo(Duration.m2180minusLRDsOJo(r24, r4), this.offset);
            long r8 = LongSaturatedMathKt.m2274saturatingAddNuflL3o(this.startedAt, r3, r4);
            long r10 = Duration.m2197truncateToUwyO8pc$kotlin_stdlib(r6, r3);
            long r9 = LongSaturatedMathKt.m2274saturatingAddNuflL3o(r8, r3, r10);
            long r12 = Duration.m2180minusLRDsOJo(r6, r10);
            long r14 = Duration.m2165getInWholeNanosecondsimpl(r12);
            if (r9 != 0) goto L9;
        L13:
            long r17 = r9;
            if (((r17 - 1) | 1) != Long.MAX_VALUE) goto L16;
            boolean r19 = true;
        L17:
            if (r19 == false) goto L19;
            long r20 = Duration.Companion.m2248getZEROUwyO8pc();
        L21:
            return new LongTimeMark(r17, this.timeSource, r20, null);
        L19:
            r20 = r12;
            goto L21
        L16:
            r19 = false;
            goto L17
        L9:
            if (r14 == 0) goto L13;
            if ((r9 ^ r14) >= 0) goto L13;
            long r1 = DurationKt.toDuration(MathKt.getSign(r14), r3);
            r9 = LongSaturatedMathKt.m2274saturatingAddNuflL3o(r9, r3, r1);
            r12 = Duration.m2180minusLRDsOJo(r12, r1);
            goto L13
        L5:
            return new LongTimeMark(LongSaturatedMathKt.m2274saturatingAddNuflL3o(this.startedAt, r3, r24), this.timeSource, Duration.Companion.m2248getZEROUwyO8pc(), null);
        }

        @Override // kotlin.time.TimeMark
        /* JADX INFO: renamed from: plus-LRDsOJo */
        public /* bridge */ /* synthetic */ TimeMark mo2138plusLRDsOJo(long r2) {
            return mo2138plusLRDsOJo(r2);
        }

        public String toString() {
            return "LongTimeMark(" + this.startedAt + DurationUnitKt.shortName(this.timeSource.getUnit()) + " + " + Duration.m2194toStringimpl(this.offset) + ", " + this.timeSource + ')';
        }
    }

    public AbstractLongTimeSource(DurationUnit r2) {
        Intrinsics.checkNotNullParameter(r2, "unit");
        this.unit = r2;
        this.zero$delegate = LazyKt.lazy(new AbstractLongTimeSource$zero$2(this));
    }

    public static final /* synthetic */ long access$adjustedRead(AbstractLongTimeSource r2) {
        return r2.adjustedRead();
    }

    private final long adjustedRead() {
        return read() - getZero();
    }

    private final long getZero() {
        return ((Number) this.zero$delegate.getValue()).longValue();
    }

    protected final DurationUnit getUnit() {
        return this.unit;
    }

    @Override // kotlin.time.TimeSource.WithComparableMarks, kotlin.time.TimeSource
    public ComparableTimeMark markNow() {
        return new LongTimeMark(adjustedRead(), this, Duration.Companion.m2248getZEROUwyO8pc(), null);
    }

    @Override // kotlin.time.TimeSource
    public /* bridge */ /* synthetic */ TimeMark markNow() {
        return markNow();
    }

    protected abstract long read();
}
