package androidx.lifecycle;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
final class ClassesInfoCache {
    private static final int CALL_TYPE_NO_ARG = 0;
    private static final int CALL_TYPE_PROVIDER = 1;
    private static final int CALL_TYPE_PROVIDER_WITH_EVENT = 2;
    static ClassesInfoCache sInstance;
    private final Map<Class<?>, CallbackInfo> mCallbackMap;
    private final Map<Class<?>, Boolean> mHasLifecycleMethods;

    static class CallbackInfo {
        final Map<Lifecycle.Event, List<MethodReference>> mEventToHandlers;
        final Map<MethodReference, Lifecycle.Event> mHandlerToEvent;

        CallbackInfo(Map<MethodReference, Lifecycle.Event> r6) {
            this.mHandlerToEvent = r6;
            this.mEventToHandlers = new HashMap();
            Iterator<Map.Entry<MethodReference, Lifecycle.Event>> r0 = r6.entrySet().iterator();
        L4:
            if (r0.hasNext() == false) goto L9;
            Map.Entry<MethodReference, Lifecycle.Event> r1 = r0.next();
            Lifecycle.Event r2 = r1.getValue();
            List<MethodReference> r3 = this.mEventToHandlers.get(r2);
            if (r3 != null) goto L8;
            r3 = new ArrayList();
            this.mEventToHandlers.put(r2, r3);
        L8:
            r3.add(r1.getKey());
            goto L4
        }

        private static void invokeMethodsForEvent(List<MethodReference> r2, LifecycleOwner r3, Lifecycle.Event r4, Object r5) {
            if (r2 == null) goto L6;
            int r0 = r2.size() - 1;
        L4:
            if (r0 < 0) goto L8;
            r2.get(r0).invokeCallback(r3, r4, r5);
            r0 = r0 - 1;
            goto L4
        L8:
            return;
        }

        void invokeCallbacks(LifecycleOwner r3, Lifecycle.Event r4, Object r5) {
            invokeMethodsForEvent(this.mEventToHandlers.get(r4), r3, r4, r5);
            invokeMethodsForEvent(this.mEventToHandlers.get(Lifecycle.Event.ON_ANY), r3, r4, r5);
        }
    }

    static final class MethodReference {
        final int mCallType;
        final Method mMethod;

        MethodReference(int r3, Method r4) {
            this.mCallType = r3;
            this.mMethod = r4;
            this.mMethod.setAccessible(true);
        }

        public boolean equals(Object r6) {
            if (this != r6) goto L6;
            return true;
        L6:
            if ((r6 instanceof MethodReference) == true) goto L8;
            return false;
        L8:
            MethodReference r1 = (MethodReference) r6;
            if (this.mCallType != r1.mCallType) goto L14;
            if (this.mMethod.getName().equals(r1.mMethod.getName()) == false) goto L14;
            return true;
        L14:
            return false;
        }

        public int hashCode() {
            return (this.mCallType * 31) + this.mMethod.getName().hashCode();
        }

        void invokeCallback(LifecycleOwner r5, Lifecycle.Event r6, Object r7) {
        L10:
            e = move-exception;
            throw new RuntimeException(e);
        L13:
            e = move-exception;
            throw new RuntimeException("Failed to call observer method", e.getCause());
        L3:
            switch(this.mCallType) {
                case 0: goto L7;
                case 1: goto L6;
                case 2: goto L5;
                default: goto L17;
            };     // Catch: IllegalAccessException -> L10 InvocationTargetException -> L13
        L5:
            this.mMethod.invoke(r7, new Object[]{r5, r6});     // Catch: IllegalAccessException -> L10 InvocationTargetException -> L13
            return;
        L6:
            this.mMethod.invoke(r7, new Object[]{r5});     // Catch: IllegalAccessException -> L10 InvocationTargetException -> L13
            return;
        L7:
            this.mMethod.invoke(r7, new Object[0]);     // Catch: IllegalAccessException -> L10 InvocationTargetException -> L13
            return;
        }
    }

    static {
        sInstance = new ClassesInfoCache();
    }

    ClassesInfoCache() {
        this.mCallbackMap = new HashMap();
        this.mHasLifecycleMethods = new HashMap();
    }

    private CallbackInfo createInfo(Class<?> r17, Method[] r18) {
        Class<? super Object> r2 = r17.getSuperclass();
        HashMap r3 = new HashMap();
        if (r2 == null) goto L7;
        CallbackInfo r4 = getInfo(r2);
        if (r4 == null) goto L7;
        r3.putAll(r4.mHandlerToEvent);
    L7:
        Class<?>[] r5 = r17.getInterfaces();
        int r6 = r5.length;
        char r7 = 0;
        int r8 = 0;
    L8:
        if (r8 >= r6) goto L14;
        Iterator<Map.Entry<MethodReference, Lifecycle.Event>> r9 = getInfo(r5[r8]).mHandlerToEvent.entrySet().iterator();
    L11:
        if (r9.hasNext() == false) goto L13;
        Map.Entry<MethodReference, Lifecycle.Event> r10 = r9.next();
        verifyAndPutHandler(r3, r10.getKey(), r10.getValue(), r17);
        goto L11
    L13:
        r8 = r8 + 1;
        goto L8
    L14:
        if (r18 == null) goto L16;
        Method[] r11 = r18;
    L17:
        boolean r12 = false;
        int r13 = r11.length;
        int r14 = 0;
    L18:
        if (r14 >= r13) goto L46;
        Method r15 = r11[r14];
        OnLifecycleEvent r16 = (OnLifecycleEvent) r15.getAnnotation(OnLifecycleEvent.class);
        if (r16 == null) goto L43;
        r12 = true;
        Class<?>[] r19 = r15.getParameterTypes();
        int r110 = 0;
        if (r19.length <= 0) goto L29;
        r110 = 1;
        if (r19[r7].isAssignableFrom(LifecycleOwner.class) == true) goto L29;
        throw new IllegalArgumentException("invalid parameter type. Must be one and instanceof LifecycleOwner");
    L29:
        Lifecycle.Event r111 = r16.value();
        if (r19.length <= 1) goto L41;
        r110 = 2;
        if (r19[1].isAssignableFrom(Lifecycle.Event.class) == false) goto L39;
        if (r111 == Lifecycle.Event.ON_ANY) goto L41;
        throw new IllegalArgumentException("Second arg is supported only for ON_ANY value");
    L39:
        throw new IllegalArgumentException("invalid parameter type. second arg must be an event");
    L41:
        if (r19.length > 2) goto L45;
        verifyAndPutHandler(r3, new MethodReference(r110, r15), r111, r17);
        goto L43
    L45:
        throw new IllegalArgumentException("cannot have more than 2 params");
    L43:
        r14 = r14 + 1;
        r7 = 0;
        goto L18
    L46:
        CallbackInfo r20 = new CallbackInfo(r3);
        this.mCallbackMap.put(r17, r20);
        this.mHasLifecycleMethods.put(r17, Boolean.valueOf(r12));
        return r20;
    L16:
        r11 = getDeclaredMethods(r17);
        goto L17
    }

    private Method[] getDeclaredMethods(Class<?> r4) {
        return r4.getDeclaredMethods();
    L4:
        e = move-exception;
        throw new IllegalArgumentException("The observer class has some methods that use newer APIs which are not available in the current OS version. Lifecycles cannot access even other methods so you should make sure that your observer classes only access framework classes that are available in your min API level OR use lifecycle:compiler annotation processor.", e);
    }

    private void verifyAndPutHandler(Map<MethodReference, Lifecycle.Event> r6, MethodReference r7, Lifecycle.Event r8, Class<?> r9) {
        Lifecycle.Event r0 = r6.get(r7);
        if (r0 == null) goto L8;
        if (r8 == r0) goto L8;
        throw new IllegalArgumentException("Method " + r7.mMethod.getName() + " in " + r9.getName() + " already declared with different @OnLifecycleEvent value: previous value " + r0 + ", new value " + r8);
    L8:
        if (r0 != null) goto L11;
        r6.put(r7, r8);
        return;
    }

    CallbackInfo getInfo(Class<?> r3) {
        CallbackInfo r0 = this.mCallbackMap.get(r3);
        if (r0 == null) goto L6;
        return r0;
    L6:
        return createInfo(r3, null);
    }

    boolean hasLifecycleMethods(Class<?> r8) {
        Boolean r0 = this.mHasLifecycleMethods.get(r8);
        if (r0 != null) goto L5;
        Method[] r1 = getDeclaredMethods(r8);
        int r2 = r1.length;
        int r4 = 0;
    L7:
        if (r4 >= r2) goto L13;
        if (((OnLifecycleEvent) r1[r4].getAnnotation(OnLifecycleEvent.class)) != null) goto L10;
        r4 = r4 + 1;
        goto L7
    L10:
        createInfo(r8, r1);
        return true;
    L13:
        this.mHasLifecycleMethods.put(r8, false);
        return false;
    L5:
        return r0.booleanValue();
    }
}
