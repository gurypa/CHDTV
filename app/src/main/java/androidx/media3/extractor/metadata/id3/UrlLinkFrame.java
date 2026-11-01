package androidx.media3.extractor.metadata.id3;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.media3.common.util.Util;

/* JADX INFO: loaded from: classes.dex */
public final class UrlLinkFrame extends Id3Frame {
    public static final Parcelable.Creator<UrlLinkFrame> CREATOR = null;
    public final String description;
    public final String url;

    static {
        CREATOR = new C04261();
    }

    UrlLinkFrame(Parcel r2) {
        super((String) Util.castNonNull(r2.readString()));
        this.description = r2.readString();
        this.url = (String) Util.castNonNull(r2.readString());
    }

    public UrlLinkFrame(String r1, String r2, String r3) {
        super(r1);
        this.description = r2;
        this.url = r3;
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
        UrlLinkFrame r2 = (UrlLinkFrame) r6;
        if (this.f55id.equals(r2.f55id) == false) goto L18;
        if (Util.areEqual(this.description, r2.description) == false) goto L18;
        if (Util.areEqual(this.url, r2.url) == false) goto L18;
        return true;
    L18:
        return false;
    }

    public int hashCode() {
        int r0 = ((17 * 31) + this.f55id.hashCode()) * 31;
        int r3 = 0;
        if (this.description == null) goto L5;
        int r2 = this.description.hashCode();
    L6:
        int r1 = (r0 + r2) * 31;
        if (this.url == null) goto L10;
        r3 = this.url.hashCode();
    L10:
        return r1 + r3;
    L5:
        r2 = 0;
        goto L6
    }

    @Override // androidx.media3.extractor.metadata.id3.Id3Frame
    public String toString() {
        return this.f55id + ": url=" + this.url;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel r2, int r3) {
        r2.writeString(this.f55id);
        r2.writeString(this.description);
        r2.writeString(this.url);
    }
}
