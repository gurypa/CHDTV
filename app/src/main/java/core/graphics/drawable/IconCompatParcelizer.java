package androidx.core.graphics.drawable;

import android.content.res.ColorStateList;
import androidx.versionedparcelable.VersionedParcel;

/* JADX INFO: loaded from: classes.dex */
public class IconCompatParcelizer {
    public IconCompatParcelizer() {
    }

    public static IconCompat read(VersionedParcel r3) {
        IconCompat r0 = new IconCompat();
        r0.mType = r3.readInt(r0.mType, 1);
        r0.mData = r3.readByteArray(r0.mData, 2);
        r0.mParcelable = r3.readParcelable(r0.mParcelable, 3);
        r0.mInt1 = r3.readInt(r0.mInt1, 4);
        r0.mInt2 = r3.readInt(r0.mInt2, 5);
        r0.mTintList = (ColorStateList) r3.readParcelable(r0.mTintList, 6);
        r0.mTintModeStr = r3.readString(r0.mTintModeStr, 7);
        r0.mString1 = r3.readString(r0.mString1, 8);
        r0.onPostParceling();
        return r0;
    }

    public static void write(IconCompat r3, VersionedParcel r4) {
        r4.setSerializationFlags(true, true);
        r3.onPreParceling(r4.isStream());
        if ((-1) == r3.mType) goto L6;
        r4.writeInt(r3.mType, 1);
    L6:
        if (r3.mData == null) goto L9;
        r4.writeByteArray(r3.mData, 2);
    L9:
        if (r3.mParcelable == null) goto L12;
        r4.writeParcelable(r3.mParcelable, 3);
    L12:
        if (r3.mInt1 == 0) goto L15;
        r4.writeInt(r3.mInt1, 4);
    L15:
        if (r3.mInt2 == 0) goto L18;
        r4.writeInt(r3.mInt2, 5);
    L18:
        if (r3.mTintList == null) goto L21;
        r4.writeParcelable(r3.mTintList, 6);
    L21:
        if (r3.mTintModeStr == null) goto L24;
        r4.writeString(r3.mTintModeStr, 7);
    L24:
        if (r3.mString1 == null) goto L27;
        r4.writeString(r3.mString1, 8);
        return;
    }
}
