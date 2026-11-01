package androidx.media3.datasource.cache;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import androidx.media3.common.util.Assertions;
import androidx.media3.database.DatabaseIOException;
import androidx.media3.database.DatabaseProvider;
import androidx.media3.database.VersionTable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
final class CacheFileMetadataIndex {
    private static final String[] COLUMNS = null;
    private static final int COLUMN_INDEX_LAST_TOUCH_TIMESTAMP = 2;
    private static final int COLUMN_INDEX_LENGTH = 1;
    private static final int COLUMN_INDEX_NAME = 0;
    private static final String COLUMN_LAST_TOUCH_TIMESTAMP = "last_touch_timestamp";
    private static final String COLUMN_LENGTH = "length";
    private static final String COLUMN_NAME = "name";
    private static final String TABLE_PREFIX = "ExoPlayerCacheFileMetadata";
    private static final String TABLE_SCHEMA = "(name TEXT PRIMARY KEY NOT NULL,length INTEGER NOT NULL,last_touch_timestamp INTEGER NOT NULL)";
    private static final int TABLE_VERSION = 1;
    private static final String WHERE_NAME_EQUALS = "name = ?";
    private final DatabaseProvider databaseProvider;
    private String tableName;

    static {
        COLUMNS = new String[]{COLUMN_NAME, COLUMN_LENGTH, COLUMN_LAST_TOUCH_TIMESTAMP};
    }

    public CacheFileMetadataIndex(DatabaseProvider r1) {
        this.databaseProvider = r1;
    }

    public static void delete(DatabaseProvider r4, long r5) throws DatabaseIOException {
        String r0 = Long.toHexString(r5);
        String r1 = getTableName(r0);     // Catch: SQLException -> L11
        SQLiteDatabase r2 = r4.getWritableDatabase();     // Catch: SQLException -> L11
        r2.beginTransactionNonExclusive();     // Catch: SQLException -> L11
        VersionTable.removeVersion(r2, 2, r0);     // Catch: Throwable -> L8
        dropTable(r2, r1);     // Catch: Throwable -> L8
        r2.setTransactionSuccessful();     // Catch: Throwable -> L8
        r2.endTransaction();     // Catch: SQLException -> L11
        return;
    L8:
        th = move-exception;
        r2.endTransaction();     // Catch: SQLException -> L11
        throw th;     // Catch: SQLException -> L11
    L11:
        e = move-exception;
        throw new DatabaseIOException(e);
    }

    private static void dropTable(SQLiteDatabase r2, String r3) {
        r2.execSQL("DROP TABLE IF EXISTS " + r3);
    }

    private Cursor getCursor() {
        Assertions.checkNotNull(this.tableName);
        return this.databaseProvider.getReadableDatabase().query(this.tableName, COLUMNS, null, null, null, null, null);
    }

    private static String getTableName(String r2) {
        return TABLE_PREFIX + r2;
    }

    public Map<String, CacheFileMetadata> getAll() throws DatabaseIOException {
        Cursor r0 = getCursor();     // Catch: SQLException -> L19
        HashMap r1 = new HashMap(r0.getCount());     // Catch: Throwable -> L12
    L5:
        if (r0.moveToNext() == false) goto L9;
        r1.put((String) Assertions.checkNotNull(r0.getString(0)), new CacheFileMetadata(r0.getLong(1), r0.getLong(2)));     // Catch: Throwable -> L12
    L9:
        if (r0 == null) goto L11;
        r0.close();     // Catch: SQLException -> L19
    L11:
        return r1;
    L12:
        th = move-exception;
        if (r0 != null) goto L22;
    L18:
        throw th;     // Catch: SQLException -> L19
    L22:
        r0.close();     // Catch: Throwable -> L16
    L16:
        th = move-exception;
        th.addSuppressed(th);     // Catch: SQLException -> L19
    L19:
        e = move-exception;
        throw new DatabaseIOException(e);
    }

    public void initialize(long r7) throws DatabaseIOException {
        String r0 = Long.toHexString(r7);     // Catch: SQLException -> L12
        this.tableName = getTableName(r0);     // Catch: SQLException -> L12
        if (VersionTable.getVersion(this.databaseProvider.getReadableDatabase(), 2, r0) == 1) goto L11;
        SQLiteDatabase r5 = this.databaseProvider.getWritableDatabase();     // Catch: SQLException -> L12
        r5.beginTransactionNonExclusive();     // Catch: SQLException -> L12
        VersionTable.setVersion(r5, 2, r0, 1);     // Catch: Throwable -> L7
        dropTable(r5, this.tableName);     // Catch: Throwable -> L7
        r5.execSQL("CREATE TABLE " + this.tableName + " " + TABLE_SCHEMA);     // Catch: Throwable -> L7
        r5.setTransactionSuccessful();     // Catch: Throwable -> L7
        r5.endTransaction();     // Catch: SQLException -> L12
        return;
    L7:
        th = move-exception;
        r5.endTransaction();     // Catch: SQLException -> L12
        throw th;     // Catch: SQLException -> L12
    L11:
        return;
    L12:
        e = move-exception;
        throw new DatabaseIOException(e);
    }

    public void remove(String r5) throws DatabaseIOException {
        Assertions.checkNotNull(this.tableName);
        this.databaseProvider.getWritableDatabase().delete(this.tableName, WHERE_NAME_EQUALS, new String[]{r5});     // Catch: SQLException -> L5
        return;
    L5:
        e = move-exception;
        throw new DatabaseIOException(e);
    }

    public void removeAll(Set<String> r7) throws DatabaseIOException {
        Assertions.checkNotNull(this.tableName);
        SQLiteDatabase r0 = this.databaseProvider.getWritableDatabase();     // Catch: SQLException -> L14
        r0.beginTransactionNonExclusive();     // Catch: SQLException -> L14
        Iterator<String> r1 = r7.iterator();     // Catch: Throwable -> L11
    L6:
        if (r1.hasNext() == false) goto L8;
        String r2 = r1.next();     // Catch: Throwable -> L11
        r0.delete(this.tableName, WHERE_NAME_EQUALS, new String[]{r2});     // Catch: Throwable -> L11
        goto L6
    L8:
        r0.setTransactionSuccessful();     // Catch: Throwable -> L11
        r0.endTransaction();     // Catch: SQLException -> L14
        return;
    L11:
        th = move-exception;
        r0.endTransaction();     // Catch: SQLException -> L14
        throw th;     // Catch: SQLException -> L14
    L14:
        e = move-exception;
        throw new DatabaseIOException(e);
    }

    public void set(String r5, long r6, long r8) throws DatabaseIOException {
        Assertions.checkNotNull(this.tableName);
        SQLiteDatabase r0 = this.databaseProvider.getWritableDatabase();     // Catch: SQLException -> L5
        ContentValues r1 = new ContentValues();     // Catch: SQLException -> L5
        r1.put(COLUMN_NAME, r5);     // Catch: SQLException -> L5
        r1.put(COLUMN_LENGTH, Long.valueOf(r6));     // Catch: SQLException -> L5
        r1.put(COLUMN_LAST_TOUCH_TIMESTAMP, Long.valueOf(r8));     // Catch: SQLException -> L5
        r0.replaceOrThrow(this.tableName, null, r1);     // Catch: SQLException -> L5
        return;
    L5:
        e = move-exception;
        throw new DatabaseIOException(e);
    }
}
