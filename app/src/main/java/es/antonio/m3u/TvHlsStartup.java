package es.antonio.m3u;

import android.content.Context;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.TrackGroup;
import androidx.media3.common.TrackSelectionParameters;
import androidx.media3.common.util.Clock;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.Renderer;
import androidx.media3.exoplayer.RenderersFactory;
import androidx.media3.exoplayer.hls.playlist.HlsMultivariantPlaylist;
import androidx.media3.exoplayer.source.chunk.MediaChunk;
import androidx.media3.exoplayer.source.chunk.MediaChunkIterator;
import androidx.media3.exoplayer.trackselection.AdaptiveTrackSelection;
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector;
import androidx.media3.exoplayer.upstream.BandwidthMeter;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/* JADX INFO: loaded from: classes7.dex */
public final class TvHlsStartup {
    static final int INITIAL_VIDEO_BITRATE = 1200000;

    static final class Factory extends AdaptiveTrackSelection.Factory {
        Factory() {
        }

        @Override // androidx.media3.exoplayer.trackselection.AdaptiveTrackSelection.Factory
        protected AdaptiveTrackSelection createAdaptiveTrackSelection(TrackGroup r8, int[] r9, int r10, BandwidthMeter r11, ImmutableList<AdaptiveTrackSelection.AdaptationCheckpoint> r12) {
            return new Selection(r8, r9, r10, r11, r12);
        }
    }

    static final class Selection extends AdaptiveTrackSelection {
        private boolean first;
        private boolean logged;

        Selection(TrackGroup r18, int[] r19, int r20, BandwidthMeter r21, List<AdaptiveTrackSelection.AdaptationCheckpoint> r22) {
            super(r18, r19, r20, r21, Renderer.DEFAULT_DURATION_TO_PROGRESS_US, 25000, 25000, AdaptiveTrackSelection.DEFAULT_MAX_WIDTH_TO_DISCARD, AdaptiveTrackSelection.DEFAULT_MAX_HEIGHT_TO_DISCARD, 0.7f, 0.75f, r22, Clock.DEFAULT);
            this.first = true;
        }

        @Override // androidx.media3.exoplayer.trackselection.AdaptiveTrackSelection
        protected boolean canSelectFormat(Format r2, int r3, long r4) {
            if (this.first == false) goto L9;
            if (r2.height <= 0) goto L9;
            if (r3 <= TvHlsStartup.INITIAL_VIDEO_BITRATE) goto L9;
            return false;
        L9:
            if (super.canSelectFormat(r2, r3, r4) == false) goto L14;
            return true;
        L14:
            return false;
        }

        @Override // androidx.media3.exoplayer.trackselection.AdaptiveTrackSelection, androidx.media3.exoplayer.trackselection.ExoTrackSelection
        public void updateSelectedTrack(long r2, long r4, long r6, List<? extends MediaChunk> r8, MediaChunkIterator[] r9) {
            if (r8.isEmpty() == true) goto L5;
            this.first = false;
        L5:
            super.updateSelectedTrack(r2, r4, r6, r8, r9);
            if (this.first == true) goto L8;
            return;
        L8:
            if (this.logged == true) goto L12;
            this.logged = true;
            Format r3 = getSelectedFormat();
            BlackBox.event("HLS_START_VIDEO", "bitrate=" + r3.bitrate + " width=" + r3.width + " height=" + r3.height + " first_chunk_only=true");
            return;
        }
    }

    public TvHlsStartup() {
    }

    public static HlsMultivariantPlaylist align(HlsMultivariantPlaylist r19) {
        if (r19.variants.size() >= 2) goto L5;
        return r19;
    L5:
        HlsMultivariantPlaylist.Variant r1 = r19.variants.get(0);
        String r3 = MimeTypes.getVideoMediaMimeType(r1.format.codecs);
        if (r3 != null) goto L8;
        return r19;
    L8:
        Iterator<HlsMultivariantPlaylist.Variant> r4 = r19.variants.iterator();
        HlsMultivariantPlaylist.Variant r5 = null;
        int r8 = Integer.MAX_VALUE;
        int r7 = -1;
        HlsMultivariantPlaylist.Variant r6 = null;
    L10:
        if (r4.hasNext() == false) goto L11;
        HlsMultivariantPlaylist.Variant r9 = r4.next();
        Format r10 = r9.format;
        if (r10.height <= 0) goto L10;
        if (r10.width <= 0) goto L10;
        if (r10.bitrate <= 0) goto L10;
        if ((r10.roleFlags & 16384) != 0) goto L10;
        if (r3.equals(MimeTypes.getVideoMediaMimeType(r10.codecs)) == false) goto L10;
        if (Objects.equals(r1.audioGroupId, r9.audioGroupId) == false) goto L10;
        if (Objects.equals(r1.videoGroupId, r9.videoGroupId) == false) goto L10;
        if (Objects.equals(r1.subtitleGroupId, r9.subtitleGroupId) == false) goto L10;
        if (Objects.equals(r1.captionGroupId, r9.captionGroupId) == false) goto L10;
        if (r10.bitrate >= r8) goto L43;
        r8 = r10.bitrate;
        r6 = r9;
    L43:
        if (r10.bitrate > INITIAL_VIDEO_BITRATE) goto L10;
        if (r10.bitrate <= r7) goto L10;
        r7 = r10.bitrate;
        r5 = r9;
        goto L10
    L11:
        if (r5 != null) goto L13;
        r5 = r6;
    L13:
        if (r5 == null) goto L18;
        if (r5 == r1) goto L18;
        ArrayList r11 = new ArrayList(r19.variants);
        r11.remove(r5);
        r11.add(0, r5);
        BlackBox.event("HLS_START_PLAYLIST", "bitrate=" + r5.format.bitrate + " width=" + r5.format.width + " height=" + r5.format.height + " variants_retained=" + r11.size());
        return new HlsMultivariantPlaylist(r19.baseUri, r19.tags, r11, r19.videos, r19.audios, r19.subtitles, r19.closedCaptions, r19.muxedAudioFormat, r19.muxedCaptionFormats, r19.hasIndependentSegments, r19.variableDefinitions, r19.sessionKeyDrmInitData);
    L18:
        return r19;
    }

    public static boolean automatic(TrackSelectionParameters r2) {
        if (r2 != null) goto L4;
        return true;
    L4:
        if (r2.overrides.isEmpty() == true) goto L6;
        return false;
    L6:
        if (r2.forceHighestSupportedBitrate == false) goto L8;
        return false;
    L8:
        if (r2.forceLowestBitrate == false) goto L10;
        return false;
    L10:
        if (r2.minVideoWidth == 0) goto L12;
        return false;
    L12:
        if (r2.minVideoHeight == 0) goto L14;
        return false;
    L14:
        if (r2.minVideoBitrate == 0) goto L16;
        return false;
    L16:
        if (r2.maxVideoBitrate == Integer.MAX_VALUE) goto L18;
        return false;
    L18:
        if (r2.maxVideoWidth == Integer.MAX_VALUE) goto L20;
        return false;
    L20:
        if (r2.maxVideoHeight == Integer.MAX_VALUE) goto L33;
        return false;
    L33:
        return true;
    }

    public static ExoPlayer.Builder builder(Context r2, RenderersFactory r3) {
        ExoPlayer.Builder r0 = new ExoPlayer.Builder(r2, r3);
        if ((r2 instanceof PlayerActivity) == false) goto L10;
        PlayerActivity r4 = (PlayerActivity) r2;
        Entry r1 = r4.tvCurrent();
        if (r4.tvHlsFastStartup() == false) goto L10;
        if (r1 == null) goto L10;
        if (MimeTypes.APPLICATION_M3U8.equals(StreamFormat.mime(r1)) == false) goto L10;
        r0.setTrackSelector(new DefaultTrackSelector(r2, new Factory()));
    L10:
        return r0;
    }
}
