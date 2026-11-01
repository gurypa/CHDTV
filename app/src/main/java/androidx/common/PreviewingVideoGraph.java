package androidx.media3.common;

import android.content.Context;
import java.util.List;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public interface PreviewingVideoGraph extends VideoGraph {

    public interface Factory {
        PreviewingVideoGraph create(Context r1, ColorInfo r2, DebugViewProvider r3, VideoGraph.Listener r4, Executor r5, List<Effect> r6, long r7) throws VideoFrameProcessingException;
    }

    void renderOutputFrame(long r1);
}
