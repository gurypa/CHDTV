package kotlin.collections;

import androidx.exifinterface.media.ExifInterface;
import com.arthenica.ffmpegkit.MediaInformation;
import com.arthenica.ffmpegkit.StreamInformation;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;

/* JADX INFO: compiled from: AbstractList.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u00008\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\b\n\u0002\u0010(\n\u0002\b\u0002\n\u0002\u0010*\n\u0002\b\b\b'\u0018\u0000 \u001c*\u0006\b\u0000\u0010\u0001 \u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003:\u0004\u001c\u001d\u001e\u001fB\u0007\b\u0004¢\u0006\u0002\u0010\u0004J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u0096\u0002J\u0016\u0010\r\u001a\u00028\u00002\u0006\u0010\u000e\u001a\u00020\u0006H¦\u0002¢\u0006\u0002\u0010\u000fJ\b\u0010\u0010\u001a\u00020\u0006H\u0016J\u0015\u0010\u0011\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010\u0013J\u000f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00028\u00000\u0015H\u0096\u0002J\u0015\u0010\u0016\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010\u0013J\u000e\u0010\u0017\u001a\b\u0012\u0004\u0012\u00028\u00000\u0018H\u0016J\u0016\u0010\u0017\u001a\b\u0012\u0004\u0012\u00028\u00000\u00182\u0006\u0010\u000e\u001a\u00020\u0006H\u0016J\u001e\u0010\u0019\u001a\b\u0012\u0004\u0012\u00028\u00000\u00032\u0006\u0010\u001a\u001a\u00020\u00062\u0006\u0010\u001b\u001a\u00020\u0006H\u0016R\u0012\u0010\u0005\u001a\u00020\u0006X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0007\u0010\b¨\u0006 "}, m421d2 = {"Lkotlin/collections/AbstractList;", ExifInterface.LONGITUDE_EAST, "Lkotlin/collections/AbstractCollection;", "", "()V", MediaInformation.KEY_SIZE, "", "getSize", "()I", "equals", "", "other", "", "get", StreamInformation.KEY_INDEX, "(I)Ljava/lang/Object;", "hashCode", "indexOf", "element", "(Ljava/lang/Object;)I", "iterator", "", "lastIndexOf", "listIterator", "", "subList", "fromIndex", "toIndex", "Companion", "IteratorImpl", "ListIteratorImpl", "SubList", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public abstract class AbstractList<E> extends AbstractCollection<E> implements List<E>, KMappedMarker {
    public static final Companion Companion = null;
    private static final int maxArraySize = 2147483639;

    /* JADX INFO: compiled from: AbstractList.kt */
    @Metadata(m420d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0012\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u001e\n\u0002\b\u0005\b\u0080\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J%\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\u0004H\u0000¢\u0006\u0002\b\nJ\u001d\u0010\u000b\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\u0004H\u0000¢\u0006\u0002\b\rJ\u001d\u0010\u000e\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\u0004H\u0000¢\u0006\u0002\b\u000fJ%\u0010\u0010\u001a\u00020\u00062\u0006\u0010\u0011\u001a\u00020\u00042\u0006\u0010\u0012\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\u0004H\u0000¢\u0006\u0002\b\u0013J\u001d\u0010\u0014\u001a\u00020\u00042\u0006\u0010\u0015\u001a\u00020\u00042\u0006\u0010\u0016\u001a\u00020\u0004H\u0000¢\u0006\u0002\b\u0017J%\u0010\u0018\u001a\u00020\u00192\n\u0010\u001a\u001a\u0006\u0012\u0002\b\u00030\u001b2\n\u0010\u001c\u001a\u0006\u0012\u0002\b\u00030\u001bH\u0000¢\u0006\u0002\b\u001dJ\u0019\u0010\u001e\u001a\u00020\u00042\n\u0010\u001a\u001a\u0006\u0012\u0002\b\u00030\u001bH\u0000¢\u0006\u0002\b\u001fR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000¨\u0006 "}, m421d2 = {"Lkotlin/collections/AbstractList$Companion;", "", "()V", "maxArraySize", "", "checkBoundsIndexes", "", "startIndex", "endIndex", MediaInformation.KEY_SIZE, "checkBoundsIndexes$kotlin_stdlib", "checkElementIndex", StreamInformation.KEY_INDEX, "checkElementIndex$kotlin_stdlib", "checkPositionIndex", "checkPositionIndex$kotlin_stdlib", "checkRangeIndexes", "fromIndex", "toIndex", "checkRangeIndexes$kotlin_stdlib", "newCapacity", "oldCapacity", "minCapacity", "newCapacity$kotlin_stdlib", "orderedEquals", "", "c", "", "other", "orderedEquals$kotlin_stdlib", "orderedHashCode", "orderedHashCode$kotlin_stdlib", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker r1) {
            this();
        }

        public final void checkBoundsIndexes$kotlin_stdlib(int r4, int r5, int r6) {
            if (r4 < 0) goto L10;
            if (r5 > r6) goto L10;
            if (r4 > r5) goto L8;
            return;
        L8:
            throw new IllegalArgumentException("startIndex: " + r4 + " > endIndex: " + r5);
        L10:
            throw new IndexOutOfBoundsException("startIndex: " + r4 + ", endIndex: " + r5 + ", size: " + r6);
        }

        public final void checkElementIndex$kotlin_stdlib(int r4, int r5) {
            if (r4 < 0) goto L6;
            if (r4 >= r5) goto L6;
            return;
        L6:
            throw new IndexOutOfBoundsException("index: " + r4 + ", size: " + r5);
        }

        public final void checkPositionIndex$kotlin_stdlib(int r4, int r5) {
            if (r4 < 0) goto L6;
            if (r4 > r5) goto L6;
            return;
        L6:
            throw new IndexOutOfBoundsException("index: " + r4 + ", size: " + r5);
        }

        public final void checkRangeIndexes$kotlin_stdlib(int r4, int r5, int r6) {
            if (r4 < 0) goto L10;
            if (r5 > r6) goto L10;
            if (r4 > r5) goto L8;
            return;
        L8:
            throw new IllegalArgumentException("fromIndex: " + r4 + " > toIndex: " + r5);
        L10:
            throw new IndexOutOfBoundsException("fromIndex: " + r4 + ", toIndex: " + r5 + ", size: " + r6);
        }

        public final int newCapacity$kotlin_stdlib(int r4, int r5) {
            int r0 = (r4 >> 1) + r4;
            if ((r0 - r5) >= 0) goto L5;
            r0 = r5;
        L5:
            int r1 = AbstractList.maxArraySize;
            if ((r0 - AbstractList.maxArraySize) <= 0) goto L11;
            if (r5 <= AbstractList.maxArraySize) goto L10;
            r1 = Integer.MAX_VALUE;
        L10:
            return r1;
        L11:
            return r0;
        }

        public final boolean orderedEquals$kotlin_stdlib(Collection<?> r7, Collection<?> r8) {
            Intrinsics.checkNotNullParameter(r7, "c");
            Intrinsics.checkNotNullParameter(r8, "other");
            if (r7.size() == r8.size()) goto L5;
            return false;
        L5:
            Iterator<?> r0 = r8.iterator();
            Iterator<?> r1 = r7.iterator();
        L7:
            if (r1.hasNext() == false) goto L11;
            if (Intrinsics.areEqual(r1.next(), r0.next()) == true) goto L7;
            return false;
        L11:
            return true;
        }

        public final int orderedHashCode$kotlin_stdlib(Collection<?> r6) {
            Intrinsics.checkNotNullParameter(r6, "c");
            int r0 = 1;
            Iterator<?> r1 = r6.iterator();
        L4:
            if (r1.hasNext() == false) goto L10;
            Object r2 = r1.next();
            int r3 = r0 * 31;
            if (r2 == null) goto L8;
            int r4 = r2.hashCode();
        L9:
            r0 = r3 + r4;
            goto L4
        L8:
            r4 = 0;
            goto L9
        L10:
            return r0;
        }
    }

    /* JADX INFO: compiled from: AbstractList.kt */
    @Metadata(m420d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010(\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0003\b\u0092\u0004\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001B\u0005¢\u0006\u0002\u0010\u0002J\t\u0010\t\u001a\u00020\nH\u0096\u0002J\u000e\u0010\u000b\u001a\u00028\u0000H\u0096\u0002¢\u0006\u0002\u0010\fR\u001a\u0010\u0003\u001a\u00020\u0004X\u0084\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\b¨\u0006\r"}, m421d2 = {"Lkotlin/collections/AbstractList$IteratorImpl;", "", "(Lkotlin/collections/AbstractList;)V", StreamInformation.KEY_INDEX, "", "getIndex", "()I", "setIndex", "(I)V", "hasNext", "", "next", "()Ljava/lang/Object;", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
    private class IteratorImpl implements Iterator<E>, KMappedMarker {
        private int index;
        final /* synthetic */ AbstractList<E> this$0;

        public IteratorImpl(AbstractList r1) {
            this.this$0 = r1;
        }

        protected final int getIndex() {
            return this.index;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.index >= this.this$0.size()) goto L5;
            return true;
        L5:
            return false;
        }

        @Override // java.util.Iterator
        public E next() {
            if (hasNext() == false) goto L7;
            AbstractList<E> r0 = this.this$0;
            int r1 = this.index;
            this.index = r1 + 1;
            return r0.get(r1);
        L7:
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }

        protected final void setIndex(int r1) {
            this.index = r1;
        }
    }

    /* JADX INFO: compiled from: AbstractList.kt */
    @Metadata(m420d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010*\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\b\u0092\u0004\u0018\u00002\f0\u0001R\b\u0012\u0004\u0012\u00028\u00000\u00022\b\u0012\u0004\u0012\u00028\u00000\u0003B\r\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\b\u0010\u0007\u001a\u00020\bH\u0016J\b\u0010\t\u001a\u00020\u0005H\u0016J\r\u0010\n\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010\u000bJ\b\u0010\f\u001a\u00020\u0005H\u0016¨\u0006\r"}, m421d2 = {"Lkotlin/collections/AbstractList$ListIteratorImpl;", "Lkotlin/collections/AbstractList$IteratorImpl;", "Lkotlin/collections/AbstractList;", "", StreamInformation.KEY_INDEX, "", "(Lkotlin/collections/AbstractList;I)V", "hasPrevious", "", "nextIndex", "previous", "()Ljava/lang/Object;", "previousIndex", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
    private class ListIteratorImpl extends AbstractList<E>.IteratorImpl implements ListIterator<E>, KMappedMarker {
        final /* synthetic */ AbstractList<E> this$0;

        public ListIteratorImpl(AbstractList r3, int r4) {
            this.this$0 = r3;
            super(r3);
            AbstractList.Companion.checkPositionIndex$kotlin_stdlib(r4, this.this$0.size());
            setIndex(r4);
        }

        @Override // java.util.ListIterator
        public void add(E r3) {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }

        @Override // java.util.ListIterator
        public boolean hasPrevious() {
            if (getIndex() <= 0) goto L5;
            return true;
        L5:
            return false;
        }

        @Override // java.util.ListIterator
        public int nextIndex() {
            return getIndex();
        }

        @Override // java.util.ListIterator
        public E previous() {
            if (hasPrevious() == false) goto L7;
            AbstractList<E> r0 = this.this$0;
            setIndex(getIndex() - 1);
            return r0.get(getIndex());
        L7:
            throw new NoSuchElementException();
        }

        @Override // java.util.ListIterator
        public int previousIndex() {
            return getIndex() - 1;
        }

        @Override // java.util.ListIterator
        public void set(E r3) {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }
    }

    /* JADX INFO: compiled from: AbstractList.kt */
    @Metadata(m420d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\n\b\u0002\u0018\u0000*\u0006\b\u0001\u0010\u0001 \u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\u00060\u0003j\u0002`\u0004B#\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00010\u0002\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\u0007¢\u0006\u0002\u0010\tJ\u0016\u0010\u000e\u001a\u00028\u00012\u0006\u0010\u000f\u001a\u00020\u0007H\u0096\u0002¢\u0006\u0002\u0010\u0010R\u000e\u0010\n\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00010\u0002X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\u00020\u00078VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\f\u0010\r¨\u0006\u0011"}, m421d2 = {"Lkotlin/collections/AbstractList$SubList;", ExifInterface.LONGITUDE_EAST, "Lkotlin/collections/AbstractList;", "Ljava/util/RandomAccess;", "Lkotlin/collections/RandomAccess;", "list", "fromIndex", "", "toIndex", "(Lkotlin/collections/AbstractList;II)V", "_size", MediaInformation.KEY_SIZE, "getSize", "()I", "get", StreamInformation.KEY_INDEX, "(I)Ljava/lang/Object;", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
    private static final class SubList<E> extends AbstractList<E> implements RandomAccess {
        private int _size;
        private final int fromIndex;
        private final AbstractList<E> list;

        /* JADX WARN: Multi-variable type inference failed */
        public SubList(AbstractList<? extends E> r4, int r5, int r6) {
            Intrinsics.checkNotNullParameter(r4, "list");
            this.list = r4;
            this.fromIndex = r5;
            AbstractList.Companion.checkRangeIndexes$kotlin_stdlib(this.fromIndex, r6, this.list.size());
            this._size = r6 - this.fromIndex;
        }

        @Override // kotlin.collections.AbstractList, java.util.List
        public E get(int r3) {
            AbstractList.Companion.checkElementIndex$kotlin_stdlib(r3, this._size);
            return this.list.get(this.fromIndex + r3);
        }

        @Override // kotlin.collections.AbstractList, kotlin.collections.AbstractCollection
        public int getSize() {
            return this._size;
        }
    }

    static {
        Companion = new Companion(null);
    }

    protected AbstractList() {
    }

    @Override // java.util.List
    public void add(int r3, E r4) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.List
    public boolean addAll(int r3, Collection<? extends E> r4) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection, java.util.List
    public boolean equals(Object r4) {
        if (r4 != this) goto L6;
        return true;
    L6:
        if ((r4 instanceof List) == true) goto L10;
        return false;
    L10:
        return Companion.orderedEquals$kotlin_stdlib(this, (Collection) r4);
    }

    @Override // java.util.List
    public abstract E get(int r1);

    @Override // kotlin.collections.AbstractCollection
    public abstract int getSize();

    @Override // java.util.Collection, java.util.List
    public int hashCode() {
        return Companion.orderedHashCode$kotlin_stdlib(this);
    }

    @Override // java.util.List
    public int indexOf(E r8) {
        int r2 = 0;
        Iterator<E> r3 = iterator();
    L4:
        if (r3.hasNext() == false) goto L10;
        if (Intrinsics.areEqual(r3.next(), r8) == true) goto L13;
        r2 = r2 + 1;
        goto L4
    L13:
        return r2;
    L10:
        return -1;
    }

    @Override // kotlin.collections.AbstractCollection, java.util.Collection, java.lang.Iterable
    public Iterator<E> iterator() {
        return new IteratorImpl(this);
    }

    @Override // java.util.List
    public int lastIndexOf(E r6) {
        AbstractList<E> r0 = this;
        ListIterator<E> r2 = r0.listIterator(r0.size());
    L4:
        if (r2.hasPrevious() == false) goto L8;
        if (Intrinsics.areEqual(r2.previous(), r6) == false) goto L4;
        return r2.nextIndex();
    L8:
        return -1;
    }

    @Override // java.util.List
    public ListIterator<E> listIterator() {
        return new ListIteratorImpl(this, 0);
    }

    @Override // java.util.List
    public ListIterator<E> listIterator(int r2) {
        return new ListIteratorImpl(this, r2);
    }

    @Override // java.util.List
    public E remove(int r3) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.List
    public E set(int r3, E r4) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.List
    public List<E> subList(int r2, int r3) {
        return new SubList(this, r2, r3);
    }
}
