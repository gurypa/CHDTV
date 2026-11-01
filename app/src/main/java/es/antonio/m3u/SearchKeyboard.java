package es.antonio.m3u;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import java.util.function.Supplier;

/* JADX INFO: loaded from: classes3.dex */
final class SearchKeyboard {
    SearchKeyboard() {
    }

    static void bind(final Activity r2, final EditText r3, final Supplier<View> r4, final Runnable r5) {
        r3.setImeOptions(268435459);
        r2.getWindow().setSoftInputMode(19);
        r3.setOnEditorActionListener(new SearchKeyboard$$ExternalSyntheticLambda0(r5, r2, r3, r4));
    }

    static void hide(Activity r3, EditText r4, View r5) {
        if (r4 != null) goto L4;
        return;
    L4:
        InputMethodManager r0 = (InputMethodManager) r3.getSystemService("input_method");
        if (r0 == null) goto L7;
        r0.hideSoftInputFromWindow(r4.getWindowToken(), 0);
    L7:
        r4.clearFocus();
        r4.setVisibility(8);
        if (r5 == null) goto L11;
        r5.requestFocus();
        return;
    }

    static /* synthetic */ boolean lambda$bind$0(Runnable r4, Activity r5, EditText r6, Supplier r7, TextView r8, int r9, KeyEvent r10) {
        if (r10 != null) goto L5;
    L11:
        boolean r2 = false;
    L12:
        if (r2 == true) goto L14;
    L16:
        if (r2 == false) goto L18;
    L25:
        r4.run();
        hide(r5, r6, (View) r7.get());
        return true;
    L18:
        if (r9 == 3) goto L25;
        if (r9 == 6) goto L25;
        if (r9 == 2) goto L25;
        return false;
    L14:
        if (r10.getAction() == 1) goto L16;
        return true;
    L5:
        if (r10.getKeyCode() != 66) goto L7;
    L10:
        r2 = true;
        goto L12
    L7:
        if (r10.getKeyCode() == 160) goto L10;
        if (r10.getKeyCode() != 23) goto L11;
        goto L10
    }
}
