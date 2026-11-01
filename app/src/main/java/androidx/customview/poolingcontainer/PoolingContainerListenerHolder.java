package androidx.customview.poolingcontainer;

import java.util.ArrayList;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: PoolingContainer.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\b\u0002\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u000e\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u0005J\u0006\u0010\n\u001a\u00020\bJ\u000e\u0010\u000b\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u0005R\u001e\u0010\u0003\u001a\u0012\u0012\u0004\u0012\u00020\u00050\u0004j\b\u0012\u0004\u0012\u00020\u0005`\u0006X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\f"}, m421d2 = {"Landroidx/customview/poolingcontainer/PoolingContainerListenerHolder;", "", "()V", "listeners", "Ljava/util/ArrayList;", "Landroidx/customview/poolingcontainer/PoolingContainerListener;", "Lkotlin/collections/ArrayList;", "addListener", "", "listener", "onRelease", "removeListener", "customview-poolingcontainer_release"}, m422k = 1, m423mv = {1, 6, 0}, m425xi = 48)
final class PoolingContainerListenerHolder {
    private final ArrayList<PoolingContainerListener> listeners;

    public PoolingContainerListenerHolder() {
        this.listeners = new ArrayList();
    }

    public final void addListener(PoolingContainerListener r2) {
        Intrinsics.checkNotNullParameter(r2, "listener");
        this.listeners.add(r2);
    }

    public final void onRelease() {
        int r0 = CollectionsKt.getLastIndex(this.listeners);
    L4:
        if ((-1) >= r0) goto L6;
        this.listeners.get(r0).onRelease();
        r0 = r0 - 1;
        goto L4
    }

    public final void removeListener(PoolingContainerListener r2) {
        Intrinsics.checkNotNullParameter(r2, "listener");
        this.listeners.remove(r2);
    }
}
