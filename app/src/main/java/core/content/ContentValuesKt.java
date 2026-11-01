package androidx.core.content;

import android.content.ContentValues;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Typography;

/* JADX INFO: compiled from: ContentValues.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\u001c\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\u0000\n\u0002\b\u0002\u001a;\u0010\u0000\u001a\u00020\u00012.\u0010\u0002\u001a\u0018\u0012\u0014\b\u0001\u0012\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00060\u00040\u0003\"\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00060\u0004¢\u0006\u0002\u0010\u0007¨\u0006\b"}, m421d2 = {"contentValuesOf", "Landroid/content/ContentValues;", "pairs", "", "Lkotlin/Pair;", "", "", "([Lkotlin/Pair;)Landroid/content/ContentValues;", "core-ktx_release"}, m422k = 2, m423mv = {1, 7, 1}, m425xi = 48)
public final class ContentValuesKt {
    public static final ContentValues contentValuesOf(Pair<String, ? extends Object>... r8) {
        Intrinsics.checkNotNullParameter(r8, "pairs");
        ContentValues r0 = new ContentValues(r8.length);
        int r3 = r8.length;
        int r4 = 0;
    L3:
        if (r4 >= r3) goto L38;
        Pair<String, ? extends Object> r5 = r8[r4];
        String r6 = r5.component1();
        Object r7 = r5.component2();
        if (r7 != null) goto L8;
        r0.putNull(r6);
    L34:
        r4 = r4 + 1;
        goto L3
    L8:
        if ((r7 instanceof String) == false) goto L11;
        r0.put(r6, (String) r7);
        goto L34
    L11:
        if ((r7 instanceof Integer) == false) goto L14;
        r0.put(r6, (Integer) r7);
        goto L34
    L14:
        if ((r7 instanceof Long) == false) goto L17;
        r0.put(r6, (Long) r7);
        goto L34
    L17:
        if ((r7 instanceof Boolean) == false) goto L20;
        r0.put(r6, (Boolean) r7);
        goto L34
    L20:
        if ((r7 instanceof Float) == false) goto L23;
        r0.put(r6, (Float) r7);
        goto L34
    L23:
        if ((r7 instanceof Double) == false) goto L26;
        r0.put(r6, (Double) r7);
        goto L34
    L26:
        if ((r7 instanceof byte[]) == false) goto L29;
        r0.put(r6, (byte[]) r7);
        goto L34
    L29:
        if ((r7 instanceof Byte) == false) goto L32;
        r0.put(r6, (Byte) r7);
        goto L34
    L32:
        if ((r7 instanceof Short) == false) goto L36;
        r0.put(r6, (Short) r7);
        goto L34
    L36:
        throw new IllegalArgumentException("Illegal value type " + r7.getClass().getCanonicalName() + " for key \"" + r6 + Typography.quote);
    L38:
        return r0;
    }
}
