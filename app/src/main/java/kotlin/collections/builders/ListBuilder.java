package kotlin.collections.builders;

import androidx.exifinterface.media.ExifInterface;
import androidx.media3.exoplayer.upstream.CmcdData;
import com.arthenica.ffmpegkit.MediaInformation;
import com.arthenica.ffmpegkit.StreamInformation;
import java.io.NotSerializableException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;
import kotlin.Metadata;
import kotlin.collections.AbstractList;
import kotlin.collections.AbstractMutableList;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMutableList;
import kotlin.jvm.internal.markers.KMutableListIterator;

/* JADX INFO: compiled from: ListBuilder.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000r\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\f\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u001e\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\b\t\n\u0002\u0010\u0000\n\u0002\b\b\n\u0002\u0010)\n\u0002\b\u0002\n\u0002\u0010+\n\u0002\b\u0016\n\u0002\u0010\u000e\n\u0002\b\u0004\b\u0000\u0018\u0000 X*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\u00060\u0003j\u0002`\u00042\b\u0012\u0004\u0012\u0002H\u00010\u00052\u00060\u0006j\u0002`\u0007:\u0002XYB\u0007\b\u0016¢\u0006\u0002\u0010\bB\u000f\b\u0016\u0012\u0006\u0010\t\u001a\u00020\n¢\u0006\u0002\u0010\u000bBM\b\u0002\u0012\f\u0010\f\u001a\b\u0012\u0004\u0012\u00028\u00000\r\u0012\u0006\u0010\u000e\u001a\u00020\n\u0012\u0006\u0010\u000f\u001a\u00020\n\u0012\u0006\u0010\u0010\u001a\u00020\u0011\u0012\u000e\u0010\u0012\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0000\u0012\u000e\u0010\u0013\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0000¢\u0006\u0002\u0010\u0014J\u0015\u0010\u001b\u001a\u00020\u00112\u0006\u0010\u001c\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010\u001dJ\u001d\u0010\u001b\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\n2\u0006\u0010\u001c\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010 J\u001e\u0010!\u001a\u00020\u00112\u0006\u0010\u001f\u001a\u00020\n2\f\u0010\"\u001a\b\u0012\u0004\u0012\u00028\u00000#H\u0016J\u0016\u0010!\u001a\u00020\u00112\f\u0010\"\u001a\b\u0012\u0004\u0012\u00028\u00000#H\u0016J&\u0010$\u001a\u00020\u001e2\u0006\u0010%\u001a\u00020\n2\f\u0010\"\u001a\b\u0012\u0004\u0012\u00028\u00000#2\u0006\u0010&\u001a\u00020\nH\u0002J\u001d\u0010'\u001a\u00020\u001e2\u0006\u0010%\u001a\u00020\n2\u0006\u0010\u001c\u001a\u00028\u0000H\u0002¢\u0006\u0002\u0010 J\f\u0010(\u001a\b\u0012\u0004\u0012\u00028\u00000)J\b\u0010*\u001a\u00020\u001eH\u0002J\b\u0010+\u001a\u00020\u001eH\u0002J\b\u0010,\u001a\u00020\u001eH\u0016J\u0014\u0010-\u001a\u00020\u00112\n\u0010.\u001a\u0006\u0012\u0002\b\u00030)H\u0002J\u0010\u0010/\u001a\u00020\u001e2\u0006\u00100\u001a\u00020\nH\u0002J\u0010\u00101\u001a\u00020\u001e2\u0006\u0010&\u001a\u00020\nH\u0002J\u0013\u00102\u001a\u00020\u00112\b\u0010.\u001a\u0004\u0018\u000103H\u0096\u0002J\u0016\u00104\u001a\u00028\u00002\u0006\u0010\u001f\u001a\u00020\nH\u0096\u0002¢\u0006\u0002\u00105J\b\u00106\u001a\u00020\nH\u0016J\u0015\u00107\u001a\u00020\n2\u0006\u0010\u001c\u001a\u00028\u0000H\u0016¢\u0006\u0002\u00108J\u0018\u00109\u001a\u00020\u001e2\u0006\u0010%\u001a\u00020\n2\u0006\u0010&\u001a\u00020\nH\u0002J\b\u0010:\u001a\u00020\u0011H\u0016J\u000f\u0010;\u001a\b\u0012\u0004\u0012\u00028\u00000<H\u0096\u0002J\u0015\u0010=\u001a\u00020\n2\u0006\u0010\u001c\u001a\u00028\u0000H\u0016¢\u0006\u0002\u00108J\u000e\u0010>\u001a\b\u0012\u0004\u0012\u00028\u00000?H\u0016J\u0016\u0010>\u001a\b\u0012\u0004\u0012\u00028\u00000?2\u0006\u0010\u001f\u001a\u00020\nH\u0016J\b\u0010@\u001a\u00020\u001eH\u0002J\u0015\u0010A\u001a\u00020\u00112\u0006\u0010\u001c\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010\u001dJ\u0016\u0010B\u001a\u00020\u00112\f\u0010\"\u001a\b\u0012\u0004\u0012\u00028\u00000#H\u0016J\u0015\u0010C\u001a\u00028\u00002\u0006\u0010\u001f\u001a\u00020\nH\u0016¢\u0006\u0002\u00105J\u0015\u0010D\u001a\u00028\u00002\u0006\u0010%\u001a\u00020\nH\u0002¢\u0006\u0002\u00105J\u0018\u0010E\u001a\u00020\u001e2\u0006\u0010F\u001a\u00020\n2\u0006\u0010G\u001a\u00020\nH\u0002J\u0016\u0010H\u001a\u00020\u00112\f\u0010\"\u001a\b\u0012\u0004\u0012\u00028\u00000#H\u0016J.\u0010I\u001a\u00020\n2\u0006\u0010F\u001a\u00020\n2\u0006\u0010G\u001a\u00020\n2\f\u0010\"\u001a\b\u0012\u0004\u0012\u00028\u00000#2\u0006\u0010J\u001a\u00020\u0011H\u0002J\u001e\u0010K\u001a\u00028\u00002\u0006\u0010\u001f\u001a\u00020\n2\u0006\u0010\u001c\u001a\u00028\u0000H\u0096\u0002¢\u0006\u0002\u0010LJ\u001e\u0010M\u001a\b\u0012\u0004\u0012\u00028\u00000\u00022\u0006\u0010N\u001a\u00020\n2\u0006\u0010O\u001a\u00020\nH\u0016J\u0015\u0010P\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001030\rH\u0016¢\u0006\u0002\u0010QJ'\u0010P\u001a\b\u0012\u0004\u0012\u0002HR0\r\"\u0004\b\u0001\u0010R2\f\u0010S\u001a\b\u0012\u0004\u0012\u0002HR0\rH\u0016¢\u0006\u0002\u0010TJ\b\u0010U\u001a\u00020VH\u0016J\b\u0010W\u001a\u000203H\u0002R\u0016\u0010\f\u001a\b\u0012\u0004\u0012\u00028\u00000\rX\u0082\u000e¢\u0006\u0004\n\u0002\u0010\u0015R\u0016\u0010\u0012\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0000X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0016\u001a\u00020\u00118BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u0016\u0010\u0017R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u0016\u0010\u0013\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0000X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0018\u001a\u00020\n8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0019\u0010\u001a¨\u0006Z"}, m421d2 = {"Lkotlin/collections/builders/ListBuilder;", ExifInterface.LONGITUDE_EAST, "", "Ljava/util/RandomAccess;", "Lkotlin/collections/RandomAccess;", "Lkotlin/collections/AbstractMutableList;", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "()V", "initialCapacity", "", "(I)V", "array", "", "offset", "length", "isReadOnly", "", "backing", "root", "([Ljava/lang/Object;IIZLkotlin/collections/builders/ListBuilder;Lkotlin/collections/builders/ListBuilder;)V", "[Ljava/lang/Object;", "isEffectivelyReadOnly", "()Z", MediaInformation.KEY_SIZE, "getSize", "()I", "add", "element", "(Ljava/lang/Object;)Z", "", StreamInformation.KEY_INDEX, "(ILjava/lang/Object;)V", "addAll", "elements", "", "addAllInternal", CmcdData.Factory.OBJECT_TYPE_INIT_SEGMENT, "n", "addAtInternal", "build", "", "checkForComodification", "checkIsMutable", "clear", "contentEquals", "other", "ensureCapacityInternal", "minCapacity", "ensureExtraCapacity", "equals", "", "get", "(I)Ljava/lang/Object;", "hashCode", "indexOf", "(Ljava/lang/Object;)I", "insertAtInternal", "isEmpty", "iterator", "", "lastIndexOf", "listIterator", "", "registerModification", "remove", "removeAll", "removeAt", "removeAtInternal", "removeRangeInternal", "rangeOffset", "rangeLength", "retainAll", "retainOrRemoveAllInternal", "retain", "set", "(ILjava/lang/Object;)Ljava/lang/Object;", "subList", "fromIndex", "toIndex", "toArray", "()[Ljava/lang/Object;", ExifInterface.GPS_DIRECTION_TRUE, "destination", "([Ljava/lang/Object;)[Ljava/lang/Object;", "toString", "", "writeReplace", "Companion", "Itr", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public final class ListBuilder<E> extends AbstractMutableList<E> implements List<E>, RandomAccess, Serializable, KMutableList {
    private static final Companion Companion = null;
    private static final ListBuilder Empty = null;
    private E[] array;
    private final ListBuilder<E> backing;
    private boolean isReadOnly;
    private int length;
    private int offset;
    private final ListBuilder<E> root;

    /* JADX INFO: compiled from: ListBuilder.kt */
    @Metadata(m420d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0001\n\u0000\b\u0082\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0006"}, m421d2 = {"Lkotlin/collections/builders/ListBuilder$Companion;", "", "()V", "Empty", "Lkotlin/collections/builders/ListBuilder;", "", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
    private static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker r1) {
            this();
        }
    }

    /* JADX INFO: compiled from: ListBuilder.kt */
    @Metadata(m420d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010+\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\t\b\u0002\u0018\u0000*\u0004\b\u0001\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u001d\b\u0016\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00010\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u0015\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00028\u0001H\u0016¢\u0006\u0002\u0010\rJ\b\u0010\u000e\u001a\u00020\u000bH\u0002J\t\u0010\u000f\u001a\u00020\u0010H\u0096\u0002J\b\u0010\u0011\u001a\u00020\u0010H\u0016J\u000e\u0010\u0012\u001a\u00028\u0001H\u0096\u0002¢\u0006\u0002\u0010\u0013J\b\u0010\u0014\u001a\u00020\u0006H\u0016J\r\u0010\u0015\u001a\u00028\u0001H\u0016¢\u0006\u0002\u0010\u0013J\b\u0010\u0016\u001a\u00020\u0006H\u0016J\b\u0010\u0017\u001a\u00020\u000bH\u0016J\u0015\u0010\u0018\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00028\u0001H\u0016¢\u0006\u0002\u0010\rR\u000e\u0010\b\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00010\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0019"}, m421d2 = {"Lkotlin/collections/builders/ListBuilder$Itr;", ExifInterface.LONGITUDE_EAST, "", "list", "Lkotlin/collections/builders/ListBuilder;", StreamInformation.KEY_INDEX, "", "(Lkotlin/collections/builders/ListBuilder;I)V", "expectedModCount", "lastIndex", "add", "", "element", "(Ljava/lang/Object;)V", "checkForComodification", "hasNext", "", "hasPrevious", "next", "()Ljava/lang/Object;", "nextIndex", "previous", "previousIndex", "remove", "set", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
    private static final class Itr<E> implements ListIterator<E>, KMutableListIterator {
        private int expectedModCount;
        private int index;
        private int lastIndex;
        private final ListBuilder<E> list;

        public Itr(ListBuilder<E> r2, int r3) {
            Intrinsics.checkNotNullParameter(r2, "list");
            this.list = r2;
            this.index = r3;
            this.lastIndex = -1;
            this.expectedModCount = ListBuilder.m1286access$getModCount$p$s2084097795(r2);
        }

        private final void checkForComodification() {
            if (ListBuilder.m1286access$getModCount$p$s2084097795(this.list) != this.expectedModCount) goto L6;
            return;
        L6:
            throw new ConcurrentModificationException();
        }

        @Override // java.util.ListIterator
        public void add(E r4) {
            checkForComodification();
            ListBuilder<E> r0 = this.list;
            int r1 = this.index;
            this.index = r1 + 1;
            r0.add(r1, r4);
            this.lastIndex = -1;
            this.expectedModCount = ListBuilder.m1286access$getModCount$p$s2084097795(this.list);
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public boolean hasNext() {
            if (this.index >= ListBuilder.access$getLength$p(this.list)) goto L5;
            return true;
        L5:
            return false;
        }

        @Override // java.util.ListIterator
        public boolean hasPrevious() {
            if (this.index <= 0) goto L5;
            return true;
        L5:
            return false;
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public E next() {
            checkForComodification();
            if (this.index >= ListBuilder.access$getLength$p(this.list)) goto L7;
            int r0 = this.index;
            this.index = r0 + 1;
            this.lastIndex = r0;
            return (E) ListBuilder.access$getArray$p(this.list)[ListBuilder.access$getOffset$p(this.list) + this.lastIndex];
        L7:
            throw new NoSuchElementException();
        }

        @Override // java.util.ListIterator
        public int nextIndex() {
            return this.index;
        }

        @Override // java.util.ListIterator
        public E previous() {
            checkForComodification();
            if (this.index <= 0) goto L7;
            this.index--;
            this.lastIndex = this.index;
            return (E) ListBuilder.access$getArray$p(this.list)[ListBuilder.access$getOffset$p(this.list) + this.lastIndex];
        L7:
            throw new NoSuchElementException();
        }

        @Override // java.util.ListIterator
        public int previousIndex() {
            return this.index - 1;
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public void remove() {
            checkForComodification();
            if (this.lastIndex == (-1)) goto L5;
            boolean r0 = true;
        L6:
            if (r0 == false) goto L10;
            this.list.remove(this.lastIndex);
            this.index = this.lastIndex;
            this.lastIndex = -1;
            this.expectedModCount = ListBuilder.m1286access$getModCount$p$s2084097795(this.list);
            return;
        L10:
            throw new IllegalStateException("Call next() or previous() before removing element from the iterator.".toString());
        L5:
            r0 = false;
            goto L6
        }

        @Override // java.util.ListIterator
        public void set(E r3) {
            checkForComodification();
            if (this.lastIndex == (-1)) goto L5;
            boolean r0 = true;
        L6:
            if (r0 == false) goto L10;
            this.list.set(this.lastIndex, r3);
            return;
        L10:
            throw new IllegalStateException("Call next() or previous() before replacing element from the iterator.".toString());
        L5:
            r0 = false;
            goto L6
        }
    }

    static {
        Companion = new Companion(null);
        ListBuilder r0 = new ListBuilder(0);
        r0.isReadOnly = true;
        Empty = r0;
    }

    public ListBuilder() {
        this(10);
    }

    public ListBuilder(int r8) {
        this(ListBuilderKt.arrayOfUninitializedElements(r8), 0, 0, false, null, null);
    }

    private ListBuilder(E[] r2, int r3, int r4, boolean r5, ListBuilder<E> r6, ListBuilder<E> r7) {
        this.array = r2;
        this.offset = r3;
        this.length = r4;
        this.isReadOnly = r5;
        this.backing = r6;
        this.root = r7;
        if (this.backing == null) goto L7;
        this.modCount = this.backing.modCount;
        return;
    }

    public static final /* synthetic */ Object[] access$getArray$p(ListBuilder r1) {
        return r1.array;
    }

    public static final /* synthetic */ int access$getLength$p(ListBuilder r1) {
        return r1.length;
    }

    /* JADX INFO: renamed from: access$getModCount$p$s-2084097795, reason: not valid java name */
    public static final /* synthetic */ int m1286access$getModCount$p$s2084097795(ListBuilder r1) {
        return r1.modCount;
    }

    public static final /* synthetic */ int access$getOffset$p(ListBuilder r1) {
        return r1.offset;
    }

    private final void addAllInternal(int r6, Collection<? extends E> r7, int r8) {
        registerModification();
        if (this.backing == null) goto L5;
        this.backing.addAllInternal(r6, r7, r8);
        this.array = this.backing.array;
        this.length += r8;
        return;
    L5:
        insertAtInternal(r6, r8);
        int r0 = 0;
        Iterator<? extends E> r1 = r7.iterator();
    L6:
        if (r0 >= r8) goto L10;
        this.array[r6 + r0] = r1.next();
        r0 = r0 + 1;
        goto L6
    }

    private final void addAtInternal(int r3, E r4) {
        registerModification();
        if (this.backing == null) goto L5;
        this.backing.addAtInternal(r3, r4);
        this.array = this.backing.array;
        this.length++;
        return;
    L5:
        insertAtInternal(r3, 1);
        this.array[r3] = r4;
    }

    private final void checkForComodification() {
        if (this.root != null) goto L5;
        return;
    L5:
        if (this.root.modCount != this.modCount) goto L8;
        return;
    L8:
        throw new ConcurrentModificationException();
    }

    private final void checkIsMutable() {
        if (isEffectivelyReadOnly() == true) goto L6;
        return;
    L6:
        throw new UnsupportedOperationException();
    }

    private final boolean contentEquals(List<?> r4) {
        return ListBuilderKt.access$subarrayContentEquals(this.array, this.offset, this.length, r4);
    }

    private final void ensureCapacityInternal(int r3) {
        if (r3 < 0) goto L8;
        if (r3 <= this.array.length) goto L9;
        int r0 = AbstractList.Companion.newCapacity$kotlin_stdlib(this.array.length, r3);
        this.array = (E[]) ListBuilderKt.copyOfUninitializedElements(this.array, r0);
        return;
    L9:
        return;
    L8:
        throw new OutOfMemoryError();
    }

    private final void ensureExtraCapacity(int r2) {
        ensureCapacityInternal(this.length + r2);
    }

    private final void insertAtInternal(int r5, int r6) {
        ensureExtraCapacity(r6);
        ArraysKt.copyInto(this.array, this.array, r5 + r6, r5, this.offset + this.length);
        this.length += r6;
    }

    private final boolean isEffectivelyReadOnly() {
        if (this.isReadOnly == false) goto L5;
    L10:
        return true;
    L5:
        if (this.root != null) goto L7;
    L9:
        return false;
    L7:
        if (this.root.isReadOnly == false) goto L9;
        goto L9
    }

    private final void registerModification() {
        this.modCount++;
    }

    private final E removeAtInternal(int r7) {
        registerModification();
        if (this.backing == null) goto L6;
        E r0 = this.backing.removeAtInternal(r7);
        this.length--;
        return r0;
    L6:
        E r1 = this.array[r7];
        ArraysKt.copyInto(this.array, this.array, r7, r7 + 1, this.offset + this.length);
        ListBuilderKt.resetAt(this.array, (this.offset + this.length) - 1);
        this.length--;
        return r1;
    }

    private final void removeRangeInternal(int r5, int r6) {
        if (r6 <= 0) goto L5;
        registerModification();
    L5:
        if (this.backing == null) goto L7;
        this.backing.removeRangeInternal(r5, r6);
    L8:
        this.length -= r6;
        return;
    L7:
        ArraysKt.copyInto(this.array, this.array, r5, r5 + r6, this.length);
        ListBuilderKt.resetRange(this.array, this.length - r6, this.length);
        goto L8
    }

    private final int retainOrRemoveAllInternal(int r9, int r10, Collection<? extends E> r11, boolean r12) {
        if (this.backing == null) goto L5;
        int r0 = this.backing.retainOrRemoveAllInternal(r9, r10, r11, r12);
    L13:
        if (r0 <= 0) goto L15;
        registerModification();
    L15:
        this.length -= r0;
        return r0;
    L5:
        int r1 = 0;
        int r2 = 0;
    L6:
        if (r1 >= r10) goto L11;
        if (r11.contains(this.array[r9 + r1]) == r12) goto L9;
        r1 = r1 + 1;
        goto L6
    L9:
        this.array[r2 + r9] = this.array[r1 + r9];
        r2 = r2 + 1;
        r1 = r1 + 1;
        goto L6
    L11:
        int r3 = r10 - r2;
        ArraysKt.copyInto(this.array, this.array, r9 + r2, r9 + r10, this.length);
        ListBuilderKt.resetRange(this.array, this.length - r3, this.length);
        r0 = r3;
        goto L13
    }

    private final Object writeReplace() {
        if (isEffectivelyReadOnly() == false) goto L7;
        return new SerializedCollection(this, 0);
    L7:
        throw new NotSerializableException("The list cannot be serialized while it is being built.");
    }

    @Override // kotlin.collections.AbstractMutableList, java.util.AbstractList, java.util.List
    public void add(int r3, E r4) {
        checkIsMutable();
        checkForComodification();
        AbstractList.Companion.checkPositionIndex$kotlin_stdlib(r3, this.length);
        addAtInternal(this.offset + r3, r4);
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean add(E r3) {
        checkIsMutable();
        checkForComodification();
        addAtInternal(this.offset + this.length, r3);
        return true;
    }

    @Override // java.util.AbstractList, java.util.List
    public boolean addAll(int r3, Collection<? extends E> r4) {
        Intrinsics.checkNotNullParameter(r4, "elements");
        checkIsMutable();
        checkForComodification();
        AbstractList.Companion.checkPositionIndex$kotlin_stdlib(r3, this.length);
        int r0 = r4.size();
        addAllInternal(this.offset + r3, r4, r0);
        if (r0 <= 0) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean addAll(Collection<? extends E> r4) {
        Intrinsics.checkNotNullParameter(r4, "elements");
        checkIsMutable();
        checkForComodification();
        int r0 = r4.size();
        addAllInternal(this.offset + this.length, r4, r0);
        if (r0 <= 0) goto L5;
        return true;
    L5:
        return false;
    }

    public final List<E> build() {
        if (this.backing != null) goto L10;
        checkIsMutable();
        this.isReadOnly = true;
        if (this.length <= 0) goto L8;
        return this;
    L8:
        return Empty;
    L10:
        throw new IllegalStateException();
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public void clear() {
        checkIsMutable();
        checkForComodification();
        removeRangeInternal(this.offset, this.length);
    }

    @Override // java.util.AbstractList, java.util.Collection, java.util.List
    public boolean equals(Object r2) {
        checkForComodification();
        if (r2 != this) goto L5;
    L10:
        return true;
    L5:
        if ((r2 instanceof List) == true) goto L7;
    L9:
        return false;
    L7:
        if (contentEquals((List) r2) == false) goto L9;
        goto L9
    }

    @Override // java.util.AbstractList, java.util.List
    public E get(int r3) {
        checkForComodification();
        AbstractList.Companion.checkElementIndex$kotlin_stdlib(r3, this.length);
        return this.array[this.offset + r3];
    }

    @Override // kotlin.collections.AbstractMutableList
    public int getSize() {
        checkForComodification();
        return this.length;
    }

    @Override // java.util.AbstractList, java.util.Collection, java.util.List
    public int hashCode() {
        checkForComodification();
        return ListBuilderKt.access$subarrayContentHashCode(this.array, this.offset, this.length);
    }

    @Override // java.util.AbstractList, java.util.List
    public int indexOf(Object r4) {
        checkForComodification();
        int r0 = 0;
    L4:
        if (r0 >= this.length) goto L9;
        if (Intrinsics.areEqual(this.array[this.offset + r0], r4) == true) goto L7;
        r0 = r0 + 1;
        goto L4
    L7:
        return r0;
    L9:
        return -1;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean isEmpty() {
        checkForComodification();
        if (this.length != 0) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
    public Iterator<E> iterator() {
        return listIterator(0);
    }

    @Override // java.util.AbstractList, java.util.List
    public int lastIndexOf(Object r4) {
        checkForComodification();
        int r0 = this.length - 1;
    L3:
        if (r0 < 0) goto L8;
        if (Intrinsics.areEqual(this.array[this.offset + r0], r4) == true) goto L6;
        r0 = r0 - 1;
        goto L3
    L6:
        return r0;
    L8:
        return -1;
    }

    @Override // java.util.AbstractList, java.util.List
    public ListIterator<E> listIterator() {
        return listIterator(0);
    }

    @Override // java.util.AbstractList, java.util.List
    public ListIterator<E> listIterator(int r3) {
        checkForComodification();
        AbstractList.Companion.checkPositionIndex$kotlin_stdlib(r3, this.length);
        return new Itr(this, r3);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean remove(Object r3) {
        checkIsMutable();
        checkForComodification();
        int r0 = indexOf(r3);
        if (r0 < 0) goto L5;
        remove(r0);
    L5:
        if (r0 < 0) goto L7;
        return true;
    L7:
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean removeAll(Collection<? extends Object> r4) {
        Intrinsics.checkNotNullParameter(r4, "elements");
        checkIsMutable();
        checkForComodification();
        if (retainOrRemoveAllInternal(this.offset, this.length, r4, false) <= 0) goto L6;
        return true;
    L6:
        return false;
    }

    @Override // kotlin.collections.AbstractMutableList
    public E removeAt(int r3) {
        checkIsMutable();
        checkForComodification();
        AbstractList.Companion.checkElementIndex$kotlin_stdlib(r3, this.length);
        return removeAtInternal(this.offset + r3);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean retainAll(Collection<? extends Object> r4) {
        Intrinsics.checkNotNullParameter(r4, "elements");
        checkIsMutable();
        checkForComodification();
        if (retainOrRemoveAllInternal(this.offset, this.length, r4, true) > 0) goto L7;
        return false;
    L7:
        return true;
    }

    @Override // kotlin.collections.AbstractMutableList, java.util.AbstractList, java.util.List
    public E set(int r4, E r5) {
        checkIsMutable();
        checkForComodification();
        AbstractList.Companion.checkElementIndex$kotlin_stdlib(r4, this.length);
        E r0 = this.array[this.offset + r4];
        this.array[this.offset + r4] = r5;
        return r0;
    }

    @Override // java.util.AbstractList, java.util.List
    public List<E> subList(int r10, int r11) {
        AbstractList.Companion.checkRangeIndexes$kotlin_stdlib(r10, r11, this.length);
        E[] r3 = this.array;
        int r4 = this.offset + r10;
        int r5 = r11 - r10;
        boolean r6 = this.isReadOnly;
        ListBuilder<E> r0 = this.root;
        if (r0 != null) goto L5;
        ListBuilder<E> r8 = this;
    L7:
        return new ListBuilder(r3, r4, r5, r6, this, r8);
    L5:
        r8 = r0;
        goto L7
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public Object[] toArray() {
        checkForComodification();
        return ArraysKt.copyOfRange(this.array, this.offset, this.offset + this.length);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public <T> T[] toArray(T[] r5) {
        Intrinsics.checkNotNullParameter(r5, "destination");
        checkForComodification();
        if (r5.length >= this.length) goto L6;
        T[] r0 = (T[]) Arrays.copyOfRange(this.array, this.offset, this.offset + this.length, r5.getClass());
        Intrinsics.checkNotNullExpressionValue(r0, "copyOfRange(...)");
        return r0;
    L6:
        ArraysKt.copyInto(this.array, r5, 0, this.offset, this.offset + this.length);
        return (T[]) CollectionsKt.terminateCollectionToArray(this.length, r5);
    }

    @Override // java.util.AbstractCollection
    public String toString() {
        checkForComodification();
        return ListBuilderKt.access$subarrayContentToString(this.array, this.offset, this.length, this);
    }
}
