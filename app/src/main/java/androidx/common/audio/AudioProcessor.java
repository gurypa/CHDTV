package androidx.media3.common.audio;

import androidx.media3.common.Format;
import androidx.media3.common.util.Util;
import com.google.common.base.Objects;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* JADX INFO: loaded from: classes.dex */
public interface AudioProcessor {
    public static final ByteBuffer EMPTY_BUFFER = null;

    /* JADX INFO: renamed from: androidx.media3.common.audio.AudioProcessor$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static long $default$getDurationAfterProcessorApplied(AudioProcessor r0, long r1) {
            return r1;
        }
    }

    public static final class AudioFormat {
        public static final AudioFormat NOT_SET = null;
        public final int bytesPerFrame;
        public final int channelCount;
        public final int encoding;
        public final int sampleRate;

        static {
            NOT_SET = new AudioFormat(-1, -1, -1);
        }

        public AudioFormat(int r2, int r3, int r4) {
            this.sampleRate = r2;
            this.channelCount = r3;
            this.encoding = r4;
            if (Util.isEncodingLinearPcm(r4) == false) goto L5;
            int r0 = Util.getPcmFrameSize(r4, r3);
        L6:
            this.bytesPerFrame = r0;
            return;
        L5:
            r0 = -1;
            goto L6
        }

        public AudioFormat(Format r4) {
            this(r4.sampleRate, r4.channelCount, r4.pcmEncoding);
        }

        public boolean equals(Object r6) {
            if (this != r6) goto L6;
            return true;
        L6:
            if ((r6 instanceof AudioFormat) == true) goto L8;
            return false;
        L8:
            AudioFormat r1 = (AudioFormat) r6;
            if (this.sampleRate != r1.sampleRate) goto L16;
            if (this.channelCount != r1.channelCount) goto L16;
            if (this.encoding != r1.encoding) goto L16;
            return true;
        L16:
            return false;
        }

        public int hashCode() {
            return Objects.hashCode(new Object[]{Integer.valueOf(this.sampleRate), Integer.valueOf(this.channelCount), Integer.valueOf(this.encoding)});
        }

        public String toString() {
            return "AudioFormat[sampleRate=" + this.sampleRate + ", channelCount=" + this.channelCount + ", encoding=" + this.encoding + ']';
        }
    }

    public static final class UnhandledAudioFormatException extends Exception {
        public final AudioFormat inputAudioFormat;

        public UnhandledAudioFormatException(AudioFormat r2) {
            this("Unhandled input format:", r2);
        }

        public UnhandledAudioFormatException(String r3, AudioFormat r4) {
            super(r3 + " " + r4);
            this.inputAudioFormat = r4;
        }
    }

    static {
        EMPTY_BUFFER = ByteBuffer.allocateDirect(0).order(ByteOrder.nativeOrder());
    }

    AudioFormat configure(AudioFormat r1) throws UnhandledAudioFormatException;

    void flush();

    long getDurationAfterProcessorApplied(long r1);

    ByteBuffer getOutput();

    boolean isActive();

    boolean isEnded();

    void queueEndOfStream();

    void queueInput(ByteBuffer r1);

    void reset();
}
