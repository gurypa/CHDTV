package androidx.media3.extractor.mp4;

import androidx.media3.extractor.SniffFailure;

/* JADX INFO: loaded from: classes.dex */
public final class IncorrectFragmentationSniffFailure implements SniffFailure {
    public static final IncorrectFragmentationSniffFailure FILE_FRAGMENTED = null;
    public static final IncorrectFragmentationSniffFailure FILE_NOT_FRAGMENTED = null;
    public final boolean fileIsFragmented;

    static {
        FILE_FRAGMENTED = new IncorrectFragmentationSniffFailure(true);
        FILE_NOT_FRAGMENTED = new IncorrectFragmentationSniffFailure(false);
    }

    private IncorrectFragmentationSniffFailure(boolean r1) {
        this.fileIsFragmented = r1;
    }
}
