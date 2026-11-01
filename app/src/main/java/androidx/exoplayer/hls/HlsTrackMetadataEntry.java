package androidx.media3.exoplayer.hls;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import androidx.media3.common.Format;
import androidx.media3.common.MediaMetadata;
import androidx.media3.common.Metadata;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class HlsTrackMetadataEntry implements Metadata.Entry {
    public static final Parcelable.Creator<HlsTrackMetadataEntry> CREATOR = null;
    public final String groupId;
    public final String name;
    public final List<VariantInfo> variantInfos;

    public static final class VariantInfo implements Parcelable {
        public static final Parcelable.Creator<VariantInfo> CREATOR = null;
        public final String audioGroupId;
        public final int averageBitrate;
        public final String captionGroupId;
        public final int peakBitrate;
        public final String subtitleGroupId;
        public final String videoGroupId;

        static {
            CREATOR = new C02951();
        }

        public VariantInfo(int r1, int r2, String r3, String r4, String r5, String r6) {
            this.averageBitrate = r1;
            this.peakBitrate = r2;
            this.videoGroupId = r3;
            this.audioGroupId = r4;
            this.subtitleGroupId = r5;
            this.captionGroupId = r6;
        }

        VariantInfo(Parcel r2) {
            this.averageBitrate = r2.readInt();
            this.peakBitrate = r2.readInt();
            this.videoGroupId = r2.readString();
            this.audioGroupId = r2.readString();
            this.subtitleGroupId = r2.readString();
            this.captionGroupId = r2.readString();
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public boolean equals(Object r6) {
            if (this != r6) goto L6;
            return true;
        L6:
            if (r6 != null) goto L8;
        L25:
            return false;
        L8:
            if (getClass() != r6.getClass()) goto L25;
            VariantInfo r2 = (VariantInfo) r6;
            if (this.averageBitrate != r2.averageBitrate) goto L24;
            if (this.peakBitrate != r2.peakBitrate) goto L24;
            if (TextUtils.equals(this.videoGroupId, r2.videoGroupId) == false) goto L24;
            if (TextUtils.equals(this.audioGroupId, r2.audioGroupId) == false) goto L24;
            if (TextUtils.equals(this.subtitleGroupId, r2.subtitleGroupId) == false) goto L24;
            if (TextUtils.equals(this.captionGroupId, r2.captionGroupId) == false) goto L24;
            return true;
        L24:
            return false;
        }

        public int hashCode() {
            int r0 = ((this.averageBitrate * 31) + this.peakBitrate) * 31;
            int r3 = 0;
            if (this.videoGroupId == null) goto L5;
            int r2 = this.videoGroupId.hashCode();
        L6:
            int r1 = (r0 + r2) * 31;
            if (this.audioGroupId == null) goto L9;
            int r4 = this.audioGroupId.hashCode();
        L10:
            int r5 = (r1 + r4) * 31;
            if (this.subtitleGroupId == null) goto L13;
            int r6 = this.subtitleGroupId.hashCode();
        L14:
            int r7 = (r5 + r6) * 31;
            if (this.captionGroupId == null) goto L18;
            r3 = this.captionGroupId.hashCode();
        L18:
            return r7 + r3;
        L13:
            r6 = 0;
            goto L14
        L9:
            r4 = 0;
            goto L10
        L5:
            r2 = 0;
            goto L6
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel r2, int r3) {
            r2.writeInt(this.averageBitrate);
            r2.writeInt(this.peakBitrate);
            r2.writeString(this.videoGroupId);
            r2.writeString(this.audioGroupId);
            r2.writeString(this.subtitleGroupId);
            r2.writeString(this.captionGroupId);
        }
    }

    static {
        CREATOR = new C02941();
    }

    HlsTrackMetadataEntry(Parcel r5) {
        this.groupId = r5.readString();
        this.name = r5.readString();
        int r0 = r5.readInt();
        ArrayList r1 = new ArrayList(r0);
        int r2 = 0;
    L3:
        if (r2 >= r0) goto L5;
        r1.add((VariantInfo) r5.readParcelable(VariantInfo.class.getClassLoader()));
        r2 = r2 + 1;
        goto L3
    L5:
        this.variantInfos = Collections.unmodifiableList(r1);
    }

    public HlsTrackMetadataEntry(String r2, String r3, List<VariantInfo> r4) {
        this.groupId = r2;
        this.name = r3;
        this.variantInfos = Collections.unmodifiableList(new ArrayList(r4));
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
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
        HlsTrackMetadataEntry r2 = (HlsTrackMetadataEntry) r6;
        if (TextUtils.equals(this.groupId, r2.groupId) == false) goto L18;
        if (TextUtils.equals(this.name, r2.name) == false) goto L18;
        if (this.variantInfos.equals(r2.variantInfos) == false) goto L18;
        return true;
    L18:
        return false;
    }

    @Override // androidx.media3.common.Metadata.Entry
    public /* synthetic */ byte[] getWrappedMetadataBytes() {
        return Metadata.Entry.CC.$default$getWrappedMetadataBytes(this);
    }

    @Override // androidx.media3.common.Metadata.Entry
    public /* synthetic */ Format getWrappedMetadataFormat() {
        return Metadata.Entry.CC.$default$getWrappedMetadataFormat(this);
    }

    public int hashCode() {
        int r1 = 0;
        if (this.groupId == null) goto L5;
        int r0 = this.groupId.hashCode();
    L6:
        int r2 = r0 * 31;
        if (this.name == null) goto L10;
        r1 = this.name.hashCode();
    L10:
        return ((r2 + r1) * 31) + this.variantInfos.hashCode();
    L5:
        r0 = 0;
        goto L6
    }

    @Override // androidx.media3.common.Metadata.Entry
    public /* synthetic */ void populateMediaMetadata(MediaMetadata.Builder r1) {
        Metadata.Entry.CC.$default$populateMediaMetadata(this, r1);
    }

    public String toString() {
        StringBuilder r0 = new StringBuilder().append("HlsTrackMetadataEntry");
        if (this.groupId == null) goto L5;
        String r1 = " [" + this.groupId + ", " + this.name + "]";
    L7:
        return r0.append(r1).toString();
    L5:
        r1 = "";
        goto L7
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel r5, int r6) {
        r5.writeString(this.groupId);
        r5.writeString(this.name);
        int r0 = this.variantInfos.size();
        r5.writeInt(r0);
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L5;
        r5.writeParcelable(this.variantInfos.get(r1), 0);
        r1 = r1 + 1;
        goto L3
    }
}
