package androidx.lifecycle;

/* JADX INFO: loaded from: classes.dex */
public interface LifecycleEventObserver extends LifecycleObserver {
    void onStateChanged(LifecycleOwner r1, Lifecycle.Event r2);
}
