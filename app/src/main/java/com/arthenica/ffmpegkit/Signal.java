package com.arthenica.ffmpegkit;

/* JADX INFO: loaded from: classes.dex */
public final enum Signal extends Enum<Signal> {
    private static final /* synthetic */ Signal[] $VALUES = null;
    public static final Signal SIGINT = null;
    public static final Signal SIGPIPE = null;
    public static final Signal SIGQUIT = null;
    public static final Signal SIGTERM = null;
    public static final Signal SIGXCPU = null;
    private final int value;

    private static /* synthetic */ Signal[] $values() {
        return new Signal[]{SIGINT, SIGQUIT, SIGPIPE, SIGTERM, SIGXCPU};
    }

    static {
        SIGINT = new Signal("SIGINT", 0, 2);
        SIGQUIT = new Signal("SIGQUIT", 1, 3);
        SIGPIPE = new Signal("SIGPIPE", 2, 13);
        SIGTERM = new Signal("SIGTERM", 3, 15);
        SIGXCPU = new Signal("SIGXCPU", 4, 24);
        $VALUES = $values();
    }

    private Signal(String r1, int r2, int r3) {
        super(r1, r2);
        this.value = r3;
    }

    public static Signal valueOf(String r1) {
        return (Signal) Enum.valueOf(Signal.class, r1);
    }

    public static Signal[] values() {
        return (Signal[]) $VALUES.clone();
    }

    public int getValue() {
        return this.value;
    }
}
