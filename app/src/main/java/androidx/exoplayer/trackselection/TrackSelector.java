package androidx.media3.exoplayer.trackselection;

import android.content.Context;
import androidx.media3.common.AudioAttributes;
import androidx.media3.common.Timeline;
import androidx.media3.common.TrackSelectionParameters;
import androidx.media3.common.util.Assertions;
import androidx.media3.exoplayer.ExoPlaybackException;
import androidx.media3.exoplayer.Renderer;
import androidx.media3.exoplayer.RendererCapabilities;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.exoplayer.source.TrackGroupArray;
import androidx.media3.exoplayer.upstream.BandwidthMeter;

/* JADX INFO: loaded from: classes.dex */
public abstract class TrackSelector {
    private BandwidthMeter bandwidthMeter;
    private InvalidationListener listener;

    public interface Factory {
        TrackSelector createTrackSelector(Context r1);
    }

    public interface InvalidationListener {

        /* JADX INFO: renamed from: androidx.media3.exoplayer.trackselection.TrackSelector$InvalidationListener$-CC, reason: invalid class name */
        public final /* synthetic */ class CC {
            public static void $default$onRendererCapabilitiesChanged(InvalidationListener r0, Renderer r1) {
            }
        }

        void onRendererCapabilitiesChanged(Renderer r1);

        void onTrackSelectionsInvalidated();
    }

    public TrackSelector() {
    }

    protected final BandwidthMeter getBandwidthMeter() {
        return (BandwidthMeter) Assertions.checkStateNotNull(this.bandwidthMeter);
    }

    public TrackSelectionParameters getParameters() {
        return TrackSelectionParameters.DEFAULT_WITHOUT_CONTEXT;
    }

    public RendererCapabilities.Listener getRendererCapabilitiesListener() {
        return null;
    }

    public void init(InvalidationListener r1, BandwidthMeter r2) {
        this.listener = r1;
        this.bandwidthMeter = r2;
    }

    protected final void invalidate() {
        if (this.listener == null) goto L6;
        this.listener.onTrackSelectionsInvalidated();
        return;
    }

    protected final void invalidateForRendererCapabilitiesChange(Renderer r2) {
        if (this.listener == null) goto L6;
        this.listener.onRendererCapabilitiesChanged(r2);
        return;
    }

    public boolean isSetParametersSupported() {
        return false;
    }

    public abstract void onSelectionActivated(Object r1);

    public void release() {
        this.listener = null;
        this.bandwidthMeter = null;
    }

    public abstract TrackSelectorResult selectTracks(RendererCapabilities[] r1, TrackGroupArray r2, MediaSource.MediaPeriodId r3, Timeline r4) throws ExoPlaybackException;

    public void setAudioAttributes(AudioAttributes r1) {
    }

    public void setParameters(TrackSelectionParameters r1) {
    }
}
