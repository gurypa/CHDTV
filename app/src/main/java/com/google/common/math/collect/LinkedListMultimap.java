package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public class LinkedListMultimap<K, V> extends AbstractMultimap<K, V> implements ListMultimap<K, V>, Serializable {
    private static final long serialVersionUID = 0;

    @CheckForNull
    private transient Node<K, V> head;
    private transient Map<K, KeyList<K, V>> keyToKeyList;
    private transient int modCount;
    private transient int size;

    @CheckForNull
    private transient Node<K, V> tail;

    private class DistinctKeyIterator implements Iterator<K> {

        @CheckForNull
        Node<K, V> current;
        int expectedModCount;

        @CheckForNull
        Node<K, V> next;
        final Set<K> seenKeys;
        final /* synthetic */ LinkedListMultimap this$0;

        private DistinctKeyIterator(LinkedListMultimap r1) {
            this.this$0 = r1;
            this.seenKeys = Sets.newHashSetWithExpectedSize(this.this$0.keySet().size());
            this.next = LinkedListMultimap.access$200(this.this$0);
            this.expectedModCount = LinkedListMultimap.access$000(this.this$0);
        }

        /* synthetic */ DistinctKeyIterator(LinkedListMultimap r1, C06661 r2) {
            this(r1);
        }

        private void checkForConcurrentModification() {
            if (LinkedListMultimap.access$000(this.this$0) != this.expectedModCount) goto L6;
            return;
        L6:
            throw new ConcurrentModificationException();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            checkForConcurrentModification();
            if (this.next == null) goto L5;
            return true;
        L5:
            return false;
        }

        @Override // java.util.Iterator
        @ParametricNullness
        public K next() {
            checkForConcurrentModification();
            if (this.next == null) goto L12;
            this.current = this.next;
            this.seenKeys.add(this.current.key);
        L5:
            this.next = this.next.next;
            if (this.next == null) goto L10;
            if (this.seenKeys.add(this.next.key) == false) goto L5;
        L10:
            return this.current.key;
        L12:
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            checkForConcurrentModification();
            if (this.current == null) goto L5;
            boolean r0 = true;
        L6:
            Preconditions.checkState(r0, "no calls to next() since the last call to remove()");
            LinkedListMultimap.access$400(this.this$0, this.current.key);
            this.current = null;
            this.expectedModCount = LinkedListMultimap.access$000(this.this$0);
            return;
        L5:
            r0 = false;
            goto L6
        }
    }

    private static class KeyList<K, V> {
        int count;
        Node<K, V> head;
        Node<K, V> tail;

        KeyList(Node<K, V> r2) {
            this.head = r2;
            this.tail = r2;
            r2.previousSibling = null;
            r2.nextSibling = null;
            this.count = 1;
        }
    }

    static final class Node<K, V> extends AbstractMapEntry<K, V> {

        @ParametricNullness
        final K key;

        @CheckForNull
        Node<K, V> next;

        @CheckForNull
        Node<K, V> nextSibling;

        @CheckForNull
        Node<K, V> previous;

        @CheckForNull
        Node<K, V> previousSibling;

        @ParametricNullness
        V value;

        Node(@ParametricNullness K r1, @ParametricNullness V r2) {
            this.key = r1;
            this.value = r2;
        }

        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
        @ParametricNullness
        public K getKey() {
            return this.key;
        }

        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
        @ParametricNullness
        public V getValue() {
            return this.value;
        }

        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
        @ParametricNullness
        public V setValue(@ParametricNullness V r2) {
            V r0 = this.value;
            this.value = r2;
            return r0;
        }
    }

    private class NodeIterator implements ListIterator<Map.Entry<K, V>> {

        @CheckForNull
        Node<K, V> current;
        int expectedModCount;

        @CheckForNull
        Node<K, V> next;
        int nextIndex;

        @CheckForNull
        Node<K, V> previous;
        final /* synthetic */ LinkedListMultimap this$0;

        NodeIterator(LinkedListMultimap r3, int r4) {
            this.this$0 = r3;
            this.expectedModCount = LinkedListMultimap.access$000(this.this$0);
            int r0 = r3.size();
            Preconditions.checkPositionIndex(r4, r0);
            if (r4 < (r0 / 2)) goto L8;
            this.previous = LinkedListMultimap.access$100(r3);
            this.nextIndex = r0;
        L5:
            int r5 = r4 + 1;
            if (r4 >= r0) goto L12;
            previous();
            r4 = r5;
        L12:
            this.current = null;
            return;
        L8:
            this.next = LinkedListMultimap.access$200(r3);
        L9:
            int r6 = r4 - 1;
            if (r4 <= 0) goto L12;
            next();
            r4 = r6;
            goto L9
        }

        private void checkForConcurrentModification() {
            if (LinkedListMultimap.access$000(this.this$0) != this.expectedModCount) goto L6;
            return;
        L6:
            throw new ConcurrentModificationException();
        }

        @Override // java.util.ListIterator
        public /* bridge */ /* synthetic */ void add(Object r1) {
            add((Map.Entry) r1);
        }

        public void add(Map.Entry<K, V> r2) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public boolean hasNext() {
            checkForConcurrentModification();
            if (this.next == null) goto L5;
            return true;
        L5:
            return false;
        }

        @Override // java.util.ListIterator
        public boolean hasPrevious() {
            checkForConcurrentModification();
            if (this.previous == null) goto L5;
            return true;
        L5:
            return false;
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public Node<K, V> next() {
            checkForConcurrentModification();
            if (this.next == null) goto L7;
            Node<K, V> r0 = this.next;
            this.current = r0;
            this.previous = r0;
            this.next = this.next.next;
            this.nextIndex++;
            return this.current;
        L7:
            throw new NoSuchElementException();
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public /* bridge */ /* synthetic */ Object next() {
            return next();
        }

        @Override // java.util.ListIterator
        public int nextIndex() {
            return this.nextIndex;
        }

        @Override // java.util.ListIterator
        public Node<K, V> previous() {
            checkForConcurrentModification();
            if (this.previous == null) goto L7;
            Node<K, V> r0 = this.previous;
            this.current = r0;
            this.next = r0;
            this.previous = this.previous.previous;
            this.nextIndex--;
            return this.current;
        L7:
            throw new NoSuchElementException();
        }

        @Override // java.util.ListIterator
        public /* bridge */ /* synthetic */ Object previous() {
            return previous();
        }

        @Override // java.util.ListIterator
        public int previousIndex() {
            return this.nextIndex - 1;
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public void remove() {
            checkForConcurrentModification();
            if (this.current == null) goto L5;
            boolean r0 = true;
        L6:
            Preconditions.checkState(r0, "no calls to next() since the last call to remove()");
            if (this.current == this.next) goto L9;
            this.previous = this.current.previous;
            this.nextIndex--;
        L10:
            LinkedListMultimap.access$300(this.this$0, this.current);
            this.current = null;
            this.expectedModCount = LinkedListMultimap.access$000(this.this$0);
            return;
        L9:
            this.next = this.current.next;
            goto L10
        L5:
            r0 = false;
            goto L6
        }

        @Override // java.util.ListIterator
        public /* bridge */ /* synthetic */ void set(Object r1) {
            set((Map.Entry) r1);
        }

        public void set(Map.Entry<K, V> r2) {
            throw new UnsupportedOperationException();
        }

        void setValue(@ParametricNullness V r2) {
            if (this.current == null) goto L5;
            boolean r0 = true;
        L6:
            Preconditions.checkState(r0);
            this.current.value = r2;
            return;
        L5:
            r0 = false;
            goto L6
        }
    }

    private class ValueForKeyIterator implements ListIterator<V> {

        @CheckForNull
        Node<K, V> current;

        @ParametricNullness
        final K key;

        @CheckForNull
        Node<K, V> next;
        int nextIndex;

        @CheckForNull
        Node<K, V> previous;
        final /* synthetic */ LinkedListMultimap this$0;

        ValueForKeyIterator(@ParametricNullness LinkedListMultimap r2, K r3) {
            this.this$0 = r2;
            this.key = r3;
            KeyList r4 = (KeyList) LinkedListMultimap.access$500(r2).get(r3);
            if (r4 != null) goto L5;
            Node<K, V> r0 = null;
        L6:
            this.next = r0;
            return;
        L5:
            r0 = r4.head;
            goto L6
        }

        public ValueForKeyIterator(@ParametricNullness LinkedListMultimap r4, K r5, int r6) {
            this.this$0 = r4;
            KeyList r7 = (KeyList) LinkedListMultimap.access$500(r4).get(r5);
            if (r7 != null) goto L5;
            int r0 = 0;
        L6:
            Preconditions.checkPositionIndex(r6, r0);
            if (r6 < (r0 / 2)) goto L15;
            if (r7 != null) goto L10;
            Node<K, V> r1 = null;
        L11:
            this.previous = r1;
            this.nextIndex = r0;
        L12:
            int r2 = r6 + 1;
            if (r6 >= r0) goto L22;
            previous();
            r6 = r2;
        L22:
            this.key = r5;
            this.current = null;
            return;
        L10:
            r1 = r7.tail;
            goto L11
        L15:
            if (r7 != null) goto L17;
            Node<K, V> r3 = null;
        L18:
            this.next = r3;
        L19:
            int r8 = r6 - 1;
            if (r6 <= 0) goto L22;
            next();
            r6 = r8;
            goto L19
        L17:
            r3 = r7.head;
            goto L18
        L5:
            r0 = r7.count;
            goto L6
        }

        @Override // java.util.ListIterator
        public void add(@ParametricNullness V r4) {
            this.previous = LinkedListMultimap.access$600(this.this$0, this.key, r4, this.next);
            this.nextIndex++;
            this.current = null;
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public boolean hasNext() {
            if (this.next == null) goto L5;
            return true;
        L5:
            return false;
        }

        @Override // java.util.ListIterator
        public boolean hasPrevious() {
            if (this.previous == null) goto L5;
            return true;
        L5:
            return false;
        }

        @Override // java.util.ListIterator, java.util.Iterator
        @ParametricNullness
        public V next() {
            if (this.next == null) goto L7;
            Node<K, V> r0 = this.next;
            this.current = r0;
            this.previous = r0;
            this.next = this.next.nextSibling;
            this.nextIndex++;
            return this.current.value;
        L7:
            throw new NoSuchElementException();
        }

        @Override // java.util.ListIterator
        public int nextIndex() {
            return this.nextIndex;
        }

        @Override // java.util.ListIterator
        @ParametricNullness
        public V previous() {
            if (this.previous == null) goto L7;
            Node<K, V> r0 = this.previous;
            this.current = r0;
            this.next = r0;
            this.previous = this.previous.previousSibling;
            this.nextIndex--;
            return this.current.value;
        L7:
            throw new NoSuchElementException();
        }

        @Override // java.util.ListIterator
        public int previousIndex() {
            return this.nextIndex - 1;
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public void remove() {
            if (this.current == null) goto L5;
            boolean r0 = true;
        L6:
            Preconditions.checkState(r0, "no calls to next() since the last call to remove()");
            if (this.current == this.next) goto L9;
            this.previous = this.current.previousSibling;
            this.nextIndex--;
        L10:
            LinkedListMultimap.access$300(this.this$0, this.current);
            this.current = null;
            return;
        L9:
            this.next = this.current.nextSibling;
            goto L10
        L5:
            r0 = false;
            goto L6
        }

        @Override // java.util.ListIterator
        public void set(@ParametricNullness V r2) {
            if (this.current == null) goto L5;
            boolean r0 = true;
        L6:
            Preconditions.checkState(r0);
            this.current.value = r2;
            return;
        L5:
            r0 = false;
            goto L6
        }
    }

    LinkedListMultimap() {
        this(12);
    }

    private LinkedListMultimap(int r2) {
        this.keyToKeyList = Platform.newHashMapWithExpectedSize(r2);
    }

    private LinkedListMultimap(Multimap<? extends K, ? extends V> r2) {
        this(r2.keySet().size());
        putAll(r2);
    }

    static /* synthetic */ int access$000(LinkedListMultimap r1) {
        return r1.modCount;
    }

    static /* synthetic */ Node access$100(LinkedListMultimap r1) {
        return r1.tail;
    }

    static /* synthetic */ Node access$200(LinkedListMultimap r1) {
        return r1.head;
    }

    static /* synthetic */ void access$300(LinkedListMultimap r0, Node r1) {
        r0.removeNode(r1);
    }

    static /* synthetic */ void access$400(LinkedListMultimap r0, Object r1) {
        r0.removeAllNodes(r1);
    }

    static /* synthetic */ Map access$500(LinkedListMultimap r1) {
        return r1.keyToKeyList;
    }

    static /* synthetic */ Node access$600(LinkedListMultimap r1, Object r2, Object r3, Node r4) {
        return r1.addNode(r2, r3, r4);
    }

    static /* synthetic */ int access$800(LinkedListMultimap r1) {
        return r1.size;
    }

    private Node<K, V> addNode(@ParametricNullness K r5, @ParametricNullness V r6, @CheckForNull Node<K, V> r7) {
        Node<K, V> r0 = new Node(r5, r6);
        if (this.head != null) goto L5;
        this.tail = r0;
        this.head = r0;
        this.keyToKeyList.put(r5, new KeyList(r0));
        this.modCount++;
    L20:
        this.size++;
        return r0;
    L5:
        if (r7 != null) goto L11;
        ((Node) Objects.requireNonNull(this.tail)).next = r0;
        r0.previous = this.tail;
        this.tail = r0;
        KeyList<K, V> r1 = this.keyToKeyList.get(r5);
        if (r1 != null) goto L9;
        this.keyToKeyList.put(r5, new KeyList(r0));
        this.modCount++;
        goto L20
    L9:
        r1.count++;
        Node<K, V> r2 = r1.tail;
        r2.nextSibling = r0;
        r0.previousSibling = r2;
        r1.tail = r0;
        goto L20
    L11:
        KeyList r3 = (KeyList) Objects.requireNonNull(this.keyToKeyList.get(r5));
        r3.count++;
        r0.previous = r7.previous;
        r0.previousSibling = r7.previousSibling;
        r0.next = r7;
        r0.nextSibling = r7;
        if (r7.previousSibling != null) goto L14;
        r3.head = r0;
    L16:
        if (r7.previous != null) goto L18;
        this.head = r0;
    L19:
        r7.previous = r0;
        r7.previousSibling = r0;
        goto L20
    L18:
        r7.previous.next = r0;
        goto L19
    L14:
        r7.previousSibling.nextSibling = r0;
        goto L16
    }

    public static <K, V> LinkedListMultimap<K, V> create() {
        return new LinkedListMultimap();
    }

    public static <K, V> LinkedListMultimap<K, V> create(int r1) {
        return new LinkedListMultimap(r1);
    }

    public static <K, V> LinkedListMultimap<K, V> create(Multimap<? extends K, ? extends V> r1) {
        return new LinkedListMultimap(r1);
    }

    private List<V> getCopy(@ParametricNullness K r2) {
        return Collections.unmodifiableList(Lists.newArrayList(new ValueForKeyIterator(this, r2)));
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void readObject(ObjectInputStream r5) throws IOException, ClassNotFoundException {
        r5.defaultReadObject();
        this.keyToKeyList = CompactLinkedHashMap.create();
        int r0 = r5.readInt();
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L5;
        put(r5.readObject(), r5.readObject());
        r1 = r1 + 1;
        goto L3
    }

    private void removeAllNodes(@ParametricNullness K r2) {
        Iterators.clear(new ValueForKeyIterator(this, r2));
    }

    private void removeNode(Node<K, V> r4) {
        if (r4.previous == null) goto L5;
        r4.previous.next = r4.next;
    L7:
        if (r4.next == null) goto L9;
        r4.next.previous = r4.previous;
    L11:
        if (r4.previousSibling == null) goto L13;
    L15:
        KeyList r0 = (KeyList) Objects.requireNonNull(this.keyToKeyList.get(r4.key));
        r0.count--;
        if (r4.previousSibling != null) goto L18;
        r0.head = (Node) Objects.requireNonNull(r4.nextSibling);
    L20:
        if (r4.nextSibling != null) goto L22;
        r0.tail = (Node) Objects.requireNonNull(r4.previousSibling);
    L23:
        this.size--;
        return;
    L22:
        r4.nextSibling.previousSibling = r4.previousSibling;
        goto L23
    L18:
        r4.previousSibling.nextSibling = r4.nextSibling;
        goto L20
    L13:
        if (r4.nextSibling != null) goto L15;
        ((KeyList) Objects.requireNonNull(this.keyToKeyList.remove(r4.key))).count = 0;
        this.modCount++;
        goto L23
    L9:
        this.tail = r4.previous;
        goto L11
    L5:
        this.head = r4.next;
        goto L7
    }

    private void writeObject(ObjectOutputStream r4) throws IOException {
        r4.defaultWriteObject();
        r4.writeInt(size());
        Iterator<Map.Entry<K, V>> r0 = entries().iterator();
    L4:
        if (r0.hasNext() == false) goto L6;
        Map.Entry<K, V> r1 = r0.next();
        r4.writeObject(r1.getKey());
        r4.writeObject(r1.getValue());
        goto L4
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
    public /* bridge */ /* synthetic */ Map asMap() {
        return super.asMap();
    }

    @Override // com.google.common.collect.Multimap
    public void clear() {
        this.head = null;
        this.tail = null;
        this.keyToKeyList.clear();
        this.size = 0;
        this.modCount++;
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ boolean containsEntry(@CheckForNull Object r1, @CheckForNull Object r2) {
        return super.containsEntry(r1, r2);
    }

    @Override // com.google.common.collect.Multimap
    public boolean containsKey(@CheckForNull Object r2) {
        return this.keyToKeyList.containsKey(r2);
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public boolean containsValue(@CheckForNull Object r2) {
        return values().contains(r2);
    }

    @Override // com.google.common.collect.AbstractMultimap
    Map<K, Collection<V>> createAsMap() {
        return new Multimaps.AsMap(this);
    }

    @Override // com.google.common.collect.AbstractMultimap
    /* bridge */ /* synthetic */ Collection createEntries() {
        return createEntries();
    }

    @Override // com.google.common.collect.AbstractMultimap
    List<Map.Entry<K, V>> createEntries() {
        return new C1EntriesImpl(this);
    }

    @Override // com.google.common.collect.AbstractMultimap
    Set<K> createKeySet() {
        return new C1KeySetImpl(this);
    }

    @Override // com.google.common.collect.AbstractMultimap
    Multiset<K> createKeys() {
        return new Multimaps.Keys(this);
    }

    @Override // com.google.common.collect.AbstractMultimap
    /* bridge */ /* synthetic */ Collection createValues() {
        return createValues();
    }

    @Override // com.google.common.collect.AbstractMultimap
    List<V> createValues() {
        return new C1ValuesImpl(this);
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ Collection entries() {
        return entries();
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public List<Map.Entry<K, V>> entries() {
        return (List) super.entries();
    }

    @Override // com.google.common.collect.AbstractMultimap
    Iterator<Map.Entry<K, V>> entryIterator() {
        throw new AssertionError("should never be called");
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
    public /* bridge */ /* synthetic */ boolean equals(@CheckForNull Object r1) {
        return super.equals(r1);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ Collection get(@ParametricNullness Object r1) {
        return get(r1);
    }

    @Override // com.google.common.collect.Multimap
    public List<V> get(@ParametricNullness final K r2) {
        return new C06661(this, r2);
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public boolean isEmpty() {
        if (this.head != null) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ Set keySet() {
        return super.keySet();
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ Multiset keys() {
        return super.keys();
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public boolean put(@ParametricNullness K r2, @ParametricNullness V r3) {
        addNode(r2, r3, null);
        return true;
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ boolean putAll(Multimap r1) {
        return super.putAll(r1);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ boolean putAll(@ParametricNullness Object r1, Iterable r2) {
        return super.putAll(r1, r2);
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ boolean remove(@CheckForNull Object r1, @CheckForNull Object r2) {
        return super.remove(r1, r2);
    }

    @Override // com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ Collection removeAll(@CheckForNull Object r1) {
        return removeAll(r1);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.Multimap
    public List<V> removeAll(@CheckForNull Object r3) {
        List<V> r1 = getCopy(r3);
        removeAllNodes(r3);
        return r1;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ Collection replaceValues(@ParametricNullness Object r1, Iterable r2) {
        return replaceValues(r1, r2);
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public List<V> replaceValues(@ParametricNullness K r5, Iterable<? extends V> r6) {
        List<V> r0 = getCopy(r5);
        ValueForKeyIterator r1 = new ValueForKeyIterator(this, r5);
        Iterator<? extends V> r2 = r6.iterator();
    L4:
        if (r1.hasNext() == false) goto L9;
        if (r2.hasNext() == false) goto L9;
        r1.next();
        r1.set(r2.next());
    L9:
        if (r1.hasNext() == false) goto L12;
        r1.next();
        r1.remove();
    L12:
        if (r2.hasNext() == false) goto L14;
        r1.add(r2.next());
        goto L12
    L14:
        return r0;
    }

    @Override // com.google.common.collect.Multimap
    public int size() {
        return this.size;
    }

    @Override // com.google.common.collect.AbstractMultimap
    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ Collection values() {
        return values();
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public List<V> values() {
        return (List) super.values();
    }
}
