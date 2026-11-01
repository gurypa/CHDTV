package com.google.common.collect;

import com.google.common.base.Function;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class ImmutableRangeMap$$ExternalSyntheticLambda0 implements Function {
    public /* synthetic */ ImmutableRangeMap$$ExternalSyntheticLambda0() {
    }

    @Override // com.google.common.base.Function
    public final Object apply(Object r1) {
        return ((Range) r1).upperBound();
    }
}
