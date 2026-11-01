package androidx.media3.p002ui;

import android.graphics.Typeface;
import android.view.accessibility.CaptioningManager;
import androidx.core.view.ViewCompat;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* JADX INFO: loaded from: classes.dex */
public final class CaptionStyleCompat {
    public static final CaptionStyleCompat DEFAULT = null;
    public static final int EDGE_TYPE_DEPRESSED = 4;
    public static final int EDGE_TYPE_DROP_SHADOW = 2;
    public static final int EDGE_TYPE_NONE = 0;
    public static final int EDGE_TYPE_OUTLINE = 1;
    public static final int EDGE_TYPE_RAISED = 3;
    public static final int USE_TRACK_COLOR_SETTINGS = 1;
    public final int backgroundColor;
    public final int edgeColor;
    public final int edgeType;
    public final int foregroundColor;
    public final Typeface typeface;
    public final int windowColor;

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface EdgeType {
    }

    static {
        DEFAULT = new CaptionStyleCompat(-1, ViewCompat.MEASURED_STATE_MASK, 0, 0, -1, null);
    }

    public CaptionStyleCompat(int r1, int r2, int r3, int r4, int r5, Typeface r6) {
        this.foregroundColor = r1;
        this.backgroundColor = r2;
        this.windowColor = r3;
        this.edgeType = r4;
        this.edgeColor = r5;
        this.typeface = r6;
    }

    public static CaptionStyleCompat createFromCaptionStyle(CaptioningManager.CaptionStyle r7) {
        if (r7.hasForegroundColor() == false) goto L5;
        int r1 = r7.foregroundColor;
    L7:
        if (r7.hasBackgroundColor() == false) goto L9;
        int r2 = r7.backgroundColor;
    L11:
        if (r7.hasWindowColor() == false) goto L13;
        int r3 = r7.windowColor;
    L15:
        if (r7.hasEdgeType() == false) goto L17;
        int r4 = r7.edgeType;
    L19:
        if (r7.hasEdgeColor() == false) goto L21;
        int r5 = r7.edgeColor;
    L23:
        return new CaptionStyleCompat(r1, r2, r3, r4, r5, r7.getTypeface());
    L21:
        r5 = DEFAULT.edgeColor;
        goto L23
    L17:
        r4 = DEFAULT.edgeType;
        goto L19
    L13:
        r3 = DEFAULT.windowColor;
        goto L15
    L9:
        r2 = DEFAULT.backgroundColor;
        goto L11
    L5:
        r1 = DEFAULT.foregroundColor;
        goto L7
    }
}
