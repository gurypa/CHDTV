package androidx.core.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.widget.ImageView;

/* JADX INFO: loaded from: classes.dex */
public class ImageViewCompat {

    static class Api21Impl {
        private Api21Impl() {
        }

        static ColorStateList getImageTintList(ImageView r1) {
            return r1.getImageTintList();
        }

        static PorterDuff.Mode getImageTintMode(ImageView r1) {
            return r1.getImageTintMode();
        }

        static void setImageTintList(ImageView r0, ColorStateList r1) {
            r0.setImageTintList(r1);
        }

        static void setImageTintMode(ImageView r0, PorterDuff.Mode r1) {
            r0.setImageTintMode(r1);
        }
    }

    private ImageViewCompat() {
    }

    public static ColorStateList getImageTintList(ImageView r1) {
        return Api21Impl.getImageTintList(r1);
    }

    public static PorterDuff.Mode getImageTintMode(ImageView r1) {
        return Api21Impl.getImageTintMode(r1);
    }

    public static void setImageTintList(ImageView r0, ColorStateList r1) {
        Api21Impl.setImageTintList(r0, r1);
    }

    public static void setImageTintMode(ImageView r0, PorterDuff.Mode r1) {
        Api21Impl.setImageTintMode(r0, r1);
    }
}
