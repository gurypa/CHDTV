package androidx.media;

import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;

/* JADX INFO: loaded from: classes.dex */
public class MediaBrowserCompatUtils {
    private MediaBrowserCompatUtils() {
    }

    public static boolean areSameOptions(Bundle r6, Bundle r7) {
        if (r6 != r7) goto L6;
        return true;
    L6:
        if (r6 == null) goto L8;
        if (r7 != null) goto L23;
        if (r6.getInt(MediaBrowserCompat.EXTRA_PAGE, -1) != (-1)) goto L21;
        if (r6.getInt(MediaBrowserCompat.EXTRA_PAGE_SIZE, -1) != (-1)) goto L21;
        return true;
    L21:
        return false;
    L23:
        if (r6.getInt(MediaBrowserCompat.EXTRA_PAGE, -1) != r7.getInt(MediaBrowserCompat.EXTRA_PAGE, -1)) goto L28;
        if (r6.getInt(MediaBrowserCompat.EXTRA_PAGE_SIZE, -1) != r7.getInt(MediaBrowserCompat.EXTRA_PAGE_SIZE, -1)) goto L28;
        return true;
    L28:
        return false;
    L8:
        if (r7.getInt(MediaBrowserCompat.EXTRA_PAGE, -1) != (-1)) goto L13;
        if (r7.getInt(MediaBrowserCompat.EXTRA_PAGE_SIZE, -1) != (-1)) goto L13;
        return true;
    L13:
        return false;
    }

    public static boolean hasDuplicatedItems(Bundle r9, Bundle r10) {
        if (r9 != null) goto L5;
        int r2 = -1;
    L6:
        if (r10 != null) goto L8;
        int r0 = -1;
    L10:
        if (r9 != null) goto L12;
        int r4 = -1;
    L13:
        if (r10 != null) goto L15;
        int r3 = -1;
    L17:
        if (r2 == (-1)) goto L21;
        if (r4 == (-1)) goto L21;
        int r6 = r4 * r2;
        int r7 = (r6 + r4) - 1;
    L22:
        if (r0 == (-1)) goto L26;
        if (r3 == (-1)) goto L26;
        int r1 = r3 * r0;
        int r8 = (r1 + r3) - 1;
    L27:
        if (r7 < r1) goto L30;
        if (r8 < r6) goto L30;
        return true;
    L30:
        return false;
    L26:
        r1 = 0;
        r8 = Integer.MAX_VALUE;
    L21:
        r6 = 0;
        r7 = Integer.MAX_VALUE;
        goto L22
    L15:
        r3 = r10.getInt(MediaBrowserCompat.EXTRA_PAGE_SIZE, -1);
        goto L17
    L12:
        r4 = r9.getInt(MediaBrowserCompat.EXTRA_PAGE_SIZE, -1);
        goto L13
    L8:
        r0 = r10.getInt(MediaBrowserCompat.EXTRA_PAGE, -1);
        goto L10
    L5:
        r2 = r9.getInt(MediaBrowserCompat.EXTRA_PAGE, -1);
        goto L6
    }
}
