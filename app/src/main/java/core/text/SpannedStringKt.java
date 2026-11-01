package androidx.core.text;

import android.text.Spanned;
import android.text.SpannedString;
import androidx.exifinterface.media.ExifInterface;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: SpannedString.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000 \n\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\r\n\u0000\u001a:\u0010\u0000\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0001\"\n\b\u0000\u0010\u0002\u0018\u0001*\u00020\u0003*\u00020\u00042\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\u0006H\u0086\b¢\u0006\u0002\u0010\b\u001a\r\u0010\t\u001a\u00020\u0004*\u00020\nH\u0086\b¨\u0006\u000b"}, m421d2 = {"getSpans", "", ExifInterface.GPS_DIRECTION_TRUE, "", "Landroid/text/Spanned;", "start", "", "end", "(Landroid/text/Spanned;II)[Ljava/lang/Object;", "toSpanned", "", "core-ktx_release"}, m422k = 2, m423mv = {1, 7, 1}, m425xi = 48)
public final class SpannedStringKt {
    public static final /* synthetic */ <T> T[] getSpans(Spanned r3, int r4, int r5) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.reifiedOperationMarker(4, ExifInterface.GPS_DIRECTION_TRUE);
        Object[] r1 = r3.getSpans(r4, r5, Object.class);
        Intrinsics.checkNotNullExpressionValue(r1, "getSpans(start, end, T::class.java)");
        return (T[]) r1;
    }

    public static /* synthetic */ Object[] getSpans$default(Spanned r1, int r2, int r3, int r4, Object r5) {
        if ((r4 & 1) == 0) goto L6;
        r2 = 0;
    L6:
        if ((r4 & 2) == 0) goto L8;
        r3 = r1.length();
    L8:
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.reifiedOperationMarker(4, ExifInterface.GPS_DIRECTION_TRUE);
        Object[] r6 = r1.getSpans(r2, r3, Object.class);
        Intrinsics.checkNotNullExpressionValue(r6, "getSpans(start, end, T::class.java)");
        return r6;
    }

    public static final Spanned toSpanned(CharSequence r3) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        SpannedString r1 = SpannedString.valueOf(r3);
        Intrinsics.checkNotNullExpressionValue(r1, "valueOf(this)");
        return r1;
    }
}
