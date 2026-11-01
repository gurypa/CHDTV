package androidx.core.content;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.CancellationSignal;
import android.os.OperationCanceledException;

/* JADX INFO: loaded from: classes.dex */
public final class ContentResolverCompat {

    static class Api16Impl {
        private Api16Impl() {
        }

        static Cursor query(ContentResolver r1, Uri r2, String[] r3, String r4, String[] r5, String r6, CancellationSignal r7) {
            return r1.query(r2, r3, r4, r5, r6, r7);
        }
    }

    private ContentResolverCompat() {
    }

    public static Cursor query(ContentResolver r8, Uri r9, String[] r10, String r11, String[] r12, String r13, androidx.core.os.CancellationSignal r14) {
        if (r14 != null) goto L23;
        Object r0 = null;
    L25:
        return Api16Impl.query(r8, r9, r10, r11, r12, r13, (CancellationSignal) r0);
    L12:
        e = e;
    L17:
        if ((e instanceof OperationCanceledException) == true) goto L19;
        throw e;
    L19:
        throw new androidx.core.os.OperationCanceledException();
    L14:
        e = e;
        goto L17
    L23:
        r0 = r14.getCancellationSignalObject();     // Catch: Exception -> L5
    L5:
        e = e;
        goto L17
    }
}
