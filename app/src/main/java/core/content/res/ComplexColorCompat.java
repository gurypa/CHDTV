package androidx.core.content.res;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: loaded from: classes.dex */
public final class ComplexColorCompat {
    private static final String LOG_TAG = "ComplexColorCompat";
    private int mColor;
    private final ColorStateList mColorStateList;
    private final Shader mShader;

    private ComplexColorCompat(Shader r1, ColorStateList r2, int r3) {
        this.mShader = r1;
        this.mColorStateList = r2;
        this.mColor = r3;
    }

    private static ComplexColorCompat createFromXml(Resources r7, int r8, Resources.Theme r9) throws IOException, XmlPullParserException {
        XmlResourceParser r0 = r7.getXml(r8);
        AttributeSet r1 = Xml.asAttributeSet(r0);
    L3:
        int r2 = r0.next();
        byte r4 = 1;
        if (r2 == 2) goto L7;
        if (r2 != 1) goto L3;
    L7:
        if (r2 != 2) goto L26;
        String r3 = r0.getName();
        switch(r3.hashCode()) {
            case 89650992: goto L15;
            case 1191572447: goto L12;
            default: goto L17;
        };
    L17:
        r4 = -1;
    L18:
        switch(r4) {
            case 0: goto L24;
            case 1: goto L22;
            default: goto L20;
        };
    L20:
        throw new XmlPullParserException(r0.getPositionDescription() + ": unsupported complex color tag " + r3);
    L22:
        return from(GradientColorInflaterCompat.createFromXmlInner(r7, r0, r1, r9));
    L24:
        return from(ColorStateListInflaterCompat.createFromXmlInner(r7, r0, r1, r9));
    L12:
        if (r3.equals("selector") == false) goto L17;
        r4 = 0;
        goto L18
    L15:
        if (r3.equals("gradient") == false) goto L17;
    L26:
        throw new XmlPullParserException("No start tag found");
    }

    static ComplexColorCompat from(int r2) {
        return new ComplexColorCompat(null, null, r2);
    }

    static ComplexColorCompat from(ColorStateList r3) {
        return new ComplexColorCompat(null, r3, r3.getDefaultColor());
    }

    static ComplexColorCompat from(Shader r3) {
        return new ComplexColorCompat(r3, null, 0);
    }

    public static ComplexColorCompat inflate(Resources r3, int r4, Resources.Theme r5) {
        return createFromXml(r3, r4, r5);
    L4:
        e = move-exception;
        Log.e(LOG_TAG, "Failed to inflate ComplexColor.", e);
        return null;
    }

    public int getColor() {
        return this.mColor;
    }

    public Shader getShader() {
        return this.mShader;
    }

    public boolean isGradient() {
        if (this.mShader == null) goto L5;
        return true;
    L5:
        return false;
    }

    public boolean isStateful() {
        if (this.mShader == null) goto L5;
    L9:
        return false;
    L5:
        if (this.mColorStateList == null) goto L9;
        if (this.mColorStateList.isStateful() == false) goto L9;
        return true;
    }

    public boolean onStateChanged(int[] r4) {
        if (isStateful() == false) goto L8;
        int r1 = this.mColorStateList.getColorForState(r4, this.mColorStateList.getDefaultColor());
        if (r1 == this.mColor) goto L9;
        this.mColor = r1;
        return true;
    L9:
        return false;
    L8:
        return false;
    }

    public void setColor(int r1) {
        this.mColor = r1;
    }

    public boolean willDraw() {
        if (isGradient() == false) goto L5;
    L8:
        return true;
    L5:
        if (this.mColor != 0) goto L8;
        return false;
    }
}
