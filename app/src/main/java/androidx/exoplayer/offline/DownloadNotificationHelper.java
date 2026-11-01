package androidx.media3.exoplayer.offline;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import androidx.core.app.NotificationCompat;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.C0250R;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class DownloadNotificationHelper {
    private static final int NULL_STRING_ID = 0;
    private final NotificationCompat.Builder notificationBuilder;

    private static final class Api31 {
        private Api31() {
        }

        public static void setForegroundServiceBehavior(NotificationCompat.Builder r1) {
            r1.setForegroundServiceBehavior(1);
        }
    }

    public DownloadNotificationHelper(Context r3, String r4) {
        this.notificationBuilder = new NotificationCompat.Builder(r3.getApplicationContext(), r4);
    }

    private Notification buildEndStateNotification(Context r12, int r13, PendingIntent r14, String r15, int r16) {
        return buildNotification(r12, r13, r14, r15, r16, 0, 0, false, false, true);
    }

    private Notification buildNotification(Context r4, int r5, PendingIntent r6, String r7, int r8, int r9, int r10, boolean r11, boolean r12, boolean r13) {
        this.notificationBuilder.setSmallIcon(r5);
        NotificationCompat.Builder r0 = this.notificationBuilder;
        NotificationCompat.BigTextStyle r1 = null;
        if (r8 != 0) goto L5;
        String r2 = null;
    L6:
        r0.setContentTitle(r2);
        this.notificationBuilder.setContentIntent(r6);
        NotificationCompat.Builder r3 = this.notificationBuilder;
        if (r7 == null) goto L10;
        r1 = new NotificationCompat.BigTextStyle().bigText(r7);
    L10:
        r3.setStyle(r1);
        this.notificationBuilder.setProgress(r9, r10, r11);
        this.notificationBuilder.setOngoing(r12);
        this.notificationBuilder.setShowWhen(r13);
        if (Util.SDK_INT < 31) goto L14;
        Api31.setForegroundServiceBehavior(this.notificationBuilder);
    L14:
        return this.notificationBuilder.build();
    L5:
        r2 = r4.getResources().getString(r8);
        goto L6
    }

    public Notification buildDownloadCompletedNotification(Context r7, int r8, PendingIntent r9, String r10) {
        return buildEndStateNotification(r7, r8, r9, r10, C0250R.string.exo_download_completed);
    }

    public Notification buildDownloadFailedNotification(Context r7, int r8, PendingIntent r9, String r10) {
        return buildEndStateNotification(r7, r8, r9, r10, C0250R.string.exo_download_failed);
    }

    public Notification buildProgressNotification(Context r23, int r24, PendingIntent r25, String r26, List<Download> r27, int r28) {
        float r0 = 0.0f;
        int r1 = 0;
        boolean r2 = true;
        boolean r3 = false;
        boolean r4 = false;
        boolean r5 = false;
        boolean r6 = false;
        int r7 = 0;
    L3:
        boolean r9 = false;
        if (r7 >= r27.size()) goto L18;
        Download r11 = r27.get(r7);
        switch(r11.state) {
            case 0: goto L16;
            case 2: goto L9;
            case 5: goto L8;
            case 7: goto L9;
            default: goto L17;
        };
    L8:
        r6 = true;
        goto L17
    L9:
        r4 = true;
        float r12 = r11.getPercentDownloaded();
        if (r12 == (-1.0f)) goto L13;
        r2 = false;
        r0 = r0 + r12;
    L13:
        if (r11.getBytesDownloaded() <= 0) goto L15;
        r9 = true;
    L15:
        r3 = r3 | r9;
        r1 = r1 + 1;
        goto L17
    L16:
        r5 = true;
    L17:
        r7 = r7 + 1;
        goto L3
    L18:
        boolean r8 = true;
        if (r4 == false) goto L21;
        int r16 = C0250R.string.exo_download_downloading;
    L34:
        if (r8 == true) goto L36;
        int r17 = 0;
        int r18 = 0;
        boolean r19 = false;
    L45:
        return buildNotification(r23, r24, r25, r26, r16, r17, r18, r19, true, false);
    L36:
        if (r4 == false) goto L42;
        int r13 = (int) (r0 / r1);
        if (r2 == false) goto L41;
        if (r3 == false) goto L41;
        r9 = true;
    L41:
        r17 = 100;
        r18 = r13;
        r19 = r9;
        goto L45
    L42:
        r17 = 100;
        r18 = 0;
        r19 = true;
        goto L45
    L21:
        if (r5 == false) goto L30;
        if (r28 == 0) goto L30;
        r8 = false;
        if ((r28 & 2) == 0) goto L27;
        r16 = C0250R.string.exo_download_paused_for_wifi;
        goto L34
    L27:
        if ((r28 & 1) == 0) goto L29;
        r16 = C0250R.string.exo_download_paused_for_network;
        goto L34
    L29:
        r16 = C0250R.string.exo_download_paused;
    L30:
        if (r6 == false) goto L32;
        r16 = C0250R.string.exo_download_removing;
        goto L34
    L32:
        r16 = 0;
        goto L34
    }
}
