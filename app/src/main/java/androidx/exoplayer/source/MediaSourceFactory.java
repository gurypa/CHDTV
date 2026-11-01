package androidx.media3.exoplayer.source;

/* JADX INFO: loaded from: classes.dex */
@Deprecated
public interface MediaSourceFactory extends MediaSource.Factory {
    public static final MediaSourceFactory UNSUPPORTED = null;

    static {
        UNSUPPORTED = new C03431();
    }
}
