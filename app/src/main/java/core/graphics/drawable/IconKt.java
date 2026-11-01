package androidx.core.graphics.drawable;

import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.net.Uri;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Icon.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\u0016\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0012\n\u0000\u001a\r\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u0087\b\u001a\r\u0010\u0003\u001a\u00020\u0001*\u00020\u0002H\u0087\b\u001a\r\u0010\u0003\u001a\u00020\u0001*\u00020\u0004H\u0087\b\u001a\r\u0010\u0003\u001a\u00020\u0001*\u00020\u0005H\u0087\b¨\u0006\u0006"}, m421d2 = {"toAdaptiveIcon", "Landroid/graphics/drawable/Icon;", "Landroid/graphics/Bitmap;", "toIcon", "Landroid/net/Uri;", "", "core-ktx_release"}, m422k = 2, m423mv = {1, 7, 1}, m425xi = 48)
public final class IconKt {
    public static final Icon toAdaptiveIcon(Bitmap r3) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Icon r1 = Icon.createWithAdaptiveBitmap(r3);
        Intrinsics.checkNotNullExpressionValue(r1, "createWithAdaptiveBitmap(this)");
        return r1;
    }

    public static final Icon toIcon(Bitmap r3) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Icon r1 = Icon.createWithBitmap(r3);
        Intrinsics.checkNotNullExpressionValue(r1, "createWithBitmap(this)");
        return r1;
    }

    public static final Icon toIcon(Uri r3) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Icon r1 = Icon.createWithContentUri(r3);
        Intrinsics.checkNotNullExpressionValue(r1, "createWithContentUri(this)");
        return r1;
    }

    public static final Icon toIcon(byte[] r3) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Icon r1 = Icon.createWithData(r3, 0, r3.length);
        Intrinsics.checkNotNullExpressionValue(r1, "createWithData(this, 0, size)");
        return r1;
    }
}
