package kotlin.collections;

import androidx.exifinterface.media.ExifInterface;
import com.arthenica.ffmpegkit.StreamInformation;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;

/* JADX INFO: compiled from: ReversedViews.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\u0018\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010!\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0007\u001a\u001c\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0001\u001a#\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0003\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u0007¢\u0006\u0002\b\u0004\u001a\u001d\u0010\u0005\u001a\u00020\u0006*\u0006\u0012\u0002\b\u00030\u00012\u0006\u0010\u0007\u001a\u00020\u0006H\u0002¢\u0006\u0002\b\b\u001a\u001d\u0010\t\u001a\u00020\u0006*\u0006\u0012\u0002\b\u00030\u00012\u0006\u0010\u0007\u001a\u00020\u0006H\u0002¢\u0006\u0002\b\n\u001a\u001d\u0010\u000b\u001a\u00020\u0006*\u0006\u0012\u0002\b\u00030\u00012\u0006\u0010\u0007\u001a\u00020\u0006H\u0002¢\u0006\u0002\b\f¨\u0006\r"}, m421d2 = {"asReversed", "", ExifInterface.GPS_DIRECTION_TRUE, "", "asReversedMutable", "reverseElementIndex", "", StreamInformation.KEY_INDEX, "reverseElementIndex$CollectionsKt__ReversedViewsKt", "reverseIteratorIndex", "reverseIteratorIndex$CollectionsKt__ReversedViewsKt", "reversePositionIndex", "reversePositionIndex$CollectionsKt__ReversedViewsKt", "kotlin-stdlib"}, m422k = 5, m423mv = {1, 9, 0}, m425xi = 49, m426xs = "kotlin/collections/CollectionsKt")
class CollectionsKt__ReversedViewsKt extends CollectionsKt__MutableCollectionsKt {
    public CollectionsKt__ReversedViewsKt() {
    }

    public static final /* synthetic */ int access$reverseElementIndex(List r1, int r2) {
        return reverseElementIndex$CollectionsKt__ReversedViewsKt(r1, r2);
    }

    public static final /* synthetic */ int access$reverseIteratorIndex(List r1, int r2) {
        return reverseIteratorIndex$CollectionsKt__ReversedViewsKt(r1, r2);
    }

    public static final /* synthetic */ int access$reversePositionIndex(List r1, int r2) {
        return reversePositionIndex$CollectionsKt__ReversedViewsKt(r1, r2);
    }

    public static final <T> List<T> asReversed(List<? extends T> r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return new ReversedListReadOnly(r1);
    }

    public static final <T> List<T> asReversedMutable(List<T> r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return new ReversedList(r1);
    }

    private static final int reverseElementIndex$CollectionsKt__ReversedViewsKt(List<?> r5, int r6) {
        if (new IntRange(0, CollectionsKt.getLastIndex(r5)).contains(r6) == false) goto L7;
        return CollectionsKt.getLastIndex(r5) - r6;
    L7:
        throw new IndexOutOfBoundsException("Element index " + r6 + " must be in range [" + new IntRange(0, CollectionsKt.getLastIndex(r5)) + "].");
    }

    private static final int reverseIteratorIndex$CollectionsKt__ReversedViewsKt(List<?> r1, int r2) {
        return CollectionsKt.getLastIndex(r1) - r2;
    }

    private static final int reversePositionIndex$CollectionsKt__ReversedViewsKt(List<?> r5, int r6) {
        if (new IntRange(0, r5.size()).contains(r6) == false) goto L7;
        return r5.size() - r6;
    L7:
        throw new IndexOutOfBoundsException("Position index " + r6 + " must be in range [" + new IntRange(0, r5.size()) + "].");
    }
}
