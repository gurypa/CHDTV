package kotlin.enums;

import androidx.exifinterface.media.ExifInterface;
import com.arthenica.ffmpegkit.MediaInformation;
import com.arthenica.ffmpegkit.StreamInformation;
import java.io.Serializable;
import java.lang.Enum;
import kotlin.Metadata;
import kotlin.collections.AbstractList;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: EnumEntries.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010\u0000\n\u0000\b\u0003\u0018\u0000*\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u00032\b\u0012\u0004\u0012\u0002H\u00010\u00042\u00060\u0005j\u0002`\u0006B\u0013\u0012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\b¢\u0006\u0002\u0010\tJ\u0016\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00028\u0000H\u0096\u0002¢\u0006\u0002\u0010\u0012J\u0016\u0010\u0013\u001a\u00028\u00002\u0006\u0010\u0014\u001a\u00020\fH\u0096\u0002¢\u0006\u0002\u0010\u0015J\u0015\u0010\u0016\u001a\u00020\f2\u0006\u0010\u0011\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010\u0017J\u0015\u0010\u0018\u001a\u00020\f2\u0006\u0010\u0011\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010\u0017J\b\u0010\u0019\u001a\u00020\u001aH\u0002R\u0016\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\bX\u0082\u0004¢\u0006\u0004\n\u0002\u0010\nR\u0014\u0010\u000b\u001a\u00020\f8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\r\u0010\u000e¨\u0006\u001b"}, m421d2 = {"Lkotlin/enums/EnumEntriesList;", ExifInterface.GPS_DIRECTION_TRUE, "", "Lkotlin/enums/EnumEntries;", "Lkotlin/collections/AbstractList;", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "entries", "", "([Ljava/lang/Enum;)V", "[Ljava/lang/Enum;", MediaInformation.KEY_SIZE, "", "getSize", "()I", "contains", "", "element", "(Ljava/lang/Enum;)Z", "get", StreamInformation.KEY_INDEX, "(I)Ljava/lang/Enum;", "indexOf", "(Ljava/lang/Enum;)I", "lastIndexOf", "writeReplace", "", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
final class EnumEntriesList<T extends Enum<T>> extends AbstractList<T> implements EnumEntries<T>, Serializable {
    private final T[] entries;

    public EnumEntriesList(T[] r2) {
        Intrinsics.checkNotNullParameter(r2, "entries");
        this.entries = r2;
    }

    private final Object writeReplace() {
        return new EnumEntriesSerializationProxy(this.entries);
    }

    public boolean contains(T r3) {
        Intrinsics.checkNotNullParameter(r3, "element");
        if (((Enum) ArraysKt.getOrNull(this.entries, r3.ordinal())) != r3) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // kotlin.collections.AbstractCollection, java.util.Collection
    public final /* bridge */ boolean contains(Object r2) {
        if ((r2 instanceof Enum) == true) goto L7;
        return false;
    L7:
        return contains((Enum) r2);
    }

    @Override // kotlin.collections.AbstractList, java.util.List
    public T get(int r3) {
        AbstractList.Companion.checkElementIndex$kotlin_stdlib(r3, this.entries.length);
        return this.entries[r3];
    }

    @Override // kotlin.collections.AbstractList, java.util.List
    public /* bridge */ /* synthetic */ Object get(int r2) {
        return get(r2);
    }

    @Override // kotlin.collections.AbstractList, kotlin.collections.AbstractCollection
    public int getSize() {
        return this.entries.length;
    }

    public int indexOf(T r4) {
        Intrinsics.checkNotNullParameter(r4, "element");
        int r0 = r4.ordinal();
        if (((Enum) ArraysKt.getOrNull(this.entries, r0)) == r4) goto L7;
        return -1;
    L7:
        return r0;
    }

    @Override // kotlin.collections.AbstractList, java.util.List
    public final /* bridge */ int indexOf(Object r2) {
        if ((r2 instanceof Enum) == true) goto L7;
        return -1;
    L7:
        return indexOf((Enum) r2);
    }

    public int lastIndexOf(T r2) {
        Intrinsics.checkNotNullParameter(r2, "element");
        return indexOf(r2);
    }

    @Override // kotlin.collections.AbstractList, java.util.List
    public final /* bridge */ int lastIndexOf(Object r2) {
        if ((r2 instanceof Enum) == true) goto L7;
        return -1;
    L7:
        return lastIndexOf((Enum) r2);
    }
}
