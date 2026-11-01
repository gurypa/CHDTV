package androidx.media3.extractor.text.webvtt;

import androidx.media3.common.text.Cue;

/* JADX INFO: loaded from: classes.dex */
public final class WebvttCueInfo {
    public final Cue cue;
    public final long endTimeUs;
    public final long startTimeUs;

    public WebvttCueInfo(Cue r1, long r2, long r4) {
        this.cue = r1;
        this.startTimeUs = r2;
        this.endTimeUs = r4;
    }
}
