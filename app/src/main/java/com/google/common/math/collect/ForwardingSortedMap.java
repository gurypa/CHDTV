package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class ForwardingSortedMap<K, V> extends ForwardingMap<K, V> implements SortedMap<K, V> {

    protected class StandardKeySet extends Maps.SortedKeySet<K, V> {
        final /* synthetic */ ForwardingSortedMap this$0;

        public StandardKeySet(ForwardingSortedMap r1) {
            this.this$0 = r1;
            super(r1);
        }
    }

    protected ForwardingSortedMap() {
    }

    static int unsafeCompare(@CheckForNull Comparator<?> r1, @CheckForNull Object r2, @CheckForNull Object r3) {
        if (r1 != null) goto L6;
        return ((Comparable) r2).compareTo(r3);
    L6:
        return r1.compare(r2, r3);
    }

    @Override // java.util.SortedMap
    @CheckForNull
    public Comparator<? super K> comparator() {
        return delegate().comparator();
    }

    @Override // com.google.common.collect.ForwardingMap, com.google.common.collect.ForwardingObject
    protected /* bridge */ /* synthetic */ Object delegate() {
        return delegate();
    }

    @Override // com.google.common.collect.ForwardingMap, com.google.common.collect.ForwardingObject
    protected /* bridge */ /* synthetic */ Map delegate() {
        return delegate();
    }

    @Override // com.google.common.collect.ForwardingMap, com.google.common.collect.ForwardingObject
    protected abstract SortedMap<K, V> delegate();

    @Override // java.util.SortedMap
    @ParametricNullness
    public K firstKey() {
        return delegate().firstKey();
    }

    @Override // java.util.SortedMap
    public SortedMap<K, V> headMap(@ParametricNullness K r2) {
        return delegate().headMap(r2);
    }

    @Override // java.util.SortedMap
    @ParametricNullness
    public K lastKey() {
        return delegate().lastKey();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.ForwardingMap
    protected boolean standardContainsKey(@CheckForNull Object r5) {
        K r2 = tailMap(r5).firstKey();     // Catch: NullPointerException -> L7 Throwable -> L9 ClassCastException -> L11
        if (unsafeCompare(comparator(), r2, r5) != 0) goto L15;
        return true;
    L15:
        return false;
    L12:
        return false;
    }

    protected SortedMap<K, V> standardSubMap(K r3, K r4) {
        if (unsafeCompare(comparator(), r3, r4) > 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0, "fromKey must be <= toKey");
        return tailMap(r3).headMap(r4);
    L5:
        r0 = false;
        goto L6
    }

    @Override // java.util.SortedMap
    public SortedMap<K, V> subMap(@ParametricNullness K r2, @ParametricNullness K r3) {
        return delegate().subMap(r2, r3);
    }

    @Override // java.util.SortedMap
    public SortedMap<K, V> tailMap(@ParametricNullness K r2) {
        return delegate().tailMap(r2);
    }
}
