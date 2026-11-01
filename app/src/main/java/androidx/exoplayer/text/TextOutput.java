package androidx.media3.exoplayer.text;

import androidx.media3.common.text.Cue;
import androidx.media3.common.text.CueGroup;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface TextOutput {

    /* JADX INFO: renamed from: androidx.media3.exoplayer.text.TextOutput$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        @Deprecated
        public static void $default$onCues(TextOutput r0, List r1) {
        }
    }

    void onCues(CueGroup r1);

    @Deprecated
    void onCues(List<Cue> r1);
}
