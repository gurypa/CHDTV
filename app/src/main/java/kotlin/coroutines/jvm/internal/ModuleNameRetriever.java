package kotlin.coroutines.jvm.internal;

import java.lang.reflect.Method;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: DebugMetadata.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\bÂ\u0002\u0018\u00002\u00020\u0001:\u0001\u000bB\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\bH\u0002J\u0010\u0010\t\u001a\u0004\u0018\u00010\n2\u0006\u0010\u0007\u001a\u00020\bR\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\f"}, m421d2 = {"Lkotlin/coroutines/jvm/internal/ModuleNameRetriever;", "", "()V", "cache", "Lkotlin/coroutines/jvm/internal/ModuleNameRetriever$Cache;", "notOnJava9", "buildCache", "continuation", "Lkotlin/coroutines/jvm/internal/BaseContinuationImpl;", "getModuleName", "", "Cache", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
final class ModuleNameRetriever {
    public static final ModuleNameRetriever INSTANCE = null;
    private static Cache cache;
    private static final Cache notOnJava9 = null;

    /* JADX INFO: compiled from: DebugMetadata.kt */
    @Metadata(m420d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0002\u0018\u00002\u00020\u0001B#\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\u0006R\u0012\u0010\u0004\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u0005\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004¢\u0006\u0002\n\u0000¨\u0006\u0007"}, m421d2 = {"Lkotlin/coroutines/jvm/internal/ModuleNameRetriever$Cache;", "", "getModuleMethod", "Ljava/lang/reflect/Method;", "getDescriptorMethod", "nameMethod", "(Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;)V", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
    private static final class Cache {
        public final Method getDescriptorMethod;
        public final Method getModuleMethod;
        public final Method nameMethod;

        public Cache(Method r1, Method r2, Method r3) {
            this.getModuleMethod = r1;
            this.getDescriptorMethod = r2;
            this.nameMethod = r3;
        }
    }

    static {
        INSTANCE = new ModuleNameRetriever();
        notOnJava9 = new Cache(null, null, null);
    }

    private ModuleNameRetriever() {
    }

    private final Cache buildCache(BaseContinuationImpl r9) {
        Cache r5 = new Cache(Class.class.getDeclaredMethod("getModule", new Class[0]), r9.getClass().getClassLoader().loadClass("java.lang.Module").getDeclaredMethod("getDescriptor", new Class[0]), r9.getClass().getClassLoader().loadClass("java.lang.module.ModuleDescriptor").getDeclaredMethod("name", new Class[0]));     // Catch: Exception -> L4
        cache = r5;     // Catch: Exception -> L4
        return r5;
    L5:
        Cache r1 = notOnJava9;
        cache = r1;
        return r1;
    }

    public final String getModuleName(BaseContinuationImpl r7) {
        Intrinsics.checkNotNullParameter(r7, "continuation");
        Cache r0 = cache;
        if (r0 != null) goto L6;
        r0 = buildCache(r7);
    L6:
        if (r0 != notOnJava9) goto L8;
        return null;
    L8:
        Method r1 = r0.getModuleMethod;
        if (r1 == null) goto L11;
        Object r2 = r1.invoke(r7.getClass(), new Object[0]);
    L12:
        if (r2 != null) goto L14;
        return null;
    L14:
        Method r4 = r0.getDescriptorMethod;
        if (r4 == null) goto L17;
        Object r5 = r4.invoke(r2, new Object[0]);
    L18:
        if (r5 != null) goto L20;
        return null;
    L20:
        Method r6 = r0.nameMethod;
        if (r6 == null) goto L23;
        Object r3 = r6.invoke(r5, new Object[0]);
    L25:
        if ((r3 instanceof String) == true) goto L27;
        return null;
    L27:
        return (String) r3;
    L23:
        r3 = null;
        goto L25
    L17:
        r5 = null;
        goto L18
    L11:
        r2 = null;
        goto L12
    }
}
