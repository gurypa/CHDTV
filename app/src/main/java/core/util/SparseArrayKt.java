package androidx.core.util;

import android.util.SparseArray;
import androidx.exifinterface.media.ExifInterface;
import com.arthenica.ffmpegkit.MediaInformation;
import com.arthenica.ffmpegkit.StreamInformation;
import java.util.Iterator;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.IntIterator;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;

/* JADX INFO: compiled from: SparseArray.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000@\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010(\n\u0000\u001a!\u0010\u0006\u001a\u00020\u0007\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\b\u001a\u00020\u0001H\u0086\n\u001a!\u0010\t\u001a\u00020\u0007\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\b\u001a\u00020\u0001H\u0086\b\u001a&\u0010\n\u001a\u00020\u0007\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\u000b\u001a\u0002H\u0002H\u0086\b¢\u0006\u0002\u0010\f\u001aT\u0010\r\u001a\u00020\u000e\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u000326\u0010\u000f\u001a2\u0012\u0013\u0012\u00110\u0001¢\u0006\f\b\u0011\u0012\b\b\u0012\u0012\u0004\b\b(\b\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b\u0011\u0012\b\b\u0012\u0012\u0004\b\b(\u000b\u0012\u0004\u0012\u00020\u000e0\u0010H\u0086\bø\u0001\u0000\u001a.\u0010\u0013\u001a\u0002H\u0002\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\b\u001a\u00020\u00012\u0006\u0010\u0014\u001a\u0002H\u0002H\u0086\b¢\u0006\u0002\u0010\u0015\u001a7\u0010\u0016\u001a\u0002H\u0002\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\b\u001a\u00020\u00012\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0017H\u0086\bø\u0001\u0000¢\u0006\u0002\u0010\u0018\u001a\u0019\u0010\u0019\u001a\u00020\u0007\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u0086\b\u001a\u0019\u0010\u001a\u001a\u00020\u0007\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u0086\b\u001a\u0016\u0010\u001b\u001a\u00020\u001c\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003\u001a-\u0010\u001d\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0003\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u0086\u0002\u001a$\u0010\u001f\u001a\u00020\u000e\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0003\u001a+\u0010 \u001a\u00020\u0007\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\b\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u0002H\u0002¢\u0006\u0002\u0010!\u001a.\u0010\"\u001a\u00020\u000e\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\b\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u0002H\u0002H\u0086\n¢\u0006\u0002\u0010#\u001a\u001c\u0010$\u001a\b\u0012\u0004\u0012\u0002H\u00020%\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003\"\"\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00038Æ\u0002¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006&"}, m421d2 = {MediaInformation.KEY_SIZE, "", ExifInterface.GPS_DIRECTION_TRUE, "Landroid/util/SparseArray;", "getSize", "(Landroid/util/SparseArray;)I", "contains", "", "key", "containsKey", "containsValue", "value", "(Landroid/util/SparseArray;Ljava/lang/Object;)Z", "forEach", "", "action", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", "getOrDefault", "defaultValue", "(Landroid/util/SparseArray;ILjava/lang/Object;)Ljava/lang/Object;", "getOrElse", "Lkotlin/Function0;", "(Landroid/util/SparseArray;ILkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "isEmpty", "isNotEmpty", "keyIterator", "Lkotlin/collections/IntIterator;", "plus", "other", "putAll", "remove", "(Landroid/util/SparseArray;ILjava/lang/Object;)Z", "set", "(Landroid/util/SparseArray;ILjava/lang/Object;)V", "valueIterator", "", "core-ktx_release"}, m422k = 2, m423mv = {1, 7, 1}, m425xi = 48)
public final class SparseArrayKt {

    /* JADX INFO: Add missing generic type declarations: [T] */
    /* JADX INFO: renamed from: androidx.core.util.SparseArrayKt$valueIterator$1 */
    /* JADX INFO: compiled from: SparseArray.kt */
    @Metadata(m420d1 = {"\u0000\u001b\n\u0000\n\u0002\u0010(\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0004*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\t\u0010\b\u001a\u00020\tH\u0096\u0002J\u0016\u0010\n\u001a\n \u000b*\u0004\u0018\u00018\u00008\u0000H\u0096\u0002¢\u0006\u0002\u0010\fR\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0004\u0010\u0005\"\u0004\b\u0006\u0010\u0007¨\u0006\r"}, m421d2 = {"androidx/core/util/SparseArrayKt$valueIterator$1", "", StreamInformation.KEY_INDEX, "", "getIndex", "()I", "setIndex", "(I)V", "hasNext", "", "next", "kotlin.jvm.PlatformType", "()Ljava/lang/Object;", "core-ktx_release"}, m422k = 1, m423mv = {1, 7, 1}, m425xi = 48)
    public static final class C01051<T> implements Iterator<T>, KMappedMarker {
        final /* synthetic */ SparseArray<T> $this_valueIterator;
        private int index;

        C01051(SparseArray<T> r1) {
            this.$this_valueIterator = r1;
        }

        public final int getIndex() {
            return this.index;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.index >= this.$this_valueIterator.size()) goto L5;
            return true;
        L5:
            return false;
        }

        @Override // java.util.Iterator
        public T next() {
            SparseArray<T> r0 = this.$this_valueIterator;
            int r1 = this.index;
            this.index = r1 + 1;
            return r0.valueAt(r1);
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }

        public final void setIndex(int r1) {
            this.index = r1;
        }
    }

    public static final <T> boolean contains(SparseArray<T> r2, int r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        if (r2.indexOfKey(r3) < 0) goto L5;
        return true;
    L5:
        return false;
    }

    public static final <T> boolean containsKey(SparseArray<T> r2, int r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        if (r2.indexOfKey(r3) < 0) goto L5;
        return true;
    L5:
        return false;
    }

    public static final <T> boolean containsValue(SparseArray<T> r2, T r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        if (r2.indexOfValue(r3) < 0) goto L5;
        return true;
    L5:
        return false;
    }

    public static final <T> void forEach(SparseArray<T> r5, Function2<? super Integer, ? super T, Unit> r6) {
        Intrinsics.checkNotNullParameter(r5, "<this>");
        Intrinsics.checkNotNullParameter(r6, "action");
        int r1 = 0;
        int r2 = r5.size();
    L3:
        if (r1 >= r2) goto L5;
        r6.invoke(Integer.valueOf(r5.keyAt(r1)), r5.valueAt(r1));
        r1 = r1 + 1;
        goto L3
    }

    public static final <T> T getOrDefault(SparseArray<T> r2, int r3, T r4) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        T r1 = r2.get(r3);
        if (r1 == null) goto L5;
        return r1;
    L5:
        return r4;
    }

    public static final <T> T getOrElse(SparseArray<T> r2, int r3, Function0<? extends T> r4) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r4, "defaultValue");
        T r1 = r2.get(r3);
        if (r1 == null) goto L5;
        return r1;
    L5:
        return r4.invoke();
    }

    public static final <T> int getSize(SparseArray<T> r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return r2.size();
    }

    public static final <T> boolean isEmpty(SparseArray<T> r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        if (r2.size() != 0) goto L5;
        return true;
    L5:
        return false;
    }

    public static final <T> boolean isNotEmpty(SparseArray<T> r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        if (r2.size() == 0) goto L5;
        return true;
    L5:
        return false;
    }

    public static final <T> IntIterator keyIterator(final SparseArray<T> r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return new C01041(r1);
    }

    public static final <T> SparseArray<T> plus(SparseArray<T> r3, SparseArray<T> r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r4, "other");
        SparseArray<T> r0 = new SparseArray(r3.size() + r4.size());
        putAll(r0, r3);
        putAll(r0, r4);
        return r0;
    }

    public static final <T> void putAll(SparseArray<T> r7, SparseArray<T> r8) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        Intrinsics.checkNotNullParameter(r8, "other");
        int r2 = 0;
        int r3 = r8.size();
    L3:
        if (r2 >= r3) goto L6;
        r7.put(r8.keyAt(r2), r8.valueAt(r2));
        r2 = r2 + 1;
        goto L3
    }

    public static final <T> boolean remove(SparseArray<T> r2, int r3, T r4) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        int r0 = r2.indexOfKey(r3);
        if (r0 >= 0) goto L5;
        return false;
    L5:
        if (Intrinsics.areEqual(r4, r2.valueAt(r0)) == false) goto L10;
        r2.removeAt(r0);
        return true;
    L10:
        return false;
    }

    public static final <T> void set(SparseArray<T> r1, int r2, T r3) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        r1.put(r2, r3);
    }

    public static final <T> Iterator<T> valueIterator(SparseArray<T> r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return new C01051(r1);
    }
}
