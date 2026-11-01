package androidx.core.database.sqlite;

import android.database.sqlite.SQLiteDatabase;
import androidx.exifinterface.media.ExifInterface;
import androidx.media3.extractor.text.ttml.TtmlNode;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: SQLiteDatabase.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\u001c\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a>\u0010\u0000\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u0001*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u00042\u0017\u0010\u0005\u001a\u0013\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u0002H\u00010\u0006¢\u0006\u0002\b\u0007H\u0086\bø\u0001\u0000¢\u0006\u0002\u0010\b\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\t"}, m421d2 = {"transaction", ExifInterface.GPS_DIRECTION_TRUE, "Landroid/database/sqlite/SQLiteDatabase;", "exclusive", "", TtmlNode.TAG_BODY, "Lkotlin/Function1;", "Lkotlin/ExtensionFunctionType;", "(Landroid/database/sqlite/SQLiteDatabase;ZLkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "core-ktx_release"}, m422k = 2, m423mv = {1, 7, 1}, m425xi = 48)
public final class SQLiteDatabaseKt {
    public static final <T> T transaction(SQLiteDatabase r3, boolean r4, Function1<? super SQLiteDatabase, ? extends T> r5) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r5, TtmlNode.TAG_BODY);
        if (r4 == false) goto L5;
        r3.beginTransaction();
    L13:
        T r1 = r5.invoke(r3);     // Catch: Throwable -> L10
        r3.setTransactionSuccessful();     // Catch: Throwable -> L10
        r3.endTransaction();
        return r1;
    L10:
        th = move-exception;
        r3.endTransaction();
        throw th;
    L5:
        r3.beginTransactionNonExclusive();
        goto L13
    }

    public static /* synthetic */ Object transaction$default(SQLiteDatabase r1, boolean r2, Function1 r3, int r4, Object r5) {
        if ((r4 & 1) == 0) goto L5;
        r2 = true;
    L5:
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r3, TtmlNode.TAG_BODY);
        if (r2 == false) goto L8;
        r1.beginTransaction();
    L16:
        Object r6 = r3.invoke(r1);     // Catch: Throwable -> L13
        r1.setTransactionSuccessful();     // Catch: Throwable -> L13
        r1.endTransaction();
        return r6;
    L13:
        th = move-exception;
        r1.endTransaction();
        throw th;
    L8:
        r1.beginTransactionNonExclusive();
        goto L16
    }
}
