package androidx.media3.extractor.metadata.emsg;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.media3.common.Format;
import androidx.media3.common.MediaMetadata;
import androidx.media3.common.Metadata;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.Util;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public final class EventMessage implements Metadata.Entry {
    public static final Parcelable.Creator<EventMessage> CREATOR = null;
    private static final Format ID3_FORMAT = null;
    public static final String ID3_SCHEME_ID_AOM = "https://aomedia.org/emsg/ID3";
    private static final String ID3_SCHEME_ID_APPLE = "https://developer.apple.com/streaming/emsg-id3";
    private static final Format SCTE35_FORMAT = null;
    public static final String SCTE35_SCHEME_ID = "urn:scte:scte35:2014:bin";
    public final long durationMs;
    private int hashCode;

    /* JADX INFO: renamed from: id */
    public final long f49id;
    public final byte[] messageData;
    public final String schemeIdUri;
    public final String value;

    static {
        ID3_FORMAT = new Format.Builder().setSampleMimeType(MimeTypes.APPLICATION_ID3).build();
        SCTE35_FORMAT = new Format.Builder().setSampleMimeType(MimeTypes.APPLICATION_SCTE35).build();
        CREATOR = new C04111();
    }

    EventMessage(Parcel r3) {
        this.schemeIdUri = (String) Util.castNonNull(r3.readString());
        this.value = (String) Util.castNonNull(r3.readString());
        this.durationMs = r3.readLong();
        this.f49id = r3.readLong();
        this.messageData = (byte[]) Util.castNonNull(r3.createByteArray());
    }

    public EventMessage(String r1, String r2, long r3, long r5, byte[] r7) {
        this.schemeIdUri = r1;
        this.value = r2;
        this.durationMs = r3;
        this.f49id = r5;
        this.messageData = r7;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object r8) {
        if (this != r8) goto L6;
        return true;
    L6:
        if (r8 != null) goto L8;
    L23:
        return false;
    L8:
        if (getClass() != r8.getClass()) goto L23;
        EventMessage r2 = (EventMessage) r8;
        if (this.durationMs != r2.durationMs) goto L22;
        if (this.f49id != r2.f49id) goto L22;
        if (Util.areEqual(this.schemeIdUri, r2.schemeIdUri) == false) goto L22;
        if (Util.areEqual(this.value, r2.value) == false) goto L22;
        if (Arrays.equals(this.messageData, r2.messageData) == false) goto L22;
        return true;
    L22:
        return false;
    }

    @Override // androidx.media3.common.Metadata.Entry
    public byte[] getWrappedMetadataBytes() {
        if (getWrappedMetadataFormat() != null) goto L7;
        return null;
    L7:
        return this.messageData;
    }

    @Override // androidx.media3.common.Metadata.Entry
    public Format getWrappedMetadataFormat() {
        String r0 = this.schemeIdUri;
        switch(r0.hashCode()) {
            case -1468477611: goto L12;
            case -795945609: goto L9;
            case 1303648457: goto L6;
            default: goto L14;
        };
    L14:
        byte r1 = -1;
    L15:
        switch(r1) {
            case 0: goto L21;
            case 1: goto L21;
            case 2: goto L19;
            default: goto L16;
        };
    L16:
        return null;
    L19:
        return SCTE35_FORMAT;
    L21:
        return ID3_FORMAT;
    L6:
        if (r0.equals(ID3_SCHEME_ID_APPLE) == false) goto L14;
        r1 = 1;
        goto L15
    L9:
        if (r0.equals(ID3_SCHEME_ID_AOM) == false) goto L14;
        r1 = 0;
        goto L15
    L12:
        if (r0.equals(SCTE35_SCHEME_ID) == false) goto L14;
        r1 = 2;
        goto L15
    }

    public int hashCode() {
        if (this.hashCode != 0) goto L13;
        int r1 = 17 * 31;
        int r3 = 0;
        if (this.schemeIdUri == null) goto L7;
        int r2 = this.schemeIdUri.hashCode();
    L8:
        int r0 = (r1 + r2) * 31;
        if (this.value == null) goto L11;
        r3 = this.value.hashCode();
    L11:
        this.hashCode = ((((((r0 + r3) * 31) + ((int) (this.durationMs ^ (this.durationMs >>> 32)))) * 31) + ((int) (this.f49id ^ (this.f49id >>> 32)))) * 31) + Arrays.hashCode(this.messageData);
        goto L13
    L7:
        r2 = 0;
    L13:
        return this.hashCode;
    }

    @Override // androidx.media3.common.Metadata.Entry
    public /* synthetic */ void populateMediaMetadata(MediaMetadata.Builder r1) {
        Metadata.Entry.CC.$default$populateMediaMetadata(this, r1);
    }

    public String toString() {
        return "EMSG: scheme=" + this.schemeIdUri + ", id=" + this.f49id + ", durationMs=" + this.durationMs + ", value=" + this.value;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel r3, int r4) {
        r3.writeString(this.schemeIdUri);
        r3.writeString(this.value);
        r3.writeLong(this.durationMs);
        r3.writeLong(this.f49id);
        r3.writeByteArray(this.messageData);
    }
}
