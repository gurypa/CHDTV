package androidx.media3.extractor.metadata.icy;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.media3.common.Format;
import androidx.media3.common.MediaMetadata;
import androidx.media3.common.Metadata;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.Util;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class IcyHeaders implements Metadata.Entry {
    public static final Parcelable.Creator<IcyHeaders> CREATOR = null;
    public static final String REQUEST_HEADER_ENABLE_METADATA_NAME = "Icy-MetaData";
    public static final String REQUEST_HEADER_ENABLE_METADATA_VALUE = "1";
    private static final String RESPONSE_HEADER_BITRATE = "icy-br";
    private static final String RESPONSE_HEADER_GENRE = "icy-genre";
    private static final String RESPONSE_HEADER_METADATA_INTERVAL = "icy-metaint";
    private static final String RESPONSE_HEADER_NAME = "icy-name";
    private static final String RESPONSE_HEADER_PUB = "icy-pub";
    private static final String RESPONSE_HEADER_URL = "icy-url";
    private static final String TAG = "IcyHeaders";
    public final int bitrate;
    public final String genre;
    public final boolean isPublic;
    public final int metadataInterval;
    public final String name;
    public final String url;

    static {
        CREATOR = new C04141();
    }

    public IcyHeaders(int r2, String r3, String r4, String r5, boolean r6, int r7) {
        if (r7 == (-1)) goto L7;
        if (r7 > 0) goto L7;
        boolean r0 = false;
    L8:
        Assertions.checkArgument(r0);
        this.bitrate = r2;
        this.genre = r3;
        this.name = r4;
        this.url = r5;
        this.isPublic = r6;
        this.metadataInterval = r7;
        return;
    L7:
        r0 = true;
        goto L8
    }

    IcyHeaders(Parcel r2) {
        this.bitrate = r2.readInt();
        this.genre = r2.readString();
        this.name = r2.readString();
        this.url = r2.readString();
        this.isPublic = Util.readBoolean(r2);
        this.metadataInterval = r2.readInt();
    }

    public static IcyHeaders parse(Map<String, List<String>> r20) {
        boolean r3 = false;
        int r4 = -1;
        int r9 = -1;
        List<String> r10 = r20.get(RESPONSE_HEADER_BITRATE);
        if (r10 == null) goto L13;
        String r13 = r10.get(0);
        r4 = Integer.parseInt(r13) * 1000;     // Catch: NumberFormatException -> L11
        if (r4 <= 0) goto L8;
        r3 = true;
    L10:
        int r14 = r4;
    L14:
        List<String> r0 = r20.get(RESPONSE_HEADER_GENRE);
        if (r0 == null) goto L17;
        r3 = true;
        String r15 = r0.get(0);
    L18:
        List<String> r1 = r20.get(RESPONSE_HEADER_NAME);
        if (r1 == null) goto L21;
        r3 = true;
        String r16 = r1.get(0);
    L22:
        List<String> r2 = r20.get(RESPONSE_HEADER_URL);
        if (r2 == null) goto L25;
        r3 = true;
        String r17 = r2.get(0);
    L26:
        List<String> r5 = r20.get(RESPONSE_HEADER_PUB);
        if (r5 == null) goto L29;
        r3 = true;
        boolean r18 = r5.get(0).equals(REQUEST_HEADER_ENABLE_METADATA_VALUE);
    L30:
        List<String> r6 = r20.get(RESPONSE_HEADER_METADATA_INTERVAL);
        if (r6 == null) goto L44;
        String r7 = r6.get(0);
        int r8 = Integer.parseInt(r7);     // Catch: NumberFormatException -> L42
        if (r8 <= 0) goto L51;
        r3 = true;
        int r11 = r8;
    L39:
        int r19 = r11;
    L45:
        if (r3 == true) goto L55;
        return null;
    L55:
        return new IcyHeaders(r14, r15, r16, r17, r18, r19);
    L51:
        Log.m48w(TAG, "Invalid metadata interval: " + r7);     // Catch: NumberFormatException -> L40
        r11 = -1;
    L41:
        r9 = r8;
    L43:
        Log.m48w(TAG, "Invalid metadata interval: " + r7);
        r19 = r9;
        goto L45
    L44:
        r19 = -1;
        goto L45
    L29:
        r18 = false;
        goto L30
    L25:
        r17 = null;
        goto L26
    L21:
        r16 = null;
        goto L22
    L17:
        r15 = null;
        goto L18
    L8:
        Log.m48w(TAG, "Invalid bitrate: " + r13);     // Catch: NumberFormatException -> L11
        r4 = -1;
    L12:
        Log.m48w(TAG, "Invalid bitrate header: " + r13);
        r14 = r4;
        goto L14
    L13:
        r14 = -1;
        goto L14
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
        IcyHeaders r2 = (IcyHeaders) r6;
        if (this.bitrate != r2.bitrate) goto L24;
        if (Util.areEqual(this.genre, r2.genre) == false) goto L24;
        if (Util.areEqual(this.name, r2.name) == false) goto L24;
        if (Util.areEqual(this.url, r2.url) == false) goto L24;
        if (this.isPublic != r2.isPublic) goto L24;
        if (this.metadataInterval != r2.metadataInterval) goto L24;
        return true;
    L24:
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
        int r0 = ((17 * 31) + this.bitrate) * 31;
        int r3 = 0;
        if (this.genre == null) goto L5;
        int r2 = this.genre.hashCode();
    L6:
        int r1 = (r0 + r2) * 31;
        if (this.name == null) goto L9;
        int r4 = this.name.hashCode();
    L10:
        int r5 = (r1 + r4) * 31;
        if (this.url == null) goto L14;
        r3 = this.url.hashCode();
    L14:
        return ((((r5 + r3) * 31) + (this.isPublic ? 1 : 0)) * 31) + this.metadataInterval;
    L9:
        r4 = 0;
        goto L10
    L5:
        r2 = 0;
        goto L6
    }

    @Override // androidx.media3.common.Metadata.Entry
    public void populateMediaMetadata(MediaMetadata.Builder r2) {
        if (this.name == null) goto L6;
        r2.setStation(this.name);
    L6:
        if (this.genre == null) goto L9;
        r2.setGenre(this.genre);
        return;
    }

    public String toString() {
        return "IcyHeaders: name=\"" + this.name + "\", genre=\"" + this.genre + "\", bitrate=" + this.bitrate + ", metadataInterval=" + this.metadataInterval;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel r2, int r3) {
        r2.writeInt(this.bitrate);
        r2.writeString(this.genre);
        r2.writeString(this.name);
        r2.writeString(this.url);
        Util.writeBoolean(r2, this.isPublic);
        r2.writeInt(this.metadataInterval);
    }
}
