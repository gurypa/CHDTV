package androidx.media3.database;

import android.database.SQLException;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class DatabaseIOException extends IOException {
    public DatabaseIOException(SQLException r1) {
        super(r1);
    }

    public DatabaseIOException(SQLException r1, String r2) {
        super(r2, r1);
    }
}
