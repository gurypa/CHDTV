package androidx.core.content;

import android.content.SharedPreferences;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: SharedPreferences.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000 \n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u001a3\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u00042\u0017\u0010\u0005\u001a\u0013\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00010\u0006¢\u0006\u0002\b\bH\u0087\bø\u0001\u0000\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\t"}, m421d2 = {"edit", "", "Landroid/content/SharedPreferences;", "commit", "", "action", "Lkotlin/Function1;", "Landroid/content/SharedPreferences$Editor;", "Lkotlin/ExtensionFunctionType;", "core-ktx_release"}, m422k = 2, m423mv = {1, 7, 1}, m425xi = 48)
public final class SharedPreferencesKt {
    public static final void edit(SharedPreferences r3, boolean r4, Function1<? super SharedPreferences.Editor, Unit> r5) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r5, "action");
        SharedPreferences.Editor r1 = r3.edit();
        Intrinsics.checkNotNullExpressionValue(r1, "editor");
        r5.invoke(r1);
        if (r4 == false) goto L5;
        r1.commit();
        return;
    L5:
        r1.apply();
    }

    public static /* synthetic */ void edit$default(SharedPreferences r1, boolean r2, Function1 r3, int r4, Object r5) {
        if ((r4 & 1) == 0) goto L5;
        r2 = false;
    L5:
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r3, "action");
        SharedPreferences.Editor r6 = r1.edit();
        Intrinsics.checkNotNullExpressionValue(r6, "editor");
        r3.invoke(r6);
        if (r2 == false) goto L8;
        r6.commit();
        return;
    L8:
        r6.apply();
    }
}
