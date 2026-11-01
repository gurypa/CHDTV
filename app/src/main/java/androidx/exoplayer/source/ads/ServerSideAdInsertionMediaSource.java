package androidx.media3.exoplayer.source.ads;

import android.os.Handler;
import android.util.Pair;
import androidx.media3.common.AdPlaybackState;
import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.MediaItem;
import androidx.media3.common.StreamKey;
import androidx.media3.common.Timeline;
import androidx.media3.common.TrackGroup;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.TransferListener;
import androidx.media3.decoder.DecoderInputBuffer;
import androidx.media3.exoplayer.FormatHolder;
import androidx.media3.exoplayer.LoadingInfo;
import androidx.media3.exoplayer.SeekParameters;
import androidx.media3.exoplayer.drm.DrmSessionEventListener;
import androidx.media3.exoplayer.source.BaseMediaSource;
import androidx.media3.exoplayer.source.EmptySampleStream;
import androidx.media3.exoplayer.source.ForwardingTimeline;
import androidx.media3.exoplayer.source.LoadEventInfo;
import androidx.media3.exoplayer.source.MediaLoadData;
import androidx.media3.exoplayer.source.MediaPeriod;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.exoplayer.source.MediaSourceEventListener;
import androidx.media3.exoplayer.source.SampleStream;
import androidx.media3.exoplayer.source.SequenceableLoader;
import androidx.media3.exoplayer.source.TrackGroupArray;
import androidx.media3.exoplayer.trackselection.ExoTrackSelection;
import androidx.media3.exoplayer.upstream.Allocator;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.UnmodifiableIterator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class ServerSideAdInsertionMediaSource extends BaseMediaSource implements MediaSource.MediaSourceCaller, MediaSourceEventListener, DrmSessionEventListener {
    private final AdPlaybackStateUpdater adPlaybackStateUpdater;
    private ImmutableMap<Object, AdPlaybackState> adPlaybackStates;
    private final DrmSessionEventListener.EventDispatcher drmEventDispatcherWithoutId;
    private SharedMediaPeriod lastUsedMediaPeriod;
    private final ListMultimap<Pair<Long, Object>, SharedMediaPeriod> mediaPeriods;
    private final MediaSource mediaSource;
    private final MediaSourceEventListener.EventDispatcher mediaSourceEventDispatcherWithoutId;
    private Handler playbackHandler;

    public interface AdPlaybackStateUpdater {
        boolean onAdPlaybackStateUpdateRequested(Timeline r1);
    }

    private static final class MediaPeriodImpl implements MediaPeriod {
        public MediaPeriod.Callback callback;
        public final DrmSessionEventListener.EventDispatcher drmEventDispatcher;
        public boolean[] hasNotifiedDownstreamFormatChange;
        public boolean isPrepared;
        public long lastStartPositionUs;
        public final MediaSource.MediaPeriodId mediaPeriodId;
        public final MediaSourceEventListener.EventDispatcher mediaSourceEventDispatcher;
        public final SharedMediaPeriod sharedPeriod;

        public MediaPeriodImpl(SharedMediaPeriod r2, MediaSource.MediaPeriodId r3, MediaSourceEventListener.EventDispatcher r4, DrmSessionEventListener.EventDispatcher r5) {
            this.sharedPeriod = r2;
            this.mediaPeriodId = r3;
            this.mediaSourceEventDispatcher = r4;
            this.drmEventDispatcher = r5;
            this.hasNotifiedDownstreamFormatChange = new boolean[0];
        }

        @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
        public boolean continueLoading(LoadingInfo r2) {
            return this.sharedPeriod.continueLoading(this, r2);
        }

        @Override // androidx.media3.exoplayer.source.MediaPeriod
        public void discardBuffer(long r2, boolean r4) {
            this.sharedPeriod.discardBuffer(this, r2, r4);
        }

        @Override // androidx.media3.exoplayer.source.MediaPeriod
        public long getAdjustedSeekPositionUs(long r3, SeekParameters r5) {
            return this.sharedPeriod.getAdjustedSeekPositionUs(this, r3, r5);
        }

        @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
        public long getBufferedPositionUs() {
            return this.sharedPeriod.getBufferedPositionUs(this);
        }

        @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
        public long getNextLoadPositionUs() {
            return this.sharedPeriod.getNextLoadPositionUs(this);
        }

        @Override // androidx.media3.exoplayer.source.MediaPeriod
        public List<StreamKey> getStreamKeys(List<ExoTrackSelection> r2) {
            return this.sharedPeriod.getStreamKeys(r2);
        }

        @Override // androidx.media3.exoplayer.source.MediaPeriod
        public TrackGroupArray getTrackGroups() {
            return this.sharedPeriod.getTrackGroups();
        }

        @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
        public boolean isLoading() {
            return this.sharedPeriod.isLoading(this);
        }

        @Override // androidx.media3.exoplayer.source.MediaPeriod
        public void maybeThrowPrepareError() throws IOException {
            this.sharedPeriod.maybeThrowPrepareError();
        }

        public void onPrepared() {
            if (this.callback == null) goto L5;
            this.callback.onPrepared(this);
        L5:
            this.isPrepared = true;
        }

        @Override // androidx.media3.exoplayer.source.MediaPeriod
        public void prepare(MediaPeriod.Callback r2, long r3) {
            this.callback = r2;
            this.sharedPeriod.prepare(this, r3);
        }

        @Override // androidx.media3.exoplayer.source.MediaPeriod
        public long readDiscontinuity() {
            return this.sharedPeriod.readDiscontinuity(this);
        }

        @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
        public void reevaluateBuffer(long r2) {
            this.sharedPeriod.reevaluateBuffer(this, r2);
        }

        @Override // androidx.media3.exoplayer.source.MediaPeriod
        public long seekToUs(long r3) {
            return this.sharedPeriod.seekToUs(this, r3);
        }

        @Override // androidx.media3.exoplayer.source.MediaPeriod
        public long selectTracks(ExoTrackSelection[] r10, boolean[] r11, SampleStream[] r12, boolean[] r13, long r14) {
            if (this.hasNotifiedDownstreamFormatChange.length != 0) goto L6;
            this.hasNotifiedDownstreamFormatChange = new boolean[r12.length];
        L6:
            return this.sharedPeriod.selectTracks(this, r10, r11, r12, r13, r14);
        }
    }

    private static final class SampleStreamImpl implements SampleStream {
        private final MediaPeriodImpl mediaPeriod;
        private final int streamIndex;

        public SampleStreamImpl(MediaPeriodImpl r1, int r2) {
            this.mediaPeriod = r1;
            this.streamIndex = r2;
        }

        @Override // androidx.media3.exoplayer.source.SampleStream
        public boolean isReady() {
            return this.mediaPeriod.sharedPeriod.isReady(this.streamIndex);
        }

        @Override // androidx.media3.exoplayer.source.SampleStream
        public void maybeThrowError() throws IOException {
            this.mediaPeriod.sharedPeriod.maybeThrowError(this.streamIndex);
        }

        @Override // androidx.media3.exoplayer.source.SampleStream
        public int readData(FormatHolder r8, DecoderInputBuffer r9, int r10) {
            return this.mediaPeriod.sharedPeriod.readData(this.mediaPeriod, this.streamIndex, r8, r9, r10);
        }

        @Override // androidx.media3.exoplayer.source.SampleStream
        public int skipData(long r4) {
            return this.mediaPeriod.sharedPeriod.skipData(this.mediaPeriod, this.streamIndex, r4);
        }
    }

    private static final class ServerSideAdInsertionTimeline extends ForwardingTimeline {
        private final ImmutableMap<Object, AdPlaybackState> adPlaybackStates;

        public ServerSideAdInsertionTimeline(Timeline r5, ImmutableMap<Object, AdPlaybackState> r6) {
            super(r5);
            if (r5.getWindowCount() != 1) goto L5;
            boolean r0 = true;
        L6:
            Assertions.checkState(r0);
            Timeline.Period r1 = new Timeline.Period();
            int r2 = 0;
        L8:
            if (r2 >= r5.getPeriodCount()) goto L10;
            r5.getPeriod(r2, r1, true);
            Assertions.checkState(r6.containsKey(Assertions.checkNotNull(r1.uid)));
            r2 = r2 + 1;
            goto L8
        L10:
            this.adPlaybackStates = r6;
            return;
        L5:
            r0 = false;
            goto L6
        }

        @Override // androidx.media3.exoplayer.source.ForwardingTimeline, androidx.media3.common.Timeline
        public Timeline.Period getPeriod(int r14, Timeline.Period r15, boolean r16) {
            super.getPeriod(r14, r15, true);
            AdPlaybackState r8 = (AdPlaybackState) Assertions.checkNotNull(this.adPlaybackStates.get(r15.uid));
            long r2 = r15.durationUs;
            if (r2 != C0180C.TIME_UNSET) goto L5;
            long r3 = r8.contentDurationUs;
        L6:
            long r6 = 0;
            Timeline.Period r4 = new Timeline.Period();
            int r5 = 0;
        L8:
            if (r5 >= (r14 + 1)) goto L15;
            this.timeline.getPeriod(r5, r4, true);
            AdPlaybackState r9 = (AdPlaybackState) Assertions.checkNotNull(this.adPlaybackStates.get(r4.uid));
            if (r5 != 0) goto L12;
            r6 = -ServerSideAdInsertionUtil.getMediaPeriodPositionUsForContent(-r4.getPositionInWindowUs(), -1, r9);
        L12:
            if (r5 == r14) goto L14;
            r6 = r6 + ServerSideAdInsertionUtil.getMediaPeriodPositionUsForContent(r4.durationUs, -1, r9);
        L14:
            r5 = r5 + 1;
            goto L8
        L15:
            long r7 = r3;
            r15.set(r15.f30id, r15.uid, r15.windowIndex, r7, r6, r8, r15.isPlaceholder);
            return r15;
        L5:
            r3 = ServerSideAdInsertionUtil.getMediaPeriodPositionUsForContent(r2, -1, r8);
            goto L6
        }

        @Override // androidx.media3.exoplayer.source.ForwardingTimeline, androidx.media3.common.Timeline
        public Timeline.Window getWindow(int r19, Timeline.Window r20, long r21) {
            super.getWindow(r19, r20, r21);
            Timeline.Period r2 = new Timeline.Period();
            Object r3 = Assertions.checkNotNull(getPeriod(r20.firstPeriodIndex, r2, true).uid);
            AdPlaybackState r5 = (AdPlaybackState) Assertions.checkNotNull(this.adPlaybackStates.get(r3));
            long r6 = ServerSideAdInsertionUtil.getMediaPeriodPositionUsForContent(r20.positionInFirstPeriodUs, -1, r5);
            if (r20.durationUs == C0180C.TIME_UNSET) goto L5;
            Timeline.Period r4 = super.getPeriod(r20.lastPeriodIndex, r2, true);
            long r9 = r4.positionInWindowUs;
            AdPlaybackState r11 = (AdPlaybackState) Assertions.checkNotNull(this.adPlaybackStates.get(r4.uid));
            Timeline.Period r12 = getPeriod(r20.lastPeriodIndex, r2);
            long r15 = ServerSideAdInsertionUtil.getMediaPeriodPositionUsForContent(r20.durationUs - r9, -1, r11);
            r20.durationUs = r12.positionInWindowUs + r15;
        L9:
            r20.positionInFirstPeriodUs = r6;
            return r20;
        L5:
            if (r5.contentDurationUs == C0180C.TIME_UNSET) goto L9;
            r20.durationUs = r5.contentDurationUs - r6;
            goto L9
        }
    }

    private static final class SharedMediaPeriod implements MediaPeriod.Callback {
        private final Map<Long, Pair<LoadEventInfo, MediaLoadData>> activeLoads;
        private final MediaPeriod actualMediaPeriod;
        private AdPlaybackState adPlaybackState;
        private boolean hasStartedPreparing;
        private boolean isPrepared;
        public MediaLoadData[] lastDownstreamFormatChangeData;
        private MediaPeriodImpl loadingPeriod;
        private final List<MediaPeriodImpl> mediaPeriods;
        private final Object periodUid;
        public SampleStream[] sampleStreams;
        public ExoTrackSelection[] trackSelections;

        public SharedMediaPeriod(MediaPeriod r3, Object r4, AdPlaybackState r5) {
            this.actualMediaPeriod = r3;
            this.periodUid = r4;
            this.adPlaybackState = r5;
            this.mediaPeriods = new ArrayList();
            this.activeLoads = new HashMap();
            this.trackSelections = new ExoTrackSelection[0];
            this.sampleStreams = new SampleStream[0];
            this.lastDownstreamFormatChangeData = new MediaLoadData[0];
        }

        static /* synthetic */ Object access$000(SharedMediaPeriod r1) {
            return r1.periodUid;
        }

        static /* synthetic */ MediaPeriodImpl access$100(SharedMediaPeriod r1) {
            return r1.loadingPeriod;
        }

        static /* synthetic */ List access$200(SharedMediaPeriod r1) {
            return r1.mediaPeriods;
        }

        private int findMatchingStreamIndex(MediaLoadData r9) {
            if (r9.trackFormat != null) goto L5;
            return -1;
        L5:
            int r0 = 0;
        L7:
            if (r0 >= this.trackSelections.length) goto L30;
            if (this.trackSelections[r0] == null) goto L29;
            TrackGroup r2 = this.trackSelections[r0].getTrackGroup();
            boolean r4 = false;
            if (r9.trackType == 0) goto L13;
        L16:
            int r3 = 0;
        L18:
            if (r3 >= r2.length) goto L29;
            Format r5 = r2.getFormat(r3);
            if (r5.equals(r9.trackFormat) == true) goto L28;
            if (r4 == false) goto L27;
            if (r5.f26id == null) goto L27;
            if (r5.f26id.equals(r9.trackFormat.f26id) == true) goto L28;
        L27:
            r3 = r3 + 1;
        L28:
            return r0;
        L13:
            if (r2.equals(getTrackGroups().get(0)) == false) goto L16;
            r4 = true;
        L29:
            r0 = r0 + 1;
            goto L7
        L30:
            return -1;
        }

        private long getMediaPeriodPositionUsWithEndOfSourceHandling(MediaPeriodImpl r8, long r9) {
            if (r9 != Long.MIN_VALUE) goto L5;
            return Long.MIN_VALUE;
        L5:
            long r2 = ServerSideAdInsertionUtil.getMediaPeriodPositionUs(r9, r8.mediaPeriodId, this.adPlaybackState);
            if (r2 < ServerSideAdInsertionMediaSource.access$300(r8, this.adPlaybackState)) goto L9;
            return Long.MIN_VALUE;
        L9:
            return r2;
        }

        private long getStreamPositionUsWithNotYetStartedHandling(MediaPeriodImpl r5, long r6) {
            if (r6 >= r5.lastStartPositionUs) goto L7;
            return ServerSideAdInsertionUtil.getStreamPositionUs(r5.lastStartPositionUs, r5.mediaPeriodId, this.adPlaybackState) - (r5.lastStartPositionUs - r6);
        L7:
            return ServerSideAdInsertionUtil.getStreamPositionUs(r6, r5.mediaPeriodId, this.adPlaybackState);
        }

        private void maybeNotifyDownstreamFormatChanged(MediaPeriodImpl r4, int r5) {
            if (r4.hasNotifiedDownstreamFormatChange[r5] == false) goto L5;
            return;
        L5:
            if (this.lastDownstreamFormatChangeData[r5] == null) goto L9;
            r4.hasNotifiedDownstreamFormatChange[r5] = true;
            r4.mediaSourceEventDispatcher.downstreamFormatChanged(ServerSideAdInsertionMediaSource.access$400(r4, this.lastDownstreamFormatChangeData[r5], this.adPlaybackState));
            return;
        }

        public void add(MediaPeriodImpl r2) {
            this.mediaPeriods.add(r2);
        }

        public boolean canReuseMediaPeriod(MediaSource.MediaPeriodId r7, long r8) {
            MediaPeriodImpl r0 = (MediaPeriodImpl) Iterables.getLast(this.mediaPeriods);
            long r1 = ServerSideAdInsertionUtil.getStreamPositionUs(ServerSideAdInsertionMediaSource.access$300(r0, this.adPlaybackState), r0.mediaPeriodId, this.adPlaybackState);
            if (ServerSideAdInsertionUtil.getStreamPositionUs(r8, r7, this.adPlaybackState) != r1) goto L5;
            return true;
        L5:
            return false;
        }

        public boolean continueLoading(MediaPeriodImpl r8, LoadingInfo r9) {
            MediaPeriodImpl r0 = this.loadingPeriod;
            if (r0 != null) goto L5;
        L10:
            this.loadingPeriod = r8;
            long r1 = getStreamPositionUsWithNotYetStartedHandling(r8, r9.playbackPositionUs);
            return this.actualMediaPeriod.continueLoading(r9.buildUpon().setPlaybackPositionUs(r1).build());
        L5:
            if (r8.equals(r0) == true) goto L10;
            Iterator<Pair<LoadEventInfo, MediaLoadData>> r2 = this.activeLoads.values().iterator();
        L8:
            if (r2.hasNext() == false) goto L10;
            Pair<LoadEventInfo, MediaLoadData> r3 = r2.next();
            r0.mediaSourceEventDispatcher.loadCompleted((LoadEventInfo) r3.first, ServerSideAdInsertionMediaSource.access$400(r0, (MediaLoadData) r3.second, this.adPlaybackState));
            r8.mediaSourceEventDispatcher.loadStarted((LoadEventInfo) r3.first, ServerSideAdInsertionMediaSource.access$400(r8, (MediaLoadData) r3.second, this.adPlaybackState));
            goto L8
        }

        public void discardBuffer(MediaPeriodImpl r4, long r5, boolean r7) {
            long r0 = ServerSideAdInsertionUtil.getStreamPositionUs(r5, r4.mediaPeriodId, this.adPlaybackState);
            this.actualMediaPeriod.discardBuffer(r0, r7);
        }

        public long getAdjustedSeekPositionUs(MediaPeriodImpl r7, long r8, SeekParameters r10) {
            long r0 = ServerSideAdInsertionUtil.getStreamPositionUs(r8, r7.mediaPeriodId, this.adPlaybackState);
            return ServerSideAdInsertionUtil.getMediaPeriodPositionUs(this.actualMediaPeriod.getAdjustedSeekPositionUs(r0, r10), r7.mediaPeriodId, this.adPlaybackState);
        }

        public long getBufferedPositionUs(MediaPeriodImpl r3) {
            return getMediaPeriodPositionUsWithEndOfSourceHandling(r3, this.actualMediaPeriod.getBufferedPositionUs());
        }

        public MediaPeriodImpl getMediaPeriodForEvent(MediaLoadData r9) {
            if (r9 != null) goto L4;
            return null;
        L4:
            if (r9.mediaStartTimeMs == C0180C.TIME_UNSET) goto L24;
            int r0 = 0;
        L7:
            if (r0 >= this.mediaPeriods.size()) goto L25;
            MediaPeriodImpl r1 = this.mediaPeriods.get(r0);
            if (r1.isPrepared == false) goto L16;
            long r2 = ServerSideAdInsertionUtil.getMediaPeriodPositionUs(Util.msToUs(r9.mediaStartTimeMs), r1.mediaPeriodId, this.adPlaybackState);
            long r4 = ServerSideAdInsertionMediaSource.access$300(r1, this.adPlaybackState);
            if (r2 < 0) goto L16;
            if (r2 >= r4) goto L16;
            return r1;
        L16:
            r0 = r0 + 1;
            goto L7
        L25:
            return null;
        L24:
            return null;
        }

        public long getNextLoadPositionUs(MediaPeriodImpl r3) {
            return getMediaPeriodPositionUsWithEndOfSourceHandling(r3, this.actualMediaPeriod.getNextLoadPositionUs());
        }

        public List<StreamKey> getStreamKeys(List<ExoTrackSelection> r2) {
            return this.actualMediaPeriod.getStreamKeys(r2);
        }

        public TrackGroupArray getTrackGroups() {
            return this.actualMediaPeriod.getTrackGroups();
        }

        public boolean isLoading(MediaPeriodImpl r2) {
            if (r2.equals(this.loadingPeriod) == true) goto L5;
        L7:
            return false;
        L5:
            if (this.actualMediaPeriod.isLoading() == false) goto L7;
            return true;
        }

        public boolean isReady(int r2) {
            return ((SampleStream) Util.castNonNull(this.sampleStreams[r2])).isReady();
        }

        public boolean isUnused() {
            return this.mediaPeriods.isEmpty();
        }

        public void maybeThrowError(int r2) throws IOException {
            ((SampleStream) Util.castNonNull(this.sampleStreams[r2])).maybeThrowError();
        }

        public void maybeThrowPrepareError() throws IOException {
            this.actualMediaPeriod.maybeThrowPrepareError();
        }

        public void onContinueLoadingRequested(MediaPeriod r3) {
            if (this.loadingPeriod != null) goto L5;
            return;
        L5:
            ((MediaPeriod.Callback) Assertions.checkNotNull(this.loadingPeriod.callback)).onContinueLoadingRequested(this.loadingPeriod);
        }

        @Override // androidx.media3.exoplayer.source.SequenceableLoader.Callback
        public /* bridge */ /* synthetic */ void onContinueLoadingRequested(SequenceableLoader r1) {
            onContinueLoadingRequested((MediaPeriod) r1);
        }

        public void onDownstreamFormatChanged(MediaPeriodImpl r4, MediaLoadData r5) {
            int r0 = findMatchingStreamIndex(r5);
            if (r0 == (-1)) goto L6;
            this.lastDownstreamFormatChangeData[r0] = r5;
            r4.hasNotifiedDownstreamFormatChange[r0] = true;
            return;
        }

        public void onLoadFinished(LoadEventInfo r4) {
            this.activeLoads.remove(Long.valueOf(r4.loadTaskId));
        }

        public void onLoadStarted(LoadEventInfo r4, MediaLoadData r5) {
            this.activeLoads.put(Long.valueOf(r4.loadTaskId), Pair.create(r4, r5));
        }

        @Override // androidx.media3.exoplayer.source.MediaPeriod.Callback
        public void onPrepared(MediaPeriod r3) {
            this.isPrepared = true;
            int r0 = 0;
        L4:
            if (r0 >= this.mediaPeriods.size()) goto L6;
            this.mediaPeriods.get(r0).onPrepared();
            r0 = r0 + 1;
            goto L4
        }

        public void prepare(MediaPeriodImpl r4, long r5) {
            r4.lastStartPositionUs = r5;
            if (this.hasStartedPreparing == true) goto L5;
            this.hasStartedPreparing = true;
            long r0 = ServerSideAdInsertionUtil.getStreamPositionUs(r5, r4.mediaPeriodId, this.adPlaybackState);
            this.actualMediaPeriod.prepare(this, r0);
            return;
        L5:
            if (this.isPrepared == false) goto L10;
            r4.onPrepared();
            return;
        }

        public int readData(MediaPeriodImpl r17, int r18, FormatHolder r19, DecoderInputBuffer r20, int r21) {
            long r6 = getBufferedPositionUs(r17);
            SampleStream r8 = (SampleStream) Util.castNonNull(this.sampleStreams[r18]);
            int r9 = r8.readData(r19, r20, (r21 | 1) | 4);
            long r10 = getMediaPeriodPositionUsWithEndOfSourceHandling(r17, r20.timeUs);
            if (r9 != (-4)) goto L7;
            if (r10 != Long.MIN_VALUE) goto L7;
        L12:
            maybeNotifyDownstreamFormatChanged(r17, r18);
            r20.clear();
            r20.addFlag(4);
            return -4;
        L7:
            if (r9 == (-3)) goto L9;
        L14:
            if (r9 != (-4)) goto L16;
            maybeNotifyDownstreamFormatChanged(r17, r18);
            ((SampleStream) Util.castNonNull(this.sampleStreams[r18])).readData(r19, r20, r21);
            r20.timeUs = r10;
        L16:
            return r9;
        L9:
            if (r6 != Long.MIN_VALUE) goto L14;
            if (r20.waitingForKeys == true) goto L14;
            goto L12
        }

        public long readDiscontinuity(MediaPeriodImpl r6) {
            if (r6.equals(this.mediaPeriods.get(0)) == true) goto L5;
            return C0180C.TIME_UNSET;
        L5:
            long r3 = this.actualMediaPeriod.readDiscontinuity();
            if (r3 != C0180C.TIME_UNSET) goto L9;
            return C0180C.TIME_UNSET;
        L9:
            return ServerSideAdInsertionUtil.getMediaPeriodPositionUs(r3, r6.mediaPeriodId, this.adPlaybackState);
        }

        public void reevaluateBuffer(MediaPeriodImpl r4, long r5) {
            this.actualMediaPeriod.reevaluateBuffer(getStreamPositionUsWithNotYetStartedHandling(r4, r5));
        }

        public void release(MediaSource r2) {
            r2.releasePeriod(this.actualMediaPeriod);
        }

        public void remove(MediaPeriodImpl r2) {
            if (r2.equals(this.loadingPeriod) == false) goto L5;
            this.loadingPeriod = null;
            this.activeLoads.clear();
        L5:
            this.mediaPeriods.remove(r2);
        }

        public long seekToUs(MediaPeriodImpl r7, long r8) {
            long r0 = ServerSideAdInsertionUtil.getStreamPositionUs(r8, r7.mediaPeriodId, this.adPlaybackState);
            return ServerSideAdInsertionUtil.getMediaPeriodPositionUs(this.actualMediaPeriod.seekToUs(r0), r7.mediaPeriodId, this.adPlaybackState);
        }

        public long selectTracks(MediaPeriodImpl r17, ExoTrackSelection[] r18, boolean[] r19, SampleStream[] r20, boolean[] r21, long r22) {
            r17.lastStartPositionUs = r22;
            if (r17.equals(this.mediaPeriods.get(0)) == false) goto L22;
            this.trackSelections = (ExoTrackSelection[]) Arrays.copyOf(r18, r18.length);
            long r7 = ServerSideAdInsertionUtil.getStreamPositionUs(r22, r17.mediaPeriodId, this.adPlaybackState);
            if (this.sampleStreams.length != 0) goto L7;
            SampleStream[] r5 = new SampleStream[r18.length];
        L8:
            long r12 = this.actualMediaPeriod.selectTracks(r18, r19, r5, r21, r7);
            this.sampleStreams = (SampleStream[]) Arrays.copyOf(r5, r5.length);
            this.lastDownstreamFormatChangeData = (MediaLoadData[]) Arrays.copyOf(this.lastDownstreamFormatChangeData, r5.length);
            int r2 = 0;
        L10:
            if (r2 >= r5.length) goto L21;
            if (r5[r2] != null) goto L15;
            r20[r2] = null;
            this.lastDownstreamFormatChangeData[r2] = null;
        L19:
            r2 = r2 + 1;
            goto L10
        L15:
            if (r20[r2] != null) goto L17;
        L18:
            r20[r2] = new SampleStreamImpl(r17, r2);
            this.lastDownstreamFormatChangeData[r2] = null;
            goto L19
        L17:
            if (r21[r2] == false) goto L19;
        L21:
            return ServerSideAdInsertionUtil.getMediaPeriodPositionUs(r12, r17.mediaPeriodId, this.adPlaybackState);
        L7:
            r5 = (SampleStream[]) Arrays.copyOf(this.sampleStreams, this.sampleStreams.length);
            goto L8
        L22:
            int r3 = 0;
        L24:
            if (r3 >= r18.length) goto L42;
            boolean r6 = true;
            if (r18[r3] != null) goto L28;
            r20[r3] = null;
            r21[r3] = true;
        L41:
            r3 = r3 + 1;
            goto L24
        L28:
            if (r19[r3] == true) goto L30;
        L33:
            r21[r3] = r6;
            if (r21[r3] == false) goto L41;
            if (Util.areEqual(this.trackSelections[r3], r18[r3]) == false) goto L38;
            SampleStream r8 = new SampleStreamImpl(r17, r3);
        L39:
            r20[r3] = r8;
            goto L41
        L38:
            r8 = new EmptySampleStream();
            goto L39
        L30:
            if (r20[r3] == null) goto L33;
            r6 = false;
            goto L33
        L42:
            return r22;
        }

        public int skipData(MediaPeriodImpl r4, int r5, long r6) {
            long r0 = ServerSideAdInsertionUtil.getStreamPositionUs(r6, r4.mediaPeriodId, this.adPlaybackState);
            return ((SampleStream) Util.castNonNull(this.sampleStreams[r5])).skipData(r0);
        }

        public void updateAdPlaybackState(AdPlaybackState r1) {
            this.adPlaybackState = r1;
        }
    }

    public ServerSideAdInsertionMediaSource(MediaSource r3, AdPlaybackStateUpdater r4) {
        this.mediaSource = r3;
        this.adPlaybackStateUpdater = r4;
        this.mediaPeriods = ArrayListMultimap.create();
        this.adPlaybackStates = ImmutableMap.m231of();
        this.mediaSourceEventDispatcherWithoutId = createEventDispatcher(null);
        this.drmEventDispatcherWithoutId = createDrmEventDispatcher(null);
    }

    static /* synthetic */ long access$300(MediaPeriodImpl r2, AdPlaybackState r3) {
        return getMediaPeriodEndPositionUs(r2, r3);
    }

    static /* synthetic */ MediaLoadData access$400(MediaPeriodImpl r1, MediaLoadData r2, AdPlaybackState r3) {
        return correctMediaLoadData(r1, r2, r3);
    }

    private static MediaLoadData correctMediaLoadData(MediaPeriodImpl r10, MediaLoadData r11, AdPlaybackState r12) {
        return new MediaLoadData(r11.dataType, r11.trackType, r11.trackFormat, r11.trackSelectionReason, r11.trackSelectionData, correctMediaLoadDataPositionMs(r11.mediaStartTimeMs, r10, r12), correctMediaLoadDataPositionMs(r11.mediaEndTimeMs, r10, r12));
    }

    private static long correctMediaLoadDataPositionMs(long r7, MediaPeriodImpl r9, AdPlaybackState r10) {
        if (r7 != C0180C.TIME_UNSET) goto L5;
        return C0180C.TIME_UNSET;
    L5:
        long r0 = Util.msToUs(r7);
        MediaSource.MediaPeriodId r2 = r9.mediaPeriodId;
        if (r2.isAd() == false) goto L8;
        long r3 = ServerSideAdInsertionUtil.getMediaPeriodPositionUsForAd(r0, r2.adGroupIndex, r2.adIndexInAdGroup, r10);
    L10:
        return Util.usToMs(r3);
    L8:
        r3 = ServerSideAdInsertionUtil.getMediaPeriodPositionUsForContent(r0, -1, r10);
        goto L10
    }

    private static long getMediaPeriodEndPositionUs(MediaPeriodImpl r9, AdPlaybackState r10) {
        MediaSource.MediaPeriodId r0 = r9.mediaPeriodId;
        if (r0.isAd() == false) goto L10;
        AdPlaybackState.AdGroup r1 = r10.getAdGroup(r0.adGroupIndex);
        if (r1.count != (-1)) goto L8;
        return 0;
    L8:
        return r1.durationsUs[r0.adIndexInAdGroup];
    L10:
        if (r0.nextAdGroupIndex != (-1)) goto L12;
        return Long.MAX_VALUE;
    L12:
        AdPlaybackState.AdGroup r2 = r10.getAdGroup(r0.nextAdGroupIndex);
        if (r2.timeUs != Long.MIN_VALUE) goto L16;
        return Long.MAX_VALUE;
    L16:
        return r2.timeUs;
    }

    private MediaPeriodImpl getMediaPeriodForEvent(MediaSource.MediaPeriodId r6, MediaLoadData r7, boolean r8) {
        if (r6 != null) goto L5;
        return null;
    L5:
        List<SharedMediaPeriod> r1 = this.mediaPeriods.get(new Pair(Long.valueOf(r6.windowSequenceNumber), r6.periodUid));
        if (r1.isEmpty() == false) goto L8;
        return null;
    L8:
        if (r8 == false) goto L14;
        SharedMediaPeriod r0 = (SharedMediaPeriod) Iterables.getLast(r1);
        if (SharedMediaPeriod.access$100(r0) == null) goto L13;
        return SharedMediaPeriod.access$100(r0);
    L13:
        return (MediaPeriodImpl) Iterables.getLast(SharedMediaPeriod.access$200(r0));
    L14:
        int r2 = 0;
    L16:
        if (r2 >= r1.size()) goto L22;
        MediaPeriodImpl r3 = r1.get(r2).getMediaPeriodForEvent(r7);
        if (r3 != null) goto L19;
        r2 = r2 + 1;
        goto L16
    L19:
        return r3;
    L22:
        return (MediaPeriodImpl) SharedMediaPeriod.access$200(r1.get(0)).get(0);
    }

    private void releaseLastUsedMediaPeriod() {
        if (this.lastUsedMediaPeriod == null) goto L6;
        this.lastUsedMediaPeriod.release(this.mediaSource);
        this.lastUsedMediaPeriod = null;
        return;
    }

    @Override // androidx.media3.exoplayer.source.BaseMediaSource, androidx.media3.exoplayer.source.MediaSource
    public boolean canUpdateMediaItem(MediaItem r2) {
        return this.mediaSource.canUpdateMediaItem(r2);
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public MediaPeriod createPeriod(MediaSource.MediaPeriodId r18, Allocator r19, long r20) {
        SharedMediaPeriod r4 = null;
        Pair<Long, Object> r5 = new Pair(Long.valueOf(r18.windowSequenceNumber), r18.periodUid);
        boolean r6 = false;
        if (this.lastUsedMediaPeriod != null) goto L5;
    L9:
        if (r4 != null) goto L17;
        SharedMediaPeriod r7 = (SharedMediaPeriod) Iterables.getLast(this.mediaPeriods.get(r5), null);
        if (r7 != null) goto L13;
    L15:
        AdPlaybackState r8 = (AdPlaybackState) Assertions.checkNotNull(this.adPlaybackStates.get(r18.periodUid));
        long r9 = ServerSideAdInsertionUtil.getStreamPositionUs(r20, r18, r8);
        r4 = new SharedMediaPeriod(this.mediaSource.createPeriod(new MediaSource.MediaPeriodId(r18.periodUid, r18.windowSequenceNumber), r19, r9), r18.periodUid, r8);
        this.mediaPeriods.put(r5, r4);
        goto L17
    L13:
        if (r7.canReuseMediaPeriod(r18, r20) == false) goto L15;
        r4 = r7;
    L17:
        MediaPeriodImpl r10 = new MediaPeriodImpl(r4, r18, createEventDispatcher(r18), createDrmEventDispatcher(r18));
        r4.add(r10);
        if (r6 == true) goto L20;
    L22:
        return r10;
    L20:
        if (r4.trackSelections.length <= 0) goto L22;
        r10.seekToUs(r20);
        goto L22
    L5:
        if (SharedMediaPeriod.access$000(this.lastUsedMediaPeriod).equals(r18.periodUid) == false) goto L7;
        r4 = this.lastUsedMediaPeriod;
        this.mediaPeriods.put(r5, r4);
        r6 = true;
    L8:
        this.lastUsedMediaPeriod = null;
        goto L9
    L7:
        this.lastUsedMediaPeriod.release(this.mediaSource);
        goto L8
    }

    @Override // androidx.media3.exoplayer.source.BaseMediaSource
    protected void disableInternal() {
        releaseLastUsedMediaPeriod();
        this.mediaSource.disable(this);
    }

    @Override // androidx.media3.exoplayer.source.BaseMediaSource
    protected void enableInternal() {
        this.mediaSource.enable(this);
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public MediaItem getMediaItem() {
        return this.mediaSource.getMediaItem();
    }

    /* JADX INFO: renamed from: lambda$setAdPlaybackStates$0$androidx-media3-exoplayer-source-ads-ServerSideAdInsertionMediaSource */
    /* synthetic */ void m136x8b1696bf(ImmutableMap r4, Timeline r5) {
        Iterator<SharedMediaPeriod> r0 = this.mediaPeriods.values().iterator();
    L4:
        if (r0.hasNext() == false) goto L10;
        SharedMediaPeriod r1 = r0.next();
        AdPlaybackState r2 = (AdPlaybackState) r4.get(SharedMediaPeriod.access$000(r1));
        if (r2 == null) goto L4;
        r1.updateAdPlaybackState(r2);
        goto L4
    L10:
        if (this.lastUsedMediaPeriod == null) goto L14;
        AdPlaybackState r3 = (AdPlaybackState) r4.get(SharedMediaPeriod.access$000(this.lastUsedMediaPeriod));
        if (r3 == null) goto L14;
        this.lastUsedMediaPeriod.updateAdPlaybackState(r3);
    L14:
        this.adPlaybackStates = r4;
        refreshSourceInfo(new ServerSideAdInsertionTimeline(r5, r4));
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public void maybeThrowSourceInfoRefreshError() throws IOException {
        this.mediaSource.maybeThrowSourceInfoRefreshError();
    }

    @Override // androidx.media3.exoplayer.source.MediaSourceEventListener
    public void onDownstreamFormatChanged(int r5, MediaSource.MediaPeriodId r6, MediaLoadData r7) {
        MediaPeriodImpl r0 = getMediaPeriodForEvent(r6, r7, false);
        if (r0 != null) goto L5;
        this.mediaSourceEventDispatcherWithoutId.downstreamFormatChanged(r7);
        return;
    L5:
        r0.sharedPeriod.onDownstreamFormatChanged(r0, r7);
        r0.mediaSourceEventDispatcher.downstreamFormatChanged(correctMediaLoadData(r0, r7, (AdPlaybackState) Assertions.checkNotNull(this.adPlaybackStates.get(r0.mediaPeriodId.periodUid))));
    }

    @Override // androidx.media3.exoplayer.drm.DrmSessionEventListener
    public void onDrmKeysLoaded(int r3, MediaSource.MediaPeriodId r4) {
        MediaPeriodImpl r0 = getMediaPeriodForEvent(r4, null, false);
        if (r0 != null) goto L5;
        this.drmEventDispatcherWithoutId.drmKeysLoaded();
        return;
    L5:
        r0.drmEventDispatcher.drmKeysLoaded();
    }

    @Override // androidx.media3.exoplayer.drm.DrmSessionEventListener
    public void onDrmKeysRemoved(int r3, MediaSource.MediaPeriodId r4) {
        MediaPeriodImpl r0 = getMediaPeriodForEvent(r4, null, false);
        if (r0 != null) goto L5;
        this.drmEventDispatcherWithoutId.drmKeysRemoved();
        return;
    L5:
        r0.drmEventDispatcher.drmKeysRemoved();
    }

    @Override // androidx.media3.exoplayer.drm.DrmSessionEventListener
    public void onDrmKeysRestored(int r3, MediaSource.MediaPeriodId r4) {
        MediaPeriodImpl r0 = getMediaPeriodForEvent(r4, null, false);
        if (r0 != null) goto L5;
        this.drmEventDispatcherWithoutId.drmKeysRestored();
        return;
    L5:
        r0.drmEventDispatcher.drmKeysRestored();
    }

    @Override // androidx.media3.exoplayer.drm.DrmSessionEventListener
    public void onDrmSessionAcquired(int r3, MediaSource.MediaPeriodId r4, int r5) {
        MediaPeriodImpl r0 = getMediaPeriodForEvent(r4, null, true);
        if (r0 != null) goto L5;
        this.drmEventDispatcherWithoutId.drmSessionAcquired(r5);
        return;
    L5:
        r0.drmEventDispatcher.drmSessionAcquired(r5);
    }

    @Override // androidx.media3.exoplayer.drm.DrmSessionEventListener
    public void onDrmSessionManagerError(int r3, MediaSource.MediaPeriodId r4, Exception r5) {
        MediaPeriodImpl r0 = getMediaPeriodForEvent(r4, null, false);
        if (r0 != null) goto L5;
        this.drmEventDispatcherWithoutId.drmSessionManagerError(r5);
        return;
    L5:
        r0.drmEventDispatcher.drmSessionManagerError(r5);
    }

    @Override // androidx.media3.exoplayer.drm.DrmSessionEventListener
    public void onDrmSessionReleased(int r3, MediaSource.MediaPeriodId r4) {
        MediaPeriodImpl r0 = getMediaPeriodForEvent(r4, null, false);
        if (r0 != null) goto L5;
        this.drmEventDispatcherWithoutId.drmSessionReleased();
        return;
    L5:
        r0.drmEventDispatcher.drmSessionReleased();
    }

    @Override // androidx.media3.exoplayer.source.MediaSourceEventListener
    public void onLoadCanceled(int r5, MediaSource.MediaPeriodId r6, LoadEventInfo r7, MediaLoadData r8) {
        MediaPeriodImpl r0 = getMediaPeriodForEvent(r6, r8, true);
        if (r0 != null) goto L5;
        this.mediaSourceEventDispatcherWithoutId.loadCanceled(r7, r8);
        return;
    L5:
        r0.sharedPeriod.onLoadFinished(r7);
        r0.mediaSourceEventDispatcher.loadCanceled(r7, correctMediaLoadData(r0, r8, (AdPlaybackState) Assertions.checkNotNull(this.adPlaybackStates.get(r0.mediaPeriodId.periodUid))));
    }

    @Override // androidx.media3.exoplayer.source.MediaSourceEventListener
    public void onLoadCompleted(int r5, MediaSource.MediaPeriodId r6, LoadEventInfo r7, MediaLoadData r8) {
        MediaPeriodImpl r0 = getMediaPeriodForEvent(r6, r8, true);
        if (r0 != null) goto L5;
        this.mediaSourceEventDispatcherWithoutId.loadCompleted(r7, r8);
        return;
    L5:
        r0.sharedPeriod.onLoadFinished(r7);
        r0.mediaSourceEventDispatcher.loadCompleted(r7, correctMediaLoadData(r0, r8, (AdPlaybackState) Assertions.checkNotNull(this.adPlaybackStates.get(r0.mediaPeriodId.periodUid))));
    }

    @Override // androidx.media3.exoplayer.source.MediaSourceEventListener
    public void onLoadError(int r5, MediaSource.MediaPeriodId r6, LoadEventInfo r7, MediaLoadData r8, IOException r9, boolean r10) {
        MediaPeriodImpl r0 = getMediaPeriodForEvent(r6, r8, true);
        if (r0 != null) goto L5;
        this.mediaSourceEventDispatcherWithoutId.loadError(r7, r8, r9, r10);
        return;
    L5:
        if (r10 == false) goto L7;
        r0.sharedPeriod.onLoadFinished(r7);
    L7:
        r0.mediaSourceEventDispatcher.loadError(r7, correctMediaLoadData(r0, r8, (AdPlaybackState) Assertions.checkNotNull(this.adPlaybackStates.get(r0.mediaPeriodId.periodUid))), r9, r10);
    }

    @Override // androidx.media3.exoplayer.source.MediaSourceEventListener
    public void onLoadStarted(int r5, MediaSource.MediaPeriodId r6, LoadEventInfo r7, MediaLoadData r8) {
        MediaPeriodImpl r0 = getMediaPeriodForEvent(r6, r8, true);
        if (r0 != null) goto L5;
        this.mediaSourceEventDispatcherWithoutId.loadStarted(r7, r8);
        return;
    L5:
        r0.sharedPeriod.onLoadStarted(r7, r8);
        r0.mediaSourceEventDispatcher.loadStarted(r7, correctMediaLoadData(r0, r8, (AdPlaybackState) Assertions.checkNotNull(this.adPlaybackStates.get(r0.mediaPeriodId.periodUid))));
    }

    @Override // androidx.media3.exoplayer.source.MediaSource.MediaSourceCaller
    public void onSourceInfoRefreshed(MediaSource r3, Timeline r4) {
        if (this.adPlaybackStateUpdater == null) goto L7;
        if (this.adPlaybackStateUpdater.onAdPlaybackStateUpdateRequested(r4) == false) goto L7;
        return;
    L7:
        if (this.adPlaybackStates.isEmpty() == true) goto L11;
        refreshSourceInfo(new ServerSideAdInsertionTimeline(r4, this.adPlaybackStates));
        return;
    }

    @Override // androidx.media3.exoplayer.source.MediaSourceEventListener
    public void onUpstreamDiscarded(int r5, MediaSource.MediaPeriodId r6, MediaLoadData r7) {
        MediaPeriodImpl r0 = getMediaPeriodForEvent(r6, r7, false);
        if (r0 != null) goto L5;
        this.mediaSourceEventDispatcherWithoutId.upstreamDiscarded(r7);
        return;
    L5:
        r0.mediaSourceEventDispatcher.upstreamDiscarded(correctMediaLoadData(r0, r7, (AdPlaybackState) Assertions.checkNotNull(this.adPlaybackStates.get(r0.mediaPeriodId.periodUid))));
    }

    @Override // androidx.media3.exoplayer.source.BaseMediaSource
    protected void prepareSourceInternal(TransferListener r4) {
        Handler r0 = Util.createHandlerForCurrentLooper();
        monitor-enter(this);
        this.playbackHandler = r0;     // Catch: Throwable -> L8
        monitor-exit(this);     // Catch: Throwable -> L8
        this.mediaSource.addEventListener(r0, this);
        this.mediaSource.addDrmEventListener(r0, this);
        this.mediaSource.prepareSource(this, r4, getPlayerId());
        return;
    L8:
        th = move-exception;
        throw th;
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public void releasePeriod(MediaPeriod r6) {
        MediaPeriodImpl r0 = (MediaPeriodImpl) r6;
        r0.sharedPeriod.remove(r0);
        if (r0.sharedPeriod.isUnused() == false) goto L9;
        this.mediaPeriods.remove(new Pair(Long.valueOf(r0.mediaPeriodId.windowSequenceNumber), r0.mediaPeriodId.periodUid), r0.sharedPeriod);
        if (this.mediaPeriods.isEmpty() == false) goto L7;
        this.lastUsedMediaPeriod = r0.sharedPeriod;
        return;
    L7:
        r0.sharedPeriod.release(this.mediaSource);
        return;
    }

    @Override // androidx.media3.exoplayer.source.BaseMediaSource
    protected void releaseSourceInternal() {
        releaseLastUsedMediaPeriod();
        monitor-enter(this);
        this.playbackHandler = null;     // Catch: Throwable -> L9
        monitor-exit(this);     // Catch: Throwable -> L9
        this.mediaSource.releaseSource(this);
        this.mediaSource.removeEventListener(this);
        this.mediaSource.removeDrmEventListener(this);
        return;
    L9:
        th = move-exception;
        throw th;
    }

    public void setAdPlaybackStates(final ImmutableMap<Object, AdPlaybackState> r20, final Timeline r21) {
        Assertions.checkArgument(!r20.isEmpty());
        Object r5 = Assertions.checkNotNull(r20.values().asList().get(0).adsId);
        UnmodifiableIterator<Map.Entry<Object, AdPlaybackState>> r0 = r20.entrySet().iterator();
    L4:
        if (r0.hasNext() == false) goto L37;
        Map.Entry<Object, AdPlaybackState> r6 = r0.next();
        Object r7 = r6.getKey();
        AdPlaybackState r8 = r6.getValue();
        Assertions.checkArgument(Util.areEqual(r5, r8.adsId));
        AdPlaybackState r9 = this.adPlaybackStates.get(r7);
        if (r9 == null) goto L35;
        int r10 = r8.removedAdGroupCount;
    L9:
        if (r10 >= r8.adGroupCount) goto L35;
        AdPlaybackState.AdGroup r11 = r8.getAdGroup(r10);
        Assertions.checkArgument(r11.isServerSideInserted);
        if (r10 >= r9.adGroupCount) goto L26;
        if (ServerSideAdInsertionUtil.getAdCountInGroup(r8, r10) >= ServerSideAdInsertionUtil.getAdCountInGroup(r9, r10)) goto L26;
        AdPlaybackState.AdGroup r12 = r8.getAdGroup(r10 + 1);
        if ((r11.contentResumeOffsetUs + r12.contentResumeOffsetUs) != r9.getAdGroup(r10).contentResumeOffsetUs) goto L17;
        boolean r4 = true;
    L18:
        Assertions.checkArgument(r4);
        if ((r11.timeUs + r11.contentResumeOffsetUs) != r12.timeUs) goto L21;
        boolean r3 = true;
    L22:
        Assertions.checkArgument(r3);
        goto L26
    L21:
        r3 = false;
        goto L22
    L17:
        r4 = false;
    L26:
        if (r11.timeUs != Long.MIN_VALUE) goto L32;
        if (ServerSideAdInsertionUtil.getAdCountInGroup(r8, r10) != 0) goto L30;
        boolean r13 = true;
    L31:
        Assertions.checkArgument(r13);
        goto L32
    L30:
        r13 = false;
    L32:
        r10 = r10 + 1;
        r5 = r5;
    L35:
        r5 = r5;
        goto L4
    L37:
        monitor-enter(this);
    L46:
        th = th;
    L48:
        monitor-exit(this);     // Catch: Throwable -> L50
        throw th;
    L39:
        if (this.playbackHandler != null) goto L43;
        this.adPlaybackStates = r20;     // Catch: Throwable -> L46
    L44:
        monitor-exit(this);     // Catch: Throwable -> L50
        return;
    L43:
        this.playbackHandler.post(new ServerSideAdInsertionMediaSource$$ExternalSyntheticLambda0(this, r20, r21));     // Catch: Throwable -> L50
    L50:
        th = th;
        goto L48
    }

    @Override // androidx.media3.exoplayer.source.BaseMediaSource, androidx.media3.exoplayer.source.MediaSource
    public void updateMediaItem(MediaItem r2) {
        this.mediaSource.updateMediaItem(r2);
    }
}
