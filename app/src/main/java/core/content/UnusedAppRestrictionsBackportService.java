package androidx.core.content;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.core.app.unusedapprestrictions.IUnusedAppRestrictionsBackportService;

/* JADX INFO: loaded from: classes.dex */
public abstract class UnusedAppRestrictionsBackportService extends Service {
    public static final String ACTION_UNUSED_APP_RESTRICTIONS_BACKPORT_CONNECTION = "android.support.unusedapprestrictions.action.CustomUnusedAppRestrictionsBackportService";
    private IUnusedAppRestrictionsBackportService.Stub mBinder;

    public UnusedAppRestrictionsBackportService() {
        this.mBinder = new BinderC00541(this);
    }

    protected abstract void isPermissionRevocationEnabled(UnusedAppRestrictionsBackportCallback r1);

    @Override // android.app.Service
    public IBinder onBind(Intent r2) {
        return this.mBinder;
    }
}
