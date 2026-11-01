package androidx.core.content.res;

import android.content.res.TypedArray;
import android.graphics.Typeface;
import com.arthenica.ffmpegkit.StreamInformation;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: TypedArray.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\bÃ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u001a\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010\u0007\u001a\u00020\bH\u0007¨\u0006\t"}, m421d2 = {"Landroidx/core/content/res/TypedArrayApi26ImplKt;", "", "()V", "getFont", "Landroid/graphics/Typeface;", "typedArray", "Landroid/content/res/TypedArray;", StreamInformation.KEY_INDEX, "", "core-ktx_release"}, m422k = 1, m423mv = {1, 7, 1}, m425xi = 48)
final class TypedArrayApi26ImplKt {
    public static final TypedArrayApi26ImplKt INSTANCE = null;

    static {
        INSTANCE = new TypedArrayApi26ImplKt();
    }

    private TypedArrayApi26ImplKt() {
    }

    @JvmStatic
    public static final Typeface getFont(TypedArray r1, int r2) {
        Intrinsics.checkNotNullParameter(r1, "typedArray");
        Typeface r0 = r1.getFont(r2);
        Intrinsics.checkNotNull(r0);
        return r0;
    }
}
