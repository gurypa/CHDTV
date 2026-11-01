package android.support.v4.media;

import android.graphics.Bitmap;
import android.media.MediaDescription;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.media.session.MediaSessionCompat;
import android.text.TextUtils;

/* JADX INFO: loaded from: classes.dex */
public final class MediaDescriptionCompat implements Parcelable {
    public static final long BT_FOLDER_TYPE_ALBUMS = 2;
    public static final long BT_FOLDER_TYPE_ARTISTS = 3;
    public static final long BT_FOLDER_TYPE_GENRES = 4;
    public static final long BT_FOLDER_TYPE_MIXED = 0;
    public static final long BT_FOLDER_TYPE_PLAYLISTS = 5;
    public static final long BT_FOLDER_TYPE_TITLES = 1;
    public static final long BT_FOLDER_TYPE_YEARS = 6;
    public static final Parcelable.Creator<MediaDescriptionCompat> CREATOR = null;
    public static final String DESCRIPTION_KEY_MEDIA_URI = "android.support.v4.media.description.MEDIA_URI";
    public static final String DESCRIPTION_KEY_NULL_BUNDLE_FLAG = "android.support.v4.media.description.NULL_BUNDLE_FLAG";
    public static final String EXTRA_BT_FOLDER_TYPE = "android.media.extra.BT_FOLDER_TYPE";
    public static final String EXTRA_DOWNLOAD_STATUS = "android.media.extra.DOWNLOAD_STATUS";
    public static final long STATUS_DOWNLOADED = 2;
    public static final long STATUS_DOWNLOADING = 1;
    public static final long STATUS_NOT_DOWNLOADED = 0;
    private static final String TAG = "MediaDescriptionCompat";
    private final CharSequence mDescription;
    private MediaDescription mDescriptionFwk;
    private final Bundle mExtras;
    private final Bitmap mIcon;
    private final Uri mIconUri;
    private final String mMediaId;
    private final Uri mMediaUri;
    private final CharSequence mSubtitle;
    private final CharSequence mTitle;

    private static class Api21Impl {
        private Api21Impl() {
        }

        static MediaDescription build(MediaDescription.Builder r1) {
            return r1.build();
        }

        static MediaDescription.Builder createBuilder() {
            return new MediaDescription.Builder();
        }

        static CharSequence getDescription(MediaDescription r1) {
            return r1.getDescription();
        }

        static Bundle getExtras(MediaDescription r1) {
            return r1.getExtras();
        }

        static Bitmap getIconBitmap(MediaDescription r1) {
            return r1.getIconBitmap();
        }

        static Uri getIconUri(MediaDescription r1) {
            return r1.getIconUri();
        }

        static String getMediaId(MediaDescription r1) {
            return r1.getMediaId();
        }

        static CharSequence getSubtitle(MediaDescription r1) {
            return r1.getSubtitle();
        }

        static CharSequence getTitle(MediaDescription r1) {
            return r1.getTitle();
        }

        static void setDescription(MediaDescription.Builder r0, CharSequence r1) {
            r0.setDescription(r1);
        }

        static void setExtras(MediaDescription.Builder r0, Bundle r1) {
            r0.setExtras(r1);
        }

        static void setIconBitmap(MediaDescription.Builder r0, Bitmap r1) {
            r0.setIconBitmap(r1);
        }

        static void setIconUri(MediaDescription.Builder r0, Uri r1) {
            r0.setIconUri(r1);
        }

        static void setMediaId(MediaDescription.Builder r0, String r1) {
            r0.setMediaId(r1);
        }

        static void setSubtitle(MediaDescription.Builder r0, CharSequence r1) {
            r0.setSubtitle(r1);
        }

        static void setTitle(MediaDescription.Builder r0, CharSequence r1) {
            r0.setTitle(r1);
        }
    }

    private static class Api23Impl {
        private Api23Impl() {
        }

        static Uri getMediaUri(MediaDescription r1) {
            return r1.getMediaUri();
        }

        static void setMediaUri(MediaDescription.Builder r0, Uri r1) {
            r0.setMediaUri(r1);
        }
    }

    public static final class Builder {
        private CharSequence mDescription;
        private Bundle mExtras;
        private Bitmap mIcon;
        private Uri mIconUri;
        private String mMediaId;
        private Uri mMediaUri;
        private CharSequence mSubtitle;
        private CharSequence mTitle;

        public Builder() {
        }

        public MediaDescriptionCompat build() {
            return new MediaDescriptionCompat(this.mMediaId, this.mTitle, this.mSubtitle, this.mDescription, this.mIcon, this.mIconUri, this.mExtras, this.mMediaUri);
        }

        public Builder setDescription(CharSequence r1) {
            this.mDescription = r1;
            return this;
        }

        public Builder setExtras(Bundle r1) {
            this.mExtras = r1;
            return this;
        }

        public Builder setIconBitmap(Bitmap r1) {
            this.mIcon = r1;
            return this;
        }

        public Builder setIconUri(Uri r1) {
            this.mIconUri = r1;
            return this;
        }

        public Builder setMediaId(String r1) {
            this.mMediaId = r1;
            return this;
        }

        public Builder setMediaUri(Uri r1) {
            this.mMediaUri = r1;
            return this;
        }

        public Builder setSubtitle(CharSequence r1) {
            this.mSubtitle = r1;
            return this;
        }

        public Builder setTitle(CharSequence r1) {
            this.mTitle = r1;
            return this;
        }
    }

    static {
        CREATOR = new C00161();
    }

    MediaDescriptionCompat(Parcel r3) {
        this.mMediaId = r3.readString();
        this.mTitle = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(r3);
        this.mSubtitle = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(r3);
        this.mDescription = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(r3);
        ClassLoader r0 = getClass().getClassLoader();
        this.mIcon = (Bitmap) r3.readParcelable(r0);
        this.mIconUri = (Uri) r3.readParcelable(r0);
        this.mExtras = r3.readBundle(r0);
        this.mMediaUri = (Uri) r3.readParcelable(r0);
    }

    MediaDescriptionCompat(String r1, CharSequence r2, CharSequence r3, CharSequence r4, Bitmap r5, Uri r6, Bundle r7, Uri r8) {
        this.mMediaId = r1;
        this.mTitle = r2;
        this.mSubtitle = r3;
        this.mDescription = r4;
        this.mIcon = r5;
        this.mIconUri = r6;
        this.mExtras = r7;
        this.mMediaUri = r8;
    }

    public static MediaDescriptionCompat fromMediaDescription(Object r8) {
        if (r8 == null) goto L22;
        Builder r0 = new Builder();
        MediaDescription r1 = (MediaDescription) r8;
        r0.setMediaId(Api21Impl.getMediaId(r1));
        r0.setTitle(Api21Impl.getTitle(r1));
        r0.setSubtitle(Api21Impl.getSubtitle(r1));
        r0.setDescription(Api21Impl.getDescription(r1));
        r0.setIconBitmap(Api21Impl.getIconBitmap(r1));
        r0.setIconUri(Api21Impl.getIconUri(r1));
        Bundle r2 = Api21Impl.getExtras(r1);
        if (r2 == null) goto L6;
        r2 = MediaSessionCompat.unparcelWithClassLoader(r2);
    L6:
        Uri r3 = null;
        if (r2 == null) goto L9;
        r3 = (Uri) r2.getParcelable(DESCRIPTION_KEY_MEDIA_URI);
    L9:
        if (r3 != null) goto L11;
    L16:
        r0.setExtras(r2);
        if (r3 == null) goto L19;
        r0.setMediaUri(r3);
    L20:
        MediaDescriptionCompat r4 = r0.build();
        r4.mDescriptionFwk = r1;
        return r4;
    L19:
        r0.setMediaUri(Api23Impl.getMediaUri(r1));
        goto L20
    L11:
        if (r2.containsKey(DESCRIPTION_KEY_NULL_BUNDLE_FLAG) == true) goto L13;
    L15:
        r2.remove(DESCRIPTION_KEY_MEDIA_URI);
        r2.remove(DESCRIPTION_KEY_NULL_BUNDLE_FLAG);
        goto L16
    L13:
        if (r2.size() != 2) goto L15;
        r2 = null;
        goto L16
    L22:
        return null;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public CharSequence getDescription() {
        return this.mDescription;
    }

    public Bundle getExtras() {
        return this.mExtras;
    }

    public Bitmap getIconBitmap() {
        return this.mIcon;
    }

    public Uri getIconUri() {
        return this.mIconUri;
    }

    public Object getMediaDescription() {
        if (this.mDescriptionFwk != null) goto L7;
        MediaDescription.Builder r0 = Api21Impl.createBuilder();
        Api21Impl.setMediaId(r0, this.mMediaId);
        Api21Impl.setTitle(r0, this.mTitle);
        Api21Impl.setSubtitle(r0, this.mSubtitle);
        Api21Impl.setDescription(r0, this.mDescription);
        Api21Impl.setIconBitmap(r0, this.mIcon);
        Api21Impl.setIconUri(r0, this.mIconUri);
        Api21Impl.setExtras(r0, this.mExtras);
        Api23Impl.setMediaUri(r0, this.mMediaUri);
        this.mDescriptionFwk = Api21Impl.build(r0);
        return this.mDescriptionFwk;
    L7:
        return this.mDescriptionFwk;
    }

    public String getMediaId() {
        return this.mMediaId;
    }

    public Uri getMediaUri() {
        return this.mMediaUri;
    }

    public CharSequence getSubtitle() {
        return this.mSubtitle;
    }

    public CharSequence getTitle() {
        return this.mTitle;
    }

    public String toString() {
        return this.mTitle + ", " + this.mSubtitle + ", " + this.mDescription;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel r2, int r3) {
        ((MediaDescription) getMediaDescription()).writeToParcel(r2, r3);
    }
}
