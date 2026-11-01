package com.google.common.p003io;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class Resources {

    private static final class UrlByteSource extends ByteSource {
        private final URL url;

        private UrlByteSource(URL r2) {
            this.url = (URL) Preconditions.checkNotNull(r2);
        }

        /* synthetic */ UrlByteSource(URL r1, C08551 r2) {
            this(r1);
        }

        @Override // com.google.common.p003io.ByteSource
        public InputStream openStream() throws IOException {
            return this.url.openStream();
        }

        public String toString() {
            return "Resources.asByteSource(" + this.url + ")";
        }
    }

    private Resources() {
    }

    public static ByteSource asByteSource(URL r2) {
        return new UrlByteSource(r2, null);
    }

    public static CharSource asCharSource(URL r1, Charset r2) {
        return asByteSource(r1).asCharSource(r2);
    }

    public static void copy(URL r1, OutputStream r2) throws IOException {
        asByteSource(r1).copyTo(r2);
    }

    public static URL getResource(Class<?> r4, String r5) {
        URL r0 = r4.getResource(r5);
        if (r0 == null) goto L5;
        boolean r1 = true;
    L6:
        Preconditions.checkArgument(r1, "resource %s relative to %s not found.", r5, r4.getName());
        return r0;
    L5:
        r1 = false;
        goto L6
    }

    public static URL getResource(String r4) {
        URL r1 = ((ClassLoader) MoreObjects.firstNonNull(Thread.currentThread().getContextClassLoader(), Resources.class.getClassLoader())).getResource(r4);
        if (r1 == null) goto L5;
        boolean r2 = true;
    L6:
        Preconditions.checkArgument(r2, "resource %s not found.", r4);
        return r1;
    L5:
        r2 = false;
        goto L6
    }

    @ParametricNullness
    public static <T> T readLines(URL r1, Charset r2, LineProcessor<T> r3) throws IOException {
        return (T) asCharSource(r1, r2).readLines(r3);
    }

    public static List<String> readLines(URL r1, Charset r2) throws IOException {
        return (List) readLines(r1, r2, new C08551());
    }

    public static byte[] toByteArray(URL r1) throws IOException {
        return asByteSource(r1).read();
    }

    public static String toString(URL r1, Charset r2) throws IOException {
        return asCharSource(r1, r2).read();
    }
}
