package com.google.common.collect;

import com.google.common.base.Ascii;
import com.google.common.base.Equivalence;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class MapMaker {
    private static final int DEFAULT_CONCURRENCY_LEVEL = 4;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final int UNSET_INT = -1;
    int concurrencyLevel;
    int initialCapacity;

    @CheckForNull
    Equivalence<Object> keyEquivalence;

    @CheckForNull
    MapMakerInternalMap.Strength keyStrength;
    boolean useCustomMap;

    @CheckForNull
    MapMakerInternalMap.Strength valueStrength;

    static final enum Dummy extends Enum<Dummy> {
        private static final /* synthetic */ Dummy[] $VALUES = null;
        public static final Dummy VALUE = null;

        private static /* synthetic */ Dummy[] $values() {
            return new Dummy[]{VALUE};
        }

        static {
            VALUE = new Dummy("VALUE", 0);
            $VALUES = $values();
        }

        private Dummy(String r1, int r2) {
            super(r1, r2);
        }

        public static Dummy valueOf(String r1) {
            return (Dummy) Enum.valueOf(Dummy.class, r1);
        }

        public static Dummy[] values() {
            return (Dummy[]) $VALUES.clone();
        }
    }

    public MapMaker() {
        this.initialCapacity = -1;
        this.concurrencyLevel = -1;
    }

    public MapMaker concurrencyLevel(int r6) {
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

    int getConcurrencyLevel() {
        if (this.concurrencyLevel != (-1)) goto L6;
        return 4;
    L6:
        return this.concurrencyLevel;
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

    MapMakerInternalMap.Strength getKeyStrength() {
        return (MapMakerInternalMap.Strength) MoreObjects.firstNonNull(this.keyStrength, MapMakerInternalMap.Strength.STRONG);
    }

    MapMakerInternalMap.Strength getValueStrength() {
        return (MapMakerInternalMap.Strength) MoreObjects.firstNonNull(this.valueStrength, MapMakerInternalMap.Strength.STRONG);
    }

    public MapMaker initialCapacity(int r6) {
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

    MapMaker keyEquivalence(Equivalence<Object> r5) {
        if (this.keyEquivalence != null) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkState(r0, "key equivalence was already set to %s", this.keyEquivalence);
        this.keyEquivalence = (Equivalence) Preconditions.checkNotNull(r5);
        this.useCustomMap = true;
        return this;
    L5:
        r0 = false;
        goto L6
    }

    public <K, V> ConcurrentMap<K, V> makeMap() {
        if (this.useCustomMap == true) goto L7;
        return new ConcurrentHashMap(getInitialCapacity(), 0.75f, getConcurrencyLevel());
    L7:
        return MapMakerInternalMap.create(this);
    }

    MapMaker setKeyStrength(MapMakerInternalMap.Strength r5) {
        if (this.keyStrength != null) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkState(r0, "Key strength was already set to %s", this.keyStrength);
        this.keyStrength = (MapMakerInternalMap.Strength) Preconditions.checkNotNull(r5);
        if (r5 == MapMakerInternalMap.Strength.STRONG) goto L9;
        this.useCustomMap = true;
    L9:
        return this;
    L5:
        r0 = false;
        goto L6
    }

    MapMaker setValueStrength(MapMakerInternalMap.Strength r5) {
        if (this.valueStrength != null) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkState(r0, "Value strength was already set to %s", this.valueStrength);
        this.valueStrength = (MapMakerInternalMap.Strength) Preconditions.checkNotNull(r5);
        if (r5 == MapMakerInternalMap.Strength.STRONG) goto L9;
        this.useCustomMap = true;
    L9:
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
        if (this.keyStrength == null) goto L12;
        r0.add("keyStrength", Ascii.toLowerCase(this.keyStrength.toString()));
    L12:
        if (this.valueStrength == null) goto L15;
        r0.add("valueStrength", Ascii.toLowerCase(this.valueStrength.toString()));
    L15:
        if (this.keyEquivalence == null) goto L18;
        r0.addValue("keyEquivalence");
    L18:
        return r0.toString();
    }

    public MapMaker weakKeys() {
        return setKeyStrength(MapMakerInternalMap.Strength.WEAK);
    }

    public MapMaker weakValues() {
        return setValueStrength(MapMakerInternalMap.Strength.WEAK);
    }
}
