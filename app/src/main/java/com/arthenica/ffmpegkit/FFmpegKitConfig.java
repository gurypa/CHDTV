package com.arthenica.ffmpegkit;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.util.SparseArray;
import androidx.core.os.EnvironmentCompat;
import com.arthenica.smartexception.java.Exceptions;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/* JADX INFO: loaded from: classes.dex */
public class FFmpegKitConfig {
    static final String FFMPEG_KIT_NAMED_PIPE_PREFIX = "fk_pipe_";
    static final String TAG = "ffmpeg-kit";
    private static Level activeLogLevel;
    private static int asyncConcurrencyLimit;
    private static ExecutorService asyncExecutorService;
    private static FFmpegSessionCompleteCallback globalFFmpegSessionCompleteCallback;
    private static FFprobeSessionCompleteCallback globalFFprobeSessionCompleteCallback;
    private static LogCallback globalLogCallback;
    private static LogRedirectionStrategy globalLogRedirectionStrategy;
    private static MediaInformationSessionCompleteCallback globalMediaInformationSessionCompleteCallback;
    private static StatisticsCallback globalStatisticsCallback;
    private static final SparseArray<SAFProtocolUrl> safFileDescriptorMap = null;
    private static final SparseArray<SAFProtocolUrl> safIdMap = null;
    private static final List<Session> sessionHistoryList = null;
    private static final Object sessionHistoryLock = null;
    private static final Map<Long, Session> sessionHistoryMap = null;
    private static int sessionHistorySize;
    private static final AtomicInteger uniqueIdGenerator = null;

    /* JADX INFO: renamed from: com.arthenica.ffmpegkit.FFmpegKitConfig$2 */
    static /* synthetic */ class C05262 {
        static final /* synthetic */ int[] $SwitchMap$com$arthenica$ffmpegkit$Level = null;
        static final /* synthetic */ int[] $SwitchMap$com$arthenica$ffmpegkit$LogRedirectionStrategy = null;

        static {
            $SwitchMap$com$arthenica$ffmpegkit$Level = new int[Level.values().length];
            $SwitchMap$com$arthenica$ffmpegkit$Level[Level.AV_LOG_QUIET.ordinal()] = 1;     // Catch: NoSuchFieldError -> L5
        L80:
            $SwitchMap$com$arthenica$ffmpegkit$Level[Level.AV_LOG_TRACE.ordinal()] = 2;     // Catch: NoSuchFieldError -> L9
        L58:
            $SwitchMap$com$arthenica$ffmpegkit$Level[Level.AV_LOG_DEBUG.ordinal()] = 3;     // Catch: NoSuchFieldError -> L13
        L70:
            $SwitchMap$com$arthenica$ffmpegkit$Level[Level.AV_LOG_INFO.ordinal()] = 4;     // Catch: NoSuchFieldError -> L17
        L60:
            $SwitchMap$com$arthenica$ffmpegkit$Level[Level.AV_LOG_WARNING.ordinal()] = 5;     // Catch: NoSuchFieldError -> L21
        L64:
            $SwitchMap$com$arthenica$ffmpegkit$Level[Level.AV_LOG_ERROR.ordinal()] = 6;     // Catch: NoSuchFieldError -> L24
        L82:
            $SwitchMap$com$arthenica$ffmpegkit$Level[Level.AV_LOG_FATAL.ordinal()] = 7;     // Catch: NoSuchFieldError -> L27
        L66:
            $SwitchMap$com$arthenica$ffmpegkit$Level[Level.AV_LOG_PANIC.ordinal()] = 8;     // Catch: NoSuchFieldError -> L30
        L72:
            $SwitchMap$com$arthenica$ffmpegkit$Level[Level.AV_LOG_STDERR.ordinal()] = 9;     // Catch: NoSuchFieldError -> L33
        L54:
            $SwitchMap$com$arthenica$ffmpegkit$Level[Level.AV_LOG_VERBOSE.ordinal()] = 10;     // Catch: NoSuchFieldError -> L36
        L37:
            $SwitchMap$com$arthenica$ffmpegkit$LogRedirectionStrategy = new int[LogRedirectionStrategy.values().length];
            $SwitchMap$com$arthenica$ffmpegkit$LogRedirectionStrategy[LogRedirectionStrategy.NEVER_PRINT_LOGS.ordinal()] = 1;     // Catch: NoSuchFieldError -> L40
        L56:
            $SwitchMap$com$arthenica$ffmpegkit$LogRedirectionStrategy[LogRedirectionStrategy.PRINT_LOGS_WHEN_GLOBAL_CALLBACK_NOT_DEFINED.ordinal()] = 2;     // Catch: NoSuchFieldError -> L43
        L74:
            $SwitchMap$com$arthenica$ffmpegkit$LogRedirectionStrategy[LogRedirectionStrategy.PRINT_LOGS_WHEN_SESSION_CALLBACK_NOT_DEFINED.ordinal()] = 3;     // Catch: NoSuchFieldError -> L46
        L76:
            $SwitchMap$com$arthenica$ffmpegkit$LogRedirectionStrategy[LogRedirectionStrategy.PRINT_LOGS_WHEN_NO_CALLBACKS_DEFINED.ordinal()] = 4;     // Catch: NoSuchFieldError -> L49
        L68:
            $SwitchMap$com$arthenica$ffmpegkit$LogRedirectionStrategy[LogRedirectionStrategy.ALWAYS_PRINT_LOGS.ordinal()] = 5;     // Catch: NoSuchFieldError -> L52
            return;
        }
    }

    static class SAFProtocolUrl {
        private final ContentResolver contentResolver;
        private final String openMode;
        private ParcelFileDescriptor parcelFileDescriptor;
        private final Integer safId;
        private final Uri uri;

        public SAFProtocolUrl(Integer r1, Uri r2, String r3, ContentResolver r4) {
            this.safId = r1;
            this.uri = r2;
            this.openMode = r3;
            this.contentResolver = r4;
        }

        public ContentResolver getContentResolver() {
            return this.contentResolver;
        }

        public String getOpenMode() {
            return this.openMode;
        }

        public ParcelFileDescriptor getParcelFileDescriptor() {
            return this.parcelFileDescriptor;
        }

        public Integer getSafId() {
            return this.safId;
        }

        public Uri getUri() {
            return this.uri;
        }

        public void setParcelFileDescriptor(ParcelFileDescriptor r1) {
            this.parcelFileDescriptor = r1;
        }
    }

    static {
        Exceptions.registerRootPackage("com.arthenica");
        android.util.Log.i(TAG, "Loading ffmpeg-kit.");
        boolean r0 = NativeLoader.loadFFmpeg();
        Abi.class.getName();
        FFmpegKit.class.getName();
        FFprobeKit.class.getName();
        NativeLoader.loadFFmpegKit(r0);
        uniqueIdGenerator = new AtomicInteger(1);
        activeLogLevel = Level.from(NativeLoader.loadLogLevel());
        asyncConcurrencyLimit = 10;
        asyncExecutorService = Executors.newFixedThreadPool(asyncConcurrencyLimit);
        sessionHistorySize = 10;
        sessionHistoryMap = new C05251();
        sessionHistoryList = new LinkedList();
        sessionHistoryLock = new Object();
        globalLogCallback = null;
        globalStatisticsCallback = null;
        globalFFmpegSessionCompleteCallback = null;
        globalFFprobeSessionCompleteCallback = null;
        globalMediaInformationSessionCompleteCallback = null;
        safIdMap = new SparseArray();
        safFileDescriptorMap = new SparseArray();
        globalLogRedirectionStrategy = LogRedirectionStrategy.PRINT_LOGS_WHEN_NO_CALLBACKS_DEFINED;
        android.util.Log.i(TAG, String.format("Loaded ffmpeg-kit-%s-%s-%s-%s.", new Object[]{NativeLoader.loadPackageName(), NativeLoader.loadAbi(), NativeLoader.loadVersion(), NativeLoader.loadBuildDate()}));
    }

    private FFmpegKitConfig() {
    }

    static /* synthetic */ int access$000() {
        return sessionHistorySize;
    }

    static void addSession(Session r5) {
        Object r0 = sessionHistoryLock;
        monitor-enter(r0);
    L9:
        th = move-exception;
        throw th;
    L5:
        if (sessionHistoryMap.containsKey(Long.valueOf(r5.getSessionId())) == true) goto L7;
        sessionHistoryMap.put(Long.valueOf(r5.getSessionId()), r5);     // Catch: Throwable -> L9
        sessionHistoryList.add(r5);     // Catch: Throwable -> L9
        deleteExpiredSessions();     // Catch: Throwable -> L9
    L7:
        monitor-exit(r0);     // Catch: Throwable -> L9
    }

    public static String argumentsToString(String[] r3) {
        if (r3 != null) goto L5;
        return "null";
    L5:
        StringBuilder r0 = new StringBuilder();
        int r1 = 0;
    L7:
        if (r1 >= r3.length) goto L12;
        if (r1 <= 0) goto L10;
        r0.append(" ");
    L10:
        r0.append(r3[r1]);
        r1 = r1 + 1;
        goto L7
    L12:
        return r0.toString();
    }

    public static void asyncFFmpegExecute(FFmpegSession r2) {
        AsyncFFmpegExecuteTask r0 = new AsyncFFmpegExecuteTask(r2);
        r2.setFuture(asyncExecutorService.submit(r0));
    }

    public static void asyncFFmpegExecute(FFmpegSession r2, ExecutorService r3) {
        r2.setFuture(r3.submit(new AsyncFFmpegExecuteTask(r2)));
    }

    public static void asyncFFprobeExecute(FFprobeSession r2) {
        AsyncFFprobeExecuteTask r0 = new AsyncFFprobeExecuteTask(r2);
        r2.setFuture(asyncExecutorService.submit(r0));
    }

    public static void asyncFFprobeExecute(FFprobeSession r2, ExecutorService r3) {
        r2.setFuture(r3.submit(new AsyncFFprobeExecuteTask(r2)));
    }

    public static void asyncGetMediaInformationExecute(MediaInformationSession r2, int r3) {
        AsyncGetMediaInformationTask r0 = new AsyncGetMediaInformationTask(r2, Integer.valueOf(r3));
        r2.setFuture(asyncExecutorService.submit(r0));
    }

    public static void asyncGetMediaInformationExecute(MediaInformationSession r2, ExecutorService r3, int r4) {
        r2.setFuture(r3.submit(new AsyncGetMediaInformationTask(r2, Integer.valueOf(r4))));
    }

    public static void clearSessions() {
        Object r0 = sessionHistoryLock;
        monitor-enter(r0);
        sessionHistoryList.clear();     // Catch: Throwable -> L7
        sessionHistoryMap.clear();     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return;
    L7:
        th = move-exception;
        throw th;
    }

    public static void closeFFmpegPipe(String r2) {
        File r0 = new File(r2);
        if (r0.exists() == false) goto L6;
        r0.delete();
        return;
    }

    private static void deleteExpiredSessions() {
    L3:
        if (sessionHistoryList.size() <= sessionHistorySize) goto L10;
        Session r0 = sessionHistoryList.remove(0);     // Catch: IndexOutOfBoundsException -> L8
        if (r0 == null) goto L3;
        sessionHistoryMap.remove(Long.valueOf(r0.getSessionId()));     // Catch: IndexOutOfBoundsException -> L8
        goto L3
    }

    private static native void disableNativeRedirection();

    public static void disableRedirection() {
        disableNativeRedirection();
    }

    public static void enableFFmpegSessionCompleteCallback(FFmpegSessionCompleteCallback r0) {
        globalFFmpegSessionCompleteCallback = r0;
    }

    public static void enableFFprobeSessionCompleteCallback(FFprobeSessionCompleteCallback r0) {
        globalFFprobeSessionCompleteCallback = r0;
    }

    public static void enableLogCallback(LogCallback r0) {
        globalLogCallback = r0;
    }

    public static void enableMediaInformationSessionCompleteCallback(MediaInformationSessionCompleteCallback r0) {
        globalMediaInformationSessionCompleteCallback = r0;
    }

    private static native void enableNativeRedirection();

    public static void enableRedirection() {
        enableNativeRedirection();
    }

    public static void enableStatisticsCallback(StatisticsCallback r0) {
        globalStatisticsCallback = r0;
    }

    static String extractExtensionFromSafDisplayName(String r5) {
        String r0 = r5;
        if (r5.lastIndexOf(".") < 0) goto L10;
        r0 = r5.substring(r5.lastIndexOf("."));
    L10:
        return new StringTokenizer(r0, " .").nextToken();
    L7:
        e = move-exception;
        android.util.Log.w(TAG, String.format("Failed to extract extension from saf display name: %s.%s", new Object[]{r5, Exceptions.getStackTraceString(e)}));
        return "raw";
    }

    public static void ffmpegExecute(FFmpegSession r5) {
        r5.startRunning();
        r5.complete(new ReturnCode(nativeFFmpegExecute(r5.getSessionId(), r5.getArguments())));     // Catch: Exception -> L5
        return;
    L5:
        e = move-exception;
        r5.fail(e);
        android.util.Log.w(TAG, String.format("FFmpeg execute failed: %s.%s", new Object[]{argumentsToString(r5.getArguments()), Exceptions.getStackTraceString(e)}));
    }

    public static void ffprobeExecute(FFprobeSession r5) {
        r5.startRunning();
        r5.complete(new ReturnCode(nativeFFprobeExecute(r5.getSessionId(), r5.getArguments())));     // Catch: Exception -> L5
        return;
    L5:
        e = move-exception;
        r5.fail(e);
        android.util.Log.w(TAG, String.format("FFprobe execute failed: %s.%s", new Object[]{argumentsToString(r5.getArguments()), Exceptions.getStackTraceString(e)}));
    }

    public static int getAsyncConcurrencyLimit() {
        return asyncConcurrencyLimit;
    }

    public static String getBuildDate() {
        return getNativeBuildDate();
    }

    public static FFmpegSessionCompleteCallback getFFmpegSessionCompleteCallback() {
        return globalFFmpegSessionCompleteCallback;
    }

    public static List<FFmpegSession> getFFmpegSessions() {
        LinkedList r0 = new LinkedList();
        Object r1 = sessionHistoryLock;
        monitor-enter(r1);
        Iterator<Session> r2 = sessionHistoryList.iterator();     // Catch: Throwable -> L13
    L6:
        if (r2.hasNext() == false) goto L11;
        Session r3 = r2.next();     // Catch: Throwable -> L13
        if (r3.isFFmpeg() == false) goto L6;
        r0.add((FFmpegSession) r3);     // Catch: Throwable -> L13
        goto L6
    L11:
        monitor-exit(r1);     // Catch: Throwable -> L13
        return r0;
    L13:
        th = move-exception;
        throw th;
    }

    public static String getFFmpegVersion() {
        return getNativeFFmpegVersion();
    }

    public static FFprobeSessionCompleteCallback getFFprobeSessionCompleteCallback() {
        return globalFFprobeSessionCompleteCallback;
    }

    public static List<FFprobeSession> getFFprobeSessions() {
        LinkedList r0 = new LinkedList();
        Object r1 = sessionHistoryLock;
        monitor-enter(r1);
        Iterator<Session> r2 = sessionHistoryList.iterator();     // Catch: Throwable -> L13
    L6:
        if (r2.hasNext() == false) goto L11;
        Session r3 = r2.next();     // Catch: Throwable -> L13
        if (r3.isFFprobe() == false) goto L6;
        r0.add((FFprobeSession) r3);     // Catch: Throwable -> L13
        goto L6
    L11:
        monitor-exit(r1);     // Catch: Throwable -> L13
        return r0;
    L13:
        th = move-exception;
        throw th;
    }

    public static Session getLastCompletedSession() {
        Object r0 = sessionHistoryLock;
        monitor-enter(r0);
        int r1 = sessionHistoryList.size() - 1;     // Catch: Throwable -> L14
    L5:
        if (r1 < 0) goto L11;
        Session r2 = sessionHistoryList.get(r1);     // Catch: Throwable -> L14
        if (r2.getState() == SessionState.COMPLETED) goto L8;
        r1 = r1 - 1;     // Catch: Throwable -> L14
        goto L5
    L8:
        monitor-exit(r0);     // Catch: Throwable -> L14
        return r2;
    L11:
        monitor-exit(r0);     // Catch: Throwable -> L14
        return null;
    L14:
        th = move-exception;
        throw th;
    }

    public static Session getLastSession() {
        Object r0 = sessionHistoryLock;
        monitor-enter(r0);
    L12:
        th = move-exception;
        throw th;
    L5:
        if (sessionHistoryList.size() <= 0) goto L9;
        Session r1 = sessionHistoryList.get(sessionHistoryList.size() - 1);     // Catch: Throwable -> L12
        monitor-exit(r0);     // Catch: Throwable -> L12
        return r1;
    L9:
        monitor-exit(r0);     // Catch: Throwable -> L12
        return null;
    }

    public static Level getLogLevel() {
        return activeLogLevel;
    }

    public static LogRedirectionStrategy getLogRedirectionStrategy() {
        return globalLogRedirectionStrategy;
    }

    public static void getMediaInformationExecute(MediaInformationSession r9, int r10) {
        r9.startRunning();
        ReturnCode r1 = new ReturnCode(nativeFFprobeExecute(r9.getSessionId(), r9.getArguments()));     // Catch: Exception -> L13
        r9.complete(r1);     // Catch: Exception -> L13
        if (r1.isValueSuccess() == false) goto L22;
        List<Log> r2 = r9.getAllLogs(r10);     // Catch: Exception -> L13
        StringBuilder r3 = new StringBuilder();     // Catch: Exception -> L13
        int r4 = 0;
        int r5 = r2.size();     // Catch: Exception -> L13
    L6:
        if (r4 >= r5) goto L11;
        Log r6 = r2.get(r4);     // Catch: Exception -> L13
        if (r6.getLevel() != Level.AV_LOG_STDERR) goto L10;
        r3.append(r6.getMessage());     // Catch: Exception -> L13
    L10:
        r4 = r4 + 1;     // Catch: Exception -> L13
        goto L6
    L11:
        r9.setMediaInformation(MediaInformationJsonParser.fromWithError(r3.toString()));     // Catch: Exception -> L13
        return;
    L22:
        return;
    L13:
        e = move-exception;
        r9.fail(e);
        android.util.Log.w(TAG, String.format("Get media information execute failed: %s.%s", new Object[]{argumentsToString(r9.getArguments()), Exceptions.getStackTraceString(e)}));
    }

    public static MediaInformationSessionCompleteCallback getMediaInformationSessionCompleteCallback() {
        return globalMediaInformationSessionCompleteCallback;
    }

    public static List<MediaInformationSession> getMediaInformationSessions() {
        LinkedList r0 = new LinkedList();
        Object r1 = sessionHistoryLock;
        monitor-enter(r1);
        Iterator<Session> r2 = sessionHistoryList.iterator();     // Catch: Throwable -> L13
    L6:
        if (r2.hasNext() == false) goto L11;
        Session r3 = r2.next();     // Catch: Throwable -> L13
        if (r3.isMediaInformation() == false) goto L6;
        r0.add((MediaInformationSession) r3);     // Catch: Throwable -> L13
        goto L6
    L11:
        monitor-exit(r1);     // Catch: Throwable -> L13
        return r0;
    L13:
        th = move-exception;
        throw th;
    }

    private static native String getNativeBuildDate();

    private static native String getNativeFFmpegVersion();

    static native int getNativeLogLevel();

    private static native String getNativeVersion();

    public static String getSafParameter(Context r9, Uri r10, String r11) {
        String r2 = EnvironmentCompat.MEDIA_UNKNOWN;
        Uri r4 = r10;
        Cursor r12 = r9.getContentResolver().query(r4, null, null, null, null);     // Catch: Throwable -> L23
        if (r12 != null) goto L30;
    L19:
        if (r12 == null) goto L21;
        r12.close();     // Catch: Throwable -> L23
    L21:
        int r13 = uniqueIdGenerator.getAndIncrement();
        safIdMap.put(r13, new SAFProtocolUrl(Integer.valueOf(r13), r4, r11, r9.getContentResolver()));
        return "saf:" + r13 + "." + extractExtensionFromSafDisplayName(r2);
    L30:
    L11:
        th = move-exception;
        if (r12 == null) goto L36;
        r12.close();     // Catch: Throwable -> L16
    L37:
        throw th;     // Catch: Throwable -> L23
    L16:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Throwable -> L23
        throw th;     // Catch: Throwable -> L23
    L36:
        throw th;     // Catch: Throwable -> L23
    L8:
        if (r12.moveToFirst() == false) goto L19;
        r2 = r12.getString(r12.getColumnIndex("_display_name"));     // Catch: Throwable -> L11
        goto L19
    L23:
        th = move-exception;
        Throwable r14 = th;
    L27:
        android.util.Log.e(TAG, String.format("Failed to get %s column for %s.%s", new Object[]{"_display_name", r4.toString(), Exceptions.getStackTraceString(r14)}));
        throw r14;
    L25:
        th = move-exception;
        r4 = r10;
        r14 = th;
        goto L27
    }

    public static String getSafParameterForRead(Context r1, Uri r2) {
        return getSafParameter(r1, r2, "r");
    }

    public static String getSafParameterForWrite(Context r1, Uri r2) {
        return getSafParameter(r1, r2, "w");
    }

    public static Session getSession(long r3) {
        Object r0 = sessionHistoryLock;
        monitor-enter(r0);
        Session r1 = sessionHistoryMap.get(Long.valueOf(r3));     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return r1;
    L7:
        th = move-exception;
        throw th;
    }

    public static int getSessionHistorySize() {
        return sessionHistorySize;
    }

    public static List<Session> getSessions() {
        Object r0 = sessionHistoryLock;
        monitor-enter(r0);
        LinkedList r1 = new LinkedList(sessionHistoryList);     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return r1;
    L7:
        th = move-exception;
        throw th;
    }

    public static List<Session> getSessionsByState(SessionState r5) {
        LinkedList r0 = new LinkedList();
        Object r1 = sessionHistoryLock;
        monitor-enter(r1);
        Iterator<Session> r2 = sessionHistoryList.iterator();     // Catch: Throwable -> L13
    L6:
        if (r2.hasNext() == false) goto L11;
        Session r3 = r2.next();     // Catch: Throwable -> L13
        if (r3.getState() != r5) goto L6;
        r0.add(r3);     // Catch: Throwable -> L13
        goto L6
    L11:
        monitor-exit(r1);     // Catch: Throwable -> L13
        return r0;
    L13:
        th = move-exception;
        throw th;
    }

    public static List<String> getSupportedCameraIds(Context r3) {
        ArrayList r0 = new ArrayList();
        if (Build.VERSION.SDK_INT < 24) goto L5;
        r0.addAll(CameraSupport.extractSupportedCameraIds(r3));
    L5:
        return r0;
    }

    public static String getVersion() {
        if (isLTSBuild() == false) goto L7;
        return String.format("%s-lts", new Object[]{getNativeVersion()});
    L7:
        return getNativeVersion();
    }

    private static native void ignoreNativeSignal(int r0);

    public static void ignoreSignal(Signal r1) {
        ignoreNativeSignal(r1.getValue());
    }

    public static boolean isLTSBuild() {
        return AbiDetect.isNativeLTSBuild();
    }

    private static void log(long r17, int r19, byte[] r20) {
        Level r2 = Level.from(r19);
        String r0 = new String(r20);
        Log r1 = new Log(r17, r2, r0);
        boolean r8 = false;
        LogRedirectionStrategy r9 = globalLogRedirectionStrategy;
        if (activeLogLevel != Level.AV_LOG_QUIET) goto L7;
        if (r19 == Level.AV_LOG_STDERR.getValue()) goto L7;
        return;
    L7:
        if (r19 > activeLogLevel.getValue()) goto L51;
        Session r10 = getSession(r17);
        if (r10 == null) goto L19;
        r9 = r10.getLogRedirectionStrategy();
        r10.addLog(r1);
        if (r10.getLogCallback() == null) goto L18;
        boolean r14 = true;
        r10.getLogCallback().apply(r1);     // Catch: Exception -> L16
        char r16 = 0;
    L20:
        LogCallback r11 = globalLogCallback;
        if (r11 == null) goto L28;
        r8 = true;
        r11.apply(r1);     // Catch: Exception -> L25
    L25:
        e = move-exception;
        Object[] r12 = new Object[1];
        r12[r16] = Exceptions.getStackTraceString(e);
        android.util.Log.e(TAG, String.format("Exception thrown inside global log callback.%s", r12));
    L28:
        switch(C05262.$SwitchMap$com$arthenica$ffmpegkit$LogRedirectionStrategy[r9.ordinal()]) {
            case 1: goto L37;
            case 2: goto L35;
            case 3: goto L33;
            case 4: goto L30;
            default: goto L39;
        };
    L30:
        if (r8 == true) goto L32;
        if (r14 == false) goto L39;
        return;
    L32:
        return;
    L33:
        if (r14 == false) goto L39;
        return;
    L35:
        if (r8 == false) goto L39;
        return;
    L37:
        return;
    L39:
        switch(C05262.$SwitchMap$com$arthenica$ffmpegkit$Level[r2.ordinal()]) {
            case 1: goto L57;
            case 2: goto L44;
            case 3: goto L44;
            case 4: goto L43;
            case 5: goto L42;
            case 6: goto L41;
            case 7: goto L41;
            case 8: goto L41;
            default: goto L40;
        };
    L40:
        android.util.Log.v(TAG, r0);
        return;
    L41:
        android.util.Log.e(TAG, r0);
        return;
    L42:
        android.util.Log.w(TAG, r0);
        return;
    L43:
        android.util.Log.i(TAG, r0);
        return;
    L44:
        android.util.Log.d(TAG, r0);
        return;
    L57:
        return;
    L16:
        e = move-exception;
        r16 = 0;
        android.util.Log.e(TAG, String.format("Exception thrown inside session log callback.%s", new Object[]{Exceptions.getStackTraceString(e)}));
        goto L20
    L18:
        r16 = 0;
        r14 = false;
        goto L20
    L19:
        r16 = 0;
        r14 = false;
        goto L20
    }

    public static native int messagesInTransmit(long r0);

    static native void nativeFFmpegCancel(long r0);

    private static native int nativeFFmpegExecute(long r0, String[] r2);

    static native int nativeFFprobeExecute(long r0, String[] r2);

    public static String[] parseArguments(String r9) {
        ArrayList r0 = new ArrayList();
        StringBuilder r1 = new StringBuilder();
        boolean r2 = false;
        boolean r3 = false;
        int r4 = 0;
    L4:
        if (r4 >= r9.length()) goto L40;
        if (r4 <= 0) goto L7;
        Character r5 = Character.valueOf(r9.charAt(r4 - 1));
    L8:
        char r6 = r9.charAt(r4);
        if (r6 != ' ') goto L18;
        if (r2 == true) goto L16;
        if (r3 == true) goto L16;
        if (r1.length() <= 0) goto L38;
        r0.add(r1.toString());
        r1 = new StringBuilder();
    L38:
        r4 = r4 + 1;
    L16:
        r1.append(r6);
        goto L38
    L18:
        if (r6 != '\'') goto L28;
        if (r5 != null) goto L21;
    L22:
        if (r2 == false) goto L24;
        r2 = false;
        goto L38
    L24:
        if (r3 == false) goto L26;
        r1.append(r6);
        goto L38
    L26:
        r2 = true;
        goto L38
    L21:
        if (r5.charValue() != '\\') goto L22;
    L28:
        if (r6 != '\"') goto L37;
        if (r5 != null) goto L31;
    L32:
        if (r3 == false) goto L34;
        r3 = false;
        goto L38
    L34:
        if (r2 == false) goto L36;
        r1.append(r6);
        goto L38
    L36:
        r3 = true;
        goto L38
    L31:
        if (r5.charValue() != '\\') goto L32;
    L37:
        r1.append(r6);
        goto L38
    L7:
        r5 = null;
        goto L8
    L40:
        if (r1.length() <= 0) goto L43;
        r0.add(r1.toString());
    L43:
        return (String[]) r0.toArray(new String[0]);
    }

    public static void printToLogcat(int r7, String r8) {
        String r1 = r8;
    L4:
        if (r1.length() > 4000) goto L6;
        android.util.Log.println(r7, TAG, r1);
        r1 = "";
    L11:
        if (r1.length() > 0) goto L4;
        return;
    L6:
        int r5 = r1.substring(0, 4000).lastIndexOf(10);
        if (r5 >= 0) goto L9;
        android.util.Log.println(r7, TAG, r1.substring(0, 4000));
        r1 = r1.substring(4000);
        goto L11
    L9:
        android.util.Log.println(r7, TAG, r1.substring(0, r5));
        r1 = r1.substring(r5);
        goto L11
    }

    public static String registerNewFFmpegPipe(Context r10) {
        File r1 = new File(r10.getCacheDir(), "pipes");
        if (r1.exists() == false) goto L5;
    L8:
        String r2 = MessageFormat.format("{0}{1}{2}{3}", new Object[]{r1, File.separator, FFMPEG_KIT_NAMED_PIPE_PREFIX, Integer.valueOf(uniqueIdGenerator.getAndIncrement())});
        closeFFmpegPipe(r2);
        int r7 = registerNewNativeFFmpegPipe(r2);
        if (r7 != 0) goto L11;
        return r2;
    L11:
        android.util.Log.e(TAG, String.format("Failed to register new FFmpeg pipe %s. Operation failed with rc=%d.", new Object[]{r2, Integer.valueOf(r7)}));
        return null;
    L5:
        if (r1.mkdirs() == true) goto L8;
        android.util.Log.e(TAG, String.format("Failed to create pipes directory: %s.", new Object[]{r1.getAbsolutePath()}));
        return null;
    }

    private static native int registerNewNativeFFmpegPipe(String r0);

    private static int safClose(int r8) {
        SAFProtocolUrl r3 = safFileDescriptorMap.get(r8);     // Catch: Throwable -> L12
        if (r3 == null) goto L10;
        ParcelFileDescriptor r4 = r3.getParcelFileDescriptor();     // Catch: Throwable -> L12
        if (r4 == null) goto L9;
        safFileDescriptorMap.delete(r8);     // Catch: Throwable -> L12
        safIdMap.delete(r3.getSafId().intValue());     // Catch: Throwable -> L12
        r4.close();     // Catch: Throwable -> L12
        return 1;
    L9:
        android.util.Log.e(TAG, String.format("ParcelFileDescriptor for SAF fd %d not found.", new Object[]{Integer.valueOf(r8)}));     // Catch: Throwable -> L12
    L14:
        return 0;
    L10:
        android.util.Log.e(TAG, String.format("SAF fd %d not found.", new Object[]{Integer.valueOf(r8)}));     // Catch: Throwable -> L12
    L12:
        th = move-exception;
        android.util.Log.e(TAG, String.format("Failed to close SAF fd: %d.%s", new Object[]{Integer.valueOf(r8), Exceptions.getStackTraceString(th)}));
        goto L14
    }

    private static int safOpen(int r7) {
        SAFProtocolUrl r3 = safIdMap.get(r7);     // Catch: Throwable -> L9
        if (r3 == null) goto L7;
        ParcelFileDescriptor r4 = r3.getContentResolver().openFileDescriptor(r3.getUri(), r3.getOpenMode());     // Catch: Throwable -> L9
        r3.setParcelFileDescriptor(r4);     // Catch: Throwable -> L9
        int r5 = r4.getFd();     // Catch: Throwable -> L9
        safFileDescriptorMap.put(r5, r3);     // Catch: Throwable -> L9
        return r5;
    L7:
        android.util.Log.e(TAG, String.format("SAF id %d not found.", new Object[]{Integer.valueOf(r7)}));     // Catch: Throwable -> L9
    L11:
        return 0;
    L9:
        th = move-exception;
        android.util.Log.e(TAG, String.format("Failed to open SAF id: %d.%s", new Object[]{Integer.valueOf(r7), Exceptions.getStackTraceString(th)}));
        goto L11
    }

    public static String sessionStateToString(SessionState r1) {
        return r1.toString();
    }

    public static void setAsyncConcurrencyLimit(int r2) {
        if (r2 <= 0) goto L5;
        asyncConcurrencyLimit = r2;
        ExecutorService r0 = asyncExecutorService;
        asyncExecutorService = Executors.newFixedThreadPool(r2);
        r0.shutdown();
        return;
    }

    public static int setEnvironmentVariable(String r1, String r2) {
        return setNativeEnvironmentVariable(r1, r2);
    }

    public static void setFontDirectory(Context r1, String r2, Map<String, String> r3) {
        setFontDirectoryList(r1, Collections.singletonList(r2), r3);
    }

    public static void setFontDirectoryList(Context r15, List<String> r16, Map<String, String> r17) {
        int r0 = 0;
        File r2 = new File(r15.getCacheDir(), "fontconfig");
        if (r2.exists() == true) goto L5;
        android.util.Log.d(TAG, String.format("Created temporary font conf directory: %s.", new Object[]{Boolean.valueOf(r2.mkdirs())}));
    L5:
        File r3 = new File(r2, "fonts.conf");
        if (r3.exists() == false) goto L8;
        android.util.Log.d(TAG, String.format("Deleted old temporary font configuration: %s.", new Object[]{Boolean.valueOf(r3.delete())}));
    L8:
        StringBuilder r7 = new StringBuilder("");
        if (r17 != null) goto L11;
    L25:
        int r8 = 0;
    L26:
        StringBuilder r1 = new StringBuilder();
        r1.append("<?xml version=\"1.0\"?>\n");
        r1.append("<!DOCTYPE fontconfig SYSTEM \"fonts.dtd\">\n");
        r1.append("<fontconfig>\n");
        r1.append("    <dir prefix=\"cwd\">.</dir>\n");
        Iterator<String> r4 = r16.iterator();
    L28:
        if (r4.hasNext() == false) goto L30;
        String r10 = r4.next();
        r1.append("    <dir>");
        r1.append(r10);
        r1.append("</dir>\n");
        goto L28
    L30:
        r1.append(r7);
        r1.append("</fontconfig>\n");
        AtomicReference r5 = new AtomicReference();
        FileOutputStream r6 = new FileOutputStream(r3);     // Catch: Throwable -> L40 IOException -> L42
        r5.set(r6);     // Catch: Throwable -> L40 IOException -> L42
        r6.write(r1.toString().getBytes());     // Catch: Throwable -> L40 IOException -> L42
        r6.flush();     // Catch: Throwable -> L40 IOException -> L42
        android.util.Log.d(TAG, String.format("Saved new temporary font configuration with %d font name mappings.", new Object[]{Integer.valueOf(r8)}));     // Catch: Throwable -> L40 IOException -> L42
        setFontconfigConfigurationPath(r2.getAbsolutePath());     // Catch: Throwable -> L40 IOException -> L42
        Iterator<String> r11 = r16.iterator();     // Catch: Throwable -> L40 IOException -> L42
    L33:
        if (r11.hasNext() == false) goto L37;
        android.util.Log.d(TAG, String.format("Font directory %s registered successfully.", new Object[]{r11.next()}));     // Catch: Throwable -> L40 IOException -> L42
        goto L77
    L37:
        if (r5.get() == null) goto L50;
        ((FileOutputStream) r5.get()).close();     // Catch: IOException -> L48
        return;
    L50:
        return;
    L42:
        e = move-exception;
        android.util.Log.e(TAG, String.format("Failed to set font directory: %s.%s", new Object[]{Arrays.toString(r16.toArray()), Exceptions.getStackTraceString(e)}));     // Catch: Throwable -> L40
        if (r5.get() == null) goto L76;
        ((FileOutputStream) r5.get()).close();     // Catch: IOException -> L48
        return;
    L76:
        return;
    L40:
        th = move-exception;
        if (r5.get() == null) goto L80;
        ((FileOutputStream) r5.get()).close();     // Catch: IOException -> L55
        throw th;
    L56:
        throw th;
    L80:
        throw th;
    L77:
        return;
    L11:
        if (r17.size() <= 0) goto L25;
        r17.entrySet();
        Iterator<Map.Entry<String, String>> r9 = r17.entrySet().iterator();
    L14:
        if (r9.hasNext() == false) goto L24;
        Map.Entry<String, String> r12 = r9.next();
        String r13 = r12.getKey();
        String r14 = r12.getValue();
        if (r13 == null) goto L14;
        if (r14 == null) goto L14;
        if (r13.trim().length() <= 0) goto L14;
        if (r14.trim().length() <= 0) goto L14;
        r7.append("    <match target=\"pattern\">\n");
        r7.append("        <test qual=\"any\" name=\"family\">\n");
        r7.append(String.format("            <string>%s</string>\n", new Object[]{r13}));
        r7.append("        </test>\n");
        r7.append("        <edit name=\"family\" mode=\"assign\" binding=\"same\">\n");
        r7.append(String.format("            <string>%s</string>\n", new Object[]{r14}));
        r7.append("        </edit>\n");
        r7.append("    </match>\n");
        r0 = r0 + 1;
        goto L14
    L24:
        r8 = r0;
        goto L26
    }

    public static int setFontconfigConfigurationPath(String r1) {
        return setNativeEnvironmentVariable("FONTCONFIG_PATH", r1);
    }

    public static void setLogLevel(Level r1) {
        if (r1 == null) goto L5;
        activeLogLevel = r1;
        setNativeLogLevel(r1.getValue());
        return;
    }

    public static void setLogRedirectionStrategy(LogRedirectionStrategy r0) {
        globalLogRedirectionStrategy = r0;
    }

    private static native int setNativeEnvironmentVariable(String r0, String r1);

    private static native void setNativeLogLevel(int r0);

    public static void setSessionHistorySize(int r2) {
        if (r2 >= 1000) goto L8;
        if (r2 <= 0) goto L9;
        sessionHistorySize = r2;
        deleteExpiredSessions();
        return;
    L9:
        return;
    L8:
        throw new IllegalArgumentException("Session history size must not exceed the hard limit!");
    }

    private static void statistics(long r14, int r16, float r17, float r18, long r19, double r21, double r23, double r25) {
        Statistics r0 = new Statistics(r14, r16, r17, r18, r19, r21, r23, r25);
        Session r2 = getSession(r14);
        if (r2 != null) goto L5;
    L12:
        StatisticsCallback r6 = globalStatisticsCallback;
        if (r6 == null) goto L23;
        r6.apply(r0);     // Catch: Exception -> L16
        return;
    L16:
        e = move-exception;
        android.util.Log.e(TAG, String.format("Exception thrown inside global statistics callback.%s", new Object[]{Exceptions.getStackTraceString(e)}));
        return;
    L23:
        return;
    L5:
        if (r2.isFFmpeg() == false) goto L12;
        FFmpegSession r7 = (FFmpegSession) r2;
        r7.addStatistics(r0);
        if (r7.getStatisticsCallback() == null) goto L12;
        r7.getStatisticsCallback().apply(r0);     // Catch: Exception -> L10
    L10:
        e = move-exception;
        android.util.Log.e(TAG, String.format("Exception thrown inside session statistics callback.%s", new Object[]{Exceptions.getStackTraceString(e)}));
        goto L12
    }
}
