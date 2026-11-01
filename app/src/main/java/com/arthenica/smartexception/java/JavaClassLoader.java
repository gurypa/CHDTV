package com.arthenica.smartexception.java;

import com.arthenica.smartexception.ClassLoader;

/* JADX INFO: loaded from: classes.dex */
public class JavaClassLoader implements ClassLoader {
    public JavaClassLoader() {
    }

    @Override // com.arthenica.smartexception.ClassLoader
    public Class<?> loadClass(String r3) {
        ClassLoader r0 = Thread.currentThread().getContextClassLoader();     // Catch: SecurityException -> L7 ClassNotFoundException -> L9
        if (r0 == null) goto L33;
        return r0.loadClass(r3);
    L33:
        ClassLoader r1 = ClassLoader.getSystemClassLoader();     // Catch: Error -> L15 IllegalStateException -> L17 SecurityException -> L19 ClassNotFoundException -> L21
        if (r1 == null) goto L31;
        return r1.loadClass(r3);
    L31:
        return Class.forName(r3);
    L27:
        return null;
    }
}
