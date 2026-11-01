package android.support.v4.media;

import android.graphics.Bitmap;
import android.media.MediaMetadata;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.media.session.MediaSessionCompat;
import android.text.TextUtils;
import android.util.Log;
import androidx.collection.ArrayMap;
import java.util.Iterator;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public final class MediaMetadataCompat implements Parcelable {
    public static final Parcelable.Creator<MediaMetadataCompat> CREATOR = null;
    static final ArrayMap<String, Integer> METADATA_KEYS_TYPE = null;
    public static final String METADATA_KEY_ADVERTISEMENT = "android.media.metadata.ADVERTISEMENT";
    public static final String METADATA_KEY_ALBUM = "android.media.metadata.ALBUM";
    public static final String METADATA_KEY_ALBUM_ART = "android.media.metadata.ALBUM_ART";
    public static final String METADATA_KEY_ALBUM_ARTIST = "android.media.metadata.ALBUM_ARTIST";
    public static final String METADATA_KEY_ALBUM_ART_URI = "android.media.metadata.ALBUM_ART_URI";
    public static final String METADATA_KEY_ART = "android.media.metadata.ART";
    public static final String METADATA_KEY_ARTIST = "android.media.metadata.ARTIST";
    public static final String METADATA_KEY_ART_URI = "android.media.metadata.ART_URI";
    public static final String METADATA_KEY_AUTHOR = "android.media.metadata.AUTHOR";
    public static final String METADATA_KEY_BT_FOLDER_TYPE = "android.media.metadata.BT_FOLDER_TYPE";
    public static final String METADATA_KEY_COMPILATION = "android.media.metadata.COMPILATION";
    public static final String METADATA_KEY_COMPOSER = "android.media.metadata.COMPOSER";
    public static final String METADATA_KEY_DATE = "android.media.metadata.DATE";
    public static final String METADATA_KEY_DISC_NUMBER = "android.media.metadata.DISC_NUMBER";
    public static final String METADATA_KEY_DISPLAY_DESCRIPTION = "android.media.metadata.DISPLAY_DESCRIPTION";
    public static final String METADATA_KEY_DISPLAY_ICON = "android.media.metadata.DISPLAY_ICON";
    public static final String METADATA_KEY_DISPLAY_ICON_URI = "android.media.metadata.DISPLAY_ICON_URI";
    public static final String METADATA_KEY_DISPLAY_SUBTITLE = "android.media.metadata.DISPLAY_SUBTITLE";
    public static final String METADATA_KEY_DISPLAY_TITLE = "android.media.metadata.DISPLAY_TITLE";
    public static final String METADATA_KEY_DOWNLOAD_STATUS = "android.media.metadata.DOWNLOAD_STATUS";
    public static final String METADATA_KEY_DURATION = "android.media.metadata.DURATION";
    public static final String METADATA_KEY_GENRE = "android.media.metadata.GENRE";
    public static final String METADATA_KEY_MEDIA_ID = "android.media.metadata.MEDIA_ID";
    public static final String METADATA_KEY_MEDIA_URI = "android.media.metadata.MEDIA_URI";
    public static final String METADATA_KEY_NUM_TRACKS = "android.media.metadata.NUM_TRACKS";
    public static final String METADATA_KEY_RATING = "android.media.metadata.RATING";
    public static final String METADATA_KEY_TITLE = "android.media.metadata.TITLE";
    public static final String METADATA_KEY_TRACK_NUMBER = "android.media.metadata.TRACK_NUMBER";
    public static final String METADATA_KEY_USER_RATING = "android.media.metadata.USER_RATING";
    public static final String METADATA_KEY_WRITER = "android.media.metadata.WRITER";
    public static final String METADATA_KEY_YEAR = "android.media.metadata.YEAR";
    static final int METADATA_TYPE_BITMAP = 2;
    static final int METADATA_TYPE_LONG = 0;
    static final int METADATA_TYPE_RATING = 3;
    static final int METADATA_TYPE_TEXT = 1;
    private static final String[] PREFERRED_BITMAP_ORDER = null;
    private static final String[] PREFERRED_DESCRIPTION_ORDER = null;
    private static final String[] PREFERRED_URI_ORDER = null;
    private static final String TAG = "MediaMetadata";
    final Bundle mBundle;
    private MediaDescriptionCompat mDescription;
    private MediaMetadata mMetadataFwk;

    public static final class Builder {
        private final Bundle mBundle;

        public Builder() {
            this.mBundle = new Bundle();
        }

        public Builder(MediaMetadataCompat r3) {
            this.mBundle = new Bundle(r3.mBundle);
            MediaSessionCompat.ensureClassLoader(this.mBundle);
        }

        public Builder(MediaMetadataCompat r6, int r7) {
            this(r6);
            Iterator<String> r0 = this.mBundle.keySet().iterator();
        L4:
            if (r0.hasNext() == false) goto L13;
            String r1 = r0.next();
            Object r2 = this.mBundle.get(r1);
            if ((r2 instanceof Bitmap) == false) goto L4;
            Bitmap r3 = (Bitmap) r2;
            if (r3.getHeight() > r7) goto L11;
            if (r3.getWidth() <= r7) goto L4;
        L11:
            putBitmap(r1, scaleBitmap(r3, r7));
            goto L4
        }

        private Bitmap scaleBitmap(Bitmap r8, int r9) {
            float r0 = r9;
            float r3 = Math.min(r0 / r8.getWidth(), r0 / r8.getHeight());
            return Bitmap.createScaledBitmap(r8, (int) (r8.getWidth() * r3), (int) (r8.getHeight() * r3), true);
        }

        public MediaMetadataCompat build() {
            return new MediaMetadataCompat(this.mBundle);
        }

        public Builder putBitmap(String r4, Bitmap r5) {
            if (MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(r4) == true) goto L5;
        L9:
            this.mBundle.putParcelable(r4, r5);
            return this;
        L5:
            if (MediaMetadataCompat.METADATA_KEYS_TYPE.get(r4).intValue() == 2) goto L9;
            throw new IllegalArgumentException("The " + r4 + " key cannot be used to put a Bitmap");
        }

        public Builder putLong(String r4, long r5) {
            if (MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(r4) == true) goto L5;
        L9:
            this.mBundle.putLong(r4, r5);
            return this;
        L5:
            if (MediaMetadataCompat.METADATA_KEYS_TYPE.get(r4).intValue() == 0) goto L9;
            throw new IllegalArgumentException("The " + r4 + " key cannot be used to put a long");
        }

        public Builder putRating(String r4, RatingCompat r5) {
            if (MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(r4) == true) goto L5;
        L9:
            this.mBundle.putParcelable(r4, (Parcelable) r5.getRating());
            return this;
        L5:
            if (MediaMetadataCompat.METADATA_KEYS_TYPE.get(r4).intValue() == 3) goto L9;
            throw new IllegalArgumentException("The " + r4 + " key cannot be used to put a Rating");
        }

        public Builder putString(String r4, String r5) {
            if (MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(r4) == true) goto L5;
        L9:
            this.mBundle.putCharSequence(r4, r5);
            return this;
        L5:
            if (MediaMetadataCompat.METADATA_KEYS_TYPE.get(r4).intValue() == 1) goto L9;
            throw new IllegalArgumentException("The " + r4 + " key cannot be used to put a String");
        }

        public Builder putText(String r4, CharSequence r5) {
            if (MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(r4) == true) goto L5;
        L9:
            this.mBundle.putCharSequence(r4, r5);
            return this;
        L5:
            if (MediaMetadataCompat.METADATA_KEYS_TYPE.get(r4).intValue() == 1) goto L9;
            throw new IllegalArgumentException("The " + r4 + " key cannot be used to put a CharSequence");
        }
    }

    static {
        METADATA_KEYS_TYPE = new ArrayMap();
        METADATA_KEYS_TYPE.put(METADATA_KEY_TITLE, 1);
        METADATA_KEYS_TYPE.put(METADATA_KEY_ARTIST, 1);
        METADATA_KEYS_TYPE.put(METADATA_KEY_DURATION, 0);
        METADATA_KEYS_TYPE.put(METADATA_KEY_ALBUM, 1);
        METADATA_KEYS_TYPE.put(METADATA_KEY_AUTHOR, 1);
        METADATA_KEYS_TYPE.put(METADATA_KEY_WRITER, 1);
        METADATA_KEYS_TYPE.put(METADATA_KEY_COMPOSER, 1);
        METADATA_KEYS_TYPE.put(METADATA_KEY_COMPILATION, 1);
        METADATA_KEYS_TYPE.put(METADATA_KEY_DATE, 1);
        METADATA_KEYS_TYPE.put(METADATA_KEY_YEAR, 0);
        METADATA_KEYS_TYPE.put(METADATA_KEY_GENRE, 1);
        METADATA_KEYS_TYPE.put(METADATA_KEY_TRACK_NUMBER, 0);
        METADATA_KEYS_TYPE.put(METADATA_KEY_NUM_TRACKS, 0);
        METADATA_KEYS_TYPE.put(METADATA_KEY_DISC_NUMBER, 0);
        METADATA_KEYS_TYPE.put(METADATA_KEY_ALBUM_ARTIST, 1);
        METADATA_KEYS_TYPE.put(METADATA_KEY_ART, 2);
        METADATA_KEYS_TYPE.put(METADATA_KEY_ART_URI, 1);
        METADATA_KEYS_TYPE.put(METADATA_KEY_ALBUM_ART, 2);
        METADATA_KEYS_TYPE.put(METADATA_KEY_ALBUM_ART_URI, 1);
        METADATA_KEYS_TYPE.put(METADATA_KEY_USER_RATING, 3);
        METADATA_KEYS_TYPE.put(METADATA_KEY_RATING, 3);
        METADATA_KEYS_TYPE.put(METADATA_KEY_DISPLAY_TITLE, 1);
        METADATA_KEYS_TYPE.put(METADATA_KEY_DISPLAY_SUBTITLE, 1);
        METADATA_KEYS_TYPE.put(METADATA_KEY_DISPLAY_DESCRIPTION, 1);
        METADATA_KEYS_TYPE.put(METADATA_KEY_DISPLAY_ICON, 2);
        METADATA_KEYS_TYPE.put(METADATA_KEY_DISPLAY_ICON_URI, 1);
        METADATA_KEYS_TYPE.put(METADATA_KEY_MEDIA_ID, 1);
        METADATA_KEYS_TYPE.put(METADATA_KEY_BT_FOLDER_TYPE, 0);
        METADATA_KEYS_TYPE.put(METADATA_KEY_MEDIA_URI, 1);
        METADATA_KEYS_TYPE.put("android.media.metadata.ADVERTISEMENT", 0);
        METADATA_KEYS_TYPE.put(METADATA_KEY_DOWNLOAD_STATUS, 0);
        PREFERRED_DESCRIPTION_ORDER = new String[]{METADATA_KEY_TITLE, METADATA_KEY_ARTIST, METADATA_KEY_ALBUM, METADATA_KEY_ALBUM_ARTIST, METADATA_KEY_WRITER, METADATA_KEY_AUTHOR, METADATA_KEY_COMPOSER};
        PREFERRED_BITMAP_ORDER = new String[]{METADATA_KEY_DISPLAY_ICON, METADATA_KEY_ART, METADATA_KEY_ALBUM_ART};
        PREFERRED_URI_ORDER = new String[]{METADATA_KEY_DISPLAY_ICON_URI, METADATA_KEY_ART_URI, METADATA_KEY_ALBUM_ART_URI};
        CREATOR = new C00171();
    }

    MediaMetadataCompat(Bundle r2) {
        this.mBundle = new Bundle(r2);
        MediaSessionCompat.ensureClassLoader(this.mBundle);
    }

    MediaMetadataCompat(Parcel r2) {
        this.mBundle = r2.readBundle(MediaSessionCompat.class.getClassLoader());
    }

    public static MediaMetadataCompat fromMediaMetadata(Object r3) {
        if (r3 == null) goto L5;
        Parcel r0 = Parcel.obtain();
        ((MediaMetadata) r3).writeToParcel(r0, 0);
        r0.setDataPosition(0);
        MediaMetadataCompat r1 = CREATOR.createFromParcel(r0);
        r0.recycle();
        r1.mMetadataFwk = (MediaMetadata) r3;
        return r1;
    L5:
        return null;
    }

    public boolean containsKey(String r2) {
        return this.mBundle.containsKey(r2);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public Bitmap getBitmap(String r5) {
        return (Bitmap) this.mBundle.getParcelable(r5);
    L5:
        e = move-exception;
        Log.w(TAG, "Failed to retrieve a key as Bitmap.", e);
        return null;
    }

    public Bundle getBundle() {
        return new Bundle(this.mBundle);
    }

    public MediaDescriptionCompat getDescription() {
        if (this.mDescription != null) goto L5;
        String r0 = getString(METADATA_KEY_MEDIA_ID);
        CharSequence[] r1 = new CharSequence[3];
        Bitmap r2 = null;
        Uri r3 = null;
        CharSequence r4 = getText(METADATA_KEY_DISPLAY_TITLE);
        if (TextUtils.isEmpty(r4) == true) goto L9;
        r1[0] = r4;
        r1[1] = getText(METADATA_KEY_DISPLAY_SUBTITLE);
        r1[2] = getText(METADATA_KEY_DISPLAY_DESCRIPTION);
    L18:
        int r5 = 0;
    L20:
        if (r5 >= PREFERRED_BITMAP_ORDER.length) goto L25;
        Bitmap r9 = getBitmap(PREFERRED_BITMAP_ORDER[r5]);
        if (r9 != null) goto L23;
        r5 = r5 + 1;
        goto L20
    L23:
        r2 = r9;
    L25:
        int r6 = 0;
    L27:
        if (r6 >= PREFERRED_URI_ORDER.length) goto L32;
        String r10 = getString(PREFERRED_URI_ORDER[r6]);
        if (TextUtils.isEmpty(r10) == false) goto L30;
        r6 = r6 + 1;
        goto L27
    L30:
        r3 = Uri.parse(r10);
    L32:
        Uri r7 = null;
        String r11 = getString(METADATA_KEY_MEDIA_URI);
        if (TextUtils.isEmpty(r11) == true) goto L35;
        r7 = Uri.parse(r11);
    L35:
        MediaDescriptionCompat.Builder r12 = new MediaDescriptionCompat.Builder();
        r12.setMediaId(r0);
        r12.setTitle(r1[0]);
        r12.setSubtitle(r1[1]);
        r12.setDescription(r1[2]);
        r12.setIconBitmap(r2);
        r12.setIconUri(r3);
        r12.setMediaUri(r7);
        Bundle r8 = new Bundle();
        if (this.mBundle.containsKey(METADATA_KEY_BT_FOLDER_TYPE) == false) goto L39;
        r8.putLong(MediaDescriptionCompat.EXTRA_BT_FOLDER_TYPE, getLong(METADATA_KEY_BT_FOLDER_TYPE));
    L39:
        if (this.mBundle.containsKey(METADATA_KEY_DOWNLOAD_STATUS) == false) goto L42;
        r8.putLong(MediaDescriptionCompat.EXTRA_DOWNLOAD_STATUS, getLong(METADATA_KEY_DOWNLOAD_STATUS));
    L42:
        if (r8.isEmpty() == true) goto L44;
        r12.setExtras(r8);
    L44:
        this.mDescription = r12.build();
        return this.mDescription;
    L9:
        int r13 = 0;
        int r14 = 0;
    L11:
        if (r13 >= r1.length) goto L18;
        if (r14 >= PREFERRED_DESCRIPTION_ORDER.length) goto L18;
        int r15 = r14 + 1;
        CharSequence r16 = getText(PREFERRED_DESCRIPTION_ORDER[r14]);
        if (TextUtils.isEmpty(r16) == true) goto L17;
        r1[r13] = r16;
        r13 = r13 + 1;
    L17:
        r14 = r15;
        goto L11
    L5:
        return this.mDescription;
    }

    public long getLong(String r4) {
        return this.mBundle.getLong(r4, 0);
    }

    public Object getMediaMetadata() {
        if (this.mMetadataFwk != null) goto L6;
        Parcel r0 = Parcel.obtain();
        writeToParcel(r0, 0);
        r0.setDataPosition(0);
        this.mMetadataFwk = (MediaMetadata) MediaMetadata.CREATOR.createFromParcel(r0);
        r0.recycle();
    L6:
        return this.mMetadataFwk;
    }

    public RatingCompat getRating(String r5) {
        return RatingCompat.fromRating(this.mBundle.getParcelable(r5));
    L5:
        e = move-exception;
        Log.w(TAG, "Failed to retrieve a key as Rating.", e);
        return null;
    }

    public String getString(String r3) {
        CharSequence r0 = this.mBundle.getCharSequence(r3);
        if (r0 != null) goto L5;
        return null;
    L5:
        return r0.toString();
    }

    public CharSequence getText(String r2) {
        return this.mBundle.getCharSequence(r2);
    }

    public Set<String> keySet() {
        return this.mBundle.keySet();
    }

    public int size() {
        return this.mBundle.size();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel r2, int r3) {
        r2.writeBundle(this.mBundle);
    }
}
