package es.antonio.m3u;

import java.util.function.Predicate;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes3.dex */
public final /* synthetic */ class MovistarStreams$$ExternalSyntheticLambda1 implements Predicate {
    public /* synthetic */ MovistarStreams$$ExternalSyntheticLambda1() {
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object r1) {
        return MovistarStreams.allowedHost((String) r1);
    }
}
