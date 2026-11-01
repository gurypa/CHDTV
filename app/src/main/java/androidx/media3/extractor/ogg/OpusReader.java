package androidx.media3.extractor.ogg;

import androidx.media3.common.Format;
import androidx.media3.common.Metadata;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.ParserException;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.extractor.OpusUtil;
import androidx.media3.extractor.VorbisUtil;
import com.google.common.collect.ImmutableList;
import java.util.Arrays;
import java.util.List;
import org.checkerframework.checker.nullness.qual.EnsuresNonNullIf;

/* JADX INFO: loaded from: classes.dex */
final class OpusReader extends StreamReader {
    private static final byte[] OPUS_COMMENT_HEADER_SIGNATURE = null;
    private static final byte[] OPUS_ID_HEADER_SIGNATURE = null;
    private boolean firstCommentHeaderSeen;

    static {
        OPUS_ID_HEADER_SIGNATURE = new byte[]{79, 112, 117, 115, 72, 101, 97, 100};
        OPUS_COMMENT_HEADER_SIGNATURE = new byte[]{79, 112, 117, 115, 84, 97, 103, 115};
    }

    OpusReader() {
    }

    private static boolean peekPacketStartsWith(ParsableByteArray r4, byte[] r5) {
        if (r4.bytesLeft() >= r5.length) goto L5;
        return false;
    L5:
        int r0 = r4.getPosition();
        byte[] r1 = new byte[r5.length];
        r4.readBytes(r1, 0, r5.length);
        r4.setPosition(r0);
        return Arrays.equals(r1, r5);
    }

    public static boolean verifyBitstreamType(ParsableByteArray r1) {
        return peekPacketStartsWith(r1, OPUS_ID_HEADER_SIGNATURE);
    }

    @Override // androidx.media3.extractor.ogg.StreamReader
    protected long preparePayload(ParsableByteArray r3) {
        return convertTimeToGranule(OpusUtil.getPacketDurationUs(r3.getData()));
    }

    @Override // androidx.media3.extractor.ogg.StreamReader
    @EnsuresNonNullIf(expression = {"#3.format"}, result = false)
    protected boolean readHeaders(ParsableByteArray r7, long r8, StreamReader.SetupData r10) throws ParserException {
        if (peekPacketStartsWith(r7, OPUS_ID_HEADER_SIGNATURE) == false) goto L10;
        byte[] r0 = Arrays.copyOf(r7.getData(), r7.limit());
        int r2 = OpusUtil.getChannelCount(r0);
        List<byte[]> r3 = OpusUtil.buildInitializationData(r0);
        if (r10.format == null) goto L7;
        return true;
    L7:
        r10.format = new Format.Builder().setSampleMimeType(MimeTypes.AUDIO_OPUS).setChannelCount(r2).setSampleRate(OpusUtil.SAMPLE_RATE).setInitializationData(r3).build();
        return true;
    L10:
        if (peekPacketStartsWith(r7, OPUS_COMMENT_HEADER_SIGNATURE) == false) goto L19;
        Assertions.checkStateNotNull(r10.format);
        if (this.firstCommentHeaderSeen == false) goto L14;
        return true;
    L14:
        this.firstCommentHeaderSeen = true;
        r7.skipBytes(OPUS_COMMENT_HEADER_SIGNATURE.length);
        Metadata r4 = VorbisUtil.parseVorbisComments(ImmutableList.copyOf(VorbisUtil.readVorbisCommentHeader(r7, false, false).comments));
        if (r4 != null) goto L17;
        return true;
    L17:
        r10.format = r10.format.buildUpon().setMetadata(r4.copyWithAppendedEntriesFrom(r10.format.metadata)).build();
        return true;
    L19:
        Assertions.checkStateNotNull(r10.format);
        return false;
    }

    @Override // androidx.media3.extractor.ogg.StreamReader
    protected void reset(boolean r2) {
        super.reset(r2);
        if (r2 == false) goto L6;
        this.firstCommentHeaderSeen = false;
        return;
    }
}
