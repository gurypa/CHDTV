package com.arthenica.ffmpegkit;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

/* JADX INFO: loaded from: classes.dex */
public interface Session {
    void addLog(Log r1);

    void cancel();

    List<Log> getAllLogs();

    List<Log> getAllLogs(int r1);

    String getAllLogsAsString();

    String getAllLogsAsString(int r1);

    String[] getArguments();

    String getCommand();

    Date getCreateTime();

    long getDuration();

    Date getEndTime();

    String getFailStackTrace();

    Future<?> getFuture();

    LogCallback getLogCallback();

    LogRedirectionStrategy getLogRedirectionStrategy();

    List<Log> getLogs();

    String getLogsAsString();

    String getOutput();

    ReturnCode getReturnCode();

    long getSessionId();

    Date getStartTime();

    SessionState getState();

    boolean isFFmpeg();

    boolean isFFprobe();

    boolean isMediaInformation();

    boolean thereAreAsynchronousMessagesInTransmit();
}
