package androidx.core.view;

import android.app.Activity;
import android.os.Build;
import android.view.DragAndDropPermissions;
import android.view.DragEvent;

/* JADX INFO: loaded from: classes.dex */
public final class DragAndDropPermissionsCompat {
    private final DragAndDropPermissions mDragAndDropPermissions;

    static class Api24Impl {
        private Api24Impl() {
        }

        static void release(DragAndDropPermissions r0) {
            r0.release();
        }

        static DragAndDropPermissions requestDragAndDropPermissions(Activity r1, DragEvent r2) {
            return r1.requestDragAndDropPermissions(r2);
        }
    }

    private DragAndDropPermissionsCompat(DragAndDropPermissions r1) {
        this.mDragAndDropPermissions = r1;
    }

    public static DragAndDropPermissionsCompat request(Activity r2, DragEvent r3) {
        if (Build.VERSION.SDK_INT < 24) goto L8;
        DragAndDropPermissions r0 = Api24Impl.requestDragAndDropPermissions(r2, r3);
        if (r0 != null) goto L7;
        return null;
    L7:
        return new DragAndDropPermissionsCompat(r0);
    L8:
        return null;
    }

    public void release() {
        if (Build.VERSION.SDK_INT < 24) goto L6;
        Api24Impl.release(this.mDragAndDropPermissions);
        return;
    }
}
