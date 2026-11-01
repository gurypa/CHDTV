package androidx.core.content.res;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.StateSet;
import android.util.TypedValue;
import android.util.Xml;
import androidx.core.C0039R;
import androidx.core.math.MathUtils;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: loaded from: classes.dex */
public final class ColorStateListInflaterCompat {
    private static final ThreadLocal<TypedValue> sTempTypedValue = null;

    static {
        sTempTypedValue = new ThreadLocal();
    }

    private ColorStateListInflaterCompat() {
    }

    public static ColorStateList createFromXml(Resources r4, XmlPullParser r5, Resources.Theme r6) throws XmlPullParserException, IOException {
        AttributeSet r0 = Xml.asAttributeSet(r5);
    L3:
        int r1 = r5.next();
        if (r1 == 2) goto L8;
        if (r1 != 1) goto L3;
    L8:
        if (r1 != 2) goto L12;
        return createFromXmlInner(r4, r5, r0, r6);
    L12:
        throw new XmlPullParserException("No start tag found");
    }

    public static ColorStateList createFromXmlInner(Resources r4, XmlPullParser r5, AttributeSet r6, Resources.Theme r7) throws XmlPullParserException, IOException {
        String r0 = r5.getName();
        if (r0.equals("selector") == false) goto L7;
        return inflate(r4, r5, r6, r7);
    L7:
        throw new XmlPullParserException(r5.getPositionDescription() + ": invalid color state list tag " + r0);
    }

    private static TypedValue getTypedValue() {
        TypedValue r0 = sTempTypedValue.get();
        if (r0 != null) goto L6;
        TypedValue r1 = new TypedValue();
        sTempTypedValue.set(r1);
        return r1;
    L6:
        return r0;
    }

    public static ColorStateList inflate(Resources r3, int r4, Resources.Theme r5) {
        return createFromXml(r3, r3.getXml(r4), r5);
    L4:
        e = move-exception;
        Log.e("CSLCompat", "Failed to inflate ColorStateList.", e);
        return null;
    }

    private static ColorStateList inflate(Resources r20, XmlPullParser r21, AttributeSet r22, Resources.Theme r23) throws XmlPullParserException, IOException {
        r20 = r20;
        r23 = r23;
        int r4 = 1;
        int r5 = r21.getDepth() + 1;
        int[][] r0 = new int[20][];
        int r8 = 0;
        int[] r7 = new int[r0.length];
        int[][] r6 = r0;
    L3:
        int r1 = r21.next();
        int r9 = r1;
        if (r1 == r4) goto L59;
        int r2 = r21.getDepth();
        if (r2 >= r5) goto L12;
        if (r9 == 3) goto L59;
    L12:
        if (r9 != 2) goto L57;
        if (r2 > r5) goto L57;
        if (r21.getName().equals("item") == false) goto L57;
        TypedArray r12 = obtainAttributes(r20, r23, r22, C0039R.styleable.ColorStateListItem);
        int r14 = r12.getResourceId(C0039R.styleable.ColorStateListItem_android_color, -1);
        if (r14 != (-1)) goto L20;
    L26:
        int r3 = r12.getColor(C0039R.styleable.ColorStateListItem_android_color, -65281);
    L27:
        float r13 = 1.0f;
        if (r12.hasValue(C0039R.styleable.ColorStateListItem_android_alpha) == false) goto L31;
        r13 = r12.getFloat(C0039R.styleable.ColorStateListItem_android_alpha, 1.0f);
    L34:
        if (Build.VERSION.SDK_INT >= 31) goto L36;
    L38:
        float r10 = r12.getFloat(C0039R.styleable.ColorStateListItem_lStar, -1.0f);
    L39:
        r12.recycle();
        int r11 = 0;
        int r15 = r22.getAttributeCount();
        int[] r16 = new int[r15];
        int r17 = 0;
    L40:
        if (r17 >= r15) goto L55;
        int r18 = r5;
        int r19 = r22.getAttributeNameResource(r17);
        int r110 = r9;
        if (r19 == 16843173) goto L54;
        if (r19 == 16843551) goto L54;
        if (r19 == C0039R.attr.alpha) goto L54;
        if (r19 == C0039R.attr.lStar) goto L54;
        int r24 = r11 + 1;
        if (r22.getAttributeBooleanValue(r17, false) == false) goto L52;
        int r25 = r19;
    L53:
        r16[r11] = r25;
        r11 = r24;
        goto L54
    L52:
        r25 = -r19;
    L54:
        r17 = r17 + 1;
        r5 = r18;
        r9 = r110;
        goto L40
    L55:
        int[] r26 = StateSet.trimStateSet(r16, r11);
        r7 = GrowingArrayUtils.append(r7, r8, modulateColorAlpha(r3, r13, r10));
        r6 = (int[][]) GrowingArrayUtils.append(r6, r8, r26);
        r8 = r8 + 1;
        r5 = r5;
        r4 = 1;
        goto L3
    L36:
        if (r12.hasValue(C0039R.styleable.ColorStateListItem_android_lStar) == false) goto L38;
        r10 = r12.getFloat(C0039R.styleable.ColorStateListItem_android_lStar, -1.0f);
        goto L39
    L31:
        if (r12.hasValue(C0039R.styleable.ColorStateListItem_alpha) == false) goto L34;
        r13 = r12.getFloat(C0039R.styleable.ColorStateListItem_alpha, 1.0f);
        goto L34
    L20:
        if (isColorInt(r20, r14) == true) goto L26;
        r3 = createFromXml(r20, r20.getXml(r14), r23).getDefaultColor();     // Catch: Exception -> L23
    L24:
        r3 = r12.getColor(C0039R.styleable.ColorStateListItem_android_color, -65281);
    L57:
        r5 = r5;
        r4 = 1;
    L59:
        int[] r27 = new int[r8];
        int[][] r28 = new int[r8][];
        System.arraycopy(r7, 0, r27, 0, r8);
        System.arraycopy(r6, 0, r28, 0, r8);
        return new ColorStateList(r28, r27);
    }

    private static boolean isColorInt(Resources r4, int r5) {
        TypedValue r0 = getTypedValue();
        r4.getValue(r5, r0, true);
        if (r0.type >= 28) goto L5;
    L7:
        return false;
    L5:
        if (r0.type > 31) goto L7;
        return true;
    }

    private static int modulateColorAlpha(int r6, float r7, float r8) {
        if (r8 >= 0.0f) goto L5;
    L7:
        boolean r0 = false;
    L9:
        if (r7 != 1.0f) goto L12;
        if (r0 == true) goto L12;
        return r6;
    L12:
        int r1 = MathUtils.clamp((int) ((Color.alpha(r6) * r7) + 0.5f), 0, 255);
        if (r0 == false) goto L16;
        CamColor r3 = CamColor.fromColor(r6);
        r6 = CamColor.toColor(r3.getHue(), r3.getChroma(), r8);
    L16:
        return (16777215 & r6) | (r1 << 24);
    L5:
        if (r8 > 100.0f) goto L7;
        r0 = true;
        goto L9
    }

    private static TypedArray obtainAttributes(Resources r1, Resources.Theme r2, AttributeSet r3, int[] r4) {
        if (r2 != null) goto L5;
        return r1.obtainAttributes(r3, r4);
    L5:
        return r2.obtainStyledAttributes(r3, r4, 0, 0);
    }
}
