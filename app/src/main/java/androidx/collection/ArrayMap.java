package androidx.collection;

import java.lang.reflect.Array;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class ArrayMap<K, V> extends SimpleArrayMap<K, V> implements Map<K, V> {
    ArrayMap<K, V>.EntrySet mEntrySet;
    ArrayMap<K, V>.KeySet mKeySet;
    ArrayMap<K, V>.ValueCollection mValues;

    final class EntrySet extends AbstractSet<Map.Entry<K, V>> {
        final /* synthetic */ ArrayMap this$0;

        EntrySet(ArrayMap r1) {
            this.this$0 = r1;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<Map.Entry<K, V>> iterator() {
            return new MapIterator(this.this$0);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.this$0.mSize;
        }
    }

    final class KeyIterator extends IndexBasedArrayIterator<K> {
        final /* synthetic */ ArrayMap this$0;

        KeyIterator(ArrayMap r2) {
            this.this$0 = r2;
            super(r2.mSize);
        }

        @Override // androidx.collection.IndexBasedArrayIterator
        protected K elementAt(int r2) {
            return this.this$0.keyAt(r2);
        }

        @Override // androidx.collection.IndexBasedArrayIterator
        protected void removeAt(int r2) {
            this.this$0.removeAt(r2);
        }
    }

    final class KeySet implements Set<K> {
        final /* synthetic */ ArrayMap this$0;

        KeySet(ArrayMap r1) {
            this.this$0 = r1;
        }

        @Override // java.util.Set, java.util.Collection
        public boolean add(K r2) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Set, java.util.Collection
        public boolean addAll(Collection<? extends K> r2) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Set, java.util.Collection
        public void clear() {
            this.this$0.clear();
        }

        @Override // java.util.Set, java.util.Collection
        public boolean contains(Object r2) {
            return this.this$0.containsKey(r2);
        }

        @Override // java.util.Set, java.util.Collection
        public boolean containsAll(Collection<?> r2) {
            return this.this$0.containsAll(r2);
        }

        @Override // java.util.Set, java.util.Collection
        public boolean equals(Object r2) {
            return ArrayMap.equalsSetHelper(this, r2);
        }

        @Override // java.util.Set, java.util.Collection
        public int hashCode() {
            int r0 = 0;
            int r1 = this.this$0.mSize - 1;
        L3:
            if (r1 < 0) goto L9;
            K r2 = this.this$0.keyAt(r1);
            if (r2 != null) goto L7;
            int r3 = 0;
        L8:
            r0 = r0 + r3;
            r1 = r1 - 1;
            goto L3
        L7:
            r3 = r2.hashCode();
            goto L8
        L9:
            return r0;
        }

        @Override // java.util.Set, java.util.Collection
        public boolean isEmpty() {
            return this.this$0.isEmpty();
        }

        @Override // java.util.Set, java.util.Collection, java.lang.Iterable
        public Iterator<K> iterator() {
            return new KeyIterator(this.this$0);
        }

        @Override // java.util.Set, java.util.Collection
        public boolean remove(Object r3) {
            int r0 = this.this$0.indexOfKey(r3);
            if (r0 < 0) goto L6;
            this.this$0.removeAt(r0);
            return true;
        L6:
            return false;
        }

        @Override // java.util.Set, java.util.Collection
        public boolean removeAll(Collection<?> r2) {
            return this.this$0.removeAll(r2);
        }

        @Override // java.util.Set, java.util.Collection
        public boolean retainAll(Collection<?> r2) {
            return this.this$0.retainAll(r2);
        }

        @Override // java.util.Set, java.util.Collection
        public int size() {
            return this.this$0.mSize;
        }

        @Override // java.util.Set, java.util.Collection
        public Object[] toArray() {
            int r0 = this.this$0.mSize;
            Object[] r1 = new Object[r0];
            int r2 = 0;
        L3:
            if (r2 >= r0) goto L5;
            r1[r2] = this.this$0.keyAt(r2);
            r2 = r2 + 1;
            goto L3
        L5:
            return r1;
        }

        @Override // java.util.Set, java.util.Collection
        public <T> T[] toArray(T[] r3) {
            return (T[]) this.this$0.toArrayHelper(r3, 0);
        }
    }

    final class MapIterator implements Iterator<Map.Entry<K, V>>, Map.Entry<K, V> {
        int mEnd;
        boolean mEntryValid;
        int mIndex;
        final /* synthetic */ ArrayMap this$0;

        MapIterator(ArrayMap r2) {
            this.this$0 = r2;
            this.mEnd = r2.mSize - 1;
            this.mIndex = -1;
        }

        @Override // java.util.Map.Entry
        public boolean equals(Object r6) {
            if (this.mEntryValid == false) goto L15;
            if ((r6 instanceof Map.Entry) == true) goto L7;
            return false;
        L7:
            Map.Entry r0 = (Map.Entry) r6;
            if (ContainerHelpers.equal(r0.getKey(), this.this$0.keyAt(this.mIndex)) == true) goto L10;
            return false;
        L10:
            if (ContainerHelpers.equal(r0.getValue(), this.this$0.valueAt(this.mIndex)) == false) goto L17;
            return true;
        L17:
            return false;
        L15:
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
        }

        @Override // java.util.Map.Entry
        public K getKey() {
            if (this.mEntryValid == false) goto L7;
            return this.this$0.keyAt(this.mIndex);
        L7:
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
        }

        @Override // java.util.Map.Entry
        public V getValue() {
            if (this.mEntryValid == false) goto L7;
            return this.this$0.valueAt(this.mIndex);
        L7:
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.mIndex >= this.mEnd) goto L5;
            return true;
        L5:
            return false;
        }

        @Override // java.util.Map.Entry
        public int hashCode() {
            if (this.mEntryValid == false) goto L14;
            K r0 = this.this$0.keyAt(this.mIndex);
            V r1 = this.this$0.valueAt(this.mIndex);
            int r2 = 0;
            if (r0 != null) goto L7;
            int r3 = 0;
        L8:
            if (r1 == null) goto L12;
            r2 = r1.hashCode();
        L12:
            return r2 ^ r3;
        L7:
            r3 = r0.hashCode();
            goto L8
        L14:
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
        }

        @Override // java.util.Iterator
        public /* bridge */ /* synthetic */ Object next() {
            return next();
        }

        @Override // java.util.Iterator
        public Map.Entry<K, V> next() {
            if (hasNext() == false) goto L7;
            this.mIndex++;
            this.mEntryValid = true;
            return this;
        L7:
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            if (this.mEntryValid == false) goto L7;
            this.this$0.removeAt(this.mIndex);
            this.mIndex--;
            this.mEnd--;
            this.mEntryValid = false;
            return;
        L7:
            throw new IllegalStateException();
        }

        @Override // java.util.Map.Entry
        public V setValue(V r3) {
            if (this.mEntryValid == false) goto L7;
            return this.this$0.setValueAt(this.mIndex, r3);
        L7:
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
        }

        public String toString() {
            return getKey() + "=" + getValue();
        }
    }

    final class ValueCollection implements Collection<V> {
        final /* synthetic */ ArrayMap this$0;

        ValueCollection(ArrayMap r1) {
            this.this$0 = r1;
        }

        @Override // java.util.Collection
        public boolean add(V r2) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Collection
        public boolean addAll(Collection<? extends V> r2) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Collection
        public void clear() {
            this.this$0.clear();
        }

        @Override // java.util.Collection
        public boolean contains(Object r2) {
            if (this.this$0.indexOfValue(r2) < 0) goto L5;
            return true;
        L5:
            return false;
        }

        @Override // java.util.Collection
        public boolean containsAll(Collection<?> r4) {
            Iterator<?> r0 = r4.iterator();
        L4:
            if (r0.hasNext() == false) goto L10;
            if (contains(r0.next()) == true) goto L4;
            return false;
        L10:
            return true;
        }

        @Override // java.util.Collection
        public boolean isEmpty() {
            return this.this$0.isEmpty();
        }

        @Override // java.util.Collection, java.lang.Iterable
        public Iterator<V> iterator() {
            return new ValueIterator(this.this$0);
        }

        @Override // java.util.Collection
        public boolean remove(Object r3) {
            int r0 = this.this$0.indexOfValue(r3);
            if (r0 < 0) goto L6;
            this.this$0.removeAt(r0);
            return true;
        L6:
            return false;
        }

        @Override // java.util.Collection
        public boolean removeAll(Collection<?> r6) {
            int r0 = this.this$0.mSize;
            boolean r1 = false;
            int r2 = 0;
        L3:
            if (r2 >= r0) goto L8;
            if (r6.contains(this.this$0.valueAt(r2)) == false) goto L7;
            this.this$0.removeAt(r2);
            r2 = r2 - 1;
            r0 = r0 - 1;
            r1 = true;
        L7:
            r2 = r2 + 1;
            goto L3
        L8:
            return r1;
        }

        @Override // java.util.Collection
        public boolean retainAll(Collection<?> r6) {
            int r0 = this.this$0.mSize;
            boolean r1 = false;
            int r2 = 0;
        L3:
            if (r2 >= r0) goto L8;
            if (r6.contains(this.this$0.valueAt(r2)) == true) goto L7;
            this.this$0.removeAt(r2);
            r2 = r2 - 1;
            r0 = r0 - 1;
            r1 = true;
        L7:
            r2 = r2 + 1;
            goto L3
        L8:
            return r1;
        }

        @Override // java.util.Collection
        public int size() {
            return this.this$0.mSize;
        }

        @Override // java.util.Collection
        public Object[] toArray() {
            int r0 = this.this$0.mSize;
            Object[] r1 = new Object[r0];
            int r2 = 0;
        L3:
            if (r2 >= r0) goto L5;
            r1[r2] = this.this$0.valueAt(r2);
            r2 = r2 + 1;
            goto L3
        L5:
            return r1;
        }

        @Override // java.util.Collection
        public <T> T[] toArray(T[] r3) {
            return (T[]) this.this$0.toArrayHelper(r3, 1);
        }
    }

    final class ValueIterator extends IndexBasedArrayIterator<V> {
        final /* synthetic */ ArrayMap this$0;

        ValueIterator(ArrayMap r2) {
            this.this$0 = r2;
            super(r2.mSize);
        }

        @Override // androidx.collection.IndexBasedArrayIterator
        protected V elementAt(int r2) {
            return this.this$0.valueAt(r2);
        }

        @Override // androidx.collection.IndexBasedArrayIterator
        protected void removeAt(int r2) {
            this.this$0.removeAt(r2);
        }
    }

    public ArrayMap() {
    }

    public ArrayMap(int r1) {
        super(r1);
    }

    public ArrayMap(SimpleArrayMap r1) {
        super(r1);
    }

    static <T> boolean equalsSetHelper(Set<T> r5, Object r6) {
        if (r5 != r6) goto L6;
        return true;
    L6:
        if ((r6 instanceof Set) == false) goto L18;
        Set r1 = (Set) r6;
        if (r5.size() != r1.size()) goto L14;
        if (r5.containsAll(r1) == false) goto L14;
        return true;
    L14:
        return false;
    L18:
        return false;
    }

    public boolean containsAll(Collection<?> r4) {
        Iterator<?> r0 = r4.iterator();
    L4:
        if (r0.hasNext() == false) goto L10;
        if (containsKey(r0.next()) == true) goto L4;
        return false;
    L10:
        return true;
    }

    @Override // java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        ArrayMap<K, V>.EntrySet r0 = this.mEntrySet;
        if (r0 != null) goto L6;
        ArrayMap<K, V>.EntrySet r1 = new EntrySet(this);
        this.mEntrySet = r1;
        return r1;
    L6:
        return r0;
    }

    @Override // java.util.Map
    public Set<K> keySet() {
        ArrayMap<K, V>.KeySet r0 = this.mKeySet;
        if (r0 != null) goto L6;
        ArrayMap<K, V>.KeySet r1 = new KeySet(this);
        this.mKeySet = r1;
        return r1;
    L6:
        return r0;
    }

    @Override // java.util.Map
    public void putAll(Map<? extends K, ? extends V> r5) {
        ensureCapacity(this.mSize + r5.size());
        Iterator<Map.Entry<? extends K, ? extends V>> r0 = r5.entrySet().iterator();
    L4:
        if (r0.hasNext() == false) goto L6;
        Map.Entry<? extends K, ? extends V> r1 = r0.next();
        put(r1.getKey(), r1.getValue());
        goto L4
    }

    public boolean removeAll(Collection<?> r4) {
        int r0 = this.mSize;
        Iterator<?> r1 = r4.iterator();
    L4:
        if (r1.hasNext() == false) goto L7;
        remove(r1.next());
        goto L4
    L7:
        if (r0 == this.mSize) goto L9;
        return true;
    L9:
        return false;
    }

    public boolean retainAll(Collection<?> r5) {
        int r0 = this.mSize;
        int r1 = this.mSize - 1;
    L3:
        if (r1 < 0) goto L9;
        if (r5.contains(keyAt(r1)) == true) goto L7;
        removeAt(r1);
    L7:
        r1 = r1 - 1;
        goto L3
    L9:
        if (r0 != this.mSize) goto L16;
        return false;
    L16:
        return true;
    }

    <T> T[] toArrayHelper(T[] r5, int r6) {
        int r0 = this.mSize;
        if (r5.length >= r0) goto L5;
        r5 = (T[]) ((Object[]) Array.newInstance(r5.getClass().getComponentType(), r0));
    L5:
        int r1 = 0;
    L6:
        if (r1 >= r0) goto L9;
        T[] r7 = r5;
        r7[r1] = this.mArray[(r1 << 1) + r6];
        r1 = r1 + 1;
        goto L6
    L9:
        if (r5.length <= r0) goto L11;
        r5[r0] = null;
    L11:
        return r5;
    }

    @Override // java.util.Map
    public Collection<V> values() {
        ArrayMap<K, V>.ValueCollection r0 = this.mValues;
        if (r0 != null) goto L6;
        ArrayMap<K, V>.ValueCollection r1 = new ValueCollection(this);
        this.mValues = r1;
        return r1;
    L6:
        return r0;
    }
}
