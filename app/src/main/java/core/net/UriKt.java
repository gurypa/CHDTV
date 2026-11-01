package androidx.core.net;

import android.net.Uri;
import java.io.File;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Uri.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\u001a\n\u0010\u0000\u001a\u00020\u0001*\u00020\u0002\u001a\r\u0010\u0003\u001a\u00020\u0002*\u00020\u0001H\u0086\b\u001a\r\u0010\u0003\u001a\u00020\u0002*\u00020\u0004H\u0086\b¨\u0006\u0005"}, m421d2 = {"toFile", "Ljava/io/File;", "Landroid/net/Uri;", "toUri", "", "core-ktx_release"}, m422k = 2, m423mv = {1, 7, 1}, m425xi = 48)
public final class UriKt {
    public static final File toFile(Uri r3) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        if (Intrinsics.areEqual(r3.getScheme(), "file") == false) goto L11;
        String r0 = r3.getPath();
        if (r0 == null) goto L9;
        return new File(r0);
    L9:
        throw new IllegalArgumentException(("Uri path is null: " + r3).toString());
    L11:
        throw new IllegalArgumentException(("Uri lacks 'file' scheme: " + r3).toString());
    }

    public static final Uri toUri(File r3) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Uri r1 = Uri.fromFile(r3);
        Intrinsics.checkNotNullExpressionValue(r1, "fromFile(this)");
        return r1;
    }

    public static final Uri toUri(String r3) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Uri r1 = Uri.parse(r3);
        Intrinsics.checkNotNullExpressionValue(r1, "parse(this)");
        return r1;
    }
}
