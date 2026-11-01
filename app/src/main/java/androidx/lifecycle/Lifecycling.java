package androidx.lifecycle;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class Lifecycling {
    private static final int GENERATED_CALLBACK = 2;
    private static final int REFLECTIVE_CALLBACK = 1;
    private static Map<Class<?>, Integer> sCallbackCache;
    private static Map<Class<?>, List<Constructor<? extends GeneratedAdapter>>> sClassToAdapters;

    static {
        sCallbackCache = new HashMap();
        sClassToAdapters = new HashMap();
    }

    private Lifecycling() {
    }

    private static GeneratedAdapter createGeneratedAdapter(Constructor<? extends GeneratedAdapter> r2, Object r3) {
        return r2.newInstance(new Object[]{r3});
    L9:
        e = move-exception;
        throw new RuntimeException(e);
    L7:
        e = move-exception;
        throw new RuntimeException(e);
    L5:
        e = move-exception;
        throw new RuntimeException(e);
    }

    private static Constructor<? extends GeneratedAdapter> generatedConstructor(Class<?> r8) {
        Package r0 = r8.getPackage();     // Catch: NoSuchMethodException -> L18 ClassNotFoundException -> L21
        String r1 = r8.getCanonicalName();     // Catch: NoSuchMethodException -> L18 ClassNotFoundException -> L21
        if (r0 == null) goto L5;
        String r2 = r0.getName();     // Catch: NoSuchMethodException -> L18 ClassNotFoundException -> L21
    L7:
        if (r2.isEmpty() == false) goto L9;
        String r3 = r1;
    L10:
        String r4 = getAdapterName(r3);     // Catch: NoSuchMethodException -> L18 ClassNotFoundException -> L21
        if (r2.isEmpty() == false) goto L13;
        String r5 = r4;
    L14:
        Constructor r6 = Class.forName(r5).getDeclaredConstructor(new Class[]{r8});     // Catch: NoSuchMethodException -> L18 ClassNotFoundException -> L21
        if (r6.isAccessible() == true) goto L17;
        r6.setAccessible(true);     // Catch: NoSuchMethodException -> L18 ClassNotFoundException -> L21
    L17:
        return r6;
    L13:
        r5 = r2 + "." + r4;     // Catch: NoSuchMethodException -> L18 ClassNotFoundException -> L21
        goto L14
    L9:
        r3 = r1.substring(r2.length() + 1);     // Catch: NoSuchMethodException -> L18 ClassNotFoundException -> L21
        goto L10
    L5:
        r2 = "";
    L18:
        e = move-exception;
        throw new RuntimeException(e);
    L22:
        return null;
    }

    public static String getAdapterName(String r3) {
        return r3.replace(".", "_") + "_LifecycleAdapter";
    }

    @Deprecated
    static GenericLifecycleObserver getCallback(Object r2) {
        final LifecycleEventObserver r0 = lifecycleEventObserver(r2);
        return new C01501(r0);
    }

    private static int getObserverConstructorType(Class<?> r4) {
        Integer r0 = sCallbackCache.get(r4);
        if (r0 != null) goto L5;
        int r1 = resolveObserverCallbackType(r4);
        sCallbackCache.put(r4, Integer.valueOf(r1));
        return r1;
    L5:
        return r0.intValue();
    }

    private static boolean isLifecycleParent(Class<?> r1) {
        if (r1 != null) goto L4;
    L6:
        return false;
    L4:
        if (LifecycleObserver.class.isAssignableFrom(r1) == false) goto L6;
        return true;
    }

    static LifecycleEventObserver lifecycleEventObserver(Object r8) {
        boolean r0 = r8 instanceof LifecycleEventObserver;
        boolean r1 = r8 instanceof FullLifecycleObserver;
        if (r0 == false) goto L7;
        if (r1 == false) goto L7;
        return new FullLifecycleObserverAdapter((FullLifecycleObserver) r8, (LifecycleEventObserver) r8);
    L7:
        if (r1 == true) goto L9;
        if (r0 == true) goto L12;
        Class<?> r2 = r8.getClass();
        if (getObserverConstructorType(r2) != 2) goto L26;
        List<Constructor<? extends GeneratedAdapter>> r4 = sClassToAdapters.get(r2);
        if (r4.size() == 1) goto L18;
        GeneratedAdapter[] r5 = new GeneratedAdapter[r4.size()];
        int r6 = 0;
    L21:
        if (r6 >= r4.size()) goto L24;
        r5[r6] = createGeneratedAdapter(r4.get(r6), r8);
        r6 = r6 + 1;
        goto L21
    L24:
        return new CompositeGeneratedAdaptersObserver(r5);
    L18:
        return new SingleGeneratedAdapterObserver(createGeneratedAdapter(r4.get(0), r8));
    L26:
        return new ReflectiveGenericLifecycleObserver(r8);
    L12:
        return (LifecycleEventObserver) r8;
    L9:
        return new FullLifecycleObserverAdapter((FullLifecycleObserver) r8, null);
    }

    private static int resolveObserverCallbackType(Class<?> r11) {
        if (r11.getCanonicalName() != null) goto L5;
        return 1;
    L5:
        Constructor<? extends GeneratedAdapter> r0 = generatedConstructor(r11);
        if (r0 == null) goto L10;
        sClassToAdapters.put(r11, Collections.singletonList(r0));
        return 2;
    L10:
        if (ClassesInfoCache.sInstance.hasLifecycleMethods(r11) == false) goto L12;
        return 1;
    L12:
        Class<? super Object> r4 = r11.getSuperclass();
        ArrayList r5 = null;
        if (isLifecycleParent(r4) == true) goto L15;
    L18:
        Class<?>[] r6 = r11.getInterfaces();
        int r7 = r6.length;
        int r8 = 0;
    L19:
        if (r8 >= r7) goto L30;
        Class<?> r9 = r6[r8];
        if (isLifecycleParent(r9) == false) goto L29;
        if (getObserverConstructorType(r9) == 1) goto L25;
        if (r5 != null) goto L28;
        r5 = new ArrayList();
    L28:
        r5.addAll(sClassToAdapters.get(r9));
        goto L29
    L25:
        return 1;
    L29:
        r8 = r8 + 1;
        goto L19
    L30:
        if (r5 == null) goto L33;
        sClassToAdapters.put(r11, r5);
        return 2;
    L33:
        return 1;
    L15:
        if (getObserverConstructorType(r4) != 1) goto L17;
        return 1;
    L17:
        r5 = new ArrayList(sClassToAdapters.get(r4));
        goto L18
    }
}
