package androidx.core.view;

import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.DisplayCutout;
import android.view.View;
import android.view.WindowInsets;
import androidx.core.graphics.Insets;
import androidx.core.util.ObjectsCompat;
import androidx.core.util.Preconditions;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public class WindowInsetsCompat {
    public static final WindowInsetsCompat CONSUMED = null;
    private static final String TAG = "WindowInsetsCompat";
    private final Impl mImpl;

    static class Api21ReflectionHolder {
        private static Field sContentInsets;
        private static boolean sReflectionSucceeded;
        private static Field sStableInsets;
        private static Field sViewAttachInfoField;

        static {
            sViewAttachInfoField = View.class.getDeclaredField("mAttachInfo");     // Catch: ReflectiveOperationException -> L4
            sViewAttachInfoField.setAccessible(true);     // Catch: ReflectiveOperationException -> L4
            Class<?> r0 = Class.forName("android.view.View$AttachInfo");     // Catch: ReflectiveOperationException -> L4
            sStableInsets = r0.getDeclaredField("mStableInsets");     // Catch: ReflectiveOperationException -> L4
            sStableInsets.setAccessible(true);     // Catch: ReflectiveOperationException -> L4
            sContentInsets = r0.getDeclaredField("mContentInsets");     // Catch: ReflectiveOperationException -> L4
            sContentInsets.setAccessible(true);     // Catch: ReflectiveOperationException -> L4
            sReflectionSucceeded = true;     // Catch: ReflectiveOperationException -> L4
            return;
        L4:
            e = move-exception;
            Log.w(WindowInsetsCompat.TAG, "Failed to get visible insets from AttachInfo " + e.getMessage(), e);
        }

        private Api21ReflectionHolder() {
        }

        public static WindowInsetsCompat getRootWindowInsets(View r7) {
            if (sReflectionSucceeded == true) goto L5;
        L19:
            return null;
        L5:
            if (r7.isAttachedToWindow() == false) goto L19;
            Object r2 = sViewAttachInfoField.get(r7.getRootView());     // Catch: IllegalAccessException -> L16
            if (r2 == null) goto L18;
            Rect r3 = (Rect) sStableInsets.get(r2);     // Catch: IllegalAccessException -> L16
            Rect r4 = (Rect) sContentInsets.get(r2);     // Catch: IllegalAccessException -> L16
            if (r3 == null) goto L18;
            if (r4 == null) goto L18;
            WindowInsetsCompat r5 = new Builder().setStableInsets(Insets.m9of(r3)).setSystemWindowInsets(Insets.m9of(r4)).build();     // Catch: IllegalAccessException -> L16
            r5.setRootWindowInsets(r5);     // Catch: IllegalAccessException -> L16
            r5.copyRootViewBounds(r7.getRootView());     // Catch: IllegalAccessException -> L16
            return r5;
        L18:
            return null;
        L16:
            e = move-exception;
            Log.w(WindowInsetsCompat.TAG, "Failed to get insets from AttachInfo. " + e.getMessage(), e);
            goto L18
        }
    }

    public static final class Builder {
        private final BuilderImpl mImpl;

        public Builder() {
            if (Build.VERSION.SDK_INT < 30) goto L6;
            this.mImpl = new BuilderImpl30();
            return;
        L6:
            if (Build.VERSION.SDK_INT < 29) goto L8;
            this.mImpl = new BuilderImpl29();
            return;
        L8:
            this.mImpl = new BuilderImpl20();
        }

        public Builder(WindowInsetsCompat r3) {
            if (Build.VERSION.SDK_INT < 30) goto L6;
            this.mImpl = new BuilderImpl30(r3);
            return;
        L6:
            if (Build.VERSION.SDK_INT < 29) goto L8;
            this.mImpl = new BuilderImpl29(r3);
            return;
        L8:
            this.mImpl = new BuilderImpl20(r3);
        }

        public WindowInsetsCompat build() {
            return this.mImpl.build();
        }

        public Builder setDisplayCutout(DisplayCutoutCompat r2) {
            this.mImpl.setDisplayCutout(r2);
            return this;
        }

        public Builder setInsets(int r2, Insets r3) {
            this.mImpl.setInsets(r2, r3);
            return this;
        }

        public Builder setInsetsIgnoringVisibility(int r2, Insets r3) {
            this.mImpl.setInsetsIgnoringVisibility(r2, r3);
            return this;
        }

        @Deprecated
        public Builder setMandatorySystemGestureInsets(Insets r2) {
            this.mImpl.setMandatorySystemGestureInsets(r2);
            return this;
        }

        @Deprecated
        public Builder setStableInsets(Insets r2) {
            this.mImpl.setStableInsets(r2);
            return this;
        }

        @Deprecated
        public Builder setSystemGestureInsets(Insets r2) {
            this.mImpl.setSystemGestureInsets(r2);
            return this;
        }

        @Deprecated
        public Builder setSystemWindowInsets(Insets r2) {
            this.mImpl.setSystemWindowInsets(r2);
            return this;
        }

        @Deprecated
        public Builder setTappableElementInsets(Insets r2) {
            this.mImpl.setTappableElementInsets(r2);
            return this;
        }

        public Builder setVisible(int r2, boolean r3) {
            this.mImpl.setVisible(r2, r3);
            return this;
        }
    }

    private static class BuilderImpl {
        private final WindowInsetsCompat mInsets;
        Insets[] mInsetsTypeMask;

        BuilderImpl() {
            WindowInsetsCompat r2 = (WindowInsetsCompat) null;
            this(new WindowInsetsCompat(null));
        }

        BuilderImpl(WindowInsetsCompat r1) {
            this.mInsets = r1;
        }

        protected final void applyInsetTypes() {
            if (this.mInsetsTypeMask == null) goto L19;
            Insets r0 = this.mInsetsTypeMask[Type.indexOf(1)];
            Insets r2 = this.mInsetsTypeMask[Type.indexOf(2)];
            if (r2 != null) goto L7;
            r2 = this.mInsets.getInsets(2);
        L7:
            if (r0 != null) goto L9;
            r0 = this.mInsets.getInsets(1);
        L9:
            setSystemWindowInsets(Insets.max(r0, r2));
            Insets r1 = this.mInsetsTypeMask[Type.indexOf(16)];
            if (r1 == null) goto L12;
            setSystemGestureInsets(r1);
        L12:
            Insets r3 = this.mInsetsTypeMask[Type.indexOf(32)];
            if (r3 == null) goto L15;
            setMandatorySystemGestureInsets(r3);
        L15:
            Insets r4 = this.mInsetsTypeMask[Type.indexOf(64)];
            if (r4 == null) goto L20;
            setTappableElementInsets(r4);
            return;
        L20:
            return;
        }

        WindowInsetsCompat build() {
            applyInsetTypes();
            return this.mInsets;
        }

        void setDisplayCutout(DisplayCutoutCompat r1) {
        }

        void setInsets(int r4, Insets r5) {
            if (this.mInsetsTypeMask != null) goto L5;
            this.mInsetsTypeMask = new Insets[9];
        L5:
            int r0 = 1;
        L7:
            if (r0 > 256) goto L13;
            if ((r4 & r0) == 0) goto L12;
            this.mInsetsTypeMask[Type.indexOf(r0)] = r5;
        L12:
            r0 = r0 << 1;
            goto L7
        }

        void setInsetsIgnoringVisibility(int r3, Insets r4) {
            if (r3 == 8) goto L6;
            return;
        L6:
            throw new IllegalArgumentException("Ignoring visibility inset not available for IME");
        }

        void setMandatorySystemGestureInsets(Insets r1) {
        }

        void setStableInsets(Insets r1) {
        }

        void setSystemGestureInsets(Insets r1) {
        }

        void setSystemWindowInsets(Insets r1) {
        }

        void setTappableElementInsets(Insets r1) {
        }

        void setVisible(int r1, boolean r2) {
        }
    }

    private static class BuilderImpl20 extends BuilderImpl {
        private static Constructor<WindowInsets> sConstructor;
        private static boolean sConstructorFetched;
        private static Field sConsumedField;
        private static boolean sConsumedFieldFetched;
        private WindowInsets mPlatformInsets;
        private Insets mStableInsets;

        static {
            sConsumedFieldFetched = false;
            sConstructorFetched = false;
        }

        BuilderImpl20() {
            this.mPlatformInsets = createWindowInsetsInstance();
        }

        BuilderImpl20(WindowInsetsCompat r2) {
            super(r2);
            this.mPlatformInsets = r2.toWindowInsets();
        }

        private static WindowInsets createWindowInsetsInstance() {
            if (sConsumedFieldFetched == true) goto L10;
            sConsumedField = WindowInsets.class.getDeclaredField("CONSUMED");     // Catch: ReflectiveOperationException -> L6
        L8:
            sConsumedFieldFetched = true;
        L6:
            e = move-exception;
            Log.i(WindowInsetsCompat.TAG, "Could not retrieve WindowInsets.CONSUMED field", e);
        L10:
            if (sConsumedField == null) goto L19;
            WindowInsets r0 = (WindowInsets) sConsumedField.get(null);     // Catch: ReflectiveOperationException -> L16
            if (r0 == null) goto L19;
            return new WindowInsets(r0);
        L16:
            e = move-exception;
            Log.i(WindowInsetsCompat.TAG, "Could not get value from WindowInsets.CONSUMED field", e);
        L19:
            if (sConstructorFetched == true) goto L26;
            sConstructor = WindowInsets.class.getConstructor(new Class[]{Rect.class});     // Catch: ReflectiveOperationException -> L22
        L24:
            sConstructorFetched = true;
        L22:
            e = move-exception;
            Log.i(WindowInsetsCompat.TAG, "Could not retrieve WindowInsets(Rect) constructor", e);
        L26:
            if (sConstructor != null) goto L38;
        L31:
            return null;
        L38:
            return sConstructor.newInstance(new Object[]{new Rect()});
        L29:
            e = move-exception;
            Log.i(WindowInsetsCompat.TAG, "Could not invoke WindowInsets(Rect) constructor", e);
            goto L31
        }

        @Override // androidx.core.view.WindowInsetsCompat.BuilderImpl
        WindowInsetsCompat build() {
            applyInsetTypes();
            WindowInsetsCompat r0 = WindowInsetsCompat.toWindowInsetsCompat(this.mPlatformInsets);
            r0.setOverriddenInsets(this.mInsetsTypeMask);
            r0.setStableInsets(this.mStableInsets);
            return r0;
        }

        @Override // androidx.core.view.WindowInsetsCompat.BuilderImpl
        void setStableInsets(Insets r1) {
            this.mStableInsets = r1;
        }

        @Override // androidx.core.view.WindowInsetsCompat.BuilderImpl
        void setSystemWindowInsets(Insets r6) {
            if (this.mPlatformInsets == null) goto L6;
            this.mPlatformInsets = this.mPlatformInsets.replaceSystemWindowInsets(r6.left, r6.top, r6.right, r6.bottom);
            return;
        }
    }

    private static class BuilderImpl29 extends BuilderImpl {
        final WindowInsets.Builder mPlatBuilder;

        BuilderImpl29() {
            this.mPlatBuilder = new WindowInsets.Builder();
        }

        BuilderImpl29(WindowInsetsCompat r3) {
            super(r3);
            WindowInsets r0 = r3.toWindowInsets();
            if (r0 == null) goto L5;
            WindowInsets.Builder r1 = new WindowInsets.Builder(r0);
        L6:
            this.mPlatBuilder = r1;
            return;
        L5:
            r1 = new WindowInsets.Builder();
            goto L6
        }

        @Override // androidx.core.view.WindowInsetsCompat.BuilderImpl
        WindowInsetsCompat build() {
            applyInsetTypes();
            WindowInsetsCompat r0 = WindowInsetsCompat.toWindowInsetsCompat(this.mPlatBuilder.build());
            r0.setOverriddenInsets(this.mInsetsTypeMask);
            return r0;
        }

        @Override // androidx.core.view.WindowInsetsCompat.BuilderImpl
        void setDisplayCutout(DisplayCutoutCompat r3) {
            WindowInsets.Builder r0 = this.mPlatBuilder;
            if (r3 == null) goto L5;
            DisplayCutout r1 = r3.unwrap();
        L6:
            r0.setDisplayCutout(r1);
            return;
        L5:
            r1 = null;
            goto L6
        }

        @Override // androidx.core.view.WindowInsetsCompat.BuilderImpl
        void setMandatorySystemGestureInsets(Insets r3) {
            this.mPlatBuilder.setMandatorySystemGestureInsets(r3.toPlatformInsets());
        }

        @Override // androidx.core.view.WindowInsetsCompat.BuilderImpl
        void setStableInsets(Insets r3) {
            this.mPlatBuilder.setStableInsets(r3.toPlatformInsets());
        }

        @Override // androidx.core.view.WindowInsetsCompat.BuilderImpl
        void setSystemGestureInsets(Insets r3) {
            this.mPlatBuilder.setSystemGestureInsets(r3.toPlatformInsets());
        }

        @Override // androidx.core.view.WindowInsetsCompat.BuilderImpl
        void setSystemWindowInsets(Insets r3) {
            this.mPlatBuilder.setSystemWindowInsets(r3.toPlatformInsets());
        }

        @Override // androidx.core.view.WindowInsetsCompat.BuilderImpl
        void setTappableElementInsets(Insets r3) {
            this.mPlatBuilder.setTappableElementInsets(r3.toPlatformInsets());
        }
    }

    private static class BuilderImpl30 extends BuilderImpl29 {
        BuilderImpl30() {
        }

        BuilderImpl30(WindowInsetsCompat r1) {
            super(r1);
        }

        @Override // androidx.core.view.WindowInsetsCompat.BuilderImpl
        void setInsets(int r4, Insets r5) {
            this.mPlatBuilder.setInsets(TypeImpl30.toPlatformType(r4), r5.toPlatformInsets());
        }

        @Override // androidx.core.view.WindowInsetsCompat.BuilderImpl
        void setInsetsIgnoringVisibility(int r4, Insets r5) {
            this.mPlatBuilder.setInsetsIgnoringVisibility(TypeImpl30.toPlatformType(r4), r5.toPlatformInsets());
        }

        @Override // androidx.core.view.WindowInsetsCompat.BuilderImpl
        void setVisible(int r3, boolean r4) {
            this.mPlatBuilder.setVisible(TypeImpl30.toPlatformType(r3), r4);
        }
    }

    private static class Impl {
        static final WindowInsetsCompat CONSUMED = null;
        final WindowInsetsCompat mHost;

        static {
            CONSUMED = new Builder().build().consumeDisplayCutout().consumeStableInsets().consumeSystemWindowInsets();
        }

        Impl(WindowInsetsCompat r1) {
            this.mHost = r1;
        }

        WindowInsetsCompat consumeDisplayCutout() {
            return this.mHost;
        }

        WindowInsetsCompat consumeStableInsets() {
            return this.mHost;
        }

        WindowInsetsCompat consumeSystemWindowInsets() {
            return this.mHost;
        }

        void copyRootViewBounds(View r1) {
        }

        void copyWindowDataInto(WindowInsetsCompat r1) {
        }

        public boolean equals(Object r6) {
            if (this != r6) goto L6;
            return true;
        L6:
            if ((r6 instanceof Impl) == true) goto L8;
            return false;
        L8:
            Impl r1 = (Impl) r6;
            if (isRound() != r1.isRound()) goto L20;
            if (isConsumed() != r1.isConsumed()) goto L20;
            if (ObjectsCompat.equals(getSystemWindowInsets(), r1.getSystemWindowInsets()) == false) goto L20;
            if (ObjectsCompat.equals(getStableInsets(), r1.getStableInsets()) == false) goto L20;
            if (ObjectsCompat.equals(getDisplayCutout(), r1.getDisplayCutout()) == false) goto L20;
            return true;
        L20:
            return false;
        }

        DisplayCutoutCompat getDisplayCutout() {
            return null;
        }

        Insets getInsets(int r2) {
            return Insets.NONE;
        }

        Insets getInsetsIgnoringVisibility(int r3) {
            if ((r3 & 8) != 0) goto L7;
            return Insets.NONE;
        L7:
            throw new IllegalArgumentException("Unable to query the maximum insets for IME");
        }

        Insets getMandatorySystemGestureInsets() {
            return getSystemWindowInsets();
        }

        Insets getStableInsets() {
            return Insets.NONE;
        }

        Insets getSystemGestureInsets() {
            return getSystemWindowInsets();
        }

        Insets getSystemWindowInsets() {
            return Insets.NONE;
        }

        Insets getTappableElementInsets() {
            return getSystemWindowInsets();
        }

        public int hashCode() {
            return ObjectsCompat.hash(new Object[]{Boolean.valueOf(isRound()), Boolean.valueOf(isConsumed()), getSystemWindowInsets(), getStableInsets(), getDisplayCutout()});
        }

        WindowInsetsCompat inset(int r2, int r3, int r4, int r5) {
            return CONSUMED;
        }

        boolean isConsumed() {
            return false;
        }

        boolean isRound() {
            return false;
        }

        boolean isVisible(int r2) {
            return true;
        }

        public void setOverriddenInsets(Insets[] r1) {
        }

        void setRootViewData(Insets r1) {
        }

        void setRootWindowInsets(WindowInsetsCompat r1) {
        }

        public void setStableInsets(Insets r1) {
        }
    }

    private static class Impl20 extends Impl {
        private static Class<?> sAttachInfoClass;
        private static Field sAttachInfoField;
        private static Method sGetViewRootImplMethod;
        private static Field sVisibleInsetsField;
        private static boolean sVisibleRectReflectionFetched;
        private Insets[] mOverriddenInsets;
        final WindowInsets mPlatformInsets;
        Insets mRootViewVisibleInsets;
        private WindowInsetsCompat mRootWindowInsets;
        private Insets mSystemWindowInsets;

        static {
            sVisibleRectReflectionFetched = false;
        }

        Impl20(WindowInsetsCompat r2, WindowInsets r3) {
            super(r2);
            this.mSystemWindowInsets = null;
            this.mPlatformInsets = r3;
        }

        Impl20(WindowInsetsCompat r3, Impl20 r4) {
            this(r3, new WindowInsets(r4.mPlatformInsets));
        }

        private Insets getInsets(int r4, boolean r5) {
            Insets r0 = Insets.NONE;
            int r1 = 1;
        L4:
            if (r1 > 256) goto L10;
            if ((r4 & r1) == 0) goto L9;
            r0 = Insets.max(r0, getInsetsForType(r1, r5));
        L9:
            r1 = r1 << 1;
            goto L4
        L10:
            return r0;
        }

        private Insets getRootStableInsets() {
            if (this.mRootWindowInsets == null) goto L7;
            return this.mRootWindowInsets.getStableInsets();
        L7:
            return Insets.NONE;
        }

        private Insets getVisibleInsets(View r6) {
            if (Build.VERSION.SDK_INT >= 30) goto L27;
            if (sVisibleRectReflectionFetched == true) goto L8;
            loadReflectionField();
        L8:
            if (sGetViewRootImplMethod != null) goto L10;
        L25:
            return null;
        L10:
            if (sAttachInfoClass == null) goto L25;
            if (sVisibleInsetsField == null) goto L25;
            Object r1 = sGetViewRootImplMethod.invoke(r6, new Object[0]);     // Catch: ReflectiveOperationException -> L22
            if (r1 != null) goto L18;
            Log.w(WindowInsetsCompat.TAG, "Failed to get visible insets. getViewRootImpl() returned null from the provided view. This means that the view is either not attached or the method has been overridden", new NullPointerException());     // Catch: ReflectiveOperationException -> L22
            return null;
        L18:
            Rect r4 = (Rect) sVisibleInsetsField.get(sAttachInfoField.get(r1));     // Catch: ReflectiveOperationException -> L22
            if (r4 != null) goto L21;
            return null;
        L21:
            return Insets.m9of(r4);
        L22:
            e = move-exception;
            Log.e(WindowInsetsCompat.TAG, "Failed to get visible insets. (Reflection error). " + e.getMessage(), e);
            return null;
        L27:
            throw new UnsupportedOperationException("getVisibleInsets() should not be called on API >= 30. Use WindowInsets.isVisible() instead.");
        }

        private static void loadReflectionField() {
            sGetViewRootImplMethod = View.class.getDeclaredMethod("getViewRootImpl", new Class[0]);     // Catch: ReflectiveOperationException -> L5
            sAttachInfoClass = Class.forName("android.view.View$AttachInfo");     // Catch: ReflectiveOperationException -> L5
            sVisibleInsetsField = sAttachInfoClass.getDeclaredField("mVisibleInsets");     // Catch: ReflectiveOperationException -> L5
            sAttachInfoField = Class.forName("android.view.ViewRootImpl").getDeclaredField("mAttachInfo");     // Catch: ReflectiveOperationException -> L5
            sVisibleInsetsField.setAccessible(true);     // Catch: ReflectiveOperationException -> L5
            sAttachInfoField.setAccessible(true);     // Catch: ReflectiveOperationException -> L5
        L7:
            sVisibleRectReflectionFetched = true;
            return;
        L5:
            e = move-exception;
            Log.e(WindowInsetsCompat.TAG, "Failed to get visible insets. (Reflection error). " + e.getMessage(), e);
            goto L7
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        void copyRootViewBounds(View r2) {
            Insets r0 = getVisibleInsets(r2);
            if (r0 != null) goto L5;
            r0 = Insets.NONE;
        L5:
            setRootViewData(r0);
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        void copyWindowDataInto(WindowInsetsCompat r2) {
            r2.setRootWindowInsets(this.mRootWindowInsets);
            r2.setRootViewData(this.mRootViewVisibleInsets);
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        public boolean equals(Object r4) {
            if (super.equals(r4) == true) goto L7;
            return false;
        L7:
            return Objects.equals(this.mRootViewVisibleInsets, ((Impl20) r4).mRootViewVisibleInsets);
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        public Insets getInsets(int r2) {
            return getInsets(r2, false);
        }

        protected Insets getInsetsForType(int r8, boolean r9) {
            Insets r0 = null;
            switch(r8) {
                case 1: goto L54;
                case 2: goto L42;
                case 8: goto L23;
                case 16: goto L21;
                case 32: goto L19;
                case 64: goto L17;
                case 128: goto L7;
                default: goto L5;
            };
        L42:
            if (r9 == false) goto L45;
            Insets r1 = getRootStableInsets();
            Insets r2 = getStableInsets();
            return Insets.m8of(Math.max(r1.left, r2.left), 0, Math.max(r1.right, r2.right), Math.max(r1.bottom, r2.bottom));
        L45:
            Insets r3 = getSystemWindowInsets();
            if (this.mRootWindowInsets == null) goto L49;
            r0 = this.mRootWindowInsets.getStableInsets();
        L49:
            int r4 = r3.bottom;
            if (r0 == null) goto L53;
            r4 = Math.min(r4, r0.bottom);
        L53:
            return Insets.m8of(r3.left, 0, r3.right, r4);
        L54:
            if (r9 == false) goto L58;
            return Insets.m8of(0, Math.max(getRootStableInsets().top, getSystemWindowInsets().top), 0, 0);
        L58:
            return Insets.m8of(0, getSystemWindowInsets().top, 0, 0);
        L5:
            return Insets.NONE;
        L7:
            if (this.mRootWindowInsets == null) goto L9;
            DisplayCutoutCompat r5 = this.mRootWindowInsets.getDisplayCutout();
        L11:
            if (r5 == null) goto L15;
            return Insets.m8of(r5.getSafeInsetLeft(), r5.getSafeInsetTop(), r5.getSafeInsetRight(), r5.getSafeInsetBottom());
        L15:
            return Insets.NONE;
        L9:
            r5 = getDisplayCutout();
            goto L11
        L17:
            return getTappableElementInsets();
        L19:
            return getMandatorySystemGestureInsets();
        L21:
            return getSystemGestureInsets();
        L23:
            if (this.mOverriddenInsets == null) goto L26;
            r0 = this.mOverriddenInsets[Type.indexOf(8)];
        L26:
            if (r0 == null) goto L28;
            return r0;
        L28:
            Insets r6 = getSystemWindowInsets();
            Insets r7 = getRootStableInsets();
            if (r6.bottom <= r7.bottom) goto L33;
            return Insets.m8of(0, 0, 0, r6.bottom);
        L33:
            if (this.mRootViewVisibleInsets == null) goto L41;
            if (this.mRootViewVisibleInsets.equals(Insets.NONE) == true) goto L41;
            if (this.mRootViewVisibleInsets.bottom <= r7.bottom) goto L41;
            return Insets.m8of(0, 0, 0, this.mRootViewVisibleInsets.bottom);
        L41:
            return Insets.NONE;
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        public Insets getInsetsIgnoringVisibility(int r2) {
            return getInsets(r2, true);
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        final Insets getSystemWindowInsets() {
            if (this.mSystemWindowInsets != null) goto L6;
            this.mSystemWindowInsets = Insets.m8of(this.mPlatformInsets.getSystemWindowInsetLeft(), this.mPlatformInsets.getSystemWindowInsetTop(), this.mPlatformInsets.getSystemWindowInsetRight(), this.mPlatformInsets.getSystemWindowInsetBottom());
        L6:
            return this.mSystemWindowInsets;
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        WindowInsetsCompat inset(int r3, int r4, int r5, int r6) {
            Builder r0 = new Builder(WindowInsetsCompat.toWindowInsetsCompat(this.mPlatformInsets));
            r0.setSystemWindowInsets(WindowInsetsCompat.insetInsets(getSystemWindowInsets(), r3, r4, r5, r6));
            r0.setStableInsets(WindowInsetsCompat.insetInsets(getStableInsets(), r3, r4, r5, r6));
            return r0.build();
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        boolean isRound() {
            return this.mPlatformInsets.isRound();
        }

        protected boolean isTypeVisible(int r4) {
            switch(r4) {
                case 1: goto L7;
                case 2: goto L7;
                case 4: goto L5;
                case 8: goto L7;
                case 128: goto L7;
                default: goto L4;
            };
        L4:
            return true;
        L5:
            return false;
        L7:
            return true ^ getInsetsForType(r4, false).equals(Insets.NONE);
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        boolean isVisible(int r3) {
            int r0 = 1;
        L4:
            if (r0 > 256) goto L13;
            if ((r3 & r0) == 0) goto L12;
            if (isTypeVisible(r0) == true) goto L12;
            return false;
        L12:
            r0 = r0 << 1;
            goto L4
        L13:
            return true;
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        public void setOverriddenInsets(Insets[] r1) {
            this.mOverriddenInsets = r1;
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        void setRootViewData(Insets r1) {
            this.mRootViewVisibleInsets = r1;
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        void setRootWindowInsets(WindowInsetsCompat r1) {
            this.mRootWindowInsets = r1;
        }
    }

    private static class Impl21 extends Impl20 {
        private Insets mStableInsets;

        Impl21(WindowInsetsCompat r2, WindowInsets r3) {
            super(r2, r3);
            this.mStableInsets = null;
        }

        Impl21(WindowInsetsCompat r2, Impl21 r3) {
            super(r2, r3);
            this.mStableInsets = null;
            this.mStableInsets = r3.mStableInsets;
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        WindowInsetsCompat consumeStableInsets() {
            return WindowInsetsCompat.toWindowInsetsCompat(this.mPlatformInsets.consumeStableInsets());
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        WindowInsetsCompat consumeSystemWindowInsets() {
            return WindowInsetsCompat.toWindowInsetsCompat(this.mPlatformInsets.consumeSystemWindowInsets());
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        final Insets getStableInsets() {
            if (this.mStableInsets != null) goto L6;
            this.mStableInsets = Insets.m8of(this.mPlatformInsets.getStableInsetLeft(), this.mPlatformInsets.getStableInsetTop(), this.mPlatformInsets.getStableInsetRight(), this.mPlatformInsets.getStableInsetBottom());
        L6:
            return this.mStableInsets;
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        boolean isConsumed() {
            return this.mPlatformInsets.isConsumed();
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        public void setStableInsets(Insets r1) {
            this.mStableInsets = r1;
        }
    }

    private static class Impl28 extends Impl21 {
        Impl28(WindowInsetsCompat r1, WindowInsets r2) {
            super(r1, r2);
        }

        Impl28(WindowInsetsCompat r1, Impl28 r2) {
            super(r1, r2);
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        WindowInsetsCompat consumeDisplayCutout() {
            return WindowInsetsCompat.toWindowInsetsCompat(this.mPlatformInsets.consumeDisplayCutout());
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl20, androidx.core.view.WindowInsetsCompat.Impl
        public boolean equals(Object r6) {
            if (this != r6) goto L6;
            return true;
        L6:
            if ((r6 instanceof Impl28) == true) goto L8;
            return false;
        L8:
            Impl28 r1 = (Impl28) r6;
            if (Objects.equals(this.mPlatformInsets, r1.mPlatformInsets) == false) goto L14;
            if (Objects.equals(this.mRootViewVisibleInsets, r1.mRootViewVisibleInsets) == false) goto L14;
            return true;
        L14:
            return false;
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        DisplayCutoutCompat getDisplayCutout() {
            return DisplayCutoutCompat.wrap(this.mPlatformInsets.getDisplayCutout());
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        public int hashCode() {
            return this.mPlatformInsets.hashCode();
        }
    }

    private static class Impl29 extends Impl28 {
        private Insets mMandatorySystemGestureInsets;
        private Insets mSystemGestureInsets;
        private Insets mTappableElementInsets;

        Impl29(WindowInsetsCompat r2, WindowInsets r3) {
            super(r2, r3);
            this.mSystemGestureInsets = null;
            this.mMandatorySystemGestureInsets = null;
            this.mTappableElementInsets = null;
        }

        Impl29(WindowInsetsCompat r2, Impl29 r3) {
            super(r2, r3);
            this.mSystemGestureInsets = null;
            this.mMandatorySystemGestureInsets = null;
            this.mTappableElementInsets = null;
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        Insets getMandatorySystemGestureInsets() {
            if (this.mMandatorySystemGestureInsets != null) goto L6;
            this.mMandatorySystemGestureInsets = Insets.toCompatInsets(this.mPlatformInsets.getMandatorySystemGestureInsets());
        L6:
            return this.mMandatorySystemGestureInsets;
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        Insets getSystemGestureInsets() {
            if (this.mSystemGestureInsets != null) goto L6;
            this.mSystemGestureInsets = Insets.toCompatInsets(this.mPlatformInsets.getSystemGestureInsets());
        L6:
            return this.mSystemGestureInsets;
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        Insets getTappableElementInsets() {
            if (this.mTappableElementInsets != null) goto L6;
            this.mTappableElementInsets = Insets.toCompatInsets(this.mPlatformInsets.getTappableElementInsets());
        L6:
            return this.mTappableElementInsets;
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl20, androidx.core.view.WindowInsetsCompat.Impl
        WindowInsetsCompat inset(int r2, int r3, int r4, int r5) {
            return WindowInsetsCompat.toWindowInsetsCompat(this.mPlatformInsets.inset(r2, r3, r4, r5));
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl21, androidx.core.view.WindowInsetsCompat.Impl
        public void setStableInsets(Insets r1) {
        }
    }

    private static class Impl30 extends Impl29 {
        static final WindowInsetsCompat CONSUMED = null;

        static {
            CONSUMED = WindowInsetsCompat.toWindowInsetsCompat(WindowInsets.CONSUMED);
        }

        Impl30(WindowInsetsCompat r1, WindowInsets r2) {
            super(r1, r2);
        }

        Impl30(WindowInsetsCompat r1, Impl30 r2) {
            super(r1, r2);
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl20, androidx.core.view.WindowInsetsCompat.Impl
        final void copyRootViewBounds(View r1) {
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl20, androidx.core.view.WindowInsetsCompat.Impl
        public Insets getInsets(int r3) {
            return Insets.toCompatInsets(this.mPlatformInsets.getInsets(TypeImpl30.toPlatformType(r3)));
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl20, androidx.core.view.WindowInsetsCompat.Impl
        public Insets getInsetsIgnoringVisibility(int r3) {
            return Insets.toCompatInsets(this.mPlatformInsets.getInsetsIgnoringVisibility(TypeImpl30.toPlatformType(r3)));
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl20, androidx.core.view.WindowInsetsCompat.Impl
        public boolean isVisible(int r3) {
            return this.mPlatformInsets.isVisible(TypeImpl30.toPlatformType(r3));
        }
    }

    public static final class Type {
        static final int CAPTION_BAR = 4;
        static final int DISPLAY_CUTOUT = 128;
        static final int FIRST = 1;
        static final int IME = 8;
        static final int LAST = 256;
        static final int MANDATORY_SYSTEM_GESTURES = 32;
        static final int NAVIGATION_BARS = 2;
        static final int SIZE = 9;
        static final int STATUS_BARS = 1;
        static final int SYSTEM_GESTURES = 16;
        static final int TAPPABLE_ELEMENT = 64;
        static final int WINDOW_DECOR = 256;

        @Retention(RetentionPolicy.SOURCE)
        public @interface InsetsType {
        }

        private Type() {
        }

        static int all() {
            return -1;
        }

        public static int captionBar() {
            return 4;
        }

        public static int displayCutout() {
            return 128;
        }

        public static int ime() {
            return 8;
        }

        static int indexOf(int r3) {
            switch(r3) {
                case 1: goto L21;
                case 2: goto L19;
                case 4: goto L17;
                case 8: goto L15;
                case 16: goto L13;
                case 32: goto L11;
                case 64: goto L9;
                case 128: goto L7;
                case 256: goto L5;
                default: goto L4;
            };
        L5:
            return 8;
        L7:
            return 7;
        L9:
            return 6;
        L11:
            return 5;
        L13:
            return 4;
        L15:
            return 3;
        L17:
            return 2;
        L19:
            return 1;
        L21:
            return 0;
        L4:
            throw new IllegalArgumentException("type needs to be >= FIRST and <= LAST, type=" + r3);
        }

        public static int mandatorySystemGestures() {
            return 32;
        }

        public static int navigationBars() {
            return 2;
        }

        public static int statusBars() {
            return 1;
        }

        public static int systemBars() {
            return 7;
        }

        public static int systemGestures() {
            return 16;
        }

        public static int tappableElement() {
            return 64;
        }
    }

    private static final class TypeImpl30 {
        private TypeImpl30() {
        }

        static int toPlatformType(int r3) {
            int r0 = 0;
            int r1 = 1;
        L4:
            if (r1 > 256) goto L18;
            if ((r3 & r1) == 0) goto L17;
            switch(r1) {
                case 1: goto L16;
                case 2: goto L15;
                case 4: goto L14;
                case 8: goto L13;
                case 16: goto L12;
                case 32: goto L11;
                case 64: goto L10;
                case 128: goto L9;
                default: goto L17;
            };
        L9:
            r0 = r0 | WindowInsets.Type.displayCutout();
            goto L17
        L10:
            r0 = r0 | WindowInsets.Type.tappableElement();
            goto L17
        L11:
            r0 = r0 | WindowInsets.Type.mandatorySystemGestures();
            goto L17
        L12:
            r0 = r0 | WindowInsets.Type.systemGestures();
            goto L17
        L13:
            r0 = r0 | WindowInsets.Type.ime();
            goto L17
        L14:
            r0 = r0 | WindowInsets.Type.captionBar();
            goto L17
        L15:
            r0 = r0 | WindowInsets.Type.navigationBars();
            goto L17
        L16:
            r0 = r0 | WindowInsets.Type.statusBars();
        L17:
            r1 = r1 << 1;
            goto L4
        L18:
            return r0;
        }
    }

    static {
        if (Build.VERSION.SDK_INT < 30) goto L5;
        CONSUMED = Impl30.CONSUMED;
        return;
    L5:
        CONSUMED = Impl.CONSUMED;
    }

    private WindowInsetsCompat(WindowInsets r3) {
        if (Build.VERSION.SDK_INT < 30) goto L6;
        this.mImpl = new Impl30(this, r3);
        return;
    L6:
        if (Build.VERSION.SDK_INT < 29) goto L9;
        this.mImpl = new Impl29(this, r3);
        return;
    L9:
        if (Build.VERSION.SDK_INT < 28) goto L11;
        this.mImpl = new Impl28(this, r3);
        return;
    L11:
        this.mImpl = new Impl21(this, r3);
    }

    public WindowInsetsCompat(WindowInsetsCompat r4) {
        if (r4 == null) goto L27;
        Impl r0 = r4.mImpl;
        if (Build.VERSION.SDK_INT < 30) goto L10;
        if ((r0 instanceof Impl30) == false) goto L10;
        this.mImpl = new Impl30(this, (Impl30) r0);
    L26:
        r0.copyWindowDataInto(this);
        return;
    L10:
        if (Build.VERSION.SDK_INT < 29) goto L15;
        if ((r0 instanceof Impl29) == false) goto L15;
        this.mImpl = new Impl29(this, (Impl29) r0);
    L15:
        if (Build.VERSION.SDK_INT < 28) goto L20;
        if ((r0 instanceof Impl28) == false) goto L20;
        this.mImpl = new Impl28(this, (Impl28) r0);
    L20:
        if ((r0 instanceof Impl21) == false) goto L23;
        this.mImpl = new Impl21(this, (Impl21) r0);
        goto L26
    L23:
        if ((r0 instanceof Impl20) == false) goto L25;
        this.mImpl = new Impl20(this, (Impl20) r0);
        goto L26
    L25:
        this.mImpl = new Impl(this);
        goto L26
    L27:
        this.mImpl = new Impl(this);
    }

    static Insets insetInsets(Insets r5, int r6, int r7, int r8, int r9) {
        int r0 = Math.max(0, r5.left - r6);
        int r2 = Math.max(0, r5.top - r7);
        int r3 = Math.max(0, r5.right - r8);
        int r1 = Math.max(0, r5.bottom - r9);
        if (r0 != r6) goto L9;
        if (r2 != r7) goto L9;
        if (r3 != r8) goto L9;
        if (r1 != r9) goto L9;
        return r5;
    L9:
        return Insets.m8of(r0, r2, r3, r1);
    }

    public static WindowInsetsCompat toWindowInsetsCompat(WindowInsets r1) {
        return toWindowInsetsCompat(r1, null);
    }

    public static WindowInsetsCompat toWindowInsetsCompat(WindowInsets r2, View r3) {
        WindowInsetsCompat r0 = new WindowInsetsCompat((WindowInsets) Preconditions.checkNotNull(r2));
        if (r3 != null) goto L5;
    L7:
        return r0;
    L5:
        if (ViewCompat.isAttachedToWindow(r3) == false) goto L7;
        r0.setRootWindowInsets(ViewCompat.getRootWindowInsets(r3));
        r0.copyRootViewBounds(r3.getRootView());
        goto L7
    }

    @Deprecated
    public WindowInsetsCompat consumeDisplayCutout() {
        return this.mImpl.consumeDisplayCutout();
    }

    @Deprecated
    public WindowInsetsCompat consumeStableInsets() {
        return this.mImpl.consumeStableInsets();
    }

    @Deprecated
    public WindowInsetsCompat consumeSystemWindowInsets() {
        return this.mImpl.consumeSystemWindowInsets();
    }

    void copyRootViewBounds(View r2) {
        this.mImpl.copyRootViewBounds(r2);
    }

    public boolean equals(Object r4) {
        if (this != r4) goto L6;
        return true;
    L6:
        if ((r4 instanceof WindowInsetsCompat) == true) goto L10;
        return false;
    L10:
        return ObjectsCompat.equals(this.mImpl, ((WindowInsetsCompat) r4).mImpl);
    }

    public DisplayCutoutCompat getDisplayCutout() {
        return this.mImpl.getDisplayCutout();
    }

    public Insets getInsets(int r2) {
        return this.mImpl.getInsets(r2);
    }

    public Insets getInsetsIgnoringVisibility(int r2) {
        return this.mImpl.getInsetsIgnoringVisibility(r2);
    }

    @Deprecated
    public Insets getMandatorySystemGestureInsets() {
        return this.mImpl.getMandatorySystemGestureInsets();
    }

    @Deprecated
    public int getStableInsetBottom() {
        return this.mImpl.getStableInsets().bottom;
    }

    @Deprecated
    public int getStableInsetLeft() {
        return this.mImpl.getStableInsets().left;
    }

    @Deprecated
    public int getStableInsetRight() {
        return this.mImpl.getStableInsets().right;
    }

    @Deprecated
    public int getStableInsetTop() {
        return this.mImpl.getStableInsets().top;
    }

    @Deprecated
    public Insets getStableInsets() {
        return this.mImpl.getStableInsets();
    }

    @Deprecated
    public Insets getSystemGestureInsets() {
        return this.mImpl.getSystemGestureInsets();
    }

    @Deprecated
    public int getSystemWindowInsetBottom() {
        return this.mImpl.getSystemWindowInsets().bottom;
    }

    @Deprecated
    public int getSystemWindowInsetLeft() {
        return this.mImpl.getSystemWindowInsets().left;
    }

    @Deprecated
    public int getSystemWindowInsetRight() {
        return this.mImpl.getSystemWindowInsets().right;
    }

    @Deprecated
    public int getSystemWindowInsetTop() {
        return this.mImpl.getSystemWindowInsets().top;
    }

    @Deprecated
    public Insets getSystemWindowInsets() {
        return this.mImpl.getSystemWindowInsets();
    }

    @Deprecated
    public Insets getTappableElementInsets() {
        return this.mImpl.getTappableElementInsets();
    }

    public boolean hasInsets() {
        if (getInsets(Type.all()).equals(Insets.NONE) == true) goto L5;
    L10:
        return true;
    L5:
        if (getInsetsIgnoringVisibility(Type.all() ^ Type.ime()).equals(Insets.NONE) == false) goto L10;
        if (getDisplayCutout() != null) goto L10;
        return false;
    }

    @Deprecated
    public boolean hasStableInsets() {
        return !this.mImpl.getStableInsets().equals(Insets.NONE);
    }

    @Deprecated
    public boolean hasSystemWindowInsets() {
        return !this.mImpl.getSystemWindowInsets().equals(Insets.NONE);
    }

    public int hashCode() {
        if (this.mImpl != null) goto L6;
        return 0;
    L6:
        return this.mImpl.hashCode();
    }

    public WindowInsetsCompat inset(int r2, int r3, int r4, int r5) {
        return this.mImpl.inset(r2, r3, r4, r5);
    }

    public WindowInsetsCompat inset(Insets r5) {
        return inset(r5.left, r5.top, r5.right, r5.bottom);
    }

    public boolean isConsumed() {
        return this.mImpl.isConsumed();
    }

    public boolean isRound() {
        return this.mImpl.isRound();
    }

    public boolean isVisible(int r2) {
        return this.mImpl.isVisible(r2);
    }

    @Deprecated
    public WindowInsetsCompat replaceSystemWindowInsets(int r3, int r4, int r5, int r6) {
        return new Builder(this).setSystemWindowInsets(Insets.m8of(r3, r4, r5, r6)).build();
    }

    @Deprecated
    public WindowInsetsCompat replaceSystemWindowInsets(Rect r3) {
        return new Builder(this).setSystemWindowInsets(Insets.m9of(r3)).build();
    }

    void setOverriddenInsets(Insets[] r2) {
        this.mImpl.setOverriddenInsets(r2);
    }

    void setRootViewData(Insets r2) {
        this.mImpl.setRootViewData(r2);
    }

    void setRootWindowInsets(WindowInsetsCompat r2) {
        this.mImpl.setRootWindowInsets(r2);
    }

    void setStableInsets(Insets r2) {
        this.mImpl.setStableInsets(r2);
    }

    public WindowInsets toWindowInsets() {
        if ((this.mImpl instanceof Impl20) == true) goto L7;
        return null;
    L7:
        return ((Impl20) this.mImpl).mPlatformInsets;
    }
}
