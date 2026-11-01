package androidx.media3.common;

import android.os.Bundle;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;

/* JADX INFO: loaded from: classes.dex */
public class Label {
    private static final String FIELD_LANGUAGE_INDEX = null;
    private static final String FIELD_VALUE_INDEX = null;
    public final String language;
    public final String value;

    static {
        FIELD_LANGUAGE_INDEX = Util.intToStringMaxRadix(0);
        FIELD_VALUE_INDEX = Util.intToStringMaxRadix(1);
    }

    public Label(String r2, String r3) {
        this.language = Util.normalizeLanguageCode(r2);
        this.value = r3;
    }

    public static Label fromBundle(Bundle r3) {
        return new Label(r3.getString(FIELD_LANGUAGE_INDEX), (String) Assertions.checkNotNull(r3.getString(FIELD_VALUE_INDEX)));
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
        Label r2 = (Label) r6;
        if (Util.areEqual(this.language, r2.language) == false) goto L16;
        if (Util.areEqual(this.value, r2.value) == false) goto L16;
        return true;
    L16:
        return false;
    }

    public int hashCode() {
        int r1 = this.value.hashCode() * 31;
        if (this.language == null) goto L5;
        int r2 = this.language.hashCode();
    L7:
        return r1 + r2;
    L5:
        r2 = 0;
        goto L7
    }

    public Bundle toBundle() {
        Bundle r0 = new Bundle();
        if (this.language == null) goto L5;
        r0.putString(FIELD_LANGUAGE_INDEX, this.language);
    L5:
        r0.putString(FIELD_VALUE_INDEX, this.value);
        return r0;
    }
}
