package androidx.core.hardware.display;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.view.Display;
import java.util.WeakHashMap;

/* JADX INFO: loaded from: classes.dex */
public final class DisplayManagerCompat {
    public static final String DISPLAY_CATEGORY_PRESENTATION = "android.hardware.display.category.PRESENTATION";
    private static final WeakHashMap<Context, DisplayManagerCompat> sInstances = null;
    private final Context mContext;

    static class Api17Impl {
        private Api17Impl() {
        }

        static Display getDisplay(DisplayManager r1, int r2) {
            return r1.getDisplay(r2);
        }

        static Display[] getDisplays(DisplayManager r1) {
            return r1.getDisplays();
        }
    }

    static {
        sInstances = new WeakHashMap();
    }

    private DisplayManagerCompat(Context r1) {
        this.mContext = r1;
    }

    public static DisplayManagerCompat getInstance(Context r3) {
        WeakHashMap<Context, DisplayManagerCompat> r0 = sInstances;
        monitor-enter(r0);
        DisplayManagerCompat r1 = sInstances.get(r3);     // Catch: Throwable -> L9
        if (r1 != null) goto L7;
        r1 = new DisplayManagerCompat(r3);     // Catch: Throwable -> L9
        sInstances.put(r3, r1);     // Catch: Throwable -> L9
    L7:
        monitor-exit(r0);     // Catch: Throwable -> L9
        return r1;
    L9:
        th = move-exception;
        throw th;
    }

    public Display getDisplay(int r3) {
        return Api17Impl.getDisplay((DisplayManager) this.mContext.getSystemService("display"), r3);
    }

    public Display[] getDisplays() {
        return Api17Impl.getDisplays((DisplayManager) this.mContext.getSystemService("display"));
    }

    public Display[] getDisplays(String r3) {
        return Api17Impl.getDisplays((DisplayManager) this.mContext.getSystemService("display"));
    }
}
