package androidx.core.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.exifinterface.media.ExifInterface;
import androidx.media3.extractor.text.ttml.TtmlNode;
import com.arthenica.ffmpegkit.MediaInformation;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;

/* JADX INFO: compiled from: View.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000j\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010\b\n\u0002\b\r\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\u001a5\u0010 \u001a\u00020!*\u00020\u00022#\b\u0004\u0010\"\u001a\u001d\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b$\u0012\b\b%\u0012\u0004\b\b(&\u0012\u0004\u0012\u00020!0#H\u0086\bø\u0001\u0000\u001a5\u0010'\u001a\u00020!*\u00020\u00022#\b\u0004\u0010\"\u001a\u001d\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b$\u0012\b\b%\u0012\u0004\b\b(&\u0012\u0004\u0012\u00020!0#H\u0086\bø\u0001\u0000\u001a5\u0010(\u001a\u00020!*\u00020\u00022#\b\u0004\u0010\"\u001a\u001d\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b$\u0012\b\b%\u0012\u0004\b\b(&\u0012\u0004\u0012\u00020!0#H\u0086\bø\u0001\u0000\u001a5\u0010)\u001a\u00020!*\u00020\u00022#\b\u0004\u0010\"\u001a\u001d\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b$\u0012\b\b%\u0012\u0004\b\b(&\u0012\u0004\u0012\u00020!0#H\u0086\bø\u0001\u0000\u001a5\u0010*\u001a\u00020+*\u00020\u00022#\b\u0004\u0010\"\u001a\u001d\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b$\u0012\b\b%\u0012\u0004\b\b(&\u0012\u0004\u0012\u00020!0#H\u0086\bø\u0001\u0000\u001a\u0014\u0010,\u001a\u00020-*\u00020\u00022\b\b\u0002\u0010.\u001a\u00020/\u001a(\u00100\u001a\u000201*\u00020\u00022\u0006\u00102\u001a\u0002032\u000e\b\u0004\u0010\"\u001a\b\u0012\u0004\u0012\u00020!04H\u0086\bø\u0001\u0000\u001a\"\u00105\u001a\u000201*\u00020\u00022\u0006\u00102\u001a\u0002032\f\u0010\"\u001a\b\u0012\u0004\u0012\u00020!04H\u0007\u001a\u0017\u00106\u001a\u00020!*\u00020\u00022\b\b\u0001\u00107\u001a\u00020\u0013H\u0086\b\u001a:\u00108\u001a\u00020!\"\n\b\u0000\u00109\u0018\u0001*\u00020:*\u00020\u00022\u0017\u0010;\u001a\u0013\u0012\u0004\u0012\u0002H9\u0012\u0004\u0012\u00020!0#¢\u0006\u0002\b<H\u0087\bø\u0001\u0000¢\u0006\u0002\b=\u001a)\u00108\u001a\u00020!*\u00020\u00022\u0017\u0010;\u001a\u0013\u0012\u0004\u0012\u00020:\u0012\u0004\u0012\u00020!0#¢\u0006\u0002\b<H\u0086\bø\u0001\u0000\u001a5\u0010>\u001a\u00020!*\u00020\u00022\b\b\u0003\u0010?\u001a\u00020\u00132\b\b\u0003\u0010@\u001a\u00020\u00132\b\b\u0003\u0010A\u001a\u00020\u00132\b\b\u0003\u0010B\u001a\u00020\u0013H\u0086\b\u001a5\u0010C\u001a\u00020!*\u00020\u00022\b\b\u0003\u0010D\u001a\u00020\u00132\b\b\u0003\u0010@\u001a\u00020\u00132\b\b\u0003\u0010E\u001a\u00020\u00132\b\b\u0003\u0010B\u001a\u00020\u0013H\u0087\b\"\u001b\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\u0003\u0010\u0004\"\u001b\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00060\u0001*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\u0007\u0010\u0004\"*\u0010\n\u001a\u00020\t*\u00020\u00022\u0006\u0010\b\u001a\u00020\t8Æ\u0002@Æ\u0002X\u0086\u000e¢\u0006\f\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\r\"*\u0010\u000e\u001a\u00020\t*\u00020\u00022\u0006\u0010\b\u001a\u00020\t8Æ\u0002@Æ\u0002X\u0086\u000e¢\u0006\f\u001a\u0004\b\u000e\u0010\u000b\"\u0004\b\u000f\u0010\r\"*\u0010\u0010\u001a\u00020\t*\u00020\u00022\u0006\u0010\b\u001a\u00020\t8Æ\u0002@Æ\u0002X\u0086\u000e¢\u0006\f\u001a\u0004\b\u0010\u0010\u000b\"\u0004\b\u0011\u0010\r\"\u0016\u0010\u0012\u001a\u00020\u0013*\u00020\u00028Æ\u0002¢\u0006\u0006\u001a\u0004\b\u0014\u0010\u0015\"\u0016\u0010\u0016\u001a\u00020\u0013*\u00020\u00028Æ\u0002¢\u0006\u0006\u001a\u0004\b\u0017\u0010\u0015\"\u0016\u0010\u0018\u001a\u00020\u0013*\u00020\u00028Æ\u0002¢\u0006\u0006\u001a\u0004\b\u0019\u0010\u0015\"\u0016\u0010\u001a\u001a\u00020\u0013*\u00020\u00028Æ\u0002¢\u0006\u0006\u001a\u0004\b\u001b\u0010\u0015\"\u0016\u0010\u001c\u001a\u00020\u0013*\u00020\u00028Æ\u0002¢\u0006\u0006\u001a\u0004\b\u001d\u0010\u0015\"\u0016\u0010\u001e\u001a\u00020\u0013*\u00020\u00028Æ\u0002¢\u0006\u0006\u001a\u0004\b\u001f\u0010\u0015\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006F"}, m421d2 = {"allViews", "Lkotlin/sequences/Sequence;", "Landroid/view/View;", "getAllViews", "(Landroid/view/View;)Lkotlin/sequences/Sequence;", "ancestors", "Landroid/view/ViewParent;", "getAncestors", "value", "", "isGone", "(Landroid/view/View;)Z", "setGone", "(Landroid/view/View;Z)V", "isInvisible", "setInvisible", "isVisible", "setVisible", "marginBottom", "", "getMarginBottom", "(Landroid/view/View;)I", "marginEnd", "getMarginEnd", "marginLeft", "getMarginLeft", "marginRight", "getMarginRight", "marginStart", "getMarginStart", "marginTop", "getMarginTop", "doOnAttach", "", "action", "Lkotlin/Function1;", "Lkotlin/ParameterName;", "name", "view", "doOnDetach", "doOnLayout", "doOnNextLayout", "doOnPreDraw", "Landroidx/core/view/OneShotPreDrawListener;", "drawToBitmap", "Landroid/graphics/Bitmap;", "config", "Landroid/graphics/Bitmap$Config;", "postDelayed", "Ljava/lang/Runnable;", "delayInMillis", "", "Lkotlin/Function0;", "postOnAnimationDelayed", "setPadding", MediaInformation.KEY_SIZE, "updateLayoutParams", ExifInterface.GPS_DIRECTION_TRUE, "Landroid/view/ViewGroup$LayoutParams;", "block", "Lkotlin/ExtensionFunctionType;", "updateLayoutParamsTyped", "updatePadding", TtmlNode.LEFT, "top", TtmlNode.RIGHT, "bottom", "updatePaddingRelative", "start", "end", "core-ktx_release"}, m422k = 2, m423mv = {1, 7, 1}, m425xi = 48)
public final class ViewKt {
    public static /* synthetic */ void $r8$lambda$nSDSBfPSk_T_inbpxeVYecbjWLE(Function0 r0) {
        m456postOnAnimationDelayed$lambda1(r0);
    }

    public static final void doOnAttach(final View r2, final Function1<? super View, Unit> r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "action");
        if (ViewCompat.isAttachedToWindow(r2) == false) goto L5;
        r3.invoke(r2);
        return;
    L5:
        r2.addOnAttachStateChangeListener(new ViewOnAttachStateChangeListenerC01201(r2, r3));
    }

    public static final void doOnDetach(final View r2, final Function1<? super View, Unit> r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "action");
        if (ViewCompat.isAttachedToWindow(r2) == true) goto L5;
        r3.invoke(r2);
        return;
    L5:
        r2.addOnAttachStateChangeListener(new ViewOnAttachStateChangeListenerC01211(r2, r3));
    }

    public static final void doOnLayout(View r4, final Function1<? super View, Unit> r5) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        Intrinsics.checkNotNullParameter(r5, "action");
        if (ViewCompat.isLaidOut(r4) == true) goto L5;
    L7:
        r4.addOnLayoutChangeListener(new ViewKt$doOnLayout$$inlined$doOnNextLayout$1(r5));
        return;
    L5:
        if (r4.isLayoutRequested() == true) goto L7;
        r5.invoke(r4);
    }

    public static final void doOnNextLayout(View r2, final Function1<? super View, Unit> r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "action");
        r2.addOnLayoutChangeListener(new ViewOnLayoutChangeListenerC01221(r3));
    }

    public static final OneShotPreDrawListener doOnPreDraw(final View r3, final Function1<? super View, Unit> r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r4, "action");
        OneShotPreDrawListener r1 = OneShotPreDrawListener.add(r3, new RunnableC01231(r4, r3));
        Intrinsics.checkNotNullExpressionValue(r1, "View.doOnPreDraw(\n    cr…dd(this) { action(this) }");
        return r1;
    }

    public static final Bitmap drawToBitmap(View r7, Bitmap.Config r8) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        Intrinsics.checkNotNullParameter(r8, "config");
        if (ViewCompat.isLaidOut(r7) == false) goto L7;
        Bitmap r0 = Bitmap.createBitmap(r7.getWidth(), r7.getHeight(), r8);
        Intrinsics.checkNotNullExpressionValue(r0, "createBitmap(width, height, config)");
        Canvas r2 = new Canvas(r0);
        r2.translate(-r7.getScrollX(), -r7.getScrollY());
        r7.draw(r2);
        return r0;
    L7:
        throw new IllegalStateException("View needs to be laid out before calling drawToBitmap()");
    }

    public static /* synthetic */ Bitmap drawToBitmap$default(View r0, Bitmap.Config r1, int r2, Object r3) {
        if ((r2 & 1) == 0) goto L6;
        r1 = Bitmap.Config.ARGB_8888;
    L6:
        return drawToBitmap(r0, r1);
    }

    public static final Sequence<View> getAllViews(View r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return SequencesKt.sequence(new ViewKt$allViews$1(r2, null));
    }

    public static final Sequence<ViewParent> getAncestors(View r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return SequencesKt.generateSequence(r2.getParent(), ViewKt$ancestors$1.INSTANCE);
    }

    public static final int getMarginBottom(View r3) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        ViewGroup.LayoutParams r1 = r3.getLayoutParams();
        if ((r1 instanceof ViewGroup.MarginLayoutParams) == false) goto L5;
        ViewGroup.MarginLayoutParams r2 = (ViewGroup.MarginLayoutParams) r1;
    L6:
        if (r2 != null) goto L10;
        return 0;
    L10:
        return r2.bottomMargin;
    L5:
        r2 = null;
        goto L6
    }

    public static final int getMarginEnd(View r3) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        ViewGroup.LayoutParams r1 = r3.getLayoutParams();
        if ((r1 instanceof ViewGroup.MarginLayoutParams) == true) goto L7;
        return 0;
    L7:
        return MarginLayoutParamsCompat.getMarginEnd((ViewGroup.MarginLayoutParams) r1);
    }

    public static final int getMarginLeft(View r3) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        ViewGroup.LayoutParams r1 = r3.getLayoutParams();
        if ((r1 instanceof ViewGroup.MarginLayoutParams) == false) goto L5;
        ViewGroup.MarginLayoutParams r2 = (ViewGroup.MarginLayoutParams) r1;
    L6:
        if (r2 != null) goto L10;
        return 0;
    L10:
        return r2.leftMargin;
    L5:
        r2 = null;
        goto L6
    }

    public static final int getMarginRight(View r3) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        ViewGroup.LayoutParams r1 = r3.getLayoutParams();
        if ((r1 instanceof ViewGroup.MarginLayoutParams) == false) goto L5;
        ViewGroup.MarginLayoutParams r2 = (ViewGroup.MarginLayoutParams) r1;
    L6:
        if (r2 != null) goto L10;
        return 0;
    L10:
        return r2.rightMargin;
    L5:
        r2 = null;
        goto L6
    }

    public static final int getMarginStart(View r3) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        ViewGroup.LayoutParams r1 = r3.getLayoutParams();
        if ((r1 instanceof ViewGroup.MarginLayoutParams) == true) goto L7;
        return 0;
    L7:
        return MarginLayoutParamsCompat.getMarginStart((ViewGroup.MarginLayoutParams) r1);
    }

    public static final int getMarginTop(View r3) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        ViewGroup.LayoutParams r1 = r3.getLayoutParams();
        if ((r1 instanceof ViewGroup.MarginLayoutParams) == false) goto L5;
        ViewGroup.MarginLayoutParams r2 = (ViewGroup.MarginLayoutParams) r1;
    L6:
        if (r2 != null) goto L10;
        return 0;
    L10:
        return r2.topMargin;
    L5:
        r2 = null;
        goto L6
    }

    public static final boolean isGone(View r3) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        if (r3.getVisibility() != 8) goto L5;
        return true;
    L5:
        return false;
    }

    public static final boolean isInvisible(View r3) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        if (r3.getVisibility() != 4) goto L5;
        return true;
    L5:
        return false;
    }

    public static final boolean isVisible(View r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        if (r2.getVisibility() != 0) goto L5;
        return true;
    L5:
        return false;
    }

    public static final Runnable postDelayed(View r2, long r3, final Function0<Unit> r5) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r5, "action");
        Runnable r1 = new ViewKt$postDelayed$runnable$1(r5);
        r2.postDelayed(r1, r3);
        return r1;
    }

    public static final Runnable postOnAnimationDelayed(View r1, long r2, final Function0<Unit> r4) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r4, "action");
        Runnable r0 = new ViewKt$$ExternalSyntheticLambda0(r4);
        Api16Impl.postOnAnimationDelayed(r1, r0, r2);
        return r0;
    }

    /* JADX INFO: renamed from: postOnAnimationDelayed$lambda-1, reason: not valid java name */
    private static final void m456postOnAnimationDelayed$lambda1(Function0 r1) {
        Intrinsics.checkNotNullParameter(r1, "$action");
        r1.invoke();
    }

    public static final void setGone(View r2, boolean r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        if (r3 == false) goto L5;
        int r1 = 8;
    L6:
        r2.setVisibility(r1);
        return;
    L5:
        r1 = 0;
        goto L6
    }

    public static final void setInvisible(View r2, boolean r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        if (r3 == false) goto L5;
        int r1 = 4;
    L6:
        r2.setVisibility(r1);
        return;
    L5:
        r1 = 0;
        goto L6
    }

    public static final void setPadding(View r1, int r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        r1.setPadding(r2, r2, r2, r2);
    }

    public static final void setVisible(View r2, boolean r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        if (r3 == false) goto L5;
        int r1 = 0;
    L6:
        r2.setVisibility(r1);
        return;
    L5:
        r1 = 8;
        goto L6
    }

    public static final void updateLayoutParams(View r5, Function1<? super ViewGroup.LayoutParams, Unit> r6) {
        Intrinsics.checkNotNullParameter(r5, "<this>");
        Intrinsics.checkNotNullParameter(r6, "block");
        ViewGroup.LayoutParams r3 = r5.getLayoutParams();
        if (r3 == null) goto L7;
        r6.invoke(r3);
        r5.setLayoutParams(r3);
        return;
    L7:
        throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroup.LayoutParams");
    }

    public static final /* synthetic */ <T extends ViewGroup.LayoutParams> void updateLayoutParamsTyped(View r4, Function1<? super T, Unit> r5) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        Intrinsics.checkNotNullParameter(r5, "block");
        ViewGroup.LayoutParams r1 = r4.getLayoutParams();
        Intrinsics.reifiedOperationMarker(1, ExifInterface.GPS_DIRECTION_TRUE);
        ViewGroup.LayoutParams r2 = r1;
        r5.invoke(r2);
        r4.setLayoutParams(r2);
    }

    public static final void updatePadding(View r1, int r2, int r3, int r4, int r5) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        r1.setPadding(r2, r3, r4, r5);
    }

    public static /* synthetic */ void updatePadding$default(View r0, int r1, int r2, int r3, int r4, int r5, Object r6) {
        if ((r5 & 1) == 0) goto L6;
        r1 = r0.getPaddingLeft();
    L6:
        if ((r5 & 2) == 0) goto L9;
        r2 = r0.getPaddingTop();
    L9:
        if ((r5 & 4) == 0) goto L12;
        r3 = r0.getPaddingRight();
    L12:
        if ((r5 & 8) == 0) goto L14;
        r4 = r0.getPaddingBottom();
    L14:
        Intrinsics.checkNotNullParameter(r0, "<this>");
        r0.setPadding(r1, r2, r3, r4);
    }

    public static final void updatePaddingRelative(View r1, int r2, int r3, int r4, int r5) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        r1.setPaddingRelative(r2, r3, r4, r5);
    }

    public static /* synthetic */ void updatePaddingRelative$default(View r0, int r1, int r2, int r3, int r4, int r5, Object r6) {
        if ((r5 & 1) == 0) goto L6;
        r1 = r0.getPaddingStart();
    L6:
        if ((r5 & 2) == 0) goto L9;
        r2 = r0.getPaddingTop();
    L9:
        if ((r5 & 4) == 0) goto L12;
        r3 = r0.getPaddingEnd();
    L12:
        if ((r5 & 8) == 0) goto L14;
        r4 = r0.getPaddingBottom();
    L14:
        Intrinsics.checkNotNullParameter(r0, "<this>");
        r0.setPaddingRelative(r1, r2, r3, r4);
    }
}
