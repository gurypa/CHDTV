package androidx.media3.exoplayer.upstream;

import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public interface LoaderErrorThrower {

    public static final class Placeholder implements LoaderErrorThrower {
        public Placeholder() {
        }

        @Override // androidx.media3.exoplayer.upstream.LoaderErrorThrower
        public void maybeThrowError() {
        }

        @Override // androidx.media3.exoplayer.upstream.LoaderErrorThrower
        public void maybeThrowError(int r1) {
        }
    }

    void maybeThrowError() throws IOException;

    void maybeThrowError(int r1) throws IOException;
}
