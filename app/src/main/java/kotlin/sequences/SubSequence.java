package kotlin.sequences;

import androidx.exifinterface.media.ExifInterface;
import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;

/* JADX INFO: compiled from: Sequences.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010(\n\u0002\b\u0002\b\u0000\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003B#\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0006¢\u0006\u0002\u0010\bJ\u0016\u0010\f\u001a\b\u0012\u0004\u0012\u00028\u00000\u00022\u0006\u0010\r\u001a\u00020\u0006H\u0016J\u000f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00028\u00000\u000fH\u0096\u0002J\u0016\u0010\u0010\u001a\b\u0012\u0004\u0012\u00028\u00000\u00022\u0006\u0010\r\u001a\u00020\u0006H\u0016R\u0014\u0010\t\u001a\u00020\u00068BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0011"}, m421d2 = {"Lkotlin/sequences/SubSequence;", ExifInterface.GPS_DIRECTION_TRUE, "Lkotlin/sequences/Sequence;", "Lkotlin/sequences/DropTakeSequence;", "sequence", "startIndex", "", "endIndex", "(Lkotlin/sequences/Sequence;II)V", "count", "getCount", "()I", "drop", "n", "iterator", "", "take", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public final class SubSequence<T> implements Sequence<T>, DropTakeSequence<T> {
    private final int endIndex;
    private final Sequence<T> sequence;
    private final int startIndex;

    /* JADX INFO: renamed from: kotlin.sequences.SubSequence$iterator$1 */
    /* JADX INFO: compiled from: Sequences.kt */
    @Metadata(m420d1 = {"\u0000#\n\u0000\n\u0002\u0010(\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\b\u0010\u000b\u001a\u00020\fH\u0002J\t\u0010\r\u001a\u00020\u000eH\u0096\u0002J\u000e\u0010\u000f\u001a\u00028\u0000H\u0096\u0002¢\u0006\u0002\u0010\u0010R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00028\u00000\u0001¢\u0006\b\n\u0000\u001a\u0004\b\u0003\u0010\u0004R\u001a\u0010\u0005\u001a\u00020\u0006X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\n¨\u0006\u0011"}, m421d2 = {"kotlin/sequences/SubSequence$iterator$1", "", "iterator", "getIterator", "()Ljava/util/Iterator;", "position", "", "getPosition", "()I", "setPosition", "(I)V", "drop", "", "hasNext", "", "next", "()Ljava/lang/Object;", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
    public static final class C11501 implements Iterator<T>, KMappedMarker {
        private final Iterator<T> iterator;
        private int position;
        final /* synthetic */ SubSequence<T> this$0;

        C11501(SubSequence<T> r2) {
            this.this$0 = r2;
            this.iterator = SubSequence.access$getSequence$p(r2).iterator();
        }

        private final void drop() {
        L3:
            if (this.position >= SubSequence.access$getStartIndex$p(this.this$0)) goto L7;
            if (this.iterator.hasNext() == false) goto L10;
            this.iterator.next();
            this.position++;
            goto L3
        L10:
            return;
        }

        public final Iterator<T> getIterator() {
            return this.iterator;
        }

        public final int getPosition() {
            return this.position;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            drop();
            if (this.position < SubSequence.access$getEndIndex$p(this.this$0)) goto L5;
        L7:
            return false;
        L5:
            if (this.iterator.hasNext() == false) goto L7;
            return true;
        }

        @Override // java.util.Iterator
        public T next() {
            drop();
            if (this.position >= SubSequence.access$getEndIndex$p(this.this$0)) goto L7;
            this.position++;
            return this.iterator.next();
        L7:
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }

        public final void setPosition(int r1) {
            this.position = r1;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public SubSequence(Sequence<? extends T> r5, int r6, int r7) {
        Intrinsics.checkNotNullParameter(r5, "sequence");
        this.sequence = r5;
        this.startIndex = r6;
        this.endIndex = r7;
        boolean r1 = true;
        if (this.startIndex < 0) goto L5;
        boolean r0 = true;
    L6:
        if (r0 == false) goto L23;
        if (this.endIndex < 0) goto L10;
        boolean r2 = true;
    L11:
        if (r2 == false) goto L21;
        if (this.endIndex >= this.startIndex) goto L16;
        r1 = false;
    L16:
        if (r1 == false) goto L19;
        return;
    L19:
        throw new IllegalArgumentException(("endIndex should be not less than startIndex, but was " + this.endIndex + " < " + this.startIndex).toString());
    L21:
        throw new IllegalArgumentException(("endIndex should be non-negative, but is " + this.endIndex).toString());
    L10:
        r2 = false;
        goto L11
    L23:
        throw new IllegalArgumentException(("startIndex should be non-negative, but is " + this.startIndex).toString());
    L5:
        r0 = false;
        goto L6
    }

    public static final /* synthetic */ int access$getEndIndex$p(SubSequence r1) {
        return r1.endIndex;
    }

    public static final /* synthetic */ Sequence access$getSequence$p(SubSequence r1) {
        return r1.sequence;
    }

    public static final /* synthetic */ int access$getStartIndex$p(SubSequence r1) {
        return r1.startIndex;
    }

    private final int getCount() {
        return this.endIndex - this.startIndex;
    }

    @Override // kotlin.sequences.DropTakeSequence
    public Sequence<T> drop(int r5) {
        if (r5 < getCount()) goto L6;
        return SequencesKt.emptySequence();
    L6:
        return new SubSequence(this.sequence, this.startIndex + r5, this.endIndex);
    }

    @Override // kotlin.sequences.Sequence
    public Iterator<T> iterator() {
        return new C11501(this);
    }

    @Override // kotlin.sequences.DropTakeSequence
    public Sequence<T> take(int r5) {
        if (r5 < getCount()) goto L6;
        return this;
    L6:
        return new SubSequence(this.sequence, this.startIndex, this.startIndex + r5);
    }
}
