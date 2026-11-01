package kotlin.collections;

import androidx.exifinterface.media.ExifInterface;
import com.arthenica.ffmpegkit.MediaInformation;
import com.arthenica.ffmpegkit.StreamInformation;
import java.util.Arrays;
import java.util.Iterator;
import java.util.RandomAccess;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

/* JADX INFO: compiled from: SlidingWindow.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0002\b\t\n\u0002\u0010\u0002\n\u0002\b\b\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010(\n\u0002\b\b\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\u00060\u0003j\u0002`\u0004B\u000f\b\u0016\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007B\u001d\u0012\u000e\u0010\b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\t\u0012\u0006\u0010\u000b\u001a\u00020\u0006¢\u0006\u0002\u0010\fJ\u0013\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00028\u0000¢\u0006\u0002\u0010\u0016J\u0014\u0010\u0017\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002\u0006\u0010\u0018\u001a\u00020\u0006J\u0016\u0010\u0019\u001a\u00028\u00002\u0006\u0010\u001a\u001a\u00020\u0006H\u0096\u0002¢\u0006\u0002\u0010\u001bJ\u0006\u0010\u001c\u001a\u00020\u001dJ\u000f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00028\u00000\u001fH\u0096\u0002J\u000e\u0010 \u001a\u00020\u00142\u0006\u0010!\u001a\u00020\u0006J\u0015\u0010\"\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\tH\u0014¢\u0006\u0002\u0010#J'\u0010\"\u001a\b\u0012\u0004\u0012\u0002H\u00010\t\"\u0004\b\u0001\u0010\u00012\f\u0010$\u001a\b\u0012\u0004\u0012\u0002H\u00010\tH\u0014¢\u0006\u0002\u0010%J\u0015\u0010&\u001a\u00020\u0006*\u00020\u00062\u0006\u0010!\u001a\u00020\u0006H\u0082\bR\u0018\u0010\b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\tX\u0082\u0004¢\u0006\u0004\n\u0002\u0010\rR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u001e\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u000e\u001a\u00020\u0006@RX\u0096\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u000e\u0010\u0012\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006'"}, m421d2 = {"Lkotlin/collections/RingBuffer;", ExifInterface.GPS_DIRECTION_TRUE, "Lkotlin/collections/AbstractList;", "Ljava/util/RandomAccess;", "Lkotlin/collections/RandomAccess;", "capacity", "", "(I)V", "buffer", "", "", "filledSize", "([Ljava/lang/Object;I)V", "[Ljava/lang/Object;", "<set-?>", MediaInformation.KEY_SIZE, "getSize", "()I", "startIndex", "add", "", "element", "(Ljava/lang/Object;)V", "expanded", "maxCapacity", "get", StreamInformation.KEY_INDEX, "(I)Ljava/lang/Object;", "isFull", "", "iterator", "", "removeFirst", "n", "toArray", "()[Ljava/lang/Object;", "array", "([Ljava/lang/Object;)[Ljava/lang/Object;", "forward", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
final class RingBuffer<T> extends AbstractList<T> implements RandomAccess {
    private final Object[] buffer;
    private final int capacity;
    private int size;
    private int startIndex;

    public RingBuffer(int r3) {
        this(new Object[r3], 0);
    }

    public RingBuffer(Object[] r4, int r5) {
        Intrinsics.checkNotNullParameter(r4, "buffer");
        this.buffer = r4;
        boolean r0 = true;
        if (r5 < 0) goto L5;
        boolean r2 = true;
    L6:
        if (r2 == false) goto L17;
        if (r5 <= this.buffer.length) goto L11;
        r0 = false;
    L11:
        if (r0 == false) goto L15;
        this.capacity = this.buffer.length;
        this.size = r5;
        return;
    L15:
        throw new IllegalArgumentException(("ring buffer filled size: " + r5 + " cannot be larger than the buffer size: " + this.buffer.length).toString());
    L17:
        throw new IllegalArgumentException(("ring buffer filled size should not be negative but it is " + r5).toString());
    L5:
        r2 = false;
        goto L6
    }

    public static final /* synthetic */ Object[] access$getBuffer$p(RingBuffer r1) {
        return r1.buffer;
    }

    public static final /* synthetic */ int access$getCapacity$p(RingBuffer r1) {
        return r1.capacity;
    }

    public static final /* synthetic */ int access$getStartIndex$p(RingBuffer r1) {
        return r1.startIndex;
    }

    private final int forward(int r4, int r5) {
        return (r4 + r5) % access$getCapacity$p(this);
    }

    @Override // java.util.Collection, java.util.List
    public final void add(T r8) {
        if (isFull() == true) goto L7;
        this.buffer[(this.startIndex + size()) % access$getCapacity$p(this)] = r8;
        this.size = size() + 1;
        return;
    L7:
        throw new IllegalStateException("ring buffer is full");
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final RingBuffer<T> expanded(int r5) {
        int r0 = RangesKt.coerceAtMost((this.capacity + (this.capacity >> 1)) + 1, r5);
        if (this.startIndex != 0) goto L5;
        Object[] r1 = Arrays.copyOf(this.buffer, r0);
        Intrinsics.checkNotNullExpressionValue(r1, "copyOf(...)");
    L7:
        return new RingBuffer(r1, size());
    L5:
        r1 = toArray(new Object[r0]);
        goto L7
    }

    @Override // kotlin.collections.AbstractList, java.util.List
    public T get(int r7) {
        AbstractList.Companion.checkElementIndex$kotlin_stdlib(r7, size());
        return (T) this.buffer[(this.startIndex + r7) % access$getCapacity$p(this)];
    }

    @Override // kotlin.collections.AbstractList, kotlin.collections.AbstractCollection
    public int getSize() {
        return this.size;
    }

    public final boolean isFull() {
        if (size() != this.capacity) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // kotlin.collections.AbstractList, kotlin.collections.AbstractCollection, java.util.Collection, java.lang.Iterable
    public Iterator<T> iterator() {
        return new C10691(this);
    }

    public final void removeFirst(int r8) {
        boolean r0 = true;
        if (r8 < 0) goto L5;
        boolean r2 = true;
    L6:
        if (r2 == false) goto L22;
        if (r8 <= size()) goto L11;
        r0 = false;
    L11:
        if (r0 == false) goto L20;
        if (r8 <= 0) goto L23;
        int r1 = this.startIndex;
        int r5 = (r1 + r8) % access$getCapacity$p(this);
        if (r1 <= r5) goto L16;
        ArraysKt.fill(this.buffer, null, r1, this.capacity);
        ArraysKt.fill(this.buffer, null, 0, r5);
    L17:
        this.startIndex = r5;
        this.size = size() - r8;
        return;
    L16:
        ArraysKt.fill(this.buffer, null, r1, r5);
        goto L17
    L23:
        return;
    L20:
        throw new IllegalArgumentException(("n shouldn't be greater than the buffer size: n = " + r8 + ", size = " + size()).toString());
    L22:
        throw new IllegalArgumentException(("n shouldn't be negative but it is " + r8).toString());
    L5:
        r2 = false;
        goto L6
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // kotlin.collections.AbstractCollection, java.util.Collection
    public Object[] toArray() {
        return toArray(new Object[size()]);
    }

    @Override // kotlin.collections.AbstractCollection, java.util.Collection
    public <T> T[] toArray(T[] r6) {
        Intrinsics.checkNotNullParameter(r6, "array");
        if (r6.length >= size()) goto L5;
        Object[] r0 = Arrays.copyOf(r6, size());
        Intrinsics.checkNotNullExpressionValue(r0, "copyOf(...)");
    L6:
        int r1 = size();
        int r2 = 0;
        int r3 = this.startIndex;
    L7:
        if (r2 >= r1) goto L11;
        if (r3 >= this.capacity) goto L11;
        r0[r2] = this.buffer[r3];
        r2 = r2 + 1;
        r3 = r3 + 1;
    L11:
        int r4 = 0;
    L12:
        if (r2 >= r1) goto L15;
        r0[r2] = this.buffer[r4];
        r2 = r2 + 1;
        r4 = r4 + 1;
        goto L12
    L15:
        return (T[]) CollectionsKt.terminateCollectionToArray(r1, r0);
    L5:
        r0 = r6;
        goto L6
    }
}
