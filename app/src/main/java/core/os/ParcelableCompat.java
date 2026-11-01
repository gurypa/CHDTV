package androidx.core.os;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
@Deprecated
public final class ParcelableCompat {

    static class ParcelableCompatCreatorHoneycombMR2<T> implements Parcelable.ClassLoaderCreator<T> {
        private final ParcelableCompatCreatorCallbacks<T> mCallbacks;

        ParcelableCompatCreatorHoneycombMR2(ParcelableCompatCreatorCallbacks<T> r1) {
            this.mCallbacks = r1;
        }

        @Override // android.os.Parcelable.Creator
        public T createFromParcel(Parcel r3) {
            return this.mCallbacks.createFromParcel(r3, null);
        }

        @Override // android.os.Parcelable.ClassLoaderCreator
        public T createFromParcel(Parcel r2, ClassLoader r3) {
            return this.mCallbacks.createFromParcel(r2, r3);
        }

        @Override // android.os.Parcelable.Creator
        public T[] newArray(int r2) {
            return this.mCallbacks.newArray(r2);
        }
    }

    private ParcelableCompat() {
    }

    @Deprecated
    public static <T> Parcelable.Creator<T> newCreator(ParcelableCompatCreatorCallbacks<T> r1) {
        return new ParcelableCompatCreatorHoneycombMR2(r1);
    }
}
