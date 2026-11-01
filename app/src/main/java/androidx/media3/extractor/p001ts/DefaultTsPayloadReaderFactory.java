package androidx.media3.extractor.p001ts;

import android.util.SparseArray;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.CodecSpecificDataUtil;
import androidx.media3.common.util.ParsableByteArray;
import com.google.common.collect.ImmutableList;
import com.google.common.primitives.SignedBytes;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class DefaultTsPayloadReaderFactory implements TsPayloadReader.Factory {
    private static final int DESCRIPTOR_TAG_CAPTION_SERVICE = 134;
    public static final int FLAG_ALLOW_NON_IDR_KEYFRAMES = 1;
    public static final int FLAG_DETECT_ACCESS_UNITS = 8;
    public static final int FLAG_ENABLE_HDMV_DTS_AUDIO_STREAMS = 64;
    public static final int FLAG_IGNORE_AAC_STREAM = 2;
    public static final int FLAG_IGNORE_H264_STREAM = 4;
    public static final int FLAG_IGNORE_SPLICE_INFO_STREAM = 16;
    public static final int FLAG_OVERRIDE_CAPTION_DESCRIPTORS = 32;
    private final List<Format> closedCaptionFormats;
    private final int flags;

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface Flags {
    }

    public DefaultTsPayloadReaderFactory() {
        this(0);
    }

    public DefaultTsPayloadReaderFactory(int r2) {
        this(r2, ImmutableList.m212of());
    }

    public DefaultTsPayloadReaderFactory(int r1, List<Format> r2) {
        this.flags = r1;
        this.closedCaptionFormats = r2;
    }

    private SeiReader buildSeiReader(TsPayloadReader.EsInfo r3) {
        return new SeiReader(getClosedCaptionFormats(r3));
    }

    private UserDataReader buildUserDataReader(TsPayloadReader.EsInfo r3) {
        return new UserDataReader(getClosedCaptionFormats(r3));
    }

    private List<Format> getClosedCaptionFormats(TsPayloadReader.EsInfo r20) {
        if (isSet(32) == true) goto L5;
        ParsableByteArray r1 = new ParsableByteArray(r20.descriptorBytes);
        List<Format> r3 = this.closedCaptionFormats;
    L8:
        if (r1.bytesLeft() <= 0) goto L31;
        int r4 = r1.readUnsignedByte();
        int r6 = r1.getPosition() + r1.readUnsignedByte();
        if (r4 != 134) goto L30;
        ArrayList r7 = new ArrayList();
        int r5 = r1.readUnsignedByte() & 31;
        int r8 = 0;
    L12:
        if (r8 >= r5) goto L29;
        String r9 = r1.readString(3);
        int r10 = r1.readUnsignedByte();
        if ((r10 & 128) == 0) goto L16;
        boolean r11 = true;
    L17:
        if (r11 == false) goto L19;
        String r14 = MimeTypes.APPLICATION_CEA708;
        int r15 = r10 & 63;
    L20:
        byte r12 = (byte) r1.readUnsignedByte();
        r1.skipBytes(1);
        List<byte[]> r17 = null;
        if (r11 == false) goto L28;
        if ((r12 & SignedBytes.MAX_POWER_OF_TWO) == 0) goto L25;
        boolean r16 = true;
    L26:
        r17 = CodecSpecificDataUtil.buildCea708InitializationData(r16);
        goto L28
    L25:
        r16 = false;
    L28:
        r7.add(new Format.Builder().setSampleMimeType(r14).setLanguage(r9).setAccessibilityChannel(r15).setInitializationData(r17).build());
        r8 = r8 + 1;
        goto L12
    L19:
        r14 = MimeTypes.APPLICATION_CEA608;
        r15 = 1;
        goto L20
    L16:
        r11 = false;
        goto L17
    L29:
        r3 = r7;
    L30:
        r1.setPosition(r6);
        goto L8
    L31:
        return r3;
    L5:
        return this.closedCaptionFormats;
    }

    private boolean isSet(int r2) {
        if ((this.flags & r2) == 0) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // androidx.media3.extractor.ts.TsPayloadReader.Factory
    public SparseArray<TsPayloadReader> createInitialPayloadReaders() {
        return new SparseArray();
    }

    @Override // androidx.media3.extractor.ts.TsPayloadReader.Factory
    public TsPayloadReader createPayloadReader(int r6, TsPayloadReader.EsInfo r7) {
        switch(r6) {
            case 2: goto L51;
            case 3: goto L49;
            case 4: goto L49;
            case 15: goto L44;
            case 16: goto L42;
            case 17: goto L37;
            case 21: goto L35;
            case 27: goto L30;
            case 36: goto L28;
            case 45: goto L26;
            case 89: goto L24;
            case 128: goto L51;
            case 129: goto L22;
            case 130: goto L17;
            case 134: goto L12;
            case 135: goto L22;
            case 136: goto L20;
            case 138: goto L20;
            case 139: goto L10;
            case 172: goto L8;
            case 257: goto L6;
            default: goto L4;
        };
    L4:
        return null;
    L6:
        return new SectionReader(new PassthroughSectionPayloadReader(MimeTypes.APPLICATION_AIT));
    L8:
        return new PesReader(new Ac4Reader(r7.language, r7.getRoleFlags()));
    L10:
        return new PesReader(new DtsReader(r7.language, r7.getRoleFlags(), 5408));
    L12:
        if (isSet(16) == false) goto L15;
        return null;
    L15:
        return new SectionReader(new PassthroughSectionPayloadReader(MimeTypes.APPLICATION_SCTE35));
    L17:
        if (isSet(64) == true) goto L20;
        return null;
    L20:
        return new PesReader(new DtsReader(r7.language, r7.getRoleFlags(), 4096));
    L22:
        return new PesReader(new Ac3Reader(r7.language, r7.getRoleFlags()));
    L24:
        return new PesReader(new DvbSubtitleReader(r7.dvbSubtitleInfos));
    L26:
        return new PesReader(new MpeghReader());
    L28:
        return new PesReader(new H265Reader(buildSeiReader(r7)));
    L30:
        if (isSet(4) == false) goto L33;
        return null;
    L33:
        return new PesReader(new H264Reader(buildSeiReader(r7), isSet(1), isSet(8)));
    L35:
        return new PesReader(new Id3Reader());
    L37:
        if (isSet(2) == false) goto L40;
        return null;
    L40:
        return new PesReader(new LatmReader(r7.language, r7.getRoleFlags()));
    L42:
        return new PesReader(new H263Reader(buildUserDataReader(r7)));
    L44:
        if (isSet(2) == false) goto L47;
        return null;
    L47:
        return new PesReader(new AdtsReader(false, r7.language, r7.getRoleFlags()));
    L49:
        return new PesReader(new MpegAudioReader(r7.language, r7.getRoleFlags()));
    L51:
        return new PesReader(new H262Reader(buildUserDataReader(r7)));
    }
}
