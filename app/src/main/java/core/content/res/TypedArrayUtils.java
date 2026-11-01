package androidx.core.content.res;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import org.xmlpull.v1.XmlPullParser;

/* JADX INFO: loaded from: classes.dex */
public class TypedArrayUtils {
    private static final String NAMESPACE = "http://schemas.android.com/apk/res/android";

    private TypedArrayUtils() {
    }

    public static int getAttr(Context r3, int r4, int r5) {
        TypedValue r0 = new TypedValue();
        r3.getTheme().resolveAttribute(r4, r0, true);
        if (r0.resourceId == 0) goto L5;
        return r4;
    L5:
        return r5;
    }

    public static boolean getBoolean(TypedArray r2, int r3, int r4, boolean r5) {
        return r2.getBoolean(r3, r2.getBoolean(r4, r5));
    }

    public static Drawable getDrawable(TypedArray r1, int r2, int r3) {
        Drawable r0 = r1.getDrawable(r2);
        if (r0 == null) goto L5;
        return r0;
    L5:
        return r1.getDrawable(r3);
    }

    public static int getInt(TypedArray r2, int r3, int r4, int r5) {
        return r2.getInt(r3, r2.getInt(r4, r5));
    }

    public static boolean getNamedBoolean(TypedArray r2, XmlPullParser r3, String r4, int r5, boolean r6) {
        if (hasAttribute(r3, r4) == true) goto L6;
        return r6;
    L6:
        return r2.getBoolean(r5, r6);
    }

    public static int getNamedColor(TypedArray r2, XmlPullParser r3, String r4, int r5, int r6) {
        if (hasAttribute(r3, r4) == true) goto L6;
        return r6;
    L6:
        return r2.getColor(r5, r6);
    }

    public static ColorStateList getNamedColorStateList(TypedArray r4, XmlPullParser r5, Resources.Theme r6, String r7, int r8) {
        if (hasAttribute(r5, r7) == false) goto L16;
        TypedValue r0 = new TypedValue();
        r4.getValue(r8, r0);
        if (r0.type == 2) goto L15;
        if (r0.type < 28) goto L13;
        if (r0.type > 31) goto L13;
        return getNamedColorStateListFromInt(r0);
    L13:
        return ColorStateListInflaterCompat.inflate(r4.getResources(), r4.getResourceId(r8, 0), r6);
    L15:
        throw new UnsupportedOperationException("Failed to resolve attribute at index " + r8 + ": " + r0);
    L16:
        return null;
    }

    private static ColorStateList getNamedColorStateListFromInt(TypedValue r1) {
        return ColorStateList.valueOf(r1.data);
    }

    public static ComplexColorCompat getNamedComplexColor(TypedArray r3, XmlPullParser r4, Resources.Theme r5, String r6, int r7, int r8) {
        if (hasAttribute(r4, r6) == false) goto L14;
        TypedValue r0 = new TypedValue();
        r3.getValue(r7, r0);
        if (r0.type >= 28) goto L7;
    L10:
        ComplexColorCompat r1 = ComplexColorCompat.inflate(r3.getResources(), r3.getResourceId(r7, 0), r5);
        if (r1 == null) goto L14;
        return r1;
    L7:
        if (r0.type > 31) goto L10;
        return ComplexColorCompat.from(r0.data);
    L14:
        return ComplexColorCompat.from(r8);
    }

    public static float getNamedFloat(TypedArray r2, XmlPullParser r3, String r4, int r5, float r6) {
        if (hasAttribute(r3, r4) == true) goto L6;
        return r6;
    L6:
        return r2.getFloat(r5, r6);
    }

    public static int getNamedInt(TypedArray r2, XmlPullParser r3, String r4, int r5, int r6) {
        if (hasAttribute(r3, r4) == true) goto L6;
        return r6;
    L6:
        return r2.getInt(r5, r6);
    }

    public static int getNamedResourceId(TypedArray r2, XmlPullParser r3, String r4, int r5, int r6) {
        if (hasAttribute(r3, r4) == true) goto L6;
        return r6;
    L6:
        return r2.getResourceId(r5, r6);
    }

    public static String getNamedString(TypedArray r2, XmlPullParser r3, String r4, int r5) {
        if (hasAttribute(r3, r4) == true) goto L7;
        return null;
    L7:
        return r2.getString(r5);
    }

    public static int getResourceId(TypedArray r2, int r3, int r4, int r5) {
        return r2.getResourceId(r3, r2.getResourceId(r4, r5));
    }

    public static String getString(TypedArray r1, int r2, int r3) {
        String r0 = r1.getString(r2);
        if (r0 == null) goto L5;
        return r0;
    L5:
        return r1.getString(r3);
    }

    public static CharSequence getText(TypedArray r1, int r2, int r3) {
        CharSequence r0 = r1.getText(r2);
        if (r0 == null) goto L5;
        return r0;
    L5:
        return r1.getText(r3);
    }

    public static CharSequence[] getTextArray(TypedArray r1, int r2, int r3) {
        CharSequence[] r0 = r1.getTextArray(r2);
        if (r0 == null) goto L5;
        return r0;
    L5:
        return r1.getTextArray(r3);
    }

    public static boolean hasAttribute(XmlPullParser r1, String r2) {
        if (r1.getAttributeValue(NAMESPACE, r2) == null) goto L5;
        return true;
    L5:
        return false;
    }

    public static TypedArray obtainAttributes(Resources r1, Resources.Theme r2, AttributeSet r3, int[] r4) {
        if (r2 != null) goto L6;
        return r1.obtainAttributes(r3, r4);
    L6:
        return r2.obtainStyledAttributes(r3, r4, 0, 0);
    }

    public static TypedValue peekNamedValue(TypedArray r2, XmlPullParser r3, String r4, int r5) {
        if (hasAttribute(r3, r4) == true) goto L7;
        return null;
    L7:
        return r2.peekValue(r5);
    }
}
