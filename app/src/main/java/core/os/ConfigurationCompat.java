package androidx.core.os;

import android.content.res.Configuration;
import android.os.Build;
import android.os.LocaleList;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public final class ConfigurationCompat {

    static class Api24Impl {
        private Api24Impl() {
        }

        static LocaleList getLocales(Configuration r1) {
            return r1.getLocales();
        }
    }

    private ConfigurationCompat() {
    }

    public static LocaleListCompat getLocales(Configuration r3) {
        if (Build.VERSION.SDK_INT < 24) goto L7;
        return LocaleListCompat.wrap(Api24Impl.getLocales(r3));
    L7:
        return LocaleListCompat.create(new Locale[]{r3.locale});
    }
}
