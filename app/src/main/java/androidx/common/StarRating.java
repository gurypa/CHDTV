package androidx.media3.common;

import android.os.Bundle;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import com.google.common.base.Objects;

/* JADX INFO: loaded from: classes.dex */
public final class StarRating extends Rating {
    private static final String FIELD_MAX_STARS = null;
    private static final String FIELD_STAR_RATING = null;
    private static final int MAX_STARS_DEFAULT = 5;
    private static final int TYPE = 2;
    private final int maxStars;
    private final float starRating;

    static {
        FIELD_MAX_STARS = Util.intToStringMaxRadix(1);
        FIELD_STAR_RATING = Util.intToStringMaxRadix(2);
    }

    public StarRating(int r3) {
        if (r3 <= 0) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkArgument(r0, "maxStars must be a positive integer");
        this.maxStars = r3;
        this.starRating = -1.0f;
        return;
    L5:
        r0 = false;
        goto L6
    }

    public StarRating(int r5, float r6) {
        boolean r0 = true;
        if (r5 <= 0) goto L5;
        boolean r2 = true;
    L6:
        Assertions.checkArgument(r2, "maxStars must be a positive integer");
        if (r6 >= 0.0f) goto L9;
    L11:
        r0 = false;
    L12:
        Assertions.checkArgument(r0, "starRating is out of range [0, maxStars]");
        this.maxStars = r5;
        this.starRating = r6;
        return;
    L9:
        if (r6 > r5) goto L11;
    L5:
        r2 = false;
        goto L6
    }

    public static StarRating fromBundle(Bundle r3) {
        if (r3.getInt(FIELD_RATING_TYPE, -1) != 2) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkArgument(r0);
        int r1 = r3.getInt(FIELD_MAX_STARS, 5);
        float r2 = r3.getFloat(FIELD_STAR_RATING, -1.0f);
        if (r2 != (-1.0f)) goto L10;
        return new StarRating(r1);
    L10:
        return new StarRating(r1, r2);
    L5:
        r0 = false;
        goto L6
    }

    public boolean equals(Object r5) {
        if ((r5 instanceof StarRating) == true) goto L5;
        return false;
    L5:
        StarRating r0 = (StarRating) r5;
        if (this.maxStars == r0.maxStars) goto L8;
        return false;
    L8:
        if (this.starRating != r0.starRating) goto L12;
        return true;
    L12:
        return false;
    }

    public int getMaxStars() {
        return this.maxStars;
    }

    public float getStarRating() {
        return this.starRating;
    }

    public int hashCode() {
        return Objects.hashCode(new Object[]{Integer.valueOf(this.maxStars), Float.valueOf(this.starRating)});
    }

    @Override // androidx.media3.common.Rating
    public boolean isRated() {
        if (this.starRating == (-1.0f)) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // androidx.media3.common.Rating
    public Bundle toBundle() {
        Bundle r0 = new Bundle();
        r0.putInt(FIELD_RATING_TYPE, 2);
        r0.putInt(FIELD_MAX_STARS, this.maxStars);
        r0.putFloat(FIELD_STAR_RATING, this.starRating);
        return r0;
    }
}
