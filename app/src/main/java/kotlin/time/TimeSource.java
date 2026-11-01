package kotlin.time;

import com.arthenica.ffmpegkit.MediaInformation;
import kotlin.Metadata;
import kotlin.ULong$$ExternalSyntheticBackport0;
import kotlin.jvm.JvmInline;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: TimeSource.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\bg\u0018\u0000 \u00042\u00020\u0001:\u0003\u0004\u0005\u0006J\b\u0010\u0002\u001a\u00020\u0003H&¨\u0006\u0007"}, m421d2 = {"Lkotlin/time/TimeSource;", "", "markNow", "Lkotlin/time/TimeMark;", "Companion", "Monotonic", "WithComparableMarks", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public interface TimeSource {
    public static final Companion Companion = null;

    /* JADX INFO: compiled from: TimeSource.kt */
    @Metadata(m420d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, m421d2 = {"Lkotlin/time/TimeSource$Companion;", "", "()V", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
    public static final class Companion {
        static final /* synthetic */ Companion $$INSTANCE = null;

        static {
            $$INSTANCE = new Companion();
        }

        private Companion() {
        }
    }

    /* JADX INFO: compiled from: TimeSource.kt */
    @Metadata(m420d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001:\u0001\tB\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0012\u0010\u0003\u001a\u00020\u0004H\u0016ø\u0001\u0000¢\u0006\u0004\b\u0005\u0010\u0006J\b\u0010\u0007\u001a\u00020\bH\u0016\u0082\u0002\u0004\n\u0002\b!¨\u0006\n"}, m421d2 = {"Lkotlin/time/TimeSource$Monotonic;", "Lkotlin/time/TimeSource$WithComparableMarks;", "()V", "markNow", "Lkotlin/time/TimeSource$Monotonic$ValueTimeMark;", "markNow-z9LOYto", "()J", "toString", "", "ValueTimeMark", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
    public static final class Monotonic implements WithComparableMarks {
        public static final Monotonic INSTANCE = null;

        /* JADX INFO: compiled from: TimeSource.kt */
        @Metadata(m420d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\u0010\u0000\n\u0002\b\u0014\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0087@\u0018\u00002\u00020\u0001B\u0015\b\u0000\u0012\n\u0010\u0002\u001a\u00060\u0003j\u0002`\u0004¢\u0006\u0004\b\u0005\u0010\u0006J\u0018\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u0000H\u0086\u0002¢\u0006\u0004\b\n\u0010\u000bJ\u0012\u0010\f\u001a\u00020\rH\u0016ø\u0001\u0000¢\u0006\u0004\b\u000e\u0010\u0006J\u001a\u0010\u000f\u001a\u00020\u00102\b\u0010\t\u001a\u0004\u0018\u00010\u0011HÖ\u0003¢\u0006\u0004\b\u0012\u0010\u0013J\u000f\u0010\u0014\u001a\u00020\u0010H\u0016¢\u0006\u0004\b\u0015\u0010\u0016J\u000f\u0010\u0017\u001a\u00020\u0010H\u0016¢\u0006\u0004\b\u0018\u0010\u0016J\u0010\u0010\u0019\u001a\u00020\bHÖ\u0001¢\u0006\u0004\b\u001a\u0010\u001bJ\u001b\u0010\u001c\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0001H\u0096\u0002ø\u0001\u0000¢\u0006\u0004\b\u001d\u0010\u001eJ\u0018\u0010\u001c\u001a\u00020\u00002\u0006\u0010\u001f\u001a\u00020\rH\u0096\u0002¢\u0006\u0004\b \u0010!J\u0018\u0010\u001c\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0000H\u0086\u0002¢\u0006\u0004\b\"\u0010!J\u0018\u0010#\u001a\u00020\u00002\u0006\u0010\u001f\u001a\u00020\rH\u0096\u0002¢\u0006\u0004\b$\u0010!J\u0010\u0010%\u001a\u00020&HÖ\u0001¢\u0006\u0004\b'\u0010(R\u0012\u0010\u0002\u001a\u00060\u0003j\u0002`\u0004X\u0080\u0004¢\u0006\u0002\n\u0000\u0088\u0001\u0002\u0092\u0001\u00060\u0003j\u0002`\u0004\u0082\u0002\u0004\n\u0002\b!¨\u0006)"}, m421d2 = {"Lkotlin/time/TimeSource$Monotonic$ValueTimeMark;", "Lkotlin/time/ComparableTimeMark;", "reading", "", "Lkotlin/time/ValueTimeMarkReading;", "constructor-impl", "(J)J", "compareTo", "", "other", "compareTo-6eNON_k", "(JJ)I", "elapsedNow", "Lkotlin/time/Duration;", "elapsedNow-UwyO8pc", "equals", "", "", "equals-impl", "(JLjava/lang/Object;)Z", "hasNotPassedNow", "hasNotPassedNow-impl", "(J)Z", "hasPassedNow", "hasPassedNow-impl", "hashCode", "hashCode-impl", "(J)I", "minus", "minus-UwyO8pc", "(JLkotlin/time/ComparableTimeMark;)J", MediaInformation.KEY_DURATION, "minus-LRDsOJo", "(JJ)J", "minus-6eNON_k", "plus", "plus-LRDsOJo", "toString", "", "toString-impl", "(J)Ljava/lang/String;", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
        @JvmInline
        public static final class ValueTimeMark implements ComparableTimeMark {
            private final long reading;

            private /* synthetic */ ValueTimeMark(long r1) {
                this.reading = r1;
            }

            /* JADX INFO: renamed from: box-impl, reason: not valid java name */
            public static final /* synthetic */ ValueTimeMark m2285boximpl(long r1) {
                return new ValueTimeMark(r1);
            }

            /* JADX INFO: renamed from: compareTo-6eNON_k, reason: not valid java name */
            public static final int m2286compareTo6eNON_k(long r4, long r6) {
                return Duration.m2144compareToLRDsOJo(m2295minus6eNON_k(r4, r6), Duration.Companion.m2248getZEROUwyO8pc());
            }

            /* JADX INFO: renamed from: compareTo-impl, reason: not valid java name */
            public static int m2287compareToimpl(long r1, ComparableTimeMark r3) {
                Intrinsics.checkNotNullParameter(r3, "other");
                return m2285boximpl(r1).compareTo2(r3);
            }

            /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
            public static long m2288constructorimpl(long r0) {
                return r0;
            }

            /* JADX INFO: renamed from: elapsedNow-UwyO8pc, reason: not valid java name */
            public static long m2289elapsedNowUwyO8pc(long r2) {
                return MonotonicTimeSource.INSTANCE.m2278elapsedFrom6eNON_k(r2);
            }

            /* JADX INFO: renamed from: equals-impl, reason: not valid java name */
            public static boolean m2290equalsimpl(long r4, Object r6) {
                if ((r6 instanceof ValueTimeMark) == true) goto L6;
                return false;
            L6:
                if (r4 == ((ValueTimeMark) r6).m2302unboximpl()) goto L8;
                return false;
            L8:
                return true;
            }

            /* JADX INFO: renamed from: equals-impl0, reason: not valid java name */
            public static final boolean m2291equalsimpl0(long r1, long r3) {
                if (r1 != r3) goto L5;
                return true;
            L5:
                return false;
            }

            /* JADX INFO: renamed from: hasNotPassedNow-impl, reason: not valid java name */
            public static boolean m2292hasNotPassedNowimpl(long r2) {
                return Duration.m2178isNegativeimpl(m2289elapsedNowUwyO8pc(r2));
            }

            /* JADX INFO: renamed from: hasPassedNow-impl, reason: not valid java name */
            public static boolean m2293hasPassedNowimpl(long r2) {
                return !Duration.m2178isNegativeimpl(m2289elapsedNowUwyO8pc(r2));
            }

            /* JADX INFO: renamed from: hashCode-impl, reason: not valid java name */
            public static int m2294hashCodeimpl(long r1) {
                return ULong$$ExternalSyntheticBackport0.m434m(r1);
            }

            /* JADX INFO: renamed from: minus-6eNON_k, reason: not valid java name */
            public static final long m2295minus6eNON_k(long r2, long r4) {
                return MonotonicTimeSource.INSTANCE.m2277differenceBetweenfRLX17w(r2, r4);
            }

            /* JADX INFO: renamed from: minus-LRDsOJo, reason: not valid java name */
            public static long m2296minusLRDsOJo(long r3, long r5) {
                return MonotonicTimeSource.INSTANCE.m2276adjustReading6QKq23U(r3, Duration.m2198unaryMinusUwyO8pc(r5));
            }

            /* JADX INFO: renamed from: minus-UwyO8pc, reason: not valid java name */
            public static long m2297minusUwyO8pc(long r3, ComparableTimeMark r5) {
                Intrinsics.checkNotNullParameter(r5, "other");
                if ((r5 instanceof ValueTimeMark) == false) goto L7;
                return m2295minus6eNON_k(r3, ((ValueTimeMark) r5).m2302unboximpl());
            L7:
                throw new IllegalArgumentException("Subtracting or comparing time marks from different time sources is not possible: " + m2299toStringimpl(r3) + " and " + r5);
            }

            /* JADX INFO: renamed from: plus-LRDsOJo, reason: not valid java name */
            public static long m2298plusLRDsOJo(long r2, long r4) {
                return MonotonicTimeSource.INSTANCE.m2276adjustReading6QKq23U(r2, r4);
            }

            /* JADX INFO: renamed from: toString-impl, reason: not valid java name */
            public static String m2299toStringimpl(long r2) {
                return "ValueTimeMark(reading=" + r2 + ')';
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
                return m2289elapsedNowUwyO8pc(this.reading);
            }

            @Override // kotlin.time.ComparableTimeMark
            public boolean equals(Object r3) {
                return m2290equalsimpl(this.reading, r3);
            }

            @Override // kotlin.time.TimeMark
            public boolean hasNotPassedNow() {
                return m2292hasNotPassedNowimpl(this.reading);
            }

            @Override // kotlin.time.TimeMark
            public boolean hasPassedNow() {
                return m2293hasPassedNowimpl(this.reading);
            }

            @Override // kotlin.time.ComparableTimeMark
            public int hashCode() {
                return m2294hashCodeimpl(this.reading);
            }

            /* JADX INFO: renamed from: minus-LRDsOJo, reason: not valid java name */
            public long m2300minusLRDsOJo(long r3) {
                return m2296minusLRDsOJo(this.reading, r3);
            }

            @Override // kotlin.time.ComparableTimeMark, kotlin.time.TimeMark
            /* JADX INFO: renamed from: minus-LRDsOJo */
            public /* bridge */ /* synthetic */ ComparableTimeMark mo2136minusLRDsOJo(long r3) {
                return m2285boximpl(m2300minusLRDsOJo(r3));
            }

            @Override // kotlin.time.TimeMark
            /* JADX INFO: renamed from: minus-LRDsOJo */
            public /* bridge */ /* synthetic */ TimeMark mo2136minusLRDsOJo(long r3) {
                return m2285boximpl(m2300minusLRDsOJo(r3));
            }

            @Override // kotlin.time.ComparableTimeMark
            /* JADX INFO: renamed from: minus-UwyO8pc */
            public long mo2137minusUwyO8pc(ComparableTimeMark r3) {
                Intrinsics.checkNotNullParameter(r3, "other");
                return m2297minusUwyO8pc(this.reading, r3);
            }

            /* JADX INFO: renamed from: plus-LRDsOJo, reason: not valid java name */
            public long m2301plusLRDsOJo(long r3) {
                return m2298plusLRDsOJo(this.reading, r3);
            }

            @Override // kotlin.time.ComparableTimeMark, kotlin.time.TimeMark
            /* JADX INFO: renamed from: plus-LRDsOJo */
            public /* bridge */ /* synthetic */ ComparableTimeMark mo2138plusLRDsOJo(long r3) {
                return m2285boximpl(m2301plusLRDsOJo(r3));
            }

            @Override // kotlin.time.TimeMark
            /* JADX INFO: renamed from: plus-LRDsOJo */
            public /* bridge */ /* synthetic */ TimeMark mo2138plusLRDsOJo(long r3) {
                return m2285boximpl(m2301plusLRDsOJo(r3));
            }

            public String toString() {
                return m2299toStringimpl(this.reading);
            }

            /* JADX INFO: renamed from: unbox-impl, reason: not valid java name */
            public final /* synthetic */ long m2302unboximpl() {
                return this.reading;
            }
        }

        static {
            INSTANCE = new Monotonic();
        }

        private Monotonic() {
        }

        @Override // kotlin.time.TimeSource.WithComparableMarks, kotlin.time.TimeSource
        public /* bridge */ /* synthetic */ ComparableTimeMark markNow() {
            return ValueTimeMark.m2285boximpl(m2284markNowz9LOYto());
        }

        @Override // kotlin.time.TimeSource
        public /* bridge */ /* synthetic */ TimeMark markNow() {
            return ValueTimeMark.m2285boximpl(m2284markNowz9LOYto());
        }

        /* JADX INFO: renamed from: markNow-z9LOYto, reason: not valid java name */
        public long m2284markNowz9LOYto() {
            return MonotonicTimeSource.INSTANCE.m2279markNowz9LOYto();
        }

        public String toString() {
            return MonotonicTimeSource.INSTANCE.toString();
        }
    }

    /* JADX INFO: compiled from: TimeSource.kt */
    @Metadata(m420d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bg\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&¨\u0006\u0004"}, m421d2 = {"Lkotlin/time/TimeSource$WithComparableMarks;", "Lkotlin/time/TimeSource;", "markNow", "Lkotlin/time/ComparableTimeMark;", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
    public interface WithComparableMarks extends TimeSource {
        @Override // kotlin.time.TimeSource
        ComparableTimeMark markNow();
    }

    static {
        Companion = Companion.$$INSTANCE;
    }

    TimeMark markNow();
}
