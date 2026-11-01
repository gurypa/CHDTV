package com.arthenica.smartexception;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class ThrowableWrapper {
    private final ThrowableWrapper cause;
    private final String className;
    private final String message;
    private final StackTraceElementWrapper[] stackTrace;
    private final ThrowableWrapper[] suppressed;

    public ThrowableWrapper(String r1, ThrowableWrapper r2, String r3, ThrowableWrapper[] r4, StackTraceElementWrapper[] r5) {
        this.message = r1;
        this.cause = r2;
        this.className = r3;
        this.suppressed = r4;
        this.stackTrace = r5;
    }

    public ThrowableWrapper(Throwable r2) {
        this(r2, Collections.newSetFromMap(new IdentityHashMap()));
    }

    public ThrowableWrapper(Throwable r9, Set<Throwable> r10) {
        r10.add(r9);
        this.message = r9.getMessage();
        if (r9.getCause() != null) goto L5;
    L7:
        this.cause = null;
    L8:
        this.className = r9.getClass().getName();
        Throwable[] r0 = r9.getSuppressed();
        LinkedList r1 = new LinkedList();
        int r2 = 0;
        int r3 = r0.length;
    L9:
        if (r2 >= r3) goto L14;
        if (r10.contains(r0[r2]) == true) goto L13;
        r1.add(new ThrowableWrapper(r0[r2], r10));
    L13:
        r2 = r2 + 1;
        goto L9
    L14:
        this.suppressed = (ThrowableWrapper[]) r1.toArray(new ThrowableWrapper[0]);
        StackTraceElement[] r4 = r9.getStackTrace();
        this.stackTrace = new StackTraceElementWrapper[r4.length];
        int r5 = 0;
        int r6 = r4.length;
    L15:
        if (r5 >= r6) goto L17;
        this.stackTrace[r5] = new StackTraceElementWrapper(r4[r5]);
        r5 = r5 + 1;
        goto L15
    L17:
        return;
    L5:
        if (r10.contains(r9.getCause()) == true) goto L7;
        this.cause = new ThrowableWrapper(r9.getCause(), r10);
        goto L8
    }

    public ThrowableWrapper getCause() {
        return this.cause;
    }

    public String getClassName() {
        return this.className;
    }

    public String getMessage() {
        return this.message;
    }

    public StackTraceElementWrapper[] getStackTrace() {
        return this.stackTrace;
    }

    public ThrowableWrapper[] getSuppressed() {
        return this.suppressed;
    }
}
