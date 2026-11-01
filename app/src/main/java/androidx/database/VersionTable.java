package androidx.media3.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import androidx.media3.common.MediaLibraryInfo;
import androidx.media3.common.util.Util;

/* JADX INFO: loaded from: classes.dex */
public final class VersionTable {
    private static final String COLUMN_FEATURE = "feature";
    private static final String COLUMN_INSTANCE_UID = "instance_uid";
    private static final String COLUMN_VERSION = "version";
    public static final int FEATURE_CACHE_CONTENT_METADATA = 1;
    public static final int FEATURE_CACHE_FILE_METADATA = 2;
    public static final int FEATURE_EXTERNAL = 1000;
    public static final int FEATURE_OFFLINE = 0;
    private static final String PRIMARY_KEY = "PRIMARY KEY (feature, instance_uid)";
    private static final String SQL_CREATE_TABLE_IF_NOT_EXISTS = "CREATE TABLE IF NOT EXISTS ExoPlayerVersions (feature INTEGER NOT NULL,instance_uid TEXT NOT NULL,version INTEGER NOT NULL,PRIMARY KEY (feature, instance_uid))";
    private static final String TABLE_NAME = "ExoPlayerVersions";
    public static final int VERSION_UNSET = -1;
    private static final String WHERE_FEATURE_AND_INSTANCE_UID_EQUALS = "feature = ? AND instance_uid = ?";

    static {
        MediaLibraryInfo.registerModule("media3.database");
    }

    private VersionTable() {
    }

    private static String[] featureAndInstanceUidArguments(int r1, String r2) {
        return new String[]{Integer.toString(r1), r2};
    }

    public static int getVersion(SQLiteDatabase r10, int r11, String r12) throws DatabaseIOException {
    L27:
        e = move-exception;
        SQLException r13 = e;
    L30:
        throw new DatabaseIOException(r13);
    L3:
        if (Util.tableExists(r10, TABLE_NAME) == true) goto L5;
        return -1;
    L5:
        Cursor r14 = r10.query(TABLE_NAME, new String[]{COLUMN_VERSION}, WHERE_FEATURE_AND_INSTANCE_UID_EQUALS, featureAndInstanceUidArguments(r11, r12), null, null, null);     // Catch: SQLException -> L25
    L17:
        th = move-exception;
        if (r14 == null) goto L38;
        r14.close();     // Catch: Throwable -> L22
    L39:
        throw th;     // Catch: SQLException -> L25
    L22:
        th = move-exception;
        th.addSuppressed(th);     // Catch: SQLException -> L25
        throw th;     // Catch: SQLException -> L25
    L38:
        throw th;     // Catch: SQLException -> L25
    L9:
        if (r14.getCount() != 0) goto L13;
        if (r14 == null) goto L12;
        r14.close();     // Catch: SQLException -> L25
    L12:
        return -1;
    L13:
        r14.moveToNext();     // Catch: Throwable -> L17
        int r0 = r14.getInt(0);     // Catch: Throwable -> L17
        if (r14 == null) goto L16;
        r14.close();     // Catch: SQLException -> L25
    L16:
        return r0;
    L25:
        e = move-exception;
        r13 = e;
        goto L30
    }

    public static void removeVersion(SQLiteDatabase r3, int r4, String r5) throws DatabaseIOException {
    L8:
        e = move-exception;
        throw new DatabaseIOException(e);
    L4:
        if (Util.tableExists(r3, TABLE_NAME) == true) goto L6;
        return;
    L6:
        r3.delete(TABLE_NAME, WHERE_FEATURE_AND_INSTANCE_UID_EQUALS, featureAndInstanceUidArguments(r4, r5));     // Catch: SQLException -> L8
    }

    public static void setVersion(SQLiteDatabase r3, int r4, String r5, int r6) throws DatabaseIOException {
        r3.execSQL(SQL_CREATE_TABLE_IF_NOT_EXISTS);     // Catch: SQLException -> L4
        ContentValues r0 = new ContentValues();     // Catch: SQLException -> L4
        r0.put(COLUMN_FEATURE, Integer.valueOf(r4));     // Catch: SQLException -> L4
        r0.put(COLUMN_INSTANCE_UID, r5);     // Catch: SQLException -> L4
        r0.put(COLUMN_VERSION, Integer.valueOf(r6));     // Catch: SQLException -> L4
        r3.replaceOrThrow(TABLE_NAME, null, r0);     // Catch: SQLException -> L4
        return;
    L4:
        e = move-exception;
        throw new DatabaseIOException(e);
    }
}
