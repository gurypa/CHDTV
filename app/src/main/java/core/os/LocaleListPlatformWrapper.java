package androidx.core.os;

import android.os.LocaleList;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
final class LocaleListPlatformWrapper implements LocaleListInterface {
    private final LocaleList mLocaleList;

    LocaleListPlatformWrapper(Object r2) {
        this.mLocaleList = (LocaleList) r2;
    }

    public boolean equals(Object r3) {
        return this.mLocaleList.equals(((LocaleListInterface) r3).getLocaleList());
    }

    @Override // androidx.core.os.LocaleListInterface
    public Locale get(int r2) {
        return this.mLocaleList.get(r2);
    }

    @Override // androidx.core.os.LocaleListInterface
    public Locale getFirstMatch(String[] r2) {
        return this.mLocaleList.getFirstMatch(r2);
    }

    @Override // androidx.core.os.LocaleListInterface
    public Object getLocaleList() {
        return this.mLocaleList;
    }

    public int hashCode() {
        return this.mLocaleList.hashCode();
    }

    @Override // androidx.core.os.LocaleListInterface
    public int indexOf(Locale r2) {
        return this.mLocaleList.indexOf(r2);
    }

    @Override // androidx.core.os.LocaleListInterface
    public boolean isEmpty() {
        return this.mLocaleList.isEmpty();
    }

    @Override // androidx.core.os.LocaleListInterface
    public int size() {
        return this.mLocaleList.size();
    }

    @Override // androidx.core.os.LocaleListInterface
    public String toLanguageTags() {
        return this.mLocaleList.toLanguageTags();
    }

    public String toString() {
        return this.mLocaleList.toString();
    }
}
