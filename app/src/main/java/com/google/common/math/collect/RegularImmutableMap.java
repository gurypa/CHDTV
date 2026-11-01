package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import javax.annotation.CheckForNull;
import kotlin.UShort;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class RegularImmutableMap<K, V> extends ImmutableMap<K, V> {
    private static final byte ABSENT = -1;
    private static final int BYTE_MASK = 255;
    private static final int BYTE_MAX_SIZE = 128;
    static final ImmutableMap<Object, Object> EMPTY = null;
    private static final int SHORT_MASK = 65535;
    private static final int SHORT_MAX_SIZE = 32768;
    private static final long serialVersionUID = 0;
    final transient Object[] alternatingKeysAndValues;

    @CheckForNull
    private final transient Object hashTable;
    private final transient int size;

    static class EntrySet<K, V> extends ImmutableSet<Map.Entry<K, V>> {
        private final transient Object[] alternatingKeysAndValues;
        private final transient int keyOffset;
        private final transient ImmutableMap<K, V> map;
        private final transient int size;

        EntrySet(ImmutableMap<K, V> r1, Object[] r2, int r3, int r4) {
            this.map = r1;
            this.alternatingKeysAndValues = r2;
            this.keyOffset = r3;
            this.size = r4;
        }

        static /* synthetic */ int access$000(EntrySet r1) {
            return r1.size;
        }

        static /* synthetic */ Object[] access$100(EntrySet r1) {
            return r1.alternatingKeysAndValues;
        }

        static /* synthetic */ int access$200(EntrySet r1) {
            return r1.keyOffset;
        }

        @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(@CheckForNull Object r6) {
            if ((r6 instanceof Map.Entry) == false) goto L10;
            Map.Entry r0 = (Map.Entry) r6;
            Object r2 = r0.getKey();
            Object r3 = r0.getValue();
            if (r3 != null) goto L7;
            return false;
        L7:
            if (r3.equals(this.map.get(r2)) == false) goto L12;
            return true;
        L12:
            return false;
        L10:
            return false;
        }

        @Override // com.google.common.collect.ImmutableCollection
        int copyIntoArray(Object[] r2, int r3) {
            return asList().copyIntoArray(r2, r3);
        }

        @Override // com.google.common.collect.ImmutableSet
        ImmutableList<Map.Entry<K, V>> createAsList() {
            return new C07201(this);
        }

        @Override // com.google.common.collect.ImmutableCollection
        boolean isPartialView() {
            return true;
        }

        @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set, java.util.NavigableSet, com.google.common.collect.SortedIterable
        public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
            return asList().iterator();
        }

        @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set, java.util.NavigableSet, com.google.common.collect.SortedIterable
        public /* bridge */ /* synthetic */ Iterator iterator() {
            return iterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.size;
        }

        @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection
        Object writeReplace() {
            return super.writeReplace();
        }
    }

    static final class KeySet<K> extends ImmutableSet<K> {
        private final transient ImmutableList<K> list;
        private final transient ImmutableMap<K, ?> map;

        KeySet(ImmutableMap<K, ?> r1, ImmutableList<K> r2) {
            this.map = r1;
            this.list = r2;
        }

        @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection
        public ImmutableList<K> asList() {
            return this.list;
        }

        @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(@CheckForNull Object r2) {
            if (this.map.get(r2) == null) goto L5;
            return true;
        L5:
            return false;
        }

        @Override // com.google.common.collect.ImmutableCollection
        int copyIntoArray(Object[] r2, int r3) {
            return asList().copyIntoArray(r2, r3);
        }

        @Override // com.google.common.collect.ImmutableCollection
        boolean isPartialView() {
            return true;
        }

        @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set, java.util.NavigableSet, com.google.common.collect.SortedIterable
        public UnmodifiableIterator<K> iterator() {
            return asList().iterator();
        }

        @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set, java.util.NavigableSet, com.google.common.collect.SortedIterable
        public /* bridge */ /* synthetic */ Iterator iterator() {
            return iterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.map.size();
        }

        @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection
        Object writeReplace() {
            return super.writeReplace();
        }
    }

    static final class KeysOrValuesAsList extends ImmutableList<Object> {
        private final transient Object[] alternatingKeysAndValues;
        private final transient int offset;
        private final transient int size;

        KeysOrValuesAsList(Object[] r1, int r2, int r3) {
            this.alternatingKeysAndValues = r1;
            this.offset = r2;
            this.size = r3;
        }

        @Override // java.util.List
        public Object get(int r4) {
            Preconditions.checkElementIndex(r4, this.size);
            return Objects.requireNonNull(this.alternatingKeysAndValues[(r4 * 2) + this.offset]);
        }

        @Override // com.google.common.collect.ImmutableCollection
        boolean isPartialView() {
            return true;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.size;
        }

        @Override // com.google.common.collect.ImmutableList, com.google.common.collect.ImmutableCollection
        Object writeReplace() {
            return super.writeReplace();
        }
    }

    static {
        EMPTY = new RegularImmutableMap(null, new Object[0], 0);
    }

    private RegularImmutableMap(@CheckForNull Object r1, Object[] r2, int r3) {
        this.hashTable = r1;
        this.alternatingKeysAndValues = r2;
        this.size = r3;
    }

    static <K, V> RegularImmutableMap<K, V> create(int r1, Object[] r2) {
        return create(r1, r2, null);
    }

    static <K, V> RegularImmutableMap<K, V> create(int r6, Object[] r7, ImmutableMap.Builder<K, V> r8) {
        if (r6 != 0) goto L6;
        return (RegularImmutableMap) EMPTY;
    L6:
        if (r6 != 1) goto L9;
        CollectPreconditions.checkEntryNotNull(Objects.requireNonNull(r7[0]), Objects.requireNonNull(r7[1]));
        return new RegularImmutableMap(null, r7, 1);
    L9:
        Preconditions.checkPositionIndex(r6, r7.length >> 1);
        Object r3 = createHashTable(r7, r6, ImmutableSet.chooseTableSize(r6), 0);
        if ((r3 instanceof Object[]) == false) goto L16;
        Object[] r4 = (Object[]) r3;
        ImmutableMap.Builder.DuplicateKey r5 = (ImmutableMap.Builder.DuplicateKey) r4[2];
        if (r8 == null) goto L15;
        r8.duplicateKey = r5;
        Object r0 = r4[0];
        r6 = ((Integer) r4[1]).intValue();
        r7 = Arrays.copyOf(r7, r6 * 2);
    L18:
        return new RegularImmutableMap(r0, r7, r6);
    L15:
        throw r5.exception();
    L16:
        r0 = r3;
        goto L18
    }

    @CheckForNull
    private static Object createHashTable(Object[] r20, int r21, int r22, int r23) {
        boolean r2 = true;
        if (r21 != 1) goto L6;
        CollectPreconditions.checkEntryNotNull(Objects.requireNonNull(r20[r23]), Objects.requireNonNull(r20[r23 ^ 1]));
        return null;
    L6:
        int r3 = r22 - 1;
        ImmutableMap.Builder.DuplicateKey r4 = null;
        int r8 = -1;
        if (r22 > 128) goto L28;
        byte[] r5 = new byte[r22];
        Arrays.fill(r5, (byte) -1);
        int r9 = 0;
        int r10 = 0;
    L9:
        if (r10 >= r21) goto L22;
        int r11 = (r10 * 2) + r23;
        int r12 = (r9 * 2) + r23;
        Object r13 = Objects.requireNonNull(r20[r11]);
        Object r14 = Objects.requireNonNull(r20[r11 ^ 1]);
        CollectPreconditions.checkEntryNotNull(r13, r14);
        int r15 = Hashing.smear(r13.hashCode());
    L11:
        int r16 = r15 & r3;
        boolean r17 = r2;
        int r6 = r5[r16] & 255;
        if (r6 == 255) goto L13;
        if (r13.equals(r20[r6]) == true) goto L19;
        r15 = r16 + 1;
        r2 = r17;
        goto L11
    L19:
        ImmutableMap.Builder.DuplicateKey r7 = new ImmutableMap.Builder.DuplicateKey(r13, r14, Objects.requireNonNull(r20[r6 ^ 1]));
        r20[r6 ^ 1] = r14;
        r4 = r7;
    L20:
        r10 = r10 + 1;
        r2 = r17;
        goto L9
    L13:
        r5[r16] = (byte) r12;
        if (r9 >= r10) goto L16;
        r20[r12] = r13;
        r20[r12 ^ 1] = r14;
    L16:
        r9 = r9 + 1;
        goto L20
    L22:
        boolean r18 = r2;
        if (r9 == r21) goto L80;
        Object[] r19 = new Object[3];
        r19[0] = r5;
        r19[r18 ? 1 : 0] = Integer.valueOf(r9);
        r19[2] = r4;
        return r19;
    L80:
        return r5;
    L28:
        if (r22 > 32768) goto L47;
        short[] r24 = new short[r22];
        Arrays.fill(r24, -1);
        int r25 = 0;
        int r26 = 0;
    L30:
        if (r26 >= r21) goto L43;
        int r27 = (r26 * 2) + r23;
        int r28 = (r25 * 2) + r23;
        Object r110 = Objects.requireNonNull(r20[r27]);
        Object r111 = Objects.requireNonNull(r20[r27 ^ 1]);
        CollectPreconditions.checkEntryNotNull(r110, r111);
        int r112 = Hashing.smear(r110.hashCode());
    L32:
        int r113 = r112 & r3;
        int r114 = r24[r113] & 65535;
        if (r114 == 65535) goto L34;
        if (r110.equals(r20[r114]) == true) goto L40;
        r112 = r113 + 1;
        goto L32
    L40:
        ImmutableMap.Builder.DuplicateKey r115 = new ImmutableMap.Builder.DuplicateKey(r110, r111, Objects.requireNonNull(r20[r114 ^ 1]));
        r20[r114 ^ 1] = r111;
        r4 = r115;
    L41:
        r26 = r26 + 1;
        goto L30
    L34:
        r24[r113] = (short) r28;
        if (r25 >= r26) goto L37;
        r20[r28] = r110;
        r20[r28 ^ 1] = r111;
    L37:
        r25 = r25 + 1;
        goto L41
    L43:
        if (r25 != r21) goto L46;
        return r24;
    L46:
        return new Object[]{r24, Integer.valueOf(r25), r4};
    L47:
        int[] r29 = new int[r22];
        Arrays.fill(r29, -1);
        int r30 = 0;
        int r31 = 0;
    L48:
        if (r31 >= r21) goto L61;
        int r32 = (r31 * 2) + r23;
        int r116 = (r30 * 2) + r23;
        Object r117 = Objects.requireNonNull(r20[r32]);
        Object r118 = Objects.requireNonNull(r20[r32 ^ 1]);
        CollectPreconditions.checkEntryNotNull(r117, r118);
        int r119 = Hashing.smear(r117.hashCode());
    L50:
        int r120 = r119 & r3;
        int r121 = r29[r120];
        if (r121 == r8) goto L52;
        if (r117.equals(r20[r121]) == true) goto L58;
        r119 = r120 + 1;
        r8 = -1;
        goto L50
    L58:
        ImmutableMap.Builder.DuplicateKey r122 = new ImmutableMap.Builder.DuplicateKey(r117, r118, Objects.requireNonNull(r20[r121 ^ 1]));
        r20[r121 ^ 1] = r118;
        r4 = r122;
    L59:
        r31 = r31 + 1;
        r8 = -1;
        goto L48
    L52:
        r29[r120] = r116;
        if (r30 >= r31) goto L55;
        r20[r116] = r117;
        r20[r116 ^ 1] = r118;
    L55:
        r30 = r30 + 1;
        goto L59
    L61:
        if (r30 != r21) goto L64;
        return r29;
    L64:
        return new Object[]{r29, Integer.valueOf(r30), r4};
    }

    @CheckForNull
    static Object createHashTableOrThrow(Object[] r4, int r5, int r6, int r7) {
        Object r0 = createHashTable(r4, r5, r6, r7);
        if ((r0 instanceof Object[]) == true) goto L6;
        return r0;
    L6:
        throw ((ImmutableMap.Builder.DuplicateKey) ((Object[]) r0)[2]).exception();
    }

    @CheckForNull
    static Object get(@CheckForNull Object r6, Object[] r7, int r8, int r9, @CheckForNull Object r10) {
        if (r10 != null) goto L6;
        return null;
    L6:
        if (r8 == 1) goto L8;
        if (r6 != null) goto L15;
        return null;
    L15:
        if ((r6 instanceof byte[]) == false) goto L26;
        byte[] r2 = (byte[]) r6;
        int r3 = r2.length - 1;
        int r1 = Hashing.smear(r10.hashCode());
    L17:
        int r4 = r1 & r3;
        int r5 = r2[r4] & 255;
        if (r5 == 255) goto L19;
        if (r10.equals(r7[r5]) == true) goto L23;
        r1 = r4 + 1;
        goto L17
    L23:
        return r7[r5 ^ 1];
    L19:
        return null;
    L26:
        if ((r6 instanceof short[]) == false) goto L36;
        short[] r11 = (short[]) r6;
        int r12 = r11.length - 1;
        int r13 = Hashing.smear(r10.hashCode());
    L28:
        int r14 = r13 & r12;
        int r15 = r11[r14] & UShort.MAX_VALUE;
        if (r15 == 65535) goto L30;
        if (r10.equals(r7[r15]) == true) goto L34;
        r13 = r14 + 1;
        goto L28
    L34:
        return r7[r15 ^ 1];
    L30:
        return null;
    L36:
        int[] r16 = (int[]) r6;
        int r17 = r16.length - 1;
        int r18 = Hashing.smear(r10.hashCode());
    L37:
        int r19 = r18 & r17;
        int r20 = r16[r19];
        if (r20 == (-1)) goto L39;
        if (r10.equals(r7[r20]) == true) goto L43;
        r18 = r19 + 1;
        goto L37
    L43:
        return r7[r20 ^ 1];
    L39:
        return null;
    L8:
        if (Objects.requireNonNull(r7[r9]).equals(r10) == true) goto L11;
        return null;
    L11:
        return Objects.requireNonNull(r7[r9 ^ 1]);
    }

    @Override // com.google.common.collect.ImmutableMap
    ImmutableSet<Map.Entry<K, V>> createEntrySet() {
        return new EntrySet(this, this.alternatingKeysAndValues, 0, this.size);
    }

    @Override // com.google.common.collect.ImmutableMap
    ImmutableSet<K> createKeySet() {
        return new KeySet(this, new KeysOrValuesAsList(this.alternatingKeysAndValues, 0, this.size));
    }

    @Override // com.google.common.collect.ImmutableMap
    ImmutableCollection<V> createValues() {
        return new KeysOrValuesAsList(this.alternatingKeysAndValues, 1, this.size);
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    @CheckForNull
    public V get(@CheckForNull Object r5) {
        V r0 = (V) get(this.hashTable, this.alternatingKeysAndValues, this.size, 0, r5);
        if (r0 != null) goto L6;
        return null;
    L6:
        return r0;
    }

    @Override // com.google.common.collect.ImmutableMap
    boolean isPartialView() {
        return false;
    }

    @Override // java.util.Map
    public int size() {
        return this.size;
    }

    @Override // com.google.common.collect.ImmutableMap
    Object writeReplace() {
        return super.writeReplace();
    }
}
