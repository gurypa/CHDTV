package androidx.core.view.accessibility;

import android.os.Parcelable;
import android.view.View;
import android.view.accessibility.AccessibilityRecord;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class AccessibilityRecordCompat {
    private final AccessibilityRecord mRecord;

    static class Api15Impl {
        private Api15Impl() {
        }

        static int getMaxScrollX(AccessibilityRecord r1) {
            return r1.getMaxScrollX();
        }

        static int getMaxScrollY(AccessibilityRecord r1) {
            return r1.getMaxScrollY();
        }

        static void setMaxScrollX(AccessibilityRecord r0, int r1) {
            r0.setMaxScrollX(r1);
        }

        static void setMaxScrollY(AccessibilityRecord r0, int r1) {
            r0.setMaxScrollY(r1);
        }
    }

    static class Api16Impl {
        private Api16Impl() {
        }

        static void setSource(AccessibilityRecord r0, View r1, int r2) {
            r0.setSource(r1, r2);
        }
    }

    @Deprecated
    public AccessibilityRecordCompat(Object r2) {
        this.mRecord = (AccessibilityRecord) r2;
    }

    public static int getMaxScrollX(AccessibilityRecord r1) {
        return Api15Impl.getMaxScrollX(r1);
    }

    public static int getMaxScrollY(AccessibilityRecord r1) {
        return Api15Impl.getMaxScrollY(r1);
    }

    @Deprecated
    public static AccessibilityRecordCompat obtain() {
        return new AccessibilityRecordCompat(AccessibilityRecord.obtain());
    }

    @Deprecated
    public static AccessibilityRecordCompat obtain(AccessibilityRecordCompat r2) {
        return new AccessibilityRecordCompat(AccessibilityRecord.obtain(r2.mRecord));
    }

    public static void setMaxScrollX(AccessibilityRecord r0, int r1) {
        Api15Impl.setMaxScrollX(r0, r1);
    }

    public static void setMaxScrollY(AccessibilityRecord r0, int r1) {
        Api15Impl.setMaxScrollY(r0, r1);
    }

    public static void setSource(AccessibilityRecord r0, View r1, int r2) {
        Api16Impl.setSource(r0, r1, r2);
    }

    @Deprecated
    public boolean equals(Object r5) {
        if (this != r5) goto L6;
        return true;
    L6:
        if ((r5 instanceof AccessibilityRecordCompat) == true) goto L8;
        return false;
    L8:
        AccessibilityRecordCompat r1 = (AccessibilityRecordCompat) r5;
        if (this.mRecord != null) goto L16;
        if (r1.mRecord != null) goto L14;
        return true;
    L14:
        return false;
    L16:
        return this.mRecord.equals(r1.mRecord);
    }

    @Deprecated
    public int getAddedCount() {
        return this.mRecord.getAddedCount();
    }

    @Deprecated
    public CharSequence getBeforeText() {
        return this.mRecord.getBeforeText();
    }

    @Deprecated
    public CharSequence getClassName() {
        return this.mRecord.getClassName();
    }

    @Deprecated
    public CharSequence getContentDescription() {
        return this.mRecord.getContentDescription();
    }

    @Deprecated
    public int getCurrentItemIndex() {
        return this.mRecord.getCurrentItemIndex();
    }

    @Deprecated
    public int getFromIndex() {
        return this.mRecord.getFromIndex();
    }

    @Deprecated
    public Object getImpl() {
        return this.mRecord;
    }

    @Deprecated
    public int getItemCount() {
        return this.mRecord.getItemCount();
    }

    @Deprecated
    public int getMaxScrollX() {
        return getMaxScrollX(this.mRecord);
    }

    @Deprecated
    public int getMaxScrollY() {
        return getMaxScrollY(this.mRecord);
    }

    @Deprecated
    public Parcelable getParcelableData() {
        return this.mRecord.getParcelableData();
    }

    @Deprecated
    public int getRemovedCount() {
        return this.mRecord.getRemovedCount();
    }

    @Deprecated
    public int getScrollX() {
        return this.mRecord.getScrollX();
    }

    @Deprecated
    public int getScrollY() {
        return this.mRecord.getScrollY();
    }

    @Deprecated
    public AccessibilityNodeInfoCompat getSource() {
        return AccessibilityNodeInfoCompat.wrapNonNullInstance(this.mRecord.getSource());
    }

    @Deprecated
    public List<CharSequence> getText() {
        return this.mRecord.getText();
    }

    @Deprecated
    public int getToIndex() {
        return this.mRecord.getToIndex();
    }

    @Deprecated
    public int getWindowId() {
        return this.mRecord.getWindowId();
    }

    @Deprecated
    public int hashCode() {
        if (this.mRecord != null) goto L6;
        return 0;
    L6:
        return this.mRecord.hashCode();
    }

    @Deprecated
    public boolean isChecked() {
        return this.mRecord.isChecked();
    }

    @Deprecated
    public boolean isEnabled() {
        return this.mRecord.isEnabled();
    }

    @Deprecated
    public boolean isFullScreen() {
        return this.mRecord.isFullScreen();
    }

    @Deprecated
    public boolean isPassword() {
        return this.mRecord.isPassword();
    }

    @Deprecated
    public boolean isScrollable() {
        return this.mRecord.isScrollable();
    }

    @Deprecated
    public void recycle() {
        this.mRecord.recycle();
    }

    @Deprecated
    public void setAddedCount(int r2) {
        this.mRecord.setAddedCount(r2);
    }

    @Deprecated
    public void setBeforeText(CharSequence r2) {
        this.mRecord.setBeforeText(r2);
    }

    @Deprecated
    public void setChecked(boolean r2) {
        this.mRecord.setChecked(r2);
    }

    @Deprecated
    public void setClassName(CharSequence r2) {
        this.mRecord.setClassName(r2);
    }

    @Deprecated
    public void setContentDescription(CharSequence r2) {
        this.mRecord.setContentDescription(r2);
    }

    @Deprecated
    public void setCurrentItemIndex(int r2) {
        this.mRecord.setCurrentItemIndex(r2);
    }

    @Deprecated
    public void setEnabled(boolean r2) {
        this.mRecord.setEnabled(r2);
    }

    @Deprecated
    public void setFromIndex(int r2) {
        this.mRecord.setFromIndex(r2);
    }

    @Deprecated
    public void setFullScreen(boolean r2) {
        this.mRecord.setFullScreen(r2);
    }

    @Deprecated
    public void setItemCount(int r2) {
        this.mRecord.setItemCount(r2);
    }

    @Deprecated
    public void setMaxScrollX(int r2) {
        setMaxScrollX(this.mRecord, r2);
    }

    @Deprecated
    public void setMaxScrollY(int r2) {
        setMaxScrollY(this.mRecord, r2);
    }

    @Deprecated
    public void setParcelableData(Parcelable r2) {
        this.mRecord.setParcelableData(r2);
    }

    @Deprecated
    public void setPassword(boolean r2) {
        this.mRecord.setPassword(r2);
    }

    @Deprecated
    public void setRemovedCount(int r2) {
        this.mRecord.setRemovedCount(r2);
    }

    @Deprecated
    public void setScrollX(int r2) {
        this.mRecord.setScrollX(r2);
    }

    @Deprecated
    public void setScrollY(int r2) {
        this.mRecord.setScrollY(r2);
    }

    @Deprecated
    public void setScrollable(boolean r2) {
        this.mRecord.setScrollable(r2);
    }

    @Deprecated
    public void setSource(View r2) {
        this.mRecord.setSource(r2);
    }

    @Deprecated
    public void setSource(View r2, int r3) {
        setSource(this.mRecord, r2, r3);
    }

    @Deprecated
    public void setToIndex(int r2) {
        this.mRecord.setToIndex(r2);
    }
}
