package androidx.media3.common;

import android.os.Bundle;
import androidx.media3.common.util.Util;

/* JADX INFO: loaded from: classes.dex */
public abstract class Rating {
    static final String FIELD_RATING_TYPE = null;
    static final int RATING_TYPE_HEART = 0;
    static final int RATING_TYPE_PERCENTAGE = 1;
    static final int RATING_TYPE_STAR = 2;
    static final int RATING_TYPE_THUMB = 3;
    static final int RATING_TYPE_UNSET = -1;
    static final float RATING_UNSET = -1.0f;

    static {
        FIELD_RATING_TYPE = Util.intToStringMaxRadix(0);
    }

    Rating() {
    }

    public static Rating fromBundle(Bundle r4) {
        int r0 = r4.getInt(FIELD_RATING_TYPE, -1);
        switch(r0) {
            case 0: goto L13;
            case 1: goto L11;
            case 2: goto L9;
            case 3: goto L7;
            default: goto L5;
        };
    L5:
        throw new IllegalArgumentException("Unknown RatingType: " + r0);
    L7:
        return ThumbRating.fromBundle(r4);
    L9:
        return StarRating.fromBundle(r4);
    L11:
        return PercentageRating.fromBundle(r4);
    L13:
        return HeartRating.fromBundle(r4);
    }

    public abstract boolean isRated();

    public abstract Bundle toBundle();
}
