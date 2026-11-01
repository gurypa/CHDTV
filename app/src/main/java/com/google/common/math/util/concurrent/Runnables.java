package com.google.common.util.concurrent;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class Runnables {
    private static final Runnable EMPTY_RUNNABLE = null;

    static {
        EMPTY_RUNNABLE = new RunnableC09561();
    }

    private Runnables() {
    }

    public static Runnable doNothing() {
        return EMPTY_RUNNABLE;
    }
}
