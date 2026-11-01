package androidx.media3.exoplayer.audio;

import androidx.media3.common.audio.AudioProcessor;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import androidx.media3.decoder.DecoderInputBuffer;
import androidx.media3.extractor.OpusUtil;
import com.google.common.base.Ascii;
import com.google.common.primitives.UnsignedBytes;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class OggOpusAudioPacketizer {
    private static final int CHECKSUM_INDEX = 22;
    private static final int FIRST_AUDIO_SAMPLE_PAGE_SEQUENCE_NUMBER = 2;
    private static final byte[] OGG_DEFAULT_COMMENT_HEADER_PAGE = null;
    private static final byte[] OGG_DEFAULT_ID_HEADER_PAGE = null;
    private static final int OGG_PACKET_HEADER_LENGTH = 28;
    private static final int SERIAL_NUMBER = 0;
    private int granulePosition;
    private ByteBuffer outputBuffer;
    private int pageSequenceNumber;

    static {
        OGG_DEFAULT_ID_HEADER_PAGE = new byte[]{79, 103, 103, 83, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, Ascii.f82FS, -43, -59, -9, 1, 19, 79, 112, 117, 115, 72, 101, 97, 100, 1, 2, 56, 1, -128, -69, 0, 0, 0, 0, 0};
        OGG_DEFAULT_COMMENT_HEADER_PAGE = new byte[]{79, 103, 103, 83, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, Ascii.f92VT, -103, 87, 83, 1, Ascii.DLE, 79, 112, 117, 115, 84, 97, 103, 115, 0, 0, 0, 0, 0, 0, 0, 0};
    }

    public OggOpusAudioPacketizer() {
        this.outputBuffer = AudioProcessor.EMPTY_BUFFER;
        this.granulePosition = 0;
        this.pageSequenceNumber = 2;
    }

    private ByteBuffer packetizeInternal(ByteBuffer r20, byte[] r21) {
        int r9 = r20.position();
        int r10 = r20.limit();
        int r11 = r10 - r9;
        int r5 = (r11 + 255) / 255;
        int r1 = (r5 + 27) + r11;
        if (this.pageSequenceNumber != 2) goto L8;
        if (r21 == null) goto L6;
        int r3 = r21.length + 28;
    L7:
        int r2 = r3;
        int r15 = r1 + (OGG_DEFAULT_COMMENT_HEADER_PAGE.length + r2);
        int r16 = r2;
    L9:
        ByteBuffer r4 = replaceOutputBuffer(r15);
        if (this.pageSequenceNumber != 2) goto L15;
        if (r21 == null) goto L13;
        writeOggIdHeaderPage(r4, r21);
    L14:
        r4.put(OGG_DEFAULT_COMMENT_HEADER_PAGE);
        goto L15
    L13:
        r4.put(OGG_DEFAULT_ID_HEADER_PAGE);
    L15:
        this.granulePosition += OpusUtil.parsePacketAudioSampleCount(r20);
        writeOggPacketHeader(r4, this.granulePosition, this.pageSequenceNumber, r5, false);
        int r6 = r11;
        int r7 = 0;
    L16:
        if (r7 >= r5) goto L21;
        if (r6 < 255) goto L19;
        r4.put((byte) -1);
        r6 = r6 - 255;
    L20:
        r7 = r7 + 1;
        goto L16
    L19:
        r4.put((byte) r6);
        r6 = 0;
        goto L20
    L21:
        int r8 = r9;
    L22:
        if (r8 >= r10) goto L24;
        r4.put(r20.get(r8));
        r8 = r8 + 1;
        goto L22
    L24:
        r20.position(r20.limit());
        r4.flip();
        if (this.pageSequenceNumber != 2) goto L27;
        r4.putInt((r16 + OGG_DEFAULT_COMMENT_HEADER_PAGE.length) + 22, Util.crc32(r4.array(), (r4.arrayOffset() + r16) + OGG_DEFAULT_COMMENT_HEADER_PAGE.length, r4.limit() - r4.position(), 0));
    L28:
        this.pageSequenceNumber++;
        return r4;
    L27:
        r4.putInt(22, Util.crc32(r4.array(), r4.arrayOffset(), r4.limit() - r4.position(), 0));
        goto L28
    L6:
        r3 = OGG_DEFAULT_ID_HEADER_PAGE.length;
        goto L7
    L8:
        r15 = r1;
        r16 = 0;
        goto L9
    }

    private ByteBuffer replaceOutputBuffer(int r3) {
        if (this.outputBuffer.capacity() >= r3) goto L5;
        this.outputBuffer = ByteBuffer.allocate(r3).order(ByteOrder.LITTLE_ENDIAN);
    L7:
        return this.outputBuffer;
    L5:
        this.outputBuffer.clear();
        goto L7
    }

    private void writeOggIdHeaderPage(ByteBuffer r8, byte[] r9) {
        writeOggPacketHeader(r8, 0, 0, 1, true);
        r8.put(UnsignedBytes.checkedCast(r9.length));
        r8.put(r9);
        r8.putInt(22, Util.crc32(r8.array(), r8.arrayOffset(), r9.length + 28, 0));
        r8.position(r9.length + 28);
    }

    private void writeOggPacketHeader(ByteBuffer r3, long r4, int r6, int r7, boolean r8) {
        r3.put((byte) 79);
        r3.put((byte) 103);
        r3.put((byte) 103);
        r3.put((byte) 83);
        r3.put((byte) 0);
        if (r8 == false) goto L5;
        byte r1 = 2;
    L6:
        r3.put(r1);
        r3.putLong(r4);
        r3.putInt(0);
        r3.putInt(r6);
        r3.putInt(0);
        r3.put(UnsignedBytes.checkedCast(r7));
        return;
    L5:
        r1 = 0;
        goto L6
    }

    public void packetize(DecoderInputBuffer r4, List<byte[]> r5) {
        Assertions.checkNotNull(r4.data);
        if ((r4.data.limit() - r4.data.position()) != 0) goto L6;
        return;
    L6:
        if (this.pageSequenceNumber == 2) goto L8;
    L12:
        byte[] r0 = null;
    L13:
        this.outputBuffer = packetizeInternal(r4.data, r0);
        r4.clear();
        r4.ensureSpaceForWrite(this.outputBuffer.remaining());
        r4.data.put(this.outputBuffer);
        r4.flip();
        return;
    L8:
        if (r5.size() != 1) goto L10;
    L11:
        r0 = r5.get(0);
        goto L13
    L10:
        if (r5.size() != 3) goto L12;
        goto L11
    }

    public void reset() {
        this.outputBuffer = AudioProcessor.EMPTY_BUFFER;
        this.granulePosition = 0;
        this.pageSequenceNumber = 2;
    }
}
