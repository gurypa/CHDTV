package androidx.media3.extractor.p001ts;

import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.ParserException;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.ParsableBitArray;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.Util;
import androidx.media3.extractor.AacUtil;
import androidx.media3.extractor.DiscardingTrackOutput;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.TrackOutput;
import java.util.Arrays;
import java.util.Collections;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;

/* JADX INFO: loaded from: classes.dex */
public final class AdtsReader implements ElementaryStreamReader {
    private static final int CRC_SIZE = 2;
    private static final int HEADER_SIZE = 5;
    private static final int ID3_HEADER_SIZE = 10;
    private static final byte[] ID3_IDENTIFIER = null;
    private static final int ID3_SIZE_OFFSET = 6;
    private static final int MATCH_STATE_FF = 512;
    private static final int MATCH_STATE_I = 768;
    private static final int MATCH_STATE_ID = 1024;
    private static final int MATCH_STATE_START = 256;
    private static final int MATCH_STATE_VALUE_SHIFT = 8;
    private static final int STATE_CHECKING_ADTS_HEADER = 1;
    private static final int STATE_FINDING_SAMPLE = 0;
    private static final int STATE_READING_ADTS_HEADER = 3;
    private static final int STATE_READING_ID3_HEADER = 2;
    private static final int STATE_READING_SAMPLE = 4;
    private static final String TAG = "AdtsReader";
    private static final int VERSION_UNSET = -1;
    private final ParsableBitArray adtsScratch;
    private int bytesRead;
    private int currentFrameVersion;
    private TrackOutput currentOutput;
    private long currentSampleDuration;
    private final boolean exposeId3;
    private int firstFrameSampleRateIndex;
    private int firstFrameVersion;
    private String formatId;
    private boolean foundFirstFrame;
    private boolean hasCrc;
    private boolean hasOutputFormat;
    private final ParsableByteArray id3HeaderBuffer;
    private TrackOutput id3Output;
    private final String language;
    private int matchState;
    private TrackOutput output;
    private final int roleFlags;
    private long sampleDurationUs;
    private int sampleSize;
    private int state;
    private long timeUs;

    static {
        ID3_IDENTIFIER = new byte[]{73, 68, 51};
    }

    public AdtsReader(boolean r3) {
        this(r3, null, 0);
    }

    public AdtsReader(boolean r4, String r5, int r6) {
        this.adtsScratch = new ParsableBitArray(new byte[7]);
        this.id3HeaderBuffer = new ParsableByteArray(Arrays.copyOf(ID3_IDENTIFIER, 10));
        setFindingSampleState();
        this.firstFrameVersion = -1;
        this.firstFrameSampleRateIndex = -1;
        this.sampleDurationUs = C0180C.TIME_UNSET;
        this.timeUs = C0180C.TIME_UNSET;
        this.exposeId3 = r4;
        this.language = r5;
        this.roleFlags = r6;
    }

    @EnsuresNonNull({"output", "currentOutput", "id3Output"})
    private void assertTracksCreated() {
        Assertions.checkNotNull(this.output);
        Util.castNonNull(this.currentOutput);
        Util.castNonNull(this.id3Output);
    }

    private void checkAdtsHeader(ParsableByteArray r4) {
        if (r4.bytesLeft() != 0) goto L5;
        return;
    L5:
        this.adtsScratch.data[0] = r4.getData()[r4.getPosition()];
        this.adtsScratch.setPosition(2);
        int r0 = this.adtsScratch.readBits(4);
        if (this.firstFrameSampleRateIndex == (-1)) goto L12;
        if (r0 == this.firstFrameSampleRateIndex) goto L12;
        resetSync();
        return;
    L12:
        if (this.foundFirstFrame == true) goto L14;
        this.foundFirstFrame = true;
        this.firstFrameVersion = this.currentFrameVersion;
        this.firstFrameSampleRateIndex = r0;
    L14:
        setReadingAdtsHeaderState();
    }

    private boolean checkSyncPositionValid(ParsableByteArray r10, int r11) {
        r10.setPosition(r11 + 1);
        if (tryRead(r10, this.adtsScratch.data, 1) == true) goto L5;
        return false;
    L5:
        this.adtsScratch.setPosition(4);
        int r0 = this.adtsScratch.readBits(1);
        if (this.firstFrameVersion == (-1)) goto L11;
        if (r0 == this.firstFrameVersion) goto L11;
        return false;
    L11:
        if (this.firstFrameSampleRateIndex == (-1)) goto L20;
        if (tryRead(r10, this.adtsScratch.data, 1) == true) goto L15;
        return true;
    L15:
        this.adtsScratch.setPosition(2);
        if (this.adtsScratch.readBits(4) == this.firstFrameSampleRateIndex) goto L18;
        return false;
    L18:
        r10.setPosition(r11 + 2);
    L20:
        if (tryRead(r10, this.adtsScratch.data, 4) == true) goto L22;
        return true;
    L22:
        this.adtsScratch.setPosition(14);
        int r3 = this.adtsScratch.readBits(13);
        if (r3 >= 7) goto L25;
        return false;
    L25:
        byte[] r4 = r10.getData();
        int r6 = r10.limit();
        int r7 = r11 + r3;
        if (r7 < r6) goto L29;
        return true;
    L29:
        if (r4[r7] != (-1)) goto L41;
        if ((r7 + 1) != r6) goto L34;
        return true;
    L34:
        if (isAdtsSyncBytes((byte) -1, r4[r7 + 1]) == false) goto L39;
        if (((r4[r7 + 1] & 8) >> 3) != r0) goto L39;
        return true;
    L39:
        return false;
    L41:
        if (r4[r7] == 73) goto L44;
        return false;
    L44:
        if ((r7 + 1) != r6) goto L47;
        return true;
    L47:
        if (r4[r7 + 1] == 68) goto L50;
        return false;
    L50:
        if ((r7 + 2) != r6) goto L53;
        return true;
    L53:
        if (r4[r7 + 2] != 51) goto L56;
        return true;
    L56:
        return false;
    }

    private boolean continueRead(ParsableByteArray r3, byte[] r4, int r5) {
        int r0 = Math.min(r3.bytesLeft(), r5 - this.bytesRead);
        r3.readBytes(r4, this.bytesRead, r0);
        this.bytesRead += r0;
        if (this.bytesRead != r5) goto L5;
        return true;
    L5:
        return false;
    }

    private void findNextSample(ParsableByteArray r8) {
        byte[] r0 = r8.getData();
        int r1 = r8.getPosition();
        int r2 = r8.limit();
    L3:
        if (r1 >= r2) goto L34;
        int r3 = r1 + 1;
        int r4 = r0[r1] & 255;
        if (this.matchState != 512) goto L23;
        if (isAdtsSyncBytes((byte) -1, (byte) r4) == false) goto L23;
        if (this.foundFirstFrame == true) goto L12;
        if (checkSyncPositionValid(r8, r3 - 2) == false) goto L23;
    L12:
        this.currentFrameVersion = (r4 & 8) >> 3;
        if ((r4 & 1) != 0) goto L15;
        boolean r5 = true;
    L16:
        this.hasCrc = r5;
        if (this.foundFirstFrame == true) goto L19;
        setCheckingAdtsHeaderState();
    L20:
        r8.setPosition(r3);
        return;
    L19:
        setReadingAdtsHeaderState();
        goto L20
    L15:
        r5 = false;
    L23:
        switch((this.matchState | r4)) {
            case 329: goto L31;
            case 511: goto L30;
            case 836: goto L29;
            case 1075: goto L27;
            default: goto L25;
        };
    L25:
        if (this.matchState != 256) goto L26;
    L32:
        r1 = r3;
        goto L3
    L26:
        this.matchState = 256;
        r1 = r3 - 1;
        goto L3
    L27:
        setReadingId3HeaderState();
        r8.setPosition(r3);
        return;
    L29:
        this.matchState = 1024;
        goto L32
    L30:
        this.matchState = 512;
        goto L32
    L31:
        this.matchState = MATCH_STATE_I;
        goto L32
    L34:
        r8.setPosition(r1);
    }

    private boolean isAdtsSyncBytes(byte r3, byte r4) {
        return isAdtsSyncWord(((r3 & 255) << 8) | (r4 & 255));
    }

    public static boolean isAdtsSyncWord(int r2) {
        if ((65526 & r2) != 65520) goto L5;
        return true;
    L5:
        return false;
    }

    @RequiresNonNull({"output"})
    private void parseAdtsHeader() throws ParserException {
        this.adtsScratch.setPosition(0);
        if (this.hasOutputFormat == true) goto L8;
        int r0 = this.adtsScratch.readBits(2) + 1;
        if (r0 == 2) goto L7;
        Log.m48w(TAG, "Detected audio object type: " + r0 + ", but assuming AAC LC.");
        r0 = 2;
    L7:
        this.adtsScratch.skipBits(5);
        byte[] r5 = AacUtil.buildAudioSpecificConfig(r0, this.firstFrameSampleRateIndex, this.adtsScratch.readBits(3));
        AacUtil.Config r6 = AacUtil.parseAudioSpecificConfig(r5);
        Format r7 = new Format.Builder().setId(this.formatId).setSampleMimeType(MimeTypes.AUDIO_AAC).setCodecs(r6.codecs).setChannelCount(r6.channelCount).setSampleRate(r6.sampleRateHz).setInitializationData(Collections.singletonList(r5)).setLanguage(this.language).setRoleFlags(this.roleFlags).build();
        this.sampleDurationUs = 1024000000 / ((long) r7.sampleRate);
        this.output.format(r7);
        this.hasOutputFormat = true;
    L9:
        this.adtsScratch.skipBits(4);
        int r1 = (this.adtsScratch.readBits(13) - 2) - 5;
        if (this.hasCrc == false) goto L12;
        int r8 = r1 - 2;
    L13:
        setReadingSampleState(this.output, this.sampleDurationUs, 0, r8);
        return;
    L12:
        r8 = r1;
        goto L13
    L8:
        this.adtsScratch.skipBits(10);
        goto L9
    }

    @RequiresNonNull({"id3Output"})
    private void parseId3Header() {
        this.id3Output.sampleData(this.id3HeaderBuffer, 10);
        this.id3HeaderBuffer.setPosition(6);
        setReadingSampleState(this.id3Output, 0, 10, this.id3HeaderBuffer.readSynchSafeInt() + 10);
    }

    @RequiresNonNull({"currentOutput"})
    private void readSample(ParsableByteArray r10) {
        int r0 = Math.min(r10.bytesLeft(), this.sampleSize - this.bytesRead);
        this.currentOutput.sampleData(r10, r0);
        this.bytesRead += r0;
        if (this.bytesRead == this.sampleSize) goto L5;
        return;
    L5:
        if (this.timeUs == C0180C.TIME_UNSET) goto L7;
        boolean r1 = true;
    L8:
        Assertions.checkState(r1);
        this.currentOutput.sampleMetadata(this.timeUs, 1, this.sampleSize, 0, null);
        this.timeUs += this.currentSampleDuration;
        setFindingSampleState();
        return;
    L7:
        r1 = false;
        goto L8
    }

    private void resetSync() {
        this.foundFirstFrame = false;
        setFindingSampleState();
    }

    private void setCheckingAdtsHeaderState() {
        this.state = 1;
        this.bytesRead = 0;
    }

    private void setFindingSampleState() {
        this.state = 0;
        this.bytesRead = 0;
        this.matchState = 256;
    }

    private void setReadingAdtsHeaderState() {
        this.state = 3;
        this.bytesRead = 0;
    }

    private void setReadingId3HeaderState() {
        this.state = 2;
        this.bytesRead = ID3_IDENTIFIER.length;
        this.sampleSize = 0;
        this.id3HeaderBuffer.setPosition(0);
    }

    private void setReadingSampleState(TrackOutput r2, long r3, int r5, int r6) {
        this.state = 4;
        this.bytesRead = r5;
        this.currentOutput = r2;
        this.currentSampleDuration = r3;
        this.sampleSize = r6;
    }

    private boolean tryRead(ParsableByteArray r3, byte[] r4, int r5) {
        if (r3.bytesLeft() >= r5) goto L5;
        return false;
    L5:
        r3.readBytes(r4, 0, r5);
        return true;
    }

    @Override // androidx.media3.extractor.p001ts.ElementaryStreamReader
    public void consume(ParsableByteArray r3) throws ParserException {
        assertTracksCreated();
    L4:
        if (r3.bytesLeft() <= 0) goto L22;
        switch(this.state) {
            case 0: goto L21;
            case 1: goto L20;
            case 2: goto L18;
            case 3: goto L11;
            case 4: goto L9;
            default: goto L8;
        };
    L8:
        throw new IllegalStateException();
    L11:
        if (this.hasCrc == false) goto L13;
        int r0 = 7;
    L15:
        if (continueRead(r3, this.adtsScratch.data, r0) == false) goto L4;
        parseAdtsHeader();
        goto L4
    L13:
        r0 = 5;
        goto L15
    L18:
        if (continueRead(r3, this.id3HeaderBuffer.getData(), 10) == false) goto L4;
        parseId3Header();
        goto L4
    L20:
        checkAdtsHeader(r3);
        goto L4
    L21:
        findNextSample(r3);
        goto L4
    L9:
        readSample(r3);
        goto L4
    }

    @Override // androidx.media3.extractor.p001ts.ElementaryStreamReader
    public void createTracks(ExtractorOutput r4, TsPayloadReader.TrackIdGenerator r5) {
        r5.generateNewId();
        this.formatId = r5.getFormatId();
        this.output = r4.track(r5.getTrackId(), 1);
        this.currentOutput = this.output;
        if (this.exposeId3 == false) goto L5;
        r5.generateNewId();
        this.id3Output = r4.track(r5.getTrackId(), 5);
        this.id3Output.format(new Format.Builder().setId(r5.getFormatId()).setSampleMimeType(MimeTypes.APPLICATION_ID3).build());
        return;
    L5:
        this.id3Output = new DiscardingTrackOutput();
    }

    public long getSampleDurationUs() {
        return this.sampleDurationUs;
    }

    @Override // androidx.media3.extractor.p001ts.ElementaryStreamReader
    public void packetFinished(boolean r1) {
    }

    @Override // androidx.media3.extractor.p001ts.ElementaryStreamReader
    public void packetStarted(long r1, int r3) {
        this.timeUs = r1;
    }

    @Override // androidx.media3.extractor.p001ts.ElementaryStreamReader
    public void seek() {
        this.timeUs = C0180C.TIME_UNSET;
        resetSync();
    }
}
