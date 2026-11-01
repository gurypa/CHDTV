package androidx.media3.common;

import android.net.Uri;
import android.os.Bundle;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class MediaMetadata {
    public static final MediaMetadata EMPTY = null;
    private static final String FIELD_ALBUM_ARTIST = null;
    private static final String FIELD_ALBUM_TITLE = null;
    private static final String FIELD_ARTIST = null;
    private static final String FIELD_ARTWORK_DATA = null;
    private static final String FIELD_ARTWORK_DATA_TYPE = null;
    private static final String FIELD_ARTWORK_URI = null;
    private static final String FIELD_COMPILATION = null;
    private static final String FIELD_COMPOSER = null;
    private static final String FIELD_CONDUCTOR = null;
    private static final String FIELD_DESCRIPTION = null;
    private static final String FIELD_DISC_NUMBER = null;
    private static final String FIELD_DISPLAY_TITLE = null;
    private static final String FIELD_DURATION_MS = null;
    private static final String FIELD_EXTRAS = null;
    private static final String FIELD_FOLDER_TYPE = null;
    private static final String FIELD_GENRE = null;
    private static final String FIELD_IS_BROWSABLE = null;
    private static final String FIELD_IS_PLAYABLE = null;
    private static final String FIELD_MEDIA_TYPE = null;
    private static final String FIELD_OVERALL_RATING = null;
    private static final String FIELD_RECORDING_DAY = null;
    private static final String FIELD_RECORDING_MONTH = null;
    private static final String FIELD_RECORDING_YEAR = null;
    private static final String FIELD_RELEASE_DAY = null;
    private static final String FIELD_RELEASE_MONTH = null;
    private static final String FIELD_RELEASE_YEAR = null;
    private static final String FIELD_STATION = null;
    private static final String FIELD_SUBTITLE = null;
    private static final String FIELD_SUPPORTED_COMMANDS = null;
    private static final String FIELD_TITLE = null;
    private static final String FIELD_TOTAL_DISC_COUNT = null;
    private static final String FIELD_TOTAL_TRACK_COUNT = null;
    private static final String FIELD_TRACK_NUMBER = null;
    private static final String FIELD_USER_RATING = null;
    private static final String FIELD_WRITER = null;

    @Deprecated
    public static final int FOLDER_TYPE_ALBUMS = 2;

    @Deprecated
    public static final int FOLDER_TYPE_ARTISTS = 3;

    @Deprecated
    public static final int FOLDER_TYPE_GENRES = 4;

    @Deprecated
    public static final int FOLDER_TYPE_MIXED = 0;

    @Deprecated
    public static final int FOLDER_TYPE_NONE = -1;

    @Deprecated
    public static final int FOLDER_TYPE_PLAYLISTS = 5;

    @Deprecated
    public static final int FOLDER_TYPE_TITLES = 1;

    @Deprecated
    public static final int FOLDER_TYPE_YEARS = 6;
    public static final int MEDIA_TYPE_ALBUM = 10;
    public static final int MEDIA_TYPE_ARTIST = 11;
    public static final int MEDIA_TYPE_AUDIO_BOOK = 15;
    public static final int MEDIA_TYPE_AUDIO_BOOK_CHAPTER = 2;
    public static final int MEDIA_TYPE_FOLDER_ALBUMS = 21;
    public static final int MEDIA_TYPE_FOLDER_ARTISTS = 22;
    public static final int MEDIA_TYPE_FOLDER_AUDIO_BOOKS = 26;
    public static final int MEDIA_TYPE_FOLDER_GENRES = 23;
    public static final int MEDIA_TYPE_FOLDER_MIXED = 20;
    public static final int MEDIA_TYPE_FOLDER_MOVIES = 35;
    public static final int MEDIA_TYPE_FOLDER_NEWS = 32;
    public static final int MEDIA_TYPE_FOLDER_PLAYLISTS = 24;
    public static final int MEDIA_TYPE_FOLDER_PODCASTS = 27;
    public static final int MEDIA_TYPE_FOLDER_RADIO_STATIONS = 31;
    public static final int MEDIA_TYPE_FOLDER_TRAILERS = 34;
    public static final int MEDIA_TYPE_FOLDER_TV_CHANNELS = 28;
    public static final int MEDIA_TYPE_FOLDER_TV_SERIES = 29;
    public static final int MEDIA_TYPE_FOLDER_TV_SHOWS = 30;
    public static final int MEDIA_TYPE_FOLDER_VIDEOS = 33;
    public static final int MEDIA_TYPE_FOLDER_YEARS = 25;
    public static final int MEDIA_TYPE_GENRE = 12;
    public static final int MEDIA_TYPE_MIXED = 0;
    public static final int MEDIA_TYPE_MOVIE = 8;
    public static final int MEDIA_TYPE_MUSIC = 1;
    public static final int MEDIA_TYPE_NEWS = 5;
    public static final int MEDIA_TYPE_PLAYLIST = 13;
    public static final int MEDIA_TYPE_PODCAST = 16;
    public static final int MEDIA_TYPE_PODCAST_EPISODE = 3;
    public static final int MEDIA_TYPE_RADIO_STATION = 4;
    public static final int MEDIA_TYPE_TRAILER = 7;
    public static final int MEDIA_TYPE_TV_CHANNEL = 17;
    public static final int MEDIA_TYPE_TV_SEASON = 19;
    public static final int MEDIA_TYPE_TV_SERIES = 18;
    public static final int MEDIA_TYPE_TV_SHOW = 9;
    public static final int MEDIA_TYPE_VIDEO = 6;
    public static final int MEDIA_TYPE_YEAR = 14;
    public static final int PICTURE_TYPE_ARTIST_PERFORMER = 8;
    public static final int PICTURE_TYPE_A_BRIGHT_COLORED_FISH = 17;
    public static final int PICTURE_TYPE_BACK_COVER = 4;
    public static final int PICTURE_TYPE_BAND_ARTIST_LOGO = 19;
    public static final int PICTURE_TYPE_BAND_ORCHESTRA = 10;
    public static final int PICTURE_TYPE_COMPOSER = 11;
    public static final int PICTURE_TYPE_CONDUCTOR = 9;
    public static final int PICTURE_TYPE_DURING_PERFORMANCE = 15;
    public static final int PICTURE_TYPE_DURING_RECORDING = 14;
    public static final int PICTURE_TYPE_FILE_ICON = 1;
    public static final int PICTURE_TYPE_FILE_ICON_OTHER = 2;
    public static final int PICTURE_TYPE_FRONT_COVER = 3;
    public static final int PICTURE_TYPE_ILLUSTRATION = 18;
    public static final int PICTURE_TYPE_LEAD_ARTIST_PERFORMER = 7;
    public static final int PICTURE_TYPE_LEAFLET_PAGE = 5;
    public static final int PICTURE_TYPE_LYRICIST = 12;
    public static final int PICTURE_TYPE_MEDIA = 6;
    public static final int PICTURE_TYPE_MOVIE_VIDEO_SCREEN_CAPTURE = 16;
    public static final int PICTURE_TYPE_OTHER = 0;
    public static final int PICTURE_TYPE_PUBLISHER_STUDIO_LOGO = 20;
    public static final int PICTURE_TYPE_RECORDING_LOCATION = 13;
    public final CharSequence albumArtist;
    public final CharSequence albumTitle;
    public final CharSequence artist;
    public final byte[] artworkData;
    public final Integer artworkDataType;
    public final Uri artworkUri;
    public final CharSequence compilation;
    public final CharSequence composer;
    public final CharSequence conductor;
    public final CharSequence description;
    public final Integer discNumber;
    public final CharSequence displayTitle;
    public final Long durationMs;
    public final Bundle extras;

    @Deprecated
    public final Integer folderType;
    public final CharSequence genre;
    public final Boolean isBrowsable;
    public final Boolean isPlayable;
    public final Integer mediaType;
    public final Rating overallRating;
    public final Integer recordingDay;
    public final Integer recordingMonth;
    public final Integer recordingYear;
    public final Integer releaseDay;
    public final Integer releaseMonth;
    public final Integer releaseYear;
    public final CharSequence station;
    public final CharSequence subtitle;
    public final ImmutableList<String> supportedCommands;
    public final CharSequence title;
    public final Integer totalDiscCount;
    public final Integer totalTrackCount;
    public final Integer trackNumber;
    public final Rating userRating;
    public final CharSequence writer;

    @Deprecated
    public final Integer year;

    /* JADX INFO: renamed from: androidx.media3.common.MediaMetadata$1 */
    static /* synthetic */ class C01901 {
    }

    public static final class Builder {
        private CharSequence albumArtist;
        private CharSequence albumTitle;
        private CharSequence artist;
        private byte[] artworkData;
        private Integer artworkDataType;
        private Uri artworkUri;
        private CharSequence compilation;
        private CharSequence composer;
        private CharSequence conductor;
        private CharSequence description;
        private Integer discNumber;
        private CharSequence displayTitle;
        private Long durationMs;
        private Bundle extras;
        private Integer folderType;
        private CharSequence genre;
        private Boolean isBrowsable;
        private Boolean isPlayable;
        private Integer mediaType;
        private Rating overallRating;
        private Integer recordingDay;
        private Integer recordingMonth;
        private Integer recordingYear;
        private Integer releaseDay;
        private Integer releaseMonth;
        private Integer releaseYear;
        private CharSequence station;
        private CharSequence subtitle;
        private ImmutableList<String> supportedCommands;
        private CharSequence title;
        private Integer totalDiscCount;
        private Integer totalTrackCount;
        private Integer trackNumber;
        private Rating userRating;
        private CharSequence writer;

        public Builder() {
            this.supportedCommands = ImmutableList.m212of();
        }

        private Builder(MediaMetadata r2) {
            this.title = r2.title;
            this.artist = r2.artist;
            this.albumTitle = r2.albumTitle;
            this.albumArtist = r2.albumArtist;
            this.displayTitle = r2.displayTitle;
            this.subtitle = r2.subtitle;
            this.description = r2.description;
            this.durationMs = r2.durationMs;
            this.userRating = r2.userRating;
            this.overallRating = r2.overallRating;
            this.artworkData = r2.artworkData;
            this.artworkDataType = r2.artworkDataType;
            this.artworkUri = r2.artworkUri;
            this.trackNumber = r2.trackNumber;
            this.totalTrackCount = r2.totalTrackCount;
            this.folderType = r2.folderType;
            this.isBrowsable = r2.isBrowsable;
            this.isPlayable = r2.isPlayable;
            this.recordingYear = r2.recordingYear;
            this.recordingMonth = r2.recordingMonth;
            this.recordingDay = r2.recordingDay;
            this.releaseYear = r2.releaseYear;
            this.releaseMonth = r2.releaseMonth;
            this.releaseDay = r2.releaseDay;
            this.writer = r2.writer;
            this.composer = r2.composer;
            this.conductor = r2.conductor;
            this.discNumber = r2.discNumber;
            this.totalDiscCount = r2.totalDiscCount;
            this.genre = r2.genre;
            this.compilation = r2.compilation;
            this.station = r2.station;
            this.mediaType = r2.mediaType;
            this.supportedCommands = r2.supportedCommands;
            this.extras = r2.extras;
        }

        /* synthetic */ Builder(MediaMetadata r1, C01901 r2) {
            this(r1);
        }

        static /* synthetic */ Boolean access$100(Builder r1) {
            return r1.isBrowsable;
        }

        static /* synthetic */ CharSequence access$1000(Builder r1) {
            return r1.description;
        }

        static /* synthetic */ Long access$1100(Builder r1) {
            return r1.durationMs;
        }

        static /* synthetic */ Rating access$1200(Builder r1) {
            return r1.userRating;
        }

        static /* synthetic */ Rating access$1300(Builder r1) {
            return r1.overallRating;
        }

        static /* synthetic */ byte[] access$1400(Builder r1) {
            return r1.artworkData;
        }

        static /* synthetic */ Integer access$1500(Builder r1) {
            return r1.artworkDataType;
        }

        static /* synthetic */ Uri access$1600(Builder r1) {
            return r1.artworkUri;
        }

        static /* synthetic */ Integer access$1700(Builder r1) {
            return r1.trackNumber;
        }

        static /* synthetic */ Integer access$1800(Builder r1) {
            return r1.totalTrackCount;
        }

        static /* synthetic */ Boolean access$1900(Builder r1) {
            return r1.isPlayable;
        }

        static /* synthetic */ Integer access$200(Builder r1) {
            return r1.folderType;
        }

        static /* synthetic */ Integer access$2000(Builder r1) {
            return r1.recordingYear;
        }

        static /* synthetic */ Integer access$2100(Builder r1) {
            return r1.recordingMonth;
        }

        static /* synthetic */ Integer access$2200(Builder r1) {
            return r1.recordingDay;
        }

        static /* synthetic */ Integer access$2300(Builder r1) {
            return r1.releaseYear;
        }

        static /* synthetic */ Integer access$2400(Builder r1) {
            return r1.releaseMonth;
        }

        static /* synthetic */ Integer access$2500(Builder r1) {
            return r1.releaseDay;
        }

        static /* synthetic */ CharSequence access$2600(Builder r1) {
            return r1.writer;
        }

        static /* synthetic */ CharSequence access$2700(Builder r1) {
            return r1.composer;
        }

        static /* synthetic */ CharSequence access$2800(Builder r1) {
            return r1.conductor;
        }

        static /* synthetic */ Integer access$2900(Builder r1) {
            return r1.discNumber;
        }

        static /* synthetic */ Integer access$300(Builder r1) {
            return r1.mediaType;
        }

        static /* synthetic */ Integer access$3000(Builder r1) {
            return r1.totalDiscCount;
        }

        static /* synthetic */ CharSequence access$3100(Builder r1) {
            return r1.genre;
        }

        static /* synthetic */ CharSequence access$3200(Builder r1) {
            return r1.compilation;
        }

        static /* synthetic */ CharSequence access$3300(Builder r1) {
            return r1.station;
        }

        static /* synthetic */ ImmutableList access$3400(Builder r1) {
            return r1.supportedCommands;
        }

        static /* synthetic */ Bundle access$3500(Builder r1) {
            return r1.extras;
        }

        static /* synthetic */ CharSequence access$400(Builder r1) {
            return r1.title;
        }

        static /* synthetic */ CharSequence access$500(Builder r1) {
            return r1.artist;
        }

        static /* synthetic */ CharSequence access$600(Builder r1) {
            return r1.albumTitle;
        }

        static /* synthetic */ CharSequence access$700(Builder r1) {
            return r1.albumArtist;
        }

        static /* synthetic */ CharSequence access$800(Builder r1) {
            return r1.displayTitle;
        }

        static /* synthetic */ CharSequence access$900(Builder r1) {
            return r1.subtitle;
        }

        public MediaMetadata build() {
            return new MediaMetadata(this, null);
        }

        public Builder maybeSetArtworkData(byte[] r4, int r5) {
            if (this.artworkData != null) goto L5;
        L8:
            this.artworkData = (byte[]) r4.clone();
            this.artworkDataType = Integer.valueOf(r5);
        L9:
            return this;
        L5:
            if (Util.areEqual(Integer.valueOf(r5), 3) == true) goto L8;
            if (Util.areEqual(this.artworkDataType, 3) == true) goto L9;
            goto L8
        }

        public Builder populate(MediaMetadata r3) {
            if (r3 != null) goto L5;
            return this;
        L5:
            if (r3.title == null) goto L8;
            setTitle(r3.title);
        L8:
            if (r3.artist == null) goto L11;
            setArtist(r3.artist);
        L11:
            if (r3.albumTitle == null) goto L14;
            setAlbumTitle(r3.albumTitle);
        L14:
            if (r3.albumArtist == null) goto L17;
            setAlbumArtist(r3.albumArtist);
        L17:
            if (r3.displayTitle == null) goto L20;
            setDisplayTitle(r3.displayTitle);
        L20:
            if (r3.subtitle == null) goto L23;
            setSubtitle(r3.subtitle);
        L23:
            if (r3.description == null) goto L26;
            setDescription(r3.description);
        L26:
            if (r3.durationMs == null) goto L29;
            setDurationMs(r3.durationMs);
        L29:
            if (r3.userRating == null) goto L32;
            setUserRating(r3.userRating);
        L32:
            if (r3.overallRating == null) goto L35;
            setOverallRating(r3.overallRating);
        L35:
            if (r3.artworkUri == null) goto L37;
        L38:
            setArtworkUri(r3.artworkUri);
            setArtworkData(r3.artworkData, r3.artworkDataType);
        L40:
            if (r3.trackNumber == null) goto L43;
            setTrackNumber(r3.trackNumber);
        L43:
            if (r3.totalTrackCount == null) goto L46;
            setTotalTrackCount(r3.totalTrackCount);
        L46:
            if (r3.folderType == null) goto L49;
            setFolderType(r3.folderType);
        L49:
            if (r3.isBrowsable == null) goto L52;
            setIsBrowsable(r3.isBrowsable);
        L52:
            if (r3.isPlayable == null) goto L55;
            setIsPlayable(r3.isPlayable);
        L55:
            if (r3.year == null) goto L58;
            setRecordingYear(r3.year);
        L58:
            if (r3.recordingYear == null) goto L61;
            setRecordingYear(r3.recordingYear);
        L61:
            if (r3.recordingMonth == null) goto L64;
            setRecordingMonth(r3.recordingMonth);
        L64:
            if (r3.recordingDay == null) goto L67;
            setRecordingDay(r3.recordingDay);
        L67:
            if (r3.releaseYear == null) goto L70;
            setReleaseYear(r3.releaseYear);
        L70:
            if (r3.releaseMonth == null) goto L73;
            setReleaseMonth(r3.releaseMonth);
        L73:
            if (r3.releaseDay == null) goto L76;
            setReleaseDay(r3.releaseDay);
        L76:
            if (r3.writer == null) goto L79;
            setWriter(r3.writer);
        L79:
            if (r3.composer == null) goto L82;
            setComposer(r3.composer);
        L82:
            if (r3.conductor == null) goto L85;
            setConductor(r3.conductor);
        L85:
            if (r3.discNumber == null) goto L88;
            setDiscNumber(r3.discNumber);
        L88:
            if (r3.totalDiscCount == null) goto L91;
            setTotalDiscCount(r3.totalDiscCount);
        L91:
            if (r3.genre == null) goto L94;
            setGenre(r3.genre);
        L94:
            if (r3.compilation == null) goto L97;
            setCompilation(r3.compilation);
        L97:
            if (r3.station == null) goto L100;
            setStation(r3.station);
        L100:
            if (r3.mediaType == null) goto L103;
            setMediaType(r3.mediaType);
        L103:
            if (r3.extras == null) goto L106;
            setExtras(r3.extras);
        L106:
            if (r3.supportedCommands.isEmpty() == true) goto L108;
            setSupportedCommands(r3.supportedCommands);
        L108:
            return this;
        L37:
            if (r3.artworkData == null) goto L40;
            goto L38
        }

        public Builder populateFromMetadata(Metadata r3) {
            int r0 = 0;
        L4:
            if (r0 >= r3.length()) goto L6;
            r3.get(r0).populateMediaMetadata(this);
            r0 = r0 + 1;
            goto L4
        L6:
            return this;
        }

        public Builder populateFromMetadata(List<Metadata> r5) {
            int r0 = 0;
        L4:
            if (r0 >= r5.size()) goto L10;
            Metadata r1 = r5.get(r0);
            int r2 = 0;
        L7:
            if (r2 >= r1.length()) goto L9;
            r1.get(r2).populateMediaMetadata(this);
            r2 = r2 + 1;
            goto L7
        L9:
            r0 = r0 + 1;
            goto L4
        L10:
            return this;
        }

        public Builder setAlbumArtist(CharSequence r1) {
            this.albumArtist = r1;
            return this;
        }

        public Builder setAlbumTitle(CharSequence r1) {
            this.albumTitle = r1;
            return this;
        }

        public Builder setArtist(CharSequence r1) {
            this.artist = r1;
            return this;
        }

        @Deprecated
        public Builder setArtworkData(byte[] r2) {
            return setArtworkData(r2, null);
        }

        public Builder setArtworkData(byte[] r2, Integer r3) {
            if (r2 != null) goto L4;
            byte[] r0 = null;
        L5:
            this.artworkData = r0;
            this.artworkDataType = r3;
            return this;
        L4:
            r0 = (byte[]) r2.clone();
            goto L5
        }

        public Builder setArtworkUri(Uri r1) {
            this.artworkUri = r1;
            return this;
        }

        public Builder setCompilation(CharSequence r1) {
            this.compilation = r1;
            return this;
        }

        public Builder setComposer(CharSequence r1) {
            this.composer = r1;
            return this;
        }

        public Builder setConductor(CharSequence r1) {
            this.conductor = r1;
            return this;
        }

        public Builder setDescription(CharSequence r1) {
            this.description = r1;
            return this;
        }

        public Builder setDiscNumber(Integer r1) {
            this.discNumber = r1;
            return this;
        }

        public Builder setDisplayTitle(CharSequence r1) {
            this.displayTitle = r1;
            return this;
        }

        public Builder setDurationMs(Long r5) {
            if (r5 != null) goto L4;
        L7:
            boolean r0 = true;
        L8:
            Assertions.checkArgument(r0);
            this.durationMs = r5;
            return this;
        L4:
            if (r5.longValue() >= 0) goto L7;
            r0 = false;
            goto L8
        }

        public Builder setExtras(Bundle r1) {
            this.extras = r1;
            return this;
        }

        @Deprecated
        public Builder setFolderType(Integer r1) {
            this.folderType = r1;
            return this;
        }

        public Builder setGenre(CharSequence r1) {
            this.genre = r1;
            return this;
        }

        public Builder setIsBrowsable(Boolean r1) {
            this.isBrowsable = r1;
            return this;
        }

        public Builder setIsPlayable(Boolean r1) {
            this.isPlayable = r1;
            return this;
        }

        public Builder setMediaType(Integer r1) {
            this.mediaType = r1;
            return this;
        }

        public Builder setOverallRating(Rating r1) {
            this.overallRating = r1;
            return this;
        }

        public Builder setRecordingDay(Integer r1) {
            this.recordingDay = r1;
            return this;
        }

        public Builder setRecordingMonth(Integer r1) {
            this.recordingMonth = r1;
            return this;
        }

        public Builder setRecordingYear(Integer r1) {
            this.recordingYear = r1;
            return this;
        }

        public Builder setReleaseDay(Integer r1) {
            this.releaseDay = r1;
            return this;
        }

        public Builder setReleaseMonth(Integer r1) {
            this.releaseMonth = r1;
            return this;
        }

        public Builder setReleaseYear(Integer r1) {
            this.releaseYear = r1;
            return this;
        }

        public Builder setStation(CharSequence r1) {
            this.station = r1;
            return this;
        }

        public Builder setSubtitle(CharSequence r1) {
            this.subtitle = r1;
            return this;
        }

        public Builder setSupportedCommands(List<String> r2) {
            this.supportedCommands = ImmutableList.copyOf(r2);
            return this;
        }

        public Builder setTitle(CharSequence r1) {
            this.title = r1;
            return this;
        }

        public Builder setTotalDiscCount(Integer r1) {
            this.totalDiscCount = r1;
            return this;
        }

        public Builder setTotalTrackCount(Integer r1) {
            this.totalTrackCount = r1;
            return this;
        }

        public Builder setTrackNumber(Integer r1) {
            this.trackNumber = r1;
            return this;
        }

        public Builder setUserRating(Rating r1) {
            this.userRating = r1;
            return this;
        }

        public Builder setWriter(CharSequence r1) {
            this.writer = r1;
            return this;
        }

        @Deprecated
        public Builder setYear(Integer r2) {
            return setRecordingYear(r2);
        }
    }

    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.TYPE_USE})
    @Deprecated
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface FolderType {
    }

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface MediaType {
    }

    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface PictureType {
    }

    static {
        EMPTY = new Builder().build();
        FIELD_TITLE = Util.intToStringMaxRadix(0);
        FIELD_ARTIST = Util.intToStringMaxRadix(1);
        FIELD_ALBUM_TITLE = Util.intToStringMaxRadix(2);
        FIELD_ALBUM_ARTIST = Util.intToStringMaxRadix(3);
        FIELD_DISPLAY_TITLE = Util.intToStringMaxRadix(4);
        FIELD_SUBTITLE = Util.intToStringMaxRadix(5);
        FIELD_DESCRIPTION = Util.intToStringMaxRadix(6);
        FIELD_USER_RATING = Util.intToStringMaxRadix(8);
        FIELD_OVERALL_RATING = Util.intToStringMaxRadix(9);
        FIELD_ARTWORK_DATA = Util.intToStringMaxRadix(10);
        FIELD_ARTWORK_URI = Util.intToStringMaxRadix(11);
        FIELD_TRACK_NUMBER = Util.intToStringMaxRadix(12);
        FIELD_TOTAL_TRACK_COUNT = Util.intToStringMaxRadix(13);
        FIELD_FOLDER_TYPE = Util.intToStringMaxRadix(14);
        FIELD_IS_PLAYABLE = Util.intToStringMaxRadix(15);
        FIELD_RECORDING_YEAR = Util.intToStringMaxRadix(16);
        FIELD_RECORDING_MONTH = Util.intToStringMaxRadix(17);
        FIELD_RECORDING_DAY = Util.intToStringMaxRadix(18);
        FIELD_RELEASE_YEAR = Util.intToStringMaxRadix(19);
        FIELD_RELEASE_MONTH = Util.intToStringMaxRadix(20);
        FIELD_RELEASE_DAY = Util.intToStringMaxRadix(21);
        FIELD_WRITER = Util.intToStringMaxRadix(22);
        FIELD_COMPOSER = Util.intToStringMaxRadix(23);
        FIELD_CONDUCTOR = Util.intToStringMaxRadix(24);
        FIELD_DISC_NUMBER = Util.intToStringMaxRadix(25);
        FIELD_TOTAL_DISC_COUNT = Util.intToStringMaxRadix(26);
        FIELD_GENRE = Util.intToStringMaxRadix(27);
        FIELD_COMPILATION = Util.intToStringMaxRadix(28);
        FIELD_ARTWORK_DATA_TYPE = Util.intToStringMaxRadix(29);
        FIELD_STATION = Util.intToStringMaxRadix(30);
        FIELD_MEDIA_TYPE = Util.intToStringMaxRadix(31);
        FIELD_IS_BROWSABLE = Util.intToStringMaxRadix(32);
        FIELD_DURATION_MS = Util.intToStringMaxRadix(33);
        FIELD_SUPPORTED_COMMANDS = Util.intToStringMaxRadix(34);
        FIELD_EXTRAS = Util.intToStringMaxRadix(1000);
    }

    private MediaMetadata(Builder r7) {
        Boolean r0 = Builder.access$100(r7);
        Integer r1 = Builder.access$200(r7);
        Integer r2 = Builder.access$300(r7);
        boolean r3 = false;
        int r4 = 0;
        if (r0 != null) goto L5;
        if (r1 != null) goto L15;
    L21:
        this.title = Builder.access$400(r7);
        this.artist = Builder.access$500(r7);
        this.albumTitle = Builder.access$600(r7);
        this.albumArtist = Builder.access$700(r7);
        this.displayTitle = Builder.access$800(r7);
        this.subtitle = Builder.access$900(r7);
        this.description = Builder.access$1000(r7);
        this.durationMs = Builder.access$1100(r7);
        this.userRating = Builder.access$1200(r7);
        this.overallRating = Builder.access$1300(r7);
        this.artworkData = Builder.access$1400(r7);
        this.artworkDataType = Builder.access$1500(r7);
        this.artworkUri = Builder.access$1600(r7);
        this.trackNumber = Builder.access$1700(r7);
        this.totalTrackCount = Builder.access$1800(r7);
        this.folderType = r1;
        this.isBrowsable = r0;
        this.isPlayable = Builder.access$1900(r7);
        this.year = Builder.access$2000(r7);
        this.recordingYear = Builder.access$2000(r7);
        this.recordingMonth = Builder.access$2100(r7);
        this.recordingDay = Builder.access$2200(r7);
        this.releaseYear = Builder.access$2300(r7);
        this.releaseMonth = Builder.access$2400(r7);
        this.releaseDay = Builder.access$2500(r7);
        this.writer = Builder.access$2600(r7);
        this.composer = Builder.access$2700(r7);
        this.conductor = Builder.access$2800(r7);
        this.discNumber = Builder.access$2900(r7);
        this.totalDiscCount = Builder.access$3000(r7);
        this.genre = Builder.access$3100(r7);
        this.compilation = Builder.access$3200(r7);
        this.station = Builder.access$3300(r7);
        this.mediaType = r2;
        this.supportedCommands = Builder.access$3400(r7);
        this.extras = Builder.access$3500(r7);
        return;
    L15:
        if (r1.intValue() == (-1)) goto L17;
        r3 = true;
    L17:
        r0 = Boolean.valueOf(r3);
        if (r0.booleanValue() == false) goto L21;
        if (r2 != null) goto L21;
        r2 = Integer.valueOf(getMediaTypeFromFolderType(r1.intValue()));
        goto L21
    L5:
        if (r0.booleanValue() == true) goto L7;
        r1 = -1;
        goto L21
    L7:
        if (r1 != null) goto L9;
    L10:
        if (r2 == null) goto L12;
        r4 = getFolderTypeFromMediaType(r2.intValue());
    L12:
        r1 = Integer.valueOf(r4);
        goto L21
    L9:
        if (r1.intValue() != (-1)) goto L21;
        goto L10
    }

    /* synthetic */ MediaMetadata(Builder r1, C01901 r2) {
        this(r1);
    }

    public static MediaMetadata fromBundle(Bundle r4) {
        Builder r0 = new Builder();
        Builder r1 = r0.setTitle(r4.getCharSequence(FIELD_TITLE)).setArtist(r4.getCharSequence(FIELD_ARTIST)).setAlbumTitle(r4.getCharSequence(FIELD_ALBUM_TITLE)).setAlbumArtist(r4.getCharSequence(FIELD_ALBUM_ARTIST)).setDisplayTitle(r4.getCharSequence(FIELD_DISPLAY_TITLE)).setSubtitle(r4.getCharSequence(FIELD_SUBTITLE)).setDescription(r4.getCharSequence(FIELD_DESCRIPTION));
        byte[] r2 = r4.getByteArray(FIELD_ARTWORK_DATA);
        if (r4.containsKey(FIELD_ARTWORK_DATA_TYPE) == false) goto L5;
        Integer r3 = Integer.valueOf(r4.getInt(FIELD_ARTWORK_DATA_TYPE));
    L6:
        r1.setArtworkData(r2, r3).setArtworkUri((Uri) r4.getParcelable(FIELD_ARTWORK_URI)).setWriter(r4.getCharSequence(FIELD_WRITER)).setComposer(r4.getCharSequence(FIELD_COMPOSER)).setConductor(r4.getCharSequence(FIELD_CONDUCTOR)).setGenre(r4.getCharSequence(FIELD_GENRE)).setCompilation(r4.getCharSequence(FIELD_COMPILATION)).setStation(r4.getCharSequence(FIELD_STATION)).setExtras(r4.getBundle(FIELD_EXTRAS));
        if (r4.containsKey(FIELD_USER_RATING) == false) goto L12;
        Bundle r5 = r4.getBundle(FIELD_USER_RATING);
        if (r5 == null) goto L12;
        r0.setUserRating(Rating.fromBundle(r5));
    L12:
        if (r4.containsKey(FIELD_OVERALL_RATING) == false) goto L17;
        Bundle r6 = r4.getBundle(FIELD_OVERALL_RATING);
        if (r6 == null) goto L17;
        r0.setOverallRating(Rating.fromBundle(r6));
    L17:
        if (r4.containsKey(FIELD_DURATION_MS) == false) goto L20;
        r0.setDurationMs(Long.valueOf(r4.getLong(FIELD_DURATION_MS)));
    L20:
        if (r4.containsKey(FIELD_TRACK_NUMBER) == false) goto L23;
        r0.setTrackNumber(Integer.valueOf(r4.getInt(FIELD_TRACK_NUMBER)));
    L23:
        if (r4.containsKey(FIELD_TOTAL_TRACK_COUNT) == false) goto L26;
        r0.setTotalTrackCount(Integer.valueOf(r4.getInt(FIELD_TOTAL_TRACK_COUNT)));
    L26:
        if (r4.containsKey(FIELD_FOLDER_TYPE) == false) goto L29;
        r0.setFolderType(Integer.valueOf(r4.getInt(FIELD_FOLDER_TYPE)));
    L29:
        if (r4.containsKey(FIELD_IS_BROWSABLE) == false) goto L32;
        r0.setIsBrowsable(Boolean.valueOf(r4.getBoolean(FIELD_IS_BROWSABLE)));
    L32:
        if (r4.containsKey(FIELD_IS_PLAYABLE) == false) goto L35;
        r0.setIsPlayable(Boolean.valueOf(r4.getBoolean(FIELD_IS_PLAYABLE)));
    L35:
        if (r4.containsKey(FIELD_RECORDING_YEAR) == false) goto L38;
        r0.setRecordingYear(Integer.valueOf(r4.getInt(FIELD_RECORDING_YEAR)));
    L38:
        if (r4.containsKey(FIELD_RECORDING_MONTH) == false) goto L41;
        r0.setRecordingMonth(Integer.valueOf(r4.getInt(FIELD_RECORDING_MONTH)));
    L41:
        if (r4.containsKey(FIELD_RECORDING_DAY) == false) goto L44;
        r0.setRecordingDay(Integer.valueOf(r4.getInt(FIELD_RECORDING_DAY)));
    L44:
        if (r4.containsKey(FIELD_RELEASE_YEAR) == false) goto L47;
        r0.setReleaseYear(Integer.valueOf(r4.getInt(FIELD_RELEASE_YEAR)));
    L47:
        if (r4.containsKey(FIELD_RELEASE_MONTH) == false) goto L50;
        r0.setReleaseMonth(Integer.valueOf(r4.getInt(FIELD_RELEASE_MONTH)));
    L50:
        if (r4.containsKey(FIELD_RELEASE_DAY) == false) goto L53;
        r0.setReleaseDay(Integer.valueOf(r4.getInt(FIELD_RELEASE_DAY)));
    L53:
        if (r4.containsKey(FIELD_DISC_NUMBER) == false) goto L56;
        r0.setDiscNumber(Integer.valueOf(r4.getInt(FIELD_DISC_NUMBER)));
    L56:
        if (r4.containsKey(FIELD_TOTAL_DISC_COUNT) == false) goto L59;
        r0.setTotalDiscCount(Integer.valueOf(r4.getInt(FIELD_TOTAL_DISC_COUNT)));
    L59:
        if (r4.containsKey(FIELD_MEDIA_TYPE) == false) goto L61;
        r0.setMediaType(Integer.valueOf(r4.getInt(FIELD_MEDIA_TYPE)));
    L61:
        ArrayList<String> r7 = r4.getStringArrayList(FIELD_SUPPORTED_COMMANDS);
        if (r7 == null) goto L65;
        r0.setSupportedCommands(r7);
    L65:
        return r0.build();
    L5:
        r3 = null;
        goto L6
    }

    private static int getFolderTypeFromMediaType(int r1) {
        switch(r1) {
            case 1: goto L15;
            case 2: goto L15;
            case 3: goto L15;
            case 4: goto L15;
            case 5: goto L15;
            case 6: goto L15;
            case 7: goto L15;
            case 8: goto L15;
            case 9: goto L15;
            case 10: goto L15;
            case 11: goto L15;
            case 12: goto L15;
            case 13: goto L15;
            case 14: goto L15;
            case 15: goto L15;
            case 16: goto L15;
            case 17: goto L15;
            case 18: goto L15;
            case 19: goto L15;
            case 20: goto L3;
            case 21: goto L13;
            case 22: goto L11;
            case 23: goto L9;
            case 24: goto L7;
            case 25: goto L5;
            case 26: goto L3;
            case 27: goto L3;
            case 28: goto L3;
            case 29: goto L3;
            case 30: goto L3;
            case 31: goto L15;
            case 32: goto L15;
            case 33: goto L15;
            case 34: goto L15;
            case 35: goto L15;
            default: goto L3;
        };
    L3:
        return 0;
    L5:
        return 6;
    L7:
        return 5;
    L9:
        return 4;
    L11:
        return 3;
    L13:
        return 2;
    L15:
        return 1;
    }

    private static int getMediaTypeFromFolderType(int r1) {
        switch(r1) {
            case 1: goto L15;
            case 2: goto L13;
            case 3: goto L11;
            case 4: goto L9;
            case 5: goto L7;
            case 6: goto L5;
            default: goto L3;
        };
    L3:
        return 20;
    L5:
        return 25;
    L7:
        return 24;
    L9:
        return 23;
    L11:
        return 22;
    L13:
        return 21;
    L15:
        return 0;
    }

    public Builder buildUpon() {
        return new Builder(this, null);
    }

    public boolean equals(Object r6) {
        if (this != r6) goto L6;
        return true;
    L6:
        if (r6 != null) goto L8;
    L90:
        return false;
    L8:
        if (getClass() != r6.getClass()) goto L90;
        MediaMetadata r2 = (MediaMetadata) r6;
        if (Util.areEqual(this.title, r2.title) == false) goto L89;
        if (Util.areEqual(this.artist, r2.artist) == false) goto L89;
        if (Util.areEqual(this.albumTitle, r2.albumTitle) == false) goto L89;
        if (Util.areEqual(this.albumArtist, r2.albumArtist) == false) goto L89;
        if (Util.areEqual(this.displayTitle, r2.displayTitle) == false) goto L89;
        if (Util.areEqual(this.subtitle, r2.subtitle) == false) goto L89;
        if (Util.areEqual(this.description, r2.description) == false) goto L89;
        if (Util.areEqual(this.durationMs, r2.durationMs) == false) goto L89;
        if (Util.areEqual(this.userRating, r2.userRating) == false) goto L89;
        if (Util.areEqual(this.overallRating, r2.overallRating) == false) goto L89;
        if (Arrays.equals(this.artworkData, r2.artworkData) == false) goto L89;
        if (Util.areEqual(this.artworkDataType, r2.artworkDataType) == false) goto L89;
        if (Util.areEqual(this.artworkUri, r2.artworkUri) == false) goto L89;
        if (Util.areEqual(this.trackNumber, r2.trackNumber) == false) goto L89;
        if (Util.areEqual(this.totalTrackCount, r2.totalTrackCount) == false) goto L89;
        if (Util.areEqual(this.folderType, r2.folderType) == false) goto L89;
        if (Util.areEqual(this.isBrowsable, r2.isBrowsable) == false) goto L89;
        if (Util.areEqual(this.isPlayable, r2.isPlayable) == false) goto L89;
        if (Util.areEqual(this.recordingYear, r2.recordingYear) == false) goto L89;
        if (Util.areEqual(this.recordingMonth, r2.recordingMonth) == false) goto L89;
        if (Util.areEqual(this.recordingDay, r2.recordingDay) == false) goto L89;
        if (Util.areEqual(this.releaseYear, r2.releaseYear) == false) goto L89;
        if (Util.areEqual(this.releaseMonth, r2.releaseMonth) == false) goto L89;
        if (Util.areEqual(this.releaseDay, r2.releaseDay) == false) goto L89;
        if (Util.areEqual(this.writer, r2.writer) == false) goto L89;
        if (Util.areEqual(this.composer, r2.composer) == false) goto L89;
        if (Util.areEqual(this.conductor, r2.conductor) == false) goto L89;
        if (Util.areEqual(this.discNumber, r2.discNumber) == false) goto L89;
        if (Util.areEqual(this.totalDiscCount, r2.totalDiscCount) == false) goto L89;
        if (Util.areEqual(this.genre, r2.genre) == false) goto L89;
        if (Util.areEqual(this.compilation, r2.compilation) == false) goto L89;
        if (Util.areEqual(this.station, r2.station) == false) goto L89;
        if (Util.areEqual(this.mediaType, r2.mediaType) == false) goto L89;
        if (Util.areEqual(this.supportedCommands, r2.supportedCommands) == false) goto L89;
        if (this.extras != null) goto L81;
        boolean r3 = true;
    L83:
        if (r2.extras != null) goto L85;
        boolean r4 = true;
    L86:
        if (r3 != r4) goto L89;
        return true;
    L85:
        r4 = false;
        goto L86
    L81:
        r3 = false;
    L89:
        return false;
    }

    public int hashCode() {
        CharSequence r1 = this.title;
        CharSequence r2 = this.artist;
        CharSequence r3 = this.albumTitle;
        CharSequence r4 = this.albumArtist;
        CharSequence r5 = this.displayTitle;
        CharSequence r6 = this.subtitle;
        CharSequence r7 = this.description;
        Long r8 = this.durationMs;
        Rating r9 = this.userRating;
        Rating r10 = this.overallRating;
        Integer r11 = Integer.valueOf(Arrays.hashCode(this.artworkData));
        Integer r12 = this.artworkDataType;
        Uri r13 = this.artworkUri;
        Integer r14 = this.trackNumber;
        Integer r15 = this.totalTrackCount;
        Integer r16 = this.folderType;
        Boolean r17 = this.isBrowsable;
        Boolean r18 = this.isPlayable;
        Integer r19 = this.recordingYear;
        Integer r20 = this.recordingMonth;
        Integer r21 = this.recordingDay;
        Integer r22 = this.releaseYear;
        Integer r23 = this.releaseMonth;
        Integer r24 = this.releaseDay;
        CharSequence r25 = this.writer;
        CharSequence r26 = this.composer;
        CharSequence r27 = this.conductor;
        Integer r28 = this.discNumber;
        Integer r29 = this.totalDiscCount;
        CharSequence r30 = this.genre;
        CharSequence r31 = this.compilation;
        CharSequence r32 = this.station;
        Integer r33 = this.mediaType;
        if (this.extras != null) goto L5;
        boolean r34 = true;
    L7:
        return Objects.hashCode(new Object[]{r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20, r21, r22, r23, r24, r25, r26, r27, r28, r29, r30, r31, r32, r33, Boolean.valueOf(r34), this.supportedCommands});
    L5:
        r34 = false;
        goto L7
    }

    public Bundle toBundle() {
        Bundle r0 = new Bundle();
        if (this.title == null) goto L6;
        r0.putCharSequence(FIELD_TITLE, this.title);
    L6:
        if (this.artist == null) goto L9;
        r0.putCharSequence(FIELD_ARTIST, this.artist);
    L9:
        if (this.albumTitle == null) goto L12;
        r0.putCharSequence(FIELD_ALBUM_TITLE, this.albumTitle);
    L12:
        if (this.albumArtist == null) goto L15;
        r0.putCharSequence(FIELD_ALBUM_ARTIST, this.albumArtist);
    L15:
        if (this.displayTitle == null) goto L18;
        r0.putCharSequence(FIELD_DISPLAY_TITLE, this.displayTitle);
    L18:
        if (this.subtitle == null) goto L21;
        r0.putCharSequence(FIELD_SUBTITLE, this.subtitle);
    L21:
        if (this.description == null) goto L24;
        r0.putCharSequence(FIELD_DESCRIPTION, this.description);
    L24:
        if (this.durationMs == null) goto L27;
        r0.putLong(FIELD_DURATION_MS, this.durationMs.longValue());
    L27:
        if (this.artworkData == null) goto L30;
        r0.putByteArray(FIELD_ARTWORK_DATA, this.artworkData);
    L30:
        if (this.artworkUri == null) goto L33;
        r0.putParcelable(FIELD_ARTWORK_URI, this.artworkUri);
    L33:
        if (this.writer == null) goto L36;
        r0.putCharSequence(FIELD_WRITER, this.writer);
    L36:
        if (this.composer == null) goto L39;
        r0.putCharSequence(FIELD_COMPOSER, this.composer);
    L39:
        if (this.conductor == null) goto L42;
        r0.putCharSequence(FIELD_CONDUCTOR, this.conductor);
    L42:
        if (this.genre == null) goto L45;
        r0.putCharSequence(FIELD_GENRE, this.genre);
    L45:
        if (this.compilation == null) goto L48;
        r0.putCharSequence(FIELD_COMPILATION, this.compilation);
    L48:
        if (this.station == null) goto L51;
        r0.putCharSequence(FIELD_STATION, this.station);
    L51:
        if (this.userRating == null) goto L54;
        r0.putBundle(FIELD_USER_RATING, this.userRating.toBundle());
    L54:
        if (this.overallRating == null) goto L57;
        r0.putBundle(FIELD_OVERALL_RATING, this.overallRating.toBundle());
    L57:
        if (this.trackNumber == null) goto L60;
        r0.putInt(FIELD_TRACK_NUMBER, this.trackNumber.intValue());
    L60:
        if (this.totalTrackCount == null) goto L63;
        r0.putInt(FIELD_TOTAL_TRACK_COUNT, this.totalTrackCount.intValue());
    L63:
        if (this.folderType == null) goto L66;
        r0.putInt(FIELD_FOLDER_TYPE, this.folderType.intValue());
    L66:
        if (this.isBrowsable == null) goto L69;
        r0.putBoolean(FIELD_IS_BROWSABLE, this.isBrowsable.booleanValue());
    L69:
        if (this.isPlayable == null) goto L72;
        r0.putBoolean(FIELD_IS_PLAYABLE, this.isPlayable.booleanValue());
    L72:
        if (this.recordingYear == null) goto L75;
        r0.putInt(FIELD_RECORDING_YEAR, this.recordingYear.intValue());
    L75:
        if (this.recordingMonth == null) goto L78;
        r0.putInt(FIELD_RECORDING_MONTH, this.recordingMonth.intValue());
    L78:
        if (this.recordingDay == null) goto L81;
        r0.putInt(FIELD_RECORDING_DAY, this.recordingDay.intValue());
    L81:
        if (this.releaseYear == null) goto L84;
        r0.putInt(FIELD_RELEASE_YEAR, this.releaseYear.intValue());
    L84:
        if (this.releaseMonth == null) goto L87;
        r0.putInt(FIELD_RELEASE_MONTH, this.releaseMonth.intValue());
    L87:
        if (this.releaseDay == null) goto L90;
        r0.putInt(FIELD_RELEASE_DAY, this.releaseDay.intValue());
    L90:
        if (this.discNumber == null) goto L93;
        r0.putInt(FIELD_DISC_NUMBER, this.discNumber.intValue());
    L93:
        if (this.totalDiscCount == null) goto L96;
        r0.putInt(FIELD_TOTAL_DISC_COUNT, this.totalDiscCount.intValue());
    L96:
        if (this.artworkDataType == null) goto L99;
        r0.putInt(FIELD_ARTWORK_DATA_TYPE, this.artworkDataType.intValue());
    L99:
        if (this.mediaType == null) goto L102;
        r0.putInt(FIELD_MEDIA_TYPE, this.mediaType.intValue());
    L102:
        if (this.supportedCommands.isEmpty() == true) goto L105;
        r0.putStringArrayList(FIELD_SUPPORTED_COMMANDS, new ArrayList(this.supportedCommands));
    L105:
        if (this.extras == null) goto L107;
        r0.putBundle(FIELD_EXTRAS, this.extras);
    L107:
        return r0;
    }
}
