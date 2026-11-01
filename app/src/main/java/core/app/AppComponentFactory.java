package androidx.core.app;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.content.Intent;

/* JADX INFO: loaded from: classes.dex */
public class AppComponentFactory extends android.app.AppComponentFactory {
    public AppComponentFactory() {
    }

    @Override // android.app.AppComponentFactory
    public final Activity instantiateActivity(ClassLoader r2, String r3, Intent r4) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return (Activity) CoreComponentFactory.checkCompatWrapper(instantiateActivityCompat(r2, r3, r4));
    }

    public Activity instantiateActivityCompat(ClassLoader r4, String r5, Intent r6) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return (Activity) Class.forName(r5, false, r4).asSubclass(Activity.class).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
    L5:
        e = move-exception;
        throw new RuntimeException("Couldn't call constructor", e);
    }

    @Override // android.app.AppComponentFactory
    public final Application instantiateApplication(ClassLoader r2, String r3) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return (Application) CoreComponentFactory.checkCompatWrapper(instantiateApplicationCompat(r2, r3));
    }

    public Application instantiateApplicationCompat(ClassLoader r4, String r5) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return (Application) Class.forName(r5, false, r4).asSubclass(Application.class).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
    L5:
        e = move-exception;
        throw new RuntimeException("Couldn't call constructor", e);
    }

    @Override // android.app.AppComponentFactory
    public final ContentProvider instantiateProvider(ClassLoader r2, String r3) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return (ContentProvider) CoreComponentFactory.checkCompatWrapper(instantiateProviderCompat(r2, r3));
    }

    public ContentProvider instantiateProviderCompat(ClassLoader r4, String r5) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return (ContentProvider) Class.forName(r5, false, r4).asSubclass(ContentProvider.class).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
    L5:
        e = move-exception;
        throw new RuntimeException("Couldn't call constructor", e);
    }

    @Override // android.app.AppComponentFactory
    public final BroadcastReceiver instantiateReceiver(ClassLoader r2, String r3, Intent r4) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return (BroadcastReceiver) CoreComponentFactory.checkCompatWrapper(instantiateReceiverCompat(r2, r3, r4));
    }

    public BroadcastReceiver instantiateReceiverCompat(ClassLoader r4, String r5, Intent r6) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return (BroadcastReceiver) Class.forName(r5, false, r4).asSubclass(BroadcastReceiver.class).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
    L5:
        e = move-exception;
        throw new RuntimeException("Couldn't call constructor", e);
    }

    @Override // android.app.AppComponentFactory
    public final Service instantiateService(ClassLoader r2, String r3, Intent r4) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return (Service) CoreComponentFactory.checkCompatWrapper(instantiateServiceCompat(r2, r3, r4));
    }

    public Service instantiateServiceCompat(ClassLoader r4, String r5, Intent r6) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return (Service) Class.forName(r5, false, r4).asSubclass(Service.class).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
    L5:
        e = move-exception;
        throw new RuntimeException("Couldn't call constructor", e);
    }
}
