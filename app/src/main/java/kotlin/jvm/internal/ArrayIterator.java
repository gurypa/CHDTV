package kotlin.jvm.internal;

import androidx.exifinterface.media.ExifInterface;
import com.arthenica.ffmpegkit.StreamInformation;
import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.jvm.internal.markers.KMappedMarker;

/* JADX INFO: compiled from: ArrayIterator.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010(\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0005\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u0013\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004¢\u0006\u0002\u0010\u0005J\t\u0010\u000b\u001a\u00020\fH\u0096\u0002J\u000e\u0010\r\u001a\u00028\u0000H\u0096\u0002¢\u0006\u0002\u0010\u000eR\u0019\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004¢\u0006\n\n\u0002\u0010\b\u001a\u0004\b\u0006\u0010\u0007R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u000f"}, m421d2 = {"Lkotlin/jvm/internal/ArrayIterator;", ExifInterface.GPS_DIRECTION_TRUE, "", "array", "", "([Ljava/lang/Object;)V", "getArray", "()[Ljava/lang/Object;", "[Ljava/lang/Object;", StreamInformation.KEY_INDEX, "", "hasNext", "", "next", "()Ljava/lang/Object;", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
final class ArrayIterator<T> implements Iterator<T>, KMappedMarker {
    private final T[] array;
    private int index;

    public ArrayIterator(T[] r2) {
        Intrinsics.checkNotNullParameter(r2, "array");
        this.array = r2;
    }

    public final T[] getArray() {
        return this.array;
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        if (this.index >= this.array.length) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // java.util.Iterator
    public T next() {
        T[] r0 = this.array;     // Catch: ArrayIndexOutOfBoundsException -> L4
        int r1 = this.index;     // Catch: ArrayIndexOutOfBoundsException -> L4
        this.index = r1 + 1;     // Catch: ArrayIndexOutOfBoundsException -> L4
        return r0[r1];
    L4:
        e = move-exception;
        this.index--;
        throw new NoSuchElementException(e.getMessage());
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
}
