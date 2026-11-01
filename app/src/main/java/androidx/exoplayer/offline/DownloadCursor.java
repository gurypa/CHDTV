package androidx.media3.exoplayer.offline;

import java.io.Closeable;

/* JADX INFO: loaded from: classes.dex */
public interface DownloadCursor extends Closeable {

    /* JADX INFO: renamed from: androidx.media3.exoplayer.offline.DownloadCursor$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static boolean $default$isAfterLast(DownloadCursor r3) {
            if (r3.getCount() != 0) goto L6;
            return true;
        L6:
            if (r3.getPosition() == r3.getCount()) goto L10;
            return false;
        L10:
            return true;
        }

        public static boolean $default$isBeforeFirst(DownloadCursor r3) {
            if (r3.getCount() != 0) goto L6;
            return true;
        L6:
            if (r3.getPosition() == (-1)) goto L10;
            return false;
        L10:
            return true;
        }

        public static boolean $default$isFirst(DownloadCursor r1) {
            if (r1.getPosition() == 0) goto L5;
        L7:
            return false;
        L5:
            if (r1.getCount() == 0) goto L7;
            return true;
        }

        public static boolean $default$isLast(DownloadCursor r3) {
            int r0 = r3.getCount();
            if (r3.getPosition() != (r0 - 1)) goto L6;
            if (r0 == 0) goto L6;
            return true;
        L6:
            return false;
        }

        public static boolean $default$moveToFirst(DownloadCursor r1) {
            return r1.moveToPosition(0);
        }

        public static boolean $default$moveToLast(DownloadCursor r1) {
            return r1.moveToPosition(r1.getCount() - 1);
        }

        public static boolean $default$moveToNext(DownloadCursor r1) {
            return r1.moveToPosition(r1.getPosition() + 1);
        }

        public static boolean $default$moveToPrevious(DownloadCursor r1) {
            return r1.moveToPosition(r1.getPosition() - 1);
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    void close();

    int getCount();

    Download getDownload();

    int getPosition();

    boolean isAfterLast();

    boolean isBeforeFirst();

    boolean isClosed();

    boolean isFirst();

    boolean isLast();

    boolean moveToFirst();

    boolean moveToLast();

    boolean moveToNext();

    boolean moveToPosition(int r1);

    boolean moveToPrevious();
}
