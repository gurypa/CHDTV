package androidx.core.os;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
final class LocaleListCompatWrapper implements LocaleListInterface {
    private static final Locale EN_LATN = null;
    private static final Locale LOCALE_AR_XB = null;
    private static final Locale LOCALE_EN_XA = null;
    private static final Locale[] sEmptyList = null;
    private final Locale[] mList;
    private final String mStringRepresentation;

    static class Api21Impl {
        private Api21Impl() {
        }

        static String getScript(Locale r1) {
            return r1.getScript();
        }
    }

    static {
        sEmptyList = new Locale[0];
        LOCALE_EN_XA = new Locale("en", "XA");
        LOCALE_AR_XB = new Locale("ar", "XB");
        EN_LATN = LocaleListCompat.forLanguageTagCompat("en-Latn");
    }

    LocaleListCompatWrapper(Locale... r9) {
        if (r9.length != 0) goto L5;
        this.mList = sEmptyList;
        this.mStringRepresentation = "";
        return;
    L5:
        ArrayList r0 = new ArrayList();
        HashSet r1 = new HashSet();
        StringBuilder r2 = new StringBuilder();
        int r3 = 0;
    L7:
        if (r3 >= r9.length) goto L19;
        Locale r4 = r9[r3];
        if (r4 == null) goto L18;
        if (r1.contains(r4) == true) goto L16;
        Locale r5 = (Locale) r4.clone();
        r0.add(r5);
        toLanguageTag(r2, r5);
        if (r3 >= (r9.length - 1)) goto L15;
        r2.append(',');
    L15:
        r1.add(r5);
    L16:
        r3 = r3 + 1;
        goto L7
    L18:
        throw new NullPointerException("list[" + r3 + "] is null");
    L19:
        this.mList = (Locale[]) r0.toArray(new Locale[0]);
        this.mStringRepresentation = r2.toString();
    }

    private Locale computeFirstMatch(Collection<String> r3, boolean r4) {
        int r0 = computeFirstMatchIndex(r3, r4);
        if (r0 != (-1)) goto L6;
        return null;
    L6:
        return this.mList[r0];
    }

    private int computeFirstMatchIndex(Collection<String> r7, boolean r8) {
        if (this.mList.length != 1) goto L6;
        return 0;
    L6:
        if (this.mList.length != 0) goto L9;
        return -1;
    L9:
        int r0 = Integer.MAX_VALUE;
        if (r8 == false) goto L16;
        int r1 = findFirstMatchIndex(EN_LATN);
        if (r1 != 0) goto L14;
        return 0;
    L14:
        if (r1 >= Integer.MAX_VALUE) goto L16;
        r0 = r1;
    L16:
        Iterator<String> r2 = r7.iterator();
    L18:
        if (r2.hasNext() == false) goto L26;
        int r5 = findFirstMatchIndex(LocaleListCompat.forLanguageTagCompat(r2.next()));
        if (r5 == 0) goto L21;
        if (r5 >= r0) goto L18;
        r0 = r5;
        goto L18
    L21:
        return 0;
    L26:
        if (r0 != Integer.MAX_VALUE) goto L28;
        return 0;
    L28:
        return r0;
    }

    private int findFirstMatchIndex(Locale r3) {
        int r0 = 0;
    L4:
        if (r0 >= this.mList.length) goto L9;
        if (matchScore(r3, this.mList[r0]) > 0) goto L7;
        r0 = r0 + 1;
        goto L4
    L7:
        return r0;
    L9:
        return Integer.MAX_VALUE;
    }

    private static String getLikelyScript(Locale r2) {
        String r0 = Api21Impl.getScript(r2);
        if (r0.isEmpty() == true) goto L5;
        return r0;
    L5:
        return "";
    }

    private static boolean isPseudoLocale(Locale r1) {
        if (LOCALE_EN_XA.equals(r1) == false) goto L5;
    L8:
        return true;
    L5:
        if (LOCALE_AR_XB.equals(r1) == true) goto L8;
        return false;
    }

    private static int matchScore(Locale r5, Locale r6) {
        if (r5.equals(r6) == false) goto L6;
        return 1;
    L6:
        if (r5.getLanguage().equals(r6.getLanguage()) == true) goto L9;
        return 0;
    L9:
        if (isPseudoLocale(r5) == false) goto L11;
    L25:
        return 0;
    L11:
        if (isPseudoLocale(r6) == true) goto L25;
        String r0 = getLikelyScript(r5);
        if (r0.isEmpty() == false) goto L24;
        String r3 = r5.getCountry();
        if (r3.isEmpty() == false) goto L18;
        return 1;
    L18:
        if (r3.equals(r6.getCountry()) == false) goto L22;
        return 1;
    L22:
        return 0;
    L24:
        return r0.equals(getLikelyScript(r6)) ? 1 : 0;
    }

    static void toLanguageTag(StringBuilder r2, Locale r3) {
        r2.append(r3.getLanguage());
        String r0 = r3.getCountry();
        if (r0 != null) goto L5;
        return;
    L5:
        if (r0.isEmpty() == true) goto L9;
        r2.append('-');
        r2.append(r3.getCountry());
        return;
    }

    public boolean equals(Object r7) {
        if (r7 != this) goto L6;
        return true;
    L6:
        if ((r7 instanceof LocaleListCompatWrapper) == true) goto L8;
        return false;
    L8:
        Locale[] r1 = ((LocaleListCompatWrapper) r7).mList;
        if (this.mList.length == r1.length) goto L11;
        return false;
    L11:
        int r3 = 0;
    L13:
        if (r3 >= this.mList.length) goto L18;
        if (this.mList[r3].equals(r1[r3]) == false) goto L16;
        r3 = r3 + 1;
        goto L13
    L16:
        return false;
    L18:
        return true;
    }

    @Override // androidx.core.os.LocaleListInterface
    public Locale get(int r2) {
        if (r2 >= 0) goto L4;
    L6:
        return null;
    L4:
        if (r2 >= this.mList.length) goto L6;
        return this.mList[r2];
    }

    @Override // androidx.core.os.LocaleListInterface
    public Locale getFirstMatch(String[] r3) {
        return computeFirstMatch(Arrays.asList(r3), false);
    }

    @Override // androidx.core.os.LocaleListInterface
    public Object getLocaleList() {
        return null;
    }

    public int hashCode() {
        int r0 = 1;
        Locale[] r1 = this.mList;
        int r2 = r1.length;
        int r3 = 0;
    L3:
        if (r3 >= r2) goto L5;
        r0 = (r0 * 31) + r1[r3].hashCode();
        r3 = r3 + 1;
        goto L3
    L5:
        return r0;
    }

    @Override // androidx.core.os.LocaleListInterface
    public int indexOf(Locale r3) {
        int r0 = 0;
    L4:
        if (r0 >= this.mList.length) goto L9;
        if (this.mList[r0].equals(r3) == true) goto L7;
        r0 = r0 + 1;
        goto L4
    L7:
        return r0;
    L9:
        return -1;
    }

    @Override // androidx.core.os.LocaleListInterface
    public boolean isEmpty() {
        if (this.mList.length != 0) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // androidx.core.os.LocaleListInterface
    public int size() {
        return this.mList.length;
    }

    @Override // androidx.core.os.LocaleListInterface
    public String toLanguageTags() {
        return this.mStringRepresentation;
    }

    public String toString() {
        StringBuilder r0 = new StringBuilder();
        r0.append("[");
        int r1 = 0;
    L4:
        if (r1 >= this.mList.length) goto L9;
        r0.append(this.mList[r1]);
        if (r1 >= (this.mList.length - 1)) goto L8;
        r0.append(',');
    L8:
        r1 = r1 + 1;
        goto L4
    L9:
        r0.append("]");
        return r0.toString();
    }
}
