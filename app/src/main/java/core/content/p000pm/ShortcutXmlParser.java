package androidx.core.content.p000pm;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: loaded from: classes.dex */
public class ShortcutXmlParser {
    private static final String ATTR_SHORTCUT_ID = "shortcutId";
    private static final Object GET_INSTANCE_LOCK = null;
    private static final String META_DATA_APP_SHORTCUTS = "android.app.shortcuts";
    private static final String TAG = "ShortcutXmlParser";
    private static final String TAG_SHORTCUT = "shortcut";
    private static volatile ArrayList<String> sShortcutIds;

    static {
        GET_INSTANCE_LOCK = new Object();
    }

    private ShortcutXmlParser() {
    }

    private static String getAttributeValue(XmlPullParser r2, String r3) {
        String r0 = r2.getAttributeValue("http://schemas.android.com/apk/res/android", r3);
        if (r0 == null) goto L5;
        return r0;
    L5:
        return r2.getAttributeValue(null, r3);
    }

    public static List<String> getShortcutIds(Context r3) {
        if (sShortcutIds != null) goto L15;
        Object r0 = GET_INSTANCE_LOCK;
        monitor-enter(r0);
    L11:
        th = move-exception;
        throw th;
    L7:
        if (sShortcutIds != null) goto L9;
        sShortcutIds = new ArrayList();     // Catch: Throwable -> L11
        sShortcutIds.addAll(parseShortcutIds(r3));     // Catch: Throwable -> L11
    L9:
        monitor-exit(r0);     // Catch: Throwable -> L11
    L15:
        return sShortcutIds;
    }

    private static XmlResourceParser getXmlResourceParser(Context r4, ActivityInfo r5) {
        XmlResourceParser r0 = r5.loadXmlMetaData(r4.getPackageManager(), META_DATA_APP_SHORTCUTS);
        if (r0 == null) goto L6;
        return r0;
    L6:
        throw new IllegalArgumentException("Failed to open android.app.shortcuts meta-data resource of " + r5.name);
    }

    public static List<String> parseShortcutIds(XmlPullParser r6) throws IOException, XmlPullParserException {
        ArrayList r0 = new ArrayList(1);
    L3:
        int r2 = r6.next();
        if (r2 == 1) goto L19;
        if (r2 == 3) goto L8;
    L9:
        int r3 = r6.getDepth();
        String r4 = r6.getName();
        if (r2 != 2) goto L3;
        if (r3 != 2) goto L3;
        if (TAG_SHORTCUT.equals(r4) == false) goto L3;
        String r5 = getAttributeValue(r6, ATTR_SHORTCUT_ID);
        if (r5 == null) goto L3;
        r0.add(r5);
        goto L3
    L8:
        if (r6.getDepth() > 0) goto L9;
    L19:
        return r0;
    }

    private static Set<String> parseShortcutIds(Context r9) {
        HashSet r0 = new HashSet();
        Intent r1 = new Intent("android.intent.action.MAIN");
        r1.addCategory("android.intent.category.LAUNCHER");
        r1.setPackage(r9.getPackageName());
        List<ResolveInfo> r2 = r9.getPackageManager().queryIntentActivities(r1, 128);
        if (r2 != null) goto L5;
    L31:
        return r0;
    L5:
        if (r2.size() == 0) goto L31;
        Iterator<ResolveInfo> r3 = r2.iterator();     // Catch: Exception -> L28
    L9:
        if (r3.hasNext() == false) goto L30;
        ActivityInfo r5 = r3.next().activityInfo;     // Catch: Exception -> L28
        Bundle r6 = r5.metaData;     // Catch: Exception -> L28
        if (r6 == null) goto L9;
        if (r6.containsKey(META_DATA_APP_SHORTCUTS) == false) goto L9;
        XmlResourceParser r7 = getXmlResourceParser(r9, r5);     // Catch: Exception -> L28
        r0.addAll(parseShortcutIds(r7));     // Catch: Throwable -> L19
        if (r7 == null) goto L9;
        r7.close();     // Catch: Exception -> L28
        goto L9
    L19:
        th = move-exception;
        if (r7 != null) goto L32;
    L25:
        throw th;     // Catch: Exception -> L28
    L32:
        r7.close();     // Catch: Throwable -> L23
    L23:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Exception -> L28
    L30:
        return r0;
    L28:
        e = move-exception;
        Log.e(TAG, "Failed to parse the Xml resource: ", e);
        goto L30
    }
}
