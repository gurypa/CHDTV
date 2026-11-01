package kotlin.text;

import java.util.Iterator;
import kotlin.Metadata;
import kotlin.jvm.internal.markers.KMappedMarker;

/* JADX INFO: compiled from: Iterables.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\u0011\n\u0000\n\u0002\u0010\u001c\n\u0000\n\u0002\u0010(\n\u0000*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\u000f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00028\u00000\u0003H\u0096\u0002¨\u0006\u0004¸\u0006\u0000"}, m421d2 = {"kotlin/collections/CollectionsKt__IterablesKt$Iterable$1", "", "iterator", "", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public final class StringsKt___StringsKt$asIterable$$inlined$Iterable$1 implements Iterable<Character>, KMappedMarker {
    final /* synthetic */ CharSequence $this_asIterable$inlined;

    public StringsKt___StringsKt$asIterable$$inlined$Iterable$1(CharSequence r1) {
        this.$this_asIterable$inlined = r1;
    }

    @Override // java.lang.Iterable
    public Iterator<Character> iterator() {
        return StringsKt.iterator(this.$this_asIterable$inlined);
    }
}
