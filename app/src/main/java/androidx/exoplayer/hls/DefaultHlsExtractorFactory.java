package androidx.media3.exoplayer.hls;

import android.net.Uri;
import android.text.TextUtils;
import androidx.media3.common.FileTypes;
import androidx.media3.common.Format;
import androidx.media3.common.Metadata;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.TimestampAdjuster;
import androidx.media3.exoplayer.analytics.PlayerId;
import androidx.media3.extractor.Extractor;
import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.mp3.Mp3Extractor;
import androidx.media3.extractor.mp4.FragmentedMp4Extractor;
import androidx.media3.extractor.p001ts.Ac3Extractor;
import androidx.media3.extractor.p001ts.Ac4Extractor;
import androidx.media3.extractor.p001ts.AdtsExtractor;
import androidx.media3.extractor.p001ts.DefaultTsPayloadReaderFactory;
import androidx.media3.extractor.p001ts.TsExtractor;
import androidx.media3.extractor.text.DefaultSubtitleParserFactory;
import androidx.media3.extractor.text.SubtitleParser;
import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Ints;
import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class DefaultHlsExtractorFactory implements HlsExtractorFactory {
    private static final int[] DEFAULT_EXTRACTOR_ORDER = null;
    private final boolean exposeCea608WhenMissingDeclarations;
    private boolean parseSubtitlesDuringExtraction;
    private final int payloadReaderFactoryFlags;
    private SubtitleParser.Factory subtitleParserFactory;

    static {
        DEFAULT_EXTRACTOR_ORDER = new int[]{8, 13, 11, 2, 0, 1, 7};
    }

    public DefaultHlsExtractorFactory() {
        this(0, true);
    }

    public DefaultHlsExtractorFactory(int r2, boolean r3) {
        this.payloadReaderFactoryFlags = r2;
        this.exposeCea608WhenMissingDeclarations = r3;
        this.subtitleParserFactory = new DefaultSubtitleParserFactory();
    }

    private static void addFileTypeIfValidAndNotPresent(int r2, List<Integer> r3) {
        if (Ints.indexOf(DEFAULT_EXTRACTOR_ORDER, r2) != (-1)) goto L5;
        return;
    L5:
        if (r3.contains(Integer.valueOf(r2)) == true) goto L10;
        r3.add(Integer.valueOf(r2));
        return;
    }

    private Extractor createExtractorByFileType(int r12, Format r13, List<Format> r14, TimestampAdjuster r15) {
        switch(r12) {
            case 0: goto L18;
            case 1: goto L16;
            case 2: goto L14;
            case 7: goto L12;
            case 8: goto L10;
            case 11: goto L8;
            case 13: goto L6;
            default: goto L4;
        };
    L4:
        return null;
    L6:
        return new WebvttExtractor(r13.language, r15, this.subtitleParserFactory, this.parseSubtitlesDuringExtraction);
    L8:
        return createTsExtractor(this.payloadReaderFactoryFlags, this.exposeCea608WhenMissingDeclarations, r13, r14, r15, this.subtitleParserFactory, this.parseSubtitlesDuringExtraction);
    L10:
        return createFragmentedMp4Extractor(this.subtitleParserFactory, this.parseSubtitlesDuringExtraction, r15, r13, r14);
    L12:
        return new Mp3Extractor(0, 0);
    L14:
        return new AdtsExtractor();
    L16:
        return new Ac4Extractor();
    L18:
        return new Ac3Extractor();
    }

    private static FragmentedMp4Extractor createFragmentedMp4Extractor(SubtitleParser.Factory r8, boolean r9, TimestampAdjuster r10, Format r11, List<Format> r12) {
        if (isFmp4Variant(r11) == false) goto L5;
        int r0 = 4;
    L6:
        if (r9 == true) goto L8;
        SubtitleParser.Factory r2 = SubtitleParser.Factory.UNSUPPORTED;
        int r3 = r0 | 32;
    L10:
        if (r12 == null) goto L12;
        List<Format> r6 = r12;
    L14:
        return new FragmentedMp4Extractor(r2, r3, r10, null, r6, null);
    L12:
        r6 = ImmutableList.m212of();
        goto L14
    L8:
        r2 = r8;
        r3 = r0;
        goto L10
    L5:
        r0 = 0;
        goto L6
    }

    private static TsExtractor createTsExtractor(int r12, boolean r13, Format r14, List<Format> r15, TimestampAdjuster r16, SubtitleParser.Factory r17, boolean r18) {
        int r0 = r12 | 16;
        if (r15 == null) goto L5;
        r0 = r0 | 32;
        List<Format> r1 = r15;
    L8:
        String r2 = r14.codecs;
        if (TextUtils.isEmpty(r2) == true) goto L17;
        if (MimeTypes.containsCodecsCorrespondingToMimeType(r2, MimeTypes.AUDIO_AAC) == true) goto L14;
        r0 = r0 | 2;
    L14:
        if (MimeTypes.containsCodecsCorrespondingToMimeType(r2, MimeTypes.VIDEO_H264) == true) goto L17;
        r0 = r0 | 4;
    L17:
        if (r18 == true) goto L19;
        SubtitleParser.Factory r8 = SubtitleParser.Factory.UNSUPPORTED;
        int r7 = 0 | 1;
    L21:
        return new TsExtractor(2, r7, r8, r16, new DefaultTsPayloadReaderFactory(r0, r1), TsExtractor.DEFAULT_TIMESTAMP_SEARCH_BYTES);
    L19:
        r8 = r17;
        r7 = 0;
        goto L21
    L5:
        if (r13 == false) goto L7;
        r1 = Collections.singletonList(new Format.Builder().setSampleMimeType(MimeTypes.APPLICATION_CEA608).build());
        goto L8
    L7:
        r1 = Collections.emptyList();
        goto L8
    }

    private static boolean isFmp4Variant(Format r5) {
        Metadata r0 = r5.metadata;
        if (r0 != null) goto L5;
        return false;
    L5:
        int r2 = 0;
    L7:
        if (r2 >= r0.length()) goto L13;
        Metadata.Entry r3 = r0.get(r2);
        if ((r3 instanceof HlsTrackMetadataEntry) == true) goto L11;
        r2 = r2 + 1;
        goto L7
    L11:
        return !((HlsTrackMetadataEntry) r3).variantInfos.isEmpty();
    L13:
        return false;
    }

    private static boolean sniffQuietly(Extractor r2, ExtractorInput r3) throws IOException {
        boolean r0 = false;
        r0 = r2.sniff(r3);     // Catch: Throwable -> L6 EOFException -> L9
        goto L5
    L6:
        th = move-exception;
        r3.resetPeekPosition();
        throw th;
    L5:
        r3.resetPeekPosition();
        return r0;
    }

    @Override // androidx.media3.exoplayer.hls.HlsExtractorFactory
    public BundledHlsMediaChunkExtractor createExtractor(Uri r17, Format r18, List<Format> r19, TimestampAdjuster r20, Map<String, List<String>> r21, ExtractorInput r22, PlayerId r23) throws IOException {
        Format r3 = r18;
        int r7 = FileTypes.inferFileTypeFromMimeType(r3.sampleMimeType);
        int r8 = FileTypes.inferFileTypeFromResponseHeaders(r21);
        int r9 = FileTypes.inferFileTypeFromUri(r17);
        ArrayList r1 = new ArrayList(DEFAULT_EXTRACTOR_ORDER.length);
        addFileTypeIfValidAndNotPresent(r7, r1);
        addFileTypeIfValidAndNotPresent(r8, r1);
        addFileTypeIfValidAndNotPresent(r9, r1);
        int[] r2 = DEFAULT_EXTRACTOR_ORDER;
        int r4 = r2.length;
        int r5 = 0;
    L3:
        if (r5 >= r4) goto L5;
        addFileTypeIfValidAndNotPresent(r2[r5], r1);
        r5 = r5 + 1;
        goto L3
    L5:
        r22.resetPeekPosition();
        Extractor r11 = null;
        int r12 = 0;
    L7:
        if (r12 >= r1.size()) goto L21;
        int r13 = ((Integer) r1.get(r12)).intValue();
        Extractor r6 = (Extractor) Assertions.checkNotNull(createExtractorByFileType(r13, r3, r19, r20));
        if (sniffQuietly(r6, r22) == true) goto L11;
        if (r11 != null) goto L19;
        if (r13 == r7) goto L18;
        if (r13 == r8) goto L18;
        if (r13 == r9) goto L18;
        if (r13 != 11) goto L19;
    L18:
        r11 = r6;
    L19:
        r12 = r12 + 1;
        r3 = r18;
        goto L7
    L11:
        return new BundledHlsMediaChunkExtractor(r6, r3, r20, this.subtitleParserFactory, this.parseSubtitlesDuringExtraction);
    L21:
        return new BundledHlsMediaChunkExtractor((Extractor) Assertions.checkNotNull(r11), r18, r20, this.subtitleParserFactory, this.parseSubtitlesDuringExtraction);
    }

    @Override // androidx.media3.exoplayer.hls.HlsExtractorFactory
    public /* bridge */ /* synthetic */ HlsMediaChunkExtractor createExtractor(Uri r1, Format r2, List r3, TimestampAdjuster r4, Map r5, ExtractorInput r6, PlayerId r7) throws IOException {
        return createExtractor(r1, r2, r3, r4, r5, r6, r7);
    }

    @Override // androidx.media3.exoplayer.hls.HlsExtractorFactory
    public DefaultHlsExtractorFactory experimentalParseSubtitlesDuringExtraction(boolean r1) {
        this.parseSubtitlesDuringExtraction = r1;
        return this;
    }

    @Override // androidx.media3.exoplayer.hls.HlsExtractorFactory
    public /* bridge */ /* synthetic */ HlsExtractorFactory experimentalParseSubtitlesDuringExtraction(boolean r1) {
        return experimentalParseSubtitlesDuringExtraction(r1);
    }

    @Override // androidx.media3.exoplayer.hls.HlsExtractorFactory
    public Format getOutputTextFormat(Format r5) {
        if (this.parseSubtitlesDuringExtraction == true) goto L5;
    L12:
        return r5;
    L5:
        if (this.subtitleParserFactory.supportsFormat(r5) == false) goto L12;
        Format.Builder r0 = r5.buildUpon().setSampleMimeType(MimeTypes.APPLICATION_MEDIA3_CUES).setCueReplacementBehavior(this.subtitleParserFactory.getCueReplacementBehavior(r5));
        StringBuilder r1 = new StringBuilder().append(r5.sampleMimeType);
        if (r5.codecs == null) goto L9;
        String r2 = " " + r5.codecs;
    L11:
        return r0.setCodecs(r1.append(r2).toString()).setSubsampleOffsetUs(Long.MAX_VALUE).build();
    L9:
        r2 = "";
        goto L11
    }

    @Override // androidx.media3.exoplayer.hls.HlsExtractorFactory
    public DefaultHlsExtractorFactory setSubtitleParserFactory(SubtitleParser.Factory r1) {
        this.subtitleParserFactory = r1;
        return this;
    }

    @Override // androidx.media3.exoplayer.hls.HlsExtractorFactory
    public /* bridge */ /* synthetic */ HlsExtractorFactory setSubtitleParserFactory(SubtitleParser.Factory r1) {
        return setSubtitleParserFactory(r1);
    }
}
