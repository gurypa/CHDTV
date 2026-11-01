package android.support.v4.media;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.media.MediaDescription;
import android.media.browse.MediaBrowser;
import android.os.BadParcelableException;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Process;
import android.os.RemoteException;
import android.support.v4.media.session.IMediaSession;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;
import androidx.collection.ArrayMap;
import androidx.core.app.BundleCompat;
import androidx.media.MediaBrowserCompatUtils;
import androidx.media.MediaBrowserProtocol;
import androidx.media.MediaBrowserServiceCompat;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class MediaBrowserCompat {
    public static final String CUSTOM_ACTION_DOWNLOAD = "android.support.v4.media.action.DOWNLOAD";
    public static final String CUSTOM_ACTION_REMOVE_DOWNLOADED_FILE = "android.support.v4.media.action.REMOVE_DOWNLOADED_FILE";
    static final boolean DEBUG = false;
    public static final String EXTRA_DOWNLOAD_PROGRESS = "android.media.browse.extra.DOWNLOAD_PROGRESS";
    public static final String EXTRA_MEDIA_ID = "android.media.browse.extra.MEDIA_ID";
    public static final String EXTRA_PAGE = "android.media.browse.extra.PAGE";
    public static final String EXTRA_PAGE_SIZE = "android.media.browse.extra.PAGE_SIZE";
    static final String TAG = "MediaBrowserCompat";
    private final MediaBrowserImpl mImpl;

    private static class Api21Impl {
        private Api21Impl() {
        }

        static MediaDescription getDescription(MediaBrowser.MediaItem r1) {
            return r1.getDescription();
        }

        static int getFlags(MediaBrowser.MediaItem r1) {
            return r1.getFlags();
        }
    }

    private static class CallbackHandler extends Handler {
        private final WeakReference<MediaBrowserServiceCallbackImpl> mCallbackImplRef;
        private WeakReference<Messenger> mCallbacksMessengerRef;

        CallbackHandler(MediaBrowserServiceCallbackImpl r2) {
            this.mCallbackImplRef = new WeakReference(r2);
        }

        @Override // android.os.Handler
        public void handleMessage(Message r11) {
            if (this.mCallbacksMessengerRef != null) goto L5;
            return;
        L5:
            if (this.mCallbacksMessengerRef.get() != null) goto L7;
            return;
        L7:
            if (this.mCallbackImplRef.get() == null) goto L31;
            Bundle r2 = r11.getData();
            MediaSessionCompat.ensureClassLoader(r2);
            MediaBrowserServiceCallbackImpl r3 = this.mCallbackImplRef.get();
            Messenger r4 = this.mCallbacksMessengerRef.get();
            switch(r11.what) {
                case 1: goto L16;
                case 2: goto L15;
                case 3: goto L14;
                default: goto L17;
            };
        L14:
            Bundle r7 = r2.getBundle(MediaBrowserProtocol.DATA_OPTIONS);     // Catch: BadParcelableException -> L19
            MediaSessionCompat.ensureClassLoader(r7);     // Catch: BadParcelableException -> L19
            Bundle r8 = r2.getBundle(MediaBrowserProtocol.DATA_NOTIFY_CHILDREN_CHANGED_OPTIONS);     // Catch: BadParcelableException -> L19
            MediaSessionCompat.ensureClassLoader(r8);     // Catch: BadParcelableException -> L19
            r3.onLoadChildren(r4, r2.getString(MediaBrowserProtocol.DATA_MEDIA_ITEM_ID), r2.getParcelableArrayList(MediaBrowserProtocol.DATA_MEDIA_ITEM_LIST), r7, r8);     // Catch: BadParcelableException -> L19
        L25:
            return;
        L15:
            r3.onConnectionFailed(r4);     // Catch: BadParcelableException -> L19
            goto L25
        L16:
            Bundle r0 = r2.getBundle(MediaBrowserProtocol.DATA_ROOT_HINTS);     // Catch: BadParcelableException -> L19
            MediaSessionCompat.ensureClassLoader(r0);     // Catch: BadParcelableException -> L19
            r3.onServiceConnected(r4, r2.getString(MediaBrowserProtocol.DATA_MEDIA_ITEM_ID), (MediaSessionCompat.Token) r2.getParcelable(MediaBrowserProtocol.DATA_MEDIA_SESSION_TOKEN), r0);     // Catch: BadParcelableException -> L19
            goto L25
        L17:
            Log.w(MediaBrowserCompat.TAG, "Unhandled message: " + r11 + "\n  Client version: 1\n  Service version: " + r11.arg1);     // Catch: BadParcelableException -> L19
        L20:
            Log.e(MediaBrowserCompat.TAG, "Could not unparcel the data.");
            if (r11.what != 1) goto L29;
            r3.onConnectionFailed(r4);
            return;
        L29:
            return;
        }

        void setCallbacksMessenger(Messenger r2) {
            this.mCallbacksMessengerRef = new WeakReference(r2);
        }
    }

    public static class ConnectionCallback {
        final MediaBrowser.ConnectionCallback mConnectionCallbackFwk;
        ConnectionCallbackInternal mConnectionCallbackInternal;

        private class ConnectionCallbackApi21 extends MediaBrowser.ConnectionCallback {
            final /* synthetic */ ConnectionCallback this$0;

            ConnectionCallbackApi21(ConnectionCallback r1) {
                this.this$0 = r1;
            }

            @Override // android.media.browse.MediaBrowser.ConnectionCallback
            public void onConnected() {
                if (this.this$0.mConnectionCallbackInternal == null) goto L5;
                this.this$0.mConnectionCallbackInternal.onConnected();
            L5:
                this.this$0.onConnected();
            }

            @Override // android.media.browse.MediaBrowser.ConnectionCallback
            public void onConnectionFailed() {
                if (this.this$0.mConnectionCallbackInternal == null) goto L5;
                this.this$0.mConnectionCallbackInternal.onConnectionFailed();
            L5:
                this.this$0.onConnectionFailed();
            }

            @Override // android.media.browse.MediaBrowser.ConnectionCallback
            public void onConnectionSuspended() {
                if (this.this$0.mConnectionCallbackInternal == null) goto L5;
                this.this$0.mConnectionCallbackInternal.onConnectionSuspended();
            L5:
                this.this$0.onConnectionSuspended();
            }
        }

        interface ConnectionCallbackInternal {
            void onConnected();

            void onConnectionFailed();

            void onConnectionSuspended();
        }

        public ConnectionCallback() {
            this.mConnectionCallbackFwk = new ConnectionCallbackApi21(this);
        }

        public void onConnected() {
        }

        public void onConnectionFailed() {
        }

        public void onConnectionSuspended() {
        }

        void setInternalConnectionCallback(ConnectionCallbackInternal r1) {
            this.mConnectionCallbackInternal = r1;
        }
    }

    public static abstract class CustomActionCallback {
        public CustomActionCallback() {
        }

        public void onError(String r1, Bundle r2, Bundle r3) {
        }

        public void onProgressUpdate(String r1, Bundle r2, Bundle r3) {
        }

        public void onResult(String r1, Bundle r2, Bundle r3) {
        }
    }

    private static class CustomActionResultReceiver extends ResultReceiver {
        private final String mAction;
        private final CustomActionCallback mCallback;
        private final Bundle mExtras;

        CustomActionResultReceiver(String r1, Bundle r2, CustomActionCallback r3, Handler r4) {
            super(r4);
            this.mAction = r1;
            this.mExtras = r2;
            this.mCallback = r3;
        }

        @Override // android.support.v4.os.ResultReceiver
        protected void onReceiveResult(int r4, Bundle r5) {
            if (this.mCallback != null) goto L5;
            return;
        L5:
            MediaSessionCompat.ensureClassLoader(r5);
            switch(r4) {
                case -1: goto L10;
                case 0: goto L9;
                case 1: goto L8;
                default: goto L7;
            };
        L7:
            Log.w(MediaBrowserCompat.TAG, "Unknown result code: " + r4 + " (extras=" + this.mExtras + ", resultData=" + r5 + ")");
            return;
        L8:
            this.mCallback.onProgressUpdate(this.mAction, this.mExtras, r5);
            return;
        L9:
            this.mCallback.onResult(this.mAction, this.mExtras, r5);
            return;
        L10:
            this.mCallback.onError(this.mAction, this.mExtras, r5);
        }
    }

    public static abstract class ItemCallback {
        final MediaBrowser.ItemCallback mItemCallbackFwk;

        private class ItemCallbackApi23 extends MediaBrowser.ItemCallback {
            final /* synthetic */ ItemCallback this$0;

            ItemCallbackApi23(ItemCallback r1) {
                this.this$0 = r1;
            }

            @Override // android.media.browse.MediaBrowser.ItemCallback
            public void onError(String r2) {
                this.this$0.onError(r2);
            }

            @Override // android.media.browse.MediaBrowser.ItemCallback
            public void onItemLoaded(MediaBrowser.MediaItem r3) {
                this.this$0.onItemLoaded(MediaItem.fromMediaItem(r3));
            }
        }

        public ItemCallback() {
            this.mItemCallbackFwk = new ItemCallbackApi23(this);
        }

        public void onError(String r1) {
        }

        public void onItemLoaded(MediaItem r1) {
        }
    }

    private static class ItemReceiver extends ResultReceiver {
        private final ItemCallback mCallback;
        private final String mMediaId;

        ItemReceiver(String r1, ItemCallback r2, Handler r3) {
            super(r3);
            this.mMediaId = r1;
            this.mCallback = r2;
        }

        @Override // android.support.v4.os.ResultReceiver
        protected void onReceiveResult(int r4, Bundle r5) {
            if (r5 == null) goto L4;
            r5 = MediaSessionCompat.unparcelWithClassLoader(r5);
        L4:
            if (r4 != 0) goto L17;
            if (r5 == null) goto L17;
            if (r5.containsKey(MediaBrowserServiceCompat.KEY_MEDIA_ITEM) == false) goto L17;
            Parcelable r0 = r5.getParcelable(MediaBrowserServiceCompat.KEY_MEDIA_ITEM);
            if (r0 != null) goto L12;
        L15:
            this.mCallback.onItemLoaded((MediaItem) r0);
            return;
        L12:
            if ((r0 instanceof MediaItem) == true) goto L15;
            this.mCallback.onError(this.mMediaId);
            return;
        L17:
            this.mCallback.onError(this.mMediaId);
        }
    }

    interface MediaBrowserImpl {
        void connect();

        void disconnect();

        Bundle getExtras();

        void getItem(String r1, ItemCallback r2);

        Bundle getNotifyChildrenChangedOptions();

        String getRoot();

        ComponentName getServiceComponent();

        MediaSessionCompat.Token getSessionToken();

        boolean isConnected();

        void search(String r1, Bundle r2, SearchCallback r3);

        void sendCustomAction(String r1, Bundle r2, CustomActionCallback r3);

        void subscribe(String r1, Bundle r2, SubscriptionCallback r3);

        void unsubscribe(String r1, SubscriptionCallback r2);
    }

    static class MediaBrowserImplApi21 implements MediaBrowserImpl, MediaBrowserServiceCallbackImpl, ConnectionCallback.ConnectionCallbackInternal {
        protected final MediaBrowser mBrowserFwk;
        protected Messenger mCallbacksMessenger;
        final Context mContext;
        protected final CallbackHandler mHandler;
        private MediaSessionCompat.Token mMediaSessionToken;
        private Bundle mNotifyChildrenChangedOptions;
        protected final Bundle mRootHints;
        protected ServiceBinderWrapper mServiceBinderWrapper;
        protected int mServiceVersion;
        private final ArrayMap<String, Subscription> mSubscriptions;

        MediaBrowserImplApi21(Context r4, ComponentName r5, ConnectionCallback r6, Bundle r7) {
            this.mHandler = new CallbackHandler(this);
            this.mSubscriptions = new ArrayMap();
            this.mContext = r4;
            if (r7 == null) goto L5;
            Bundle r0 = new Bundle(r7);
        L6:
            this.mRootHints = r0;
            this.mRootHints.putInt(MediaBrowserProtocol.EXTRA_CLIENT_VERSION, 1);
            this.mRootHints.putInt(MediaBrowserProtocol.EXTRA_CALLING_PID, Process.myPid());
            r6.setInternalConnectionCallback(this);
            this.mBrowserFwk = new MediaBrowser(r4, r5, r6.mConnectionCallbackFwk, this.mRootHints);
            return;
        L5:
            r0 = new Bundle();
            goto L6
        }

        @Override // android.support.v4.media.MediaBrowserCompat.MediaBrowserImpl
        public void connect() {
            this.mBrowserFwk.connect();
        }

        @Override // android.support.v4.media.MediaBrowserCompat.MediaBrowserImpl
        public void disconnect() {
            if (this.mServiceBinderWrapper != null) goto L5;
        L10:
            this.mBrowserFwk.disconnect();
            return;
        L5:
            if (this.mCallbacksMessenger == null) goto L10;
            this.mServiceBinderWrapper.unregisterCallbackMessenger(this.mCallbacksMessenger);     // Catch: RemoteException -> L8
        L9:
            Log.i(MediaBrowserCompat.TAG, "Remote error unregistering client messenger.");
            goto L10
        }

        @Override // android.support.v4.media.MediaBrowserCompat.MediaBrowserImpl
        public Bundle getExtras() {
            return this.mBrowserFwk.getExtras();
        }

        @Override // android.support.v4.media.MediaBrowserCompat.MediaBrowserImpl
        public void getItem(final String r6, final ItemCallback r7) {
            if (TextUtils.isEmpty(r6) == true) goto L22;
            if (r7 == null) goto L20;
            if (this.mBrowserFwk.isConnected() == true) goto L10;
            Log.i(MediaBrowserCompat.TAG, "Not connected, unable to retrieve the MediaItem.");
            this.mHandler.post(new RunnableC00001(this, r7, r6));
            return;
        L10:
            if (this.mServiceBinderWrapper != null) goto L23;
            this.mHandler.post(new RunnableC00012(this, r7, r6));
            return;
        L23:
            this.mServiceBinderWrapper.getMediaItem(r6, new ItemReceiver(r6, r7, this.mHandler), this.mCallbacksMessenger);     // Catch: RemoteException -> L16
            return;
        L17:
            Log.i(MediaBrowserCompat.TAG, "Remote error getting media item: " + r6);
            this.mHandler.post(new RunnableC00023(this, r7, r6));
            return;
        L20:
            throw new IllegalArgumentException("cb is null");
        L22:
            throw new IllegalArgumentException("mediaId is empty");
        }

        @Override // android.support.v4.media.MediaBrowserCompat.MediaBrowserImpl
        public Bundle getNotifyChildrenChangedOptions() {
            return this.mNotifyChildrenChangedOptions;
        }

        @Override // android.support.v4.media.MediaBrowserCompat.MediaBrowserImpl
        public String getRoot() {
            return this.mBrowserFwk.getRoot();
        }

        @Override // android.support.v4.media.MediaBrowserCompat.MediaBrowserImpl
        public ComponentName getServiceComponent() {
            return this.mBrowserFwk.getServiceComponent();
        }

        @Override // android.support.v4.media.MediaBrowserCompat.MediaBrowserImpl
        public MediaSessionCompat.Token getSessionToken() {
            if (this.mMediaSessionToken != null) goto L6;
            this.mMediaSessionToken = MediaSessionCompat.Token.fromToken(this.mBrowserFwk.getSessionToken());
        L6:
            return this.mMediaSessionToken;
        }

        @Override // android.support.v4.media.MediaBrowserCompat.MediaBrowserImpl
        public boolean isConnected() {
            return this.mBrowserFwk.isConnected();
        }

        @Override // android.support.v4.media.MediaBrowserCompat.ConnectionCallback.ConnectionCallbackInternal
        public void onConnected() {
            Bundle r1 = this.mBrowserFwk.getExtras();     // Catch: IllegalStateException -> L17
            if (r1 != null) goto L6;
            return;
        L6:
            this.mServiceVersion = r1.getInt(MediaBrowserProtocol.EXTRA_SERVICE_VERSION, 0);
            IBinder r2 = BundleCompat.getBinder(r1, MediaBrowserProtocol.EXTRA_MESSENGER_BINDER);
            if (r2 == null) goto L13;
            this.mServiceBinderWrapper = new ServiceBinderWrapper(r2, this.mRootHints);
            this.mCallbacksMessenger = new Messenger(this.mHandler);
            this.mHandler.setCallbacksMessenger(this.mCallbacksMessenger);
            this.mServiceBinderWrapper.registerCallbackMessenger(this.mContext, this.mCallbacksMessenger);     // Catch: RemoteException -> L11
        L12:
            Log.i(MediaBrowserCompat.TAG, "Remote error registering client messenger.");
        L13:
            IMediaSession r0 = IMediaSession.Stub.asInterface(BundleCompat.getBinder(r1, MediaBrowserProtocol.EXTRA_SESSION_BINDER));
            if (r0 == null) goto L24;
            this.mMediaSessionToken = MediaSessionCompat.Token.fromToken(this.mBrowserFwk.getSessionToken(), r0);
            return;
        L24:
            return;
        L17:
            e = move-exception;
            Log.e(MediaBrowserCompat.TAG, "Unexpected IllegalStateException", e);
        }

        @Override // android.support.v4.media.MediaBrowserCompat.ConnectionCallback.ConnectionCallbackInternal
        public void onConnectionFailed() {
        }

        @Override // android.support.v4.media.MediaBrowserCompat.MediaBrowserServiceCallbackImpl
        public void onConnectionFailed(Messenger r1) {
        }

        @Override // android.support.v4.media.MediaBrowserCompat.ConnectionCallback.ConnectionCallbackInternal
        public void onConnectionSuspended() {
            this.mServiceBinderWrapper = null;
            this.mCallbacksMessenger = null;
            this.mMediaSessionToken = null;
            this.mHandler.setCallbacksMessenger(null);
        }

        @Override // android.support.v4.media.MediaBrowserCompat.MediaBrowserServiceCallbackImpl
        public void onLoadChildren(Messenger r4, String r5, List<MediaItem> r6, Bundle r7, Bundle r8) {
            if (this.mCallbacksMessenger == r4) goto L5;
            return;
        L5:
            Subscription r0 = this.mSubscriptions.get(r5);
            if (r0 == null) goto L8;
            SubscriptionCallback r1 = r0.getCallback(r7);
            if (r1 != null) goto L14;
            return;
        L14:
            if (r7 != null) goto L18;
            if (r6 != null) goto L17;
            r1.onError(r5);
            return;
        L17:
            this.mNotifyChildrenChangedOptions = r8;
            r1.onChildrenLoaded(r5, r6);
            this.mNotifyChildrenChangedOptions = null;
            return;
        L18:
            if (r6 != null) goto L20;
            r1.onError(r5, r7);
            return;
        L20:
            this.mNotifyChildrenChangedOptions = r8;
            r1.onChildrenLoaded(r5, r6, r7);
            this.mNotifyChildrenChangedOptions = null;
            return;
        L8:
            if (MediaBrowserCompat.DEBUG == false) goto L22;
            Log.d(MediaBrowserCompat.TAG, "onLoadChildren for id that isn't subscribed id=" + r5);
            return;
        }

        @Override // android.support.v4.media.MediaBrowserCompat.MediaBrowserServiceCallbackImpl
        public void onServiceConnected(Messenger r1, String r2, MediaSessionCompat.Token r3, Bundle r4) {
        }

        @Override // android.support.v4.media.MediaBrowserCompat.MediaBrowserImpl
        public void search(final String r6, final Bundle r7, final SearchCallback r8) {
            if (isConnected() == false) goto L15;
            if (this.mServiceBinderWrapper != null) goto L16;
            Log.i(MediaBrowserCompat.TAG, "The connected service doesn't support search.");
            this.mHandler.post(new RunnableC00034(this, r8, r6, r7));
            return;
        L16:
            this.mServiceBinderWrapper.search(r6, r7, new SearchResultReceiver(r6, r7, r8, this.mHandler), this.mCallbacksMessenger);     // Catch: RemoteException -> L11
            return;
        L11:
            e = move-exception;
            Log.i(MediaBrowserCompat.TAG, "Remote error searching items with query: " + r6, e);
            this.mHandler.post(new RunnableC00045(this, r8, r6, r7));
            return;
        L15:
            throw new IllegalStateException("search() called while not connected");
        }

        @Override // android.support.v4.media.MediaBrowserCompat.MediaBrowserImpl
        public void sendCustomAction(final String r6, final Bundle r7, final CustomActionCallback r8) {
            if (isConnected() == false) goto L18;
            if (this.mServiceBinderWrapper != null) goto L19;
            Log.i(MediaBrowserCompat.TAG, "The connected service doesn't support sendCustomAction.");
            if (r8 == null) goto L19;
            this.mHandler.post(new RunnableC00056(this, r8, r6, r7));
        L19:
            this.mServiceBinderWrapper.sendCustomAction(r6, r7, new CustomActionResultReceiver(r6, r7, r8, this.mHandler), this.mCallbacksMessenger);     // Catch: RemoteException -> L12
            return;
        L12:
            e = move-exception;
            Log.i(MediaBrowserCompat.TAG, "Remote error sending a custom action: action=" + r6 + ", extras=" + r7, e);
            if (r8 == null) goto L21;
            this.mHandler.post(new RunnableC00067(this, r8, r6, r7));
            return;
        L21:
            return;
        L18:
            throw new IllegalStateException("Cannot send a custom action (" + r6 + ") with extras " + r7 + " because the browser is not connected to the service.");
        }

        @Override // android.support.v4.media.MediaBrowserCompat.MediaBrowserImpl
        public void subscribe(String r6, Bundle r7, SubscriptionCallback r8) {
            Subscription r0 = this.mSubscriptions.get(r6);
            if (r0 != null) goto L5;
            r0 = new Subscription();
            this.mSubscriptions.put(r6, r0);
        L5:
            r8.setSubscription(r0);
            if (r7 != null) goto L8;
            Bundle r1 = null;
        L9:
            r0.putCallback(r1, r8);
            if (this.mServiceBinderWrapper != null) goto L17;
            this.mBrowserFwk.subscribe(r6, r8.mSubscriptionCallbackFwk);
            return;
        L17:
            this.mServiceBinderWrapper.addSubscription(r6, r8.mToken, r1, this.mCallbacksMessenger);     // Catch: RemoteException -> L14
            return;
        L15:
            Log.i(MediaBrowserCompat.TAG, "Remote error subscribing media item: " + r6);
            return;
        L8:
            r1 = new Bundle(r7);
            goto L9
        }

        @Override // android.support.v4.media.MediaBrowserCompat.MediaBrowserImpl
        public void unsubscribe(String r8, SubscriptionCallback r9) {
            Subscription r0 = this.mSubscriptions.get(r8);
            if (r0 != null) goto L6;
            return;
        L6:
            if (this.mServiceBinderWrapper != null) goto L35;
            if (r9 != null) goto L9;
            this.mBrowserFwk.unsubscribe(r8);
        L31:
            if (r0.isEmpty() == true) goto L33;
            if (r9 == null) goto L33;
            return;
        L33:
            this.mSubscriptions.remove(r8);
            return;
        L9:
            List<SubscriptionCallback> r1 = r0.getCallbacks();
            List<Bundle> r2 = r0.getOptionsList();
            int r3 = r1.size() - 1;
        L10:
            if (r3 < 0) goto L16;
            if (r1.get(r3) != r9) goto L14;
            r1.remove(r3);
            r2.remove(r3);
        L14:
            r3 = r3 - 1;
            goto L10
        L16:
            if (r1.size() != 0) goto L31;
            this.mBrowserFwk.unsubscribe(r8);
            goto L31
        L35:
            if (r9 != null) goto L21;
            this.mServiceBinderWrapper.removeSubscription(r8, null, this.mCallbacksMessenger);     // Catch: RemoteException -> L28
            goto L31
        L21:
            List<SubscriptionCallback> r4 = r0.getCallbacks();     // Catch: RemoteException -> L28
            List<Bundle> r5 = r0.getOptionsList();     // Catch: RemoteException -> L28
            int r6 = r4.size() - 1;     // Catch: RemoteException -> L28
        L22:
            if (r6 < 0) goto L31;
            if (r4.get(r6) != r9) goto L26;
            this.mServiceBinderWrapper.removeSubscription(r8, r9.mToken, this.mCallbacksMessenger);     // Catch: RemoteException -> L28
            r4.remove(r6);     // Catch: RemoteException -> L28
            r5.remove(r6);     // Catch: RemoteException -> L28
        L26:
            r6 = r6 - 1;
            goto L22
        L29:
            Log.d(MediaBrowserCompat.TAG, "removeSubscription failed with RemoteException parentId=" + r8);
            goto L31
        }
    }

    static class MediaBrowserImplApi23 extends MediaBrowserImplApi21 {
        MediaBrowserImplApi23(Context r1, ComponentName r2, ConnectionCallback r3, Bundle r4) {
            super(r1, r2, r3, r4);
        }

        @Override // android.support.v4.media.MediaBrowserCompat.MediaBrowserImplApi21, android.support.v4.media.MediaBrowserCompat.MediaBrowserImpl
        public void getItem(String r3, ItemCallback r4) {
            if (this.mServiceBinderWrapper != null) goto L5;
            this.mBrowserFwk.getItem(r3, r4.mItemCallbackFwk);
            return;
        L5:
            super.getItem(r3, r4);
        }
    }

    static class MediaBrowserImplApi26 extends MediaBrowserImplApi23 {
        MediaBrowserImplApi26(Context r1, ComponentName r2, ConnectionCallback r3, Bundle r4) {
            super(r1, r2, r3, r4);
        }

        @Override // android.support.v4.media.MediaBrowserCompat.MediaBrowserImplApi21, android.support.v4.media.MediaBrowserCompat.MediaBrowserImpl
        public void subscribe(String r3, Bundle r4, SubscriptionCallback r5) {
            if (this.mServiceBinderWrapper != null) goto L5;
        L8:
            if (r4 != null) goto L10;
            this.mBrowserFwk.subscribe(r3, r5.mSubscriptionCallbackFwk);
            return;
        L10:
            this.mBrowserFwk.subscribe(r3, r4, r5.mSubscriptionCallbackFwk);
            return;
        L5:
            if (this.mServiceVersion < 2) goto L8;
            super.subscribe(r3, r4, r5);
        }

        @Override // android.support.v4.media.MediaBrowserCompat.MediaBrowserImplApi21, android.support.v4.media.MediaBrowserCompat.MediaBrowserImpl
        public void unsubscribe(String r3, SubscriptionCallback r4) {
            if (this.mServiceBinderWrapper != null) goto L5;
        L8:
            if (r4 != null) goto L10;
            this.mBrowserFwk.unsubscribe(r3);
            return;
        L10:
            this.mBrowserFwk.unsubscribe(r3, r4.mSubscriptionCallbackFwk);
            return;
        L5:
            if (this.mServiceVersion < 2) goto L8;
            super.unsubscribe(r3, r4);
        }
    }

    static class MediaBrowserImplBase implements MediaBrowserImpl, MediaBrowserServiceCallbackImpl {
        static final int CONNECT_STATE_CONNECTED = 3;
        static final int CONNECT_STATE_CONNECTING = 2;
        static final int CONNECT_STATE_DISCONNECTED = 1;
        static final int CONNECT_STATE_DISCONNECTING = 0;
        static final int CONNECT_STATE_SUSPENDED = 4;
        final ConnectionCallback mCallback;
        Messenger mCallbacksMessenger;
        final Context mContext;
        private Bundle mExtras;
        final CallbackHandler mHandler;
        private MediaSessionCompat.Token mMediaSessionToken;
        private Bundle mNotifyChildrenChangedOptions;
        final Bundle mRootHints;
        private String mRootId;
        ServiceBinderWrapper mServiceBinderWrapper;
        final ComponentName mServiceComponent;
        MediaServiceConnection mServiceConnection;
        int mState;
        private final ArrayMap<String, Subscription> mSubscriptions;

        private class MediaServiceConnection implements ServiceConnection {
            final /* synthetic */ MediaBrowserImplBase this$0;

            MediaServiceConnection(MediaBrowserImplBase r1) {
                this.this$0 = r1;
            }

            private void postOrRun(Runnable r3) {
                if (Thread.currentThread() != this.this$0.mHandler.getLooper().getThread()) goto L5;
                r3.run();
                return;
            L5:
                this.this$0.mHandler.post(r3);
            }

            boolean isCurrent(String r3) {
                if (this.this$0.mServiceConnection != this) goto L11;
                if (this.this$0.mState == 0) goto L11;
                if (this.this$0.mState == 1) goto L11;
                return true;
            L11:
                if (this.this$0.mState != 0) goto L13;
                return false;
            L13:
                if (this.this$0.mState == 1) goto L18;
                Log.i(MediaBrowserCompat.TAG, r3 + " for " + this.this$0.mServiceComponent + " with mServiceConnection=" + this.this$0.mServiceConnection + " this=" + this);
                return false;
            L18:
                return false;
            }

            @Override // android.content.ServiceConnection
            public void onServiceConnected(final ComponentName r2, final IBinder r3) {
                postOrRun(new RunnableC00131(this, r2, r3));
            }

            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(final ComponentName r2) {
                postOrRun(new RunnableC00142(this, r2));
            }
        }

        public MediaBrowserImplBase(Context r3, ComponentName r4, ConnectionCallback r5, Bundle r6) {
            this.mHandler = new CallbackHandler(this);
            this.mSubscriptions = new ArrayMap();
            this.mState = 1;
            if (r3 == null) goto L17;
            if (r4 == null) goto L15;
            if (r5 == null) goto L13;
            this.mContext = r3;
            this.mServiceComponent = r4;
            this.mCallback = r5;
            if (r6 != null) goto L9;
            Bundle r0 = null;
        L10:
            this.mRootHints = r0;
            return;
        L9:
            r0 = new Bundle(r6);
            goto L10
        L13:
            throw new IllegalArgumentException("connection callback must not be null");
        L15:
            throw new IllegalArgumentException("service component must not be null");
        L17:
            throw new IllegalArgumentException("context must not be null");
        }

        private static String getStateLabel(int r2) {
            switch(r2) {
                case 0: goto L13;
                case 1: goto L11;
                case 2: goto L9;
                case 3: goto L7;
                case 4: goto L5;
                default: goto L4;
            };
        L5:
            return "CONNECT_STATE_SUSPENDED";
        L7:
            return "CONNECT_STATE_CONNECTED";
        L9:
            return "CONNECT_STATE_CONNECTING";
        L11:
            return "CONNECT_STATE_DISCONNECTED";
        L13:
            return "CONNECT_STATE_DISCONNECTING";
        L4:
            return "UNKNOWN/" + r2;
        }

        private boolean isCurrent(Messenger r3, String r4) {
            if (this.mCallbacksMessenger != r3) goto L11;
            if (this.mState == 0) goto L11;
            if (this.mState == 1) goto L11;
            return true;
        L11:
            if (this.mState != 0) goto L13;
            return false;
        L13:
            if (this.mState == 1) goto L18;
            Log.i(MediaBrowserCompat.TAG, r4 + " for " + this.mServiceComponent + " with mCallbacksMessenger=" + this.mCallbacksMessenger + " this=" + this);
            return false;
        L18:
            return false;
        }

        @Override // android.support.v4.media.MediaBrowserCompat.MediaBrowserImpl
        public void connect() {
            if (this.mState != 0) goto L5;
        L9:
            this.mState = 2;
            this.mHandler.post(new RunnableC00071(this));
            return;
        L5:
            if (this.mState == 1) goto L9;
            throw new IllegalStateException("connect() called while neigther disconnecting nor disconnected (state=" + getStateLabel(this.mState) + ")");
        }

        @Override // android.support.v4.media.MediaBrowserCompat.MediaBrowserImpl
        public void disconnect() {
            this.mState = 0;
            this.mHandler.post(new RunnableC00082(this));
        }

        void dump() {
            Log.d(MediaBrowserCompat.TAG, "MediaBrowserCompat...");
            Log.d(MediaBrowserCompat.TAG, "  mServiceComponent=" + this.mServiceComponent);
            Log.d(MediaBrowserCompat.TAG, "  mCallback=" + this.mCallback);
            Log.d(MediaBrowserCompat.TAG, "  mRootHints=" + this.mRootHints);
            Log.d(MediaBrowserCompat.TAG, "  mState=" + getStateLabel(this.mState));
            Log.d(MediaBrowserCompat.TAG, "  mServiceConnection=" + this.mServiceConnection);
            Log.d(MediaBrowserCompat.TAG, "  mServiceBinderWrapper=" + this.mServiceBinderWrapper);
            Log.d(MediaBrowserCompat.TAG, "  mCallbacksMessenger=" + this.mCallbacksMessenger);
            Log.d(MediaBrowserCompat.TAG, "  mRootId=" + this.mRootId);
            Log.d(MediaBrowserCompat.TAG, "  mMediaSessionToken=" + this.mMediaSessionToken);
        }

        void forceCloseConnection() {
            if (this.mServiceConnection == null) goto L5;
            this.mContext.unbindService(this.mServiceConnection);
        L5:
            this.mState = 1;
            this.mServiceConnection = null;
            this.mServiceBinderWrapper = null;
            this.mCallbacksMessenger = null;
            this.mHandler.setCallbacksMessenger(null);
            this.mRootId = null;
            this.mMediaSessionToken = null;
        }

        @Override // android.support.v4.media.MediaBrowserCompat.MediaBrowserImpl
        public Bundle getExtras() {
            if (isConnected() == false) goto L7;
            return this.mExtras;
        L7:
            throw new IllegalStateException("getExtras() called while not connected (state=" + getStateLabel(this.mState) + ")");
        }

        @Override // android.support.v4.media.MediaBrowserCompat.MediaBrowserImpl
        public void getItem(final String r6, final ItemCallback r7) {
            if (TextUtils.isEmpty(r6) == true) goto L18;
            if (r7 == null) goto L16;
            if (isConnected() == true) goto L19;
            Log.i(MediaBrowserCompat.TAG, "Not connected, unable to retrieve the MediaItem.");
            this.mHandler.post(new RunnableC00093(this, r7, r6));
            return;
        L19:
            this.mServiceBinderWrapper.getMediaItem(r6, new ItemReceiver(r6, r7, this.mHandler), this.mCallbacksMessenger);     // Catch: RemoteException -> L12
            return;
        L13:
            Log.i(MediaBrowserCompat.TAG, "Remote error getting media item: " + r6);
            this.mHandler.post(new RunnableC00104(this, r7, r6));
            return;
        L16:
            throw new IllegalArgumentException("cb is null");
        L18:
            throw new IllegalArgumentException("mediaId is empty");
        }

        @Override // android.support.v4.media.MediaBrowserCompat.MediaBrowserImpl
        public Bundle getNotifyChildrenChangedOptions() {
            return this.mNotifyChildrenChangedOptions;
        }

        @Override // android.support.v4.media.MediaBrowserCompat.MediaBrowserImpl
        public String getRoot() {
            if (isConnected() == false) goto L7;
            return this.mRootId;
        L7:
            throw new IllegalStateException("getRoot() called while not connected(state=" + getStateLabel(this.mState) + ")");
        }

        @Override // android.support.v4.media.MediaBrowserCompat.MediaBrowserImpl
        public ComponentName getServiceComponent() {
            if (isConnected() == false) goto L7;
            return this.mServiceComponent;
        L7:
            throw new IllegalStateException("getServiceComponent() called while not connected (state=" + this.mState + ")");
        }

        @Override // android.support.v4.media.MediaBrowserCompat.MediaBrowserImpl
        public MediaSessionCompat.Token getSessionToken() {
            if (isConnected() == false) goto L7;
            return this.mMediaSessionToken;
        L7:
            throw new IllegalStateException("getSessionToken() called while not connected(state=" + this.mState + ")");
        }

        @Override // android.support.v4.media.MediaBrowserCompat.MediaBrowserImpl
        public boolean isConnected() {
            if (this.mState != 3) goto L5;
            return true;
        L5:
            return false;
        }

        @Override // android.support.v4.media.MediaBrowserCompat.MediaBrowserServiceCallbackImpl
        public void onConnectionFailed(Messenger r4) {
            Log.e(MediaBrowserCompat.TAG, "onConnectFailed for " + this.mServiceComponent);
            if (isCurrent(r4, "onConnectFailed") == true) goto L6;
            return;
        L6:
            if (this.mState == 2) goto L9;
            Log.w(MediaBrowserCompat.TAG, "onConnect from service while mState=" + getStateLabel(this.mState) + "... ignoring");
            return;
        L9:
            forceCloseConnection();
            this.mCallback.onConnectionFailed();
        }

        @Override // android.support.v4.media.MediaBrowserCompat.MediaBrowserServiceCallbackImpl
        public void onLoadChildren(Messenger r5, String r6, List<MediaItem> r7, Bundle r8, Bundle r9) {
            if (isCurrent(r5, "onLoadChildren") == true) goto L6;
            return;
        L6:
            if (MediaBrowserCompat.DEBUG == false) goto L8;
            Log.d(MediaBrowserCompat.TAG, "onLoadChildren for " + this.mServiceComponent + " id=" + r6);
        L8:
            Subscription r0 = this.mSubscriptions.get(r6);
            if (r0 == null) goto L11;
            SubscriptionCallback r1 = r0.getCallback(r8);
            if (r1 != null) goto L17;
            return;
        L17:
            if (r8 != null) goto L21;
            if (r7 != null) goto L20;
            r1.onError(r6);
            return;
        L20:
            this.mNotifyChildrenChangedOptions = r9;
            r1.onChildrenLoaded(r6, r7);
            this.mNotifyChildrenChangedOptions = null;
            return;
        L21:
            if (r7 != null) goto L23;
            r1.onError(r6, r8);
            return;
        L23:
            this.mNotifyChildrenChangedOptions = r9;
            r1.onChildrenLoaded(r6, r7, r8);
            this.mNotifyChildrenChangedOptions = null;
            return;
        L11:
            if (MediaBrowserCompat.DEBUG == false) goto L25;
            Log.d(MediaBrowserCompat.TAG, "onLoadChildren for id that isn't subscribed id=" + r6);
            return;
        }

        @Override // android.support.v4.media.MediaBrowserCompat.MediaBrowserServiceCallbackImpl
        public void onServiceConnected(Messenger r18, String r19, MediaSessionCompat.Token r20, Bundle r21) {
            if (isCurrent(r18, "onConnect") == true) goto L6;
            return;
        L6:
            if (this.mState == 2) goto L9;
            Log.w(MediaBrowserCompat.TAG, "onConnect from service while mState=" + getStateLabel(this.mState) + "... ignoring");
            return;
        L9:
            this.mRootId = r19;
            this.mMediaSessionToken = r20;
            this.mExtras = r21;
            this.mState = 3;
            if (MediaBrowserCompat.DEBUG == false) goto L12;
            Log.d(MediaBrowserCompat.TAG, "ServiceCallbacks.onConnect...");
            dump();
        L12:
            this.mCallback.onConnected();
            Iterator<Map.Entry<String, Subscription>> r0 = this.mSubscriptions.entrySet().iterator();     // Catch: RemoteException -> L23
        L15:
            if (r0.hasNext() == false) goto L30;
            Map.Entry<String, Subscription> r7 = r0.next();     // Catch: RemoteException -> L23
            String r8 = r7.getKey();     // Catch: RemoteException -> L23
            Subscription r9 = r7.getValue();     // Catch: RemoteException -> L23
            List<SubscriptionCallback> r10 = r9.getCallbacks();     // Catch: RemoteException -> L23
            List<Bundle> r11 = r9.getOptionsList();     // Catch: RemoteException -> L23
            int r12 = 0;
        L18:
            if (r12 >= r10.size()) goto L15;
            Iterator<Map.Entry<String, Subscription>> r16 = r0;
            this.mServiceBinderWrapper.addSubscription(r8, r10.get(r12).mToken, r11.get(r12), this.mCallbacksMessenger);     // Catch: RemoteException -> L23
            r12 = r12 + 1;
            r0 = r16;
            goto L18
        L30:
            return;
        L24:
            Log.d(MediaBrowserCompat.TAG, "addSubscription failed with RemoteException.");
        }

        @Override // android.support.v4.media.MediaBrowserCompat.MediaBrowserImpl
        public void search(final String r5, final Bundle r6, final SearchCallback r7) {
            if (isConnected() == false) goto L11;
            this.mServiceBinderWrapper.search(r5, r6, new SearchResultReceiver(r5, r6, r7, this.mHandler), this.mCallbacksMessenger);     // Catch: RemoteException -> L7
            return;
        L7:
            e = move-exception;
            Log.i(MediaBrowserCompat.TAG, "Remote error searching items with query: " + r5, e);
            this.mHandler.post(new RunnableC00115(this, r7, r5, r6));
            return;
        L11:
            throw new IllegalStateException("search() called while not connected (state=" + getStateLabel(this.mState) + ")");
        }

        @Override // android.support.v4.media.MediaBrowserCompat.MediaBrowserImpl
        public void sendCustomAction(final String r5, final Bundle r6, final CustomActionCallback r7) {
            if (isConnected() == false) goto L13;
            this.mServiceBinderWrapper.sendCustomAction(r5, r6, new CustomActionResultReceiver(r5, r6, r7, this.mHandler), this.mCallbacksMessenger);     // Catch: RemoteException -> L7
            return;
        L7:
            e = move-exception;
            Log.i(MediaBrowserCompat.TAG, "Remote error sending a custom action: action=" + r5 + ", extras=" + r6, e);
            if (r7 == null) goto L16;
            this.mHandler.post(new RunnableC00126(this, r7, r5, r6));
            return;
        L16:
            return;
        L13:
            throw new IllegalStateException("Cannot send a custom action (" + r5 + ") with extras " + r6 + " because the browser is not connected to the service.");
        }

        @Override // android.support.v4.media.MediaBrowserCompat.MediaBrowserImpl
        public void subscribe(String r6, Bundle r7, SubscriptionCallback r8) {
            Subscription r0 = this.mSubscriptions.get(r6);
            if (r0 != null) goto L5;
            r0 = new Subscription();
            this.mSubscriptions.put(r6, r0);
        L5:
            if (r7 != null) goto L7;
            Bundle r1 = null;
        L8:
            r0.putCallback(r1, r8);
            if (isConnected() == false) goto L17;
            this.mServiceBinderWrapper.addSubscription(r6, r8.mToken, r1, this.mCallbacksMessenger);     // Catch: RemoteException -> L12
            return;
        L13:
            Log.d(MediaBrowserCompat.TAG, "addSubscription failed with RemoteException parentId=" + r6);
            return;
        L17:
            return;
        L7:
            r1 = new Bundle(r7);
            goto L8
        }

        @Override // android.support.v4.media.MediaBrowserCompat.MediaBrowserImpl
        public void unsubscribe(String r8, SubscriptionCallback r9) {
            Subscription r0 = this.mSubscriptions.get(r8);
            if (r0 != null) goto L26;
            return;
        L26:
            if (r9 == null) goto L7;
            List<SubscriptionCallback> r1 = r0.getCallbacks();     // Catch: RemoteException -> L19
            List<Bundle> r2 = r0.getOptionsList();     // Catch: RemoteException -> L19
            int r3 = r1.size() - 1;     // Catch: RemoteException -> L19
        L10:
            if (r3 < 0) goto L22;
            if (r1.get(r3) != r9) goto L17;
            if (isConnected() == false) goto L16;
            this.mServiceBinderWrapper.removeSubscription(r8, r9.mToken, this.mCallbacksMessenger);     // Catch: RemoteException -> L19
        L16:
            r1.remove(r3);     // Catch: RemoteException -> L19
            r2.remove(r3);     // Catch: RemoteException -> L19
        L17:
            r3 = r3 - 1;
        L22:
            if (r0.isEmpty() == true) goto L24;
            if (r9 == null) goto L24;
            return;
        L24:
            this.mSubscriptions.remove(r8);
            return;
        L7:
            if (isConnected() == false) goto L22;
            this.mServiceBinderWrapper.removeSubscription(r8, null, this.mCallbacksMessenger);     // Catch: RemoteException -> L19
            goto L22
        L20:
            Log.d(MediaBrowserCompat.TAG, "removeSubscription failed with RemoteException parentId=" + r8);
            goto L22
        }
    }

    interface MediaBrowserServiceCallbackImpl {
        void onConnectionFailed(Messenger r1);

        void onLoadChildren(Messenger r1, String r2, List<MediaItem> r3, Bundle r4, Bundle r5);

        void onServiceConnected(Messenger r1, String r2, MediaSessionCompat.Token r3, Bundle r4);
    }

    public static class MediaItem implements Parcelable {
        public static final Parcelable.Creator<MediaItem> CREATOR = null;
        public static final int FLAG_BROWSABLE = 1;
        public static final int FLAG_PLAYABLE = 2;
        private final MediaDescriptionCompat mDescription;
        private final int mFlags;

        static {
            CREATOR = new C00151();
        }

        MediaItem(Parcel r2) {
            this.mFlags = r2.readInt();
            this.mDescription = MediaDescriptionCompat.CREATOR.createFromParcel(r2);
        }

        public MediaItem(MediaDescriptionCompat r3, int r4) {
            if (r3 == null) goto L11;
            if (TextUtils.isEmpty(r3.getMediaId()) == true) goto L9;
            this.mFlags = r4;
            this.mDescription = r3;
            return;
        L9:
            throw new IllegalArgumentException("description must have a non-empty media id");
        L11:
            throw new IllegalArgumentException("description cannot be null");
        }

        public static MediaItem fromMediaItem(Object r4) {
            if (r4 == null) goto L5;
            MediaBrowser.MediaItem r0 = (MediaBrowser.MediaItem) r4;
            int r1 = Api21Impl.getFlags(r0);
            return new MediaItem(MediaDescriptionCompat.fromMediaDescription(Api21Impl.getDescription(r0)), r1);
        L5:
            return null;
        }

        public static List<MediaItem> fromMediaItemList(List<?> r4) {
            if (r4 == null) goto L8;
            ArrayList r0 = new ArrayList(r4.size());
            Iterator<?> r1 = r4.iterator();
        L5:
            if (r1.hasNext() == false) goto L7;
            r0.add(fromMediaItem(r1.next()));
            goto L5
        L7:
            return r0;
        L8:
            return null;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public MediaDescriptionCompat getDescription() {
            return this.mDescription;
        }

        public int getFlags() {
            return this.mFlags;
        }

        public String getMediaId() {
            return this.mDescription.getMediaId();
        }

        public boolean isBrowsable() {
            if ((this.mFlags & 1) != 0) goto L7;
            return false;
        L7:
            return true;
        }

        public boolean isPlayable() {
            if ((this.mFlags & 2) == 0) goto L5;
            return true;
        L5:
            return false;
        }

        public String toString() {
            StringBuilder r0 = new StringBuilder("MediaItem{");
            r0.append("mFlags=").append(this.mFlags);
            r0.append(", mDescription=").append(this.mDescription);
            r0.append('}');
            return r0.toString();
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel r2, int r3) {
            r2.writeInt(this.mFlags);
            this.mDescription.writeToParcel(r2, r3);
        }
    }

    public static abstract class SearchCallback {
        public SearchCallback() {
        }

        public void onError(String r1, Bundle r2) {
        }

        public void onSearchResult(String r1, Bundle r2, List<MediaItem> r3) {
        }
    }

    private static class SearchResultReceiver extends ResultReceiver {
        private final SearchCallback mCallback;
        private final Bundle mExtras;
        private final String mQuery;

        SearchResultReceiver(String r1, Bundle r2, SearchCallback r3, Handler r4) {
            super(r4);
            this.mQuery = r1;
            this.mExtras = r2;
            this.mCallback = r3;
        }

        @Override // android.support.v4.os.ResultReceiver
        protected void onReceiveResult(int r7, Bundle r8) {
            if (r8 == null) goto L4;
            r8 = MediaSessionCompat.unparcelWithClassLoader(r8);
        L4:
            if (r7 != 0) goto L17;
            if (r8 == null) goto L17;
            if (r8.containsKey(MediaBrowserServiceCompat.KEY_SEARCH_RESULTS) == false) goto L17;
            Parcelable[] r0 = r8.getParcelableArray(MediaBrowserServiceCompat.KEY_SEARCH_RESULTS);
            if (r0 == null) goto L15;
            ArrayList r1 = new ArrayList(r0.length);
            int r2 = r0.length;
            int r3 = 0;
        L12:
            if (r3 >= r2) goto L14;
            r1.add((MediaItem) r0[r3]);
            r3 = r3 + 1;
            goto L12
        L14:
            this.mCallback.onSearchResult(this.mQuery, this.mExtras, r1);
            return;
        L15:
            this.mCallback.onError(this.mQuery, this.mExtras);
            return;
        L17:
            this.mCallback.onError(this.mQuery, this.mExtras);
        }
    }

    private static class ServiceBinderWrapper {
        private Messenger mMessenger;
        private Bundle mRootHints;

        public ServiceBinderWrapper(IBinder r2, Bundle r3) {
            this.mMessenger = new Messenger(r2);
            this.mRootHints = r3;
        }

        private void sendRequest(int r3, Bundle r4, Messenger r5) throws RemoteException {
            Message r0 = Message.obtain();
            r0.what = r3;
            r0.arg1 = 1;
            r0.setData(r4);
            r0.replyTo = r5;
            this.mMessenger.send(r0);
        }

        void addSubscription(String r3, IBinder r4, Bundle r5, Messenger r6) throws RemoteException {
            Bundle r0 = new Bundle();
            r0.putString(MediaBrowserProtocol.DATA_MEDIA_ITEM_ID, r3);
            BundleCompat.putBinder(r0, MediaBrowserProtocol.DATA_CALLBACK_TOKEN, r4);
            r0.putBundle(MediaBrowserProtocol.DATA_OPTIONS, r5);
            sendRequest(3, r0, r6);
        }

        void connect(Context r4, Messenger r5) throws RemoteException {
            Bundle r0 = new Bundle();
            r0.putString(MediaBrowserProtocol.DATA_PACKAGE_NAME, r4.getPackageName());
            r0.putInt(MediaBrowserProtocol.DATA_CALLING_PID, Process.myPid());
            r0.putBundle(MediaBrowserProtocol.DATA_ROOT_HINTS, this.mRootHints);
            sendRequest(1, r0, r5);
        }

        void disconnect(Messenger r3) throws RemoteException {
            sendRequest(2, null, r3);
        }

        void getMediaItem(String r3, ResultReceiver r4, Messenger r5) throws RemoteException {
            Bundle r0 = new Bundle();
            r0.putString(MediaBrowserProtocol.DATA_MEDIA_ITEM_ID, r3);
            r0.putParcelable(MediaBrowserProtocol.DATA_RESULT_RECEIVER, r4);
            sendRequest(5, r0, r5);
        }

        void registerCallbackMessenger(Context r4, Messenger r5) throws RemoteException {
            Bundle r0 = new Bundle();
            r0.putString(MediaBrowserProtocol.DATA_PACKAGE_NAME, r4.getPackageName());
            r0.putInt(MediaBrowserProtocol.DATA_CALLING_PID, Process.myPid());
            r0.putBundle(MediaBrowserProtocol.DATA_ROOT_HINTS, this.mRootHints);
            sendRequest(6, r0, r5);
        }

        void removeSubscription(String r3, IBinder r4, Messenger r5) throws RemoteException {
            Bundle r0 = new Bundle();
            r0.putString(MediaBrowserProtocol.DATA_MEDIA_ITEM_ID, r3);
            BundleCompat.putBinder(r0, MediaBrowserProtocol.DATA_CALLBACK_TOKEN, r4);
            sendRequest(4, r0, r5);
        }

        void search(String r3, Bundle r4, ResultReceiver r5, Messenger r6) throws RemoteException {
            Bundle r0 = new Bundle();
            r0.putString(MediaBrowserProtocol.DATA_SEARCH_QUERY, r3);
            r0.putBundle(MediaBrowserProtocol.DATA_SEARCH_EXTRAS, r4);
            r0.putParcelable(MediaBrowserProtocol.DATA_RESULT_RECEIVER, r5);
            sendRequest(8, r0, r6);
        }

        void sendCustomAction(String r3, Bundle r4, ResultReceiver r5, Messenger r6) throws RemoteException {
            Bundle r0 = new Bundle();
            r0.putString(MediaBrowserProtocol.DATA_CUSTOM_ACTION, r3);
            r0.putBundle(MediaBrowserProtocol.DATA_CUSTOM_ACTION_EXTRAS, r4);
            r0.putParcelable(MediaBrowserProtocol.DATA_RESULT_RECEIVER, r5);
            sendRequest(9, r0, r6);
        }

        void unregisterCallbackMessenger(Messenger r3) throws RemoteException {
            sendRequest(7, null, r3);
        }
    }

    private static class Subscription {
        private final List<SubscriptionCallback> mCallbacks;
        private final List<Bundle> mOptionsList;

        public Subscription() {
            this.mCallbacks = new ArrayList();
            this.mOptionsList = new ArrayList();
        }

        public SubscriptionCallback getCallback(Bundle r3) {
            int r0 = 0;
        L4:
            if (r0 >= this.mOptionsList.size()) goto L10;
            if (MediaBrowserCompatUtils.areSameOptions(this.mOptionsList.get(r0), r3) == true) goto L8;
            r0 = r0 + 1;
            goto L4
        L8:
            return this.mCallbacks.get(r0);
        L10:
            return null;
        }

        public List<SubscriptionCallback> getCallbacks() {
            return this.mCallbacks;
        }

        public List<Bundle> getOptionsList() {
            return this.mOptionsList;
        }

        public boolean isEmpty() {
            return this.mCallbacks.isEmpty();
        }

        public void putCallback(Bundle r3, SubscriptionCallback r4) {
            int r0 = 0;
        L4:
            if (r0 >= this.mOptionsList.size()) goto L10;
            if (MediaBrowserCompatUtils.areSameOptions(this.mOptionsList.get(r0), r3) == true) goto L7;
            r0 = r0 + 1;
            goto L4
        L7:
            this.mCallbacks.set(r0, r4);
            return;
        L10:
            this.mCallbacks.add(r4);
            this.mOptionsList.add(r3);
        }
    }

    public static abstract class SubscriptionCallback {
        final MediaBrowser.SubscriptionCallback mSubscriptionCallbackFwk;
        WeakReference<Subscription> mSubscriptionRef;
        final IBinder mToken;

        private class SubscriptionCallbackApi21 extends MediaBrowser.SubscriptionCallback {
            final /* synthetic */ SubscriptionCallback this$0;

            SubscriptionCallbackApi21(SubscriptionCallback r1) {
                this.this$0 = r1;
            }

            List<MediaItem> applyOptions(List<MediaItem> r6, Bundle r7) {
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

            @Override // android.media.browse.MediaBrowser.SubscriptionCallback
            public void onChildrenLoaded(String r9, List<MediaBrowser.MediaItem> r10) {
                if (this.this$0.mSubscriptionRef != null) goto L5;
                Subscription r0 = null;
            L6:
                if (r0 != null) goto L8;
                this.this$0.onChildrenLoaded(r9, MediaItem.fromMediaItemList(r10));
                return;
            L8:
                List<MediaItem> r1 = MediaItem.fromMediaItemList(r10);
                List<SubscriptionCallback> r2 = r0.getCallbacks();
                List<Bundle> r3 = r0.getOptionsList();
                int r4 = 0;
            L10:
                if (r4 >= r2.size()) goto L20;
                Bundle r5 = r3.get(r4);
                if (r5 != null) goto L14;
                this.this$0.onChildrenLoaded(r9, r1);
            L15:
                r4 = r4 + 1;
                goto L10
            L14:
                this.this$0.onChildrenLoaded(r9, applyOptions(r1, r5), r5);
                goto L15
            L20:
                return;
            L5:
                r0 = this.this$0.mSubscriptionRef.get();
                goto L6
            }

            @Override // android.media.browse.MediaBrowser.SubscriptionCallback
            public void onError(String r2) {
                this.this$0.onError(r2);
            }
        }

        private class SubscriptionCallbackApi26 extends SubscriptionCallbackApi21 {
            final /* synthetic */ SubscriptionCallback this$0;

            SubscriptionCallbackApi26(SubscriptionCallback r1) {
                this.this$0 = r1;
                super(r1);
            }

            @Override // android.media.browse.MediaBrowser.SubscriptionCallback
            public void onChildrenLoaded(String r3, List<MediaBrowser.MediaItem> r4, Bundle r5) {
                MediaSessionCompat.ensureClassLoader(r5);
                this.this$0.onChildrenLoaded(r3, MediaItem.fromMediaItemList(r4), r5);
            }

            @Override // android.media.browse.MediaBrowser.SubscriptionCallback
            public void onError(String r2, Bundle r3) {
                MediaSessionCompat.ensureClassLoader(r3);
                this.this$0.onError(r2, r3);
            }
        }

        public SubscriptionCallback() {
            this.mToken = new Binder();
            if (Build.VERSION.SDK_INT < 26) goto L5;
            this.mSubscriptionCallbackFwk = new SubscriptionCallbackApi26(this);
            return;
        L5:
            this.mSubscriptionCallbackFwk = new SubscriptionCallbackApi21(this);
        }

        public void onChildrenLoaded(String r1, List<MediaItem> r2) {
        }

        public void onChildrenLoaded(String r1, List<MediaItem> r2, Bundle r3) {
        }

        public void onError(String r1) {
        }

        public void onError(String r1, Bundle r2) {
        }

        void setSubscription(Subscription r2) {
            this.mSubscriptionRef = new WeakReference(r2);
        }
    }

    static {
        DEBUG = Log.isLoggable(TAG, 3);
    }

    public MediaBrowserCompat(Context r3, ComponentName r4, ConnectionCallback r5, Bundle r6) {
        if (Build.VERSION.SDK_INT < 26) goto L5;
        this.mImpl = new MediaBrowserImplApi26(r3, r4, r5, r6);
        return;
    L5:
        this.mImpl = new MediaBrowserImplApi23(r3, r4, r5, r6);
    }

    public void connect() {
        Log.d(TAG, "Connecting to a MediaBrowserService.");
        this.mImpl.connect();
    }

    public void disconnect() {
        this.mImpl.disconnect();
    }

    public Bundle getExtras() {
        return this.mImpl.getExtras();
    }

    public void getItem(String r2, ItemCallback r3) {
        this.mImpl.getItem(r2, r3);
    }

    public Bundle getNotifyChildrenChangedOptions() {
        return this.mImpl.getNotifyChildrenChangedOptions();
    }

    public String getRoot() {
        return this.mImpl.getRoot();
    }

    public ComponentName getServiceComponent() {
        return this.mImpl.getServiceComponent();
    }

    public MediaSessionCompat.Token getSessionToken() {
        return this.mImpl.getSessionToken();
    }

    public boolean isConnected() {
        return this.mImpl.isConnected();
    }

    public void search(String r3, Bundle r4, SearchCallback r5) {
        if (TextUtils.isEmpty(r3) == true) goto L10;
        if (r5 == null) goto L8;
        this.mImpl.search(r3, r4, r5);
        return;
    L8:
        throw new IllegalArgumentException("callback cannot be null");
    L10:
        throw new IllegalArgumentException("query cannot be empty");
    }

    public void sendCustomAction(String r3, Bundle r4, CustomActionCallback r5) {
        if (TextUtils.isEmpty(r3) == true) goto L7;
        this.mImpl.sendCustomAction(r3, r4, r5);
        return;
    L7:
        throw new IllegalArgumentException("action cannot be empty");
    }

    public void subscribe(String r3, Bundle r4, SubscriptionCallback r5) {
        if (TextUtils.isEmpty(r3) == true) goto L13;
        if (r5 == null) goto L11;
        if (r4 == null) goto L9;
        this.mImpl.subscribe(r3, r4, r5);
        return;
    L9:
        throw new IllegalArgumentException("options are null");
    L11:
        throw new IllegalArgumentException("callback is null");
    L13:
        throw new IllegalArgumentException("parentId is empty");
    }

    public void subscribe(String r3, SubscriptionCallback r4) {
        if (TextUtils.isEmpty(r3) == true) goto L10;
        if (r4 == null) goto L8;
        this.mImpl.subscribe(r3, null, r4);
        return;
    L8:
        throw new IllegalArgumentException("callback is null");
    L10:
        throw new IllegalArgumentException("parentId is empty");
    }

    public void unsubscribe(String r3) {
        if (TextUtils.isEmpty(r3) == true) goto L7;
        this.mImpl.unsubscribe(r3, null);
        return;
    L7:
        throw new IllegalArgumentException("parentId is empty");
    }

    public void unsubscribe(String r3, SubscriptionCallback r4) {
        if (TextUtils.isEmpty(r3) == true) goto L10;
        if (r4 == null) goto L8;
        this.mImpl.unsubscribe(r3, r4);
        return;
    L8:
        throw new IllegalArgumentException("callback is null");
    L10:
        throw new IllegalArgumentException("parentId is empty");
    }
}
