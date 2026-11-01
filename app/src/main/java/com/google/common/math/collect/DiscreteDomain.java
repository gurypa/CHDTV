package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import java.io.Serializable;
import java.lang.Comparable;
import java.math.BigInteger;
import java.util.NoSuchElementException;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class DiscreteDomain<C extends Comparable> {
    final boolean supportsFastOffset;

    /* JADX INFO: renamed from: com.google.common.collect.DiscreteDomain$1 */
    static /* synthetic */ class C06161 {
    }

    private static final class BigIntegerDomain extends DiscreteDomain<BigInteger> implements Serializable {
        private static final BigIntegerDomain INSTANCE = null;
        private static final BigInteger MAX_LONG = null;
        private static final BigInteger MIN_LONG = null;
        private static final long serialVersionUID = 0;

        static {
            INSTANCE = new BigIntegerDomain();
            MIN_LONG = BigInteger.valueOf(Long.MIN_VALUE);
            MAX_LONG = BigInteger.valueOf(Long.MAX_VALUE);
        }

        BigIntegerDomain() {
            super(true, null);
        }

        static /* synthetic */ BigIntegerDomain access$300() {
            return INSTANCE;
        }

        private Object readResolve() {
            return INSTANCE;
        }

        @Override // com.google.common.collect.DiscreteDomain
        public /* bridge */ /* synthetic */ long distance(Comparable r1, Comparable r2) {
            return distance((BigInteger) r1, (BigInteger) r2);
        }

        public long distance(BigInteger r3, BigInteger r4) {
            return r4.subtract(r3).max(MIN_LONG).min(MAX_LONG).longValue();
        }

        @Override // com.google.common.collect.DiscreteDomain
        public /* bridge */ /* synthetic */ Comparable next(Comparable r1) {
            return next((BigInteger) r1);
        }

        public BigInteger next(BigInteger r2) {
            return r2.add(BigInteger.ONE);
        }

        @Override // com.google.common.collect.DiscreteDomain
        /* bridge */ /* synthetic */ Comparable offset(Comparable r1, long r2) {
            return offset((BigInteger) r1, r2);
        }

        BigInteger offset(BigInteger r2, long r3) {
            CollectPreconditions.checkNonnegative(r3, "distance");
            return r2.add(BigInteger.valueOf(r3));
        }

        @Override // com.google.common.collect.DiscreteDomain
        public /* bridge */ /* synthetic */ Comparable previous(Comparable r1) {
            return previous((BigInteger) r1);
        }

        public BigInteger previous(BigInteger r2) {
            return r2.subtract(BigInteger.ONE);
        }

        public String toString() {
            return "DiscreteDomain.bigIntegers()";
        }
    }

    private static final class IntegerDomain extends DiscreteDomain<Integer> implements Serializable {
        private static final IntegerDomain INSTANCE = null;
        private static final long serialVersionUID = 0;

        static {
            INSTANCE = new IntegerDomain();
        }

        IntegerDomain() {
            super(true, null);
        }

        static /* synthetic */ IntegerDomain access$000() {
            return INSTANCE;
        }

        private Object readResolve() {
            return INSTANCE;
        }

        @Override // com.google.common.collect.DiscreteDomain
        public /* bridge */ /* synthetic */ long distance(Comparable r1, Comparable r2) {
            return distance((Integer) r1, (Integer) r2);
        }

        public long distance(Integer r5, Integer r6) {
            return ((long) r6.intValue()) - ((long) r5.intValue());
        }

        @Override // com.google.common.collect.DiscreteDomain
        public /* bridge */ /* synthetic */ Comparable maxValue() {
            return maxValue();
        }

        @Override // com.google.common.collect.DiscreteDomain
        public Integer maxValue() {
            return Integer.MAX_VALUE;
        }

        @Override // com.google.common.collect.DiscreteDomain
        public /* bridge */ /* synthetic */ Comparable minValue() {
            return minValue();
        }

        @Override // com.google.common.collect.DiscreteDomain
        public Integer minValue() {
            return Integer.MIN_VALUE;
        }

        @Override // com.google.common.collect.DiscreteDomain
        @CheckForNull
        public /* bridge */ /* synthetic */ Comparable next(Comparable r1) {
            return next((Integer) r1);
        }

        @CheckForNull
        public Integer next(Integer r3) {
            int r0 = r3.intValue();
            if (r0 != Integer.MAX_VALUE) goto L6;
            return null;
        L6:
            return Integer.valueOf(r0 + 1);
        }

        @Override // com.google.common.collect.DiscreteDomain
        /* bridge */ /* synthetic */ Comparable offset(Comparable r1, long r2) {
            return offset((Integer) r1, r2);
        }

        Integer offset(Integer r3, long r4) {
            CollectPreconditions.checkNonnegative(r4, "distance");
            return Integer.valueOf(Ints.checkedCast(r3.longValue() + r4));
        }

        @Override // com.google.common.collect.DiscreteDomain
        @CheckForNull
        public /* bridge */ /* synthetic */ Comparable previous(Comparable r1) {
            return previous((Integer) r1);
        }

        @CheckForNull
        public Integer previous(Integer r3) {
            int r0 = r3.intValue();
            if (r0 != Integer.MIN_VALUE) goto L6;
            return null;
        L6:
            return Integer.valueOf(r0 - 1);
        }

        public String toString() {
            return "DiscreteDomain.integers()";
        }
    }

    private static final class LongDomain extends DiscreteDomain<Long> implements Serializable {
        private static final LongDomain INSTANCE = null;
        private static final long serialVersionUID = 0;

        static {
            INSTANCE = new LongDomain();
        }

        LongDomain() {
            super(true, null);
        }

        static /* synthetic */ LongDomain access$200() {
            return INSTANCE;
        }

        private Object readResolve() {
            return INSTANCE;
        }

        @Override // com.google.common.collect.DiscreteDomain
        public /* bridge */ /* synthetic */ long distance(Comparable r1, Comparable r2) {
            return distance((Long) r1, (Long) r2);
        }

        public long distance(Long r10, Long r11) {
            long r0 = r11.longValue() - r10.longValue();
            if (r11.longValue() <= r10.longValue()) goto L9;
            if (r0 >= 0) goto L9;
            return Long.MAX_VALUE;
        L9:
            if (r11.longValue() < r10.longValue()) goto L11;
        L14:
            return r0;
        L11:
            if (r0 <= 0) goto L14;
            return Long.MIN_VALUE;
        }

        @Override // com.google.common.collect.DiscreteDomain
        public /* bridge */ /* synthetic */ Comparable maxValue() {
            return maxValue();
        }

        @Override // com.google.common.collect.DiscreteDomain
        public Long maxValue() {
            return Long.MAX_VALUE;
        }

        @Override // com.google.common.collect.DiscreteDomain
        public /* bridge */ /* synthetic */ Comparable minValue() {
            return minValue();
        }

        @Override // com.google.common.collect.DiscreteDomain
        public Long minValue() {
            return Long.MIN_VALUE;
        }

        @Override // com.google.common.collect.DiscreteDomain
        @CheckForNull
        public /* bridge */ /* synthetic */ Comparable next(Comparable r1) {
            return next((Long) r1);
        }

        @CheckForNull
        public Long next(Long r5) {
            long r0 = r5.longValue();
            if (r0 != Long.MAX_VALUE) goto L6;
            return null;
        L6:
            return Long.valueOf(1 + r0);
        }

        @Override // com.google.common.collect.DiscreteDomain
        /* bridge */ /* synthetic */ Comparable offset(Comparable r1, long r2) {
            return offset((Long) r1, r2);
        }

        Long offset(Long r7, long r8) {
            CollectPreconditions.checkNonnegative(r8, "distance");
            long r0 = r7.longValue() + r8;
            if (r0 >= 0) goto L10;
            if (r7.longValue() >= 0) goto L7;
            boolean r2 = true;
        L8:
            Preconditions.checkArgument(r2, "overflow");
            goto L10
        L7:
            r2 = false;
        L10:
            return Long.valueOf(r0);
        }

        @Override // com.google.common.collect.DiscreteDomain
        @CheckForNull
        public /* bridge */ /* synthetic */ Comparable previous(Comparable r1) {
            return previous((Long) r1);
        }

        @CheckForNull
        public Long previous(Long r5) {
            long r0 = r5.longValue();
            if (r0 != Long.MIN_VALUE) goto L6;
            return null;
        L6:
            return Long.valueOf(r0 - 1);
        }

        public String toString() {
            return "DiscreteDomain.longs()";
        }
    }

    protected DiscreteDomain() {
        this(false);
    }

    private DiscreteDomain(boolean r1) {
        this.supportsFastOffset = r1;
    }

    /* synthetic */ DiscreteDomain(boolean r1, C06161 r2) {
        this(r1);
    }

    public static DiscreteDomain<BigInteger> bigIntegers() {
        return BigIntegerDomain.access$300();
    }

    public static DiscreteDomain<Integer> integers() {
        return IntegerDomain.access$000();
    }

    public static DiscreteDomain<Long> longs() {
        return LongDomain.access$200();
    }

    public abstract long distance(C r1, C r2);

    public C maxValue() {
        throw new NoSuchElementException();
    }

    public C minValue() {
        throw new NoSuchElementException();
    }

    @CheckForNull
    public abstract C next(C r1);

    C offset(C r7, long r8) {
        C r0 = r7;
        CollectPreconditions.checkNonnegative(r8, "distance");
        long r1 = 0;
    L4:
        if (r1 >= r8) goto L10;
        r0 = (C) next(r0);
        if (r0 == null) goto L9;
        r1 = r1 + 1;
        goto L4
    L9:
        throw new IllegalArgumentException("overflowed computing offset(" + r7 + ", " + r8 + ")");
    L10:
        return r0;
    }

    @CheckForNull
    public abstract C previous(C r1);
}
