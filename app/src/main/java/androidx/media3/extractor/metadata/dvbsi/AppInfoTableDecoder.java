package androidx.media3.extractor.metadata.dvbsi;

import androidx.media3.common.Metadata;
import androidx.media3.common.util.ParsableBitArray;
import androidx.media3.extractor.metadata.MetadataInputBuffer;
import androidx.media3.extractor.metadata.SimpleMetadataDecoder;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public final class AppInfoTableDecoder extends SimpleMetadataDecoder {
    public static final int APPLICATION_INFORMATION_TABLE_ID = 116;
    private static final int DESCRIPTOR_SIMPLE_APPLICATION_LOCATION = 21;
    private static final int DESCRIPTOR_TRANSPORT_PROTOCOL = 2;
    private static final int TRANSPORT_PROTOCOL_HTTP = 3;

    public AppInfoTableDecoder() {
    }

    private static Metadata parseAit(ParsableBitArray r20) {
        int r1 = 12;
        r20.skipBits(12);
        int r2 = r20.readBits(12);
        int r4 = 4;
        int r3 = (r20.getBytePosition() + r2) - 4;
        r20.skipBits(44);
        r20.skipBytes(r20.readBits(12));
        int r6 = 16;
        r20.skipBits(16);
        ArrayList r7 = new ArrayList();
    L4:
        if (r20.getBytePosition() >= r3) goto L31;
        String r8 = null;
        String r9 = null;
        r20.skipBits(48);
        int r10 = 8;
        int r11 = r20.readBits(8);
        r20.skipBits(r4);
        int r13 = r20.getBytePosition() + r20.readBits(r1);
    L7:
        if (r20.getBytePosition() >= r13) goto L25;
        int r14 = r20.readBits(r10);
        int r15 = r20.readBits(r10);
        int r5 = r20.getBytePosition() + r15;
        if (r14 != 2) goto L20;
        int r12 = r20.readBits(r6);
        r20.skipBits(r10);
        if (r12 == 3) goto L13;
        int r17 = r5;
        int r19 = r2;
        int r16 = r10;
    L24:
        r20.setPosition(r17 * 8);
        r10 = r16;
        r2 = r19;
        r6 = 16;
    L13:
        if (r20.getBytePosition() >= r5) goto L18;
        r8 = r20.readBytesAsString(r20.readBits(r10), StandardCharsets.US_ASCII);
        int r18 = r5;
        int r21 = r20.readBits(8);
        int r110 = 0;
    L15:
        if (r110 >= r21) goto L17;
        r20.skipBytes(r20.readBits(8));
        r110 = r110 + 1;
        r21 = r21;
        r2 = r2;
        goto L15
    L17:
        r10 = 8;
        r5 = r18;
        goto L13
    L18:
        r17 = r5;
        r19 = r2;
        r16 = r10;
        goto L24
    L20:
        r17 = r5;
        r19 = r2;
        r16 = r10;
        if (r14 != 21) goto L24;
        r9 = r20.readBytesAsString(r15, StandardCharsets.US_ASCII);
        goto L24
    L25:
        int r111 = r2;
        r20.setPosition(r13 * 8);
        if (r8 == null) goto L29;
        if (r9 == null) goto L29;
        r7.add(new AppInfoTable(r11, r8 + r9));
    L29:
        r2 = r111;
        r1 = 12;
        r4 = 4;
        r6 = 16;
        goto L4
    L31:
        if (r7.isEmpty() == false) goto L34;
        return null;
    L34:
        return new Metadata(r7);
    }

    @Override // androidx.media3.extractor.metadata.SimpleMetadataDecoder
    protected Metadata decode(MetadataInputBuffer r5, ByteBuffer r6) {
        if (r6.get() == 116) goto L7;
        return null;
    L7:
        return parseAit(new ParsableBitArray(r6.array(), r6.limit()));
    }
}
