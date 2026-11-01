package androidx.core.view;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/* JADX INFO: loaded from: classes.dex */
public class MenuHostHelper {
    private final CopyOnWriteArrayList<MenuProvider> mMenuProviders;
    private final Runnable mOnInvalidateMenuCallback;
    private final Map<MenuProvider, LifecycleContainer> mProviderToLifecycleContainers;

    private static class LifecycleContainer {
        final Lifecycle mLifecycle;
        private LifecycleEventObserver mObserver;

        LifecycleContainer(Lifecycle r2, LifecycleEventObserver r3) {
            this.mLifecycle = r2;
            this.mObserver = r3;
            this.mLifecycle.addObserver(r3);
        }

        void clearObservers() {
            this.mLifecycle.removeObserver(this.mObserver);
            this.mObserver = null;
        }
    }

    public MenuHostHelper(Runnable r2) {
        this.mMenuProviders = new CopyOnWriteArrayList();
        this.mProviderToLifecycleContainers = new HashMap();
        this.mOnInvalidateMenuCallback = r2;
    }

    public void addMenuProvider(MenuProvider r2) {
        this.mMenuProviders.add(r2);
        this.mOnInvalidateMenuCallback.run();
    }

    public void addMenuProvider(final MenuProvider r6, LifecycleOwner r7) {
        addMenuProvider(r6);
        Lifecycle r0 = r7.getLifecycle();
        LifecycleContainer r1 = this.mProviderToLifecycleContainers.remove(r6);
        if (r1 == null) goto L5;
        r1.clearObservers();
    L5:
        LifecycleEventObserver r2 = new MenuHostHelper$$ExternalSyntheticLambda1(this, r6);
        this.mProviderToLifecycleContainers.put(r6, new LifecycleContainer(r0, r2));
    }

    public void addMenuProvider(final MenuProvider r6, LifecycleOwner r7, final Lifecycle.State r8) {
        Lifecycle r0 = r7.getLifecycle();
        LifecycleContainer r1 = this.mProviderToLifecycleContainers.remove(r6);
        if (r1 == null) goto L5;
        r1.clearObservers();
    L5:
        LifecycleEventObserver r2 = new MenuHostHelper$$ExternalSyntheticLambda0(this, r8, r6);
        this.mProviderToLifecycleContainers.put(r6, new LifecycleContainer(r0, r2));
    }

    /* JADX INFO: renamed from: lambda$addMenuProvider$0$androidx-core-view-MenuHostHelper, reason: not valid java name */
    /* synthetic */ void m454lambda$addMenuProvider$0$androidxcoreviewMenuHostHelper(MenuProvider r2, LifecycleOwner r3, Lifecycle.Event r4) {
        if (r4 != Lifecycle.Event.ON_DESTROY) goto L6;
        removeMenuProvider(r2);
        return;
    }

    /* JADX INFO: renamed from: lambda$addMenuProvider$1$androidx-core-view-MenuHostHelper, reason: not valid java name */
    /* synthetic */ void m455lambda$addMenuProvider$1$androidxcoreviewMenuHostHelper(Lifecycle.State r2, MenuProvider r3, LifecycleOwner r4, Lifecycle.Event r5) {
        if (r5 != Lifecycle.Event.upTo(r2)) goto L6;
        addMenuProvider(r3);
        return;
    L6:
        if (r5 != Lifecycle.Event.ON_DESTROY) goto L9;
        removeMenuProvider(r3);
        return;
    L9:
        if (r5 != Lifecycle.Event.downFrom(r2)) goto L14;
        this.mMenuProviders.remove(r3);
        this.mOnInvalidateMenuCallback.run();
        return;
    }

    public void onCreateMenu(Menu r3, MenuInflater r4) {
        Iterator<MenuProvider> r0 = this.mMenuProviders.iterator();
    L4:
        if (r0.hasNext() == false) goto L6;
        r0.next().onCreateMenu(r3, r4);
        goto L4
    }

    public void onMenuClosed(Menu r3) {
        Iterator<MenuProvider> r0 = this.mMenuProviders.iterator();
    L4:
        if (r0.hasNext() == false) goto L6;
        r0.next().onMenuClosed(r3);
        goto L4
    }

    public boolean onMenuItemSelected(MenuItem r4) {
        Iterator<MenuProvider> r0 = this.mMenuProviders.iterator();
    L4:
        if (r0.hasNext() == false) goto L10;
        if (r0.next().onMenuItemSelected(r4) == false) goto L4;
        return true;
    L10:
        return false;
    }

    public void onPrepareMenu(Menu r3) {
        Iterator<MenuProvider> r0 = this.mMenuProviders.iterator();
    L4:
        if (r0.hasNext() == false) goto L6;
        r0.next().onPrepareMenu(r3);
        goto L4
    }

    public void removeMenuProvider(MenuProvider r3) {
        this.mMenuProviders.remove(r3);
        LifecycleContainer r0 = this.mProviderToLifecycleContainers.remove(r3);
        if (r0 == null) goto L5;
        r0.clearObservers();
    L5:
        this.mOnInvalidateMenuCallback.run();
    }
}
