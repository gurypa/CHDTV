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

/* JADX INFO: compiled from: UIntArray.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u001e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0015\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010\u0000\n\u0002\b\f\n\u0002\u0010(\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0004\b\u0087@\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u00012B\u0011\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0004\b\u0005\u0010\u0006B\u0011\b\u0001\u0012\u0006\u0010\u0007\u001a\u00020\b¢\u0006\u0004\b\u0005\u0010\tJ\u0018\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0002H\u0096\u0002¢\u0006\u0004\b\u0011\u0010\u0012J\u001d\u0010\u0013\u001a\u00020\u000f2\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001H\u0016¢\u0006\u0004\b\u0015\u0010\u0016J\u001a\u0010\u0017\u001a\u00020\u000f2\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019HÖ\u0003¢\u0006\u0004\b\u001a\u0010\u001bJ\u001b\u0010\u001c\u001a\u00020\u00022\u0006\u0010\u001d\u001a\u00020\u0004H\u0086\u0002ø\u0001\u0000¢\u0006\u0004\b\u001e\u0010\u001fJ\u0010\u0010 \u001a\u00020\u0004HÖ\u0001¢\u0006\u0004\b!\u0010\u000bJ\u000f\u0010\"\u001a\u00020\u000fH\u0016¢\u0006\u0004\b#\u0010$J\u0016\u0010%\u001a\b\u0012\u0004\u0012\u00020\u00020&H\u0096\u0002¢\u0006\u0004\b'\u0010(J \u0010)\u001a\u00020*2\u0006\u0010\u001d\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0002H\u0086\u0002¢\u0006\u0004\b,\u0010-J\u0010\u0010.\u001a\u00020/HÖ\u0001¢\u0006\u0004\b0\u00101R\u0014\u0010\u0003\u001a\u00020\u00048VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0016\u0010\u0007\u001a\u00020\b8\u0000X\u0081\u0004¢\u0006\b\n\u0000\u0012\u0004\b\f\u0010\r\u0088\u0001\u0007\u0092\u0001\u00020\b\u0082\u0002\u0004\n\u0002\b!¨\u00063"}, m421d2 = {"Lkotlin/UIntArray;", "", "Lkotlin/UInt;", MediaInformation.KEY_SIZE, "", "constructor-impl", "(I)[I", "storage", "", "([I)[I", "getSize-impl", "([I)I", "getStorage$annotations", "()V", "contains", "", "element", "contains-WZ4Q5Ns", "([II)Z", "containsAll", "elements", "containsAll-impl", "([ILjava/util/Collection;)Z", "equals", "other", "", "equals-impl", "([ILjava/lang/Object;)Z", "get", StreamInformation.KEY_INDEX, "get-pVg5ArA", "([II)I", "hashCode", "hashCode-impl", "isEmpty", "isEmpty-impl", "([I)Z", "iterator", "", "iterator-impl", "([I)Ljava/util/Iterator;", "set", "", "value", "set-VXSXFK8", "([III)V", "toString", "", "toString-impl", "([I)Ljava/lang/String;", "Iterator", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
@JvmInline
public final class UIntArray implements Collection<UInt>, KMappedMarker {
    private final int[] storage;

    /* JADX INFO: compiled from: UIntArray.kt */
    @Metadata(m420d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010(\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\t\u0010\b\u001a\u00020\tH\u0096\u0002J\u0013\u0010\n\u001a\u00020\u0002H\u0096\u0002ø\u0001\u0000¢\u0006\u0004\b\u000b\u0010\fR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b!¨\u0006\r"}, m421d2 = {"Lkotlin/UIntArray$Iterator;", "", "Lkotlin/UInt;", "array", "", "([I)V", StreamInformation.KEY_INDEX, "", "hasNext", "", "next", "next-pVg5ArA", "()I", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
    private static final class Iterator implements java.util.Iterator<UInt>, KMappedMarker {
        private final int[] array;
        private int index;

        public Iterator(int[] r2) {
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
        public /* bridge */ /* synthetic */ UInt next() {
            return UInt.m908boximpl(m984nextpVg5ArA());
        }

        /* JADX INFO: renamed from: next-pVg5ArA, reason: not valid java name */
        public int m984nextpVg5ArA() {
            if (this.index >= this.array.length) goto L7;
            int[] r0 = this.array;
            int r1 = this.index;
            this.index = r1 + 1;
            return UInt.m914constructorimpl(r0[r1]);
        L7:
            throw new NoSuchElementException(String.valueOf(this.index));
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }
    }

    private /* synthetic */ UIntArray(int[] r1) {
        this.storage = r1;
    }

    /* JADX INFO: renamed from: box-impl, reason: not valid java name */
    public static final /* synthetic */ UIntArray m967boximpl(int[] r1) {
        return new UIntArray(r1);
    }

    /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
    public static int[] m968constructorimpl(int r1) {
        return m969constructorimpl(new int[r1]);
    }

    /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
    public static int[] m969constructorimpl(int[] r1) {
        Intrinsics.checkNotNullParameter(r1, "storage");
        return r1;
    }

    /* JADX INFO: renamed from: contains-WZ4Q5Ns, reason: not valid java name */
    public static boolean m970containsWZ4Q5Ns(int[] r1, int r2) {
        return ArraysKt.contains(r1, r2);
    }

    /* JADX INFO: renamed from: containsAll-impl, reason: not valid java name */
    public static boolean m971containsAllimpl(int[] r9, Collection<UInt> r10) {
        Intrinsics.checkNotNullParameter(r10, "elements");
        Collection<UInt> r0 = r10;
        if (r0.isEmpty() == true) goto L23;
        java.util.Iterator<T> r2 = r0.iterator();
    L7:
        if (r2.hasNext() == false) goto L24;
        Object r4 = r2.next();
        if ((r4 instanceof UInt) == true) goto L11;
    L13:
        boolean r5 = false;
    L14:
        if (r5 == true) goto L7;
        return false;
    L11:
        if (ArraysKt.contains(r9, ((UInt) r4).m966unboximpl()) == false) goto L13;
        r5 = true;
        goto L14
    L24:
        return true;
    L23:
        return true;
    }

    /* JADX INFO: renamed from: equals-impl, reason: not valid java name */
    public static boolean m972equalsimpl(int[] r2, Object r3) {
        if ((r3 instanceof UIntArray) == true) goto L6;
        return false;
    L6:
        if (Intrinsics.areEqual(r2, ((UIntArray) r3).m983unboximpl()) == true) goto L8;
        return false;
    L8:
        return true;
    }

    /* JADX INFO: renamed from: equals-impl0, reason: not valid java name */
    public static final boolean m973equalsimpl0(int[] r1, int[] r2) {
        return Intrinsics.areEqual(r1, r2);
    }

    /* JADX INFO: renamed from: get-pVg5ArA, reason: not valid java name */
    public static final int m974getpVg5ArA(int[] r1, int r2) {
        return UInt.m914constructorimpl(r1[r2]);
    }

    /* JADX INFO: renamed from: getSize-impl, reason: not valid java name */
    public static int m975getSizeimpl(int[] r1) {
        return r1.length;
    }

    public static /* synthetic */ void getStorage$annotations() {
    }

    /* JADX INFO: renamed from: hashCode-impl, reason: not valid java name */
    public static int m976hashCodeimpl(int[] r1) {
        return Arrays.hashCode(r1);
    }

    /* JADX INFO: renamed from: isEmpty-impl, reason: not valid java name */
    public static boolean m977isEmptyimpl(int[] r1) {
        if (r1.length != 0) goto L5;
        return true;
    L5:
        return false;
    }

    /* JADX INFO: renamed from: iterator-impl, reason: not valid java name */
    public static java.util.Iterator<UInt> m978iteratorimpl(int[] r1) {
        return new Iterator(r1);
    }

    /* JADX INFO: renamed from: set-VXSXFK8, reason: not valid java name */
    public static final void m979setVXSXFK8(int[] r0, int r1, int r2) {
        r0[r1] = r2;
    }

    /* JADX INFO: renamed from: toString-impl, reason: not valid java name */
    public static String m980toStringimpl(int[] r2) {
        return "UIntArray(storage=" + Arrays.toString(r2) + ')';
    }

    @Override // java.util.Collection
    public /* bridge */ /* synthetic */ boolean add(UInt r3) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    /* JADX INFO: renamed from: add-WZ4Q5Ns, reason: not valid java name */
    public boolean m981addWZ4Q5Ns(int r3) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public boolean addAll(Collection<? extends UInt> r3) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public void clear() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public final /* bridge */ boolean contains(Object r2) {
        if ((r2 instanceof UInt) == true) goto L7;
        return false;
    L7:
        return m982containsWZ4Q5Ns(((UInt) r2).m966unboximpl());
    }

    /* JADX INFO: renamed from: contains-WZ4Q5Ns, reason: not valid java name */
    public boolean m982containsWZ4Q5Ns(int r2) {
        return m970containsWZ4Q5Ns(this.storage, r2);
    }

    @Override // java.util.Collection
    public boolean containsAll(Collection<? extends Object> r2) {
        Intrinsics.checkNotNullParameter(r2, "elements");
        return m971containsAllimpl(this.storage, r2);
    }

    @Override // java.util.Collection
    public boolean equals(Object r2) {
        return m972equalsimpl(this.storage, r2);
    }

    public int getSize() {
        return m975getSizeimpl(this.storage);
    }

    @Override // java.util.Collection
    public int hashCode() {
        return m976hashCodeimpl(this.storage);
    }

    @Override // java.util.Collection
    public boolean isEmpty() {
        return m977isEmptyimpl(this.storage);
    }

    @Override // java.util.Collection, java.lang.Iterable
    public java.util.Iterator<UInt> iterator() {
        return m978iteratorimpl(this.storage);
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
        return m980toStringimpl(this.storage);
    }

    /* JADX INFO: renamed from: unbox-impl, reason: not valid java name */
    public final /* synthetic */ int[] m983unboximpl() {
        return this.storage;
    }
}
