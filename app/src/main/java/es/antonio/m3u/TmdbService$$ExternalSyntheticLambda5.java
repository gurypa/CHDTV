package es.antonio.m3u;

import java.util.Map;
import java.util.function.ToLongFunction;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes3.dex */
public final /* synthetic */ class TmdbService$$ExternalSyntheticLambda5 implements ToLongFunction {
    public final /* synthetic */ Map f$0;

    public /* synthetic */ TmdbService$$ExternalSyntheticLambda5(Map r1) {
        this.f$0 = r1;
    }

    @Override // java.util.function.ToLongFunction
    public final long applyAsLong(Object r3) {
        return TmdbService.lambda$remember$5(this.f$0, (Map.Entry) r3);
    }
}
