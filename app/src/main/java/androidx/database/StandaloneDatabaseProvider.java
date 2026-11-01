package androidx.media3.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.media3.common.util.Log;

/* JADX INFO: loaded from: classes.dex */
public class StandaloneDatabaseProvider extends SQLiteOpenHelper implements DatabaseProvider {
    public static final String DATABASE_NAME = "exoplayer_internal.db";
    private static final String TAG = "SADatabaseProvider";
    private static final int VERSION = 1;

    public StandaloneDatabaseProvider(Context r5) {
        super(r5.getApplicationContext(), DATABASE_NAME, null, 1);
    }

    private static void wipeDatabase(SQLiteDatabase r9) {
        Cursor r10 = r9.query("sqlite_master", new String[]{"type", "name"}, null, null, null, null, null);
    L27:
        if (r10.moveToNext() == false) goto L14;
        String r0 = r10.getString(0);     // Catch: Throwable -> L17
        String r1 = r10.getString(1);     // Catch: Throwable -> L17
        if ("sqlite_sequence".equals(r1) == true) goto L27;
        String r2 = "DROP " + r0 + " IF EXISTS " + r1;     // Catch: Throwable -> L17
        r9.execSQL(r2);     // Catch: SQLException -> L11 Throwable -> L17
    L11:
        e = move-exception;
        Log.m45e(TAG, "Error executing " + r2, e);     // Catch: Throwable -> L17
        goto L27
    L14:
        if (r10 == null) goto L37;
        r10.close();
        return;
    L37:
        return;
    L17:
        th = move-exception;
        if (r10 == null) goto L38;
        r10.close();     // Catch: Throwable -> L22
        throw th;
    L22:
        th = move-exception;
        th.addSuppressed(th);
        throw th;
    L38:
        throw th;
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase r1) {
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onDowngrade(SQLiteDatabase r1, int r2, int r3) {
        wipeDatabase(r1);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase r1, int r2, int r3) {
    }
}
