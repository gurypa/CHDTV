package androidx.core.app;

import android.app.AppOpsManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.provider.Settings;
import android.support.v4.app.INotificationSideChannel;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public final class NotificationManagerCompat {
    public static final String ACTION_BIND_SIDE_CHANNEL = "android.support.BIND_NOTIFICATION_SIDE_CHANNEL";
    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    public static final String EXTRA_USE_SIDE_CHANNEL = "android.support.useSideChannel";
    public static final int IMPORTANCE_DEFAULT = 3;
    public static final int IMPORTANCE_HIGH = 4;
    public static final int IMPORTANCE_LOW = 2;
    public static final int IMPORTANCE_MAX = 5;
    public static final int IMPORTANCE_MIN = 1;
    public static final int IMPORTANCE_NONE = 0;
    public static final int IMPORTANCE_UNSPECIFIED = -1000;
    static final int MAX_SIDE_CHANNEL_SDK_VERSION = 19;
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";
    private static final String SETTING_ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    private static final int SIDE_CHANNEL_RETRY_BASE_INTERVAL_MS = 1000;
    private static final int SIDE_CHANNEL_RETRY_MAX_COUNT = 6;
    private static final String TAG = "NotifManCompat";
    private static Set<String> sEnabledNotificationListenerPackages;
    private static String sEnabledNotificationListeners;
    private static final Object sEnabledNotificationListenersLock = null;
    private static final Object sLock = null;
    private static SideChannelManager sSideChannelManager;
    private final Context mContext;
    private final NotificationManager mNotificationManager;

    private static class CancelTask implements Task {
        final boolean all;

        /* JADX INFO: renamed from: id */
        final int f6id;
        final String packageName;
        final String tag;

        CancelTask(String r2) {
            this.packageName = r2;
            this.f6id = 0;
            this.tag = null;
            this.all = true;
        }

        CancelTask(String r2, int r3, String r4) {
            this.packageName = r2;
            this.f6id = r3;
            this.tag = r4;
            this.all = false;
        }

        @Override // androidx.core.app.NotificationManagerCompat.Task
        public void send(INotificationSideChannel r4) throws RemoteException {
            if (this.all == false) goto L5;
            r4.cancelAll(this.packageName);
            return;
        L5:
            r4.cancel(this.packageName, this.f6id, this.tag);
        }

        public String toString() {
            StringBuilder r0 = new StringBuilder("CancelTask[");
            r0.append("packageName:").append(this.packageName);
            r0.append(", id:").append(this.f6id);
            r0.append(", tag:").append(this.tag);
            r0.append(", all:").append(this.all);
            r0.append("]");
            return r0.toString();
        }
    }

    private static class NotifyTask implements Task {

        /* JADX INFO: renamed from: id */
        final int f7id;
        final Notification notif;
        final String packageName;
        final String tag;

        NotifyTask(String r1, int r2, String r3, Notification r4) {
            this.packageName = r1;
            this.f7id = r2;
            this.tag = r3;
            this.notif = r4;
        }

        @Override // androidx.core.app.NotificationManagerCompat.Task
        public void send(INotificationSideChannel r5) throws RemoteException {
            r5.notify(this.packageName, this.f7id, this.tag, this.notif);
        }

        public String toString() {
            StringBuilder r0 = new StringBuilder("NotifyTask[");
            r0.append("packageName:").append(this.packageName);
            r0.append(", id:").append(this.f7id);
            r0.append(", tag:").append(this.tag);
            r0.append("]");
            return r0.toString();
        }
    }

    private static class ServiceConnectedEvent {
        final ComponentName componentName;
        final IBinder iBinder;

        ServiceConnectedEvent(ComponentName r1, IBinder r2) {
            this.componentName = r1;
            this.iBinder = r2;
        }
    }

    private static class SideChannelManager implements Handler.Callback, ServiceConnection {
        private static final int MSG_QUEUE_TASK = 0;
        private static final int MSG_RETRY_LISTENER_QUEUE = 3;
        private static final int MSG_SERVICE_CONNECTED = 1;
        private static final int MSG_SERVICE_DISCONNECTED = 2;
        private Set<String> mCachedEnabledPackages;
        private final Context mContext;
        private final Handler mHandler;
        private final HandlerThread mHandlerThread;
        private final Map<ComponentName, ListenerRecord> mRecordMap;

        private static class ListenerRecord {
            boolean bound;
            final ComponentName componentName;
            int retryCount;
            INotificationSideChannel service;
            ArrayDeque<Task> taskQueue;

            ListenerRecord(ComponentName r3) {
                this.bound = false;
                this.taskQueue = new ArrayDeque();
                this.retryCount = 0;
                this.componentName = r3;
            }
        }

        SideChannelManager(Context r3) {
            this.mRecordMap = new HashMap();
            this.mCachedEnabledPackages = new HashSet();
            this.mContext = r3;
            this.mHandlerThread = new HandlerThread("NotificationManagerCompat");
            this.mHandlerThread.start();
            this.mHandler = new Handler(this.mHandlerThread.getLooper(), this);
        }

        private boolean ensureServiceBound(ListenerRecord r4) {
            if (r4.bound == false) goto L6;
            return true;
        L6:
            r4.bound = this.mContext.bindService(new Intent(NotificationManagerCompat.ACTION_BIND_SIDE_CHANNEL).setComponent(r4.componentName), this, 33);
            if (r4.bound == false) goto L9;
            r4.retryCount = 0;
        L11:
            return r4.bound;
        L9:
            Log.w(NotificationManagerCompat.TAG, "Unable to bind to listener " + r4.componentName);
            this.mContext.unbindService(this);
            goto L11
        }

        private void ensureServiceUnbound(ListenerRecord r2) {
            if (r2.bound == false) goto L5;
            this.mContext.unbindService(this);
            r2.bound = false;
        L5:
            r2.service = null;
        }

        private void handleQueueTask(Task r4) {
            updateListenerMap();
            Iterator<ListenerRecord> r0 = this.mRecordMap.values().iterator();
        L4:
            if (r0.hasNext() == false) goto L6;
            ListenerRecord r1 = r0.next();
            r1.taskQueue.add(r4);
            processListenerQueue(r1);
            goto L4
        }

        private void handleRetryListenerQueue(ComponentName r2) {
            ListenerRecord r0 = this.mRecordMap.get(r2);
            if (r0 == null) goto L6;
            processListenerQueue(r0);
            return;
        }

        private void handleServiceConnected(ComponentName r3, IBinder r4) {
            ListenerRecord r0 = this.mRecordMap.get(r3);
            if (r0 == null) goto L6;
            r0.service = INotificationSideChannel.Stub.asInterface(r4);
            r0.retryCount = 0;
            processListenerQueue(r0);
            return;
        }

        private void handleServiceDisconnected(ComponentName r2) {
            ListenerRecord r0 = this.mRecordMap.get(r2);
            if (r0 == null) goto L6;
            ensureServiceUnbound(r0);
            return;
        }

        private void processListenerQueue(ListenerRecord r6) {
            if (Log.isLoggable(NotificationManagerCompat.TAG, 3) == false) goto L6;
            Log.d(NotificationManagerCompat.TAG, "Processing component " + r6.componentName + ", " + r6.taskQueue.size() + " queued tasks");
        L6:
            if (r6.taskQueue.isEmpty() == false) goto L9;
            return;
        L9:
            if (ensureServiceBound(r6) == true) goto L11;
        L32:
            scheduleListenerRetry(r6);
            return;
        L11:
            if (r6.service == null) goto L32;
        L13:
            Task r2 = r6.taskQueue.peek();
            if (r2 == null) goto L29;
            if (Log.isLoggable(NotificationManagerCompat.TAG, 3) == false) goto L19;
            Log.d(NotificationManagerCompat.TAG, "Sending task " + r2);     // Catch: RemoteException -> L21 DeadObjectException -> L23
        L19:
            r2.send(r6.service);     // Catch: RemoteException -> L21 DeadObjectException -> L23
            r6.taskQueue.remove();     // Catch: RemoteException -> L21 DeadObjectException -> L23
            goto L13
        L21:
            e = move-exception;
            Log.w(NotificationManagerCompat.TAG, "RemoteException communicating with " + r6.componentName, e);
        L25:
            if (Log.isLoggable(NotificationManagerCompat.TAG, 3) == false) goto L29;
            Log.d(NotificationManagerCompat.TAG, "Remote service has died: " + r6.componentName);
        L29:
            if (r6.taskQueue.isEmpty() == true) goto L39;
            scheduleListenerRetry(r6);
            return;
        }

        private void scheduleListenerRetry(ListenerRecord r6) {
            if (this.mHandler.hasMessages(3, r6.componentName) == false) goto L5;
            return;
        L5:
            r6.retryCount++;
            if (r6.retryCount <= 6) goto L9;
            Log.w(NotificationManagerCompat.TAG, "Giving up on delivering " + r6.taskQueue.size() + " tasks to " + r6.componentName + " after " + r6.retryCount + " retries");
            r6.taskQueue.clear();
            return;
        L9:
            int r0 = (1 << (r6.retryCount - 1)) * 1000;
            if (Log.isLoggable(NotificationManagerCompat.TAG, 3) == false) goto L12;
            Log.d(NotificationManagerCompat.TAG, "Scheduling retry for " + r0 + " ms");
        L12:
            this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(3, r6.componentName), r0);
        }

        private void updateListenerMap() {
            Set<String> r0 = NotificationManagerCompat.getEnabledListenerPackages(this.mContext);
            if (r0.equals(this.mCachedEnabledPackages) == false) goto L5;
            return;
        L5:
            this.mCachedEnabledPackages = r0;
            List<ResolveInfo> r1 = this.mContext.getPackageManager().queryIntentServices(new Intent().setAction(NotificationManagerCompat.ACTION_BIND_SIDE_CHANNEL), 0);
            HashSet r2 = new HashSet();
            Iterator<ResolveInfo> r3 = r1.iterator();
        L7:
            if (r3.hasNext() == false) goto L15;
            ResolveInfo r4 = r3.next();
            if (r0.contains(r4.serviceInfo.packageName) == false) goto L7;
            ComponentName r6 = new ComponentName(r4.serviceInfo.packageName, r4.serviceInfo.name);
            if (r4.serviceInfo.permission != null) goto L13;
            r2.add(r6);
            goto L7
        L13:
            Log.w(NotificationManagerCompat.TAG, "Permission present on component " + r6 + ", not adding listener record.");
            goto L7
        L15:
            Iterator r5 = r2.iterator();
        L17:
            if (r5.hasNext() == false) goto L25;
            ComponentName r7 = (ComponentName) r5.next();
            if (this.mRecordMap.containsKey(r7) == true) goto L17;
            if (Log.isLoggable(NotificationManagerCompat.TAG, 3) == false) goto L23;
            Log.d(NotificationManagerCompat.TAG, "Adding listener record for " + r7);
        L23:
            this.mRecordMap.put(r7, new ListenerRecord(r7));
            goto L17
        L25:
            Iterator<Map.Entry<ComponentName, ListenerRecord>> r8 = this.mRecordMap.entrySet().iterator();
        L27:
            if (r8.hasNext() == false) goto L35;
            Map.Entry<ComponentName, ListenerRecord> r9 = r8.next();
            if (r2.contains(r9.getKey()) == true) goto L27;
            if (Log.isLoggable(NotificationManagerCompat.TAG, 3) == false) goto L33;
            Log.d(NotificationManagerCompat.TAG, "Removing listener record for " + r9.getKey());
        L33:
            ensureServiceUnbound(r9.getValue());
            r8.remove();
            goto L27
        }

        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message r5) {
            switch(r5.what) {
                case 0: goto L12;
                case 1: goto L10;
                case 2: goto L8;
                case 3: goto L6;
                default: goto L4;
            };
        L4:
            return false;
        L6:
            handleRetryListenerQueue((ComponentName) r5.obj);
            return true;
        L8:
            handleServiceDisconnected((ComponentName) r5.obj);
            return true;
        L10:
            ServiceConnectedEvent r0 = (ServiceConnectedEvent) r5.obj;
            handleServiceConnected(r0.componentName, r0.iBinder);
            return true;
        L12:
            handleQueueTask((Task) r5.obj);
            return true;
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName r4, IBinder r5) {
            if (Log.isLoggable(NotificationManagerCompat.TAG, 3) == false) goto L5;
            Log.d(NotificationManagerCompat.TAG, "Connected to service " + r4);
        L5:
            this.mHandler.obtainMessage(1, new ServiceConnectedEvent(r4, r5)).sendToTarget();
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName r4) {
            if (Log.isLoggable(NotificationManagerCompat.TAG, 3) == false) goto L5;
            Log.d(NotificationManagerCompat.TAG, "Disconnected from service " + r4);
        L5:
            this.mHandler.obtainMessage(2, r4).sendToTarget();
        }

        public void queueTask(Task r3) {
            this.mHandler.obtainMessage(0, r3).sendToTarget();
        }
    }

    private interface Task {
        void send(INotificationSideChannel r1) throws RemoteException;
    }

    static {
        sEnabledNotificationListenersLock = new Object();
        sEnabledNotificationListenerPackages = new HashSet();
        sLock = new Object();
    }

    private NotificationManagerCompat(Context r3) {
        this.mContext = r3;
        this.mNotificationManager = (NotificationManager) this.mContext.getSystemService("notification");
    }

    public static NotificationManagerCompat from(Context r1) {
        return new NotificationManagerCompat(r1);
    }

    public static Set<String> getEnabledListenerPackages(Context r9) {
        String r0 = Settings.Secure.getString(r9.getContentResolver(), SETTING_ENABLED_NOTIFICATION_LISTENERS);
        Object r1 = sEnabledNotificationListenersLock;
        monitor-enter(r1);
        if (r0 != null) goto L20;
    L14:
        Set<String> r2 = sEnabledNotificationListenerPackages;     // Catch: Throwable -> L17
        monitor-exit(r1);     // Catch: Throwable -> L17
        return r2;
    L17:
        th = move-exception;
        throw th;
    L20:
        if (r0.equals(sEnabledNotificationListeners) == true) goto L14;
        String[] r3 = r0.split(":", -1);     // Catch: Throwable -> L17
        HashSet r4 = new HashSet(r3.length);     // Catch: Throwable -> L17
        int r5 = r3.length;     // Catch: Throwable -> L17
        int r6 = 0;
    L8:
        if (r6 >= r5) goto L13;
        ComponentName r7 = ComponentName.unflattenFromString(r3[r6]);     // Catch: Throwable -> L17
        if (r7 == null) goto L12;
        r4.add(r7.getPackageName());     // Catch: Throwable -> L17
    L12:
        r6 = r6 + 1;     // Catch: Throwable -> L17
        goto L8
    L13:
        sEnabledNotificationListenerPackages = r4;     // Catch: Throwable -> L17
        sEnabledNotificationListeners = r0;     // Catch: Throwable -> L17
        goto L14
    }

    private void pushSideChannelQueue(Task r4) {
        Object r0 = sLock;
        monitor-enter(r0);
    L10:
        th = move-exception;
        throw th;
    L5:
        if (sSideChannelManager != null) goto L7;
        sSideChannelManager = new SideChannelManager(this.mContext.getApplicationContext());     // Catch: Throwable -> L10
    L7:
        sSideChannelManager.queueTask(r4);     // Catch: Throwable -> L10
        monitor-exit(r0);     // Catch: Throwable -> L10
    }

    private static boolean useSideChannelForNotification(Notification r2) {
        Bundle r0 = NotificationCompat.getExtras(r2);
        if (r0 != null) goto L5;
    L7:
        return false;
    L5:
        if (r0.getBoolean(EXTRA_USE_SIDE_CHANNEL) == false) goto L7;
        return true;
    }

    public boolean areNotificationsEnabled() {
        if (Build.VERSION.SDK_INT >= 24) goto L5;
        AppOpsManager r0 = (AppOpsManager) this.mContext.getSystemService("appops");
        ApplicationInfo r1 = this.mContext.getApplicationInfo();
        String r2 = this.mContext.getApplicationContext().getPackageName();
        int r3 = r1.uid;
        Class<?> r5 = Class.forName(AppOpsManager.class.getName());     // Catch: RuntimeException -> L12 Throwable -> L14 InvocationTargetException -> L16 NoSuchFieldException -> L18 NoSuchMethodException -> L20 ClassNotFoundException -> L22
        Class<?> r9 = Integer.TYPE;     // Catch: RuntimeException -> L12 Throwable -> L14 InvocationTargetException -> L16 NoSuchFieldException -> L18 NoSuchMethodException -> L20 ClassNotFoundException -> L22
        if (((Integer) r5.getMethod(CHECK_OP_NO_THROW, new Class[]{r9, r9, String.class}).invoke(r0, new Object[]{Integer.valueOf(((Integer) r5.getDeclaredField(OP_POST_NOTIFICATION).get(Integer.class)).intValue()), Integer.valueOf(r3), r2})).intValue() != 0) goto L11;
        return true;
    L11:
        return false;
    L23:
        return true;
    L5:
        return this.mNotificationManager.areNotificationsEnabled();
    }

    public void cancel(int r2) {
        cancel(null, r2);
    }

    public void cancel(String r2, int r3) {
        this.mNotificationManager.cancel(r2, r3);
    }

    public void cancelAll() {
        this.mNotificationManager.cancelAll();
    }

    public void createNotificationChannel(NotificationChannel r3) {
        if (Build.VERSION.SDK_INT < 26) goto L6;
        this.mNotificationManager.createNotificationChannel(r3);
        return;
    }

    public void createNotificationChannel(NotificationChannelCompat r2) {
        createNotificationChannel(r2.getNotificationChannel());
    }

    public void createNotificationChannelGroup(NotificationChannelGroup r3) {
        if (Build.VERSION.SDK_INT < 26) goto L6;
        this.mNotificationManager.createNotificationChannelGroup(r3);
        return;
    }

    public void createNotificationChannelGroup(NotificationChannelGroupCompat r2) {
        createNotificationChannelGroup(r2.getNotificationChannelGroup());
    }

    public void createNotificationChannelGroups(List<NotificationChannelGroup> r3) {
        if (Build.VERSION.SDK_INT < 26) goto L6;
        this.mNotificationManager.createNotificationChannelGroups(r3);
        return;
    }

    public void createNotificationChannelGroupsCompat(List<NotificationChannelGroupCompat> r5) {
        if (Build.VERSION.SDK_INT >= 26) goto L5;
        return;
    L5:
        if (r5.isEmpty() == true) goto L14;
        ArrayList r0 = new ArrayList(r5.size());
        Iterator<NotificationChannelGroupCompat> r1 = r5.iterator();
    L8:
        if (r1.hasNext() == false) goto L10;
        r0.add(r1.next().getNotificationChannelGroup());
        goto L8
    L10:
        this.mNotificationManager.createNotificationChannelGroups(r0);
        return;
    }

    public void createNotificationChannels(List<NotificationChannel> r3) {
        if (Build.VERSION.SDK_INT < 26) goto L6;
        this.mNotificationManager.createNotificationChannels(r3);
        return;
    }

    public void createNotificationChannelsCompat(List<NotificationChannelCompat> r5) {
        if (Build.VERSION.SDK_INT >= 26) goto L5;
        return;
    L5:
        if (r5.isEmpty() == true) goto L14;
        ArrayList r0 = new ArrayList(r5.size());
        Iterator<NotificationChannelCompat> r1 = r5.iterator();
    L8:
        if (r1.hasNext() == false) goto L10;
        r0.add(r1.next().getNotificationChannel());
        goto L8
    L10:
        this.mNotificationManager.createNotificationChannels(r0);
        return;
    }

    public void deleteNotificationChannel(String r3) {
        if (Build.VERSION.SDK_INT < 26) goto L6;
        this.mNotificationManager.deleteNotificationChannel(r3);
        return;
    }

    public void deleteNotificationChannelGroup(String r3) {
        if (Build.VERSION.SDK_INT < 26) goto L6;
        this.mNotificationManager.deleteNotificationChannelGroup(r3);
        return;
    }

    public void deleteUnlistedNotificationChannels(Collection<String> r5) {
        if (Build.VERSION.SDK_INT < 26) goto L16;
        Iterator<NotificationChannel> r0 = this.mNotificationManager.getNotificationChannels().iterator();
    L6:
        if (r0.hasNext() == false) goto L25;
        NotificationChannel r1 = r0.next();
        if (r5.contains(r1.getId()) == true) goto L6;
        if (Build.VERSION.SDK_INT < 30) goto L15;
        if (r5.contains(r1.getParentChannelId()) == true) goto L6;
    L15:
        this.mNotificationManager.deleteNotificationChannel(r1.getId());
        goto L6
    L25:
        return;
    }

    public int getImportance() {
        if (Build.VERSION.SDK_INT >= 24) goto L5;
        return -1000;
    L5:
        return this.mNotificationManager.getImportance();
    }

    public NotificationChannel getNotificationChannel(String r3) {
        if (Build.VERSION.SDK_INT >= 26) goto L5;
        return null;
    L5:
        return this.mNotificationManager.getNotificationChannel(r3);
    }

    public NotificationChannel getNotificationChannel(String r3, String r4) {
        if (Build.VERSION.SDK_INT < 30) goto L7;
        return this.mNotificationManager.getNotificationChannel(r3, r4);
    L7:
        return getNotificationChannel(r3);
    }

    public NotificationChannelCompat getNotificationChannelCompat(String r3) {
        if (Build.VERSION.SDK_INT < 26) goto L8;
        NotificationChannel r0 = getNotificationChannel(r3);
        if (r0 != null) goto L7;
        return null;
    L7:
        return new NotificationChannelCompat(r0);
    L8:
        return null;
    }

    public NotificationChannelCompat getNotificationChannelCompat(String r3, String r4) {
        if (Build.VERSION.SDK_INT < 26) goto L8;
        NotificationChannel r0 = getNotificationChannel(r3, r4);
        if (r0 != null) goto L7;
        return null;
    L7:
        return new NotificationChannelCompat(r0);
    L8:
        return null;
    }

    public NotificationChannelGroup getNotificationChannelGroup(String r5) {
        if (Build.VERSION.SDK_INT < 28) goto L7;
        return this.mNotificationManager.getNotificationChannelGroup(r5);
    L7:
        if (Build.VERSION.SDK_INT < 26) goto L16;
        Iterator<NotificationChannelGroup> r0 = getNotificationChannelGroups().iterator();
    L10:
        if (r0.hasNext() == false) goto L15;
        NotificationChannelGroup r1 = r0.next();
        if (r1.getId().equals(r5) == false) goto L10;
        return r1;
    L15:
        return null;
    L16:
        return null;
    }

    public NotificationChannelGroupCompat getNotificationChannelGroupCompat(String r4) {
        if (Build.VERSION.SDK_INT < 28) goto L10;
        NotificationChannelGroup r0 = getNotificationChannelGroup(r4);
        if (r0 != null) goto L7;
        return null;
    L7:
        return new NotificationChannelGroupCompat(r0);
    L10:
        if (Build.VERSION.SDK_INT < 26) goto L18;
        NotificationChannelGroup r1 = getNotificationChannelGroup(r4);
        if (r1 != null) goto L14;
        return null;
    L14:
        return new NotificationChannelGroupCompat(r1, getNotificationChannels());
    L18:
        return null;
    }

    public List<NotificationChannelGroup> getNotificationChannelGroups() {
        if (Build.VERSION.SDK_INT < 26) goto L7;
        return this.mNotificationManager.getNotificationChannelGroups();
    L7:
        return Collections.emptyList();
    }

    public List<NotificationChannelGroupCompat> getNotificationChannelGroupsCompat() {
        if (Build.VERSION.SDK_INT < 26) goto L20;
        List<NotificationChannelGroup> r0 = getNotificationChannelGroups();
        if (r0.isEmpty() == true) goto L20;
        if (Build.VERSION.SDK_INT < 28) goto L9;
        List<NotificationChannel> r1 = Collections.emptyList();
    L10:
        ArrayList r3 = new ArrayList(r0.size());
        Iterator<NotificationChannelGroup> r4 = r0.iterator();
    L12:
        if (r4.hasNext() == false) goto L18;
        NotificationChannelGroup r5 = r4.next();
        if (Build.VERSION.SDK_INT >= 28) goto L15;
        r3.add(new NotificationChannelGroupCompat(r5, r1));
        goto L12
    L15:
        r3.add(new NotificationChannelGroupCompat(r5));
        goto L12
    L18:
        return r3;
    L9:
        r1 = getNotificationChannels();
    L20:
        return Collections.emptyList();
    }

    public List<NotificationChannel> getNotificationChannels() {
        if (Build.VERSION.SDK_INT < 26) goto L7;
        return this.mNotificationManager.getNotificationChannels();
    L7:
        return Collections.emptyList();
    }

    public List<NotificationChannelCompat> getNotificationChannelsCompat() {
        if (Build.VERSION.SDK_INT < 26) goto L12;
        List<NotificationChannel> r0 = getNotificationChannels();
        if (r0.isEmpty() == true) goto L12;
        ArrayList r1 = new ArrayList(r0.size());
        Iterator<NotificationChannel> r2 = r0.iterator();
    L8:
        if (r2.hasNext() == false) goto L10;
        r1.add(new NotificationChannelCompat(r2.next()));
        goto L8
    L10:
        return r1;
    L12:
        return Collections.emptyList();
    }

    public void notify(int r2, Notification r3) {
        notify(null, r2, r3);
    }

    public void notify(String r3, int r4, Notification r5) {
        if (useSideChannelForNotification(r5) == false) goto L5;
        pushSideChannelQueue(new NotifyTask(this.mContext.getPackageName(), r4, r3, r5));
        this.mNotificationManager.cancel(r3, r4);
        return;
    L5:
        this.mNotificationManager.notify(r3, r4, r5);
    }
}
