package androidx.media3.exoplayer.dash;

import androidx.media3.common.Format;
import androidx.media3.datasource.TransferListener;
import androidx.media3.exoplayer.analytics.PlayerId;
import androidx.media3.exoplayer.dash.manifest.DashManifest;
import androidx.media3.exoplayer.source.chunk.ChunkSource;
import androidx.media3.exoplayer.trackselection.ExoTrackSelection;
import androidx.media3.exoplayer.upstream.CmcdConfiguration;
import androidx.media3.exoplayer.upstream.LoaderErrorThrower;
import androidx.media3.extractor.text.SubtitleParser;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface DashChunkSource extends ChunkSource {

    public interface Factory {

        /* JADX INFO: renamed from: androidx.media3.exoplayer.dash.DashChunkSource$Factory$-CC, reason: invalid class name */
        public final /* synthetic */ class CC {
            public static Factory $default$experimentalParseSubtitlesDuringExtraction(Factory r0, boolean r1) {
                return r0;
            }

            public static Format $default$getOutputTextFormat(Factory r0, Format r1) {
                return r1;
            }

            public static Factory $default$setSubtitleParserFactory(Factory r0, SubtitleParser.Factory r1) {
                return r0;
            }
        }

        DashChunkSource createDashChunkSource(LoaderErrorThrower r1, DashManifest r2, BaseUrlExclusionList r3, int r4, int[] r5, ExoTrackSelection r6, int r7, long r8, boolean r10, List<Format> r11, PlayerEmsgHandler.PlayerTrackEmsgHandler r12, TransferListener r13, PlayerId r14, CmcdConfiguration r15);

        Factory experimentalParseSubtitlesDuringExtraction(boolean r1);

        Format getOutputTextFormat(Format r1);

        Factory setSubtitleParserFactory(SubtitleParser.Factory r1);
    }

    void updateManifest(DashManifest r1, int r2);

    void updateTrackSelection(ExoTrackSelection r1);
}
