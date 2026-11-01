package com.arthenica.smartexception;

/* JADX INFO: loaded from: classes.dex */
public interface StackTraceElementSerializer {
    String getModuleName(StackTraceElement r1);

    String getNativeMethodDefinition();

    String getPackageInformation(StackTraceElement r1);

    String getUnknownSourceDefinition();

    String toString(StackTraceElement r1, boolean r2, boolean r3);
}
