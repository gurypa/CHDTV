package androidx.media3.extractor;

import androidx.media3.common.util.ParsableByteArray;

/* JADX INFO: loaded from: classes.dex */
public final class DolbyVisionConfig {
    public final String codecs;
    public final int level;
    public final int profile;

    private DolbyVisionConfig(int r1, int r2, String r3) {
        this.profile = r1;
        this.level = r2;
        this.codecs = r3;
    }

    public static DolbyVisionConfig parse(ParsableByteArray r7) {
        r7.skipBytes(2);
        int r0 = r7.readUnsignedByte();
        int r1 = r0 >> 1;
        int r2 = ((r0 & 1) << 5) | ((r7.readUnsignedByte() >> 3) & 31);
        if (r1 == 4) goto L16;
        if (r1 == 5) goto L16;
        if (r1 == 7) goto L16;
        if (r1 != 8) goto L12;
        String r3 = "hev1";
    L17:
        String r5 = ".0";
        StringBuilder r4 = new StringBuilder().append(r3).append(".0").append(r1);
        if (r2 < 10) goto L22;
        r5 = ".";
    L22:
        return new DolbyVisionConfig(r1, r2, r4.append(r5).append(r2).toString());
    L12:
        if (r1 != 9) goto L14;
        r3 = "avc3";
        goto L17
    L14:
        return null;
    L16:
        r3 = "dvhe";
        goto L17
    }
}
