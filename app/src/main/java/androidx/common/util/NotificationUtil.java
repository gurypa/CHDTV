package androidx.media3.common.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* JADX INFO: loaded from: classes.dex */
public final class NotificationUtil {
    public static final int IMPORTANCE_DEFAULT = 3;
    public static final int IMPORTANCE_HIGH = 4;
    public static final int IMPORTANCE_LOW = 2;
    public static final int IMPORTANCE_MIN = 1;
    public static final int IMPORTANCE_NONE = 0;
    public static final int IMPORTANCE_UNSPECIFIED = -1000;

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface Importance {
    }

    private NotificationUtil() {
    }

    public static void createNotificationChannel(Context r3, String r4, int r5, int r6, int r7) {
        if (Util.SDK_INT < 26) goto L9;
        NotificationManager r0 = (NotificationManager) Assertions.checkNotNull((NotificationManager) r3.getSystemService("notification"));
        NotificationChannel r1 = new NotificationChannel(r4, r3.getString(r5), r7);
        if (r6 == 0) goto L7;
        r1.setDescription(r3.getString(r6));
    L7:
        r0.createNotificationChannel(r1);
        return;
    }

    public static void setNotification(Context r1, int r2, Notification r3) {
        NotificationManager r0 = (NotificationManager) Assertions.checkNotNull((NotificationManager) r1.getSystemService("notification"));
        if (r3 == null) goto L5;
        r0.notify(r2, r3);
        return;
    L5:
        r0.cancel(r2);
    }
}
