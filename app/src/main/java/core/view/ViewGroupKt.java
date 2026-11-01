package androidx.core.view;

import android.view.View;
import android.view.ViewGroup;
import androidx.media3.extractor.text.ttml.TtmlNode;
import com.arthenica.ffmpegkit.MediaInformation;
import com.arthenica.ffmpegkit.StreamInformation;
import java.util.Iterator;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMutableIterator;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;

/* JADX INFO: compiled from: ViewGroup.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000T\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010)\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\t\u001a\u0015\u0010\u0010\u001a\u00020\u0011*\u00020\u00032\u0006\u0010\u0012\u001a\u00020\u0002H\u0086\n\u001a3\u0010\u0013\u001a\u00020\u0014*\u00020\u00032!\u0010\u0015\u001a\u001d\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u0017\u0012\b\b\u0018\u0012\u0004\b\b(\u0012\u0012\u0004\u0012\u00020\u00140\u0016H\u0086\bø\u0001\u0000\u001aH\u0010\u0019\u001a\u00020\u0014*\u00020\u000326\u0010\u0015\u001a2\u0012\u0013\u0012\u00110\r¢\u0006\f\b\u0017\u0012\b\b\u0018\u0012\u0004\b\b(\u001b\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u0017\u0012\b\b\u0018\u0012\u0004\b\b(\u0012\u0012\u0004\u0012\u00020\u00140\u001aH\u0086\bø\u0001\u0000\u001a\u0015\u0010\u001c\u001a\u00020\u0002*\u00020\u00032\u0006\u0010\u001b\u001a\u00020\rH\u0086\u0002\u001a\r\u0010\u001d\u001a\u00020\u0011*\u00020\u0003H\u0086\b\u001a\r\u0010\u001e\u001a\u00020\u0011*\u00020\u0003H\u0086\b\u001a\u0013\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u00020 *\u00020\u0003H\u0086\u0002\u001a\u0015\u0010!\u001a\u00020\u0014*\u00020\u00032\u0006\u0010\u0012\u001a\u00020\u0002H\u0086\n\u001a\u0015\u0010\"\u001a\u00020\u0014*\u00020\u00032\u0006\u0010\u0012\u001a\u00020\u0002H\u0086\n\u001a\u0017\u0010#\u001a\u00020\u0014*\u00020$2\b\b\u0001\u0010\f\u001a\u00020\rH\u0086\b\u001a5\u0010%\u001a\u00020\u0014*\u00020$2\b\b\u0003\u0010&\u001a\u00020\r2\b\b\u0003\u0010'\u001a\u00020\r2\b\b\u0003\u0010(\u001a\u00020\r2\b\b\u0003\u0010)\u001a\u00020\rH\u0086\b\u001a5\u0010*\u001a\u00020\u0014*\u00020$2\b\b\u0003\u0010+\u001a\u00020\r2\b\b\u0003\u0010'\u001a\u00020\r2\b\b\u0003\u0010,\u001a\u00020\r2\b\b\u0003\u0010)\u001a\u00020\rH\u0087\b\"\u001b\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001*\u00020\u00038F¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005\"\u001b\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001*\u00020\u00038F¢\u0006\u0006\u001a\u0004\b\u0007\u0010\u0005\"\u0016\u0010\b\u001a\u00020\t*\u00020\u00038Æ\u0002¢\u0006\u0006\u001a\u0004\b\n\u0010\u000b\"\u0016\u0010\f\u001a\u00020\r*\u00020\u00038Æ\u0002¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u000f\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006-"}, m421d2 = {"children", "Lkotlin/sequences/Sequence;", "Landroid/view/View;", "Landroid/view/ViewGroup;", "getChildren", "(Landroid/view/ViewGroup;)Lkotlin/sequences/Sequence;", "descendants", "getDescendants", "indices", "Lkotlin/ranges/IntRange;", "getIndices", "(Landroid/view/ViewGroup;)Lkotlin/ranges/IntRange;", MediaInformation.KEY_SIZE, "", "getSize", "(Landroid/view/ViewGroup;)I", "contains", "", "view", "forEach", "", "action", "Lkotlin/Function1;", "Lkotlin/ParameterName;", "name", "forEachIndexed", "Lkotlin/Function2;", StreamInformation.KEY_INDEX, "get", "isEmpty", "isNotEmpty", "iterator", "", "minusAssign", "plusAssign", "setMargins", "Landroid/view/ViewGroup$MarginLayoutParams;", "updateMargins", TtmlNode.LEFT, "top", TtmlNode.RIGHT, "bottom", "updateMarginsRelative", "start", "end", "core-ktx_release"}, m422k = 2, m423mv = {1, 7, 1}, m425xi = 48)
public final class ViewGroupKt {

    /* JADX INFO: renamed from: androidx.core.view.ViewGroupKt$iterator$1 */
    /* JADX INFO: compiled from: ViewGroup.kt */
    @Metadata(m420d1 = {"\u0000#\n\u0000\n\u0002\u0010)\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001J\t\u0010\u0005\u001a\u00020\u0006H\u0096\u0002J\t\u0010\u0007\u001a\u00020\u0002H\u0096\u0002J\b\u0010\b\u001a\u00020\tH\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\n"}, m421d2 = {"androidx/core/view/ViewGroupKt$iterator$1", "", "Landroid/view/View;", StreamInformation.KEY_INDEX, "", "hasNext", "", "next", "remove", "", "core-ktx_release"}, m422k = 1, m423mv = {1, 7, 1}, m425xi = 48)
    public static final class C01191 implements Iterator<View>, KMutableIterator {
        final /* synthetic */ ViewGroup $this_iterator;
        private int index;

        C01191(ViewGroup r1) {
            this.$this_iterator = r1;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.index >= this.$this_iterator.getChildCount()) goto L5;
            return true;
        L5:
            return false;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public View next() {
            ViewGroup r0 = this.$this_iterator;
            int r1 = this.index;
            this.index = r1 + 1;
            View r2 = r0.getChildAt(r1);
            if (r2 == null) goto L6;
            return r2;
        L6:
            throw new IndexOutOfBoundsException();
        }

        @Override // java.util.Iterator
        public /* bridge */ /* synthetic */ View next() {
            return next();
        }

        @Override // java.util.Iterator
        public void remove() {
            ViewGroup r0 = this.$this_iterator;
            this.index--;
            r0.removeViewAt(this.index);
        }
    }

    public static final boolean contains(ViewGroup r3, View r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r4, "view");
        if (r3.indexOfChild(r4) == (-1)) goto L5;
        return true;
    L5:
        return false;
    }

    public static final void forEach(ViewGroup r5, Function1<? super View, Unit> r6) {
        Intrinsics.checkNotNullParameter(r5, "<this>");
        Intrinsics.checkNotNullParameter(r6, "action");
        int r1 = 0;
        int r2 = r5.getChildCount();
    L3:
        if (r1 >= r2) goto L5;
        View r3 = r5.getChildAt(r1);
        Intrinsics.checkNotNullExpressionValue(r3, "getChildAt(index)");
        r6.invoke(r3);
        r1 = r1 + 1;
        goto L3
    }

    public static final void forEachIndexed(ViewGroup r6, Function2<? super Integer, ? super View, Unit> r7) {
        Intrinsics.checkNotNullParameter(r6, "<this>");
        Intrinsics.checkNotNullParameter(r7, "action");
        int r1 = 0;
        int r2 = r6.getChildCount();
    L3:
        if (r1 >= r2) goto L5;
        Integer r3 = Integer.valueOf(r1);
        View r4 = r6.getChildAt(r1);
        Intrinsics.checkNotNullExpressionValue(r4, "getChildAt(index)");
        r7.invoke(r3, r4);
        r1 = r1 + 1;
        goto L3
    }

    public static final View get(ViewGroup r3, int r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        View r0 = r3.getChildAt(r4);
        if (r0 == null) goto L6;
        return r0;
    L6:
        throw new IndexOutOfBoundsException("Index: " + r4 + ", Size: " + r3.getChildCount());
    }

    public static final Sequence<View> getChildren(final ViewGroup r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return new ViewGroupKt$children$1(r1);
    }

    public static final Sequence<View> getDescendants(ViewGroup r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return SequencesKt.sequence(new ViewGroupKt$descendants$1(r2, null));
    }

    public static final IntRange getIndices(ViewGroup r3) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        return RangesKt.until(0, r3.getChildCount());
    }

    public static final int getSize(ViewGroup r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return r2.getChildCount();
    }

    public static final boolean isEmpty(ViewGroup r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        if (r2.getChildCount() != 0) goto L5;
        return true;
    L5:
        return false;
    }

    public static final boolean isNotEmpty(ViewGroup r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        if (r2.getChildCount() == 0) goto L5;
        return true;
    L5:
        return false;
    }

    public static final Iterator<View> iterator(ViewGroup r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return new C01191(r1);
    }

    public static final void minusAssign(ViewGroup r1, View r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "view");
        r1.removeView(r2);
    }

    public static final void plusAssign(ViewGroup r1, View r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "view");
        r1.addView(r2);
    }

    public static final void setMargins(ViewGroup.MarginLayoutParams r1, int r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        r1.setMargins(r2, r2, r2, r2);
    }

    public static final void updateMargins(ViewGroup.MarginLayoutParams r1, int r2, int r3, int r4, int r5) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        r1.setMargins(r2, r3, r4, r5);
    }

    public static /* synthetic */ void updateMargins$default(ViewGroup.MarginLayoutParams r0, int r1, int r2, int r3, int r4, int r5, Object r6) {
        if ((r5 & 1) == 0) goto L6;
        r1 = r0.leftMargin;
    L6:
        if ((r5 & 2) == 0) goto L9;
        r2 = r0.topMargin;
    L9:
        if ((r5 & 4) == 0) goto L12;
        r3 = r0.rightMargin;
    L12:
        if ((r5 & 8) == 0) goto L14;
        r4 = r0.bottomMargin;
    L14:
        Intrinsics.checkNotNullParameter(r0, "<this>");
        r0.setMargins(r1, r2, r3, r4);
    }

    public static final void updateMarginsRelative(ViewGroup.MarginLayoutParams r1, int r2, int r3, int r4, int r5) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        r1.setMarginStart(r2);
        r1.topMargin = r3;
        r1.setMarginEnd(r4);
        r1.bottomMargin = r5;
    }

    public static /* synthetic */ void updateMarginsRelative$default(ViewGroup.MarginLayoutParams r0, int r1, int r2, int r3, int r4, int r5, Object r6) {
        if ((r5 & 1) == 0) goto L6;
        r1 = r0.getMarginStart();
    L6:
        if ((r5 & 2) == 0) goto L9;
        r2 = r0.topMargin;
    L9:
        if ((r5 & 4) == 0) goto L12;
        r3 = r0.getMarginEnd();
    L12:
        if ((r5 & 8) == 0) goto L14;
        r4 = r0.bottomMargin;
    L14:
        Intrinsics.checkNotNullParameter(r0, "<this>");
        r0.setMarginStart(r1);
        r0.topMargin = r2;
        r0.setMarginEnd(r3);
        r0.bottomMargin = r4;
    }
}
