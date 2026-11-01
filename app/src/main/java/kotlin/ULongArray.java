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

/* JADX INFO: compiled from: ULongArray.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u001e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0016\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010\u0000\n\u0002\b\f\n\u0002\u0010(\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0004\b\u0087@\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u00012B\u0011\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0004\b\u0005\u0010\u0006B\u0011\b\u0001\u0012\u0006\u0010\u0007\u001a\u00020\b¢\u0006\u0004\b\u0005\u0010\tJ\u0018\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0002H\u0096\u0002¢\u0006\u0004\b\u0011\u0010\u0012J\u001d\u0010\u0013\u001a\u00020\u000f2\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001H\u0016¢\u0006\u0004\b\u0015\u0010\u0016J\u001a\u0010\u0017\u001a\u00020\u000f2\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019HÖ\u0003¢\u0006\u0004\b\u001a\u0010\u001bJ\u001b\u0010\u001c\u001a\u00020\u00022\u0006\u0010\u001d\u001a\u00020\u0004H\u0086\u0002ø\u0001\u0000¢\u0006\u0004\b\u001e\u0010\u001fJ\u0010\u0010 \u001a\u00020\u0004HÖ\u0001¢\u0006\u0004\b!\u0010\u000bJ\u000f\u0010\"\u001a\u00020\u000fH\u0016¢\u0006\u0004\b#\u0010$J\u0016\u0010%\u001a\b\u0012\u0004\u0012\u00020\u00020&H\u0096\u0002¢\u0006\u0004\b'\u0010(J \u0010)\u001a\u00020*2\u0006\u0010\u001d\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0002H\u0086\u0002¢\u0006\u0004\b,\u0010-J\u0010\u0010.\u001a\u00020/HÖ\u0001¢\u0006\u0004\b0\u00101R\u0014\u0010\u0003\u001a\u00020\u00048VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0016\u0010\u0007\u001a\u00020\b8\u0000X\u0081\u0004¢\u0006\b\n\u0000\u0012\u0004\b\f\u0010\r\u0088\u0001\u0007\u0092\u0001\u00020\b\u0082\u0002\u0004\n\u0002\b!¨\u00063"}, m421d2 = {"Lkotlin/ULongArray;", "", "Lkotlin/ULong;", MediaInformation.KEY_SIZE, "", "constructor-impl", "(I)[J", "storage", "", "([J)[J", "getSize-impl", "([J)I", "getStorage$annotations", "()V", "contains", "", "element", "contains-VKZWuLQ", "([JJ)Z", "containsAll", "elements", "containsAll-impl", "([JLjava/util/Collection;)Z", "equals", "other", "", "equals-impl", "([JLjava/lang/Object;)Z", "get", StreamInformation.KEY_INDEX, "get-s-VKNKU", "([JI)J", "hashCode", "hashCode-impl", "isEmpty", "isEmpty-impl", "([J)Z", "iterator", "", "iterator-impl", "([J)Ljava/util/Iterator;", "set", "", "value", "set-k8EXiF4", "([JIJ)V", "toString", "", "toString-impl", "([J)Ljava/lang/String;", "Iterator", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
@JvmInline
public final class ULongArray implements Collection<ULong>, KMappedMarker {
    private final long[] storage;

    /* JADX INFO: compiled from: ULongArray.kt */
    @Metadata(m420d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010(\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0016\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\t\u0010\b\u001a\u00020\tH\u0096\u0002J\u0013\u0010\n\u001a\u00020\u0002H\u0096\u0002ø\u0001\u0000¢\u0006\u0004\b\u000b\u0010\fR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b!¨\u0006\r"}, m421d2 = {"Lkotlin/ULongArray$Iterator;", "", "Lkotlin/ULong;", "array", "", "([J)V", StreamInformation.KEY_INDEX, "", "hasNext", "", "next", "next-s-VKNKU", "()J", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
    private static final class Iterator implements java.util.Iterator<ULong>, KMappedMarker {
        private final long[] array;
        private int index;

        public Iterator(long[] r2) {
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
        public /* bridge */ /* synthetic */ ULong next() {
            return ULong.m987boximpl(m1063nextsVKNKU());
        }

        /* JADX INFO: renamed from: next-s-VKNKU, reason: not valid java name */
        public long m1063nextsVKNKU() {
            if (this.index >= this.array.length) goto L7;
            long[] r0 = this.array;
            int r1 = this.index;
            this.index = r1 + 1;
            return ULong.m993constructorimpl(r0[r1]);
        L7:
            throw new NoSuchElementException(String.valueOf(this.index));
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }
    }

    private /* synthetic */ ULongArray(long[] r1) {
        this.storage = r1;
    }

    /* JADX INFO: renamed from: box-impl, reason: not valid java name */
    public static final /* synthetic */ ULongArray m1046boximpl(long[] r1) {
        return new ULongArray(r1);
    }

    /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
    public static long[] m1047constructorimpl(int r1) {
        return m1048constructorimpl(new long[r1]);
    }

    /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
    public static long[] m1048constructorimpl(long[] r1) {
        Intrinsics.checkNotNullParameter(r1, "storage");
        return r1;
    }

    /* JADX INFO: renamed from: contains-VKZWuLQ, reason: not valid java name */
    public static boolean m1049containsVKZWuLQ(long[] r1, long r2) {
        return ArraysKt.contains(r1, r2);
    }

    /* JADX INFO: renamed from: containsAll-impl, reason: not valid java name */
    public static boolean m1050containsAllimpl(long[] r11, Collection<ULong> r12) {
        Intrinsics.checkNotNullParameter(r12, "elements");
        Collection<ULong> r0 = r12;
        if (r0.isEmpty() == true) goto L23;
        java.util.Iterator<T> r2 = r0.iterator();
    L7:
        if (r2.hasNext() == false) goto L24;
        Object r4 = r2.next();
        if ((r4 instanceof ULong) == true) goto L11;
    L13:
        boolean r5 = false;
    L14:
        if (r5 == true) goto L7;
        return false;
    L11:
        if (ArraysKt.contains(r11, ((ULong) r4).m1045unboximpl()) == false) goto L13;
        r5 = true;
        goto L14
    L24:
        return true;
    L23:
        return true;
    }

    /* JADX INFO: renamed from: equals-impl, reason: not valid java name */
    public static boolean m1051equalsimpl(long[] r2, Object r3) {
        if ((r3 instanceof ULongArray) == true) goto L6;
        return false;
    L6:
        if (Intrinsics.areEqual(r2, ((ULongArray) r3).m1062unboximpl()) == true) goto L8;
        return false;
    L8:
        return true;
    }

    /* JADX INFO: renamed from: equals-impl0, reason: not valid java name */
    public static final boolean m1052equalsimpl0(long[] r1, long[] r2) {
        return Intrinsics.areEqual(r1, r2);
    }

    /* JADX INFO: renamed from: get-s-VKNKU, reason: not valid java name */
    public static final long m1053getsVKNKU(long[] r2, int r3) {
        return ULong.m993constructorimpl(r2[r3]);
    }

    /* JADX INFO: renamed from: getSize-impl, reason: not valid java name */
    public static int m1054getSizeimpl(long[] r1) {
        return r1.length;
    }

    public static /* synthetic */ void getStorage$annotations() {
    }

    /* JADX INFO: renamed from: hashCode-impl, reason: not valid java name */
    public static int m1055hashCodeimpl(long[] r1) {
        return Arrays.hashCode(r1);
    }

    /* JADX INFO: renamed from: isEmpty-impl, reason: not valid java name */
    public static boolean m1056isEmptyimpl(long[] r1) {
        if (r1.length != 0) goto L5;
        return true;
    L5:
        return false;
    }

    /* JADX INFO: renamed from: iterator-impl, reason: not valid java name */
    public static java.util.Iterator<ULong> m1057iteratorimpl(long[] r1) {
        return new Iterator(r1);
    }

    /* JADX INFO: renamed from: set-k8EXiF4, reason: not valid java name */
    public static final void m1058setk8EXiF4(long[] r0, int r1, long r2) {
        r0[r1] = r2;
    }

    /* JADX INFO: renamed from: toString-impl, reason: not valid java name */
    public static String m1059toStringimpl(long[] r2) {
        return "ULongArray(storage=" + Arrays.toString(r2) + ')';
    }

    @Override // java.util.Collection
    public /* bridge */ /* synthetic */ boolean add(ULong r3) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    /* JADX INFO: renamed from: add-VKZWuLQ, reason: not valid java name */
    public boolean m1060addVKZWuLQ(long r3) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public boolean addAll(Collection<? extends ULong> r3) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public void clear() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public final /* bridge */ boolean contains(Object r3) {
        if ((r3 instanceof ULong) == true) goto L7;
        return false;
    L7:
        return m1061containsVKZWuLQ(((ULong) r3).m1045unboximpl());
    }

    /* JADX INFO: renamed from: contains-VKZWuLQ, reason: not valid java name */
    public boolean m1061containsVKZWuLQ(long r2) {
        return m1049containsVKZWuLQ(this.storage, r2);
    }

    @Override // java.util.Collection
    public boolean containsAll(Collection<? extends Object> r2) {
        Intrinsics.checkNotNullParameter(r2, "elements");
        return m1050containsAllimpl(this.storage, r2);
    }

    @Override // java.util.Collection
    public boolean equals(Object r2) {
        return m1051equalsimpl(this.storage, r2);
    }

    public int getSize() {
        return m1054getSizeimpl(this.storage);
    }

    @Override // java.util.Collection
    public int hashCode() {
        return m1055hashCodeimpl(this.storage);
    }

    @Override // java.util.Collection
    public boolean isEmpty() {
        return m1056isEmptyimpl(this.storage);
    }

    @Override // java.util.Collection, java.lang.Iterable
    public java.util.Iterator<ULong> iterator() {
        return m1057iteratorimpl(this.storage);
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
        return m1059toStringimpl(this.storage);
    }

    /* JADX INFO: renamed from: unbox-impl, reason: not valid java name */
    public final /* synthetic */ long[] m1062unboximpl() {
        return this.storage;
    }
}
