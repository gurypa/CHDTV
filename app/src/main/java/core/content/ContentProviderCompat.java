package androidx.core.content;

import android.content.ContentProvider;
import android.content.Context;

/* JADX INFO: loaded from: classes.dex */
public final class ContentProviderCompat {
    private ContentProviderCompat() {
    }

    public static Context requireContext(ContentProvider r3) {
        Context r0 = r3.getContext();
        if (r0 == null) goto L6;
        return r0;
    L6:
        throw new IllegalStateException("Cannot find context from the provider.");
    }
}
