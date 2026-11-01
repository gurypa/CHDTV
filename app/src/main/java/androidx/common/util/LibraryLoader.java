package androidx.media3.common.util;

import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public abstract class LibraryLoader {
    private static final String TAG = "LibraryLoader";
    private boolean isAvailable;
    private boolean loadAttempted;
    private String[] nativeLibraries;

    public LibraryLoader(String... r1) {
        this.nativeLibraries = r1;
    }

    public synchronized boolean isAvailable() {
        monitor-enter(this);
    L20:
        th = move-exception;
        throw th;
    L4:
        if (this.loadAttempted == false) goto L9;
        boolean r0 = this.isAvailable;     // Catch: Throwable -> L20
        monitor-exit(this);
        return r0;
    L9:
        this.loadAttempted = true;     // Catch: Throwable -> L20
        String[] r1 = this.nativeLibraries;     // Catch: UnsatisfiedLinkError -> L15 Throwable -> L20
        int r2 = r1.length;     // Catch: UnsatisfiedLinkError -> L15 Throwable -> L20
        int r3 = 0;
    L11:
        if (r3 >= r2) goto L13;
        loadLibrary(r1[r3]);     // Catch: UnsatisfiedLinkError -> L15 Throwable -> L20
        r3 = r3 + 1;     // Catch: UnsatisfiedLinkError -> L15 Throwable -> L20
        goto L11
    L13:
        this.isAvailable = true;     // Catch: UnsatisfiedLinkError -> L15 Throwable -> L20
    L17:
        boolean r4 = this.isAvailable;     // Catch: Throwable -> L20
        monitor-exit(this);
        return r4;
    L16:
        Log.m48w(TAG, "Failed to load " + Arrays.toString(this.nativeLibraries));     // Catch: Throwable -> L20
        goto L17
    }

    protected abstract void loadLibrary(String r1);

    public synchronized void setLibraries(String... r3) {
        monitor-enter(this);
    L10:
        th = move-exception;
        throw th;
    L4:
        if (this.loadAttempted == true) goto L6;
        boolean r0 = true;
    L7:
        Assertions.checkState(r0, "Cannot set libraries after loading");     // Catch: Throwable -> L10
        this.nativeLibraries = r3;     // Catch: Throwable -> L10
        monitor-exit(this);
        return;
    L6:
        r0 = false;
        goto L7
    }
}
