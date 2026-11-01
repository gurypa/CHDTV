package androidx.core.app;

import android.app.AlarmManager;
import android.app.PendingIntent;

/* JADX INFO: loaded from: classes.dex */
public final class AlarmManagerCompat {

    static class Api19Impl {
        private Api19Impl() {
        }

        static void setExact(AlarmManager r0, int r1, long r2, PendingIntent r4) {
            r0.setExact(r1, r2, r4);
        }
    }

    static class Api21Impl {
        private Api21Impl() {
        }

        static AlarmManager.AlarmClockInfo createAlarmClockInfo(long r1, PendingIntent r3) {
            return new AlarmManager.AlarmClockInfo(r1, r3);
        }

        static void setAlarmClock(AlarmManager r1, Object r2, PendingIntent r3) {
            r1.setAlarmClock((AlarmManager.AlarmClockInfo) r2, r3);
        }
    }

    static class Api23Impl {
        private Api23Impl() {
        }

        static void setAndAllowWhileIdle(AlarmManager r0, int r1, long r2, PendingIntent r4) {
            r0.setAndAllowWhileIdle(r1, r2, r4);
        }

        static void setExactAndAllowWhileIdle(AlarmManager r0, int r1, long r2, PendingIntent r4) {
            r0.setExactAndAllowWhileIdle(r1, r2, r4);
        }
    }

    private AlarmManagerCompat() {
    }

    public static void setAlarmClock(AlarmManager r1, long r2, PendingIntent r4, PendingIntent r5) {
        Api21Impl.setAlarmClock(r1, Api21Impl.createAlarmClockInfo(r2, r4), r5);
    }

    public static void setAndAllowWhileIdle(AlarmManager r0, int r1, long r2, PendingIntent r4) {
        Api23Impl.setAndAllowWhileIdle(r0, r1, r2, r4);
    }

    public static void setExact(AlarmManager r0, int r1, long r2, PendingIntent r4) {
        Api19Impl.setExact(r0, r1, r2, r4);
    }

    public static void setExactAndAllowWhileIdle(AlarmManager r0, int r1, long r2, PendingIntent r4) {
        Api23Impl.setExactAndAllowWhileIdle(r0, r1, r2, r4);
    }
}
