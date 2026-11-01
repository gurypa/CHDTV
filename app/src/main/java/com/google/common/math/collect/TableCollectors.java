package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class TableCollectors {

    /* JADX INFO: renamed from: com.google.common.collect.TableCollectors$1 */
    static /* synthetic */ class C07521 {
    }

    private static final class ImmutableTableCollectorState<R, C, V> {
        final List<MutableCell<R, C, V>> insertionOrder;
        final Table<R, C, MutableCell<R, C, V>> table;

        private ImmutableTableCollectorState() {
            this.insertionOrder = new ArrayList();
            this.table = HashBasedTable.create();
        }

        /* synthetic */ ImmutableTableCollectorState(C07521 r1) {
            this();
        }

        ImmutableTableCollectorState<R, C, V> combine(ImmutableTableCollectorState<R, C, V> r6, BinaryOperator<V> r7) {
            Iterator<MutableCell<R, C, V>> r0 = r6.insertionOrder.iterator();
        L4:
            if (r0.hasNext() == false) goto L6;
            MutableCell<R, C, V> r1 = r0.next();
            put(r1.getRowKey(), r1.getColumnKey(), r1.getValue(), r7);
            goto L4
        L6:
            return this;
        }

        void put(R r4, C r5, V r6, BinaryOperator<V> r7) {
            MutableCell<R, C, V> r0 = this.table.get(r4, r5);
            if (r0 != null) goto L5;
            MutableCell<R, C, V> r1 = new MutableCell(r4, r5, r6);
            this.insertionOrder.add(r1);
            this.table.put(r4, r5, r1);
            return;
        L5:
            r0.merge(r6, r7);
        }

        ImmutableTable<R, C, V> toTable() {
            return ImmutableTable.copyOf(this.insertionOrder);
        }
    }

    private static final class MutableCell<R, C, V> extends Tables.AbstractCell<R, C, V> {
        private final C column;
        private final R row;
        private V value;

        MutableCell(R r2, C r3, V r4) {
            this.row = (R) Preconditions.checkNotNull(r2, "row");
            this.column = (C) Preconditions.checkNotNull(r3, "column");
            this.value = (V) Preconditions.checkNotNull(r4, "value");
        }

        @Override // com.google.common.collect.Table.Cell
        public C getColumnKey() {
            return this.column;
        }

        @Override // com.google.common.collect.Table.Cell
        public R getRowKey() {
            return this.row;
        }

        @Override // com.google.common.collect.Table.Cell
        public V getValue() {
            return this.value;
        }

        void merge(V r3, BinaryOperator<V> r4) {
            Preconditions.checkNotNull(r3, "value");
            this.value = (V) Preconditions.checkNotNull(r4.apply(this.value, r3), "mergeFunction.apply");
        }
    }

    private TableCollectors() {
    }

    static /* synthetic */ void lambda$toImmutableTable$0(Function r3, Function r4, Function r5, ImmutableTable.Builder r6, Object r7) {
        r6.put(r3.apply(r7), r4.apply(r7), r5.apply(r7));
    }

    static /* synthetic */ ImmutableTableCollectorState lambda$toImmutableTable$1() {
        return new ImmutableTableCollectorState(null);
    }

    static /* synthetic */ void lambda$toImmutableTable$2(Function r3, Function r4, Function r5, BinaryOperator r6, ImmutableTableCollectorState r7, Object r8) {
        r7.put(r3.apply(r8), r4.apply(r8), r5.apply(r8), r6);
    }

    static /* synthetic */ ImmutableTableCollectorState lambda$toImmutableTable$3(BinaryOperator r1, ImmutableTableCollectorState r2, ImmutableTableCollectorState r3) {
        return r2.combine(r3, r1);
    }

    static /* synthetic */ ImmutableTable lambda$toImmutableTable$4(ImmutableTableCollectorState r1) {
        return r1.toTable();
    }

    static /* synthetic */ Object lambda$toTable$5(Object r3, Object r4) {
        throw new IllegalStateException("Conflicting values " + r3 + " and " + r4);
    }

    static /* synthetic */ void lambda$toTable$6(Function r3, Function r4, Function r5, BinaryOperator r6, Table r7, Object r8) {
        mergeTables(r7, r3.apply(r8), r4.apply(r8), r5.apply(r8), r6);
    }

    static /* synthetic */ Table lambda$toTable$7(BinaryOperator r5, Table r6, Table r7) {
        Iterator r0 = r7.cellSet().iterator();
    L4:
        if (r0.hasNext() == false) goto L6;
        Table.Cell r1 = (Table.Cell) r0.next();
        mergeTables(r6, r1.getRowKey(), r1.getColumnKey(), r1.getValue(), r5);
        goto L4
    L6:
        return r6;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static <R, C, V> void mergeTables(Table<R, C, V> r2, @ParametricNullness R r3, @ParametricNullness C r4, V r5, BinaryOperator<V> r6) {
        Preconditions.checkNotNull(r5);
        V r0 = r2.get(r3, r4);
        if (r0 != null) goto L5;
        r2.put(r3, r4, r5);
        return;
    L5:
        Object r1 = r6.apply(r0, r5);
        if (r1 != null) goto L8;
        r2.remove(r3, r4);
        return;
    L8:
        r2.put(r3, r4, r1);
    }

    static <T, R, C, V> Collector<T, ?, ImmutableTable<R, C, V>> toImmutableTable(final Function<? super T, ? extends R> r5, final Function<? super T, ? extends C> r6, final Function<? super T, ? extends V> r7) {
        Preconditions.checkNotNull(r5, "rowFunction");
        Preconditions.checkNotNull(r6, "columnFunction");
        Preconditions.checkNotNull(r7, "valueFunction");
        return Collector.of(new TableCollectors$$ExternalSyntheticLambda8(), new TableCollectors$$ExternalSyntheticLambda9(r5, r6, r7), new TableCollectors$$ExternalSyntheticLambda10(), new TableCollectors$$ExternalSyntheticLambda1(), new Collector.Characteristics[0]);
    }

    static <T, R, C, V> Collector<T, ?, ImmutableTable<R, C, V>> toImmutableTable(final Function<? super T, ? extends R> r5, final Function<? super T, ? extends C> r6, final Function<? super T, ? extends V> r7, final BinaryOperator<V> r8) {
        Preconditions.checkNotNull(r5, "rowFunction");
        Preconditions.checkNotNull(r6, "columnFunction");
        Preconditions.checkNotNull(r7, "valueFunction");
        Preconditions.checkNotNull(r8, "mergeFunction");
        return Collector.of(new TableCollectors$$ExternalSyntheticLambda4(), new TableCollectors$$ExternalSyntheticLambda5(r5, r6, r7, r8), new TableCollectors$$ExternalSyntheticLambda6(r8), new TableCollectors$$ExternalSyntheticLambda7(), new Collector.Characteristics[0]);
    }

    static <T, R, C, V, I extends Table<R, C, V>> Collector<T, ?, I> toTable(final Function<? super T, ? extends R> r3, final Function<? super T, ? extends C> r4, final Function<? super T, ? extends V> r5, final BinaryOperator<V> r6, Supplier<I> r7) {
        Preconditions.checkNotNull(r3);
        Preconditions.checkNotNull(r4);
        Preconditions.checkNotNull(r5);
        Preconditions.checkNotNull(r6);
        Preconditions.checkNotNull(r7);
        return Collector.of(r7, new TableCollectors$$ExternalSyntheticLambda2(r3, r4, r5, r6), new TableCollectors$$ExternalSyntheticLambda3(r6), new Collector.Characteristics[0]);
    }

    static <T, R, C, V, I extends Table<R, C, V>> Collector<T, ?, I> toTable(Function<? super T, ? extends R> r1, Function<? super T, ? extends C> r2, Function<? super T, ? extends V> r3, Supplier<I> r4) {
        return toTable(r1, r2, r3, new TableCollectors$$ExternalSyntheticLambda0(), r4);
    }
}
