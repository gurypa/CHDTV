package com.google.common.util.concurrent;

import java.util.concurrent.atomic.AtomicReferenceArray;

/* JADX INFO: renamed from: com.google.common.util.concurrent.Striped$SmallLazyStriped$$ExternalSyntheticBackportWithForwarding0 */
/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class C0965xa7a47114 {
    /* JADX INFO: renamed from: m */
    public static /* synthetic */ boolean m388m(AtomicReferenceArray r1, int r2, Object r3, Object r4) {
    L3:
        if (r1.compareAndSet(r2, r3, r4) == true) goto L4;
        if (r1.get(r2) == r3) goto L3;
        return false;
    L4:
        return true;
    }
}
