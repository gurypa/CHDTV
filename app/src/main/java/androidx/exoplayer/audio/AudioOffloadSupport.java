package androidx.media3.exoplayer.audio;

/* JADX INFO: loaded from: classes.dex */
public final class AudioOffloadSupport {
    public static final AudioOffloadSupport DEFAULT_UNSUPPORTED = null;
    public final boolean isFormatSupported;
    public final boolean isGaplessSupported;
    public final boolean isSpeedChangeSupported;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.audio.AudioOffloadSupport$1 */
    static /* synthetic */ class C02551 {
    }

    public static final class Builder {
        private boolean isFormatSupported;
        private boolean isGaplessSupported;
        private boolean isSpeedChangeSupported;

        public Builder() {
        }

        public Builder(AudioOffloadSupport r2) {
            this.isFormatSupported = r2.isFormatSupported;
            this.isGaplessSupported = r2.isGaplessSupported;
            this.isSpeedChangeSupported = r2.isSpeedChangeSupported;
        }

        static /* synthetic */ boolean access$100(Builder r1) {
            return r1.isFormatSupported;
        }

        static /* synthetic */ boolean access$200(Builder r1) {
            return r1.isGaplessSupported;
        }

        static /* synthetic */ boolean access$300(Builder r1) {
            return r1.isSpeedChangeSupported;
        }

        public AudioOffloadSupport build() {
            if (this.isFormatSupported == true) goto L12;
            if (this.isGaplessSupported == true) goto L10;
            if (this.isSpeedChangeSupported == false) goto L12;
        L10:
            throw new IllegalStateException("Secondary offload attribute fields are true but primary isFormatSupported is false");
        L12:
            return new AudioOffloadSupport(this, null);
        }

        public Builder setIsFormatSupported(boolean r1) {
            this.isFormatSupported = r1;
            return this;
        }

        public Builder setIsGaplessSupported(boolean r1) {
            this.isGaplessSupported = r1;
            return this;
        }

        public Builder setIsSpeedChangeSupported(boolean r1) {
            this.isSpeedChangeSupported = r1;
            return this;
        }
    }

    static {
        DEFAULT_UNSUPPORTED = new Builder().build();
    }

    private AudioOffloadSupport(Builder r2) {
        this.isFormatSupported = Builder.access$100(r2);
        this.isGaplessSupported = Builder.access$200(r2);
        this.isSpeedChangeSupported = Builder.access$300(r2);
    }

    /* synthetic */ AudioOffloadSupport(Builder r1, C02551 r2) {
        this(r1);
    }

    public Builder buildUpon() {
        return new Builder(this);
    }

    public boolean equals(Object r6) {
        if (this != r6) goto L6;
        return true;
    L6:
        if (r6 != null) goto L8;
    L19:
        return false;
    L8:
        if (getClass() != r6.getClass()) goto L19;
        AudioOffloadSupport r2 = (AudioOffloadSupport) r6;
        if (this.isFormatSupported != r2.isFormatSupported) goto L18;
        if (this.isGaplessSupported != r2.isGaplessSupported) goto L18;
        if (this.isSpeedChangeSupported != r2.isSpeedChangeSupported) goto L18;
        return true;
    L18:
        return false;
    }

    public int hashCode() {
        return (((this.isFormatSupported ? 1 : 0) << 2) + ((this.isGaplessSupported ? 1 : 0) << 1)) + (this.isSpeedChangeSupported ? 1 : 0);
    }
}
