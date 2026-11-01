package kotlin.sequences;

import androidx.exifinterface.media.ExifInterface;
import java.util.Iterator;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;

/* JADX INFO: compiled from: Sequences.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010(\n\u0000\b\u0000\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B'\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002\u0012\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u00060\u0005¢\u0006\u0002\u0010\u0007J\u000f\u0010\b\u001a\b\u0012\u0004\u0012\u00028\u00000\tH\u0096\u0002R\u001a\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\n"}, m421d2 = {"Lkotlin/sequences/DropWhileSequence;", ExifInterface.GPS_DIRECTION_TRUE, "Lkotlin/sequences/Sequence;", "sequence", "predicate", "Lkotlin/Function1;", "", "(Lkotlin/sequences/Sequence;Lkotlin/jvm/functions/Function1;)V", "iterator", "", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public final class DropWhileSequence<T> implements Sequence<T> {
    private final Function1<T, Boolean> predicate;
    private final Sequence<T> sequence;

    /* JADX INFO: renamed from: kotlin.sequences.DropWhileSequence$iterator$1 */
    /* JADX INFO: compiled from: Sequences.kt */
    @Metadata(m420d1 = {"\u0000!\n\u0000\n\u0002\u0010(\n\u0000\n\u0002\u0010\b\n\u0002\b\u000e\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\b\u0010\u0011\u001a\u00020\u0012H\u0002J\t\u0010\u0013\u001a\u00020\u0014H\u0096\u0002J\u000e\u0010\u0015\u001a\u00028\u0000H\u0096\u0002¢\u0006\u0002\u0010\rR\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0004\u0010\u0005\"\u0004\b\u0006\u0010\u0007R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00028\u00000\u0001¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u001e\u0010\u000b\u001a\u0004\u0018\u00018\u0000X\u0086\u000e¢\u0006\u0010\n\u0002\u0010\u0010\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000f¨\u0006\u0016"}, m421d2 = {"kotlin/sequences/DropWhileSequence$iterator$1", "", "dropState", "", "getDropState", "()I", "setDropState", "(I)V", "iterator", "getIterator", "()Ljava/util/Iterator;", "nextItem", "getNextItem", "()Ljava/lang/Object;", "setNextItem", "(Ljava/lang/Object;)V", "Ljava/lang/Object;", "drop", "", "hasNext", "", "next", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
    public static final class C11091 implements Iterator<T>, KMappedMarker {
        private int dropState;
        private final Iterator<T> iterator;
        private T nextItem;
        final /* synthetic */ DropWhileSequence<T> this$0;

        C11091(DropWhileSequence<T> r2) {
            this.this$0 = r2;
            this.iterator = DropWhileSequence.access$getSequence$p(r2).iterator();
            this.dropState = -1;
        }

        private final void drop() {
        L3:
            if (this.iterator.hasNext() == false) goto L8;
            T r0 = this.iterator.next();
            if (((Boolean) DropWhileSequence.access$getPredicate$p(this.this$0).invoke(r0)).booleanValue() == true) goto L3;
            this.nextItem = r0;
            this.dropState = 1;
            return;
        L8:
            this.dropState = 0;
        }

        public final int getDropState() {
            return this.dropState;
        }

        public final Iterator<T> getIterator() {
            return this.iterator;
        }

        public final T getNextItem() {
            return this.nextItem;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.dropState != (-1)) goto L6;
            drop();
        L6:
            if (this.dropState != 1) goto L8;
            return true;
        L8:
            if (this.iterator.hasNext() == true) goto L13;
            return false;
        L13:
            return true;
        }

        @Override // java.util.Iterator
        public T next() {
            if (this.dropState != (-1)) goto L6;
            drop();
        L6:
            if (this.dropState != 1) goto L10;
            T r0 = this.nextItem;
            this.nextItem = null;
            this.dropState = 0;
            return r0;
        L10:
            return this.iterator.next();
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }

        public final void setDropState(int r1) {
            this.dropState = r1;
        }

        public final void setNextItem(T r1) {
            this.nextItem = r1;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public DropWhileSequence(Sequence<? extends T> r2, Function1<? super T, Boolean> r3) {
        Intrinsics.checkNotNullParameter(r2, "sequence");
        Intrinsics.checkNotNullParameter(r3, "predicate");
        this.sequence = r2;
        this.predicate = r3;
    }

    public static final /* synthetic */ Function1 access$getPredicate$p(DropWhileSequence r1) {
        return r1.predicate;
    }

    public static final /* synthetic */ Sequence access$getSequence$p(DropWhileSequence r1) {
        return r1.sequence;
    }

    @Override // kotlin.sequences.Sequence
    public Iterator<T> iterator() {
        return new C11091(this);
    }
}
