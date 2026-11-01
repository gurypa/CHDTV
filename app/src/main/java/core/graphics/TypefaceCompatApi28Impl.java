package androidx.core.graphics;

import android.content.Context;
import android.graphics.Typeface;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
public class TypefaceCompatApi28Impl extends TypefaceCompatApi26Impl {
    private static final String CREATE_FROM_FAMILIES_WITH_DEFAULT_METHOD = "createFromFamiliesWithDefault";
    private static final String DEFAULT_FAMILY = "sans-serif";
    private static final int RESOLVE_BY_FONT_TABLE = -1;

    public TypefaceCompatApi28Impl() {
    }

    @Override // androidx.core.graphics.TypefaceCompatApi26Impl
    protected Typeface createFromFamiliesWithDefault(Object r8) {
        Object r0 = Array.newInstance(this.mFontFamily, 1);     // Catch: InvocationTargetException -> L4 Throwable -> L6
        Array.set(r0, 0, r8);     // Catch: InvocationTargetException -> L4 Throwable -> L6
        return (Typeface) this.mCreateFromFamiliesWithDefault.invoke(null, new Object[]{r0, "sans-serif", -1, -1});
    L6:
        e = move-exception;
        throw new RuntimeException(e);
    }

    @Override // androidx.core.graphics.TypefaceCompatApi26Impl, androidx.core.graphics.TypefaceCompatApi21Impl, androidx.core.graphics.TypefaceCompatBaseImpl
    Typeface createWeightStyle(Context r2, Typeface r3, int r4, boolean r5) {
        return Typeface.create(r3, r4, r5);
    }

    @Override // androidx.core.graphics.TypefaceCompatApi26Impl
    protected Method obtainCreateFromFamiliesWithDefaultMethod(Class<?> r7) throws NoSuchMethodException {
        Class r3 = Integer.TYPE;
        Method r2 = Typeface.class.getDeclaredMethod(CREATE_FROM_FAMILIES_WITH_DEFAULT_METHOD, new Class[]{Array.newInstance(r7, 1).getClass(), String.class, r3, r3});
        r2.setAccessible(true);
        return r2;
    }
}
