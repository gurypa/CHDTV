package androidx.media3.common;

import android.os.Bundle;
import android.os.Parcelable;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.BundleCollectionUtil;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.Util;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public final class TrackGroup {
    private static final String FIELD_FORMATS = null;
    private static final String FIELD_ID = null;
    private static final String TAG = "TrackGroup";
    private final Format[] formats;
    private int hashCode;

    /* JADX INFO: renamed from: id */
    public final String f31id;
    public final int length;
    public final int type;

    static {
        FIELD_FORMATS = Util.intToStringMaxRadix(0);
        FIELD_ID = Util.intToStringMaxRadix(1);
    }

    public TrackGroup(String r4, Format... r5) {
        if (r5.length <= 0) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkArgument(r0);
        this.f31id = r4;
        this.formats = r5;
        this.length = r5.length;
        int r1 = MimeTypes.getTrackType(r5[0].sampleMimeType);
        if (r1 != (-1)) goto L9;
        r1 = MimeTypes.getTrackType(r5[0].containerMimeType);
    L9:
        this.type = r1;
        verifyCorrectness();
        return;
    L5:
        r0 = false;
        goto L6
    }

    public TrackGroup(Format... r2) {
        this("", r2);
    }

    public static TrackGroup fromBundle(Bundle r5) {
        ArrayList r0 = r5.getParcelableArrayList(FIELD_FORMATS);
        if (r0 != null) goto L5;
        ImmutableList r1 = ImmutableList.m212of();
    L7:
        return new TrackGroup(r5.getString(FIELD_ID, ""), (Format[]) r1.toArray(new Format[0]));
    L5:
        r1 = BundleCollectionUtil.fromBundleList(new TrackGroup$$ExternalSyntheticLambda0(), r0);
        goto L7
    }

    private static void logErrorMessage(String r3, String r4, String r5, int r6) {
        Log.m45e(TAG, "", new IllegalStateException("Different " + r3 + " combined in one TrackGroup: '" + r4 + "' (track 0) and '" + r5 + "' (track " + r6 + ")"));
    }

    private static String normalizeLanguage(String r1) {
        if (r1 != null) goto L4;
    L7:
        return "";
    L4:
        if (r1.equals(C0180C.LANGUAGE_UNDETERMINED) == true) goto L7;
        return r1;
    }

    private static int normalizeRoleFlags(int r1) {
        return r1 | 16384;
    }

    private void verifyCorrectness() {
        String r0 = normalizeLanguage(this.formats[0].language);
        int r2 = normalizeRoleFlags(this.formats[0].roleFlags);
        int r3 = 1;
    L4:
        if (r3 >= this.formats.length) goto L14;
        if (r0.equals(normalizeLanguage(this.formats[r3].language)) == false) goto L7;
        if (r2 != normalizeRoleFlags(this.formats[r3].roleFlags)) goto L11;
        r3 = r3 + 1;
        goto L4
    L11:
        logErrorMessage("role flags", Integer.toBinaryString(this.formats[0].roleFlags), Integer.toBinaryString(this.formats[r3].roleFlags), r3);
        return;
    L7:
        logErrorMessage("languages", this.formats[0].language, this.formats[r3].language, r3);
        return;
    }

    public TrackGroup copyWithId(String r3) {
        return new TrackGroup(r3, this.formats);
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
        TrackGroup r2 = (TrackGroup) r6;
        if (this.f31id.equals(r2.f31id) == false) goto L16;
        if (Arrays.equals(this.formats, r2.formats) == false) goto L16;
        return true;
    L16:
        return false;
    }

    public Format getFormat(int r2) {
        return this.formats[r2];
    }

    public int hashCode() {
        if (this.hashCode != 0) goto L6;
        this.hashCode = (((17 * 31) + this.f31id.hashCode()) * 31) + Arrays.hashCode(this.formats);
    L6:
        return this.hashCode;
    }

    public int indexOf(Format r3) {
        int r0 = 0;
    L4:
        if (r0 >= this.formats.length) goto L9;
        if (r3 == this.formats[r0]) goto L7;
        r0 = r0 + 1;
        goto L4
    L7:
        return r0;
    L9:
        return -1;
    }

    public Bundle toBundle() {
        Bundle r0 = new Bundle();
        ArrayList<? extends Parcelable> r1 = new ArrayList(this.formats.length);
        Format[] r2 = this.formats;
        int r3 = r2.length;
        int r4 = 0;
    L3:
        if (r4 >= r3) goto L5;
        r1.add(r2[r4].toBundle(true));
        r4 = r4 + 1;
        goto L3
    L5:
        r0.putParcelableArrayList(FIELD_FORMATS, r1);
        r0.putString(FIELD_ID, this.f31id);
        return r0;
    }
}
