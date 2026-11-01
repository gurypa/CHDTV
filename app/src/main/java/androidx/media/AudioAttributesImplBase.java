package androidx.media;

import android.util.Log;
import androidx.media3.exoplayer.analytics.AnalyticsListener;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public class AudioAttributesImplBase implements AudioAttributesImpl {
    public int mContentType;
    public int mFlags;
    public int mLegacyStream;
    public int mUsage;

    static class Builder implements AudioAttributesImpl.Builder {
        private int mContentType;
        private int mFlags;
        private int mLegacyStream;
        private int mUsage;

        Builder() {
            this.mUsage = 0;
            this.mContentType = 0;
            this.mFlags = 0;
            this.mLegacyStream = -1;
        }

        Builder(AudioAttributesCompat r2) {
            this.mUsage = 0;
            this.mContentType = 0;
            this.mFlags = 0;
            this.mLegacyStream = -1;
            this.mUsage = r2.getUsage();
            this.mContentType = r2.getContentType();
            this.mFlags = r2.getFlags();
            this.mLegacyStream = r2.getRawLegacyStreamType();
        }

        private Builder setInternalLegacyStreamType(int r4) {
            switch(r4) {
                case 0: goto L15;
                case 1: goto L14;
                case 2: goto L13;
                case 3: goto L12;
                case 4: goto L11;
                case 5: goto L10;
                case 6: goto L9;
                case 7: goto L8;
                case 8: goto L7;
                case 9: goto L6;
                case 10: goto L5;
                default: goto L4;
            };
        L4:
            Log.e("AudioAttributesCompat", "Invalid stream type " + r4 + " for AudioAttributesCompat");
        L16:
            this.mUsage = AudioAttributesImplBase.usageForStreamType(r4);
            return this;
        L5:
            this.mContentType = 1;
            goto L16
        L6:
            this.mContentType = 4;
            goto L16
        L7:
            this.mContentType = 4;
            goto L16
        L8:
            this.mFlags = 1 | this.mFlags;
            goto L14
        L9:
            this.mContentType = 1;
            this.mFlags |= 4;
            goto L16
        L10:
            this.mContentType = 4;
            goto L16
        L11:
            this.mContentType = 4;
            goto L16
        L12:
            this.mContentType = 2;
            goto L16
        L13:
            this.mContentType = 4;
        L14:
            this.mContentType = 4;
            goto L16
        L15:
            this.mContentType = 1;
            goto L16
        }

        @Override // androidx.media.AudioAttributesImpl.Builder
        public AudioAttributesImpl build() {
            return new AudioAttributesImplBase(this.mContentType, this.mFlags, this.mUsage, this.mLegacyStream);
        }

        @Override // androidx.media.AudioAttributesImpl.Builder
        public /* bridge */ /* synthetic */ AudioAttributesImpl.Builder setContentType(int r1) {
            return setContentType(r1);
        }

        @Override // androidx.media.AudioAttributesImpl.Builder
        public Builder setContentType(int r2) {
            switch(r2) {
                case 0: goto L4;
                case 1: goto L4;
                case 2: goto L4;
                case 3: goto L4;
                case 4: goto L4;
                default: goto L3;
            };
        L3:
            this.mContentType = 0;
        L5:
            return this;
        L4:
            this.mContentType = r2;
            goto L5
        }

        @Override // androidx.media.AudioAttributesImpl.Builder
        public /* bridge */ /* synthetic */ AudioAttributesImpl.Builder setFlags(int r1) {
            return setFlags(r1);
        }

        @Override // androidx.media.AudioAttributesImpl.Builder
        public Builder setFlags(int r2) {
            this.mFlags |= r2 & AnalyticsListener.EVENT_DRM_KEYS_LOADED;
            return this;
        }

        @Override // androidx.media.AudioAttributesImpl.Builder
        public /* bridge */ /* synthetic */ AudioAttributesImpl.Builder setLegacyStreamType(int r1) {
            return setLegacyStreamType(r1);
        }

        @Override // androidx.media.AudioAttributesImpl.Builder
        public Builder setLegacyStreamType(int r3) {
            if (r3 == 10) goto L7;
            this.mLegacyStream = r3;
            return setInternalLegacyStreamType(r3);
        L7:
            throw new IllegalArgumentException("STREAM_ACCESSIBILITY is not a legacy stream type that was used for audio playback");
        }

        @Override // androidx.media.AudioAttributesImpl.Builder
        public /* bridge */ /* synthetic */ AudioAttributesImpl.Builder setUsage(int r1) {
            return setUsage(r1);
        }

        @Override // androidx.media.AudioAttributesImpl.Builder
        public Builder setUsage(int r2) {
            switch(r2) {
                case 0: goto L5;
                case 1: goto L5;
                case 2: goto L5;
                case 3: goto L5;
                case 4: goto L5;
                case 5: goto L5;
                case 6: goto L5;
                case 7: goto L5;
                case 8: goto L5;
                case 9: goto L5;
                case 10: goto L5;
                case 11: goto L5;
                case 12: goto L5;
                case 13: goto L5;
                case 14: goto L5;
                case 15: goto L5;
                case 16: goto L4;
                default: goto L3;
            };
        L3:
            this.mUsage = 0;
        L6:
            return this;
        L4:
            this.mUsage = 12;
            goto L6
        L5:
            this.mUsage = r2;
            goto L6
        }
    }

    public AudioAttributesImplBase() {
        this.mUsage = 0;
        this.mContentType = 0;
        this.mFlags = 0;
        this.mLegacyStream = -1;
    }

    AudioAttributesImplBase(int r2, int r3, int r4, int r5) {
        this.mUsage = 0;
        this.mContentType = 0;
        this.mFlags = 0;
        this.mLegacyStream = -1;
        this.mContentType = r2;
        this.mFlags = r3;
        this.mUsage = r4;
        this.mLegacyStream = r5;
    }

    static int usageForStreamType(int r1) {
        switch(r1) {
            case 0: goto L21;
            case 1: goto L19;
            case 2: goto L17;
            case 3: goto L15;
            case 4: goto L13;
            case 5: goto L11;
            case 6: goto L10;
            case 7: goto L19;
            case 8: goto L8;
            case 9: goto L4;
            case 10: goto L6;
            default: goto L4;
        };
    L4:
        return 0;
    L6:
        return 11;
    L8:
        return 3;
    L10:
        return 2;
    L11:
        return 5;
    L13:
        return 4;
    L15:
        return 1;
    L17:
        return 6;
    L19:
        return 13;
    L21:
        return 2;
    }

    public boolean equals(Object r5) {
        if ((r5 instanceof AudioAttributesImplBase) == true) goto L5;
        return false;
    L5:
        AudioAttributesImplBase r0 = (AudioAttributesImplBase) r5;
        if (this.mContentType == r0.getContentType()) goto L8;
        return false;
    L8:
        if (this.mFlags == r0.getFlags()) goto L10;
        return false;
    L10:
        if (this.mUsage == r0.getUsage()) goto L12;
        return false;
    L12:
        if (this.mLegacyStream != r0.mLegacyStream) goto L19;
        return true;
    L19:
        return false;
    }

    @Override // androidx.media.AudioAttributesImpl
    public Object getAudioAttributes() {
        return null;
    }

    @Override // androidx.media.AudioAttributesImpl
    public int getContentType() {
        return this.mContentType;
    }

    @Override // androidx.media.AudioAttributesImpl
    public int getFlags() {
        int r0 = this.mFlags;
        int r1 = getLegacyStreamType();
        if (r1 != 6) goto L6;
        r0 = r0 | 4;
    L9:
        return r0 & 273;
    L6:
        if (r1 != 7) goto L9;
        r0 = r0 | 1;
        goto L9
    }

    @Override // androidx.media.AudioAttributesImpl
    public int getLegacyStreamType() {
        if (this.mLegacyStream == (-1)) goto L7;
        return this.mLegacyStream;
    L7:
        return AudioAttributesCompat.toVolumeStreamType(false, this.mFlags, this.mUsage);
    }

    @Override // androidx.media.AudioAttributesImpl
    public int getRawLegacyStreamType() {
        return this.mLegacyStream;
    }

    @Override // androidx.media.AudioAttributesImpl
    public int getUsage() {
        return this.mUsage;
    }

    @Override // androidx.media.AudioAttributesImpl
    public int getVolumeControlStream() {
        return AudioAttributesCompat.toVolumeStreamType(true, this.mFlags, this.mUsage);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{Integer.valueOf(this.mContentType), Integer.valueOf(this.mFlags), Integer.valueOf(this.mUsage), Integer.valueOf(this.mLegacyStream)});
    }

    public String toString() {
        StringBuilder r0 = new StringBuilder("AudioAttributesCompat:");
        if (this.mLegacyStream == (-1)) goto L5;
        r0.append(" stream=").append(this.mLegacyStream);
        r0.append(" derived");
    L5:
        r0.append(" usage=").append(AudioAttributesCompat.usageToString(this.mUsage)).append(" content=").append(this.mContentType).append(" flags=0x").append(Integer.toHexString(this.mFlags).toUpperCase());
        return r0.toString();
    }
}
