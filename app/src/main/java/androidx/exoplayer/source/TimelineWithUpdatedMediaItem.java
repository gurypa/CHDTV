package androidx.media3.exoplayer.source;

import androidx.media3.common.MediaItem;
import androidx.media3.common.Timeline;

/* JADX INFO: loaded from: classes.dex */
public final class TimelineWithUpdatedMediaItem extends ForwardingTimeline {
    private final MediaItem updatedMediaItem;

    public TimelineWithUpdatedMediaItem(Timeline r1, MediaItem r2) {
        super(r1);
        this.updatedMediaItem = r2;
    }

    @Override // androidx.media3.exoplayer.source.ForwardingTimeline, androidx.media3.common.Timeline
    public Timeline.Window getWindow(int r2, Timeline.Window r3, long r4) {
        super.getWindow(r2, r3, r4);
        r3.mediaItem = this.updatedMediaItem;
        if (this.updatedMediaItem.localConfiguration == null) goto L5;
        Object r0 = this.updatedMediaItem.localConfiguration.tag;
    L6:
        r3.tag = r0;
        return r3;
    L5:
        r0 = null;
        goto L6
    }
}
