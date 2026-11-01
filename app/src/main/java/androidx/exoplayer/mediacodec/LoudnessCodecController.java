package androidx.media3.exoplayer.mediacodec;

import android.media.MediaCodec;
import android.os.Bundle;
import androidx.media3.common.util.Assertions;
import com.google.common.util.concurrent.MoreExecutors;
import java.util.HashSet;
import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
public final class LoudnessCodecController {
    private android.media.LoudnessCodecController loudnessCodecController;
    private final HashSet<MediaCodec> mediaCodecs;
    private final LoudnessParameterUpdateListener updateListener;

    public interface LoudnessParameterUpdateListener {
        public static final LoudnessParameterUpdateListener DEFAULT = null;

        /* JADX INFO: renamed from: androidx.media3.exoplayer.mediacodec.LoudnessCodecController$LoudnessParameterUpdateListener$-CC, reason: invalid class name */
        public final /* synthetic */ class CC {
            static {
                LoudnessParameterUpdateListener r0 = LoudnessParameterUpdateListener.DEFAULT;
            }

            public static /* synthetic */ Bundle lambda$static$0(Bundle r0) {
                return r0;
            }
        }

        static {
            DEFAULT = new C0308xf9c82391();
        }

        Bundle onLoudnessParameterUpdate(Bundle r1);
    }

    public LoudnessCodecController() {
        this(LoudnessParameterUpdateListener.DEFAULT);
    }

    public LoudnessCodecController(LoudnessParameterUpdateListener r2) {
        this.mediaCodecs = new HashSet();
        this.updateListener = r2;
    }

    static /* synthetic */ LoudnessParameterUpdateListener access$000(LoudnessCodecController r1) {
        return r1.updateListener;
    }

    public void addMediaCodec(MediaCodec r2) {
        if (this.loudnessCodecController != null) goto L5;
    L7:
        Assertions.checkState(this.mediaCodecs.add(r2));
        return;
    L5:
        if (this.loudnessCodecController.addMediaCodec(r2) == true) goto L7;
    }

    public void release() {
        this.mediaCodecs.clear();
        if (this.loudnessCodecController == null) goto L6;
        this.loudnessCodecController.close();
        return;
    }

    public void removeMediaCodec(MediaCodec r3) {
        if (this.mediaCodecs.remove(r3) == true) goto L5;
        return;
    L5:
        if (this.loudnessCodecController == null) goto L9;
        this.loudnessCodecController.removeMediaCodec(r3);
        return;
    }

    public void setAudioSessionId(int r4) {
        if (this.loudnessCodecController == null) goto L5;
        this.loudnessCodecController.close();
        this.loudnessCodecController = null;
    L5:
        android.media.LoudnessCodecController r0 = android.media.LoudnessCodecController.create(r4, MoreExecutors.directExecutor(), new C03071(this));
        this.loudnessCodecController = r0;
        Iterator<MediaCodec> r1 = this.mediaCodecs.iterator();
    L7:
        if (r1.hasNext() == false) goto L12;
        if (r0.addMediaCodec(r1.next()) == true) goto L7;
        r1.remove();
        goto L7
    }
}
