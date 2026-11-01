package androidx.media3.common;

import android.os.Bundle;
import androidx.media3.common.util.Util;

/* JADX INFO: loaded from: classes.dex */
public final class AudioAttributes {
    public static final AudioAttributes DEFAULT = null;
    private static final String FIELD_ALLOWED_CAPTURE_POLICY = null;
    private static final String FIELD_CONTENT_TYPE = null;
    private static final String FIELD_FLAGS = null;
    private static final String FIELD_SPATIALIZATION_BEHAVIOR = null;
    private static final String FIELD_USAGE = null;
    public final int allowedCapturePolicy;
    private AudioAttributesV21 audioAttributesV21;
    public final int contentType;
    public final int flags;
    public final int spatializationBehavior;
    public final int usage;

    /* JADX INFO: renamed from: androidx.media3.common.AudioAttributes$1 */
    static /* synthetic */ class C01791 {
    }

    private static final class Api29 {
        private Api29() {
        }

        public static void setAllowedCapturePolicy(android.media.AudioAttributes.Builder r0, int r1) {
            r0.setAllowedCapturePolicy(r1);
        }
    }

    private static final class Api32 {
        private Api32() {
        }

        public static void setSpatializationBehavior(android.media.AudioAttributes.Builder r0, int r1) {
            r0.setSpatializationBehavior(r1);
        }
    }

    public static final class AudioAttributesV21 {
        public final android.media.AudioAttributes audioAttributes;

        private AudioAttributesV21(AudioAttributes r4) {
            android.media.AudioAttributes.Builder r0 = new android.media.AudioAttributes.Builder().setContentType(r4.contentType).setFlags(r4.flags).setUsage(r4.usage);
            if (Util.SDK_INT < 29) goto L6;
            Api29.setAllowedCapturePolicy(r0, r4.allowedCapturePolicy);
        L6:
            if (Util.SDK_INT < 32) goto L8;
            Api32.setSpatializationBehavior(r0, r4.spatializationBehavior);
        L8:
            this.audioAttributes = r0.build();
        }

        /* synthetic */ AudioAttributesV21(AudioAttributes r1, C01791 r2) {
            this(r1);
        }
    }

    public static final class Builder {
        private int allowedCapturePolicy;
        private int contentType;
        private int flags;
        private int spatializationBehavior;
        private int usage;

        public Builder() {
            this.contentType = 0;
            this.flags = 0;
            this.usage = 1;
            this.allowedCapturePolicy = 1;
            this.spatializationBehavior = 0;
        }

        public AudioAttributes build() {
            return new AudioAttributes(this.contentType, this.flags, this.usage, this.allowedCapturePolicy, this.spatializationBehavior, null);
        }

        public Builder setAllowedCapturePolicy(int r1) {
            this.allowedCapturePolicy = r1;
            return this;
        }

        public Builder setContentType(int r1) {
            this.contentType = r1;
            return this;
        }

        public Builder setFlags(int r1) {
            this.flags = r1;
            return this;
        }

        public Builder setSpatializationBehavior(int r1) {
            this.spatializationBehavior = r1;
            return this;
        }

        public Builder setUsage(int r1) {
            this.usage = r1;
            return this;
        }
    }

    static {
        DEFAULT = new Builder().build();
        FIELD_CONTENT_TYPE = Util.intToStringMaxRadix(0);
        FIELD_FLAGS = Util.intToStringMaxRadix(1);
        FIELD_USAGE = Util.intToStringMaxRadix(2);
        FIELD_ALLOWED_CAPTURE_POLICY = Util.intToStringMaxRadix(3);
        FIELD_SPATIALIZATION_BEHAVIOR = Util.intToStringMaxRadix(4);
    }

    private AudioAttributes(int r1, int r2, int r3, int r4, int r5) {
        this.contentType = r1;
        this.flags = r2;
        this.usage = r3;
        this.allowedCapturePolicy = r4;
        this.spatializationBehavior = r5;
    }

    /* synthetic */ AudioAttributes(int r1, int r2, int r3, int r4, int r5, C01791 r6) {
        this(r1, r2, r3, r4, r5);
    }

    public static AudioAttributes fromBundle(Bundle r2) {
        Builder r0 = new Builder();
        if (r2.containsKey(FIELD_CONTENT_TYPE) == false) goto L6;
        r0.setContentType(r2.getInt(FIELD_CONTENT_TYPE));
    L6:
        if (r2.containsKey(FIELD_FLAGS) == false) goto L9;
        r0.setFlags(r2.getInt(FIELD_FLAGS));
    L9:
        if (r2.containsKey(FIELD_USAGE) == false) goto L12;
        r0.setUsage(r2.getInt(FIELD_USAGE));
    L12:
        if (r2.containsKey(FIELD_ALLOWED_CAPTURE_POLICY) == false) goto L15;
        r0.setAllowedCapturePolicy(r2.getInt(FIELD_ALLOWED_CAPTURE_POLICY));
    L15:
        if (r2.containsKey(FIELD_SPATIALIZATION_BEHAVIOR) == false) goto L18;
        r0.setSpatializationBehavior(r2.getInt(FIELD_SPATIALIZATION_BEHAVIOR));
    L18:
        return r0.build();
    }

    public boolean equals(Object r6) {
        if (this != r6) goto L6;
        return true;
    L6:
        if (r6 != null) goto L8;
    L23:
        return false;
    L8:
        if (getClass() != r6.getClass()) goto L23;
        AudioAttributes r2 = (AudioAttributes) r6;
        if (this.contentType != r2.contentType) goto L22;
        if (this.flags != r2.flags) goto L22;
        if (this.usage != r2.usage) goto L22;
        if (this.allowedCapturePolicy != r2.allowedCapturePolicy) goto L22;
        if (this.spatializationBehavior != r2.spatializationBehavior) goto L22;
        return true;
    L22:
        return false;
    }

    public AudioAttributesV21 getAudioAttributesV21() {
        if (this.audioAttributesV21 != null) goto L6;
        this.audioAttributesV21 = new AudioAttributesV21(this, null);
    L6:
        return this.audioAttributesV21;
    }

    public int hashCode() {
        return (((((((((17 * 31) + this.contentType) * 31) + this.flags) * 31) + this.usage) * 31) + this.allowedCapturePolicy) * 31) + this.spatializationBehavior;
    }

    public Bundle toBundle() {
        Bundle r0 = new Bundle();
        r0.putInt(FIELD_CONTENT_TYPE, this.contentType);
        r0.putInt(FIELD_FLAGS, this.flags);
        r0.putInt(FIELD_USAGE, this.usage);
        r0.putInt(FIELD_ALLOWED_CAPTURE_POLICY, this.allowedCapturePolicy);
        r0.putInt(FIELD_SPATIALIZATION_BEHAVIOR, this.spatializationBehavior);
        return r0;
    }
}
