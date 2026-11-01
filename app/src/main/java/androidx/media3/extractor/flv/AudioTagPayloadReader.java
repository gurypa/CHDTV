package androidx.media3.extractor.flv;

import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.ParserException;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.extractor.AacUtil;
import androidx.media3.extractor.TrackOutput;
import java.util.Collections;

/* JADX INFO: loaded from: classes.dex */
final class AudioTagPayloadReader extends TagPayloadReader {
    private static final int AAC_PACKET_TYPE_AAC_RAW = 1;
    private static final int AAC_PACKET_TYPE_SEQUENCE_HEADER = 0;
    private static final int AUDIO_FORMAT_AAC = 10;
    private static final int AUDIO_FORMAT_ALAW = 7;
    private static final int AUDIO_FORMAT_MP3 = 2;
    private static final int AUDIO_FORMAT_ULAW = 8;
    private static final int[] AUDIO_SAMPLING_RATE_TABLE = null;
    private int audioFormat;
    private boolean hasOutputFormat;
    private boolean hasParsedAudioDataHeader;

    static {
        AUDIO_SAMPLING_RATE_TABLE = new int[]{5512, 11025, 22050, 44100};
    }

    public AudioTagPayloadReader(TrackOutput r1) {
        super(r1);
    }

    @Override // androidx.media3.extractor.flv.TagPayloadReader
    protected boolean parseHeader(ParsableByteArray r7) throws TagPayloadReader.UnsupportedFormatException {
        if (this.hasParsedAudioDataHeader == true) goto L23;
        int r0 = r7.readUnsignedByte();
        this.audioFormat = (r0 >> 4) & 15;
        if (this.audioFormat != 2) goto L8;
        this.output.format(new Format.Builder().setSampleMimeType(MimeTypes.AUDIO_MPEG).setChannelCount(1).setSampleRate(AUDIO_SAMPLING_RATE_TABLE[(r0 >> 2) & 3]).build());
        this.hasOutputFormat = true;
    L22:
        this.hasParsedAudioDataHeader = true;
    L24:
        return true;
    L8:
        if (this.audioFormat == 7) goto L18;
        if (this.audioFormat == 8) goto L18;
        if (this.audioFormat == 10) goto L22;
        throw new TagPayloadReader.UnsupportedFormatException("Audio format not supported: " + this.audioFormat);
    L18:
        if (this.audioFormat != 7) goto L20;
        String r2 = MimeTypes.AUDIO_ALAW;
    L21:
        this.output.format(new Format.Builder().setSampleMimeType(r2).setChannelCount(1).setSampleRate(8000).build());
        this.hasOutputFormat = true;
        goto L22
    L20:
        r2 = MimeTypes.AUDIO_MLAW;
        goto L21
    L23:
        r7.skipBytes(1);
        goto L24
    }

    @Override // androidx.media3.extractor.flv.TagPayloadReader
    protected boolean parsePayload(ParsableByteArray r20, long r21) throws ParserException {
        if (this.audioFormat != 2) goto L6;
        int r9 = r20.bytesLeft();
        this.output.sampleData(r20, r9);
        this.output.sampleMetadata(r21, 1, r9, 0, null);
        return true;
    L6:
        int r2 = r20.readUnsignedByte();
        if (r2 != 0) goto L13;
        if (this.hasOutputFormat == true) goto L13;
        byte[] r5 = new byte[r20.bytesLeft()];
        r20.readBytes(r5, 0, r5.length);
        AacUtil.Config r6 = AacUtil.parseAudioSpecificConfig(r5);
        Format r7 = new Format.Builder().setSampleMimeType(MimeTypes.AUDIO_AAC).setCodecs(r6.codecs).setChannelCount(r6.channelCount).setSampleRate(r6.sampleRateHz).setInitializationData(Collections.singletonList(r5)).build();
        this.output.format(r7);
        this.hasOutputFormat = true;
        return false;
    L13:
        if (this.audioFormat != 10) goto L17;
        if (r2 == 1) goto L17;
        return false;
    L17:
        int r3 = r20.bytesLeft();
        this.output.sampleData(r20, r3);
        this.output.sampleMetadata(r21, 1, r3, 0, null);
        return true;
    }

    @Override // androidx.media3.extractor.flv.TagPayloadReader
    public void seek() {
    }
}
