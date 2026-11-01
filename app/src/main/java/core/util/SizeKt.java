package androidx.core.util;

import android.util.Size;
import android.util.SizeF;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Size.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\u001a\n\u0000\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\u0010\u0007\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\r\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u0087\n\u001a\r\u0010\u0000\u001a\u00020\u0003*\u00020\u0004H\u0087\n\u001a\r\u0010\u0000\u001a\u00020\u0003*\u00020\u0005H\u0086\n\u001a\r\u0010\u0006\u001a\u00020\u0001*\u00020\u0002H\u0087\n\u001a\r\u0010\u0006\u001a\u00020\u0003*\u00020\u0004H\u0087\n\u001a\r\u0010\u0006\u001a\u00020\u0003*\u00020\u0005H\u0086\n¨\u0006\u0007"}, m421d2 = {"component1", "", "Landroid/util/Size;", "", "Landroid/util/SizeF;", "Landroidx/core/util/SizeFCompat;", "component2", "core-ktx_release"}, m422k = 2, m423mv = {1, 7, 1}, m425xi = 48)
public final class SizeKt {
    public static final float component1(SizeF r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return r2.getWidth();
    }

    public static final float component1(SizeFCompat r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return r2.getWidth();
    }

    public static final int component1(Size r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return r2.getWidth();
    }

    public static final float component2(SizeF r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return r2.getHeight();
    }

    public static final float component2(SizeFCompat r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return r2.getHeight();
    }

    public static final int component2(Size r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return r2.getHeight();
    }
}
