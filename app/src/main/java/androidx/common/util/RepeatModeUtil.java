package androidx.media3.common.util;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* JADX INFO: loaded from: classes.dex */
public final class RepeatModeUtil {
    public static final int REPEAT_TOGGLE_MODE_ALL = 2;
    public static final int REPEAT_TOGGLE_MODE_NONE = 0;
    public static final int REPEAT_TOGGLE_MODE_ONE = 1;

    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface RepeatToggleModes {
    }

    private RepeatModeUtil() {
    }

    public static int getNextRepeatMode(int r3, int r4) {
        int r0 = 1;
    L4:
        if (r0 > 2) goto L9;
        int r1 = (r3 + r0) % 3;
        if (isRepeatModeEnabled(r1, r4) == true) goto L7;
        r0 = r0 + 1;
        goto L4
    L7:
        return r1;
    L9:
        return r3;
    }

    public static boolean isRepeatModeEnabled(int r3, int r4) {
        switch(r3) {
            case 0: goto L13;
            case 1: goto L10;
            case 2: goto L6;
            default: goto L4;
        };
    L4:
        return false;
    L13:
        return true;
    L6:
        if ((r4 & 2) != 0) goto L8;
        return false;
    L8:
        return true;
    L10:
        if ((r4 & 1) != 0) goto L12;
        return false;
    L12:
        return true;
    }
}
