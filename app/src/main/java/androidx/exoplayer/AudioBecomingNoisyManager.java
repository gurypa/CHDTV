package androidx.media3.exoplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;

/* JADX INFO: loaded from: classes.dex */
final class AudioBecomingNoisyManager {
    private final Context context;
    private final AudioBecomingNoisyReceiver receiver;
    private boolean receiverRegistered;

    private final class AudioBecomingNoisyReceiver extends BroadcastReceiver implements Runnable {
        private final Handler eventHandler;
        private final EventListener listener;
        final /* synthetic */ AudioBecomingNoisyManager this$0;

        public AudioBecomingNoisyReceiver(AudioBecomingNoisyManager r1, Handler r2, EventListener r3) {
            this.this$0 = r1;
            this.eventHandler = r2;
            this.listener = r3;
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context r3, Intent r4) {
            if ("android.media.AUDIO_BECOMING_NOISY".equals(r4.getAction()) == false) goto L6;
            this.eventHandler.post(this);
            return;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (AudioBecomingNoisyManager.access$000(this.this$0) == false) goto L6;
            this.listener.onAudioBecomingNoisy();
            return;
        }
    }

    public interface EventListener {
        void onAudioBecomingNoisy();
    }

    public AudioBecomingNoisyManager(Context r2, Handler r3, EventListener r4) {
        this.context = r2.getApplicationContext();
        this.receiver = new AudioBecomingNoisyReceiver(this, r3, r4);
    }

    static /* synthetic */ boolean access$000(AudioBecomingNoisyManager r1) {
        return r1.receiverRegistered;
    }

    public void setEnabled(boolean r5) {
        if (r5 == true) goto L4;
    L6:
        if (r5 == false) goto L8;
        return;
    L8:
        if (this.receiverRegistered == false) goto L13;
        this.context.unregisterReceiver(this.receiver);
        this.receiverRegistered = false;
        return;
    L13:
        return;
    L4:
        if (this.receiverRegistered == true) goto L6;
        this.context.registerReceiver(this.receiver, new IntentFilter("android.media.AUDIO_BECOMING_NOISY"));
        this.receiverRegistered = true;
    }
}
