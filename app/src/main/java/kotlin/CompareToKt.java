package kotlin;

import androidx.exifinterface.media.ExifInterface;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: compareTo.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000f\n\u0002\b\u0003\u001a&\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\u0004\u001a\u0002H\u0002H\u0087\f¢\u0006\u0002\u0010\u0005¨\u0006\u0006"}, m421d2 = {"compareTo", "", ExifInterface.GPS_DIRECTION_TRUE, "", "other", "(Ljava/lang/Comparable;Ljava/lang/Object;)I", "kotlin-stdlib"}, m422k = 2, m423mv = {1, 9, 0}, m425xi = 48)
public final class CompareToKt {
    /* JADX WARN: Multi-variable type inference failed */
    private static final <T> int compareTo(Comparable<? super T> r1, T r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return r1.compareTo(r2);
    }
}
