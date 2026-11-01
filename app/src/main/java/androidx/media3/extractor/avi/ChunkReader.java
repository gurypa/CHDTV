package androidx.media3.extractor.avi;

import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.SeekMap;
import androidx.media3.extractor.SeekPoint;
import androidx.media3.extractor.TrackOutput;
import java.io.IOException;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
final class ChunkReader {
    private static final int CHUNK_TYPE_AUDIO = 1651965952;
    private static final int CHUNK_TYPE_VIDEO_COMPRESSED = 1667497984;
    private static final int CHUNK_TYPE_VIDEO_UNCOMPRESSED = 1650720768;
    private static final int INITIAL_INDEX_SIZE = 512;
    private final int alternativeChunkId;
    private int bytesRemainingInCurrentChunk;
    private final int chunkId;
    private int currentChunkIndex;
    private int currentChunkSize;
    private final long durationUs;
    private long firstIndexChunkOffset;
    private int indexChunkCount;
    private int indexSize;
    private int[] keyFrameIndices;
    private long[] keyFrameOffsets;
    private final int streamHeaderChunkCount;
    protected final TrackOutput trackOutput;

    public ChunkReader(int r5, int r6, long r7, int r9, TrackOutput r10) {
        boolean r1 = true;
        if (r6 == 1) goto L7;
        if (r6 == 2) goto L7;
        r1 = false;
    L7:
        Assertions.checkArgument(r1);
        this.durationUs = r7;
        this.streamHeaderChunkCount = r9;
        this.trackOutput = r10;
        if (r6 != 2) goto L10;
        int r2 = CHUNK_TYPE_VIDEO_COMPRESSED;
    L11:
        this.chunkId = getChunkIdFourCc(r5, r2);
        if (r6 != 2) goto L14;
        int r0 = getChunkIdFourCc(r5, CHUNK_TYPE_VIDEO_UNCOMPRESSED);
    L15:
        this.alternativeChunkId = r0;
        this.firstIndexChunkOffset = -1;
        this.keyFrameOffsets = new long[512];
        this.keyFrameIndices = new int[512];
        return;
    L14:
        r0 = -1;
        goto L15
    L10:
        r2 = CHUNK_TYPE_AUDIO;
        goto L11
    }

    private static int getChunkIdFourCc(int r4, int r5) {
        return ((((r4 % 10) + 48) << 8) | ((r4 / 10) + 48)) | r5;
    }

    private long getChunkTimestampUs(int r5) {
        return (this.durationUs * ((long) r5)) / ((long) this.streamHeaderChunkCount);
    }

    private SeekPoint getSeekPoint(int r7) {
        return new SeekPoint(((long) this.keyFrameIndices[r7]) * getFrameDurationUs(), this.keyFrameOffsets[r7]);
    }

    public void advanceCurrentChunk() {
        this.currentChunkIndex++;
    }

    public void appendIndexChunk(long r5, boolean r7) {
        if (this.firstIndexChunkOffset != (-1)) goto L5;
        this.firstIndexChunkOffset = r5;
    L5:
        if (r7 == true) goto L7;
    L10:
        this.indexChunkCount++;
        return;
    L7:
        if (this.indexSize != this.keyFrameIndices.length) goto L9;
        this.keyFrameOffsets = Arrays.copyOf(this.keyFrameOffsets, (this.keyFrameOffsets.length * 3) / 2);
        this.keyFrameIndices = Arrays.copyOf(this.keyFrameIndices, (this.keyFrameIndices.length * 3) / 2);
    L9:
        this.keyFrameOffsets[this.indexSize] = r5;
        this.keyFrameIndices[this.indexSize] = this.indexChunkCount;
        this.indexSize++;
        goto L10
    }

    public void compactIndex() {
        this.keyFrameOffsets = Arrays.copyOf(this.keyFrameOffsets, this.indexSize);
        this.keyFrameIndices = Arrays.copyOf(this.keyFrameIndices, this.indexSize);
    }

    public long getCurrentChunkTimestampUs() {
        return getChunkTimestampUs(this.currentChunkIndex);
    }

    public long getFrameDurationUs() {
        return getChunkTimestampUs(1);
    }

    public SeekMap.SeekPoints getSeekPoints(long r7) {
        if (this.indexSize == 0) goto L5;
        int r0 = (int) (r7 / getFrameDurationUs());
        int r1 = Util.binarySearchFloor(this.keyFrameIndices, r0, true, true);
        if (this.keyFrameIndices[r1] == r0) goto L9;
        SeekPoint r2 = getSeekPoint(r1);
        if ((r1 + 1) >= this.keyFrameOffsets.length) goto L15;
        return new SeekMap.SeekPoints(r2, getSeekPoint(r1 + 1));
    L15:
        return new SeekMap.SeekPoints(r2);
    L9:
        return new SeekMap.SeekPoints(getSeekPoint(r1));
    L5:
        return new SeekMap.SeekPoints(new SeekPoint(0, this.firstIndexChunkOffset));
    }

    public boolean handlesChunkId(int r2) {
        if (this.chunkId != r2) goto L5;
    L8:
        return true;
    L5:
        if (this.alternativeChunkId == r2) goto L8;
        return false;
    }

    public boolean isAudio() {
        if ((this.chunkId & CHUNK_TYPE_AUDIO) != CHUNK_TYPE_AUDIO) goto L5;
        return true;
    L5:
        return false;
    }

    public boolean isCurrentFrameAKeyFrame() {
        if (Arrays.binarySearch(this.keyFrameIndices, this.currentChunkIndex) < 0) goto L5;
        return true;
    L5:
        return false;
    }

    public boolean isVideo() {
        if ((this.chunkId & CHUNK_TYPE_VIDEO_COMPRESSED) != CHUNK_TYPE_VIDEO_COMPRESSED) goto L5;
        return true;
    L5:
        return false;
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    public boolean onChunkData(ExtractorInput r12) throws IOException {
        boolean r3 = false;
        this.bytesRemainingInCurrentChunk -= this.trackOutput.sampleData(r12, this.bytesRemainingInCurrentChunk, false);
        if (this.bytesRemainingInCurrentChunk != 0) goto L5;
        r3 = true;
    L5:
        if (r3 == true) goto L7;
    L10:
        return r3;
    L7:
        if (this.currentChunkSize <= 0) goto L9;
        TrackOutput r4 = this.trackOutput;
        long r5 = getCurrentChunkTimestampUs();
        boolean r7 = isCurrentFrameAKeyFrame();
        r4.sampleMetadata(r5, r7 ? 1 : 0, this.currentChunkSize, 0, null);
    L9:
        advanceCurrentChunk();
        goto L10
    }

    public void onChunkStart(int r1) {
        this.currentChunkSize = r1;
        this.bytesRemainingInCurrentChunk = r1;
    }

    public void seekToPosition(long r3) {
        if (this.indexSize != 0) goto L5;
        this.currentChunkIndex = 0;
        return;
    L5:
        int r0 = Util.binarySearchFloor(this.keyFrameOffsets, r3, true, true);
        this.currentChunkIndex = this.keyFrameIndices[r0];
    }
}
