package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class ThreadFactoryBuilder {

    @CheckForNull
    private ThreadFactory backingThreadFactory;

    @CheckForNull
    private Boolean daemon;

    @CheckForNull
    private String nameFormat;

    @CheckForNull
    private Integer priority;

    @CheckForNull
    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

    public ThreadFactoryBuilder() {
        this.nameFormat = null;
        this.daemon = null;
        this.priority = null;
        this.uncaughtExceptionHandler = null;
        this.backingThreadFactory = null;
    }

    static /* synthetic */ String access$000(String r1, Object[] r2) {
        return format(r1, r2);
    }

    private static ThreadFactory doBuild(ThreadFactoryBuilder r9) {
        final String r2 = r9.nameFormat;
        final Boolean r4 = r9.daemon;
        final Integer r5 = r9.priority;
        final Thread.UncaughtExceptionHandler r6 = r9.uncaughtExceptionHandler;
        if (r9.backingThreadFactory == null) goto L5;
        final ThreadFactory r1 = r9.backingThreadFactory;
    L7:
        if (r2 == null) goto L9;
        AtomicLong r0 = new AtomicLong(0);
    L10:
        final AtomicLong r3 = r0;
        return new ThreadFactoryC09661(r1, r2, r3, r4, r5, r6);
    L9:
        r0 = null;
        goto L10
    L5:
        r1 = Executors.defaultThreadFactory();
        goto L7
    }

    private static String format(String r1, Object... r2) {
        return String.format(Locale.ROOT, r1, r2);
    }

    public ThreadFactory build() {
        return doBuild(this);
    }

    public ThreadFactoryBuilder setDaemon(boolean r2) {
        this.daemon = Boolean.valueOf(r2);
        return this;
    }

    public ThreadFactoryBuilder setNameFormat(String r4) {
        format(r4, new Object[]{0});
        this.nameFormat = r4;
        return this;
    }

    public ThreadFactoryBuilder setPriority(int r5) {
        boolean r0 = false;
        if (r5 < 1) goto L5;
        boolean r2 = true;
    L6:
        Preconditions.checkArgument(r2, "Thread priority (%s) must be >= %s", r5, 1);
        if (r5 > 10) goto L9;
        r0 = true;
    L9:
        Preconditions.checkArgument(r0, "Thread priority (%s) must be <= %s", r5, 10);
        this.priority = Integer.valueOf(r5);
        return this;
    L5:
        r2 = false;
        goto L6
    }

    public ThreadFactoryBuilder setThreadFactory(ThreadFactory r2) {
        this.backingThreadFactory = (ThreadFactory) Preconditions.checkNotNull(r2);
        return this;
    }

    public ThreadFactoryBuilder setUncaughtExceptionHandler(Thread.UncaughtExceptionHandler r2) {
        this.uncaughtExceptionHandler = (Thread.UncaughtExceptionHandler) Preconditions.checkNotNull(r2);
        return this;
    }
}
