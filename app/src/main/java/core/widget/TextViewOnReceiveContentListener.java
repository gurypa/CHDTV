package androidx.core.widget;

import android.content.ClipData;
import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.core.view.ContentInfoCompat;
import androidx.core.view.OnReceiveContentListener;

/* JADX INFO: loaded from: classes.dex */
public final class TextViewOnReceiveContentListener implements OnReceiveContentListener {
    private static final String LOG_TAG = "ReceiveContent";

    private static final class Api16Impl {
        private Api16Impl() {
        }

        static CharSequence coerce(Context r2, ClipData.Item r3, int r4) {
            if ((r4 & 1) == 0) goto L10;
            CharSequence r0 = r3.coerceToText(r2);
            if ((r0 instanceof Spanned) == false) goto L8;
            return r0.toString();
        L8:
            return r0;
        L10:
            return r3.coerceToStyledText(r2);
        }
    }

    private static final class ApiImpl {
        private ApiImpl() {
        }

        static CharSequence coerce(Context r2, ClipData.Item r3, int r4) {
            CharSequence r0 = r3.coerceToText(r2);
            if ((r4 & 1) != 0) goto L5;
            return r0;
        L5:
            if ((r0 instanceof Spanned) == true) goto L7;
            return r0;
        L7:
            return r0.toString();
        }
    }

    public TextViewOnReceiveContentListener() {
    }

    private static CharSequence coerceToText(Context r1, ClipData.Item r2, int r3) {
        return Api16Impl.coerce(r1, r2, r3);
    }

    private static void replaceSelection(Editable r5, CharSequence r6) {
        int r0 = Selection.getSelectionStart(r5);
        int r1 = Selection.getSelectionEnd(r5);
        int r2 = Math.max(0, Math.min(r0, r1));
        int r3 = Math.max(0, Math.max(r0, r1));
        Selection.setSelection(r5, r3);
        r5.replace(r2, r3, r6);
    }

    @Override // androidx.core.view.OnReceiveContentListener
    public ContentInfoCompat onReceiveContent(View r12, ContentInfoCompat r13) {
        if (Log.isLoggable(LOG_TAG, 3) == false) goto L6;
        Log.d(LOG_TAG, "onReceive: " + r13);
    L6:
        if (r13.getSource() != 2) goto L8;
        return r13;
    L8:
        ClipData r1 = r13.getClip();
        int r2 = r13.getFlags();
        TextView r3 = (TextView) r12;
        Editable r4 = (Editable) r3.getText();
        Context r5 = r3.getContext();
        boolean r6 = false;
        int r7 = 0;
    L10:
        if (r7 >= r1.getItemCount()) goto L17;
        CharSequence r8 = coerceToText(r5, r1.getItemAt(r7), r2);
        if (r8 == null) goto L16;
        if (r6 == true) goto L15;
        replaceSelection(r4, r8);
        r6 = true;
        goto L16
    L15:
        r4.insert(Selection.getSelectionEnd(r4), "\n");
        r4.insert(Selection.getSelectionEnd(r4), r8);
    L16:
        r7 = r7 + 1;
        goto L10
    L17:
        return null;
    }
}
