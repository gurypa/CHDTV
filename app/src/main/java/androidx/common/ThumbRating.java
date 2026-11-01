package androidx.media3.common;

import android.os.Bundle;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import com.google.common.base.Objects;

/* JADX INFO: loaded from: classes.dex */
public final class ThumbRating extends Rating {
    private static final String FIELD_IS_THUMBS_UP = null;
    private static final String FIELD_RATED = null;
    private static final int TYPE = 3;
    private final boolean isThumbsUp;
    private final boolean rated;

    static {
        FIELD_RATED = Util.intToStringMaxRadix(1);
        FIELD_IS_THUMBS_UP = Util.intToStringMaxRadix(2);
    }

    public ThumbRating() {
        this.rated = false;
        this.isThumbsUp = false;
    }

    public ThumbRating(boolean r2) {
        this.rated = true;
        this.isThumbsUp = r2;
    }

    public static ThumbRating fromBundle(Bundle r4) {
        if (r4.getInt(FIELD_RATING_TYPE, -1) != 3) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkArgument(r0);
        if (r4.getBoolean(FIELD_RATED, false) == false) goto L10;
        return new ThumbRating(r4.getBoolean(FIELD_IS_THUMBS_UP, false));
    L10:
        return new ThumbRating();
    L5:
        r0 = false;
        goto L6
    }

    public boolean equals(Object r5) {
        if ((r5 instanceof ThumbRating) == true) goto L5;
        return false;
    L5:
        ThumbRating r0 = (ThumbRating) r5;
        if (this.isThumbsUp == r0.isThumbsUp) goto L8;
        return false;
    L8:
        if (this.rated != r0.rated) goto L12;
        return true;
    L12:
        return false;
    }

    public int hashCode() {
        return Objects.hashCode(new Object[]{Boolean.valueOf(this.rated), Boolean.valueOf(this.isThumbsUp)});
    }

    @Override // androidx.media3.common.Rating
    public boolean isRated() {
        return this.rated;
    }

    public boolean isThumbsUp() {
        return this.isThumbsUp;
    }

    @Override // androidx.media3.common.Rating
    public Bundle toBundle() {
        Bundle r0 = new Bundle();
        r0.putInt(FIELD_RATING_TYPE, 3);
        r0.putBoolean(FIELD_RATED, this.rated);
        r0.putBoolean(FIELD_IS_THUMBS_UP, this.isThumbsUp);
        return r0;
    }
}
