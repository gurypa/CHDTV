package androidx.recyclerview.widget;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewPropertyAnimator;
import androidx.core.view.ViewCompat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class DefaultItemAnimator extends SimpleItemAnimator {
    private static final boolean DEBUG = false;
    private static TimeInterpolator sDefaultInterpolator;
    ArrayList<RecyclerView.ViewHolder> mAddAnimations;
    ArrayList<ArrayList<RecyclerView.ViewHolder>> mAdditionsList;
    ArrayList<RecyclerView.ViewHolder> mChangeAnimations;
    ArrayList<ArrayList<ChangeInfo>> mChangesList;
    ArrayList<RecyclerView.ViewHolder> mMoveAnimations;
    ArrayList<ArrayList<MoveInfo>> mMovesList;
    private ArrayList<RecyclerView.ViewHolder> mPendingAdditions;
    private ArrayList<ChangeInfo> mPendingChanges;
    private ArrayList<MoveInfo> mPendingMoves;
    private ArrayList<RecyclerView.ViewHolder> mPendingRemovals;
    ArrayList<RecyclerView.ViewHolder> mRemoveAnimations;

    private static class ChangeInfo {
        public int fromX;
        public int fromY;
        public RecyclerView.ViewHolder newHolder;
        public RecyclerView.ViewHolder oldHolder;
        public int toX;
        public int toY;

        private ChangeInfo(RecyclerView.ViewHolder r1, RecyclerView.ViewHolder r2) {
            this.oldHolder = r1;
            this.newHolder = r2;
        }

        ChangeInfo(RecyclerView.ViewHolder r1, RecyclerView.ViewHolder r2, int r3, int r4, int r5, int r6) {
            this(r1, r2);
            this.fromX = r3;
            this.fromY = r4;
            this.toX = r5;
            this.toY = r6;
        }

        public String toString() {
            return "ChangeInfo{oldHolder=" + this.oldHolder + ", newHolder=" + this.newHolder + ", fromX=" + this.fromX + ", fromY=" + this.fromY + ", toX=" + this.toX + ", toY=" + this.toY + '}';
        }
    }

    private static class MoveInfo {
        public int fromX;
        public int fromY;
        public RecyclerView.ViewHolder holder;
        public int toX;
        public int toY;

        MoveInfo(RecyclerView.ViewHolder r1, int r2, int r3, int r4, int r5) {
            this.holder = r1;
            this.fromX = r2;
            this.fromY = r3;
            this.toX = r4;
            this.toY = r5;
        }
    }

    public DefaultItemAnimator() {
        this.mPendingRemovals = new ArrayList();
        this.mPendingAdditions = new ArrayList();
        this.mPendingMoves = new ArrayList();
        this.mPendingChanges = new ArrayList();
        this.mAdditionsList = new ArrayList();
        this.mMovesList = new ArrayList();
        this.mChangesList = new ArrayList();
        this.mAddAnimations = new ArrayList();
        this.mMoveAnimations = new ArrayList();
        this.mRemoveAnimations = new ArrayList();
        this.mChangeAnimations = new ArrayList();
    }

    private void animateRemoveImpl(final RecyclerView.ViewHolder r5) {
        final View r0 = r5.itemView;
        final ViewPropertyAnimator r1 = r0.animate();
        this.mRemoveAnimations.add(r5);
        r1.setDuration(getRemoveDuration()).alpha(0.0f).setListener(new C04794(this, r5, r1, r0)).start();
    }

    private void endChangeAnimation(List<ChangeInfo> r4, RecyclerView.ViewHolder r5) {
        int r0 = r4.size() - 1;
    L3:
        if (r0 < 0) goto L12;
        ChangeInfo r1 = r4.get(r0);
        if (endChangeAnimationIfNecessary(r1, r5) == false) goto L11;
        if (r1.oldHolder != null) goto L11;
        if (r1.newHolder != null) goto L11;
        r4.remove(r1);
    L11:
        r0 = r0 - 1;
        goto L3
    }

    private void endChangeAnimationIfNecessary(ChangeInfo r2) {
        if (r2.oldHolder == null) goto L6;
        endChangeAnimationIfNecessary(r2, r2.oldHolder);
    L6:
        if (r2.newHolder == null) goto L9;
        endChangeAnimationIfNecessary(r2, r2.newHolder);
        return;
    }

    private boolean endChangeAnimationIfNecessary(ChangeInfo r4, RecyclerView.ViewHolder r5) {
        boolean r0 = false;
        if (r4.newHolder != r5) goto L6;
        r4.newHolder = null;
    L8:
        r5.itemView.setAlpha(1.0f);
        r5.itemView.setTranslationX(0.0f);
        r5.itemView.setTranslationY(0.0f);
        dispatchChangeFinished(r5, r0);
        return true;
    L6:
        if (r4.oldHolder != r5) goto L10;
        r4.oldHolder = null;
        r0 = true;
        goto L8
    L10:
        return false;
    }

    private void resetAnimation(RecyclerView.ViewHolder r3) {
        if (sDefaultInterpolator != null) goto L5;
        sDefaultInterpolator = new ValueAnimator().getInterpolator();
    L5:
        r3.itemView.animate().setInterpolator(sDefaultInterpolator);
        endAnimation(r3);
    }

    @Override // androidx.recyclerview.widget.SimpleItemAnimator
    public boolean animateAdd(RecyclerView.ViewHolder r3) {
        resetAnimation(r3);
        r3.itemView.setAlpha(0.0f);
        this.mPendingAdditions.add(r3);
        return true;
    }

    void animateAddImpl(final RecyclerView.ViewHolder r6) {
        final View r0 = r6.itemView;
        final ViewPropertyAnimator r1 = r0.animate();
        this.mAddAnimations.add(r6);
        r1.alpha(1.0f).setDuration(getAddDuration()).setListener(new C04805(this, r6, r0, r1)).start();
    }

    @Override // androidx.recyclerview.widget.SimpleItemAnimator
    public boolean animateChange(RecyclerView.ViewHolder r10, RecyclerView.ViewHolder r11, int r12, int r13, int r14, int r15) {
        if (r10 == r11) goto L4;
        float r16 = r10.itemView.getTranslationX();
        float r17 = r10.itemView.getTranslationY();
        float r18 = r10.itemView.getAlpha();
        resetAnimation(r10);
        int r19 = (int) ((r14 - r12) - r16);
        int r110 = (int) ((r15 - r13) - r17);
        r10.itemView.setTranslationX(r16);
        r10.itemView.setTranslationY(r17);
        r10.itemView.setAlpha(r18);
        if (r11 == null) goto L8;
        resetAnimation(r11);
        r11.itemView.setTranslationX(-r19);
        r11.itemView.setTranslationY(-r110);
        r11.itemView.setAlpha(0.0f);
    L8:
        this.mPendingChanges.add(new ChangeInfo(r10, r11, r12, r13, r14, r15));
        return true;
    L4:
        return animateMove(r10, r12, r13, r14, r15);
    }

    void animateChangeImpl(final ChangeInfo r9) {
        RecyclerView.ViewHolder r0 = r9.oldHolder;
        final View r1 = null;
        if (r0 != null) goto L5;
        final View r2 = null;
    L6:
        RecyclerView.ViewHolder r3 = r9.newHolder;
        if (r3 == null) goto L10;
        r1 = r3.itemView;
    L10:
        if (r2 == null) goto L12;
        final ViewPropertyAnimator r5 = r2.animate().setDuration(getChangeDuration());
        this.mChangeAnimations.add(r9.oldHolder);
        r5.translationX(r9.toX - r9.fromX);
        r5.translationY(r9.toY - r9.fromY);
        r5.alpha(0.0f).setListener(new C04827(this, r9, r5, r2)).start();
    L12:
        if (r1 == null) goto L15;
        final ViewPropertyAnimator r6 = r1.animate();
        this.mChangeAnimations.add(r9.newHolder);
        r6.translationX(0.0f).translationY(0.0f).setDuration(getChangeDuration()).alpha(1.0f).setListener(new C04838(this, r9, r6, r1)).start();
        return;
    L15:
        return;
    L5:
        r2 = r0.itemView;
        goto L6
    }

    @Override // androidx.recyclerview.widget.SimpleItemAnimator
    public boolean animateMove(RecyclerView.ViewHolder r9, int r10, int r11, int r12, int r13) {
        View r0 = r9.itemView;
        int r4 = r10 + ((int) r9.itemView.getTranslationX());
        int r5 = r11 + ((int) r9.itemView.getTranslationY());
        resetAnimation(r9);
        int r14 = r12 - r4;
        int r15 = r13 - r5;
        if (r14 != 0) goto L7;
        if (r15 != 0) goto L7;
        dispatchMoveFinished(r9);
        return false;
    L7:
        if (r14 == 0) goto L9;
        r0.setTranslationX(-r14);
    L9:
        if (r15 == 0) goto L11;
        r0.setTranslationY(-r15);
    L11:
        this.mPendingMoves.add(new MoveInfo(r9, r4, r5, r12, r13));
        return true;
    }

    void animateMoveImpl(final RecyclerView.ViewHolder r9, int r10, int r11, int r12, int r13) {
        final View r4 = r9.itemView;
        final int r3 = r12 - r10;
        final int r5 = r13 - r11;
        if (r3 == 0) goto L5;
        r4.animate().translationX(0.0f);
    L5:
        if (r5 == 0) goto L7;
        r4.animate().translationY(0.0f);
    L7:
        final ViewPropertyAnimator r6 = r4.animate();
        this.mMoveAnimations.add(r9);
        r6.setDuration(getMoveDuration()).setListener(new C04816(this, r9, r3, r4, r5, r6)).start();
    }

    @Override // androidx.recyclerview.widget.SimpleItemAnimator
    public boolean animateRemove(RecyclerView.ViewHolder r2) {
        resetAnimation(r2);
        this.mPendingRemovals.add(r2);
        return true;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemAnimator
    public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder r2, List<Object> r3) {
        if (r3.isEmpty() == true) goto L5;
    L8:
        return true;
    L5:
        if (super.canReuseUpdatedViewHolder(r2, r3) == true) goto L8;
        return false;
    }

    void cancelAll(List<RecyclerView.ViewHolder> r3) {
        int r0 = r3.size() - 1;
    L3:
        if (r0 < 0) goto L5;
        r3.get(r0).itemView.animate().cancel();
        r0 = r0 - 1;
        goto L3
    }

    void dispatchFinishedWhenDone() {
        if (isRunning() == true) goto L6;
        dispatchAnimationsFinished();
        return;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemAnimator
    public void endAnimation(RecyclerView.ViewHolder r9) {
        View r0 = r9.itemView;
        r0.animate().cancel();
        int r1 = this.mPendingMoves.size();
    L3:
        r1 = r1 - 1;
        if (r1 < 0) goto L9;
        if (this.mPendingMoves.get(r1).holder != r9) goto L3;
        r0.setTranslationY(0.0f);
        r0.setTranslationX(0.0f);
        dispatchMoveFinished(r9);
        this.mPendingMoves.remove(r1);
        goto L3
    L9:
        endChangeAnimation(this.mPendingChanges, r9);
        if (this.mPendingRemovals.remove(r9) == false) goto L13;
        r0.setAlpha(1.0f);
        dispatchRemoveFinished(r9);
    L13:
        if (this.mPendingAdditions.remove(r9) == false) goto L15;
        r0.setAlpha(1.0f);
        dispatchAddFinished(r9);
    L15:
        int r2 = this.mChangesList.size() - 1;
    L16:
        if (r2 < 0) goto L21;
        ArrayList<ChangeInfo> r4 = this.mChangesList.get(r2);
        endChangeAnimation(r4, r9);
        if (r4.isEmpty() == false) goto L20;
        this.mChangesList.remove(r2);
    L20:
        r2 = r2 - 1;
        goto L16
    L21:
        int r3 = this.mMovesList.size() - 1;
    L22:
        if (r3 < 0) goto L32;
        ArrayList<MoveInfo> r5 = this.mMovesList.get(r3);
        int r6 = r5.size() - 1;
    L24:
        if (r6 < 0) goto L31;
        if (r5.get(r6).holder == r9) goto L27;
        r6 = r6 - 1;
        goto L24
    L27:
        r0.setTranslationY(0.0f);
        r0.setTranslationX(0.0f);
        dispatchMoveFinished(r9);
        r5.remove(r6);
        if (r5.isEmpty() == false) goto L31;
        this.mMovesList.remove(r3);
    L31:
        r3 = r3 - 1;
        goto L22
    L32:
        int r7 = this.mAdditionsList.size() - 1;
    L33:
        if (r7 < 0) goto L40;
        ArrayList<RecyclerView.ViewHolder> r8 = this.mAdditionsList.get(r7);
        if (r8.remove(r9) == false) goto L39;
        r0.setAlpha(1.0f);
        dispatchAddFinished(r9);
        if (r8.isEmpty() == false) goto L39;
        this.mAdditionsList.remove(r7);
    L39:
        r7 = r7 - 1;
        goto L33
    L40:
        this.mRemoveAnimations.remove(r9);
        this.mAddAnimations.remove(r9);
        this.mChangeAnimations.remove(r9);
        this.mMoveAnimations.remove(r9);
        dispatchFinishedWhenDone();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemAnimator
    public void endAnimations() {
        int r1 = this.mPendingMoves.size() - 1;
    L4:
        if (r1 < 0) goto L6;
        MoveInfo r3 = this.mPendingMoves.get(r1);
        View r4 = r3.holder.itemView;
        r4.setTranslationY(0.0f);
        r4.setTranslationX(0.0f);
        dispatchMoveFinished(r3.holder);
        this.mPendingMoves.remove(r1);
        r1 = r1 - 1;
        goto L4
    L6:
        int r2 = this.mPendingRemovals.size() - 1;
    L7:
        if (r2 < 0) goto L9;
        dispatchRemoveFinished(this.mPendingRemovals.get(r2));
        this.mPendingRemovals.remove(r2);
        r2 = r2 - 1;
        goto L7
    L9:
        int r5 = this.mPendingAdditions.size() - 1;
    L11:
        if (r5 < 0) goto L13;
        RecyclerView.ViewHolder r6 = this.mPendingAdditions.get(r5);
        r6.itemView.setAlpha(1.0f);
        dispatchAddFinished(r6);
        this.mPendingAdditions.remove(r5);
        r5 = r5 - 1;
        goto L11
    L13:
        int r7 = this.mPendingChanges.size() - 1;
    L14:
        if (r7 < 0) goto L16;
        endChangeAnimationIfNecessary(this.mPendingChanges.get(r7));
        r7 = r7 - 1;
        goto L14
    L16:
        this.mPendingChanges.clear();
        if (isRunning() == true) goto L19;
        return;
    L19:
        int r8 = this.mMovesList.size() - 1;
    L20:
        if (r8 < 0) goto L28;
        ArrayList<MoveInfo> r9 = this.mMovesList.get(r8);
        int r10 = r9.size() - 1;
    L22:
        if (r10 < 0) goto L27;
        MoveInfo r11 = r9.get(r10);
        View r12 = r11.holder.itemView;
        r12.setTranslationY(0.0f);
        r12.setTranslationX(0.0f);
        dispatchMoveFinished(r11.holder);
        r9.remove(r10);
        if (r9.isEmpty() == false) goto L26;
        this.mMovesList.remove(r9);
    L26:
        r10 = r10 - 1;
        goto L22
    L27:
        r8 = r8 - 1;
        goto L20
    L28:
        int r13 = this.mAdditionsList.size() - 1;
    L29:
        if (r13 < 0) goto L37;
        ArrayList<RecyclerView.ViewHolder> r14 = this.mAdditionsList.get(r13);
        int r15 = r14.size() - 1;
    L31:
        if (r15 < 0) goto L36;
        RecyclerView.ViewHolder r16 = r14.get(r15);
        r16.itemView.setAlpha(1.0f);
        dispatchAddFinished(r16);
        r14.remove(r15);
        if (r14.isEmpty() == false) goto L35;
        this.mAdditionsList.remove(r14);
    L35:
        r15 = r15 - 1;
        goto L31
    L36:
        r13 = r13 - 1;
        goto L29
    L37:
        int r17 = this.mChangesList.size() - 1;
    L38:
        if (r17 < 0) goto L46;
        ArrayList<ChangeInfo> r18 = this.mChangesList.get(r17);
        int r19 = r18.size() - 1;
    L40:
        if (r19 < 0) goto L45;
        endChangeAnimationIfNecessary(r18.get(r19));
        if (r18.isEmpty() == false) goto L44;
        this.mChangesList.remove(r18);
    L44:
        r19 = r19 - 1;
        goto L40
    L45:
        r17 = r17 - 1;
        goto L38
    L46:
        cancelAll(this.mRemoveAnimations);
        cancelAll(this.mMoveAnimations);
        cancelAll(this.mAddAnimations);
        cancelAll(this.mChangeAnimations);
        dispatchAnimationsFinished();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemAnimator
    public boolean isRunning() {
        if (this.mPendingAdditions.isEmpty() == true) goto L5;
    L26:
        return true;
    L5:
        if (this.mPendingChanges.isEmpty() == false) goto L26;
        if (this.mPendingMoves.isEmpty() == false) goto L26;
        if (this.mPendingRemovals.isEmpty() == false) goto L26;
        if (this.mMoveAnimations.isEmpty() == false) goto L26;
        if (this.mRemoveAnimations.isEmpty() == false) goto L26;
        if (this.mAddAnimations.isEmpty() == false) goto L26;
        if (this.mChangeAnimations.isEmpty() == false) goto L26;
        if (this.mMovesList.isEmpty() == false) goto L26;
        if (this.mAdditionsList.isEmpty() == false) goto L26;
        if (this.mChangesList.isEmpty() == false) goto L26;
        return false;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemAnimator
    public void runPendingAnimations() {
        boolean r0 = !this.mPendingRemovals.isEmpty();
        boolean r1 = !this.mPendingMoves.isEmpty();
        boolean r2 = !this.mPendingChanges.isEmpty();
        boolean r3 = !this.mPendingAdditions.isEmpty();
        if (r0 == true) goto L8;
        if (r1 == true) goto L8;
        if (r3 == true) goto L8;
        if (r2 == true) goto L8;
        return;
    L8:
        Iterator<RecyclerView.ViewHolder> r4 = this.mPendingRemovals.iterator();
    L10:
        if (r4.hasNext() == false) goto L12;
        animateRemoveImpl(r4.next());
        goto L10
    L12:
        this.mPendingRemovals.clear();
        if (r1 == false) goto L18;
        final ArrayList<MoveInfo> r5 = new ArrayList();
        r5.addAll(this.mPendingMoves);
        this.mMovesList.add(r5);
        this.mPendingMoves.clear();
        Runnable r6 = new RunnableC04761(this, r5);
        if (r0 == false) goto L17;
        ViewCompat.postOnAnimationDelayed(r5.get(0).holder.itemView, r6, getRemoveDuration());
        goto L18
    L17:
        r6.run();
    L18:
        if (r2 == false) goto L23;
        final ArrayList<ChangeInfo> r7 = new ArrayList();
        r7.addAll(this.mPendingChanges);
        this.mChangesList.add(r7);
        this.mPendingChanges.clear();
        Runnable r8 = new RunnableC04772(this, r7);
        if (r0 == false) goto L22;
        ViewCompat.postOnAnimationDelayed(r7.get(0).oldHolder.itemView, r8, getRemoveDuration());
        goto L23
    L22:
        r8.run();
    L23:
        if (r3 == false) goto L42;
        final ArrayList<RecyclerView.ViewHolder> r9 = new ArrayList();
        r9.addAll(this.mPendingAdditions);
        this.mAdditionsList.add(r9);
        this.mPendingAdditions.clear();
        Runnable r10 = new RunnableC04783(this, r9);
        if (r0 == true) goto L30;
        if (r1 == true) goto L30;
        if (r2 == true) goto L30;
        r10.run();
        return;
    L30:
        long r11 = 0;
        if (r0 == false) goto L33;
        long r12 = getRemoveDuration();
    L34:
        if (r1 == false) goto L36;
        long r13 = getMoveDuration();
    L37:
        if (r2 == false) goto L39;
        r11 = getChangeDuration();
    L39:
        ViewCompat.postOnAnimationDelayed(r9.get(0).itemView, r10, Math.max(r13, r11) + r12);
        return;
    L36:
        r13 = 0;
        goto L37
    L33:
        r12 = 0;
        goto L34
    }
}
