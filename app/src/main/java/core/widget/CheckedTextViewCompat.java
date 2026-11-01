package androidx.core.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.CheckedTextView;
import java.lang.reflect.Field;

/* JADX INFO: loaded from: classes.dex */
public final class CheckedTextViewCompat {
    private static final String TAG = "CheckedTextViewCompat";

    private static class Api14Impl {
        private static Field sCheckMarkDrawableField;
        private static boolean sResolved;

        private Api14Impl() {
        }

        static Drawable getCheckMarkDrawable(CheckedTextView r4) {
            if (sResolved == true) goto L11;
            sCheckMarkDrawableField = CheckedTextView.class.getDeclaredField("mCheckMarkDrawable");     // Catch: NoSuchFieldException -> L7
            sCheckMarkDrawableField.setAccessible(true);     // Catch: NoSuchFieldException -> L7
        L9:
            sResolved = true;
        L7:
            e = move-exception;
            Log.i(CheckedTextViewCompat.TAG, "Failed to retrieve mCheckMarkDrawable field", e);
        L11:
            if (sCheckMarkDrawableField != null) goto L19;
        L16:
            return null;
        L19:
            return (Drawable) sCheckMarkDrawableField.get(r4);
        L14:
            e = move-exception;
            Log.i(CheckedTextViewCompat.TAG, "Failed to get check mark drawable via reflection", e);
            sCheckMarkDrawableField = null;
            goto L16
        }
    }

    private static class Api16Impl {
        private Api16Impl() {
        }

        static Drawable getCheckMarkDrawable(CheckedTextView r1) {
            return r1.getCheckMarkDrawable();
        }
    }

    private static class Api21Impl {
        private Api21Impl() {
        }

        static ColorStateList getCheckMarkTintList(CheckedTextView r1) {
            return r1.getCheckMarkTintList();
        }

        static PorterDuff.Mode getCheckMarkTintMode(CheckedTextView r1) {
            return r1.getCheckMarkTintMode();
        }

        static void setCheckMarkTintList(CheckedTextView r0, ColorStateList r1) {
            r0.setCheckMarkTintList(r1);
        }

        static void setCheckMarkTintMode(CheckedTextView r0, PorterDuff.Mode r1) {
            r0.setCheckMarkTintMode(r1);
        }
    }

    private CheckedTextViewCompat() {
    }

    public static Drawable getCheckMarkDrawable(CheckedTextView r1) {
        return Api16Impl.getCheckMarkDrawable(r1);
    }

    public static ColorStateList getCheckMarkTintList(CheckedTextView r1) {
        return Api21Impl.getCheckMarkTintList(r1);
    }

    public static PorterDuff.Mode getCheckMarkTintMode(CheckedTextView r1) {
        return Api21Impl.getCheckMarkTintMode(r1);
    }

    public static void setCheckMarkTintList(CheckedTextView r0, ColorStateList r1) {
        Api21Impl.setCheckMarkTintList(r0, r1);
    }

    public static void setCheckMarkTintMode(CheckedTextView r0, PorterDuff.Mode r1) {
        Api21Impl.setCheckMarkTintMode(r0, r1);
    }
}
