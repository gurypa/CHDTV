package androidx.concurrent.futures;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/* JADX INFO: renamed from: androidx.concurrent.futures.AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0 */
/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class C0037xc40028dd {
    /* JADX INFO: renamed from: m */
    public static /* synthetic */ boolean m4m(AtomicReferenceFieldUpdater r1, Object r2, Object r3, Object r4) {
    L3:
        if (r1.compareAndSet(r2, r3, r4) == true) goto L4;
        if (r1.get(r2) == r3) goto L3;
        return false;
    L4:
        return true;
    }
}
