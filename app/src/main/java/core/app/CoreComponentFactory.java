package androidx.core.app;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.content.Intent;

/* JADX INFO: loaded from: classes.dex */
public class CoreComponentFactory extends android.app.AppComponentFactory {

    public interface CompatWrapped {
        Object getWrapper();
    }

    public CoreComponentFactory() {
    }

    static <T> T checkCompatWrapper(T r1) {
        if ((r1 instanceof CompatWrapped) == false) goto L7;
        T r0 = (T) ((CompatWrapped) r1).getWrapper();
        if (r0 == null) goto L7;
        return r0;
    L7:
        return r1;
    }

    @Override // android.app.AppComponentFactory
    public Activity instantiateActivity(ClassLoader r2, String r3, Intent r4) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return (Activity) checkCompatWrapper(super.instantiateActivity(r2, r3, r4));
    }

    @Override // android.app.AppComponentFactory
    public Application instantiateApplication(ClassLoader r2, String r3) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return (Application) checkCompatWrapper(super.instantiateApplication(r2, r3));
    }

    @Override // android.app.AppComponentFactory
    public ContentProvider instantiateProvider(ClassLoader r2, String r3) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return (ContentProvider) checkCompatWrapper(super.instantiateProvider(r2, r3));
    }

    @Override // android.app.AppComponentFactory
    public BroadcastReceiver instantiateReceiver(ClassLoader r2, String r3, Intent r4) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return (BroadcastReceiver) checkCompatWrapper(super.instantiateReceiver(r2, r3, r4));
    }

    @Override // android.app.AppComponentFactory
    public Service instantiateService(ClassLoader r2, String r3, Intent r4) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return (Service) checkCompatWrapper(super.instantiateService(r2, r3, r4));
    }
}
