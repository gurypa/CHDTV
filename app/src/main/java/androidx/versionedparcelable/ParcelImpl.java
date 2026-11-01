package androidx.versionedparcelable;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public class ParcelImpl implements Parcelable {
    public static final Parcelable.Creator<ParcelImpl> CREATOR = null;
    private final VersionedParcelable mParcel;

    static {
        CREATOR = new C05211();
    }

    protected ParcelImpl(Parcel r2) {
        this.mParcel = new VersionedParcelParcel(r2).readVersionedParcelable();
    }

    public ParcelImpl(VersionedParcelable r1) {
        this.mParcel = r1;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public <T extends VersionedParcelable> T getVersionedParcel() {
        return (T) this.mParcel;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel r3, int r4) {
        new VersionedParcelParcel(r3).writeVersionedParcelable(this.mParcel);
    }
}
