package androidx.core.content;

import android.content.UriMatcher;
import android.net.Uri;
import androidx.core.util.Predicate;

/* JADX INFO: loaded from: classes.dex */
public class UriMatcherCompat {
    private UriMatcherCompat() {
    }

    public static Predicate<Uri> asPredicate(final UriMatcher r1) {
        return new UriMatcherCompat$$ExternalSyntheticLambda0(r1);
    }

    static /* synthetic */ boolean lambda$asPredicate$0(UriMatcher r2, Uri r3) {
        if (r2.match(r3) == (-1)) goto L5;
        return true;
    L5:
        return false;
    }
}
