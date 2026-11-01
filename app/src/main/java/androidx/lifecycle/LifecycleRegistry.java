package androidx.lifecycle;

import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.arch.core.internal.FastSafeIterableMap;
import androidx.arch.core.internal.SafeIterableMap;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class LifecycleRegistry extends Lifecycle {
    private int mAddingObserverCounter;
    private final boolean mEnforceMainThread;
    private boolean mHandlingEvent;
    private final WeakReference<LifecycleOwner> mLifecycleOwner;
    private boolean mNewEventOccurred;
    private FastSafeIterableMap<LifecycleObserver, ObserverWithState> mObserverMap;
    private ArrayList<Lifecycle.State> mParentStates;
    private Lifecycle.State mState;

    static class ObserverWithState {
        LifecycleEventObserver mLifecycleObserver;
        Lifecycle.State mState;

        ObserverWithState(LifecycleObserver r2, Lifecycle.State r3) {
            this.mLifecycleObserver = Lifecycling.lifecycleEventObserver(r2);
            this.mState = r3;
        }

        void dispatchEvent(LifecycleOwner r3, Lifecycle.Event r4) {
            Lifecycle.State r0 = r4.getTargetState();
            this.mState = LifecycleRegistry.min(this.mState, r0);
            this.mLifecycleObserver.onStateChanged(r3, r4);
            this.mState = r0;
        }
    }

    public LifecycleRegistry(LifecycleOwner r2) {
        this(r2, true);
    }

    private LifecycleRegistry(LifecycleOwner r2, boolean r3) {
        this.mObserverMap = new FastSafeIterableMap();
        this.mAddingObserverCounter = 0;
        this.mHandlingEvent = false;
        this.mNewEventOccurred = false;
        this.mParentStates = new ArrayList();
        this.mLifecycleOwner = new WeakReference(r2);
        this.mState = Lifecycle.State.INITIALIZED;
        this.mEnforceMainThread = r3;
    }

    private void backwardPass(LifecycleOwner r8) {
        Iterator<Map.Entry<LifecycleObserver, ObserverWithState>> r0 = this.mObserverMap.descendingIterator();
    L4:
        if (r0.hasNext() == false) goto L20;
        if (this.mNewEventOccurred == true) goto L28;
        Map.Entry<LifecycleObserver, ObserverWithState> r1 = r0.next();
        ObserverWithState r2 = r1.getValue();
    L9:
        if (r2.mState.compareTo(this.mState) <= 0) goto L4;
        if (this.mNewEventOccurred == true) goto L4;
        if (this.mObserverMap.contains(r1.getKey()) == false) goto L4;
        Lifecycle.Event r3 = Lifecycle.Event.downFrom(r2.mState);
        if (r3 == null) goto L18;
        pushParentState(r3.getTargetState());
        r2.dispatchEvent(r8, r3);
        popParentState();
        goto L9
    L18:
        throw new IllegalStateException("no event down from " + r2.mState);
    L28:
        return;
    }

    private Lifecycle.State calculateTargetState(LifecycleObserver r5) {
        Map.Entry<LifecycleObserver, ObserverWithState> r0 = this.mObserverMap.ceil(r5);
        Lifecycle.State r1 = null;
        if (r0 == null) goto L5;
        Lifecycle.State r2 = r0.getValue().mState;
    L7:
        if (this.mParentStates.isEmpty() == true) goto L11;
        r1 = this.mParentStates.get(this.mParentStates.size() - 1);
    L11:
        return min(min(this.mState, r2), r1);
    L5:
        r2 = null;
        goto L7
    }

    public static LifecycleRegistry createUnsafe(LifecycleOwner r2) {
        return new LifecycleRegistry(r2, false);
    }

    private void enforceMainThreadIfNeeded(String r4) {
        if (this.mEnforceMainThread == true) goto L5;
        return;
    L5:
        if (ArchTaskExecutor.getInstance().isMainThread() == false) goto L8;
        return;
    L8:
        throw new IllegalStateException("Method " + r4 + " must be called on the main thread");
    }

    private void forwardPass(LifecycleOwner r8) {
        SafeIterableMap<LifecycleObserver, ObserverWithState>.IteratorWithAdditions r0 = this.mObserverMap.iteratorWithAdditions();
    L4:
        if (r0.hasNext() == false) goto L20;
        if (this.mNewEventOccurred == true) goto L28;
        Map.Entry r1 = r0.next();
        ObserverWithState r2 = (ObserverWithState) r1.getValue();
    L9:
        if (r2.mState.compareTo(this.mState) >= 0) goto L4;
        if (this.mNewEventOccurred == true) goto L4;
        if (this.mObserverMap.contains((LifecycleObserver) r1.getKey()) == false) goto L4;
        pushParentState(r2.mState);
        Lifecycle.Event r3 = Lifecycle.Event.upFrom(r2.mState);
        if (r3 == null) goto L18;
        r2.dispatchEvent(r8, r3);
        popParentState();
        goto L9
    L18:
        throw new IllegalStateException("no event up from " + r2.mState);
    L28:
        return;
    }

    private boolean isSynced() {
        if (this.mObserverMap.size() != 0) goto L5;
        return true;
    L5:
        Lifecycle.State r0 = this.mObserverMap.eldest().getValue().mState;
        Lifecycle.State r2 = this.mObserverMap.newest().getValue().mState;
        if (r0 == r2) goto L8;
    L10:
        return false;
    L8:
        if (this.mState != r2) goto L10;
        return true;
    }

    static Lifecycle.State min(Lifecycle.State r1, Lifecycle.State r2) {
        if (r2 == null) goto L7;
        if (r2.compareTo(r1) >= 0) goto L7;
        return r2;
    L7:
        return r1;
    }

    private void moveToState(Lifecycle.State r3) {
        if (this.mState != r3) goto L5;
        return;
    L5:
        this.mState = r3;
        if (this.mHandlingEvent == false) goto L8;
    L12:
        this.mNewEventOccurred = true;
        return;
    L8:
        if (this.mAddingObserverCounter != 0) goto L12;
        this.mHandlingEvent = true;
        sync();
        this.mHandlingEvent = false;
    }

    private void popParentState() {
        this.mParentStates.remove(this.mParentStates.size() - 1);
    }

    private void pushParentState(Lifecycle.State r2) {
        this.mParentStates.add(r2);
    }

    private void sync() {
        LifecycleOwner r0 = this.mLifecycleOwner.get();
        if (r0 == null) goto L19;
    L5:
        if (isSynced() == true) goto L16;
        this.mNewEventOccurred = false;
        if (this.mState.compareTo(this.mObserverMap.eldest().getValue().mState) >= 0) goto L9;
        backwardPass(r0);
    L9:
        Map.Entry<LifecycleObserver, ObserverWithState> r1 = this.mObserverMap.newest();
        if (this.mNewEventOccurred == true) goto L5;
        if (r1 == null) goto L5;
        if (this.mState.compareTo(r1.getValue().mState) <= 0) goto L5;
        forwardPass(r0);
        goto L5
    L16:
        this.mNewEventOccurred = false;
        return;
    L19:
        throw new IllegalStateException("LifecycleOwner of this LifecycleRegistry is alreadygarbage collected. It is too late to change lifecycle state.");
    }

    @Override // androidx.lifecycle.Lifecycle
    public void addObserver(LifecycleObserver r11) {
        enforceMainThreadIfNeeded("addObserver");
        if (this.mState != Lifecycle.State.DESTROYED) goto L5;
        Lifecycle.State r0 = Lifecycle.State.DESTROYED;
    L6:
        ObserverWithState r1 = new ObserverWithState(r11, r0);
        if (this.mObserverMap.putIfAbsent(r11, r1) == null) goto L9;
        return;
    L9:
        LifecycleOwner r3 = this.mLifecycleOwner.get();
        if (r3 != null) goto L13;
        return;
    L13:
        if (this.mAddingObserverCounter == 0) goto L15;
    L18:
        boolean r4 = true;
    L19:
        Lifecycle.State r6 = calculateTargetState(r11);
        this.mAddingObserverCounter++;
    L21:
        if (r1.mState.compareTo(r6) >= 0) goto L29;
        if (this.mObserverMap.contains(r11) == false) goto L29;
        pushParentState(r1.mState);
        Lifecycle.Event r7 = Lifecycle.Event.upFrom(r1.mState);
        if (r7 == null) goto L28;
        r1.dispatchEvent(r3, r7);
        popParentState();
        r6 = calculateTargetState(r11);
        goto L21
    L28:
        throw new IllegalStateException("no event up from " + r1.mState);
    L29:
        if (r4 == true) goto L31;
        sync();
    L31:
        this.mAddingObserverCounter--;
        return;
    L15:
        if (this.mHandlingEvent == true) goto L18;
        r4 = false;
        goto L19
    L5:
        r0 = Lifecycle.State.INITIALIZED;
        goto L6
    }

    @Override // androidx.lifecycle.Lifecycle
    public Lifecycle.State getCurrentState() {
        return this.mState;
    }

    public int getObserverCount() {
        enforceMainThreadIfNeeded("getObserverCount");
        return this.mObserverMap.size();
    }

    public void handleLifecycleEvent(Lifecycle.Event r2) {
        enforceMainThreadIfNeeded("handleLifecycleEvent");
        moveToState(r2.getTargetState());
    }

    @Deprecated
    public void markState(Lifecycle.State r2) {
        enforceMainThreadIfNeeded("markState");
        setCurrentState(r2);
    }

    @Override // androidx.lifecycle.Lifecycle
    public void removeObserver(LifecycleObserver r2) {
        enforceMainThreadIfNeeded("removeObserver");
        this.mObserverMap.remove(r2);
    }

    public void setCurrentState(Lifecycle.State r2) {
        enforceMainThreadIfNeeded("setCurrentState");
        moveToState(r2);
    }
}
