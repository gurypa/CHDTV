package android.support.v4.media;

import android.media.Rating;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* JADX INFO: loaded from: classes.dex */
public final class RatingCompat implements Parcelable {
    public static final Parcelable.Creator<RatingCompat> CREATOR = null;
    public static final int RATING_3_STARS = 3;
    public static final int RATING_4_STARS = 4;
    public static final int RATING_5_STARS = 5;
    public static final int RATING_HEART = 1;
    public static final int RATING_NONE = 0;
    private static final float RATING_NOT_RATED = -1.0f;
    public static final int RATING_PERCENTAGE = 6;
    public static final int RATING_THUMB_UP_DOWN = 2;
    private static final String TAG = "Rating";
    private Object mRatingObj;
    private final int mRatingStyle;
    private final float mRatingValue;

    private static class Api19Impl {
        private Api19Impl() {
        }

        static float getPercentRating(Rating r1) {
            return r1.getPercentRating();
        }

        static int getRatingStyle(Rating r1) {
            return r1.getRatingStyle();
        }

        static float getStarRating(Rating r1) {
            return r1.getStarRating();
        }

        static boolean hasHeart(Rating r1) {
            return r1.hasHeart();
        }

        static boolean isRated(Rating r1) {
            return r1.isRated();
        }

        static boolean isThumbUp(Rating r1) {
            return r1.isThumbUp();
        }

        static Rating newHeartRating(boolean r1) {
            return Rating.newHeartRating(r1);
        }

        static Rating newPercentageRating(float r1) {
            return Rating.newPercentageRating(r1);
        }

        static Rating newStarRating(int r1, float r2) {
            return Rating.newStarRating(r1, r2);
        }

        static Rating newThumbRating(boolean r1) {
            return Rating.newThumbRating(r1);
        }

        static Rating newUnratedRating(int r1) {
            return Rating.newUnratedRating(r1);
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface StarStyle {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Style {
    }

    static {
        CREATOR = new C00181();
    }

    RatingCompat(int r1, float r2) {
        this.mRatingStyle = r1;
        this.mRatingValue = r2;
    }

    public static RatingCompat fromRating(Object r3) {
        if (r3 == null) goto L15;
        int r1 = Api19Impl.getRatingStyle((Rating) r3);
        if (Api19Impl.isRated((Rating) r3) == false) goto L12;
        switch(r1) {
            case 1: goto L11;
            case 2: goto L10;
            case 3: goto L9;
            case 4: goto L9;
            case 5: goto L9;
            case 6: goto L8;
            default: goto L7;
        };
    L7:
        return null;
    L8:
        RatingCompat r0 = newPercentageRating(Api19Impl.getPercentRating((Rating) r3));
    L13:
        r0.mRatingObj = r3;
        return r0;
    L9:
        r0 = newStarRating(r1, Api19Impl.getStarRating((Rating) r3));
        goto L13
    L10:
        r0 = newThumbRating(Api19Impl.isThumbUp((Rating) r3));
        goto L13
    L11:
        r0 = newHeartRating(Api19Impl.hasHeart((Rating) r3));
        goto L13
    L12:
        r0 = newUnratedRating(r1);
        goto L13
    L15:
        return null;
    }

    public static RatingCompat newHeartRating(boolean r3) {
        if (r3 == false) goto L5;
        float r1 = 1.0f;
    L7:
        return new RatingCompat(1, r1);
    L5:
        r1 = 0.0f;
        goto L7
    }

    public static RatingCompat newPercentageRating(float r2) {
        if (r2 >= 0.0f) goto L5;
    L9:
        Log.e(TAG, "Invalid percentage-based rating value");
        return null;
    L5:
        if (r2 > 100.0f) goto L9;
        return new RatingCompat(6, r2);
    }

    public static RatingCompat newStarRating(int r5, float r6) {
        switch(r5) {
            case 3: goto L8;
            case 4: goto L7;
            case 5: goto L6;
            default: goto L4;
        };
    L4:
        Log.e(TAG, "Invalid rating style (" + r5 + ") for a star rating");
        return null;
    L6:
        float r0 = 5.0f;
    L10:
        if (r6 >= 0.0f) goto L12;
    L16:
        Log.e(TAG, "Trying to set out of range star-based rating");
        return null;
    L12:
        if (r6 > r0) goto L16;
        return new RatingCompat(r5, r6);
    L7:
        r0 = 4.0f;
        goto L10
    L8:
        r0 = 3.0f;
        goto L10
    }

    public static RatingCompat newThumbRating(boolean r3) {
        if (r3 == false) goto L5;
        float r1 = 1.0f;
    L7:
        return new RatingCompat(2, r1);
    L5:
        r1 = 0.0f;
        goto L7
    }

    public static RatingCompat newUnratedRating(int r2) {
        switch(r2) {
            case 1: goto L6;
            case 2: goto L6;
            case 3: goto L6;
            case 4: goto L6;
            case 5: goto L6;
            case 6: goto L6;
            default: goto L3;
        };
    L3:
        return null;
    L6:
        return new RatingCompat(r2, RATING_NOT_RATED);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return this.mRatingStyle;
    }

    public float getPercentRating() {
        if (this.mRatingStyle == 6) goto L5;
        return RATING_NOT_RATED;
    L5:
        if (isRated() == true) goto L8;
        return RATING_NOT_RATED;
    L8:
        return this.mRatingValue;
    }

    public Object getRating() {
        if (this.mRatingObj != null) goto L16;
        if (isRated() == true) goto L7;
        this.mRatingObj = Api19Impl.newUnratedRating(this.mRatingStyle);
        goto L16
    L7:
        switch(this.mRatingStyle) {
            case 1: goto L13;
            case 2: goto L12;
            case 3: goto L11;
            case 4: goto L11;
            case 5: goto L11;
            case 6: goto L10;
            default: goto L8;
        };
    L8:
        return null;
    L10:
        this.mRatingObj = Api19Impl.newPercentageRating(getPercentRating());
        goto L16
    L11:
        this.mRatingObj = Api19Impl.newStarRating(this.mRatingStyle, getStarRating());
        goto L16
    L12:
        this.mRatingObj = Api19Impl.newThumbRating(isThumbUp());
        goto L16
    L13:
        this.mRatingObj = Api19Impl.newHeartRating(hasHeart());
    L16:
        return this.mRatingObj;
    }

    public int getRatingStyle() {
        return this.mRatingStyle;
    }

    public float getStarRating() {
        switch(this.mRatingStyle) {
            case 3: goto L6;
            case 4: goto L6;
            case 5: goto L6;
            default: goto L11;
        };
    L11:
        return RATING_NOT_RATED;
    L6:
        if (isRated() == true) goto L8;
        return RATING_NOT_RATED;
    L8:
        return this.mRatingValue;
    }

    public boolean hasHeart() {
        if (this.mRatingStyle == 1) goto L6;
        return false;
    L6:
        if (this.mRatingValue == 1.0f) goto L8;
        return false;
    L8:
        return true;
    }

    public boolean isRated() {
        if (this.mRatingValue < 0.0f) goto L5;
        return true;
    L5:
        return false;
    }

    public boolean isThumbUp() {
        if (this.mRatingStyle == 2) goto L6;
        return false;
    L6:
        if (this.mRatingValue != 1.0f) goto L9;
        return true;
    L9:
        return false;
    }

    public String toString() {
        StringBuilder r0 = new StringBuilder().append("Rating:style=").append(this.mRatingStyle).append(" rating=");
        if (this.mRatingValue >= 0.0f) goto L5;
        String r1 = "unrated";
    L7:
        return r0.append(r1).toString();
    L5:
        r1 = String.valueOf(this.mRatingValue);
        goto L7
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel r2, int r3) {
        r2.writeInt(this.mRatingStyle);
        r2.writeFloat(this.mRatingValue);
    }
}
