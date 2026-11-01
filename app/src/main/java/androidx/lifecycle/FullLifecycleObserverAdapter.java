package androidx.lifecycle;

/* JADX INFO: loaded from: classes.dex */
class FullLifecycleObserverAdapter implements LifecycleEventObserver {
    private final FullLifecycleObserver mFullLifecycleObserver;
    private final LifecycleEventObserver mLifecycleEventObserver;

    /* JADX INFO: renamed from: androidx.lifecycle.FullLifecycleObserverAdapter$1 */
    static /* synthetic */ class C01481 {
        static final /* synthetic */ int[] $SwitchMap$androidx$lifecycle$Lifecycle$Event = null;

        static {
            $SwitchMap$androidx$lifecycle$Lifecycle$Event = new int[Lifecycle.Event.values().length];
            $SwitchMap$androidx$lifecycle$Lifecycle$Event[Lifecycle.Event.ON_CREATE.ordinal()] = 1;     // Catch: NoSuchFieldError -> L5
        L35:
            $SwitchMap$androidx$lifecycle$Lifecycle$Event[Lifecycle.Event.ON_START.ordinal()] = 2;     // Catch: NoSuchFieldError -> L8
        L27:
            $SwitchMap$androidx$lifecycle$Lifecycle$Event[Lifecycle.Event.ON_RESUME.ordinal()] = 3;     // Catch: NoSuchFieldError -> L11
        L25:
            $SwitchMap$androidx$lifecycle$Lifecycle$Event[Lifecycle.Event.ON_PAUSE.ordinal()] = 4;     // Catch: NoSuchFieldError -> L14
        L29:
            $SwitchMap$androidx$lifecycle$Lifecycle$Event[Lifecycle.Event.ON_STOP.ordinal()] = 5;     // Catch: NoSuchFieldError -> L17
        L33:
            $SwitchMap$androidx$lifecycle$Lifecycle$Event[Lifecycle.Event.ON_DESTROY.ordinal()] = 6;     // Catch: NoSuchFieldError -> L20
        L37:
            $SwitchMap$androidx$lifecycle$Lifecycle$Event[Lifecycle.Event.ON_ANY.ordinal()] = 7;     // Catch: NoSuchFieldError -> L23
            return;
        }
    }

    FullLifecycleObserverAdapter(FullLifecycleObserver r1, LifecycleEventObserver r2) {
        this.mFullLifecycleObserver = r1;
        this.mLifecycleEventObserver = r2;
    }

    @Override // androidx.lifecycle.LifecycleEventObserver
    public void onStateChanged(LifecycleOwner r3, Lifecycle.Event r4) {
        switch(C01481.$SwitchMap$androidx$lifecycle$Lifecycle$Event[r4.ordinal()]) {
            case 1: goto L12;
            case 2: goto L11;
            case 3: goto L10;
            case 4: goto L9;
            case 5: goto L8;
            case 6: goto L7;
            case 7: goto L6;
            default: goto L14;
        };
    L7:
        this.mFullLifecycleObserver.onDestroy(r3);
        goto L14
    L8:
        this.mFullLifecycleObserver.onStop(r3);
        goto L14
    L9:
        this.mFullLifecycleObserver.onPause(r3);
        goto L14
    L10:
        this.mFullLifecycleObserver.onResume(r3);
        goto L14
    L11:
        this.mFullLifecycleObserver.onStart(r3);
        goto L14
    L12:
        this.mFullLifecycleObserver.onCreate(r3);
        goto L14
    L6:
        throw new IllegalArgumentException("ON_ANY must not been send by anybody");
    L14:
        if (this.mLifecycleEventObserver == null) goto L17;
        this.mLifecycleEventObserver.onStateChanged(r3, r4);
        return;
    }
}
