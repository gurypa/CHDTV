package androidx.media3.exoplayer.smoothstreaming;

import androidx.media3.common.Format;
import androidx.media3.datasource.TransferListener;
import androidx.media3.exoplayer.smoothstreaming.manifest.SsManifest;
import androidx.media3.exoplayer.source.chunk.ChunkSource;
import androidx.media3.exoplayer.trackselection.ExoTrackSelection;
import androidx.media3.exoplayer.upstream.CmcdConfiguration;
import androidx.media3.exoplayer.upstream.LoaderErrorThrower;
import androidx.media3.extractor.text.SubtitleParser;

/* JADX INFO: loaded from: classes.dex */
public interface SsChunkSource extends ChunkSource {

    public interface Factory {

        /* JADX INFO: renamed from: androidx.media3.exoplayer.smoothstreaming.SsChunkSource$Factory$-CC, reason: invalid class name */
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

        SsChunkSource createChunkSource(LoaderErrorThrower r1, SsManifest r2, int r3, ExoTrackSelection r4, TransferListener r5, CmcdConfiguration r6);

        Factory experimentalParseSubtitlesDuringExtraction(boolean r1);

        Format getOutputTextFormat(Format r1);

        Factory setSubtitleParserFactory(SubtitleParser.Factory r1);
    }

    void updateManifest(SsManifest r1);

    void updateTrackSelection(ExoTrackSelection r1);
}
