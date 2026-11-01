package com.arthenica.ffmpegkit;

/* JADX INFO: loaded from: classes.dex */
public final enum Level extends Enum<Level> {
    private static final /* synthetic */ Level[] $VALUES = null;
    public static final Level AV_LOG_DEBUG = null;
    public static final Level AV_LOG_ERROR = null;
    public static final Level AV_LOG_FATAL = null;
    public static final Level AV_LOG_INFO = null;
    public static final Level AV_LOG_PANIC = null;
    public static final Level AV_LOG_QUIET = null;
    public static final Level AV_LOG_STDERR = null;
    public static final Level AV_LOG_TRACE = null;
    public static final Level AV_LOG_VERBOSE = null;
    public static final Level AV_LOG_WARNING = null;
    private final int value;

    private static /* synthetic */ Level[] $values() {
        return new Level[]{AV_LOG_STDERR, AV_LOG_QUIET, AV_LOG_PANIC, AV_LOG_FATAL, AV_LOG_ERROR, AV_LOG_WARNING, AV_LOG_INFO, AV_LOG_VERBOSE, AV_LOG_DEBUG, AV_LOG_TRACE};
    }

    static {
        AV_LOG_STDERR = new Level("AV_LOG_STDERR", 0, -16);
        AV_LOG_QUIET = new Level("AV_LOG_QUIET", 1, -8);
        AV_LOG_PANIC = new Level("AV_LOG_PANIC", 2, 0);
        AV_LOG_FATAL = new Level("AV_LOG_FATAL", 3, 8);
        AV_LOG_ERROR = new Level("AV_LOG_ERROR", 4, 16);
        AV_LOG_WARNING = new Level("AV_LOG_WARNING", 5, 24);
        AV_LOG_INFO = new Level("AV_LOG_INFO", 6, 32);
        AV_LOG_VERBOSE = new Level("AV_LOG_VERBOSE", 7, 40);
        AV_LOG_DEBUG = new Level("AV_LOG_DEBUG", 8, 48);
        AV_LOG_TRACE = new Level("AV_LOG_TRACE", 9, 56);
        $VALUES = $values();
    }

    private Level(String r1, int r2, int r3) {
        super(r1, r2);
        this.value = r3;
    }

    public static Level from(int r1) {
        if (r1 != AV_LOG_STDERR.getValue()) goto L7;
        return AV_LOG_STDERR;
    L7:
        if (r1 != AV_LOG_QUIET.getValue()) goto L11;
        return AV_LOG_QUIET;
    L11:
        if (r1 != AV_LOG_PANIC.getValue()) goto L15;
        return AV_LOG_PANIC;
    L15:
        if (r1 != AV_LOG_FATAL.getValue()) goto L19;
        return AV_LOG_FATAL;
    L19:
        if (r1 != AV_LOG_ERROR.getValue()) goto L23;
        return AV_LOG_ERROR;
    L23:
        if (r1 != AV_LOG_WARNING.getValue()) goto L27;
        return AV_LOG_WARNING;
    L27:
        if (r1 != AV_LOG_INFO.getValue()) goto L31;
        return AV_LOG_INFO;
    L31:
        if (r1 != AV_LOG_VERBOSE.getValue()) goto L35;
        return AV_LOG_VERBOSE;
    L35:
        if (r1 != AV_LOG_DEBUG.getValue()) goto L39;
        return AV_LOG_DEBUG;
    L39:
        return AV_LOG_TRACE;
    }

    public static Level valueOf(String r1) {
        return (Level) Enum.valueOf(Level.class, r1);
    }

    public static Level[] values() {
        return (Level[]) $VALUES.clone();
    }

    public int getValue() {
        return this.value;
    }
}
