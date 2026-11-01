package androidx.core.content;

import android.content.LocusId;
import android.os.Build;
import androidx.core.util.Preconditions;

/* JADX INFO: loaded from: classes.dex */
public final class LocusIdCompat {
    private final String mId;
    private final LocusId mWrapped;

    private static class Api29Impl {
        private Api29Impl() {
        }

        static LocusId create(String r1) {
            return new LocusId(r1);
        }

        static String getId(LocusId r1) {
            return r1.getId();
        }
    }

    public LocusIdCompat(String r3) {
        this.mId = (String) Preconditions.checkStringNotEmpty(r3, "id cannot be empty");
        if (Build.VERSION.SDK_INT < 29) goto L5;
        this.mWrapped = Api29Impl.create(r3);
        return;
    L5:
        this.mWrapped = null;
    }

    private String getSanitizedId() {
        return this.mId.length() + "_chars";
    }

    public static LocusIdCompat toLocusIdCompat(LocusId r3) {
        Preconditions.checkNotNull(r3, "locusId cannot be null");
        return new LocusIdCompat((String) Preconditions.checkStringNotEmpty(Api29Impl.getId(r3), "id cannot be empty"));
    }

    public boolean equals(Object r5) {
        if (this != r5) goto L6;
        return true;
    L6:
        if (r5 != null) goto L9;
        return false;
    L9:
        if (getClass() == r5.getClass()) goto L11;
        return false;
    L11:
        LocusIdCompat r2 = (LocusIdCompat) r5;
        if (this.mId != null) goto L19;
        if (r2.mId != null) goto L17;
        return true;
    L17:
        return false;
    L19:
        return this.mId.equals(r2.mId);
    }

    public String getId() {
        return this.mId;
    }

    public int hashCode() {
        int r2 = 1 * 31;
        if (this.mId != null) goto L5;
        int r3 = 0;
    L7:
        return r2 + r3;
    L5:
        r3 = this.mId.hashCode();
        goto L7
    }

    public LocusId toLocusId() {
        return this.mWrapped;
    }

    public String toString() {
        return "LocusIdCompat[" + getSanitizedId() + "]";
    }
}
