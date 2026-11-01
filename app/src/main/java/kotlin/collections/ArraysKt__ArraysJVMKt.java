package kotlin.collections;

import androidx.exifinterface.media.ExifInterface;
import com.arthenica.ffmpegkit.MediaInformation;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: ArraysJVM.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u00002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u001e\n\u0002\b\u0002\u001a/\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0004\u001a\u00020\u0005H\u0000¢\u0006\u0002\u0010\u0006\u001a\u0018\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u00052\u0006\u0010\u0004\u001a\u00020\u0005H\u0001\u001a#\u0010\n\u001a\u00020\u0005\"\u0004\b\u0000\u0010\u0002*\f\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0018\u00010\u0001H\u0001¢\u0006\u0004\b\u000b\u0010\f\u001a,\u0010\r\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0001\"\u0006\b\u0000\u0010\u0002\u0018\u0001*\f\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0018\u00010\u0001H\u0086\b¢\u0006\u0002\u0010\u000e\u001a\u0015\u0010\u000f\u001a\u00020\u0010*\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0087\b\u001a&\u0010\u0014\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0006\b\u0000\u0010\u0002\u0018\u0001*\b\u0012\u0004\u0012\u0002H\u00020\u0015H\u0086\b¢\u0006\u0002\u0010\u0016¨\u0006\u0017"}, m421d2 = {"arrayOfNulls", "", ExifInterface.GPS_DIRECTION_TRUE, "reference", MediaInformation.KEY_SIZE, "", "([Ljava/lang/Object;I)[Ljava/lang/Object;", "copyOfRangeToIndexCheck", "", "toIndex", "contentDeepHashCodeImpl", "contentDeepHashCode", "([Ljava/lang/Object;)I", "orEmpty", "([Ljava/lang/Object;)[Ljava/lang/Object;", "toString", "", "", "charset", "Ljava/nio/charset/Charset;", "toTypedArray", "", "(Ljava/util/Collection;)[Ljava/lang/Object;", "kotlin-stdlib"}, m422k = 5, m423mv = {1, 9, 0}, m425xi = 49, m426xs = "kotlin/collections/ArraysKt")
class ArraysKt__ArraysJVMKt {
    public ArraysKt__ArraysJVMKt() {
    }

    public static final <T> T[] arrayOfNulls(T[] r2, int r3) {
        Intrinsics.checkNotNullParameter(r2, "reference");
        Object r0 = Array.newInstance(r2.getClass().getComponentType(), r3);
        Intrinsics.checkNotNull(r0, "null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.arrayOfNulls>");
        return (T[]) ((Object[]) r0);
    }

    public static final <T> int contentDeepHashCode(T[] r1) {
        return Arrays.deepHashCode(r1);
    }

    public static final void copyOfRangeToIndexCheck(int r3, int r4) {
        if (r3 > r4) goto L5;
        return;
    L5:
        throw new IndexOutOfBoundsException("toIndex (" + r3 + ") is greater than size (" + r4 + ").");
    }

    public static final /* synthetic */ <T> T[] orEmpty(T[] r4) {
        if (r4 != null) goto L6;
        Intrinsics.reifiedOperationMarker(0, "T?");
        return (T[]) new Object[0];
    L6:
        return r4;
    }

    private static final String toString(byte[] r1, Charset r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "charset");
        return new String(r1, r2);
    }

    public static final /* synthetic */ <T> T[] toTypedArray(Collection<? extends T> r4) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        Intrinsics.reifiedOperationMarker(0, "T?");
        return (T[]) r4.toArray(new Object[0]);
    }
}
