package androidx.core.content.res;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import com.arthenica.ffmpegkit.StreamInformation;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: TypedArray.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000R\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\r\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0016\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\b\b\u0001\u0010\u0003\u001a\u00020\u0004H\u0002\u001a\u0014\u0010\u0005\u001a\u00020\u0006*\u00020\u00022\b\b\u0001\u0010\u0003\u001a\u00020\u0004\u001a\u0016\u0010\u0007\u001a\u00020\u0004*\u00020\u00022\b\b\u0001\u0010\u0003\u001a\u00020\u0004H\u0007\u001a\u0014\u0010\b\u001a\u00020\t*\u00020\u00022\b\b\u0001\u0010\u0003\u001a\u00020\u0004\u001a\u0014\u0010\n\u001a\u00020\u000b*\u00020\u00022\b\b\u0001\u0010\u0003\u001a\u00020\u0004\u001a\u0016\u0010\f\u001a\u00020\u0004*\u00020\u00022\b\b\u0001\u0010\u0003\u001a\u00020\u0004H\u0007\u001a\u0016\u0010\r\u001a\u00020\u0004*\u00020\u00022\b\b\u0001\u0010\u0003\u001a\u00020\u0004H\u0007\u001a\u0014\u0010\u000e\u001a\u00020\u000f*\u00020\u00022\b\b\u0001\u0010\u0003\u001a\u00020\u0004\u001a\u0014\u0010\u0010\u001a\u00020\u000b*\u00020\u00022\b\b\u0001\u0010\u0003\u001a\u00020\u0004\u001a\u0016\u0010\u0011\u001a\u00020\u0012*\u00020\u00022\b\b\u0001\u0010\u0003\u001a\u00020\u0004H\u0007\u001a\u0014\u0010\u0013\u001a\u00020\u0004*\u00020\u00022\b\b\u0001\u0010\u0003\u001a\u00020\u0004\u001a\u0014\u0010\u0014\u001a\u00020\u0004*\u00020\u00022\b\b\u0001\u0010\u0003\u001a\u00020\u0004\u001a\u0016\u0010\u0015\u001a\u00020\u0004*\u00020\u00022\b\b\u0001\u0010\u0003\u001a\u00020\u0004H\u0007\u001a\u0014\u0010\u0016\u001a\u00020\u0017*\u00020\u00022\b\b\u0001\u0010\u0003\u001a\u00020\u0004\u001a\u001f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u001a0\u0019*\u00020\u00022\b\b\u0001\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u001b\u001a\u0014\u0010\u001c\u001a\u00020\u001a*\u00020\u00022\b\b\u0001\u0010\u0003\u001a\u00020\u0004\u001a/\u0010\u001d\u001a\u0002H\u001e\"\u0004\b\u0000\u0010\u001e*\u00020\u00022\u0012\u0010\u001f\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u0002H\u001e0 H\u0086\bø\u0001\u0000¢\u0006\u0002\u0010!\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\""}, m421d2 = {"checkAttribute", "", "Landroid/content/res/TypedArray;", StreamInformation.KEY_INDEX, "", "getBooleanOrThrow", "", "getColorOrThrow", "getColorStateListOrThrow", "Landroid/content/res/ColorStateList;", "getDimensionOrThrow", "", "getDimensionPixelOffsetOrThrow", "getDimensionPixelSizeOrThrow", "getDrawableOrThrow", "Landroid/graphics/drawable/Drawable;", "getFloatOrThrow", "getFontOrThrow", "Landroid/graphics/Typeface;", "getIntOrThrow", "getIntegerOrThrow", "getResourceIdOrThrow", "getStringOrThrow", "", "getTextArrayOrThrow", "", "", "(Landroid/content/res/TypedArray;I)[Ljava/lang/CharSequence;", "getTextOrThrow", "use", "R", "block", "Lkotlin/Function1;", "(Landroid/content/res/TypedArray;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "core-ktx_release"}, m422k = 2, m423mv = {1, 7, 1}, m425xi = 48)
public final class TypedArrayKt {
    private static final void checkAttribute(TypedArray r2, int r3) {
        if (r2.hasValue(r3) == false) goto L6;
        return;
    L6:
        throw new IllegalArgumentException("Attribute not defined in set.");
    }

    public static final boolean getBooleanOrThrow(TypedArray r1, int r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        checkAttribute(r1, r2);
        return r1.getBoolean(r2, false);
    }

    public static final int getColorOrThrow(TypedArray r1, int r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        checkAttribute(r1, r2);
        return r1.getColor(r2, 0);
    }

    public static final ColorStateList getColorStateListOrThrow(TypedArray r2, int r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        checkAttribute(r2, r3);
        ColorStateList r0 = r2.getColorStateList(r3);
        if (r0 == null) goto L6;
        return r0;
    L6:
        throw new IllegalStateException("Attribute value was not a color or color state list.".toString());
    }

    public static final float getDimensionOrThrow(TypedArray r1, int r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        checkAttribute(r1, r2);
        return r1.getDimension(r2, 0.0f);
    }

    public static final int getDimensionPixelOffsetOrThrow(TypedArray r1, int r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        checkAttribute(r1, r2);
        return r1.getDimensionPixelOffset(r2, 0);
    }

    public static final int getDimensionPixelSizeOrThrow(TypedArray r1, int r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        checkAttribute(r1, r2);
        return r1.getDimensionPixelSize(r2, 0);
    }

    public static final Drawable getDrawableOrThrow(TypedArray r1, int r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        checkAttribute(r1, r2);
        Drawable r0 = r1.getDrawable(r2);
        Intrinsics.checkNotNull(r0);
        return r0;
    }

    public static final float getFloatOrThrow(TypedArray r1, int r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        checkAttribute(r1, r2);
        return r1.getFloat(r2, 0.0f);
    }

    public static final Typeface getFontOrThrow(TypedArray r1, int r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        checkAttribute(r1, r2);
        return TypedArrayApi26ImplKt.getFont(r1, r2);
    }

    public static final int getIntOrThrow(TypedArray r1, int r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        checkAttribute(r1, r2);
        return r1.getInt(r2, 0);
    }

    public static final int getIntegerOrThrow(TypedArray r1, int r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        checkAttribute(r1, r2);
        return r1.getInteger(r2, 0);
    }

    public static final int getResourceIdOrThrow(TypedArray r1, int r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        checkAttribute(r1, r2);
        return r1.getResourceId(r2, 0);
    }

    public static final String getStringOrThrow(TypedArray r2, int r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        checkAttribute(r2, r3);
        String r0 = r2.getString(r3);
        if (r0 == null) goto L6;
        return r0;
    L6:
        throw new IllegalStateException("Attribute value could not be coerced to String.".toString());
    }

    public static final CharSequence[] getTextArrayOrThrow(TypedArray r2, int r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        checkAttribute(r2, r3);
        CharSequence[] r0 = r2.getTextArray(r3);
        Intrinsics.checkNotNullExpressionValue(r0, "getTextArray(index)");
        return r0;
    }

    public static final CharSequence getTextOrThrow(TypedArray r2, int r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        checkAttribute(r2, r3);
        CharSequence r0 = r2.getText(r3);
        if (r0 == null) goto L6;
        return r0;
    L6:
        throw new IllegalStateException("Attribute value could not be coerced to CharSequence.".toString());
    }

    public static final <R> R use(TypedArray r4, Function1<? super TypedArray, ? extends R> r5) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        Intrinsics.checkNotNullParameter(r5, "block");
        R r1 = r5.invoke(r4);
        r4.recycle();
        return r1;
    }
}
