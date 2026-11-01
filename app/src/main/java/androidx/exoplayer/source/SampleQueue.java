package androidx.media3.exoplayer.source;

import android.os.Looper;
import androidx.media3.common.C0180C;
import androidx.media3.common.DataReader;
import androidx.media3.common.DrmInitData;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.Util;
import androidx.media3.decoder.DecoderInputBuffer;
import androidx.media3.exoplayer.FormatHolder;
import androidx.media3.exoplayer.analytics.PlayerId;
import androidx.media3.exoplayer.drm.DrmSession;
import androidx.media3.exoplayer.drm.DrmSessionEventListener;
import androidx.media3.exoplayer.drm.DrmSessionManager;
import androidx.media3.exoplayer.upstream.Allocator;
import androidx.media3.extractor.TrackOutput;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public class SampleQueue implements TrackOutput {
    static final int SAMPLE_CAPACITY_INCREMENT = 1000;
    private static final String TAG = "SampleQueue";
    private int absoluteFirstIndex;
    private boolean allSamplesAreSyncSamples;
    private int capacity;
    private TrackOutput.CryptoData[] cryptoDatas;
    private DrmSession currentDrmSession;
    private Format downstreamFormat;
    private final DrmSessionEventListener.EventDispatcher drmEventDispatcher;
    private final DrmSessionManager drmSessionManager;
    private final SampleExtrasHolder extrasHolder;
    private int[] flags;
    private boolean isLastSampleQueued;
    private long largestDiscardedTimestampUs;
    private long largestQueuedTimestampUs;
    private int length;
    private boolean loggedUnexpectedNonSyncSample;
    private long[] offsets;
    private boolean pendingSplice;
    private int readPosition;
    private int relativeFirstIndex;
    private final SampleDataQueue sampleDataQueue;
    private long sampleOffsetUs;
    private final SpannedData<SharedSampleMetadata> sharedSampleMetadata;
    private int[] sizes;
    private long[] sourceIds;
    private long startTimeUs;
    private long[] timesUs;
    private Format unadjustedUpstreamFormat;
    private Format upstreamFormat;
    private boolean upstreamFormatAdjustmentRequired;
    private UpstreamFormatChangedListener upstreamFormatChangeListener;
    private boolean upstreamFormatRequired;
    private boolean upstreamKeyframeRequired;
    private long upstreamSourceId;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.source.SampleQueue$1 */
    static /* synthetic */ class C03471 {
    }

    static final class SampleExtrasHolder {
        public TrackOutput.CryptoData cryptoData;
        public long offset;
        public int size;

        SampleExtrasHolder() {
        }
    }

    private static final class SharedSampleMetadata {
        public final DrmSessionManager.DrmSessionReference drmSessionReference;
        public final Format format;

        private SharedSampleMetadata(Format r1, DrmSessionManager.DrmSessionReference r2) {
            this.format = r1;
            this.drmSessionReference = r2;
        }

        /* synthetic */ SharedSampleMetadata(Format r1, DrmSessionManager.DrmSessionReference r2, C03471 r3) {
            this(r1, r2);
        }
    }

    public interface UpstreamFormatChangedListener {
        void onUpstreamFormatChanged(Format r1);
    }

    protected SampleQueue(Allocator r3, DrmSessionManager r4, DrmSessionEventListener.EventDispatcher r5) {
        this.drmSessionManager = r4;
        this.drmEventDispatcher = r5;
        this.sampleDataQueue = new SampleDataQueue(r3);
        this.extrasHolder = new SampleExtrasHolder();
        this.capacity = 1000;
        this.sourceIds = new long[this.capacity];
        this.offsets = new long[this.capacity];
        this.timesUs = new long[this.capacity];
        this.flags = new int[this.capacity];
        this.sizes = new int[this.capacity];
        this.cryptoDatas = new TrackOutput.CryptoData[this.capacity];
        this.sharedSampleMetadata = new SpannedData(new SampleQueue$$ExternalSyntheticLambda0());
        this.startTimeUs = Long.MIN_VALUE;
        this.largestDiscardedTimestampUs = Long.MIN_VALUE;
        this.largestQueuedTimestampUs = Long.MIN_VALUE;
        this.upstreamFormatRequired = true;
        this.upstreamKeyframeRequired = true;
        this.allSamplesAreSyncSamples = true;
    }

    private synchronized boolean attemptSplice(long r6) {
        monitor-enter(this);
        boolean r1 = true;
        if (this.length != 0) goto L14;
        if (r6 > this.largestDiscardedTimestampUs) goto L10;
        r1 = false;
    L10:
        monitor-exit(this);
        return r1;
    L14:
        if (getLargestReadTimestampUs() < r6) goto L17;
        monitor-exit(this);
        return false;
    L17:
        discardUpstreamSampleMetadata(this.absoluteFirstIndex + countUnreadSamplesBefore(r6));     // Catch: Throwable -> L20
        monitor-exit(this);
        return true;
    L20:
        th = move-exception;
        throw th;
    }

    private synchronized void commitSample(long r15, int r17, long r18, int r20, TrackOutput.CryptoData r21) {
        monitor-enter(this);
    L29:
        th = move-exception;
        throw th;
    L5:
        if (this.length <= 0) goto L12;
        int r2 = getRelativeIndex(this.length - 1);     // Catch: Throwable -> L29
        if ((this.offsets[r2] + ((long) this.sizes[r2])) > r18) goto L9;
        boolean r5 = true;
    L10:
        Assertions.checkArgument(r5);     // Catch: Throwable -> L29
        goto L12
    L9:
        r5 = false;
    L12:
        if ((r17 & C0180C.BUFFER_FLAG_LAST_SAMPLE) == 0) goto L14;
        boolean r3 = true;
    L15:
        this.isLastSampleQueued = r3;     // Catch: Throwable -> L29
        this.largestQueuedTimestampUs = Math.max(this.largestQueuedTimestampUs, r15);     // Catch: Throwable -> L29
        int r4 = getRelativeIndex(this.length);     // Catch: Throwable -> L29
        this.timesUs[r4] = r15;     // Catch: Throwable -> L29
        this.offsets[r4] = r18;     // Catch: Throwable -> L29
        this.sizes[r4] = r20;     // Catch: Throwable -> L29
        this.flags[r4] = r17;     // Catch: Throwable -> L29
        this.cryptoDatas[r4] = r21;     // Catch: Throwable -> L29
        this.sourceIds[r4] = this.upstreamSourceId;     // Catch: Throwable -> L29
        if (this.sharedSampleMetadata.isEmpty() == false) goto L18;
    L19:
        Format r6 = (Format) Assertions.checkNotNull(this.upstreamFormat);     // Catch: Throwable -> L29
        if (this.drmSessionManager == null) goto L22;
        DrmSessionManager.DrmSessionReference r7 = this.drmSessionManager.preacquireSession(this.drmEventDispatcher, r6);     // Catch: Throwable -> L29
    L23:
        this.sharedSampleMetadata.appendSpan(getWriteIndex(), new SharedSampleMetadata(r6, r7, null));     // Catch: Throwable -> L29
    L24:
        this.length++;     // Catch: Throwable -> L29
        if (this.length != this.capacity) goto L27;
        int r8 = this.capacity + 1000;     // Catch: Throwable -> L29
        long[] r9 = new long[r8];     // Catch: Throwable -> L29
        long[] r10 = new long[r8];     // Catch: Throwable -> L29
        long[] r11 = new long[r8];     // Catch: Throwable -> L29
        int[] r12 = new int[r8];     // Catch: Throwable -> L29
        int[] r13 = new int[r8];     // Catch: Throwable -> L29
        TrackOutput.CryptoData[] r14 = new TrackOutput.CryptoData[r8];     // Catch: Throwable -> L29
        int r16 = this.capacity - this.relativeFirstIndex;     // Catch: Throwable -> L29
        System.arraycopy(this.offsets, this.relativeFirstIndex, r10, 0, r16);     // Catch: Throwable -> L29
        System.arraycopy(this.timesUs, this.relativeFirstIndex, r11, 0, r16);     // Catch: Throwable -> L29
        System.arraycopy(this.flags, this.relativeFirstIndex, r12, 0, r16);     // Catch: Throwable -> L29
        System.arraycopy(this.sizes, this.relativeFirstIndex, r13, 0, r16);     // Catch: Throwable -> L29
        System.arraycopy(this.cryptoDatas, this.relativeFirstIndex, r14, 0, r16);     // Catch: Throwable -> L29
        System.arraycopy(this.sourceIds, this.relativeFirstIndex, r9, 0, r16);     // Catch: Throwable -> L29
        int r19 = this.relativeFirstIndex;     // Catch: Throwable -> L29
        System.arraycopy(this.offsets, 0, r10, r16, r19);     // Catch: Throwable -> L29
        System.arraycopy(this.timesUs, 0, r11, r16, r19);     // Catch: Throwable -> L29
        System.arraycopy(this.flags, 0, r12, r16, r19);     // Catch: Throwable -> L29
        System.arraycopy(this.sizes, 0, r13, r16, r19);     // Catch: Throwable -> L29
        System.arraycopy(this.cryptoDatas, 0, r14, r16, r19);     // Catch: Throwable -> L29
        System.arraycopy(this.sourceIds, 0, r9, r16, r19);     // Catch: Throwable -> L29
        this.offsets = r10;     // Catch: Throwable -> L29
        this.timesUs = r11;     // Catch: Throwable -> L29
        this.flags = r12;     // Catch: Throwable -> L29
        this.sizes = r13;     // Catch: Throwable -> L29
        this.cryptoDatas = r14;     // Catch: Throwable -> L29
        this.sourceIds = r9;     // Catch: Throwable -> L29
        this.relativeFirstIndex = 0;     // Catch: Throwable -> L29
        this.capacity = r8;     // Catch: Throwable -> L29
    L27:
        monitor-exit(this);
        return;
    L22:
        r7 = DrmSessionManager.DrmSessionReference.EMPTY;     // Catch: Throwable -> L29
        goto L23
    L18:
        if (this.sharedSampleMetadata.getEndValue().format.equals(this.upstreamFormat) == true) goto L24;
    L14:
        r3 = false;
        goto L15
    }

    private int countUnreadSamplesBefore(long r6) {
        int r0 = this.length;
        int r1 = getRelativeIndex(this.length - 1);
    L4:
        if (r0 <= this.readPosition) goto L10;
        if (this.timesUs[r1] < r6) goto L10;
        r0 = r0 - 1;
        r1 = r1 - 1;
        if (r1 != (-1)) goto L4;
        r1 = this.capacity - 1;
    L10:
        return r0;
    }

    @Deprecated
    public static SampleQueue createWithDrm(Allocator r3, Looper r4, DrmSessionManager r5, DrmSessionEventListener.EventDispatcher r6) {
        r5.setPlayer(r4, PlayerId.UNSET);
        return new SampleQueue(r3, (DrmSessionManager) Assertions.checkNotNull(r5), (DrmSessionEventListener.EventDispatcher) Assertions.checkNotNull(r6));
    }

    public static SampleQueue createWithDrm(Allocator r3, DrmSessionManager r4, DrmSessionEventListener.EventDispatcher r5) {
        return new SampleQueue(r3, (DrmSessionManager) Assertions.checkNotNull(r4), (DrmSessionEventListener.EventDispatcher) Assertions.checkNotNull(r5));
    }

    public static SampleQueue createWithoutDrm(Allocator r2) {
        return new SampleQueue(r2, null, null);
    }

    private synchronized long discardSampleMetadataTo(long r10, boolean r12, boolean r13) {
        monitor-enter(this);
    L30:
        th = th;
    L32:
        Throwable r11 = th;
    L33:
        monitor-exit(this);     // Catch: Throwable -> L35
        throw r11;
    L4:
        if (this.length == 0) goto L28;
        if (r10 < this.timesUs[this.relativeFirstIndex]) goto L28;
        if (r13 == true) goto L37;
    L16:
        int r0 = this.length;     // Catch: Throwable -> L30
    L17:
        int r5 = r0;
        int r14 = findSampleBefore(this.relativeFirstIndex, r5, r10, r12);     // Catch: Throwable -> L35
        if (r14 != (-1)) goto L24;
        monitor-exit(this);
        return -1;
    L24:
        long r15 = discardSamples(r14);     // Catch: Throwable -> L35
        monitor-exit(this);
        return r15;
    L37:
    L14:
        th = move-exception;
        r11 = th;
        goto L33
    L11:
        if (this.readPosition == this.length) goto L16;
        r0 = this.readPosition + 1;
    L28:
        monitor-exit(this);
        return -1;
    L35:
        th = th;
        goto L32
    }

    private synchronized long discardSampleMetadataToEnd() {
        monitor-enter(this);
    L11:
        th = move-exception;
        throw th;
    L4:
        if (this.length != 0) goto L8;
        monitor-exit(this);
        return -1;
    L8:
        long r0 = discardSamples(this.length);     // Catch: Throwable -> L11
        monitor-exit(this);
        return r0;
    }

    private long discardSamples(int r7) {
        this.largestDiscardedTimestampUs = Math.max(this.largestDiscardedTimestampUs, getLargestTimestamp(r7));
        this.length -= r7;
        this.absoluteFirstIndex += r7;
        this.relativeFirstIndex += r7;
        if (this.relativeFirstIndex < this.capacity) goto L5;
        this.relativeFirstIndex -= this.capacity;
    L5:
        this.readPosition -= r7;
        if (this.readPosition >= 0) goto L8;
        this.readPosition = 0;
    L8:
        this.sharedSampleMetadata.discardTo(this.absoluteFirstIndex);
        if (this.length != 0) goto L17;
        if (this.relativeFirstIndex != 0) goto L13;
        int r0 = this.capacity;
    L14:
        int r1 = r0 - 1;
        return this.offsets[r1] + ((long) this.sizes[r1]);
    L13:
        r0 = this.relativeFirstIndex;
        goto L14
    L17:
        return this.offsets[this.relativeFirstIndex];
    }

    private long discardUpstreamSampleMetadata(int r8) {
        int r0 = getWriteIndex() - r8;
        boolean r1 = false;
        if (r0 >= 0) goto L5;
    L7:
        boolean r3 = false;
    L8:
        Assertions.checkArgument(r3);
        this.length -= r0;
        this.largestQueuedTimestampUs = Math.max(this.largestDiscardedTimestampUs, getLargestTimestamp(this.length));
        if (r0 == 0) goto L11;
    L13:
        this.isLastSampleQueued = r1;
        this.sharedSampleMetadata.discardFrom(r8);
        if (this.length == 0) goto L17;
        int r2 = getRelativeIndex(this.length - 1);
        return this.offsets[r2] + ((long) this.sizes[r2]);
    L17:
        return 0;
    L11:
        if (this.isLastSampleQueued == false) goto L13;
        r1 = true;
        goto L13
    L5:
        if (r0 > (this.length - this.readPosition)) goto L7;
        r3 = true;
        goto L8
    }

    private int findSampleAfter(int r6, int r7, long r8, boolean r10) {
        int r0 = r6;
        int r1 = 0;
    L3:
        if (r1 >= r7) goto L11;
        if (this.timesUs[r0] >= r8) goto L6;
        r0 = r0 + 1;
        if (r0 != this.capacity) goto L10;
        r0 = 0;
    L10:
        r1 = r1 + 1;
        goto L3
    L6:
        return r1;
    L11:
        if (r10 == true) goto L19;
        return -1;
    L19:
        return r7;
    }

    private int findSampleBefore(int r7, int r8, long r9, boolean r11) {
        int r0 = -1;
        int r1 = r7;
        int r2 = 0;
    L3:
        if (r2 >= r8) goto L16;
        if (this.timesUs[r1] > r9) goto L16;
        if (r11 == true) goto L8;
    L9:
        r0 = r2;
        if (this.timesUs[r1] == r9) goto L16;
    L12:
        r1 = r1 + 1;
        if (r1 != this.capacity) goto L15;
        r1 = 0;
    L15:
        r2 = r2 + 1;
        goto L3
    L8:
        if ((this.flags[r1] & 1) == 0) goto L12;
    L16:
        return r0;
    }

    private long getLargestTimestamp(int r8) {
        if (r8 != 0) goto L5;
        return Long.MIN_VALUE;
    L5:
        long r0 = Long.MIN_VALUE;
        int r2 = getRelativeIndex(r8 - 1);
        int r3 = 0;
    L6:
        if (r3 >= r8) goto L14;
        r0 = Math.max(r0, this.timesUs[r2]);
        if ((this.flags[r2] & 1) != 0) goto L14;
        r2 = r2 - 1;
        if (r2 != (-1)) goto L13;
        r2 = this.capacity - 1;
    L13:
        r3 = r3 + 1;
    L14:
        return r0;
    }

    private int getRelativeIndex(int r3) {
        int r0 = this.relativeFirstIndex + r3;
        if (r0 >= this.capacity) goto L6;
        return r0;
    L6:
        return r0 - this.capacity;
    }

    private boolean hasNextSample() {
        if (this.readPosition == this.length) goto L5;
        return true;
    L5:
        return false;
    }

    static /* synthetic */ void lambda$new$0(SharedSampleMetadata r1) {
        r1.drmSessionReference.release();
    }

    private boolean mayReadSample(int r3) {
        if (this.currentDrmSession != null) goto L5;
    L12:
        return true;
    L5:
        if (this.currentDrmSession.getState() == 4) goto L12;
        if ((this.flags[r3] & 1073741824) == 0) goto L9;
    L11:
        return false;
    L9:
        if (this.currentDrmSession.playClearSamplesWithoutKeys() == false) goto L11;
        goto L11
    }

    private void onFormatResult(Format r7, FormatHolder r8) {
        if (this.downstreamFormat != null) goto L5;
        boolean r0 = true;
    L7:
        if (this.downstreamFormat != null) goto L9;
        DrmInitData r1 = null;
    L10:
        this.downstreamFormat = r7;
        DrmInitData r2 = r7.drmInitData;
        if (this.drmSessionManager == null) goto L13;
        Format r3 = r7.copyWithCryptoType(this.drmSessionManager.getCryptoType(r7));
    L14:
        r8.format = r3;
        r8.drmSession = this.currentDrmSession;
        if (this.drmSessionManager != null) goto L17;
        return;
    L17:
        if (r0 == false) goto L19;
    L21:
        DrmSession r4 = this.currentDrmSession;
        this.currentDrmSession = this.drmSessionManager.acquireSession(this.drmEventDispatcher, r7);
        r8.drmSession = this.currentDrmSession;
        if (r4 == null) goto L25;
        r4.release(this.drmEventDispatcher);
        return;
    L25:
        return;
    L19:
        if (Util.areEqual(r1, r2) == false) goto L21;
        return;
    L13:
        r3 = r7;
        goto L14
    L9:
        r1 = this.downstreamFormat.drmInitData;
        goto L10
    L5:
        r0 = false;
        goto L7
    }

    private synchronized int peekSampleMetadata(FormatHolder r7, DecoderInputBuffer r8, boolean r9, boolean r10, SampleExtrasHolder r11) {
        monitor-enter(this);
        r8.waitingForKeys = false;     // Catch: Throwable -> L46
        if (hasNextSample() == true) goto L24;
        if (r10 == true) goto L21;
        if (this.isLastSampleQueued == true) goto L21;
        if (this.upstreamFormat == null) goto L18;
        if (r9 == false) goto L14;
    L15:
        onFormatResult((Format) Assertions.checkNotNull(this.upstreamFormat), r7);     // Catch: Throwable -> L46
        monitor-exit(this);
        return -5;
    L14:
        if (this.upstreamFormat != this.downstreamFormat) goto L15;
    L18:
        monitor-exit(this);
        return -3;
    L21:
        r8.setFlags(4);     // Catch: Throwable -> L46
        r8.timeUs = Long.MIN_VALUE;     // Catch: Throwable -> L46
        monitor-exit(this);
        return -4;
    L24:
        Format r0 = this.sharedSampleMetadata.get(getReadIndex()).format;     // Catch: Throwable -> L46
        if (r9 == false) goto L27;
    L43:
        onFormatResult(r0, r7);     // Catch: Throwable -> L46
        monitor-exit(this);
        return -5;
    L27:
        if (r0 != this.downstreamFormat) goto L43;
        int r1 = getRelativeIndex(this.readPosition);     // Catch: Throwable -> L46
        if (mayReadSample(r1) == true) goto L34;
        r8.waitingForKeys = true;     // Catch: Throwable -> L46
        monitor-exit(this);
        return -3;
    L34:
        r8.setFlags(this.flags[r1]);     // Catch: Throwable -> L46
        if (this.readPosition != (this.length - 1)) goto L40;
        if (r10 == false) goto L38;
    L39:
        r8.addFlag(C0180C.BUFFER_FLAG_LAST_SAMPLE);     // Catch: Throwable -> L46
        goto L40
    L38:
        if (this.isLastSampleQueued == true) goto L39;
    L40:
        r8.timeUs = this.timesUs[r1];     // Catch: Throwable -> L46
        r11.size = this.sizes[r1];     // Catch: Throwable -> L46
        r11.offset = this.offsets[r1];     // Catch: Throwable -> L46
        r11.cryptoData = this.cryptoDatas[r1];     // Catch: Throwable -> L46
        monitor-exit(this);
        return -4;
    L46:
        th = move-exception;
        throw th;
    }

    private void releaseDrmSessionReferences() {
        if (this.currentDrmSession == null) goto L6;
        this.currentDrmSession.release(this.drmEventDispatcher);
        this.currentDrmSession = null;
        this.downstreamFormat = null;
        return;
    }

    private synchronized void rewind() {
        monitor-enter(this);
        this.readPosition = 0;     // Catch: Throwable -> L7
        this.sampleDataQueue.rewind();     // Catch: Throwable -> L7
        monitor-exit(this);
        return;
    L7:
        th = move-exception;
        throw th;
    }

    private synchronized boolean setUpstreamFormat(Format r5) {
        monitor-enter(this);
        this.upstreamFormatRequired = false;     // Catch: Throwable -> L18
        if (Util.areEqual(r5, this.upstreamFormat) == false) goto L9;
        monitor-exit(this);
        return false;
    L9:
        if (this.sharedSampleMetadata.isEmpty() == false) goto L11;
    L13:
        this.upstreamFormat = r5;     // Catch: Throwable -> L18
    L14:
        this.allSamplesAreSyncSamples &= MimeTypes.allSamplesAreSyncSamples(this.upstreamFormat.sampleMimeType, this.upstreamFormat.codecs);     // Catch: Throwable -> L18
        this.loggedUnexpectedNonSyncSample = false;     // Catch: Throwable -> L18
        monitor-exit(this);
        return true;
    L11:
        if (this.sharedSampleMetadata.getEndValue().format.equals(r5) == false) goto L13;
        this.upstreamFormat = this.sharedSampleMetadata.getEndValue().format;     // Catch: Throwable -> L18
    L18:
        th = move-exception;
        throw th;
    }

    public synchronized long discardSampleMetadataToRead() {
        monitor-enter(this);
    L11:
        th = move-exception;
        throw th;
    L4:
        if (this.readPosition != 0) goto L8;
        monitor-exit(this);
        return -1;
    L8:
        long r0 = discardSamples(this.readPosition);     // Catch: Throwable -> L11
        monitor-exit(this);
        return r0;
    }

    public final void discardTo(long r4, boolean r6, boolean r7) {
        this.sampleDataQueue.discardDownstreamTo(discardSampleMetadataTo(r4, r6, r7));
    }

    public final void discardToEnd() {
        this.sampleDataQueue.discardDownstreamTo(discardSampleMetadataToEnd());
    }

    public final void discardToRead() {
        this.sampleDataQueue.discardDownstreamTo(discardSampleMetadataToRead());
    }

    public final void discardUpstreamFrom(long r3) {
        if (this.length != 0) goto L6;
        return;
    L6:
        if (r3 <= getLargestReadTimestampUs()) goto L8;
        boolean r0 = true;
    L9:
        Assertions.checkArgument(r0);
        int r1 = countUnreadSamplesBefore(r3);
        discardUpstreamSamples(this.absoluteFirstIndex + r1);
        return;
    L8:
        r0 = false;
        goto L9
    }

    public final void discardUpstreamSamples(int r4) {
        this.sampleDataQueue.discardUpstreamSampleBytes(discardUpstreamSampleMetadata(r4));
    }

    @Override // androidx.media3.extractor.TrackOutput
    public final void format(Format r4) {
        Format r0 = getAdjustedUpstreamFormat(r4);
        this.upstreamFormatAdjustmentRequired = false;
        this.unadjustedUpstreamFormat = r4;
        boolean r1 = setUpstreamFormat(r0);
        if (this.upstreamFormatChangeListener == null) goto L7;
        if (r1 == false) goto L8;
        this.upstreamFormatChangeListener.onUpstreamFormatChanged(r0);
        return;
    L8:
        return;
    }

    protected Format getAdjustedUpstreamFormat(Format r6) {
        if (this.sampleOffsetUs != 0) goto L5;
        return r6;
    L5:
        if (r6.subsampleOffsetUs != Long.MAX_VALUE) goto L7;
        return r6;
    L7:
        return r6.buildUpon().setSubsampleOffsetUs(r6.subsampleOffsetUs + this.sampleOffsetUs).build();
    }

    public final int getFirstIndex() {
        return this.absoluteFirstIndex;
    }

    public final synchronized long getFirstTimestampUs() {
        monitor-enter(this);
    L10:
        th = move-exception;
        throw th;
    L4:
        if (this.length != 0) goto L6;
        long r0 = Long.MIN_VALUE;
    L8:
        monitor-exit(this);
        return r0;
    L6:
        r0 = this.timesUs[this.relativeFirstIndex];     // Catch: Throwable -> L10
        goto L8
    }

    public final synchronized long getLargestQueuedTimestampUs() {
        monitor-enter(this);
        long r0 = this.largestQueuedTimestampUs;     // Catch: Throwable -> L6
        monitor-exit(this);
        return r0;
    L6:
        th = move-exception;
        throw th;
    }

    public final synchronized long getLargestReadTimestampUs() {
        monitor-enter(this);
        long r0 = Math.max(this.largestDiscardedTimestampUs, getLargestTimestamp(this.readPosition));     // Catch: Throwable -> L6
        monitor-exit(this);
        return r0;
    L6:
        th = move-exception;
        throw th;
    }

    public final int getReadIndex() {
        return this.absoluteFirstIndex + this.readPosition;
    }

    public final synchronized int getSkipCount(long r9, boolean r11) {
        monitor-enter(this);
        int r0 = getRelativeIndex(this.readPosition);     // Catch: Throwable -> L30
        if (hasNextSample() == false) goto L28;
        if (r9 < this.timesUs[r0]) goto L28;
        if (r9 <= this.largestQueuedTimestampUs) goto L18;
        if (r11 == false) goto L18;
        int r1 = this.length - this.readPosition;
        monitor-exit(this);
        return r1;
    L16:
        th = move-exception;
        Throwable r10 = th;
    L33:
        monitor-exit(this);     // Catch: Throwable -> L35
        throw r10;
    L18:
        int r12 = findSampleBefore(r0, this.length - this.readPosition, r9, true);     // Catch: Throwable -> L35
        if (r12 != (-1)) goto L25;
        monitor-exit(this);
        return 0;
    L25:
        monitor-exit(this);
        return r12;
    L28:
        monitor-exit(this);
        return 0;
    L30:
        th = th;
    L32:
        r10 = th;
    L35:
        th = th;
        goto L32
    }

    public final synchronized Format getUpstreamFormat() {
        monitor-enter(this);
    L9:
        th = move-exception;
        throw th;
    L4:
        if (this.upstreamFormatRequired == false) goto L6;
        Format r0 = null;
    L7:
        monitor-exit(this);
        return r0;
    L6:
        r0 = this.upstreamFormat;     // Catch: Throwable -> L9
        goto L7
    }

    public final int getWriteIndex() {
        return this.absoluteFirstIndex + this.length;
    }

    protected final void invalidateUpstreamFormatAdjustment() {
        this.upstreamFormatAdjustmentRequired = true;
    }

    public final synchronized boolean isLastSampleQueued() {
        monitor-enter(this);
        boolean r0 = this.isLastSampleQueued;     // Catch: Throwable -> L6
        monitor-exit(this);
        return r0;
    L6:
        th = move-exception;
        throw th;
    }

    public synchronized boolean isReady(boolean r4) {
        monitor-enter(this);
        boolean r1 = true;
        if (hasNextSample() == true) goto L17;
        if (r4 == false) goto L7;
    L14:
        monitor-exit(this);
        return r1;
    L7:
        if (this.isLastSampleQueued == true) goto L14;
        if (this.upstreamFormat != null) goto L11;
    L13:
        r1 = false;
        goto L14
    L11:
        if (this.upstreamFormat == this.downstreamFormat) goto L13;
    L17:
        if (this.sharedSampleMetadata.get(getReadIndex()).format == this.downstreamFormat) goto L20;
        monitor-exit(this);
        return true;
    L20:
        boolean r0 = mayReadSample(getRelativeIndex(this.readPosition));     // Catch: Throwable -> L23
        monitor-exit(this);
        return r0;
    L23:
        th = move-exception;
        throw th;
    }

    public void maybeThrowError() throws IOException {
        if (this.currentDrmSession != null) goto L5;
        return;
    L5:
        if (this.currentDrmSession.getState() == 1) goto L8;
        return;
    L8:
        throw ((DrmSession.DrmSessionException) Assertions.checkNotNull(this.currentDrmSession.getError()));
    }

    public final synchronized long peekSourceId() {
        monitor-enter(this);
        int r0 = getRelativeIndex(this.readPosition);     // Catch: Throwable -> L9
        if (hasNextSample() == false) goto L6;
        long r2 = this.sourceIds[r0];     // Catch: Throwable -> L9
    L7:
        monitor-exit(this);
        return r2;
    L6:
        r2 = this.upstreamSourceId;     // Catch: Throwable -> L9
    L9:
        th = move-exception;
        throw th;
    }

    public void preRelease() {
        discardToEnd();
        releaseDrmSessionReferences();
    }

    public int read(FormatHolder r10, DecoderInputBuffer r11, int r12, boolean r13) {
        boolean r1 = false;
        if ((r12 & 2) == 0) goto L5;
        boolean r6 = true;
    L6:
        int r14 = peekSampleMetadata(r10, r11, r6, r13, this.extrasHolder);
        if (r14 == (-4)) goto L9;
    L20:
        return r14;
    L9:
        if (r11.isEndOfStream() == true) goto L20;
        if ((r12 & 1) == 0) goto L14;
        r1 = true;
    L14:
        if ((r12 & 4) != 0) goto L18;
        if (r1 == false) goto L17;
        this.sampleDataQueue.peekToBuffer(r11, this.extrasHolder);
        goto L18
    L17:
        this.sampleDataQueue.readToBuffer(r11, this.extrasHolder);
    L18:
        if (r1 == true) goto L20;
        this.readPosition++;
        goto L20
    L5:
        r6 = false;
        goto L6
    }

    public void release() {
        reset(true);
        releaseDrmSessionReferences();
    }

    public final void reset() {
        reset(false);
    }

    public void reset(boolean r5) {
        this.sampleDataQueue.reset();
        this.length = 0;
        this.absoluteFirstIndex = 0;
        this.relativeFirstIndex = 0;
        this.readPosition = 0;
        this.upstreamKeyframeRequired = true;
        this.startTimeUs = Long.MIN_VALUE;
        this.largestDiscardedTimestampUs = Long.MIN_VALUE;
        this.largestQueuedTimestampUs = Long.MIN_VALUE;
        this.isLastSampleQueued = false;
        this.sharedSampleMetadata.clear();
        if (r5 == false) goto L6;
        this.unadjustedUpstreamFormat = null;
        this.upstreamFormat = null;
        this.upstreamFormatRequired = true;
        this.allSamplesAreSyncSamples = true;
        return;
    }

    @Override // androidx.media3.extractor.TrackOutput
    public /* synthetic */ int sampleData(DataReader r1, int r2, boolean r3) {
        return TrackOutput.CC.$default$sampleData(this, r1, r2, r3);
    }

    @Override // androidx.media3.extractor.TrackOutput
    public final int sampleData(DataReader r2, int r3, boolean r4, int r5) throws IOException {
        return this.sampleDataQueue.sampleData(r2, r3, r4);
    }

    @Override // androidx.media3.extractor.TrackOutput
    public /* synthetic */ void sampleData(ParsableByteArray r1, int r2) {
        TrackOutput.CC.$default$sampleData(this, r1, r2);
    }

    @Override // androidx.media3.extractor.TrackOutput
    public final void sampleData(ParsableByteArray r2, int r3, int r4) {
        this.sampleDataQueue.sampleData(r2, r3);
    }

    @Override // androidx.media3.extractor.TrackOutput
    public void sampleMetadata(long r14, int r16, int r17, int r18, TrackOutput.CryptoData r19) {
        if (this.upstreamFormatAdjustmentRequired == false) goto L6;
        format((Format) Assertions.checkStateNotNull(this.unadjustedUpstreamFormat));
    L6:
        if ((r16 & 1) == 0) goto L8;
        boolean r0 = true;
    L10:
        if (this.upstreamKeyframeRequired == false) goto L14;
        if (r0 == true) goto L13;
        return;
    L13:
        this.upstreamKeyframeRequired = false;
    L14:
        long r6 = r14 + this.sampleOffsetUs;
        if (this.allSamplesAreSyncSamples == true) goto L17;
    L25:
        int r8 = r16;
    L27:
        if (this.pendingSplice == false) goto L34;
        if (r0 == true) goto L30;
        return;
    L30:
        if (attemptSplice(r6) == false) goto L36;
        this.pendingSplice = false;
        goto L34
    L36:
        return;
    L34:
        commitSample(r6, r8, (this.sampleDataQueue.getTotalBytesWritten() - ((long) r17)) - ((long) r18), r17, r19);
        return;
    L17:
        if (r6 >= this.startTimeUs) goto L20;
        return;
    L20:
        if ((r16 & 1) != 0) goto L25;
        if (this.loggedUnexpectedNonSyncSample == true) goto L24;
        Log.m48w(TAG, "Overriding unexpected non-sync sample for format: " + this.upstreamFormat);
        this.loggedUnexpectedNonSyncSample = true;
    L24:
        r8 = r16 | 1;
        goto L27
    L8:
        r0 = false;
        goto L10
    }

    public final synchronized boolean seekTo(int r3) {
        monitor-enter(this);
        rewind();     // Catch: Throwable -> L15
        if (r3 >= this.absoluteFirstIndex) goto L6;
    L12:
        monitor-exit(this);
        return false;
    L6:
        if (r3 > (this.absoluteFirstIndex + this.length)) goto L12;
        this.startTimeUs = Long.MIN_VALUE;     // Catch: Throwable -> L15
        this.readPosition = r3 - this.absoluteFirstIndex;     // Catch: Throwable -> L15
        monitor-exit(this);
        return true;
    L15:
        th = move-exception;
        throw th;
    }

    public final synchronized boolean seekTo(long r9, boolean r11) {
        monitor-enter(this);
        rewind();     // Catch: Throwable -> L28
        int r0 = getRelativeIndex(this.readPosition);     // Catch: Throwable -> L28
        if (hasNextSample() == false) goto L26;
        if (r9 < this.timesUs[r0]) goto L26;
        if (r9 <= this.largestQueuedTimestampUs) goto L12;
        if (r11 == false) goto L26;
    L12:
        if (this.allSamplesAreSyncSamples == false) goto L16;
        SampleQueue r1 = this;
        long r4 = r9;
        int r10 = r1.findSampleAfter(r0, this.length - this.readPosition, r4, r11);     // Catch: Throwable -> L33
    L18:
        if (r10 != (-1)) goto L21;
        monitor-exit(this);
        return false;
    L21:
        r1.startTimeUs = r4;     // Catch: Throwable -> L33
        r1.readPosition += r10;     // Catch: Throwable -> L33
        monitor-exit(this);
        return true;
    L16:
        r1 = this;
        r4 = r9;
        r10 = r1.findSampleBefore(r0, r1.length - r1.readPosition, r4, true);     // Catch: Throwable -> L33
    L26:
        monitor-exit(this);
        return false;
    L33:
        th = th;
    L30:
        Throwable r12 = th;
        monitor-exit(this);     // Catch: Throwable -> L33
        throw r12;
    L28:
        th = th;
        goto L30
    }

    public final void setSampleOffsetUs(long r3) {
        if (this.sampleOffsetUs == r3) goto L6;
        this.sampleOffsetUs = r3;
        invalidateUpstreamFormatAdjustment();
        return;
    }

    public final void setStartTimeUs(long r1) {
        this.startTimeUs = r1;
    }

    public final void setUpstreamFormatChangeListener(UpstreamFormatChangedListener r1) {
        this.upstreamFormatChangeListener = r1;
    }

    public final synchronized void skip(int r3) {
        monitor-enter(this);
        if (r3 >= 0) goto L15;
    L9:
        boolean r0 = false;
    L10:
        Assertions.checkArgument(r0);     // Catch: Throwable -> L7
        this.readPosition += r3;     // Catch: Throwable -> L7
        monitor-exit(this);
        return;
    L7:
        th = move-exception;
        throw th;
    L15:
        if ((this.readPosition + r3) > this.length) goto L9;
        r0 = true;
        goto L10
    }

    public final void sourceId(long r1) {
        this.upstreamSourceId = r1;
    }

    public final void splice() {
        this.pendingSplice = true;
    }
}
