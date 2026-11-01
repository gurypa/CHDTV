package android.support.v4.media.session;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.media.MediaMetadata;
import android.media.Rating;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.RatingCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import androidx.core.app.BundleCompat;
import androidx.media.AudioAttributesCompat;
import androidx.media.C0175R;
import androidx.versionedparcelable.ParcelUtils;
import androidx.versionedparcelable.VersionedParcelable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public final class MediaControllerCompat {
    public static final String COMMAND_ADD_QUEUE_ITEM = "android.support.v4.media.session.command.ADD_QUEUE_ITEM";
    public static final String COMMAND_ADD_QUEUE_ITEM_AT = "android.support.v4.media.session.command.ADD_QUEUE_ITEM_AT";
    public static final String COMMAND_ARGUMENT_INDEX = "android.support.v4.media.session.command.ARGUMENT_INDEX";
    public static final String COMMAND_ARGUMENT_MEDIA_DESCRIPTION = "android.support.v4.media.session.command.ARGUMENT_MEDIA_DESCRIPTION";
    public static final String COMMAND_GET_EXTRA_BINDER = "android.support.v4.media.session.command.GET_EXTRA_BINDER";
    public static final String COMMAND_REMOVE_QUEUE_ITEM = "android.support.v4.media.session.command.REMOVE_QUEUE_ITEM";
    public static final String COMMAND_REMOVE_QUEUE_ITEM_AT = "android.support.v4.media.session.command.REMOVE_QUEUE_ITEM_AT";
    static final String TAG = "MediaControllerCompat";
    private final MediaControllerImpl mImpl;
    private final Set<Callback> mRegisteredCallbacks;
    private final MediaSessionCompat.Token mToken;

    public static abstract class Callback implements IBinder.DeathRecipient {
        final MediaController.Callback mCallbackFwk;
        MessageHandler mHandler;
        IMediaControllerCallback mIControllerCallback;

        private static class MediaControllerCallbackApi21 extends MediaController.Callback {
            private final WeakReference<Callback> mCallback;

            MediaControllerCallbackApi21(Callback r2) {
                this.mCallback = new WeakReference(r2);
            }

            @Override // android.media.session.MediaController.Callback
            public void onAudioInfoChanged(MediaController.PlaybackInfo r8) {
                Callback r0 = this.mCallback.get();
                if (r0 == null) goto L6;
                r0.onAudioInfoChanged(new PlaybackInfo(r8.getPlaybackType(), AudioAttributesCompat.wrap(r8.getAudioAttributes()), r8.getVolumeControl(), r8.getMaxVolume(), r8.getCurrentVolume()));
                return;
            }

            @Override // android.media.session.MediaController.Callback
            public void onExtrasChanged(Bundle r2) {
                MediaSessionCompat.ensureClassLoader(r2);
                Callback r0 = this.mCallback.get();
                if (r0 == null) goto L6;
                r0.onExtrasChanged(r2);
                return;
            }

            @Override // android.media.session.MediaController.Callback
            public void onMetadataChanged(MediaMetadata r3) {
                Callback r0 = this.mCallback.get();
                if (r0 == null) goto L6;
                r0.onMetadataChanged(MediaMetadataCompat.fromMediaMetadata(r3));
                return;
            }

            @Override // android.media.session.MediaController.Callback
            public void onPlaybackStateChanged(PlaybackState r3) {
                Callback r0 = this.mCallback.get();
                if (r0 != null) goto L5;
                return;
            L5:
                if (r0.mIControllerCallback != null) goto L10;
                r0.onPlaybackStateChanged(PlaybackStateCompat.fromPlaybackState(r3));
                return;
            }

            @Override // android.media.session.MediaController.Callback
            public void onQueueChanged(List<MediaSession.QueueItem> r3) {
                Callback r0 = this.mCallback.get();
                if (r0 == null) goto L6;
                r0.onQueueChanged(MediaSessionCompat.QueueItem.fromQueueItemList(r3));
                return;
            }

            @Override // android.media.session.MediaController.Callback
            public void onQueueTitleChanged(CharSequence r2) {
                Callback r0 = this.mCallback.get();
                if (r0 == null) goto L6;
                r0.onQueueTitleChanged(r2);
                return;
            }

            @Override // android.media.session.MediaController.Callback
            public void onSessionDestroyed() {
                Callback r0 = this.mCallback.get();
                if (r0 == null) goto L6;
                r0.onSessionDestroyed();
                return;
            }

            @Override // android.media.session.MediaController.Callback
            public void onSessionEvent(String r3, Bundle r4) {
                MediaSessionCompat.ensureClassLoader(r4);
                Callback r0 = this.mCallback.get();
                if (r0 == null) goto L6;
                IMediaControllerCallback r1 = r0.mIControllerCallback;
                r0.onSessionEvent(r3, r4);
                return;
            }
        }

        private class MessageHandler extends Handler {
            private static final int MSG_DESTROYED = 8;
            private static final int MSG_EVENT = 1;
            private static final int MSG_SESSION_READY = 13;
            private static final int MSG_UPDATE_CAPTIONING_ENABLED = 11;
            private static final int MSG_UPDATE_EXTRAS = 7;
            private static final int MSG_UPDATE_METADATA = 3;
            private static final int MSG_UPDATE_PLAYBACK_STATE = 2;
            private static final int MSG_UPDATE_QUEUE = 5;
            private static final int MSG_UPDATE_QUEUE_TITLE = 6;
            private static final int MSG_UPDATE_REPEAT_MODE = 9;
            private static final int MSG_UPDATE_SHUFFLE_MODE = 12;
            private static final int MSG_UPDATE_VOLUME = 4;
            boolean mRegistered;
            final /* synthetic */ Callback this$0;

            MessageHandler(Callback r1, Looper r2) {
                this.this$0 = r1;
                super(r2);
                this.mRegistered = false;
            }

            @Override // android.os.Handler
            public void handleMessage(Message r4) {
                if (this.mRegistered == true) goto L6;
                return;
            L6:
                switch(r4.what) {
                    case 1: goto L19;
                    case 2: goto L18;
                    case 3: goto L17;
                    case 4: goto L16;
                    case 5: goto L15;
                    case 6: goto L14;
                    case 7: goto L13;
                    case 8: goto L12;
                    case 9: goto L11;
                    case 10: goto L32;
                    case 11: goto L10;
                    case 12: goto L9;
                    case 13: goto L8;
                    default: goto L32;
                };
            L8:
                this.this$0.onSessionReady();
                return;
            L9:
                this.this$0.onShuffleModeChanged(((Integer) r4.obj).intValue());
                return;
            L10:
                this.this$0.onCaptioningEnabledChanged(((Boolean) r4.obj).booleanValue());
                return;
            L11:
                this.this$0.onRepeatModeChanged(((Integer) r4.obj).intValue());
                return;
            L12:
                this.this$0.onSessionDestroyed();
                return;
            L13:
                Bundle r0 = (Bundle) r4.obj;
                MediaSessionCompat.ensureClassLoader(r0);
                this.this$0.onExtrasChanged(r0);
                return;
            L14:
                this.this$0.onQueueTitleChanged((CharSequence) r4.obj);
                return;
            L15:
                this.this$0.onQueueChanged((List) r4.obj);
                return;
            L16:
                this.this$0.onAudioInfoChanged((PlaybackInfo) r4.obj);
                return;
            L17:
                this.this$0.onMetadataChanged((MediaMetadataCompat) r4.obj);
                return;
            L18:
                this.this$0.onPlaybackStateChanged((PlaybackStateCompat) r4.obj);
                return;
            L19:
                Bundle r1 = r4.getData();
                MediaSessionCompat.ensureClassLoader(r1);
                this.this$0.onSessionEvent((String) r4.obj, r1);
                return;
            }
        }

        private static class StubCompat extends IMediaControllerCallback.Stub {
            private final WeakReference<Callback> mCallback;

            StubCompat(Callback r2) {
                this.mCallback = new WeakReference(r2);
            }

            @Override // android.support.v4.media.session.IMediaControllerCallback
            public void onCaptioningEnabledChanged(boolean r5) throws RemoteException {
                Callback r0 = this.mCallback.get();
                if (r0 == null) goto L6;
                r0.postToHandler(11, Boolean.valueOf(r5), null);
                return;
            }

            @Override // android.support.v4.media.session.IMediaControllerCallback
            public void onEvent(String r3, Bundle r4) throws RemoteException {
                Callback r0 = this.mCallback.get();
                if (r0 == null) goto L6;
                r0.postToHandler(1, r3, r4);
                return;
            }

            @Override // android.support.v4.media.session.IMediaControllerCallback
            public void onExtrasChanged(Bundle r4) throws RemoteException {
                Callback r0 = this.mCallback.get();
                if (r0 == null) goto L6;
                r0.postToHandler(7, r4, null);
                return;
            }

            @Override // android.support.v4.media.session.IMediaControllerCallback
            public void onMetadataChanged(MediaMetadataCompat r4) throws RemoteException {
                Callback r0 = this.mCallback.get();
                if (r0 == null) goto L6;
                r0.postToHandler(3, r4, null);
                return;
            }

            @Override // android.support.v4.media.session.IMediaControllerCallback
            public void onPlaybackStateChanged(PlaybackStateCompat r4) throws RemoteException {
                Callback r0 = this.mCallback.get();
                if (r0 == null) goto L6;
                r0.postToHandler(2, r4, null);
                return;
            }

            @Override // android.support.v4.media.session.IMediaControllerCallback
            public void onQueueChanged(List<MediaSessionCompat.QueueItem> r4) throws RemoteException {
                Callback r0 = this.mCallback.get();
                if (r0 == null) goto L6;
                r0.postToHandler(5, r4, null);
                return;
            }

            @Override // android.support.v4.media.session.IMediaControllerCallback
            public void onQueueTitleChanged(CharSequence r4) throws RemoteException {
                Callback r0 = this.mCallback.get();
                if (r0 == null) goto L6;
                r0.postToHandler(6, r4, null);
                return;
            }

            @Override // android.support.v4.media.session.IMediaControllerCallback
            public void onRepeatModeChanged(int r5) throws RemoteException {
                Callback r0 = this.mCallback.get();
                if (r0 == null) goto L6;
                r0.postToHandler(9, Integer.valueOf(r5), null);
                return;
            }

            @Override // android.support.v4.media.session.IMediaControllerCallback
            public void onSessionDestroyed() throws RemoteException {
                Callback r0 = this.mCallback.get();
                if (r0 == null) goto L6;
                r0.postToHandler(8, null, null);
                return;
            }

            @Override // android.support.v4.media.session.IMediaControllerCallback
            public void onSessionReady() throws RemoteException {
                Callback r0 = this.mCallback.get();
                if (r0 == null) goto L6;
                r0.postToHandler(13, null, null);
                return;
            }

            @Override // android.support.v4.media.session.IMediaControllerCallback
            public void onShuffleModeChanged(int r5) throws RemoteException {
                Callback r0 = this.mCallback.get();
                if (r0 == null) goto L6;
                r0.postToHandler(12, Integer.valueOf(r5), null);
                return;
            }

            @Override // android.support.v4.media.session.IMediaControllerCallback
            public void onShuffleModeChangedRemoved(boolean r1) throws RemoteException {
            }

            @Override // android.support.v4.media.session.IMediaControllerCallback
            public void onVolumeInfoChanged(ParcelableVolumeInfo r9) throws RemoteException {
                Callback r0 = this.mCallback.get();
                if (r0 == null) goto L9;
                PlaybackInfo r1 = null;
                if (r9 == null) goto L7;
                r1 = new PlaybackInfo(r9.volumeType, r9.audioStream, r9.controlType, r9.maxVolume, r9.currentVolume);
            L7:
                r0.postToHandler(4, r1, null);
                return;
            }
        }

        public Callback() {
            this.mCallbackFwk = new MediaControllerCallbackApi21(this);
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            postToHandler(8, null, null);
        }

        public IMediaControllerCallback getIControllerCallback() {
            return this.mIControllerCallback;
        }

        public void onAudioInfoChanged(PlaybackInfo r1) {
        }

        public void onCaptioningEnabledChanged(boolean r1) {
        }

        public void onExtrasChanged(Bundle r1) {
        }

        public void onMetadataChanged(MediaMetadataCompat r1) {
        }

        public void onPlaybackStateChanged(PlaybackStateCompat r1) {
        }

        public void onQueueChanged(List<MediaSessionCompat.QueueItem> r1) {
        }

        public void onQueueTitleChanged(CharSequence r1) {
        }

        public void onRepeatModeChanged(int r1) {
        }

        public void onSessionDestroyed() {
        }

        public void onSessionEvent(String r1, Bundle r2) {
        }

        public void onSessionReady() {
        }

        public void onShuffleModeChanged(int r1) {
        }

        void postToHandler(int r2, Object r3, Bundle r4) {
            if (this.mHandler == null) goto L6;
            Message r0 = this.mHandler.obtainMessage(r2, r3);
            r0.setData(r4);
            r0.sendToTarget();
            return;
        }

        void setHandler(Handler r3) {
            if (r3 == null) goto L4;
            this.mHandler = new MessageHandler(this, r3.getLooper());
            this.mHandler.mRegistered = true;
            return;
        L4:
            if (this.mHandler == null) goto L8;
            this.mHandler.mRegistered = false;
            this.mHandler.removeCallbacksAndMessages(null);
            this.mHandler = null;
            return;
        }
    }

    interface MediaControllerImpl {
        void addQueueItem(MediaDescriptionCompat r1);

        void addQueueItem(MediaDescriptionCompat r1, int r2);

        void adjustVolume(int r1, int r2);

        boolean dispatchMediaButtonEvent(KeyEvent r1);

        Bundle getExtras();

        long getFlags();

        Object getMediaController();

        MediaMetadataCompat getMetadata();

        String getPackageName();

        PlaybackInfo getPlaybackInfo();

        PlaybackStateCompat getPlaybackState();

        List<MediaSessionCompat.QueueItem> getQueue();

        CharSequence getQueueTitle();

        int getRatingType();

        int getRepeatMode();

        PendingIntent getSessionActivity();

        Bundle getSessionInfo();

        int getShuffleMode();

        TransportControls getTransportControls();

        boolean isCaptioningEnabled();

        boolean isSessionReady();

        void registerCallback(Callback r1, Handler r2);

        void removeQueueItem(MediaDescriptionCompat r1);

        void sendCommand(String r1, Bundle r2, ResultReceiver r3);

        void setVolumeTo(int r1, int r2);

        void unregisterCallback(Callback r1);
    }

    static class MediaControllerImplApi21 implements MediaControllerImpl {
        private HashMap<Callback, ExtraCallback> mCallbackMap;
        protected final MediaController mControllerFwk;
        final Object mLock;
        private final List<Callback> mPendingCallbacks;
        protected Bundle mSessionInfo;
        final MediaSessionCompat.Token mSessionToken;

        private static class ExtraBinderRequestResultReceiver extends ResultReceiver {
            private WeakReference<MediaControllerImplApi21> mMediaControllerImpl;

            ExtraBinderRequestResultReceiver(MediaControllerImplApi21 r2) {
                super(null);
                this.mMediaControllerImpl = new WeakReference(r2);
            }

            @Override // android.os.ResultReceiver
            protected void onReceiveResult(int r5, Bundle r6) {
                MediaControllerImplApi21 r0 = this.mMediaControllerImpl.get();
                if (r0 == null) goto L14;
                if (r6 == null) goto L17;
                Object r1 = r0.mLock;
                monitor-enter(r1);
                r0.mSessionToken.setExtraBinder(IMediaSession.Stub.asInterface(BundleCompat.getBinder(r6, MediaSessionCompat.KEY_EXTRA_BINDER)));     // Catch: Throwable -> L11
                r0.mSessionToken.setSession2Token(ParcelUtils.getVersionedParcelable(r6, MediaSessionCompat.KEY_SESSION2_TOKEN));     // Catch: Throwable -> L11
                r0.processPendingCallbacksLocked();     // Catch: Throwable -> L11
                monitor-exit(r1);     // Catch: Throwable -> L11
                return;
            L11:
                th = move-exception;
                throw th;
            L17:
                return;
            }
        }

        private static class ExtraCallback extends Callback.StubCompat {
            ExtraCallback(Callback r1) {
                super(r1);
            }

            @Override // android.support.v4.media.session.MediaControllerCompat.Callback.StubCompat, android.support.v4.media.session.IMediaControllerCallback
            public void onExtrasChanged(Bundle r2) throws RemoteException {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.MediaControllerCompat.Callback.StubCompat, android.support.v4.media.session.IMediaControllerCallback
            public void onMetadataChanged(MediaMetadataCompat r2) throws RemoteException {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.MediaControllerCompat.Callback.StubCompat, android.support.v4.media.session.IMediaControllerCallback
            public void onQueueChanged(List<MediaSessionCompat.QueueItem> r2) throws RemoteException {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.MediaControllerCompat.Callback.StubCompat, android.support.v4.media.session.IMediaControllerCallback
            public void onQueueTitleChanged(CharSequence r2) throws RemoteException {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.MediaControllerCompat.Callback.StubCompat, android.support.v4.media.session.IMediaControllerCallback
            public void onSessionDestroyed() throws RemoteException {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.MediaControllerCompat.Callback.StubCompat, android.support.v4.media.session.IMediaControllerCallback
            public void onVolumeInfoChanged(ParcelableVolumeInfo r2) throws RemoteException {
                throw new AssertionError();
            }
        }

        MediaControllerImplApi21(Context r3, MediaSessionCompat.Token r4) {
            this.mLock = new Object();
            this.mPendingCallbacks = new ArrayList();
            this.mCallbackMap = new HashMap();
            this.mSessionToken = r4;
            this.mControllerFwk = new MediaController(r3, (MediaSession.Token) this.mSessionToken.getToken());
            if (this.mSessionToken.getExtraBinder() != null) goto L6;
            requestExtraBinder();
            return;
        }

        static MediaControllerCompat getMediaController(Activity r4) {
            MediaController r0 = r4.getMediaController();
            if (r0 != null) goto L7;
            return null;
        L7:
            return new MediaControllerCompat(r4, MediaSessionCompat.Token.fromToken(r0.getSessionToken()));
        }

        private void requestExtraBinder() {
            sendCommand(MediaControllerCompat.COMMAND_GET_EXTRA_BINDER, null, new ExtraBinderRequestResultReceiver(this));
        }

        static void setMediaController(Activity r4, MediaControllerCompat r5) {
            MediaController r0 = null;
            if (r5 == null) goto L5;
            r0 = new MediaController(r4, (MediaSession.Token) r5.getSessionToken().getToken());
        L5:
            r4.setMediaController(r0);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public void addQueueItem(MediaDescriptionCompat r7) {
            if ((4 & getFlags()) == 0) goto L7;
            Bundle r2 = new Bundle();
            r2.putParcelable(MediaControllerCompat.COMMAND_ARGUMENT_MEDIA_DESCRIPTION, r7);
            sendCommand(MediaControllerCompat.COMMAND_ADD_QUEUE_ITEM, r2, null);
            return;
        L7:
            throw new UnsupportedOperationException("This session doesn't support queue management operations");
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public void addQueueItem(MediaDescriptionCompat r7, int r8) {
            if ((4 & getFlags()) == 0) goto L7;
            Bundle r2 = new Bundle();
            r2.putParcelable(MediaControllerCompat.COMMAND_ARGUMENT_MEDIA_DESCRIPTION, r7);
            r2.putInt(MediaControllerCompat.COMMAND_ARGUMENT_INDEX, r8);
            sendCommand(MediaControllerCompat.COMMAND_ADD_QUEUE_ITEM_AT, r2, null);
            return;
        L7:
            throw new UnsupportedOperationException("This session doesn't support queue management operations");
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public void adjustVolume(int r2, int r3) {
            this.mControllerFwk.adjustVolume(r2, r3);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public boolean dispatchMediaButtonEvent(KeyEvent r2) {
            return this.mControllerFwk.dispatchMediaButtonEvent(r2);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public Bundle getExtras() {
            return this.mControllerFwk.getExtras();
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public long getFlags() {
            return this.mControllerFwk.getFlags();
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public Object getMediaController() {
            return this.mControllerFwk;
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public MediaMetadataCompat getMetadata() {
            MediaMetadata r0 = this.mControllerFwk.getMetadata();
            if (r0 != null) goto L7;
            return null;
        L7:
            return MediaMetadataCompat.fromMediaMetadata(r0);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public String getPackageName() {
            return this.mControllerFwk.getPackageName();
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public PlaybackInfo getPlaybackInfo() {
            MediaController.PlaybackInfo r0 = this.mControllerFwk.getPlaybackInfo();
            if (r0 != null) goto L7;
            return null;
        L7:
            return new PlaybackInfo(r0.getPlaybackType(), AudioAttributesCompat.wrap(r0.getAudioAttributes()), r0.getVolumeControl(), r0.getMaxVolume(), r0.getCurrentVolume());
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public PlaybackStateCompat getPlaybackState() {
            if (this.mSessionToken.getExtraBinder() != null) goto L13;
        L8:
            PlaybackState r0 = this.mControllerFwk.getPlaybackState();
            if (r0 != null) goto L15;
            return null;
        L15:
            return PlaybackStateCompat.fromPlaybackState(r0);
        L13:
            return this.mSessionToken.getExtraBinder().getPlaybackState();
        L6:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in getPlaybackState.", e);
            goto L8
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public List<MediaSessionCompat.QueueItem> getQueue() {
            List<MediaSession.QueueItem> r0 = this.mControllerFwk.getQueue();
            if (r0 != null) goto L7;
            return null;
        L7:
            return MediaSessionCompat.QueueItem.fromQueueItemList(r0);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public CharSequence getQueueTitle() {
            return this.mControllerFwk.getQueueTitle();
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public int getRatingType() {
            return this.mControllerFwk.getRatingType();
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public int getRepeatMode() {
            if (this.mSessionToken.getExtraBinder() == null) goto L12;
            return this.mSessionToken.getExtraBinder().getRepeatMode();
        L6:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in getRepeatMode.", e);
            return -1;
        L12:
            return -1;
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public PendingIntent getSessionActivity() {
            return this.mControllerFwk.getSessionActivity();
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public Bundle getSessionInfo() {
            if (this.mSessionInfo == null) goto L7;
            return new Bundle(this.mSessionInfo);
        L7:
            if (this.mSessionToken.getExtraBinder() != null) goto L17;
        L12:
            this.mSessionInfo = MediaSessionCompat.unparcelWithClassLoader(this.mSessionInfo);
            if (this.mSessionInfo != null) goto L16;
            return Bundle.EMPTY;
        L16:
            return new Bundle(this.mSessionInfo);
        L17:
            this.mSessionInfo = this.mSessionToken.getExtraBinder().getSessionInfo();     // Catch: RemoteException -> L10
        L10:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in getSessionInfo.", e);
            this.mSessionInfo = Bundle.EMPTY;
            goto L12
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public int getShuffleMode() {
            if (this.mSessionToken.getExtraBinder() == null) goto L12;
            return this.mSessionToken.getExtraBinder().getShuffleMode();
        L6:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in getShuffleMode.", e);
            return -1;
        L12:
            return -1;
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public TransportControls getTransportControls() {
            MediaController.TransportControls r0 = this.mControllerFwk.getTransportControls();
            if (Build.VERSION.SDK_INT < 29) goto L7;
            return new TransportControlsApi29(r0);
        L7:
            if (Build.VERSION.SDK_INT < 24) goto L11;
            return new TransportControlsApi24(r0);
        L11:
            return new TransportControlsApi23(r0);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public boolean isCaptioningEnabled() {
            if (this.mSessionToken.getExtraBinder() == null) goto L12;
            return this.mSessionToken.getExtraBinder().isCaptioningEnabled();
        L6:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in isCaptioningEnabled.", e);
            return false;
        L12:
            return false;
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public boolean isSessionReady() {
            if (this.mSessionToken.getExtraBinder() == null) goto L5;
            return true;
        L5:
            return false;
        }

        void processPendingCallbacksLocked() {
            if (this.mSessionToken.getExtraBinder() != null) goto L5;
            return;
        L5:
            Iterator<Callback> r0 = this.mPendingCallbacks.iterator();
        L7:
            if (r0.hasNext() == false) goto L13;
            Callback r1 = r0.next();
            ExtraCallback r2 = new ExtraCallback(r1);
            this.mCallbackMap.put(r1, r2);
            r1.mIControllerCallback = r2;
            this.mSessionToken.getExtraBinder().registerCallbackListener(r2);     // Catch: RemoteException -> L11
            r1.postToHandler(13, null, null);
        L11:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in registerCallback.", e);
        L13:
            this.mPendingCallbacks.clear();
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public final void registerCallback(Callback r6, Handler r7) {
            this.mControllerFwk.registerCallback(r6.mCallbackFwk, r7);
            Object r0 = this.mLock;
            monitor-enter(r0);
        L15:
            th = move-exception;
            throw th;
        L5:
            if (this.mSessionToken.getExtraBinder() == null) goto L12;
            ExtraCallback r1 = new ExtraCallback(r6);     // Catch: Throwable -> L15
            this.mCallbackMap.put(r6, r1);     // Catch: Throwable -> L15
            r6.mIControllerCallback = r1;     // Catch: Throwable -> L15
            this.mSessionToken.getExtraBinder().registerCallbackListener(r1);     // Catch: RemoteException -> L9 Throwable -> L15
            r6.postToHandler(13, null, null);     // Catch: RemoteException -> L9 Throwable -> L15
        L13:
            monitor-exit(r0);     // Catch: Throwable -> L15
            return;
        L9:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in registerCallback.", e);     // Catch: Throwable -> L15
            goto L13
        L12:
            r6.mIControllerCallback = null;     // Catch: Throwable -> L15
            this.mPendingCallbacks.add(r6);     // Catch: Throwable -> L15
            goto L13
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public void removeQueueItem(MediaDescriptionCompat r7) {
            if ((4 & getFlags()) == 0) goto L7;
            Bundle r2 = new Bundle();
            r2.putParcelable(MediaControllerCompat.COMMAND_ARGUMENT_MEDIA_DESCRIPTION, r7);
            sendCommand(MediaControllerCompat.COMMAND_REMOVE_QUEUE_ITEM, r2, null);
            return;
        L7:
            throw new UnsupportedOperationException("This session doesn't support queue management operations");
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public void sendCommand(String r2, Bundle r3, ResultReceiver r4) {
            this.mControllerFwk.sendCommand(r2, r3, r4);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public void setVolumeTo(int r2, int r3) {
            this.mControllerFwk.setVolumeTo(r2, r3);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public final void unregisterCallback(Callback r5) {
            this.mControllerFwk.unregisterCallback(r5.mCallbackFwk);
            Object r0 = this.mLock;
            monitor-enter(r0);
        L15:
            th = move-exception;
            throw th;
        L5:
            if (this.mSessionToken.getExtraBinder() != null) goto L18;
            this.mPendingCallbacks.remove(r5);     // Catch: Throwable -> L15
        L13:
            monitor-exit(r0);     // Catch: Throwable -> L15
            return;
        L18:
            ExtraCallback r1 = this.mCallbackMap.remove(r5);     // Catch: RemoteException -> L10 Throwable -> L15
            if (r1 == null) goto L13;
            r5.mIControllerCallback = null;     // Catch: RemoteException -> L10 Throwable -> L15
            this.mSessionToken.getExtraBinder().unregisterCallbackListener(r1);     // Catch: RemoteException -> L10 Throwable -> L15
        L10:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in unregisterCallback.", e);     // Catch: Throwable -> L15
            goto L13
        }
    }

    static class MediaControllerImplApi29 extends MediaControllerImplApi21 {
        MediaControllerImplApi29(Context r1, MediaSessionCompat.Token r2) {
            super(r1, r2);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImplApi21, android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public Bundle getSessionInfo() {
            if (this.mSessionInfo != null) goto L5;
            this.mSessionInfo = this.mControllerFwk.getSessionInfo();
            this.mSessionInfo = MediaSessionCompat.unparcelWithClassLoader(this.mSessionInfo);
            if (this.mSessionInfo != null) goto L10;
            return Bundle.EMPTY;
        L10:
            return new Bundle(this.mSessionInfo);
        L5:
            return new Bundle(this.mSessionInfo);
        }
    }

    static class MediaControllerImplBase implements MediaControllerImpl {
        private IMediaSession mBinder;
        private Bundle mSessionInfo;
        private TransportControls mTransportControls;

        MediaControllerImplBase(MediaSessionCompat.Token r2) {
            this.mBinder = IMediaSession.Stub.asInterface((IBinder) r2.getToken());
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public void addQueueItem(MediaDescriptionCompat r7) {
        L7:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in addQueueItem.", e);
            return;
        L3:
            if ((4 & this.mBinder.getFlags()) == 0) goto L6;
            this.mBinder.addQueueItem(r7);     // Catch: RemoteException -> L7
            return;
        L6:
            throw new UnsupportedOperationException("This session doesn't support queue management operations");     // Catch: RemoteException -> L7
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public void addQueueItem(MediaDescriptionCompat r7, int r8) {
        L7:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in addQueueItemAt.", e);
            return;
        L3:
            if ((4 & this.mBinder.getFlags()) == 0) goto L6;
            this.mBinder.addQueueItemAt(r7, r8);     // Catch: RemoteException -> L7
            return;
        L6:
            throw new UnsupportedOperationException("This session doesn't support queue management operations");     // Catch: RemoteException -> L7
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public void adjustVolume(int r4, int r5) {
            this.mBinder.adjustVolume(r4, r5, null);     // Catch: RemoteException -> L4
            return;
        L4:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in adjustVolume.", e);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public boolean dispatchMediaButtonEvent(KeyEvent r4) {
            if (r4 == null) goto L10;
            this.mBinder.sendMediaButton(r4);     // Catch: RemoteException -> L5
            return false;
        L5:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in dispatchMediaButtonEvent.", e);
            return false;
        L10:
            throw new IllegalArgumentException("event may not be null.");
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public Bundle getExtras() {
            return this.mBinder.getExtras();
        L4:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in getExtras.", e);
            return null;
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public long getFlags() {
            return this.mBinder.getFlags();
        L4:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in getFlags.", e);
            return 0;
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public Object getMediaController() {
            return null;
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public MediaMetadataCompat getMetadata() {
            return this.mBinder.getMetadata();
        L4:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in getMetadata.", e);
            return null;
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public String getPackageName() {
            return this.mBinder.getPackageName();
        L4:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in getPackageName.", e);
            return null;
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public PlaybackInfo getPlaybackInfo() {
            ParcelableVolumeInfo r0 = this.mBinder.getVolumeAttributes();     // Catch: RemoteException -> L4
            return new PlaybackInfo(r0.volumeType, r0.audioStream, r0.controlType, r0.maxVolume, r0.currentVolume);
        L4:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in getPlaybackInfo.", e);
            return null;
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public PlaybackStateCompat getPlaybackState() {
            return this.mBinder.getPlaybackState();
        L4:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in getPlaybackState.", e);
            return null;
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public List<MediaSessionCompat.QueueItem> getQueue() {
            return this.mBinder.getQueue();
        L4:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in getQueue.", e);
            return null;
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public CharSequence getQueueTitle() {
            return this.mBinder.getQueueTitle();
        L4:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in getQueueTitle.", e);
            return null;
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public int getRatingType() {
            return this.mBinder.getRatingType();
        L4:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in getRatingType.", e);
            return 0;
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public int getRepeatMode() {
            return this.mBinder.getRepeatMode();
        L4:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in getRepeatMode.", e);
            return -1;
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public PendingIntent getSessionActivity() {
            return this.mBinder.getLaunchPendingIntent();
        L4:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in getSessionActivity.", e);
            return null;
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public Bundle getSessionInfo() {
            this.mSessionInfo = this.mBinder.getSessionInfo();     // Catch: RemoteException -> L4
        L6:
            this.mSessionInfo = MediaSessionCompat.unparcelWithClassLoader(this.mSessionInfo);
            if (this.mSessionInfo != null) goto L10;
            return Bundle.EMPTY;
        L10:
            return new Bundle(this.mSessionInfo);
        L4:
            e = move-exception;
            Log.d(MediaControllerCompat.TAG, "Dead object in getSessionInfo.", e);
            goto L6
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public int getShuffleMode() {
            return this.mBinder.getShuffleMode();
        L4:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in getShuffleMode.", e);
            return -1;
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public TransportControls getTransportControls() {
            if (this.mTransportControls != null) goto L6;
            this.mTransportControls = new TransportControlsBase(this.mBinder);
        L6:
            return this.mTransportControls;
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public boolean isCaptioningEnabled() {
            return this.mBinder.isCaptioningEnabled();
        L4:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in isCaptioningEnabled.", e);
            return false;
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public boolean isSessionReady() {
            return true;
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public void registerCallback(Callback r5, Handler r6) {
            if (r5 == null) goto L10;
            this.mBinder.asBinder().linkToDeath(r5, 0);     // Catch: RemoteException -> L6
            this.mBinder.registerCallbackListener(r5.mIControllerCallback);     // Catch: RemoteException -> L6
            r5.postToHandler(13, null, null);     // Catch: RemoteException -> L6
            return;
        L6:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in registerCallback.", e);
            r5.postToHandler(8, null, null);
            return;
        L10:
            throw new IllegalArgumentException("callback may not be null.");
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public void removeQueueItem(MediaDescriptionCompat r7) {
        L7:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in removeQueueItem.", e);
            return;
        L3:
            if ((4 & this.mBinder.getFlags()) == 0) goto L6;
            this.mBinder.removeQueueItem(r7);     // Catch: RemoteException -> L7
            return;
        L6:
            throw new UnsupportedOperationException("This session doesn't support queue management operations");     // Catch: RemoteException -> L7
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public void sendCommand(String r4, Bundle r5, ResultReceiver r6) {
            IMediaSession r0 = this.mBinder;     // Catch: RemoteException -> L8
            if (r6 != null) goto L5;
            MediaSessionCompat.ResultReceiverWrapper r1 = null;
        L6:
            r0.sendCommand(r4, r5, r1);     // Catch: RemoteException -> L8
            return;
        L5:
            r1 = new MediaSessionCompat.ResultReceiverWrapper(r6);     // Catch: RemoteException -> L8
        L8:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in sendCommand.", e);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public void setVolumeTo(int r4, int r5) {
            this.mBinder.setVolumeTo(r4, r5, null);     // Catch: RemoteException -> L4
            return;
        L4:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in setVolumeTo.", e);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public void unregisterCallback(Callback r4) {
            if (r4 == null) goto L9;
            this.mBinder.unregisterCallbackListener(r4.mIControllerCallback);     // Catch: RemoteException -> L5
            this.mBinder.asBinder().unlinkToDeath(r4, 0);     // Catch: RemoteException -> L5
            return;
        L5:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in unregisterCallback.", e);
            return;
        L9:
            throw new IllegalArgumentException("callback may not be null.");
        }
    }

    public static final class PlaybackInfo {
        public static final int PLAYBACK_TYPE_LOCAL = 1;
        public static final int PLAYBACK_TYPE_REMOTE = 2;
        private final AudioAttributesCompat mAudioAttrsCompat;
        private final int mCurrentVolume;
        private final int mMaxVolume;
        private final int mPlaybackType;
        private final int mVolumeControl;

        PlaybackInfo(int r8, int r9, int r10, int r11, int r12) {
            this(r8, new AudioAttributesCompat.Builder().setLegacyStreamType(r9).build(), r10, r11, r12);
        }

        PlaybackInfo(int r1, AudioAttributesCompat r2, int r3, int r4, int r5) {
            this.mPlaybackType = r1;
            this.mAudioAttrsCompat = r2;
            this.mVolumeControl = r3;
            this.mMaxVolume = r4;
            this.mCurrentVolume = r5;
        }

        public AudioAttributesCompat getAudioAttributes() {
            return this.mAudioAttrsCompat;
        }

        @Deprecated
        public int getAudioStream() {
            return this.mAudioAttrsCompat.getLegacyStreamType();
        }

        public int getCurrentVolume() {
            return this.mCurrentVolume;
        }

        public int getMaxVolume() {
            return this.mMaxVolume;
        }

        public int getPlaybackType() {
            return this.mPlaybackType;
        }

        public int getVolumeControl() {
            return this.mVolumeControl;
        }
    }

    public static abstract class TransportControls {

        @Deprecated
        public static final String EXTRA_LEGACY_STREAM_TYPE = "android.media.session.extra.LEGACY_STREAM_TYPE";

        TransportControls() {
        }

        public abstract void fastForward();

        public abstract void pause();

        public abstract void play();

        public abstract void playFromMediaId(String r1, Bundle r2);

        public abstract void playFromSearch(String r1, Bundle r2);

        public abstract void playFromUri(Uri r1, Bundle r2);

        public abstract void prepare();

        public abstract void prepareFromMediaId(String r1, Bundle r2);

        public abstract void prepareFromSearch(String r1, Bundle r2);

        public abstract void prepareFromUri(Uri r1, Bundle r2);

        public abstract void rewind();

        public abstract void seekTo(long r1);

        public abstract void sendCustomAction(PlaybackStateCompat.CustomAction r1, Bundle r2);

        public abstract void sendCustomAction(String r1, Bundle r2);

        public abstract void setCaptioningEnabled(boolean r1);

        public void setPlaybackSpeed(float r1) {
        }

        public abstract void setRating(RatingCompat r1);

        public abstract void setRating(RatingCompat r1, Bundle r2);

        public abstract void setRepeatMode(int r1);

        public abstract void setShuffleMode(int r1);

        public abstract void skipToNext();

        public abstract void skipToPrevious();

        public abstract void skipToQueueItem(long r1);

        public abstract void stop();
    }

    static class TransportControlsApi21 extends TransportControls {
        protected final MediaController.TransportControls mControlsFwk;

        TransportControlsApi21(MediaController.TransportControls r1) {
            this.mControlsFwk = r1;
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void fastForward() {
            this.mControlsFwk.fastForward();
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void pause() {
            this.mControlsFwk.pause();
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void play() {
            this.mControlsFwk.play();
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void playFromMediaId(String r2, Bundle r3) {
            this.mControlsFwk.playFromMediaId(r2, r3);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void playFromSearch(String r2, Bundle r3) {
            this.mControlsFwk.playFromSearch(r2, r3);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void playFromUri(Uri r3, Bundle r4) {
            if (r3 == null) goto L8;
            if (Uri.EMPTY.equals(r3) == true) goto L8;
            Bundle r0 = new Bundle();
            r0.putParcelable(MediaSessionCompat.ACTION_ARGUMENT_URI, r3);
            r0.putBundle(MediaSessionCompat.ACTION_ARGUMENT_EXTRAS, r4);
            sendCustomAction(MediaSessionCompat.ACTION_PLAY_FROM_URI, r0);
            return;
        L8:
            throw new IllegalArgumentException("You must specify a non-empty Uri for playFromUri.");
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void prepare() {
            sendCustomAction(MediaSessionCompat.ACTION_PREPARE, null);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void prepareFromMediaId(String r3, Bundle r4) {
            Bundle r0 = new Bundle();
            r0.putString(MediaSessionCompat.ACTION_ARGUMENT_MEDIA_ID, r3);
            r0.putBundle(MediaSessionCompat.ACTION_ARGUMENT_EXTRAS, r4);
            sendCustomAction(MediaSessionCompat.ACTION_PREPARE_FROM_MEDIA_ID, r0);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void prepareFromSearch(String r3, Bundle r4) {
            Bundle r0 = new Bundle();
            r0.putString(MediaSessionCompat.ACTION_ARGUMENT_QUERY, r3);
            r0.putBundle(MediaSessionCompat.ACTION_ARGUMENT_EXTRAS, r4);
            sendCustomAction(MediaSessionCompat.ACTION_PREPARE_FROM_SEARCH, r0);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void prepareFromUri(Uri r3, Bundle r4) {
            Bundle r0 = new Bundle();
            r0.putParcelable(MediaSessionCompat.ACTION_ARGUMENT_URI, r3);
            r0.putBundle(MediaSessionCompat.ACTION_ARGUMENT_EXTRAS, r4);
            sendCustomAction(MediaSessionCompat.ACTION_PREPARE_FROM_URI, r0);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void rewind() {
            this.mControlsFwk.rewind();
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void seekTo(long r2) {
            this.mControlsFwk.seekTo(r2);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void sendCustomAction(PlaybackStateCompat.CustomAction r3, Bundle r4) {
            MediaControllerCompat.validateCustomAction(r3.getAction(), r4);
            this.mControlsFwk.sendCustomAction(r3.getAction(), r4);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void sendCustomAction(String r2, Bundle r3) {
            MediaControllerCompat.validateCustomAction(r2, r3);
            this.mControlsFwk.sendCustomAction(r2, r3);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void setCaptioningEnabled(boolean r3) {
            Bundle r0 = new Bundle();
            r0.putBoolean(MediaSessionCompat.ACTION_ARGUMENT_CAPTIONING_ENABLED, r3);
            sendCustomAction(MediaSessionCompat.ACTION_SET_CAPTIONING_ENABLED, r0);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void setPlaybackSpeed(float r3) {
            if (r3 == 0.0f) goto L7;
            Bundle r0 = new Bundle();
            r0.putFloat(MediaSessionCompat.ACTION_ARGUMENT_PLAYBACK_SPEED, r3);
            sendCustomAction(MediaSessionCompat.ACTION_SET_PLAYBACK_SPEED, r0);
            return;
        L7:
            throw new IllegalArgumentException("speed must not be zero");
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void setRating(RatingCompat r3) {
            MediaController.TransportControls r0 = this.mControlsFwk;
            if (r3 == null) goto L5;
            Rating r1 = (Rating) r3.getRating();
        L6:
            r0.setRating(r1);
            return;
        L5:
            r1 = null;
            goto L6
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void setRating(RatingCompat r3, Bundle r4) {
            Bundle r0 = new Bundle();
            r0.putParcelable(MediaSessionCompat.ACTION_ARGUMENT_RATING, r3);
            r0.putBundle(MediaSessionCompat.ACTION_ARGUMENT_EXTRAS, r4);
            sendCustomAction(MediaSessionCompat.ACTION_SET_RATING, r0);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void setRepeatMode(int r3) {
            Bundle r0 = new Bundle();
            r0.putInt(MediaSessionCompat.ACTION_ARGUMENT_REPEAT_MODE, r3);
            sendCustomAction(MediaSessionCompat.ACTION_SET_REPEAT_MODE, r0);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void setShuffleMode(int r3) {
            Bundle r0 = new Bundle();
            r0.putInt(MediaSessionCompat.ACTION_ARGUMENT_SHUFFLE_MODE, r3);
            sendCustomAction(MediaSessionCompat.ACTION_SET_SHUFFLE_MODE, r0);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void skipToNext() {
            this.mControlsFwk.skipToNext();
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void skipToPrevious() {
            this.mControlsFwk.skipToPrevious();
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void skipToQueueItem(long r2) {
            this.mControlsFwk.skipToQueueItem(r2);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void stop() {
            this.mControlsFwk.stop();
        }
    }

    static class TransportControlsApi23 extends TransportControlsApi21 {
        TransportControlsApi23(MediaController.TransportControls r1) {
            super(r1);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControlsApi21, android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void playFromUri(Uri r2, Bundle r3) {
            this.mControlsFwk.playFromUri(r2, r3);
        }
    }

    static class TransportControlsApi24 extends TransportControlsApi23 {
        TransportControlsApi24(MediaController.TransportControls r1) {
            super(r1);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControlsApi21, android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void prepare() {
            this.mControlsFwk.prepare();
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControlsApi21, android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void prepareFromMediaId(String r2, Bundle r3) {
            this.mControlsFwk.prepareFromMediaId(r2, r3);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControlsApi21, android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void prepareFromSearch(String r2, Bundle r3) {
            this.mControlsFwk.prepareFromSearch(r2, r3);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControlsApi21, android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void prepareFromUri(Uri r2, Bundle r3) {
            this.mControlsFwk.prepareFromUri(r2, r3);
        }
    }

    static class TransportControlsApi29 extends TransportControlsApi24 {
        TransportControlsApi29(MediaController.TransportControls r1) {
            super(r1);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControlsApi21, android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void setPlaybackSpeed(float r3) {
            if (r3 == 0.0f) goto L7;
            this.mControlsFwk.setPlaybackSpeed(r3);
            return;
        L7:
            throw new IllegalArgumentException("speed must not be zero");
        }
    }

    static class TransportControlsBase extends TransportControls {
        private IMediaSession mBinder;

        public TransportControlsBase(IMediaSession r1) {
            this.mBinder = r1;
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void fastForward() {
            this.mBinder.fastForward();     // Catch: RemoteException -> L4
            return;
        L4:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in fastForward.", e);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void pause() {
            this.mBinder.pause();     // Catch: RemoteException -> L4
            return;
        L4:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in pause.", e);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void play() {
            this.mBinder.play();     // Catch: RemoteException -> L4
            return;
        L4:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in play.", e);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void playFromMediaId(String r4, Bundle r5) {
            this.mBinder.playFromMediaId(r4, r5);     // Catch: RemoteException -> L4
            return;
        L4:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in playFromMediaId.", e);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void playFromSearch(String r4, Bundle r5) {
            this.mBinder.playFromSearch(r4, r5);     // Catch: RemoteException -> L4
            return;
        L4:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in playFromSearch.", e);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void playFromUri(Uri r4, Bundle r5) {
            this.mBinder.playFromUri(r4, r5);     // Catch: RemoteException -> L4
            return;
        L4:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in playFromUri.", e);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void prepare() {
            this.mBinder.prepare();     // Catch: RemoteException -> L4
            return;
        L4:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in prepare.", e);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void prepareFromMediaId(String r4, Bundle r5) {
            this.mBinder.prepareFromMediaId(r4, r5);     // Catch: RemoteException -> L4
            return;
        L4:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in prepareFromMediaId.", e);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void prepareFromSearch(String r4, Bundle r5) {
            this.mBinder.prepareFromSearch(r4, r5);     // Catch: RemoteException -> L4
            return;
        L4:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in prepareFromSearch.", e);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void prepareFromUri(Uri r4, Bundle r5) {
            this.mBinder.prepareFromUri(r4, r5);     // Catch: RemoteException -> L4
            return;
        L4:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in prepareFromUri.", e);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void rewind() {
            this.mBinder.rewind();     // Catch: RemoteException -> L4
            return;
        L4:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in rewind.", e);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void seekTo(long r4) {
            this.mBinder.seekTo(r4);     // Catch: RemoteException -> L4
            return;
        L4:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in seekTo.", e);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void sendCustomAction(PlaybackStateCompat.CustomAction r2, Bundle r3) {
            sendCustomAction(r2.getAction(), r3);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void sendCustomAction(String r4, Bundle r5) {
            MediaControllerCompat.validateCustomAction(r4, r5);
            this.mBinder.sendCustomAction(r4, r5);     // Catch: RemoteException -> L5
            return;
        L5:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in sendCustomAction.", e);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void setCaptioningEnabled(boolean r4) {
            this.mBinder.setCaptioningEnabled(r4);     // Catch: RemoteException -> L4
            return;
        L4:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in setCaptioningEnabled.", e);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void setPlaybackSpeed(float r4) {
            if (r4 == 0.0f) goto L10;
            this.mBinder.setPlaybackSpeed(r4);     // Catch: RemoteException -> L6
            return;
        L6:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in setPlaybackSpeed.", e);
            return;
        L10:
            throw new IllegalArgumentException("speed must not be zero");
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void setRating(RatingCompat r4) {
            this.mBinder.rate(r4);     // Catch: RemoteException -> L4
            return;
        L4:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in setRating.", e);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void setRating(RatingCompat r4, Bundle r5) {
            this.mBinder.rateWithExtras(r4, r5);     // Catch: RemoteException -> L4
            return;
        L4:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in setRating.", e);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void setRepeatMode(int r4) {
            this.mBinder.setRepeatMode(r4);     // Catch: RemoteException -> L4
            return;
        L4:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in setRepeatMode.", e);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void setShuffleMode(int r4) {
            this.mBinder.setShuffleMode(r4);     // Catch: RemoteException -> L4
            return;
        L4:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in setShuffleMode.", e);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void skipToNext() {
            this.mBinder.next();     // Catch: RemoteException -> L4
            return;
        L4:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in skipToNext.", e);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void skipToPrevious() {
            this.mBinder.previous();     // Catch: RemoteException -> L4
            return;
        L4:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in skipToPrevious.", e);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void skipToQueueItem(long r4) {
            this.mBinder.skipToQueueItem(r4);     // Catch: RemoteException -> L4
            return;
        L4:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in skipToQueueItem.", e);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void stop() {
            this.mBinder.stop();     // Catch: RemoteException -> L4
            return;
        L4:
            e = move-exception;
            Log.e(MediaControllerCompat.TAG, "Dead object in stop.", e);
        }
    }

    public MediaControllerCompat(Context r3, MediaSessionCompat.Token r4) {
        if (r4 == null) goto L10;
        this.mRegisteredCallbacks = Collections.synchronizedSet(new HashSet());
        this.mToken = r4;
        if (Build.VERSION.SDK_INT < 29) goto L7;
        this.mImpl = new MediaControllerImplApi29(r3, r4);
        return;
    L7:
        this.mImpl = new MediaControllerImplApi21(r3, r4);
        return;
    L10:
        throw new IllegalArgumentException("sessionToken must not be null");
    }

    public MediaControllerCompat(Context r2, MediaSessionCompat r3) {
        this(r2, r3.getSessionToken());
    }

    public static MediaControllerCompat getMediaController(Activity r2) {
        Object r0 = r2.getWindow().getDecorView().getTag(C0175R.id.media_controller_compat_view_tag);
        if ((r0 instanceof MediaControllerCompat) == false) goto L7;
        return (MediaControllerCompat) r0;
    L7:
        return MediaControllerImplApi21.getMediaController(r2);
    }

    public static void setMediaController(Activity r2, MediaControllerCompat r3) {
        r2.getWindow().getDecorView().setTag(C0175R.id.media_controller_compat_view_tag, r3);
        MediaControllerImplApi21.setMediaController(r2, r3);
    }

    static void validateCustomAction(String r3, Bundle r4) {
        if (r3 != null) goto L5;
        return;
    L5:
        switch(r3.hashCode()) {
            case -1348483723: goto L11;
            case 503011406: goto L8;
            default: goto L13;
        };
    L13:
        byte r0 = -1;
    L14:
        switch(r0) {
            case 0: goto L16;
            case 1: goto L16;
            default: goto L22;
        };
    L16:
        if (r4 == null) goto L21;
        if (r4.containsKey(MediaSessionCompat.ARGUMENT_MEDIA_ATTRIBUTE) == false) goto L21;
        return;
    L21:
        throw new IllegalArgumentException("An extra field android.support.v4.media.session.ARGUMENT_MEDIA_ATTRIBUTE is required for this action " + r3 + ".");
    L22:
        return;
    L8:
        if (r3.equals(MediaSessionCompat.ACTION_UNFOLLOW) == false) goto L13;
        r0 = 1;
        goto L14
    L11:
        if (r3.equals(MediaSessionCompat.ACTION_FOLLOW) == false) goto L13;
        r0 = 0;
        goto L14
    }

    public void addQueueItem(MediaDescriptionCompat r2) {
        this.mImpl.addQueueItem(r2);
    }

    public void addQueueItem(MediaDescriptionCompat r2, int r3) {
        this.mImpl.addQueueItem(r2, r3);
    }

    public void adjustVolume(int r2, int r3) {
        this.mImpl.adjustVolume(r2, r3);
    }

    public boolean dispatchMediaButtonEvent(KeyEvent r3) {
        if (r3 == null) goto L6;
        return this.mImpl.dispatchMediaButtonEvent(r3);
    L6:
        throw new IllegalArgumentException("KeyEvent may not be null");
    }

    public Bundle getExtras() {
        return this.mImpl.getExtras();
    }

    public long getFlags() {
        return this.mImpl.getFlags();
    }

    public Object getMediaController() {
        return this.mImpl.getMediaController();
    }

    public MediaMetadataCompat getMetadata() {
        return this.mImpl.getMetadata();
    }

    public String getPackageName() {
        return this.mImpl.getPackageName();
    }

    public PlaybackInfo getPlaybackInfo() {
        return this.mImpl.getPlaybackInfo();
    }

    public PlaybackStateCompat getPlaybackState() {
        return this.mImpl.getPlaybackState();
    }

    public List<MediaSessionCompat.QueueItem> getQueue() {
        return this.mImpl.getQueue();
    }

    public CharSequence getQueueTitle() {
        return this.mImpl.getQueueTitle();
    }

    public int getRatingType() {
        return this.mImpl.getRatingType();
    }

    public int getRepeatMode() {
        return this.mImpl.getRepeatMode();
    }

    public VersionedParcelable getSession2Token() {
        return this.mToken.getSession2Token();
    }

    public PendingIntent getSessionActivity() {
        return this.mImpl.getSessionActivity();
    }

    public Bundle getSessionInfo() {
        return this.mImpl.getSessionInfo();
    }

    public MediaSessionCompat.Token getSessionToken() {
        return this.mToken;
    }

    public int getShuffleMode() {
        return this.mImpl.getShuffleMode();
    }

    public TransportControls getTransportControls() {
        return this.mImpl.getTransportControls();
    }

    public boolean isCaptioningEnabled() {
        return this.mImpl.isCaptioningEnabled();
    }

    public boolean isSessionReady() {
        return this.mImpl.isSessionReady();
    }

    public void registerCallback(Callback r2) {
        registerCallback(r2, null);
    }

    public void registerCallback(Callback r3, Handler r4) {
        if (r3 == null) goto L12;
        if (this.mRegisteredCallbacks.add(r3) == true) goto L7;
        Log.w(TAG, "the callback has already been registered");
        return;
    L7:
        if (r4 != null) goto L9;
        r4 = new Handler();
    L9:
        r3.setHandler(r4);
        this.mImpl.registerCallback(r3, r4);
        return;
    L12:
        throw new IllegalArgumentException("callback must not be null");
    }

    public void removeQueueItem(MediaDescriptionCompat r2) {
        this.mImpl.removeQueueItem(r2);
    }

    @Deprecated
    public void removeQueueItemAt(int r4) {
        List<MediaSessionCompat.QueueItem> r0 = getQueue();
        if (r0 == null) goto L11;
        if (r4 >= 0) goto L6;
        return;
    L6:
        if (r4 >= r0.size()) goto L13;
        MediaSessionCompat.QueueItem r1 = r0.get(r4);
        if (r1 == null) goto L14;
        removeQueueItem(r1.getDescription());
        return;
    L14:
        return;
    L13:
        return;
    }

    public void sendCommand(String r3, Bundle r4, ResultReceiver r5) {
        if (TextUtils.isEmpty(r3) == true) goto L7;
        this.mImpl.sendCommand(r3, r4, r5);
        return;
    L7:
        throw new IllegalArgumentException("command must neither be null nor empty");
    }

    public void setVolumeTo(int r2, int r3) {
        this.mImpl.setVolumeTo(r2, r3);
    }

    public void unregisterCallback(Callback r3) {
        if (r3 == null) goto L15;
        if (this.mRegisteredCallbacks.remove(r3) == true) goto L16;
        Log.w(TAG, "the callback has never been registered");
        return;
    L16:
        this.mImpl.unregisterCallback(r3);     // Catch: Throwable -> L11
        r3.setHandler(null);
        return;
    L11:
        th = move-exception;
        r3.setHandler(null);
        throw th;
    L15:
        throw new IllegalArgumentException("callback must not be null");
    }
}
