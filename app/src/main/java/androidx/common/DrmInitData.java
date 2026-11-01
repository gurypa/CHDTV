package androidx.media3.common;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/* JADX INFO: loaded from: classes.dex */
public final class DrmInitData implements Comparator<SchemeData>, Parcelable {
    public static final Parcelable.Creator<DrmInitData> CREATOR = null;
    private int hashCode;
    public final int schemeDataCount;
    private final SchemeData[] schemeDatas;
    public final String schemeType;

    public static final class SchemeData implements Parcelable {
        public static final Parcelable.Creator<SchemeData> CREATOR = null;
        public final byte[] data;
        private int hashCode;
        public final String licenseServerUrl;
        public final String mimeType;
        public final UUID uuid;

        static {
            CREATOR = new C01841();
        }

        SchemeData(Parcel r6) {
            this.uuid = new UUID(r6.readLong(), r6.readLong());
            this.licenseServerUrl = r6.readString();
            this.mimeType = (String) Util.castNonNull(r6.readString());
            this.data = r6.createByteArray();
        }

        public SchemeData(UUID r2, String r3, String r4, byte[] r5) {
            this.uuid = (UUID) Assertions.checkNotNull(r2);
            this.licenseServerUrl = r3;
            this.mimeType = MimeTypes.normalizeMimeType((String) Assertions.checkNotNull(r4));
            this.data = r5;
        }

        public SchemeData(UUID r2, String r3, byte[] r4) {
            this(r2, null, r3, r4);
        }

        public boolean canReplace(SchemeData r2) {
            if (hasData() == true) goto L5;
        L9:
            return false;
        L5:
            if (r2.hasData() == true) goto L9;
            if (matches(r2.uuid) == false) goto L9;
            return true;
        }

        public SchemeData copyWithData(byte[] r5) {
            return new SchemeData(this.uuid, this.licenseServerUrl, this.mimeType, r5);
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public boolean equals(Object r6) {
            if ((r6 instanceof SchemeData) == true) goto L6;
            return false;
        L6:
            if (r6 != this) goto L8;
            return true;
        L8:
            SchemeData r2 = (SchemeData) r6;
            if (Util.areEqual(this.licenseServerUrl, r2.licenseServerUrl) == true) goto L11;
            return false;
        L11:
            if (Util.areEqual(this.mimeType, r2.mimeType) == true) goto L13;
            return false;
        L13:
            if (Util.areEqual(this.uuid, r2.uuid) == true) goto L15;
            return false;
        L15:
            if (Arrays.equals(this.data, r2.data) == true) goto L18;
            return false;
        L18:
            return true;
        }

        public boolean hasData() {
            if (this.data == null) goto L5;
            return true;
        L5:
            return false;
        }

        public int hashCode() {
            if (this.hashCode != 0) goto L10;
            int r1 = this.uuid.hashCode() * 31;
            if (this.licenseServerUrl != null) goto L7;
            int r2 = 0;
        L8:
            this.hashCode = ((((r1 + r2) * 31) + this.mimeType.hashCode()) * 31) + Arrays.hashCode(this.data);
            goto L10
        L7:
            r2 = this.licenseServerUrl.hashCode();
        L10:
            return this.hashCode;
        }

        public boolean matches(UUID r3) {
            if (C0180C.UUID_NIL.equals(this.uuid) == false) goto L5;
        L8:
            return true;
        L5:
            if (r3.equals(this.uuid) == true) goto L8;
            return false;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel r3, int r4) {
            r3.writeLong(this.uuid.getMostSignificantBits());
            r3.writeLong(this.uuid.getLeastSignificantBits());
            r3.writeString(this.licenseServerUrl);
            r3.writeString(this.mimeType);
            r3.writeByteArray(this.data);
        }
    }

    static {
        CREATOR = new C01831();
    }

    DrmInitData(Parcel r2) {
        this.schemeType = r2.readString();
        this.schemeDatas = (SchemeData[]) Util.castNonNull((SchemeData[]) r2.createTypedArray(SchemeData.CREATOR));
        this.schemeDataCount = this.schemeDatas.length;
    }

    public DrmInitData(String r3, List<SchemeData> r4) {
        this(r3, false, (SchemeData[]) r4.toArray(new SchemeData[0]));
    }

    private DrmInitData(String r2, boolean r3, SchemeData... r4) {
        this.schemeType = r2;
        if (r3 == false) goto L5;
        r4 = (SchemeData[]) r4.clone();
    L5:
        this.schemeDatas = r4;
        this.schemeDataCount = r4.length;
        Arrays.sort(this.schemeDatas, this);
    }

    public DrmInitData(String r2, SchemeData... r3) {
        this(r2, true, r3);
    }

    public DrmInitData(List<SchemeData> r4) {
        this(null, false, (SchemeData[]) r4.toArray(new SchemeData[0]));
    }

    public DrmInitData(SchemeData... r2) {
        this(null, r2);
    }

    private static boolean containsSchemeDataWithUuid(ArrayList<SchemeData> r2, int r3, UUID r4) {
        int r0 = 0;
    L3:
        if (r0 >= r3) goto L9;
        if (r2.get(r0).uuid.equals(r4) == true) goto L6;
        r0 = r0 + 1;
        goto L3
    L6:
        return true;
    L9:
        return false;
    }

    public static DrmInitData createSessionCreationData(DrmInitData r8, DrmInitData r9) {
        ArrayList r0 = new ArrayList();
        String r1 = null;
        int r2 = 0;
        if (r8 == null) goto L10;
        r1 = r8.schemeType;
        SchemeData[] r3 = r8.schemeDatas;
        int r4 = r3.length;
        int r5 = 0;
    L5:
        if (r5 >= r4) goto L10;
        SchemeData r6 = r3[r5];
        if (r6.hasData() == false) goto L9;
        r0.add(r6);
    L9:
        r5 = r5 + 1;
    L10:
        if (r9 == null) goto L22;
        if (r1 != null) goto L13;
        r1 = r9.schemeType;
    L13:
        int r7 = r0.size();
        SchemeData[] r10 = r9.schemeDatas;
        int r11 = r10.length;
    L14:
        if (r2 >= r11) goto L22;
        SchemeData r12 = r10[r2];
        if (r12.hasData() == false) goto L20;
        if (containsSchemeDataWithUuid(r0, r7, r12.uuid) == true) goto L20;
        r0.add(r12);
    L20:
        r2 = r2 + 1;
    L22:
        if (r0.isEmpty() == false) goto L25;
        return null;
    L25:
        return new DrmInitData(r1, r0);
    }

    /* JADX INFO: renamed from: compare, reason: avoid collision after fix types in other method */
    public int compare2(SchemeData r3, SchemeData r4) {
        if (C0180C.UUID_NIL.equals(r3.uuid) == false) goto L9;
        if (C0180C.UUID_NIL.equals(r4.uuid) == false) goto L7;
        return 0;
    L7:
        return 1;
    L9:
        return r3.uuid.compareTo(r4.uuid);
    }

    @Override // java.util.Comparator
    public /* bridge */ /* synthetic */ int compare(SchemeData r1, SchemeData r2) {
        return compare2(r1, r2);
    }

    public DrmInitData copyWithSchemeType(String r4) {
        if (Util.areEqual(this.schemeType, r4) == false) goto L6;
        return this;
    L6:
        return new DrmInitData(r4, false, this.schemeDatas);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // java.util.Comparator
    public boolean equals(Object r6) {
        if (this != r6) goto L6;
        return true;
    L6:
        if (r6 != null) goto L8;
    L17:
        return false;
    L8:
        if (getClass() != r6.getClass()) goto L17;
        DrmInitData r2 = (DrmInitData) r6;
        if (Util.areEqual(this.schemeType, r2.schemeType) == false) goto L16;
        if (Arrays.equals(this.schemeDatas, r2.schemeDatas) == false) goto L16;
        return true;
    L16:
        return false;
    }

    public SchemeData get(int r2) {
        return this.schemeDatas[r2];
    }

    public int hashCode() {
        if (this.hashCode != 0) goto L10;
        if (this.schemeType != null) goto L7;
        int r0 = 0;
    L8:
        this.hashCode = (r0 * 31) + Arrays.hashCode(this.schemeDatas);
        goto L10
    L7:
        r0 = this.schemeType.hashCode();
    L10:
        return this.hashCode;
    }

    public DrmInitData merge(DrmInitData r4) {
        if (this.schemeType != null) goto L5;
    L10:
        boolean r0 = true;
    L11:
        Assertions.checkState(r0);
        if (this.schemeType == null) goto L14;
        String r1 = this.schemeType;
    L16:
        return new DrmInitData(r1, (SchemeData[]) Util.nullSafeArrayConcatenation(this.schemeDatas, r4.schemeDatas));
    L14:
        r1 = r4.schemeType;
        goto L16
    L5:
        if (r4.schemeType == null) goto L10;
        if (TextUtils.equals(this.schemeType, r4.schemeType) == true) goto L10;
        r0 = false;
        goto L11
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel r3, int r4) {
        r3.writeString(this.schemeType);
        r3.writeTypedArray(this.schemeDatas, 0);
    }
}
