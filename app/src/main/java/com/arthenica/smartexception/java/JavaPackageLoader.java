package com.arthenica.smartexception.java;

import com.arthenica.smartexception.AbstractExceptions;
import com.arthenica.smartexception.PackageLoader;

/* JADX INFO: loaded from: classes.dex */
public class JavaPackageLoader implements PackageLoader {
    public JavaPackageLoader() {
    }

    @Override // com.arthenica.smartexception.PackageLoader
    public Package getPackage(ClassLoader r2, String r3) {
        return Package.getPackage(AbstractExceptions.packageName(r3));
    }
}
