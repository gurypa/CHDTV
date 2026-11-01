package androidx.core.app;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
public final class TaskStackBuilder implements Iterable<Intent> {
    private static final String TAG = "TaskStackBuilder";
    private final ArrayList<Intent> mIntents;
    private final Context mSourceContext;

    static class Api16Impl {
        private Api16Impl() {
        }

        static PendingIntent getActivities(Context r1, int r2, Intent[] r3, int r4, Bundle r5) {
            return PendingIntent.getActivities(r1, r2, r3, r4, r5);
        }
    }

    public interface SupportParentable {
        Intent getSupportParentActivityIntent();
    }

    private TaskStackBuilder(Context r2) {
        this.mIntents = new ArrayList();
        this.mSourceContext = r2;
    }

    public static TaskStackBuilder create(Context r1) {
        return new TaskStackBuilder(r1);
    }

    @Deprecated
    public static TaskStackBuilder from(Context r1) {
        return create(r1);
    }

    public TaskStackBuilder addNextIntent(Intent r2) {
        this.mIntents.add(r2);
        return this;
    }

    public TaskStackBuilder addNextIntentWithParentStack(Intent r3) {
        ComponentName r0 = r3.getComponent();
        if (r0 != null) goto L5;
        r0 = r3.resolveActivity(this.mSourceContext.getPackageManager());
    L5:
        if (r0 == null) goto L7;
        addParentStack(r0);
    L7:
        addNextIntent(r3);
        return this;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public TaskStackBuilder addParentStack(Activity r4) {
        Intent r0 = null;
        if ((r4 instanceof SupportParentable) == false) goto L5;
        r0 = ((SupportParentable) r4).getSupportParentActivityIntent();
    L5:
        if (r0 != null) goto L7;
        r0 = NavUtils.getParentActivityIntent(r4);
    L7:
        if (r0 == null) goto L12;
        ComponentName r1 = r0.getComponent();
        if (r1 != null) goto L11;
        r1 = r0.resolveActivity(this.mSourceContext.getPackageManager());
    L11:
        addParentStack(r1);
        addNextIntent(r0);
    L12:
        return this;
    }

    public TaskStackBuilder addParentStack(ComponentName r5) {
        int r0 = this.mIntents.size();
        Intent r1 = NavUtils.getParentActivityIntent(this.mSourceContext, r5);     // Catch: PackageManager.NameNotFoundException -> L9
    L4:
        if (r1 == null) goto L8;
        this.mIntents.add(r0, r1);     // Catch: PackageManager.NameNotFoundException -> L9
        r1 = NavUtils.getParentActivityIntent(this.mSourceContext, r1.getComponent());     // Catch: PackageManager.NameNotFoundException -> L9
        goto L4
    L8:
        return this;
    L9:
        e = move-exception;
        Log.e(TAG, "Bad ComponentName while traversing activity parent metadata");
        throw new IllegalArgumentException(e);
    }

    public TaskStackBuilder addParentStack(Class<?> r3) {
        return addParentStack(new ComponentName(this.mSourceContext, r3));
    }

    public Intent editIntentAt(int r2) {
        return this.mIntents.get(r2);
    }

    @Deprecated
    public Intent getIntent(int r2) {
        return editIntentAt(r2);
    }

    public int getIntentCount() {
        return this.mIntents.size();
    }

    public Intent[] getIntents() {
        Intent[] r0 = new Intent[this.mIntents.size()];
        if (r0.length != 0) goto L5;
        return r0;
    L5:
        r0[0] = new Intent(this.mIntents.get(0)).addFlags(268484608);
        int r1 = 1;
    L7:
        if (r1 >= r0.length) goto L9;
        r0[r1] = new Intent(this.mIntents.get(r1));
        r1 = r1 + 1;
        goto L7
    L9:
        return r0;
    }

    public PendingIntent getPendingIntent(int r2, int r3) {
        return getPendingIntent(r2, r3, null);
    }

    public PendingIntent getPendingIntent(int r5, int r6, Bundle r7) {
        if (this.mIntents.isEmpty() == true) goto L7;
        Intent[] r0 = (Intent[]) this.mIntents.toArray(new Intent[0]);
        r0[0] = new Intent(r0[0]).addFlags(268484608);
        return Api16Impl.getActivities(this.mSourceContext, r5, r0, r6, r7);
    L7:
        throw new IllegalStateException("No intents added to TaskStackBuilder; cannot getPendingIntent");
    }

    @Override // java.lang.Iterable
    @Deprecated
    public Iterator<Intent> iterator() {
        return this.mIntents.iterator();
    }

    public void startActivities() {
        startActivities(null);
    }

    public void startActivities(Bundle r5) {
        if (this.mIntents.isEmpty() == true) goto L9;
        Intent[] r0 = (Intent[]) this.mIntents.toArray(new Intent[0]);
        r0[0] = new Intent(r0[0]).addFlags(268484608);
        if (ContextCompat.startActivities(this.mSourceContext, r0, r5) == true) goto L10;
        Intent r1 = new Intent(r0[r0.length - 1]);
        r1.addFlags(268435456);
        this.mSourceContext.startActivity(r1);
        return;
    L10:
        return;
    L9:
        throw new IllegalStateException("No intents added to TaskStackBuilder; cannot startActivities");
    }
}
