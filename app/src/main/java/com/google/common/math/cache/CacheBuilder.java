package com.google.common.cache;

import com.google.common.base.Ascii;
import com.google.common.base.Equivalence;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.base.Ticker;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class CacheBuilder<K, V> {
    static final Supplier<AbstractCache.StatsCounter> CACHE_STATS_COUNTER = null;
    private static final int DEFAULT_CONCURRENCY_LEVEL = 4;
    private static final int DEFAULT_EXPIRATION_NANOS = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int DEFAULT_REFRESH_NANOS = 0;
    static final CacheStats EMPTY_STATS = null;
    static final Supplier<? extends AbstractCache.StatsCounter> NULL_STATS_COUNTER = null;
    static final Ticker NULL_TICKER = null;
    static final int UNSET_INT = -1;
    int concurrencyLevel;
    long expireAfterAccessNanos;
    long expireAfterWriteNanos;
    int initialCapacity;

    @CheckForNull
    Equivalence<Object> keyEquivalence;

    @CheckForNull
    LocalCache.Strength keyStrength;
    long maximumSize;
    long maximumWeight;
    long refreshNanos;

    @CheckForNull
    RemovalListener<? super K, ? super V> removalListener;
    Supplier<? extends AbstractCache.StatsCounter> statsCounterSupplier;
    boolean strictParsing;

    @CheckForNull
    Ticker ticker;

    @CheckForNull
    Equivalence<Object> valueEquivalence;

    @CheckForNull
    LocalCache.Strength valueStrength;

    @CheckForNull
    Weigher<? super K, ? super V> weigher;

    private static final class LoggerHolder {
        static final Logger logger = null;

        static {
            logger = Logger.getLogger(CacheBuilder.class.getName());
        }

        private LoggerHolder() {
        }
    }

    static final enum NullListener extends Enum<NullListener> implements RemovalListener<Object, Object> {
        private static final /* synthetic */ NullListener[] $VALUES = null;
        public static final NullListener INSTANCE = null;

        private static /* synthetic */ NullListener[] $values() {
            return new NullListener[]{INSTANCE};
        }

        static {
            INSTANCE = new NullListener("INSTANCE", 0);
            $VALUES = $values();
        }

        private NullListener(String r1, int r2) {
            super(r1, r2);
        }

        public static NullListener valueOf(String r1) {
            return (NullListener) Enum.valueOf(NullListener.class, r1);
        }

        public static NullListener[] values() {
            return (NullListener[]) $VALUES.clone();
        }

        @Override // com.google.common.cache.RemovalListener
        public void onRemoval(RemovalNotification<Object, Object> r1) {
        }
    }

    static final enum OneWeigher extends Enum<OneWeigher> implements Weigher<Object, Object> {
        private static final /* synthetic */ OneWeigher[] $VALUES = null;
        public static final OneWeigher INSTANCE = null;

        private static /* synthetic */ OneWeigher[] $values() {
            return new OneWeigher[]{INSTANCE};
        }

        static {
            INSTANCE = new OneWeigher("INSTANCE", 0);
            $VALUES = $values();
        }

        private OneWeigher(String r1, int r2) {
            super(r1, r2);
        }

        public static OneWeigher valueOf(String r1) {
            return (OneWeigher) Enum.valueOf(OneWeigher.class, r1);
        }

        public static OneWeigher[] values() {
            return (OneWeigher[]) $VALUES.clone();
        }

        @Override // com.google.common.cache.Weigher
        public int weigh(Object r2, Object r3) {
            return 1;
        }
    }

    static {
        NULL_STATS_COUNTER = Suppliers.ofInstance(new C05591());
        EMPTY_STATS = new CacheStats(0, 0, 0, 0, 0, 0);
        CACHE_STATS_COUNTER = new C05602();
        NULL_TICKER = new C05613();
    }

    private CacheBuilder() {
        this.strictParsing = true;
        this.initialCapacity = -1;
        this.concurrencyLevel = -1;
        this.maximumSize = -1;
        this.maximumWeight = -1;
        this.expireAfterWriteNanos = -1;
        this.expireAfterAccessNanos = -1;
        this.refreshNanos = -1;
        this.statsCounterSupplier = NULL_STATS_COUNTER;
    }

    private void checkNonLoadingCache() {
        if (this.refreshNanos != (-1)) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkState(r0, "refreshAfterWrite requires a LoadingCache");
        return;
    L5:
        r0 = false;
        goto L6
    }

    private void checkWeightWithWeigher() {
        boolean r1 = true;
        if (this.weigher != null) goto L10;
        if (this.maximumWeight == (-1)) goto L8;
        r1 = false;
    L8:
        Preconditions.checkState(r1, "maximumWeight requires weigher");
        return;
    L10:
        if (this.strictParsing == false) goto L17;
        if (this.maximumWeight != (-1)) goto L15;
        r1 = false;
    L15:
        Preconditions.checkState(r1, "weigher requires maximumWeight");
        return;
    L17:
        if (this.maximumWeight != (-1)) goto L22;
        LoggerHolder.logger.log(Level.WARNING, "ignoring weigher specified without maximumWeight");
        return;
    }

    public static CacheBuilder<Object, Object> from(CacheBuilderSpec r1) {
        return r1.toCacheBuilder().lenientParsing();
    }

    public static CacheBuilder<Object, Object> from(String r1) {
        return from(CacheBuilderSpec.parse(r1));
    }

    public static CacheBuilder<Object, Object> newBuilder() {
        return new CacheBuilder();
    }

    private static long toNanosSaturated(Duration r3) {
        return r3.toNanos();
    L6:
        if (r3.isNegative() == false) goto L8;
        return Long.MIN_VALUE;
    L8:
        return Long.MAX_VALUE;
    }

    public <K1 extends K, V1 extends V> Cache<K1, V1> build() {
        checkWeightWithWeigher();
        checkNonLoadingCache();
        return new LocalCache.LocalManualCache(this);
    }

    public <K1 extends K, V1 extends V> LoadingCache<K1, V1> build(CacheLoader<? super K1, V1> r2) {
        checkWeightWithWeigher();
        return new LocalCache.LocalLoadingCache(this, r2);
    }

    public CacheBuilder<K, V> concurrencyLevel(int r6) {
        boolean r2 = true;
        if (this.concurrencyLevel != (-1)) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkState(r0, "concurrency level was already set to %s", this.concurrencyLevel);
        if (r6 > 0) goto L10;
        r2 = false;
    L10:
        Preconditions.checkArgument(r2);
        this.concurrencyLevel = r6;
        return this;
    L5:
        r0 = false;
        goto L6
    }

    public CacheBuilder<K, V> expireAfterAccess(long r7, TimeUnit r9) {
        boolean r1 = true;
        if (this.expireAfterAccessNanos != (-1)) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkState(r0, "expireAfterAccess was already set to %s ns", this.expireAfterAccessNanos);
        if (r7 >= 0) goto L10;
        r1 = false;
    L10:
        Preconditions.checkArgument(r1, "duration cannot be negative: %s %s", r7, r9);
        this.expireAfterAccessNanos = r9.toNanos(r7);
        return this;
    L5:
        r0 = false;
        goto L6
    }

    public CacheBuilder<K, V> expireAfterAccess(Duration r4) {
        return expireAfterAccess(toNanosSaturated(r4), TimeUnit.NANOSECONDS);
    }

    public CacheBuilder<K, V> expireAfterWrite(long r7, TimeUnit r9) {
        boolean r1 = true;
        if (this.expireAfterWriteNanos != (-1)) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkState(r0, "expireAfterWrite was already set to %s ns", this.expireAfterWriteNanos);
        if (r7 >= 0) goto L10;
        r1 = false;
    L10:
        Preconditions.checkArgument(r1, "duration cannot be negative: %s %s", r7, r9);
        this.expireAfterWriteNanos = r9.toNanos(r7);
        return this;
    L5:
        r0 = false;
        goto L6
    }

    public CacheBuilder<K, V> expireAfterWrite(Duration r4) {
        return expireAfterWrite(toNanosSaturated(r4), TimeUnit.NANOSECONDS);
    }

    int getConcurrencyLevel() {
        if (this.concurrencyLevel != (-1)) goto L6;
        return 4;
    L6:
        return this.concurrencyLevel;
    }

    long getExpireAfterAccessNanos() {
        if (this.expireAfterAccessNanos != (-1)) goto L6;
        return 0;
    L6:
        return this.expireAfterAccessNanos;
    }

    long getExpireAfterWriteNanos() {
        if (this.expireAfterWriteNanos != (-1)) goto L6;
        return 0;
    L6:
        return this.expireAfterWriteNanos;
    }

    int getInitialCapacity() {
        if (this.initialCapacity != (-1)) goto L6;
        return 16;
    L6:
        return this.initialCapacity;
    }

    Equivalence<Object> getKeyEquivalence() {
        return (Equivalence) MoreObjects.firstNonNull(this.keyEquivalence, getKeyStrength().defaultEquivalence());
    }

    LocalCache.Strength getKeyStrength() {
        return (LocalCache.Strength) MoreObjects.firstNonNull(this.keyStrength, LocalCache.Strength.STRONG);
    }

    long getMaximumWeight() {
        if (this.expireAfterWriteNanos != 0) goto L5;
    L12:
        return 0;
    L5:
        if (this.expireAfterAccessNanos == 0) goto L12;
        if (this.weigher != null) goto L11;
        return this.maximumSize;
    L11:
        return this.maximumWeight;
    }

    long getRefreshNanos() {
        if (this.refreshNanos != (-1)) goto L6;
        return 0;
    L6:
        return this.refreshNanos;
    }

    <K1 extends K, V1 extends V> RemovalListener<K1, V1> getRemovalListener() {
        return (RemovalListener) MoreObjects.firstNonNull(this.removalListener, NullListener.INSTANCE);
    }

    Supplier<? extends AbstractCache.StatsCounter> getStatsCounterSupplier() {
        return this.statsCounterSupplier;
    }

    Ticker getTicker(boolean r2) {
        if (this.ticker != null) goto L5;
        if (r2 == false) goto L9;
        return Ticker.systemTicker();
    L9:
        return NULL_TICKER;
    L5:
        return this.ticker;
    }

    Equivalence<Object> getValueEquivalence() {
        return (Equivalence) MoreObjects.firstNonNull(this.valueEquivalence, getValueStrength().defaultEquivalence());
    }

    LocalCache.Strength getValueStrength() {
        return (LocalCache.Strength) MoreObjects.firstNonNull(this.valueStrength, LocalCache.Strength.STRONG);
    }

    <K1 extends K, V1 extends V> Weigher<K1, V1> getWeigher() {
        return (Weigher) MoreObjects.firstNonNull(this.weigher, OneWeigher.INSTANCE);
    }

    public CacheBuilder<K, V> initialCapacity(int r6) {
        boolean r2 = true;
        if (this.initialCapacity != (-1)) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkState(r0, "initial capacity was already set to %s", this.initialCapacity);
        if (r6 >= 0) goto L10;
        r2 = false;
    L10:
        Preconditions.checkArgument(r2);
        this.initialCapacity = r6;
        return this;
    L5:
        r0 = false;
        goto L6
    }

    boolean isRecordingStats() {
        if (this.statsCounterSupplier != CACHE_STATS_COUNTER) goto L5;
        return true;
    L5:
        return false;
    }

    CacheBuilder<K, V> keyEquivalence(Equivalence<Object> r4) {
        if (this.keyEquivalence != null) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkState(r0, "key equivalence was already set to %s", this.keyEquivalence);
        this.keyEquivalence = (Equivalence) Preconditions.checkNotNull(r4);
        return this;
    L5:
        r0 = false;
        goto L6
    }

    CacheBuilder<K, V> lenientParsing() {
        this.strictParsing = false;
        return this;
    }

    public CacheBuilder<K, V> maximumSize(long r9) {
        boolean r1 = true;
        if (this.maximumSize != (-1)) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkState(r0, "maximum size was already set to %s", this.maximumSize);
        if (this.maximumWeight != (-1)) goto L9;
        boolean r2 = true;
    L10:
        Preconditions.checkState(r2, "maximum weight was already set to %s", this.maximumWeight);
        if (this.weigher != null) goto L13;
        boolean r3 = true;
    L14:
        Preconditions.checkState(r3, "maximum size can not be combined with weigher");
        if (r9 >= 0) goto L18;
        r1 = false;
    L18:
        Preconditions.checkArgument(r1, "maximum size must not be negative");
        this.maximumSize = r9;
        return this;
    L13:
        r3 = false;
        goto L14
    L9:
        r2 = false;
        goto L10
    L5:
        r0 = false;
        goto L6
    }

    public CacheBuilder<K, V> maximumWeight(long r9) {
        boolean r1 = true;
        if (this.maximumWeight != (-1)) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkState(r0, "maximum weight was already set to %s", this.maximumWeight);
        if (this.maximumSize != (-1)) goto L9;
        boolean r2 = true;
    L10:
        Preconditions.checkState(r2, "maximum size was already set to %s", this.maximumSize);
        if (r9 >= 0) goto L14;
        r1 = false;
    L14:
        Preconditions.checkArgument(r1, "maximum weight must not be negative");
        this.maximumWeight = r9;
        return this;
    L9:
        r2 = false;
        goto L10
    L5:
        r0 = false;
        goto L6
    }

    public CacheBuilder<K, V> recordStats() {
        this.statsCounterSupplier = CACHE_STATS_COUNTER;
        return this;
    }

    public CacheBuilder<K, V> refreshAfterWrite(long r7, TimeUnit r9) {
        Preconditions.checkNotNull(r9);
        boolean r1 = true;
        if (this.refreshNanos != (-1)) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkState(r0, "refresh was already set to %s ns", this.refreshNanos);
        if (r7 > 0) goto L10;
        r1 = false;
    L10:
        Preconditions.checkArgument(r1, "duration must be positive: %s %s", r7, r9);
        this.refreshNanos = r9.toNanos(r7);
        return this;
    L5:
        r0 = false;
        goto L6
    }

    public CacheBuilder<K, V> refreshAfterWrite(Duration r4) {
        return refreshAfterWrite(toNanosSaturated(r4), TimeUnit.NANOSECONDS);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <K1 extends K, V1 extends V> CacheBuilder<K1, V1> removalListener(RemovalListener<? super K1, ? super V1> r3) {
        if (this.removalListener != null) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkState(r0);
        this.removalListener = (RemovalListener) Preconditions.checkNotNull(r3);
        return this;
    L5:
        r0 = false;
        goto L6
    }

    CacheBuilder<K, V> setKeyStrength(LocalCache.Strength r4) {
        if (this.keyStrength != null) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkState(r0, "Key strength was already set to %s", this.keyStrength);
        this.keyStrength = (LocalCache.Strength) Preconditions.checkNotNull(r4);
        return this;
    L5:
        r0 = false;
        goto L6
    }

    CacheBuilder<K, V> setValueStrength(LocalCache.Strength r4) {
        if (this.valueStrength != null) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkState(r0, "Value strength was already set to %s", this.valueStrength);
        this.valueStrength = (LocalCache.Strength) Preconditions.checkNotNull(r4);
        return this;
    L5:
        r0 = false;
        goto L6
    }

    public CacheBuilder<K, V> softValues() {
        return setValueStrength(LocalCache.Strength.SOFT);
    }

    public CacheBuilder<K, V> ticker(Ticker r2) {
        if (this.ticker != null) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkState(r0);
        this.ticker = (Ticker) Preconditions.checkNotNull(r2);
        return this;
    L5:
        r0 = false;
        goto L6
    }

    public String toString() {
        MoreObjects.ToStringHelper r0 = MoreObjects.toStringHelper(this);
        if (this.initialCapacity == (-1)) goto L6;
        r0.add("initialCapacity", this.initialCapacity);
    L6:
        if (this.concurrencyLevel == (-1)) goto L9;
        r0.add("concurrencyLevel", this.concurrencyLevel);
    L9:
        if (this.maximumSize == (-1)) goto L12;
        r0.add("maximumSize", this.maximumSize);
    L12:
        if (this.maximumWeight == (-1)) goto L15;
        r0.add("maximumWeight", this.maximumWeight);
    L15:
        if (this.expireAfterWriteNanos == (-1)) goto L18;
        r0.add("expireAfterWrite", this.expireAfterWriteNanos + "ns");
    L18:
        if (this.expireAfterAccessNanos == (-1)) goto L21;
        r0.add("expireAfterAccess", this.expireAfterAccessNanos + "ns");
    L21:
        if (this.keyStrength == null) goto L24;
        r0.add("keyStrength", Ascii.toLowerCase(this.keyStrength.toString()));
    L24:
        if (this.valueStrength == null) goto L27;
        r0.add("valueStrength", Ascii.toLowerCase(this.valueStrength.toString()));
    L27:
        if (this.keyEquivalence == null) goto L30;
        r0.addValue("keyEquivalence");
    L30:
        if (this.valueEquivalence == null) goto L33;
        r0.addValue("valueEquivalence");
    L33:
        if (this.removalListener == null) goto L36;
        r0.addValue("removalListener");
    L36:
        return r0.toString();
    }

    CacheBuilder<K, V> valueEquivalence(Equivalence<Object> r4) {
        if (this.valueEquivalence != null) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkState(r0, "value equivalence was already set to %s", this.valueEquivalence);
        this.valueEquivalence = (Equivalence) Preconditions.checkNotNull(r4);
        return this;
    L5:
        r0 = false;
        goto L6
    }

    public CacheBuilder<K, V> weakKeys() {
        return setKeyStrength(LocalCache.Strength.WEAK);
    }

    public CacheBuilder<K, V> weakValues() {
        return setValueStrength(LocalCache.Strength.WEAK);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <K1 extends K, V1 extends V> CacheBuilder<K1, V1> weigher(Weigher<? super K1, ? super V1> r8) {
        boolean r1 = true;
        if (this.weigher != null) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkState(r0);
        if (this.strictParsing == true) goto L9;
    L13:
        this.weigher = (Weigher) Preconditions.checkNotNull(r8);
        return this;
    L9:
        if (this.maximumSize == (-1)) goto L12;
        r1 = false;
    L12:
        Preconditions.checkState(r1, "weigher can not be combined with maximum size (%s provided)", this.maximumSize);
        goto L13
    L5:
        r0 = false;
        goto L6
    }
}
