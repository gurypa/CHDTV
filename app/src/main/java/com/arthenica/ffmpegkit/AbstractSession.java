package com.arthenica.ffmpegkit;

import com.arthenica.smartexception.java.Exceptions;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

/* JADX INFO: loaded from: classes.dex */
public abstract class AbstractSession implements Session {
    public static final int DEFAULT_TIMEOUT_FOR_ASYNCHRONOUS_MESSAGES_IN_TRANSMIT = 5000;
    protected static final AtomicLong sessionIdGenerator = null;
    protected final String[] arguments;
    protected final Date createTime;
    protected Date endTime;
    protected String failStackTrace;
    protected Future<?> future;
    protected final LogCallback logCallback;
    protected final LogRedirectionStrategy logRedirectionStrategy;
    protected final List<Log> logs;
    protected final Object logsLock;
    protected ReturnCode returnCode;
    protected final long sessionId;
    protected Date startTime;
    protected SessionState state;

    static {
        sessionIdGenerator = new AtomicLong(1);
    }

    protected AbstractSession(String[] r3, LogCallback r4, LogRedirectionStrategy r5) {
        this.sessionId = sessionIdGenerator.getAndIncrement();
        this.logCallback = r4;
        this.createTime = new Date();
        this.startTime = null;
        this.endTime = null;
        this.arguments = r3;
        this.logs = new LinkedList();
        this.logsLock = new Object();
        this.future = null;
        this.state = SessionState.CREATED;
        this.returnCode = null;
        this.failStackTrace = null;
        this.logRedirectionStrategy = r5;
        FFmpegKitConfig.addSession(this);
    }

    @Override // com.arthenica.ffmpegkit.Session
    public void addLog(Log r3) {
        Object r0 = this.logsLock;
        monitor-enter(r0);
        this.logs.add(r3);     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return;
    L7:
        th = move-exception;
        throw th;
    }

    @Override // com.arthenica.ffmpegkit.Session
    public void cancel() {
        if (this.state != SessionState.RUNNING) goto L6;
        FFmpegKit.cancel(this.sessionId);
        return;
    }

    void complete(ReturnCode r2) {
        this.returnCode = r2;
        this.state = SessionState.COMPLETED;
        this.endTime = new Date();
    }

    void fail(Exception r2) {
        this.failStackTrace = Exceptions.getStackTraceString(r2);
        this.state = SessionState.FAILED;
        this.endTime = new Date();
    }

    @Override // com.arthenica.ffmpegkit.Session
    public List<Log> getAllLogs() {
        return getAllLogs(5000);
    }

    @Override // com.arthenica.ffmpegkit.Session
    public List<Log> getAllLogs(int r4) {
        waitForAsynchronousMessagesInTransmit(r4);
        if (thereAreAsynchronousMessagesInTransmit() == false) goto L6;
        android.util.Log.i("ffmpeg-kit", String.format("getAllLogs was called to return all logs but there are still logs being transmitted for session id %d.", new Object[]{Long.valueOf(this.sessionId)}));
    L6:
        return getLogs();
    }

    @Override // com.arthenica.ffmpegkit.Session
    public String getAllLogsAsString() {
        return getAllLogsAsString(5000);
    }

    @Override // com.arthenica.ffmpegkit.Session
    public String getAllLogsAsString(int r4) {
        waitForAsynchronousMessagesInTransmit(r4);
        if (thereAreAsynchronousMessagesInTransmit() == false) goto L6;
        android.util.Log.i("ffmpeg-kit", String.format("getAllLogsAsString was called to return all logs but there are still logs being transmitted for session id %d.", new Object[]{Long.valueOf(this.sessionId)}));
    L6:
        return getLogsAsString();
    }

    @Override // com.arthenica.ffmpegkit.Session
    public String[] getArguments() {
        return this.arguments;
    }

    @Override // com.arthenica.ffmpegkit.Session
    public String getCommand() {
        return FFmpegKitConfig.argumentsToString(this.arguments);
    }

    @Override // com.arthenica.ffmpegkit.Session
    public Date getCreateTime() {
        return this.createTime;
    }

    @Override // com.arthenica.ffmpegkit.Session
    public long getDuration() {
        Date r0 = this.startTime;
        Date r1 = this.endTime;
        if (r0 == null) goto L7;
        if (r1 != null) goto L6;
        return 0;
    L6:
        return r1.getTime() - r0.getTime();
    L7:
        return 0;
    }

    @Override // com.arthenica.ffmpegkit.Session
    public Date getEndTime() {
        return this.endTime;
    }

    @Override // com.arthenica.ffmpegkit.Session
    public String getFailStackTrace() {
        return this.failStackTrace;
    }

    @Override // com.arthenica.ffmpegkit.Session
    public Future<?> getFuture() {
        return this.future;
    }

    @Override // com.arthenica.ffmpegkit.Session
    public LogCallback getLogCallback() {
        return this.logCallback;
    }

    @Override // com.arthenica.ffmpegkit.Session
    public LogRedirectionStrategy getLogRedirectionStrategy() {
        return this.logRedirectionStrategy;
    }

    @Override // com.arthenica.ffmpegkit.Session
    public List<Log> getLogs() {
        Object r0 = this.logsLock;
        monitor-enter(r0);
        LinkedList r1 = new LinkedList(this.logs);     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return r1;
    L7:
        th = move-exception;
        throw th;
    }

    @Override // com.arthenica.ffmpegkit.Session
    public String getLogsAsString() {
        StringBuilder r0 = new StringBuilder();
        Object r1 = this.logsLock;
        monitor-enter(r1);
        Iterator<Log> r2 = this.logs.iterator();     // Catch: Throwable -> L11
    L6:
        if (r2.hasNext() == false) goto L8;
        r0.append(r2.next().getMessage());     // Catch: Throwable -> L11
        goto L6
    L8:
        monitor-exit(r1);     // Catch: Throwable -> L11
        return r0.toString();
    L11:
        th = move-exception;
        throw th;
    }

    @Override // com.arthenica.ffmpegkit.Session
    public String getOutput() {
        return getAllLogsAsString();
    }

    @Override // com.arthenica.ffmpegkit.Session
    public ReturnCode getReturnCode() {
        return this.returnCode;
    }

    @Override // com.arthenica.ffmpegkit.Session
    public long getSessionId() {
        return this.sessionId;
    }

    @Override // com.arthenica.ffmpegkit.Session
    public Date getStartTime() {
        return this.startTime;
    }

    @Override // com.arthenica.ffmpegkit.Session
    public SessionState getState() {
        return this.state;
    }

    void setFuture(Future<?> r1) {
        this.future = r1;
    }

    void startRunning() {
        this.state = SessionState.RUNNING;
        this.startTime = new Date();
    }

    @Override // com.arthenica.ffmpegkit.Session
    public boolean thereAreAsynchronousMessagesInTransmit() {
        if (FFmpegKitConfig.messagesInTransmit(this.sessionId) == 0) goto L5;
        return true;
    L5:
        return false;
    }

    protected void waitForAsynchronousMessagesInTransmit(int r7) {
        long r0 = System.currentTimeMillis();
    L4:
        if (thereAreAsynchronousMessagesInTransmit() == false) goto L18;
        if (System.currentTimeMillis() >= (((long) r7) + r0)) goto L23;
        monitor-enter(this);
        wait(100);     // Catch: Throwable -> L11 InterruptedException -> L13
    L11:
        th = move-exception;
        throw th;
    L14:
        monitor-exit(this);     // Catch: Throwable -> L11
        goto L4
    L23:
        return;
    }
}
