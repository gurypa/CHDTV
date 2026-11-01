package kotlin.text;

import java.util.Locale;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: _OneToManyTitlecaseMappings.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u000e\n\u0002\u0010\f\n\u0000\u001a\f\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u0000¨\u0006\u0003"}, m421d2 = {"titlecaseImpl", "", "", "kotlin-stdlib"}, m422k = 2, m423mv = {1, 9, 0}, m425xi = 48)
public final class _OneToManyTitlecaseMappingsKt {
    public static final String titlecaseImpl(char r5) {
        String r0 = String.valueOf(r5);
        Intrinsics.checkNotNull(r0, "null cannot be cast to non-null type java.lang.String");
        String r1 = r0.toUpperCase(Locale.ROOT);
        Intrinsics.checkNotNullExpressionValue(r1, "toUpperCase(...)");
        if (r1.length() <= 1) goto L10;
        if (r5 == 329) goto L11;
        char r2 = r1.charAt(0);
        Intrinsics.checkNotNull(r1, "null cannot be cast to non-null type java.lang.String");
        String r3 = r1.substring(1);
        Intrinsics.checkNotNullExpressionValue(r3, "substring(...)");
        Intrinsics.checkNotNull(r3, "null cannot be cast to non-null type java.lang.String");
        String r4 = r3.toLowerCase(Locale.ROOT);
        Intrinsics.checkNotNullExpressionValue(r4, "toLowerCase(...)");
        return r2 + r4;
    L11:
        return r1;
    L10:
        return String.valueOf(Character.toTitleCase(r5));
    }
}
