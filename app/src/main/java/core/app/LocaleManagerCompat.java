package androidx.core.app;

import android.app.LocaleManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.LocaleList;
import androidx.core.os.BuildCompat;
import androidx.core.os.LocaleListCompat;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public final class LocaleManagerCompat {

    static class Api21Impl {
        private Api21Impl() {
        }

        static String toLanguageTag(Locale r1) {
            return r1.toLanguageTag();
        }
    }

    static class Api24Impl {
        private Api24Impl() {
        }

        static LocaleListCompat getLocales(Configuration r1) {
            return LocaleListCompat.forLanguageTags(r1.getLocales().toLanguageTags());
        }
    }

    static class Api33Impl {
        private Api33Impl() {
        }

        static LocaleList localeManagerGetSystemLocales(Object r2) {
            return ((LocaleManager) r2).getSystemLocales();
        }
    }

    private LocaleManagerCompat() {
    }

    static LocaleListCompat getConfigurationLocales(Configuration r2) {
        if (Build.VERSION.SDK_INT < 24) goto L7;
        return Api24Impl.getLocales(r2);
    L7:
        return LocaleListCompat.forLanguageTags(Api21Impl.toLanguageTag(r2.locale));
    }

    private static Object getLocaleManagerForApplication(Context r1) {
        return r1.getSystemService("locale");
    }

    public static LocaleListCompat getSystemLocales(Context r3) {
        LocaleListCompat r0 = LocaleListCompat.getEmptyLocaleList();
        if (BuildCompat.isAtLeastT() == false) goto L9;
        Object r1 = getLocaleManagerForApplication(r3);
        if (r1 != null) goto L10;
        return r0;
    L10:
        return LocaleListCompat.wrap(Api33Impl.localeManagerGetSystemLocales(r1));
    L9:
        return getConfigurationLocales(r3.getApplicationContext().getResources().getConfiguration());
    }
}
