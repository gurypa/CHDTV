package androidx.media;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.browse.MediaBrowser;
import android.media.session.MediaSession;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.service.media.MediaBrowserService;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.IMediaSession;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;
import androidx.collection.ArrayMap;
import androidx.core.app.BundleCompat;
import androidx.core.util.Pair;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public abstract class MediaBrowserServiceCompat extends Service {
    static final boolean DEBUG = false;
    private static final float EPSILON = 1.0E-5f;
    public static final String KEY_MEDIA_ITEM = "media_item";
    public static final String KEY_SEARCH_RESULTS = "search_results";
    public static final int RESULT_ERROR = -1;
    static final int RESULT_FLAG_ON_LOAD_ITEM_NOT_IMPLEMENTED = 2;
    static final int RESULT_FLAG_ON_SEARCH_NOT_IMPLEMENTED = 4;
    static final int RESULT_FLAG_OPTION_NOT_HANDLED = 1;
    public static final int RESULT_OK = 0;
    public static final int RESULT_PROGRESS_UPDATE = 1;
    public static final String SERVICE_INTERFACE = "android.media.browse.MediaBrowserService";
    static final String TAG = "MBServiceCompat";
    final ConnectionRecord mConnectionFromFwk;
    final ArrayMap<IBinder, ConnectionRecord> mConnections;
    ConnectionRecord mCurConnection;
    final ServiceHandler mHandler;
    private MediaBrowserServiceImpl mImpl;
    final ArrayList<ConnectionRecord> mPendingConnections;
    private final ServiceBinderImpl mServiceBinderImpl;
    MediaSessionCompat.Token mSession;

    public static final class BrowserRoot {
        public static final String EXTRA_OFFLINE = "android.service.media.extra.OFFLINE";
        public static final String EXTRA_RECENT = "android.service.media.extra.RECENT";
        public static final String EXTRA_SUGGESTED = "android.service.media.extra.SUGGESTED";

        @Deprecated
        public static final String EXTRA_SUGGESTION_KEYWORDS = "android.service.media.extra.SUGGESTION_KEYWORDS";
        private final Bundle mExtras;
        private final String mRootId;

        public BrowserRoot(String r3, Bundle r4) {
            if (r3 == null) goto L7;
            this.mRootId = r3;
            this.mExtras = r4;
            return;
        L7:
            throw new IllegalArgumentException("The root id in BrowserRoot cannot be null. Use null for BrowserRoot instead");
        }

        static /* synthetic */ String access$000(BrowserRoot r1) {
            return r1.mRootId;
        }

        static /* synthetic */ Bundle access$100(BrowserRoot r1) {
            return r1.mExtras;
        }

        public Bundle getExtras() {
            return this.mExtras;
        }

        public String getRootId() {
            return this.mRootId;
        }
    }

    private class ConnectionRecord implements IBinder.DeathRecipient {
        public final MediaSessionManager.RemoteUserInfo browserInfo;
        public final ServiceCallbacks callbacks;
        public final int pid;
        public final String pkg;
        public BrowserRoot root;
        public final Bundle rootHints;
        public final HashMap<String, List<Pair<IBinder, Bundle>>> subscriptions;
        final /* synthetic */ MediaBrowserServiceCompat this$0;
        public final int uid;

        ConnectionRecord(MediaBrowserServiceCompat r1, String r2, int r3, int r4, Bundle r5, ServiceCallbacks r6) {
            this.this$0 = r1;
            this.subscriptions = new HashMap();
            this.pkg = r2;
            this.pid = r3;
            this.uid = r4;
            this.browserInfo = new MediaSessionManager.RemoteUserInfo(r2, r3, r4);
            this.rootHints = r5;
            this.callbacks = r6;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            this.this$0.mHandler.post(new RunnableC01561(this));
        }
    }

    interface MediaBrowserServiceImpl {
        Bundle getBrowserRootHints();

        MediaSessionManager.RemoteUserInfo getCurrentBrowserInfo();

        void notifyChildrenChanged(MediaSessionManager.RemoteUserInfo r1, String r2, Bundle r3);

        void notifyChildrenChanged(String r1, Bundle r2);

        IBinder onBind(Intent r1);

        void onCreate();

        void setSessionToken(MediaSessionCompat.Token r1);
    }

    class MediaBrowserServiceImplApi21 implements MediaBrowserServiceImpl {
        Messenger mMessenger;
        final List<Bundle> mRootExtrasList;
        MediaBrowserService mServiceFwk;
        final /* synthetic */ MediaBrowserServiceCompat this$0;

        class MediaBrowserServiceApi21 extends MediaBrowserService {
            final /* synthetic */ MediaBrowserServiceImplApi21 this$1;

            MediaBrowserServiceApi21(MediaBrowserServiceImplApi21 r1, Context r2) {
                this.this$1 = r1;
                attachBaseContext(r2);
            }

            @Override // android.service.media.MediaBrowserService
            public MediaBrowserService.BrowserRoot onGetRoot(String r5, int r6, Bundle r7) {
                MediaSessionCompat.ensureClassLoader(r7);
                MediaBrowserServiceImplApi21 r0 = this.this$1;
                if (r7 != null) goto L5;
                Bundle r2 = null;
            L6:
                BrowserRoot r1 = r0.onGetRoot(r5, r6, r2);
                if (r1 != null) goto L10;
                return null;
            L10:
                return new MediaBrowserService.BrowserRoot(BrowserRoot.access$000(r1), BrowserRoot.access$100(r1));
            L5:
                r2 = new Bundle(r7);
                goto L6
            }

            @Override // android.service.media.MediaBrowserService
            public void onLoadChildren(String r3, MediaBrowserService.Result<List<MediaBrowser.MediaItem>> r4) {
                this.this$1.onLoadChildren(r3, new ResultWrapper(r4));
            }
        }

        MediaBrowserServiceImplApi21(MediaBrowserServiceCompat r2) {
            this.this$0 = r2;
            this.mRootExtrasList = new ArrayList();
        }

        @Override // androidx.media.MediaBrowserServiceCompat.MediaBrowserServiceImpl
        public Bundle getBrowserRootHints() {
            if (this.mMessenger != null) goto L6;
            return null;
        L6:
            if (this.this$0.mCurConnection == null) goto L13;
            if (this.this$0.mCurConnection.rootHints != null) goto L11;
            return null;
        L11:
            return new Bundle(this.this$0.mCurConnection.rootHints);
        L13:
            throw new IllegalStateException("This should be called inside of onGetRoot, onLoadChildren, onLoadItem, onSearch, or onCustomAction methods");
        }

        @Override // androidx.media.MediaBrowserServiceCompat.MediaBrowserServiceImpl
        public MediaSessionManager.RemoteUserInfo getCurrentBrowserInfo() {
            if (this.this$0.mCurConnection == null) goto L7;
            return this.this$0.mCurConnection.browserInfo;
        L7:
            throw new IllegalStateException("This should be called inside of onGetRoot, onLoadChildren, onLoadItem, onSearch, or onCustomAction methods");
        }

        @Override // androidx.media.MediaBrowserServiceCompat.MediaBrowserServiceImpl
        public void notifyChildrenChanged(MediaSessionManager.RemoteUserInfo r1, String r2, Bundle r3) {
            notifyChildrenChangedForCompat(r1, r2, r3);
        }

        @Override // androidx.media.MediaBrowserServiceCompat.MediaBrowserServiceImpl
        public void notifyChildrenChanged(String r1, Bundle r2) {
            notifyChildrenChangedForFramework(r1, r2);
            notifyChildrenChangedForCompat(r1, r2);
        }

        void notifyChildrenChangedForCompat(final MediaSessionManager.RemoteUserInfo r3, final String r4, final Bundle r5) {
            this.this$0.mHandler.post(new RunnableC01604(this, r3, r4, r5));
        }

        void notifyChildrenChangedForCompat(final String r3, final Bundle r4) {
            this.this$0.mHandler.post(new RunnableC01593(this, r3, r4));
        }

        void notifyChildrenChangedForCompatOnHandler(ConnectionRecord r6, String r7, Bundle r8) {
            List<Pair<IBinder, Bundle>> r0 = r6.subscriptions.get(r7);
            if (r0 == null) goto L11;
            Iterator<Pair<IBinder, Bundle>> r1 = r0.iterator();
        L6:
            if (r1.hasNext() == false) goto L17;
            Pair<IBinder, Bundle> r2 = r1.next();
            if (MediaBrowserCompatUtils.hasDuplicatedItems(r8, r2.second) == false) goto L6;
            this.this$0.performLoadChildren(r7, r6, r2.second, r8);
            goto L6
        L17:
            return;
        }

        void notifyChildrenChangedForFramework(String r2, Bundle r3) {
            this.mServiceFwk.notifyChildrenChanged(r2);
        }

        @Override // androidx.media.MediaBrowserServiceCompat.MediaBrowserServiceImpl
        public IBinder onBind(Intent r2) {
            return this.mServiceFwk.onBind(r2);
        }

        @Override // androidx.media.MediaBrowserServiceCompat.MediaBrowserServiceImpl
        public void onCreate() {
            this.mServiceFwk = new MediaBrowserServiceApi21(this, this.this$0);
            this.mServiceFwk.onCreate();
        }

        public BrowserRoot onGetRoot(String r11, int r12, Bundle r13) {
            Bundle r0 = null;
            if (r13 != null) goto L5;
        L15:
            int r6 = -1;
        L16:
            ConnectionRecord r3 = new ConnectionRecord(this.this$0, r11, r6, r12, r13, null);
            this.this$0.mCurConnection = r3;
            BrowserRoot r14 = this.this$0.onGetRoot(r11, r12, r13);
            this.this$0.mCurConnection = null;
            if (r14 != null) goto L20;
            return null;
        L20:
            if (this.mMessenger == null) goto L22;
            this.this$0.mPendingConnections.add(r3);
        L22:
            if (r0 != null) goto L25;
            r0 = r14.getExtras();
        L28:
            return new BrowserRoot(r14.getRootId(), r0);
        L25:
            if (r14.getExtras() == null) goto L28;
            r0.putAll(r14.getExtras());
            goto L28
        L5:
            if (r13.getInt(MediaBrowserProtocol.EXTRA_CLIENT_VERSION, 0) == 0) goto L15;
            r13.remove(MediaBrowserProtocol.EXTRA_CLIENT_VERSION);
            this.mMessenger = new Messenger(this.this$0.mHandler);
            r0 = new Bundle();
            r0.putInt(MediaBrowserProtocol.EXTRA_SERVICE_VERSION, 2);
            BundleCompat.putBinder(r0, MediaBrowserProtocol.EXTRA_MESSENGER_BINDER, this.mMessenger.getBinder());
            if (this.this$0.mSession == null) goto L13;
            IMediaSession r4 = this.this$0.mSession.getExtraBinder();
            if (r4 != null) goto L11;
            IBinder r5 = null;
        L12:
            BundleCompat.putBinder(r0, MediaBrowserProtocol.EXTRA_SESSION_BINDER, r5);
        L14:
            int r1 = r13.getInt(MediaBrowserProtocol.EXTRA_CALLING_PID, -1);
            r13.remove(MediaBrowserProtocol.EXTRA_CALLING_PID);
            r6 = r1;
            goto L16
        L11:
            r5 = r4.asBinder();
            goto L12
        L13:
            this.mRootExtrasList.add(r0);
            goto L14
        }

        public void onLoadChildren(final String r4, final ResultWrapper<List<Parcel>> r5) {
            Result<List<MediaBrowserCompat.MediaItem>> r0 = new C01582(this, r4, r5);
            MediaBrowserServiceCompat r1 = this.this$0;
            r1.mCurConnection = this.this$0.mConnectionFromFwk;
            this.this$0.onLoadChildren(r4, r0);
            this.this$0.mCurConnection = null;
        }

        @Override // androidx.media.MediaBrowserServiceCompat.MediaBrowserServiceImpl
        public void setSessionToken(final MediaSessionCompat.Token r3) {
            this.this$0.mHandler.postOrRun(new RunnableC01571(this, r3));
        }

        void setSessionTokenOnHandler(MediaSessionCompat.Token r6) {
            if (this.mRootExtrasList.isEmpty() == true) goto L11;
            IMediaSession r0 = r6.getExtraBinder();
            if (r0 == null) goto L10;
            Iterator<Bundle> r1 = this.mRootExtrasList.iterator();
        L8:
            if (r1.hasNext() == false) goto L10;
            BundleCompat.putBinder(r1.next(), MediaBrowserProtocol.EXTRA_SESSION_BINDER, r0.asBinder());
        L10:
            this.mRootExtrasList.clear();
        L11:
            this.mServiceFwk.setSessionToken((MediaSession.Token) r6.getToken());
        }
    }

    class MediaBrowserServiceImplApi23 extends MediaBrowserServiceImplApi21 {
        final /* synthetic */ MediaBrowserServiceCompat this$0;

        class MediaBrowserServiceApi23 extends MediaBrowserServiceImplApi21.MediaBrowserServiceApi21 {
            final /* synthetic */ MediaBrowserServiceImplApi23 this$1;

            MediaBrowserServiceApi23(MediaBrowserServiceImplApi23 r1, Context r2) {
                this.this$1 = r1;
                super(r1, r2);
            }

            @Override // android.service.media.MediaBrowserService
            public void onLoadItem(String r3, MediaBrowserService.Result<MediaBrowser.MediaItem> r4) {
                this.this$1.onLoadItem(r3, new ResultWrapper(r4));
            }
        }

        MediaBrowserServiceImplApi23(MediaBrowserServiceCompat r1) {
            this.this$0 = r1;
            super(r1);
        }

        @Override // androidx.media.MediaBrowserServiceCompat.MediaBrowserServiceImplApi21, androidx.media.MediaBrowserServiceCompat.MediaBrowserServiceImpl
        public void onCreate() {
            this.mServiceFwk = new MediaBrowserServiceApi23(this, this.this$0);
            this.mServiceFwk.onCreate();
        }

        public void onLoadItem(final String r4, final ResultWrapper<Parcel> r5) {
            Result<MediaBrowserCompat.MediaItem> r0 = new C01611(this, r4, r5);
            MediaBrowserServiceCompat r1 = this.this$0;
            r1.mCurConnection = this.this$0.mConnectionFromFwk;
            this.this$0.onLoadItem(r4, r0);
            this.this$0.mCurConnection = null;
        }
    }

    class MediaBrowserServiceImplApi26 extends MediaBrowserServiceImplApi23 {
        final /* synthetic */ MediaBrowserServiceCompat this$0;

        class MediaBrowserServiceApi26 extends MediaBrowserServiceImplApi23.MediaBrowserServiceApi23 {
            final /* synthetic */ MediaBrowserServiceImplApi26 this$1;

            MediaBrowserServiceApi26(MediaBrowserServiceImplApi26 r1, Context r2) {
                this.this$1 = r1;
                super(r1, r2);
            }

            @Override // android.service.media.MediaBrowserService
            public void onLoadChildren(String r3, MediaBrowserService.Result<List<MediaBrowser.MediaItem>> r4, Bundle r5) {
                MediaSessionCompat.ensureClassLoader(r5);
                MediaBrowserServiceCompat r0 = this.this$1.this$0;
                r0.mCurConnection = this.this$1.this$0.mConnectionFromFwk;
                this.this$1.onLoadChildren(r3, new ResultWrapper(r4), r5);
                this.this$1.this$0.mCurConnection = null;
            }
        }

        MediaBrowserServiceImplApi26(MediaBrowserServiceCompat r1) {
            this.this$0 = r1;
            super(r1);
        }

        @Override // androidx.media.MediaBrowserServiceCompat.MediaBrowserServiceImplApi21, androidx.media.MediaBrowserServiceCompat.MediaBrowserServiceImpl
        public Bundle getBrowserRootHints() {
            if (this.this$0.mCurConnection == null) goto L14;
            if (this.this$0.mCurConnection != this.this$0.mConnectionFromFwk) goto L9;
            return this.mServiceFwk.getBrowserRootHints();
        L9:
            if (this.this$0.mCurConnection.rootHints != null) goto L12;
            return null;
        L12:
            return new Bundle(this.this$0.mCurConnection.rootHints);
        L14:
            throw new IllegalStateException("This should be called inside of onGetRoot, onLoadChildren, onLoadItem, onSearch, or onCustomAction methods");
        }

        @Override // androidx.media.MediaBrowserServiceCompat.MediaBrowserServiceImplApi21
        void notifyChildrenChangedForFramework(String r2, Bundle r3) {
            if (r3 == null) goto L4;
            this.mServiceFwk.notifyChildrenChanged(r2, r3);
            return;
        L4:
            super.notifyChildrenChangedForFramework(r2, r3);
        }

        @Override // androidx.media.MediaBrowserServiceCompat.MediaBrowserServiceImplApi23, androidx.media.MediaBrowserServiceCompat.MediaBrowserServiceImplApi21, androidx.media.MediaBrowserServiceCompat.MediaBrowserServiceImpl
        public void onCreate() {
            this.mServiceFwk = new MediaBrowserServiceApi26(this, this.this$0);
            this.mServiceFwk.onCreate();
        }

        public void onLoadChildren(final String r4, final ResultWrapper<List<Parcel>> r5, final Bundle r6) {
            Result<List<MediaBrowserCompat.MediaItem>> r0 = new C01621(this, r4, r5, r6);
            MediaBrowserServiceCompat r1 = this.this$0;
            r1.mCurConnection = this.this$0.mConnectionFromFwk;
            this.this$0.onLoadChildren(r4, r0, r6);
            this.this$0.mCurConnection = null;
        }
    }

    class MediaBrowserServiceImplApi28 extends MediaBrowserServiceImplApi26 {
        final /* synthetic */ MediaBrowserServiceCompat this$0;

        MediaBrowserServiceImplApi28(MediaBrowserServiceCompat r1) {
            this.this$0 = r1;
            super(r1);
        }

        @Override // androidx.media.MediaBrowserServiceCompat.MediaBrowserServiceImplApi21, androidx.media.MediaBrowserServiceCompat.MediaBrowserServiceImpl
        public MediaSessionManager.RemoteUserInfo getCurrentBrowserInfo() {
            if (this.this$0.mCurConnection == null) goto L11;
            if (this.this$0.mCurConnection != this.this$0.mConnectionFromFwk) goto L9;
            return new MediaSessionManager.RemoteUserInfo(this.mServiceFwk.getCurrentBrowserInfo());
        L9:
            return this.this$0.mCurConnection.browserInfo;
        L11:
            throw new IllegalStateException("This should be called inside of onGetRoot, onLoadChildren, onLoadItem, onSearch, or onCustomAction methods");
        }
    }

    class MediaBrowserServiceImplBase implements MediaBrowserServiceImpl {
        private Messenger mMessenger;
        final /* synthetic */ MediaBrowserServiceCompat this$0;

        MediaBrowserServiceImplBase(MediaBrowserServiceCompat r1) {
            this.this$0 = r1;
        }

        @Override // androidx.media.MediaBrowserServiceCompat.MediaBrowserServiceImpl
        public Bundle getBrowserRootHints() {
            if (this.this$0.mCurConnection == null) goto L10;
            if (this.this$0.mCurConnection.rootHints != null) goto L8;
            return null;
        L8:
            return new Bundle(this.this$0.mCurConnection.rootHints);
        L10:
            throw new IllegalStateException("This should be called inside of onLoadChildren, onLoadItem, onSearch, or onCustomAction methods");
        }

        @Override // androidx.media.MediaBrowserServiceCompat.MediaBrowserServiceImpl
        public MediaSessionManager.RemoteUserInfo getCurrentBrowserInfo() {
            if (this.this$0.mCurConnection == null) goto L7;
            return this.this$0.mCurConnection.browserInfo;
        L7:
            throw new IllegalStateException("This should be called inside of onLoadChildren, onLoadItem, onSearch, or onCustomAction methods");
        }

        @Override // androidx.media.MediaBrowserServiceCompat.MediaBrowserServiceImpl
        public void notifyChildrenChanged(final MediaSessionManager.RemoteUserInfo r3, final String r4, final Bundle r5) {
            this.this$0.mHandler.post(new RunnableC01653(this, r3, r4, r5));
        }

        @Override // androidx.media.MediaBrowserServiceCompat.MediaBrowserServiceImpl
        public void notifyChildrenChanged(final String r3, final Bundle r4) {
            this.this$0.mHandler.post(new RunnableC01642(this, r3, r4));
        }

        void notifyChildrenChangedOnHandler(ConnectionRecord r6, String r7, Bundle r8) {
            List<Pair<IBinder, Bundle>> r0 = r6.subscriptions.get(r7);
            if (r0 == null) goto L11;
            Iterator<Pair<IBinder, Bundle>> r1 = r0.iterator();
        L6:
            if (r1.hasNext() == false) goto L17;
            Pair<IBinder, Bundle> r2 = r1.next();
            if (MediaBrowserCompatUtils.hasDuplicatedItems(r8, r2.second) == false) goto L6;
            this.this$0.performLoadChildren(r7, r6, r2.second, r8);
            goto L6
        L17:
            return;
        }

        @Override // androidx.media.MediaBrowserServiceCompat.MediaBrowserServiceImpl
        public IBinder onBind(Intent r3) {
            if (MediaBrowserServiceCompat.SERVICE_INTERFACE.equals(r3.getAction()) == true) goto L5;
            return null;
        L5:
            return this.mMessenger.getBinder();
        }

        @Override // androidx.media.MediaBrowserServiceCompat.MediaBrowserServiceImpl
        public void onCreate() {
            this.mMessenger = new Messenger(this.this$0.mHandler);
        }

        @Override // androidx.media.MediaBrowserServiceCompat.MediaBrowserServiceImpl
        public void setSessionToken(final MediaSessionCompat.Token r3) {
            this.this$0.mHandler.post(new RunnableC01631(this, r3));
        }
    }

    public static class Result<T> {
        private final Object mDebug;
        private boolean mDetachCalled;
        private int mFlags;
        private boolean mSendErrorCalled;
        private boolean mSendResultCalled;

        Result(Object r1) {
            this.mDebug = r1;
        }

        private void checkExtraFields(Bundle r4) {
            if (r4 != null) goto L5;
            return;
        L5:
            if (r4.containsKey(MediaBrowserCompat.EXTRA_DOWNLOAD_PROGRESS) == false) goto L13;
            float r0 = r4.getFloat(MediaBrowserCompat.EXTRA_DOWNLOAD_PROGRESS);
            if (r0 < (-1.0E-5f)) goto L12;
            if (r0 > 1.00001f) goto L12;
            return;
        L12:
            throw new IllegalArgumentException("The value of the EXTRA_DOWNLOAD_PROGRESS field must be a float number within [0.0, 1.0]");
        }

        public void detach() {
            if (this.mDetachCalled == true) goto L15;
            if (this.mSendResultCalled == true) goto L13;
            if (this.mSendErrorCalled == true) goto L11;
            this.mDetachCalled = true;
            return;
        L11:
            throw new IllegalStateException("detach() called when sendError() had already been called for: " + this.mDebug);
        L13:
            throw new IllegalStateException("detach() called when sendResult() had already been called for: " + this.mDebug);
        L15:
            throw new IllegalStateException("detach() called when detach() had already been called for: " + this.mDebug);
        }

        int getFlags() {
            return this.mFlags;
        }

        boolean isDone() {
            if (this.mDetachCalled == false) goto L5;
        L10:
            return true;
        L5:
            if (this.mSendResultCalled == true) goto L10;
            if (this.mSendErrorCalled == true) goto L10;
            return false;
        }

        void onErrorSent(Bundle r4) {
            throw new UnsupportedOperationException("It is not supported to send an error for " + this.mDebug);
        }

        void onProgressUpdateSent(Bundle r4) {
            throw new UnsupportedOperationException("It is not supported to send an interim update for " + this.mDebug);
        }

        void onResultSent(T r1) {
        }

        public void sendError(Bundle r4) {
            if (this.mSendResultCalled == true) goto L9;
            if (this.mSendErrorCalled == true) goto L9;
            this.mSendErrorCalled = true;
            onErrorSent(r4);
            return;
        L9:
            throw new IllegalStateException("sendError() called when either sendResult() or sendError() had already been called for: " + this.mDebug);
        }

        public void sendProgressUpdate(Bundle r4) {
            if (this.mSendResultCalled == true) goto L9;
            if (this.mSendErrorCalled == true) goto L9;
            checkExtraFields(r4);
            onProgressUpdateSent(r4);
            return;
        L9:
            throw new IllegalStateException("sendProgressUpdate() called when either sendResult() or sendError() had already been called for: " + this.mDebug);
        }

        public void sendResult(T r4) {
            if (this.mSendResultCalled == true) goto L9;
            if (this.mSendErrorCalled == true) goto L9;
            this.mSendResultCalled = true;
            onResultSent(r4);
            return;
        L9:
            throw new IllegalStateException("sendResult() called when either sendResult() or sendError() had already been called for: " + this.mDebug);
        }

        void setFlags(int r1) {
            this.mFlags = r1;
        }
    }

    static class ResultWrapper<T> {
        MediaBrowserService.Result mResultFwk;

        ResultWrapper(MediaBrowserService.Result r1) {
            this.mResultFwk = r1;
        }

        public void detach() {
            this.mResultFwk.detach();
        }

        List<MediaBrowser.MediaItem> parcelListToItemList(List<Parcel> r5) {
            if (r5 != null) goto L5;
            return null;
        L5:
            ArrayList r0 = new ArrayList(r5.size());
            Iterator<Parcel> r1 = r5.iterator();
        L7:
            if (r1.hasNext() == false) goto L9;
            Parcel r2 = r1.next();
            r2.setDataPosition(0);
            r0.add((MediaBrowser.MediaItem) MediaBrowser.MediaItem.CREATOR.createFromParcel(r2));
            r2.recycle();
            goto L7
        L9:
            return r0;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void sendResult(T r4) {
            if ((r4 instanceof List) == false) goto L6;
            this.mResultFwk.sendResult(parcelListToItemList((List) r4));
            return;
        L6:
            if ((r4 instanceof Parcel) == false) goto L8;
            Parcel r0 = (Parcel) r4;
            r0.setDataPosition(0);
            this.mResultFwk.sendResult(MediaBrowser.MediaItem.CREATOR.createFromParcel(r0));
            r0.recycle();
            return;
        L8:
            this.mResultFwk.sendResult(null);
        }
    }

    private class ServiceBinderImpl {
        final /* synthetic */ MediaBrowserServiceCompat this$0;

        ServiceBinderImpl(MediaBrowserServiceCompat r1) {
            this.this$0 = r1;
        }

        public void addSubscription(final String r8, final IBinder r9, final Bundle r10, final ServiceCallbacks r11) {
            this.this$0.mHandler.postOrRun(new RunnableC01683(this, r11, r8, r9, r10));
        }

        public void connect(final String r9, final int r10, final int r11, final Bundle r12, final ServiceCallbacks r13) {
            if (this.this$0.isValidPackage(r9, r11) == false) goto L7;
            this.this$0.mHandler.postOrRun(new RunnableC01661(this, r13, r9, r10, r11, r12));
            return;
        L7:
            throw new IllegalArgumentException("Package/uid mismatch: uid=" + r11 + " package=" + r9);
        }

        public void disconnect(final ServiceCallbacks r3) {
            this.this$0.mHandler.postOrRun(new RunnableC01672(this, r3));
        }

        public void getMediaItem(final String r3, final ResultReceiver r4, final ServiceCallbacks r5) {
            if (TextUtils.isEmpty(r3) == true) goto L8;
            if (r4 == null) goto L9;
            this.this$0.mHandler.postOrRun(new RunnableC01705(this, r5, r3, r4));
            return;
        L9:
            return;
        }

        public void registerCallbacks(final ServiceCallbacks r9, final String r10, final int r11, final int r12, final Bundle r13) {
            this.this$0.mHandler.postOrRun(new RunnableC01716(this, r9, r12, r10, r11, r13));
        }

        public void removeSubscription(final String r3, final IBinder r4, final ServiceCallbacks r5) {
            this.this$0.mHandler.postOrRun(new RunnableC01694(this, r5, r3, r4));
        }

        public void search(final String r8, final Bundle r9, final ResultReceiver r10, final ServiceCallbacks r11) {
            if (TextUtils.isEmpty(r8) == true) goto L9;
            if (r10 == null) goto L10;
            this.this$0.mHandler.postOrRun(new RunnableC01738(this, r11, r8, r9, r10));
            return;
        L10:
            return;
        }

        public void sendCustomAction(final String r8, final Bundle r9, final ResultReceiver r10, final ServiceCallbacks r11) {
            if (TextUtils.isEmpty(r8) == true) goto L9;
            if (r10 == null) goto L10;
            this.this$0.mHandler.postOrRun(new RunnableC01749(this, r11, r8, r9, r10));
            return;
        L10:
            return;
        }

        public void unregisterCallbacks(final ServiceCallbacks r3) {
            this.this$0.mHandler.postOrRun(new RunnableC01727(this, r3));
        }
    }

    private interface ServiceCallbacks {
        IBinder asBinder();

        void onConnect(String r1, MediaSessionCompat.Token r2, Bundle r3) throws RemoteException;

        void onConnectFailed() throws RemoteException;

        void onLoadChildren(String r1, List<MediaBrowserCompat.MediaItem> r2, Bundle r3, Bundle r4) throws RemoteException;
    }

    private static class ServiceCallbacksCompat implements ServiceCallbacks {
        final Messenger mCallbacks;

        ServiceCallbacksCompat(Messenger r1) {
            this.mCallbacks = r1;
        }

        private void sendRequest(int r3, Bundle r4) throws RemoteException {
            Message r0 = Message.obtain();
            r0.what = r3;
            r0.arg1 = 2;
            r0.setData(r4);
            this.mCallbacks.send(r0);
        }

        @Override // androidx.media.MediaBrowserServiceCompat.ServiceCallbacks
        public IBinder asBinder() {
            return this.mCallbacks.getBinder();
        }

        @Override // androidx.media.MediaBrowserServiceCompat.ServiceCallbacks
        public void onConnect(String r3, MediaSessionCompat.Token r4, Bundle r5) throws RemoteException {
            if (r5 != null) goto L4;
            r5 = new Bundle();
        L4:
            r5.putInt(MediaBrowserProtocol.EXTRA_SERVICE_VERSION, 2);
            Bundle r0 = new Bundle();
            r0.putString(MediaBrowserProtocol.DATA_MEDIA_ITEM_ID, r3);
            r0.putParcelable(MediaBrowserProtocol.DATA_MEDIA_SESSION_TOKEN, r4);
            r0.putBundle(MediaBrowserProtocol.DATA_ROOT_HINTS, r5);
            sendRequest(1, r0);
        }

        @Override // androidx.media.MediaBrowserServiceCompat.ServiceCallbacks
        public void onConnectFailed() throws RemoteException {
            sendRequest(2, null);
        }

        @Override // androidx.media.MediaBrowserServiceCompat.ServiceCallbacks
        public void onLoadChildren(String r4, List<MediaBrowserCompat.MediaItem> r5, Bundle r6, Bundle r7) throws RemoteException {
            Bundle r0 = new Bundle();
            r0.putString(MediaBrowserProtocol.DATA_MEDIA_ITEM_ID, r4);
            r0.putBundle(MediaBrowserProtocol.DATA_OPTIONS, r6);
            r0.putBundle(MediaBrowserProtocol.DATA_NOTIFY_CHILDREN_CHANGED_OPTIONS, r7);
            if (r5 != null) goto L5;
        L9:
            sendRequest(3, r0);
            return;
        L5:
            if ((r5 instanceof ArrayList) == false) goto L7;
            ArrayList<? extends Parcelable> r1 = (ArrayList) r5;
        L8:
            r0.putParcelableArrayList(MediaBrowserProtocol.DATA_MEDIA_ITEM_LIST, r1);
            goto L9
        L7:
            r1 = new ArrayList(r5);
            goto L8
        }
    }

    private static final class ServiceHandler extends Handler {
        private MediaBrowserServiceCompat mService;

        ServiceHandler(MediaBrowserServiceCompat r1) {
            this.mService = r1;
        }

        @Override // android.os.Handler
        public void handleMessage(Message r2) {
            if (this.mService == null) goto L5;
            this.mService.handleMessageInternal(r2);
            return;
        L5:
            removeCallbacksAndMessages(null);
        }

        public void postOrRun(Runnable r3) {
            if (Thread.currentThread() != getLooper().getThread()) goto L5;
            r3.run();
            return;
        L5:
            post(r3);
        }

        public void release() {
            this.mService = null;
        }

        @Override // android.os.Handler
        public boolean sendMessageAtTime(Message r5, long r6) {
            Bundle r0 = r5.getData();
            r0.setClassLoader(MediaBrowserCompat.class.getClassLoader());
            r0.putInt(MediaBrowserProtocol.DATA_CALLING_UID, Binder.getCallingUid());
            int r1 = Binder.getCallingPid();
            if (r1 <= 0) goto L6;
            r0.putInt(MediaBrowserProtocol.DATA_CALLING_PID, r1);
        L9:
            return super.sendMessageAtTime(r5, r6);
        L6:
            if (r0.containsKey(MediaBrowserProtocol.DATA_CALLING_PID) == true) goto L9;
            r0.putInt(MediaBrowserProtocol.DATA_CALLING_PID, -1);
            goto L9
        }
    }

    static {
        DEBUG = Log.isLoggable(TAG, 3);
    }

    public MediaBrowserServiceCompat() {
        this.mServiceBinderImpl = new ServiceBinderImpl(this);
        this.mConnectionFromFwk = new ConnectionRecord(this, MediaSessionManager.RemoteUserInfo.LEGACY_CONTROLLER, -1, -1, null, null);
        this.mPendingConnections = new ArrayList();
        this.mConnections = new ArrayMap();
        this.mHandler = new ServiceHandler(this);
    }

    void addSubscription(String r5, ConnectionRecord r6, IBinder r7, Bundle r8) {
        List<Pair<IBinder, Bundle>> r0 = r6.subscriptions.get(r5);
        if (r0 != null) goto L5;
        r0 = new ArrayList();
    L5:
        Iterator<Pair<IBinder, Bundle>> r1 = r0.iterator();
    L7:
        if (r1.hasNext() == false) goto L14;
        Pair<IBinder, Bundle> r2 = r1.next();
        if (r7 != r2.first) goto L7;
        if (MediaBrowserCompatUtils.areSameOptions(r8, r2.second) == false) goto L7;
        return;
    L14:
        r0.add(new Pair(r7, r8));
        r6.subscriptions.put(r5, r0);
        performLoadChildren(r5, r6, r8, null);
        this.mCurConnection = r6;
        onSubscribe(r5, r8);
        this.mCurConnection = null;
    }

    List<MediaBrowserCompat.MediaItem> applyOptions(List<MediaBrowserCompat.MediaItem> r6, Bundle r7) {
        if (r6 != null) goto L5;
        return null;
    L5:
        int r0 = r7.getInt(MediaBrowserCompat.EXTRA_PAGE, -1);
        int r2 = r7.getInt(MediaBrowserCompat.EXTRA_PAGE_SIZE, -1);
        if (r0 != (-1)) goto L9;
        if (r2 != (-1)) goto L9;
        return r6;
    L9:
        int r1 = r2 * r0;
        int r3 = r1 + r2;
        if (r0 < 0) goto L22;
        if (r2 < 1) goto L22;
        if (r1 >= r6.size()) goto L22;
        if (r3 <= r6.size()) goto L20;
        r3 = r6.size();
    L20:
        return r6.subList(r1, r3);
    L22:
        return Collections.emptyList();
    }

    public void attachToBaseContext(Context r1) {
        attachBaseContext(r1);
    }

    @Override // android.app.Service
    public void dump(FileDescriptor r1, PrintWriter r2, String[] r3) {
    }

    public final Bundle getBrowserRootHints() {
        return this.mImpl.getBrowserRootHints();
    }

    public final MediaSessionManager.RemoteUserInfo getCurrentBrowserInfo() {
        return this.mImpl.getCurrentBrowserInfo();
    }

    public MediaSessionCompat.Token getSessionToken() {
        return this.mSession;
    }

    void handleMessageInternal(Message r11) {
        Bundle r0 = r11.getData();
        switch(r11.what) {
            case 1: goto L13;
            case 2: goto L12;
            case 3: goto L11;
            case 4: goto L10;
            case 5: goto L9;
            case 6: goto L8;
            case 7: goto L7;
            case 8: goto L6;
            case 9: goto L5;
            default: goto L4;
        };
    L4:
        Log.w(TAG, "Unhandled message: " + r11 + "\n  Service version: 2\n  Client version: " + r11.arg1);
        return;
    L5:
        Bundle r1 = r0.getBundle(MediaBrowserProtocol.DATA_CUSTOM_ACTION_EXTRAS);
        MediaSessionCompat.ensureClassLoader(r1);
        this.mServiceBinderImpl.sendCustomAction(r0.getString(MediaBrowserProtocol.DATA_CUSTOM_ACTION), r1, (ResultReceiver) r0.getParcelable(MediaBrowserProtocol.DATA_RESULT_RECEIVER), new ServiceCallbacksCompat(r11.replyTo));
        return;
    L6:
        Bundle r2 = r0.getBundle(MediaBrowserProtocol.DATA_SEARCH_EXTRAS);
        MediaSessionCompat.ensureClassLoader(r2);
        this.mServiceBinderImpl.search(r0.getString(MediaBrowserProtocol.DATA_SEARCH_QUERY), r2, (ResultReceiver) r0.getParcelable(MediaBrowserProtocol.DATA_RESULT_RECEIVER), new ServiceCallbacksCompat(r11.replyTo));
        return;
    L7:
        this.mServiceBinderImpl.unregisterCallbacks(new ServiceCallbacksCompat(r11.replyTo));
        return;
    L8:
        Bundle r9 = r0.getBundle(MediaBrowserProtocol.DATA_ROOT_HINTS);
        MediaSessionCompat.ensureClassLoader(r9);
        this.mServiceBinderImpl.registerCallbacks(new ServiceCallbacksCompat(r11.replyTo), r0.getString(MediaBrowserProtocol.DATA_PACKAGE_NAME), r0.getInt(MediaBrowserProtocol.DATA_CALLING_PID), r0.getInt(MediaBrowserProtocol.DATA_CALLING_UID), r9);
        return;
    L9:
        this.mServiceBinderImpl.getMediaItem(r0.getString(MediaBrowserProtocol.DATA_MEDIA_ITEM_ID), (ResultReceiver) r0.getParcelable(MediaBrowserProtocol.DATA_RESULT_RECEIVER), new ServiceCallbacksCompat(r11.replyTo));
        return;
    L10:
        this.mServiceBinderImpl.removeSubscription(r0.getString(MediaBrowserProtocol.DATA_MEDIA_ITEM_ID), BundleCompat.getBinder(r0, MediaBrowserProtocol.DATA_CALLBACK_TOKEN), new ServiceCallbacksCompat(r11.replyTo));
        return;
    L11:
        Bundle r3 = r0.getBundle(MediaBrowserProtocol.DATA_OPTIONS);
        MediaSessionCompat.ensureClassLoader(r3);
        this.mServiceBinderImpl.addSubscription(r0.getString(MediaBrowserProtocol.DATA_MEDIA_ITEM_ID), BundleCompat.getBinder(r0, MediaBrowserProtocol.DATA_CALLBACK_TOKEN), r3, new ServiceCallbacksCompat(r11.replyTo));
        return;
    L12:
        this.mServiceBinderImpl.disconnect(new ServiceCallbacksCompat(r11.replyTo));
        return;
    L13:
        Bundle r8 = r0.getBundle(MediaBrowserProtocol.DATA_ROOT_HINTS);
        MediaSessionCompat.ensureClassLoader(r8);
        this.mServiceBinderImpl.connect(r0.getString(MediaBrowserProtocol.DATA_PACKAGE_NAME), r0.getInt(MediaBrowserProtocol.DATA_CALLING_PID), r0.getInt(MediaBrowserProtocol.DATA_CALLING_UID), r8, new ServiceCallbacksCompat(r11.replyTo));
    }

    boolean isValidPackage(String r7, int r8) {
        if (r7 != null) goto L5;
        return false;
    L5:
        String[] r2 = getPackageManager().getPackagesForUid(r8);
        int r3 = r2.length;
        int r4 = 0;
    L6:
        if (r4 >= r3) goto L12;
        if (r2[r4].equals(r7) == true) goto L9;
        r4 = r4 + 1;
        goto L6
    L9:
        return true;
    L12:
        return false;
    }

    public void notifyChildrenChanged(MediaSessionManager.RemoteUserInfo r3, String r4, Bundle r5) {
        if (r3 == null) goto L12;
        if (r4 == null) goto L10;
        if (r5 == null) goto L8;
        this.mImpl.notifyChildrenChanged(r3, r4, r5);
        return;
    L8:
        throw new IllegalArgumentException("options cannot be null in notifyChildrenChanged");
    L10:
        throw new IllegalArgumentException("parentId cannot be null in notifyChildrenChanged");
    L12:
        throw new IllegalArgumentException("remoteUserInfo cannot be null in notifyChildrenChanged");
    }

    public void notifyChildrenChanged(String r3) {
        if (r3 == null) goto L6;
        this.mImpl.notifyChildrenChanged(r3, null);
        return;
    L6:
        throw new IllegalArgumentException("parentId cannot be null in notifyChildrenChanged");
    }

    public void notifyChildrenChanged(String r3, Bundle r4) {
        if (r3 == null) goto L9;
        if (r4 == null) goto L7;
        this.mImpl.notifyChildrenChanged(r3, r4);
        return;
    L7:
        throw new IllegalArgumentException("options cannot be null in notifyChildrenChanged");
    L9:
        throw new IllegalArgumentException("parentId cannot be null in notifyChildrenChanged");
    }

    @Override // android.app.Service
    public IBinder onBind(Intent r2) {
        return this.mImpl.onBind(r2);
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT < 28) goto L6;
        this.mImpl = new MediaBrowserServiceImplApi28(this);
    L9:
        this.mImpl.onCreate();
        return;
    L6:
        if (Build.VERSION.SDK_INT < 26) goto L8;
        this.mImpl = new MediaBrowserServiceImplApi26(this);
        goto L9
    L8:
        this.mImpl = new MediaBrowserServiceImplApi23(this);
        goto L9
    }

    public void onCustomAction(String r2, Bundle r3, Result<Bundle> r4) {
        r4.sendError(null);
    }

    @Override // android.app.Service
    public void onDestroy() {
        this.mHandler.release();
    }

    public abstract BrowserRoot onGetRoot(String r1, int r2, Bundle r3);

    public abstract void onLoadChildren(String r1, Result<List<MediaBrowserCompat.MediaItem>> r2);

    public void onLoadChildren(String r2, Result<List<MediaBrowserCompat.MediaItem>> r3, Bundle r4) {
        r3.setFlags(1);
        onLoadChildren(r2, r3);
    }

    public void onLoadItem(String r2, Result<MediaBrowserCompat.MediaItem> r3) {
        r3.setFlags(2);
        r3.sendResult(null);
    }

    public void onSearch(String r2, Bundle r3, Result<List<MediaBrowserCompat.MediaItem>> r4) {
        r4.setFlags(4);
        r4.sendResult(null);
    }

    public void onSubscribe(String r1, Bundle r2) {
    }

    public void onUnsubscribe(String r1) {
    }

    void performCustomAction(final String r5, Bundle r6, ConnectionRecord r7, final ResultReceiver r8) {
        Result<Bundle> r0 = new C01554(this, r5, r8);
        this.mCurConnection = r7;
        onCustomAction(r5, r6, r0);
        this.mCurConnection = null;
        if (r0.isDone() == false) goto L6;
        return;
    L6:
        throw new IllegalStateException("onCustomAction must call detach() or sendResult() or sendError() before returning for action=" + r5 + " extras=" + r6);
    }

    void performLoadChildren(final String r8, final ConnectionRecord r9, final Bundle r10, final Bundle r11) {
        Result<List<MediaBrowserCompat.MediaItem>> r0 = new C01521(this, r8, r9, r8, r10, r11);
        this.mCurConnection = r9;
        if (r10 != null) goto L5;
        onLoadChildren(r8, r0);
    L6:
        this.mCurConnection = null;
        if (r0.isDone() == false) goto L10;
        return;
    L10:
        throw new IllegalStateException("onLoadChildren must call detach() or sendResult() before returning for package=" + r9.pkg + " id=" + r8);
    L5:
        onLoadChildren(r8, r0, r10);
        goto L6
    }

    void performLoadItem(final String r5, ConnectionRecord r6, final ResultReceiver r7) {
        Result<MediaBrowserCompat.MediaItem> r0 = new C01532(this, r5, r7);
        this.mCurConnection = r6;
        onLoadItem(r5, r0);
        this.mCurConnection = null;
        if (r0.isDone() == false) goto L6;
        return;
    L6:
        throw new IllegalStateException("onLoadItem must call detach() or sendResult() before returning for id=" + r5);
    }

    void performSearch(final String r5, Bundle r6, ConnectionRecord r7, final ResultReceiver r8) {
        Result<List<MediaBrowserCompat.MediaItem>> r0 = new C01543(this, r5, r8);
        this.mCurConnection = r7;
        onSearch(r5, r6, r0);
        this.mCurConnection = null;
        if (r0.isDone() == false) goto L6;
        return;
    L6:
        throw new IllegalStateException("onSearch must call detach() or sendResult() before returning for query=" + r5);
    }

    boolean removeSubscription(String r6, ConnectionRecord r7, IBinder r8) {
        if (r8 == null) goto L5;
        boolean r1 = false;
        List<Pair<IBinder, Bundle>> r2 = r7.subscriptions.get(r6);     // Catch: Throwable -> L24
        if (r2 == null) goto L22;
        Iterator<Pair<IBinder, Bundle>> r3 = r2.iterator();     // Catch: Throwable -> L24
    L15:
        if (r3.hasNext() == false) goto L20;
        if (r8 != r3.next().first) goto L15;
        r1 = true;
        r3.remove();     // Catch: Throwable -> L24
        goto L15
    L20:
        if (r2.size() != 0) goto L22;
        r7.subscriptions.remove(r6);     // Catch: Throwable -> L24
    L22:
        this.mCurConnection = r7;
        onUnsubscribe(r6);
        this.mCurConnection = null;
        return r1;
    L5:
        if (r7.subscriptions.remove(r6) == null) goto L7;
        boolean r4 = true;
    L8:
        this.mCurConnection = r7;
        onUnsubscribe(r6);
        this.mCurConnection = null;
        return r4;
    L7:
        r4 = false;
    L24:
        th = move-exception;
        this.mCurConnection = r7;
        onUnsubscribe(r6);
        this.mCurConnection = null;
        throw th;
    }

    public void setSessionToken(MediaSessionCompat.Token r3) {
        if (r3 == null) goto L10;
        if (this.mSession != null) goto L8;
        this.mSession = r3;
        this.mImpl.setSessionToken(r3);
        return;
    L8:
        throw new IllegalStateException("The session token has already been set");
    L10:
        throw new IllegalArgumentException("Session token may not be null");
    }
}
