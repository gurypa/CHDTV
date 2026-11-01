package androidx.media3.extractor;

import android.support.v4.media.session.PlaybackStateCompat;
import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.Metadata;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.ParsableBitArray;
import androidx.media3.common.util.Util;
import androidx.media3.extractor.metadata.flac.PictureFrame;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class FlacStreamMetadata {
    public static final int NOT_IN_LOOKUP_TABLE = -1;
    private static final String TAG = "FlacStreamMetadata";
    public final int bitsPerSample;
    public final int bitsPerSampleLookupKey;
    public final int channels;
    public final int maxBlockSizeSamples;
    public final int maxFrameSize;
    private final Metadata metadata;
    public final int minBlockSizeSamples;
    public final int minFrameSize;
    public final int sampleRate;
    public final int sampleRateLookupKey;
    public final SeekTable seekTable;
    public final long totalSamples;

    public static class SeekTable {
        public final long[] pointOffsets;
        public final long[] pointSampleNumbers;

        public SeekTable(long[] r1, long[] r2) {
            this.pointSampleNumbers = r1;
            this.pointOffsets = r2;
        }
    }

    private FlacStreamMetadata(int r2, int r3, int r4, int r5, int r6, int r7, int r8, long r9, SeekTable r11, Metadata r12) {
        this.minBlockSizeSamples = r2;
        this.maxBlockSizeSamples = r3;
        this.minFrameSize = r4;
        this.maxFrameSize = r5;
        this.sampleRate = r6;
        this.sampleRateLookupKey = getSampleRateLookupKey(r6);
        this.channels = r7;
        this.bitsPerSample = r8;
        this.bitsPerSampleLookupKey = getBitsPerSampleLookupKey(r8);
        this.totalSamples = r9;
        this.seekTable = r11;
        this.metadata = r12;
    }

    public FlacStreamMetadata(int r13, int r14, int r15, int r16, int r17, int r18, int r19, long r20, ArrayList<String> r22, ArrayList<PictureFrame> r23) {
        this(r13, r14, r15, r16, r17, r18, r19, r20, null, concatenateVorbisMetadata(r22, r23));
    }

    public FlacStreamMetadata(byte[] r4, int r5) {
        ParsableBitArray r0 = new ParsableBitArray(r4);
        r0.setPosition(r5 * 8);
        this.minBlockSizeSamples = r0.readBits(16);
        this.maxBlockSizeSamples = r0.readBits(16);
        this.minFrameSize = r0.readBits(24);
        this.maxFrameSize = r0.readBits(24);
        this.sampleRate = r0.readBits(20);
        this.sampleRateLookupKey = getSampleRateLookupKey(this.sampleRate);
        this.channels = r0.readBits(3) + 1;
        this.bitsPerSample = r0.readBits(5) + 1;
        this.bitsPerSampleLookupKey = getBitsPerSampleLookupKey(this.bitsPerSample);
        this.totalSamples = r0.readBitsToLong(36);
        this.seekTable = null;
        this.metadata = null;
    }

    private static Metadata concatenateVorbisMetadata(List<String> r2, List<PictureFrame> r3) {
        Metadata r0 = VorbisUtil.parseVorbisComments(r2);
        if (r0 != null) goto L9;
        if (r3.isEmpty() == false) goto L9;
        return null;
    L9:
        return new Metadata(r3).copyWithAppendedEntriesFrom(r0);
    }

    private static int getBitsPerSampleLookupKey(int r1) {
        switch(r1) {
            case 8: goto L13;
            case 12: goto L11;
            case 16: goto L9;
            case 20: goto L7;
            case 24: goto L5;
            default: goto L3;
        };
    L3:
        return -1;
    L5:
        return 6;
    L7:
        return 5;
    L9:
        return 4;
    L11:
        return 2;
    L13:
        return 1;
    }

    private static int getSampleRateLookupKey(int r1) {
        switch(r1) {
            case 8000: goto L25;
            case 16000: goto L23;
            case 22050: goto L21;
            case 24000: goto L19;
            case 32000: goto L17;
            case 44100: goto L15;
            case 48000: goto L13;
            case 88200: goto L11;
            case 96000: goto L9;
            case 176400: goto L7;
            case 192000: goto L5;
            default: goto L3;
        };
    L3:
        return -1;
    L5:
        return 3;
    L7:
        return 2;
    L9:
        return 11;
    L11:
        return 1;
    L13:
        return 10;
    L15:
        return 9;
    L17:
        return 8;
    L19:
        return 7;
    L21:
        return 6;
    L23:
        return 5;
    L25:
        return 4;
    }

    public FlacStreamMetadata copyWithPictureFrames(List<PictureFrame> r14) {
        Metadata r12 = getMetadataCopyWithAppendedEntriesFrom(new Metadata(r14));
        return new FlacStreamMetadata(this.minBlockSizeSamples, this.maxBlockSizeSamples, this.minFrameSize, this.maxFrameSize, this.sampleRate, this.channels, this.bitsPerSample, this.totalSamples, this.seekTable, r12);
    }

    public FlacStreamMetadata copyWithSeekTable(SeekTable r13) {
        return new FlacStreamMetadata(this.minBlockSizeSamples, this.maxBlockSizeSamples, this.minFrameSize, this.maxFrameSize, this.sampleRate, this.channels, this.bitsPerSample, this.totalSamples, r13, this.metadata);
    }

    public FlacStreamMetadata copyWithVorbisComments(List<String> r14) {
        Metadata r12 = getMetadataCopyWithAppendedEntriesFrom(VorbisUtil.parseVorbisComments(r14));
        return new FlacStreamMetadata(this.minBlockSizeSamples, this.maxBlockSizeSamples, this.minFrameSize, this.maxFrameSize, this.sampleRate, this.channels, this.bitsPerSample, this.totalSamples, this.seekTable, r12);
    }

    public long getApproxBytesPerFrame() {
        if (this.maxFrameSize <= 0) goto L6;
        return ((((long) this.maxFrameSize) + ((long) this.minFrameSize)) / 2) + 1;
    L6:
        if (this.minBlockSizeSamples == this.maxBlockSizeSamples) goto L8;
    L10:
        long r0 = PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM;
    L12:
        return (((((long) this.channels) * r0) * ((long) this.bitsPerSample)) / 8) + 64;
    L8:
        if (this.minBlockSizeSamples <= 0) goto L10;
        r0 = this.minBlockSizeSamples;
        goto L12
    }

    public int getDecodedBitrate() {
        return (this.bitsPerSample * this.sampleRate) * this.channels;
    }

    public long getDurationUs() {
        if (this.totalSamples != 0) goto L6;
        return C0180C.TIME_UNSET;
    L6:
        return (this.totalSamples * 1000000) / ((long) this.sampleRate);
    }

    public Format getFormat(byte[] r5, Metadata r6) {
        r5[4] = -128;
        if (this.maxFrameSize <= 0) goto L5;
        int r0 = this.maxFrameSize;
    L6:
        Metadata r1 = getMetadataCopyWithAppendedEntriesFrom(r6);
        return new Format.Builder().setSampleMimeType(MimeTypes.AUDIO_FLAC).setMaxInputSize(r0).setChannelCount(this.channels).setSampleRate(this.sampleRate).setPcmEncoding(Util.getPcmEncoding(this.bitsPerSample)).setInitializationData(Collections.singletonList(r5)).setMetadata(r1).build();
    L5:
        r0 = -1;
        goto L6
    }

    public int getMaxDecodedFrameSize() {
        return (this.maxBlockSizeSamples * this.channels) * (this.bitsPerSample / 8);
    }

    public Metadata getMetadataCopyWithAppendedEntriesFrom(Metadata r2) {
        if (this.metadata != null) goto L6;
        return r2;
    L6:
        return this.metadata.copyWithAppendedEntriesFrom(r2);
    }

    public long getSampleNumber(long r11) {
        return Util.constrainValue((((long) this.sampleRate) * r11) / 1000000, 0, this.totalSamples - 1);
    }
}
