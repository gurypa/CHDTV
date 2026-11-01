package androidx.media3.exoplayer;

import androidx.media3.common.MediaItem;

/* JADX INFO: loaded from: classes.dex */
public interface LivePlaybackSpeedControl {
    float getAdjustedPlaybackSpeed(long r1, long r3);

    long getTargetLiveOffsetUs();

    void notifyRebuffer();

    void setLiveConfiguration(MediaItem.LiveConfiguration r1);

    void setTargetLiveOffsetOverrideUs(long r1);
}
