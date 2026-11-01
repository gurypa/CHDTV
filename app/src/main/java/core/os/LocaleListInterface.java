package androidx.core.os;

import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
interface LocaleListInterface {
    Locale get(int r1);

    Locale getFirstMatch(String[] r1);

    Object getLocaleList();

    int indexOf(Locale r1);

    boolean isEmpty();

    int size();

    String toLanguageTags();
}
