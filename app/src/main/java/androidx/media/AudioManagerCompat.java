package androidx.media;

import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.os.Build;

/* JADX INFO: loaded from: classes.dex */
public final class AudioManagerCompat {
    public static final int AUDIOFOCUS_GAIN = 1;
    public static final int AUDIOFOCUS_GAIN_TRANSIENT = 2;
    public static final int AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE = 4;
    public static final int AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK = 3;
    private static final String TAG = "AudioManCompat";

    private static class Api21Impl {
        private Api21Impl() {
        }

        static boolean isVolumeFixed(AudioManager r1) {
            return r1.isVolumeFixed();
        }
    }

    private static class Api26Impl {
        private Api26Impl() {
        }

        static int abandonAudioFocusRequest(AudioManager r1, AudioFocusRequest r2) {
            return r1.abandonAudioFocusRequest(r2);
        }

        static int requestAudioFocus(AudioManager r1, AudioFocusRequest r2) {
            return r1.requestAudioFocus(r2);
        }
    }

    private static class Api28Impl {
        private Api28Impl() {
        }

        static int getStreamMinVolume(AudioManager r1, int r2) {
            return r1.getStreamMinVolume(r2);
        }
    }

    private AudioManagerCompat() {
    }

    public static int abandonAudioFocusRequest(AudioManager r2, AudioFocusRequestCompat r3) {
        if (r2 == null) goto L13;
        if (r3 == null) goto L11;
        if (Build.VERSION.SDK_INT < 26) goto L9;
        return Api26Impl.abandonAudioFocusRequest(r2, r3.getAudioFocusRequest());
    L9:
        return r2.abandonAudioFocus(r3.getOnAudioFocusChangeListener());
    L11:
        throw new IllegalArgumentException("AudioFocusRequestCompat must not be null");
    L13:
        throw new IllegalArgumentException("AudioManager must not be null");
    }

    public static int getStreamMaxVolume(AudioManager r1, int r2) {
        return r1.getStreamMaxVolume(r2);
    }

    public static int getStreamMinVolume(AudioManager r2, int r3) {
        if (Build.VERSION.SDK_INT >= 28) goto L5;
        return 0;
    L5:
        return Api28Impl.getStreamMinVolume(r2, r3);
    }

    public static boolean isVolumeFixed(AudioManager r1) {
        return Api21Impl.isVolumeFixed(r1);
    }

    public static int requestAudioFocus(AudioManager r3, AudioFocusRequestCompat r4) {
        if (r3 == null) goto L13;
        if (r4 == null) goto L11;
        if (Build.VERSION.SDK_INT < 26) goto L9;
        return Api26Impl.requestAudioFocus(r3, r4.getAudioFocusRequest());
    L9:
        return r3.requestAudioFocus(r4.getOnAudioFocusChangeListener(), r4.getAudioAttributesCompat().getLegacyStreamType(), r4.getFocusGain());
    L11:
        throw new IllegalArgumentException("AudioFocusRequestCompat must not be null");
    L13:
        throw new IllegalArgumentException("AudioManager must not be null");
    }
}
