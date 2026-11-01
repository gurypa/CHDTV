package com.google.common.collect;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class Serialization {

    /* JADX INFO: renamed from: com.google.common.collect.Serialization$1 */
    static /* synthetic */ class C07231 {
    }

    static final class FieldSetter<T> {
        private final Field field;

        private FieldSetter(Field r2) {
            this.field = r2;
            r2.setAccessible(true);
        }

        /* synthetic */ FieldSetter(Field r1, C07231 r2) {
            this(r1);
        }

        void set(T r3, int r4) {
            this.field.set(r3, Integer.valueOf(r4));     // Catch: IllegalAccessException -> L4
            return;
        L4:
            e = move-exception;
            throw new AssertionError(e);
        }

        void set(T r3, Object r4) {
            this.field.set(r3, r4);     // Catch: IllegalAccessException -> L4
            return;
        L4:
            e = move-exception;
            throw new AssertionError(e);
        }
    }

    private Serialization() {
    }

    static <T> FieldSetter<T> getFieldSetter(Class<T> r3, String r4) {
        return new FieldSetter(r3.getDeclaredField(r4), null);
    L4:
        e = move-exception;
        throw new AssertionError(e);
    }

    static <K, V> void populateMap(Map<K, V> r1, ObjectInputStream r2) throws IOException, ClassNotFoundException {
        populateMap(r1, r2, r2.readInt());
    }

    /* JADX WARN: Multi-variable type inference failed */
    static <K, V> void populateMap(Map<K, V> r3, ObjectInputStream r4, int r5) throws IOException, ClassNotFoundException {
        int r0 = 0;
    L3:
        if (r0 >= r5) goto L5;
        r3.put(r4.readObject(), r4.readObject());
        r0 = r0 + 1;
        goto L3
    }

    static <K, V> void populateMultimap(Multimap<K, V> r1, ObjectInputStream r2) throws IOException, ClassNotFoundException {
        populateMultimap(r1, r2, r2.readInt());
    }

    /* JADX WARN: Multi-variable type inference failed */
    static <K, V> void populateMultimap(Multimap<K, V> r6, ObjectInputStream r7, int r8) throws IOException, ClassNotFoundException {
        int r0 = 0;
    L3:
        if (r0 >= r8) goto L8;
        Collection r2 = r6.get(r7.readObject());
        int r3 = r7.readInt();
        int r4 = 0;
    L5:
        if (r4 >= r3) goto L7;
        r2.add(r7.readObject());
        r4 = r4 + 1;
        goto L5
    L7:
        r0 = r0 + 1;
        goto L3
    }

    static <E> void populateMultiset(Multiset<E> r1, ObjectInputStream r2) throws IOException, ClassNotFoundException {
        populateMultiset(r1, r2, r2.readInt());
    }

    /* JADX WARN: Multi-variable type inference failed */
    static <E> void populateMultiset(Multiset<E> r3, ObjectInputStream r4, int r5) throws IOException, ClassNotFoundException {
        int r0 = 0;
    L3:
        if (r0 >= r5) goto L5;
        r3.add(r4.readObject(), r4.readInt());
        r0 = r0 + 1;
        goto L3
    }

    static int readCount(ObjectInputStream r1) throws IOException {
        return r1.readInt();
    }

    static <K, V> void writeMap(Map<K, V> r3, ObjectOutputStream r4) throws IOException {
        r4.writeInt(r3.size());
        Iterator<Map.Entry<K, V>> r0 = r3.entrySet().iterator();
    L4:
        if (r0.hasNext() == false) goto L6;
        Map.Entry<K, V> r1 = r0.next();
        r4.writeObject(r1.getKey());
        r4.writeObject(r1.getValue());
        goto L4
    }

    static <K, V> void writeMultimap(Multimap<K, V> r4, ObjectOutputStream r5) throws IOException {
        r5.writeInt(r4.asMap().size());
        Iterator<Map.Entry<K, Collection<V>>> r0 = r4.asMap().entrySet().iterator();
    L4:
        if (r0.hasNext() == false) goto L10;
        Map.Entry<K, Collection<V>> r1 = r0.next();
        r5.writeObject(r1.getKey());
        r5.writeInt(r1.getValue().size());
        Iterator<V> r2 = r1.getValue().iterator();
    L7:
        if (r2.hasNext() == false) goto L4;
        r5.writeObject(r2.next());
        goto L7
    }

    static <E> void writeMultiset(Multiset<E> r4, ObjectOutputStream r5) throws IOException {
        r5.writeInt(r4.entrySet().size());
        Iterator<Multiset.Entry<E>> r1 = r4.entrySet().iterator();
    L4:
        if (r1.hasNext() == false) goto L6;
        Multiset.Entry<E> r2 = r1.next();
        r5.writeObject(r2.getElement());
        r5.writeInt(r2.getCount());
        goto L4
    }
}
