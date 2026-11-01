package androidx.core.app;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import androidx.collection.SimpleArrayMap;
import androidx.core.os.BuildCompat;
import androidx.core.view.KeyEventDispatcher;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.ReportFragment;

/* JADX INFO: loaded from: classes.dex */
public class ComponentActivity extends Activity implements LifecycleOwner, KeyEventDispatcher.Component {
    private SimpleArrayMap<Class<? extends ExtraData>, ExtraData> mExtraDataMap;
    private LifecycleRegistry mLifecycleRegistry;

    @Deprecated
    public static class ExtraData {
        public ExtraData() {
        }
    }

    public ComponentActivity() {
        this.mExtraDataMap = new SimpleArrayMap();
        this.mLifecycleRegistry = new LifecycleRegistry(this);
    }

    private static boolean shouldSkipDump(String[] r4) {
        if (r4 != null) goto L5;
    L41:
        return false;
    L5:
        if (r4.length <= 0) goto L41;
        String r1 = r4[0];
        switch(r1.hashCode()) {
            case -645125871: goto L22;
            case 100470631: goto L19;
            case 472614934: goto L16;
            case 1159329357: goto L13;
            case 1455016274: goto L10;
            default: goto L24;
        };
    L24:
        byte r2 = -1;
    L25:
        switch(r2) {
            case 0: goto L38;
            case 1: goto L34;
            case 2: goto L30;
            case 3: goto L28;
            case 4: goto L28;
            default: goto L41;
        };
    L28:
        return BuildCompat.isAtLeastT();
    L30:
        if (Build.VERSION.SDK_INT >= 31) goto L32;
        return false;
    L32:
        return true;
    L34:
        if (Build.VERSION.SDK_INT >= 29) goto L36;
        return false;
    L36:
        return true;
    L38:
        if (Build.VERSION.SDK_INT >= 26) goto L40;
        return false;
    L40:
        return true;
    L10:
        if (r1.equals("--autofill") == false) goto L24;
        r2 = 0;
        goto L25
    L13:
        if (r1.equals("--contentcapture") == false) goto L24;
        r2 = 1;
        goto L25
    L16:
        if (r1.equals("--list-dumpables") == false) goto L24;
        r2 = 3;
        goto L25
    L19:
        if (r1.equals("--dump-dumpable") == false) goto L24;
        r2 = 4;
        goto L25
    L22:
        if (r1.equals("--translation") == false) goto L24;
        r2 = 2;
        goto L25
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public boolean dispatchKeyEvent(KeyEvent r3) {
        View r0 = getWindow().getDecorView();
        if (r0 == null) goto L9;
        if (KeyEventDispatcher.dispatchBeforeHierarchy(r0, r3) == false) goto L9;
        return true;
    L9:
        return KeyEventDispatcher.dispatchKeyEvent(this, r0, this, r3);
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public boolean dispatchKeyShortcutEvent(KeyEvent r3) {
        View r0 = getWindow().getDecorView();
        if (r0 == null) goto L9;
        if (KeyEventDispatcher.dispatchBeforeHierarchy(r0, r3) == false) goto L9;
        return true;
    L9:
        return super.dispatchKeyShortcutEvent(r3);
    }

    @Deprecated
    public <T extends ExtraData> T getExtraData(Class<T> r2) {
        return (T) this.mExtraDataMap.get(r2);
    }

    @Override // androidx.lifecycle.LifecycleOwner
    public Lifecycle getLifecycle() {
        return this.mLifecycleRegistry;
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle r1) {
        super.onCreate(r1);
        ReportFragment.injectIfNeededIn(this);
    }

    @Override // android.app.Activity
    protected void onSaveInstanceState(Bundle r3) {
        this.mLifecycleRegistry.markState(Lifecycle.State.CREATED);
        super.onSaveInstanceState(r3);
    }

    @Deprecated
    public void putExtraData(ExtraData r3) {
        this.mExtraDataMap.put((Class<? extends ExtraData>) r3.getClass(), r3);
    }

    protected final boolean shouldDumpInternalState(String[] r2) {
        return !shouldSkipDump(r2);
    }

    @Override // androidx.core.view.KeyEventDispatcher.Component
    public boolean superDispatchKeyEvent(KeyEvent r2) {
        return super.dispatchKeyEvent(r2);
    }
}
