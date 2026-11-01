package androidx.media3.exoplayer.audio;

import androidx.media3.common.audio.AudioProcessor;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.Util;
import androidx.media3.extractor.WavUtil;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* JADX INFO: loaded from: classes.dex */
public final class TeeAudioProcessor extends androidx.media3.common.audio.BaseAudioProcessor {
    private final AudioBufferSink audioBufferSink;

    public interface AudioBufferSink {
        void flush(int r1, int r2, int r3);

        void handleBuffer(ByteBuffer r1);
    }

    public static final class WavFileAudioBufferSink implements AudioBufferSink {
        private static final int FILE_SIZE_MINUS_44_OFFSET = 40;
        private static final int FILE_SIZE_MINUS_8_OFFSET = 4;
        private static final int HEADER_LENGTH = 44;
        private static final String TAG = "WaveFileAudioBufferSink";
        private int bytesWritten;
        private int channelCount;
        private int counter;
        private int encoding;
        private final String outputFileNamePrefix;
        private RandomAccessFile randomAccessFile;
        private int sampleRateHz;
        private final byte[] scratchBuffer;
        private final ByteBuffer scratchByteBuffer;

        public WavFileAudioBufferSink(String r3) {
            this.outputFileNamePrefix = r3;
            this.scratchBuffer = new byte[1024];
            this.scratchByteBuffer = ByteBuffer.wrap(this.scratchBuffer).order(ByteOrder.LITTLE_ENDIAN);
        }

        private String getNextOutputFileName() {
            String r0 = this.outputFileNamePrefix;
            int r1 = this.counter;
            this.counter = r1 + 1;
            return Util.formatInvariant("%s-%04d.wav", new Object[]{r0, Integer.valueOf(r1)});
        }

        private void maybePrepareFile() throws IOException {
            if (this.randomAccessFile == null) goto L5;
            return;
        L5:
            RandomAccessFile r0 = new RandomAccessFile(getNextOutputFileName(), "rw");
            writeFileHeader(r0);
            this.randomAccessFile = r0;
            this.bytesWritten = 44;
        }

        private void reset() throws IOException {
            RandomAccessFile r0 = this.randomAccessFile;
            if (r0 != null) goto L16;
            return;
        L16:
            this.scratchByteBuffer.clear();     // Catch: IOException -> L7
            this.scratchByteBuffer.putInt(this.bytesWritten - 8);     // Catch: IOException -> L7
            r0.seek(4);     // Catch: IOException -> L7
            r0.write(this.scratchBuffer, 0, 4);     // Catch: IOException -> L7
            this.scratchByteBuffer.clear();     // Catch: IOException -> L7
            this.scratchByteBuffer.putInt(this.bytesWritten - 44);     // Catch: IOException -> L7
            r0.seek(40);     // Catch: IOException -> L7
            r0.write(this.scratchBuffer, 0, 4);     // Catch: IOException -> L7
        L18:
            r0.close();     // Catch: Throwable -> L13
            this.randomAccessFile = null;
            return;
        L13:
            th = move-exception;
            this.randomAccessFile = null;
            throw th;
        L7:
            e = move-exception;
            Log.m49w(TAG, "Error updating file size", e);
            goto L18
        }

        private void writeBuffer(ByteBuffer r5) throws IOException {
            RandomAccessFile r0 = (RandomAccessFile) Assertions.checkNotNull(this.randomAccessFile);
        L4:
            if (r5.hasRemaining() == false) goto L6;
            int r1 = Math.min(r5.remaining(), this.scratchBuffer.length);
            r5.get(this.scratchBuffer, 0, r1);
            r0.write(this.scratchBuffer, 0, r1);
            this.bytesWritten += r1;
            goto L4
        }

        private void writeFileHeader(RandomAccessFile r6) throws IOException {
            r6.writeInt(WavUtil.RIFF_FOURCC);
            r6.writeInt(-1);
            r6.writeInt(WavUtil.WAVE_FOURCC);
            r6.writeInt(WavUtil.FMT_FOURCC);
            this.scratchByteBuffer.clear();
            this.scratchByteBuffer.putInt(16);
            this.scratchByteBuffer.putShort((short) WavUtil.getTypeForPcmEncoding(this.encoding));
            this.scratchByteBuffer.putShort((short) this.channelCount);
            this.scratchByteBuffer.putInt(this.sampleRateHz);
            int r1 = Util.getPcmFrameSize(this.encoding, this.channelCount);
            this.scratchByteBuffer.putInt(this.sampleRateHz * r1);
            this.scratchByteBuffer.putShort((short) r1);
            this.scratchByteBuffer.putShort((short) ((r1 * 8) / this.channelCount));
            r6.write(this.scratchBuffer, 0, this.scratchByteBuffer.position());
            r6.writeInt(1684108385);
            r6.writeInt(-1);
        }

        @Override // androidx.media3.exoplayer.audio.TeeAudioProcessor.AudioBufferSink
        public void flush(int r4, int r5, int r6) {
            reset();     // Catch: IOException -> L4
        L6:
            this.sampleRateHz = r4;
            this.channelCount = r5;
            this.encoding = r6;
            return;
        L4:
            e = move-exception;
            Log.m45e(TAG, "Error resetting", e);
            goto L6
        }

        @Override // androidx.media3.exoplayer.audio.TeeAudioProcessor.AudioBufferSink
        public void handleBuffer(ByteBuffer r4) {
            maybePrepareFile();     // Catch: IOException -> L4
            writeBuffer(r4);     // Catch: IOException -> L4
            return;
        L4:
            e = move-exception;
            Log.m45e(TAG, "Error writing data", e);
        }
    }

    public TeeAudioProcessor(AudioBufferSink r2) {
        this.audioBufferSink = (AudioBufferSink) Assertions.checkNotNull(r2);
    }

    private void flushSinkIfActive() {
        if (isActive() == false) goto L6;
        this.audioBufferSink.flush(this.inputAudioFormat.sampleRate, this.inputAudioFormat.channelCount, this.inputAudioFormat.encoding);
        return;
    }

    @Override // androidx.media3.common.audio.BaseAudioProcessor
    public AudioProcessor.AudioFormat onConfigure(AudioProcessor.AudioFormat r1) {
        return r1;
    }

    @Override // androidx.media3.common.audio.BaseAudioProcessor
    protected void onFlush() {
        flushSinkIfActive();
    }

    @Override // androidx.media3.common.audio.BaseAudioProcessor
    protected void onQueueEndOfStream() {
        flushSinkIfActive();
    }

    @Override // androidx.media3.common.audio.BaseAudioProcessor
    protected void onReset() {
        flushSinkIfActive();
    }

    @Override // androidx.media3.common.audio.AudioProcessor
    public void queueInput(ByteBuffer r4) {
        int r0 = r4.remaining();
        if (r0 != 0) goto L5;
        return;
    L5:
        this.audioBufferSink.handleBuffer(Util.createReadOnlyByteBuffer(r4));
        replaceOutputBuffer(r0).put(r4).flip();
    }
}
