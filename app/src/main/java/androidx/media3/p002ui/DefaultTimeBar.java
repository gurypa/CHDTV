package androidx.media3.p002ui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.media3.common.C0180C;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import java.util.Collections;
import java.util.Formatter;
import java.util.Iterator;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArraySet;

/* JADX INFO: loaded from: classes.dex */
public class DefaultTimeBar extends View implements TimeBar {
    private static final String ACCESSIBILITY_CLASS_NAME = "android.widget.SeekBar";
    public static final int BAR_GRAVITY_BOTTOM = 1;
    public static final int BAR_GRAVITY_CENTER = 0;
    public static final int DEFAULT_AD_MARKER_COLOR = -1291845888;
    public static final int DEFAULT_AD_MARKER_WIDTH_DP = 4;
    public static final int DEFAULT_BAR_HEIGHT_DP = 4;
    public static final int DEFAULT_BUFFERED_COLOR = -855638017;
    private static final int DEFAULT_INCREMENT_COUNT = 20;
    public static final int DEFAULT_PLAYED_AD_MARKER_COLOR = 872414976;
    public static final int DEFAULT_PLAYED_COLOR = -1;
    public static final int DEFAULT_SCRUBBER_COLOR = -1;
    public static final int DEFAULT_SCRUBBER_DISABLED_SIZE_DP = 0;
    public static final int DEFAULT_SCRUBBER_DRAGGED_SIZE_DP = 16;
    public static final int DEFAULT_SCRUBBER_ENABLED_SIZE_DP = 12;
    public static final int DEFAULT_TOUCH_TARGET_HEIGHT_DP = 26;
    public static final int DEFAULT_UNPLAYED_COLOR = 872415231;
    private static final int FINE_SCRUB_RATIO = 3;
    private static final int FINE_SCRUB_Y_THRESHOLD_DP = -50;
    private static final float HIDDEN_SCRUBBER_SCALE = 0.0f;
    private static final float SHOWN_SCRUBBER_SCALE = 1.0f;
    private static final long STOP_SCRUBBING_TIMEOUT_MS = 1000;
    private int adGroupCount;
    private long[] adGroupTimesMs;
    private final Paint adMarkerPaint;
    private final int adMarkerWidth;
    private final int barGravity;
    private final int barHeight;
    private final Rect bufferedBar;
    private final Paint bufferedPaint;
    private long bufferedPosition;
    private final float density;
    private long duration;
    private final int fineScrubYThreshold;
    private final StringBuilder formatBuilder;
    private final Formatter formatter;
    private int keyCountIncrement;
    private long keyTimeIncrement;
    private int lastCoarseScrubXPosition;
    private Rect lastExclusionRectangle;
    private final CopyOnWriteArraySet<TimeBar.OnScrubListener> listeners;
    private boolean[] playedAdGroups;
    private final Paint playedAdMarkerPaint;
    private final Paint playedPaint;
    private long position;
    private final Rect progressBar;
    private long scrubPosition;
    private final Rect scrubberBar;
    private final int scrubberDisabledSize;
    private final int scrubberDraggedSize;
    private final Drawable scrubberDrawable;
    private final int scrubberEnabledSize;
    private final int scrubberPadding;
    private boolean scrubberPaddingDisabled;
    private final Paint scrubberPaint;
    private float scrubberScale;
    private ValueAnimator scrubberScalingAnimator;
    private boolean scrubbing;
    private final Rect seekBounds;
    private final Runnable stopScrubbingRunnable;
    private final Point touchPosition;
    private final int touchTargetHeight;
    private final Paint unplayedPaint;

    public DefaultTimeBar(Context r2) {
        this(r2, null);
    }

    public DefaultTimeBar(Context r2, AttributeSet r3) {
        this(r2, r3, 0);
    }

    public DefaultTimeBar(Context r1, AttributeSet r2, int r3) {
        this(r1, r2, r3, r2);
    }

    public DefaultTimeBar(Context r7, AttributeSet r8, int r9, AttributeSet r10) {
        this(r7, r8, r9, r10, 0);
    }

    public DefaultTimeBar(Context r21, AttributeSet r22, int r23, AttributeSet r24, int r25) {
        super(r21, r22, r23);
        this.seekBounds = new Rect();
        this.progressBar = new Rect();
        this.bufferedBar = new Rect();
        this.scrubberBar = new Rect();
        this.playedPaint = new Paint();
        this.bufferedPaint = new Paint();
        this.unplayedPaint = new Paint();
        this.adMarkerPaint = new Paint();
        this.playedAdMarkerPaint = new Paint();
        this.scrubberPaint = new Paint();
        this.scrubberPaint.setAntiAlias(true);
        this.listeners = new CopyOnWriteArraySet();
        this.touchPosition = new Point();
        this.density = r21.getResources().getDisplayMetrics().density;
        this.fineScrubYThreshold = dpToPx(this.density, FINE_SCRUB_Y_THRESHOLD_DP);
        int r7 = dpToPx(this.density, 4);
        int r8 = dpToPx(this.density, 26);
        int r6 = dpToPx(this.density, 4);
        int r9 = dpToPx(this.density, 12);
        int r11 = dpToPx(this.density, 0);
        int r12 = dpToPx(this.density, 16);
        if (r24 == null) goto L31;
        TypedArray r13 = r21.getTheme().obtainStyledAttributes(r24, C0467R.styleable.DefaultTimeBar, r23, r25);
        this.scrubberDrawable = r13.getDrawable(C0467R.styleable.DefaultTimeBar_scrubber_drawable);     // Catch: Throwable -> L27
        if (this.scrubberDrawable == null) goto L48;
        setDrawableLayoutDirection(this.scrubberDrawable);     // Catch: Throwable -> L9
        r8 = Math.max(this.scrubberDrawable.getMinimumHeight(), r8);     // Catch: Throwable -> L9
    L9:
        th = th;
    L29:
        r13.recycle();
        throw th;
    L48:
        this.barHeight = r13.getDimensionPixelSize(C0467R.styleable.DefaultTimeBar_bar_height, r7);     // Catch: Throwable -> L25
        this.touchTargetHeight = r13.getDimensionPixelSize(C0467R.styleable.DefaultTimeBar_touch_target_height, r8);     // Catch: Throwable -> L25
        this.barGravity = r13.getInt(C0467R.styleable.DefaultTimeBar_bar_gravity, 0);     // Catch: Throwable -> L25
        this.adMarkerWidth = r13.getDimensionPixelSize(C0467R.styleable.DefaultTimeBar_ad_marker_width, r6);     // Catch: Throwable -> L25
        this.scrubberEnabledSize = r13.getDimensionPixelSize(C0467R.styleable.DefaultTimeBar_scrubber_enabled_size, r9);     // Catch: Throwable -> L25
        this.scrubberDisabledSize = r13.getDimensionPixelSize(C0467R.styleable.DefaultTimeBar_scrubber_disabled_size, r11);     // Catch: Throwable -> L25
        this.scrubberDraggedSize = r13.getDimensionPixelSize(C0467R.styleable.DefaultTimeBar_scrubber_dragged_size, r12);     // Catch: Throwable -> L25
        int r0 = r13.getInt(C0467R.styleable.DefaultTimeBar_played_color, -1);     // Catch: Throwable -> L25
        int r3 = r13.getInt(C0467R.styleable.DefaultTimeBar_scrubber_color, -1);     // Catch: Throwable -> L25
        int r2 = r13.getInt(C0467R.styleable.DefaultTimeBar_buffered_color, DEFAULT_BUFFERED_COLOR);     // Catch: Throwable -> L25
        int r4 = r13.getInt(C0467R.styleable.DefaultTimeBar_unplayed_color, DEFAULT_UNPLAYED_COLOR);     // Catch: Throwable -> L23
        int r5 = r13.getInt(C0467R.styleable.DefaultTimeBar_ad_marker_color, DEFAULT_AD_MARKER_COLOR);     // Catch: Throwable -> L21
        int r10 = r13.getInt(C0467R.styleable.DefaultTimeBar_played_ad_marker_color, DEFAULT_PLAYED_AD_MARKER_COLOR);     // Catch: Throwable -> L19
        this.playedPaint.setColor(r0);     // Catch: Throwable -> L19
        this.scrubberPaint.setColor(r3);     // Catch: Throwable -> L19
        this.bufferedPaint.setColor(r2);     // Catch: Throwable -> L19
        this.unplayedPaint.setColor(r4);     // Catch: Throwable -> L19
        this.adMarkerPaint.setColor(r5);     // Catch: Throwable -> L19
        this.playedAdMarkerPaint.setColor(r10);     // Catch: Throwable -> L19
        r13.recycle();
    L32:
        this.formatBuilder = new StringBuilder();
        this.formatter = new Formatter(this.formatBuilder, Locale.getDefault());
        this.stopScrubbingRunnable = new DefaultTimeBar$$ExternalSyntheticLambda0(this);
        if (this.scrubberDrawable == null) goto L35;
        this.scrubberPadding = (this.scrubberDrawable.getMinimumWidth() + 1) / 2;
    L36:
        this.scrubberScale = 1.0f;
        this.scrubberScalingAnimator = new ValueAnimator();
        this.scrubberScalingAnimator.addUpdateListener(new DefaultTimeBar$$ExternalSyntheticLambda1(this));
        this.duration = C0180C.TIME_UNSET;
        this.keyTimeIncrement = C0180C.TIME_UNSET;
        this.keyCountIncrement = 20;
        setFocusable(true);
        if (getImportantForAccessibility() != 0) goto L52;
        setImportantForAccessibility(1);
        return;
    L52:
        return;
    L35:
        this.scrubberPadding = (Math.max(this.scrubberDisabledSize, Math.max(this.scrubberEnabledSize, this.scrubberDraggedSize)) + 1) / 2;
    L19:
        th = th;
    L21:
        th = th;
    L23:
        th = th;
    L25:
        th = th;
    L27:
        th = th;
        goto L29
    L31:
        this.barHeight = r7;
        this.touchTargetHeight = r8;
        this.barGravity = 0;
        this.adMarkerWidth = r6;
        this.scrubberEnabledSize = r9;
        this.scrubberDisabledSize = r11;
        this.scrubberDraggedSize = r12;
        this.playedPaint.setColor(-1);
        this.scrubberPaint.setColor(-1);
        this.bufferedPaint.setColor(DEFAULT_BUFFERED_COLOR);
        this.unplayedPaint.setColor(DEFAULT_UNPLAYED_COLOR);
        this.adMarkerPaint.setColor(DEFAULT_AD_MARKER_COLOR);
        this.playedAdMarkerPaint.setColor(DEFAULT_PLAYED_AD_MARKER_COLOR);
        this.scrubberDrawable = null;
        goto L32
    }

    private static int dpToPx(float r2, int r3) {
        return (int) ((r3 * r2) + 0.5f);
    }

    private void drawPlayhead(Canvas r10) {
        if (this.duration > 0) goto L5;
        return;
    L5:
        int r0 = Util.constrainValue(this.scrubberBar.right, this.scrubberBar.left, this.progressBar.right);
        int r1 = this.scrubberBar.centerY();
        if (this.scrubberDrawable == null) goto L8;
        int r2 = (int) (this.scrubberDrawable.getIntrinsicWidth() * this.scrubberScale);
        int r3 = (int) (this.scrubberDrawable.getIntrinsicHeight() * this.scrubberScale);
        this.scrubberDrawable.setBounds(r0 - (r2 / 2), r1 - (r3 / 2), (r2 / 2) + r0, (r3 / 2) + r1);
        this.scrubberDrawable.draw(r10);
        return;
    L8:
        if (this.scrubbing == false) goto L10;
    L16:
        int r4 = this.scrubberDraggedSize;
    L17:
        r10.drawCircle(r0, r1, (int) ((r4 * this.scrubberScale) / 2.0f), this.scrubberPaint);
        return;
    L10:
        if (isFocused() == true) goto L16;
        if (isEnabled() == false) goto L15;
        r4 = this.scrubberEnabledSize;
        goto L17
    L15:
        r4 = this.scrubberDisabledSize;
        goto L17
    }

    private void drawTimeBar(Canvas r22) {
        int r1 = this.progressBar.height();
        int r2 = this.progressBar.centerY() - (r1 / 2);
        int r3 = r2 + r1;
        if (this.duration > 0) goto L6;
        r22.drawRect(this.progressBar.left, r2, this.progressBar.right, r3, this.unplayedPaint);
        return;
    L6:
        int r4 = this.bufferedBar.left;
        int r5 = this.bufferedBar.right;
        int r6 = Math.max(Math.max(this.progressBar.left, r5), this.scrubberBar.right);
        if (r6 >= this.progressBar.right) goto L9;
        r22.drawRect(r6, r2, this.progressBar.right, r3, this.unplayedPaint);
    L9:
        int r7 = Math.max(r4, this.scrubberBar.right);
        if (r5 <= r7) goto L13;
        r22.drawRect(r7, r2, r5, r3, this.bufferedPaint);
    L13:
        if (this.scrubberBar.width() <= 0) goto L16;
        r22.drawRect(this.scrubberBar.left, r2, this.scrubberBar.right, r3, this.playedPaint);
    L16:
        if (this.adGroupCount != 0) goto L18;
        return;
    L18:
        long[] r8 = (long[]) Assertions.checkNotNull(this.adGroupTimesMs);
        boolean[] r9 = (boolean[]) Assertions.checkNotNull(this.playedAdGroups);
        int r10 = this.adMarkerWidth / 2;
        int r11 = 0;
    L20:
        if (r11 >= this.adGroupCount) goto L26;
        int r12 = this.progressBar.left + Math.min(this.progressBar.width() - this.adMarkerWidth, Math.max(0, ((int) ((((long) this.progressBar.width()) * Util.constrainValue(r8[r11], 0, this.duration)) / this.duration)) - r10));
        if (r9[r11] == false) goto L24;
        Paint r13 = this.playedAdMarkerPaint;
    L25:
        r22.drawRect(r12, r2, this.adMarkerWidth + r12, r3, r13);
        r11 = r11 + 1;
        goto L20
    L24:
        r13 = this.adMarkerPaint;
        goto L25
    }

    private long getPositionIncrement() {
        if (this.keyTimeIncrement != C0180C.TIME_UNSET) goto L9;
        if (this.duration != C0180C.TIME_UNSET) goto L11;
        return 0;
    L11:
        return this.duration / ((long) this.keyCountIncrement);
    L9:
        return this.keyTimeIncrement;
    }

    private String getProgressText() {
        return Util.getStringForTime(this.formatBuilder, this.formatter, this.position);
    }

    private long getScrubberPosition() {
        if (this.progressBar.width() > 0) goto L5;
        return 0;
    L5:
        if (this.duration != C0180C.TIME_UNSET) goto L8;
        return 0;
    L8:
        return (((long) this.scrubberBar.width()) * this.duration) / ((long) this.progressBar.width());
    }

    private boolean isInSeekBar(float r4, float r5) {
        return this.seekBounds.contains((int) r4, (int) r5);
    }

    private void positionScrubber(float r5) {
        Rect r0 = this.scrubberBar;
        r0.right = Util.constrainValue((int) r5, this.progressBar.left, this.progressBar.right);
    }

    private static int pxToDp(float r1, int r2) {
        return (int) (r2 / r1);
    }

    private Point resolveRelativeTouchPosition(MotionEvent r4) {
        this.touchPosition.set((int) r4.getX(), (int) r4.getY());
        return this.touchPosition;
    }

    private boolean scrubIncrementally(long r11) {
        if (this.duration > 0) goto L6;
        return false;
    L6:
        if (this.scrubbing == false) goto L8;
        long r2 = this.scrubPosition;
    L9:
        long r4 = Util.constrainValue(r2 + r11, 0, this.duration);
        if (r4 != r2) goto L13;
        return false;
    L13:
        if (this.scrubbing == true) goto L15;
        startScrubbing(r4);
    L16:
        update();
        return true;
    L15:
        updateScrubbing(r4);
        goto L16
    L8:
        r2 = this.position;
        goto L9
    }

    private boolean setDrawableLayoutDirection(Drawable r3) {
        if (Util.SDK_INT >= 23) goto L5;
    L7:
        return false;
    L5:
        if (setDrawableLayoutDirection(r3, getLayoutDirection()) == false) goto L7;
        return true;
    }

    private static boolean setDrawableLayoutDirection(Drawable r2, int r3) {
        if (Util.SDK_INT >= 23) goto L5;
    L7:
        return false;
    L5:
        if (r2.setLayoutDirection(r3) == false) goto L7;
        return true;
    }

    private void setSystemGestureExclusionRectsV29(int r3, int r4) {
        if (this.lastExclusionRectangle != null) goto L5;
    L9:
        this.lastExclusionRectangle = new Rect(0, 0, r3, r4);
        setSystemGestureExclusionRects(Collections.singletonList(this.lastExclusionRectangle));
        return;
    L5:
        if (this.lastExclusionRectangle.width() != r3) goto L9;
        if (this.lastExclusionRectangle.height() != r4) goto L9;
    }

    private void startScrubbing(long r4) {
        this.scrubPosition = r4;
        this.scrubbing = true;
        setPressed(true);
        ViewParent r1 = getParent();
        if (r1 == null) goto L5;
        r1.requestDisallowInterceptTouchEvent(true);
    L5:
        Iterator<TimeBar.OnScrubListener> r0 = this.listeners.iterator();
    L7:
        if (r0.hasNext() == false) goto L9;
        r0.next().onScrubStart(this, r4);
        goto L7
    }

    private void stopScrubbing(boolean r6) {
        removeCallbacks(this.stopScrubbingRunnable);
        this.scrubbing = false;
        setPressed(false);
        ViewParent r1 = getParent();
        if (r1 == null) goto L5;
        r1.requestDisallowInterceptTouchEvent(false);
    L5:
        invalidate();
        Iterator<TimeBar.OnScrubListener> r0 = this.listeners.iterator();
    L7:
        if (r0.hasNext() == false) goto L9;
        r0.next().onScrubStop(this, this.scrubPosition, r6);
        goto L7
    }

    private void update() {
        this.bufferedBar.set(this.progressBar);
        this.scrubberBar.set(this.progressBar);
        if (this.scrubbing == false) goto L5;
        long r0 = this.scrubPosition;
    L7:
        if (this.duration <= 0) goto L9;
        int r2 = (int) ((((long) this.progressBar.width()) * this.bufferedPosition) / this.duration);
        Rect r3 = this.bufferedBar;
        r3.right = Math.min(this.progressBar.left + r2, this.progressBar.right);
        int r4 = (int) ((((long) this.progressBar.width()) * r0) / this.duration);
        Rect r5 = this.scrubberBar;
        r5.right = Math.min(this.progressBar.left + r4, this.progressBar.right);
    L10:
        invalidate(this.seekBounds);
        return;
    L9:
        Rect r6 = this.bufferedBar;
        r6.right = this.progressBar.left;
        Rect r7 = this.scrubberBar;
        r7.right = this.progressBar.left;
        goto L10
    L5:
        r0 = this.position;
        goto L7
    }

    private void updateDrawableState() {
        if (this.scrubberDrawable != null) goto L5;
        return;
    L5:
        if (this.scrubberDrawable.isStateful() == true) goto L7;
        return;
    L7:
        if (this.scrubberDrawable.setState(getDrawableState()) == false) goto L12;
        invalidate();
        return;
    }

    private void updateScrubbing(long r3) {
        if (this.scrubPosition != r3) goto L5;
        return;
    L5:
        this.scrubPosition = r3;
        Iterator<TimeBar.OnScrubListener> r0 = this.listeners.iterator();
    L7:
        if (r0.hasNext() == false) goto L9;
        r0.next().onScrubMove(this, r3);
        goto L7
    }

    @Override // androidx.media3.p002ui.TimeBar
    public void addListener(TimeBar.OnScrubListener r2) {
        Assertions.checkNotNull(r2);
        this.listeners.add(r2);
    }

    @Override // android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        updateDrawableState();
    }

    @Override // androidx.media3.p002ui.TimeBar
    public long getPreferredUpdateDelay() {
        int r0 = pxToDp(this.density, this.progressBar.width());
        if (r0 != 0) goto L5;
    L10:
        return Long.MAX_VALUE;
    L5:
        if (this.duration == 0) goto L10;
        if (this.duration == C0180C.TIME_UNSET) goto L10;
        return this.duration / ((long) r0);
    }

    public void hideScrubber(long r5) {
        if (this.scrubberScalingAnimator.isStarted() == false) goto L5;
        this.scrubberScalingAnimator.cancel();
    L5:
        this.scrubberScalingAnimator.setFloatValues(new float[]{this.scrubberScale, 0.0f});
        this.scrubberScalingAnimator.setDuration(r5);
        this.scrubberScalingAnimator.start();
    }

    public void hideScrubber(boolean r2) {
        if (this.scrubberScalingAnimator.isStarted() == false) goto L5;
        this.scrubberScalingAnimator.cancel();
    L5:
        this.scrubberPaddingDisabled = r2;
        this.scrubberScale = 0.0f;
        invalidate(this.seekBounds);
    }

    @Override // android.view.View
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        if (this.scrubberDrawable == null) goto L6;
        this.scrubberDrawable.jumpToCurrentState();
        return;
    }

    /* JADX INFO: renamed from: lambda$new$0$androidx-media3-ui-DefaultTimeBar, reason: not valid java name */
    /* synthetic */ void m484lambda$new$0$androidxmedia3uiDefaultTimeBar() {
        stopScrubbing(false);
    }

    /* JADX INFO: renamed from: lambda$new$1$androidx-media3-ui-DefaultTimeBar, reason: not valid java name */
    /* synthetic */ void m485lambda$new$1$androidxmedia3uiDefaultTimeBar(ValueAnimator r2) {
        this.scrubberScale = ((Float) r2.getAnimatedValue()).floatValue();
        invalidate(this.seekBounds);
    }

    @Override // android.view.View
    public void onDraw(Canvas r1) {
        r1.save();
        drawTimeBar(r1);
        drawPlayhead(r1);
        r1.restore();
    }

    @Override // android.view.View
    protected void onFocusChanged(boolean r2, int r3, Rect r4) {
        super.onFocusChanged(r2, r3, r4);
        if (this.scrubbing == false) goto L7;
        if (r2 == true) goto L8;
        stopScrubbing(false);
        return;
    L8:
        return;
    }

    @Override // android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent r3) {
        super.onInitializeAccessibilityEvent(r3);
        if (r3.getEventType() != 4) goto L5;
        r3.getText().add(getProgressText());
    L5:
        r3.setClassName(ACCESSIBILITY_CLASS_NAME);
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo r5) {
        super.onInitializeAccessibilityNodeInfo(r5);
        r5.setClassName(ACCESSIBILITY_CLASS_NAME);
        r5.setContentDescription(getProgressText());
        if (this.duration > 0) goto L5;
        return;
    L5:
        r5.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_FORWARD);
        r5.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_BACKWARD);
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyDown(int r7, KeyEvent r8) {
        if (isEnabled() == false) goto L17;
        long r0 = getPositionIncrement();
        switch(r7) {
            case 21: goto L11;
            case 22: goto L13;
            case 23: goto L8;
            case 66: goto L8;
            default: goto L17;
        };
    L11:
        r0 = -r0;
        goto L13
    L8:
        if (this.scrubbing == false) goto L17;
        stopScrubbing(false);
        return true;
    L13:
        if (scrubIncrementally(r0) == false) goto L17;
        removeCallbacks(this.stopScrubbingRunnable);
        postDelayed(this.stopScrubbingRunnable, 1000);
        return true;
    L17:
        return super.onKeyDown(r7, r8);
    }

    @Override // android.view.View
    protected void onLayout(boolean r12, int r13, int r14, int r15, int r16) {
        int r0 = r15 - r13;
        int r1 = r16 - r14;
        int r2 = getPaddingLeft();
        int r3 = r0 - getPaddingRight();
        if (this.scrubberPaddingDisabled == false) goto L5;
        int r4 = 0;
    L7:
        if (this.barGravity != 1) goto L9;
        int r6 = (r1 - getPaddingBottom()) - this.touchTargetHeight;
        int r7 = ((r1 - getPaddingBottom()) - this.barHeight) - Math.max(r4 - (this.barHeight / 2), 0);
    L10:
        this.seekBounds.set(r2, r6, r3, this.touchTargetHeight + r6);
        this.progressBar.set(this.seekBounds.left + r4, r7, this.seekBounds.right - r4, this.barHeight + r7);
        if (Util.SDK_INT < 29) goto L13;
        setSystemGestureExclusionRectsV29(r0, r1);
    L13:
        update();
        return;
    L9:
        r6 = (r1 - this.touchTargetHeight) / 2;
        r7 = (r1 - this.barHeight) / 2;
        goto L10
    L5:
        r4 = this.scrubberPadding;
        goto L7
    }

    @Override // android.view.View
    protected void onMeasure(int r5, int r6) {
        int r0 = View.MeasureSpec.getMode(r6);
        int r1 = View.MeasureSpec.getSize(r6);
        if (r0 != 0) goto L6;
        int r2 = this.touchTargetHeight;
    L9:
        setMeasuredDimension(View.MeasureSpec.getSize(r5), r2);
        updateDrawableState();
        return;
    L6:
        if (r0 != 1073741824) goto L8;
        r2 = r1;
        goto L9
    L8:
        r2 = Math.min(this.touchTargetHeight, r1);
        goto L9
    }

    @Override // android.view.View
    public void onRtlPropertiesChanged(int r2) {
        if (this.scrubberDrawable != null) goto L5;
        return;
    L5:
        if (setDrawableLayoutDirection(this.scrubberDrawable, r2) == false) goto L9;
        invalidate();
        return;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent r9) {
        boolean r1 = false;
        if (isEnabled() == true) goto L5;
    L30:
        return false;
    L5:
        if (this.duration <= 0) goto L30;
        Point r0 = resolveRelativeTouchPosition(r9);
        int r2 = r0.x;
        int r3 = r0.y;
        switch(r9.getAction()) {
            case 0: goto L26;
            case 1: goto L19;
            case 2: goto L11;
            case 3: goto L19;
            default: goto L29;
        };
    L29:
        return false;
    L11:
        if (this.scrubbing == false) goto L29;
        if (r3 >= this.fineScrubYThreshold) goto L15;
        positionScrubber(this.lastCoarseScrubXPosition + ((r2 - this.lastCoarseScrubXPosition) / 3));
    L16:
        updateScrubbing(getScrubberPosition());
        update();
        invalidate();
        return true;
    L15:
        this.lastCoarseScrubXPosition = r2;
        positionScrubber(r2);
        goto L16
    L19:
        if (this.scrubbing == false) goto L29;
        if (r9.getAction() != 3) goto L23;
        r1 = true;
    L23:
        stopScrubbing(r1);
        return true;
    L26:
        if (isInSeekBar(r2, r3) == false) goto L29;
        positionScrubber(r2);
        startScrubbing(getScrubberPosition());
        update();
        invalidate();
        return true;
    }

    @Override // android.view.View
    public boolean performAccessibilityAction(int r7, Bundle r8) {
        if (super.performAccessibilityAction(r7, r8) == false) goto L6;
        return true;
    L6:
        if (this.duration > 0) goto L9;
        return false;
    L9:
        if (r7 != 8192) goto L14;
        if (scrubIncrementally(-getPositionIncrement()) == false) goto L18;
        stopScrubbing(false);
    L18:
        sendAccessibilityEvent(4);
        return true;
    L14:
        if (r7 == 4096) goto L16;
        return false;
    L16:
        if (scrubIncrementally(getPositionIncrement()) == false) goto L18;
        stopScrubbing(false);
        goto L18
    }

    @Override // androidx.media3.p002ui.TimeBar
    public void removeListener(TimeBar.OnScrubListener r2) {
        this.listeners.remove(r2);
    }

    @Override // androidx.media3.p002ui.TimeBar
    public void setAdGroupTimesMs(long[] r2, boolean[] r3, int r4) {
        if (r4 == 0) goto L7;
        if (r2 == null) goto L6;
        if (r3 != null) goto L7;
    L6:
        boolean r0 = false;
    L8:
        Assertions.checkArgument(r0);
        this.adGroupCount = r4;
        this.adGroupTimesMs = r2;
        this.playedAdGroups = r3;
        update();
        return;
    L7:
        r0 = true;
        goto L8
    }

    public void setAdMarkerColor(int r2) {
        this.adMarkerPaint.setColor(r2);
        invalidate(this.seekBounds);
    }

    public void setBufferedColor(int r2) {
        this.bufferedPaint.setColor(r2);
        invalidate(this.seekBounds);
    }

    @Override // androidx.media3.p002ui.TimeBar
    public void setBufferedPosition(long r3) {
        if (this.bufferedPosition != r3) goto L5;
        return;
    L5:
        this.bufferedPosition = r3;
        update();
    }

    @Override // androidx.media3.p002ui.TimeBar
    public void setDuration(long r3) {
        if (this.duration != r3) goto L5;
        return;
    L5:
        this.duration = r3;
        if (this.scrubbing == true) goto L8;
    L10:
        update();
        return;
    L8:
        if (r3 != C0180C.TIME_UNSET) goto L10;
        stopScrubbing(true);
        goto L10
    }

    @Override // android.view.View, androidx.media3.p002ui.TimeBar
    public void setEnabled(boolean r2) {
        super.setEnabled(r2);
        if (this.scrubbing == false) goto L7;
        if (r2 == true) goto L8;
        stopScrubbing(true);
        return;
    L8:
        return;
    }

    @Override // androidx.media3.p002ui.TimeBar
    public void setKeyCountIncrement(int r3) {
        if (r3 <= 0) goto L4;
        boolean r0 = true;
    L5:
        Assertions.checkArgument(r0);
        this.keyCountIncrement = r3;
        this.keyTimeIncrement = C0180C.TIME_UNSET;
        return;
    L4:
        r0 = false;
        goto L5
    }

    @Override // androidx.media3.p002ui.TimeBar
    public void setKeyTimeIncrement(long r3) {
        if (r3 <= 0) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkArgument(r0);
        this.keyCountIncrement = -1;
        this.keyTimeIncrement = r3;
        return;
    L5:
        r0 = false;
        goto L6
    }

    public void setPlayedAdMarkerColor(int r2) {
        this.playedAdMarkerPaint.setColor(r2);
        invalidate(this.seekBounds);
    }

    public void setPlayedColor(int r2) {
        this.playedPaint.setColor(r2);
        invalidate(this.seekBounds);
    }

    @Override // androidx.media3.p002ui.TimeBar
    public void setPosition(long r3) {
        if (this.position != r3) goto L5;
        return;
    L5:
        this.position = r3;
        setContentDescription(getProgressText());
        update();
    }

    public void setScrubberColor(int r2) {
        this.scrubberPaint.setColor(r2);
        invalidate(this.seekBounds);
    }

    public void setUnplayedColor(int r2) {
        this.unplayedPaint.setColor(r2);
        invalidate(this.seekBounds);
    }

    public void showScrubber() {
        if (this.scrubberScalingAnimator.isStarted() == false) goto L5;
        this.scrubberScalingAnimator.cancel();
    L5:
        this.scrubberPaddingDisabled = false;
        this.scrubberScale = 1.0f;
        invalidate(this.seekBounds);
    }

    public void showScrubber(long r5) {
        if (this.scrubberScalingAnimator.isStarted() == false) goto L5;
        this.scrubberScalingAnimator.cancel();
    L5:
        this.scrubberPaddingDisabled = false;
        this.scrubberScalingAnimator.setFloatValues(new float[]{this.scrubberScale, 1.0f});
        this.scrubberScalingAnimator.setDuration(r5);
        this.scrubberScalingAnimator.start();
    }
}
