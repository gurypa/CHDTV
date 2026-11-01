package androidx.media3.extractor.flv;

import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.ParserException;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.container.NalUnitUtil;
import androidx.media3.extractor.AvcConfig;
import androidx.media3.extractor.TrackOutput;

/* JADX INFO: loaded from: classes.dex */
final class VideoTagPayloadReader extends TagPayloadReader {
    private static final int AVC_PACKET_TYPE_AVC_NALU = 1;
    private static final int AVC_PACKET_TYPE_SEQUENCE_HEADER = 0;
    private static final int VIDEO_CODEC_AVC = 7;
    private static final int VIDEO_FRAME_KEYFRAME = 1;
    private static final int VIDEO_FRAME_VIDEO_INFO = 5;
    private int frameType;
    private boolean hasOutputFormat;
    private boolean hasOutputKeyframe;
    private final ParsableByteArray nalLength;
    private final ParsableByteArray nalStartCode;
    private int nalUnitLengthFieldLength;

    public VideoTagPayloadReader(TrackOutput r3) {
        super(r3);
        this.nalStartCode = new ParsableByteArray(NalUnitUtil.NAL_START_CODE);
        this.nalLength = new ParsableByteArray(4);
    }

    @Override // androidx.media3.extractor.flv.TagPayloadReader
    protected boolean parseHeader(ParsableByteArray r7) throws TagPayloadReader.UnsupportedFormatException {
        int r0 = r7.readUnsignedByte();
        int r1 = (r0 >> 4) & 15;
        int r2 = r0 & 15;
        if (r2 != 7) goto L10;
        this.frameType = r1;
        if (r1 == 5) goto L7;
        return true;
    L7:
        return false;
    L10:
        throw new TagPayloadReader.UnsupportedFormatException("Video format not supported: " + r2);
    }

    @Override // androidx.media3.extractor.flv.TagPayloadReader
    protected boolean parsePayload(ParsableByteArray r13, long r14) throws ParserException {
        int r0 = r13.readUnsignedByte();
        long r5 = r14 + (((long) r13.readInt24()) * 1000);
        if (r0 == 0) goto L5;
    L8:
        if (r0 == 1) goto L10;
    L29:
        return false;
    L10:
        if (this.hasOutputFormat == false) goto L29;
        if (this.frameType != 1) goto L14;
        boolean r2 = true;
    L16:
        if (this.hasOutputKeyframe == true) goto L19;
        if (r2 == true) goto L19;
        return false;
    L19:
        byte[] r3 = this.nalLength.getData();
        r3[0] = 0;
        r3[1] = 0;
        r3[2] = 0;
        int r11 = 4 - this.nalUnitLengthFieldLength;
        int r8 = 0;
    L21:
        if (r13.bytesLeft() <= 0) goto L23;
        r13.readBytes(this.nalLength.getData(), r11, this.nalUnitLengthFieldLength);
        this.nalLength.setPosition(0);
        int r4 = this.nalLength.readUnsignedIntToInt();
        this.nalStartCode.setPosition(0);
        this.output.sampleData(this.nalStartCode, 4);
        this.output.sampleData(r13, r4);
        r8 = (r8 + 4) + r4;
        goto L21
    L23:
        TrackOutput r6 = this.output;
        if (r2 == false) goto L26;
        int r7 = 1;
    L27:
        r6.sampleMetadata(r5, r7, r8, 0, null);
        this.hasOutputKeyframe = true;
        return true;
    L26:
        r7 = 0;
        goto L27
    L14:
        r2 = false;
        goto L16
    L5:
        if (this.hasOutputFormat == true) goto L8;
        ParsableByteArray r9 = new ParsableByteArray(new byte[r13.bytesLeft()]);
        r13.readBytes(r9.getData(), 0, r13.bytesLeft());
        AvcConfig r10 = AvcConfig.parse(r9);
        this.nalUnitLengthFieldLength = r10.nalUnitLengthFieldLength;
        this.output.format(new Format.Builder().setSampleMimeType(MimeTypes.VIDEO_H264).setCodecs(r10.codecs).setWidth(r10.width).setHeight(r10.height).setPixelWidthHeightRatio(r10.pixelWidthHeightRatio).setInitializationData(r10.initializationData).build());
        this.hasOutputFormat = true;
        return false;
    }

    @Override // androidx.media3.extractor.flv.TagPayloadReader
    public void seek() {
        this.hasOutputKeyframe = false;
    }
}
