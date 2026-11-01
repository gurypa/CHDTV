package kotlin.sequences;

import androidx.exifinterface.media.ExifInterface;
import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;

/* JADX INFO: compiled from: Sequences.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010(\n\u0000\b\u0000\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B1\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u00050\u0007¢\u0006\u0002\u0010\bJ\u000f\u0010\t\u001a\b\u0012\u0004\u0012\u00028\u00000\nH\u0096\u0002R\u001a\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u00050\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u000b"}, m421d2 = {"Lkotlin/sequences/FilteringSequence;", ExifInterface.GPS_DIRECTION_TRUE, "Lkotlin/sequences/Sequence;", "sequence", "sendWhen", "", "predicate", "Lkotlin/Function1;", "(Lkotlin/sequences/Sequence;ZLkotlin/jvm/functions/Function1;)V", "iterator", "", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public final class FilteringSequence<T> implements Sequence<T> {
    private final Function1<T, Boolean> predicate;
    private final boolean sendWhen;
    private final Sequence<T> sequence;

    /* JADX INFO: renamed from: kotlin.sequences.FilteringSequence$iterator$1 */
    /* JADX INFO: compiled from: Sequences.kt */
    @Metadata(m420d1 = {"\u0000#\n\u0000\n\u0002\u0010(\n\u0002\b\n\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\b\u0010\u0011\u001a\u00020\u0012H\u0002J\t\u0010\u0013\u001a\u00020\u0014H\u0096\u0002J\u000e\u0010\u0015\u001a\u00028\u0000H\u0096\u0002¢\u0006\u0002\u0010\u0007R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00028\u00000\u0001¢\u0006\b\n\u0000\u001a\u0004\b\u0003\u0010\u0004R\u001e\u0010\u0005\u001a\u0004\u0018\u00018\u0000X\u0086\u000e¢\u0006\u0010\n\u0002\u0010\n\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR\u001a\u0010\u000b\u001a\u00020\fX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010¨\u0006\u0016"}, m421d2 = {"kotlin/sequences/FilteringSequence$iterator$1", "", "iterator", "getIterator", "()Ljava/util/Iterator;", "nextItem", "getNextItem", "()Ljava/lang/Object;", "setNextItem", "(Ljava/lang/Object;)V", "Ljava/lang/Object;", "nextState", "", "getNextState", "()I", "setNextState", "(I)V", "calcNext", "", "hasNext", "", "next", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
    public static final class C11101 implements Iterator<T>, KMappedMarker {
        private final Iterator<T> iterator;
        private T nextItem;
        private int nextState;
        final /* synthetic */ FilteringSequence<T> this$0;

        C11101(FilteringSequence<T> r2) {
            this.this$0 = r2;
            this.iterator = FilteringSequence.access$getSequence$p(r2).iterator();
            this.nextState = -1;
        }

        private final void calcNext() {
        L3:
            if (this.iterator.hasNext() == false) goto L8;
            T r0 = this.iterator.next();
            if (((Boolean) FilteringSequence.access$getPredicate$p(this.this$0).invoke(r0)).booleanValue() != FilteringSequence.access$getSendWhen$p(this.this$0)) goto L3;
            this.nextItem = r0;
            this.nextState = 1;
            return;
        L8:
            this.nextState = 0;
        }

        public final Iterator<T> getIterator() {
            return this.iterator;
        }

        public final T getNextItem() {
            return this.nextItem;
        }

        public final int getNextState() {
            return this.nextState;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.nextState != (-1)) goto L6;
            calcNext();
        L6:
            if (this.nextState == 1) goto L10;
            return false;
        L10:
            return true;
        }

        @Override // java.util.Iterator
        public T next() {
            if (this.nextState != (-1)) goto L6;
            calcNext();
        L6:
            if (this.nextState == 0) goto L10;
            T r0 = this.nextItem;
            this.nextItem = null;
            this.nextState = -1;
            return r0;
        L10:
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }

        public final void setNextItem(T r1) {
            this.nextItem = r1;
        }

        public final void setNextState(int r1) {
            this.nextState = r1;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public FilteringSequence(Sequence<? extends T> r2, boolean r3, Function1<? super T, Boolean> r4) {
        Intrinsics.checkNotNullParameter(r2, "sequence");
        Intrinsics.checkNotNullParameter(r4, "predicate");
        this.sequence = r2;
        this.sendWhen = r3;
        this.predicate = r4;
    }

    public /* synthetic */ FilteringSequence(Sequence r1, boolean r2, Function1 r3, int r4, DefaultConstructorMarker r5) {
        if ((r4 & 2) == 0) goto L5;
        r2 = true;
    L5:
        this(r1, r2, r3);
    }

    public static final /* synthetic */ Function1 access$getPredicate$p(FilteringSequence r1) {
        return r1.predicate;
    }

    public static final /* synthetic */ boolean access$getSendWhen$p(FilteringSequence r1) {
        return r1.sendWhen;
    }

    public static final /* synthetic */ Sequence access$getSequence$p(FilteringSequence r1) {
        return r1.sequence;
    }

    @Override // kotlin.sequences.Sequence
    public Iterator<T> iterator() {
        return new C11101(this);
    }
}
