package androidx.core.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.widget.CompoundButton;
import java.lang.reflect.Field;

/* JADX INFO: loaded from: classes.dex */
public final class CompoundButtonCompat {
    private static final String TAG = "CompoundButtonCompat";
    private static Field sButtonDrawableField;
    private static boolean sButtonDrawableFieldFetched;

    static class Api21Impl {
        private Api21Impl() {
        }

        static ColorStateList getButtonTintList(CompoundButton r1) {
            return r1.getButtonTintList();
        }

        static PorterDuff.Mode getButtonTintMode(CompoundButton r1) {
            return r1.getButtonTintMode();
        }

        static void setButtonTintList(CompoundButton r0, ColorStateList r1) {
            r0.setButtonTintList(r1);
        }

        static void setButtonTintMode(CompoundButton r0, PorterDuff.Mode r1) {
            r0.setButtonTintMode(r1);
        }
    }

    static class Api23Impl {
        private Api23Impl() {
        }

        static Drawable getButtonDrawable(CompoundButton r1) {
            return r1.getButtonDrawable();
        }
    }

    private CompoundButtonCompat() {
    }

    public static Drawable getButtonDrawable(CompoundButton r1) {
        return Api23Impl.getButtonDrawable(r1);
    }

    public static ColorStateList getButtonTintList(CompoundButton r1) {
        return Api21Impl.getButtonTintList(r1);
    }

    public static PorterDuff.Mode getButtonTintMode(CompoundButton r1) {
        return Api21Impl.getButtonTintMode(r1);
    }

    public static void setButtonTintList(CompoundButton r0, ColorStateList r1) {
        Api21Impl.setButtonTintList(r0, r1);
    }

    public static void setButtonTintMode(CompoundButton r0, PorterDuff.Mode r1) {
        Api21Impl.setButtonTintMode(r0, r1);
    }
}
