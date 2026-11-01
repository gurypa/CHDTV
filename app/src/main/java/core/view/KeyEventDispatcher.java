package androidx.core.view;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
public class KeyEventDispatcher {
    private static boolean sActionBarFieldsFetched;
    private static Method sActionBarOnMenuKeyMethod;
    private static boolean sDialogFieldsFetched;
    private static Field sDialogKeyListenerField;

    public interface Component {
        boolean superDispatchKeyEvent(KeyEvent r1);
    }

    static {
        sActionBarFieldsFetched = false;
        sActionBarOnMenuKeyMethod = null;
        sDialogFieldsFetched = false;
        sDialogKeyListenerField = null;
    }

    private KeyEventDispatcher() {
    }

    private static boolean actionBarOnMenuKeyEventPre28(ActionBar r6, KeyEvent r7) {
        if (sActionBarFieldsFetched == true) goto L9;
        sActionBarOnMenuKeyMethod = r6.getClass().getMethod("onMenuKeyEvent", new Class[]{KeyEvent.class});     // Catch: NoSuchMethodException -> L6
    L7:
        sActionBarFieldsFetched = true;
    L9:
        if (sActionBarOnMenuKeyMethod != null) goto L21;
    L18:
        return false;
    L21:
        Object r0 = sActionBarOnMenuKeyMethod.invoke(r6, new Object[]{r7});     // Catch: InvocationTargetException -> L15 IllegalAccessException -> L17
        if (r0 != null) goto L13;
        return false;
    L13:
        return ((Boolean) r0).booleanValue();
        goto L18
    }

    private static boolean activitySuperDispatchKeyEventPre28(Activity r5, KeyEvent r6) {
        r5.onUserInteraction();
        Window r0 = r5.getWindow();
        if (r0.hasFeature(8) == false) goto L11;
        ActionBar r1 = r5.getActionBar();
        if (r6.getKeyCode() != 82) goto L11;
        if (r1 == null) goto L11;
        if (actionBarOnMenuKeyEventPre28(r1, r6) == false) goto L11;
        return true;
    L11:
        if (r0.superDispatchKeyEvent(r6) == false) goto L13;
        return true;
    L13:
        View r2 = r0.getDecorView();
        if (ViewCompat.dispatchUnhandledKeyEventBeforeCallback(r2, r6) == false) goto L16;
        return true;
    L16:
        if (r2 == null) goto L18;
        KeyEvent.DispatcherState r3 = r2.getKeyDispatcherState();
    L20:
        return r6.dispatch(r5, r3, r5);
    L18:
        r3 = null;
        goto L20
    }

    private static boolean dialogSuperDispatchKeyEventPre28(Dialog r5, KeyEvent r6) {
        DialogInterface.OnKeyListener r0 = getDialogKeyListenerPre28(r5);
        if (r0 != null) goto L5;
    L7:
        Window r2 = r5.getWindow();
        if (r2.superDispatchKeyEvent(r6) == false) goto L10;
        return true;
    L10:
        View r3 = r2.getDecorView();
        if (ViewCompat.dispatchUnhandledKeyEventBeforeCallback(r3, r6) == false) goto L13;
        return true;
    L13:
        if (r3 == null) goto L15;
        KeyEvent.DispatcherState r1 = r3.getKeyDispatcherState();
    L17:
        return r6.dispatch(r5, r1, r5);
    L15:
        r1 = null;
        goto L17
    L5:
        if (r0.onKey(r5, r6.getKeyCode(), r6) == false) goto L7;
        return true;
    }

    public static boolean dispatchBeforeHierarchy(View r1, KeyEvent r2) {
        return ViewCompat.dispatchUnhandledKeyEventBeforeHierarchy(r1, r2);
    }

    public static boolean dispatchKeyEvent(Component r3, View r4, Window.Callback r5, KeyEvent r6) {
        if (r3 != null) goto L6;
        return false;
    L6:
        if (Build.VERSION.SDK_INT < 28) goto L10;
        return r3.superDispatchKeyEvent(r6);
    L10:
        if ((r5 instanceof Activity) == false) goto L14;
        return activitySuperDispatchKeyEventPre28((Activity) r5, r6);
    L14:
        if ((r5 instanceof Dialog) == true) goto L16;
        if (r4 == null) goto L21;
        if (ViewCompat.dispatchUnhandledKeyEventBeforeCallback(r4, r6) == false) goto L21;
    L22:
        return true;
    L21:
        if (r3.superDispatchKeyEvent(r6) == true) goto L22;
        return false;
    L16:
        return dialogSuperDispatchKeyEventPre28((Dialog) r5, r6);
    }

    private static DialogInterface.OnKeyListener getDialogKeyListenerPre28(Dialog r3) {
        if (sDialogFieldsFetched == true) goto L10;
        sDialogKeyListenerField = Dialog.class.getDeclaredField("mOnKeyListener");     // Catch: NoSuchFieldException -> L7
        sDialogKeyListenerField.setAccessible(true);     // Catch: NoSuchFieldException -> L7
    L8:
        sDialogFieldsFetched = true;
    L10:
        if (sDialogKeyListenerField == null) goto L20;
        return (DialogInterface.OnKeyListener) sDialogKeyListenerField.get(r3);
    L14:
        return null;
    L20:
        return null;
    }
}
