package androidx.core.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import java.lang.reflect.Field;

/* JADX INFO: loaded from: classes.dex */
public final class LayoutInflaterCompat {
    private static final String TAG = "LayoutInflaterCompatHC";
    private static boolean sCheckedField;
    private static Field sLayoutInflaterFactory2Field;

    static class Factory2Wrapper implements LayoutInflater.Factory2 {
        final LayoutInflaterFactory mDelegateFactory;

        Factory2Wrapper(LayoutInflaterFactory r1) {
            this.mDelegateFactory = r1;
        }

        @Override // android.view.LayoutInflater.Factory2
        public View onCreateView(View r2, String r3, Context r4, AttributeSet r5) {
            return this.mDelegateFactory.onCreateView(r2, r3, r4, r5);
        }

        @Override // android.view.LayoutInflater.Factory
        public View onCreateView(String r3, Context r4, AttributeSet r5) {
            return this.mDelegateFactory.onCreateView(null, r3, r4, r5);
        }

        public String toString() {
            return getClass().getName() + "{" + this.mDelegateFactory + "}";
        }
    }

    private LayoutInflaterCompat() {
    }

    private static void forceSetFactory2(LayoutInflater r6, LayoutInflater.Factory2 r7) {
        if (sCheckedField == true) goto L11;
        sLayoutInflaterFactory2Field = LayoutInflater.class.getDeclaredField("mFactory2");     // Catch: NoSuchFieldException -> L7
        sLayoutInflaterFactory2Field.setAccessible(true);     // Catch: NoSuchFieldException -> L7
    L9:
        sCheckedField = true;
    L7:
        e = move-exception;
        Log.e(TAG, "forceSetFactory2 Could not find field 'mFactory2' on class " + LayoutInflater.class.getName() + "; inflation may have unexpected results.", e);
    L11:
        if (sLayoutInflaterFactory2Field == null) goto L21;
        sLayoutInflaterFactory2Field.set(r6, r7);     // Catch: IllegalAccessException -> L14
        return;
    L14:
        e = move-exception;
        Log.e(TAG, "forceSetFactory2 could not set the Factory2 on LayoutInflater " + r6 + "; inflation may have unexpected results.", e);
        return;
    }

    @Deprecated
    public static LayoutInflaterFactory getFactory(LayoutInflater r2) {
        LayoutInflater.Factory r0 = r2.getFactory();
        if ((r0 instanceof Factory2Wrapper) == true) goto L5;
        return null;
    L5:
        return ((Factory2Wrapper) r0).mDelegateFactory;
    }

    @Deprecated
    public static void setFactory(LayoutInflater r1, LayoutInflaterFactory r2) {
        r1.setFactory2(new Factory2Wrapper(r2));
    }

    public static void setFactory2(LayoutInflater r0, LayoutInflater.Factory2 r1) {
        r0.setFactory2(r1);
    }
}
