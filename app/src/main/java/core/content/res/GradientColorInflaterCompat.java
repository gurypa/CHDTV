package androidx.core.content.res;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.LinearGradient;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Xml;
import androidx.core.C0039R;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: loaded from: classes.dex */
final class GradientColorInflaterCompat {
    private static final int TILE_MODE_CLAMP = 0;
    private static final int TILE_MODE_MIRROR = 2;
    private static final int TILE_MODE_REPEAT = 1;

    static final class ColorStops {
        final int[] mColors;
        final float[] mOffsets;

        ColorStops(int r2, int r3) {
            this.mColors = new int[]{r2, r3};
            this.mOffsets = new float[]{0.0f, 1.0f};
        }

        ColorStops(int r2, int r3, int r4) {
            this.mColors = new int[]{r2, r3, r4};
            this.mOffsets = new float[]{0.0f, 0.5f, 1.0f};
        }

        ColorStops(List<Integer> r5, List<Float> r6) {
            int r0 = r5.size();
            this.mColors = new int[r0];
            this.mOffsets = new float[r0];
            int r1 = 0;
        L3:
            if (r1 >= r0) goto L5;
            this.mColors[r1] = r5.get(r1).intValue();
            this.mOffsets[r1] = r6.get(r1).floatValue();
            r1 = r1 + 1;
            goto L3
        }
    }

    private GradientColorInflaterCompat() {
    }

    private static ColorStops checkColors(ColorStops r1, int r2, int r3, boolean r4, int r5) {
        if (r1 == null) goto L4;
        return r1;
    L4:
        if (r4 == false) goto L8;
        return new ColorStops(r2, r5, r3);
    L8:
        return new ColorStops(r2, r3);
    }

    static Shader createFromXml(Resources r4, XmlPullParser r5, Resources.Theme r6) throws XmlPullParserException, IOException {
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

    static Shader createFromXmlInner(Resources r23, XmlPullParser r24, AttributeSet r25, Resources.Theme r26) throws IOException, XmlPullParserException {
        String r1 = r24.getName();
        if (r1.equals("gradient") == false) goto L17;
        TypedArray r2 = TypedArrayUtils.obtainAttributes(r23, r26, r25, C0039R.styleable.GradientColor);
        float r10 = TypedArrayUtils.getNamedFloat(r2, r24, "startX", C0039R.styleable.GradientColor_android_startX, 0.0f);
        float r11 = TypedArrayUtils.getNamedFloat(r2, r24, "startY", C0039R.styleable.GradientColor_android_startY, 0.0f);
        float r12 = TypedArrayUtils.getNamedFloat(r2, r24, "endX", C0039R.styleable.GradientColor_android_endX, 0.0f);
        float r13 = TypedArrayUtils.getNamedFloat(r2, r24, "endY", C0039R.styleable.GradientColor_android_endY, 0.0f);
        float r15 = TypedArrayUtils.getNamedFloat(r2, r24, "centerX", C0039R.styleable.GradientColor_android_centerX, 0.0f);
        float r6 = TypedArrayUtils.getNamedFloat(r2, r24, "centerY", C0039R.styleable.GradientColor_android_centerY, 0.0f);
        int r7 = TypedArrayUtils.getNamedInt(r2, r24, "type", C0039R.styleable.GradientColor_android_type, 0);
        int r8 = TypedArrayUtils.getNamedColor(r2, r24, "startColor", C0039R.styleable.GradientColor_android_startColor, 0);
        boolean r14 = TypedArrayUtils.hasAttribute(r24, "centerColor");
        int r3 = TypedArrayUtils.getNamedColor(r2, r24, "centerColor", C0039R.styleable.GradientColor_android_centerColor, 0);
        int r5 = TypedArrayUtils.getNamedColor(r2, r24, "endColor", C0039R.styleable.GradientColor_android_endColor, 0);
        int r4 = TypedArrayUtils.getNamedInt(r2, r24, "tileMode", C0039R.styleable.GradientColor_android_tileMode, 0);
        float r17 = TypedArrayUtils.getNamedFloat(r2, r24, "gradientRadius", C0039R.styleable.GradientColor_android_gradientRadius, 0.0f);
        r2.recycle();
        ColorStops r9 = checkColors(inflateChildElements(r23, r24, r25, r26), r8, r5, r14, r3);
        switch(r7) {
            case 1: goto L11;
            case 2: goto L9;
            default: goto L7;
        };
    L7:
        return new LinearGradient(r10, r11, r12, r13, r9.mColors, r9.mOffsets, parseTileMode(r4));
    L9:
        return new SweepGradient(r15, r6, r9.mColors, r9.mOffsets);
    L11:
        if (r17 <= 0.0f) goto L15;
        return new RadialGradient(r15, r6, r17, r9.mColors, r9.mOffsets, parseTileMode(r4));
    L15:
        throw new XmlPullParserException("<gradient> tag requires 'gradientRadius' attribute with radial type");
    L17:
        throw new XmlPullParserException(r24.getPositionDescription() + ": invalid gradient color tag " + r1);
    }

    private static ColorStops inflateChildElements(Resources r12, XmlPullParser r13, AttributeSet r14, Resources.Theme r15) throws XmlPullParserException, IOException {
        int r0 = r13.getDepth() + 1;
        ArrayList r2 = new ArrayList(20);
        ArrayList r4 = new ArrayList(20);
    L3:
        int r3 = r13.next();
        if (r3 == 1) goto L23;
        int r5 = r13.getDepth();
        if (r5 >= r0) goto L10;
        if (r3 == 3) goto L23;
    L10:
        if (r3 != 2) goto L3;
        if (r5 > r0) goto L3;
        if (r13.getName().equals("item") == false) goto L3;
        TypedArray r6 = TypedArrayUtils.obtainAttributes(r12, r15, r14, C0039R.styleable.GradientColorItem);
        boolean r7 = r6.hasValue(C0039R.styleable.GradientColorItem_android_color);
        boolean r8 = r6.hasValue(C0039R.styleable.GradientColorItem_android_offset);
        if (r7 == false) goto L21;
        if (r8 == false) goto L21;
        int r9 = r6.getColor(C0039R.styleable.GradientColorItem_android_color, 0);
        float r10 = r6.getFloat(C0039R.styleable.GradientColorItem_android_offset, 0.0f);
        r6.recycle();
        r4.add(Integer.valueOf(r9));
        r2.add(Float.valueOf(r10));
    L21:
        throw new XmlPullParserException(r13.getPositionDescription() + ": <item> tag requires a 'color' attribute and a 'offset' attribute!");
    L23:
        if (r4.size() > 0) goto L25;
        return null;
    L25:
        return new ColorStops(r4, r2);
    }

    private static Shader.TileMode parseTileMode(int r1) {
        switch(r1) {
            case 1: goto L8;
            case 2: goto L6;
            default: goto L4;
        };
    L4:
        return Shader.TileMode.CLAMP;
    L6:
        return Shader.TileMode.MIRROR;
    L8:
        return Shader.TileMode.REPEAT;
    }
}
