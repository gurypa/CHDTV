package androidx.media3.exoplayer.audio;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.media.AudioDeviceCallback;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import androidx.media3.common.AudioAttributes;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;

/* JADX INFO: loaded from: classes.dex */
public final class AudioCapabilitiesReceiver {
    private AudioAttributes audioAttributes;
    private AudioCapabilities audioCapabilities;
    private final AudioDeviceCallbackV23 audioDeviceCallback;
    private final Context context;
    private final ExternalSurroundSoundSettingObserver externalSurroundSoundSettingObserver;
    private final Handler handler;
    private final BroadcastReceiver hdmiAudioPlugBroadcastReceiver;
    private final Listener listener;
    private boolean registered;
    private AudioDeviceInfoApi23 routedDevice;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.audio.AudioCapabilitiesReceiver$1 */
    static /* synthetic */ class C02541 {
    }

    private static final class Api23 {
        private Api23() {
        }

        public static void registerAudioDeviceCallback(Context r2, AudioDeviceCallback r3, Handler r4) {
            ((AudioManager) Assertions.checkNotNull((AudioManager) r2.getSystemService(MimeTypes.BASE_TYPE_AUDIO))).registerAudioDeviceCallback(r3, r4);
        }

        public static void unregisterAudioDeviceCallback(Context r2, AudioDeviceCallback r3) {
            ((AudioManager) Assertions.checkNotNull((AudioManager) r2.getSystemService(MimeTypes.BASE_TYPE_AUDIO))).unregisterAudioDeviceCallback(r3);
        }
    }

    private final class AudioDeviceCallbackV23 extends AudioDeviceCallback {
        final /* synthetic */ AudioCapabilitiesReceiver this$0;

        private AudioDeviceCallbackV23(AudioCapabilitiesReceiver r1) {
            this.this$0 = r1;
        }

        /* synthetic */ AudioDeviceCallbackV23(AudioCapabilitiesReceiver r1, C02541 r2) {
            this(r1);
        }

        @Override // android.media.AudioDeviceCallback
        public void onAudioDevicesAdded(AudioDeviceInfo[] r5) {
            AudioCapabilitiesReceiver.access$400(this.this$0, AudioCapabilities.getCapabilitiesInternal(AudioCapabilitiesReceiver.access$500(this.this$0), AudioCapabilitiesReceiver.access$200(this.this$0), AudioCapabilitiesReceiver.access$300(this.this$0)));
        }

        @Override // android.media.AudioDeviceCallback
        public void onAudioDevicesRemoved(AudioDeviceInfo[] r5) {
            if (Util.contains(r5, AudioCapabilitiesReceiver.access$300(this.this$0)) == false) goto L5;
            AudioCapabilitiesReceiver.access$302(this.this$0, null);
        L5:
            AudioCapabilitiesReceiver.access$400(this.this$0, AudioCapabilities.getCapabilitiesInternal(AudioCapabilitiesReceiver.access$500(this.this$0), AudioCapabilitiesReceiver.access$200(this.this$0), AudioCapabilitiesReceiver.access$300(this.this$0)));
        }
    }

    private final class ExternalSurroundSoundSettingObserver extends ContentObserver {
        private final ContentResolver resolver;
        private final Uri settingUri;
        final /* synthetic */ AudioCapabilitiesReceiver this$0;

        public ExternalSurroundSoundSettingObserver(AudioCapabilitiesReceiver r1, Handler r2, ContentResolver r3, Uri r4) {
            this.this$0 = r1;
            super(r2);
            this.resolver = r3;
            this.settingUri = r4;
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean r5) {
            AudioCapabilitiesReceiver.access$400(this.this$0, AudioCapabilities.getCapabilitiesInternal(AudioCapabilitiesReceiver.access$500(this.this$0), AudioCapabilitiesReceiver.access$200(this.this$0), AudioCapabilitiesReceiver.access$300(this.this$0)));
        }

        public void register() {
            this.resolver.registerContentObserver(this.settingUri, false, this);
        }

        public void unregister() {
            this.resolver.unregisterContentObserver(this);
        }
    }

    private final class HdmiAudioPlugBroadcastReceiver extends BroadcastReceiver {
        final /* synthetic */ AudioCapabilitiesReceiver this$0;

        private HdmiAudioPlugBroadcastReceiver(AudioCapabilitiesReceiver r1) {
            this.this$0 = r1;
        }

        /* synthetic */ HdmiAudioPlugBroadcastReceiver(AudioCapabilitiesReceiver r1, C02541 r2) {
            this(r1);
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context r4, Intent r5) {
            if (isInitialStickyBroadcast() == true) goto L6;
            AudioCapabilitiesReceiver.access$400(this.this$0, AudioCapabilities.getCapabilitiesInternal(r4, r5, AudioCapabilitiesReceiver.access$200(this.this$0), AudioCapabilitiesReceiver.access$300(this.this$0)));
            return;
        }
    }

    public interface Listener {
        void onAudioCapabilitiesChanged(AudioCapabilities r1);
    }

    @Deprecated
    public AudioCapabilitiesReceiver(Context r4, Listener r5) {
        AudioDeviceInfo r2 = (AudioDeviceInfo) null;
        this(r4, r5, AudioAttributes.DEFAULT, null);
    }

    public AudioCapabilitiesReceiver(Context r3, Listener r4, AudioAttributes r5, AudioDeviceInfo r6) {
        if (Util.SDK_INT < 23) goto L6;
        if (r6 == null) goto L6;
        AudioDeviceInfoApi23 r0 = new AudioDeviceInfoApi23(r6);
    L7:
        this(r3, r4, r5, r0);
        return;
    L6:
        r0 = null;
        goto L7
    }

    /* JADX WARN: Multi-variable type inference failed */
    AudioCapabilitiesReceiver(Context r5, Listener r6, AudioAttributes r7, AudioDeviceInfoApi23 r8) {
        Context r9 = r5.getApplicationContext();
        this.context = r9;
        this.listener = (Listener) Assertions.checkNotNull(r6);
        this.audioAttributes = r7;
        this.routedDevice = r8;
        this.handler = Util.createHandlerForCurrentOrMainLooper();
        ExternalSurroundSoundSettingObserver r2 = null;
        Object[] r3 = 0;
        Object[] r4 = 0;
        if (Util.SDK_INT < 23) goto L5;
        AudioDeviceCallbackV23 r0 = new AudioDeviceCallbackV23(this, r4 == true ? 1 : 0);
    L6:
        this.audioDeviceCallback = r0;
        this.hdmiAudioPlugBroadcastReceiver = new HdmiAudioPlugBroadcastReceiver(this, r3 == true ? 1 : 0);
        Uri r1 = AudioCapabilities.getExternalSurroundSoundGlobalSettingUri();
        if (r1 == null) goto L10;
        r2 = new ExternalSurroundSoundSettingObserver(this, this.handler, r9.getContentResolver(), r1);
    L10:
        this.externalSurroundSoundSettingObserver = r2;
        return;
    L5:
        r0 = null;
        goto L6
    }

    static /* synthetic */ AudioAttributes access$200(AudioCapabilitiesReceiver r1) {
        return r1.audioAttributes;
    }

    static /* synthetic */ AudioDeviceInfoApi23 access$300(AudioCapabilitiesReceiver r1) {
        return r1.routedDevice;
    }

    static /* synthetic */ AudioDeviceInfoApi23 access$302(AudioCapabilitiesReceiver r0, AudioDeviceInfoApi23 r1) {
        r0.routedDevice = r1;
        return r1;
    }

    static /* synthetic */ void access$400(AudioCapabilitiesReceiver r0, AudioCapabilities r1) {
        r0.onNewAudioCapabilities(r1);
    }

    static /* synthetic */ Context access$500(AudioCapabilitiesReceiver r1) {
        return r1.context;
    }

    private void onNewAudioCapabilities(AudioCapabilities r2) {
        if (this.registered == true) goto L5;
        return;
    L5:
        if (r2.equals(this.audioCapabilities) == true) goto L9;
        this.audioCapabilities = r2;
        this.listener.onAudioCapabilitiesChanged(r2);
        return;
    }

    public AudioCapabilities register() {
        if (this.registered == true) goto L5;
        this.registered = true;
        if (this.externalSurroundSoundSettingObserver == null) goto L10;
        this.externalSurroundSoundSettingObserver.register();
    L10:
        if (Util.SDK_INT >= 23) goto L12;
    L14:
        Intent r0 = this.context.registerReceiver(this.hdmiAudioPlugBroadcastReceiver, new IntentFilter("android.media.action.HDMI_AUDIO_PLUG"), null, this.handler);
        this.audioCapabilities = AudioCapabilities.getCapabilitiesInternal(this.context, r0, this.audioAttributes, this.routedDevice);
        return this.audioCapabilities;
    L12:
        if (this.audioDeviceCallback == null) goto L14;
        Api23.registerAudioDeviceCallback(this.context, this.audioDeviceCallback, this.handler);
        goto L14
    L5:
        return (AudioCapabilities) Assertions.checkNotNull(this.audioCapabilities);
    }

    public void setAudioAttributes(AudioAttributes r3) {
        this.audioAttributes = r3;
        onNewAudioCapabilities(AudioCapabilities.getCapabilitiesInternal(this.context, r3, this.routedDevice));
    }

    public void setRoutedDevice(AudioDeviceInfo r4) {
        AudioDeviceInfoApi23 r1 = null;
        if (this.routedDevice != null) goto L5;
        AudioDeviceInfo r0 = null;
    L7:
        if (Util.areEqual(r4, r0) == false) goto L9;
        return;
    L9:
        if (r4 == null) goto L11;
        r1 = new AudioDeviceInfoApi23(r4);
    L11:
        this.routedDevice = r1;
        onNewAudioCapabilities(AudioCapabilities.getCapabilitiesInternal(this.context, this.audioAttributes, this.routedDevice));
        return;
    L5:
        r0 = this.routedDevice.audioDeviceInfo;
        goto L7
    }

    public void unregister() {
        if (this.registered == true) goto L5;
        return;
    L5:
        this.audioCapabilities = null;
        if (Util.SDK_INT >= 23) goto L8;
    L10:
        this.context.unregisterReceiver(this.hdmiAudioPlugBroadcastReceiver);
        if (this.externalSurroundSoundSettingObserver == null) goto L13;
        this.externalSurroundSoundSettingObserver.unregister();
    L13:
        this.registered = false;
        return;
    L8:
        if (this.audioDeviceCallback == null) goto L10;
        Api23.unregisterAudioDeviceCallback(this.context, this.audioDeviceCallback);
        goto L10
    }
}
