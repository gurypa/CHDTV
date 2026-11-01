package com.google.common.util.concurrent;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.Lists;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
abstract class CollectionFuture<V, C> extends AggregateFuture<V, C> {

    @CheckForNull
    @LazyInit
    private List<Present<V>> values;

    static final class ListFuture<V> extends CollectionFuture<V, List<V>> {
        ListFuture(ImmutableCollection<? extends ListenableFuture<? extends V>> r1, boolean r2) {
            super(r1, r2);
            init();
        }

        @Override // com.google.common.util.concurrent.CollectionFuture
        public /* bridge */ /* synthetic */ Object combine(List r1) {
            return combine(r1);
        }

        @Override // com.google.common.util.concurrent.CollectionFuture
        public List<V> combine(List<Present<V>> r5) {
            ArrayList r0 = Lists.newArrayListWithCapacity(r5.size());
            Iterator<Present<V>> r1 = r5.iterator();
        L4:
            if (r1.hasNext() == false) goto L11;
            Present<V> r2 = r1.next();
            if (r2 == null) goto L8;
            V r3 = r2.value;
        L9:
            r0.add(r3);
            goto L4
        L8:
            r3 = null;
            goto L9
        L11:
            return Collections.unmodifiableList(r0);
        }
    }

    private static final class Present<V> {

        @ParametricNullness
        final V value;

        Present(@ParametricNullness V r1) {
            this.value = r1;
        }
    }

    CollectionFuture(ImmutableCollection<? extends ListenableFuture<? extends V>> r4, boolean r5) {
        super(r4, r5, true);
        if (r4.isEmpty() == false) goto L5;
        List<Present<V>> r0 = Collections.emptyList();
    L6:
        int r1 = 0;
    L8:
        if (r1 >= r4.size()) goto L10;
        r0.add(null);
        r1 = r1 + 1;
        goto L8
    L10:
        this.values = r0;
        return;
    L5:
        r0 = Lists.newArrayListWithCapacity(r4.size());
        goto L6
    }

    @Override // com.google.common.util.concurrent.AggregateFuture
    final void collectOneValue(int r3, @ParametricNullness V r4) {
        List<Present<V>> r0 = this.values;
        if (r0 == null) goto L6;
        r0.set(r3, new Present(r4));
        return;
    }

    abstract C combine(List<Present<V>> r1);

    @Override // com.google.common.util.concurrent.AggregateFuture
    final void handleAllCompleted() {
        List<Present<V>> r0 = this.values;
        if (r0 == null) goto L6;
        set(combine(r0));
        return;
    }

    @Override // com.google.common.util.concurrent.AggregateFuture
    void releaseResources(AggregateFuture.ReleaseResourcesReason r2) {
        super.releaseResources(r2);
        this.values = null;
    }
}
