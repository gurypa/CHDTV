package androidx.media3.exoplayer.dash.manifest;

import android.net.Uri;
import androidx.media3.common.util.UriUtil;

/* JADX INFO: loaded from: classes.dex */
public final class RangedUri {
    private int hashCode;
    public final long length;
    private final String referenceUri;
    public final long start;

    public RangedUri(String r2, long r3, long r5) {
        if (r2 != null) goto L5;
        String r0 = "";
    L6:
        this.referenceUri = r0;
        this.start = r3;
        this.length = r5;
        return;
    L5:
        r0 = r2;
        goto L6
    }

    public RangedUri attemptMerge(RangedUri r9, String r10) {
        String r1 = resolveUriString(r10);
        if (r9 != null) goto L5;
    L28:
        return null;
    L5:
        if (r1.equals(r9.resolveUriString(r10)) == false) goto L28;
        long r4 = -1;
        if (this.length == (-1)) goto L18;
        if ((this.start + this.length) != r9.start) goto L18;
        long r2 = this.start;
        if (r9.length == (-1)) goto L16;
        r4 = this.length + r9.length;
    L16:
        return new RangedUri(r1, r2, r4);
    L18:
        if (r9.length != (-1)) goto L20;
    L27:
        return null;
    L20:
        if ((r9.start + r9.length) != this.start) goto L27;
        long r3 = r9.start;
        if (this.length == (-1)) goto L26;
        r4 = r9.length + this.length;
    L26:
        return new RangedUri(r1, r3, r4);
    }

    public boolean equals(Object r8) {
        if (this != r8) goto L6;
        return true;
    L6:
        if (r8 != null) goto L8;
    L19:
        return false;
    L8:
        if (getClass() != r8.getClass()) goto L19;
        RangedUri r2 = (RangedUri) r8;
        if (this.start != r2.start) goto L18;
        if (this.length != r2.length) goto L18;
        if (this.referenceUri.equals(r2.referenceUri) == false) goto L18;
        return true;
    L18:
        return false;
    }

    public int hashCode() {
        if (this.hashCode != 0) goto L6;
        this.hashCode = (((((17 * 31) + ((int) this.start)) * 31) + ((int) this.length)) * 31) + this.referenceUri.hashCode();
    L6:
        return this.hashCode;
    }

    public Uri resolveUri(String r2) {
        return UriUtil.resolveToUri(r2, this.referenceUri);
    }

    public String resolveUriString(String r2) {
        return UriUtil.resolve(r2, this.referenceUri);
    }

    public String toString() {
        return "RangedUri(referenceUri=" + this.referenceUri + ", start=" + this.start + ", length=" + this.length + ")";
    }
}
