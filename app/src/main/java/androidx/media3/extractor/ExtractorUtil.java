package androidx.media3.extractor;

import androidx.media3.common.ParserException;
import java.io.EOFException;
import java.io.IOException;
import org.checkerframework.dataflow.qual.Pure;

/* JADX INFO: loaded from: classes.dex */
public final class ExtractorUtil {
    private ExtractorUtil() {
    }

    @Pure
    public static void checkContainerInput(boolean r1, String r2) throws ParserException {
        if (r1 == false) goto L5;
        return;
    L5:
        throw ParserException.createForMalformedContainer(r2, null);
    }

    public static boolean peekFullyQuietly(ExtractorInput r2, byte[] r3, int r4, int r5, boolean r6) throws IOException {
        return r2.peekFully(r3, r4, r5, r6);
    L4:
        e = move-exception;
        if (r6 == false) goto L8;
        return false;
    L8:
        throw e;
    }

    public static int peekToLength(ExtractorInput r3, byte[] r4, int r5, int r6) throws IOException {
        int r0 = 0;
    L3:
        if (r0 >= r6) goto L8;
        int r1 = r3.peek(r4, r5 + r0, r6 - r0);
        if (r1 == (-1)) goto L8;
        r0 = r0 + r1;
    L8:
        return r0;
    }

    public static boolean readFullyQuietly(ExtractorInput r2, byte[] r3, int r4, int r5) throws IOException {
        r2.readFully(r3, r4, r5);     // Catch: EOFException -> L5
        return true;
    L6:
        return false;
    }

    public static boolean skipFullyQuietly(ExtractorInput r2, int r3) throws IOException {
        r2.skipFully(r3);     // Catch: EOFException -> L5
        return true;
    L6:
        return false;
    }
}
