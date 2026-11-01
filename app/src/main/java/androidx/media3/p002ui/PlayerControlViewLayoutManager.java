package androidx.media3.p002ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
final class PlayerControlViewLayoutManager {
    private static final long ANIMATION_INTERVAL_MS = 2000;
    private static final long DURATION_FOR_HIDING_ANIMATION_MS = 250;
    private static final long DURATION_FOR_SHOWING_ANIMATION_MS = 250;
    private static final int UX_STATE_ALL_VISIBLE = 0;
    private static final int UX_STATE_ANIMATING_HIDE = 3;
    private static final int UX_STATE_ANIMATING_SHOW = 4;
    private static final int UX_STATE_NONE_VISIBLE = 2;
    private static final int UX_STATE_ONLY_PROGRESS_VISIBLE = 1;
    private boolean animationEnabled;
    private final ViewGroup basicControls;
    private final ViewGroup bottomBar;
    private final ViewGroup centerControls;
    private final View controlsBackground;
    private final ViewGroup extraControls;
    private final ViewGroup extraControlsScrollView;
    private final AnimatorSet hideAllBarsAnimator;
    private final Runnable hideAllBarsRunnable;
    private final Runnable hideControllerRunnable;
    private final AnimatorSet hideMainBarAnimator;
    private final Runnable hideMainBarRunnable;
    private final AnimatorSet hideProgressBarAnimator;
    private final Runnable hideProgressBarRunnable;
    private boolean isMinimalMode;
    private final ViewGroup minimalControls;
    private boolean needToShowBars;
    private final View.OnLayoutChangeListener onLayoutChangeListener;
    private final ValueAnimator overflowHideAnimator;
    private final ValueAnimator overflowShowAnimator;
    private final View overflowShowButton;
    private final PlayerControlView playerControlView;
    private final AnimatorSet showAllBarsAnimator;
    private final Runnable showAllBarsRunnable;
    private final AnimatorSet showMainBarAnimator;
    private final List<View> shownButtons;
    private final View timeBar;
    private final ViewGroup timeView;
    private int uxState;

    public static /* synthetic */ void $r8$lambda$3HeZ34EzakoxxM2Sdnb84qSCi8Y(PlayerControlViewLayoutManager r0) {
        r0.updateLayoutForSizeChange();
    }

    public static /* synthetic */ void $r8$lambda$8TTkEsG5P2iXul5H8fqbOohBklk(PlayerControlViewLayoutManager r0) {
        r0.showAllBars();
    }

    public static /* synthetic */ void $r8$lambda$GOumMVb_nT2iiIvIZ0zOPhMLtnc(PlayerControlViewLayoutManager r0) {
        r0.hideProgressBar();
    }

    /* JADX INFO: renamed from: $r8$lambda$RvNhhTKoZYwW33hrXY6mTxN-mUE, reason: not valid java name */
    public static /* synthetic */ void m487$r8$lambda$RvNhhTKoZYwW33hrXY6mTxNmUE(PlayerControlViewLayoutManager r0) {
        r0.hideMainBar();
    }

    public static /* synthetic */ void $r8$lambda$TNKfYJAaRtCcfgegvDBJvWV2bkQ(PlayerControlViewLayoutManager r0) {
        r0.hideController();
    }

    public static /* synthetic */ void $r8$lambda$VHBwT6imrVqBc1kvU6XW4F6usTM(PlayerControlViewLayoutManager r0) {
        r0.onLayoutWidthChanged();
    }

    public static /* synthetic */ void $r8$lambda$aLUsTiRwcbBeuZ7H9FKuQqfWgMQ(PlayerControlViewLayoutManager r0) {
        r0.hideAllBars();
    }

    /* JADX INFO: renamed from: $r8$lambda$bjt2nbwnFTjg5ZlnWprUnT-os4U, reason: not valid java name */
    public static /* synthetic */ void m488$r8$lambda$bjt2nbwnFTjg5ZlnWprUnTos4U(PlayerControlViewLayoutManager r0, View r1, int r2, int r3, int r4, int r5, int r6, int r7, int r8, int r9) {
        r0.onLayoutChange(r1, r2, r3, r4, r5, r6, r7, r8, r9);
    }

    public static /* synthetic */ void $r8$lambda$tph9vvqRIGm8YHpPlKGv1qW34pc(PlayerControlViewLayoutManager r0, View r1) {
        r0.onOverflowButtonClick(r1);
    }

    public PlayerControlViewLayoutManager(final PlayerControlView r13) {
        this.playerControlView = r13;
        this.showAllBarsRunnable = new PlayerControlViewLayoutManager$$ExternalSyntheticLambda5(this);
        this.hideAllBarsRunnable = new PlayerControlViewLayoutManager$$ExternalSyntheticLambda8(this);
        this.hideProgressBarRunnable = new PlayerControlViewLayoutManager$$ExternalSyntheticLambda9(this);
        this.hideMainBarRunnable = new PlayerControlViewLayoutManager$$ExternalSyntheticLambda10(this);
        this.hideControllerRunnable = new PlayerControlViewLayoutManager$$ExternalSyntheticLambda11(this);
        this.onLayoutChangeListener = new PlayerControlViewLayoutManager$$ExternalSyntheticLambda12(this);
        this.animationEnabled = true;
        this.uxState = 0;
        this.shownButtons = new ArrayList();
        this.controlsBackground = r13.findViewById(C0467R.id.exo_controls_background);
        this.centerControls = (ViewGroup) r13.findViewById(C0467R.id.exo_center_controls);
        this.minimalControls = (ViewGroup) r13.findViewById(C0467R.id.exo_minimal_controls);
        this.bottomBar = (ViewGroup) r13.findViewById(C0467R.id.exo_bottom_bar);
        this.timeView = (ViewGroup) r13.findViewById(C0467R.id.exo_time);
        this.timeBar = r13.findViewById(C0467R.id.exo_progress);
        this.basicControls = (ViewGroup) r13.findViewById(C0467R.id.exo_basic_controls);
        this.extraControls = (ViewGroup) r13.findViewById(C0467R.id.exo_extra_controls);
        this.extraControlsScrollView = (ViewGroup) r13.findViewById(C0467R.id.exo_extra_controls_scroll_view);
        this.overflowShowButton = r13.findViewById(C0467R.id.exo_overflow_show);
        View r0 = r13.findViewById(C0467R.id.exo_overflow_hide);
        if (this.overflowShowButton == null) goto L6;
        if (r0 == null) goto L6;
        this.overflowShowButton.setOnClickListener(new PlayerControlViewLayoutManager$$ExternalSyntheticLambda1(this));
        r0.setOnClickListener(new PlayerControlViewLayoutManager$$ExternalSyntheticLambda1(this));
    L6:
        ValueAnimator r2 = ValueAnimator.ofFloat(new float[]{1.0f, 0.0f});
        r2.setInterpolator(new LinearInterpolator());
        r2.addUpdateListener(new PlayerControlViewLayoutManager$$ExternalSyntheticLambda2(this));
        r2.addListener(new C04561(this));
        ValueAnimator r3 = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        r3.setInterpolator(new LinearInterpolator());
        r3.addUpdateListener(new PlayerControlViewLayoutManager$$ExternalSyntheticLambda3(this));
        r3.addListener(new C04572(this));
        Resources r4 = r13.getResources();
        float r5 = r4.getDimension(C0467R.dimen.exo_styled_bottom_bar_height) - r4.getDimension(C0467R.dimen.exo_styled_progress_bar_height);
        float r6 = r4.getDimension(C0467R.dimen.exo_styled_bottom_bar_height);
        this.hideMainBarAnimator = new AnimatorSet();
        this.hideMainBarAnimator.setDuration(250);
        this.hideMainBarAnimator.addListener(new C04583(this, r13));
        this.hideMainBarAnimator.play(r2).with(ofTranslationY(0.0f, r5, this.timeBar)).with(ofTranslationY(0.0f, r5, this.bottomBar));
        this.hideProgressBarAnimator = new AnimatorSet();
        this.hideProgressBarAnimator.setDuration(250);
        this.hideProgressBarAnimator.addListener(new C04594(this, r13));
        this.hideProgressBarAnimator.play(ofTranslationY(r5, r6, this.timeBar)).with(ofTranslationY(r5, r6, this.bottomBar));
        this.hideAllBarsAnimator = new AnimatorSet();
        this.hideAllBarsAnimator.setDuration(250);
        this.hideAllBarsAnimator.addListener(new C04605(this, r13));
        this.hideAllBarsAnimator.play(r2).with(ofTranslationY(0.0f, r6, this.timeBar)).with(ofTranslationY(0.0f, r6, this.bottomBar));
        this.showMainBarAnimator = new AnimatorSet();
        this.showMainBarAnimator.setDuration(250);
        this.showMainBarAnimator.addListener(new C04616(this));
        this.showMainBarAnimator.play(r3).with(ofTranslationY(r5, 0.0f, this.timeBar)).with(ofTranslationY(r5, 0.0f, this.bottomBar));
        this.showAllBarsAnimator = new AnimatorSet();
        this.showAllBarsAnimator.setDuration(250);
        this.showAllBarsAnimator.addListener(new C04627(this));
        this.showAllBarsAnimator.play(r3).with(ofTranslationY(r6, 0.0f, this.timeBar)).with(ofTranslationY(r6, 0.0f, this.bottomBar));
        this.overflowShowAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        this.overflowShowAnimator.setDuration(250);
        this.overflowShowAnimator.addUpdateListener(new PlayerControlViewLayoutManager$$ExternalSyntheticLambda6(this));
        this.overflowShowAnimator.addListener(new C04638(this));
        this.overflowHideAnimator = ValueAnimator.ofFloat(new float[]{1.0f, 0.0f});
        this.overflowHideAnimator.setDuration(250);
        this.overflowHideAnimator.addUpdateListener(new PlayerControlViewLayoutManager$$ExternalSyntheticLambda7(this));
        this.overflowHideAnimator.addListener(new C04649(this));
    }

    static /* synthetic */ View access$000(PlayerControlViewLayoutManager r1) {
        return r1.timeBar;
    }

    static /* synthetic */ boolean access$100(PlayerControlViewLayoutManager r1) {
        return r1.isMinimalMode;
    }

    static /* synthetic */ View access$200(PlayerControlViewLayoutManager r1) {
        return r1.controlsBackground;
    }

    static /* synthetic */ ViewGroup access$300(PlayerControlViewLayoutManager r1) {
        return r1.centerControls;
    }

    static /* synthetic */ ViewGroup access$400(PlayerControlViewLayoutManager r1) {
        return r1.minimalControls;
    }

    static /* synthetic */ void access$500(PlayerControlViewLayoutManager r0, int r1) {
        r0.setUxState(r1);
    }

    static /* synthetic */ boolean access$600(PlayerControlViewLayoutManager r1) {
        return r1.needToShowBars;
    }

    static /* synthetic */ boolean access$602(PlayerControlViewLayoutManager r0, boolean r1) {
        r0.needToShowBars = r1;
        return r1;
    }

    static /* synthetic */ Runnable access$700(PlayerControlViewLayoutManager r1) {
        return r1.showAllBarsRunnable;
    }

    static /* synthetic */ ViewGroup access$800(PlayerControlViewLayoutManager r1) {
        return r1.extraControlsScrollView;
    }

    static /* synthetic */ ViewGroup access$900(PlayerControlViewLayoutManager r1) {
        return r1.basicControls;
    }

    private void animateOverflow(float r5) {
        if (this.extraControlsScrollView == null) goto L6;
        this.extraControlsScrollView.setTranslationX((int) (this.extraControlsScrollView.getWidth() * (1.0f - r5)));
    L6:
        if (this.timeView == null) goto L9;
        this.timeView.setAlpha(1.0f - r5);
    L9:
        if (this.basicControls == null) goto L12;
        this.basicControls.setAlpha(1.0f - r5);
        return;
    }

    private static int getHeightWithMargins(View r5) {
        if (r5 != null) goto L5;
        return 0;
    L5:
        int r0 = r5.getHeight();
        ViewGroup.LayoutParams r1 = r5.getLayoutParams();
        if ((r1 instanceof ViewGroup.MarginLayoutParams) == false) goto L9;
        ViewGroup.MarginLayoutParams r2 = (ViewGroup.MarginLayoutParams) r1;
        return r0 + (r2.topMargin + r2.bottomMargin);
    L9:
        return r0;
    }

    private static int getWidthWithMargins(View r5) {
        if (r5 != null) goto L5;
        return 0;
    L5:
        int r0 = r5.getWidth();
        ViewGroup.LayoutParams r1 = r5.getLayoutParams();
        if ((r1 instanceof ViewGroup.MarginLayoutParams) == false) goto L9;
        ViewGroup.MarginLayoutParams r2 = (ViewGroup.MarginLayoutParams) r1;
        return r0 + (r2.leftMargin + r2.rightMargin);
    L9:
        return r0;
    }

    private void hideAllBars() {
        this.hideAllBarsAnimator.start();
    }

    private void hideController() {
        setUxState(2);
    }

    private void hideMainBar() {
        this.hideMainBarAnimator.start();
        postDelayedRunnable(this.hideProgressBarRunnable, 2000);
    }

    private void hideProgressBar() {
        this.hideProgressBarAnimator.start();
    }

    private static ObjectAnimator ofTranslationY(float r2, float r3, View r4) {
        return ObjectAnimator.ofFloat(r4, "translationY", new float[]{r2, r3});
    }

    private void onLayoutChange(View r4, int r5, int r6, int r7, int r8, int r9, int r10, int r11, int r12) {
        boolean r0 = useMinimalMode();
        if (this.isMinimalMode == r0) goto L6;
        this.isMinimalMode = r0;
        r4.post(new PlayerControlViewLayoutManager$$ExternalSyntheticLambda0(this));
    L6:
        if ((r7 - r5) == (r11 - r9)) goto L8;
        boolean r1 = true;
    L10:
        if (this.isMinimalMode == true) goto L14;
        if (r1 == false) goto L15;
        r4.post(new PlayerControlViewLayoutManager$$ExternalSyntheticLambda4(this));
        return;
    L15:
        return;
    L14:
        return;
    L8:
        r1 = false;
        goto L10
    }

    private void onLayoutWidthChanged() {
        if (this.basicControls != null) goto L5;
        return;
    L5:
        if (this.extraControls == null) goto L53;
        int r0 = (this.playerControlView.getWidth() - this.playerControlView.getPaddingLeft()) - this.playerControlView.getPaddingRight();
    L9:
        if (this.extraControls.getChildCount() <= 1) goto L12;
        int r1 = this.extraControls.getChildCount() - 2;
        View r3 = this.extraControls.getChildAt(r1);
        this.extraControls.removeViewAt(r1);
        this.basicControls.addView(r3, 0);
        goto L9
    L12:
        if (this.overflowShowButton == null) goto L14;
        this.overflowShowButton.setVisibility(8);
    L14:
        int r2 = getWidthWithMargins(this.timeView);
        int r4 = this.basicControls.getChildCount() - 1;
        int r5 = 0;
    L15:
        if (r5 >= r4) goto L17;
        r2 = r2 + getWidthWithMargins(this.basicControls.getChildAt(r5));
        r5 = r5 + 1;
        goto L15
    L17:
        if (r2 <= r0) goto L35;
        if (this.overflowShowButton == null) goto L21;
        this.overflowShowButton.setVisibility(0);
        r2 = r2 + getWidthWithMargins(this.overflowShowButton);
    L21:
        ArrayList r6 = new ArrayList();
        int r7 = 0;
    L22:
        if (r7 >= r4) goto L28;
        View r8 = this.basicControls.getChildAt(r7);
        r2 = r2 - getWidthWithMargins(r8);
        r6.add(r8);
        if (r2 <= r0) goto L28;
        r7 = r7 + 1;
    L28:
        if (r6.isEmpty() == true) goto L51;
        this.basicControls.removeViews(0, r6.size());
        int r9 = 0;
    L31:
        if (r9 >= r6.size()) goto L52;
        this.extraControls.addView((View) r6.get(r9), this.extraControls.getChildCount() - 1);
        r9 = r9 + 1;
        goto L31
    L52:
        return;
    L51:
        return;
    L35:
        if (this.extraControlsScrollView != null) goto L37;
        return;
    L37:
        if (this.extraControlsScrollView.getVisibility() == 0) goto L39;
        return;
    L39:
        if (this.overflowHideAnimator.isStarted() == true) goto L50;
        this.overflowShowAnimator.cancel();
        this.overflowHideAnimator.start();
        return;
    L50:
        return;
    }

    private void onOverflowButtonClick(View r3) {
        resetHideCallbacks();
        if (r3.getId() != C0467R.id.exo_overflow_show) goto L6;
        this.overflowShowAnimator.start();
        return;
    L6:
        if (r3.getId() != C0467R.id.exo_overflow_hide) goto L10;
        this.overflowHideAnimator.start();
        return;
    }

    private void postDelayedRunnable(Runnable r3, long r4) {
        if (r4 < 0) goto L6;
        this.playerControlView.postDelayed(r3, r4);
        return;
    }

    private void setUxState(int r4) {
        int r0 = this.uxState;
        this.uxState = r4;
        if (r4 != 2) goto L5;
        this.playerControlView.setVisibility(8);
    L7:
        if (r0 == r4) goto L10;
        this.playerControlView.notifyOnVisibilityChange();
        return;
    L10:
        return;
    L5:
        if (r0 != 2) goto L7;
        this.playerControlView.setVisibility(0);
        goto L7
    }

    private boolean shouldHideInMinimalMode(View r3) {
        int r0 = r3.getId();
        if (r0 != C0467R.id.exo_bottom_bar) goto L5;
    L18:
        return true;
    L5:
        if (r0 == C0467R.id.exo_prev) goto L18;
        if (r0 == C0467R.id.exo_next) goto L18;
        if (r0 == C0467R.id.exo_rew) goto L18;
        if (r0 == C0467R.id.exo_rew_with_amount) goto L18;
        if (r0 == C0467R.id.exo_ffwd) goto L18;
        if (r0 == C0467R.id.exo_ffwd_with_amount) goto L18;
        return false;
    }

    private void showAllBars() {
        if (this.animationEnabled == true) goto L7;
        setUxState(0);
        resetHideCallbacks();
        return;
    L7:
        switch(this.uxState) {
            case 1: goto L12;
            case 2: goto L11;
            case 3: goto L10;
            case 4: goto L9;
            default: goto L13;
        };
    L9:
        return;
    L10:
        this.needToShowBars = true;
        goto L13
    L11:
        this.showAllBarsAnimator.start();
        goto L13
    L12:
        this.showMainBarAnimator.start();
    L13:
        resetHideCallbacks();
    }

    private void updateLayoutForSizeChange() {
        if (this.minimalControls == null) goto L10;
        ViewGroup r0 = this.minimalControls;
        if (this.isMinimalMode == false) goto L7;
        int r3 = 0;
    L8:
        r0.setVisibility(r3);
        goto L10
    L7:
        r3 = 4;
    L10:
        if (this.timeBar == null) goto L29;
        int r1 = this.playerControlView.getResources().getDimensionPixelSize(C0467R.dimen.exo_styled_progress_margin_bottom);
        ViewGroup.MarginLayoutParams r4 = (ViewGroup.MarginLayoutParams) this.timeBar.getLayoutParams();
        if (r4 == null) goto L19;
        if (this.isMinimalMode == false) goto L16;
        int r5 = 0;
    L17:
        r4.bottomMargin = r5;
        this.timeBar.setLayoutParams(r4);
        goto L19
    L16:
        r5 = r1;
    L19:
        if ((this.timeBar instanceof DefaultTimeBar) == false) goto L29;
        DefaultTimeBar r6 = (DefaultTimeBar) this.timeBar;
        if (this.isMinimalMode == false) goto L24;
        r6.hideScrubber(true);
        goto L29
    L24:
        if (this.uxState != 1) goto L27;
        r6.hideScrubber(false);
        goto L29
    L27:
        if (this.uxState == 3) goto L29;
        r6.showScrubber();
    L29:
        Iterator<View> r2 = this.shownButtons.iterator();
    L31:
        if (r2.hasNext() == false) goto L39;
        View r7 = r2.next();
        if (this.isMinimalMode == true) goto L35;
    L37:
        int r8 = 0;
    L38:
        r7.setVisibility(r8);
        goto L31
    L35:
        if (shouldHideInMinimalMode(r7) == false) goto L37;
        r8 = 4;
        goto L38
    }

    private boolean useMinimalMode() {
        int r0 = (this.playerControlView.getWidth() - this.playerControlView.getPaddingLeft()) - this.playerControlView.getPaddingRight();
        int r1 = (this.playerControlView.getHeight() - this.playerControlView.getPaddingBottom()) - this.playerControlView.getPaddingTop();
        int r2 = getWidthWithMargins(this.centerControls);
        if (this.centerControls == null) goto L5;
        int r3 = this.centerControls.getPaddingLeft() + this.centerControls.getPaddingRight();
    L6:
        int r4 = r2 - r3;
        int r5 = getHeightWithMargins(this.centerControls);
        if (this.centerControls == null) goto L9;
        int r6 = this.centerControls.getPaddingTop() + this.centerControls.getPaddingBottom();
    L10:
        int r7 = r5 - r6;
        int r8 = Math.max(r4, getWidthWithMargins(this.timeView) + getWidthWithMargins(this.overflowShowButton));
        int r9 = (getHeightWithMargins(this.bottomBar) * 2) + r7;
        if (r0 <= r8) goto L13;
        if (r1 <= r9) goto L13;
        return false;
    L13:
        return true;
    L9:
        r6 = 0;
        goto L10
    L5:
        r3 = 0;
        goto L6
    }

    public boolean getShowButton(View r2) {
        if (r2 != null) goto L4;
    L6:
        return false;
    L4:
        if (this.shownButtons.contains(r2) == false) goto L6;
        return true;
    }

    public void hide() {
        if (this.uxState != 3) goto L5;
        return;
    L5:
        if (this.uxState == 2) goto L18;
        removeHideCallbacks();
        if (this.animationEnabled == true) goto L11;
        hideController();
        return;
    L11:
        if (this.uxState != 1) goto L13;
        hideProgressBar();
        return;
    L13:
        hideAllBars();
        return;
    }

    public void hideImmediately() {
        if (this.uxState != 3) goto L5;
        return;
    L5:
        if (this.uxState == 2) goto L10;
        removeHideCallbacks();
        hideController();
        return;
    }

    public boolean isAnimationEnabled() {
        return this.animationEnabled;
    }

    public boolean isFullyVisible() {
        if (this.uxState == 0) goto L5;
    L7:
        return false;
    L5:
        if (this.playerControlView.isVisible() == false) goto L7;
        return true;
    }

    /* JADX INFO: renamed from: lambda$new$0$androidx-media3-ui-PlayerControlViewLayoutManager, reason: not valid java name */
    /* synthetic */ void m489lambda$new$0$androidxmedia3uiPlayerControlViewLayoutManager(ValueAnimator r3) {
        float r0 = ((Float) r3.getAnimatedValue()).floatValue();
        if (this.controlsBackground == null) goto L6;
        this.controlsBackground.setAlpha(r0);
    L6:
        if (this.centerControls == null) goto L9;
        this.centerControls.setAlpha(r0);
    L9:
        if (this.minimalControls == null) goto L12;
        this.minimalControls.setAlpha(r0);
        return;
    }

    /* JADX INFO: renamed from: lambda$new$1$androidx-media3-ui-PlayerControlViewLayoutManager, reason: not valid java name */
    /* synthetic */ void m490lambda$new$1$androidxmedia3uiPlayerControlViewLayoutManager(ValueAnimator r3) {
        float r0 = ((Float) r3.getAnimatedValue()).floatValue();
        if (this.controlsBackground == null) goto L6;
        this.controlsBackground.setAlpha(r0);
    L6:
        if (this.centerControls == null) goto L9;
        this.centerControls.setAlpha(r0);
    L9:
        if (this.minimalControls == null) goto L12;
        this.minimalControls.setAlpha(r0);
        return;
    }

    /* JADX INFO: renamed from: lambda$new$2$androidx-media3-ui-PlayerControlViewLayoutManager, reason: not valid java name */
    /* synthetic */ void m491lambda$new$2$androidxmedia3uiPlayerControlViewLayoutManager(ValueAnimator r2) {
        animateOverflow(((Float) r2.getAnimatedValue()).floatValue());
    }

    /* JADX INFO: renamed from: lambda$new$3$androidx-media3-ui-PlayerControlViewLayoutManager, reason: not valid java name */
    /* synthetic */ void m492lambda$new$3$androidxmedia3uiPlayerControlViewLayoutManager(ValueAnimator r2) {
        animateOverflow(((Float) r2.getAnimatedValue()).floatValue());
    }

    public void onAttachedToWindow() {
        this.playerControlView.addOnLayoutChangeListener(this.onLayoutChangeListener);
    }

    public void onDetachedFromWindow() {
        this.playerControlView.removeOnLayoutChangeListener(this.onLayoutChangeListener);
    }

    public void onLayout(boolean r5, int r6, int r7, int r8, int r9) {
        if (this.controlsBackground == null) goto L6;
        this.controlsBackground.layout(0, 0, r8 - r6, r9 - r7);
        return;
    }

    public void removeHideCallbacks() {
        this.playerControlView.removeCallbacks(this.hideControllerRunnable);
        this.playerControlView.removeCallbacks(this.hideAllBarsRunnable);
        this.playerControlView.removeCallbacks(this.hideMainBarRunnable);
        this.playerControlView.removeCallbacks(this.hideProgressBarRunnable);
    }

    public void resetHideCallbacks() {
        if (this.uxState != 3) goto L5;
        return;
    L5:
        removeHideCallbacks();
        int r0 = this.playerControlView.getShowTimeoutMs();
        if (r0 > 0) goto L8;
        return;
    L8:
        if (this.animationEnabled == true) goto L11;
        postDelayedRunnable(this.hideControllerRunnable, r0);
        return;
    L11:
        if (this.uxState != 1) goto L13;
        postDelayedRunnable(this.hideProgressBarRunnable, 2000);
        return;
    L13:
        postDelayedRunnable(this.hideMainBarRunnable, r0);
    }

    public void setAnimationEnabled(boolean r1) {
        this.animationEnabled = r1;
    }

    public void setShowButton(View r2, boolean r3) {
        if (r2 != null) goto L4;
        return;
    L4:
        if (r3 == true) goto L8;
        r2.setVisibility(8);
        this.shownButtons.remove(r2);
        return;
    L8:
        if (this.isMinimalMode == true) goto L10;
    L12:
        r2.setVisibility(0);
    L13:
        this.shownButtons.add(r2);
        return;
    L10:
        if (shouldHideInMinimalMode(r2) == false) goto L12;
        r2.setVisibility(4);
        goto L13
    }

    public void show() {
        if (this.playerControlView.isVisible() == true) goto L5;
        this.playerControlView.setVisibility(0);
        this.playerControlView.updateAll();
        this.playerControlView.requestPlayPauseFocus();
    L5:
        showAllBars();
    }
}
