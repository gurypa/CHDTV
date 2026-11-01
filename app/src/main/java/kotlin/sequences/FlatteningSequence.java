package kotlin.sequences;

import androidx.exifinterface.media.ExifInterface;
import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;

/* JADX INFO: compiled from: Sequences.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010(\n\u0002\b\u0002\b\u0000\u0018\u0000*\u0004\b\u0000\u0010\u0001*\u0004\b\u0001\u0010\u0002*\u0004\b\u0002\u0010\u00032\b\u0012\u0004\u0012\u0002H\u00030\u0004BA\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004\u0012\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0007\u0012\u0018\u0010\b\u001a\u0014\u0012\u0004\u0012\u00028\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00020\t0\u0007¢\u0006\u0002\u0010\nJ\u000f\u0010\b\u001a\b\u0012\u0004\u0012\u00028\u00020\tH\u0096\u0002R \u0010\b\u001a\u0014\u0012\u0004\u0012\u00028\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00020\t0\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0007X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u000b"}, m421d2 = {"Lkotlin/sequences/FlatteningSequence;", ExifInterface.GPS_DIRECTION_TRUE, "R", ExifInterface.LONGITUDE_EAST, "Lkotlin/sequences/Sequence;", "sequence", "transformer", "Lkotlin/Function1;", "iterator", "", "(Lkotlin/sequences/Sequence;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;)V", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public final class FlatteningSequence<T, R, E> implements Sequence<E> {
    private final Function1<R, Iterator<E>> iterator;
    private final Sequence<T> sequence;
    private final Function1<T, R> transformer;

    /* JADX INFO: renamed from: kotlin.sequences.FlatteningSequence$iterator$1 */
    /* JADX INFO: compiled from: Sequences.kt */
    @Metadata(m420d1 = {"\u0000\u0015\n\u0000\n\u0002\u0010(\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\b\u0004*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\b\u0010\t\u001a\u00020\nH\u0002J\t\u0010\u000b\u001a\u00020\nH\u0096\u0002J\u000e\u0010\f\u001a\u00028\u0000H\u0096\u0002¢\u0006\u0002\u0010\rR\"\u0010\u0002\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0001X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0003\u0010\u0004\"\u0004\b\u0005\u0010\u0006R\u0017\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00010\u0001¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0004¨\u0006\u000e"}, m421d2 = {"kotlin/sequences/FlatteningSequence$iterator$1", "", "itemIterator", "getItemIterator", "()Ljava/util/Iterator;", "setItemIterator", "(Ljava/util/Iterator;)V", "iterator", "getIterator", "ensureItemIterator", "", "hasNext", "next", "()Ljava/lang/Object;", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
    public static final class C11111 implements Iterator<E>, KMappedMarker {
        private Iterator<? extends E> itemIterator;
        private final Iterator<T> iterator;
        final /* synthetic */ FlatteningSequence<T, R, E> this$0;

        C11111(FlatteningSequence<T, R, E> r2) {
            this.this$0 = r2;
            this.iterator = FlatteningSequence.access$getSequence$p(r2).iterator();
        }

        private final boolean ensureItemIterator() {
            Iterator<? extends E> r0 = this.itemIterator;
            if (r0 != null) goto L5;
        L7:
            boolean r1 = false;
        L8:
            if (r1 == false) goto L11;
            this.itemIterator = null;
        L11:
            if (this.itemIterator != null) goto L19;
            if (this.iterator.hasNext() == false) goto L14;
            Iterator<? extends E> r3 = (Iterator) FlatteningSequence.access$getIterator$p(this.this$0).invoke(FlatteningSequence.access$getTransformer$p(this.this$0).invoke(this.iterator.next()));
            if (r3.hasNext() == false) goto L11;
            this.itemIterator = r3;
            return true;
        L14:
            return false;
        L19:
            return true;
        L5:
            if (r0.hasNext() == true) goto L7;
            r1 = true;
            goto L8
        }

        public final Iterator<E> getItemIterator() {
            return this.itemIterator;
        }

        public final Iterator<T> getIterator() {
            return this.iterator;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return ensureItemIterator();
        }

        @Override // java.util.Iterator
        public E next() {
            if (ensureItemIterator() == false) goto L7;
            Iterator<? extends E> r0 = this.itemIterator;
            Intrinsics.checkNotNull(r0);
            return r0.next();
        L7:
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }

        public final void setItemIterator(Iterator<? extends E> r1) {
            this.itemIterator = r1;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public FlatteningSequence(Sequence<? extends T> r2, Function1<? super T, ? extends R> r3, Function1<? super R, ? extends Iterator<? extends E>> r4) {
        Intrinsics.checkNotNullParameter(r2, "sequence");
        Intrinsics.checkNotNullParameter(r3, "transformer");
        Intrinsics.checkNotNullParameter(r4, "iterator");
        this.sequence = r2;
        this.transformer = r3;
        this.iterator = r4;
    }

    public static final /* synthetic */ Function1 access$getIterator$p(FlatteningSequence r1) {
        return r1.iterator;
    }

    public static final /* synthetic */ Sequence access$getSequence$p(FlatteningSequence r1) {
        return r1.sequence;
    }

    public static final /* synthetic */ Function1 access$getTransformer$p(FlatteningSequence r1) {
        return r1.transformer;
    }

    @Override // kotlin.sequences.Sequence
    public Iterator<E> iterator() {
        return new C11111(this);
    }
}
