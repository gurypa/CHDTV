package androidx.media3.extractor.metadata.id3;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.media3.common.util.Util;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public final class BinaryFrame extends Id3Frame {
    public static final Parcelable.Creator<BinaryFrame> CREATOR = null;
    public final byte[] data;

    static {
        CREATOR = new C04171();
    }

    BinaryFrame(Parcel r2) {
        super((String) Util.castNonNull(r2.readString()));
        this.data = (byte[]) Util.castNonNull(r2.createByteArray());
    }

    public BinaryFrame(String r1, byte[] r2) {
        super(r1);
        this.data = r2;
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
        BinaryFrame r2 = (BinaryFrame) r6;
        if (this.f55id.equals(r2.f55id) == false) goto L16;
        if (Arrays.equals(this.data, r2.data) == false) goto L16;
        return true;
    L16:
        return false;
    }

    public int hashCode() {
        return (((17 * 31) + this.f55id.hashCode()) * 31) + Arrays.hashCode(this.data);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel r2, int r3) {
        r2.writeString(this.f55id);
        r2.writeByteArray(this.data);
    }
}
