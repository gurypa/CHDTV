package androidx.media3.extractor.text;

import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.extractor.text.dvb.DvbParser;
import androidx.media3.extractor.text.pgs.PgsParser;
import androidx.media3.extractor.text.ssa.SsaParser;
import androidx.media3.extractor.text.subrip.SubripParser;
import androidx.media3.extractor.text.ttml.TtmlParser;
import androidx.media3.extractor.text.tx3g.Tx3gParser;
import androidx.media3.extractor.text.webvtt.Mp4WebvttParser;
import androidx.media3.extractor.text.webvtt.WebvttParser;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public final class DefaultSubtitleParserFactory implements SubtitleParser.Factory {
    public DefaultSubtitleParserFactory() {
    }

    @Override // androidx.media3.extractor.text.SubtitleParser.Factory
    public SubtitleParser create(Format r5) {
        String r0 = r5.sampleMimeType;
        if (r0 == null) goto L51;
        switch(r0.hashCode()) {
            case -1351681404: goto L29;
            case -1248334819: goto L26;
            case -1026075066: goto L23;
            case -1004728940: goto L20;
            case 691401887: goto L17;
            case 822864842: goto L14;
            case 1668750253: goto L11;
            case 1693976202: goto L8;
            default: goto L31;
        };
    L31:
        byte r1 = -1;
    L32:
        switch(r1) {
            case 0: goto L49;
            case 1: goto L47;
            case 2: goto L45;
            case 3: goto L43;
            case 4: goto L41;
            case 5: goto L39;
            case 6: goto L37;
            case 7: goto L35;
            default: goto L51;
        };
    L35:
        return new TtmlParser();
    L37:
        return new DvbParser(r5.initializationData);
    L39:
        return new PgsParser();
    L41:
        return new Tx3gParser(r5.initializationData);
    L43:
        return new SubripParser();
    L45:
        return new Mp4WebvttParser();
    L47:
        return new WebvttParser();
    L49:
        return new SsaParser(r5.initializationData);
    L8:
        if (r0.equals(MimeTypes.APPLICATION_TTML) == false) goto L31;
        r1 = 7;
        goto L32
    L11:
        if (r0.equals(MimeTypes.APPLICATION_SUBRIP) == false) goto L31;
        r1 = 3;
        goto L32
    L14:
        if (r0.equals(MimeTypes.TEXT_SSA) == false) goto L31;
        r1 = 0;
        goto L32
    L17:
        if (r0.equals(MimeTypes.APPLICATION_TX3G) == false) goto L31;
        r1 = 4;
        goto L32
    L20:
        if (r0.equals(MimeTypes.TEXT_VTT) == false) goto L31;
        r1 = 1;
        goto L32
    L23:
        if (r0.equals(MimeTypes.APPLICATION_MP4VTT) == false) goto L31;
        r1 = 2;
        goto L32
    L26:
        if (r0.equals(MimeTypes.APPLICATION_PGS) == false) goto L31;
        r1 = 5;
        goto L32
    L29:
        if (r0.equals(MimeTypes.APPLICATION_DVBSUBS) == false) goto L31;
        r1 = 6;
    L51:
        throw new IllegalArgumentException("Unsupported MIME type: " + r0);
    }

    @Override // androidx.media3.extractor.text.SubtitleParser.Factory
    public int getCueReplacementBehavior(Format r5) {
        String r0 = r5.sampleMimeType;
        if (r0 == null) goto L43;
        switch(r0.hashCode()) {
            case -1351681404: goto L29;
            case -1248334819: goto L26;
            case -1026075066: goto L23;
            case -1004728940: goto L20;
            case 691401887: goto L17;
            case 822864842: goto L14;
            case 1668750253: goto L11;
            case 1693976202: goto L8;
            default: goto L31;
        };
    L31:
        byte r1 = -1;
    L32:
        switch(r1) {
            case 0: goto L41;
            case 1: goto L40;
            case 2: goto L39;
            case 3: goto L38;
            case 4: goto L37;
            case 5: goto L36;
            case 6: goto L35;
            case 7: goto L34;
            default: goto L43;
        };
    L34:
        return 1;
    L35:
        return 2;
    L36:
        return 2;
    L37:
        return 2;
    L38:
        return 1;
    L39:
        return 2;
    L40:
        return 1;
    L41:
        return 1;
    L8:
        if (r0.equals(MimeTypes.APPLICATION_TTML) == false) goto L31;
        r1 = 7;
        goto L32
    L11:
        if (r0.equals(MimeTypes.APPLICATION_SUBRIP) == false) goto L31;
        r1 = 3;
        goto L32
    L14:
        if (r0.equals(MimeTypes.TEXT_SSA) == false) goto L31;
        r1 = 0;
        goto L32
    L17:
        if (r0.equals(MimeTypes.APPLICATION_TX3G) == false) goto L31;
        r1 = 4;
        goto L32
    L20:
        if (r0.equals(MimeTypes.TEXT_VTT) == false) goto L31;
        r1 = 1;
        goto L32
    L23:
        if (r0.equals(MimeTypes.APPLICATION_MP4VTT) == false) goto L31;
        r1 = 2;
        goto L32
    L26:
        if (r0.equals(MimeTypes.APPLICATION_PGS) == false) goto L31;
        r1 = 5;
        goto L32
    L29:
        if (r0.equals(MimeTypes.APPLICATION_DVBSUBS) == false) goto L31;
        r1 = 6;
    L43:
        throw new IllegalArgumentException("Unsupported MIME type: " + r0);
    }

    @Override // androidx.media3.extractor.text.SubtitleParser.Factory
    public boolean supportsFormat(Format r3) {
        String r0 = r3.sampleMimeType;
        if (Objects.equals(r0, MimeTypes.TEXT_SSA) == false) goto L5;
    L20:
        return true;
    L5:
        if (Objects.equals(r0, MimeTypes.TEXT_VTT) == true) goto L20;
        if (Objects.equals(r0, MimeTypes.APPLICATION_MP4VTT) == true) goto L20;
        if (Objects.equals(r0, MimeTypes.APPLICATION_SUBRIP) == true) goto L20;
        if (Objects.equals(r0, MimeTypes.APPLICATION_TX3G) == true) goto L20;
        if (Objects.equals(r0, MimeTypes.APPLICATION_PGS) == true) goto L20;
        if (Objects.equals(r0, MimeTypes.APPLICATION_DVBSUBS) == true) goto L20;
        if (Objects.equals(r0, MimeTypes.APPLICATION_TTML) == true) goto L20;
        return false;
    }
}
