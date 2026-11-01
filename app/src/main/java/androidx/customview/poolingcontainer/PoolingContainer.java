package androidx.customview.poolingcontainer;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.core.view.ViewGroupKt;
import androidx.core.view.ViewKt;
import java.util.Iterator;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: PoolingContainer.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u00006\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0014\u0010\u000f\u001a\u00020\u0010*\u00020\u00062\u0006\u0010\u0011\u001a\u00020\u0012H\u0007\u001a\n\u0010\u0013\u001a\u00020\u0010*\u00020\u0006\u001a\n\u0010\u0014\u001a\u00020\u0010*\u00020\u0015\u001a\u0014\u0010\u0016\u001a\u00020\u0010*\u00020\u00062\u0006\u0010\u0011\u001a\u00020\u0012H\u0007\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000\"(\u0010\u0005\u001a\u00020\u0004*\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00048F@FX\u0086\u000e¢\u0006\f\u001a\u0004\b\u0005\u0010\u0007\"\u0004\b\b\u0010\t\"\u0015\u0010\n\u001a\u00020\u0004*\u00020\u00068F¢\u0006\u0006\u001a\u0004\b\n\u0010\u0007\"\u0018\u0010\u000b\u001a\u00020\f*\u00020\u00068BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\r\u0010\u000e¨\u0006\u0017"}, m421d2 = {"IsPoolingContainerTag", "", "PoolingContainerListenerHolderTag", "value", "", "isPoolingContainer", "Landroid/view/View;", "(Landroid/view/View;)Z", "setPoolingContainer", "(Landroid/view/View;Z)V", "isWithinPoolingContainer", "poolingContainerListenerHolder", "Landroidx/customview/poolingcontainer/PoolingContainerListenerHolder;", "getPoolingContainerListenerHolder", "(Landroid/view/View;)Landroidx/customview/poolingcontainer/PoolingContainerListenerHolder;", "addPoolingContainerListener", "", "listener", "Landroidx/customview/poolingcontainer/PoolingContainerListener;", "callPoolingContainerOnRelease", "callPoolingContainerOnReleaseForChildren", "Landroid/view/ViewGroup;", "removePoolingContainerListener", "customview-poolingcontainer_release"}, m422k = 2, m423mv = {1, 6, 0}, m425xi = 48)
public final class PoolingContainer {
    private static final int IsPoolingContainerTag = 0;
    private static final int PoolingContainerListenerHolderTag = 0;

    static {
        PoolingContainerListenerHolderTag = C0139R.id.pooling_container_listener_holder_tag;
        IsPoolingContainerTag = C0139R.id.is_pooling_container_tag;
    }

    public static final void addPoolingContainerListener(View r1, PoolingContainerListener r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "listener");
        getPoolingContainerListenerHolder(r1).addListener(r2);
    }

    public static final void callPoolingContainerOnRelease(View r7) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        Iterator<View> r2 = ViewKt.getAllViews(r7).iterator();
    L4:
        if (r2.hasNext() == false) goto L7;
        getPoolingContainerListenerHolder(r2.next()).onRelease();
        goto L4
    }

    public static final void callPoolingContainerOnReleaseForChildren(ViewGroup r7) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        Iterator<View> r2 = ViewGroupKt.getChildren(r7).iterator();
    L4:
        if (r2.hasNext() == false) goto L7;
        getPoolingContainerListenerHolder(r2.next()).onRelease();
        goto L4
    }

    private static final PoolingContainerListenerHolder getPoolingContainerListenerHolder(View r2) {
        PoolingContainerListenerHolder r0 = (PoolingContainerListenerHolder) r2.getTag(PoolingContainerListenerHolderTag);
        if (r0 != null) goto L6;
        PoolingContainerListenerHolder r1 = new PoolingContainerListenerHolder();
        r2.setTag(PoolingContainerListenerHolderTag, r1);
        return r1;
    L6:
        return r0;
    }

    public static final boolean isPoolingContainer(View r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Object r0 = r2.getTag(IsPoolingContainerTag);
        if ((r0 instanceof Boolean) == false) goto L5;
        Boolean r1 = (Boolean) r0;
    L6:
        if (r1 != null) goto L10;
        return false;
    L10:
        return r1.booleanValue();
    L5:
        r1 = null;
        goto L6
    }

    public static final boolean isWithinPoolingContainer(View r7) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        Iterator<ViewParent> r2 = ViewKt.getAncestors(r7).iterator();
    L4:
        if (r2.hasNext() == false) goto L12;
        Object r4 = (ViewParent) r2.next();
        if ((r4 instanceof View) == false) goto L4;
        if (isPoolingContainer((View) r4) == false) goto L4;
        return true;
    L12:
        return false;
    }

    public static final void removePoolingContainerListener(View r1, PoolingContainerListener r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "listener");
        getPoolingContainerListenerHolder(r1).removeListener(r2);
    }

    public static final void setPoolingContainer(View r2, boolean r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        r2.setTag(IsPoolingContainerTag, Boolean.valueOf(r3));
    }
}
