package es.antonio.m3u;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

/* JADX INFO: loaded from: classes3.dex */
public final class ChdtvApplication extends Application implements Application.ActivityLifecycleCallbacks {
    private final Runnable background;
    private final Runnable expiryTick;
    private Activity foreground;
    private final Handler handler;
    private final Runnable lock;
    private final Set<Activity> startupReads;
    private int visible;

    /* JADX INFO: renamed from: -$$Nest$fgetforeground, reason: not valid java name */
    static /* bridge */ /* synthetic */ Activity m539$$Nest$fgetforeground(ChdtvApplication r0) {
        return r0.foreground;
    }

    /* JADX INFO: renamed from: -$$Nest$fgethandler, reason: not valid java name */
    static /* bridge */ /* synthetic */ Handler m540$$Nest$fgethandler(ChdtvApplication r0) {
        return r0.handler;
    }

    public ChdtvApplication() {
        this.startupReads = Collections.newSetFromMap(new IdentityHashMap());
        this.expiryTick = new RunnableC09741(this);
        this.background = new ChdtvApplication$$ExternalSyntheticLambda0();
        this.handler = new Handler(Looper.getMainLooper());
        this.lock = new ChdtvApplication$$ExternalSyntheticLambda1();
    }

    static /* synthetic */ void lambda$new$0() {
        CdnTokens.background();
        DaznTokens.background();
    }

    static /* synthetic */ void lambda$new$1() {
        ParentalControl.unlocked = false;
    }

    private void startTokenTasks() {
        CdnTokens.foreground(this);
        DaznTokens.foreground(this);
    }

    void catalogLoading(Activity r2) {
        this.startupReads.add(r2);
    }

    void catalogReady(Activity r2) {
        this.startupReads.remove(r2);
        if (this.startupReads.isEmpty() == true) goto L5;
        return;
    L5:
        if (this.visible <= 0) goto L9;
        startTokenTasks();
        return;
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityCreated(Activity r3, Bundle r4) {
        BlackBox.attach(r3);
        BlackBox.event("SCREEN_CREATE", r3.getClass().getSimpleName());
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityDestroyed(Activity r2) {
        if (this.startupReads.contains(r2) == false) goto L5;
        catalogReady(r2);
    L5:
        BlackBox.destroyed(r2);
        ParentalControl.pending.remove(r2);
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityPaused(Activity r3) {
        BlackBox.event("SCREEN_PAUSE", r3.getClass().getSimpleName());
        if (this.foreground != r3) goto L6;
        this.foreground = null;
        this.handler.removeCallbacks(this.expiryTick);
        return;
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityPreCreated(Activity r3, Bundle r4) {
        BlackBox.foreground(true);
        BlackBox.event("SCREEN_CREATE_START", r3.getClass().getSimpleName());
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityResumed(Activity r3) {
        BackgroundWork.resumed(r3);
        BlackBox.foreground(true);
        BlackBox.event("SCREEN_RESUME", r3.getClass().getSimpleName());
        this.foreground = r3;
        this.handler.removeCallbacks(this.expiryTick);
        this.handler.post(this.expiryTick);
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivitySaveInstanceState(Activity r1, Bundle r2) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityStarted(Activity r3) {
        this.visible++;
        BlackBox.foreground(true);
        this.handler.removeCallbacks(this.lock);
        this.handler.removeCallbacks(this.background);
        if (this.startupReads.isEmpty() == false) goto L5;
        startTokenTasks();
        return;
    L5:
        BlackBox.event("STARTUP_TOKENS_DEFERRED", "");
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityStopped(Activity r6) {
        this.visible--;
        if (this.visible <= 0) goto L5;
        return;
    L5:
        if (r6.isChangingConfigurations() == true) goto L12;
        BlackBox.foreground(false);
        this.handler.postDelayed(this.background, 1000);
        if (r6.isFinishing() == false) goto L9;
        ParentalControl.unlocked = false;
        return;
    L9:
        this.handler.postDelayed(this.lock, 30000);
        return;
    }

    @Override // android.app.Application
    public void onCreate() {
        super.onCreate();
        BlackBox.init(this);
        ParentalControl.warm(this);
        TimedAccess.warm(this);
        registerActivityLifecycleCallbacks(this);
    }
}
