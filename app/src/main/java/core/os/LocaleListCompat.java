package androidx.core.os;

import android.os.Build;
import android.os.LocaleList;
import androidx.core.text.ICUCompat;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public final class LocaleListCompat {
    private static final LocaleListCompat sEmptyLocaleList = null;
    private final LocaleListInterface mImpl;

    static class Api21Impl {
        private static final Locale[] PSEUDO_LOCALE = null;

        static {
            PSEUDO_LOCALE = new Locale[]{new Locale("en", "XA"), new Locale("ar", "XB")};
        }

        private Api21Impl() {
        }

        static Locale forLanguageTag(String r1) {
            return Locale.forLanguageTag(r1);
        }

        private static boolean isPseudoLocale(Locale r6) {
            Locale[] r0 = PSEUDO_LOCALE;
            int r1 = r0.length;
            int r3 = 0;
        L3:
            if (r3 >= r1) goto L9;
            if (r0[r3].equals(r6) == true) goto L6;
            r3 = r3 + 1;
            goto L3
        L6:
            return true;
        L9:
            return false;
        }

        static boolean matchesLanguageAndScript(Locale r5, Locale r6) {
            if (r5.equals(r6) == false) goto L6;
            return true;
        L6:
            if (r5.getLanguage().equals(r6.getLanguage()) == true) goto L9;
            return false;
        L9:
            if (isPseudoLocale(r5) == false) goto L11;
        L24:
            return false;
        L11:
            if (isPseudoLocale(r6) == true) goto L24;
            String r0 = ICUCompat.maximizeAndGetScript(r5);
            if (r0.isEmpty() == false) goto L23;
            String r3 = r5.getCountry();
            if (r3.isEmpty() == false) goto L18;
            return true;
        L18:
            if (r3.equals(r6.getCountry()) == false) goto L21;
            return true;
        L21:
            return false;
        L23:
            return r0.equals(ICUCompat.maximizeAndGetScript(r6));
        }
    }

    static class Api24Impl {
        private Api24Impl() {
        }

        static LocaleList createLocaleList(Locale... r1) {
            return new LocaleList(r1);
        }

        static LocaleList getAdjustedDefault() {
            return LocaleList.getAdjustedDefault();
        }

        static LocaleList getDefault() {
            return LocaleList.getDefault();
        }
    }

    static {
        sEmptyLocaleList = create(new Locale[0]);
    }

    private LocaleListCompat(LocaleListInterface r1) {
        this.mImpl = r1;
    }

    public static LocaleListCompat create(Locale... r2) {
        if (Build.VERSION.SDK_INT < 24) goto L7;
        return wrap(Api24Impl.createLocaleList(r2));
    L7:
        return new LocaleListCompat(new LocaleListCompatWrapper(r2));
    }

    static Locale forLanguageTagCompat(String r6) {
        if (r6.contains("-") == false) goto L18;
        String[] r0 = r6.split("-", -1);
        if (r0.length <= 2) goto L9;
        return new Locale(r0[0], r0[1], r0[2]);
    L9:
        if (r0.length <= 1) goto L13;
        return new Locale(r0[0], r0[1]);
    L13:
        if (r0.length != 1) goto L33;
        return new Locale(r0[0]);
    L33:
        throw new IllegalArgumentException("Can not parse language tag: [" + r6 + "]");
    L18:
        if (r6.contains("_") == false) goto L35;
        String[] r1 = r6.split("_", -1);
        if (r1.length <= 2) goto L24;
        return new Locale(r1[0], r1[1], r1[2]);
    L24:
        if (r1.length <= 1) goto L28;
        return new Locale(r1[0], r1[1]);
    L28:
        if (r1.length != 1) goto L33;
        return new Locale(r1[0]);
    L35:
        return new Locale(r6);
    }

    public static LocaleListCompat forLanguageTags(String r4) {
        if (r4 == null) goto L13;
        if (r4.isEmpty() == true) goto L13;
        String[] r0 = r4.split(",", -1);
        Locale[] r1 = new Locale[r0.length];
        int r2 = 0;
    L8:
        if (r2 >= r1.length) goto L11;
        r1[r2] = Api21Impl.forLanguageTag(r0[r2]);
        r2 = r2 + 1;
        goto L8
    L11:
        return create(r1);
    L13:
        return getEmptyLocaleList();
    }

    public static LocaleListCompat getAdjustedDefault() {
        if (Build.VERSION.SDK_INT < 24) goto L7;
        return wrap(Api24Impl.getAdjustedDefault());
    L7:
        return create(new Locale[]{Locale.getDefault()});
    }

    public static LocaleListCompat getDefault() {
        if (Build.VERSION.SDK_INT < 24) goto L7;
        return wrap(Api24Impl.getDefault());
    L7:
        return create(new Locale[]{Locale.getDefault()});
    }

    public static LocaleListCompat getEmptyLocaleList() {
        return sEmptyLocaleList;
    }

    public static boolean matchesLanguageAndScript(Locale r1, Locale r2) {
        if (BuildCompat.isAtLeastT() == false) goto L7;
        return LocaleList.matchesLanguageAndScript(r1, r2);
    L7:
        return Api21Impl.matchesLanguageAndScript(r1, r2);
    }

    public static LocaleListCompat wrap(LocaleList r2) {
        return new LocaleListCompat(new LocaleListPlatformWrapper(r2));
    }

    @Deprecated
    public static LocaleListCompat wrap(Object r1) {
        return wrap((LocaleList) r1);
    }

    public boolean equals(Object r3) {
        if ((r3 instanceof LocaleListCompat) == true) goto L5;
    L7:
        return false;
    L5:
        if (this.mImpl.equals(((LocaleListCompat) r3).mImpl) == false) goto L7;
        return true;
    }

    public Locale get(int r2) {
        return this.mImpl.get(r2);
    }

    public Locale getFirstMatch(String[] r2) {
        return this.mImpl.getFirstMatch(r2);
    }

    public int hashCode() {
        return this.mImpl.hashCode();
    }

    public int indexOf(Locale r2) {
        return this.mImpl.indexOf(r2);
    }

    public boolean isEmpty() {
        return this.mImpl.isEmpty();
    }

    public int size() {
        return this.mImpl.size();
    }

    public String toLanguageTags() {
        return this.mImpl.toLanguageTags();
    }

    public String toString() {
        return this.mImpl.toString();
    }

    public Object unwrap() {
        return this.mImpl.getLocaleList();
    }
}
