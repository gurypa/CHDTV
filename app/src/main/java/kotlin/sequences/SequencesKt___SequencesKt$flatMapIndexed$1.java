package kotlin.sequences;

import java.util.Iterator;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: Add missing generic type declarations: [R] */
/* JADX INFO: compiled from: _Sequences.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m422k = 3, m423mv = {1, 9, 0}, m425xi = 48)
/* synthetic */ class SequencesKt___SequencesKt$flatMapIndexed$1<R> extends FunctionReferenceImpl implements Function1<Iterable<? extends R>, Iterator<? extends R>> {
    public static final SequencesKt___SequencesKt$flatMapIndexed$1 INSTANCE = null;

    static {
        INSTANCE = new SequencesKt___SequencesKt$flatMapIndexed$1();
    }

    SequencesKt___SequencesKt$flatMapIndexed$1() {
        super(1, Iterable.class, "iterator", "iterator()Ljava/util/Iterator;", 0);
    }

    @Override // kotlin.jvm.functions.Function1
    public /* bridge */ /* synthetic */ Object invoke(Object r2) {
        return invoke((Iterable) r2);
    }

    public final Iterator<R> invoke(Iterable<? extends R> r2) {
        Intrinsics.checkNotNullParameter(r2, "p0");
        return r2.iterator();
    }
}
