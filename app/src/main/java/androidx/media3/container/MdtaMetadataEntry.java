package androidx.media3.container;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.media3.common.Format;
import androidx.media3.common.MediaMetadata;
import androidx.media3.common.Metadata;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.Util;
import com.google.common.base.Joiner;
import com.google.common.primitives.Ints;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class MdtaMetadataEntry implements Metadata.Entry {
    public static final Parcelable.Creator<MdtaMetadataEntry> CREATOR = null;
    public static final int DEFAULT_LOCALE_INDICATOR = 0;
    public static final byte EDITABLE_TRACKS_SAMPLES_LOCATION_INTERLEAVED = 1;
    public static final byte EDITABLE_TRACKS_SAMPLES_LOCATION_IN_EDIT_DATA_MP4 = 0;
    public static final String KEY_ANDROID_CAPTURE_FPS = "com.android.capture.fps";
    public static final String KEY_EDITABLE_TRACKS_LENGTH = "editable.tracks.length";
    public static final String KEY_EDITABLE_TRACKS_MAP = "editable.tracks.map";
    public static final String KEY_EDITABLE_TRACKS_OFFSET = "editable.tracks.offset";
    public static final String KEY_EDITABLE_TRACKS_SAMPLES_LOCATION = "editable.tracks.samples.location";
    public static final int TYPE_INDICATOR_8_BIT_UNSIGNED_INT = 75;
    public static final int TYPE_INDICATOR_FLOAT32 = 23;
    public static final int TYPE_INDICATOR_INT32 = 67;
    public static final int TYPE_INDICATOR_RESERVED = 0;
    public static final int TYPE_INDICATOR_STRING = 1;
    public static final int TYPE_INDICATOR_UNSIGNED_INT64 = 78;
    public final String key;
    public final int localeIndicator;
    public final int typeIndicator;
    public final byte[] value;

    static {
        CREATOR = new C02021();
    }

    private MdtaMetadataEntry(Parcel r4) {
        this.key = (String) Util.castNonNull(r4.readString());
        this.value = (byte[]) Util.castNonNull(r4.createByteArray());
        this.localeIndicator = r4.readInt();
        this.typeIndicator = r4.readInt();
        validateData(this.key, this.value, this.typeIndicator);
    }

    /* synthetic */ MdtaMetadataEntry(Parcel r1, C02021 r2) {
        this(r1);
    }

    public MdtaMetadataEntry(String r2, byte[] r3, int r4) {
        this(r2, r3, 0, r4);
    }

    public MdtaMetadataEntry(String r1, byte[] r2, int r3, int r4) {
        validateData(r1, r2, r4);
        this.key = r1;
        this.value = r2;
        this.localeIndicator = r3;
        this.typeIndicator = r4;
    }

    private static String getFormattedValueForEditableTracksMap(List<Integer> r2) {
        StringBuilder r0 = new StringBuilder();
        r0.append("track types = ");
        Joiner.m183on(',').appendTo(r0, r2);
        return r0.toString();
    }

    private static void validateData(String r4, byte[] r5, int r6) {
        boolean r2 = true;
        switch(r4.hashCode()) {
            case -1949883051: goto L18;
            case -1555642602: goto L15;
            case 101820674: goto L12;
            case 188404399: goto L9;
            case 1805012160: goto L6;
            default: goto L20;
        };
    L20:
        byte r0 = -1;
    L21:
        switch(r0) {
            case 0: goto L46;
            case 1: goto L39;
            case 2: goto L39;
            case 3: goto L34;
            case 4: goto L24;
            default: goto L56;
        };
    L34:
        if (r6 == 0) goto L37;
        r2 = false;
    L37:
        Assertions.checkArgument(r2);
        return;
    L56:
        return;
    L24:
        if (r6 == 75) goto L26;
    L32:
        r2 = false;
    L33:
        Assertions.checkArgument(r2);
        return;
    L26:
        if (r5.length != 1) goto L32;
        if (r5[0] == 0) goto L33;
        if (r5[0] != 1) goto L32;
    L39:
        if (r6 == 78) goto L41;
    L43:
        r2 = false;
    L44:
        Assertions.checkArgument(r2);
        return;
    L41:
        if (r5.length != 8) goto L43;
    L46:
        if (r6 == 23) goto L48;
    L50:
        r2 = false;
    L51:
        Assertions.checkArgument(r2);
        return;
    L48:
        if (r5.length != 4) goto L50;
    L6:
        if (r4.equals(KEY_EDITABLE_TRACKS_MAP) == false) goto L20;
        r0 = 3;
        goto L21
    L9:
        if (r4.equals(KEY_EDITABLE_TRACKS_OFFSET) == false) goto L20;
        r0 = 1;
        goto L21
    L12:
        if (r4.equals(KEY_EDITABLE_TRACKS_LENGTH) == false) goto L20;
        r0 = 2;
        goto L21
    L15:
        if (r4.equals(KEY_EDITABLE_TRACKS_SAMPLES_LOCATION) == false) goto L20;
        r0 = 4;
        goto L21
    L18:
        if (r4.equals(KEY_ANDROID_CAPTURE_FPS) == false) goto L20;
        r0 = 0;
        goto L21
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
    L21:
        return false;
    L8:
        if (getClass() != r6.getClass()) goto L21;
        MdtaMetadataEntry r2 = (MdtaMetadataEntry) r6;
        if (this.key.equals(r2.key) == false) goto L20;
        if (Arrays.equals(this.value, r2.value) == false) goto L20;
        if (this.localeIndicator != r2.localeIndicator) goto L20;
        if (this.typeIndicator != r2.typeIndicator) goto L20;
        return true;
    L20:
        return false;
    }

    public List<Integer> getEditableTrackTypesFromMap() {
        Assertions.checkState(this.key.equals(KEY_EDITABLE_TRACKS_MAP), "Metadata is not an editable tracks map");
        byte r0 = this.value[1];
        ArrayList r1 = new ArrayList();
        int r2 = 0;
    L3:
        if (r2 >= r0) goto L5;
        r1.add(Integer.valueOf(this.value[r2 + 2]));
        r2 = r2 + 1;
        goto L3
    L5:
        return r1;
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
        return (((((((17 * 31) + this.key.hashCode()) * 31) + Arrays.hashCode(this.value)) * 31) + this.localeIndicator) * 31) + this.typeIndicator;
    }

    @Override // androidx.media3.common.Metadata.Entry
    public /* synthetic */ void populateMediaMetadata(MediaMetadata.Builder r1) {
        Metadata.Entry.CC.$default$populateMediaMetadata(this, r1);
    }

    public String toString() {
        switch(this.typeIndicator) {
            case 0: goto L11;
            case 1: goto L9;
            case 23: goto L8;
            case 67: goto L7;
            case 75: goto L6;
            case 78: goto L5;
            default: goto L13;
        };
    L5:
        String r0 = String.valueOf(new ParsableByteArray(this.value).readUnsignedLongToLong());
    L15:
        return "mdta: key=" + this.key + ", value=" + r0;
    L6:
        r0 = String.valueOf(MdtaMetadataEntry$$ExternalSyntheticBackport0.m55m(this.value[0]));
        goto L15
    L7:
        r0 = String.valueOf(Ints.fromByteArray(this.value));
        goto L15
    L8:
        r0 = String.valueOf(Float.intBitsToFloat(Ints.fromByteArray(this.value)));
        goto L15
    L9:
        r0 = Util.fromUtf8Bytes(this.value);
    L13:
        r0 = Util.toHexString(this.value);
        goto L15
    L11:
        if (this.key.equals(KEY_EDITABLE_TRACKS_MAP) == false) goto L13;
        r0 = getFormattedValueForEditableTracksMap(getEditableTrackTypesFromMap());
        goto L15
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel r2, int r3) {
        r2.writeString(this.key);
        r2.writeByteArray(this.value);
        r2.writeInt(this.localeIndicator);
        r2.writeInt(this.typeIndicator);
    }
}
