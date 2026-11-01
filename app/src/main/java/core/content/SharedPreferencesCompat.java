package androidx.core.content;

import android.content.SharedPreferences;

/* JADX INFO: loaded from: classes.dex */
@Deprecated
public final class SharedPreferencesCompat {

    @Deprecated
    public static final class EditorCompat {
        private static EditorCompat sInstance;
        private final Helper mHelper;

        private static class Helper {
            Helper() {
            }

            public void apply(SharedPreferences.Editor r2) {
                r2.apply();     // Catch: AbstractMethodError -> L4
                return;
            L5:
                r2.commit();
            }
        }

        private EditorCompat() {
            this.mHelper = new Helper();
        }

        @Deprecated
        public static EditorCompat getInstance() {
            if (sInstance != null) goto L6;
            sInstance = new EditorCompat();
        L6:
            return sInstance;
        }

        @Deprecated
        public void apply(SharedPreferences.Editor r2) {
            this.mHelper.apply(r2);
        }
    }

    private SharedPreferencesCompat() {
    }
}
