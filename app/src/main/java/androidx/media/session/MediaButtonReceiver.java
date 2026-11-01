package androidx.media.session;

import android.app.ForegroundServiceStartNotAllowedException;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.KeyEvent;
import androidx.core.content.ContextCompat;
import androidx.media.MediaBrowserServiceCompat;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class MediaButtonReceiver extends BroadcastReceiver {
    private static final String TAG = "MediaButtonReceiver";

    private static final class Api31 {
        private Api31() {
        }

        public static ForegroundServiceStartNotAllowedException castToForegroundServiceStartNotAllowedException(IllegalStateException r1) {
            return (ForegroundServiceStartNotAllowedException) r1;
        }

        public static boolean instanceOfForegroundServiceStartNotAllowedException(IllegalStateException r1) {
            return r1 instanceof ForegroundServiceStartNotAllowedException;
        }
    }

    private static class MediaButtonConnectionCallback extends MediaBrowserCompat.ConnectionCallback {
        private final Context mContext;
        private final Intent mIntent;
        private MediaBrowserCompat mMediaBrowser;
        private final BroadcastReceiver.PendingResult mPendingResult;

        MediaButtonConnectionCallback(Context r1, Intent r2, BroadcastReceiver.PendingResult r3) {
            this.mContext = r1;
            this.mIntent = r2;
            this.mPendingResult = r3;
        }

        private void finish() {
            this.mMediaBrowser.disconnect();
            this.mPendingResult.finish();
        }

        @Override // android.support.v4.media.MediaBrowserCompat.ConnectionCallback
        public void onConnected() {
            new MediaControllerCompat(this.mContext, this.mMediaBrowser.getSessionToken()).dispatchMediaButtonEvent((KeyEvent) this.mIntent.getParcelableExtra("android.intent.extra.KEY_EVENT"));
            finish();
        }

        @Override // android.support.v4.media.MediaBrowserCompat.ConnectionCallback
        public void onConnectionFailed() {
            finish();
        }

        @Override // android.support.v4.media.MediaBrowserCompat.ConnectionCallback
        public void onConnectionSuspended() {
            finish();
        }

        void setMediaBrowser(MediaBrowserCompat r1) {
            this.mMediaBrowser = r1;
        }
    }

    public MediaButtonReceiver() {
    }

    public static PendingIntent buildMediaButtonPendingIntent(Context r3, long r4) {
        ComponentName r0 = getMediaButtonReceiverComponent(r3);
        if (r0 != null) goto L7;
        Log.w(TAG, "A unique media button receiver could not be found in the given context, so couldn't build a pending intent.");
        return null;
    L7:
        return buildMediaButtonPendingIntent(r3, r0, r4);
    }

    public static PendingIntent buildMediaButtonPendingIntent(Context r5, ComponentName r6, long r7) {
        if (r6 != null) goto L6;
        Log.w(TAG, "The component name of media button receiver should be provided.");
        return null;
    L6:
        int r2 = PlaybackStateCompat.toKeyCode(r7);
        if (r2 != 0) goto L10;
        Log.w(TAG, "Cannot build a media button pending intent with the given action: " + r7);
        return null;
    L10:
        Intent r0 = new Intent("android.intent.action.MEDIA_BUTTON");
        r0.setComponent(r6);
        int r3 = 0;
        r0.putExtra("android.intent.extra.KEY_EVENT", new KeyEvent(0, r2));
        r0.addFlags(268435456);
        if (Build.VERSION.SDK_INT < 31) goto L14;
        r3 = 33554432;
    L14:
        return PendingIntent.getBroadcast(r5, r2, r0, r3);
    }

    public static ComponentName getMediaButtonReceiverComponent(Context r7) {
        Intent r0 = new Intent("android.intent.action.MEDIA_BUTTON");
        r0.setPackage(r7.getPackageName());
        List<ResolveInfo> r3 = r7.getPackageManager().queryBroadcastReceivers(r0, 0);
        if (r3.size() != 1) goto L7;
        ResolveInfo r2 = r3.get(0);
        return new ComponentName(r2.activityInfo.packageName, r2.activityInfo.name);
    L7:
        if (r3.size() <= 1) goto L11;
        Log.w(TAG, "More than one BroadcastReceiver that handles android.intent.action.MEDIA_BUTTON was found, returning null.");
        return null;
    L11:
        return null;
    }

    private static ComponentName getServiceComponentByAction(Context r7, String r8) {
        PackageManager r0 = r7.getPackageManager();
        Intent r1 = new Intent(r8);
        r1.setPackage(r7.getPackageName());
        List<ResolveInfo> r3 = r0.queryIntentServices(r1, 0);
        if (r3.size() != 1) goto L7;
        ResolveInfo r2 = r3.get(0);
        return new ComponentName(r2.serviceInfo.packageName, r2.serviceInfo.name);
    L7:
        if (r3.isEmpty() == false) goto L11;
        return null;
    L11:
        throw new IllegalStateException("Expected 1 service that handles " + r8 + ", found " + r3.size());
    }

    public static KeyEvent handleIntent(MediaSessionCompat r2, Intent r3) {
        if (r2 == null) goto L11;
        if (r3 != null) goto L5;
        return null;
    L5:
        if ("android.intent.action.MEDIA_BUTTON".equals(r3.getAction()) == true) goto L7;
        return null;
    L7:
        if (r3.hasExtra("android.intent.extra.KEY_EVENT") == false) goto L15;
        KeyEvent r0 = (KeyEvent) r3.getParcelableExtra("android.intent.extra.KEY_EVENT");
        r2.getController().dispatchMediaButtonEvent(r0);
        return r0;
    L15:
        return null;
    L11:
        return null;
    }

    protected void onForegroundServiceStartNotAllowedException(Intent r3, ForegroundServiceStartNotAllowedException r4) {
        Log.e(TAG, "caught exception when trying to start a foreground service from the background: " + r4.getMessage());
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context r8, Intent r9) {
        if (r9 != null) goto L4;
    L27:
        Log.d(TAG, "Ignore unsupported intent: " + r9);
        return;
    L4:
        if ("android.intent.action.MEDIA_BUTTON".equals(r9.getAction()) == false) goto L27;
        if (r9.hasExtra("android.intent.extra.KEY_EVENT") == false) goto L27;
        ComponentName r0 = getServiceComponentByAction(r8, "android.intent.action.MEDIA_BUTTON");
        if (r0 == null) goto L21;
        r9.setComponent(r0);
        ContextCompat.startForegroundService(r8, r9);     // Catch: IllegalStateException -> L13
        return;
    L13:
        e = move-exception;
        if (Build.VERSION.SDK_INT >= 31) goto L17;
    L20:
        throw e;
    L17:
        if (Api31.instanceOfForegroundServiceStartNotAllowedException(e) == false) goto L20;
        onForegroundServiceStartNotAllowedException(r9, Api31.castToForegroundServiceStartNotAllowedException(e));
        return;
    L21:
        ComponentName r1 = getServiceComponentByAction(r8, MediaBrowserServiceCompat.SERVICE_INTERFACE);
        if (r1 == null) goto L26;
        BroadcastReceiver.PendingResult r2 = goAsync();
        Context r3 = r8.getApplicationContext();
        MediaButtonConnectionCallback r4 = new MediaButtonConnectionCallback(r3, r9, r2);
        MediaBrowserCompat r5 = new MediaBrowserCompat(r3, r1, r4, null);
        r4.setMediaBrowser(r5);
        r5.connect();
        return;
    L26:
        throw new IllegalStateException("Could not find any Service that handles android.intent.action.MEDIA_BUTTON or implements a media browser service.");
    }
}
