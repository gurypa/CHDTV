package androidx.media3.extractor.metadata.icy;

import androidx.media3.common.Metadata;
import androidx.media3.extractor.metadata.MetadataInputBuffer;
import androidx.media3.extractor.metadata.SimpleMetadataDecoder;
import com.google.common.base.Ascii;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
public final class IcyDecoder extends SimpleMetadataDecoder {
    private static final Pattern METADATA_ELEMENT = null;
    private static final String STREAM_KEY_NAME = "streamtitle";
    private static final String STREAM_KEY_URL = "streamurl";
    private final CharsetDecoder iso88591Decoder;
    private final CharsetDecoder utf8Decoder;

    static {
        METADATA_ELEMENT = Pattern.compile("(.+?)='(.*?)';", 32);
    }

    public IcyDecoder() {
        this.utf8Decoder = StandardCharsets.UTF_8.newDecoder();
        this.iso88591Decoder = StandardCharsets.ISO_8859_1.newDecoder();
    }

    private String decodeToString(ByteBuffer r3) {
        String r0 = this.utf8Decoder.decode(r3).toString();     // Catch: Throwable -> L5 CharacterCodingException -> L8
        this.utf8Decoder.reset();
        r3.rewind();
        return r0;
    L5:
        th = move-exception;
        this.utf8Decoder.reset();
        r3.rewind();
        throw th;
    L9:
        this.utf8Decoder.reset();
        r3.rewind();
        String r1 = this.iso88591Decoder.decode(r3).toString();     // Catch: Throwable -> L13 CharacterCodingException -> L16
        this.iso88591Decoder.reset();
        r3.rewind();
        return r1;
    L13:
        th = move-exception;
        this.iso88591Decoder.reset();
        r3.rewind();
        throw th;
    L17:
        this.iso88591Decoder.reset();
        r3.rewind();
        return null;
    }

    @Override // androidx.media3.extractor.metadata.SimpleMetadataDecoder
    protected Metadata decode(MetadataInputBuffer r13, ByteBuffer r14) {
        String r0 = decodeToString(r14);
        byte[] r1 = new byte[r14.limit()];
        r14.get(r1);
        if (r0 == null) goto L5;
        String r4 = null;
        String r5 = null;
        int r6 = 0;
        Matcher r7 = METADATA_ELEMENT.matcher(r0);
    L8:
        if (r7.find(r6) == false) goto L27;
        String r8 = r7.group(1);
        String r9 = r7.group(2);
        if (r8 == null) goto L25;
        String r10 = Ascii.toLowerCase(r8);
        switch(r10.hashCode()) {
            case -315603473: goto L18;
            case 1646559960: goto L15;
            default: goto L20;
        };
    L20:
        byte r11 = -1;
    L21:
        switch(r11) {
            case 0: goto L24;
            case 1: goto L23;
            default: goto L25;
        };
    L23:
        r5 = r9;
        goto L25
    L24:
        r4 = r9;
        goto L25
    L15:
        if (r10.equals(STREAM_KEY_NAME) == false) goto L20;
        r11 = 0;
        goto L21
    L18:
        if (r10.equals(STREAM_KEY_URL) == false) goto L20;
        r11 = 1;
    L25:
        r6 = r7.end();
        goto L8
    L27:
        return new Metadata(new Metadata.Entry[]{new IcyInfo(r1, r4, r5)});
    L5:
        return new Metadata(new Metadata.Entry[]{new IcyInfo(r1, null, null)});
    }
}
