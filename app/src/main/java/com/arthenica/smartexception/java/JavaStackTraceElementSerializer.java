package com.arthenica.smartexception.java;

import com.arthenica.smartexception.AbstractExceptions;
import com.arthenica.smartexception.StackTraceElementSerializer;

/* JADX INFO: loaded from: classes.dex */
public class JavaStackTraceElementSerializer implements StackTraceElementSerializer {
    public JavaStackTraceElementSerializer() {
    }

    @Override // com.arthenica.smartexception.StackTraceElementSerializer
    public String getModuleName(StackTraceElement r2) {
        return "";
    }

    @Override // com.arthenica.smartexception.StackTraceElementSerializer
    public String getNativeMethodDefinition() {
        return "(Native Method)";
    }

    @Override // com.arthenica.smartexception.StackTraceElementSerializer
    public String getPackageInformation(StackTraceElement r7) {
        StringBuilder r0 = new StringBuilder();
        String r1 = r7.getClassName();
        Class<?> r2 = Exceptions.classLoader.loadClass(r1);
        if (r2 == null) goto L6;
        r0.append(AbstractExceptions.packageInformation(AbstractExceptions.libraryName(r2), AbstractExceptions.version(Exceptions.packageLoader, r2, AbstractExceptions.packageName(r1))));
    L6:
        return r0.toString();
    }

    @Override // com.arthenica.smartexception.StackTraceElementSerializer
    public String getUnknownSourceDefinition() {
        return "(Unknown Source)";
    }

    @Override // com.arthenica.smartexception.StackTraceElementSerializer
    public String toString(StackTraceElement r3, boolean r4, boolean r5) {
        StringBuilder r0 = new StringBuilder();
        r0.append(r3.getClassName());
        r0.append(".");
        r0.append(r3.getMethodName());
        if (r3.isNativeMethod() == false) goto L6;
        r0.append(getNativeMethodDefinition());
    L14:
        if (r5 == false) goto L17;
        r0.append(getPackageInformation(r3));
    L17:
        return r0.toString();
    L6:
        if (r3.getFileName() != null) goto L8;
    L13:
        r0.append(getUnknownSourceDefinition());
        goto L14
    L8:
        if (r3.getFileName().length() <= 0) goto L13;
        r0.append("(");
        r0.append(r3.getFileName());
        if (r3.getLineNumber() < 0) goto L12;
        r0.append(":");
        r0.append(r3.getLineNumber());
    L12:
        r0.append(")");
        goto L14
    }
}
