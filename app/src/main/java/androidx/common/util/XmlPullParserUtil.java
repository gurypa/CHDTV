package androidx.media3.common.util;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: loaded from: classes.dex */
public final class XmlPullParserUtil {
    private XmlPullParserUtil() {
    }

    public static String getAttributeValue(XmlPullParser r3, String r4) {
        int r0 = r3.getAttributeCount();
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L9;
        if (r3.getAttributeName(r1).equals(r4) == true) goto L7;
        r1 = r1 + 1;
        goto L3
    L7:
        return r3.getAttributeValue(r1);
    L9:
        return null;
    }

    public static String getAttributeValueIgnorePrefix(XmlPullParser r3, String r4) {
        int r0 = r3.getAttributeCount();
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L9;
        if (stripPrefix(r3.getAttributeName(r1)).equals(r4) == true) goto L7;
        r1 = r1 + 1;
        goto L3
    L7:
        return r3.getAttributeValue(r1);
    L9:
        return null;
    }

    public static boolean isEndTag(XmlPullParser r2) throws XmlPullParserException {
        if (r2.getEventType() != 3) goto L5;
        return true;
    L5:
        return false;
    }

    public static boolean isEndTag(XmlPullParser r1, String r2) throws XmlPullParserException {
        if (isEndTag(r1) == true) goto L5;
    L7:
        return false;
    L5:
        if (r1.getName().equals(r2) == false) goto L7;
        return true;
    }

    public static boolean isStartTag(XmlPullParser r2) throws XmlPullParserException {
        if (r2.getEventType() != 2) goto L5;
        return true;
    L5:
        return false;
    }

    public static boolean isStartTag(XmlPullParser r1, String r2) throws XmlPullParserException {
        if (isStartTag(r1) == true) goto L5;
    L7:
        return false;
    L5:
        if (r1.getName().equals(r2) == false) goto L7;
        return true;
    }

    public static boolean isStartTagIgnorePrefix(XmlPullParser r1, String r2) throws XmlPullParserException {
        if (isStartTag(r1) == true) goto L5;
    L7:
        return false;
    L5:
        if (stripPrefix(r1.getName()).equals(r2) == false) goto L7;
        return true;
    }

    private static String stripPrefix(String r2) {
        int r0 = r2.indexOf(58);
        if (r0 != (-1)) goto L6;
        return r2;
    L6:
        return r2.substring(r0 + 1);
    }
}
