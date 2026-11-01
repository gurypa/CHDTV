package kotlin.p005io;

import java.io.BufferedReader;
import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;
import kotlin.sequences.Sequence;

/* JADX INFO: compiled from: ReadWrite.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010(\n\u0000\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\u000f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00020\u0007H\u0096\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\b"}, m421d2 = {"Lkotlin/io/LinesSequence;", "Lkotlin/sequences/Sequence;", "", "reader", "Ljava/io/BufferedReader;", "(Ljava/io/BufferedReader;)V", "iterator", "", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
final class LinesSequence implements Sequence<String> {
    private final BufferedReader reader;

    /* JADX INFO: renamed from: kotlin.io.LinesSequence$iterator$1 */
    /* JADX INFO: compiled from: ReadWrite.kt */
    @Metadata(m420d1 = {"\u0000\u0017\n\u0000\n\u0002\u0010(\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001J\t\u0010\u0006\u001a\u00020\u0004H\u0096\u0002J\t\u0010\u0007\u001a\u00020\u0002H\u0096\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0002X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\b"}, m421d2 = {"kotlin/io/LinesSequence$iterator$1", "", "", "done", "", "nextValue", "hasNext", "next", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
    public static final class C10971 implements Iterator<String>, KMappedMarker {
        private boolean done;
        private String nextValue;
        final /* synthetic */ LinesSequence this$0;

        C10971(LinesSequence r1) {
            this.this$0 = r1;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.nextValue != null) goto L10;
            if (this.done == true) goto L10;
            this.nextValue = LinesSequence.access$getReader$p(this.this$0).readLine();
            if (this.nextValue != null) goto L10;
            this.done = true;
        L10:
            if (this.nextValue != null) goto L14;
            return false;
        L14:
            return true;
        }

        @Override // java.util.Iterator
        public /* bridge */ /* synthetic */ String next() {
            return next2();
        }

        @Override // java.util.Iterator
        /* JADX INFO: renamed from: next, reason: avoid collision after fix types in other method */
        public String next2() {
            if (hasNext() == false) goto L7;
            String r0 = this.nextValue;
            this.nextValue = null;
            Intrinsics.checkNotNull(r0);
            return r0;
        L7:
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }
    }

    public LinesSequence(BufferedReader r2) {
        Intrinsics.checkNotNullParameter(r2, "reader");
        this.reader = r2;
    }

    public static final /* synthetic */ BufferedReader access$getReader$p(LinesSequence r1) {
        return r1.reader;
    }

    @Override // kotlin.sequences.Sequence
    public Iterator<String> iterator() {
        return new C10971(this);
    }
}
