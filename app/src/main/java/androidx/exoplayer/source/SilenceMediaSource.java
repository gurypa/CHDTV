package androidx.media3.exoplayer.source;

import android.net.Uri;
import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.MediaItem;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.TrackGroup;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.TransferListener;
import androidx.media3.decoder.DecoderInputBuffer;
import androidx.media3.exoplayer.FormatHolder;
import androidx.media3.exoplayer.LoadingInfo;
import androidx.media3.exoplayer.SeekParameters;
import androidx.media3.exoplayer.trackselection.ExoTrackSelection;
import androidx.media3.exoplayer.upstream.Allocator;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class SilenceMediaSource extends BaseMediaSource {
    private static final int CHANNEL_COUNT = 2;
    private static final Format FORMAT = null;
    public static final String MEDIA_ID = "SilenceMediaSource";
    private static final MediaItem MEDIA_ITEM = null;
    private static final int PCM_ENCODING = 2;
    private static final int SAMPLE_RATE_HZ = 44100;
    private static final byte[] SILENCE_SAMPLE = null;
    private final long durationUs;
    private MediaItem mediaItem;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.source.SilenceMediaSource$1 */
    static /* synthetic */ class C03481 {
    }

    public static final class Factory {
        private long durationUs;
        private Object tag;

        public Factory() {
        }

        public SilenceMediaSource createMediaSource() {
            if (this.durationUs <= 0) goto L5;
            boolean r0 = true;
        L6:
            Assertions.checkState(r0);
            return new SilenceMediaSource(this.durationUs, SilenceMediaSource.access$000().buildUpon().setTag(this.tag).build(), null);
        L5:
            r0 = false;
            goto L6
        }

        public Factory setDurationUs(long r1) {
            this.durationUs = r1;
            return this;
        }

        public Factory setTag(Object r1) {
            this.tag = r1;
            return this;
        }
    }

    private static final class SilenceMediaPeriod implements MediaPeriod {
        private static final TrackGroupArray TRACKS = null;
        private final long durationUs;
        private final ArrayList<SampleStream> sampleStreams;

        static {
            TRACKS = new TrackGroupArray(new TrackGroup[]{new TrackGroup(new Format[]{SilenceMediaSource.access$200()})});
        }

        public SilenceMediaPeriod(long r2) {
            this.durationUs = r2;
            this.sampleStreams = new ArrayList();
        }

        private long constrainSeekPosition(long r7) {
            return Util.constrainValue(r7, 0, this.durationUs);
        }

        @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
        public boolean continueLoading(LoadingInfo r2) {
            return false;
        }

        @Override // androidx.media3.exoplayer.source.MediaPeriod
        public void discardBuffer(long r1, boolean r3) {
        }

        @Override // androidx.media3.exoplayer.source.MediaPeriod
        public long getAdjustedSeekPositionUs(long r3, SeekParameters r5) {
            return constrainSeekPosition(r3);
        }

        @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
        public long getBufferedPositionUs() {
            return Long.MIN_VALUE;
        }

        @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
        public long getNextLoadPositionUs() {
            return Long.MIN_VALUE;
        }

        @Override // androidx.media3.exoplayer.source.MediaPeriod
        public /* synthetic */ List getStreamKeys(List r1) {
            return MediaPeriod.CC.$default$getStreamKeys(this, r1);
        }

        @Override // androidx.media3.exoplayer.source.MediaPeriod
        public TrackGroupArray getTrackGroups() {
            return TRACKS;
        }

        @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
        public boolean isLoading() {
            return false;
        }

        @Override // androidx.media3.exoplayer.source.MediaPeriod
        public void maybeThrowPrepareError() {
        }

        @Override // androidx.media3.exoplayer.source.MediaPeriod
        public void prepare(MediaPeriod.Callback r1, long r2) {
            r1.onPrepared(this);
        }

        @Override // androidx.media3.exoplayer.source.MediaPeriod
        public long readDiscontinuity() {
            return C0180C.TIME_UNSET;
        }

        @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
        public void reevaluateBuffer(long r1) {
        }

        @Override // androidx.media3.exoplayer.source.MediaPeriod
        public long seekToUs(long r3) {
            long r4 = constrainSeekPosition(r3);
            int r0 = 0;
        L4:
            if (r0 >= this.sampleStreams.size()) goto L6;
            ((SilenceSampleStream) this.sampleStreams.get(r0)).seekTo(r4);
            r0 = r0 + 1;
            goto L4
        L6:
            return r4;
        }

        @Override // androidx.media3.exoplayer.source.MediaPeriod
        public long selectTracks(ExoTrackSelection[] r5, boolean[] r6, SampleStream[] r7, boolean[] r8, long r9) {
            long r10 = constrainSeekPosition(r9);
            int r0 = 0;
        L4:
            if (r0 >= r5.length) goto L18;
            if (r7[r0] == null) goto L13;
            if (r5[r0] != null) goto L10;
        L11:
            this.sampleStreams.remove(r7[r0]);
            r7[r0] = null;
            goto L13
        L10:
            if (r6[r0] == false) goto L11;
        L13:
            if (r7[r0] != null) goto L17;
            if (r5[r0] == null) goto L17;
            SilenceSampleStream r1 = new SilenceSampleStream(this.durationUs);
            r1.seekTo(r10);
            this.sampleStreams.add(r1);
            r7[r0] = r1;
            r8[r0] = true;
        L17:
            r0 = r0 + 1;
            goto L4
        L18:
            return r10;
        }
    }

    private static final class SilenceSampleStream implements SampleStream {
        private final long durationBytes;
        private long positionBytes;
        private boolean sentFormat;

        public SilenceSampleStream(long r3) {
            this.durationBytes = SilenceMediaSource.access$300(r3);
            seekTo(0);
        }

        @Override // androidx.media3.exoplayer.source.SampleStream
        public boolean isReady() {
            return true;
        }

        @Override // androidx.media3.exoplayer.source.SampleStream
        public void maybeThrowError() {
        }

        @Override // androidx.media3.exoplayer.source.SampleStream
        public int readData(FormatHolder r10, DecoderInputBuffer r11, int r12) {
            if (this.sentFormat == true) goto L5;
        L18:
            r10.format = SilenceMediaSource.access$200();
            this.sentFormat = true;
            return -5;
        L5:
            if ((r12 & 2) != 0) goto L18;
            long r2 = this.durationBytes - this.positionBytes;
            if (r2 != 0) goto L11;
            r11.addFlag(4);
            return -4;
        L11:
            r11.timeUs = SilenceMediaSource.access$400(this.positionBytes);
            r11.addFlag(1);
            int r0 = (int) Math.min(SilenceMediaSource.access$500().length, r2);
            if ((r12 & 4) != 0) goto L15;
            r11.ensureSpaceForWrite(r0);
            r11.data.put(SilenceMediaSource.access$500(), 0, r0);
        L15:
            if ((r12 & 1) != 0) goto L17;
            this.positionBytes += (long) r0;
        L17:
            return -4;
        }

        public void seekTo(long r7) {
            this.positionBytes = Util.constrainValue(SilenceMediaSource.access$300(r7), 0, this.durationBytes);
        }

        @Override // androidx.media3.exoplayer.source.SampleStream
        public int skipData(long r7) {
            long r0 = this.positionBytes;
            seekTo(r7);
            return (int) ((this.positionBytes - r0) / ((long) SilenceMediaSource.access$500().length));
        }
    }

    static {
        FORMAT = new Format.Builder().setSampleMimeType(MimeTypes.AUDIO_RAW).setChannelCount(2).setSampleRate(SAMPLE_RATE_HZ).setPcmEncoding(2).build();
        MEDIA_ITEM = new MediaItem.Builder().setMediaId(MEDIA_ID).setUri(Uri.EMPTY).setMimeType(FORMAT.sampleMimeType).build();
        SILENCE_SAMPLE = new byte[Util.getPcmFrameSize(2, 2) * 1024];
    }

    public SilenceMediaSource(long r2) {
        this(r2, MEDIA_ITEM);
    }

    private SilenceMediaSource(long r3, MediaItem r5) {
        if (r3 < 0) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkArgument(r0);
        this.durationUs = r3;
        this.mediaItem = r5;
        return;
    L5:
        r0 = false;
        goto L6
    }

    /* synthetic */ SilenceMediaSource(long r1, MediaItem r3, C03481 r4) {
        this(r1, r3);
    }

    static /* synthetic */ MediaItem access$000() {
        return MEDIA_ITEM;
    }

    static /* synthetic */ Format access$200() {
        return FORMAT;
    }

    static /* synthetic */ long access$300(long r2) {
        return getAudioByteCount(r2);
    }

    static /* synthetic */ long access$400(long r2) {
        return getAudioPositionUs(r2);
    }

    static /* synthetic */ byte[] access$500() {
        return SILENCE_SAMPLE;
    }

    private static long getAudioByteCount(long r4) {
        return Util.getPcmFrameSize(2, 2) * ((44100 * r4) / 1000000);
    }

    private static long getAudioPositionUs(long r6) {
        return (1000000 * (r6 / ((long) Util.getPcmFrameSize(2, 2)))) / 44100;
    }

    @Override // androidx.media3.exoplayer.source.BaseMediaSource, androidx.media3.exoplayer.source.MediaSource
    public boolean canUpdateMediaItem(MediaItem r2) {
        return true;
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public MediaPeriod createPeriod(MediaSource.MediaPeriodId r4, Allocator r5, long r6) {
        return new SilenceMediaPeriod(this.durationUs);
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public synchronized MediaItem getMediaItem() {
        monitor-enter(this);
        MediaItem r0 = this.mediaItem;     // Catch: Throwable -> L6
        monitor-exit(this);
        return r0;
    L6:
        th = move-exception;
        throw th;
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public void maybeThrowSourceInfoRefreshError() {
    }

    @Override // androidx.media3.exoplayer.source.BaseMediaSource
    protected void prepareSourceInternal(TransferListener r9) {
        refreshSourceInfo(new SinglePeriodTimeline(this.durationUs, true, false, false, null, getMediaItem()));
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public void releasePeriod(MediaPeriod r1) {
    }

    @Override // androidx.media3.exoplayer.source.BaseMediaSource
    protected void releaseSourceInternal() {
    }

    @Override // androidx.media3.exoplayer.source.BaseMediaSource, androidx.media3.exoplayer.source.MediaSource
    public synchronized void updateMediaItem(MediaItem r1) {
        monitor-enter(this);
        this.mediaItem = r1;     // Catch: Throwable -> L6
        monitor-exit(this);
        return;
    L6:
        th = move-exception;
        throw th;
    }
}
