package kotlin.text;

import com.arthenica.ffmpegkit.MediaInformation;
import com.arthenica.ffmpegkit.StreamInformation;
import java.util.Iterator;
import kotlin.Metadata;
import kotlin.collections.AbstractCollection;
import kotlin.collections.CollectionsKt;
import kotlin.internal.PlatformImplementationsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;
import kotlin.sequences.SequencesKt;

/* JADX INFO: compiled from: Regex.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000-\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010(\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u00012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00030\u0002J\u0013\u0010\b\u001a\u0004\u0018\u00010\u00032\u0006\u0010\t\u001a\u00020\u0005H\u0096\u0002J\u0013\u0010\b\u001a\u0004\u0018\u00010\u00032\u0006\u0010\n\u001a\u00020\u000bH\u0096\u0002J\b\u0010\f\u001a\u00020\rH\u0016J\u0011\u0010\u000e\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00030\u000fH\u0096\u0002R\u0014\u0010\u0004\u001a\u00020\u00058VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007¨\u0006\u0010"}, m421d2 = {"kotlin/text/MatcherMatchResult$groups$1", "Lkotlin/text/MatchNamedGroupCollection;", "Lkotlin/collections/AbstractCollection;", "Lkotlin/text/MatchGroup;", MediaInformation.KEY_SIZE, "", "getSize", "()I", "get", StreamInformation.KEY_INDEX, "name", "", "isEmpty", "", "iterator", "", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public final class MatcherMatchResult$groups$1 extends AbstractCollection<MatchGroup> implements MatchNamedGroupCollection {
    final /* synthetic */ MatcherMatchResult this$0;

    MatcherMatchResult$groups$1(MatcherMatchResult r1) {
        this.this$0 = r1;
    }

    @Override // kotlin.collections.AbstractCollection, java.util.Collection
    public final /* bridge */ boolean contains(Object r2) {
        if (r2 != null) goto L4;
        boolean r0 = true;
    L5:
        if (r0 == true) goto L9;
        return false;
    L9:
        return contains((MatchGroup) r2);
    L4:
        r0 = r2 instanceof MatchGroup;
        goto L5
    }

    public /* bridge */ boolean contains(MatchGroup r2) {
        return super.contains(r2);
    }

    @Override // kotlin.text.MatchGroupCollection
    public MatchGroup get(int r5) {
        IntRange r0 = RegexKt.access$range(MatcherMatchResult.access$getMatchResult(this.this$0), r5);
        if (r0.getStart().intValue() < 0) goto L5;
        String r2 = MatcherMatchResult.access$getMatchResult(this.this$0).group(r5);
        Intrinsics.checkNotNullExpressionValue(r2, "group(...)");
        return new MatchGroup(r2, r0);
    L5:
        return null;
    }

    @Override // kotlin.text.MatchNamedGroupCollection
    public MatchGroup get(String r3) {
        Intrinsics.checkNotNullParameter(r3, "name");
        return PlatformImplementationsKt.IMPLEMENTATIONS.getMatchResultNamedGroup(MatcherMatchResult.access$getMatchResult(this.this$0), r3);
    }

    @Override // kotlin.collections.AbstractCollection
    public int getSize() {
        return MatcherMatchResult.access$getMatchResult(this.this$0).groupCount() + 1;
    }

    @Override // kotlin.collections.AbstractCollection, java.util.Collection
    public boolean isEmpty() {
        return false;
    }

    @Override // kotlin.collections.AbstractCollection, java.util.Collection, java.lang.Iterable
    public Iterator<MatchGroup> iterator() {
        return SequencesKt.map(CollectionsKt.asSequence(CollectionsKt.getIndices(this)), new MatcherMatchResult$groups$1$iterator$1(this)).iterator();
    }
}
