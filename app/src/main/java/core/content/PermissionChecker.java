package androidx.core.content;

import android.content.Context;
import android.os.Binder;
import android.os.Process;
import androidx.core.app.AppOpsManagerCompat;
import androidx.core.util.ObjectsCompat;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* JADX INFO: loaded from: classes.dex */
public final class PermissionChecker {
    public static final int PERMISSION_DENIED = -1;
    public static final int PERMISSION_DENIED_APP_OP = -2;
    public static final int PERMISSION_GRANTED = 0;

    @Retention(RetentionPolicy.SOURCE)
    public @interface PermissionResult {
    }

    private PermissionChecker() {
    }

    public static int checkCallingOrSelfPermission(Context r3, String r4) {
        if (Binder.getCallingPid() != Process.myPid()) goto L5;
        String r0 = r3.getPackageName();
    L7:
        return checkPermission(r3, r4, Binder.getCallingPid(), Binder.getCallingUid(), r0);
    L5:
        r0 = null;
        goto L7
    }

    public static int checkCallingPermission(Context r2, String r3, String r4) {
        if (Binder.getCallingPid() != Process.myPid()) goto L7;
        return -1;
    L7:
        return checkPermission(r2, r3, Binder.getCallingPid(), Binder.getCallingUid(), r4);
    }

    public static int checkPermission(Context r6, String r7, int r8, int r9, String r10) {
        if (r6.checkPermission(r7, r8, r9) != (-1)) goto L5;
        return -1;
    L5:
        String r0 = AppOpsManagerCompat.permissionToOp(r7);
        if (r0 != null) goto L8;
        return 0;
    L8:
        if (r10 != null) goto L16;
        String[] r3 = r6.getPackageManager().getPackagesForUid(r9);
        if (r3 != null) goto L12;
    L15:
        return -1;
    L12:
        if (r3.length <= 0) goto L15;
        r10 = r3[0];
    L16:
        int r1 = Process.myUid();
        String r4 = r6.getPackageName();
        if (r1 == r9) goto L19;
    L21:
        boolean r5 = false;
    L22:
        if (r5 == false) goto L24;
        int r11 = AppOpsManagerCompat.checkOrNoteProxyOp(r6, r9, r0, r10);
    L25:
        if (r11 == 0) goto L29;
        return -2;
    L29:
        return 0;
    L24:
        r11 = AppOpsManagerCompat.noteProxyOpNoThrow(r6, r0, r10);
        goto L25
    L19:
        if (ObjectsCompat.equals(r4, r10) == false) goto L21;
        r5 = true;
        goto L22
    }

    public static int checkSelfPermission(Context r3, String r4) {
        return checkPermission(r3, r4, Process.myPid(), Process.myUid(), r3.getPackageName());
    }
}
