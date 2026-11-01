package androidx.customview.view;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public abstract class AbsSavedState implements Parcelable {
    public static final Parcelable.Creator<AbsSavedState> CREATOR = null;
    public static final AbsSavedState EMPTY_STATE = null;
    private final Parcelable mSuperState;

    static {
        EMPTY_STATE = new C01401();
        CREATOR = new C01412();
    }

    private AbsSavedState() {
        this.mSuperState = null;
    }

    protected AbsSavedState(Parcel r2) {
        this(r2, null);
    }

    protected AbsSavedState(Parcel r3, ClassLoader r4) {
        Parcelable r0 = r3.readParcelable(r4);
        if (r0 == null) goto L5;
        Parcelable r1 = r0;
    L6:
        this.mSuperState = r1;
        return;
    L5:
        r1 = EMPTY_STATE;
        goto L6
    }

    protected AbsSavedState(Parcelable r3) {
        if (r3 == null) goto L11;
        if (r3 == EMPTY_STATE) goto L7;
        Parcelable r0 = r3;
    L8:
        this.mSuperState = r0;
        return;
    L7:
        r0 = null;
        goto L8
    L11:
        throw new IllegalArgumentException("superState must not be null");
    }

    /* synthetic */ AbsSavedState(C01401 r1) {
        this();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public final Parcelable getSuperState() {
        return this.mSuperState;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel r2, int r3) {
        r2.writeParcelable(this.mSuperState, r3);
    }
}
