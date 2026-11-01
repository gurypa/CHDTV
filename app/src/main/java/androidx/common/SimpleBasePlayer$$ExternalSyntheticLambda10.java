package androidx.media3.common;

import androidx.media3.common.util.ListenerSet;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SimpleBasePlayer$$ExternalSyntheticLambda10 implements ListenerSet.Event {
    public /* synthetic */ SimpleBasePlayer$$ExternalSyntheticLambda10() {
    }

    @Override // androidx.media3.common.util.ListenerSet.Event
    public final void invoke(Object r1) {
        ((Player.Listener) r1).onRenderedFirstFrame();
    }
}
