package androidx.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Build;
import android.os.Bundle;

/* JADX INFO: loaded from: classes.dex */
public class ReportFragment extends Fragment {
    private static final String REPORT_FRAGMENT_TAG = "androidx.lifecycle.LifecycleDispatcher.report_fragment_tag";
    private ActivityInitializationListener mProcessListener;

    interface ActivityInitializationListener {
        void onCreate();

        void onResume();

        void onStart();
    }

    static class LifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
        LifecycleCallbacks() {
        }

        static void registerIn(Activity r1) {
            r1.registerActivityLifecycleCallbacks(new LifecycleCallbacks());
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityCreated(Activity r1, Bundle r2) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityDestroyed(Activity r1) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityPaused(Activity r1) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityPostCreated(Activity r2, Bundle r3) {
            ReportFragment.dispatch(r2, Lifecycle.Event.ON_CREATE);
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityPostResumed(Activity r2) {
            ReportFragment.dispatch(r2, Lifecycle.Event.ON_RESUME);
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityPostStarted(Activity r2) {
            ReportFragment.dispatch(r2, Lifecycle.Event.ON_START);
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityPreDestroyed(Activity r2) {
            ReportFragment.dispatch(r2, Lifecycle.Event.ON_DESTROY);
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityPrePaused(Activity r2) {
            ReportFragment.dispatch(r2, Lifecycle.Event.ON_PAUSE);
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityPreStopped(Activity r2) {
            ReportFragment.dispatch(r2, Lifecycle.Event.ON_STOP);
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityResumed(Activity r1) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivitySaveInstanceState(Activity r1, Bundle r2) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityStarted(Activity r1) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityStopped(Activity r1) {
        }
    }

    public ReportFragment() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    static void dispatch(Activity r2, Lifecycle.Event r3) {
        if ((r2 instanceof LifecycleRegistryOwner) == false) goto L7;
        ((LifecycleRegistryOwner) r2).getLifecycle().handleLifecycleEvent(r3);
        return;
    L7:
        if ((r2 instanceof LifecycleOwner) == false) goto L12;
        Lifecycle r0 = ((LifecycleOwner) r2).getLifecycle();
        if ((r0 instanceof LifecycleRegistry) == false) goto L13;
        ((LifecycleRegistry) r0).handleLifecycleEvent(r3);
        return;
    L13:
        return;
    }

    private void dispatch(Lifecycle.Event r3) {
        if (Build.VERSION.SDK_INT >= 29) goto L6;
        dispatch(getActivity(), r3);
        return;
    }

    private void dispatchCreate(ActivityInitializationListener r1) {
        if (r1 == null) goto L5;
        r1.onCreate();
        return;
    }

    private void dispatchResume(ActivityInitializationListener r1) {
        if (r1 == null) goto L5;
        r1.onResume();
        return;
    }

    private void dispatchStart(ActivityInitializationListener r1) {
        if (r1 == null) goto L5;
        r1.onStart();
        return;
    }

    static ReportFragment get(Activity r2) {
        return (ReportFragment) r2.getFragmentManager().findFragmentByTag(REPORT_FRAGMENT_TAG);
    }

    public static void injectIfNeededIn(Activity r4) {
        if (Build.VERSION.SDK_INT < 29) goto L5;
        LifecycleCallbacks.registerIn(r4);
    L5:
        FragmentManager r0 = r4.getFragmentManager();
        if (r0.findFragmentByTag(REPORT_FRAGMENT_TAG) != null) goto L9;
        r0.beginTransaction().add(new ReportFragment(), REPORT_FRAGMENT_TAG).commit();
        r0.executePendingTransactions();
        return;
    }

    @Override // android.app.Fragment
    public void onActivityCreated(Bundle r2) {
        super.onActivityCreated(r2);
        dispatchCreate(this.mProcessListener);
        dispatch(Lifecycle.Event.ON_CREATE);
    }

    @Override // android.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        dispatch(Lifecycle.Event.ON_DESTROY);
        this.mProcessListener = null;
    }

    @Override // android.app.Fragment
    public void onPause() {
        super.onPause();
        dispatch(Lifecycle.Event.ON_PAUSE);
    }

    @Override // android.app.Fragment
    public void onResume() {
        super.onResume();
        dispatchResume(this.mProcessListener);
        dispatch(Lifecycle.Event.ON_RESUME);
    }

    @Override // android.app.Fragment
    public void onStart() {
        super.onStart();
        dispatchStart(this.mProcessListener);
        dispatch(Lifecycle.Event.ON_START);
    }

    @Override // android.app.Fragment
    public void onStop() {
        super.onStop();
        dispatch(Lifecycle.Event.ON_STOP);
    }

    void setProcessListener(ActivityInitializationListener r1) {
        this.mProcessListener = r1;
    }
}
