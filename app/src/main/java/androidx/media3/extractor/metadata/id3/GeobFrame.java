package androidx.media3.extractor.metadata.id3;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.media3.common.util.Util;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public final class GeobFrame extends Id3Frame {
    public static final Parcelable.Creator<GeobFrame> CREATOR = null;

    /* JADX INFO: renamed from: ID */
    public static final String f54ID = "GEOB";
    public final byte[] data;
    public final String description;
    public final String filename;
    public final String mimeType;

    static {
        CREATOR = new C04211();
    }

    GeobFrame(Parcel r2) {
        super(f54ID);
        this.mimeType = (String) Util.castNonNull(r2.readString());
        this.filename = (String) Util.castNonNull(r2.readString());
        this.description = (String) Util.castNonNull(r2.readString());
        this.data = (byte[]) Util.castNonNull(r2.createByteArray());
    }

    public GeobFrame(String r2, String r3, String r4, byte[] r5) {
        super(f54ID);
        this.mimeType = r2;
        this.filename = r3;
        this.description = r4;
        this.data = r5;
    }

    public boolean equals(Object r6) {
        if (this != r6) goto L6;
        return true;
    L6:
        if (r6 != null) goto L8;
    L21:
        return false;
    L8:
        if (getClass() != r6.getClass()) goto L21;
        GeobFrame r2 = (GeobFrame) r6;
        if (Util.areEqual(this.mimeType, r2.mimeType) == false) goto L20;
        if (Util.areEqual(this.filename, r2.filename) == false) goto L20;
        if (Util.areEqual(this.description, r2.description) == false) goto L20;
        if (Arrays.equals(this.data, r2.data) == false) goto L20;
        return true;
    L20:
        return false;
    }

    public int hashCode() {
        int r1 = 17 * 31;
        int r3 = 0;
        if (this.mimeType == null) goto L5;
        int r2 = this.mimeType.hashCode();
    L6:
        int r0 = (r1 + r2) * 31;
        if (this.filename == null) goto L9;
        int r4 = this.filename.hashCode();
    L10:
        int r5 = (r0 + r4) * 31;
        if (this.description == null) goto L14;
        r3 = this.description.hashCode();
    L14:
        return ((r5 + r3) * 31) + Arrays.hashCode(this.data);
    L9:
        r4 = 0;
        goto L10
    L5:
        r2 = 0;
        goto L6
    }

    @Override // androidx.media3.extractor.metadata.id3.Id3Frame
    public String toString() {
        return this.f55id + ": mimeType=" + this.mimeType + ", filename=" + this.filename + ", description=" + this.description;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel r2, int r3) {
        r2.writeString(this.mimeType);
        r2.writeString(this.filename);
        r2.writeString(this.description);
        r2.writeByteArray(this.data);
    }
}
