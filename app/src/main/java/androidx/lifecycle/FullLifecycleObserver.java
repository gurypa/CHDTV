package androidx.lifecycle;

/* JADX INFO: loaded from: classes.dex */
interface FullLifecycleObserver extends LifecycleObserver {
    void onCreate(LifecycleOwner r1);

    void onDestroy(LifecycleOwner r1);

    void onPause(LifecycleOwner r1);

    void onResume(LifecycleOwner r1);

    void onStart(LifecycleOwner r1);

    void onStop(LifecycleOwner r1);
}
