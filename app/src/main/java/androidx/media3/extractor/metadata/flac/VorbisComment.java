package androidx.media3.extractor.metadata.flac;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.media3.common.Format;
import androidx.media3.common.MediaMetadata;
import androidx.media3.common.Metadata;
import androidx.media3.common.util.Util;
import com.google.common.base.Ascii;
import com.google.common.primitives.Ints;

/* JADX INFO: loaded from: classes.dex */
@Deprecated
public class VorbisComment implements Metadata.Entry {
    public static final Parcelable.Creator<VorbisComment> CREATOR = null;
    public final String key;
    public final String value;

    static {
        CREATOR = new C04131();
    }

    protected VorbisComment(Parcel r2) {
        this.key = (String) Util.castNonNull(r2.readString());
        this.value = (String) Util.castNonNull(r2.readString());
    }

    public VorbisComment(String r2, String r3) {
        this.key = Ascii.toUpperCase(r2);
        this.value = r3;
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
    L17:
        return false;
    L8:
        if (getClass() != r6.getClass()) goto L17;
        VorbisComment r2 = (VorbisComment) r6;
        if (this.key.equals(r2.key) == false) goto L16;
        if (this.value.equals(r2.value) == false) goto L16;
        return true;
    L16:
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
        return (((17 * 31) + this.key.hashCode()) * 31) + this.value.hashCode();
    }

    @Override // androidx.media3.common.Metadata.Entry
    public void populateMediaMetadata(MediaMetadata.Builder r3) {
        String r0 = this.key;
        switch(r0.hashCode()) {
            case -1935137620: goto L33;
            case -215998278: goto L30;
            case -113312716: goto L27;
            case 62359119: goto L24;
            case 67703139: goto L21;
            case 79833656: goto L18;
            case 428414940: goto L15;
            case 993300766: goto L12;
            case 1746739798: goto L9;
            case 1939198791: goto L6;
            default: goto L35;
        };
    L35:
        byte r1 = -1;
    L36:
        switch(r1) {
            case 0: goto L55;
            case 1: goto L54;
            case 2: goto L53;
            case 3: goto L52;
            case 4: goto L49;
            case 5: goto L46;
            case 6: goto L43;
            case 7: goto L40;
            case 8: goto L39;
            case 9: goto L38;
            default: goto L70;
        };
    L38:
        r3.setDescription(this.value);
        return;
    L39:
        r3.setGenre(this.value);
        return;
    L40:
        Integer r2 = Ints.tryParse(this.value);
        if (r2 == null) goto L59;
        r3.setTotalDiscCount(r2);
        return;
    L59:
        return;
    L43:
        Integer r4 = Ints.tryParse(this.value);
        if (r4 == null) goto L61;
        r3.setDiscNumber(r4);
        return;
    L61:
        return;
    L46:
        Integer r5 = Ints.tryParse(this.value);
        if (r5 == null) goto L63;
        r3.setTotalTrackCount(r5);
        return;
    L63:
        return;
    L49:
        Integer r6 = Ints.tryParse(this.value);
        if (r6 == null) goto L65;
        r3.setTrackNumber(r6);
        return;
    L65:
        return;
    L52:
        r3.setAlbumArtist(this.value);
        return;
    L53:
        r3.setAlbumTitle(this.value);
        return;
    L54:
        r3.setArtist(this.value);
        return;
    L55:
        r3.setTitle(this.value);
        return;
    L70:
        return;
    L6:
        if (r0.equals("ARTIST") == false) goto L35;
        r1 = 1;
        goto L36
    L9:
        if (r0.equals("ALBUMARTIST") == false) goto L35;
        r1 = 3;
        goto L36
    L12:
        if (r0.equals("DISCNUMBER") == false) goto L35;
        r1 = 6;
        goto L36
    L15:
        if (r0.equals("DESCRIPTION") == false) goto L35;
        r1 = 9;
        goto L36
    L18:
        if (r0.equals("TITLE") == false) goto L35;
        r1 = 0;
        goto L36
    L21:
        if (r0.equals("GENRE") == false) goto L35;
        r1 = 8;
        goto L36
    L24:
        if (r0.equals("ALBUM") == false) goto L35;
        r1 = 2;
        goto L36
    L27:
        if (r0.equals("TRACKNUMBER") == false) goto L35;
        r1 = 4;
        goto L36
    L30:
        if (r0.equals("TOTALDISCS") == false) goto L35;
        r1 = 7;
        goto L36
    L33:
        if (r0.equals("TOTALTRACKS") == false) goto L35;
        r1 = 5;
        goto L36
    }

    public String toString() {
        return "VC: " + this.key + "=" + this.value;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel r2, int r3) {
        r2.writeString(this.key);
        r2.writeString(this.value);
    }
}
