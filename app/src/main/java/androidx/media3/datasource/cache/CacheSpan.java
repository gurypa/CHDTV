package androidx.media3.datasource.cache;

import androidx.media3.common.C0180C;
import java.io.File;

/* JADX INFO: loaded from: classes.dex */
public class CacheSpan implements Comparable<CacheSpan> {
    public final File file;
    public final boolean isCached;
    public final String key;
    public final long lastTouchTimestamp;
    public final long length;
    public final long position;

    public CacheSpan(String r10, long r11, long r13) {
        this(r10, r11, r13, C0180C.TIME_UNSET, null);
    }

    public CacheSpan(String r2, long r3, long r5, long r7, File r9) {
        this.key = r2;
        this.position = r3;
        this.length = r5;
        if (r9 == null) goto L5;
        boolean r0 = true;
    L6:
        this.isCached = r0;
        this.file = r9;
        this.lastTouchTimestamp = r7;
        return;
    L5:
        r0 = false;
        goto L6
    }

    /* JADX INFO: renamed from: compareTo, reason: avoid collision after fix types in other method */
    public int compareTo2(CacheSpan r6) {
        if (this.key.equals(r6.key) == false) goto L5;
        long r0 = this.position - r6.position;
        if (r0 != 0) goto L10;
        return 0;
    L10:
        if (r0 >= 0) goto L12;
        return -1;
    L12:
        return 1;
    L5:
        return this.key.compareTo(r6.key);
    }

    @Override // java.lang.Comparable
    public /* bridge */ /* synthetic */ int compareTo(CacheSpan r1) {
        return compareTo2(r1);
    }

    public boolean isHoleSpan() {
        return !this.isCached;
    }

    public boolean isOpenEnded() {
        if (this.length != (-1)) goto L5;
        return true;
    L5:
        return false;
    }

    public String toString() {
        return "[" + this.position + ", " + this.length + "]";
    }
}
