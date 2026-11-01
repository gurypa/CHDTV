package com.arthenica.ffmpegkit;

/* JADX INFO: loaded from: classes.dex */
public final enum LogRedirectionStrategy extends Enum<LogRedirectionStrategy> {
    private static final /* synthetic */ LogRedirectionStrategy[] $VALUES = null;
    public static final LogRedirectionStrategy ALWAYS_PRINT_LOGS = null;
    public static final LogRedirectionStrategy NEVER_PRINT_LOGS = null;
    public static final LogRedirectionStrategy PRINT_LOGS_WHEN_GLOBAL_CALLBACK_NOT_DEFINED = null;
    public static final LogRedirectionStrategy PRINT_LOGS_WHEN_NO_CALLBACKS_DEFINED = null;
    public static final LogRedirectionStrategy PRINT_LOGS_WHEN_SESSION_CALLBACK_NOT_DEFINED = null;

    private static /* synthetic */ LogRedirectionStrategy[] $values() {
        return new LogRedirectionStrategy[]{ALWAYS_PRINT_LOGS, PRINT_LOGS_WHEN_NO_CALLBACKS_DEFINED, PRINT_LOGS_WHEN_GLOBAL_CALLBACK_NOT_DEFINED, PRINT_LOGS_WHEN_SESSION_CALLBACK_NOT_DEFINED, NEVER_PRINT_LOGS};
    }

    static {
        ALWAYS_PRINT_LOGS = new LogRedirectionStrategy("ALWAYS_PRINT_LOGS", 0);
        PRINT_LOGS_WHEN_NO_CALLBACKS_DEFINED = new LogRedirectionStrategy("PRINT_LOGS_WHEN_NO_CALLBACKS_DEFINED", 1);
        PRINT_LOGS_WHEN_GLOBAL_CALLBACK_NOT_DEFINED = new LogRedirectionStrategy("PRINT_LOGS_WHEN_GLOBAL_CALLBACK_NOT_DEFINED", 2);
        PRINT_LOGS_WHEN_SESSION_CALLBACK_NOT_DEFINED = new LogRedirectionStrategy("PRINT_LOGS_WHEN_SESSION_CALLBACK_NOT_DEFINED", 3);
        NEVER_PRINT_LOGS = new LogRedirectionStrategy("NEVER_PRINT_LOGS", 4);
        $VALUES = $values();
    }

    private LogRedirectionStrategy(String r1, int r2) {
        super(r1, r2);
    }

    public static LogRedirectionStrategy valueOf(String r1) {
        return (LogRedirectionStrategy) Enum.valueOf(LogRedirectionStrategy.class, r1);
    }

    public static LogRedirectionStrategy[] values() {
        return (LogRedirectionStrategy[]) $VALUES.clone();
    }
}
