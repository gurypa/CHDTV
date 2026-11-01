package androidx.media3.exoplayer.offline;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.NotificationUtil;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.scheduler.Requirements;
import androidx.media3.exoplayer.scheduler.Scheduler;
import java.util.HashMap;
import java.util.List;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;

/* JADX INFO: loaded from: classes.dex */
public abstract class DownloadService extends Service {
    public static final String ACTION_ADD_DOWNLOAD = "androidx.media3.exoplayer.downloadService.action.ADD_DOWNLOAD";
    public static final String ACTION_INIT = "androidx.media3.exoplayer.downloadService.action.INIT";
    public static final String ACTION_PAUSE_DOWNLOADS = "androidx.media3.exoplayer.downloadService.action.PAUSE_DOWNLOADS";
    public static final String ACTION_REMOVE_ALL_DOWNLOADS = "androidx.media3.exoplayer.downloadService.action.REMOVE_ALL_DOWNLOADS";
    public static final String ACTION_REMOVE_DOWNLOAD = "androidx.media3.exoplayer.downloadService.action.REMOVE_DOWNLOAD";
    private static final String ACTION_RESTART = "androidx.media3.exoplayer.downloadService.action.RESTART";
    public static final String ACTION_RESUME_DOWNLOADS = "androidx.media3.exoplayer.downloadService.action.RESUME_DOWNLOADS";
    public static final String ACTION_SET_REQUIREMENTS = "androidx.media3.exoplayer.downloadService.action.SET_REQUIREMENTS";
    public static final String ACTION_SET_STOP_REASON = "androidx.media3.exoplayer.downloadService.action.SET_STOP_REASON";
    public static final long DEFAULT_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL = 1000;
    public static final int FOREGROUND_NOTIFICATION_ID_NONE = 0;
    public static final String KEY_CONTENT_ID = "content_id";
    public static final String KEY_DOWNLOAD_REQUEST = "download_request";
    public static final String KEY_FOREGROUND = "foreground";
    public static final String KEY_REQUIREMENTS = "requirements";
    public static final String KEY_STOP_REASON = "stop_reason";
    private static final String TAG = "DownloadService";
    private static final HashMap<Class<? extends DownloadService>, DownloadManagerHelper> downloadManagerHelpers = null;
    private final int channelDescriptionResourceId;
    private final String channelId;
    private final int channelNameResourceId;
    private DownloadManagerHelper downloadManagerHelper;
    private final ForegroundNotificationUpdater foregroundNotificationUpdater;
    private boolean isDestroyed;
    private boolean isStopped;
    private int lastStartId;
    private boolean startedInForeground;
    private boolean taskRemoved;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.offline.DownloadService$1 */
    static /* synthetic */ class C03181 {
    }

    private static final class DownloadManagerHelper implements DownloadManager.Listener {
        private final Context context;
        private final DownloadManager downloadManager;
        private DownloadService downloadService;
        private final boolean foregroundAllowed;
        private Requirements scheduledRequirements;
        private final Scheduler scheduler;
        private final Class<? extends DownloadService> serviceClass;

        private DownloadManagerHelper(Context r1, DownloadManager r2, boolean r3, Scheduler r4, Class<? extends DownloadService> r5) {
            this.context = r1;
            this.downloadManager = r2;
            this.foregroundAllowed = r3;
            this.scheduler = r4;
            this.serviceClass = r5;
            r2.addListener(this);
            updateScheduler();
        }

        /* synthetic */ DownloadManagerHelper(Context r1, DownloadManager r2, boolean r3, Scheduler r4, Class r5, C03181 r6) {
            this(r1, r2, r3, r4, r5);
        }

        static /* synthetic */ DownloadManager access$100(DownloadManagerHelper r1) {
            return r1.downloadManager;
        }

        @RequiresNonNull({"scheduler"})
        private void cancelScheduler() {
            Requirements r0 = new Requirements(0);
            if (schedulerNeedsUpdate(r0) == false) goto L6;
            this.scheduler.cancel();
            this.scheduledRequirements = r0;
            return;
        }

        private void restartService() {
            if (this.foregroundAllowed == true) goto L15;
            Intent r0 = DownloadService.access$900(this.context, this.serviceClass, DownloadService.ACTION_INIT);     // Catch: IllegalStateException -> L10
            this.context.startService(r0);     // Catch: IllegalStateException -> L10
            return;
        L11:
            Log.m48w(DownloadService.TAG, "Failed to restart (process is idle)");
            return;
        L15:
            Intent r1 = DownloadService.access$900(this.context, this.serviceClass, DownloadService.ACTION_RESTART);     // Catch: IllegalStateException -> L6
            Util.startForegroundService(this.context, r1);     // Catch: IllegalStateException -> L6
            return;
        L7:
            Log.m48w(DownloadService.TAG, "Failed to restart (foreground launch restriction)");
        }

        private boolean schedulerNeedsUpdate(Requirements r2) {
            return !Util.areEqual(this.scheduledRequirements, r2);
        }

        private boolean serviceMayNeedRestart() {
            if (this.downloadService != null) goto L5;
        L8:
            return true;
        L5:
            if (DownloadService.access$800(this.downloadService) == true) goto L8;
            return false;
        }

        public void attachService(final DownloadService r3) {
            if (this.downloadService != null) goto L5;
            boolean r0 = true;
        L6:
            Assertions.checkState(r0);
            this.downloadService = r3;
            if (this.downloadManager.isInitialized() == false) goto L10;
            Util.createHandlerForCurrentOrMainLooper().postAtFrontOfQueue(new DownloadService$DownloadManagerHelper$$ExternalSyntheticLambda0(this, r3));
            return;
        L10:
            return;
        L5:
            r0 = false;
            goto L6
        }

        public void detachService(DownloadService r2) {
            if (this.downloadService != r2) goto L5;
            boolean r0 = true;
        L6:
            Assertions.checkState(r0);
            this.downloadService = null;
            return;
        L5:
            r0 = false;
            goto L6
        }

        /* JADX INFO: renamed from: lambda$attachService$0$androidx-media3-exoplayer-offline-DownloadService$DownloadManagerHelper */
        /* synthetic */ void m116xee6ee963(DownloadService r2) {
            DownloadService.access$300(r2, this.downloadManager.getCurrentDownloads());
        }

        @Override // androidx.media3.exoplayer.offline.DownloadManager.Listener
        public void onDownloadChanged(DownloadManager r3, Download r4, Exception r5) {
            if (this.downloadService == null) goto L6;
            DownloadService.access$400(this.downloadService, r4);
        L6:
            if (serviceMayNeedRestart() == true) goto L8;
            return;
        L8:
            if (DownloadService.access$500(r4.state) == false) goto L12;
            Log.m48w(DownloadService.TAG, "DownloadService wasn't running. Restarting.");
            restartService();
            return;
        }

        @Override // androidx.media3.exoplayer.offline.DownloadManager.Listener
        public void onDownloadRemoved(DownloadManager r2, Download r3) {
            if (this.downloadService == null) goto L6;
            DownloadService.access$600(this.downloadService);
            return;
        }

        @Override // androidx.media3.exoplayer.offline.DownloadManager.Listener
        public /* synthetic */ void onDownloadsPausedChanged(DownloadManager r1, boolean r2) {
            DownloadManager.Listener.CC.$default$onDownloadsPausedChanged(this, r1, r2);
        }

        @Override // androidx.media3.exoplayer.offline.DownloadManager.Listener
        public final void onIdle(DownloadManager r2) {
            if (this.downloadService == null) goto L6;
            DownloadService.access$700(this.downloadService);
            return;
        }

        @Override // androidx.media3.exoplayer.offline.DownloadManager.Listener
        public void onInitialized(DownloadManager r3) {
            if (this.downloadService == null) goto L6;
            DownloadService.access$300(this.downloadService, r3.getCurrentDownloads());
            return;
        }

        @Override // androidx.media3.exoplayer.offline.DownloadManager.Listener
        public void onRequirementsStateChanged(DownloadManager r1, Requirements r2, int r3) {
            updateScheduler();
        }

        @Override // androidx.media3.exoplayer.offline.DownloadManager.Listener
        public void onWaitingForRequirementsChanged(DownloadManager r4, boolean r5) {
            if (r5 == false) goto L4;
            return;
        L4:
            if (r4.getDownloadsPaused() == false) goto L6;
            return;
        L6:
            if (serviceMayNeedRestart() == false) goto L19;
            List<Download> r0 = r4.getCurrentDownloads();
            int r1 = 0;
        L9:
            if (r1 >= r0.size()) goto L20;
            if (r0.get(r1).state == 0) goto L12;
            r1 = r1 + 1;
            goto L9
        L12:
            restartService();
            return;
        L20:
            return;
        }

        public boolean updateScheduler() {
            boolean r0 = this.downloadManager.isWaitingForRequirements();
            if (this.scheduler != null) goto L7;
            return !r0;
        L7:
            if (r0 == true) goto L10;
            cancelScheduler();
            return true;
        L10:
            Requirements r2 = this.downloadManager.getRequirements();
            if (this.scheduler.getSupportedRequirements(r2).equals(r2) == true) goto L15;
            cancelScheduler();
            return false;
        L15:
            if (schedulerNeedsUpdate(r2) == true) goto L18;
            return true;
        L18:
            if (this.scheduler.schedule(r2, this.context.getPackageName(), DownloadService.ACTION_RESTART) == false) goto L21;
            this.scheduledRequirements = r2;
            return true;
        L21:
            Log.m48w(DownloadService.TAG, "Failed to schedule restart");
            cancelScheduler();
            return false;
        }
    }

    private final class ForegroundNotificationUpdater {
        private final Handler handler;
        private boolean notificationDisplayed;
        private final int notificationId;
        private boolean periodicUpdatesStarted;
        final /* synthetic */ DownloadService this$0;
        private final long updateInterval;

        public static /* synthetic */ void $r8$lambda$kgZvcyiqiwHb4kLR_T2bvLJML3I(ForegroundNotificationUpdater r0) {
            r0.update();
        }

        public ForegroundNotificationUpdater(DownloadService r2, int r3, long r4) {
            this.this$0 = r2;
            this.notificationId = r3;
            this.updateInterval = r4;
            this.handler = new Handler(Looper.getMainLooper());
        }

        private void update() {
            DownloadManager r0 = DownloadManagerHelper.access$100((DownloadManagerHelper) Assertions.checkNotNull(DownloadService.access$200(this.this$0)));
            List<Download> r1 = r0.getCurrentDownloads();
            int r2 = r0.getNotMetRequirements();
            Notification r3 = this.this$0.getForegroundNotification(r1, r2);
            if (this.notificationDisplayed == true) goto L5;
            Util.setForegroundServiceNotification(this.this$0, this.notificationId, r3, 1, "dataSync");
            this.notificationDisplayed = true;
        L7:
            if (this.periodicUpdatesStarted == false) goto L10;
            this.handler.removeCallbacksAndMessages(null);
            this.handler.postDelayed(new RunnableC0319xac84e153(this), this.updateInterval);
            return;
        L10:
            return;
        L5:
            ((NotificationManager) this.this$0.getSystemService("notification")).notify(this.notificationId, r3);
            goto L7
        }

        public void invalidate() {
            if (this.notificationDisplayed == false) goto L6;
            update();
            return;
        }

        public void showNotificationIfNotAlready() {
            if (this.notificationDisplayed == true) goto L6;
            update();
            return;
        }

        public void startPeriodicUpdates() {
            this.periodicUpdatesStarted = true;
            update();
        }

        public void stopPeriodicUpdates() {
            this.periodicUpdatesStarted = false;
            this.handler.removeCallbacksAndMessages(null);
        }
    }

    static {
        downloadManagerHelpers = new HashMap();
    }

    protected DownloadService(int r3) {
        this(r3, 1000);
    }

    protected DownloadService(int r8, long r9) {
        this(r8, r9, null, 0, 0);
    }

    protected DownloadService(int r2, long r3, String r5, int r6, int r7) {
        if (r2 != 0) goto L5;
        this.foregroundNotificationUpdater = null;
        this.channelId = null;
        this.channelNameResourceId = 0;
        this.channelDescriptionResourceId = 0;
        return;
    L5:
        this.foregroundNotificationUpdater = new ForegroundNotificationUpdater(this, r2, r3);
        this.channelId = r5;
        this.channelNameResourceId = r6;
        this.channelDescriptionResourceId = r7;
    }

    static /* synthetic */ DownloadManagerHelper access$200(DownloadService r1) {
        return r1.downloadManagerHelper;
    }

    static /* synthetic */ void access$300(DownloadService r0, List r1) {
        r0.notifyDownloads(r1);
    }

    static /* synthetic */ void access$400(DownloadService r0, Download r1) {
        r0.notifyDownloadChanged(r1);
    }

    static /* synthetic */ boolean access$500(int r1) {
        return needsStartedService(r1);
    }

    static /* synthetic */ void access$600(DownloadService r0) {
        r0.notifyDownloadRemoved();
    }

    static /* synthetic */ void access$700(DownloadService r0) {
        r0.onIdle();
    }

    static /* synthetic */ boolean access$800(DownloadService r1) {
        return r1.isStopped();
    }

    static /* synthetic */ Intent access$900(Context r1, Class r2, String r3) {
        return getIntent(r1, r2, r3);
    }

    public static Intent buildAddDownloadIntent(Context r2, Class<? extends DownloadService> r3, DownloadRequest r4, int r5, boolean r6) {
        return getIntent(r2, r3, ACTION_ADD_DOWNLOAD, r6).putExtra(KEY_DOWNLOAD_REQUEST, r4).putExtra(KEY_STOP_REASON, r5);
    }

    public static Intent buildAddDownloadIntent(Context r1, Class<? extends DownloadService> r2, DownloadRequest r3, boolean r4) {
        return buildAddDownloadIntent(r1, r2, r3, 0, r4);
    }

    public static Intent buildPauseDownloadsIntent(Context r1, Class<? extends DownloadService> r2, boolean r3) {
        return getIntent(r1, r2, ACTION_PAUSE_DOWNLOADS, r3);
    }

    public static Intent buildRemoveAllDownloadsIntent(Context r1, Class<? extends DownloadService> r2, boolean r3) {
        return getIntent(r1, r2, ACTION_REMOVE_ALL_DOWNLOADS, r3);
    }

    public static Intent buildRemoveDownloadIntent(Context r2, Class<? extends DownloadService> r3, String r4, boolean r5) {
        return getIntent(r2, r3, ACTION_REMOVE_DOWNLOAD, r5).putExtra(KEY_CONTENT_ID, r4);
    }

    public static Intent buildResumeDownloadsIntent(Context r1, Class<? extends DownloadService> r2, boolean r3) {
        return getIntent(r1, r2, ACTION_RESUME_DOWNLOADS, r3);
    }

    public static Intent buildSetRequirementsIntent(Context r2, Class<? extends DownloadService> r3, Requirements r4, boolean r5) {
        return getIntent(r2, r3, ACTION_SET_REQUIREMENTS, r5).putExtra(KEY_REQUIREMENTS, r4);
    }

    public static Intent buildSetStopReasonIntent(Context r2, Class<? extends DownloadService> r3, String r4, int r5, boolean r6) {
        return getIntent(r2, r3, ACTION_SET_STOP_REASON, r6).putExtra(KEY_CONTENT_ID, r4).putExtra(KEY_STOP_REASON, r5);
    }

    public static void clearDownloadManagerHelpers() {
        downloadManagerHelpers.clear();
    }

    private static Intent getIntent(Context r1, Class<? extends DownloadService> r2, String r3) {
        return new Intent(r1, r2).setAction(r3);
    }

    private static Intent getIntent(Context r2, Class<? extends DownloadService> r3, String r4, boolean r5) {
        return getIntent(r2, r3, r4).putExtra(KEY_FOREGROUND, r5);
    }

    private boolean isStopped() {
        return this.isStopped;
    }

    private static boolean needsStartedService(int r1) {
        if (r1 != 2) goto L5;
    L10:
        return true;
    L5:
        if (r1 == 5) goto L10;
        if (r1 == 7) goto L10;
        return false;
    }

    private void notifyDownloadChanged(Download r2) {
        if (this.foregroundNotificationUpdater != null) goto L5;
        return;
    L5:
        if (needsStartedService(r2.state) == false) goto L7;
        this.foregroundNotificationUpdater.startPeriodicUpdates();
        return;
    L7:
        this.foregroundNotificationUpdater.invalidate();
    }

    private void notifyDownloadRemoved() {
        if (this.foregroundNotificationUpdater == null) goto L6;
        this.foregroundNotificationUpdater.invalidate();
        return;
    }

    private void notifyDownloads(List<Download> r3) {
        if (this.foregroundNotificationUpdater == null) goto L11;
        int r0 = 0;
    L6:
        if (r0 >= r3.size()) goto L15;
        if (needsStartedService(r3.get(r0).state) == true) goto L9;
        r0 = r0 + 1;
        goto L6
    L9:
        this.foregroundNotificationUpdater.startPeriodicUpdates();
        return;
    L15:
        return;
    }

    private void onIdle() {
        if (this.foregroundNotificationUpdater == null) goto L6;
        this.foregroundNotificationUpdater.stopPeriodicUpdates();
    L6:
        if (((DownloadManagerHelper) Assertions.checkNotNull(this.downloadManagerHelper)).updateScheduler() == true) goto L9;
        return;
    L9:
        if (Util.SDK_INT < 28) goto L11;
    L13:
        this.isStopped |= stopSelfResult(this.lastStartId);
        return;
    L11:
        if (this.taskRemoved == false) goto L13;
        stopSelf();
        this.isStopped = true;
    }

    public static void sendAddDownload(Context r1, Class<? extends DownloadService> r2, DownloadRequest r3, int r4, boolean r5) {
        startService(r1, buildAddDownloadIntent(r1, r2, r3, r4, r5), r5);
    }

    public static void sendAddDownload(Context r1, Class<? extends DownloadService> r2, DownloadRequest r3, boolean r4) {
        startService(r1, buildAddDownloadIntent(r1, r2, r3, r4), r4);
    }

    public static void sendPauseDownloads(Context r1, Class<? extends DownloadService> r2, boolean r3) {
        startService(r1, buildPauseDownloadsIntent(r1, r2, r3), r3);
    }

    public static void sendRemoveAllDownloads(Context r1, Class<? extends DownloadService> r2, boolean r3) {
        startService(r1, buildRemoveAllDownloadsIntent(r1, r2, r3), r3);
    }

    public static void sendRemoveDownload(Context r1, Class<? extends DownloadService> r2, String r3, boolean r4) {
        startService(r1, buildRemoveDownloadIntent(r1, r2, r3, r4), r4);
    }

    public static void sendResumeDownloads(Context r1, Class<? extends DownloadService> r2, boolean r3) {
        startService(r1, buildResumeDownloadsIntent(r1, r2, r3), r3);
    }

    public static void sendSetRequirements(Context r1, Class<? extends DownloadService> r2, Requirements r3, boolean r4) {
        startService(r1, buildSetRequirementsIntent(r1, r2, r3, r4), r4);
    }

    public static void sendSetStopReason(Context r1, Class<? extends DownloadService> r2, String r3, int r4, boolean r5) {
        startService(r1, buildSetStopReasonIntent(r1, r2, r3, r4, r5), r5);
    }

    public static void start(Context r1, Class<? extends DownloadService> r2) {
        r1.startService(getIntent(r1, r2, ACTION_INIT));
    }

    public static void startForeground(Context r2, Class<? extends DownloadService> r3) {
        Util.startForegroundService(r2, getIntent(r2, r3, ACTION_INIT, true));
    }

    private static void startService(Context r0, Intent r1, boolean r2) {
        if (r2 == false) goto L4;
        Util.startForegroundService(r0, r1);
        return;
    L4:
        r0.startService(r1);
    }

    protected abstract DownloadManager getDownloadManager();

    protected abstract Notification getForegroundNotification(List<Download> r1, int r2);

    protected abstract Scheduler getScheduler();

    protected final void invalidateForegroundNotification() {
        if (this.foregroundNotificationUpdater != null) goto L5;
        return;
    L5:
        if (this.isDestroyed == true) goto L9;
        this.foregroundNotificationUpdater.invalidate();
        return;
    }

    @Override // android.app.Service
    public final IBinder onBind(Intent r2) {
        throw new UnsupportedOperationException();
    }

    @Override // android.app.Service
    public void onCreate() {
        if (this.channelId == null) goto L5;
        NotificationUtil.createNotificationChannel(this, this.channelId, this.channelNameResourceId, this.channelDescriptionResourceId, 2);
    L5:
        Class<?> r9 = getClass();
        DownloadManagerHelper r0 = downloadManagerHelpers.get(r9);
        if (r0 != null) goto L20;
        boolean r2 = true;
        if (this.foregroundNotificationUpdater == null) goto L10;
        boolean r7 = true;
    L12:
        if (Util.SDK_INT < 31) goto L15;
        r2 = false;
    L15:
        if (r7 == false) goto L18;
        if (r2 == false) goto L18;
        Scheduler r1 = getScheduler();
    L19:
        DownloadManager r6 = getDownloadManager();
        r6.resumeDownloads();
        r0 = new DownloadManagerHelper(getApplicationContext(), r6, r7, r1, r9, null);
        downloadManagerHelpers.put((Class<? extends DownloadService>) r9, r0);
    L18:
        r1 = null;
        goto L19
    L10:
        r7 = false;
    L20:
        this.downloadManagerHelper = r0;
        r0.attachService(this);
    }

    @Override // android.app.Service
    public void onDestroy() {
        this.isDestroyed = true;
        ((DownloadManagerHelper) Assertions.checkNotNull(this.downloadManagerHelper)).detachService(this);
        if (this.foregroundNotificationUpdater == null) goto L6;
        this.foregroundNotificationUpdater.stopPeriodicUpdates();
        return;
    }

    @Override // android.app.Service
    public int onStartCommand(Intent r10, int r11, int r12) {
        this.lastStartId = r12;
        this.taskRemoved = false;
        String r1 = null;
        String r2 = null;
        if (r10 == null) goto L12;
        r1 = r10.getAction();
        r2 = r10.getStringExtra(KEY_CONTENT_ID);
        boolean r5 = this.startedInForeground;
        if (r10.getBooleanExtra(KEY_FOREGROUND, false) == false) goto L7;
    L10:
        boolean r6 = true;
    L11:
        this.startedInForeground = r5 | r6;
        goto L12
    L7:
        if (ACTION_RESTART.equals(r1) == true) goto L10;
        r6 = false;
    L12:
        if (r1 != null) goto L14;
        r1 = ACTION_INIT;
    L14:
        DownloadManager r7 = DownloadManagerHelper.access$100((DownloadManagerHelper) Assertions.checkNotNull(this.downloadManagerHelper));
        switch(r1.hashCode()) {
            case -2068303304: goto L42;
            case -1192305801: goto L39;
            case -659421309: goto L36;
            case -238450692: goto L33;
            case 32678949: goto L30;
            case 464223742: goto L27;
            case 829812082: goto L24;
            case 845668953: goto L21;
            case 1746253622: goto L18;
            default: goto L44;
        };
    L44:
        byte r3 = -1;
    L46:
        switch(r3) {
            case 0: goto L68;
            case 1: goto L68;
            case 2: goto L62;
            case 3: goto L59;
            case 4: goto L58;
            case 5: goto L57;
            case 6: goto L56;
            case 7: goto L53;
            case 8: goto L48;
            default: goto L47;
        };
    L47:
        Log.m44e(TAG, "Ignored unrecognized action: " + r1);
        goto L68
    L48:
        Requirements r4 = (Requirements) ((Intent) Assertions.checkNotNull(r10)).getParcelableExtra(KEY_REQUIREMENTS);
        if (r4 != null) goto L51;
        Log.m44e(TAG, "Ignored SET_REQUIREMENTS: Missing requirements extra");
        goto L68
    L51:
        r7.setRequirements(r4);
        goto L68
    L56:
        r7.pauseDownloads();
        goto L68
    L57:
        r7.resumeDownloads();
        goto L68
    L58:
        r7.removeAllDownloads();
        goto L68
    L59:
        if (r2 != null) goto L61;
        Log.m44e(TAG, "Ignored REMOVE_DOWNLOAD: Missing content_id extra");
        goto L68
    L61:
        r7.removeDownload(r2);
        goto L68
    L62:
        DownloadRequest r8 = (DownloadRequest) ((Intent) Assertions.checkNotNull(r10)).getParcelableExtra(KEY_DOWNLOAD_REQUEST);
        if (r8 != null) goto L65;
        Log.m44e(TAG, "Ignored ADD_DOWNLOAD: Missing download_request extra");
        goto L68
    L65:
        r7.addDownload(r8, r10.getIntExtra(KEY_STOP_REASON, 0));
        goto L68
    L53:
        if (((Intent) Assertions.checkNotNull(r10)).hasExtra(KEY_STOP_REASON) == true) goto L55;
        Log.m44e(TAG, "Ignored SET_STOP_REASON: Missing stop_reason extra");
        goto L68
    L55:
        r7.setStopReason(r2, r10.getIntExtra(KEY_STOP_REASON, 0));
    L68:
        if (Util.SDK_INT >= 26) goto L70;
    L74:
        this.isStopped = false;
        if (r7.isIdle() == false) goto L77;
        onIdle();
    L77:
        return 1;
    L70:
        if (this.startedInForeground == false) goto L74;
        if (this.foregroundNotificationUpdater == null) goto L74;
        this.foregroundNotificationUpdater.showNotificationIfNotAlready();
        goto L74
    L18:
        if (r1.equals(ACTION_PAUSE_DOWNLOADS) == false) goto L44;
        r3 = 6;
        goto L46
    L21:
        if (r1.equals(ACTION_SET_REQUIREMENTS) == false) goto L44;
        r3 = 8;
        goto L46
    L24:
        if (r1.equals(ACTION_ADD_DOWNLOAD) == false) goto L44;
        r3 = 2;
        goto L46
    L27:
        if (r1.equals(ACTION_REMOVE_ALL_DOWNLOADS) == false) goto L44;
        r3 = 4;
        goto L46
    L30:
        if (r1.equals(ACTION_RESUME_DOWNLOADS) == false) goto L44;
        r3 = 5;
        goto L46
    L33:
        if (r1.equals(ACTION_INIT) == false) goto L44;
        r3 = 0;
        goto L46
    L36:
        if (r1.equals(ACTION_RESTART) == false) goto L44;
        r3 = 1;
        goto L46
    L39:
        if (r1.equals(ACTION_REMOVE_DOWNLOAD) == false) goto L44;
        r3 = 3;
        goto L46
    L42:
        if (r1.equals(ACTION_SET_STOP_REASON) == false) goto L44;
        r3 = 7;
        goto L46
    }

    @Override // android.app.Service
    public void onTaskRemoved(Intent r2) {
        this.taskRemoved = true;
    }
}
