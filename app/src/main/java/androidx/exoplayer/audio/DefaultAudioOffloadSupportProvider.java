package androidx.media3.exoplayer.audio;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;

/* JADX INFO: loaded from: classes.dex */
public final class DefaultAudioOffloadSupportProvider implements DefaultAudioSink.AudioOffloadSupportProvider {
    private static final String OFFLOAD_VARIABLE_RATE_SUPPORTED_KEY = "offloadVariableRateSupported";
    private final Context context;
    private Boolean isOffloadVariableRateSupported;

    private static final class Api29 {
        private Api29() {
        }

        public static AudioOffloadSupport getOffloadedPlaybackSupport(AudioFormat r2, AudioAttributes r3, boolean r4) {
            if (AudioManager.isOffloadedPlaybackSupported(r2, r3) == true) goto L7;
            return AudioOffloadSupport.DEFAULT_UNSUPPORTED;
        L7:
            return new AudioOffloadSupport.Builder().setIsFormatSupported(true).setIsSpeedChangeSupported(r4).build();
        }
    }

    private static final class Api31 {
        private Api31() {
        }

        public static AudioOffloadSupport getOffloadedPlaybackSupport(AudioFormat r5, AudioAttributes r6, boolean r7) {
            int r0 = AudioManager.getPlaybackOffloadSupport(r5, r6);
            if (r0 == 0) goto L5;
            AudioOffloadSupport.Builder r1 = new AudioOffloadSupport.Builder();
            if (Util.SDK_INT > 32) goto L9;
        L11:
            boolean r2 = false;
        L13:
            return r1.setIsFormatSupported(true).setIsGaplessSupported(r2).setIsSpeedChangeSupported(r7).build();
        L9:
            if (r0 != 2) goto L11;
            r2 = true;
            goto L13
        L5:
            return AudioOffloadSupport.DEFAULT_UNSUPPORTED;
        }
    }

    public DefaultAudioOffloadSupportProvider() {
        this(null);
    }

    public DefaultAudioOffloadSupportProvider(Context r1) {
        this.context = r1;
    }

    private boolean isOffloadVariableRateSupported(Context r5) {
        if (this.isOffloadVariableRateSupported != null) goto L5;
        boolean r0 = false;
        if (r5 == null) goto L19;
        AudioManager r1 = (AudioManager) r5.getSystemService(MimeTypes.BASE_TYPE_AUDIO);
        if (r1 == null) goto L17;
        String r2 = r1.getParameters(OFFLOAD_VARIABLE_RATE_SUPPORTED_KEY);
        if (r2 != null) goto L13;
    L16:
        this.isOffloadVariableRateSupported = Boolean.valueOf(r0);
    L21:
        return this.isOffloadVariableRateSupported.booleanValue();
    L13:
        if (r2.equals("offloadVariableRateSupported=1") == false) goto L16;
        r0 = true;
        goto L16
    L17:
        this.isOffloadVariableRateSupported = false;
        goto L21
    L19:
        this.isOffloadVariableRateSupported = false;
        goto L21
    L5:
        return this.isOffloadVariableRateSupported.booleanValue();
    }

    @Override // androidx.media3.exoplayer.audio.DefaultAudioSink.AudioOffloadSupportProvider
    public AudioOffloadSupport getAudioOffloadSupport(Format r7, androidx.media3.common.AudioAttributes r8) {
        Assertions.checkNotNull(r7);
        Assertions.checkNotNull(r8);
        if (Util.SDK_INT < 29) goto L29;
        if (r7.sampleRate == (-1)) goto L29;
        boolean r0 = isOffloadVariableRateSupported(this.context);
        int r1 = MimeTypes.getEncoding((String) Assertions.checkNotNull(r7.sampleMimeType), r7.codecs);
        if (r1 == 0) goto L27;
        if (Util.SDK_INT < Util.getApiLevelThatAudioFormatIntroducedAudioEncoding(r1)) goto L27;
        int r2 = Util.getAudioTrackChannelConfig(r7.channelCount);
        if (r2 == 0) goto L15;
        AudioFormat r3 = Util.getAudioFormat(r7.sampleRate, r2, r1);     // Catch: IllegalArgumentException -> L23
        if (Util.SDK_INT < 31) goto L22;
        return Api31.getOffloadedPlaybackSupport(r3, r8.getAudioAttributesV21().audioAttributes, r0);
    L22:
        return Api29.getOffloadedPlaybackSupport(r3, r8.getAudioAttributesV21().audioAttributes, r0);
    L25:
        return AudioOffloadSupport.DEFAULT_UNSUPPORTED;
    L15:
        return AudioOffloadSupport.DEFAULT_UNSUPPORTED;
    L27:
        return AudioOffloadSupport.DEFAULT_UNSUPPORTED;
    L29:
        return AudioOffloadSupport.DEFAULT_UNSUPPORTED;
    }
}
