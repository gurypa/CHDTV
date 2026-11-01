package androidx.media;

import android.media.AudioAttributes;

/* JADX INFO: loaded from: classes.dex */
public class AudioAttributesImplApi21 implements AudioAttributesImpl {
    public AudioAttributes mAudioAttributes;
    public int mLegacyStreamType;

    static class Builder implements AudioAttributesImpl.Builder {
        final AudioAttributes.Builder mFwkBuilder;

        Builder() {
            this.mFwkBuilder = new AudioAttributes.Builder();
        }

        Builder(Object r3) {
            this.mFwkBuilder = new AudioAttributes.Builder((AudioAttributes) r3);
        }

        @Override // androidx.media.AudioAttributesImpl.Builder
        public AudioAttributesImpl build() {
            return new AudioAttributesImplApi21(this.mFwkBuilder.build());
        }

        @Override // androidx.media.AudioAttributesImpl.Builder
        public /* bridge */ /* synthetic */ AudioAttributesImpl.Builder setContentType(int r1) {
            return setContentType(r1);
        }

        @Override // androidx.media.AudioAttributesImpl.Builder
        public Builder setContentType(int r2) {
            this.mFwkBuilder.setContentType(r2);
            return this;
        }

        @Override // androidx.media.AudioAttributesImpl.Builder
        public /* bridge */ /* synthetic */ AudioAttributesImpl.Builder setFlags(int r1) {
            return setFlags(r1);
        }

        @Override // androidx.media.AudioAttributesImpl.Builder
        public Builder setFlags(int r2) {
            this.mFwkBuilder.setFlags(r2);
            return this;
        }

        @Override // androidx.media.AudioAttributesImpl.Builder
        public /* bridge */ /* synthetic */ AudioAttributesImpl.Builder setLegacyStreamType(int r1) {
            return setLegacyStreamType(r1);
        }

        @Override // androidx.media.AudioAttributesImpl.Builder
        public Builder setLegacyStreamType(int r2) {
            this.mFwkBuilder.setLegacyStreamType(r2);
            return this;
        }

        @Override // androidx.media.AudioAttributesImpl.Builder
        public /* bridge */ /* synthetic */ AudioAttributesImpl.Builder setUsage(int r1) {
            return setUsage(r1);
        }

        @Override // androidx.media.AudioAttributesImpl.Builder
        public Builder setUsage(int r2) {
            if (r2 != 16) goto L5;
            r2 = 12;
        L5:
            this.mFwkBuilder.setUsage(r2);
            return this;
        }
    }

    public AudioAttributesImplApi21() {
        this.mLegacyStreamType = -1;
    }

    AudioAttributesImplApi21(AudioAttributes r2) {
        this(r2, -1);
    }

    AudioAttributesImplApi21(AudioAttributes r2, int r3) {
        this.mLegacyStreamType = -1;
        this.mAudioAttributes = r2;
        this.mLegacyStreamType = r3;
    }

    public boolean equals(Object r4) {
        if ((r4 instanceof AudioAttributesImplApi21) == true) goto L7;
        return false;
    L7:
        return this.mAudioAttributes.equals(((AudioAttributesImplApi21) r4).mAudioAttributes);
    }

    @Override // androidx.media.AudioAttributesImpl
    public Object getAudioAttributes() {
        return this.mAudioAttributes;
    }

    @Override // androidx.media.AudioAttributesImpl
    public int getContentType() {
        return this.mAudioAttributes.getContentType();
    }

    @Override // androidx.media.AudioAttributesImpl
    public int getFlags() {
        return this.mAudioAttributes.getFlags();
    }

    @Override // androidx.media.AudioAttributesImpl
    public int getLegacyStreamType() {
        if (this.mLegacyStreamType == (-1)) goto L7;
        return this.mLegacyStreamType;
    L7:
        return AudioAttributesCompat.toVolumeStreamType(false, getFlags(), getUsage());
    }

    @Override // androidx.media.AudioAttributesImpl
    public int getRawLegacyStreamType() {
        return this.mLegacyStreamType;
    }

    @Override // androidx.media.AudioAttributesImpl
    public int getUsage() {
        return this.mAudioAttributes.getUsage();
    }

    @Override // androidx.media.AudioAttributesImpl
    public int getVolumeControlStream() {
        return AudioAttributesCompat.toVolumeStreamType(true, getFlags(), getUsage());
    }

    public int hashCode() {
        return this.mAudioAttributes.hashCode();
    }

    public String toString() {
        return "AudioAttributesCompat: audioattributes=" + this.mAudioAttributes;
    }
}
