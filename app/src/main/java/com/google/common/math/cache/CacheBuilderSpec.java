package com.google.common.cache;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class CacheBuilderSpec {
    private static final Splitter KEYS_SPLITTER = null;
    private static final Splitter KEY_VALUE_SPLITTER = null;
    private static final ImmutableMap<String, ValueParser> VALUE_PARSERS = null;
    long accessExpirationDuration;

    @CheckForNull
    TimeUnit accessExpirationTimeUnit;

    @CheckForNull
    Integer concurrencyLevel;

    @CheckForNull
    Integer initialCapacity;

    @CheckForNull
    LocalCache.Strength keyStrength;

    @CheckForNull
    Long maximumSize;

    @CheckForNull
    Long maximumWeight;

    @CheckForNull
    Boolean recordStats;
    long refreshDuration;

    @CheckForNull
    TimeUnit refreshTimeUnit;
    private final String specification;

    @CheckForNull
    LocalCache.Strength valueStrength;
    long writeExpirationDuration;

    @CheckForNull
    TimeUnit writeExpirationTimeUnit;

    /* JADX INFO: renamed from: com.google.common.cache.CacheBuilderSpec$1 */
    static /* synthetic */ class C05621 {
        static final /* synthetic */ int[] $SwitchMap$com$google$common$cache$LocalCache$Strength = null;

        static {
            $SwitchMap$com$google$common$cache$LocalCache$Strength = new int[LocalCache.Strength.values().length];
            $SwitchMap$com$google$common$cache$LocalCache$Strength[LocalCache.Strength.WEAK.ordinal()] = 1;     // Catch: NoSuchFieldError -> L5
        L12:
            $SwitchMap$com$google$common$cache$LocalCache$Strength[LocalCache.Strength.SOFT.ordinal()] = 2;     // Catch: NoSuchFieldError -> L8
            return;
        }
    }

    static class AccessDurationParser extends DurationParser {
        AccessDurationParser() {
        }

        @Override // com.google.common.cache.CacheBuilderSpec.DurationParser
        protected void parseDuration(CacheBuilderSpec r3, long r4, TimeUnit r6) {
            if (r3.accessExpirationTimeUnit != null) goto L5;
            boolean r0 = true;
        L6:
            Preconditions.checkArgument(r0, "expireAfterAccess already set");
            r3.accessExpirationDuration = r4;
            r3.accessExpirationTimeUnit = r6;
            return;
        L5:
            r0 = false;
            goto L6
        }
    }

    static class ConcurrencyLevelParser extends IntegerParser {
        ConcurrencyLevelParser() {
        }

        @Override // com.google.common.cache.CacheBuilderSpec.IntegerParser
        protected void parseInteger(CacheBuilderSpec r4, int r5) {
            if (r4.concurrencyLevel != null) goto L5;
            boolean r0 = true;
        L6:
            Preconditions.checkArgument(r0, "concurrency level was already set to %s", r4.concurrencyLevel);
            r4.concurrencyLevel = Integer.valueOf(r5);
            return;
        L5:
            r0 = false;
            goto L6
        }
    }

    static abstract class DurationParser implements ValueParser {
        DurationParser() {
        }

        @Override // com.google.common.cache.CacheBuilderSpec.ValueParser
        public void parse(CacheBuilderSpec r8, String r9, @CheckForNull String r10) {
            if (Strings.isNullOrEmpty(r10) == true) goto L20;
            switch(r10.charAt(r10.length() - 1)) {
                case 100: goto L11;
                case 104: goto L10;
                case 109: goto L9;
                case 115: goto L8;
                default: goto L15;
            };     // Catch: NumberFormatException -> L16
        L8:
            TimeUnit r4 = TimeUnit.SECONDS;     // Catch: NumberFormatException -> L16
        L12:
            parseDuration(r8, Long.parseLong(r10.substring(0, r10.length() - 1)), r4);     // Catch: NumberFormatException -> L16
            return;
        L9:
            r4 = TimeUnit.MINUTES;     // Catch: NumberFormatException -> L16
            goto L12
        L10:
            r4 = TimeUnit.HOURS;     // Catch: NumberFormatException -> L16
            goto L12
        L11:
            r4 = TimeUnit.DAYS;     // Catch: NumberFormatException -> L16
            goto L12
        L15:
            throw new IllegalArgumentException(CacheBuilderSpec.access$000("key %s invalid unit: was %s, must end with one of [dhms]", new Object[]{r9, r10}));     // Catch: NumberFormatException -> L16
        L18:
            throw new IllegalArgumentException(CacheBuilderSpec.access$000("key %s value set to %s, must be integer", new Object[]{r9, r10}));
        L20:
            throw new IllegalArgumentException("value of key " + r9 + " omitted");
        }

        protected abstract void parseDuration(CacheBuilderSpec r1, long r2, TimeUnit r4);
    }

    static class InitialCapacityParser extends IntegerParser {
        InitialCapacityParser() {
        }

        @Override // com.google.common.cache.CacheBuilderSpec.IntegerParser
        protected void parseInteger(CacheBuilderSpec r4, int r5) {
            if (r4.initialCapacity != null) goto L5;
            boolean r0 = true;
        L6:
            Preconditions.checkArgument(r0, "initial capacity was already set to %s", r4.initialCapacity);
            r4.initialCapacity = Integer.valueOf(r5);
            return;
        L5:
            r0 = false;
            goto L6
        }
    }

    static abstract class IntegerParser implements ValueParser {
        IntegerParser() {
        }

        @Override // com.google.common.cache.CacheBuilderSpec.ValueParser
        public void parse(CacheBuilderSpec r5, String r6, String r7) {
            if (Strings.isNullOrEmpty(r7) == true) goto L10;
            parseInteger(r5, Integer.parseInt(r7));     // Catch: NumberFormatException -> L6
            return;
        L6:
            e = move-exception;
            throw new IllegalArgumentException(CacheBuilderSpec.access$000("key %s value set to %s, must be integer", new Object[]{r6, r7}), e);
        L10:
            throw new IllegalArgumentException("value of key " + r6 + " omitted");
        }

        protected abstract void parseInteger(CacheBuilderSpec r1, int r2);
    }

    static class KeyStrengthParser implements ValueParser {
        private final LocalCache.Strength strength;

        public KeyStrengthParser(LocalCache.Strength r1) {
            this.strength = r1;
        }

        @Override // com.google.common.cache.CacheBuilderSpec.ValueParser
        public void parse(CacheBuilderSpec r5, String r6, @CheckForNull String r7) {
            boolean r0 = true;
            if (r7 != null) goto L5;
            boolean r2 = true;
        L6:
            Preconditions.checkArgument(r2, "key %s does not take values", r6);
            if (r5.keyStrength == null) goto L10;
            r0 = false;
        L10:
            Preconditions.checkArgument(r0, "%s was already set to %s", r6, r5.keyStrength);
            r5.keyStrength = this.strength;
            return;
        L5:
            r2 = false;
            goto L6
        }
    }

    static abstract class LongParser implements ValueParser {
        LongParser() {
        }

        @Override // com.google.common.cache.CacheBuilderSpec.ValueParser
        public void parse(CacheBuilderSpec r5, String r6, String r7) {
            if (Strings.isNullOrEmpty(r7) == true) goto L10;
            parseLong(r5, Long.parseLong(r7));     // Catch: NumberFormatException -> L6
            return;
        L6:
            e = move-exception;
            throw new IllegalArgumentException(CacheBuilderSpec.access$000("key %s value set to %s, must be integer", new Object[]{r6, r7}), e);
        L10:
            throw new IllegalArgumentException("value of key " + r6 + " omitted");
        }

        protected abstract void parseLong(CacheBuilderSpec r1, long r2);
    }

    static class MaximumSizeParser extends LongParser {
        MaximumSizeParser() {
        }

        @Override // com.google.common.cache.CacheBuilderSpec.LongParser
        protected void parseLong(CacheBuilderSpec r6, long r7) {
            boolean r1 = true;
            if (r6.maximumSize != null) goto L5;
            boolean r0 = true;
        L6:
            Preconditions.checkArgument(r0, "maximum size was already set to %s", r6.maximumSize);
            if (r6.maximumWeight == null) goto L10;
            r1 = false;
        L10:
            Preconditions.checkArgument(r1, "maximum weight was already set to %s", r6.maximumWeight);
            r6.maximumSize = Long.valueOf(r7);
            return;
        L5:
            r0 = false;
            goto L6
        }
    }

    static class MaximumWeightParser extends LongParser {
        MaximumWeightParser() {
        }

        @Override // com.google.common.cache.CacheBuilderSpec.LongParser
        protected void parseLong(CacheBuilderSpec r6, long r7) {
            boolean r1 = true;
            if (r6.maximumWeight != null) goto L5;
            boolean r0 = true;
        L6:
            Preconditions.checkArgument(r0, "maximum weight was already set to %s", r6.maximumWeight);
            if (r6.maximumSize == null) goto L10;
            r1 = false;
        L10:
            Preconditions.checkArgument(r1, "maximum size was already set to %s", r6.maximumSize);
            r6.maximumWeight = Long.valueOf(r7);
            return;
        L5:
            r0 = false;
            goto L6
        }
    }

    static class RecordStatsParser implements ValueParser {
        RecordStatsParser() {
        }

        @Override // com.google.common.cache.CacheBuilderSpec.ValueParser
        public void parse(CacheBuilderSpec r5, String r6, @CheckForNull String r7) {
            boolean r0 = false;
            if (r7 != null) goto L5;
            boolean r2 = true;
        L6:
            Preconditions.checkArgument(r2, "recordStats does not take values");
            if (r5.recordStats != null) goto L9;
            r0 = true;
        L9:
            Preconditions.checkArgument(r0, "recordStats already set");
            r5.recordStats = true;
            return;
        L5:
            r2 = false;
            goto L6
        }
    }

    static class RefreshDurationParser extends DurationParser {
        RefreshDurationParser() {
        }

        @Override // com.google.common.cache.CacheBuilderSpec.DurationParser
        protected void parseDuration(CacheBuilderSpec r3, long r4, TimeUnit r6) {
            if (r3.refreshTimeUnit != null) goto L5;
            boolean r0 = true;
        L6:
            Preconditions.checkArgument(r0, "refreshAfterWrite already set");
            r3.refreshDuration = r4;
            r3.refreshTimeUnit = r6;
            return;
        L5:
            r0 = false;
            goto L6
        }
    }

    private interface ValueParser {
        void parse(CacheBuilderSpec r1, String r2, @CheckForNull String r3);
    }

    static class ValueStrengthParser implements ValueParser {
        private final LocalCache.Strength strength;

        public ValueStrengthParser(LocalCache.Strength r1) {
            this.strength = r1;
        }

        @Override // com.google.common.cache.CacheBuilderSpec.ValueParser
        public void parse(CacheBuilderSpec r5, String r6, @CheckForNull String r7) {
            boolean r0 = true;
            if (r7 != null) goto L5;
            boolean r2 = true;
        L6:
            Preconditions.checkArgument(r2, "key %s does not take values", r6);
            if (r5.valueStrength == null) goto L10;
            r0 = false;
        L10:
            Preconditions.checkArgument(r0, "%s was already set to %s", r6, r5.valueStrength);
            r5.valueStrength = this.strength;
            return;
        L5:
            r2 = false;
            goto L6
        }
    }

    static class WriteDurationParser extends DurationParser {
        WriteDurationParser() {
        }

        @Override // com.google.common.cache.CacheBuilderSpec.DurationParser
        protected void parseDuration(CacheBuilderSpec r3, long r4, TimeUnit r6) {
            if (r3.writeExpirationTimeUnit != null) goto L5;
            boolean r0 = true;
        L6:
            Preconditions.checkArgument(r0, "expireAfterWrite already set");
            r3.writeExpirationDuration = r4;
            r3.writeExpirationTimeUnit = r6;
            return;
        L5:
            r0 = false;
            goto L6
        }
    }

    static {
        KEYS_SPLITTER = Splitter.m190on(',').trimResults();
        KEY_VALUE_SPLITTER = Splitter.m190on('=').trimResults();
        VALUE_PARSERS = ImmutableMap.builder().put("initialCapacity", new InitialCapacityParser()).put("maximumSize", new MaximumSizeParser()).put("maximumWeight", new MaximumWeightParser()).put("concurrencyLevel", new ConcurrencyLevelParser()).put("weakKeys", new KeyStrengthParser(LocalCache.Strength.WEAK)).put("softValues", new ValueStrengthParser(LocalCache.Strength.SOFT)).put("weakValues", new ValueStrengthParser(LocalCache.Strength.WEAK)).put("recordStats", new RecordStatsParser()).put("expireAfterAccess", new AccessDurationParser()).put("expireAfterWrite", new WriteDurationParser()).put("refreshAfterWrite", new RefreshDurationParser()).put("refreshInterval", new RefreshDurationParser()).buildOrThrow();
    }

    private CacheBuilderSpec(String r1) {
        this.specification = r1;
    }

    static /* synthetic */ String access$000(String r1, Object[] r2) {
        return format(r1, r2);
    }

    public static CacheBuilderSpec disableCaching() {
        return parse("maximumSize=0");
    }

    @CheckForNull
    private static Long durationInNanos(long r2, @CheckForNull TimeUnit r4) {
        if (r4 != null) goto L5;
        return null;
    L5:
        return Long.valueOf(r4.toNanos(r2));
    }

    private static String format(String r1, Object... r2) {
        return String.format(Locale.ROOT, r1, r2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static CacheBuilderSpec parse(String r9) {
        CacheBuilderSpec r0 = new CacheBuilderSpec(r9);
        if (r9.isEmpty() == true) goto L19;
        Iterator<String> r1 = KEYS_SPLITTER.split(r9).iterator();
    L6:
        if (r1.hasNext() == false) goto L19;
        String r2 = r1.next();
        ImmutableList r3 = ImmutableList.copyOf(KEY_VALUE_SPLITTER.split(r2));
        Preconditions.checkArgument(!r3.isEmpty(), "blank key-value pair");
        boolean r7 = false;
        if (r3.size() > 2) goto L10;
        boolean r4 = true;
    L11:
        Preconditions.checkArgument(r4, "key-value pair %s with more than one equals sign", r2);
        String r5 = (String) r3.get(0);
        ValueParser r6 = VALUE_PARSERS.get(r5);
        if (r6 == null) goto L14;
        r7 = true;
    L14:
        Preconditions.checkArgument(r7, "unknown key %s", r5);
        if (r3.size() != 1) goto L17;
        String r8 = null;
    L18:
        r6.parse(r0, r5, r8);
        goto L6
    L17:
        r8 = (String) r3.get(1);
        goto L18
    L10:
        r4 = false;
    L19:
        return r0;
    }

    public boolean equals(@CheckForNull Object r8) {
        if (this != r8) goto L6;
        return true;
    L6:
        if ((r8 instanceof CacheBuilderSpec) == true) goto L8;
        return false;
    L8:
        CacheBuilderSpec r1 = (CacheBuilderSpec) r8;
        if (Objects.equal(this.initialCapacity, r1.initialCapacity) == false) goto L30;
        if (Objects.equal(this.maximumSize, r1.maximumSize) == false) goto L30;
        if (Objects.equal(this.maximumWeight, r1.maximumWeight) == false) goto L30;
        if (Objects.equal(this.concurrencyLevel, r1.concurrencyLevel) == false) goto L30;
        if (Objects.equal(this.keyStrength, r1.keyStrength) == false) goto L30;
        if (Objects.equal(this.valueStrength, r1.valueStrength) == false) goto L30;
        if (Objects.equal(this.recordStats, r1.recordStats) == false) goto L30;
        if (Objects.equal(durationInNanos(this.writeExpirationDuration, this.writeExpirationTimeUnit), durationInNanos(r1.writeExpirationDuration, r1.writeExpirationTimeUnit)) == false) goto L30;
        if (Objects.equal(durationInNanos(this.accessExpirationDuration, this.accessExpirationTimeUnit), durationInNanos(r1.accessExpirationDuration, r1.accessExpirationTimeUnit)) == false) goto L30;
        if (Objects.equal(durationInNanos(this.refreshDuration, this.refreshTimeUnit), durationInNanos(r1.refreshDuration, r1.refreshTimeUnit)) == false) goto L30;
        return true;
    L30:
        return false;
    }

    public int hashCode() {
        return Objects.hashCode(new Object[]{this.initialCapacity, this.maximumSize, this.maximumWeight, this.concurrencyLevel, this.keyStrength, this.valueStrength, this.recordStats, durationInNanos(this.writeExpirationDuration, this.writeExpirationTimeUnit), durationInNanos(this.accessExpirationDuration, this.accessExpirationTimeUnit), durationInNanos(this.refreshDuration, this.refreshTimeUnit)});
    }

    CacheBuilder<Object, Object> toCacheBuilder() {
        CacheBuilder<Object, Object> r0 = CacheBuilder.newBuilder();
        if (this.initialCapacity == null) goto L6;
        r0.initialCapacity(this.initialCapacity.intValue());
    L6:
        if (this.maximumSize == null) goto L9;
        r0.maximumSize(this.maximumSize.longValue());
    L9:
        if (this.maximumWeight == null) goto L12;
        r0.maximumWeight(this.maximumWeight.longValue());
    L12:
        if (this.concurrencyLevel == null) goto L15;
        r0.concurrencyLevel(this.concurrencyLevel.intValue());
    L15:
        if (this.keyStrength == null) goto L22;
        switch(C05621.$SwitchMap$com$google$common$cache$LocalCache$Strength[this.keyStrength.ordinal()]) {
            case 1: goto L20;
            default: goto L19;
        };
    L20:
        r0.weakKeys();
        goto L22
    L19:
        throw new AssertionError();
    L22:
        if (this.valueStrength == null) goto L30;
        switch(C05621.$SwitchMap$com$google$common$cache$LocalCache$Strength[this.valueStrength.ordinal()]) {
            case 1: goto L28;
            case 2: goto L27;
            default: goto L26;
        };
    L27:
        r0.softValues();
        goto L30
    L28:
        r0.weakValues();
        goto L30
    L26:
        throw new AssertionError();
    L30:
        if (this.recordStats == null) goto L35;
        if (this.recordStats.booleanValue() == false) goto L35;
        r0.recordStats();
    L35:
        if (this.writeExpirationTimeUnit == null) goto L38;
        r0.expireAfterWrite(this.writeExpirationDuration, this.writeExpirationTimeUnit);
    L38:
        if (this.accessExpirationTimeUnit == null) goto L41;
        r0.expireAfterAccess(this.accessExpirationDuration, this.accessExpirationTimeUnit);
    L41:
        if (this.refreshTimeUnit == null) goto L43;
        r0.refreshAfterWrite(this.refreshDuration, this.refreshTimeUnit);
    L43:
        return r0;
    }

    public String toParsableString() {
        return this.specification;
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).addValue(toParsableString()).toString();
    }
}
