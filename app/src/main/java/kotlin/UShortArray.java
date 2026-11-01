package kotlin;

import com.arthenica.ffmpegkit.MediaInformation;
import com.arthenica.ffmpegkit.StreamInformation;
import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import kotlin.collections.ArraysKt;
import kotlin.jvm.JvmInline;
import kotlin.jvm.internal.CollectionToArray;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;

/* JADX INFO: compiled from: UShortArray.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u001e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0017\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010\u0000\n\u0002\b\f\n\u0002\u0010(\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0004\b\u0087@\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u00012B\u0011\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0004\b\u0005\u0010\u0006B\u0011\b\u0001\u0012\u0006\u0010\u0007\u001a\u00020\b¢\u0006\u0004\b\u0005\u0010\tJ\u0018\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0002H\u0096\u0002¢\u0006\u0004\b\u0011\u0010\u0012J\u001d\u0010\u0013\u001a\u00020\u000f2\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001H\u0016¢\u0006\u0004\b\u0015\u0010\u0016J\u001a\u0010\u0017\u001a\u00020\u000f2\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019HÖ\u0003¢\u0006\u0004\b\u001a\u0010\u001bJ\u001b\u0010\u001c\u001a\u00020\u00022\u0006\u0010\u001d\u001a\u00020\u0004H\u0086\u0002ø\u0001\u0000¢\u0006\u0004\b\u001e\u0010\u001fJ\u0010\u0010 \u001a\u00020\u0004HÖ\u0001¢\u0006\u0004\b!\u0010\u000bJ\u000f\u0010\"\u001a\u00020\u000fH\u0016¢\u0006\u0004\b#\u0010$J\u0016\u0010%\u001a\b\u0012\u0004\u0012\u00020\u00020&H\u0096\u0002¢\u0006\u0004\b'\u0010(J \u0010)\u001a\u00020*2\u0006\u0010\u001d\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0002H\u0086\u0002¢\u0006\u0004\b,\u0010-J\u0010\u0010.\u001a\u00020/HÖ\u0001¢\u0006\u0004\b0\u00101R\u0014\u0010\u0003\u001a\u00020\u00048VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0016\u0010\u0007\u001a\u00020\b8\u0000X\u0081\u0004¢\u0006\b\n\u0000\u0012\u0004\b\f\u0010\r\u0088\u0001\u0007\u0092\u0001\u00020\b\u0082\u0002\u0004\n\u0002\b!¨\u00063"}, m421d2 = {"Lkotlin/UShortArray;", "", "Lkotlin/UShort;", MediaInformation.KEY_SIZE, "", "constructor-impl", "(I)[S", "storage", "", "([S)[S", "getSize-impl", "([S)I", "getStorage$annotations", "()V", "contains", "", "element", "contains-xj2QHRw", "([SS)Z", "containsAll", "elements", "containsAll-impl", "([SLjava/util/Collection;)Z", "equals", "other", "", "equals-impl", "([SLjava/lang/Object;)Z", "get", StreamInformation.KEY_INDEX, "get-Mh2AYeg", "([SI)S", "hashCode", "hashCode-impl", "isEmpty", "isEmpty-impl", "([S)Z", "iterator", "", "iterator-impl", "([S)Ljava/util/Iterator;", "set", "", "value", "set-01HTLdE", "([SIS)V", "toString", "", "toString-impl", "([S)Ljava/lang/String;", "Iterator", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
@JvmInline
public final class UShortArray implements Collection<UShort>, KMappedMarker {
    private final short[] storage;

    /* JADX INFO: compiled from: UShortArray.kt */
    @Metadata(m420d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010(\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0017\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\t\u0010\b\u001a\u00020\tH\u0096\u0002J\u0013\u0010\n\u001a\u00020\u0002H\u0096\u0002ø\u0001\u0000¢\u0006\u0004\b\u000b\u0010\fR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b!¨\u0006\r"}, m421d2 = {"Lkotlin/UShortArray$Iterator;", "", "Lkotlin/UShort;", "array", "", "([S)V", StreamInformation.KEY_INDEX, "", "hasNext", "", "next", "next-Mh2AYeg", "()S", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
    private static final class Iterator implements java.util.Iterator<UShort>, KMappedMarker {
        private final short[] array;
        private int index;

        public Iterator(short[] r2) {
            Intrinsics.checkNotNullParameter(r2, "array");
            this.array = r2;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.index >= this.array.length) goto L5;
            return true;
        L5:
            return false;
        }

        @Override // java.util.Iterator
        public /* bridge */ /* synthetic */ UShort next() {
            return UShort.m1094boximpl(m1168nextMh2AYeg());
        }

        /* JADX INFO: renamed from: next-Mh2AYeg, reason: not valid java name */
        public short m1168nextMh2AYeg() {
            if (this.index >= this.array.length) goto L7;
            short[] r0 = this.array;
            int r1 = this.index;
            this.index = r1 + 1;
            return UShort.m1100constructorimpl(r0[r1]);
        L7:
            throw new NoSuchElementException(String.valueOf(this.index));
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }
    }

    private /* synthetic */ UShortArray(short[] r1) {
        this.storage = r1;
    }

    /* JADX INFO: renamed from: box-impl, reason: not valid java name */
    public static final /* synthetic */ UShortArray m1151boximpl(short[] r1) {
        return new UShortArray(r1);
    }

    /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
    public static short[] m1152constructorimpl(int r1) {
        return m1153constructorimpl(new short[r1]);
    }

    /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
    public static short[] m1153constructorimpl(short[] r1) {
        Intrinsics.checkNotNullParameter(r1, "storage");
        return r1;
    }

    /* JADX INFO: renamed from: contains-xj2QHRw, reason: not valid java name */
    public static boolean m1154containsxj2QHRw(short[] r1, short r2) {
        return ArraysKt.contains(r1, r2);
    }

    /* JADX INFO: renamed from: containsAll-impl, reason: not valid java name */
    public static boolean m1155containsAllimpl(short[] r9, Collection<UShort> r10) {
        Intrinsics.checkNotNullParameter(r10, "elements");
        Collection<UShort> r0 = r10;
        if (r0.isEmpty() == true) goto L23;
        java.util.Iterator<T> r2 = r0.iterator();
    L7:
        if (r2.hasNext() == false) goto L24;
        Object r4 = r2.next();
        if ((r4 instanceof UShort) == true) goto L11;
    L13:
        boolean r5 = false;
    L14:
        if (r5 == true) goto L7;
        return false;
    L11:
        if (ArraysKt.contains(r9, ((UShort) r4).m1150unboximpl()) == false) goto L13;
        r5 = true;
        goto L14
    L24:
        return true;
    L23:
        return true;
    }

    /* JADX INFO: renamed from: equals-impl, reason: not valid java name */
    public static boolean m1156equalsimpl(short[] r2, Object r3) {
        if ((r3 instanceof UShortArray) == true) goto L6;
        return false;
    L6:
        if (Intrinsics.areEqual(r2, ((UShortArray) r3).m1167unboximpl()) == true) goto L8;
        return false;
    L8:
        return true;
    }

    /* JADX INFO: renamed from: equals-impl0, reason: not valid java name */
    public static final boolean m1157equalsimpl0(short[] r1, short[] r2) {
        return Intrinsics.areEqual(r1, r2);
    }

    /* JADX INFO: renamed from: get-Mh2AYeg, reason: not valid java name */
    public static final short m1158getMh2AYeg(short[] r1, int r2) {
        return UShort.m1100constructorimpl(r1[r2]);
    }

    /* JADX INFO: renamed from: getSize-impl, reason: not valid java name */
    public static int m1159getSizeimpl(short[] r1) {
        return r1.length;
    }

    public static /* synthetic */ void getStorage$annotations() {
    }

    /* JADX INFO: renamed from: hashCode-impl, reason: not valid java name */
    public static int m1160hashCodeimpl(short[] r1) {
        return Arrays.hashCode(r1);
    }

    /* JADX INFO: renamed from: isEmpty-impl, reason: not valid java name */
    public static boolean m1161isEmptyimpl(short[] r1) {
        if (r1.length != 0) goto L5;
        return true;
    L5:
        return false;
    }

    /* JADX INFO: renamed from: iterator-impl, reason: not valid java name */
    public static java.util.Iterator<UShort> m1162iteratorimpl(short[] r1) {
        return new Iterator(r1);
    }

    /* JADX INFO: renamed from: set-01HTLdE, reason: not valid java name */
    public static final void m1163set01HTLdE(short[] r0, int r1, short r2) {
        r0[r1] = r2;
    }

    /* JADX INFO: renamed from: toString-impl, reason: not valid java name */
    public static String m1164toStringimpl(short[] r2) {
        return "UShortArray(storage=" + Arrays.toString(r2) + ')';
    }

    @Override // java.util.Collection
    public /* bridge */ /* synthetic */ boolean add(UShort r3) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    /* JADX INFO: renamed from: add-xj2QHRw, reason: not valid java name */
    public boolean m1165addxj2QHRw(short r3) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public boolean addAll(Collection<? extends UShort> r3) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public void clear() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public final /* bridge */ boolean contains(Object r2) {
        if ((r2 instanceof UShort) == true) goto L7;
        return false;
    L7:
        return m1166containsxj2QHRw(((UShort) r2).m1150unboximpl());
    }

    /* JADX INFO: renamed from: contains-xj2QHRw, reason: not valid java name */
    public boolean m1166containsxj2QHRw(short r2) {
        return m1154containsxj2QHRw(this.storage, r2);
    }

    @Override // java.util.Collection
    public boolean containsAll(Collection<? extends Object> r2) {
        Intrinsics.checkNotNullParameter(r2, "elements");
        return m1155containsAllimpl(this.storage, r2);
    }

    @Override // java.util.Collection
    public boolean equals(Object r2) {
        return m1156equalsimpl(this.storage, r2);
    }

    public int getSize() {
        return m1159getSizeimpl(this.storage);
    }

    @Override // java.util.Collection
    public int hashCode() {
        return m1160hashCodeimpl(this.storage);
    }

    @Override // java.util.Collection
    public boolean isEmpty() {
        return m1161isEmptyimpl(this.storage);
    }

    @Override // java.util.Collection, java.lang.Iterable
    public java.util.Iterator<UShort> iterator() {
        return m1162iteratorimpl(this.storage);
    }

    @Override // java.util.Collection
    public boolean remove(Object r3) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public boolean removeAll(Collection<? extends Object> r3) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public boolean retainAll(Collection<? extends Object> r3) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public /* bridge */ /* synthetic */ int size() {
        return getSize();
    }

    @Override // java.util.Collection
    public Object[] toArray() {
        return CollectionToArray.toArray(this);
    }

    @Override // java.util.Collection
    public <T> T[] toArray(T[] r2) {
        Intrinsics.checkNotNullParameter(r2, "array");
        return (T[]) CollectionToArray.toArray(this, r2);
    }

    public String toString() {
        return m1164toStringimpl(this.storage);
    }

    /* JADX INFO: renamed from: unbox-impl, reason: not valid java name */
    public final /* synthetic */ short[] m1167unboximpl() {
        return this.storage;
    }
}
