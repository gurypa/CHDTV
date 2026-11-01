package androidx.core.os;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class ParcelCompat {

    static class Api29Impl {
        private Api29Impl() {
        }

        static final <T extends Parcelable> List<T> readParcelableList(Parcel r1, List<T> r2, ClassLoader r3) {
            return r1.readParcelableList(r2, r3);
        }
    }

    static class Api30Impl {
        private Api30Impl() {
        }

        static final Parcelable.Creator<?> readParcelableCreator(Parcel r1, ClassLoader r2) {
            return r1.readParcelableCreator(r2);
        }
    }

    static class TiramisuImpl {
        private TiramisuImpl() {
        }

        public static <T> T[] readArray(Parcel r1, ClassLoader r2, Class<T> r3) {
            return (T[]) r1.readArray(r2, r3);
        }

        public static <T> ArrayList<T> readArrayList(Parcel r1, ClassLoader r2, Class<? extends T> r3) {
            return r1.readArrayList(r2, r3);
        }

        public static <V, K> HashMap<K, V> readHashMap(Parcel r1, ClassLoader r2, Class<? extends K> r3, Class<? extends V> r4) {
            return r1.readHashMap(r2, r3, r4);
        }

        public static <T> void readList(Parcel r0, List<? super T> r1, ClassLoader r2, Class<T> r3) {
            r0.readList(r1, r2, r3);
        }

        public static <K, V> void readMap(Parcel r0, Map<? super K, ? super V> r1, ClassLoader r2, Class<K> r3, Class<V> r4) {
            r0.readMap(r1, r2, r3, r4);
        }

        static <T extends Parcelable> T readParcelable(Parcel r1, ClassLoader r2, Class<T> r3) {
            return (T) r1.readParcelable(r2, r3);
        }

        static <T> T[] readParcelableArray(Parcel r1, ClassLoader r2, Class<T> r3) {
            return (T[]) r1.readParcelableArray(r2, r3);
        }

        public static <T> Parcelable.Creator<T> readParcelableCreator(Parcel r1, ClassLoader r2, Class<T> r3) {
            return r1.readParcelableCreator(r2, r3);
        }

        static <T> List<T> readParcelableList(Parcel r1, List<T> r2, ClassLoader r3, Class<T> r4) {
            return r1.readParcelableList(r2, r3, r4);
        }

        static <T extends Serializable> T readSerializable(Parcel r1, ClassLoader r2, Class<T> r3) {
            return (T) r1.readSerializable(r2, r3);
        }

        public static <T> SparseArray<T> readSparseArray(Parcel r1, ClassLoader r2, Class<? extends T> r3) {
            return r1.readSparseArray(r2, r3);
        }
    }

    private ParcelCompat() {
    }

    public static <T> T[] readArray(Parcel r1, ClassLoader r2, Class<T> r3) {
        if (BuildCompat.isAtLeastT() == false) goto L7;
        return (T[]) TiramisuImpl.readArray(r1, r2, r3);
    L7:
        return (T[]) r1.readArray(r2);
    }

    public static <T> ArrayList<T> readArrayList(Parcel r1, ClassLoader r2, Class<? extends T> r3) {
        if (BuildCompat.isAtLeastT() == false) goto L7;
        return TiramisuImpl.readArrayList(r1, r2, r3);
    L7:
        return r1.readArrayList(r2);
    }

    public static boolean readBoolean(Parcel r1) {
        if (r1.readInt() == 0) goto L5;
        return true;
    L5:
        return false;
    }

    public static <K, V> HashMap<K, V> readHashMap(Parcel r1, ClassLoader r2, Class<? extends K> r3, Class<? extends V> r4) {
        if (BuildCompat.isAtLeastT() == false) goto L7;
        return TiramisuImpl.readHashMap(r1, r2, r3, r4);
    L7:
        return r1.readHashMap(r2);
    }

    public static <T> void readList(Parcel r1, List<? super T> r2, ClassLoader r3, Class<T> r4) {
        if (BuildCompat.isAtLeastT() == false) goto L5;
        TiramisuImpl.readList(r1, r2, r3, r4);
        return;
    L5:
        r1.readList(r2, r3);
    }

    public static <K, V> void readMap(Parcel r1, Map<? super K, ? super V> r2, ClassLoader r3, Class<K> r4, Class<V> r5) {
        if (BuildCompat.isAtLeastT() == false) goto L5;
        TiramisuImpl.readMap(r1, r2, r3, r4, r5);
        return;
    L5:
        r1.readMap(r2, r3);
    }

    public static <T extends Parcelable> T readParcelable(Parcel r1, ClassLoader r2, Class<T> r3) {
        if (BuildCompat.isAtLeastT() == false) goto L7;
        return (T) TiramisuImpl.readParcelable(r1, r2, r3);
    L7:
        return (T) r1.readParcelable(r2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <T> T[] readParcelableArray(Parcel r1, ClassLoader r2, Class<T> r3) {
        if (BuildCompat.isAtLeastT() == false) goto L7;
        return (T[]) TiramisuImpl.readParcelableArray(r1, r2, r3);
    L7:
        return (T[]) r1.readParcelableArray(r2);
    }

    public static <T> Parcelable.Creator<T> readParcelableCreator(Parcel r1, ClassLoader r2, Class<T> r3) {
        if (BuildCompat.isAtLeastT() == false) goto L7;
        return TiramisuImpl.readParcelableCreator(r1, r2, r3);
    L7:
        return (Parcelable.Creator<T>) Api30Impl.readParcelableCreator(r1, r2);
    }

    public static <T> List<T> readParcelableList(Parcel r1, List<T> r2, ClassLoader r3, Class<T> r4) {
        if (BuildCompat.isAtLeastT() == false) goto L7;
        return TiramisuImpl.readParcelableList(r1, r2, r3, r4);
    L7:
        return Api29Impl.readParcelableList(r1, r2, r3);
    }

    public static <T extends Serializable> T readSerializable(Parcel r1, ClassLoader r2, Class<T> r3) {
        if (BuildCompat.isAtLeastT() == false) goto L7;
        return (T) TiramisuImpl.readSerializable(r1, r2, r3);
    L7:
        return (T) r1.readSerializable();
    }

    public static <T> SparseArray<T> readSparseArray(Parcel r1, ClassLoader r2, Class<? extends T> r3) {
        if (BuildCompat.isAtLeastT() == false) goto L7;
        return TiramisuImpl.readSparseArray(r1, r2, r3);
    L7:
        return r1.readSparseArray(r2);
    }

    public static void writeBoolean(Parcel r0, boolean r1) {
        r0.writeInt(r1 ? 1 : 0);
    }
}
