package com.google.common.util.concurrent;

import sun.misc.Unsafe;

/* JADX INFO: renamed from: com.google.common.util.concurrent.AbstractFuture$UnsafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0 */
/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class C0902x75145c29 {
    /* JADX INFO: renamed from: m */
    public static /* synthetic */ boolean m380m(Unsafe r1, Object r2, long r3, Object r5, Object r6) {
    L3:
        if (r1.compareAndSwapObject(r2, r3, r5, r6) == true) goto L4;
        if (r1.getObject(r2, r3) == r5) goto L3;
        return false;
    L4:
        return true;
    }
}
