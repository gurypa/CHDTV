package androidx.core.os;

import android.content.Context;
import android.os.Build;
import android.os.UserManager;

/* JADX INFO: loaded from: classes.dex */
public class UserManagerCompat {

    static class Api24Impl {
        private Api24Impl() {
        }

        static boolean isUserUnlocked(Context r1) {
            return ((UserManager) r1.getSystemService(UserManager.class)).isUserUnlocked();
        }
    }

    private UserManagerCompat() {
    }

    public static boolean isUserUnlocked(Context r2) {
        if (Build.VERSION.SDK_INT >= 24) goto L5;
        return true;
    L5:
        return Api24Impl.isUserUnlocked(r2);
    }
}
