package androidx.media3.common.audio;

import androidx.media3.common.PlaybackParameters;

/* JADX INFO: loaded from: classes.dex */
public interface AudioProcessorChain {
    PlaybackParameters applyPlaybackParameters(PlaybackParameters r1);

    boolean applySkipSilenceEnabled(boolean r1);

    AudioProcessor[] getAudioProcessors();

    long getMediaDuration(long r1);

    long getSkippedOutputFrameCount();
}
