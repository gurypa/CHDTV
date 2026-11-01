package androidx.media3.exoplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Handler;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.Util;

/* JADX INFO: loaded from: classes.dex */
final class StreamVolumeManager {
    private static final String TAG = "StreamVolumeManager";
    private static final String VOLUME_CHANGED_ACTION = "android.media.VOLUME_CHANGED_ACTION";
    private final Context applicationContext;
    private final AudioManager audioManager;
    private final Handler eventHandler;
    private final Listener listener;
    private boolean muted;
    private VolumeChangeReceiver receiver;
    private int streamType;
    private int volume;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.StreamVolumeManager$1 */
    static /* synthetic */ class C02511 {
    }

    public interface Listener {
        void onStreamTypeChanged(int r1);

        void onStreamVolumeChanged(int r1, boolean r2);
    }

    private final class VolumeChangeReceiver extends BroadcastReceiver {
        final /* synthetic */ StreamVolumeManager this$0;

        private VolumeChangeReceiver(StreamVolumeManager r1) {
            this.this$0 = r1;
        }

        /* synthetic */ VolumeChangeReceiver(StreamVolumeManager r1, C02511 r2) {
            this(r1);
        }

        static /* synthetic */ void lambda$onReceive$0(StreamVolumeManager r0) {
            StreamVolumeManager.access$200(r0);
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context r4, Intent r5) {
            Handler r0 = StreamVolumeManager.access$100(this.this$0);
            final StreamVolumeManager r1 = this.this$0;
            r0.post(new RunnableC0252x8b49c079(r1));
        }
    }

    public StreamVolumeManager(Context r6, Handler r7, Listener r8, int r9) {
        this.applicationContext = r6.getApplicationContext();
        this.eventHandler = r7;
        this.listener = r8;
        this.audioManager = (AudioManager) Assertions.checkStateNotNull((AudioManager) this.applicationContext.getSystemService(MimeTypes.BASE_TYPE_AUDIO));
        this.streamType = r9;
        this.volume = getVolumeFromManager(this.audioManager, r9);
        this.muted = getMutedFromManager(this.audioManager, r9);
        VolumeChangeReceiver r0 = new VolumeChangeReceiver(this, null);
        IntentFilter r1 = new IntentFilter(VOLUME_CHANGED_ACTION);
        this.applicationContext.registerReceiver(r0, r1);     // Catch: RuntimeException -> L5
        this.receiver = r0;     // Catch: RuntimeException -> L5
        return;
    L5:
        e = move-exception;
        Log.m49w(TAG, "Error registering stream volume receiver", e);
    }

    static /* synthetic */ Handler access$100(StreamVolumeManager r1) {
        return r1.eventHandler;
    }

    static /* synthetic */ void access$200(StreamVolumeManager r0) {
        r0.updateVolumeAndNotifyIfChanged();
    }

    private static boolean getMutedFromManager(AudioManager r2, int r3) {
        if (Util.SDK_INT < 23) goto L7;
        return r2.isStreamMute(r3);
    L7:
        if (getVolumeFromManager(r2, r3) != 0) goto L9;
        return true;
    L9:
        return false;
    }

    private static int getVolumeFromManager(AudioManager r3, int r4) {
        return r3.getStreamVolume(r4);
    L4:
        e = move-exception;
        Log.m49w(TAG, "Could not retrieve stream volume for stream type " + r4, e);
        return r3.getStreamMaxVolume(r4);
    }

    private void updateVolumeAndNotifyIfChanged() {
        int r0 = getVolumeFromManager(this.audioManager, this.streamType);
        boolean r1 = getMutedFromManager(this.audioManager, this.streamType);
        if (this.volume == r0) goto L5;
    L6:
        this.volume = r0;
        this.muted = r1;
        this.listener.onStreamVolumeChanged(r0, r1);
        return;
    L5:
        if (this.muted != r1) goto L6;
    }

    public void decreaseVolume(int r4) {
        if (this.volume > getMinVolume()) goto L5;
        return;
    L5:
        this.audioManager.adjustStreamVolume(this.streamType, -1, r4);
        updateVolumeAndNotifyIfChanged();
    }

    public int getMaxVolume() {
        return this.audioManager.getStreamMaxVolume(this.streamType);
    }

    public int getMinVolume() {
        if (Util.SDK_INT >= 28) goto L7;
        return 0;
    L7:
        return this.audioManager.getStreamMinVolume(this.streamType);
    }

    public int getVolume() {
        return this.volume;
    }

    public void increaseVolume(int r4) {
        if (this.volume < getMaxVolume()) goto L5;
        return;
    L5:
        this.audioManager.adjustStreamVolume(this.streamType, 1, r4);
        updateVolumeAndNotifyIfChanged();
    }

    public boolean isMuted() {
        return this.muted;
    }

    public void release() {
        if (this.receiver == null) goto L12;
        this.applicationContext.unregisterReceiver(this.receiver);     // Catch: RuntimeException -> L6
    L8:
        this.receiver = null;
        return;
    L6:
        e = move-exception;
        Log.m49w(TAG, "Error unregistering stream volume receiver", e);
        goto L8
    }

    public void setMuted(boolean r4, int r5) {
        if (Util.SDK_INT < 23) goto L9;
        AudioManager r0 = this.audioManager;
        int r1 = this.streamType;
        if (r4 == false) goto L7;
        int r2 = -100;
    L8:
        r0.adjustStreamVolume(r1, r2, r5);
    L10:
        updateVolumeAndNotifyIfChanged();
        return;
    L7:
        r2 = 100;
        goto L8
    L9:
        this.audioManager.setStreamMute(this.streamType, r4);
        goto L10
    }

    public void setStreamType(int r2) {
        if (this.streamType != r2) goto L5;
        return;
    L5:
        this.streamType = r2;
        updateVolumeAndNotifyIfChanged();
        this.listener.onStreamTypeChanged(r2);
    }

    public void setVolume(int r3, int r4) {
        if (r3 >= getMinVolume()) goto L5;
        return;
    L5:
        if (r3 > getMaxVolume()) goto L10;
        this.audioManager.setStreamVolume(this.streamType, r3, r4);
        updateVolumeAndNotifyIfChanged();
        return;
    }
}
