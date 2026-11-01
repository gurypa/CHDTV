package androidx.media3.extractor.avi;

import androidx.media3.common.util.Log;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.Util;

/* JADX INFO: loaded from: classes.dex */
final class AviStreamHeaderChunk implements AviChunk {
    private static final String TAG = "AviStreamHeaderChunk";
    public final int initialFrames;
    public final int length;
    public final int rate;
    public final int scale;
    public final int streamType;
    public final int suggestedBufferSize;

    private AviStreamHeaderChunk(int r1, int r2, int r3, int r4, int r5, int r6) {
        this.streamType = r1;
        this.initialFrames = r2;
        this.scale = r3;
        this.rate = r4;
        this.length = r5;
        this.suggestedBufferSize = r6;
    }

    public static AviStreamHeaderChunk parseFrom(ParsableByteArray r7) {
        int r1 = r7.readLittleEndianInt();
        r7.skipBytes(12);
        int r2 = r7.readLittleEndianInt();
        int r3 = r7.readLittleEndianInt();
        int r4 = r7.readLittleEndianInt();
        r7.skipBytes(4);
        int r5 = r7.readLittleEndianInt();
        int r6 = r7.readLittleEndianInt();
        r7.skipBytes(8);
        return new AviStreamHeaderChunk(r1, r2, r3, r4, r5, r6);
    }

    public long getDurationUs() {
        return Util.scaleLargeTimestamp(this.length, ((long) this.scale) * 1000000, this.rate);
    }

    public float getFrameRate() {
        return this.rate / this.scale;
    }

    public int getTrackType() {
        switch(this.streamType) {
            case 1935960438: goto L10;
            case 1935963489: goto L8;
            case 1937012852: goto L6;
            default: goto L4;
        };
    L4:
        Log.m48w(TAG, "Found unsupported streamType fourCC: " + Integer.toHexString(this.streamType));
        return -1;
    L6:
        return 3;
    L8:
        return 1;
    L10:
        return 2;
    }

    @Override // androidx.media3.extractor.avi.AviChunk
    public int getType() {
        return AviExtractor.FOURCC_strh;
    }
}
