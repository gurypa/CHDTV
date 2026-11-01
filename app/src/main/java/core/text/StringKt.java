package androidx.core.text;

import android.text.TextUtils;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: String.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u000e\n\u0000\u001a\r\u0010\u0000\u001a\u00020\u0001*\u00020\u0001H\u0086\b¨\u0006\u0002"}, m421d2 = {"htmlEncode", "", "core-ktx_release"}, m422k = 2, m423mv = {1, 7, 1}, m425xi = 48)
public final class StringKt {
    public static final String htmlEncode(String r3) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        String r1 = TextUtils.htmlEncode(r3);
        Intrinsics.checkNotNullExpressionValue(r1, "htmlEncode(this)");
        return r1;
    }
}
