package androidx.media3.exoplayer.dash;

import android.os.Handler;
import android.os.Message;
import androidx.exifinterface.media.ExifInterface;
import androidx.media3.common.C0180C;
import androidx.media3.common.DataReader;
import androidx.media3.common.Format;
import androidx.media3.common.Metadata;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.FormatHolder;
import androidx.media3.exoplayer.dash.manifest.DashManifest;
import androidx.media3.exoplayer.source.SampleQueue;
import androidx.media3.exoplayer.source.chunk.Chunk;
import androidx.media3.exoplayer.upstream.Allocator;
import androidx.media3.extractor.TrackOutput;
import androidx.media3.extractor.metadata.MetadataInputBuffer;
import androidx.media3.extractor.metadata.emsg.EventMessage;
import androidx.media3.extractor.metadata.emsg.EventMessageDecoder;
import androidx.media3.extractor.metadata.icy.IcyHeaders;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/* JADX INFO: loaded from: classes.dex */
public final class PlayerEmsgHandler implements Handler.Callback {
    private static final int EMSG_MANIFEST_EXPIRED = 1;
    private final Allocator allocator;
    private boolean chunkLoadedCompletedSinceLastManifestRefreshRequest;
    private final EventMessageDecoder decoder;
    private long expiredManifestPublishTimeUs;
    private final Handler handler;
    private boolean isWaitingForManifestRefresh;
    private DashManifest manifest;
    private final TreeMap<Long, Long> manifestPublishTimeToExpiryTimeUs;
    private final PlayerEmsgCallback playerEmsgCallback;
    private boolean released;

    private static final class ManifestExpiryEventInfo {
        public final long eventTimeUs;
        public final long manifestPublishTimeMsInEmsg;

        public ManifestExpiryEventInfo(long r1, long r3) {
            this.eventTimeUs = r1;
            this.manifestPublishTimeMsInEmsg = r3;
        }
    }

    public interface PlayerEmsgCallback {
        void onDashManifestPublishTimeExpired(long r1);

        void onDashManifestRefreshRequested();
    }

    public final class PlayerTrackEmsgHandler implements TrackOutput {
        private final MetadataInputBuffer buffer;
        private final FormatHolder formatHolder;
        private long maxLoadedChunkEndTimeUs;
        private final SampleQueue sampleQueue;
        final /* synthetic */ PlayerEmsgHandler this$0;

        PlayerTrackEmsgHandler(PlayerEmsgHandler r3, Allocator r4) {
            this.this$0 = r3;
            this.sampleQueue = SampleQueue.createWithoutDrm(r4);
            this.formatHolder = new FormatHolder();
            this.buffer = new MetadataInputBuffer();
            this.maxLoadedChunkEndTimeUs = C0180C.TIME_UNSET;
        }

        private MetadataInputBuffer dequeueSample() {
            this.buffer.clear();
            if (this.sampleQueue.read(this.formatHolder, this.buffer, 0, false) != (-4)) goto L6;
            this.buffer.flip();
            return this.buffer;
        L6:
            return null;
        }

        private void onManifestExpiredMessageEncountered(long r5, long r7) {
            ManifestExpiryEventInfo r0 = new ManifestExpiryEventInfo(r5, r7);
            PlayerEmsgHandler.access$300(this.this$0).sendMessage(PlayerEmsgHandler.access$300(this.this$0).obtainMessage(1, r0));
        }

        private void parseAndDiscardSamples() {
        L3:
            if (this.sampleQueue.isReady(false) == false) goto L14;
            MetadataInputBuffer r0 = dequeueSample();
            if (r0 == null) goto L3;
            long r2 = r0.timeUs;
            Metadata r4 = PlayerEmsgHandler.access$000(this.this$0).decode(r0);
            if (r4 == null) goto L3;
            EventMessage r1 = (EventMessage) r4.get(0);
            if (PlayerEmsgHandler.access$100(r1.schemeIdUri, r1.value) == false) goto L3;
            parsePlayerEmsgEvent(r2, r1);
            goto L3
        L14:
            this.sampleQueue.discardToRead();
        }

        private void parsePlayerEmsgEvent(long r5, EventMessage r7) {
            long r0 = PlayerEmsgHandler.access$200(r7);
            if (r0 != C0180C.TIME_UNSET) goto L5;
            return;
        L5:
            onManifestExpiredMessageEncountered(r5, r0);
        }

        @Override // androidx.media3.extractor.TrackOutput
        public void format(Format r2) {
            this.sampleQueue.format(r2);
        }

        public boolean maybeRefreshManifestBeforeLoadingNextChunk(long r2) {
            return this.this$0.maybeRefreshManifestBeforeLoadingNextChunk(r2);
        }

        public void onChunkLoadCompleted(Chunk r5) {
            if (this.maxLoadedChunkEndTimeUs != C0180C.TIME_UNSET) goto L5;
        L6:
            this.maxLoadedChunkEndTimeUs = r5.endTimeUs;
        L7:
            this.this$0.onChunkLoadCompleted(r5);
            return;
        L5:
            if (r5.endTimeUs <= this.maxLoadedChunkEndTimeUs) goto L7;
            goto L6
        }

        public boolean onChunkLoadError(Chunk r5) {
            if (this.maxLoadedChunkEndTimeUs != C0180C.TIME_UNSET) goto L5;
        L7:
            boolean r0 = false;
        L9:
            return this.this$0.onChunkLoadError(r0);
        L5:
            if (this.maxLoadedChunkEndTimeUs >= r5.startTimeUs) goto L7;
            r0 = true;
            goto L9
        }

        public void release() {
            this.sampleQueue.release();
        }

        @Override // androidx.media3.extractor.TrackOutput
        public /* synthetic */ int sampleData(DataReader r1, int r2, boolean r3) {
            return TrackOutput.CC.$default$sampleData(this, r1, r2, r3);
        }

        @Override // androidx.media3.extractor.TrackOutput
        public int sampleData(DataReader r2, int r3, boolean r4, int r5) throws IOException {
            return this.sampleQueue.sampleData(r2, r3, r4);
        }

        @Override // androidx.media3.extractor.TrackOutput
        public /* synthetic */ void sampleData(ParsableByteArray r1, int r2) {
            TrackOutput.CC.$default$sampleData(this, r1, r2);
        }

        @Override // androidx.media3.extractor.TrackOutput
        public void sampleData(ParsableByteArray r2, int r3, int r4) {
            this.sampleQueue.sampleData(r2, r3);
        }

        @Override // androidx.media3.extractor.TrackOutput
        public void sampleMetadata(long r8, int r10, int r11, int r12, TrackOutput.CryptoData r13) {
            this.sampleQueue.sampleMetadata(r8, r10, r11, r12, r13);
            parseAndDiscardSamples();
        }
    }

    public PlayerEmsgHandler(DashManifest r2, PlayerEmsgCallback r3, Allocator r4) {
        this.manifest = r2;
        this.playerEmsgCallback = r3;
        this.allocator = r4;
        this.manifestPublishTimeToExpiryTimeUs = new TreeMap();
        this.handler = Util.createHandlerForCurrentLooper(this);
        this.decoder = new EventMessageDecoder();
    }

    static /* synthetic */ EventMessageDecoder access$000(PlayerEmsgHandler r1) {
        return r1.decoder;
    }

    static /* synthetic */ boolean access$100(String r1, String r2) {
        return isPlayerEmsgEvent(r1, r2);
    }

    static /* synthetic */ long access$200(EventMessage r2) {
        return getManifestPublishTimeMsInEmsg(r2);
    }

    static /* synthetic */ Handler access$300(PlayerEmsgHandler r1) {
        return r1.handler;
    }

    private Map.Entry<Long, Long> ceilingExpiryEntryForPublishTime(long r3) {
        return this.manifestPublishTimeToExpiryTimeUs.ceilingEntry(Long.valueOf(r3));
    }

    private static long getManifestPublishTimeMsInEmsg(EventMessage r3) {
        return Util.parseXsDateTime(Util.fromUtf8Bytes(r3.messageData));
    L5:
        return C0180C.TIME_UNSET;
    }

    private void handleManifestExpiredMessage(long r5, long r7) {
        Long r0 = this.manifestPublishTimeToExpiryTimeUs.get(Long.valueOf(r7));
        if (r0 != null) goto L6;
        this.manifestPublishTimeToExpiryTimeUs.put(Long.valueOf(r7), Long.valueOf(r5));
        return;
    L6:
        if (r0.longValue() <= r5) goto L10;
        this.manifestPublishTimeToExpiryTimeUs.put(Long.valueOf(r7), Long.valueOf(r5));
        return;
    }

    private static boolean isPlayerEmsgEvent(String r1, String r2) {
        if ("urn:mpeg:dash:event:2012".equals(r1) == true) goto L5;
    L11:
        return false;
    L5:
        if (IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE.equals(r2) == false) goto L7;
    L10:
        return true;
    L7:
        if (ExifInterface.GPS_MEASUREMENT_2D.equals(r2) == true) goto L10;
        if (ExifInterface.GPS_MEASUREMENT_3D.equals(r2) == false) goto L11;
        goto L10
    }

    private void maybeNotifyDashManifestRefreshNeeded() {
        if (this.chunkLoadedCompletedSinceLastManifestRefreshRequest == true) goto L5;
        return;
    L5:
        this.isWaitingForManifestRefresh = true;
        this.chunkLoadedCompletedSinceLastManifestRefreshRequest = false;
        this.playerEmsgCallback.onDashManifestRefreshRequested();
    }

    private void notifyManifestPublishTimeExpired() {
        this.playerEmsgCallback.onDashManifestPublishTimeExpired(this.expiredManifestPublishTimeUs);
    }

    private void removePreviouslyExpiredManifestPublishTimeValues() {
        Iterator<Map.Entry<Long, Long>> r0 = this.manifestPublishTimeToExpiryTimeUs.entrySet().iterator();
    L4:
        if (r0.hasNext() == false) goto L9;
        if (r0.next().getKey().longValue() >= this.manifest.publishTimeMs) goto L4;
        r0.remove();
        goto L4
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message r7) {
        if (this.released == false) goto L6;
        return true;
    L6:
        switch(r7.what) {
            case 1: goto L9;
            default: goto L7;
        };
    L7:
        return false;
    L9:
        ManifestExpiryEventInfo r0 = (ManifestExpiryEventInfo) r7.obj;
        handleManifestExpiredMessage(r0.eventTimeUs, r0.manifestPublishTimeMsInEmsg);
        return true;
    }

    boolean maybeRefreshManifestBeforeLoadingNextChunk(long r7) {
        if (this.manifest.dynamic == true) goto L7;
        return false;
    L7:
        if (this.isWaitingForManifestRefresh == false) goto L10;
        return true;
    L10:
        boolean r0 = false;
        Map.Entry<Long, Long> r1 = ceilingExpiryEntryForPublishTime(this.manifest.publishTimeMs);
        if (r1 != null) goto L13;
    L15:
        if (r0 == false) goto L17;
        maybeNotifyDashManifestRefreshNeeded();
    L17:
        return r0;
    L13:
        if (r1.getValue().longValue() >= r7) goto L15;
        this.expiredManifestPublishTimeUs = r1.getKey().longValue();
        notifyManifestPublishTimeExpired();
        r0 = true;
        goto L15
    }

    public PlayerTrackEmsgHandler newPlayerTrackEmsgHandler() {
        return new PlayerTrackEmsgHandler(this, this.allocator);
    }

    void onChunkLoadCompleted(Chunk r2) {
        this.chunkLoadedCompletedSinceLastManifestRefreshRequest = true;
    }

    boolean onChunkLoadError(boolean r4) {
        if (this.manifest.dynamic == true) goto L6;
        return false;
    L6:
        if (this.isWaitingForManifestRefresh == false) goto L8;
        return true;
    L8:
        if (r4 == false) goto L11;
        maybeNotifyDashManifestRefreshNeeded();
        return true;
    L11:
        return false;
    }

    public void release() {
        this.released = true;
        this.handler.removeCallbacksAndMessages(null);
    }

    public void updateManifest(DashManifest r3) {
        this.isWaitingForManifestRefresh = false;
        this.expiredManifestPublishTimeUs = C0180C.TIME_UNSET;
        this.manifest = r3;
        removePreviouslyExpiredManifestPublishTimeValues();
    }
}
