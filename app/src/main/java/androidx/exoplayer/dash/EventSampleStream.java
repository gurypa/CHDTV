package androidx.media3.exoplayer.dash;

import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.util.Util;
import androidx.media3.decoder.DecoderInputBuffer;
import androidx.media3.exoplayer.FormatHolder;
import androidx.media3.exoplayer.dash.manifest.EventStream;
import androidx.media3.exoplayer.source.SampleStream;
import androidx.media3.extractor.metadata.emsg.EventMessageEncoder;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
final class EventSampleStream implements SampleStream {
    private int currentIndex;
    private final EventMessageEncoder eventMessageEncoder;
    private EventStream eventStream;
    private boolean eventStreamAppendable;
    private long[] eventTimesUs;
    private boolean isFormatSentDownstream;
    private long pendingSeekPositionUs;
    private final Format upstreamFormat;

    public EventSampleStream(EventStream r3, Format r4, boolean r5) {
        this.upstreamFormat = r4;
        this.eventStream = r3;
        this.eventMessageEncoder = new EventMessageEncoder();
        this.pendingSeekPositionUs = C0180C.TIME_UNSET;
        this.eventTimesUs = r3.presentationTimesUs;
        updateEventStream(r3, r5);
    }

    public String eventStreamId() {
        return this.eventStream.m91id();
    }

    @Override // androidx.media3.exoplayer.source.SampleStream
    public boolean isReady() {
        return true;
    }

    @Override // androidx.media3.exoplayer.source.SampleStream
    public void maybeThrowError() throws IOException {
    }

    @Override // androidx.media3.exoplayer.source.SampleStream
    public int readData(FormatHolder r8, DecoderInputBuffer r9, int r10) {
        if (this.currentIndex != this.eventTimesUs.length) goto L5;
        boolean r0 = true;
    L7:
        if (r0 == false) goto L13;
        if (this.eventStreamAppendable == true) goto L13;
        r9.setFlags(4);
        return -4;
    L13:
        if ((r10 & 2) == 0) goto L15;
    L28:
        r8.format = this.upstreamFormat;
        this.isFormatSentDownstream = true;
        return -5;
    L15:
        if (this.isFormatSentDownstream == false) goto L28;
        if (r0 == false) goto L20;
        return -3;
    L20:
        int r3 = this.currentIndex;
        if ((r10 & 1) != 0) goto L24;
        this.currentIndex++;
    L24:
        if ((r10 & 4) != 0) goto L26;
        byte[] r4 = this.eventMessageEncoder.encode(this.eventStream.events[r3]);
        r9.ensureSpaceForWrite(r4.length);
        r9.data.put(r4);
    L26:
        r9.timeUs = this.eventTimesUs[r3];
        r9.setFlags(1);
        return -4;
    L5:
        r0 = false;
        goto L7
    }

    public void seekToUs(long r5) {
        boolean r1 = true;
        this.currentIndex = Util.binarySearchCeil(this.eventTimesUs, r5, true, false);
        if (this.eventStreamAppendable == true) goto L5;
    L7:
        r1 = false;
    L8:
        if (r1 == false) goto L10;
        long r2 = r5;
    L11:
        this.pendingSeekPositionUs = r2;
        return;
    L10:
        r2 = C0180C.TIME_UNSET;
        goto L11
    L5:
        if (this.currentIndex != this.eventTimesUs.length) goto L7;
        goto L7
    }

    @Override // androidx.media3.exoplayer.source.SampleStream
    public int skipData(long r5) {
        int r0 = Math.max(this.currentIndex, Util.binarySearchCeil(this.eventTimesUs, r5, true, false));
        int r1 = r0 - this.currentIndex;
        this.currentIndex = r0;
        return r1;
    }

    public void updateEventStream(EventStream r8, boolean r9) {
        if (this.currentIndex != 0) goto L5;
        long r3 = -9223372036854775807L;
    L6:
        this.eventStreamAppendable = r9;
        this.eventStream = r8;
        this.eventTimesUs = r8.presentationTimesUs;
        if (this.pendingSeekPositionUs == C0180C.TIME_UNSET) goto L10;
        seekToUs(this.pendingSeekPositionUs);
        return;
    L10:
        if (r3 == C0180C.TIME_UNSET) goto L14;
        this.currentIndex = Util.binarySearchCeil(this.eventTimesUs, r3, false, false);
        return;
    L14:
        return;
    L5:
        r3 = this.eventTimesUs[this.currentIndex - 1];
        goto L6
    }
}
