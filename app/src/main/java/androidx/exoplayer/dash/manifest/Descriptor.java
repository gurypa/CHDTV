package androidx.media3.exoplayer.dash.manifest;

import androidx.media3.common.util.Util;

/* JADX INFO: loaded from: classes.dex */
public final class Descriptor {

    /* JADX INFO: renamed from: id */
    public final String f39id;
    public final String schemeIdUri;
    public final String value;

    public Descriptor(String r1, String r2, String r3) {
        this.schemeIdUri = r1;
        this.value = r2;
        this.f39id = r3;
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
        Descriptor r2 = (Descriptor) r6;
        if (Util.areEqual(this.schemeIdUri, r2.schemeIdUri) == false) goto L18;
        if (Util.areEqual(this.value, r2.value) == false) goto L18;
        if (Util.areEqual(this.f39id, r2.f39id) == false) goto L18;
        return true;
    L18:
        return false;
    }

    public int hashCode() {
        int r1 = this.schemeIdUri.hashCode() * 31;
        int r3 = 0;
        if (this.value == null) goto L5;
        int r2 = this.value.hashCode();
    L6:
        int r0 = (r1 + r2) * 31;
        if (this.f39id == null) goto L10;
        r3 = this.f39id.hashCode();
    L10:
        return r0 + r3;
    L5:
        r2 = 0;
        goto L6
    }
}
