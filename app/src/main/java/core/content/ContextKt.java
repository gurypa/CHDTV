package androidx.core.content;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.exifinterface.media.ExifInterface;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Context.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000:\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0015\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a \u0010\u0000\u001a\u0004\u0018\u0001H\u0001\"\n\b\u0000\u0010\u0001\u0018\u0001*\u00020\u0002*\u00020\u0003H\u0086\b¢\u0006\u0002\u0010\u0004\u001aQ\u0010\u0005\u001a\u00020\u0006*\u00020\u00032\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b2\u0006\u0010\t\u001a\u00020\n2\b\b\u0003\u0010\u000b\u001a\u00020\f2\b\b\u0003\u0010\r\u001a\u00020\f2\u0017\u0010\u000e\u001a\u0013\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u00060\u000f¢\u0006\u0002\b\u0011H\u0086\bø\u0001\u0000\u001a;\u0010\u0005\u001a\u00020\u0006*\u00020\u00032\b\b\u0001\u0010\u0012\u001a\u00020\f2\u0006\u0010\t\u001a\u00020\n2\u0017\u0010\u000e\u001a\u0013\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u00060\u000f¢\u0006\u0002\b\u0011H\u0086\bø\u0001\u0000\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\u0013"}, m421d2 = {"getSystemService", ExifInterface.GPS_DIRECTION_TRUE, "", "Landroid/content/Context;", "(Landroid/content/Context;)Ljava/lang/Object;", "withStyledAttributes", "", "set", "Landroid/util/AttributeSet;", "attrs", "", "defStyleAttr", "", "defStyleRes", "block", "Lkotlin/Function1;", "Landroid/content/res/TypedArray;", "Lkotlin/ExtensionFunctionType;", "resourceId", "core-ktx_release"}, m422k = 2, m423mv = {1, 7, 1}, m425xi = 48)
public final class ContextKt {
    public static final /* synthetic */ <T> T getSystemService(Context r3) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.reifiedOperationMarker(4, ExifInterface.GPS_DIRECTION_TRUE);
        return (T) ContextCompat.getSystemService(r3, Object.class);
    }

    public static final void withStyledAttributes(Context r3, int r4, int[] r5, Function1<? super TypedArray, Unit> r6) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r5, "attrs");
        Intrinsics.checkNotNullParameter(r6, "block");
        TypedArray r1 = r3.obtainStyledAttributes(r4, r5);
        Intrinsics.checkNotNullExpressionValue(r1, "obtainStyledAttributes(resourceId, attrs)");
        r6.invoke(r1);
        r1.recycle();
    }

    public static final void withStyledAttributes(Context r3, AttributeSet r4, int[] r5, int r6, int r7, Function1<? super TypedArray, Unit> r8) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r5, "attrs");
        Intrinsics.checkNotNullParameter(r8, "block");
        TypedArray r1 = r3.obtainStyledAttributes(r4, r5, r6, r7);
        Intrinsics.checkNotNullExpressionValue(r1, "obtainStyledAttributes(s…efStyleAttr, defStyleRes)");
        r8.invoke(r1);
        r1.recycle();
    }

    public static /* synthetic */ void withStyledAttributes$default(Context r1, AttributeSet r2, int[] r3, int r4, int r5, Function1 r6, int r7, Object r8) {
        if ((r7 & 1) == 0) goto L6;
        r2 = null;
    L6:
        if ((r7 & 4) == 0) goto L9;
        r4 = 0;
    L9:
        if ((r7 & 8) == 0) goto L11;
        r5 = 0;
    L11:
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r3, "attrs");
        Intrinsics.checkNotNullParameter(r6, "block");
        TypedArray r9 = r1.obtainStyledAttributes(r2, r3, r4, r5);
        Intrinsics.checkNotNullExpressionValue(r9, "obtainStyledAttributes(s…efStyleAttr, defStyleRes)");
        r6.invoke(r9);
        r9.recycle();
    }
}
