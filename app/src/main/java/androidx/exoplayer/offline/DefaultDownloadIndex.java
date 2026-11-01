package androidx.media3.exoplayer.offline;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.text.TextUtils;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.StreamKey;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import androidx.media3.database.DatabaseIOException;
import androidx.media3.database.DatabaseProvider;
import androidx.media3.database.VersionTable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class DefaultDownloadIndex implements WritableDownloadIndex {
    private static final String[] COLUMNS = null;
    private static final String COLUMN_BYTES_DOWNLOADED = "bytes_downloaded";
    private static final String COLUMN_CONTENT_LENGTH = "content_length";
    private static final String COLUMN_CUSTOM_CACHE_KEY = "custom_cache_key";
    private static final String COLUMN_DATA = "data";
    private static final String COLUMN_FAILURE_REASON = "failure_reason";
    private static final String COLUMN_ID = "id";
    private static final int COLUMN_INDEX_BYTES_DOWNLOADED = 13;
    private static final int COLUMN_INDEX_CONTENT_LENGTH = 9;
    private static final int COLUMN_INDEX_CUSTOM_CACHE_KEY = 4;
    private static final int COLUMN_INDEX_DATA = 5;
    private static final int COLUMN_INDEX_FAILURE_REASON = 11;
    private static final int COLUMN_INDEX_ID = 0;
    private static final int COLUMN_INDEX_KEY_SET_ID = 14;
    private static final int COLUMN_INDEX_MIME_TYPE = 1;
    private static final int COLUMN_INDEX_PERCENT_DOWNLOADED = 12;
    private static final int COLUMN_INDEX_START_TIME_MS = 7;
    private static final int COLUMN_INDEX_STATE = 6;
    private static final int COLUMN_INDEX_STOP_REASON = 10;
    private static final int COLUMN_INDEX_STREAM_KEYS = 3;
    private static final int COLUMN_INDEX_UPDATE_TIME_MS = 8;
    private static final int COLUMN_INDEX_URI = 2;
    private static final String COLUMN_KEY_SET_ID = "key_set_id";
    private static final String COLUMN_MIME_TYPE = "mime_type";
    private static final String COLUMN_PERCENT_DOWNLOADED = "percent_downloaded";
    private static final String COLUMN_START_TIME_MS = "start_time_ms";
    private static final String COLUMN_STATE = "state";
    private static final String COLUMN_STOP_REASON = "stop_reason";
    private static final String COLUMN_STREAM_KEYS = "stream_keys";
    private static final String COLUMN_UPDATE_TIME_MS = "update_time_ms";
    private static final String COLUMN_URI = "uri";
    private static final String TABLE_PREFIX = "ExoPlayerDownloads";
    private static final String TABLE_SCHEMA = "(id TEXT PRIMARY KEY NOT NULL,mime_type TEXT,uri TEXT NOT NULL,stream_keys TEXT NOT NULL,custom_cache_key TEXT,data BLOB NOT NULL,state INTEGER NOT NULL,start_time_ms INTEGER NOT NULL,update_time_ms INTEGER NOT NULL,content_length INTEGER NOT NULL,stop_reason INTEGER NOT NULL,failure_reason INTEGER NOT NULL,percent_downloaded REAL NOT NULL,bytes_downloaded INTEGER NOT NULL,key_set_id BLOB NOT NULL)";
    static final int TABLE_VERSION = 3;
    private static final String TRUE = "1";
    private static final String WHERE_ID_EQUALS = "id = ?";
    private static final String WHERE_STATE_IS_DOWNLOADING = "state = 2";
    private static final String WHERE_STATE_IS_TERMINAL = null;
    private final DatabaseProvider databaseProvider;
    private final Object initializationLock;
    private boolean initialized;
    private final String name;
    private final String tableName;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.offline.DefaultDownloadIndex$1 */
    static /* synthetic */ class C03131 {
    }

    private static final class DownloadCursorImpl implements DownloadCursor {
        private final Cursor cursor;

        private DownloadCursorImpl(Cursor r1) {
            this.cursor = r1;
        }

        /* synthetic */ DownloadCursorImpl(Cursor r1, C03131 r2) {
            this(r1);
        }

        @Override // androidx.media3.exoplayer.offline.DownloadCursor, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            this.cursor.close();
        }

        @Override // androidx.media3.exoplayer.offline.DownloadCursor
        public int getCount() {
            return this.cursor.getCount();
        }

        @Override // androidx.media3.exoplayer.offline.DownloadCursor
        public Download getDownload() {
            return DefaultDownloadIndex.access$100(this.cursor);
        }

        @Override // androidx.media3.exoplayer.offline.DownloadCursor
        public int getPosition() {
            return this.cursor.getPosition();
        }

        @Override // androidx.media3.exoplayer.offline.DownloadCursor
        public /* synthetic */ boolean isAfterLast() {
            return DownloadCursor.CC.$default$isAfterLast(this);
        }

        @Override // androidx.media3.exoplayer.offline.DownloadCursor
        public /* synthetic */ boolean isBeforeFirst() {
            return DownloadCursor.CC.$default$isBeforeFirst(this);
        }

        @Override // androidx.media3.exoplayer.offline.DownloadCursor
        public boolean isClosed() {
            return this.cursor.isClosed();
        }

        @Override // androidx.media3.exoplayer.offline.DownloadCursor
        public /* synthetic */ boolean isFirst() {
            return DownloadCursor.CC.$default$isFirst(this);
        }

        @Override // androidx.media3.exoplayer.offline.DownloadCursor
        public /* synthetic */ boolean isLast() {
            return DownloadCursor.CC.$default$isLast(this);
        }

        @Override // androidx.media3.exoplayer.offline.DownloadCursor
        public /* synthetic */ boolean moveToFirst() {
            return DownloadCursor.CC.$default$moveToFirst(this);
        }

        @Override // androidx.media3.exoplayer.offline.DownloadCursor
        public /* synthetic */ boolean moveToLast() {
            return DownloadCursor.CC.$default$moveToLast(this);
        }

        @Override // androidx.media3.exoplayer.offline.DownloadCursor
        public /* synthetic */ boolean moveToNext() {
            return DownloadCursor.CC.$default$moveToNext(this);
        }

        @Override // androidx.media3.exoplayer.offline.DownloadCursor
        public boolean moveToPosition(int r2) {
            return this.cursor.moveToPosition(r2);
        }

        @Override // androidx.media3.exoplayer.offline.DownloadCursor
        public /* synthetic */ boolean moveToPrevious() {
            return DownloadCursor.CC.$default$moveToPrevious(this);
        }
    }

    static {
        WHERE_STATE_IS_TERMINAL = getStateQuery(new int[]{3, 4});
        COLUMNS = new String[]{"id", COLUMN_MIME_TYPE, COLUMN_URI, COLUMN_STREAM_KEYS, COLUMN_CUSTOM_CACHE_KEY, "data", COLUMN_STATE, COLUMN_START_TIME_MS, COLUMN_UPDATE_TIME_MS, COLUMN_CONTENT_LENGTH, "stop_reason", COLUMN_FAILURE_REASON, COLUMN_PERCENT_DOWNLOADED, COLUMN_BYTES_DOWNLOADED, COLUMN_KEY_SET_ID};
    }

    public DefaultDownloadIndex(DatabaseProvider r2) {
        this(r2, "");
    }

    public DefaultDownloadIndex(DatabaseProvider r3, String r4) {
        this.name = r4;
        this.databaseProvider = r3;
        this.tableName = TABLE_PREFIX + r4;
        this.initializationLock = new Object();
    }

    static /* synthetic */ Download access$100(Cursor r1) {
        return getDownloadForCurrentRow(r1);
    }

    private static List<StreamKey> decodeStreamKeys(String r11) {
        ArrayList r0 = new ArrayList();
        if (TextUtils.isEmpty(r11) == false) goto L5;
        return r0;
    L5:
        String[] r1 = Util.split(r11, ",");
        int r2 = r1.length;
        int r4 = 0;
    L6:
        if (r4 >= r2) goto L12;
        String[] r6 = Util.split(r1[r4], "\\.");
        if (r6.length != 3) goto L10;
        boolean r7 = true;
    L11:
        Assertions.checkState(r7);
        r0.add(new StreamKey(Integer.parseInt(r6[0]), Integer.parseInt(r6[1]), Integer.parseInt(r6[2])));
        r4 = r4 + 1;
        goto L6
    L10:
        r7 = false;
        goto L11
    L12:
        return r0;
    }

    static String encodeStreamKeys(List<StreamKey> r6) {
        StringBuilder r0 = new StringBuilder();
        int r1 = 0;
    L4:
        if (r1 >= r6.size()) goto L7;
        StreamKey r2 = r6.get(r1);
        r0.append(r2.periodIndex).append('.').append(r2.groupIndex).append('.').append(r2.streamIndex).append(',');
        r1 = r1 + 1;
        goto L4
    L7:
        if (r0.length() <= 0) goto L10;
        r0.setLength(r0.length() - 1);
    L10:
        return r0.toString();
    }

    private void ensureInitialized() throws DatabaseIOException {
        Object r0 = this.initializationLock;
        monitor-enter(r0);
    L30:
        th = move-exception;
        throw th;
    L5:
        if (this.initialized == false) goto L33;
        monitor-exit(r0);     // Catch: Throwable -> L30
        return;
    L33:
        int r2 = VersionTable.getVersion(this.databaseProvider.getReadableDatabase(), 0, this.name);     // Catch: SQLException -> L27 Throwable -> L30
        if (r2 == 3) goto L24;
        SQLiteDatabase r5 = this.databaseProvider.getWritableDatabase();     // Catch: SQLException -> L27 Throwable -> L30
        r5.beginTransactionNonExclusive();     // Catch: SQLException -> L27 Throwable -> L30
        VersionTable.setVersion(r5, 0, this.name, 3);     // Catch: Throwable -> L21
        if (r2 != 2) goto L14;
        List<Download> r3 = loadDownloadsFromVersion2(r5);     // Catch: Throwable -> L21
    L15:
        r5.execSQL("DROP TABLE IF EXISTS " + this.tableName);     // Catch: Throwable -> L21
        r5.execSQL("CREATE TABLE " + this.tableName + " " + TABLE_SCHEMA);     // Catch: Throwable -> L21
        Iterator<Download> r4 = r3.iterator();     // Catch: Throwable -> L21
    L17:
        if (r4.hasNext() == false) goto L19;
        putDownloadInternal(r4.next(), r5);     // Catch: Throwable -> L21
        goto L17
    L19:
        r5.setTransactionSuccessful();     // Catch: Throwable -> L21
        r5.endTransaction();     // Catch: SQLException -> L27 Throwable -> L30
        goto L24
    L14:
        r3 = new ArrayList();     // Catch: Throwable -> L21
    L21:
        th = move-exception;
        r5.endTransaction();     // Catch: SQLException -> L27 Throwable -> L30
        throw th;     // Catch: SQLException -> L27 Throwable -> L30
    L24:
        this.initialized = true;     // Catch: SQLException -> L27 Throwable -> L30
        monitor-exit(r0);     // Catch: Throwable -> L30
        return;
    L27:
        e = move-exception;
        throw new DatabaseIOException(e);     // Catch: Throwable -> L30
    }

    private Cursor getCursor(String r10, String[] r11) throws DatabaseIOException {
        return this.databaseProvider.getReadableDatabase().query(this.tableName, COLUMNS, r10, r11, null, null, "start_time_ms ASC");
    L6:
        e = move-exception;
        SQLiteException r12 = e;
    L11:
        throw new DatabaseIOException(r12);
    L8:
        e = move-exception;
        r12 = e;
        goto L11
    }

    private static Download getDownloadForCurrentRow(Cursor r17) {
        byte[] r1 = r17.getBlob(14);
        DownloadRequest.Builder r2 = new DownloadRequest.Builder((String) Assertions.checkNotNull(r17.getString(0)), Uri.parse((String) Assertions.checkNotNull(r17.getString(2)))).setMimeType(r17.getString(1)).setStreamKeys(decodeStreamKeys(r17.getString(3)));
        if (r1.length <= 0) goto L5;
        byte[] r4 = r1;
    L6:
        DownloadRequest r6 = r2.setKeySetId(r4).setCustomCacheKey(r17.getString(4)).setData(r17.getBlob(5)).build();
        DownloadProgress r16 = new DownloadProgress();
        r16.bytesDownloaded = r17.getLong(13);
        r16.percentDownloaded = r17.getFloat(12);
        int r7 = r17.getInt(6);
        if (r7 != 4) goto L9;
        int r15 = r17.getInt(11);
    L11:
        return new Download(r6, r7, r17.getLong(7), r17.getLong(8), r17.getLong(9), r17.getInt(10), r15, r16);
    L9:
        r15 = 0;
        goto L11
    L5:
        r4 = null;
        goto L6
    }

    private static Download getDownloadForCurrentRowV2(Cursor r15) {
        int r1 = 0;
        DownloadRequest r4 = new DownloadRequest.Builder((String) Assertions.checkNotNull(r15.getString(0)), Uri.parse((String) Assertions.checkNotNull(r15.getString(2)))).setMimeType(inferMimeType(r15.getString(1))).setStreamKeys(decodeStreamKeys(r15.getString(3))).setCustomCacheKey(r15.getString(4)).setData(r15.getBlob(5)).build();
        DownloadProgress r14 = new DownloadProgress();
        r14.bytesDownloaded = r15.getLong(13);
        r14.percentDownloaded = r15.getFloat(12);
        int r5 = r15.getInt(6);
        if (r5 != 4) goto L5;
        r1 = r15.getInt(11);
    L5:
        long r6 = r15.getLong(7);
        long r8 = r15.getLong(8);
        long r10 = r15.getLong(9);
        int r12 = r15.getInt(10);
        return new Download(r4, r5, r6, r8, r10, r12, r1, r14);
    }

    private static String getStateQuery(int... r3) {
        if (r3.length != 0) goto L6;
        return "1";
    L6:
        StringBuilder r0 = new StringBuilder();
        r0.append(COLUMN_STATE).append(" IN (");
        int r1 = 0;
    L8:
        if (r1 >= r3.length) goto L12;
        if (r1 <= 0) goto L11;
        r0.append(',');
    L11:
        r0.append(r3[r1]);
        r1 = r1 + 1;
        goto L8
    L12:
        r0.append(')');
        return r0.toString();
    }

    private static String inferMimeType(String r1) {
        if ("dash".equals(r1) == false) goto L7;
        return MimeTypes.APPLICATION_MPD;
    L7:
        if ("hls".equals(r1) == false) goto L11;
        return MimeTypes.APPLICATION_M3U8;
    L11:
        if ("ss".equals(r1) == false) goto L14;
        return MimeTypes.APPLICATION_SS;
    L14:
        return MimeTypes.VIDEO_UNKNOWN;
    }

    private List<Download> loadDownloadsFromVersion2(SQLiteDatabase r19) {
        ArrayList r0 = new ArrayList();
        if (Util.tableExists(r19, this.tableName) == true) goto L5;
        return r0;
    L5:
        String[] r5 = {"id", "title", COLUMN_URI, COLUMN_STREAM_KEYS, COLUMN_CUSTOM_CACHE_KEY, "data", COLUMN_STATE, COLUMN_START_TIME_MS, COLUMN_UPDATE_TIME_MS, COLUMN_CONTENT_LENGTH, "stop_reason", COLUMN_FAILURE_REASON, COLUMN_PERCENT_DOWNLOADED, COLUMN_BYTES_DOWNLOADED};
        Cursor r4 = r19.query(this.tableName, r5, null, null, null, null, null);
    L24:
    L14:
        th = move-exception;
        if (r4 == null) goto L28;
        r4.close();     // Catch: Throwable -> L19
        throw th;
    L19:
        th = move-exception;
        th.addSuppressed(th);
        throw th;
    L28:
        throw th;
    L7:
        if (r4.moveToNext() == false) goto L11;
        r0.add(getDownloadForCurrentRowV2(r4));     // Catch: Throwable -> L14
        goto L24
    L11:
        if (r4 == null) goto L13;
        r4.close();
    L13:
        return r0;
    }

    private void putDownloadInternal(Download r5, SQLiteDatabase r6) {
        if (r5.request.keySetId != null) goto L5;
        byte[] r0 = Util.EMPTY_BYTE_ARRAY;
    L6:
        ContentValues r1 = new ContentValues();
        r1.put("id", r5.request.f41id);
        r1.put(COLUMN_MIME_TYPE, r5.request.mimeType);
        r1.put(COLUMN_URI, r5.request.uri.toString());
        r1.put(COLUMN_STREAM_KEYS, encodeStreamKeys(r5.request.streamKeys));
        r1.put(COLUMN_CUSTOM_CACHE_KEY, r5.request.customCacheKey);
        r1.put("data", r5.request.data);
        r1.put(COLUMN_STATE, Integer.valueOf(r5.state));
        r1.put(COLUMN_START_TIME_MS, Long.valueOf(r5.startTimeMs));
        r1.put(COLUMN_UPDATE_TIME_MS, Long.valueOf(r5.updateTimeMs));
        r1.put(COLUMN_CONTENT_LENGTH, Long.valueOf(r5.contentLength));
        r1.put("stop_reason", Integer.valueOf(r5.stopReason));
        r1.put(COLUMN_FAILURE_REASON, Integer.valueOf(r5.failureReason));
        r1.put(COLUMN_PERCENT_DOWNLOADED, Float.valueOf(r5.getPercentDownloaded()));
        r1.put(COLUMN_BYTES_DOWNLOADED, Long.valueOf(r5.getBytesDownloaded()));
        r1.put(COLUMN_KEY_SET_ID, r0);
        r6.replaceOrThrow(this.tableName, null, r1);
        return;
    L5:
        r0 = r5.request.keySetId;
        goto L6
    }

    @Override // androidx.media3.exoplayer.offline.DownloadIndex
    public Download getDownload(String r4) throws DatabaseIOException {
        ensureInitialized();
        Cursor r0 = getCursor(WHERE_ID_EQUALS, new String[]{r4});     // Catch: SQLiteException -> L21
    L14:
        th = move-exception;
        if (r0 != null) goto L27;
    L20:
        throw th;     // Catch: SQLiteException -> L21
    L27:
        r0.close();     // Catch: Throwable -> L18
    L18:
        th = move-exception;
        th.addSuppressed(th);     // Catch: SQLiteException -> L21
        goto L20
    L5:
        if (r0.getCount() != 0) goto L10;
        if (r0 == null) goto L29;
        r0.close();     // Catch: SQLiteException -> L21
        return null;
    L29:
        return null;
    L10:
        r0.moveToNext();     // Catch: Throwable -> L14
        Download r1 = getDownloadForCurrentRow(r0);     // Catch: Throwable -> L14
        if (r0 == null) goto L13;
        r0.close();     // Catch: SQLiteException -> L21
    L13:
        return r1;
    L21:
        e = move-exception;
        throw new DatabaseIOException(e);
    }

    @Override // androidx.media3.exoplayer.offline.DownloadIndex
    public DownloadCursor getDownloads(int... r4) throws DatabaseIOException {
        ensureInitialized();
        Cursor r0 = getCursor(getStateQuery(r4), null);
        return new DownloadCursorImpl(r0, null);
    }

    @Override // androidx.media3.exoplayer.offline.WritableDownloadIndex
    public void putDownload(Download r3) throws DatabaseIOException {
        ensureInitialized();
        putDownloadInternal(r3, this.databaseProvider.getWritableDatabase());     // Catch: SQLiteException -> L5
        return;
    L5:
        e = move-exception;
        throw new DatabaseIOException(e);
    }

    @Override // androidx.media3.exoplayer.offline.WritableDownloadIndex
    public void removeDownload(String r5) throws DatabaseIOException {
        ensureInitialized();
        this.databaseProvider.getWritableDatabase().delete(this.tableName, WHERE_ID_EQUALS, new String[]{r5});     // Catch: SQLiteException -> L5
        return;
    L5:
        e = move-exception;
        throw new DatabaseIOException(e);
    }

    @Override // androidx.media3.exoplayer.offline.WritableDownloadIndex
    public void setDownloadingStatesToQueued() throws DatabaseIOException {
        ensureInitialized();
        ContentValues r0 = new ContentValues();     // Catch: SQLException -> L5
        r0.put(COLUMN_STATE, 0);     // Catch: SQLException -> L5
        this.databaseProvider.getWritableDatabase().update(this.tableName, r0, WHERE_STATE_IS_DOWNLOADING, null);     // Catch: SQLException -> L5
        return;
    L5:
        e = move-exception;
        throw new DatabaseIOException(e);
    }

    @Override // androidx.media3.exoplayer.offline.WritableDownloadIndex
    public void setStatesToRemoving() throws DatabaseIOException {
        ensureInitialized();
        ContentValues r0 = new ContentValues();     // Catch: SQLException -> L5
        r0.put(COLUMN_STATE, 5);     // Catch: SQLException -> L5
        r0.put(COLUMN_FAILURE_REASON, 0);     // Catch: SQLException -> L5
        this.databaseProvider.getWritableDatabase().update(this.tableName, r0, null, null);     // Catch: SQLException -> L5
        return;
    L5:
        e = move-exception;
        throw new DatabaseIOException(e);
    }

    @Override // androidx.media3.exoplayer.offline.WritableDownloadIndex
    public void setStopReason(int r6) throws DatabaseIOException {
        ensureInitialized();
        ContentValues r0 = new ContentValues();     // Catch: SQLException -> L5
        r0.put("stop_reason", Integer.valueOf(r6));     // Catch: SQLException -> L5
        this.databaseProvider.getWritableDatabase().update(this.tableName, r0, WHERE_STATE_IS_TERMINAL, null);     // Catch: SQLException -> L5
        return;
    L5:
        e = move-exception;
        throw new DatabaseIOException(e);
    }

    @Override // androidx.media3.exoplayer.offline.WritableDownloadIndex
    public void setStopReason(String r6, int r7) throws DatabaseIOException {
        ensureInitialized();
        ContentValues r0 = new ContentValues();     // Catch: SQLException -> L5
        r0.put("stop_reason", Integer.valueOf(r7));     // Catch: SQLException -> L5
        this.databaseProvider.getWritableDatabase().update(this.tableName, r0, WHERE_STATE_IS_TERMINAL + " AND " + WHERE_ID_EQUALS, new String[]{r6});     // Catch: SQLException -> L5
        return;
    L5:
        e = move-exception;
        throw new DatabaseIOException(e);
    }
}
