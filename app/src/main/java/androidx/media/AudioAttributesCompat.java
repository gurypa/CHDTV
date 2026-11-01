package androidx.media;

import android.media.AudioAttributes;
import android.os.Build;
import android.util.SparseIntArray;
import androidx.versionedparcelable.VersionedParcelable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* JADX INFO: loaded from: classes.dex */
public class AudioAttributesCompat implements VersionedParcelable {
    public static final int CONTENT_TYPE_MOVIE = 3;
    public static final int CONTENT_TYPE_MUSIC = 2;
    public static final int CONTENT_TYPE_SONIFICATION = 4;
    public static final int CONTENT_TYPE_SPEECH = 1;
    public static final int CONTENT_TYPE_UNKNOWN = 0;
    static final int FLAG_ALL = 1023;
    static final int FLAG_ALL_PUBLIC = 273;
    public static final int FLAG_AUDIBILITY_ENFORCED = 1;
    static final int FLAG_BEACON = 8;
    static final int FLAG_BYPASS_INTERRUPTION_POLICY = 64;
    static final int FLAG_BYPASS_MUTE = 128;
    static final int FLAG_DEEP_BUFFER = 512;
    public static final int FLAG_HW_AV_SYNC = 16;
    static final int FLAG_HW_HOTWORD = 32;
    static final int FLAG_LOW_LATENCY = 256;
    static final int FLAG_SCO = 4;
    static final int FLAG_SECURE = 2;
    static final int INVALID_STREAM_TYPE = -1;
    private static final int[] SDK_USAGES = null;
    private static final int SUPPRESSIBLE_CALL = 2;
    private static final int SUPPRESSIBLE_NOTIFICATION = 1;
    private static final SparseIntArray SUPPRESSIBLE_USAGES = null;
    static final String TAG = "AudioAttributesCompat";
    public static final int USAGE_ALARM = 4;
    public static final int USAGE_ASSISTANCE_ACCESSIBILITY = 11;
    public static final int USAGE_ASSISTANCE_NAVIGATION_GUIDANCE = 12;
    public static final int USAGE_ASSISTANCE_SONIFICATION = 13;
    public static final int USAGE_ASSISTANT = 16;
    public static final int USAGE_GAME = 14;
    public static final int USAGE_MEDIA = 1;
    public static final int USAGE_NOTIFICATION = 5;
    public static final int USAGE_NOTIFICATION_COMMUNICATION_DELAYED = 9;
    public static final int USAGE_NOTIFICATION_COMMUNICATION_INSTANT = 8;
    public static final int USAGE_NOTIFICATION_COMMUNICATION_REQUEST = 7;
    public static final int USAGE_NOTIFICATION_EVENT = 10;
    public static final int USAGE_NOTIFICATION_RINGTONE = 6;
    public static final int USAGE_UNKNOWN = 0;
    static final int USAGE_VIRTUAL_SOURCE = 15;
    public static final int USAGE_VOICE_COMMUNICATION = 2;
    public static final int USAGE_VOICE_COMMUNICATION_SIGNALLING = 3;
    static boolean sForceLegacyBehavior;
    public AudioAttributesImpl mImpl;

    @Retention(RetentionPolicy.SOURCE)
    public @interface AttributeContentType {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface AttributeUsage {
    }

    static abstract class AudioManagerHidden {
        public static final int STREAM_ACCESSIBILITY = 10;
        public static final int STREAM_BLUETOOTH_SCO = 6;
        public static final int STREAM_SYSTEM_ENFORCED = 7;
        public static final int STREAM_TTS = 9;

        private AudioManagerHidden() {
        }
    }

    public static class Builder {
        final AudioAttributesImpl.Builder mBuilderImpl;

        public Builder() {
            if (AudioAttributesCompat.sForceLegacyBehavior == false) goto L6;
            this.mBuilderImpl = new AudioAttributesImplBase.Builder();
            return;
        L6:
            if (Build.VERSION.SDK_INT < 26) goto L8;
            this.mBuilderImpl = new AudioAttributesImplApi26.Builder();
            return;
        L8:
            this.mBuilderImpl = new AudioAttributesImplApi21.Builder();
        }

        public Builder(AudioAttributesCompat r3) {
            if (AudioAttributesCompat.sForceLegacyBehavior == false) goto L6;
            this.mBuilderImpl = new AudioAttributesImplBase.Builder(r3);
            return;
        L6:
            if (Build.VERSION.SDK_INT < 26) goto L8;
            this.mBuilderImpl = new AudioAttributesImplApi26.Builder(r3.unwrap());
            return;
        L8:
            this.mBuilderImpl = new AudioAttributesImplApi21.Builder(r3.unwrap());
        }

        public AudioAttributesCompat build() {
            return new AudioAttributesCompat(this.mBuilderImpl.build());
        }

        public Builder setContentType(int r2) {
            this.mBuilderImpl.setContentType(r2);
            return this;
        }

        public Builder setFlags(int r2) {
            this.mBuilderImpl.setFlags(r2);
            return this;
        }

        public Builder setLegacyStreamType(int r2) {
            this.mBuilderImpl.setLegacyStreamType(r2);
            return this;
        }

        public Builder setUsage(int r2) {
            this.mBuilderImpl.setUsage(r2);
            return this;
        }
    }

    static {
        SUPPRESSIBLE_USAGES = new SparseIntArray();
        SUPPRESSIBLE_USAGES.put(5, 1);
        SUPPRESSIBLE_USAGES.put(6, 2);
        SUPPRESSIBLE_USAGES.put(7, 2);
        SUPPRESSIBLE_USAGES.put(8, 1);
        SUPPRESSIBLE_USAGES.put(9, 1);
        SUPPRESSIBLE_USAGES.put(10, 1);
        SDK_USAGES = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 16};
    }

    public AudioAttributesCompat() {
    }

    AudioAttributesCompat(AudioAttributesImpl r1) {
        this.mImpl = r1;
    }

    public static void setForceLegacyBehavior(boolean r0) {
        sForceLegacyBehavior = r0;
    }

    static int toVolumeStreamType(boolean r4, int r5, int r6) {
        if ((r5 & 1) != 1) goto L9;
        if (r4 == true) goto L35;
        return 7;
    L35:
        return 1;
    L9:
        if ((r5 & 4) != 4) goto L15;
        if (r4 == true) goto L36;
        return 6;
    L36:
        return 0;
    L15:
        switch(r6) {
            case 0: goto L32;
            case 1: goto L31;
            case 2: goto L30;
            case 3: goto L26;
            case 4: goto L25;
            case 5: goto L23;
            case 6: goto L21;
            case 7: goto L23;
            case 8: goto L23;
            case 9: goto L23;
            case 10: goto L23;
            case 11: goto L19;
            case 12: goto L31;
            case 13: goto L18;
            case 14: goto L31;
            case 15: goto L16;
            case 16: goto L31;
            default: goto L16;
        };
    L16:
        if (r4 == true) goto L34;
        return 3;
    L34:
        throw new IllegalArgumentException("Unknown usage value " + r6 + " in audio attributes");
    L18:
        return 1;
    L19:
        return 10;
    L21:
        return 2;
    L23:
        return 5;
    L25:
        return 4;
    L26:
        if (r4 == true) goto L37;
        return 8;
    L37:
        return 0;
    L30:
        return 0;
    L31:
        return 3;
    L32:
        return 3;
    }

    static String usageToString(int r2) {
        switch(r2) {
            case 0: goto L35;
            case 1: goto L33;
            case 2: goto L31;
            case 3: goto L29;
            case 4: goto L27;
            case 5: goto L25;
            case 6: goto L23;
            case 7: goto L21;
            case 8: goto L19;
            case 9: goto L17;
            case 10: goto L15;
            case 11: goto L13;
            case 12: goto L11;
            case 13: goto L9;
            case 14: goto L7;
            case 15: goto L4;
            case 16: goto L5;
            default: goto L4;
        };
    L5:
        return "USAGE_ASSISTANT";
    L7:
        return "USAGE_GAME";
    L9:
        return "USAGE_ASSISTANCE_SONIFICATION";
    L11:
        return "USAGE_ASSISTANCE_NAVIGATION_GUIDANCE";
    L13:
        return "USAGE_ASSISTANCE_ACCESSIBILITY";
    L15:
        return "USAGE_NOTIFICATION_EVENT";
    L17:
        return "USAGE_NOTIFICATION_COMMUNICATION_DELAYED";
    L19:
        return "USAGE_NOTIFICATION_COMMUNICATION_INSTANT";
    L21:
        return "USAGE_NOTIFICATION_COMMUNICATION_REQUEST";
    L23:
        return "USAGE_NOTIFICATION_RINGTONE";
    L25:
        return "USAGE_NOTIFICATION";
    L27:
        return "USAGE_ALARM";
    L29:
        return "USAGE_VOICE_COMMUNICATION_SIGNALLING";
    L31:
        return "USAGE_VOICE_COMMUNICATION";
    L33:
        return "USAGE_MEDIA";
    L35:
        return "USAGE_UNKNOWN";
    L4:
        return "unknown usage " + r2;
    }

    public static AudioAttributesCompat wrap(Object r3) {
        if (sForceLegacyBehavior == false) goto L7;
        return null;
    L7:
        if (Build.VERSION.SDK_INT < 26) goto L11;
        return new AudioAttributesCompat(new AudioAttributesImplApi26((AudioAttributes) r3));
    L11:
        return new AudioAttributesCompat(new AudioAttributesImplApi21((AudioAttributes) r3));
    }

    public boolean equals(Object r4) {
        if ((r4 instanceof AudioAttributesCompat) == true) goto L5;
        return false;
    L5:
        AudioAttributesCompat r0 = (AudioAttributesCompat) r4;
        if (this.mImpl != null) goto L12;
        if (r0.mImpl != null) goto L13;
        return true;
    L13:
        return false;
    L12:
        return this.mImpl.equals(r0.mImpl);
    }

    public int getContentType() {
        return this.mImpl.getContentType();
    }

    public int getFlags() {
        return this.mImpl.getFlags();
    }

    public int getLegacyStreamType() {
        return this.mImpl.getLegacyStreamType();
    }

    int getRawLegacyStreamType() {
        return this.mImpl.getRawLegacyStreamType();
    }

    public int getUsage() {
        return this.mImpl.getUsage();
    }

    public int getVolumeControlStream() {
        return this.mImpl.getVolumeControlStream();
    }

    public int hashCode() {
        return this.mImpl.hashCode();
    }

    public String toString() {
        return this.mImpl.toString();
    }

    public Object unwrap() {
        return this.mImpl.getAudioAttributes();
    }
}
