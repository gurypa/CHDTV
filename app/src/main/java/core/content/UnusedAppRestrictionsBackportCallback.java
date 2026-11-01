package androidx.core.content;

import android.os.RemoteException;
import androidx.core.app.unusedapprestrictions.IUnusedAppRestrictionsBackportCallback;

/* JADX INFO: loaded from: classes.dex */
public class UnusedAppRestrictionsBackportCallback {
    private IUnusedAppRestrictionsBackportCallback mCallback;

    public UnusedAppRestrictionsBackportCallback(IUnusedAppRestrictionsBackportCallback r1) {
        this.mCallback = r1;
    }

    public void onResult(boolean r2, boolean r3) throws RemoteException {
        this.mCallback.onIsPermissionRevocationEnabledForAppResult(r2, r3);
    }
}
