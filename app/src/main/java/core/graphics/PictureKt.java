package androidx.core.graphics;

import android.graphics.Canvas;
import android.graphics.Picture;
import com.arthenica.ffmpegkit.StreamInformation;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Picture.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\"\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\u001a9\u0010\u0000\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0017\u0010\u0005\u001a\u0013\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\b0\u0006¢\u0006\u0002\b\tH\u0086\bø\u0001\u0000\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\n"}, m421d2 = {"record", "Landroid/graphics/Picture;", StreamInformation.KEY_WIDTH, "", StreamInformation.KEY_HEIGHT, "block", "Lkotlin/Function1;", "Landroid/graphics/Canvas;", "", "Lkotlin/ExtensionFunctionType;", "core-ktx_release"}, m422k = 2, m423mv = {1, 7, 1}, m425xi = 48)
public final class PictureKt {
    public static final Picture record(Picture r3, int r4, int r5, Function1<? super Canvas, Unit> r6) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r6, "block");
        Canvas r1 = r3.beginRecording(r4, r5);
        Intrinsics.checkNotNullExpressionValue(r1, "beginRecording(width, height)");
        r6.invoke(r1);     // Catch: Throwable -> L6
        r3.endRecording();
        return r3;
    L6:
        th = move-exception;
        r3.endRecording();
        throw th;
    }
}
