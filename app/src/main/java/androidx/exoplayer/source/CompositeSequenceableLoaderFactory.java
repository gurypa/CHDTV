package androidx.media3.exoplayer.source;

import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface CompositeSequenceableLoaderFactory {
    SequenceableLoader create(List<? extends SequenceableLoader> r1, List<List<Integer>> r2);

    @Deprecated
    SequenceableLoader createCompositeSequenceableLoader(SequenceableLoader... r1);

    SequenceableLoader empty();
}
