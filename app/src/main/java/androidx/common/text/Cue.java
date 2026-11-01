package androidx.media3.common.text;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import androidx.core.view.ViewCompat;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import com.google.common.base.Objects;
import java.io.ByteArrayOutputStream;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Iterator;
import org.checkerframework.dataflow.qual.Pure;

/* JADX INFO: loaded from: classes.dex */
public final class Cue {
    public static final int ANCHOR_TYPE_END = 2;
    public static final int ANCHOR_TYPE_MIDDLE = 1;
    public static final int ANCHOR_TYPE_START = 0;
    public static final float DIMEN_UNSET = -3.4028235E38f;

    @Deprecated
    public static final Cue EMPTY = null;
    private static final String FIELD_BITMAP_BYTES = null;
    private static final String FIELD_BITMAP_HEIGHT = null;
    private static final String FIELD_BITMAP_PARCELABLE = null;
    private static final String FIELD_CUSTOM_SPANS = null;
    private static final String FIELD_LINE = null;
    private static final String FIELD_LINE_ANCHOR = null;
    private static final String FIELD_LINE_TYPE = null;
    private static final String FIELD_MULTI_ROW_ALIGNMENT = null;
    private static final String FIELD_POSITION = null;
    private static final String FIELD_POSITION_ANCHOR = null;
    private static final String FIELD_SHEAR_DEGREES = null;
    private static final String FIELD_SIZE = null;
    private static final String FIELD_TEXT = null;
    private static final String FIELD_TEXT_ALIGNMENT = null;
    private static final String FIELD_TEXT_SIZE = null;
    private static final String FIELD_TEXT_SIZE_TYPE = null;
    private static final String FIELD_VERTICAL_TYPE = null;
    private static final String FIELD_WINDOW_COLOR = null;
    private static final String FIELD_WINDOW_COLOR_SET = null;
    public static final int LINE_TYPE_FRACTION = 0;
    public static final int LINE_TYPE_NUMBER = 1;
    public static final int TEXT_SIZE_TYPE_ABSOLUTE = 2;
    public static final int TEXT_SIZE_TYPE_FRACTIONAL = 0;
    public static final int TEXT_SIZE_TYPE_FRACTIONAL_IGNORE_PADDING = 1;
    public static final int TYPE_UNSET = Integer.MIN_VALUE;
    public static final int VERTICAL_TYPE_LR = 2;
    public static final int VERTICAL_TYPE_RL = 1;
    public final Bitmap bitmap;
    public final float bitmapHeight;
    public final float line;
    public final int lineAnchor;
    public final int lineType;
    public final Layout.Alignment multiRowAlignment;
    public final float position;
    public final int positionAnchor;
    public final float shearDegrees;
    public final float size;
    public final CharSequence text;
    public final Layout.Alignment textAlignment;
    public final float textSize;
    public final int textSizeType;
    public final int verticalType;
    public final int windowColor;
    public final boolean windowColorSet;

    /* JADX INFO: renamed from: androidx.media3.common.text.Cue$1 */
    static /* synthetic */ class C01981 {
    }

    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface AnchorType {
    }

    public static final class Builder {
        private Bitmap bitmap;
        private float bitmapHeight;
        private float line;
        private int lineAnchor;
        private int lineType;
        private Layout.Alignment multiRowAlignment;
        private float position;
        private int positionAnchor;
        private float shearDegrees;
        private float size;
        private CharSequence text;
        private Layout.Alignment textAlignment;
        private float textSize;
        private int textSizeType;
        private int verticalType;
        private int windowColor;
        private boolean windowColorSet;

        public Builder() {
            this.text = null;
            this.bitmap = null;
            this.textAlignment = null;
            this.multiRowAlignment = null;
            this.line = -3.4028235E38f;
            this.lineType = Integer.MIN_VALUE;
            this.lineAnchor = Integer.MIN_VALUE;
            this.position = -3.4028235E38f;
            this.positionAnchor = Integer.MIN_VALUE;
            this.textSizeType = Integer.MIN_VALUE;
            this.textSize = -3.4028235E38f;
            this.size = -3.4028235E38f;
            this.bitmapHeight = -3.4028235E38f;
            this.windowColorSet = false;
            this.windowColor = ViewCompat.MEASURED_STATE_MASK;
            this.verticalType = Integer.MIN_VALUE;
        }

        private Builder(Cue r2) {
            this.text = r2.text;
            this.bitmap = r2.bitmap;
            this.textAlignment = r2.textAlignment;
            this.multiRowAlignment = r2.multiRowAlignment;
            this.line = r2.line;
            this.lineType = r2.lineType;
            this.lineAnchor = r2.lineAnchor;
            this.position = r2.position;
            this.positionAnchor = r2.positionAnchor;
            this.textSizeType = r2.textSizeType;
            this.textSize = r2.textSize;
            this.size = r2.size;
            this.bitmapHeight = r2.bitmapHeight;
            this.windowColorSet = r2.windowColorSet;
            this.windowColor = r2.windowColor;
            this.verticalType = r2.verticalType;
            this.shearDegrees = r2.shearDegrees;
        }

        /* synthetic */ Builder(Cue r1, C01981 r2) {
            this(r1);
        }

        public Cue build() {
            return new Cue(this.text, this.textAlignment, this.multiRowAlignment, this.bitmap, this.line, this.lineType, this.lineAnchor, this.position, this.positionAnchor, this.textSizeType, this.textSize, this.size, this.bitmapHeight, this.windowColorSet, this.windowColor, this.verticalType, this.shearDegrees, null);
        }

        public Builder clearWindowColor() {
            this.windowColorSet = false;
            return this;
        }

        @Pure
        public Bitmap getBitmap() {
            return this.bitmap;
        }

        @Pure
        public float getBitmapHeight() {
            return this.bitmapHeight;
        }

        @Pure
        public float getLine() {
            return this.line;
        }

        @Pure
        public int getLineAnchor() {
            return this.lineAnchor;
        }

        @Pure
        public int getLineType() {
            return this.lineType;
        }

        @Pure
        public float getPosition() {
            return this.position;
        }

        @Pure
        public int getPositionAnchor() {
            return this.positionAnchor;
        }

        @Pure
        public float getSize() {
            return this.size;
        }

        @Pure
        public CharSequence getText() {
            return this.text;
        }

        @Pure
        public Layout.Alignment getTextAlignment() {
            return this.textAlignment;
        }

        @Pure
        public float getTextSize() {
            return this.textSize;
        }

        @Pure
        public int getTextSizeType() {
            return this.textSizeType;
        }

        @Pure
        public int getVerticalType() {
            return this.verticalType;
        }

        @Pure
        public int getWindowColor() {
            return this.windowColor;
        }

        public boolean isWindowColorSet() {
            return this.windowColorSet;
        }

        public Builder setBitmap(Bitmap r1) {
            this.bitmap = r1;
            return this;
        }

        public Builder setBitmapHeight(float r1) {
            this.bitmapHeight = r1;
            return this;
        }

        public Builder setLine(float r1, int r2) {
            this.line = r1;
            this.lineType = r2;
            return this;
        }

        public Builder setLineAnchor(int r1) {
            this.lineAnchor = r1;
            return this;
        }

        public Builder setMultiRowAlignment(Layout.Alignment r1) {
            this.multiRowAlignment = r1;
            return this;
        }

        public Builder setPosition(float r1) {
            this.position = r1;
            return this;
        }

        public Builder setPositionAnchor(int r1) {
            this.positionAnchor = r1;
            return this;
        }

        public Builder setShearDegrees(float r1) {
            this.shearDegrees = r1;
            return this;
        }

        public Builder setSize(float r1) {
            this.size = r1;
            return this;
        }

        public Builder setText(CharSequence r1) {
            this.text = r1;
            return this;
        }

        public Builder setTextAlignment(Layout.Alignment r1) {
            this.textAlignment = r1;
            return this;
        }

        public Builder setTextSize(float r1, int r2) {
            this.textSize = r1;
            this.textSizeType = r2;
            return this;
        }

        public Builder setVerticalType(int r1) {
            this.verticalType = r1;
            return this;
        }

        public Builder setWindowColor(int r2) {
            this.windowColor = r2;
            this.windowColorSet = true;
            return this;
        }
    }

    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface LineType {
    }

    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface TextSizeType {
    }

    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface VerticalType {
    }

    static {
        EMPTY = new Builder().setText("").build();
        FIELD_TEXT = Util.intToStringMaxRadix(0);
        FIELD_CUSTOM_SPANS = Util.intToStringMaxRadix(17);
        FIELD_TEXT_ALIGNMENT = Util.intToStringMaxRadix(1);
        FIELD_MULTI_ROW_ALIGNMENT = Util.intToStringMaxRadix(2);
        FIELD_BITMAP_PARCELABLE = Util.intToStringMaxRadix(3);
        FIELD_BITMAP_BYTES = Util.intToStringMaxRadix(18);
        FIELD_LINE = Util.intToStringMaxRadix(4);
        FIELD_LINE_TYPE = Util.intToStringMaxRadix(5);
        FIELD_LINE_ANCHOR = Util.intToStringMaxRadix(6);
        FIELD_POSITION = Util.intToStringMaxRadix(7);
        FIELD_POSITION_ANCHOR = Util.intToStringMaxRadix(8);
        FIELD_TEXT_SIZE_TYPE = Util.intToStringMaxRadix(9);
        FIELD_TEXT_SIZE = Util.intToStringMaxRadix(10);
        FIELD_SIZE = Util.intToStringMaxRadix(11);
        FIELD_BITMAP_HEIGHT = Util.intToStringMaxRadix(12);
        FIELD_WINDOW_COLOR = Util.intToStringMaxRadix(13);
        FIELD_WINDOW_COLOR_SET = Util.intToStringMaxRadix(14);
        FIELD_VERTICAL_TYPE = Util.intToStringMaxRadix(15);
        FIELD_SHEAR_DEGREES = Util.intToStringMaxRadix(16);
    }

    private Cue(CharSequence r17, Layout.Alignment r18, Layout.Alignment r19, Bitmap r20, float r21, int r22, int r23, float r24, int r25, int r26, float r27, float r28, float r29, boolean r30, int r31, int r32, float r33) {
        if (r17 != null) goto L5;
        Assertions.checkNotNull(r20);
    L10:
        if ((r17 instanceof Spanned) == false) goto L12;
        this.text = SpannedString.valueOf(r17);
    L15:
        this.textAlignment = r18;
        this.multiRowAlignment = r19;
        this.bitmap = r20;
        this.line = r21;
        this.lineType = r22;
        this.lineAnchor = r23;
        this.position = r24;
        this.positionAnchor = r25;
        this.size = r28;
        this.bitmapHeight = r29;
        this.windowColorSet = r30;
        this.windowColor = r31;
        this.textSizeType = r26;
        this.textSize = r27;
        this.verticalType = r32;
        this.shearDegrees = r33;
        return;
    L12:
        if (r17 == null) goto L14;
        this.text = r17.toString();
        goto L15
    L14:
        this.text = null;
        goto L15
    L5:
        if (r20 != null) goto L7;
        boolean r3 = true;
    L8:
        Assertions.checkArgument(r3);
        goto L10
    L7:
        r3 = false;
        goto L8
    }

    /* synthetic */ Cue(CharSequence r1, Layout.Alignment r2, Layout.Alignment r3, Bitmap r4, float r5, int r6, int r7, float r8, int r9, int r10, float r11, float r12, float r13, boolean r14, int r15, int r16, float r17, C01981 r18) {
        this(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17);
    }

    public static Cue fromBundle(Bundle r8) {
        Builder r0 = new Builder();
        CharSequence r1 = r8.getCharSequence(FIELD_TEXT);
        if (r1 == null) goto L11;
        r0.setText(r1);
        ArrayList r2 = r8.getParcelableArrayList(FIELD_CUSTOM_SPANS);
        if (r2 == null) goto L11;
        SpannableString r3 = SpannableString.valueOf(r1);
        Iterator r4 = r2.iterator();
    L8:
        if (r4.hasNext() == false) goto L10;
        CustomSpanBundler.unbundleAndApplyCustomSpan((Bundle) r4.next(), r3);
        goto L8
    L10:
        r0.setText(r3);
    L11:
        Layout.Alignment r5 = (Layout.Alignment) r8.getSerializable(FIELD_TEXT_ALIGNMENT);
        if (r5 == null) goto L14;
        r0.setTextAlignment(r5);
    L14:
        Layout.Alignment r6 = (Layout.Alignment) r8.getSerializable(FIELD_MULTI_ROW_ALIGNMENT);
        if (r6 == null) goto L17;
        r0.setMultiRowAlignment(r6);
    L17:
        Bitmap r7 = (Bitmap) r8.getParcelable(FIELD_BITMAP_PARCELABLE);
        if (r7 == null) goto L20;
        r0.setBitmap(r7);
    L24:
        if (r8.containsKey(FIELD_LINE) == false) goto L29;
        if (r8.containsKey(FIELD_LINE_TYPE) == false) goto L29;
        r0.setLine(r8.getFloat(FIELD_LINE), r8.getInt(FIELD_LINE_TYPE));
    L29:
        if (r8.containsKey(FIELD_LINE_ANCHOR) == false) goto L32;
        r0.setLineAnchor(r8.getInt(FIELD_LINE_ANCHOR));
    L32:
        if (r8.containsKey(FIELD_POSITION) == false) goto L35;
        r0.setPosition(r8.getFloat(FIELD_POSITION));
    L35:
        if (r8.containsKey(FIELD_POSITION_ANCHOR) == false) goto L38;
        r0.setPositionAnchor(r8.getInt(FIELD_POSITION_ANCHOR));
    L38:
        if (r8.containsKey(FIELD_TEXT_SIZE) == false) goto L43;
        if (r8.containsKey(FIELD_TEXT_SIZE_TYPE) == false) goto L43;
        r0.setTextSize(r8.getFloat(FIELD_TEXT_SIZE), r8.getInt(FIELD_TEXT_SIZE_TYPE));
    L43:
        if (r8.containsKey(FIELD_SIZE) == false) goto L46;
        r0.setSize(r8.getFloat(FIELD_SIZE));
    L46:
        if (r8.containsKey(FIELD_BITMAP_HEIGHT) == false) goto L49;
        r0.setBitmapHeight(r8.getFloat(FIELD_BITMAP_HEIGHT));
    L49:
        if (r8.containsKey(FIELD_WINDOW_COLOR) == false) goto L52;
        r0.setWindowColor(r8.getInt(FIELD_WINDOW_COLOR));
    L52:
        if (r8.getBoolean(FIELD_WINDOW_COLOR_SET, false) == true) goto L55;
        r0.clearWindowColor();
    L55:
        if (r8.containsKey(FIELD_VERTICAL_TYPE) == false) goto L58;
        r0.setVerticalType(r8.getInt(FIELD_VERTICAL_TYPE));
    L58:
        if (r8.containsKey(FIELD_SHEAR_DEGREES) == false) goto L61;
        r0.setShearDegrees(r8.getFloat(FIELD_SHEAR_DEGREES));
    L61:
        return r0.build();
    L20:
        byte[] r9 = r8.getByteArray(FIELD_BITMAP_BYTES);
        if (r9 == null) goto L24;
        r0.setBitmap(BitmapFactory.decodeByteArray(r9, 0, r9.length));
        goto L24
    }

    private Bundle toBundleWithoutBitmap() {
        Bundle r0 = new Bundle();
        if (this.text == null) goto L9;
        r0.putCharSequence(FIELD_TEXT, this.text);
        if ((this.text instanceof Spanned) == false) goto L9;
        ArrayList<Bundle> r1 = CustomSpanBundler.bundleCustomSpans((Spanned) this.text);
        if (r1.isEmpty() == true) goto L9;
        r0.putParcelableArrayList(FIELD_CUSTOM_SPANS, r1);
    L9:
        r0.putSerializable(FIELD_TEXT_ALIGNMENT, this.textAlignment);
        r0.putSerializable(FIELD_MULTI_ROW_ALIGNMENT, this.multiRowAlignment);
        r0.putFloat(FIELD_LINE, this.line);
        r0.putInt(FIELD_LINE_TYPE, this.lineType);
        r0.putInt(FIELD_LINE_ANCHOR, this.lineAnchor);
        r0.putFloat(FIELD_POSITION, this.position);
        r0.putInt(FIELD_POSITION_ANCHOR, this.positionAnchor);
        r0.putInt(FIELD_TEXT_SIZE_TYPE, this.textSizeType);
        r0.putFloat(FIELD_TEXT_SIZE, this.textSize);
        r0.putFloat(FIELD_SIZE, this.size);
        r0.putFloat(FIELD_BITMAP_HEIGHT, this.bitmapHeight);
        r0.putBoolean(FIELD_WINDOW_COLOR_SET, this.windowColorSet);
        r0.putInt(FIELD_WINDOW_COLOR, this.windowColor);
        r0.putInt(FIELD_VERTICAL_TYPE, this.verticalType);
        r0.putFloat(FIELD_SHEAR_DEGREES, this.shearDegrees);
        return r0;
    }

    public Builder buildUpon() {
        return new Builder(this, null);
    }

    public boolean equals(Object r6) {
        if (this != r6) goto L6;
        return true;
    L6:
        if (r6 != null) goto L8;
    L54:
        return false;
    L8:
        if (getClass() != r6.getClass()) goto L54;
        Cue r2 = (Cue) r6;
        if (TextUtils.equals(this.text, r2.text) == false) goto L53;
        if (this.textAlignment != r2.textAlignment) goto L53;
        if (this.multiRowAlignment != r2.multiRowAlignment) goto L53;
        if (this.bitmap != null) goto L22;
        if (r2.bitmap != null) goto L53;
    L26:
        if (this.line != r2.line) goto L53;
        if (this.lineType != r2.lineType) goto L53;
        if (this.lineAnchor != r2.lineAnchor) goto L53;
        if (this.position != r2.position) goto L53;
        if (this.positionAnchor != r2.positionAnchor) goto L53;
        if (this.size != r2.size) goto L53;
        if (this.bitmapHeight != r2.bitmapHeight) goto L53;
        if (this.windowColorSet != r2.windowColorSet) goto L53;
        if (this.windowColor != r2.windowColor) goto L53;
        if (this.textSizeType != r2.textSizeType) goto L53;
        if (this.textSize != r2.textSize) goto L53;
        if (this.verticalType != r2.verticalType) goto L53;
        if (this.shearDegrees != r2.shearDegrees) goto L53;
        return true;
    L22:
        if (r2.bitmap == null) goto L53;
        if (this.bitmap.sameAs(r2.bitmap) == true) goto L26;
    L53:
        return false;
    }

    public int hashCode() {
        return Objects.hashCode(new Object[]{this.text, this.textAlignment, this.multiRowAlignment, this.bitmap, Float.valueOf(this.line), Integer.valueOf(this.lineType), Integer.valueOf(this.lineAnchor), Float.valueOf(this.position), Integer.valueOf(this.positionAnchor), Float.valueOf(this.size), Float.valueOf(this.bitmapHeight), Boolean.valueOf(this.windowColorSet), Integer.valueOf(this.windowColor), Integer.valueOf(this.textSizeType), Float.valueOf(this.textSize), Integer.valueOf(this.verticalType), Float.valueOf(this.shearDegrees)});
    }

    public Bundle toBinderBasedBundle() {
        Bundle r0 = toBundleWithoutBitmap();
        if (this.bitmap == null) goto L5;
        r0.putParcelable(FIELD_BITMAP_PARCELABLE, this.bitmap);
    L5:
        return r0;
    }

    @Deprecated
    public Bundle toBundle() {
        return toBinderBasedBundle();
    }

    public Bundle toSerializableBundle() {
        Bundle r0 = toBundleWithoutBitmap();
        if (this.bitmap == null) goto L5;
        ByteArrayOutputStream r1 = new ByteArrayOutputStream();
        Assertions.checkState(this.bitmap.compress(Bitmap.CompressFormat.PNG, 0, r1));
        r0.putByteArray(FIELD_BITMAP_BYTES, r1.toByteArray());
    L5:
        return r0;
    }
}
