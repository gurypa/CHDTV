package kotlin.collections;

import androidx.exifinterface.media.ExifInterface;
import androidx.media3.extractor.text.ttml.TtmlNode;
import com.arthenica.ffmpegkit.MediaInformation;
import com.arthenica.ffmpegkit.StreamInformation;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

/* JADX INFO: compiled from: ArrayDeque.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u001e\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0011\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u001b\b\u0007\u0018\u0000 P*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002:\u0001PB\u000f\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005B\u0007\b\u0016¢\u0006\u0002\u0010\u0006B\u0015\b\u0016\u0012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\b¢\u0006\u0002\u0010\tJ\u0015\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010\u0016J\u001d\u0010\u0013\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00042\u0006\u0010\u0015\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010\u0019J\u001e\u0010\u001a\u001a\u00020\u00142\u0006\u0010\u0018\u001a\u00020\u00042\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\bH\u0016J\u0016\u0010\u001a\u001a\u00020\u00142\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\bH\u0016J\u0013\u0010\u001b\u001a\u00020\u00172\u0006\u0010\u0015\u001a\u00028\u0000¢\u0006\u0002\u0010\u001cJ\u0013\u0010\u001d\u001a\u00020\u00172\u0006\u0010\u0015\u001a\u00028\u0000¢\u0006\u0002\u0010\u001cJ\b\u0010\u001e\u001a\u00020\u0017H\u0016J\u0016\u0010\u001f\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00028\u0000H\u0096\u0002¢\u0006\u0002\u0010\u0016J\u001e\u0010 \u001a\u00020\u00172\u0006\u0010!\u001a\u00020\u00042\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\bH\u0002J\u0010\u0010\"\u001a\u00020\u00172\u0006\u0010#\u001a\u00020\u0004H\u0002J\u0010\u0010$\u001a\u00020\u00042\u0006\u0010\u0018\u001a\u00020\u0004H\u0002J\u0010\u0010%\u001a\u00020\u00172\u0006\u0010&\u001a\u00020\u0004H\u0002J\u001d\u0010'\u001a\u00020\u00142\u0012\u0010(\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u00140)H\u0082\bJ\u000b\u0010*\u001a\u00028\u0000¢\u0006\u0002\u0010+J\r\u0010,\u001a\u0004\u0018\u00018\u0000¢\u0006\u0002\u0010+J\u0016\u0010-\u001a\u00028\u00002\u0006\u0010\u0018\u001a\u00020\u0004H\u0096\u0002¢\u0006\u0002\u0010.J\u0010\u0010/\u001a\u00020\u00042\u0006\u0010\u0018\u001a\u00020\u0004H\u0002J\u0015\u00100\u001a\u00020\u00042\u0006\u0010\u0015\u001a\u00028\u0000H\u0016¢\u0006\u0002\u00101J\u0016\u00102\u001a\u00028\u00002\u0006\u0010!\u001a\u00020\u0004H\u0083\b¢\u0006\u0002\u0010.J\u0011\u0010!\u001a\u00020\u00042\u0006\u0010\u0018\u001a\u00020\u0004H\u0083\bJM\u00103\u001a\u00020\u00172>\u00104\u001a:\u0012\u0013\u0012\u00110\u0004¢\u0006\f\b6\u0012\b\b7\u0012\u0004\b\b(\u000e\u0012\u001b\u0012\u0019\u0012\u0006\u0012\u0004\u0018\u00010\f0\u000b¢\u0006\f\b6\u0012\b\b7\u0012\u0004\b\b(\u0007\u0012\u0004\u0012\u00020\u001705H\u0000¢\u0006\u0002\b8J\b\u00109\u001a\u00020\u0014H\u0016J\u000b\u0010:\u001a\u00028\u0000¢\u0006\u0002\u0010+J\u0015\u0010;\u001a\u00020\u00042\u0006\u0010\u0015\u001a\u00028\u0000H\u0016¢\u0006\u0002\u00101J\r\u0010<\u001a\u0004\u0018\u00018\u0000¢\u0006\u0002\u0010+J\u0010\u0010=\u001a\u00020\u00042\u0006\u0010\u0018\u001a\u00020\u0004H\u0002J\u0010\u0010>\u001a\u00020\u00042\u0006\u0010\u0018\u001a\u00020\u0004H\u0002J\u0015\u0010?\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010\u0016J\u0016\u0010@\u001a\u00020\u00142\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\bH\u0016J\u0015\u0010A\u001a\u00028\u00002\u0006\u0010\u0018\u001a\u00020\u0004H\u0016¢\u0006\u0002\u0010.J\u000b\u0010B\u001a\u00028\u0000¢\u0006\u0002\u0010+J\r\u0010C\u001a\u0004\u0018\u00018\u0000¢\u0006\u0002\u0010+J\u000b\u0010D\u001a\u00028\u0000¢\u0006\u0002\u0010+J\r\u0010E\u001a\u0004\u0018\u00018\u0000¢\u0006\u0002\u0010+J\u0016\u0010F\u001a\u00020\u00142\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\bH\u0016J\u001e\u0010G\u001a\u00028\u00002\u0006\u0010\u0018\u001a\u00020\u00042\u0006\u0010\u0015\u001a\u00028\u0000H\u0096\u0002¢\u0006\u0002\u0010HJ\u0017\u0010I\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\f0\u000bH\u0000¢\u0006\u0004\bJ\u0010KJ)\u0010I\u001a\b\u0012\u0004\u0012\u0002HL0\u000b\"\u0004\b\u0001\u0010L2\f\u0010M\u001a\b\u0012\u0004\u0012\u0002HL0\u000bH\u0000¢\u0006\u0004\bJ\u0010NJ\u0015\u0010O\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\f0\u000bH\u0016¢\u0006\u0002\u0010KJ'\u0010O\u001a\b\u0012\u0004\u0012\u0002HL0\u000b\"\u0004\b\u0001\u0010L2\f\u0010M\u001a\b\u0012\u0004\u0012\u0002HL0\u000bH\u0016¢\u0006\u0002\u0010NR\u0018\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\f0\u000bX\u0082\u000e¢\u0006\u0004\n\u0002\u0010\rR\u000e\u0010\u000e\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u001e\u0010\u0010\u001a\u00020\u00042\u0006\u0010\u000f\u001a\u00020\u0004@RX\u0096\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012¨\u0006Q"}, m421d2 = {"Lkotlin/collections/ArrayDeque;", ExifInterface.LONGITUDE_EAST, "Lkotlin/collections/AbstractMutableList;", "initialCapacity", "", "(I)V", "()V", "elements", "", "(Ljava/util/Collection;)V", "elementData", "", "", "[Ljava/lang/Object;", TtmlNode.TAG_HEAD, "<set-?>", MediaInformation.KEY_SIZE, "getSize", "()I", "add", "", "element", "(Ljava/lang/Object;)Z", "", StreamInformation.KEY_INDEX, "(ILjava/lang/Object;)V", "addAll", "addFirst", "(Ljava/lang/Object;)V", "addLast", "clear", "contains", "copyCollectionElements", "internalIndex", "copyElements", "newCapacity", "decremented", "ensureCapacity", "minCapacity", "filterInPlace", "predicate", "Lkotlin/Function1;", "first", "()Ljava/lang/Object;", "firstOrNull", "get", "(I)Ljava/lang/Object;", "incremented", "indexOf", "(Ljava/lang/Object;)I", "internalGet", "internalStructure", "structure", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", "internalStructure$kotlin_stdlib", "isEmpty", "last", "lastIndexOf", "lastOrNull", "negativeMod", "positiveMod", "remove", "removeAll", "removeAt", "removeFirst", "removeFirstOrNull", "removeLast", "removeLastOrNull", "retainAll", "set", "(ILjava/lang/Object;)Ljava/lang/Object;", "testToArray", "testToArray$kotlin_stdlib", "()[Ljava/lang/Object;", ExifInterface.GPS_DIRECTION_TRUE, "array", "([Ljava/lang/Object;)[Ljava/lang/Object;", "toArray", "Companion", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public final class ArrayDeque<E> extends AbstractMutableList<E> {
    public static final Companion Companion = null;
    private static final int defaultMinCapacity = 10;
    private static final Object[] emptyElementData = null;
    private Object[] elementData;
    private int head;
    private int size;

    /* JADX INFO: compiled from: ArrayDeque.kt */
    @Metadata(m420d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0002\b\u0080\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u0018\u0010\u0005\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u0006X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u0007¨\u0006\b"}, m421d2 = {"Lkotlin/collections/ArrayDeque$Companion;", "", "()V", "defaultMinCapacity", "", "emptyElementData", "", "[Ljava/lang/Object;", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker r1) {
            this();
        }
    }

    static {
        Companion = new Companion(null);
        emptyElementData = new Object[0];
    }

    public ArrayDeque() {
        this.elementData = emptyElementData;
    }

    public ArrayDeque(int r4) {
        if (r4 != 0) goto L5;
        Object[] r0 = emptyElementData;
    L7:
        this.elementData = r0;
        return;
    L5:
        if (r4 <= 0) goto L10;
        r0 = new Object[r4];
        goto L7
    L10:
        throw new IllegalArgumentException("Illegal Capacity: " + r4);
    }

    public ArrayDeque(Collection<? extends E> r6) {
        Intrinsics.checkNotNullParameter(r6, "elements");
        boolean r3 = false;
        this.elementData = r6.toArray(new Object[0]);
        this.size = this.elementData.length;
        if (this.elementData.length != 0) goto L5;
        r3 = true;
    L5:
        if (r3 == false) goto L8;
        this.elementData = emptyElementData;
        return;
    }

    private final void copyCollectionElements(int r6, Collection<? extends E> r7) {
        Iterator<? extends E> r0 = r7.iterator();
        int r1 = r6;
        int r2 = this.elementData.length;
    L3:
        if (r1 >= r2) goto L7;
        if (r0.hasNext() == false) goto L7;
        this.elementData[r1] = r0.next();
        r1 = r1 + 1;
    L7:
        int r3 = 0;
        int r4 = this.head;
    L8:
        if (r3 >= r4) goto L12;
        if (r0.hasNext() == false) goto L12;
        this.elementData[r3] = r0.next();
        r3 = r3 + 1;
    L12:
        this.size = size() + r7.size();
    }

    private final void copyElements(int r6) {
        Object[] r0 = new Object[r6];
        ArraysKt.copyInto(this.elementData, r0, 0, this.head, this.elementData.length);
        ArraysKt.copyInto(this.elementData, r0, this.elementData.length - this.head, 0, this.head);
        this.head = 0;
        this.elementData = r0;
    }

    private final int decremented(int r2) {
        if (r2 != 0) goto L5;
        return ArraysKt.getLastIndex(this.elementData);
    L5:
        return r2 - 1;
    }

    private final void ensureCapacity(int r3) {
        if (r3 < 0) goto L13;
        if (r3 > this.elementData.length) goto L7;
        return;
    L7:
        if (this.elementData != emptyElementData) goto L10;
        this.elementData = new Object[RangesKt.coerceAtLeast(r3, 10)];
        return;
    L10:
        copyElements(AbstractList.Companion.newCapacity$kotlin_stdlib(this.elementData.length, r3));
        return;
    L13:
        throw new IllegalStateException("Deque is too big.");
    }

    private final boolean filterInPlace(Function1<? super E, Boolean> r11) {
        if (isEmpty() == false) goto L5;
    L37:
        return false;
    L5:
        if (this.elementData.length != 0) goto L7;
        boolean r1 = true;
    L8:
        if (r1 == true) goto L37;
        int r2 = positiveMod(this.head + size());
        int r3 = this.head;
        boolean r4 = false;
        if (this.head >= r2) goto L20;
        int r5 = this.head;
    L13:
        if (r5 >= r2) goto L19;
        Object r6 = this.elementData[r5];
        if (r11.invoke(r6).booleanValue() == false) goto L17;
        this.elementData[r3] = r6;
        r3 = r3 + 1;
    L18:
        r5 = r5 + 1;
        goto L13
    L17:
        r4 = true;
        goto L18
    L19:
        ArraysKt.fill(this.elementData, null, r3, r2);
    L34:
        if (r4 == false) goto L36;
        this.size = negativeMod(r3 - this.head);
    L36:
        return r4;
    L20:
        int r7 = this.head;
        int r8 = this.elementData.length;
    L21:
        if (r7 >= r8) goto L27;
        Object r9 = this.elementData[r7];
        this.elementData[r7] = null;
        if (r11.invoke(r9).booleanValue() == false) goto L25;
        this.elementData[r3] = r9;
        r3 = r3 + 1;
    L26:
        r7 = r7 + 1;
        goto L21
    L25:
        r4 = true;
        goto L26
    L27:
        r3 = positiveMod(r3);
        int r10 = 0;
    L28:
        if (r10 >= r2) goto L34;
        Object r12 = this.elementData[r10];
        this.elementData[r10] = null;
        if (r11.invoke(r12).booleanValue() == false) goto L32;
        this.elementData[r3] = r12;
        r3 = incremented(r3);
    L33:
        r10 = r10 + 1;
        goto L28
    L32:
        r4 = true;
        goto L33
    L7:
        r1 = false;
        goto L8
    }

    private final int incremented(int r2) {
        if (r2 != ArraysKt.getLastIndex(this.elementData)) goto L6;
        return 0;
    L6:
        return r2 + 1;
    }

    private final E internalGet(int r2) {
        return (E) this.elementData[r2];
    }

    private final int internalIndex(int r2) {
        return positiveMod(this.head + r2);
    }

    private final int negativeMod(int r2) {
        if (r2 >= 0) goto L5;
        return this.elementData.length + r2;
    L5:
        return r2;
    }

    private final int positiveMod(int r2) {
        if (r2 < this.elementData.length) goto L6;
        return r2 - this.elementData.length;
    L6:
        return r2;
    }

    @Override // kotlin.collections.AbstractMutableList, java.util.AbstractList, java.util.List
    public void add(int r11, E r12) {
        AbstractList.Companion.checkPositionIndex$kotlin_stdlib(r11, size());
        if (r11 != size()) goto L6;
        addLast(r12);
        return;
    L6:
        if (r11 != 0) goto L9;
        addFirst(r12);
        return;
    L9:
        ensureCapacity(size() + 1);
        int r0 = positiveMod(this.head + r11);
        if (r11 >= ((size() + 1) >> 1)) goto L16;
        int r2 = decremented(r0);
        int r4 = decremented(this.head);
        if (r2 < this.head) goto L14;
        this.elementData[r4] = this.elementData[this.head];
        ArraysKt.copyInto(this.elementData, this.elementData, this.head, this.head + 1, r2 + 1);
    L15:
        this.elementData[r2] = r12;
        this.head = r4;
    L21:
        this.size = size() + 1;
        return;
    L14:
        ArraysKt.copyInto(this.elementData, this.elementData, this.head - 1, this.head, this.elementData.length);
        this.elementData[this.elementData.length - 1] = this.elementData[0];
        ArraysKt.copyInto(this.elementData, this.elementData, 0, 1, r2 + 1);
        goto L15
    L16:
        int r3 = positiveMod(this.head + size());
        if (r0 >= r3) goto L19;
        ArraysKt.copyInto(this.elementData, this.elementData, r0 + 1, r0, r3);
    L20:
        this.elementData[r0] = r12;
        goto L21
    L19:
        ArraysKt.copyInto(this.elementData, this.elementData, 1, 0, r3);
        this.elementData[0] = this.elementData[this.elementData.length - 1];
        ArraysKt.copyInto(this.elementData, this.elementData, r0 + 1, r0, this.elementData.length - 1);
        goto L20
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean add(E r2) {
        addLast(r2);
        return true;
    }

    @Override // java.util.AbstractList, java.util.List
    public boolean addAll(int r13, Collection<? extends E> r14) {
        Intrinsics.checkNotNullParameter(r14, "elements");
        AbstractList.Companion.checkPositionIndex$kotlin_stdlib(r13, size());
        if (r14.isEmpty() == false) goto L6;
        return false;
    L6:
        if (r13 == size()) goto L8;
        ensureCapacity(size() + r14.size());
        int r0 = positiveMod(this.head + size());
        int r2 = positiveMod(this.head + r13);
        int r3 = r14.size();
        if (r13 >= ((size() + 1) >> 1)) goto L24;
        int r4 = this.head - r3;
        if (r2 < this.head) goto L19;
        if (r4 < 0) goto L15;
        ArraysKt.copyInto(this.elementData, this.elementData, r4, this.head, r2);
    L23:
        this.head = r4;
        copyCollectionElements(negativeMod(r2 - r3), r14);
    L38:
        return true;
    L15:
        r4 = r4 + this.elementData.length;
        int r6 = r2 - this.head;
        int r7 = this.elementData.length - r4;
        if (r7 < r6) goto L18;
        ArraysKt.copyInto(this.elementData, this.elementData, r4, this.head, r2);
        goto L23
    L18:
        ArraysKt.copyInto(this.elementData, this.elementData, r4, this.head, this.head + r7);
        ArraysKt.copyInto(this.elementData, this.elementData, 0, this.head + r7, r2);
        goto L23
    L19:
        ArraysKt.copyInto(this.elementData, this.elementData, r4, this.head, this.elementData.length);
        if (r3 < r2) goto L22;
        ArraysKt.copyInto(this.elementData, this.elementData, this.elementData.length - r3, 0, r2);
        goto L23
    L22:
        ArraysKt.copyInto(this.elementData, this.elementData, this.elementData.length - r3, 0, r3);
        ArraysKt.copyInto(this.elementData, this.elementData, 0, r3, r2);
        goto L23
    L24:
        int r5 = r2 + r3;
        if (r2 < r0) goto L27;
        ArraysKt.copyInto(this.elementData, this.elementData, r3, 0, r0);
        if (r5 < this.elementData.length) goto L36;
        ArraysKt.copyInto(this.elementData, this.elementData, r5 - this.elementData.length, r2, this.elementData.length);
    L37:
        copyCollectionElements(r2, r14);
        goto L38
    L36:
        ArraysKt.copyInto(this.elementData, this.elementData, 0, this.elementData.length - r3, this.elementData.length);
        ArraysKt.copyInto(this.elementData, this.elementData, r5, r2, this.elementData.length - r3);
        goto L37
    L27:
        if ((r0 + r3) > this.elementData.length) goto L30;
        ArraysKt.copyInto(this.elementData, this.elementData, r5, r2, r0);
        goto L37
    L30:
        if (r5 < this.elementData.length) goto L32;
        ArraysKt.copyInto(this.elementData, this.elementData, r5 - this.elementData.length, r2, r0);
        goto L37
    L32:
        int r8 = (r0 + r3) - this.elementData.length;
        ArraysKt.copyInto(this.elementData, this.elementData, 0, r0 - r8, r0);
        ArraysKt.copyInto(this.elementData, this.elementData, r5, r2, r0 - r8);
        goto L37
    L8:
        return addAll(r14);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean addAll(Collection<? extends E> r3) {
        Intrinsics.checkNotNullParameter(r3, "elements");
        if (r3.isEmpty() == false) goto L6;
        return false;
    L6:
        ensureCapacity(size() + r3.size());
        copyCollectionElements(positiveMod(this.head + size()), r3);
        return true;
    }

    public final void addFirst(E r3) {
        ensureCapacity(size() + 1);
        this.head = decremented(this.head);
        this.elementData[this.head] = r3;
        this.size = size() + 1;
    }

    public final void addLast(E r4) {
        ensureCapacity(size() + 1);
        this.elementData[positiveMod(this.head + size())] = r4;
        this.size = size() + 1;
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public void clear() {
        int r0 = positiveMod(this.head + size());
        if (this.head >= r0) goto L6;
        ArraysKt.fill(this.elementData, null, this.head, r0);
    L8:
        this.head = 0;
        this.size = 0;
        return;
    L6:
        if (isEmpty() == true) goto L8;
        ArraysKt.fill(this.elementData, null, this.head, this.elementData.length);
        ArraysKt.fill(this.elementData, null, 0, r0);
        goto L8
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean contains(Object r3) {
        if (indexOf(r3) == (-1)) goto L5;
        return true;
    L5:
        return false;
    }

    public final E first() {
        if (isEmpty() == true) goto L7;
        return (E) this.elementData[this.head];
    L7:
        throw new NoSuchElementException("ArrayDeque is empty.");
    }

    public final E firstOrNull() {
        if (isEmpty() == false) goto L6;
        return null;
    L6:
        return (E) this.elementData[this.head];
    }

    @Override // java.util.AbstractList, java.util.List
    public E get(int r3) {
        AbstractList.Companion.checkElementIndex$kotlin_stdlib(r3, size());
        return (E) this.elementData[positiveMod(this.head + r3)];
    }

    @Override // kotlin.collections.AbstractMutableList
    public int getSize() {
        return this.size;
    }

    @Override // java.util.AbstractList, java.util.List
    public int indexOf(Object r5) {
        int r0 = positiveMod(this.head + size());
        if (this.head >= r0) goto L12;
        int r1 = this.head;
    L5:
        if (r1 >= r0) goto L35;
        if (Intrinsics.areEqual(r5, this.elementData[r1]) == true) goto L9;
        r1 = r1 + 1;
        goto L5
    L9:
        return r1 - this.head;
    L35:
        return -1;
    L12:
        if (this.head < r0) goto L27;
        int r2 = this.head;
        int r3 = this.elementData.length;
    L14:
        if (r2 >= r3) goto L20;
        if (Intrinsics.areEqual(r5, this.elementData[r2]) == true) goto L18;
        r2 = r2 + 1;
        goto L14
    L18:
        return r2 - this.head;
    L20:
        int r4 = 0;
    L21:
        if (r4 >= r0) goto L36;
        if (Intrinsics.areEqual(r5, this.elementData[r4]) == true) goto L25;
        r4 = r4 + 1;
        goto L21
    L25:
        return (this.elementData.length + r4) - this.head;
    L36:
        return -1;
    L27:
        return -1;
    }

    public final void internalStructure$kotlin_stdlib(Function2<? super Integer, ? super Object[], Unit> r5) {
        Intrinsics.checkNotNullParameter(r5, "structure");
        int r0 = positiveMod(this.head + size());
        if (isEmpty() == false) goto L5;
    L8:
        int r1 = this.head;
    L9:
        r5.invoke(Integer.valueOf(r1), toArray());
        return;
    L5:
        if (this.head < r0) goto L8;
        r1 = this.head - this.elementData.length;
        goto L9
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean isEmpty() {
        if (size() != 0) goto L5;
        return true;
    L5:
        return false;
    }

    public final E last() {
        if (isEmpty() == true) goto L7;
        return (E) this.elementData[positiveMod(this.head + CollectionsKt.getLastIndex(this))];
    L7:
        throw new NoSuchElementException("ArrayDeque is empty.");
    }

    @Override // java.util.AbstractList, java.util.List
    public int lastIndexOf(Object r6) {
        int r0 = positiveMod(this.head + size());
        if (this.head >= r0) goto L13;
        int r1 = r0 - 1;
        int r3 = this.head;
        if (r3 <= r1) goto L7;
    L29:
        return -1;
    L7:
        if (Intrinsics.areEqual(r6, this.elementData[r1]) == true) goto L9;
        if (r1 == r3) goto L29;
        r1 = r1 - 1;
        goto L7
    L9:
        return r1 - this.head;
    L13:
        if (this.head <= r0) goto L29;
        int r2 = r0 - 1;
    L15:
        if ((-1) >= r2) goto L21;
        if (Intrinsics.areEqual(r6, this.elementData[r2]) == true) goto L19;
        r2 = r2 - 1;
        goto L15
    L19:
        return (this.elementData.length + r2) - this.head;
    L21:
        int r4 = ArraysKt.getLastIndex(this.elementData);
        int r5 = this.head;
        if (r5 > r4) goto L29;
    L24:
        if (Intrinsics.areEqual(r6, this.elementData[r4]) == true) goto L26;
        if (r4 == r5) goto L29;
        r4 = r4 - 1;
        goto L24
    L26:
        return r4 - this.head;
    }

    public final E lastOrNull() {
        if (isEmpty() == false) goto L6;
        return null;
    L6:
        return (E) this.elementData[positiveMod(this.head + CollectionsKt.getLastIndex(this))];
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean remove(Object r3) {
        int r0 = indexOf(r3);
        if (r0 != (-1)) goto L6;
        return false;
    L6:
        remove(r0);
        return true;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean removeAll(Collection<? extends Object> r14) {
        Intrinsics.checkNotNullParameter(r14, "elements");
        boolean r3 = false;
        if (isEmpty() == false) goto L5;
    L39:
        return r3;
    L5:
        if (this.elementData.length != 0) goto L7;
        boolean r2 = true;
    L8:
        if (r2 == true) goto L39;
        int r4 = positiveMod(this.head + size());
        int r5 = this.head;
        boolean r6 = false;
        if (this.head >= r4) goto L20;
        int r7 = this.head;
    L13:
        if (r7 >= r4) goto L19;
        Object r8 = this.elementData[r7];
        if (r14.contains(r8) == true) goto L17;
        this.elementData[r5] = r8;
        r5 = r5 + 1;
    L18:
        r7 = r7 + 1;
        goto L13
    L17:
        r6 = true;
        goto L18
    L19:
        ArraysKt.fill(this.elementData, null, r5, r4);
        boolean r12 = r6;
        int r9 = r5;
        r3 = r12;
    L35:
        if (r3 == false) goto L39;
        this.size = negativeMod(r9 - this.head);
        goto L39
    L20:
        int r10 = this.head;
        int r11 = this.elementData.length;
    L21:
        if (r10 >= r11) goto L27;
        Object r13 = this.elementData[r10];
        this.elementData[r10] = null;
        if (r14.contains(r13) == true) goto L25;
        this.elementData[r5] = r13;
        r5 = r5 + 1;
    L26:
        r10 = r10 + 1;
        goto L21
    L25:
        r6 = true;
        goto L26
    L27:
        int r15 = positiveMod(r5);
        int r16 = 0;
    L28:
        if (r16 >= r4) goto L34;
        Object r17 = this.elementData[r16];
        this.elementData[r16] = null;
        if (r14.contains(r17) == true) goto L32;
        this.elementData[r15] = r17;
        r15 = incremented(r15);
    L33:
        r16 = r16 + 1;
        goto L28
    L32:
        r6 = true;
        goto L33
    L34:
        boolean r18 = r6;
        r9 = r15;
        r3 = r18;
        goto L35
    L7:
        r2 = false;
        goto L8
    }

    @Override // kotlin.collections.AbstractMutableList
    public E removeAt(int r11) {
        AbstractList.Companion.checkElementIndex$kotlin_stdlib(r11, size());
        if (r11 == CollectionsKt.getLastIndex(this)) goto L5;
        if (r11 == 0) goto L8;
        int r0 = positiveMod(this.head + r11);
        E r1 = (E) this.elementData[r0];
        if (r11 < (size() >> 1)) goto L12;
        int r2 = positiveMod(this.head + CollectionsKt.getLastIndex(this));
        if (r0 > r2) goto L19;
        ArraysKt.copyInto(this.elementData, this.elementData, r0, r0 + 1, r2 + 1);
    L20:
        this.elementData[r2] = null;
    L21:
        this.size = size() - 1;
        return r1;
    L19:
        ArraysKt.copyInto(this.elementData, this.elementData, r0, r0 + 1, this.elementData.length);
        this.elementData[this.elementData.length - 1] = this.elementData[0];
        ArraysKt.copyInto(this.elementData, this.elementData, 0, 1, r2 + 1);
        goto L20
    L12:
        if (r0 < this.head) goto L14;
        ArraysKt.copyInto(this.elementData, this.elementData, this.head + 1, this.head, r0);
    L15:
        this.elementData[this.head] = null;
        this.head = incremented(this.head);
        goto L21
    L14:
        ArraysKt.copyInto(this.elementData, this.elementData, 1, 0, r0);
        this.elementData[0] = this.elementData[this.elementData.length - 1];
        ArraysKt.copyInto(this.elementData, this.elementData, this.head + 1, this.head, this.elementData.length - 1);
        goto L15
    L8:
        return removeFirst();
    L5:
        return removeLast();
    }

    public final E removeFirst() {
        if (isEmpty() == true) goto L7;
        E r0 = (E) this.elementData[this.head];
        this.elementData[this.head] = null;
        this.head = incremented(this.head);
        this.size = size() - 1;
        return r0;
    L7:
        throw new NoSuchElementException("ArrayDeque is empty.");
    }

    public final E removeFirstOrNull() {
        if (isEmpty() == false) goto L6;
        return null;
    L6:
        return removeFirst();
    }

    public final E removeLast() {
        if (isEmpty() == true) goto L7;
        int r0 = positiveMod(this.head + CollectionsKt.getLastIndex(this));
        E r1 = (E) this.elementData[r0];
        this.elementData[r0] = null;
        this.size = size() - 1;
        return r1;
    L7:
        throw new NoSuchElementException("ArrayDeque is empty.");
    }

    public final E removeLastOrNull() {
        if (isEmpty() == false) goto L6;
        return null;
    L6:
        return removeLast();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean retainAll(Collection<? extends Object> r13) {
        Intrinsics.checkNotNullParameter(r13, "elements");
        boolean r3 = false;
        if (isEmpty() == false) goto L5;
    L39:
        return r3;
    L5:
        if (this.elementData.length != 0) goto L7;
        boolean r2 = true;
    L8:
        if (r2 == true) goto L39;
        int r4 = positiveMod(this.head + size());
        int r5 = this.head;
        boolean r6 = false;
        if (this.head >= r4) goto L20;
        int r7 = this.head;
    L13:
        if (r7 >= r4) goto L19;
        Object r8 = this.elementData[r7];
        if (r13.contains(r8) == false) goto L17;
        this.elementData[r5] = r8;
        r5 = r5 + 1;
    L18:
        r7 = r7 + 1;
        goto L13
    L17:
        r6 = true;
        goto L18
    L19:
        ArraysKt.fill(this.elementData, null, r5, r4);
        boolean r11 = r6;
        int r9 = r5;
        r3 = r11;
    L35:
        if (r3 == false) goto L39;
        this.size = negativeMod(r9 - this.head);
        goto L39
    L20:
        int r10 = this.head;
        int r12 = this.elementData.length;
    L21:
        if (r10 >= r12) goto L27;
        Object r14 = this.elementData[r10];
        this.elementData[r10] = null;
        if (r13.contains(r14) == false) goto L25;
        this.elementData[r5] = r14;
        r5 = r5 + 1;
    L26:
        r10 = r10 + 1;
        goto L21
    L25:
        r6 = true;
        goto L26
    L27:
        int r15 = positiveMod(r5);
        int r16 = 0;
    L28:
        if (r16 >= r4) goto L34;
        Object r17 = this.elementData[r16];
        this.elementData[r16] = null;
        if (r13.contains(r17) == false) goto L32;
        this.elementData[r15] = r17;
        r15 = incremented(r15);
    L33:
        r16 = r16 + 1;
        goto L28
    L32:
        r6 = true;
        goto L33
    L34:
        boolean r18 = r6;
        r9 = r15;
        r3 = r18;
        goto L35
    L7:
        r2 = false;
        goto L8
    }

    @Override // kotlin.collections.AbstractMutableList, java.util.AbstractList, java.util.List
    public E set(int r4, E r5) {
        AbstractList.Companion.checkElementIndex$kotlin_stdlib(r4, size());
        int r0 = positiveMod(this.head + r4);
        E r1 = (E) this.elementData[r0];
        this.elementData[r0] = r5;
        return r1;
    }

    public final Object[] testToArray$kotlin_stdlib() {
        return toArray();
    }

    public final <T> T[] testToArray$kotlin_stdlib(T[] r2) {
        Intrinsics.checkNotNullParameter(r2, "array");
        return (T[]) toArray(r2);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public Object[] toArray() {
        return toArray(new Object[size()]);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public <T> T[] toArray(T[] r9) {
        Intrinsics.checkNotNullParameter(r9, "array");
        if (r9.length < size()) goto L5;
        Object[] r0 = r9;
    L6:
        Object[] r2 = r0;
        int r5 = positiveMod(this.head + size());
        if (this.head >= r5) goto L10;
        ArraysKt.copyInto$default(this.elementData, r2, 0, this.head, r5, 2, null);
    L13:
        return (T[]) CollectionsKt.terminateCollectionToArray(size(), r2);
    L10:
        if (isEmpty() == true) goto L13;
        ArraysKt.copyInto(this.elementData, r2, 0, this.head, this.elementData.length);
        ArraysKt.copyInto(this.elementData, r2, this.elementData.length - this.head, 0, r5);
        goto L13
    L5:
        r0 = ArraysKt.arrayOfNulls(r9, size());
        goto L6
    }
}
