package androidx.core.view.inputmethod;

import android.os.Build;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import androidx.core.util.Preconditions;

/* JADX INFO: loaded from: classes.dex */
public final class EditorInfoCompat {
    private static final String CONTENT_MIME_TYPES_INTEROP_KEY = "android.support.v13.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES";
    private static final String CONTENT_MIME_TYPES_KEY = "androidx.core.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES";
    private static final String CONTENT_SELECTION_END_KEY = "androidx.core.view.inputmethod.EditorInfoCompat.CONTENT_SELECTION_END";
    private static final String CONTENT_SELECTION_HEAD_KEY = "androidx.core.view.inputmethod.EditorInfoCompat.CONTENT_SELECTION_HEAD";
    private static final String CONTENT_SURROUNDING_TEXT_KEY = "androidx.core.view.inputmethod.EditorInfoCompat.CONTENT_SURROUNDING_TEXT";
    private static final String[] EMPTY_STRING_ARRAY = null;
    public static final int IME_FLAG_FORCE_ASCII = Integer.MIN_VALUE;
    public static final int IME_FLAG_NO_PERSONALIZED_LEARNING = 16777216;
    static final int MAX_INITIAL_SELECTION_LENGTH = 1024;
    static final int MEMORY_EFFICIENT_TEXT_LENGTH = 2048;

    private static class Api30Impl {
        private Api30Impl() {
        }

        static CharSequence getInitialSelectedText(EditorInfo r1, int r2) {
            return r1.getInitialSelectedText(r2);
        }

        static CharSequence getInitialTextAfterCursor(EditorInfo r1, int r2, int r3) {
            return r1.getInitialTextAfterCursor(r2, r3);
        }

        static CharSequence getInitialTextBeforeCursor(EditorInfo r1, int r2, int r3) {
            return r1.getInitialTextBeforeCursor(r2, r3);
        }

        static void setInitialSurroundingSubText(EditorInfo r0, CharSequence r1, int r2) {
            r0.setInitialSurroundingSubText(r1, r2);
        }
    }

    static {
        EMPTY_STRING_ARRAY = new String[0];
    }

    @Deprecated
    public EditorInfoCompat() {
    }

    public static String[] getContentMimeTypes(EditorInfo r3) {
        if (Build.VERSION.SDK_INT < 25) goto L10;
        String[] r0 = r3.contentMimeTypes;
        if (r0 == null) goto L8;
        return r0;
    L8:
        return EMPTY_STRING_ARRAY;
    L10:
        if (r3.extras == null) goto L12;
        String[] r1 = r3.extras.getStringArray(CONTENT_MIME_TYPES_KEY);
        if (r1 != null) goto L16;
        r1 = r3.extras.getStringArray(CONTENT_MIME_TYPES_INTEROP_KEY);
    L16:
        if (r1 == null) goto L19;
        return r1;
    L19:
        return EMPTY_STRING_ARRAY;
    L12:
        return EMPTY_STRING_ARRAY;
    }

    public static CharSequence getInitialSelectedText(EditorInfo r8, int r9) {
        if (Build.VERSION.SDK_INT < 30) goto L7;
        return Api30Impl.getInitialSelectedText(r8, r9);
    L7:
        if (r8.extras != null) goto L9;
        return null;
    L9:
        int r0 = Math.min(r8.initialSelStart, r8.initialSelEnd);
        int r2 = Math.max(r8.initialSelStart, r8.initialSelEnd);
        int r3 = r8.extras.getInt(CONTENT_SELECTION_HEAD_KEY);
        int r4 = r8.extras.getInt(CONTENT_SELECTION_END_KEY);
        int r5 = r2 - r0;
        if (r8.initialSelStart >= 0) goto L12;
    L24:
        return null;
    L12:
        if (r8.initialSelEnd < 0) goto L24;
        if ((r4 - r3) != r5) goto L24;
        CharSequence r6 = r8.extras.getCharSequence(CONTENT_SURROUNDING_TEXT_KEY);
        if (r6 != null) goto L20;
        return null;
    L20:
        if ((r9 & 1) == 0) goto L23;
        return r6.subSequence(r3, r4);
    L23:
        return TextUtils.substring(r6, r3, r4);
    }

    public static CharSequence getInitialTextAfterCursor(EditorInfo r4, int r5, int r6) {
        if (Build.VERSION.SDK_INT < 30) goto L7;
        return Api30Impl.getInitialTextAfterCursor(r4, r5, r6);
    L7:
        if (r4.extras != null) goto L9;
        return null;
    L9:
        CharSequence r0 = r4.extras.getCharSequence(CONTENT_SURROUNDING_TEXT_KEY);
        if (r0 != null) goto L12;
        return null;
    L12:
        int r1 = r4.extras.getInt(CONTENT_SELECTION_END_KEY);
        int r2 = Math.min(r5, r0.length() - r1);
        if ((r6 & 1) == 0) goto L16;
        return r0.subSequence(r1, r1 + r2);
    L16:
        return TextUtils.substring(r0, r1, r1 + r2);
    }

    public static CharSequence getInitialTextBeforeCursor(EditorInfo r4, int r5, int r6) {
        if (Build.VERSION.SDK_INT < 30) goto L7;
        return Api30Impl.getInitialTextBeforeCursor(r4, r5, r6);
    L7:
        if (r4.extras != null) goto L9;
        return null;
    L9:
        CharSequence r0 = r4.extras.getCharSequence(CONTENT_SURROUNDING_TEXT_KEY);
        if (r0 != null) goto L12;
        return null;
    L12:
        int r1 = r4.extras.getInt(CONTENT_SELECTION_HEAD_KEY);
        int r2 = Math.min(r5, r1);
        if ((r6 & 1) == 0) goto L16;
        return r0.subSequence(r1 - r2, r1);
    L16:
        return TextUtils.substring(r0, r1 - r2, r1);
    }

    static int getProtocol(EditorInfo r4) {
        if (Build.VERSION.SDK_INT < 25) goto L7;
        return 1;
    L7:
        if (r4.extras != null) goto L9;
        return 0;
    L9:
        boolean r0 = r4.extras.containsKey(CONTENT_MIME_TYPES_KEY);
        boolean r2 = r4.extras.containsKey(CONTENT_MIME_TYPES_INTEROP_KEY);
        if (r0 == false) goto L14;
        if (r2 == false) goto L14;
        return 4;
    L14:
        if (r0 == false) goto L17;
        return 3;
    L17:
        if (r2 == false) goto L20;
        return 2;
    L20:
        return 0;
    }

    private static boolean isCutOnSurrogate(CharSequence r1, int r2, int r3) {
        switch(r3) {
            case 0: goto L8;
            case 1: goto L6;
            default: goto L3;
        };
    L3:
        return false;
    L6:
        return Character.isHighSurrogate(r1.charAt(r2));
    L8:
        return Character.isLowSurrogate(r1.charAt(r2));
    }

    private static boolean isPasswordInputType(int r2) {
        int r0 = r2 & 4095;
        if (r0 != 129) goto L5;
    L10:
        return true;
    L5:
        if (r0 == 225) goto L10;
        if (r0 == 18) goto L10;
        return false;
    }

    public static void setContentMimeTypes(EditorInfo r2, String[] r3) {
        if (Build.VERSION.SDK_INT < 25) goto L6;
        r2.contentMimeTypes = r3;
        return;
    L6:
        if (r2.extras != null) goto L8;
        r2.extras = new Bundle();
    L8:
        r2.extras.putStringArray(CONTENT_MIME_TYPES_KEY, r3);
        r2.extras.putStringArray(CONTENT_MIME_TYPES_INTEROP_KEY, r3);
    }

    public static void setInitialSurroundingSubText(EditorInfo r6, CharSequence r7, int r8) {
        Preconditions.checkNotNull(r7);
        if (Build.VERSION.SDK_INT < 30) goto L7;
        Api30Impl.setInitialSurroundingSubText(r6, r7, r8);
        return;
    L7:
        if (r6.initialSelStart <= r6.initialSelEnd) goto L9;
        int r0 = r6.initialSelEnd - r8;
    L11:
        if (r6.initialSelStart <= r6.initialSelEnd) goto L13;
        int r1 = r6.initialSelStart - r8;
    L14:
        int r2 = r7.length();
        if (r8 < 0) goto L29;
        if (r0 < 0) goto L29;
        if (r1 > r2) goto L29;
        if (isPasswordInputType(r6.inputType) == false) goto L24;
        setSurroundingText(r6, null, 0, 0);
        return;
    L24:
        if (r2 > 2048) goto L27;
        setSurroundingText(r6, r7, r0, r1);
        return;
    L27:
        trimLongSurroundingText(r6, r7, r0, r1);
        return;
    L29:
        setSurroundingText(r6, null, 0, 0);
        return;
    L13:
        r1 = r6.initialSelEnd - r8;
        goto L14
    L9:
        r0 = r6.initialSelStart - r8;
        goto L11
    }

    public static void setInitialSurroundingText(EditorInfo r3, CharSequence r4) {
        if (Build.VERSION.SDK_INT < 30) goto L5;
        Api30Impl.setInitialSurroundingSubText(r3, r4, 0);
        return;
    L5:
        setInitialSurroundingSubText(r3, r4, 0);
    }

    private static void setSurroundingText(EditorInfo r3, CharSequence r4, int r5, int r6) {
        if (r3.extras != null) goto L5;
        r3.extras = new Bundle();
    L5:
        if (r4 == null) goto L7;
        SpannableStringBuilder r0 = new SpannableStringBuilder(r4);
    L8:
        r3.extras.putCharSequence(CONTENT_SURROUNDING_TEXT_KEY, r0);
        r3.extras.putInt(CONTENT_SELECTION_HEAD_KEY, r5);
        r3.extras.putInt(CONTENT_SELECTION_END_KEY, r6);
        return;
    L7:
        r0 = null;
        goto L8
    }

    private static void trimLongSurroundingText(EditorInfo r17, CharSequence r18, int r19, int r20) {
        int r3 = r20 - r19;
        if (r3 <= 1024) goto L5;
        int r4 = 0;
    L6:
        int r7 = 2048 - r4;
        int r9 = Math.min(r18.length() - r20, r7 - Math.min(r19, (int) (((double) r7) * 0.8d)));
        int r10 = Math.min(r19, r7 - r9);
        int r11 = r19 - r10;
        if (isCutOnSurrogate(r18, r19 - r10, 0) == false) goto L10;
        r11 = r11 + 1;
        r10 = r10 - 1;
    L10:
        if (isCutOnSurrogate(r18, (r20 + r9) - 1, 1) == false) goto L12;
        r9 = r9 - 1;
    L12:
        int r12 = (r10 + r4) + r9;
        if (r4 == r3) goto L15;
        CharSequence r5 = TextUtils.concat(new CharSequence[]{r18.subSequence(r11, r11 + r10), r18.subSequence(r20, r20 + r9)});
    L16:
        int r13 = 0 + r10;
        setSurroundingText(r17, r5, r13, r13 + r4);
        return;
    L15:
        r5 = r18.subSequence(r11, r11 + r12);
        goto L16
    L5:
        r4 = r3;
        goto L6
    }
}
