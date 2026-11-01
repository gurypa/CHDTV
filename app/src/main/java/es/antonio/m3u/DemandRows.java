package es.antonio.m3u;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.RandomAccess;

/* JADX INFO: loaded from: classes7.dex */
public final class DemandRows extends AbstractList<MainActivity.Row> implements RandomAccess {
    private boolean borrowed;
    private List<MainActivity.Row> values;

    static final class Prefix extends AbstractList<MainActivity.Row> implements View, RandomAccess {
        final MainActivity.Row heading;
        final List<MainActivity.Row> rows;

        Prefix(MainActivity.Row r1, List<MainActivity.Row> r2) {
            this.heading = r1;
            this.rows = r2;
        }

        @Override // java.util.AbstractList, java.util.List
        public MainActivity.Row get(int r2) {
            if (r2 < 0) goto L10;
            if (r2 >= size()) goto L10;
            if (r2 != 0) goto L8;
            return this.heading;
        L8:
            return this.rows.get(r2 - 1);
        L10:
            throw new IndexOutOfBoundsException();
        }

        @Override // java.util.AbstractList, java.util.List
        public /* bridge */ /* synthetic */ Object get(int r1) {
            return get(r1);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.rows.size() + 1;
        }
    }

    interface View {
    }

    public DemandRows() {
        this.values = new ArrayList();
    }

    private void own() {
        if (this.borrowed == false) goto L6;
        this.values = new ArrayList(this.values);
        this.borrowed = false;
        return;
    }

    static List<MainActivity.Row> prepend(MainActivity.Row r1, List<MainActivity.Row> r2) {
        return new Prefix(r1, r2);
    }

    public void add(int r2, MainActivity.Row r3) {
        own();
        this.values.add(r2, r3);
        this.modCount++;
    }

    @Override // java.util.AbstractList, java.util.List
    public /* bridge */ /* synthetic */ void add(int r1, Object r2) {
        add(r1, (MainActivity.Row) r2);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean addAll(Collection<? extends MainActivity.Row> r3) {
        if (this.values.isEmpty() == true) goto L5;
    L8:
        own();
        boolean r4 = this.values.addAll(r3);
        if (r4 == false) goto L11;
        this.modCount++;
    L11:
        return r4;
    L5:
        if ((r3 instanceof View) == false) goto L8;
        this.values = (List) r3;
        this.borrowed = true;
        this.modCount++;
        return !r3.isEmpty();
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public void clear() {
        this.values = new ArrayList();
        this.borrowed = false;
        this.modCount++;
    }

    @Override // java.util.AbstractList, java.util.List
    public MainActivity.Row get(int r2) {
        return this.values.get(r2);
    }

    @Override // java.util.AbstractList, java.util.List
    public /* bridge */ /* synthetic */ Object get(int r1) {
        return get(r1);
    }

    @Override // java.util.AbstractList, java.util.List
    public MainActivity.Row remove(int r2) {
        own();
        this.modCount++;
        return this.values.remove(r2);
    }

    @Override // java.util.AbstractList, java.util.List
    public /* bridge */ /* synthetic */ Object remove(int r1) {
        return remove(r1);
    }

    public MainActivity.Row set(int r2, MainActivity.Row r3) {
        own();
        return this.values.set(r2, r3);
    }

    @Override // java.util.AbstractList, java.util.List
    public /* bridge */ /* synthetic */ Object set(int r1, Object r2) {
        return set(r1, (MainActivity.Row) r2);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public int size() {
        return this.values.size();
    }
}
