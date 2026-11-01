package androidx.media3.exoplayer.hls;

import android.media.MediaFormat;
import android.media.MediaParser;
import android.net.Uri;
import android.text.TextUtils;
import androidx.media3.common.FileTypes;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.TimestampAdjuster;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.analytics.PlayerId;
import androidx.media3.exoplayer.source.mediaparser.InputReaderAdapterV30;
import androidx.media3.exoplayer.source.mediaparser.MediaParserUtil;
import androidx.media3.exoplayer.source.mediaparser.OutputConsumerAdapterV30;
import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.text.SubtitleParser;
import com.google.common.collect.ImmutableList;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class MediaParserHlsMediaChunkExtractor implements HlsMediaChunkExtractor {
    public static final HlsExtractorFactory FACTORY = null;
    private final Format format;
    private final InputReaderAdapterV30 inputReaderAdapter;
    private final MediaParser mediaParser;
    private final ImmutableList<MediaFormat> muxedCaptionMediaFormats;
    private final OutputConsumerAdapterV30 outputConsumerAdapter;
    private final boolean overrideInBandCaptionDeclarations;
    private int pendingSkipBytes;
    private final PlayerId playerId;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.hls.MediaParserHlsMediaChunkExtractor$1 */
    static /* synthetic */ class C02961 {
    }

    private static final class PeekingInputReader implements MediaParser.SeekableInputReader {
        private final ExtractorInput extractorInput;
        private int totalPeekedBytes;

        private PeekingInputReader(ExtractorInput r1) {
            this.extractorInput = r1;
        }

        /* synthetic */ PeekingInputReader(ExtractorInput r1, C02961 r2) {
            this(r1);
        }

        static /* synthetic */ int access$100(PeekingInputReader r1) {
            return r1.totalPeekedBytes;
        }

        @Override // android.media.MediaParser.InputReader
        public long getLength() {
            return this.extractorInput.getLength();
        }

        @Override // android.media.MediaParser.InputReader
        public long getPosition() {
            return this.extractorInput.getPeekPosition();
        }

        @Override // android.media.MediaParser.InputReader
        public int read(byte[] r3, int r4, int r5) throws IOException {
            int r0 = this.extractorInput.peek(r3, r4, r5);
            this.totalPeekedBytes += r0;
            return r0;
        }

        @Override // android.media.MediaParser.SeekableInputReader
        public void seekToPosition(long r2) {
            throw new UnsupportedOperationException();
        }
    }

    static {
        FACTORY = new MediaParserHlsMediaChunkExtractor$$ExternalSyntheticLambda0();
    }

    public MediaParserHlsMediaChunkExtractor(MediaParser r2, OutputConsumerAdapterV30 r3, Format r4, boolean r5, ImmutableList<MediaFormat> r6, int r7, PlayerId r8) {
        this.mediaParser = r2;
        this.outputConsumerAdapter = r3;
        this.overrideInBandCaptionDeclarations = r5;
        this.muxedCaptionMediaFormats = r6;
        this.format = r4;
        this.playerId = r8;
        this.pendingSkipBytes = r7;
        this.inputReaderAdapter = new InputReaderAdapterV30();
    }

    private static MediaParser createMediaParserInstance(MediaParser.OutputConsumer r5, Format r6, boolean r7, ImmutableList<MediaFormat> r8, PlayerId r9, String... r10) {
        if (r10.length != 1) goto L5;
        MediaParser r0 = MediaParser.createByName(r10[0], r5);
    L6:
        r0.setParameter(MediaParserUtil.PARAMETER_EXPOSE_CAPTION_FORMATS, r8);
        r0.setParameter(MediaParserUtil.PARAMETER_OVERRIDE_IN_BAND_CAPTION_DECLARATIONS, Boolean.valueOf(r7));
        r0.setParameter(MediaParserUtil.PARAMETER_IN_BAND_CRYPTO_INFO, true);
        r0.setParameter(MediaParserUtil.PARAMETER_EAGERLY_EXPOSE_TRACK_TYPE, true);
        r0.setParameter(MediaParserUtil.PARAMETER_IGNORE_TIMESTAMP_OFFSET, true);
        r0.setParameter("android.media.mediaparser.ts.ignoreSpliceInfoStream", true);
        r0.setParameter("android.media.mediaparser.ts.mode", "hls");
        String r1 = r6.codecs;
        if (TextUtils.isEmpty(r1) == true) goto L15;
        if (MimeTypes.AUDIO_AAC.equals(MimeTypes.getAudioMediaMimeType(r1)) == true) goto L12;
        r0.setParameter("android.media.mediaparser.ts.ignoreAacStream", true);
    L12:
        if (MimeTypes.VIDEO_H264.equals(MimeTypes.getVideoMediaMimeType(r1)) == true) goto L15;
        r0.setParameter("android.media.mediaparser.ts.ignoreAvcStream", true);
    L15:
        if (Util.SDK_INT < 31) goto L17;
        MediaParserUtil.setLogSessionIdOnMediaParser(r0, r9);
    L17:
        return r0;
    L5:
        r0 = MediaParser.create(r5, r10);
        goto L6
    }

    /* JADX WARN: Multi-variable type inference failed */
    static /* synthetic */ HlsMediaChunkExtractor lambda$static$0(Uri r17, Format r18, List r19, TimestampAdjuster r20, Map r21, ExtractorInput r22, PlayerId r23) throws IOException {
        boolean r3 = false;
        if (FileTypes.inferFileTypeFromMimeType(r18.sampleMimeType) == 13) goto L5;
        if (r19 == null) goto L8;
        r3 = true;
    L8:
        boolean r2 = r3;
        ImmutableList.Builder r10 = ImmutableList.builder();
        if (r19 == null) goto L15;
        int r0 = 0;
    L12:
        if (r0 >= r19.size()) goto L16;
        r10.add(MediaParserUtil.toCaptionsMediaFormat((Format) r19.get(r0)));
        r0 = r0 + 1;
    L16:
        ImmutableList r4 = r10.build();
        OutputConsumerAdapterV30 r1 = new OutputConsumerAdapterV30();
        if (r19 == null) goto L19;
        List r5 = r19;
    L20:
        r1.setMuxedCaptionFormats(r5);
        r1.setTimestampAdjuster(r20);
        MediaParser r6 = createMediaParserInstance(r1, r18, r2, r4, r23, new String[]{"android.media.mediaparser.FragmentedMp4Parser", "android.media.mediaparser.Ac3Parser", "android.media.mediaparser.Ac4Parser", "android.media.mediaparser.AdtsParser", "android.media.mediaparser.Mp3Parser", "android.media.mediaparser.TsParser"});
        PeekingInputReader r7 = new PeekingInputReader(r22, null);
        r6.advance(r7);
        r1.setSelectedParserName(r6.getParserName());
        return new MediaParserHlsMediaChunkExtractor(r6, r1, r18, r2, r4, PeekingInputReader.access$100(r7), r23);
    L19:
        r5 = ImmutableList.m212of();
        goto L20
    L15:
        r10.add(MediaParserUtil.toCaptionsMediaFormat(new Format.Builder().setSampleMimeType(MimeTypes.APPLICATION_CEA608).build()));
        goto L16
    L5:
        return new BundledHlsMediaChunkExtractor(new WebvttExtractor(r18.language, r20, SubtitleParser.Factory.UNSUPPORTED, false), r18, r20);
    }

    @Override // androidx.media3.exoplayer.hls.HlsMediaChunkExtractor
    public void init(ExtractorOutput r2) {
        this.outputConsumerAdapter.setExtractorOutput(r2);
    }

    @Override // androidx.media3.exoplayer.hls.HlsMediaChunkExtractor
    public boolean isPackedAudioExtractor() {
        String r0 = this.mediaParser.getParserName();
        if ("android.media.mediaparser.Ac3Parser".equals(r0) == false) goto L5;
    L12:
        return true;
    L5:
        if ("android.media.mediaparser.Ac4Parser".equals(r0) == true) goto L12;
        if ("android.media.mediaparser.AdtsParser".equals(r0) == true) goto L12;
        if ("android.media.mediaparser.Mp3Parser".equals(r0) == true) goto L12;
        return false;
    }

    @Override // androidx.media3.exoplayer.hls.HlsMediaChunkExtractor
    public boolean isReusable() {
        String r0 = this.mediaParser.getParserName();
        if ("android.media.mediaparser.FragmentedMp4Parser".equals(r0) == false) goto L5;
    L8:
        return true;
    L5:
        if ("android.media.mediaparser.TsParser".equals(r0) == true) goto L8;
        return false;
    }

    @Override // androidx.media3.exoplayer.hls.HlsMediaChunkExtractor
    public void onTruncatedSegmentParsed() {
        this.mediaParser.seek(MediaParser.SeekPoint.START);
    }

    @Override // androidx.media3.exoplayer.hls.HlsMediaChunkExtractor
    public boolean read(ExtractorInput r4) throws IOException {
        r4.skipFully(this.pendingSkipBytes);
        this.pendingSkipBytes = 0;
        this.inputReaderAdapter.setDataReader(r4, r4.getLength());
        return this.mediaParser.advance(this.inputReaderAdapter);
    }

    @Override // androidx.media3.exoplayer.hls.HlsMediaChunkExtractor
    public HlsMediaChunkExtractor recreate() {
        Assertions.checkState(!isReusable());
        return new MediaParserHlsMediaChunkExtractor(createMediaParserInstance(this.outputConsumerAdapter, this.format, this.overrideInBandCaptionDeclarations, this.muxedCaptionMediaFormats, this.playerId, new String[]{this.mediaParser.getParserName()}), this.outputConsumerAdapter, this.format, this.overrideInBandCaptionDeclarations, this.muxedCaptionMediaFormats, 0, this.playerId);
    }
}
