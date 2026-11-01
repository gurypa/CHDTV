package androidx.core.os;

import android.os.Environment;
import java.io.File;

/* JADX INFO: loaded from: classes.dex */
public final class EnvironmentCompat {
    public static final String MEDIA_UNKNOWN = "unknown";
    private static final String TAG = "EnvironmentCompat";

    static class Api19Impl {
        private Api19Impl() {
        }

        static String getStorageState(File r1) {
            return Environment.getStorageState(r1);
        }
    }

    static class Api21Impl {
        private Api21Impl() {
        }

        static String getExternalStorageState(File r1) {
            return Environment.getExternalStorageState(r1);
        }
    }

    private EnvironmentCompat() {
    }

    public static String getStorageState(File r1) {
        return Api21Impl.getExternalStorageState(r1);
    }
}
