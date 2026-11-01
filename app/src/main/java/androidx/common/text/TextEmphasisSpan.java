package androidx.media3.common.text;

import android.os.Bundle;
import androidx.media3.common.util.Util;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* JADX INFO: loaded from: classes.dex */
public final class TextEmphasisSpan implements LanguageFeatureSpan {
    private static final String FIELD_MARK_FILL = null;
    private static final String FIELD_MARK_SHAPE = null;
    private static final String FIELD_POSITION = null;
    public static final int MARK_FILL_FILLED = 1;
    public static final int MARK_FILL_OPEN = 2;
    public static final int MARK_FILL_UNKNOWN = 0;
    public static final int MARK_SHAPE_CIRCLE = 1;
    public static final int MARK_SHAPE_DOT = 2;
    public static final int MARK_SHAPE_NONE = 0;
    public static final int MARK_SHAPE_SESAME = 3;
    public int markFill;
    public int markShape;
    public final int position;

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface MarkFill {
    }

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface MarkShape {
    }

    static {
        FIELD_MARK_SHAPE = Util.intToStringMaxRadix(0);
        FIELD_MARK_FILL = Util.intToStringMaxRadix(1);
        FIELD_POSITION = Util.intToStringMaxRadix(2);
    }

    public TextEmphasisSpan(int r1, int r2, int r3) {
        this.markShape = r1;
        this.markFill = r2;
        this.position = r3;
    }

    public static TextEmphasisSpan fromBundle(Bundle r4) {
        return new TextEmphasisSpan(r4.getInt(FIELD_MARK_SHAPE), r4.getInt(FIELD_MARK_FILL), r4.getInt(FIELD_POSITION));
    }

    public Bundle toBundle() {
        Bundle r0 = new Bundle();
        r0.putInt(FIELD_MARK_SHAPE, this.markShape);
        r0.putInt(FIELD_MARK_FILL, this.markFill);
        r0.putInt(FIELD_POSITION, this.position);
        return r0;
    }
}
