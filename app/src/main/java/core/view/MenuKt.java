package androidx.core.view;

import android.view.Menu;
import android.view.MenuItem;
import com.arthenica.ffmpegkit.MediaInformation;
import com.arthenica.ffmpegkit.StreamInformation;
import java.util.Iterator;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMutableIterator;
import kotlin.sequences.Sequence;

/* JADX INFO: compiled from: Menu.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000D\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010)\n\u0002\b\u0003\u001a\u0015\u0010\n\u001a\u00020\u000b*\u00020\u00032\u0006\u0010\f\u001a\u00020\u0002H\u0086\u0002\u001a3\u0010\r\u001a\u00020\u000e*\u00020\u00032!\u0010\u000f\u001a\u001d\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u0011\u0012\b\b\u0012\u0012\u0004\b\b(\f\u0012\u0004\u0012\u00020\u000e0\u0010H\u0086\bø\u0001\u0000\u001aH\u0010\u0013\u001a\u00020\u000e*\u00020\u000326\u0010\u000f\u001a2\u0012\u0013\u0012\u00110\u0007¢\u0006\f\b\u0011\u0012\b\b\u0012\u0012\u0004\b\b(\u0015\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u0011\u0012\b\b\u0012\u0012\u0004\b\b(\f\u0012\u0004\u0012\u00020\u000e0\u0014H\u0086\bø\u0001\u0000\u001a\u0015\u0010\u0016\u001a\u00020\u0002*\u00020\u00032\u0006\u0010\u0015\u001a\u00020\u0007H\u0086\n\u001a\r\u0010\u0017\u001a\u00020\u000b*\u00020\u0003H\u0086\b\u001a\r\u0010\u0018\u001a\u00020\u000b*\u00020\u0003H\u0086\b\u001a\u0013\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00020\u001a*\u00020\u0003H\u0086\u0002\u001a\u0015\u0010\u001b\u001a\u00020\u000e*\u00020\u00032\u0006\u0010\f\u001a\u00020\u0002H\u0086\n\u001a\u0015\u0010\u001c\u001a\u00020\u000e*\u00020\u00032\u0006\u0010\u0015\u001a\u00020\u0007H\u0086\b\"\u001b\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001*\u00020\u00038F¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005\"\u0016\u0010\u0006\u001a\u00020\u0007*\u00020\u00038Æ\u0002¢\u0006\u0006\u001a\u0004\b\b\u0010\t\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\u001d"}, m421d2 = {"children", "Lkotlin/sequences/Sequence;", "Landroid/view/MenuItem;", "Landroid/view/Menu;", "getChildren", "(Landroid/view/Menu;)Lkotlin/sequences/Sequence;", MediaInformation.KEY_SIZE, "", "getSize", "(Landroid/view/Menu;)I", "contains", "", "item", "forEach", "", "action", "Lkotlin/Function1;", "Lkotlin/ParameterName;", "name", "forEachIndexed", "Lkotlin/Function2;", StreamInformation.KEY_INDEX, "get", "isEmpty", "isNotEmpty", "iterator", "", "minusAssign", "removeItemAt", "core-ktx_release"}, m422k = 2, m423mv = {1, 7, 1}, m425xi = 48)
public final class MenuKt {

    /* JADX INFO: renamed from: androidx.core.view.MenuKt$iterator$1 */
    /* JADX INFO: compiled from: Menu.kt */
    @Metadata(m420d1 = {"\u0000#\n\u0000\n\u0002\u0010)\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001J\t\u0010\u0005\u001a\u00020\u0006H\u0096\u0002J\t\u0010\u0007\u001a\u00020\u0002H\u0096\u0002J\b\u0010\b\u001a\u00020\tH\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\n"}, m421d2 = {"androidx/core/view/MenuKt$iterator$1", "", "Landroid/view/MenuItem;", StreamInformation.KEY_INDEX, "", "hasNext", "", "next", "remove", "", "core-ktx_release"}, m422k = 1, m423mv = {1, 7, 1}, m425xi = 48)
    public static final class C01131 implements Iterator<MenuItem>, KMutableIterator {
        final /* synthetic */ Menu $this_iterator;
        private int index;

        C01131(Menu r1) {
            this.$this_iterator = r1;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.index >= this.$this_iterator.size()) goto L5;
            return true;
        L5:
            return false;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public MenuItem next() {
            Menu r0 = this.$this_iterator;
            int r1 = this.index;
            this.index = r1 + 1;
            MenuItem r2 = r0.getItem(r1);
            if (r2 == null) goto L6;
            return r2;
        L6:
            throw new IndexOutOfBoundsException();
        }

        @Override // java.util.Iterator
        public /* bridge */ /* synthetic */ MenuItem next() {
            return next();
        }

        @Override // java.util.Iterator
        public void remove() {
            Menu r0 = this.$this_iterator;
            this.index--;
            MenuItem r3 = r0.getItem(this.index);
            if (r3 == null) goto L5;
            Intrinsics.checkNotNullExpressionValue(r3, "getItem(index)");
            r0.removeItem(r3.getItemId());
            Unit r4 = Unit.INSTANCE;
        L6:
            if (r4 == null) goto L9;
            return;
        L9:
            throw new IndexOutOfBoundsException();
        L5:
            r4 = null;
            goto L6
        }
    }

    public static final boolean contains(Menu r3, MenuItem r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r4, "item");
        int r0 = 0;
        int r1 = r3.size();
    L3:
        if (r0 >= r1) goto L9;
        if (Intrinsics.areEqual(r3.getItem(r0), r4) == true) goto L6;
        r0 = r0 + 1;
        goto L3
    L6:
        return true;
    L9:
        return false;
    }

    public static final void forEach(Menu r5, Function1<? super MenuItem, Unit> r6) {
        Intrinsics.checkNotNullParameter(r5, "<this>");
        Intrinsics.checkNotNullParameter(r6, "action");
        int r1 = 0;
        int r2 = r5.size();
    L3:
        if (r1 >= r2) goto L5;
        MenuItem r3 = r5.getItem(r1);
        Intrinsics.checkNotNullExpressionValue(r3, "getItem(index)");
        r6.invoke(r3);
        r1 = r1 + 1;
        goto L3
    }

    public static final void forEachIndexed(Menu r6, Function2<? super Integer, ? super MenuItem, Unit> r7) {
        Intrinsics.checkNotNullParameter(r6, "<this>");
        Intrinsics.checkNotNullParameter(r7, "action");
        int r1 = 0;
        int r2 = r6.size();
    L3:
        if (r1 >= r2) goto L5;
        Integer r3 = Integer.valueOf(r1);
        MenuItem r4 = r6.getItem(r1);
        Intrinsics.checkNotNullExpressionValue(r4, "getItem(index)");
        r7.invoke(r3, r4);
        r1 = r1 + 1;
        goto L3
    }

    public static final MenuItem get(Menu r3, int r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        MenuItem r1 = r3.getItem(r4);
        Intrinsics.checkNotNullExpressionValue(r1, "getItem(index)");
        return r1;
    }

    public static final Sequence<MenuItem> getChildren(final Menu r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return new MenuKt$children$1(r1);
    }

    public static final int getSize(Menu r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return r2.size();
    }

    public static final boolean isEmpty(Menu r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        if (r2.size() != 0) goto L5;
        return true;
    L5:
        return false;
    }

    public static final boolean isNotEmpty(Menu r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        if (r2.size() == 0) goto L5;
        return true;
    L5:
        return false;
    }

    public static final Iterator<MenuItem> iterator(Menu r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return new C01131(r1);
    }

    public static final void minusAssign(Menu r2, MenuItem r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "item");
        r2.removeItem(r3.getItemId());
    }

    public static final void removeItemAt(Menu r4, int r5) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        MenuItem r1 = r4.getItem(r5);
        if (r1 == null) goto L5;
        r4.removeItem(r1.getItemId());
        Unit r2 = Unit.INSTANCE;
    L6:
        if (r2 == null) goto L9;
        return;
    L9:
        throw new IndexOutOfBoundsException();
    L5:
        r2 = null;
        goto L6
    }
}
