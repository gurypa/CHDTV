package com.arthenica.ffmpegkit;

/* JADX INFO: loaded from: classes.dex */
public class Log {
    private final Level level;
    private final String message;
    private final long sessionId;

    public Log(long r1, Level r3, String r4) {
        this.sessionId = r1;
        this.level = r3;
        this.message = r4;
    }

    public Level getLevel() {
        return this.level;
    }

    public String getMessage() {
        return this.message;
    }

    public long getSessionId() {
        return this.sessionId;
    }

    public String toString() {
        return "Log{sessionId=" + this.sessionId + ", level=" + this.level + ", message='" + this.message + "'}";
    }
}
