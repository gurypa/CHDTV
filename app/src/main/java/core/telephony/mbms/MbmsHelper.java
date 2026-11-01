package androidx.core.telephony.mbms;

import android.content.Context;
import android.os.Build;
import android.telephony.mbms.ServiceInfo;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public final class MbmsHelper {

    static class Api28Impl {
        private Api28Impl() {
        }

        static CharSequence getBestNameForService(Context r7, ServiceInfo r8) {
            Set<Locale> r0 = r8.getNamedContentLocales();
            if (r0.isEmpty() == false) goto L5;
            return null;
        L5:
            String[] r1 = new String[r0.size()];
            int r3 = 0;
            Iterator<Locale> r4 = r8.getNamedContentLocales().iterator();
        L7:
            if (r4.hasNext() == false) goto L9;
            r1[r3] = r4.next().toLanguageTag();
            r3 = r3 + 1;
            goto L7
        L9:
            Locale r5 = r7.getResources().getConfiguration().getLocales().getFirstMatch(r1);
            if (r5 != null) goto L13;
            return null;
        L13:
            return r8.getNameForLocale(r5);
        }
    }

    private MbmsHelper() {
    }

    public static CharSequence getBestNameForService(Context r2, ServiceInfo r3) {
        if (Build.VERSION.SDK_INT >= 28) goto L5;
        return null;
    L5:
        return Api28Impl.getBestNameForService(r2, r3);
    }
}
