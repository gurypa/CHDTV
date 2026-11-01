package androidx.media3.exoplayer.source;

import androidx.media3.common.StreamKey;
import androidx.media3.exoplayer.LoadingInfo;
import androidx.media3.exoplayer.SeekParameters;
import androidx.media3.exoplayer.trackselection.ExoTrackSelection;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface MediaPeriod extends SequenceableLoader {

    /* JADX INFO: renamed from: androidx.media3.exoplayer.source.MediaPeriod$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static List $default$getStreamKeys(MediaPeriod r1, List r2) {
            return Collections.emptyList();
        }
    }

    public interface Callback extends SequenceableLoader.Callback<MediaPeriod> {
        void onPrepared(MediaPeriod r1);
    }

    @Override // androidx.media3.exoplayer.source.SequenceableLoader
    boolean continueLoading(LoadingInfo r1);

    void discardBuffer(long r1, boolean r3);

    long getAdjustedSeekPositionUs(long r1, SeekParameters r3);

    @Override // androidx.media3.exoplayer.source.SequenceableLoader
    long getBufferedPositionUs();

    @Override // androidx.media3.exoplayer.source.SequenceableLoader
    long getNextLoadPositionUs();

    List<StreamKey> getStreamKeys(List<ExoTrackSelection> r1);

    TrackGroupArray getTrackGroups();

    @Override // androidx.media3.exoplayer.source.SequenceableLoader
    boolean isLoading();

    void maybeThrowPrepareError() throws IOException;

    void prepare(Callback r1, long r2);

    long readDiscontinuity();

    @Override // androidx.media3.exoplayer.source.SequenceableLoader
    void reevaluateBuffer(long r1);

    long seekToUs(long r1);

    long selectTracks(ExoTrackSelection[] r1, boolean[] r2, SampleStream[] r3, boolean[] r4, long r5);
}
