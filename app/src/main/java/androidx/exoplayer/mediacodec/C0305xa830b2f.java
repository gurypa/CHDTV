package androidx.media3.exoplayer.mediacodec;

import java.util.concurrent.atomic.AtomicReference;

/* JADX INFO: renamed from: androidx.media3.exoplayer.mediacodec.AsynchronousMediaCodecBufferEnqueuer$$ExternalSyntheticBackportWithForwarding0 */
/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class C0305xa830b2f {
    /* JADX INFO: renamed from: m */
    public static /* synthetic */ boolean m111m(AtomicReference r1, Object r2, Object r3) {
    L3:
        if (r1.compareAndSet(r2, r3) == true) goto L4;
        if (r1.get() == r2) goto L3;
        return false;
    L4:
        return true;
    }
}
