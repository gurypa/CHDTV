package com.google.common.base;

import java.util.Arrays;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class Objects extends ExtraObjectsMethodsForWeb {
    private Objects() {
    }

    public static boolean equal(@CheckForNull Object r1, @CheckForNull Object r2) {
        if (r1 == r2) goto L8;
        if (r1 != null) goto L5;
    L7:
        return false;
    L5:
        if (r1.equals(r2) == false) goto L7;
    L8:
        return true;
    }

    public static int hashCode(@CheckForNull Object... r1) {
        return Arrays.hashCode(r1);
    }
}
