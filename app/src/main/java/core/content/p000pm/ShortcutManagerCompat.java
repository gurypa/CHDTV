package androidx.core.content.p000pm;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.IconCompat;
import androidx.core.util.Preconditions;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public class ShortcutManagerCompat {
    static final String ACTION_INSTALL_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
    private static final int DEFAULT_MAX_ICON_DIMENSION_DP = 96;
    private static final int DEFAULT_MAX_ICON_DIMENSION_LOWRAM_DP = 48;
    public static final String EXTRA_SHORTCUT_ID = "android.intent.extra.shortcut.ID";
    public static final int FLAG_MATCH_CACHED = 8;
    public static final int FLAG_MATCH_DYNAMIC = 2;
    public static final int FLAG_MATCH_MANIFEST = 1;
    public static final int FLAG_MATCH_PINNED = 4;
    static final String INSTALL_SHORTCUT_PERMISSION = "com.android.launcher.permission.INSTALL_SHORTCUT";
    private static final String SHORTCUT_LISTENER_INTENT_FILTER_ACTION = "androidx.core.content.pm.SHORTCUT_LISTENER";
    private static final String SHORTCUT_LISTENER_META_DATA_KEY = "androidx.core.content.pm.shortcut_listener_impl";
    private static volatile List<ShortcutInfoChangeListener> sShortcutInfoChangeListeners;
    private static volatile ShortcutInfoCompatSaver<?> sShortcutInfoCompatSaver;

    private static class Api25Impl {
        private Api25Impl() {
        }

        static String getShortcutInfoWithLowestRank(List<ShortcutInfo> r5) {
            int r0 = -1;
            String r1 = null;
            Iterator<ShortcutInfo> r2 = r5.iterator();
        L4:
            if (r2.hasNext() == false) goto L9;
            ShortcutInfo r3 = r2.next();
            if (r3.getRank() <= r0) goto L4;
            r1 = r3.getId();
            r0 = r3.getRank();
            goto L4
        L9:
            return r1;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ShortcutMatchFlags {
    }

    static {
        sShortcutInfoCompatSaver = null;
        sShortcutInfoChangeListeners = null;
    }

    private ShortcutManagerCompat() {
    }

    public static boolean addDynamicShortcuts(Context r6, List<ShortcutInfoCompat> r7) {
        List<ShortcutInfoCompat> r1 = removeShortcutsExcludedFromSurface(r7, 1);
        if (Build.VERSION.SDK_INT > 29) goto L6;
        convertUriIconsToBitmapIcons(r6, r1);
    L6:
        if (Build.VERSION.SDK_INT < 25) goto L15;
        ArrayList r2 = new ArrayList();
        Iterator<ShortcutInfoCompat> r3 = r1.iterator();
    L9:
        if (r3.hasNext() == false) goto L12;
        r2.add(r3.next().toShortcutInfo());
        goto L9
    L12:
        if (((ShortcutManager) r6.getSystemService(ShortcutManager.class)).addDynamicShortcuts(r2) == true) goto L15;
        return false;
    L15:
        getShortcutInfoSaverInstance(r6).addShortcuts(r1);
        Iterator<ShortcutInfoChangeListener> r4 = getShortcutInfoListeners(r6).iterator();
    L17:
        if (r4.hasNext() == false) goto L19;
        r4.next().onShortcutAdded(r7);
        goto L17
    L19:
        return true;
    }

    static boolean convertUriIconToBitmapIcon(Context r6, ShortcutInfoCompat r7) {
        if (r7.mIcon != null) goto L5;
        return false;
    L5:
        int r0 = r7.mIcon.mType;
        if (r0 != 6) goto L8;
    L10:
        InputStream r4 = r7.mIcon.getUriInputStream(r6);
        if (r4 != null) goto L13;
        return false;
    L13:
        Bitmap r5 = BitmapFactory.decodeStream(r4);
        if (r5 != null) goto L16;
        return false;
    L16:
        if (r0 != 6) goto L18;
        IconCompat r1 = IconCompat.createWithAdaptiveBitmap(r5);
    L19:
        r7.mIcon = r1;
        return true;
    L18:
        r1 = IconCompat.createWithBitmap(r5);
        goto L19
    L8:
        if (r0 == 4) goto L10;
        return true;
    }

    static void convertUriIconsToBitmapIcons(Context r4, List<ShortcutInfoCompat> r5) {
        Iterator r1 = new ArrayList(r5).iterator();
    L4:
        if (r1.hasNext() == false) goto L9;
        ShortcutInfoCompat r2 = (ShortcutInfoCompat) r1.next();
        if (convertUriIconToBitmapIcon(r4, r2) == true) goto L4;
        r5.remove(r2);
        goto L4
    }

    public static Intent createShortcutResultIntent(Context r3, ShortcutInfoCompat r4) {
        Intent r0 = null;
        if (Build.VERSION.SDK_INT < 26) goto L5;
        r0 = ((ShortcutManager) r3.getSystemService(ShortcutManager.class)).createShortcutResultIntent(r4.toShortcutInfo());
    L5:
        if (r0 != null) goto L8;
        r0 = new Intent();
    L8:
        return r4.addToIntent(r0);
    }

    public static void disableShortcuts(Context r2, List<String> r3, CharSequence r4) {
        if (Build.VERSION.SDK_INT < 25) goto L5;
        ((ShortcutManager) r2.getSystemService(ShortcutManager.class)).disableShortcuts(r3, r4);
    L5:
        getShortcutInfoSaverInstance(r2).removeShortcuts(r3);
        Iterator<ShortcutInfoChangeListener> r0 = getShortcutInfoListeners(r2).iterator();
    L7:
        if (r0.hasNext() == false) goto L9;
        r0.next().onShortcutRemoved(r3);
        goto L7
    }

    public static void enableShortcuts(Context r5, List<ShortcutInfoCompat> r6) {
        List<ShortcutInfoCompat> r0 = removeShortcutsExcludedFromSurface(r6, 1);
        if (Build.VERSION.SDK_INT < 25) goto L9;
        ArrayList r1 = new ArrayList(r6.size());
        Iterator<ShortcutInfoCompat> r2 = r0.iterator();
    L6:
        if (r2.hasNext() == false) goto L8;
        r1.add(r2.next().mId);
        goto L6
    L8:
        ((ShortcutManager) r5.getSystemService(ShortcutManager.class)).enableShortcuts(r1);
    L9:
        getShortcutInfoSaverInstance(r5).addShortcuts(r0);
        Iterator<ShortcutInfoChangeListener> r3 = getShortcutInfoListeners(r5).iterator();
    L11:
        if (r3.hasNext() == false) goto L13;
        r3.next().onShortcutAdded(r6);
        goto L11
    }

    public static List<ShortcutInfoCompat> getDynamicShortcuts(Context r5) {
        if (Build.VERSION.SDK_INT < 25) goto L14;
        List<ShortcutInfo> r0 = ((ShortcutManager) r5.getSystemService(ShortcutManager.class)).getDynamicShortcuts();
        ArrayList r1 = new ArrayList(r0.size());
        Iterator<ShortcutInfo> r2 = r0.iterator();
    L6:
        if (r2.hasNext() == false) goto L8;
        r1.add(new ShortcutInfoCompat.Builder(r5, r2.next()).build());
        goto L6
    L8:
        return r1;
    L14:
        return getShortcutInfoSaverInstance(r5).getShortcuts();
    L13:
        return new ArrayList();
    }

    private static int getIconDimensionInternal(Context r6, boolean r7) {
        ActivityManager r0 = (ActivityManager) r6.getSystemService("activity");
        if (r0 != null) goto L5;
    L8:
        boolean r2 = true;
    L9:
        if (r2 == false) goto L11;
        int r3 = DEFAULT_MAX_ICON_DIMENSION_LOWRAM_DP;
    L12:
        int r1 = Math.max(1, r3);
        DisplayMetrics r4 = r6.getResources().getDisplayMetrics();
        if (r7 == false) goto L15;
        float r5 = r4.xdpi;
    L17:
        return (int) (r1 * (r5 / 160.0f));
    L15:
        r5 = r4.ydpi;
        goto L17
    L11:
        r3 = DEFAULT_MAX_ICON_DIMENSION_DP;
        goto L12
    L5:
        if (r0.isLowRamDevice() == true) goto L8;
        r2 = false;
        goto L9
    }

    public static int getIconMaxHeight(Context r2) {
        Preconditions.checkNotNull(r2);
        if (Build.VERSION.SDK_INT < 25) goto L7;
        return ((ShortcutManager) r2.getSystemService(ShortcutManager.class)).getIconMaxHeight();
    L7:
        return getIconDimensionInternal(r2, false);
    }

    public static int getIconMaxWidth(Context r2) {
        Preconditions.checkNotNull(r2);
        if (Build.VERSION.SDK_INT < 25) goto L7;
        return ((ShortcutManager) r2.getSystemService(ShortcutManager.class)).getIconMaxWidth();
    L7:
        return getIconDimensionInternal(r2, true);
    }

    public static int getMaxShortcutCountPerActivity(Context r2) {
        Preconditions.checkNotNull(r2);
        if (Build.VERSION.SDK_INT >= 25) goto L5;
        return 5;
    L5:
        return ((ShortcutManager) r2.getSystemService(ShortcutManager.class)).getMaxShortcutCountPerActivity();
    }

    static List<ShortcutInfoChangeListener> getShortcutInfoChangeListeners() {
        return sShortcutInfoChangeListeners;
    }

    private static String getShortcutInfoCompatWithLowestRank(List<ShortcutInfoCompat> r5) {
        int r0 = -1;
        String r1 = null;
        Iterator<ShortcutInfoCompat> r2 = r5.iterator();
    L4:
        if (r2.hasNext() == false) goto L9;
        ShortcutInfoCompat r3 = r2.next();
        if (r3.getRank() <= r0) goto L4;
        r1 = r3.getId();
        r0 = r3.getRank();
        goto L4
    L9:
        return r1;
    }

    private static List<ShortcutInfoChangeListener> getShortcutInfoListeners(Context r16) {
        if (sShortcutInfoChangeListeners != null) goto L24;
        ArrayList r0 = new ArrayList();
        PackageManager r2 = r16.getPackageManager();
        Intent r1 = new Intent(SHORTCUT_LISTENER_INTENT_FILTER_ACTION);
        r1.setPackage(r16.getPackageName());
        Iterator<ResolveInfo> r5 = r2.queryIntentActivities(r1, 128).iterator();
    L6:
        if (r5.hasNext() == false) goto L21;
        ActivityInfo r7 = r5.next().activityInfo;
        if (r7 == null) goto L6;
        Bundle r8 = r7.metaData;
        if (r8 == null) goto L6;
        String r9 = r8.getString(SHORTCUT_LISTENER_META_DATA_KEY);
        if (r9 == null) goto L6;
        r0.add((ShortcutInfoChangeListener) Class.forName(r9, false, ShortcutManagerCompat.class.getClassLoader()).getMethod("getInstance", new Class[]{Context.class}).invoke(null, new Object[]{r16}));     // Catch: Exception -> L18
        goto L6
    L21:
        if (sShortcutInfoChangeListeners != null) goto L24;
        sShortcutInfoChangeListeners = r0;
    L24:
        return sShortcutInfoChangeListeners;
    }

    private static ShortcutInfoCompatSaver<?> getShortcutInfoSaverInstance(Context r7) {
        if (sShortcutInfoCompatSaver != null) goto L11;
        sShortcutInfoCompatSaver = (ShortcutInfoCompatSaver) Class.forName("androidx.sharetarget.ShortcutInfoCompatSaverImpl", false, ShortcutManagerCompat.class.getClassLoader()).getMethod("getInstance", new Class[]{Context.class}).invoke(null, new Object[]{r7});     // Catch: Exception -> L6
    L8:
        if (sShortcutInfoCompatSaver != null) goto L11;
        sShortcutInfoCompatSaver = new ShortcutInfoCompatSaver.NoopImpl();
    L11:
        return sShortcutInfoCompatSaver;
    }

    public static List<ShortcutInfoCompat> getShortcuts(Context r3, int r4) {
        if (Build.VERSION.SDK_INT < 30) goto L7;
        return ShortcutInfoCompat.fromShortcuts(r3, ((ShortcutManager) r3.getSystemService(ShortcutManager.class)).getShortcuts(r4));
    L7:
        if (Build.VERSION.SDK_INT < 25) goto L20;
        ShortcutManager r0 = (ShortcutManager) r3.getSystemService(ShortcutManager.class);
        ArrayList r1 = new ArrayList();
        if ((r4 & 1) == 0) goto L12;
        r1.addAll(r0.getManifestShortcuts());
    L12:
        if ((r4 & 2) == 0) goto L15;
        r1.addAll(r0.getDynamicShortcuts());
    L15:
        if ((r4 & 4) == 0) goto L18;
        r1.addAll(r0.getPinnedShortcuts());
    L18:
        return ShortcutInfoCompat.fromShortcuts(r3, r1);
    L20:
        if ((r4 & 2) == 0) goto L25;
        return getShortcutInfoSaverInstance(r3).getShortcuts();
    L25:
        return Collections.emptyList();
    }

    public static boolean isRateLimitingActive(Context r2) {
        Preconditions.checkNotNull(r2);
        if (Build.VERSION.SDK_INT < 25) goto L7;
        return ((ShortcutManager) r2.getSystemService(ShortcutManager.class)).isRateLimitingActive();
    L7:
        if (getShortcuts(r2, 3).size() != getMaxShortcutCountPerActivity(r2)) goto L9;
        return true;
    L9:
        return false;
    }

    public static boolean isRequestPinShortcutSupported(Context r6) {
        if (Build.VERSION.SDK_INT < 26) goto L7;
        return ((ShortcutManager) r6.getSystemService(ShortcutManager.class)).isRequestPinShortcutSupported();
    L7:
        if (ContextCompat.checkSelfPermission(r6, INSTALL_SHORTCUT_PERMISSION) == 0) goto L9;
        return false;
    L9:
        Iterator<ResolveInfo> r1 = r6.getPackageManager().queryBroadcastReceivers(new Intent(ACTION_INSTALL_SHORTCUT), 0).iterator();
    L11:
        if (r1.hasNext() == false) goto L20;
        String r4 = r1.next().activityInfo.permission;
        if (TextUtils.isEmpty(r4) == true) goto L18;
        if (INSTALL_SHORTCUT_PERMISSION.equals(r4) == false) goto L11;
        return true;
    L18:
        return true;
    L20:
        return false;
    }

    public static boolean pushDynamicShortcut(Context r7, ShortcutInfoCompat r8) {
        Preconditions.checkNotNull(r7);
        Preconditions.checkNotNull(r8);
        if (Build.VERSION.SDK_INT <= 31) goto L5;
    L11:
        int r0 = getMaxShortcutCountPerActivity(r7);
        if (r0 != 0) goto L15;
        return false;
    L15:
        if (Build.VERSION.SDK_INT > 29) goto L18;
        convertUriIconToBitmapIcon(r7, r8);
    L18:
        if (Build.VERSION.SDK_INT < 30) goto L21;
        ((ShortcutManager) r7.getSystemService(ShortcutManager.class)).pushDynamicShortcut(r8.toShortcutInfo());
    L29:
        ShortcutInfoCompatSaver<?> r3 = getShortcutInfoSaverInstance(r7);
        List<ShortcutInfoCompat> r4 = r3.getShortcuts();     // Catch: Throwable -> L40 Exception -> L47
        if (r4.size() < r0) goto L33;
        r3.removeShortcuts(Arrays.asList(new String[]{getShortcutInfoCompatWithLowestRank(r4)}));     // Catch: Throwable -> L40 Exception -> L47
    L33:
        r3.addShortcuts(Arrays.asList(new ShortcutInfoCompat[]{r8}));     // Catch: Throwable -> L40 Exception -> L47
        Iterator<ShortcutInfoChangeListener> r1 = getShortcutInfoListeners(r7).iterator();
    L36:
        if (r1.hasNext() == false) goto L38;
        r1.next().onShortcutAdded(Collections.singletonList(r8));
        goto L36
    L38:
        reportShortcutUsed(r7, r8.getId());
        return true;
    L40:
        th = move-exception;
        Iterator<ShortcutInfoChangeListener> r2 = getShortcutInfoListeners(r7).iterator();
    L43:
        if (r2.hasNext() == false) goto L45;
        r2.next().onShortcutAdded(Collections.singletonList(r8));
        goto L43
    L45:
        reportShortcutUsed(r7, r8.getId());
        throw th;
    L48:
        Iterator<ShortcutInfoChangeListener> r5 = getShortcutInfoListeners(r7).iterator();
    L50:
        if (r5.hasNext() == false) goto L52;
        r5.next().onShortcutAdded(Collections.singletonList(r8));
        goto L50
    L52:
        reportShortcutUsed(r7, r8.getId());
        return false;
    L21:
        if (Build.VERSION.SDK_INT < 25) goto L29;
        ShortcutManager r6 = (ShortcutManager) r7.getSystemService(ShortcutManager.class);
        if (r6.isRateLimitingActive() == false) goto L25;
        return false;
    L25:
        List<ShortcutInfo> r9 = r6.getDynamicShortcuts();
        if (r9.size() < r0) goto L28;
        r6.removeDynamicShortcuts(Arrays.asList(new String[]{Api25Impl.getShortcutInfoWithLowestRank(r9)}));
    L28:
        r6.addDynamicShortcuts(Arrays.asList(new ShortcutInfo[]{r8.toShortcutInfo()}));
        goto L29
    L5:
        if (r8.isExcludedFromSurfaces(1) == false) goto L11;
        Iterator<ShortcutInfoChangeListener> r10 = getShortcutInfoListeners(r7).iterator();
    L8:
        if (r10.hasNext() == false) goto L10;
        r10.next().onShortcutAdded(Collections.singletonList(r8));
        goto L8
    L10:
        return true;
    }

    public static void removeAllDynamicShortcuts(Context r2) {
        if (Build.VERSION.SDK_INT < 25) goto L5;
        ((ShortcutManager) r2.getSystemService(ShortcutManager.class)).removeAllDynamicShortcuts();
    L5:
        getShortcutInfoSaverInstance(r2).removeAllShortcuts();
        Iterator<ShortcutInfoChangeListener> r0 = getShortcutInfoListeners(r2).iterator();
    L7:
        if (r0.hasNext() == false) goto L9;
        r0.next().onAllShortcutsRemoved();
        goto L7
    }

    public static void removeDynamicShortcuts(Context r2, List<String> r3) {
        if (Build.VERSION.SDK_INT < 25) goto L5;
        ((ShortcutManager) r2.getSystemService(ShortcutManager.class)).removeDynamicShortcuts(r3);
    L5:
        getShortcutInfoSaverInstance(r2).removeShortcuts(r3);
        Iterator<ShortcutInfoChangeListener> r0 = getShortcutInfoListeners(r2).iterator();
    L7:
        if (r0.hasNext() == false) goto L9;
        r0.next().onShortcutRemoved(r3);
        goto L7
    }

    public static void removeLongLivedShortcuts(Context r2, List<String> r3) {
        if (Build.VERSION.SDK_INT >= 30) goto L6;
        removeDynamicShortcuts(r2, r3);
        return;
    L6:
        ((ShortcutManager) r2.getSystemService(ShortcutManager.class)).removeLongLivedShortcuts(r3);
        getShortcutInfoSaverInstance(r2).removeShortcuts(r3);
        Iterator<ShortcutInfoChangeListener> r0 = getShortcutInfoListeners(r2).iterator();
    L8:
        if (r0.hasNext() == false) goto L10;
        r0.next().onShortcutRemoved(r3);
        goto L8
    }

    private static List<ShortcutInfoCompat> removeShortcutsExcludedFromSurface(List<ShortcutInfoCompat> r4, int r5) {
        Objects.requireNonNull(r4);
        if (Build.VERSION.SDK_INT <= 31) goto L5;
        return r4;
    L5:
        ArrayList r0 = new ArrayList(r4);
        Iterator<ShortcutInfoCompat> r1 = r4.iterator();
    L7:
        if (r1.hasNext() == false) goto L12;
        ShortcutInfoCompat r2 = r1.next();
        if (r2.isExcludedFromSurfaces(r5) == false) goto L7;
        r0.remove(r2);
        goto L7
    L12:
        return r0;
    }

    public static void reportShortcutUsed(Context r3, String r4) {
        Preconditions.checkNotNull(r3);
        Preconditions.checkNotNull(r4);
        if (Build.VERSION.SDK_INT < 25) goto L5;
        ((ShortcutManager) r3.getSystemService(ShortcutManager.class)).reportShortcutUsed(r4);
    L5:
        Iterator<ShortcutInfoChangeListener> r0 = getShortcutInfoListeners(r3).iterator();
    L7:
        if (r0.hasNext() == false) goto L9;
        r0.next().onShortcutUsageReported(Collections.singletonList(r4));
        goto L7
    }

    public static boolean requestPinShortcut(Context r12, ShortcutInfoCompat r13, final IntentSender r14) {
        if (Build.VERSION.SDK_INT > 31) goto L8;
        if (r13.isExcludedFromSurfaces(1) == false) goto L8;
        return false;
    L8:
        if (Build.VERSION.SDK_INT < 26) goto L12;
        return ((ShortcutManager) r12.getSystemService(ShortcutManager.class)).requestPinShortcut(r13.toShortcutInfo(), r14);
    L12:
        if (isRequestPinShortcutSupported(r12) == true) goto L14;
        return false;
    L14:
        Intent r5 = r13.addToIntent(new Intent(ACTION_INSTALL_SHORTCUT));
        if (r14 != null) goto L18;
        r12.sendBroadcast(r5);
        return true;
    L18:
        r12.sendOrderedBroadcast(r5, null, new C00561(r14), null, -1, null, null);
        return true;
    }

    public static boolean setDynamicShortcuts(Context r6, List<ShortcutInfoCompat> r7) {
        Preconditions.checkNotNull(r6);
        Preconditions.checkNotNull(r7);
        List<ShortcutInfoCompat> r1 = removeShortcutsExcludedFromSurface(r7, 1);
        if (Build.VERSION.SDK_INT < 25) goto L12;
        ArrayList r2 = new ArrayList(r1.size());
        Iterator<ShortcutInfoCompat> r3 = r1.iterator();
    L6:
        if (r3.hasNext() == false) goto L9;
        r2.add(r3.next().toShortcutInfo());
        goto L6
    L9:
        if (((ShortcutManager) r6.getSystemService(ShortcutManager.class)).setDynamicShortcuts(r2) == true) goto L12;
        return false;
    L12:
        getShortcutInfoSaverInstance(r6).removeAllShortcuts();
        getShortcutInfoSaverInstance(r6).addShortcuts(r1);
        Iterator<ShortcutInfoChangeListener> r4 = getShortcutInfoListeners(r6).iterator();
    L14:
        if (r4.hasNext() == false) goto L16;
        ShortcutInfoChangeListener r5 = r4.next();
        r5.onAllShortcutsRemoved();
        r5.onShortcutAdded(r7);
        goto L14
    L16:
        return true;
    }

    static void setShortcutInfoChangeListeners(List<ShortcutInfoChangeListener> r0) {
        sShortcutInfoChangeListeners = r0;
    }

    static void setShortcutInfoCompatSaver(ShortcutInfoCompatSaver<Void> r0) {
        sShortcutInfoCompatSaver = r0;
    }

    public static boolean updateShortcuts(Context r6, List<ShortcutInfoCompat> r7) {
        List<ShortcutInfoCompat> r1 = removeShortcutsExcludedFromSurface(r7, 1);
        if (Build.VERSION.SDK_INT > 29) goto L6;
        convertUriIconsToBitmapIcons(r6, r1);
    L6:
        if (Build.VERSION.SDK_INT < 25) goto L15;
        ArrayList r2 = new ArrayList();
        Iterator<ShortcutInfoCompat> r3 = r1.iterator();
    L9:
        if (r3.hasNext() == false) goto L12;
        r2.add(r3.next().toShortcutInfo());
        goto L9
    L12:
        if (((ShortcutManager) r6.getSystemService(ShortcutManager.class)).updateShortcuts(r2) == true) goto L15;
        return false;
    L15:
        getShortcutInfoSaverInstance(r6).addShortcuts(r1);
        Iterator<ShortcutInfoChangeListener> r4 = getShortcutInfoListeners(r6).iterator();
    L17:
        if (r4.hasNext() == false) goto L19;
        r4.next().onShortcutUpdated(r7);
        goto L17
    L19:
        return true;
    }
}
