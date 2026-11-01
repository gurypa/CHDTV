package androidx.media3.exoplayer.audio;

import androidx.media3.common.util.Util;
import androidx.media3.extractor.AacUtil;
import androidx.media3.extractor.Ac3Util;
import androidx.media3.extractor.Ac4Util;
import androidx.media3.extractor.DtsUtil;
import androidx.media3.extractor.MpegAudioUtil;
import androidx.media3.extractor.OpusUtil;
import com.google.common.math.IntMath;
import com.google.common.primitives.Ints;
import java.math.RoundingMode;

/* JADX INFO: loaded from: classes.dex */
public class DefaultAudioTrackBufferSizeProvider implements DefaultAudioSink.AudioTrackBufferSizeProvider {
    private static final int AC3_BUFFER_MULTIPLICATION_FACTOR = 2;
    private static final int DTSHD_BUFFER_MULTIPLICATION_FACTOR = 4;
    private static final int MAX_PCM_BUFFER_DURATION_US = 750000;
    private static final int MIN_PCM_BUFFER_DURATION_US = 250000;
    private static final int OFFLOAD_BUFFER_DURATION_US = 50000000;
    private static final int PASSTHROUGH_BUFFER_DURATION_US = 250000;
    private static final int PCM_BUFFER_MULTIPLICATION_FACTOR = 4;
    public final int ac3BufferMultiplicationFactor;
    public final int dtshdBufferMultiplicationFactor;
    protected final int maxPcmBufferDurationUs;
    protected final int minPcmBufferDurationUs;
    protected final int offloadBufferDurationUs;
    protected final int passthroughBufferDurationUs;
    protected final int pcmBufferMultiplicationFactor;

    public static class Builder {
        private int ac3BufferMultiplicationFactor;
        private int dtshdBufferMultiplicationFactor;
        private int maxPcmBufferDurationUs;
        private int minPcmBufferDurationUs;
        private int offloadBufferDurationUs;
        private int passthroughBufferDurationUs;
        private int pcmBufferMultiplicationFactor;

        public Builder() {
            this.minPcmBufferDurationUs = 250000;
            this.maxPcmBufferDurationUs = DefaultAudioTrackBufferSizeProvider.MAX_PCM_BUFFER_DURATION_US;
            this.pcmBufferMultiplicationFactor = 4;
            this.passthroughBufferDurationUs = 250000;
            this.offloadBufferDurationUs = DefaultAudioTrackBufferSizeProvider.OFFLOAD_BUFFER_DURATION_US;
            this.ac3BufferMultiplicationFactor = 2;
            this.dtshdBufferMultiplicationFactor = 4;
        }

        static /* synthetic */ int access$000(Builder r1) {
            return r1.minPcmBufferDurationUs;
        }

        static /* synthetic */ int access$100(Builder r1) {
            return r1.maxPcmBufferDurationUs;
        }

        static /* synthetic */ int access$200(Builder r1) {
            return r1.pcmBufferMultiplicationFactor;
        }

        static /* synthetic */ int access$300(Builder r1) {
            return r1.passthroughBufferDurationUs;
        }

        static /* synthetic */ int access$400(Builder r1) {
            return r1.offloadBufferDurationUs;
        }

        static /* synthetic */ int access$500(Builder r1) {
            return r1.ac3BufferMultiplicationFactor;
        }

        static /* synthetic */ int access$600(Builder r1) {
            return r1.dtshdBufferMultiplicationFactor;
        }

        public DefaultAudioTrackBufferSizeProvider build() {
            return new DefaultAudioTrackBufferSizeProvider(this);
        }

        public Builder setAc3BufferMultiplicationFactor(int r1) {
            this.ac3BufferMultiplicationFactor = r1;
            return this;
        }

        public Builder setDtshdBufferMultiplicationFactor(int r1) {
            this.dtshdBufferMultiplicationFactor = r1;
            return this;
        }

        public Builder setMaxPcmBufferDurationUs(int r1) {
            this.maxPcmBufferDurationUs = r1;
            return this;
        }

        public Builder setMinPcmBufferDurationUs(int r1) {
            this.minPcmBufferDurationUs = r1;
            return this;
        }

        public Builder setOffloadBufferDurationUs(int r1) {
            this.offloadBufferDurationUs = r1;
            return this;
        }

        public Builder setPassthroughBufferDurationUs(int r1) {
            this.passthroughBufferDurationUs = r1;
            return this;
        }

        public Builder setPcmBufferMultiplicationFactor(int r1) {
            this.pcmBufferMultiplicationFactor = r1;
            return this;
        }
    }

    protected DefaultAudioTrackBufferSizeProvider(Builder r2) {
        this.minPcmBufferDurationUs = Builder.access$000(r2);
        this.maxPcmBufferDurationUs = Builder.access$100(r2);
        this.pcmBufferMultiplicationFactor = Builder.access$200(r2);
        this.passthroughBufferDurationUs = Builder.access$300(r2);
        this.offloadBufferDurationUs = Builder.access$400(r2);
        this.ac3BufferMultiplicationFactor = Builder.access$500(r2);
        this.dtshdBufferMultiplicationFactor = Builder.access$600(r2);
    }

    protected static int durationUsToBytes(int r4, int r5, int r6) {
        return Ints.checkedCast(((((long) r4) * ((long) r5)) * ((long) r6)) / 1000000);
    }

    protected static int getMaximumEncodedRateBytesPerSecond(int r1) {
        switch(r1) {
            case 5: goto L29;
            case 6: goto L27;
            case 7: goto L25;
            case 8: goto L23;
            case 9: goto L21;
            case 10: goto L19;
            case 11: goto L17;
            case 12: goto L15;
            case 13: goto L4;
            case 14: goto L13;
            case 15: goto L11;
            case 16: goto L9;
            case 17: goto L7;
            case 18: goto L27;
            case 19: goto L4;
            case 20: goto L5;
            case 21: goto L4;
            case 22: goto L4;
            case 23: goto L4;
            case 24: goto L4;
            case 25: goto L4;
            case 26: goto L4;
            case 27: goto L4;
            case 28: goto L4;
            case 29: goto L4;
            case 30: goto L23;
            default: goto L4;
        };
    L5:
        return OpusUtil.MAX_BYTES_PER_SECOND;
    L7:
        return Ac4Util.MAX_RATE_BYTES_PER_SECOND;
    L9:
        return AacUtil.AAC_XHE_MAX_RATE_BYTES_PER_SECOND;
    L11:
        return 8000;
    L13:
        return Ac3Util.TRUEHD_MAX_RATE_BYTES_PER_SECOND;
    L15:
        return 7000;
    L17:
        return AacUtil.AAC_HE_V1_MAX_RATE_BYTES_PER_SECOND;
    L19:
        return AacUtil.AAC_LC_MAX_RATE_BYTES_PER_SECOND;
    L21:
        return MpegAudioUtil.MAX_RATE_BYTES_PER_SECOND;
    L23:
        return DtsUtil.DTS_HD_MAX_RATE_BYTES_PER_SECOND;
    L25:
        return DtsUtil.DTS_MAX_RATE_BYTES_PER_SECOND;
    L27:
        return 768000;
    L29:
        return Ac3Util.AC3_MAX_RATE_BYTES_PER_SECOND;
    L4:
        throw new IllegalArgumentException();
    }

    protected int get1xBufferSizeInBytes(int r2, int r3, int r4, int r5, int r6, int r7) {
        switch(r4) {
            case 0: goto L10;
            case 1: goto L8;
            case 2: goto L6;
            default: goto L4;
        };
    L4:
        throw new IllegalArgumentException();
    L6:
        return getPassthroughBufferSizeInBytes(r3, r7);
    L8:
        return getOffloadBufferSizeInBytes(r3);
    L10:
        return getPcmBufferSizeInBytes(r2, r6, r5);
    }

    @Override // androidx.media3.exoplayer.audio.DefaultAudioSink.AudioTrackBufferSizeProvider
    public int getBufferSizeInBytes(int r4, int r5, int r6, int r7, int r8, int r9, double r10) {
        return (((Math.max(r4, (int) (((double) get1xBufferSizeInBytes(r4, r5, r6, r7, r8, r9)) * r10)) + r7) - 1) / r7) * r7;
    }

    protected int getOffloadBufferSizeInBytes(int r6) {
        int r0 = getMaximumEncodedRateBytesPerSecond(r6);
        return Ints.checkedCast((((long) this.offloadBufferDurationUs) * ((long) r0)) / 1000000);
    }

    protected int getPassthroughBufferSizeInBytes(int r7, int r8) {
        int r0 = this.passthroughBufferDurationUs;
        if (r7 != 5) goto L5;
        r0 = r0 * this.ac3BufferMultiplicationFactor;
    L8:
        if (r8 == (-1)) goto L10;
        int r1 = IntMath.divide(r8, 8, RoundingMode.CEILING);
    L12:
        return Ints.checkedCast((((long) r0) * ((long) r1)) / 1000000);
    L10:
        r1 = getMaximumEncodedRateBytesPerSecond(r7);
        goto L12
    L5:
        if (r7 != 8) goto L8;
        r0 = r0 * this.dtshdBufferMultiplicationFactor;
        goto L8
    }

    protected int getPcmBufferSizeInBytes(int r5, int r6, int r7) {
        return Util.constrainValue(this.pcmBufferMultiplicationFactor * r5, durationUsToBytes(this.minPcmBufferDurationUs, r6, r7), durationUsToBytes(this.maxPcmBufferDurationUs, r6, r7));
    }
}
