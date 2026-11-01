package com.google.common.collect;

import java.util.function.BinaryOperator;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class CollectCollectors$$ExternalSyntheticLambda42 implements BinaryOperator {
    public /* synthetic */ CollectCollectors$$ExternalSyntheticLambda42() {
    }

    @Override // java.util.function.BiFunction
    public final Object apply(Object r1, Object r2) {
        return ((CollectCollectors.EnumMapAccumulator) r1).combine((CollectCollectors.EnumMapAccumulator) r2);
    }
}
