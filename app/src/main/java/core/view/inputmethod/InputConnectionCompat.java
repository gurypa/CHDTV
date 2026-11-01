package androidx.core.view.inputmethod;

import android.content.ClipData;
import android.content.ClipDescription;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputContentInfo;
import androidx.core.util.ObjectsCompat;
import androidx.core.util.Preconditions;
import androidx.core.view.ContentInfoCompat;
import androidx.core.view.ViewCompat;

/* JADX INFO: loaded from: classes.dex */
public final class InputConnectionCompat {
    private static final String COMMIT_CONTENT_ACTION = "androidx.core.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT";
    private static final String COMMIT_CONTENT_CONTENT_URI_INTEROP_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_URI";
    private static final String COMMIT_CONTENT_CONTENT_URI_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_URI";
    private static final String COMMIT_CONTENT_DESCRIPTION_INTEROP_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION";
    private static final String COMMIT_CONTENT_DESCRIPTION_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION";
    private static final String COMMIT_CONTENT_FLAGS_INTEROP_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS";
    private static final String COMMIT_CONTENT_FLAGS_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS";
    private static final String COMMIT_CONTENT_INTEROP_ACTION = "android.support.v13.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT";
    private static final String COMMIT_CONTENT_LINK_URI_INTEROP_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI";
    private static final String COMMIT_CONTENT_LINK_URI_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI";
    private static final String COMMIT_CONTENT_OPTS_INTEROP_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_OPTS";
    private static final String COMMIT_CONTENT_OPTS_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_OPTS";
    private static final String COMMIT_CONTENT_RESULT_INTEROP_RECEIVER_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_RESULT_RECEIVER";
    private static final String COMMIT_CONTENT_RESULT_RECEIVER_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_RESULT_RECEIVER";
    private static final String EXTRA_INPUT_CONTENT_INFO = "androidx.core.view.extra.INPUT_CONTENT_INFO";
    public static final int INPUT_CONTENT_GRANT_READ_URI_PERMISSION = 1;
    private static final String LOG_TAG = "InputConnectionCompat";

    static class Api25Impl {
        private Api25Impl() {
        }

        static boolean commitContent(InputConnection r1, InputContentInfo r2, int r3, Bundle r4) {
            return r1.commitContent(r2, r3, r4);
        }
    }

    public interface OnCommitContentListener {
        boolean onCommitContent(InputContentInfoCompat r1, int r2, Bundle r3);
    }

    @Deprecated
    public InputConnectionCompat() {
    }

    public static boolean commitContent(InputConnection r5, EditorInfo r6, InputContentInfoCompat r7, int r8, Bundle r9) {
        if (Build.VERSION.SDK_INT < 25) goto L7;
        return Api25Impl.commitContent(r5, (InputContentInfo) r7.unwrap(), r8, r9);
    L7:
        switch(EditorInfoCompat.getProtocol(r6)) {
            case 2: goto L11;
            case 3: goto L10;
            case 4: goto L10;
            default: goto L8;
        };
    L8:
        return false;
    L10:
        boolean r1 = false;
    L12:
        Bundle r2 = new Bundle();
        if (r1 == false) goto L15;
        String r3 = COMMIT_CONTENT_CONTENT_URI_INTEROP_KEY;
    L16:
        r2.putParcelable(r3, r7.getContentUri());
        if (r1 == false) goto L19;
        String r4 = COMMIT_CONTENT_DESCRIPTION_INTEROP_KEY;
    L20:
        r2.putParcelable(r4, r7.getDescription());
        if (r1 == false) goto L23;
        String r10 = COMMIT_CONTENT_LINK_URI_INTEROP_KEY;
    L24:
        r2.putParcelable(r10, r7.getLinkUri());
        if (r1 == false) goto L27;
        String r11 = COMMIT_CONTENT_FLAGS_INTEROP_KEY;
    L28:
        r2.putInt(r11, r8);
        if (r1 == false) goto L31;
        String r12 = COMMIT_CONTENT_OPTS_INTEROP_KEY;
    L32:
        r2.putParcelable(r12, r9);
        if (r1 == false) goto L35;
        String r13 = COMMIT_CONTENT_INTEROP_ACTION;
    L37:
        return r5.performPrivateCommand(r13, r2);
    L35:
        r13 = COMMIT_CONTENT_ACTION;
        goto L37
    L31:
        r12 = COMMIT_CONTENT_OPTS_KEY;
        goto L32
    L27:
        r11 = COMMIT_CONTENT_FLAGS_KEY;
        goto L28
    L23:
        r10 = COMMIT_CONTENT_LINK_URI_KEY;
        goto L24
    L19:
        r4 = COMMIT_CONTENT_DESCRIPTION_KEY;
        goto L20
    L15:
        r3 = COMMIT_CONTENT_CONTENT_URI_KEY;
        goto L16
    L11:
        r1 = true;
        goto L12
    }

    private static OnCommitContentListener createOnCommitContentListenerUsingPerformReceiveContent(final View r1) {
        Preconditions.checkNotNull(r1);
        return new InputConnectionCompat$$ExternalSyntheticLambda0(r1);
    }

    public static InputConnection createWrapper(View r2, InputConnection r3, EditorInfo r4) {
        return createWrapper(r3, r4, createOnCommitContentListenerUsingPerformReceiveContent(r2));
    }

    @Deprecated
    public static InputConnection createWrapper(final InputConnection r4, EditorInfo r5, final OnCommitContentListener r6) {
        ObjectsCompat.requireNonNull(r4, "inputConnection must be non-null");
        ObjectsCompat.requireNonNull(r5, "editorInfo must be non-null");
        ObjectsCompat.requireNonNull(r6, "onCommitContentListener must be non-null");
        final boolean r2 = false;
        if (Build.VERSION.SDK_INT < 25) goto L7;
        return new C01291(r4, r2, r6);
    L7:
        if (EditorInfoCompat.getContentMimeTypes(r5).length != 0) goto L10;
        return r4;
    L10:
        return new C01302(r4, r2, r6);
    }

    static boolean handlePerformPrivateCommand(String r12, Bundle r13, OnCommitContentListener r14) {
        int r0 = 0;
        if (r13 != null) goto L6;
        return false;
    L6:
        if (TextUtils.equals(COMMIT_CONTENT_ACTION, r12) == false) goto L9;
        boolean r1 = false;
    L11:
        ResultReceiver r2 = null;
        boolean r3 = false;
        if (r1 == false) goto L14;
        String r5 = COMMIT_CONTENT_RESULT_INTEROP_RECEIVER_KEY;
    L50:
        r2 = (ResultReceiver) r13.getParcelable(r5);     // Catch: Throwable -> L45
        if (r1 == false) goto L18;
        String r6 = COMMIT_CONTENT_CONTENT_URI_INTEROP_KEY;
    L19:
        Uri r7 = (Uri) r13.getParcelable(r6);     // Catch: Throwable -> L45
        if (r1 == false) goto L22;
        String r8 = COMMIT_CONTENT_DESCRIPTION_INTEROP_KEY;
    L23:
        ClipDescription r9 = (ClipDescription) r13.getParcelable(r8);     // Catch: Throwable -> L45
        if (r1 == false) goto L26;
        String r10 = COMMIT_CONTENT_LINK_URI_INTEROP_KEY;
    L27:
        Uri r11 = (Uri) r13.getParcelable(r10);     // Catch: Throwable -> L45
        if (r1 == false) goto L30;
        String r15 = COMMIT_CONTENT_FLAGS_INTEROP_KEY;
    L31:
        int r16 = r13.getInt(r15);     // Catch: Throwable -> L45
        if (r1 == false) goto L34;
        String r17 = COMMIT_CONTENT_OPTS_INTEROP_KEY;
    L35:
        Bundle r18 = (Bundle) r13.getParcelable(r17);     // Catch: Throwable -> L45
        if (r7 == null) goto L40;
        if (r9 == null) goto L40;
        r3 = r14.onCommitContent(new InputContentInfoCompat(r7, r9, r11), r16, r18);     // Catch: Throwable -> L45
    L40:
        if (r2 == null) goto L44;
        if (r3 == false) goto L43;
        r0 = 1;
    L43:
        r2.send(r0, null);
    L44:
        return r3;
    L34:
        r17 = COMMIT_CONTENT_OPTS_KEY;
        goto L35
    L30:
        r15 = COMMIT_CONTENT_FLAGS_KEY;
        goto L31
    L26:
        r10 = COMMIT_CONTENT_LINK_URI_KEY;
        goto L27
    L22:
        r8 = COMMIT_CONTENT_DESCRIPTION_KEY;
        goto L23
    L18:
        r6 = COMMIT_CONTENT_CONTENT_URI_KEY;
    L45:
        th = move-exception;
        if (r2 == null) goto L48;
        r2.send(0, null);
    L48:
        throw th;
    L14:
        r5 = COMMIT_CONTENT_RESULT_RECEIVER_KEY;
        goto L50
    L9:
        if (TextUtils.equals(COMMIT_CONTENT_INTEROP_ACTION, r12) == false) goto L49;
        r1 = true;
        goto L11
    L49:
        return false;
    }

    static /* synthetic */ boolean lambda$createOnCommitContentListenerUsingPerformReceiveContent$0(View r6, InputContentInfoCompat r7, int r8, Bundle r9) {
        Bundle r0 = r9;
        if (Build.VERSION.SDK_INT < 25) goto L16;
        if ((r8 & 1) == 0) goto L16;
        r7.requestPermission();     // Catch: Exception -> L12
        InputContentInfo r1 = (InputContentInfo) r7.unwrap();
        if (r9 != null) goto L10;
        Bundle r2 = new Bundle();
    L11:
        r0 = r2;
        r0.putParcelable(EXTRA_INPUT_CONTENT_INFO, r1);
        goto L16
    L10:
        r2 = new Bundle(r9);
    L12:
        e = move-exception;
        Log.w(LOG_TAG, "Can't insert content from IME; requestPermission() failed", e);
        return false;
    L16:
        if (ViewCompat.performReceiveContent(r6, new ContentInfoCompat.Builder(new ClipData(r7.getDescription(), new ClipData.Item(r7.getContentUri())), 2).setLinkUri(r7.getLinkUri()).setExtras(r0).build()) != null) goto L21;
        return true;
    L21:
        return false;
    }
}
