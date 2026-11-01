package androidx.core.os;

import android.os.Build;
import android.os.UserHandle;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
public class UserHandleCompat {
    private static Method sGetUserIdMethod;
    private static Constructor<UserHandle> sUserHandleConstructor;

    private static class Api24Impl {
        private Api24Impl() {
        }

        static UserHandle getUserHandleForUid(int r1) {
            return UserHandle.getUserHandleForUid(r1);
        }
    }

    private UserHandleCompat() {
    }

    private static Method getGetUserIdMethod() throws NoSuchMethodException {
        if (sGetUserIdMethod != null) goto L6;
        sGetUserIdMethod = UserHandle.class.getDeclaredMethod("getUserId", new Class[]{Integer.TYPE});
        sGetUserIdMethod.setAccessible(true);
    L6:
        return sGetUserIdMethod;
    }

    private static Constructor<UserHandle> getUserHandleConstructor() throws NoSuchMethodException {
        if (sUserHandleConstructor != null) goto L6;
        sUserHandleConstructor = UserHandle.class.getDeclaredConstructor(new Class[]{Integer.TYPE});
        sUserHandleConstructor.setAccessible(true);
    L6:
        return sUserHandleConstructor;
    }

    public static UserHandle getUserHandleForUid(int r5) {
        if (Build.VERSION.SDK_INT >= 24) goto L5;
        Integer r0 = (Integer) getGetUserIdMethod().invoke(null, new Object[]{Integer.valueOf(r5)});     // Catch: InvocationTargetException -> L8 InstantiationException -> L11 IllegalAccessException -> L14 NoSuchMethodException -> L17
        return getUserHandleConstructor().newInstance(new Object[]{r0});
    L14:
        e = move-exception;
        IllegalAccessError r1 = new IllegalAccessError();
        r1.initCause(e);
        throw r1;
    L11:
        e = move-exception;
        InstantiationError r2 = new InstantiationError();
        r2.initCause(e);
        throw r2;
    L17:
        e = move-exception;
        NoSuchMethodError r3 = new NoSuchMethodError();
        r3.initCause(e);
        throw r3;
    L8:
        e = move-exception;
        throw new RuntimeException(e);
    L5:
        return Api24Impl.getUserHandleForUid(r5);
    }
}
