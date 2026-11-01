package androidx.media3.exoplayer.mediacodec;

import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface MediaCodecSelector {
    public static final MediaCodecSelector DEFAULT = null;

    static {
        DEFAULT = new MediaCodecSelector$$ExternalSyntheticLambda0();
    }

    List<MediaCodecInfo> getDecoderInfos(String r1, boolean r2, boolean r3) throws MediaCodecUtil.DecoderQueryException;
}
