package androidx.media3.extractor.metadata.id3;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.media3.common.util.Util;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public final class PrivFrame extends Id3Frame {
    public static final Parcelable.Creator<PrivFrame> CREATOR = null;

    /* JADX INFO: renamed from: ID */
    public static final String f58ID = "PRIV";
    public final String owner;
    public final byte[] privateData;

    static {
        CREATOR = new C04241();
    }

    PrivFrame(Parcel r2) {
        super(f58ID);
        this.owner = (String) Util.castNonNull(r2.readString());
        this.privateData = (byte[]) Util.castNonNull(r2.createByteArray());
    }

    public PrivFrame(String r2, byte[] r3) {
        super(f58ID);
        this.owner = r2;
        this.privateData = r3;
    }

    public boolean equals(Object r6) {
        if (this != r6) goto L6;
        return true;
    L6:
        if (r6 != null) goto L8;
    L17:
        return false;
    L8:
        if (getClass() != r6.getClass()) goto L17;
        PrivFrame r2 = (PrivFrame) r6;
        if (Util.areEqual(this.owner, r2.owner) == false) goto L16;
        if (Arrays.equals(this.privateData, r2.privateData) == false) goto L16;
        return true;
    L16:
        return false;
    }

    public int hashCode() {
        int r1 = 17 * 31;
        if (this.owner == null) goto L5;
        int r2 = this.owner.hashCode();
    L7:
        return ((r1 + r2) * 31) + Arrays.hashCode(this.privateData);
    L5:
        r2 = 0;
        goto L7
    }

    @Override // androidx.media3.extractor.metadata.id3.Id3Frame
    public String toString() {
        return this.f55id + ": owner=" + this.owner;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel r2, int r3) {
        r2.writeString(this.owner);
        r2.writeByteArray(this.privateData);
    }
}
