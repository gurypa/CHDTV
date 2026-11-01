package androidx.media3.exoplayer;

import android.os.Handler;
import androidx.media3.exoplayer.audio.AudioRendererEventListener;
import androidx.media3.exoplayer.metadata.MetadataOutput;
import androidx.media3.exoplayer.text.TextOutput;
import androidx.media3.exoplayer.video.VideoRendererEventListener;

/* JADX INFO: loaded from: classes.dex */
public interface RenderersFactory {
    Renderer[] createRenderers(Handler r1, VideoRendererEventListener r2, AudioRendererEventListener r3, TextOutput r4, MetadataOutput r5);
}
