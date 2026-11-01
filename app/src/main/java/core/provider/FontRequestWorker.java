package androidx.core.provider;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import androidx.collection.LruCache;
import androidx.collection.SimpleArrayMap;
import androidx.core.graphics.TypefaceCompat;
import androidx.core.util.Consumer;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/* JADX INFO: loaded from: classes.dex */
class FontRequestWorker {
    private static final ExecutorService DEFAULT_EXECUTOR_SERVICE = null;
    static final Object LOCK = null;
    static final SimpleArrayMap<String, ArrayList<Consumer<TypefaceResult>>> PENDING_REPLIES = null;
    static final LruCache<String, Typeface> sTypefaceCache = null;

    static final class TypefaceResult {
        final int mResult;
        final Typeface mTypeface;

        TypefaceResult(int r2) {
            this.mTypeface = null;
            this.mResult = r2;
        }

        TypefaceResult(Typeface r2) {
            this.mTypeface = r2;
            this.mResult = 0;
        }

        boolean isSuccess() {
            if (this.mResult != 0) goto L5;
            return true;
        L5:
            return false;
        }
    }

    static {
        sTypefaceCache = new LruCache(16);
        DEFAULT_EXECUTOR_SERVICE = RequestExecutor.createDefaultExecutor("fonts-androidx", 10, 10000);
        LOCK = new Object();
        PENDING_REPLIES = new SimpleArrayMap();
    }

    private FontRequestWorker() {
    }

    private static String createCacheId(FontRequest r2, int r3) {
        return r2.getId() + "-" + r3;
    }

    private static int getFontFamilyResultStatus(FontsContractCompat.FontFamilyResult r7) {
        if (r7.getStatusCode() != 0) goto L5;
        FontsContractCompat.FontInfo[] r0 = r7.getFonts();
        if (r0 != null) goto L12;
        return 1;
    L12:
        if (r0.length == 0) goto L29;
        int r2 = r0.length;
        int r4 = 0;
    L15:
        if (r4 >= r2) goto L23;
        int r6 = r0[r4].getResultCode();
        if (r6 != 0) goto L18;
        r4 = r4 + 1;
        goto L15
    L18:
        if (r6 >= 0) goto L21;
        return -3;
    L21:
        return r6;
    L23:
        return 0;
    L29:
        return 1;
    L5:
        switch(r7.getStatusCode()) {
            case 1: goto L7;
            default: goto L6;
        };
    L6:
        return -3;
    L7:
        return -2;
    }

    static TypefaceResult getFontSync(String r6, Context r7, FontRequest r8, int r9) {
        Typeface r0 = sTypefaceCache.get(r6);
        if (r0 == null) goto L21;
        return new TypefaceResult(r0);
    L21:
        FontsContractCompat.FontFamilyResult r2 = FontProvider.getFontFamilyResult(r7, r8, null);     // Catch: PackageManager.NameNotFoundException -> L18
        int r3 = getFontFamilyResultStatus(r2);
        if (r3 != 0) goto L11;
        Typeface r1 = TypefaceCompat.createFromFontInfo(r7, null, r2.getFonts(), r9);
        if (r1 == null) goto L17;
        sTypefaceCache.put(r6, r1);
        return new TypefaceResult(r1);
    L17:
        return new TypefaceResult(-3);
    L11:
        return new TypefaceResult(r3);
    L20:
        return new TypefaceResult(-1);
    }

    static Typeface requestFontAsync(final Context r7, final FontRequest r8, final int r9, Executor r10, final CallbackWithHandler r11) {
        final String r0 = createCacheId(r8, r9);
        Typeface r1 = sTypefaceCache.get(r0);
        if (r1 == null) goto L6;
        r11.onTypefaceResult(new TypefaceResult(r1));
        return r1;
    L6:
        Consumer<TypefaceResult> r2 = new C00852(r11);
        Object r3 = LOCK;
        monitor-enter(r3);
        ArrayList<Consumer<TypefaceResult>> r4 = PENDING_REPLIES.get(r0);     // Catch: Throwable -> L21
        if (r4 == null) goto L13;
        r4.add(r2);     // Catch: Throwable -> L21
        monitor-exit(r3);     // Catch: Throwable -> L21
        return null;
    L13:
        ArrayList<Consumer<TypefaceResult>> r6 = new ArrayList();     // Catch: Throwable -> L21
        r6.add(r2);     // Catch: Throwable -> L21
        PENDING_REPLIES.put(r0, r6);     // Catch: Throwable -> L21
        monitor-exit(r3);     // Catch: Throwable -> L21
        Callable<TypefaceResult> r5 = new CallableC00863(r0, r7, r8, r9);
        if (r10 != null) goto L18;
        Executor r12 = DEFAULT_EXECUTOR_SERVICE;
    L19:
        RequestExecutor.execute(r12, r5, new C00874(r0));
        return null;
    L18:
        r12 = r10;
    L21:
        th = move-exception;
        throw th;
    }

    static Typeface requestFontSync(final Context r6, final FontRequest r7, CallbackWithHandler r8, final int r9, int r10) {
        final String r0 = createCacheId(r7, r9);
        Typeface r1 = sTypefaceCache.get(r0);
        if (r1 == null) goto L7;
        r8.onTypefaceResult(new TypefaceResult(r1));
        return r1;
    L7:
        if (r10 != (-1)) goto L10;
        TypefaceResult r2 = getFontSync(r0, r6, r7, r9);
        r8.onTypefaceResult(r2);
        return r2.mTypeface;
    L10:
        Callable<TypefaceResult> r3 = new CallableC00841(r0, r6, r7, r9);
        TypefaceResult r4 = (TypefaceResult) RequestExecutor.submit(DEFAULT_EXECUTOR_SERVICE, r3, r10);     // Catch: InterruptedException -> L13
        r8.onTypefaceResult(r4);     // Catch: InterruptedException -> L13
        return r4.mTypeface;
    L14:
        r8.onTypefaceResult(new TypefaceResult(-3));
        return null;
    }

    static void resetTypefaceCache() {
        sTypefaceCache.evictAll();
    }
}
