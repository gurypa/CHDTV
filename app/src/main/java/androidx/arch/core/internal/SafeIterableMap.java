package androidx.arch.core.internal;

import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

/* JADX INFO: loaded from: classes.dex */
public class SafeIterableMap<K, V> implements Iterable<Map.Entry<K, V>> {
    private Entry<K, V> mEnd;
    private WeakHashMap<SupportRemove<K, V>, Boolean> mIterators;
    private int mSize;
    Entry<K, V> mStart;

    static class AscendingIterator<K, V> extends ListIterator<K, V> {
        AscendingIterator(Entry<K, V> r1, Entry<K, V> r2) {
            super(r1, r2);
        }

        @Override // androidx.arch.core.internal.SafeIterableMap.ListIterator
        Entry<K, V> backward(Entry<K, V> r2) {
            return r2.mPrevious;
        }

        @Override // androidx.arch.core.internal.SafeIterableMap.ListIterator
        Entry<K, V> forward(Entry<K, V> r2) {
            return r2.mNext;
        }
    }

    private static class DescendingIterator<K, V> extends ListIterator<K, V> {
        DescendingIterator(Entry<K, V> r1, Entry<K, V> r2) {
            super(r1, r2);
        }

        @Override // androidx.arch.core.internal.SafeIterableMap.ListIterator
        Entry<K, V> backward(Entry<K, V> r2) {
            return r2.mNext;
        }

        @Override // androidx.arch.core.internal.SafeIterableMap.ListIterator
        Entry<K, V> forward(Entry<K, V> r2) {
            return r2.mPrevious;
        }
    }

    static class Entry<K, V> implements Map.Entry<K, V> {
        final K mKey;
        Entry<K, V> mNext;
        Entry<K, V> mPrevious;
        final V mValue;

        Entry(K r1, V r2) {
            this.mKey = r1;
            this.mValue = r2;
        }

        @Override // java.util.Map.Entry
        public boolean equals(Object r6) {
            if (r6 != this) goto L6;
            return true;
        L6:
            if ((r6 instanceof Entry) == true) goto L8;
            return false;
        L8:
            Entry r1 = (Entry) r6;
            if (this.mKey.equals(r1.mKey) == false) goto L14;
            if (this.mValue.equals(r1.mValue) == false) goto L14;
            return true;
        L14:
            return false;
        }

        @Override // java.util.Map.Entry
        public K getKey() {
            return this.mKey;
        }

        @Override // java.util.Map.Entry
        public V getValue() {
            return this.mValue;
        }

        @Override // java.util.Map.Entry
        public int hashCode() {
            return this.mKey.hashCode() ^ this.mValue.hashCode();
        }

        @Override // java.util.Map.Entry
        public V setValue(V r3) {
            throw new UnsupportedOperationException("An entry modification is not supported");
        }

        public String toString() {
            return this.mKey + "=" + this.mValue;
        }
    }

    private class IteratorWithAdditions implements Iterator<Map.Entry<K, V>>, SupportRemove<K, V> {
        private boolean mBeforeStart;
        private Entry<K, V> mCurrent;
        final /* synthetic */ SafeIterableMap this$0;

        IteratorWithAdditions(SafeIterableMap r1) {
            this.this$0 = r1;
            this.mBeforeStart = true;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.mBeforeStart == false) goto L10;
            if (this.this$0.mStart == null) goto L8;
            return true;
        L8:
            return false;
        L10:
            if (this.mCurrent == null) goto L15;
            if (this.mCurrent.mNext == null) goto L15;
            return true;
        L15:
            return false;
        }

        @Override // java.util.Iterator
        public /* bridge */ /* synthetic */ Object next() {
            return next();
        }

        @Override // java.util.Iterator
        public Map.Entry<K, V> next() {
            if (this.mBeforeStart == false) goto L6;
            this.mBeforeStart = false;
            this.mCurrent = this.this$0.mStart;
        L11:
            return this.mCurrent;
        L6:
            if (this.mCurrent == null) goto L8;
            Entry<K, V> r0 = this.mCurrent.mNext;
        L9:
            this.mCurrent = r0;
            goto L11
        L8:
            r0 = null;
            goto L9
        }

        @Override // androidx.arch.core.internal.SafeIterableMap.SupportRemove
        public void supportRemove(Entry<K, V> r2) {
            if (r2 != this.mCurrent) goto L10;
            this.mCurrent = this.mCurrent.mPrevious;
            if (this.mCurrent != null) goto L7;
            boolean r0 = true;
        L8:
            this.mBeforeStart = r0;
            return;
        L7:
            r0 = false;
            goto L8
        }
    }

    private static abstract class ListIterator<K, V> implements Iterator<Map.Entry<K, V>>, SupportRemove<K, V> {
        Entry<K, V> mExpectedEnd;
        Entry<K, V> mNext;

        ListIterator(Entry<K, V> r1, Entry<K, V> r2) {
            this.mExpectedEnd = r2;
            this.mNext = r1;
        }

        private Entry<K, V> nextNode() {
            if (this.mNext != this.mExpectedEnd) goto L5;
            return null;
        L5:
            if (this.mExpectedEnd != null) goto L8;
            return null;
        L8:
            return forward(this.mNext);
        }

        abstract Entry<K, V> backward(Entry<K, V> r1);

        abstract Entry<K, V> forward(Entry<K, V> r1);

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.mNext == null) goto L5;
            return true;
        L5:
            return false;
        }

        @Override // java.util.Iterator
        public /* bridge */ /* synthetic */ Object next() {
            return next();
        }

        @Override // java.util.Iterator
        public Map.Entry<K, V> next() {
            Entry<K, V> r0 = this.mNext;
            this.mNext = nextNode();
            return r0;
        }

        @Override // androidx.arch.core.internal.SafeIterableMap.SupportRemove
        public void supportRemove(Entry<K, V> r2) {
            if (this.mExpectedEnd != r2) goto L8;
            if (r2 != this.mNext) goto L8;
            this.mNext = null;
            this.mExpectedEnd = null;
        L8:
            if (this.mExpectedEnd != r2) goto L11;
            this.mExpectedEnd = backward(this.mExpectedEnd);
        L11:
            if (this.mNext != r2) goto L14;
            this.mNext = nextNode();
            return;
        }
    }

    interface SupportRemove<K, V> {
        void supportRemove(Entry<K, V> r1);
    }

    public SafeIterableMap() {
        this.mIterators = new WeakHashMap();
        this.mSize = 0;
    }

    public Iterator<Map.Entry<K, V>> descendingIterator() {
        DescendingIterator r0 = new DescendingIterator(this.mEnd, this.mStart);
        this.mIterators.put(r0, false);
        return r0;
    }

    public Map.Entry<K, V> eldest() {
        return this.mStart;
    }

    public boolean equals(Object r9) {
        if (r9 != this) goto L6;
        return true;
    L6:
        if ((r9 instanceof SafeIterableMap) == true) goto L8;
        return false;
    L8:
        SafeIterableMap r1 = (SafeIterableMap) r9;
        if (size() == r1.size()) goto L11;
        return false;
    L11:
        Iterator<Map.Entry<K, V>> r3 = iterator();
        Iterator<Map.Entry<K, V>> r4 = r1.iterator();
    L13:
        if (r3.hasNext() == false) goto L25;
        if (r4.hasNext() == false) goto L25;
        Map.Entry<K, V> r5 = r3.next();
        Map.Entry<K, V> r6 = r4.next();
        if (r5 != null) goto L19;
        if (r6 == null) goto L19;
    L22:
        return false;
    L19:
        if (r5 == null) goto L13;
        if (r5.equals(r6) == true) goto L13;
    L25:
        if (r3.hasNext() == true) goto L30;
        if (r4.hasNext() == true) goto L30;
        return true;
    L30:
        return false;
    }

    protected Entry<K, V> get(K r3) {
        Entry<K, V> r0 = this.mStart;
    L3:
        if (r0 == null) goto L8;
        if (r0.mKey.equals(r3) == true) goto L8;
        r0 = r0.mNext;
    L8:
        return r0;
    }

    public int hashCode() {
        int r0 = 0;
        Iterator<Map.Entry<K, V>> r1 = iterator();
    L4:
        if (r1.hasNext() == false) goto L6;
        r0 = r0 + r1.next().hashCode();
        goto L4
    L6:
        return r0;
    }

    @Override // java.lang.Iterable
    public Iterator<Map.Entry<K, V>> iterator() {
        AscendingIterator r0 = new AscendingIterator(this.mStart, this.mEnd);
        this.mIterators.put(r0, false);
        return r0;
    }

    public SafeIterableMap<K, V>.IteratorWithAdditions iteratorWithAdditions() {
        SafeIterableMap<K, V>.IteratorWithAdditions r0 = new IteratorWithAdditions(this);
        this.mIterators.put(r0, false);
        return r0;
    }

    public Map.Entry<K, V> newest() {
        return this.mEnd;
    }

    protected Entry<K, V> put(K r3, V r4) {
        Entry<K, V> r0 = new Entry(r3, r4);
        this.mSize++;
        if (this.mEnd != null) goto L6;
        this.mStart = r0;
        this.mEnd = this.mStart;
        return r0;
    L6:
        this.mEnd.mNext = r0;
        r0.mPrevious = this.mEnd;
        this.mEnd = r0;
        return r0;
    }

    public V putIfAbsent(K r3, V r4) {
        Entry<K, V> r0 = get(r3);
        if (r0 != null) goto L5;
        put(r3, r4);
        return null;
    L5:
        return r0.mValue;
    }

    public V remove(K r5) {
        Entry<K, V> r0 = get(r5);
        if (r0 != null) goto L5;
        return null;
    L5:
        this.mSize--;
        if (this.mIterators.isEmpty() == true) goto L12;
        Iterator<SupportRemove<K, V>> r2 = this.mIterators.keySet().iterator();
    L9:
        if (r2.hasNext() == false) goto L12;
        r2.next().supportRemove(r0);
    L12:
        if (r0.mPrevious == null) goto L14;
        r0.mPrevious.mNext = r0.mNext;
    L16:
        if (r0.mNext == null) goto L18;
        r0.mNext.mPrevious = r0.mPrevious;
    L19:
        r0.mNext = null;
        r0.mPrevious = null;
        return r0.mValue;
    L18:
        this.mEnd = r0.mPrevious;
        goto L19
    L14:
        this.mStart = r0.mNext;
        goto L16
    }

    public int size() {
        return this.mSize;
    }

    public String toString() {
        StringBuilder r0 = new StringBuilder();
        r0.append("[");
        Iterator<Map.Entry<K, V>> r1 = iterator();
    L4:
        if (r1.hasNext() == false) goto L8;
        r0.append(r1.next().toString());
        if (r1.hasNext() == false) goto L4;
        r0.append(", ");
        goto L4
    L8:
        r0.append("]");
        return r0.toString();
    }
}
