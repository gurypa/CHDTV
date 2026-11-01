package androidx.core.database;

import android.database.Cursor;
import com.arthenica.ffmpegkit.StreamInformation;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Cursor.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u00008\n\u0000\n\u0002\u0010\u0012\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\n\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\u001a\u0017\u0010\u0000\u001a\u0004\u0018\u00010\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u0086\b\u001a\u001c\u0010\u0005\u001a\u0004\u0018\u00010\u0006*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u0086\b¢\u0006\u0002\u0010\u0007\u001a\u001c\u0010\b\u001a\u0004\u0018\u00010\t*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u0086\b¢\u0006\u0002\u0010\n\u001a\u001c\u0010\u000b\u001a\u0004\u0018\u00010\u0004*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u0086\b¢\u0006\u0002\u0010\f\u001a\u001c\u0010\r\u001a\u0004\u0018\u00010\u000e*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u0086\b¢\u0006\u0002\u0010\u000f\u001a\u001c\u0010\u0010\u001a\u0004\u0018\u00010\u0011*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u0086\b¢\u0006\u0002\u0010\u0012\u001a\u0017\u0010\u0013\u001a\u0004\u0018\u00010\u0014*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u0086\b¨\u0006\u0015"}, m421d2 = {"getBlobOrNull", "", "Landroid/database/Cursor;", StreamInformation.KEY_INDEX, "", "getDoubleOrNull", "", "(Landroid/database/Cursor;I)Ljava/lang/Double;", "getFloatOrNull", "", "(Landroid/database/Cursor;I)Ljava/lang/Float;", "getIntOrNull", "(Landroid/database/Cursor;I)Ljava/lang/Integer;", "getLongOrNull", "", "(Landroid/database/Cursor;I)Ljava/lang/Long;", "getShortOrNull", "", "(Landroid/database/Cursor;I)Ljava/lang/Short;", "getStringOrNull", "", "core-ktx_release"}, m422k = 2, m423mv = {1, 7, 1}, m425xi = 48)
public final class CursorKt {
    public static final byte[] getBlobOrNull(Cursor r2, int r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        if (r2.isNull(r3) == false) goto L6;
        return null;
    L6:
        return r2.getBlob(r3);
    }

    public static final Double getDoubleOrNull(Cursor r3, int r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        if (r3.isNull(r4) == false) goto L6;
        return null;
    L6:
        return Double.valueOf(r3.getDouble(r4));
    }

    public static final Float getFloatOrNull(Cursor r2, int r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        if (r2.isNull(r3) == false) goto L6;
        return null;
    L6:
        return Float.valueOf(r2.getFloat(r3));
    }

    public static final Integer getIntOrNull(Cursor r2, int r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        if (r2.isNull(r3) == false) goto L6;
        return null;
    L6:
        return Integer.valueOf(r2.getInt(r3));
    }

    public static final Long getLongOrNull(Cursor r3, int r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        if (r3.isNull(r4) == false) goto L6;
        return null;
    L6:
        return Long.valueOf(r3.getLong(r4));
    }

    public static final Short getShortOrNull(Cursor r2, int r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        if (r2.isNull(r3) == false) goto L6;
        return null;
    L6:
        return Short.valueOf(r2.getShort(r3));
    }

    public static final String getStringOrNull(Cursor r2, int r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        if (r2.isNull(r3) == false) goto L6;
        return null;
    L6:
        return r2.getString(r3);
    }
}
