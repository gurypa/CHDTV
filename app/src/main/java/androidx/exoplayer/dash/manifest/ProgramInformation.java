package androidx.media3.exoplayer.dash.manifest;

import androidx.media3.common.util.Util;

/* JADX INFO: loaded from: classes.dex */
public final class ProgramInformation {
    public final String copyright;
    public final String lang;
    public final String moreInformationURL;
    public final String source;
    public final String title;

    public ProgramInformation(String r1, String r2, String r3, String r4, String r5) {
        this.title = r1;
        this.source = r2;
        this.copyright = r3;
        this.moreInformationURL = r4;
        this.lang = r5;
    }

    public boolean equals(Object r6) {
        if (this != r6) goto L6;
        return true;
    L6:
        if ((r6 instanceof ProgramInformation) == true) goto L8;
        return false;
    L8:
        ProgramInformation r1 = (ProgramInformation) r6;
        if (Util.areEqual(this.title, r1.title) == false) goto L20;
        if (Util.areEqual(this.source, r1.source) == false) goto L20;
        if (Util.areEqual(this.copyright, r1.copyright) == false) goto L20;
        if (Util.areEqual(this.moreInformationURL, r1.moreInformationURL) == false) goto L20;
        if (Util.areEqual(this.lang, r1.lang) == false) goto L20;
        return true;
    L20:
        return false;
    }

    public int hashCode() {
        int r1 = 17 * 31;
        int r3 = 0;
        if (this.title == null) goto L5;
        int r2 = this.title.hashCode();
    L6:
        int r0 = (r1 + r2) * 31;
        if (this.source == null) goto L9;
        int r4 = this.source.hashCode();
    L10:
        int r5 = (r0 + r4) * 31;
        if (this.copyright == null) goto L13;
        int r6 = this.copyright.hashCode();
    L14:
        int r7 = (r5 + r6) * 31;
        if (this.moreInformationURL == null) goto L17;
        int r8 = this.moreInformationURL.hashCode();
    L18:
        int r9 = (r7 + r8) * 31;
        if (this.lang == null) goto L22;
        r3 = this.lang.hashCode();
    L22:
        return r9 + r3;
    L17:
        r8 = 0;
        goto L18
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
}
