package com.arthenica.ffmpegkit;

/* JADX INFO: loaded from: classes.dex */
public final enum SessionState extends Enum<SessionState> {
    private static final /* synthetic */ SessionState[] $VALUES = null;
    public static final SessionState COMPLETED = null;
    public static final SessionState CREATED = null;
    public static final SessionState FAILED = null;
    public static final SessionState RUNNING = null;

    private static /* synthetic */ SessionState[] $values() {
        return new SessionState[]{CREATED, RUNNING, FAILED, COMPLETED};
    }

    static {
        CREATED = new SessionState("CREATED", 0);
        RUNNING = new SessionState("RUNNING", 1);
        FAILED = new SessionState("FAILED", 2);
        COMPLETED = new SessionState("COMPLETED", 3);
        $VALUES = $values();
    }

    private SessionState(String r1, int r2) {
        super(r1, r2);
    }

    public static SessionState valueOf(String r1) {
        return (SessionState) Enum.valueOf(SessionState.class, r1);
    }

    public static SessionState[] values() {
        return (SessionState[]) $VALUES.clone();
    }
}
