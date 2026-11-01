package androidx.media3.extractor.metadata.id3;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.media3.common.util.Util;

/* JADX INFO: loaded from: classes.dex */
public final class InternalFrame extends Id3Frame {
    public static final Parcelable.Creator<InternalFrame> CREATOR = null;

    /* JADX INFO: renamed from: ID */
    public static final String f56ID = "----";
    public final String description;
    public final String domain;
    public final String text;

    static {
        CREATOR = new C04221();
    }

    InternalFrame(Parcel r2) {
        super(f56ID);
        this.domain = (String) Util.castNonNull(r2.readString());
        this.description = (String) Util.castNonNull(r2.readString());
        this.text = (String) Util.castNonNull(r2.readString());
    }

    public InternalFrame(String r2, String r3, String r4) {
        super(f56ID);
        this.domain = r2;
        this.description = r3;
        this.text = r4;
    }

    public boolean equals(Object r6) {
        if (this != r6) goto L6;
        return true;
    L6:
        if (r6 != null) goto L8;
    L19:
        return false;
    L8:
        if (getClass() != r6.getClass()) goto L19;
        InternalFrame r2 = (InternalFrame) r6;
        if (Util.areEqual(this.description, r2.description) == false) goto L18;
        if (Util.areEqual(this.domain, r2.domain) == false) goto L18;
        if (Util.areEqual(this.text, r2.text) == false) goto L18;
        return true;
    L18:
        return false;
    }

    public int hashCode() {
        int r1 = 17 * 31;
        int r3 = 0;
        if (this.domain == null) goto L5;
        int r2 = this.domain.hashCode();
    L6:
        int r0 = (r1 + r2) * 31;
        if (this.description == null) goto L9;
        int r4 = this.description.hashCode();
    L10:
        int r5 = (r0 + r4) * 31;
        if (this.text == null) goto L14;
        r3 = this.text.hashCode();
    L14:
        return r5 + r3;
    L9:
        r4 = 0;
        goto L10
    L5:
        r2 = 0;
        goto L6
    }

    @Override // androidx.media3.extractor.metadata.id3.Id3Frame
    public String toString() {
        return this.f55id + ": domain=" + this.domain + ", description=" + this.description;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel r2, int r3) {
        r2.writeString(this.f55id);
        r2.writeString(this.domain);
        r2.writeString(this.text);
    }
}
