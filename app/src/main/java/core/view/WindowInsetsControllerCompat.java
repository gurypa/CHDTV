package androidx.core.view;

import android.R;
import android.os.Build;
import android.os.CancellationSignal;
import android.view.View;
import android.view.Window;
import android.view.WindowInsetsAnimationControlListener;
import android.view.WindowInsetsController;
import android.view.animation.Interpolator;
import android.view.inputmethod.InputMethodManager;
import androidx.collection.SimpleArrayMap;
import androidx.media3.common.C0180C;

/* JADX INFO: loaded from: classes.dex */
public final class WindowInsetsControllerCompat {
    public static final int BEHAVIOR_SHOW_BARS_BY_SWIPE = 1;
    public static final int BEHAVIOR_SHOW_BARS_BY_TOUCH = 0;
    public static final int BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE = 2;
    private final Impl mImpl;

    private static class Impl {
        Impl() {
        }

        void addOnControllableInsetsChangedListener(OnControllableInsetsChangedListener r1) {
        }

        void controlWindowInsetsAnimation(int r1, long r2, Interpolator r4, CancellationSignal r5, WindowInsetsAnimationControlListenerCompat r6) {
        }

        int getSystemBarsBehavior() {
            return 0;
        }

        void hide(int r1) {
        }

        public boolean isAppearanceLightNavigationBars() {
            return false;
        }

        public boolean isAppearanceLightStatusBars() {
            return false;
        }

        void removeOnControllableInsetsChangedListener(OnControllableInsetsChangedListener r1) {
        }

        public void setAppearanceLightNavigationBars(boolean r1) {
        }

        public void setAppearanceLightStatusBars(boolean r1) {
        }

        void setSystemBarsBehavior(int r1) {
        }

        void show(int r1) {
        }
    }

    private static class Impl20 extends Impl {
        private final View mView;
        protected final Window mWindow;

        Impl20(Window r1, View r2) {
            this.mWindow = r1;
            this.mView = r2;
        }

        private void hideForType(int r4) {
            switch(r4) {
                case 1: goto L7;
                case 2: goto L5;
                case 8: goto L4;
                default: goto L10;
            };
        L4:
            ((InputMethodManager) this.mWindow.getContext().getSystemService("input_method")).hideSoftInputFromWindow(this.mWindow.getDecorView().getWindowToken(), 0);
            return;
        L5:
            setSystemUiFlag(2);
            return;
        L7:
            setSystemUiFlag(4);
            return;
        }

        static /* synthetic */ void lambda$showForType$0(View r2) {
            ((InputMethodManager) r2.getContext().getSystemService("input_method")).showSoftInput(r2, 0);
        }

        private void showForType(int r4) {
            switch(r4) {
                case 1: goto L19;
                case 2: goto L17;
                case 8: goto L4;
                default: goto L24;
            };
        L4:
            View r0 = this.mView;
            if (r0.isInEditMode() == false) goto L7;
        L10:
            r0.requestFocus();
        L11:
            if (r0 != null) goto L13;
            r0 = this.mWindow.findViewById(R.id.content);
        L13:
            if (r0 != null) goto L15;
            return;
        L15:
            if (r0.hasWindowFocus() == false) goto L22;
            final View r1 = r0;
            r1.post(new WindowInsetsControllerCompat$Impl20$$ExternalSyntheticLambda0(r1));
            return;
        L22:
            return;
        L7:
            if (r0.onCheckIsTextEditor() == true) goto L10;
            r0 = this.mWindow.getCurrentFocus();
            goto L11
        L17:
            unsetSystemUiFlag(2);
            return;
        L19:
            unsetSystemUiFlag(4);
            unsetWindowFlag(1024);
            return;
        }

        @Override // androidx.core.view.WindowInsetsControllerCompat.Impl
        void addOnControllableInsetsChangedListener(OnControllableInsetsChangedListener r1) {
        }

        @Override // androidx.core.view.WindowInsetsControllerCompat.Impl
        void controlWindowInsetsAnimation(int r1, long r2, Interpolator r4, CancellationSignal r5, WindowInsetsAnimationControlListenerCompat r6) {
        }

        @Override // androidx.core.view.WindowInsetsControllerCompat.Impl
        int getSystemBarsBehavior() {
            return 0;
        }

        @Override // androidx.core.view.WindowInsetsControllerCompat.Impl
        void hide(int r3) {
            int r0 = 1;
        L4:
            if (r0 > 256) goto L10;
            if ((r3 & r0) == 0) goto L9;
            hideForType(r0);
        L9:
            r0 = r0 << 1;
            goto L4
        }

        @Override // androidx.core.view.WindowInsetsControllerCompat.Impl
        void removeOnControllableInsetsChangedListener(OnControllableInsetsChangedListener r1) {
        }

        @Override // androidx.core.view.WindowInsetsControllerCompat.Impl
        void setSystemBarsBehavior(int r3) {
            switch(r3) {
                case 0: goto L7;
                case 1: goto L6;
                case 2: goto L5;
                default: goto L11;
            };
        L5:
            unsetSystemUiFlag(2048);
            setSystemUiFlag(4096);
            return;
        L6:
            unsetSystemUiFlag(4096);
            setSystemUiFlag(2048);
            return;
        L7:
            unsetSystemUiFlag(6144);
            return;
        }

        protected void setSystemUiFlag(int r3) {
            View r0 = this.mWindow.getDecorView();
            r0.setSystemUiVisibility(r0.getSystemUiVisibility() | r3);
        }

        protected void setWindowFlag(int r2) {
            this.mWindow.addFlags(r2);
        }

        @Override // androidx.core.view.WindowInsetsControllerCompat.Impl
        void show(int r3) {
            int r0 = 1;
        L4:
            if (r0 > 256) goto L10;
            if ((r3 & r0) == 0) goto L9;
            showForType(r0);
        L9:
            r0 = r0 << 1;
            goto L4
        }

        protected void unsetSystemUiFlag(int r4) {
            View r0 = this.mWindow.getDecorView();
            r0.setSystemUiVisibility(r0.getSystemUiVisibility() & (~r4));
        }

        protected void unsetWindowFlag(int r2) {
            this.mWindow.clearFlags(r2);
        }
    }

    private static class Impl23 extends Impl20 {
        Impl23(Window r1, View r2) {
            super(r1, r2);
        }

        @Override // androidx.core.view.WindowInsetsControllerCompat.Impl
        public boolean isAppearanceLightStatusBars() {
            if ((this.mWindow.getDecorView().getSystemUiVisibility() & 8192) == 0) goto L5;
            return true;
        L5:
            return false;
        }

        @Override // androidx.core.view.WindowInsetsControllerCompat.Impl
        public void setAppearanceLightStatusBars(boolean r3) {
            if (r3 == false) goto L5;
            unsetWindowFlag(C0180C.BUFFER_FLAG_NOT_DEPENDED_ON);
            setWindowFlag(Integer.MIN_VALUE);
            setSystemUiFlag(8192);
            return;
        L5:
            unsetSystemUiFlag(8192);
        }
    }

    private static class Impl26 extends Impl23 {
        Impl26(Window r1, View r2) {
            super(r1, r2);
        }

        @Override // androidx.core.view.WindowInsetsControllerCompat.Impl
        public boolean isAppearanceLightNavigationBars() {
            if ((this.mWindow.getDecorView().getSystemUiVisibility() & 16) == 0) goto L5;
            return true;
        L5:
            return false;
        }

        @Override // androidx.core.view.WindowInsetsControllerCompat.Impl
        public void setAppearanceLightNavigationBars(boolean r3) {
            if (r3 == false) goto L5;
            unsetWindowFlag(C0180C.BUFFER_FLAG_FIRST_SAMPLE);
            setWindowFlag(Integer.MIN_VALUE);
            setSystemUiFlag(16);
            return;
        L5:
            unsetSystemUiFlag(16);
        }
    }

    private static class Impl30 extends Impl {
        final WindowInsetsControllerCompat mCompatController;
        final WindowInsetsController mInsetsController;
        private final SimpleArrayMap<OnControllableInsetsChangedListener, WindowInsetsController.OnControllableInsetsChangedListener> mListeners;
        protected Window mWindow;

        Impl30(Window r2, WindowInsetsControllerCompat r3) {
            this(r2.getInsetsController(), r3);
            this.mWindow = r2;
        }

        Impl30(WindowInsetsController r2, WindowInsetsControllerCompat r3) {
            this.mListeners = new SimpleArrayMap();
            this.mInsetsController = r2;
            this.mCompatController = r3;
        }

        @Override // androidx.core.view.WindowInsetsControllerCompat.Impl
        void addOnControllableInsetsChangedListener(final OnControllableInsetsChangedListener r3) {
            if (this.mListeners.containsKey(r3) == false) goto L5;
            return;
        L5:
            WindowInsetsController.OnControllableInsetsChangedListener r0 = new WindowInsetsControllerCompat$Impl30$$ExternalSyntheticLambda0(this, r3);
            this.mListeners.put(r3, r0);
            this.mInsetsController.addOnControllableInsetsChangedListener(r0);
        }

        @Override // androidx.core.view.WindowInsetsControllerCompat.Impl
        void controlWindowInsetsAnimation(int r9, long r10, Interpolator r12, CancellationSignal r13, final WindowInsetsAnimationControlListenerCompat r14) {
            WindowInsetsAnimationControlListener r0 = new WindowInsetsAnimationControlListenerC01281(this, r14);
            this.mInsetsController.controlWindowInsetsAnimation(r9, r10, r12, r13, r0);
        }

        @Override // androidx.core.view.WindowInsetsControllerCompat.Impl
        int getSystemBarsBehavior() {
            return this.mInsetsController.getSystemBarsBehavior();
        }

        @Override // androidx.core.view.WindowInsetsControllerCompat.Impl
        void hide(int r2) {
            this.mInsetsController.hide(r2);
        }

        @Override // androidx.core.view.WindowInsetsControllerCompat.Impl
        public boolean isAppearanceLightNavigationBars() {
            if ((this.mInsetsController.getSystemBarsAppearance() & 16) == 0) goto L5;
            return true;
        L5:
            return false;
        }

        @Override // androidx.core.view.WindowInsetsControllerCompat.Impl
        public boolean isAppearanceLightStatusBars() {
            if ((this.mInsetsController.getSystemBarsAppearance() & 8) == 0) goto L5;
            return true;
        L5:
            return false;
        }

        /* JADX INFO: renamed from: lambda$addOnControllableInsetsChangedListener$0$androidx-core-view-WindowInsetsControllerCompat$Impl30 */
        /* synthetic */ void m37xe96d8c51(OnControllableInsetsChangedListener r2, WindowInsetsController r3, int r4) {
            if (this.mInsetsController != r3) goto L6;
            r2.onControllableInsetsChanged(this.mCompatController, r4);
            return;
        }

        @Override // androidx.core.view.WindowInsetsControllerCompat.Impl
        void removeOnControllableInsetsChangedListener(OnControllableInsetsChangedListener r3) {
            WindowInsetsController.OnControllableInsetsChangedListener r0 = this.mListeners.remove(r3);
            if (r0 == null) goto L6;
            this.mInsetsController.removeOnControllableInsetsChangedListener(r0);
            return;
        }

        @Override // androidx.core.view.WindowInsetsControllerCompat.Impl
        public void setAppearanceLightNavigationBars(boolean r4) {
            if (r4 == false) goto L9;
            if (this.mWindow == null) goto L7;
            setSystemUiFlag(16);
        L7:
            this.mInsetsController.setSystemBarsAppearance(16, 16);
            return;
        L9:
            if (this.mWindow == null) goto L11;
            unsetSystemUiFlag(16);
        L11:
            this.mInsetsController.setSystemBarsAppearance(0, 16);
        }

        @Override // androidx.core.view.WindowInsetsControllerCompat.Impl
        public void setAppearanceLightStatusBars(boolean r4) {
            if (r4 == false) goto L9;
            if (this.mWindow == null) goto L7;
            setSystemUiFlag(8192);
        L7:
            this.mInsetsController.setSystemBarsAppearance(8, 8);
            return;
        L9:
            if (this.mWindow == null) goto L11;
            unsetSystemUiFlag(8192);
        L11:
            this.mInsetsController.setSystemBarsAppearance(0, 8);
        }

        @Override // androidx.core.view.WindowInsetsControllerCompat.Impl
        void setSystemBarsBehavior(int r2) {
            this.mInsetsController.setSystemBarsBehavior(r2);
        }

        protected void setSystemUiFlag(int r3) {
            View r0 = this.mWindow.getDecorView();
            r0.setSystemUiVisibility(r0.getSystemUiVisibility() | r3);
        }

        @Override // androidx.core.view.WindowInsetsControllerCompat.Impl
        void show(int r3) {
            if (this.mWindow != null) goto L5;
        L9:
            this.mInsetsController.show(r3);
            return;
        L5:
            if ((r3 & 8) == 0) goto L9;
            if (Build.VERSION.SDK_INT >= 32) goto L9;
            ((InputMethodManager) this.mWindow.getContext().getSystemService("input_method")).isActive();
            goto L9
        }

        protected void unsetSystemUiFlag(int r4) {
            View r0 = this.mWindow.getDecorView();
            r0.setSystemUiVisibility(r0.getSystemUiVisibility() & (~r4));
        }
    }

    public interface OnControllableInsetsChangedListener {
        void onControllableInsetsChanged(WindowInsetsControllerCompat r1, int r2);
    }

    public WindowInsetsControllerCompat(Window r3, View r4) {
        if (Build.VERSION.SDK_INT < 30) goto L6;
        this.mImpl = new Impl30(r3, this);
        return;
    L6:
        if (Build.VERSION.SDK_INT < 26) goto L8;
        this.mImpl = new Impl26(r3, r4);
        return;
    L8:
        this.mImpl = new Impl23(r3, r4);
    }

    @Deprecated
    private WindowInsetsControllerCompat(WindowInsetsController r2) {
        this.mImpl = new Impl30(r2, this);
    }

    @Deprecated
    public static WindowInsetsControllerCompat toWindowInsetsControllerCompat(WindowInsetsController r1) {
        return new WindowInsetsControllerCompat(r1);
    }

    public void addOnControllableInsetsChangedListener(OnControllableInsetsChangedListener r2) {
        this.mImpl.addOnControllableInsetsChangedListener(r2);
    }

    public void controlWindowInsetsAnimation(int r8, long r9, Interpolator r11, CancellationSignal r12, WindowInsetsAnimationControlListenerCompat r13) {
        this.mImpl.controlWindowInsetsAnimation(r8, r9, r11, r12, r13);
    }

    public int getSystemBarsBehavior() {
        return this.mImpl.getSystemBarsBehavior();
    }

    public void hide(int r2) {
        this.mImpl.hide(r2);
    }

    public boolean isAppearanceLightNavigationBars() {
        return this.mImpl.isAppearanceLightNavigationBars();
    }

    public boolean isAppearanceLightStatusBars() {
        return this.mImpl.isAppearanceLightStatusBars();
    }

    public void removeOnControllableInsetsChangedListener(OnControllableInsetsChangedListener r2) {
        this.mImpl.removeOnControllableInsetsChangedListener(r2);
    }

    public void setAppearanceLightNavigationBars(boolean r2) {
        this.mImpl.setAppearanceLightNavigationBars(r2);
    }

    public void setAppearanceLightStatusBars(boolean r2) {
        this.mImpl.setAppearanceLightStatusBars(r2);
    }

    public void setSystemBarsBehavior(int r2) {
        this.mImpl.setSystemBarsBehavior(r2);
    }

    public void show(int r2) {
        this.mImpl.show(r2);
    }
}
