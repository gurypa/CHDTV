package kotlin.collections.builders;

import androidx.exifinterface.media.ExifInterface;
import com.arthenica.ffmpegkit.MediaInformation;
import com.arthenica.ffmpegkit.StreamInformation;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: ListBuilder.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u00008\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u001e\n\u0002\b\u0002\u001a!\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u0000¢\u0006\u0002\u0010\u0005\u001a+\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0001\"\u0004\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070\u00012\u0006\u0010\b\u001a\u00020\u0004H\u0000¢\u0006\u0002\u0010\t\u001a%\u0010\n\u001a\u00020\u000b\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\f\u001a\u00020\u0004H\u0000¢\u0006\u0002\u0010\r\u001a-\u0010\u000e\u001a\u00020\u000b\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u000f\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u00020\u0004H\u0000¢\u0006\u0002\u0010\u0011\u001a9\u0010\u0012\u001a\u00020\u0013\"\u0004\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070\u00012\u0006\u0010\u0014\u001a\u00020\u00042\u0006\u0010\u0015\u001a\u00020\u00042\n\u0010\u0016\u001a\u0006\u0012\u0002\b\u00030\u0017H\u0002¢\u0006\u0002\u0010\u0018\u001a-\u0010\u0019\u001a\u00020\u0004\"\u0004\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070\u00012\u0006\u0010\u0014\u001a\u00020\u00042\u0006\u0010\u0015\u001a\u00020\u0004H\u0002¢\u0006\u0002\u0010\u001a\u001a=\u0010\u001b\u001a\u00020\u001c\"\u0004\b\u0000\u0010\u0007*\n\u0012\u0006\b\u0001\u0012\u0002H\u00070\u00012\u0006\u0010\u0014\u001a\u00020\u00042\u0006\u0010\u0015\u001a\u00020\u00042\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u0002H\u00070\u001eH\u0002¢\u0006\u0002\u0010\u001f¨\u0006 "}, m421d2 = {"arrayOfUninitializedElements", "", ExifInterface.LONGITUDE_EAST, MediaInformation.KEY_SIZE, "", "(I)[Ljava/lang/Object;", "copyOfUninitializedElements", ExifInterface.GPS_DIRECTION_TRUE, "newSize", "([Ljava/lang/Object;I)[Ljava/lang/Object;", "resetAt", "", StreamInformation.KEY_INDEX, "([Ljava/lang/Object;I)V", "resetRange", "fromIndex", "toIndex", "([Ljava/lang/Object;II)V", "subarrayContentEquals", "", "offset", "length", "other", "", "([Ljava/lang/Object;IILjava/util/List;)Z", "subarrayContentHashCode", "([Ljava/lang/Object;II)I", "subarrayContentToString", "", "thisCollection", "", "([Ljava/lang/Object;IILjava/util/Collection;)Ljava/lang/String;", "kotlin-stdlib"}, m422k = 2, m423mv = {1, 9, 0}, m425xi = 48)
public final class ListBuilderKt {
    public static final /* synthetic */ boolean access$subarrayContentEquals(Object[] r1, int r2, int r3, List r4) {
        return subarrayContentEquals(r1, r2, r3, r4);
    }

    public static final /* synthetic */ int access$subarrayContentHashCode(Object[] r1, int r2, int r3) {
        return subarrayContentHashCode(r1, r2, r3);
    }

    public static final /* synthetic */ String access$subarrayContentToString(Object[] r1, int r2, int r3, Collection r4) {
        return subarrayContentToString(r1, r2, r3, r4);
    }

    public static final <E> E[] arrayOfUninitializedElements(int r2) {
        if (r2 < 0) goto L4;
        boolean r0 = true;
    L5:
        if (r0 == false) goto L9;
        return (E[]) new Object[r2];
    L9:
        throw new IllegalArgumentException("capacity must be non-negative.".toString());
    L4:
        r0 = false;
        goto L5
    }

    public static final <T> T[] copyOfUninitializedElements(T[] r2, int r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        T[] r0 = (T[]) Arrays.copyOf(r2, r3);
        Intrinsics.checkNotNullExpressionValue(r0, "copyOf(...)");
        return r0;
    }

    public static final <E> void resetAt(E[] r1, int r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        r1[r2] = null;
    }

    public static final <E> void resetRange(E[] r1, int r2, int r3) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        int r0 = r2;
    L3:
        if (r0 >= r3) goto L5;
        resetAt(r1, r0);
        r0 = r0 + 1;
        goto L3
    }

    private static final <T> boolean subarrayContentEquals(T[] r4, int r5, int r6, List<?> r7) {
        if (r6 == r7.size()) goto L5;
        return false;
    L5:
        int r0 = 0;
    L6:
        if (r0 >= r6) goto L11;
        if (Intrinsics.areEqual(r4[r5 + r0], r7.get(r0)) == false) goto L9;
        r0 = r0 + 1;
        goto L6
    L9:
        return false;
    L11:
        return true;
    }

    private static final <T> int subarrayContentHashCode(T[] r5, int r6, int r7) {
        int r0 = 1;
        int r1 = 0;
    L3:
        if (r1 >= r7) goto L9;
        T r2 = r5[r6 + r1];
        int r3 = r0 * 31;
        if (r2 == null) goto L7;
        int r4 = r2.hashCode();
    L8:
        r0 = r3 + r4;
        r1 = r1 + 1;
        goto L3
    L7:
        r4 = 0;
        goto L8
    L9:
        return r0;
    }

    private static final <T> String subarrayContentToString(T[] r4, int r5, int r6, Collection<? extends T> r7) {
        StringBuilder r0 = new StringBuilder((r6 * 3) + 2);
        r0.append("[");
        int r1 = 0;
    L3:
        if (r1 >= r6) goto L11;
        if (r1 <= 0) goto L6;
        r0.append(", ");
    L6:
        T r2 = r4[r5 + r1];
        if (r2 != r7) goto L9;
        r0.append("(this Collection)");
    L10:
        r1 = r1 + 1;
        goto L3
    L9:
        r0.append(r2);
        goto L10
    L11:
        r0.append("]");
        String r3 = r0.toString();
        Intrinsics.checkNotNullExpressionValue(r3, "toString(...)");
        return r3;
    }
}
