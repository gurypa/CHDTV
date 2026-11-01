package androidx.media3.extractor.ogg;

import androidx.media3.common.Format;
import androidx.media3.common.Metadata;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.extractor.VorbisUtil;
import com.google.common.collect.ImmutableList;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import org.checkerframework.checker.nullness.qual.EnsuresNonNullIf;

/* JADX INFO: loaded from: classes.dex */
final class VorbisReader extends StreamReader {
    private VorbisUtil.CommentHeader commentHeader;
    private int previousPacketBlockSize;
    private boolean seenFirstAudioPacket;
    private VorbisUtil.VorbisIdHeader vorbisIdHeader;
    private VorbisSetup vorbisSetup;

    static final class VorbisSetup {
        public final VorbisUtil.CommentHeader commentHeader;
        public final int iLogModes;
        public final VorbisUtil.VorbisIdHeader idHeader;
        public final VorbisUtil.Mode[] modes;
        public final byte[] setupHeaderData;

        public VorbisSetup(VorbisUtil.VorbisIdHeader r1, VorbisUtil.CommentHeader r2, byte[] r3, VorbisUtil.Mode[] r4, int r5) {
            this.idHeader = r1;
            this.commentHeader = r2;
            this.setupHeaderData = r3;
            this.modes = r4;
            this.iLogModes = r5;
        }
    }

    VorbisReader() {
    }

    static void appendNumberOfSamples(ParsableByteArray r6, long r7) {
        if (r6.capacity() >= (r6.limit() + 4)) goto L5;
        r6.reset(Arrays.copyOf(r6.getData(), r6.limit() + 4));
    L6:
        byte[] r0 = r6.getData();
        r0[r6.limit() - 4] = (byte) (r7 & 255);
        r0[r6.limit() - 3] = (byte) ((r7 >>> 8) & 255);
        r0[r6.limit() - 2] = (byte) ((r7 >>> 16) & 255);
        r0[r6.limit() - 1] = (byte) (255 & (r7 >>> 24));
        return;
    L5:
        r6.setLimit(r6.limit() + 4);
        goto L6
    }

    private static int decodeBlockSize(byte r2, VorbisSetup r3) {
        int r0 = readBits(r2, r3.iLogModes, 1);
        if (r3.modes[r0].blockFlag == true) goto L6;
        return r3.idHeader.blockSize0;
    L6:
        return r3.idHeader.blockSize1;
    }

    static int readBits(byte r3, int r4, int r5) {
        return (r3 >> r5) & (255 >>> (8 - r4));
    }

    public static boolean verifyBitstreamType(ParsableByteArray r2) {
        return VorbisUtil.verifyVorbisHeaderCapturePattern(1, r2, true);
    L6:
        return false;
    }

    @Override // androidx.media3.extractor.ogg.StreamReader
    protected void onSeekEnd(long r3) {
        super.onSeekEnd(r3);
        int r1 = 0;
        if (r3 == 0) goto L5;
        boolean r0 = true;
    L6:
        this.seenFirstAudioPacket = r0;
        if (this.vorbisIdHeader == null) goto L9;
        r1 = this.vorbisIdHeader.blockSize0;
    L9:
        this.previousPacketBlockSize = r1;
        return;
    L5:
        r0 = false;
        goto L6
    }

    @Override // androidx.media3.extractor.ogg.StreamReader
    protected long preparePayload(ParsableByteArray r6) {
        int r1 = 0;
        if ((r6.getData()[0] & 1) != 1) goto L6;
        return -1;
    L6:
        int r0 = decodeBlockSize(r6.getData()[0], (VorbisSetup) Assertions.checkStateNotNull(this.vorbisSetup));
        if (this.seenFirstAudioPacket == false) goto L9;
        r1 = (this.previousPacketBlockSize + r0) / 4;
    L9:
        appendNumberOfSamples(r6, r1);
        this.seenFirstAudioPacket = true;
        this.previousPacketBlockSize = r0;
        return r1;
    }

    @Override // androidx.media3.extractor.ogg.StreamReader
    @EnsuresNonNullIf(expression = {"#3.format"}, result = false)
    protected boolean readHeaders(ParsableByteArray r8, long r9, StreamReader.SetupData r11) throws IOException {
        if (this.vorbisSetup == null) goto L6;
        Assertions.checkNotNull(r11.format);
        return false;
    L6:
        this.vorbisSetup = readSetupHeaders(r8);
        if (this.vorbisSetup != null) goto L9;
        return true;
    L9:
        VorbisSetup r0 = this.vorbisSetup;
        VorbisUtil.VorbisIdHeader r2 = r0.idHeader;
        ArrayList r3 = new ArrayList();
        r3.add(r2.data);
        r3.add(r0.setupHeaderData);
        Metadata r4 = VorbisUtil.parseVorbisComments(ImmutableList.copyOf(r0.commentHeader.comments));
        r11.format = new Format.Builder().setSampleMimeType(MimeTypes.AUDIO_VORBIS).setAverageBitrate(r2.bitrateNominal).setPeakBitrate(r2.bitrateMaximum).setChannelCount(r2.channels).setSampleRate(r2.sampleRate).setInitializationData(r3).setMetadata(r4).build();
        return true;
    }

    VorbisSetup readSetupHeaders(ParsableByteArray r9) throws IOException {
        if (this.vorbisIdHeader != null) goto L7;
        this.vorbisIdHeader = VorbisUtil.readVorbisIdentificationHeader(r9);
        return null;
    L7:
        if (this.commentHeader != null) goto L10;
        this.commentHeader = VorbisUtil.readVorbisCommentHeader(r9);
        return null;
    L10:
        VorbisUtil.VorbisIdHeader r3 = this.vorbisIdHeader;
        VorbisUtil.CommentHeader r4 = this.commentHeader;
        byte[] r5 = new byte[r9.limit()];
        System.arraycopy(r9.getData(), 0, r5, 0, r9.limit());
        VorbisUtil.Mode[] r6 = VorbisUtil.readVorbisModes(r9, r3.channels);
        return new VorbisSetup(r3, r4, r5, r6, VorbisUtil.iLog(r6.length - 1));
    }

    @Override // androidx.media3.extractor.ogg.StreamReader
    protected void reset(boolean r2) {
        super.reset(r2);
        if (r2 == false) goto L5;
        this.vorbisSetup = null;
        this.vorbisIdHeader = null;
        this.commentHeader = null;
    L5:
        this.previousPacketBlockSize = 0;
        this.seenFirstAudioPacket = false;
    }
}
