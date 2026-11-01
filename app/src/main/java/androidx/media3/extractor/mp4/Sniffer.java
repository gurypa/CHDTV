package androidx.media3.extractor.mp4;

import android.support.v4.media.session.PlaybackStateCompat;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.container.Mp4Box;
import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.SniffFailure;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class Sniffer {
    public static final int BRAND_HEIC = 1751476579;
    public static final int BRAND_QUICKTIME = 1903435808;
    private static final int[] COMPATIBLE_BRANDS = null;
    private static final int SEARCH_LENGTH = 4096;

    static {
        COMPATIBLE_BRANDS = new int[]{1769172845, 1769172786, 1769172787, 1769172788, 1769172789, 1769172790, 1769172793, Mp4Box.TYPE_avc1, Mp4Box.TYPE_hvc1, Mp4Box.TYPE_hev1, Mp4Box.TYPE_av01, 1836069937, 1836069938, 862401121, 862401122, 862417462, 862417718, 862414134, 862414646, 1295275552, 1295270176, 1714714144, 1801741417, 1295275600, BRAND_QUICKTIME, 1297305174, 1684175153, 1769172332, 1885955686};
    }

    private Sniffer() {
    }

    private static boolean isCompatibleBrand(int r6, boolean r7) {
        if ((r6 >>> 8) != 3368816) goto L6;
        return true;
    L6:
        if (r6 != 1751476579) goto L9;
        if (r7 == false) goto L9;
        return true;
    L9:
        int[] r0 = COMPATIBLE_BRANDS;
        int r1 = r0.length;
        int r4 = 0;
    L10:
        if (r4 >= r1) goto L15;
        if (r0[r4] == r6) goto L13;
        r4 = r4 + 1;
        goto L10
    L13:
        return true;
    L15:
        return false;
    }

    public static SniffFailure sniffFragmented(ExtractorInput r2) throws IOException {
        return sniffInternal(r2, true, false);
    }

    private static SniffFailure sniffInternal(ExtractorInput r25, boolean r26, boolean r27) throws IOException {
        long r2 = r25.getLength();
        long r4 = -1;
        long r7 = PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM;
        if (r2 != (-1)) goto L5;
    L9:
        int r6 = (int) r7;
        ParsableByteArray r8 = new ParsableByteArray(64);
        int r9 = 0;
        boolean r10 = false;
        boolean r11 = false;
    L10:
        if (r9 >= r6) goto L83;
        int r12 = 8;
        r8.reset(8);
        if (r25.peekFully(r8.getData(), 0, 8, true) == false) goto L83;
        long r14 = r8.readUnsignedInt();
        r4 = r4;
        int r5 = r8.readInt();
        if (r14 != 1) goto L18;
        r12 = 16;
        r25.peekFully(r8.getData(), 8, 8);
        r8.setLimit(16);
        long r15 = r8.readLong();
    L25:
        int r13 = r6;
        if (r15 < r12) goto L28;
        r9 = r9 + r12;
        if (r5 == 1836019574) goto L31;
        boolean r20 = r10;
        boolean r16 = r11;
        if (r5 == 1836019558) goto L81;
        if (r5 == 1836475768) goto L81;
        if (r5 != 1835295092) goto L46;
        boolean r17 = true;
    L47:
        long r21 = r2;
        if (((((long) r9) + r15) - ((long) r12)) >= r13) goto L49;
        int r3 = (int) (r15 - ((long) r12));
        int r18 = r9 + r3;
        if (r5 == 1718909296) goto L53;
        boolean r19 = r17;
        if (r3 == 0) goto L78;
        r25.advancePeekPosition(r3);
    L78:
        r10 = r19;
    L79:
        r11 = r16;
        r6 = r13;
        r9 = r18;
        r2 = r21;
        goto L10
    L53:
        if (r3 < 8) goto L55;
        boolean r110 = r17;
        r8.reset(r3);
        r25.peekFully(r8.getData(), 0, r3);
        int r22 = r8.readInt();
        if (isCompatibleBrand(r22, r27) == false) goto L59;
        r10 = true;
    L60:
        r8.skipBytes(4);
        int r23 = r8.bytesLeft() / 4;
        int[] r24 = null;
        if (r10 == true) goto L71;
        if (r23 <= 0) goto L71;
        r24 = new int[r23];
        int r111 = 0;
    L64:
        if (r111 >= r23) goto L71;
        r24[r111] = r8.readInt();
        int r112 = r5;
        if (isCompatibleBrand(r24[r111], r27) == true) goto L67;
        r111 = r111 + 1;
        r5 = r112;
        goto L64
    L67:
        r10 = true;
    L71:
        if (r10 == true) goto L79;
        return new UnsupportedBrandsSniffFailure(r22, r24);
    L59:
        r10 = r110;
        goto L60
    L55:
        return new AtomSizeTooSmallSniffFailure(r5, r3, 8);
    L49:
        boolean r113 = r16;
    L84:
        if (r17 == true) goto L88;
        return NoDeclaredBrandSniffFailure.INSTANCE;
    L88:
        if (r26 == r113) goto L93;
        if (r113 == false) goto L92;
        return IncorrectFragmentationSniffFailure.FILE_FRAGMENTED;
    L92:
        return IncorrectFragmentationSniffFailure.FILE_NOT_FRAGMENTED;
    L93:
        return null;
    L46:
        r17 = r20;
    L81:
        r113 = true;
        r17 = r20;
        goto L84
    L31:
        r6 = r13 + ((int) r15);
        if (r2 == r4) goto L36;
        boolean r28 = r10;
        boolean r29 = r11;
        if (r6 > r2) goto L35;
    L37:
        r11 = r29;
        r10 = r28;
        goto L10
    L35:
        r6 = (int) r2;
        r11 = r29;
        r10 = r28;
        goto L10
    L36:
        r28 = r10;
        r29 = r11;
        goto L37
    L28:
        return new AtomSizeTooSmallSniffFailure(r5, r15, r12);
    L18:
        if (r14 != 0) goto L24;
        long r114 = r25.getLength();
        if (r114 == r4) goto L24;
        r15 = (r114 - r25.getPeekPosition()) + ((long) 8);
    L24:
        r15 = r14;
    L83:
        r113 = r11;
        r17 = r10;
        goto L84
    L5:
        if (r2 > PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM) goto L9;
        r7 = r2;
        goto L9
    }

    public static SniffFailure sniffUnfragmented(ExtractorInput r1, boolean r2) throws IOException {
        return sniffInternal(r1, false, r2);
    }
}
