package kotlin.p005io;

import java.io.File;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Exceptions.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a$\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00032\b\u0010\u0005\u001a\u0004\u0018\u00010\u0001H\u0002¨\u0006\u0006"}, m421d2 = {"constructMessage", "", "file", "Ljava/io/File;", "other", "reason", "kotlin-stdlib"}, m422k = 2, m423mv = {1, 9, 0}, m425xi = 48)
public final class ExceptionsKt {
    public static final /* synthetic */ String access$constructMessage(File r1, File r2, String r3) {
        return constructMessage(r1, r2, r3);
    }

    private static final String constructMessage(File r3, File r4, String r5) {
        StringBuilder r0 = new StringBuilder(r3.toString());
        if (r4 == null) goto L5;
        r0.append(" -> " + r4);
    L5:
        if (r5 == null) goto L7;
        r0.append(": " + r5);
    L7:
        String r1 = r0.toString();
        Intrinsics.checkNotNullExpressionValue(r1, "toString(...)");
        return r1;
    }
}
