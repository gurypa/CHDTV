package android.support.v4.media.session;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaDescription;
import android.media.MediaMetadata;
import android.media.Rating;
import android.media.RemoteControlClient;
import android.media.VolumeProvider;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.BadParcelableException;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.RatingCompat;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import androidx.core.app.BundleCompat;
import androidx.media.MediaSessionManager;
import androidx.media.VolumeProviderCompat;
import androidx.media.session.MediaButtonReceiver;
import androidx.media3.common.MimeTypes;
import androidx.versionedparcelable.ParcelUtils;
import androidx.versionedparcelable.VersionedParcelable;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/* JADX INFO: loaded from: classes.dex */
public class MediaSessionCompat {
    public static final String ACTION_ARGUMENT_CAPTIONING_ENABLED = "android.support.v4.media.session.action.ARGUMENT_CAPTIONING_ENABLED";
    public static final String ACTION_ARGUMENT_EXTRAS = "android.support.v4.media.session.action.ARGUMENT_EXTRAS";
    public static final String ACTION_ARGUMENT_MEDIA_ID = "android.support.v4.media.session.action.ARGUMENT_MEDIA_ID";
    public static final String ACTION_ARGUMENT_PLAYBACK_SPEED = "android.support.v4.media.session.action.ARGUMENT_PLAYBACK_SPEED";
    public static final String ACTION_ARGUMENT_QUERY = "android.support.v4.media.session.action.ARGUMENT_QUERY";
    public static final String ACTION_ARGUMENT_RATING = "android.support.v4.media.session.action.ARGUMENT_RATING";
    public static final String ACTION_ARGUMENT_REPEAT_MODE = "android.support.v4.media.session.action.ARGUMENT_REPEAT_MODE";
    public static final String ACTION_ARGUMENT_SHUFFLE_MODE = "android.support.v4.media.session.action.ARGUMENT_SHUFFLE_MODE";
    public static final String ACTION_ARGUMENT_URI = "android.support.v4.media.session.action.ARGUMENT_URI";
    public static final String ACTION_FLAG_AS_INAPPROPRIATE = "android.support.v4.media.session.action.FLAG_AS_INAPPROPRIATE";
    public static final String ACTION_FOLLOW = "android.support.v4.media.session.action.FOLLOW";
    public static final String ACTION_PLAY_FROM_URI = "android.support.v4.media.session.action.PLAY_FROM_URI";
    public static final String ACTION_PREPARE = "android.support.v4.media.session.action.PREPARE";
    public static final String ACTION_PREPARE_FROM_MEDIA_ID = "android.support.v4.media.session.action.PREPARE_FROM_MEDIA_ID";
    public static final String ACTION_PREPARE_FROM_SEARCH = "android.support.v4.media.session.action.PREPARE_FROM_SEARCH";
    public static final String ACTION_PREPARE_FROM_URI = "android.support.v4.media.session.action.PREPARE_FROM_URI";
    public static final String ACTION_SET_CAPTIONING_ENABLED = "android.support.v4.media.session.action.SET_CAPTIONING_ENABLED";
    public static final String ACTION_SET_PLAYBACK_SPEED = "android.support.v4.media.session.action.SET_PLAYBACK_SPEED";
    public static final String ACTION_SET_RATING = "android.support.v4.media.session.action.SET_RATING";
    public static final String ACTION_SET_REPEAT_MODE = "android.support.v4.media.session.action.SET_REPEAT_MODE";
    public static final String ACTION_SET_SHUFFLE_MODE = "android.support.v4.media.session.action.SET_SHUFFLE_MODE";
    public static final String ACTION_SKIP_AD = "android.support.v4.media.session.action.SKIP_AD";
    public static final String ACTION_UNFOLLOW = "android.support.v4.media.session.action.UNFOLLOW";
    public static final String ARGUMENT_MEDIA_ATTRIBUTE = "android.support.v4.media.session.ARGUMENT_MEDIA_ATTRIBUTE";
    public static final String ARGUMENT_MEDIA_ATTRIBUTE_VALUE = "android.support.v4.media.session.ARGUMENT_MEDIA_ATTRIBUTE_VALUE";
    private static final String DATA_CALLING_PACKAGE = "data_calling_pkg";
    private static final String DATA_CALLING_PID = "data_calling_pid";
    private static final String DATA_CALLING_UID = "data_calling_uid";
    private static final String DATA_EXTRAS = "data_extras";

    @Deprecated
    public static final int FLAG_HANDLES_MEDIA_BUTTONS = 1;
    public static final int FLAG_HANDLES_QUEUE_COMMANDS = 4;

    @Deprecated
    public static final int FLAG_HANDLES_TRANSPORT_CONTROLS = 2;
    public static final String KEY_EXTRA_BINDER = "android.support.v4.media.session.EXTRA_BINDER";
    public static final String KEY_SESSION2_TOKEN = "android.support.v4.media.session.SESSION_TOKEN2";
    public static final String KEY_TOKEN = "android.support.v4.media.session.TOKEN";
    private static final int MAX_BITMAP_SIZE_IN_DP = 320;
    public static final int MEDIA_ATTRIBUTE_ALBUM = 1;
    public static final int MEDIA_ATTRIBUTE_ARTIST = 0;
    public static final int MEDIA_ATTRIBUTE_PLAYLIST = 2;
    static final String TAG = "MediaSessionCompat";
    static int sMaxBitmapSize;
    private final ArrayList<OnActiveChangeListener> mActiveListeners;
    private final MediaControllerCompat mController;
    private final MediaSessionImpl mImpl;

    public static abstract class Callback {
        final MediaSession.Callback mCallbackFwk;
        CallbackHandler mCallbackHandler;
        final Object mLock;
        private boolean mMediaPlayPausePendingOnHandler;
        WeakReference<MediaSessionImpl> mSessionImpl;

        private class CallbackHandler extends Handler {
            private static final int MSG_MEDIA_PLAY_PAUSE_KEY_DOUBLE_TAP_TIMEOUT = 1;
            final /* synthetic */ Callback this$0;

            CallbackHandler(Callback r1, Looper r2) {
                this.this$0 = r1;
                super(r2);
            }

            @Override // android.os.Handler
            public void handleMessage(Message r5) {
                if (r5.what != 1) goto L18;
                Object r0 = this.this$0.mLock;
                monitor-enter(r0);
                MediaSessionImpl r1 = this.this$0.mSessionImpl.get();     // Catch: Throwable -> L15
                CallbackHandler r2 = this.this$0.mCallbackHandler;     // Catch: Throwable -> L15
                monitor-exit(r0);     // Catch: Throwable -> L15
                if (r1 != null) goto L10;
                return;
            L10:
                if (this.this$0 != r1.getCallback()) goto L21;
                if (r2 == null) goto L22;
                r1.setCurrentControllerInfo((MediaSessionManager.RemoteUserInfo) r5.obj);
                this.this$0.handleMediaPlayPauseIfPendingOnHandler(r1, r2);
                r1.setCurrentControllerInfo(null);
                return;
            L22:
                return;
            L21:
                return;
            L15:
                th = move-exception;
                throw th;
            }
        }

        private class MediaSessionCallbackApi21 extends MediaSession.Callback {
            final /* synthetic */ Callback this$0;

            MediaSessionCallbackApi21(Callback r1) {
                this.this$0 = r1;
            }

            private void clearCurrentControllerInfo(MediaSessionImpl r2) {
                r2.setCurrentControllerInfo(null);
            }

            private MediaSessionImplApi21 getSessionImplIfCallbackIsSet() {
                Object r0 = this.this$0.mLock;
                monitor-enter(r0);
                MediaSessionImplApi21 r1 = (MediaSessionImplApi21) this.this$0.mSessionImpl.get();     // Catch: Throwable -> L12
                monitor-exit(r0);     // Catch: Throwable -> L12
                if (r1 != null) goto L8;
            L10:
                return null;
            L8:
                if (this.this$0 != r1.getCallback()) goto L10;
                return r1;
            L12:
                th = move-exception;
                throw th;
            }

            private void setCurrentControllerInfo(MediaSessionImpl r4) {
                if (Build.VERSION.SDK_INT < 28) goto L5;
                return;
            L5:
                String r0 = r4.getCallingPackage();
                if (TextUtils.isEmpty(r0) == false) goto L8;
                r0 = MediaSessionManager.RemoteUserInfo.LEGACY_CONTROLLER;
            L8:
                r4.setCurrentControllerInfo(new MediaSessionManager.RemoteUserInfo(r0, -1, -1));
            }

            @Override // android.media.session.MediaSession.Callback
            public void onCommand(String r7, Bundle r8, ResultReceiver r9) {
                MediaSessionImplApi21 r0 = getSessionImplIfCallbackIsSet();
                if (r0 != null) goto L5;
                return;
            L5:
                MediaSessionCompat.ensureClassLoader(r8);
                setCurrentControllerInfo(r0);
                QueueItem r2 = null;
                IBinder r3 = null;
                r2 = null;
                if (r7.equals(MediaControllerCompat.COMMAND_GET_EXTRA_BINDER) == false) goto L15;
                Bundle r1 = new Bundle();     // Catch: BadParcelableException -> L39
                Token r4 = r0.getSessionToken();     // Catch: BadParcelableException -> L39
                IMediaSession r5 = r4.getExtraBinder();     // Catch: BadParcelableException -> L39
                if (r5 == null) goto L12;
                r3 = r5.asBinder();     // Catch: BadParcelableException -> L39
            L12:
                BundleCompat.putBinder(r1, MediaSessionCompat.KEY_EXTRA_BINDER, r3);     // Catch: BadParcelableException -> L39
                ParcelUtils.putVersionedParcelable(r1, MediaSessionCompat.KEY_SESSION2_TOKEN, r4.getSession2Token());     // Catch: BadParcelableException -> L39
                r9.send(0, r1);     // Catch: BadParcelableException -> L39
            L41:
                clearCurrentControllerInfo(r0);
                return;
            L15:
                if (r7.equals(MediaControllerCompat.COMMAND_ADD_QUEUE_ITEM) == false) goto L19;
                this.this$0.onAddQueueItem((MediaDescriptionCompat) r8.getParcelable(MediaControllerCompat.COMMAND_ARGUMENT_MEDIA_DESCRIPTION));     // Catch: BadParcelableException -> L39
                goto L41
            L19:
                if (r7.equals(MediaControllerCompat.COMMAND_ADD_QUEUE_ITEM_AT) == false) goto L22;
                this.this$0.onAddQueueItem((MediaDescriptionCompat) r8.getParcelable(MediaControllerCompat.COMMAND_ARGUMENT_MEDIA_DESCRIPTION), r8.getInt(MediaControllerCompat.COMMAND_ARGUMENT_INDEX));     // Catch: BadParcelableException -> L39
                goto L41
            L22:
                if (r7.equals(MediaControllerCompat.COMMAND_REMOVE_QUEUE_ITEM) == false) goto L25;
                this.this$0.onRemoveQueueItem((MediaDescriptionCompat) r8.getParcelable(MediaControllerCompat.COMMAND_ARGUMENT_MEDIA_DESCRIPTION));     // Catch: BadParcelableException -> L39
                goto L41
            L25:
                if (r7.equals(MediaControllerCompat.COMMAND_REMOVE_QUEUE_ITEM_AT) == true) goto L27;
                this.this$0.onCommand(r7, r8, r9);     // Catch: BadParcelableException -> L39
                goto L41
            L27:
                if (r0.mQueue == null) goto L41;
                int r6 = r8.getInt(MediaControllerCompat.COMMAND_ARGUMENT_INDEX, -1);     // Catch: BadParcelableException -> L39
                if (r6 >= 0) goto L31;
            L34:
                if (r2 == null) goto L41;
                this.this$0.onRemoveQueueItem(r2.getDescription());     // Catch: BadParcelableException -> L39
                goto L41
            L31:
                if (r6 >= r0.mQueue.size()) goto L34;
                r2 = r0.mQueue.get(r6);     // Catch: BadParcelableException -> L39
                goto L34
            L40:
                Log.e(MediaSessionCompat.TAG, "Could not unparcel the extra data.");
                goto L41
            }

            @Override // android.media.session.MediaSession.Callback
            public void onCustomAction(String r5, Bundle r6) {
                MediaSessionImplApi21 r0 = getSessionImplIfCallbackIsSet();
                if (r0 != null) goto L5;
                return;
            L5:
                MediaSessionCompat.ensureClassLoader(r6);
                setCurrentControllerInfo(r0);
                if (r5.equals(MediaSessionCompat.ACTION_PLAY_FROM_URI) == false) goto L11;
                Uri r1 = (Uri) r6.getParcelable(MediaSessionCompat.ACTION_ARGUMENT_URI);     // Catch: BadParcelableException -> L39
                Bundle r2 = r6.getBundle(MediaSessionCompat.ACTION_ARGUMENT_EXTRAS);     // Catch: BadParcelableException -> L39
                MediaSessionCompat.ensureClassLoader(r2);     // Catch: BadParcelableException -> L39
                this.this$0.onPlayFromUri(r1, r2);     // Catch: BadParcelableException -> L39
            L43:
            L41:
                clearCurrentControllerInfo(r0);
                return;
            L11:
                if (r5.equals(MediaSessionCompat.ACTION_PREPARE) == false) goto L14;
                this.this$0.onPrepare();     // Catch: BadParcelableException -> L39
                goto L43
            L14:
                if (r5.equals(MediaSessionCompat.ACTION_PREPARE_FROM_MEDIA_ID) == false) goto L17;
                String r3 = r6.getString(MediaSessionCompat.ACTION_ARGUMENT_MEDIA_ID);     // Catch: BadParcelableException -> L39
                Bundle r4 = r6.getBundle(MediaSessionCompat.ACTION_ARGUMENT_EXTRAS);     // Catch: BadParcelableException -> L39
                MediaSessionCompat.ensureClassLoader(r4);     // Catch: BadParcelableException -> L39
                this.this$0.onPrepareFromMediaId(r3, r4);     // Catch: BadParcelableException -> L39
                goto L43
            L17:
                if (r5.equals(MediaSessionCompat.ACTION_PREPARE_FROM_SEARCH) == false) goto L20;
                String r7 = r6.getString(MediaSessionCompat.ACTION_ARGUMENT_QUERY);     // Catch: BadParcelableException -> L39
                Bundle r8 = r6.getBundle(MediaSessionCompat.ACTION_ARGUMENT_EXTRAS);     // Catch: BadParcelableException -> L39
                MediaSessionCompat.ensureClassLoader(r8);     // Catch: BadParcelableException -> L39
                this.this$0.onPrepareFromSearch(r7, r8);     // Catch: BadParcelableException -> L39
                goto L43
            L20:
                if (r5.equals(MediaSessionCompat.ACTION_PREPARE_FROM_URI) == false) goto L23;
                Uri r9 = (Uri) r6.getParcelable(MediaSessionCompat.ACTION_ARGUMENT_URI);     // Catch: BadParcelableException -> L39
                Bundle r10 = r6.getBundle(MediaSessionCompat.ACTION_ARGUMENT_EXTRAS);     // Catch: BadParcelableException -> L39
                MediaSessionCompat.ensureClassLoader(r10);     // Catch: BadParcelableException -> L39
                this.this$0.onPrepareFromUri(r9, r10);     // Catch: BadParcelableException -> L39
                goto L43
            L23:
                if (r5.equals(MediaSessionCompat.ACTION_SET_CAPTIONING_ENABLED) == false) goto L26;
                boolean r11 = r6.getBoolean(MediaSessionCompat.ACTION_ARGUMENT_CAPTIONING_ENABLED);     // Catch: BadParcelableException -> L39
                this.this$0.onSetCaptioningEnabled(r11);     // Catch: BadParcelableException -> L39
                goto L43
            L26:
                if (r5.equals(MediaSessionCompat.ACTION_SET_REPEAT_MODE) == false) goto L29;
                int r12 = r6.getInt(MediaSessionCompat.ACTION_ARGUMENT_REPEAT_MODE);     // Catch: BadParcelableException -> L39
                this.this$0.onSetRepeatMode(r12);     // Catch: BadParcelableException -> L39
                goto L43
            L29:
                if (r5.equals(MediaSessionCompat.ACTION_SET_SHUFFLE_MODE) == false) goto L32;
                int r13 = r6.getInt(MediaSessionCompat.ACTION_ARGUMENT_SHUFFLE_MODE);     // Catch: BadParcelableException -> L39
                this.this$0.onSetShuffleMode(r13);     // Catch: BadParcelableException -> L39
                goto L43
            L32:
                if (r5.equals(MediaSessionCompat.ACTION_SET_RATING) == false) goto L35;
                RatingCompat r14 = (RatingCompat) r6.getParcelable(MediaSessionCompat.ACTION_ARGUMENT_RATING);     // Catch: BadParcelableException -> L39
                Bundle r15 = r6.getBundle(MediaSessionCompat.ACTION_ARGUMENT_EXTRAS);     // Catch: BadParcelableException -> L39
                MediaSessionCompat.ensureClassLoader(r15);     // Catch: BadParcelableException -> L39
                this.this$0.onSetRating(r14, r15);     // Catch: BadParcelableException -> L39
                goto L43
            L35:
                if (r5.equals(MediaSessionCompat.ACTION_SET_PLAYBACK_SPEED) == false) goto L37;
                float r16 = r6.getFloat(MediaSessionCompat.ACTION_ARGUMENT_PLAYBACK_SPEED, 1.0f);     // Catch: BadParcelableException -> L39
                this.this$0.onSetPlaybackSpeed(r16);     // Catch: BadParcelableException -> L39
                goto L43
            L37:
                this.this$0.onCustomAction(r5, r6);     // Catch: BadParcelableException -> L39
            L40:
                Log.e(MediaSessionCompat.TAG, "Could not unparcel the data.");
                goto L41
            }

            @Override // android.media.session.MediaSession.Callback
            public void onFastForward() {
                MediaSessionImplApi21 r0 = getSessionImplIfCallbackIsSet();
                if (r0 != null) goto L5;
                return;
            L5:
                setCurrentControllerInfo(r0);
                this.this$0.onFastForward();
                clearCurrentControllerInfo(r0);
            }

            @Override // android.media.session.MediaSession.Callback
            public boolean onMediaButtonEvent(Intent r5) {
                MediaSessionImplApi21 r0 = getSessionImplIfCallbackIsSet();
                if (r0 != null) goto L5;
                return false;
            L5:
                setCurrentControllerInfo(r0);
                boolean r2 = this.this$0.onMediaButtonEvent(r5);
                clearCurrentControllerInfo(r0);
                if (r2 == false) goto L8;
            L9:
                return true;
            L8:
                if (super.onMediaButtonEvent(r5) == true) goto L9;
                return false;
            }

            @Override // android.media.session.MediaSession.Callback
            public void onPause() {
                MediaSessionImplApi21 r0 = getSessionImplIfCallbackIsSet();
                if (r0 != null) goto L5;
                return;
            L5:
                setCurrentControllerInfo(r0);
                this.this$0.onPause();
                clearCurrentControllerInfo(r0);
            }

            @Override // android.media.session.MediaSession.Callback
            public void onPlay() {
                MediaSessionImplApi21 r0 = getSessionImplIfCallbackIsSet();
                if (r0 != null) goto L5;
                return;
            L5:
                setCurrentControllerInfo(r0);
                this.this$0.onPlay();
                clearCurrentControllerInfo(r0);
            }

            @Override // android.media.session.MediaSession.Callback
            public void onPlayFromMediaId(String r3, Bundle r4) {
                MediaSessionImplApi21 r0 = getSessionImplIfCallbackIsSet();
                if (r0 != null) goto L5;
                return;
            L5:
                MediaSessionCompat.ensureClassLoader(r4);
                setCurrentControllerInfo(r0);
                this.this$0.onPlayFromMediaId(r3, r4);
                clearCurrentControllerInfo(r0);
            }

            @Override // android.media.session.MediaSession.Callback
            public void onPlayFromSearch(String r3, Bundle r4) {
                MediaSessionImplApi21 r0 = getSessionImplIfCallbackIsSet();
                if (r0 != null) goto L5;
                return;
            L5:
                MediaSessionCompat.ensureClassLoader(r4);
                setCurrentControllerInfo(r0);
                this.this$0.onPlayFromSearch(r3, r4);
                clearCurrentControllerInfo(r0);
            }

            @Override // android.media.session.MediaSession.Callback
            public void onPlayFromUri(Uri r3, Bundle r4) {
                MediaSessionImplApi21 r0 = getSessionImplIfCallbackIsSet();
                if (r0 != null) goto L5;
                return;
            L5:
                MediaSessionCompat.ensureClassLoader(r4);
                setCurrentControllerInfo(r0);
                this.this$0.onPlayFromUri(r3, r4);
                clearCurrentControllerInfo(r0);
            }

            @Override // android.media.session.MediaSession.Callback
            public void onPrepare() {
                MediaSessionImplApi21 r0 = getSessionImplIfCallbackIsSet();
                if (r0 != null) goto L5;
                return;
            L5:
                setCurrentControllerInfo(r0);
                this.this$0.onPrepare();
                clearCurrentControllerInfo(r0);
            }

            @Override // android.media.session.MediaSession.Callback
            public void onPrepareFromMediaId(String r3, Bundle r4) {
                MediaSessionImplApi21 r0 = getSessionImplIfCallbackIsSet();
                if (r0 != null) goto L5;
                return;
            L5:
                MediaSessionCompat.ensureClassLoader(r4);
                setCurrentControllerInfo(r0);
                this.this$0.onPrepareFromMediaId(r3, r4);
                clearCurrentControllerInfo(r0);
            }

            @Override // android.media.session.MediaSession.Callback
            public void onPrepareFromSearch(String r3, Bundle r4) {
                MediaSessionImplApi21 r0 = getSessionImplIfCallbackIsSet();
                if (r0 != null) goto L5;
                return;
            L5:
                MediaSessionCompat.ensureClassLoader(r4);
                setCurrentControllerInfo(r0);
                this.this$0.onPrepareFromSearch(r3, r4);
                clearCurrentControllerInfo(r0);
            }

            @Override // android.media.session.MediaSession.Callback
            public void onPrepareFromUri(Uri r3, Bundle r4) {
                MediaSessionImplApi21 r0 = getSessionImplIfCallbackIsSet();
                if (r0 != null) goto L5;
                return;
            L5:
                MediaSessionCompat.ensureClassLoader(r4);
                setCurrentControllerInfo(r0);
                this.this$0.onPrepareFromUri(r3, r4);
                clearCurrentControllerInfo(r0);
            }

            @Override // android.media.session.MediaSession.Callback
            public void onRewind() {
                MediaSessionImplApi21 r0 = getSessionImplIfCallbackIsSet();
                if (r0 != null) goto L5;
                return;
            L5:
                setCurrentControllerInfo(r0);
                this.this$0.onRewind();
                clearCurrentControllerInfo(r0);
            }

            @Override // android.media.session.MediaSession.Callback
            public void onSeekTo(long r3) {
                MediaSessionImplApi21 r0 = getSessionImplIfCallbackIsSet();
                if (r0 != null) goto L5;
                return;
            L5:
                setCurrentControllerInfo(r0);
                this.this$0.onSeekTo(r3);
                clearCurrentControllerInfo(r0);
            }

            @Override // android.media.session.MediaSession.Callback
            public void onSetPlaybackSpeed(float r3) {
                MediaSessionImplApi21 r0 = getSessionImplIfCallbackIsSet();
                if (r0 != null) goto L5;
                return;
            L5:
                setCurrentControllerInfo(r0);
                this.this$0.onSetPlaybackSpeed(r3);
                clearCurrentControllerInfo(r0);
            }

            @Override // android.media.session.MediaSession.Callback
            public void onSetRating(Rating r4) {
                MediaSessionImplApi21 r0 = getSessionImplIfCallbackIsSet();
                if (r0 != null) goto L5;
                return;
            L5:
                setCurrentControllerInfo(r0);
                this.this$0.onSetRating(RatingCompat.fromRating(r4));
                clearCurrentControllerInfo(r0);
            }

            @Override // android.media.session.MediaSession.Callback
            public void onSkipToNext() {
                MediaSessionImplApi21 r0 = getSessionImplIfCallbackIsSet();
                if (r0 != null) goto L5;
                return;
            L5:
                setCurrentControllerInfo(r0);
                this.this$0.onSkipToNext();
                clearCurrentControllerInfo(r0);
            }

            @Override // android.media.session.MediaSession.Callback
            public void onSkipToPrevious() {
                MediaSessionImplApi21 r0 = getSessionImplIfCallbackIsSet();
                if (r0 != null) goto L5;
                return;
            L5:
                setCurrentControllerInfo(r0);
                this.this$0.onSkipToPrevious();
                clearCurrentControllerInfo(r0);
            }

            @Override // android.media.session.MediaSession.Callback
            public void onSkipToQueueItem(long r3) {
                MediaSessionImplApi21 r0 = getSessionImplIfCallbackIsSet();
                if (r0 != null) goto L5;
                return;
            L5:
                setCurrentControllerInfo(r0);
                this.this$0.onSkipToQueueItem(r3);
                clearCurrentControllerInfo(r0);
            }

            @Override // android.media.session.MediaSession.Callback
            public void onStop() {
                MediaSessionImplApi21 r0 = getSessionImplIfCallbackIsSet();
                if (r0 != null) goto L5;
                return;
            L5:
                setCurrentControllerInfo(r0);
                this.this$0.onStop();
                clearCurrentControllerInfo(r0);
            }
        }

        public Callback() {
            this.mLock = new Object();
            this.mCallbackFwk = new MediaSessionCallbackApi21(this);
            this.mSessionImpl = new WeakReference(null);
        }

        void handleMediaPlayPauseIfPendingOnHandler(MediaSessionImpl r12, Handler r13) {
            if (this.mMediaPlayPausePendingOnHandler == true) goto L5;
            return;
        L5:
            boolean r0 = false;
            this.mMediaPlayPausePendingOnHandler = false;
            r13.removeMessages(1);
            PlaybackStateCompat r2 = r12.getPlaybackState();
            if (r2 != null) goto L8;
            long r5 = 0;
        L9:
            if (r2 != null) goto L11;
        L13:
            boolean r7 = false;
        L15:
            if ((516 & r5) == 0) goto L17;
            boolean r8 = true;
        L19:
            if ((514 & r5) == 0) goto L21;
            r0 = true;
        L21:
            if (r7 == false) goto L24;
            if (r0 == false) goto L24;
            onPause();
            return;
        L24:
            if (r7 == true) goto L29;
            if (r8 == false) goto L30;
            onPlay();
            return;
        L30:
            return;
        L29:
            return;
        L17:
            r8 = false;
            goto L19
        L11:
            if (r2.getState() != 3) goto L13;
            r7 = true;
            goto L15
        L8:
            r5 = r2.getActions();
            goto L9
        }

        public void onAddQueueItem(MediaDescriptionCompat r1) {
        }

        public void onAddQueueItem(MediaDescriptionCompat r1, int r2) {
        }

        public void onCommand(String r1, Bundle r2, ResultReceiver r3) {
        }

        public void onCustomAction(String r1, Bundle r2) {
        }

        public void onFastForward() {
        }

        public boolean onMediaButtonEvent(Intent r15) {
            if (Build.VERSION.SDK_INT < 27) goto L5;
            return false;
        L5:
            Object r0 = this.mLock;
            monitor-enter(r0);
            MediaSessionImpl r1 = this.mSessionImpl.get();     // Catch: Throwable -> L38
            CallbackHandler r3 = this.mCallbackHandler;     // Catch: Throwable -> L38
            monitor-exit(r0);     // Catch: Throwable -> L38
            if (r1 == null) goto L37;
            if (r3 == null) goto L37;
            KeyEvent r2 = (KeyEvent) r15.getParcelableExtra("android.intent.extra.KEY_EVENT");
            if (r2 != null) goto L15;
        L36:
            return false;
        L15:
            if (r2.getAction() != 0) goto L36;
            MediaSessionManager.RemoteUserInfo r4 = r1.getCurrentControllerInfo();
            switch(r2.getKeyCode()) {
                case 79: goto L22;
                case 85: goto L22;
                default: goto L19;
            };
        L19:
            handleMediaPlayPauseIfPendingOnHandler(r1, r3);
            return false;
        L22:
            if (r2.getRepeatCount() == 0) goto L24;
            handleMediaPlayPauseIfPendingOnHandler(r1, r3);
        L35:
            return true;
        L24:
            if (this.mMediaPlayPausePendingOnHandler == false) goto L33;
            r3.removeMessages(1);
            this.mMediaPlayPausePendingOnHandler = false;
            PlaybackStateCompat r5 = r1.getPlaybackState();
            if (r5 != null) goto L28;
            long r10 = 0;
        L30:
            if ((32 & r10) == 0) goto L35;
            onSkipToNext();
            goto L35
        L28:
            r10 = r5.getActions();
            goto L30
        L33:
            this.mMediaPlayPausePendingOnHandler = true;
            r3.sendMessageDelayed(r3.obtainMessage(1, r4), ViewConfiguration.getDoubleTapTimeout());
        L37:
            return false;
        L38:
            th = move-exception;
            throw th;
        }

        public void onPause() {
        }

        public void onPlay() {
        }

        public void onPlayFromMediaId(String r1, Bundle r2) {
        }

        public void onPlayFromSearch(String r1, Bundle r2) {
        }

        public void onPlayFromUri(Uri r1, Bundle r2) {
        }

        public void onPrepare() {
        }

        public void onPrepareFromMediaId(String r1, Bundle r2) {
        }

        public void onPrepareFromSearch(String r1, Bundle r2) {
        }

        public void onPrepareFromUri(Uri r1, Bundle r2) {
        }

        public void onRemoveQueueItem(MediaDescriptionCompat r1) {
        }

        @Deprecated
        public void onRemoveQueueItemAt(int r1) {
        }

        public void onRewind() {
        }

        public void onSeekTo(long r1) {
        }

        public void onSetCaptioningEnabled(boolean r1) {
        }

        public void onSetPlaybackSpeed(float r1) {
        }

        public void onSetRating(RatingCompat r1) {
        }

        public void onSetRating(RatingCompat r1, Bundle r2) {
        }

        public void onSetRepeatMode(int r1) {
        }

        public void onSetShuffleMode(int r1) {
        }

        public void onSkipToNext() {
        }

        public void onSkipToPrevious() {
        }

        public void onSkipToQueueItem(long r1) {
        }

        public void onStop() {
        }

        void setSessionImpl(MediaSessionImpl r4, Handler r5) {
            Object r0 = this.mLock;
            monitor-enter(r0);
            this.mSessionImpl = new WeakReference(r4);     // Catch: Throwable -> L14
            CallbackHandler r2 = null;
            if (this.mCallbackHandler == null) goto L7;
            this.mCallbackHandler.removeCallbacksAndMessages(null);     // Catch: Throwable -> L14
        L7:
            if (r4 == null) goto L11;
            if (r5 == null) goto L11;
            r2 = new CallbackHandler(this, r5.getLooper());     // Catch: Throwable -> L14
        L11:
            this.mCallbackHandler = r2;     // Catch: Throwable -> L14
            monitor-exit(r0);     // Catch: Throwable -> L14
            return;
        L14:
            th = move-exception;
            throw th;
        }
    }

    interface MediaSessionImpl {
        Callback getCallback();

        String getCallingPackage();

        MediaSessionManager.RemoteUserInfo getCurrentControllerInfo();

        Object getMediaSession();

        PlaybackStateCompat getPlaybackState();

        Object getRemoteControlClient();

        Token getSessionToken();

        boolean isActive();

        void release();

        void sendSessionEvent(String r1, Bundle r2);

        void setActive(boolean r1);

        void setCallback(Callback r1, Handler r2);

        void setCaptioningEnabled(boolean r1);

        void setCurrentControllerInfo(MediaSessionManager.RemoteUserInfo r1);

        void setExtras(Bundle r1);

        void setFlags(int r1);

        void setMediaButtonReceiver(PendingIntent r1);

        void setMetadata(MediaMetadataCompat r1);

        void setPlaybackState(PlaybackStateCompat r1);

        void setPlaybackToLocal(int r1);

        void setPlaybackToRemote(VolumeProviderCompat r1);

        void setQueue(List<QueueItem> r1);

        void setQueueTitle(CharSequence r1);

        void setRatingType(int r1);

        void setRegistrationCallback(RegistrationCallback r1, Handler r2);

        void setRepeatMode(int r1);

        void setSessionActivity(PendingIntent r1);

        void setShuffleMode(int r1);
    }

    static class MediaSessionImplApi18 extends MediaSessionImplBase {
        private static boolean sIsMbrPendingIntentSupported;

        static {
            sIsMbrPendingIntentSupported = true;
        }

        MediaSessionImplApi18(Context r1, String r2, ComponentName r3, PendingIntent r4, VersionedParcelable r5, Bundle r6) {
            super(r1, r2, r3, r4, r5, r6);
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase
        int getRccTransportControlFlagsFromActions(long r6) {
            int r0 = super.getRccTransportControlFlagsFromActions(r6);
            if ((256 & r6) != 0) goto L5;
            return r0;
        L5:
            return r0 | 256;
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase
        void registerMediaButtonEventReceiver(PendingIntent r4, ComponentName r5) {
            if (sIsMbrPendingIntentSupported == false) goto L9;
            this.mAudioManager.registerMediaButtonEventReceiver(r4);     // Catch: NullPointerException -> L6
        L7:
            Log.w(MediaSessionCompat.TAG, "Unable to register media button event receiver with PendingIntent, falling back to ComponentName.");
            sIsMbrPendingIntentSupported = false;
        L9:
            if (sIsMbrPendingIntentSupported == true) goto L14;
            super.registerMediaButtonEventReceiver(r4, r5);
            return;
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase, android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public void setCallback(Callback r3, Handler r4) {
            super.setCallback(r3, r4);
            if (r3 != null) goto L5;
            this.mRcc.setPlaybackPositionUpdateListener(null);
            return;
        L5:
            RemoteControlClient.OnPlaybackPositionUpdateListener r0 = new C00201(this);
            this.mRcc.setPlaybackPositionUpdateListener(r0);
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase
        void setRccState(PlaybackStateCompat r12) {
            long r0 = r12.getPosition();
            float r2 = r12.getPlaybackSpeed();
            long r3 = r12.getLastPositionUpdateTime();
            long r5 = SystemClock.elapsedRealtime();
            if (r12.getState() == 3) goto L5;
        L14:
            this.mRcc.setPlaybackState(getRccStateFromState(r12.getState()), r0, r2);
            return;
        L5:
            if (r0 <= 0) goto L14;
            long r9 = 0;
            if (r3 <= 0) goto L13;
            r9 = r5 - r3;
            if (r2 <= 0.0f) goto L13;
            if (r2 == 1.0f) goto L13;
            r9 = (long) (r9 * r2);
        L13:
            r0 = r0 + r9;
            goto L14
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase
        void unregisterMediaButtonEventReceiver(PendingIntent r2, ComponentName r3) {
            if (sIsMbrPendingIntentSupported == false) goto L5;
            this.mAudioManager.unregisterMediaButtonEventReceiver(r2);
            return;
        L5:
            super.unregisterMediaButtonEventReceiver(r2, r3);
        }
    }

    static class MediaSessionImplApi19 extends MediaSessionImplApi18 {
        MediaSessionImplApi19(Context r1, String r2, ComponentName r3, PendingIntent r4, VersionedParcelable r5, Bundle r6) {
            super(r1, r2, r3, r4, r5, r6);
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase
        RemoteControlClient.MetadataEditor buildRccMetadata(Bundle r9) {
            RemoteControlClient.MetadataEditor r0 = super.buildRccMetadata(r9);
            if (this.mState != null) goto L5;
            long r4 = 0;
        L7:
            if ((128 & r4) == 0) goto L9;
            r0.addEditableKey(268435457);
        L9:
            if (r9 != null) goto L12;
            return r0;
        L12:
            if (r9.containsKey(MediaMetadataCompat.METADATA_KEY_YEAR) == false) goto L15;
            r0.putLong(8, r9.getLong(MediaMetadataCompat.METADATA_KEY_YEAR));
        L15:
            if (r9.containsKey(MediaMetadataCompat.METADATA_KEY_RATING) == false) goto L18;
            r0.putObject(101, r9.getParcelable(MediaMetadataCompat.METADATA_KEY_RATING));
        L18:
            if (r9.containsKey(MediaMetadataCompat.METADATA_KEY_USER_RATING) == false) goto L20;
            r0.putObject(268435457, r9.getParcelable(MediaMetadataCompat.METADATA_KEY_USER_RATING));
        L20:
            return r0;
        L5:
            r4 = this.mState.getActions();
            goto L7
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImplApi18, android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase
        int getRccTransportControlFlagsFromActions(long r6) {
            int r0 = super.getRccTransportControlFlagsFromActions(r6);
            if ((128 & r6) != 0) goto L5;
            return r0;
        L5:
            return r0 | 512;
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImplApi18, android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase, android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public void setCallback(Callback r3, Handler r4) {
            super.setCallback(r3, r4);
            if (r3 != null) goto L5;
            this.mRcc.setMetadataUpdateListener(null);
            return;
        L5:
            RemoteControlClient.OnMetadataUpdateListener r0 = new C00211(this);
            this.mRcc.setMetadataUpdateListener(r0);
        }
    }

    static class MediaSessionImplApi21 implements MediaSessionImpl {
        Callback mCallback;
        boolean mCaptioningEnabled;
        boolean mDestroyed;
        final RemoteCallbackList<IMediaControllerCallback> mExtraControllerCallbacks;
        final ExtraSession mExtraSession;
        final Object mLock;
        MediaMetadataCompat mMetadata;
        PlaybackStateCompat mPlaybackState;
        List<QueueItem> mQueue;
        int mRatingType;
        RegistrationCallbackHandler mRegistrationCallbackHandler;
        MediaSessionManager.RemoteUserInfo mRemoteUserInfo;
        int mRepeatMode;
        final MediaSession mSessionFwk;
        Bundle mSessionInfo;
        int mShuffleMode;
        final Token mToken;

        private static class ExtraSession extends IMediaSession.Stub {
            private final AtomicReference<MediaSessionImplApi21> mMediaSessionImplRef;

            ExtraSession(MediaSessionImplApi21 r2) {
                this.mMediaSessionImplRef = new AtomicReference(r2);
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void addQueueItem(MediaDescriptionCompat r2) {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void addQueueItemAt(MediaDescriptionCompat r2, int r3) {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void adjustVolume(int r2, int r3, String r4) {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void fastForward() {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.IMediaSession
            public Bundle getExtras() {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.IMediaSession
            public long getFlags() {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.IMediaSession
            public PendingIntent getLaunchPendingIntent() {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.IMediaSession
            public MediaMetadataCompat getMetadata() {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.IMediaSession
            public String getPackageName() {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.IMediaSession
            public PlaybackStateCompat getPlaybackState() {
                MediaSessionImplApi21 r0 = this.mMediaSessionImplRef.get();
                if (r0 != null) goto L5;
                return null;
            L5:
                return MediaSessionCompat.getStateWithUpdatedPosition(r0.mPlaybackState, r0.mMetadata);
            }

            @Override // android.support.v4.media.session.IMediaSession
            public List<QueueItem> getQueue() {
                return null;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public CharSequence getQueueTitle() {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.IMediaSession
            public int getRatingType() {
                MediaSessionImplApi21 r0 = this.mMediaSessionImplRef.get();
                if (r0 != null) goto L7;
                return 0;
            L7:
                return r0.mRatingType;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public int getRepeatMode() {
                MediaSessionImplApi21 r0 = this.mMediaSessionImplRef.get();
                if (r0 != null) goto L7;
                return -1;
            L7:
                return r0.mRepeatMode;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public Bundle getSessionInfo() {
                MediaSessionImplApi21 r0 = this.mMediaSessionImplRef.get();
                if (r0.mSessionInfo != null) goto L6;
                return null;
            L6:
                return new Bundle(r0.mSessionInfo);
            }

            @Override // android.support.v4.media.session.IMediaSession
            public int getShuffleMode() {
                MediaSessionImplApi21 r0 = this.mMediaSessionImplRef.get();
                if (r0 != null) goto L7;
                return -1;
            L7:
                return r0.mShuffleMode;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public String getTag() {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.IMediaSession
            public ParcelableVolumeInfo getVolumeAttributes() {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.IMediaSession
            public boolean isCaptioningEnabled() {
                MediaSessionImplApi21 r0 = this.mMediaSessionImplRef.get();
                if (r0 != null) goto L5;
            L7:
                return false;
            L5:
                if (r0.mCaptioningEnabled == false) goto L7;
                return true;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public boolean isShuffleModeEnabledRemoved() {
                return false;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public boolean isTransportControlEnabled() {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void next() {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void pause() {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void play() throws RemoteException {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void playFromMediaId(String r2, Bundle r3) {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void playFromSearch(String r2, Bundle r3) {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void playFromUri(Uri r2, Bundle r3) {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void prepare() throws RemoteException {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void prepareFromMediaId(String r2, Bundle r3) {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void prepareFromSearch(String r2, Bundle r3) {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void prepareFromUri(Uri r2, Bundle r3) {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void previous() {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void rate(RatingCompat r2) {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void rateWithExtras(RatingCompat r2, Bundle r3) {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void registerCallbackListener(IMediaControllerCallback r7) {
                MediaSessionImplApi21 r0 = this.mMediaSessionImplRef.get();
                if (r0 != null) goto L5;
                return;
            L5:
                int r1 = Binder.getCallingPid();
                int r2 = Binder.getCallingUid();
                MediaSessionManager.RemoteUserInfo r3 = new MediaSessionManager.RemoteUserInfo(MediaSessionManager.RemoteUserInfo.LEGACY_CONTROLLER, r1, r2);
                r0.mExtraControllerCallbacks.register(r7, r3);
                Object r4 = r0.mLock;
                monitor-enter(r4);
            L12:
                th = move-exception;
                throw th;
            L8:
                if (r0.mRegistrationCallbackHandler == null) goto L10;
                r0.mRegistrationCallbackHandler.postCallbackRegistered(r1, r2);     // Catch: Throwable -> L12
            L10:
                monitor-exit(r4);     // Catch: Throwable -> L12
            }

            public void release() {
                this.mMediaSessionImplRef.set(null);
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void removeQueueItem(MediaDescriptionCompat r2) {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void removeQueueItemAt(int r2) {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void rewind() {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void seekTo(long r2) {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void sendCommand(String r2, Bundle r3, ResultReceiverWrapper r4) {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void sendCustomAction(String r2, Bundle r3) throws RemoteException {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.IMediaSession
            public boolean sendMediaButton(KeyEvent r2) {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void setCaptioningEnabled(boolean r2) {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void setPlaybackSpeed(float r2) {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void setRepeatMode(int r2) {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void setShuffleMode(int r2) throws RemoteException {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void setShuffleModeEnabledRemoved(boolean r1) {
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void setVolumeTo(int r2, int r3, String r4) {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void skipToQueueItem(long r2) {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void stop() {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void unregisterCallbackListener(IMediaControllerCallback r6) {
                MediaSessionImplApi21 r0 = this.mMediaSessionImplRef.get();
                if (r0 != null) goto L5;
                return;
            L5:
                r0.mExtraControllerCallbacks.unregister(r6);
                int r1 = Binder.getCallingPid();
                int r2 = Binder.getCallingUid();
                Object r3 = r0.mLock;
                monitor-enter(r3);
            L12:
                th = move-exception;
                throw th;
            L8:
                if (r0.mRegistrationCallbackHandler == null) goto L10;
                r0.mRegistrationCallbackHandler.postCallbackUnregistered(r1, r2);     // Catch: Throwable -> L12
            L10:
                monitor-exit(r3);     // Catch: Throwable -> L12
            }
        }

        MediaSessionImplApi21(Context r4, String r5, VersionedParcelable r6, Bundle r7) {
            this.mLock = new Object();
            this.mDestroyed = false;
            this.mExtraControllerCallbacks = new RemoteCallbackList();
            this.mSessionFwk = createFwkMediaSession(r4, r5, r7);
            this.mExtraSession = new ExtraSession(this);
            this.mToken = new Token(this.mSessionFwk.getSessionToken(), this.mExtraSession, r6);
            this.mSessionInfo = r7;
            setFlags(3);
        }

        MediaSessionImplApi21(Object r4) {
            this.mLock = new Object();
            this.mDestroyed = false;
            this.mExtraControllerCallbacks = new RemoteCallbackList();
            if ((r4 instanceof MediaSession) == false) goto L7;
            this.mSessionFwk = (MediaSession) r4;
            this.mExtraSession = new ExtraSession(this);
            this.mToken = new Token(this.mSessionFwk.getSessionToken(), this.mExtraSession);
            this.mSessionInfo = null;
            setFlags(3);
            return;
        L7:
            throw new IllegalArgumentException("mediaSession is not a valid MediaSession object");
        }

        public MediaSession createFwkMediaSession(Context r2, String r3, Bundle r4) {
            return new MediaSession(r2, r3);
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public Callback getCallback() {
            Object r0 = this.mLock;
            monitor-enter(r0);
            Callback r1 = this.mCallback;     // Catch: Throwable -> L7
            monitor-exit(r0);     // Catch: Throwable -> L7
            return r1;
        L7:
            th = move-exception;
            throw th;
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public String getCallingPackage() {
            if (Build.VERSION.SDK_INT >= 24) goto L10;
            return null;
        L10:
            return (String) this.mSessionFwk.getClass().getMethod("getCallingPackage", new Class[0]).invoke(this.mSessionFwk, new Object[0]);
        L7:
            e = move-exception;
            Log.e(MediaSessionCompat.TAG, "Cannot execute MediaSession.getCallingPackage()", e);
            return null;
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public MediaSessionManager.RemoteUserInfo getCurrentControllerInfo() {
            Object r0 = this.mLock;
            monitor-enter(r0);
            MediaSessionManager.RemoteUserInfo r1 = this.mRemoteUserInfo;     // Catch: Throwable -> L7
            monitor-exit(r0);     // Catch: Throwable -> L7
            return r1;
        L7:
            th = move-exception;
            throw th;
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public Object getMediaSession() {
            return this.mSessionFwk;
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public PlaybackStateCompat getPlaybackState() {
            return this.mPlaybackState;
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public Object getRemoteControlClient() {
            return null;
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public Token getSessionToken() {
            return this.mToken;
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public boolean isActive() {
            return this.mSessionFwk.isActive();
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public void release() {
            this.mDestroyed = true;
            this.mExtraControllerCallbacks.kill();
            if (Build.VERSION.SDK_INT == 27) goto L12;
        L10:
            this.mSessionFwk.setCallback(null);
            this.mExtraSession.release();
            this.mSessionFwk.release();
            return;
        L12:
            Field r1 = this.mSessionFwk.getClass().getDeclaredField("mCallback");     // Catch: Exception -> L8
            r1.setAccessible(true);     // Catch: Exception -> L8
            Handler r0 = (Handler) r1.get(this.mSessionFwk);     // Catch: Exception -> L8
            if (r0 == null) goto L10;
            r0.removeCallbacksAndMessages(null);     // Catch: Exception -> L8
        L8:
            e = move-exception;
            Log.w(MediaSessionCompat.TAG, "Exception happened while accessing MediaSession.mCallback.", e);
            goto L10
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public void sendSessionEvent(String r2, Bundle r3) {
            this.mSessionFwk.sendSessionEvent(r2, r3);
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public void setActive(boolean r2) {
            this.mSessionFwk.setActive(r2);
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public void setCallback(Callback r4, Handler r5) {
            Object r0 = this.mLock;
            monitor-enter(r0);
            this.mCallback = r4;     // Catch: Throwable -> L13
            MediaSession r1 = this.mSessionFwk;     // Catch: Throwable -> L13
            if (r4 != null) goto L7;
            MediaSession.Callback r2 = null;
        L8:
            r1.setCallback(r2, r5);     // Catch: Throwable -> L13
            if (r4 == null) goto L11;
            r4.setSessionImpl(this, r5);     // Catch: Throwable -> L13
        L11:
            monitor-exit(r0);     // Catch: Throwable -> L13
            return;
        L7:
            r2 = r4.mCallbackFwk;     // Catch: Throwable -> L13
        L13:
            th = move-exception;
            throw th;
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public void setCaptioningEnabled(boolean r6) {
            if (this.mCaptioningEnabled == r6) goto L19;
            this.mCaptioningEnabled = r6;
            Object r0 = this.mLock;
            monitor-enter(r0);
            int r2 = this.mExtraControllerCallbacks.beginBroadcast() - 1;     // Catch: Throwable -> L16
        L7:
            if (r2 < 0) goto L13;
            ((IMediaControllerCallback) this.mExtraControllerCallbacks.getBroadcastItem(r2)).onCaptioningEnabledChanged(r6);     // Catch: RemoteException -> L11 Throwable -> L16
        L12:
            r2 = r2 - 1;
            goto L7
        L13:
            this.mExtraControllerCallbacks.finishBroadcast();     // Catch: Throwable -> L16
            monitor-exit(r0);     // Catch: Throwable -> L16
            return;
        L16:
            th = move-exception;
            throw th;
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public void setCurrentControllerInfo(MediaSessionManager.RemoteUserInfo r3) {
            Object r0 = this.mLock;
            monitor-enter(r0);
            this.mRemoteUserInfo = r3;     // Catch: Throwable -> L7
            monitor-exit(r0);     // Catch: Throwable -> L7
            return;
        L7:
            th = move-exception;
            throw th;
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public void setExtras(Bundle r2) {
            this.mSessionFwk.setExtras(r2);
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public void setFlags(int r3) {
            this.mSessionFwk.setFlags((r3 | 1) | 2);
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public void setMediaButtonReceiver(PendingIntent r2) {
            this.mSessionFwk.setMediaButtonReceiver(r2);
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public void setMetadata(MediaMetadataCompat r3) {
            this.mMetadata = r3;
            MediaSession r0 = this.mSessionFwk;
            if (r3 != null) goto L5;
            MediaMetadata r1 = null;
        L6:
            r0.setMetadata(r1);
            return;
        L5:
            r1 = (MediaMetadata) r3.getMediaMetadata();
            goto L6
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public void setPlaybackState(PlaybackStateCompat r6) {
            this.mPlaybackState = r6;
            Object r0 = this.mLock;
            monitor-enter(r0);
            int r2 = this.mExtraControllerCallbacks.beginBroadcast() - 1;     // Catch: Throwable -> L19
        L5:
            if (r2 < 0) goto L11;
            ((IMediaControllerCallback) this.mExtraControllerCallbacks.getBroadcastItem(r2)).onPlaybackStateChanged(r6);     // Catch: RemoteException -> L9 Throwable -> L19
        L10:
            r2 = r2 - 1;
            goto L5
        L11:
            this.mExtraControllerCallbacks.finishBroadcast();     // Catch: Throwable -> L19
            monitor-exit(r0);     // Catch: Throwable -> L19
            MediaSession r1 = this.mSessionFwk;
            if (r6 != null) goto L16;
            PlaybackState r3 = null;
        L17:
            r1.setPlaybackState(r3);
            return;
        L16:
            r3 = (PlaybackState) r6.getPlaybackState();
        L19:
            th = move-exception;
            throw th;
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public void setPlaybackToLocal(int r4) {
            AudioAttributes.Builder r0 = new AudioAttributes.Builder();
            r0.setLegacyStreamType(r4);
            this.mSessionFwk.setPlaybackToLocal(r0.build());
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public void setPlaybackToRemote(VolumeProviderCompat r3) {
            this.mSessionFwk.setPlaybackToRemote((VolumeProvider) r3.getVolumeProvider());
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public void setQueue(List<QueueItem> r5) {
            this.mQueue = r5;
            if (r5 != null) goto L6;
            this.mSessionFwk.setQueue(null);
            return;
        L6:
            ArrayList r0 = new ArrayList(r5.size());
            Iterator<QueueItem> r1 = r5.iterator();
        L8:
            if (r1.hasNext() == false) goto L10;
            r0.add((MediaSession.QueueItem) r1.next().getQueueItem());
            goto L8
        L10:
            this.mSessionFwk.setQueue(r0);
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public void setQueueTitle(CharSequence r2) {
            this.mSessionFwk.setQueueTitle(r2);
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public void setRatingType(int r1) {
            this.mRatingType = r1;
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public void setRegistrationCallback(RegistrationCallback r4, Handler r5) {
            Object r0 = this.mLock;
            monitor-enter(r0);
        L12:
            th = move-exception;
            throw th;
        L5:
            if (this.mRegistrationCallbackHandler == null) goto L7;
            this.mRegistrationCallbackHandler.removeCallbacksAndMessages(null);     // Catch: Throwable -> L12
        L7:
            if (r4 == null) goto L9;
            this.mRegistrationCallbackHandler = new RegistrationCallbackHandler(r5.getLooper(), r4);     // Catch: Throwable -> L12
        L10:
            monitor-exit(r0);     // Catch: Throwable -> L12
            return;
        L9:
            this.mRegistrationCallbackHandler = null;     // Catch: Throwable -> L12
            goto L10
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public void setRepeatMode(int r6) {
            if (this.mRepeatMode == r6) goto L19;
            this.mRepeatMode = r6;
            Object r0 = this.mLock;
            monitor-enter(r0);
            int r2 = this.mExtraControllerCallbacks.beginBroadcast() - 1;     // Catch: Throwable -> L16
        L7:
            if (r2 < 0) goto L13;
            ((IMediaControllerCallback) this.mExtraControllerCallbacks.getBroadcastItem(r2)).onRepeatModeChanged(r6);     // Catch: RemoteException -> L11 Throwable -> L16
        L12:
            r2 = r2 - 1;
            goto L7
        L13:
            this.mExtraControllerCallbacks.finishBroadcast();     // Catch: Throwable -> L16
            monitor-exit(r0);     // Catch: Throwable -> L16
            return;
        L16:
            th = move-exception;
            throw th;
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public void setSessionActivity(PendingIntent r2) {
            this.mSessionFwk.setSessionActivity(r2);
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public void setShuffleMode(int r6) {
            if (this.mShuffleMode == r6) goto L19;
            this.mShuffleMode = r6;
            Object r0 = this.mLock;
            monitor-enter(r0);
            int r2 = this.mExtraControllerCallbacks.beginBroadcast() - 1;     // Catch: Throwable -> L16
        L7:
            if (r2 < 0) goto L13;
            ((IMediaControllerCallback) this.mExtraControllerCallbacks.getBroadcastItem(r2)).onShuffleModeChanged(r6);     // Catch: RemoteException -> L11 Throwable -> L16
        L12:
            r2 = r2 - 1;
            goto L7
        L13:
            this.mExtraControllerCallbacks.finishBroadcast();     // Catch: Throwable -> L16
            monitor-exit(r0);     // Catch: Throwable -> L16
            return;
        L16:
            th = move-exception;
            throw th;
        }
    }

    static class MediaSessionImplApi22 extends MediaSessionImplApi21 {
        MediaSessionImplApi22(Context r1, String r2, VersionedParcelable r3, Bundle r4) {
            super(r1, r2, r3, r4);
        }

        MediaSessionImplApi22(Object r1) {
            super(r1);
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImplApi21, android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public void setRatingType(int r2) {
            this.mSessionFwk.setRatingType(r2);
        }
    }

    static class MediaSessionImplApi28 extends MediaSessionImplApi22 {
        MediaSessionImplApi28(Context r1, String r2, VersionedParcelable r3, Bundle r4) {
            super(r1, r2, r3, r4);
        }

        MediaSessionImplApi28(Object r1) {
            super(r1);
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImplApi21, android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public final MediaSessionManager.RemoteUserInfo getCurrentControllerInfo() {
            return new MediaSessionManager.RemoteUserInfo(this.mSessionFwk.getCurrentControllerInfo());
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImplApi21, android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public void setCurrentControllerInfo(MediaSessionManager.RemoteUserInfo r1) {
        }
    }

    static class MediaSessionImplApi29 extends MediaSessionImplApi28 {
        MediaSessionImplApi29(Context r1, String r2, VersionedParcelable r3, Bundle r4) {
            super(r1, r2, r3, r4);
        }

        MediaSessionImplApi29(Object r2) {
            super(r2);
            this.mSessionInfo = ((MediaSession) r2).getController().getSessionInfo();
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImplApi21
        public MediaSession createFwkMediaSession(Context r2, String r3, Bundle r4) {
            return new MediaSession(r2, r3, r4);
        }
    }

    static class MediaSessionImplBase implements MediaSessionImpl {
        static final int RCC_PLAYSTATE_NONE = 0;
        final AudioManager mAudioManager;
        volatile Callback mCallback;
        boolean mCaptioningEnabled;
        private final Context mContext;
        final RemoteCallbackList<IMediaControllerCallback> mControllerCallbacks;
        boolean mDestroyed;
        Bundle mExtras;
        int mFlags;
        private MessageHandler mHandler;
        boolean mIsActive;
        int mLocalStream;
        final Object mLock;
        private final ComponentName mMediaButtonReceiverComponentName;
        private final PendingIntent mMediaButtonReceiverIntent;
        MediaMetadataCompat mMetadata;
        List<QueueItem> mQueue;
        CharSequence mQueueTitle;
        int mRatingType;
        final RemoteControlClient mRcc;
        RegistrationCallbackHandler mRegistrationCallbackHandler;
        private MediaSessionManager.RemoteUserInfo mRemoteUserInfo;
        int mRepeatMode;
        PendingIntent mSessionActivity;
        final Bundle mSessionInfo;
        int mShuffleMode;
        PlaybackStateCompat mState;
        private final MediaSessionStub mStub;
        private final Token mToken;
        private VolumeProviderCompat.Callback mVolumeCallback;
        VolumeProviderCompat mVolumeProvider;
        int mVolumeType;

        private static final class Command {
            public final String command;
            public final Bundle extras;
            public final ResultReceiver stub;

            public Command(String r1, Bundle r2, ResultReceiver r3) {
                this.command = r1;
                this.extras = r2;
                this.stub = r3;
            }
        }

        static class MediaSessionStub extends IMediaSession.Stub {
            private final AtomicReference<MediaSessionImplBase> mMediaSessionImplRef;
            private final String mPackageName;
            private final String mTag;

            MediaSessionStub(MediaSessionImplBase r2, String r3, String r4) {
                this.mMediaSessionImplRef = new AtomicReference(r2);
                this.mPackageName = r3;
                this.mTag = r4;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void addQueueItem(MediaDescriptionCompat r2) {
                postToHandler(25, r2);
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void addQueueItemAt(MediaDescriptionCompat r3, int r4) {
                postToHandler(26, r3, r4, null);
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void adjustVolume(int r2, int r3, String r4) {
                MediaSessionImplBase r0 = this.mMediaSessionImplRef.get();
                if (r0 == null) goto L6;
                r0.adjustVolume(r2, r3);
                return;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void fastForward() {
                postToHandler(16);
            }

            @Override // android.support.v4.media.session.IMediaSession
            public Bundle getExtras() {
                MediaSessionImplBase r0 = this.mMediaSessionImplRef.get();
                if (r0 != null) goto L6;
                return null;
            L6:
                Object r1 = r0.mLock;
                monitor-enter(r1);
                Bundle r2 = r0.mExtras;     // Catch: Throwable -> L11
                monitor-exit(r1);     // Catch: Throwable -> L11
                return r2;
            L11:
                th = move-exception;
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public long getFlags() {
                MediaSessionImplBase r0 = this.mMediaSessionImplRef.get();
                if (r0 != null) goto L6;
                return 0;
            L6:
                Object r1 = r0.mLock;
                monitor-enter(r1);
                long r2 = r0.mFlags;     // Catch: Throwable -> L11
                monitor-exit(r1);     // Catch: Throwable -> L11
                return r2;
            L11:
                th = move-exception;
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public PendingIntent getLaunchPendingIntent() {
                MediaSessionImplBase r0 = this.mMediaSessionImplRef.get();
                if (r0 != null) goto L6;
                return null;
            L6:
                Object r1 = r0.mLock;
                monitor-enter(r1);
                PendingIntent r2 = r0.mSessionActivity;     // Catch: Throwable -> L11
                monitor-exit(r1);     // Catch: Throwable -> L11
                return r2;
            L11:
                th = move-exception;
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public MediaMetadataCompat getMetadata() {
                MediaSessionImplBase r0 = this.mMediaSessionImplRef.get();
                if (r0 != null) goto L7;
                return null;
            L7:
                return r0.mMetadata;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public String getPackageName() {
                return this.mPackageName;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public PlaybackStateCompat getPlaybackState() {
                MediaSessionImplBase r0 = this.mMediaSessionImplRef.get();
                if (r0 != null) goto L6;
                return null;
            L6:
                Object r1 = r0.mLock;
                monitor-enter(r1);
                PlaybackStateCompat r2 = r0.mState;     // Catch: Throwable -> L12
                MediaMetadataCompat r3 = r0.mMetadata;     // Catch: Throwable -> L12
                monitor-exit(r1);     // Catch: Throwable -> L12
                return MediaSessionCompat.getStateWithUpdatedPosition(r2, r3);
            L12:
                th = move-exception;
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public List<QueueItem> getQueue() {
                MediaSessionImplBase r0 = this.mMediaSessionImplRef.get();
                if (r0 != null) goto L6;
                return null;
            L6:
                Object r1 = r0.mLock;
                monitor-enter(r1);
                List<QueueItem> r2 = r0.mQueue;     // Catch: Throwable -> L11
                monitor-exit(r1);     // Catch: Throwable -> L11
                return r2;
            L11:
                th = move-exception;
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public CharSequence getQueueTitle() {
                MediaSessionImplBase r0 = this.mMediaSessionImplRef.get();
                if (r0 != null) goto L7;
                return null;
            L7:
                return r0.mQueueTitle;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public int getRatingType() {
                MediaSessionImplBase r0 = this.mMediaSessionImplRef.get();
                if (r0 != null) goto L7;
                return 0;
            L7:
                return r0.mRatingType;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public int getRepeatMode() {
                MediaSessionImplBase r0 = this.mMediaSessionImplRef.get();
                if (r0 != null) goto L7;
                return -1;
            L7:
                return r0.mRepeatMode;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public Bundle getSessionInfo() {
                MediaSessionImplBase r0 = this.mMediaSessionImplRef.get();
                if (r0 != null) goto L5;
            L7:
                return null;
            L5:
                if (r0.mSessionInfo == null) goto L7;
                return new Bundle(r0.mSessionInfo);
            }

            @Override // android.support.v4.media.session.IMediaSession
            public int getShuffleMode() {
                MediaSessionImplBase r0 = this.mMediaSessionImplRef.get();
                if (r0 != null) goto L7;
                return -1;
            L7:
                return r0.mShuffleMode;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public String getTag() {
                return this.mTag;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public ParcelableVolumeInfo getVolumeAttributes() {
                MediaSessionImplBase r1 = this.mMediaSessionImplRef.get();
                if (r1 != null) goto L6;
                return null;
            L6:
                Object r2 = r1.mLock;
                monitor-enter(r2);
                int r0 = r1.mVolumeType;     // Catch: Throwable -> L15
                int r5 = r1.mLocalStream;     // Catch: Throwable -> L15
                VolumeProviderCompat r3 = r1.mVolumeProvider;     // Catch: Throwable -> L15
                if (r0 != 2) goto L11;
                int r4 = r3.getVolumeControl();     // Catch: Throwable -> L15
                int r6 = r3.getMaxVolume();     // Catch: Throwable -> L15
                int r8 = r3.getCurrentVolume();     // Catch: Throwable -> L15
                int r7 = r6;
                int r9 = r4;
            L12:
                ParcelableVolumeInfo r10 = new ParcelableVolumeInfo(r0, r5, r9, r7, r8);     // Catch: Throwable -> L15
                monitor-exit(r2);     // Catch: Throwable -> L15
                return r10;
            L11:
                int r11 = r1.mAudioManager.getStreamMaxVolume(r5);     // Catch: Throwable -> L15
                r8 = r1.mAudioManager.getStreamVolume(r5);     // Catch: Throwable -> L15
                r7 = r11;
                r9 = 2;
            L15:
                th = move-exception;
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public boolean isCaptioningEnabled() {
                MediaSessionImplBase r0 = this.mMediaSessionImplRef.get();
                if (r0 != null) goto L5;
            L7:
                return false;
            L5:
                if (r0.mCaptioningEnabled == false) goto L7;
                return true;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public boolean isShuffleModeEnabledRemoved() {
                return false;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public boolean isTransportControlEnabled() {
                return true;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void next() {
                postToHandler(14);
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void pause() {
                postToHandler(12);
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void play() throws RemoteException {
                postToHandler(7);
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void playFromMediaId(String r2, Bundle r3) {
                postToHandler(8, r2, r3);
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void playFromSearch(String r2, Bundle r3) {
                postToHandler(9, r2, r3);
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void playFromUri(Uri r2, Bundle r3) {
                postToHandler(10, r2, r3);
            }

            void postToHandler(int r3) {
                postToHandler(r3, null, 0, null);
            }

            void postToHandler(int r2, int r3) {
                postToHandler(r2, null, r3, null);
            }

            void postToHandler(int r3, Object r4) {
                postToHandler(r3, r4, 0, null);
            }

            void postToHandler(int r8, Object r9, int r10, Bundle r11) {
                MediaSessionImplBase r1 = this.mMediaSessionImplRef.get();
                if (r1 == null) goto L6;
                r1.postToHandler(r8, r10, 0, r9, r11);
                return;
            }

            void postToHandler(int r2, Object r3, Bundle r4) {
                postToHandler(r2, r3, 0, r4);
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void prepare() throws RemoteException {
                postToHandler(3);
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void prepareFromMediaId(String r2, Bundle r3) {
                postToHandler(4, r2, r3);
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void prepareFromSearch(String r2, Bundle r3) {
                postToHandler(5, r2, r3);
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void prepareFromUri(Uri r2, Bundle r3) {
                postToHandler(6, r2, r3);
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void previous() {
                postToHandler(15);
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void rate(RatingCompat r2) {
                postToHandler(19, r2);
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void rateWithExtras(RatingCompat r2, Bundle r3) {
                postToHandler(31, r2, r3);
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void registerCallbackListener(IMediaControllerCallback r7) {
                MediaSessionImplBase r0 = this.mMediaSessionImplRef.get();
                if (r0 == null) goto L18;
                int r1 = Binder.getCallingPid();
                int r2 = Binder.getCallingUid();
                MediaSessionManager.RemoteUserInfo r3 = new MediaSessionManager.RemoteUserInfo(r0.getPackageNameForUid(r2), r1, r2);
                r0.mControllerCallbacks.register(r7, r3);
                Object r4 = r0.mLock;
                monitor-enter(r4);
            L15:
                th = move-exception;
                throw th;
            L11:
                if (r0.mRegistrationCallbackHandler == null) goto L13;
                r0.mRegistrationCallbackHandler.postCallbackRegistered(r1, r2);     // Catch: Throwable -> L15
            L13:
                monitor-exit(r4);     // Catch: Throwable -> L15
                return;
            L18:
                r7.onSessionDestroyed();     // Catch: Exception -> L6
                return;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void removeQueueItem(MediaDescriptionCompat r2) {
                postToHandler(27, r2);
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void removeQueueItemAt(int r2) {
                postToHandler(28, r2);
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void rewind() {
                postToHandler(17);
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void seekTo(long r3) {
                postToHandler(18, Long.valueOf(r3));
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void sendCommand(String r3, Bundle r4, ResultReceiverWrapper r5) {
                if (r5 != null) goto L5;
                ResultReceiver r1 = null;
            L6:
                postToHandler(1, new Command(r3, r4, r1));
                return;
            L5:
                r1 = r5.mResultReceiver;
                goto L6
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void sendCustomAction(String r2, Bundle r3) throws RemoteException {
                postToHandler(20, r2, r3);
            }

            @Override // android.support.v4.media.session.IMediaSession
            public boolean sendMediaButton(KeyEvent r2) {
                postToHandler(21, r2);
                return true;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void setCaptioningEnabled(boolean r3) {
                postToHandler(29, Boolean.valueOf(r3));
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void setPlaybackSpeed(float r3) {
                postToHandler(32, Float.valueOf(r3));
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void setRepeatMode(int r2) {
                postToHandler(23, r2);
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void setShuffleMode(int r2) {
                postToHandler(30, r2);
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void setShuffleModeEnabledRemoved(boolean r1) {
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void setVolumeTo(int r2, int r3, String r4) {
                MediaSessionImplBase r0 = this.mMediaSessionImplRef.get();
                if (r0 == null) goto L6;
                r0.setVolumeTo(r2, r3);
                return;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void skipToQueueItem(long r3) {
                postToHandler(11, Long.valueOf(r3));
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void stop() {
                postToHandler(13);
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void unregisterCallbackListener(IMediaControllerCallback r6) {
                MediaSessionImplBase r0 = this.mMediaSessionImplRef.get();
                if (r0 != null) goto L5;
                return;
            L5:
                r0.mControllerCallbacks.unregister(r6);
                int r1 = Binder.getCallingPid();
                int r2 = Binder.getCallingUid();
                Object r3 = r0.mLock;
                monitor-enter(r3);
            L12:
                th = move-exception;
                throw th;
            L8:
                if (r0.mRegistrationCallbackHandler == null) goto L10;
                r0.mRegistrationCallbackHandler.postCallbackUnregistered(r1, r2);     // Catch: Throwable -> L12
            L10:
                monitor-exit(r3);     // Catch: Throwable -> L12
            }
        }

        class MessageHandler extends Handler {
            private static final int KEYCODE_MEDIA_PAUSE = 127;
            private static final int KEYCODE_MEDIA_PLAY = 126;
            private static final int MSG_ADD_QUEUE_ITEM = 25;
            private static final int MSG_ADD_QUEUE_ITEM_AT = 26;
            private static final int MSG_ADJUST_VOLUME = 2;
            private static final int MSG_COMMAND = 1;
            private static final int MSG_CUSTOM_ACTION = 20;
            private static final int MSG_FAST_FORWARD = 16;
            private static final int MSG_MEDIA_BUTTON = 21;
            private static final int MSG_NEXT = 14;
            private static final int MSG_PAUSE = 12;
            private static final int MSG_PLAY = 7;
            private static final int MSG_PLAY_MEDIA_ID = 8;
            private static final int MSG_PLAY_SEARCH = 9;
            private static final int MSG_PLAY_URI = 10;
            private static final int MSG_PREPARE = 3;
            private static final int MSG_PREPARE_MEDIA_ID = 4;
            private static final int MSG_PREPARE_SEARCH = 5;
            private static final int MSG_PREPARE_URI = 6;
            private static final int MSG_PREVIOUS = 15;
            private static final int MSG_RATE = 19;
            private static final int MSG_RATE_EXTRA = 31;
            private static final int MSG_REMOVE_QUEUE_ITEM = 27;
            private static final int MSG_REMOVE_QUEUE_ITEM_AT = 28;
            private static final int MSG_REWIND = 17;
            private static final int MSG_SEEK_TO = 18;
            private static final int MSG_SET_CAPTIONING_ENABLED = 29;
            private static final int MSG_SET_PLAYBACK_SPEED = 32;
            private static final int MSG_SET_REPEAT_MODE = 23;
            private static final int MSG_SET_SHUFFLE_MODE = 30;
            private static final int MSG_SET_VOLUME = 22;
            private static final int MSG_SKIP_TO_ITEM = 11;
            private static final int MSG_STOP = 13;
            final /* synthetic */ MediaSessionImplBase this$0;

            public MessageHandler(MediaSessionImplBase r1, Looper r2) {
                this.this$0 = r1;
                super(r2);
            }

            private void onMediaButtonEvent(KeyEvent r8, Callback r9) {
                if (r8 != null) goto L4;
                return;
            L4:
                if (r8.getAction() == 0) goto L7;
                return;
            L7:
                if (this.this$0.mState != null) goto L9;
                long r3 = 0;
            L11:
                switch(r8.getKeyCode()) {
                    case 79: goto L34;
                    case 85: goto L34;
                    case 86: goto L32;
                    case 87: goto L29;
                    case 88: goto L26;
                    case 89: goto L23;
                    case 90: goto L20;
                    case 126: goto L17;
                    case 127: goto L14;
                    default: goto L51;
                };
            L34:
                Log.w(MediaSessionCompat.TAG, "KEYCODE_MEDIA_PLAY_PAUSE and KEYCODE_HEADSETHOOK are handled already");
                return;
            L51:
                return;
            L14:
                if ((2 & r3) == 0) goto L37;
                r9.onPause();
                return;
            L37:
                return;
            L17:
                if ((4 & r3) == 0) goto L39;
                r9.onPlay();
                return;
            L39:
                return;
            L20:
                if ((64 & r3) == 0) goto L41;
                r9.onFastForward();
                return;
            L41:
                return;
            L23:
                if ((8 & r3) == 0) goto L43;
                r9.onRewind();
                return;
            L43:
                return;
            L26:
                if ((16 & r3) == 0) goto L45;
                r9.onSkipToPrevious();
                return;
            L45:
                return;
            L29:
                if ((32 & r3) == 0) goto L47;
                r9.onSkipToNext();
                return;
            L47:
                return;
            L32:
                if ((1 & r3) == 0) goto L49;
                r9.onStop();
                return;
            L49:
                return;
            L9:
                r3 = this.this$0.mState.getActions();
                goto L11
            }

            @Override // android.os.Handler
            public void handleMessage(Message r9) {
                Callback r0 = this.this$0.mCallback;
                if (r0 != null) goto L5;
                return;
            L5:
                Bundle r1 = r9.getData();
                MediaSessionCompat.ensureClassLoader(r1);
                this.this$0.setCurrentControllerInfo(new MediaSessionManager.RemoteUserInfo(r1.getString(MediaSessionCompat.DATA_CALLING_PACKAGE), r1.getInt("data_calling_pid"), r1.getInt("data_calling_uid")));
                Bundle r2 = r1.getBundle(MediaSessionCompat.DATA_EXTRAS);
                MediaSessionCompat.ensureClassLoader(r2);
            L54:
                th = move-exception;
                this.this$0.setCurrentControllerInfo(null);
                throw th;
            L7:
                switch(r9.what) {
                    case 1: goto L51;
                    case 2: goto L50;
                    case 3: goto L49;
                    case 4: goto L48;
                    case 5: goto L47;
                    case 6: goto L46;
                    case 7: goto L45;
                    case 8: goto L44;
                    case 9: goto L43;
                    case 10: goto L42;
                    case 11: goto L41;
                    case 12: goto L40;
                    case 13: goto L39;
                    case 14: goto L38;
                    case 15: goto L37;
                    case 16: goto L36;
                    case 17: goto L35;
                    case 18: goto L34;
                    case 19: goto L33;
                    case 20: goto L32;
                    case 21: goto L29;
                    case 22: goto L28;
                    case 23: goto L27;
                    case 24: goto L52;
                    case 25: goto L26;
                    case 26: goto L25;
                    case 27: goto L24;
                    case 28: goto L14;
                    case 29: goto L12;
                    case 30: goto L11;
                    case 31: goto L10;
                    case 32: goto L9;
                    default: goto L52;
                };     // Catch: Throwable -> L54
            L9:
                r0.onSetPlaybackSpeed(((Float) r9.obj).floatValue());     // Catch: Throwable -> L54
                goto L52
            L10:
                r0.onSetRating((RatingCompat) r9.obj, r2);     // Catch: Throwable -> L54
                goto L52
            L11:
                r0.onSetShuffleMode(r9.arg1);     // Catch: Throwable -> L54
                goto L52
            L12:
                r0.onSetCaptioningEnabled(((Boolean) r9.obj).booleanValue());     // Catch: Throwable -> L54
                goto L52
            L24:
                r0.onRemoveQueueItem((MediaDescriptionCompat) r9.obj);     // Catch: Throwable -> L54
                goto L52
            L25:
                r0.onAddQueueItem((MediaDescriptionCompat) r9.obj, r9.arg1);     // Catch: Throwable -> L54
                goto L52
            L26:
                r0.onAddQueueItem((MediaDescriptionCompat) r9.obj);     // Catch: Throwable -> L54
                goto L52
            L27:
                r0.onSetRepeatMode(r9.arg1);     // Catch: Throwable -> L54
                goto L52
            L28:
                this.this$0.setVolumeTo(r9.arg1, 0);     // Catch: Throwable -> L54
                goto L52
            L29:
                KeyEvent r4 = (KeyEvent) r9.obj;     // Catch: Throwable -> L54
                Intent r5 = new Intent("android.intent.action.MEDIA_BUTTON");     // Catch: Throwable -> L54
                r5.putExtra("android.intent.extra.KEY_EVENT", r4);     // Catch: Throwable -> L54
                if (r0.onMediaButtonEvent(r5) == true) goto L52;
                onMediaButtonEvent(r4, r0);     // Catch: Throwable -> L54
                goto L52
            L32:
                r0.onCustomAction((String) r9.obj, r2);     // Catch: Throwable -> L54
                goto L52
            L33:
                r0.onSetRating((RatingCompat) r9.obj);     // Catch: Throwable -> L54
                goto L52
            L34:
                r0.onSeekTo(((Long) r9.obj).longValue());     // Catch: Throwable -> L54
                goto L52
            L35:
                r0.onRewind();     // Catch: Throwable -> L54
                goto L52
            L36:
                r0.onFastForward();     // Catch: Throwable -> L54
                goto L52
            L37:
                r0.onSkipToPrevious();     // Catch: Throwable -> L54
                goto L52
            L38:
                r0.onSkipToNext();     // Catch: Throwable -> L54
                goto L52
            L39:
                r0.onStop();     // Catch: Throwable -> L54
                goto L52
            L40:
                r0.onPause();     // Catch: Throwable -> L54
                goto L52
            L41:
                r0.onSkipToQueueItem(((Long) r9.obj).longValue());     // Catch: Throwable -> L54
                goto L52
            L42:
                r0.onPlayFromUri((Uri) r9.obj, r2);     // Catch: Throwable -> L54
                goto L52
            L43:
                r0.onPlayFromSearch((String) r9.obj, r2);     // Catch: Throwable -> L54
                goto L52
            L44:
                r0.onPlayFromMediaId((String) r9.obj, r2);     // Catch: Throwable -> L54
                goto L52
            L45:
                r0.onPlay();     // Catch: Throwable -> L54
                goto L52
            L46:
                r0.onPrepareFromUri((Uri) r9.obj, r2);     // Catch: Throwable -> L54
                goto L52
            L47:
                r0.onPrepareFromSearch((String) r9.obj, r2);     // Catch: Throwable -> L54
                goto L52
            L48:
                r0.onPrepareFromMediaId((String) r9.obj, r2);     // Catch: Throwable -> L54
                goto L52
            L49:
                r0.onPrepare();     // Catch: Throwable -> L54
                goto L52
            L50:
                this.this$0.adjustVolume(r9.arg1, 0);     // Catch: Throwable -> L54
                goto L52
            L51:
                Command r6 = (Command) r9.obj;     // Catch: Throwable -> L54
                r0.onCommand(r6.command, r6.extras, r6.stub);     // Catch: Throwable -> L54
            L52:
                this.this$0.setCurrentControllerInfo(null);
                return;
            L14:
                if (this.this$0.mQueue == null) goto L52;
                if (r9.arg1 >= 0) goto L18;
            L20:
                QueueItem r7 = null;
            L21:
                if (r7 == null) goto L52;
                r0.onRemoveQueueItem(r7.getDescription());     // Catch: Throwable -> L54
                goto L52
            L18:
                if (r9.arg1 >= this.this$0.mQueue.size()) goto L20;
                r7 = this.this$0.mQueue.get(r9.arg1);     // Catch: Throwable -> L54
                goto L21
            }
        }

        public MediaSessionImplBase(Context r6, String r7, ComponentName r8, PendingIntent r9, VersionedParcelable r10, Bundle r11) {
            this.mLock = new Object();
            this.mControllerCallbacks = new RemoteCallbackList();
            this.mDestroyed = false;
            this.mIsActive = false;
            this.mFlags = 3;
            this.mVolumeCallback = new C00221(this);
            if (r8 == null) goto L7;
            this.mContext = r6;
            this.mSessionInfo = r11;
            this.mAudioManager = (AudioManager) r6.getSystemService(MimeTypes.BASE_TYPE_AUDIO);
            this.mMediaButtonReceiverComponentName = r8;
            this.mMediaButtonReceiverIntent = r9;
            this.mStub = new MediaSessionStub(this, r6.getPackageName(), r7);
            this.mToken = new Token(this.mStub, null, r10);
            this.mRatingType = 0;
            this.mVolumeType = 1;
            this.mLocalStream = 3;
            this.mRcc = new RemoteControlClient(r9);
            return;
        L7:
            throw new IllegalArgumentException("MediaButtonReceiver component may not be null");
        }

        private void sendCaptioningEnabled(boolean r6) {
            Object r0 = this.mLock;
            monitor-enter(r0);
            int r2 = this.mControllerCallbacks.beginBroadcast() - 1;     // Catch: Throwable -> L14
        L5:
            if (r2 < 0) goto L11;
            ((IMediaControllerCallback) this.mControllerCallbacks.getBroadcastItem(r2)).onCaptioningEnabledChanged(r6);     // Catch: RemoteException -> L9 Throwable -> L14
        L10:
            r2 = r2 - 1;
            goto L5
        L11:
            this.mControllerCallbacks.finishBroadcast();     // Catch: Throwable -> L14
            monitor-exit(r0);     // Catch: Throwable -> L14
            return;
        L14:
            th = move-exception;
            throw th;
        }

        private void sendEvent(String r6, Bundle r7) {
            Object r0 = this.mLock;
            monitor-enter(r0);
            int r2 = this.mControllerCallbacks.beginBroadcast() - 1;     // Catch: Throwable -> L14
        L5:
            if (r2 < 0) goto L11;
            ((IMediaControllerCallback) this.mControllerCallbacks.getBroadcastItem(r2)).onEvent(r6, r7);     // Catch: RemoteException -> L9 Throwable -> L14
        L10:
            r2 = r2 - 1;
            goto L5
        L11:
            this.mControllerCallbacks.finishBroadcast();     // Catch: Throwable -> L14
            monitor-exit(r0);     // Catch: Throwable -> L14
            return;
        L14:
            th = move-exception;
            throw th;
        }

        private void sendExtras(Bundle r6) {
            Object r0 = this.mLock;
            monitor-enter(r0);
            int r2 = this.mControllerCallbacks.beginBroadcast() - 1;     // Catch: Throwable -> L14
        L5:
            if (r2 < 0) goto L11;
            ((IMediaControllerCallback) this.mControllerCallbacks.getBroadcastItem(r2)).onExtrasChanged(r6);     // Catch: RemoteException -> L9 Throwable -> L14
        L10:
            r2 = r2 - 1;
            goto L5
        L11:
            this.mControllerCallbacks.finishBroadcast();     // Catch: Throwable -> L14
            monitor-exit(r0);     // Catch: Throwable -> L14
            return;
        L14:
            th = move-exception;
            throw th;
        }

        private void sendMetadata(MediaMetadataCompat r6) {
            Object r0 = this.mLock;
            monitor-enter(r0);
            int r2 = this.mControllerCallbacks.beginBroadcast() - 1;     // Catch: Throwable -> L14
        L5:
            if (r2 < 0) goto L11;
            ((IMediaControllerCallback) this.mControllerCallbacks.getBroadcastItem(r2)).onMetadataChanged(r6);     // Catch: RemoteException -> L9 Throwable -> L14
        L10:
            r2 = r2 - 1;
            goto L5
        L11:
            this.mControllerCallbacks.finishBroadcast();     // Catch: Throwable -> L14
            monitor-exit(r0);     // Catch: Throwable -> L14
            return;
        L14:
            th = move-exception;
            throw th;
        }

        private void sendQueue(List<QueueItem> r6) {
            Object r0 = this.mLock;
            monitor-enter(r0);
            int r2 = this.mControllerCallbacks.beginBroadcast() - 1;     // Catch: Throwable -> L14
        L5:
            if (r2 < 0) goto L11;
            ((IMediaControllerCallback) this.mControllerCallbacks.getBroadcastItem(r2)).onQueueChanged(r6);     // Catch: RemoteException -> L9 Throwable -> L14
        L10:
            r2 = r2 - 1;
            goto L5
        L11:
            this.mControllerCallbacks.finishBroadcast();     // Catch: Throwable -> L14
            monitor-exit(r0);     // Catch: Throwable -> L14
            return;
        L14:
            th = move-exception;
            throw th;
        }

        private void sendQueueTitle(CharSequence r6) {
            Object r0 = this.mLock;
            monitor-enter(r0);
            int r2 = this.mControllerCallbacks.beginBroadcast() - 1;     // Catch: Throwable -> L14
        L5:
            if (r2 < 0) goto L11;
            ((IMediaControllerCallback) this.mControllerCallbacks.getBroadcastItem(r2)).onQueueTitleChanged(r6);     // Catch: RemoteException -> L9 Throwable -> L14
        L10:
            r2 = r2 - 1;
            goto L5
        L11:
            this.mControllerCallbacks.finishBroadcast();     // Catch: Throwable -> L14
            monitor-exit(r0);     // Catch: Throwable -> L14
            return;
        L14:
            th = move-exception;
            throw th;
        }

        private void sendRepeatMode(int r6) {
            Object r0 = this.mLock;
            monitor-enter(r0);
            int r2 = this.mControllerCallbacks.beginBroadcast() - 1;     // Catch: Throwable -> L14
        L5:
            if (r2 < 0) goto L11;
            ((IMediaControllerCallback) this.mControllerCallbacks.getBroadcastItem(r2)).onRepeatModeChanged(r6);     // Catch: RemoteException -> L9 Throwable -> L14
        L10:
            r2 = r2 - 1;
            goto L5
        L11:
            this.mControllerCallbacks.finishBroadcast();     // Catch: Throwable -> L14
            monitor-exit(r0);     // Catch: Throwable -> L14
            return;
        L14:
            th = move-exception;
            throw th;
        }

        private void sendSessionDestroyed() {
            Object r0 = this.mLock;
            monitor-enter(r0);
            int r2 = this.mControllerCallbacks.beginBroadcast() - 1;     // Catch: Throwable -> L14
        L5:
            if (r2 < 0) goto L11;
            ((IMediaControllerCallback) this.mControllerCallbacks.getBroadcastItem(r2)).onSessionDestroyed();     // Catch: RemoteException -> L9 Throwable -> L14
        L10:
            r2 = r2 - 1;
            goto L5
        L11:
            this.mControllerCallbacks.finishBroadcast();     // Catch: Throwable -> L14
            this.mControllerCallbacks.kill();     // Catch: Throwable -> L14
            monitor-exit(r0);     // Catch: Throwable -> L14
            return;
        L14:
            th = move-exception;
            throw th;
        }

        private void sendShuffleMode(int r6) {
            Object r0 = this.mLock;
            monitor-enter(r0);
            int r2 = this.mControllerCallbacks.beginBroadcast() - 1;     // Catch: Throwable -> L14
        L5:
            if (r2 < 0) goto L11;
            ((IMediaControllerCallback) this.mControllerCallbacks.getBroadcastItem(r2)).onShuffleModeChanged(r6);     // Catch: RemoteException -> L9 Throwable -> L14
        L10:
            r2 = r2 - 1;
            goto L5
        L11:
            this.mControllerCallbacks.finishBroadcast();     // Catch: Throwable -> L14
            monitor-exit(r0);     // Catch: Throwable -> L14
            return;
        L14:
            th = move-exception;
            throw th;
        }

        private void sendState(PlaybackStateCompat r6) {
            Object r0 = this.mLock;
            monitor-enter(r0);
            int r2 = this.mControllerCallbacks.beginBroadcast() - 1;     // Catch: Throwable -> L14
        L5:
            if (r2 < 0) goto L11;
            ((IMediaControllerCallback) this.mControllerCallbacks.getBroadcastItem(r2)).onPlaybackStateChanged(r6);     // Catch: RemoteException -> L9 Throwable -> L14
        L10:
            r2 = r2 - 1;
            goto L5
        L11:
            this.mControllerCallbacks.finishBroadcast();     // Catch: Throwable -> L14
            monitor-exit(r0);     // Catch: Throwable -> L14
            return;
        L14:
            th = move-exception;
            throw th;
        }

        void adjustVolume(int r3, int r4) {
            if (this.mVolumeType == 2) goto L5;
            this.mAudioManager.adjustStreamVolume(this.mLocalStream, r3, r4);
            return;
        L5:
            if (this.mVolumeProvider == null) goto L9;
            this.mVolumeProvider.onAdjustVolume(r3);
            return;
        }

        RemoteControlClient.MetadataEditor buildRccMetadata(Bundle r7) {
            RemoteControlClient.MetadataEditor r0 = this.mRcc.editMetadata(true);
            if (r7 != null) goto L6;
            return r0;
        L6:
            if (r7.containsKey(MediaMetadataCompat.METADATA_KEY_ART) == false) goto L12;
            Bitmap r2 = (Bitmap) r7.getParcelable(MediaMetadataCompat.METADATA_KEY_ART);
            if (r2 == null) goto L10;
            r2 = r2.copy(r2.getConfig(), false);
        L10:
            r0.putBitmap(100, r2);
        L19:
            if (r7.containsKey(MediaMetadataCompat.METADATA_KEY_ALBUM) == false) goto L22;
            r0.putString(1, r7.getString(MediaMetadataCompat.METADATA_KEY_ALBUM));
        L22:
            if (r7.containsKey(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST) == false) goto L25;
            r0.putString(13, r7.getString(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST));
        L25:
            if (r7.containsKey(MediaMetadataCompat.METADATA_KEY_ARTIST) == false) goto L28;
            r0.putString(2, r7.getString(MediaMetadataCompat.METADATA_KEY_ARTIST));
        L28:
            if (r7.containsKey(MediaMetadataCompat.METADATA_KEY_AUTHOR) == false) goto L31;
            r0.putString(3, r7.getString(MediaMetadataCompat.METADATA_KEY_AUTHOR));
        L31:
            if (r7.containsKey(MediaMetadataCompat.METADATA_KEY_COMPILATION) == false) goto L34;
            r0.putString(15, r7.getString(MediaMetadataCompat.METADATA_KEY_COMPILATION));
        L34:
            if (r7.containsKey(MediaMetadataCompat.METADATA_KEY_COMPOSER) == false) goto L37;
            r0.putString(4, r7.getString(MediaMetadataCompat.METADATA_KEY_COMPOSER));
        L37:
            if (r7.containsKey(MediaMetadataCompat.METADATA_KEY_DATE) == false) goto L40;
            r0.putString(5, r7.getString(MediaMetadataCompat.METADATA_KEY_DATE));
        L40:
            if (r7.containsKey(MediaMetadataCompat.METADATA_KEY_DISC_NUMBER) == false) goto L43;
            r0.putLong(14, r7.getLong(MediaMetadataCompat.METADATA_KEY_DISC_NUMBER));
        L43:
            if (r7.containsKey(MediaMetadataCompat.METADATA_KEY_DURATION) == false) goto L46;
            r0.putLong(9, r7.getLong(MediaMetadataCompat.METADATA_KEY_DURATION));
        L46:
            if (r7.containsKey(MediaMetadataCompat.METADATA_KEY_GENRE) == false) goto L49;
            r0.putString(6, r7.getString(MediaMetadataCompat.METADATA_KEY_GENRE));
        L49:
            if (r7.containsKey(MediaMetadataCompat.METADATA_KEY_TITLE) == false) goto L52;
            r0.putString(7, r7.getString(MediaMetadataCompat.METADATA_KEY_TITLE));
        L52:
            if (r7.containsKey(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER) == false) goto L55;
            r0.putLong(0, r7.getLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER));
        L55:
            if (r7.containsKey(MediaMetadataCompat.METADATA_KEY_WRITER) == false) goto L57;
            r0.putString(11, r7.getString(MediaMetadataCompat.METADATA_KEY_WRITER));
        L57:
            return r0;
        L12:
            if (r7.containsKey(MediaMetadataCompat.METADATA_KEY_ALBUM_ART) == false) goto L19;
            Bitmap r3 = (Bitmap) r7.getParcelable(MediaMetadataCompat.METADATA_KEY_ALBUM_ART);
            if (r3 == null) goto L16;
            r3 = r3.copy(r3.getConfig(), false);
        L16:
            r0.putBitmap(100, r3);
            goto L19
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public Callback getCallback() {
            Object r0 = this.mLock;
            monitor-enter(r0);
            Callback r1 = this.mCallback;     // Catch: Throwable -> L7
            monitor-exit(r0);     // Catch: Throwable -> L7
            return r1;
        L7:
            th = move-exception;
            throw th;
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public String getCallingPackage() {
            return null;
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public MediaSessionManager.RemoteUserInfo getCurrentControllerInfo() {
            Object r0 = this.mLock;
            monitor-enter(r0);
            MediaSessionManager.RemoteUserInfo r1 = this.mRemoteUserInfo;     // Catch: Throwable -> L7
            monitor-exit(r0);     // Catch: Throwable -> L7
            return r1;
        L7:
            th = move-exception;
            throw th;
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public Object getMediaSession() {
            return null;
        }

        String getPackageNameForUid(int r3) {
            String r0 = this.mContext.getPackageManager().getNameForUid(r3);
            if (TextUtils.isEmpty(r0) == false) goto L6;
            return MediaSessionManager.RemoteUserInfo.LEGACY_CONTROLLER;
        L6:
            return r0;
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public PlaybackStateCompat getPlaybackState() {
            Object r0 = this.mLock;
            monitor-enter(r0);
            PlaybackStateCompat r1 = this.mState;     // Catch: Throwable -> L7
            monitor-exit(r0);     // Catch: Throwable -> L7
            return r1;
        L7:
            th = move-exception;
            throw th;
        }

        int getRccStateFromState(int r2) {
            switch(r2) {
                case 0: goto L23;
                case 1: goto L21;
                case 2: goto L19;
                case 3: goto L17;
                case 4: goto L15;
                case 5: goto L13;
                case 6: goto L11;
                case 7: goto L9;
                case 8: goto L11;
                case 9: goto L7;
                case 10: goto L5;
                case 11: goto L5;
                default: goto L3;
            };
        L3:
            return -1;
        L5:
            return 6;
        L7:
            return 7;
        L9:
            return 9;
        L11:
            return 8;
        L13:
            return 5;
        L15:
            return 4;
        L17:
            return 3;
        L19:
            return 2;
        L21:
            return 1;
        L23:
            return 0;
        }

        int getRccTransportControlFlagsFromActions(long r6) {
            int r0 = 0;
            if ((1 & r6) == 0) goto L6;
            r0 = 0 | 32;
        L6:
            if ((2 & r6) == 0) goto L9;
            r0 = r0 | 16;
        L9:
            if ((4 & r6) == 0) goto L12;
            r0 = r0 | 4;
        L12:
            if ((8 & r6) == 0) goto L15;
            r0 = r0 | 2;
        L15:
            if ((16 & r6) == 0) goto L18;
            r0 = r0 | 1;
        L18:
            if ((32 & r6) == 0) goto L21;
            r0 = r0 | 128;
        L21:
            if ((64 & r6) == 0) goto L24;
            r0 = r0 | 64;
        L24:
            if ((512 & r6) != 0) goto L26;
            return r0;
        L26:
            return r0 | 8;
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public Object getRemoteControlClient() {
            return null;
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public Token getSessionToken() {
            return this.mToken;
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public boolean isActive() {
            return this.mIsActive;
        }

        void postToHandler(int r8, int r9, int r10, Object r11, Bundle r12) {
            Object r0 = this.mLock;
            monitor-enter(r0);
        L15:
            th = move-exception;
            throw th;
        L5:
            if (this.mHandler == null) goto L13;
            Message r1 = this.mHandler.obtainMessage(r8, r9, r10, r11);     // Catch: Throwable -> L15
            Bundle r2 = new Bundle();     // Catch: Throwable -> L15
            int r3 = Binder.getCallingUid();     // Catch: Throwable -> L15
            r2.putInt("data_calling_uid", r3);     // Catch: Throwable -> L15
            r2.putString(MediaSessionCompat.DATA_CALLING_PACKAGE, getPackageNameForUid(r3));     // Catch: Throwable -> L15
            int r4 = Binder.getCallingPid();     // Catch: Throwable -> L15
            if (r4 <= 0) goto L9;
            r2.putInt("data_calling_pid", r4);     // Catch: Throwable -> L15
        L10:
            if (r12 == null) goto L12;
            r2.putBundle(MediaSessionCompat.DATA_EXTRAS, r12);     // Catch: Throwable -> L15
        L12:
            r1.setData(r2);     // Catch: Throwable -> L15
            r1.sendToTarget();     // Catch: Throwable -> L15
            goto L13
        L9:
            r2.putInt("data_calling_pid", -1);     // Catch: Throwable -> L15
        L13:
            monitor-exit(r0);     // Catch: Throwable -> L15
        }

        void registerMediaButtonEventReceiver(PendingIntent r2, ComponentName r3) {
            this.mAudioManager.registerMediaButtonEventReceiver(r3);
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public void release() {
            this.mIsActive = false;
            this.mDestroyed = true;
            updateMbrAndRcc();
            sendSessionDestroyed();
            setCallback(null, null);
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public void sendSessionEvent(String r1, Bundle r2) {
            sendEvent(r1, r2);
        }

        void sendVolumeInfoChanged(ParcelableVolumeInfo r6) {
            Object r0 = this.mLock;
            monitor-enter(r0);
            int r2 = this.mControllerCallbacks.beginBroadcast() - 1;     // Catch: Throwable -> L14
        L5:
            if (r2 < 0) goto L11;
            ((IMediaControllerCallback) this.mControllerCallbacks.getBroadcastItem(r2)).onVolumeInfoChanged(r6);     // Catch: RemoteException -> L9 Throwable -> L14
        L10:
            r2 = r2 - 1;
            goto L5
        L11:
            this.mControllerCallbacks.finishBroadcast();     // Catch: Throwable -> L14
            monitor-exit(r0);     // Catch: Throwable -> L14
            return;
        L14:
            th = move-exception;
            throw th;
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public void setActive(boolean r2) {
            if (r2 != this.mIsActive) goto L5;
            return;
        L5:
            this.mIsActive = r2;
            updateMbrAndRcc();
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public void setCallback(Callback r5, Handler r6) {
            Object r0 = this.mLock;
            monitor-enter(r0);
        L22:
            th = move-exception;
            throw th;
        L5:
            if (this.mHandler == null) goto L7;
            this.mHandler.removeCallbacksAndMessages(null);     // Catch: Throwable -> L22
        L7:
            if (r5 == null) goto L11;
            if (r6 == null) goto L11;
            MessageHandler r1 = new MessageHandler(this, r6.getLooper());     // Catch: Throwable -> L22
        L12:
            this.mHandler = r1;     // Catch: Throwable -> L22
            if (this.mCallback != r5) goto L15;
        L17:
            this.mCallback = r5;     // Catch: Throwable -> L22
            if (this.mCallback == null) goto L20;
            this.mCallback.setSessionImpl(this, r6);     // Catch: Throwable -> L22
        L20:
            monitor-exit(r0);     // Catch: Throwable -> L22
            return;
        L15:
            if (this.mCallback == null) goto L17;
            this.mCallback.setSessionImpl(null, null);     // Catch: Throwable -> L22
        L11:
            r1 = null;
            goto L12
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public void setCaptioningEnabled(boolean r2) {
            if (this.mCaptioningEnabled == r2) goto L6;
            this.mCaptioningEnabled = r2;
            sendCaptioningEnabled(r2);
            return;
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public void setCurrentControllerInfo(MediaSessionManager.RemoteUserInfo r3) {
            Object r0 = this.mLock;
            monitor-enter(r0);
            this.mRemoteUserInfo = r3;     // Catch: Throwable -> L7
            monitor-exit(r0);     // Catch: Throwable -> L7
            return;
        L7:
            th = move-exception;
            throw th;
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public void setExtras(Bundle r1) {
            this.mExtras = r1;
            sendExtras(r1);
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public void setFlags(int r3) {
            Object r0 = this.mLock;
            monitor-enter(r0);
            this.mFlags = (r3 | 1) | 2;     // Catch: Throwable -> L8
            monitor-exit(r0);     // Catch: Throwable -> L8
            return;
        L8:
            th = move-exception;
            throw th;
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public void setMediaButtonReceiver(PendingIntent r1) {
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public void setMetadata(MediaMetadataCompat r3) {
            if (r3 == null) goto L4;
            r3 = new MediaMetadataCompat.Builder(r3, MediaSessionCompat.sMaxBitmapSize).build();
        L4:
            Object r0 = this.mLock;
            monitor-enter(r0);
            this.mMetadata = r3;     // Catch: Throwable -> L17
            monitor-exit(r0);     // Catch: Throwable -> L17
            sendMetadata(r3);
            if (this.mIsActive == true) goto L12;
            return;
        L12:
            if (r3 != null) goto L14;
            Bundle r1 = null;
        L15:
            buildRccMetadata(r1).apply();
            return;
        L14:
            r1 = r3.getBundle();
        L17:
            th = move-exception;
            throw th;
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public void setPlaybackState(PlaybackStateCompat r4) {
            Object r0 = this.mLock;
            monitor-enter(r0);
            this.mState = r4;     // Catch: Throwable -> L13
            monitor-exit(r0);     // Catch: Throwable -> L13
            sendState(r4);
            if (this.mIsActive == true) goto L9;
            return;
        L9:
            if (r4 != null) goto L11;
            this.mRcc.setPlaybackState(0);
            this.mRcc.setTransportControlFlags(0);
            return;
        L11:
            setRccState(r4);
            this.mRcc.setTransportControlFlags(getRccTransportControlFlagsFromActions(r4.getActions()));
            return;
        L13:
            th = move-exception;
            throw th;
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public void setPlaybackToLocal(int r8) {
            if (this.mVolumeProvider == null) goto L5;
            this.mVolumeProvider.setCallback(null);
        L5:
            this.mLocalStream = r8;
            this.mVolumeType = 1;
            sendVolumeInfoChanged(new ParcelableVolumeInfo(this.mVolumeType, this.mLocalStream, 2, this.mAudioManager.getStreamMaxVolume(this.mLocalStream), this.mAudioManager.getStreamVolume(this.mLocalStream)));
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public void setPlaybackToRemote(VolumeProviderCompat r8) {
            if (r8 == null) goto L9;
            if (this.mVolumeProvider == null) goto L6;
            this.mVolumeProvider.setCallback(null);
        L6:
            this.mVolumeType = 2;
            this.mVolumeProvider = r8;
            sendVolumeInfoChanged(new ParcelableVolumeInfo(this.mVolumeType, this.mLocalStream, this.mVolumeProvider.getVolumeControl(), this.mVolumeProvider.getMaxVolume(), this.mVolumeProvider.getCurrentVolume()));
            r8.setCallback(this.mVolumeCallback);
            return;
        L9:
            throw new IllegalArgumentException("volumeProvider may not be null");
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public void setQueue(List<QueueItem> r1) {
            this.mQueue = r1;
            sendQueue(r1);
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public void setQueueTitle(CharSequence r1) {
            this.mQueueTitle = r1;
            sendQueueTitle(r1);
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public void setRatingType(int r1) {
            this.mRatingType = r1;
        }

        void setRccState(PlaybackStateCompat r3) {
            this.mRcc.setPlaybackState(getRccStateFromState(r3.getState()));
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public void setRegistrationCallback(RegistrationCallback r4, Handler r5) {
            Object r0 = this.mLock;
            monitor-enter(r0);
        L12:
            th = move-exception;
            throw th;
        L5:
            if (this.mRegistrationCallbackHandler == null) goto L7;
            this.mRegistrationCallbackHandler.removeCallbacksAndMessages(null);     // Catch: Throwable -> L12
        L7:
            if (r4 == null) goto L9;
            this.mRegistrationCallbackHandler = new RegistrationCallbackHandler(r5.getLooper(), r4);     // Catch: Throwable -> L12
        L10:
            monitor-exit(r0);     // Catch: Throwable -> L12
            return;
        L9:
            this.mRegistrationCallbackHandler = null;     // Catch: Throwable -> L12
            goto L10
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public void setRepeatMode(int r2) {
            if (this.mRepeatMode == r2) goto L6;
            this.mRepeatMode = r2;
            sendRepeatMode(r2);
            return;
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public void setSessionActivity(PendingIntent r3) {
            Object r0 = this.mLock;
            monitor-enter(r0);
            this.mSessionActivity = r3;     // Catch: Throwable -> L7
            monitor-exit(r0);     // Catch: Throwable -> L7
            return;
        L7:
            th = move-exception;
            throw th;
        }

        @Override // android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl
        public void setShuffleMode(int r2) {
            if (this.mShuffleMode == r2) goto L6;
            this.mShuffleMode = r2;
            sendShuffleMode(r2);
            return;
        }

        void setVolumeTo(int r3, int r4) {
            if (this.mVolumeType == 2) goto L5;
            this.mAudioManager.setStreamVolume(this.mLocalStream, r3, r4);
            return;
        L5:
            if (this.mVolumeProvider == null) goto L9;
            this.mVolumeProvider.onSetVolumeTo(r3);
            return;
        }

        void unregisterMediaButtonEventReceiver(PendingIntent r2, ComponentName r3) {
            this.mAudioManager.unregisterMediaButtonEventReceiver(r3);
        }

        void updateMbrAndRcc() {
            if (this.mIsActive == false) goto L5;
            registerMediaButtonEventReceiver(this.mMediaButtonReceiverIntent, this.mMediaButtonReceiverComponentName);
            this.mAudioManager.registerRemoteControlClient(this.mRcc);
            setMetadata(this.mMetadata);
            setPlaybackState(this.mState);
            return;
        L5:
            unregisterMediaButtonEventReceiver(this.mMediaButtonReceiverIntent, this.mMediaButtonReceiverComponentName);
            this.mRcc.setPlaybackState(0);
            this.mAudioManager.unregisterRemoteControlClient(this.mRcc);
        }
    }

    public interface OnActiveChangeListener {
        void onActiveChanged();
    }

    public static final class QueueItem implements Parcelable {
        public static final Parcelable.Creator<QueueItem> CREATOR = null;
        public static final int UNKNOWN_ID = -1;
        private final MediaDescriptionCompat mDescription;
        private final long mId;
        private MediaSession.QueueItem mItemFwk;

        private static class Api21Impl {
            private Api21Impl() {
            }

            static MediaSession.QueueItem createQueueItem(MediaDescription r1, long r2) {
                return new MediaSession.QueueItem(r1, r2);
            }

            static MediaDescription getDescription(MediaSession.QueueItem r1) {
                return r1.getDescription();
            }

            static long getQueueId(MediaSession.QueueItem r2) {
                return r2.getQueueId();
            }
        }

        static {
            CREATOR = new C00231();
        }

        private QueueItem(MediaSession.QueueItem r3, MediaDescriptionCompat r4, long r5) {
            if (r4 == null) goto L11;
            if (r5 == (-1)) goto L9;
            this.mDescription = r4;
            this.mId = r5;
            this.mItemFwk = r3;
            return;
        L9:
            throw new IllegalArgumentException("Id cannot be QueueItem.UNKNOWN_ID");
        L11:
            throw new IllegalArgumentException("Description cannot be null");
        }

        QueueItem(Parcel r3) {
            this.mDescription = MediaDescriptionCompat.CREATOR.createFromParcel(r3);
            this.mId = r3.readLong();
        }

        public QueueItem(MediaDescriptionCompat r2, long r3) {
            this(null, r2, r3);
        }

        public static QueueItem fromQueueItem(Object r6) {
            if (r6 == null) goto L5;
            MediaSession.QueueItem r0 = (MediaSession.QueueItem) r6;
            return new QueueItem(r0, MediaDescriptionCompat.fromMediaDescription(Api21Impl.getDescription(r0)), Api21Impl.getQueueId(r0));
        L5:
            return null;
        }

        public static List<QueueItem> fromQueueItemList(List<?> r4) {
            if (r4 == null) goto L8;
            ArrayList r0 = new ArrayList(r4.size());
            Iterator<?> r1 = r4.iterator();
        L5:
            if (r1.hasNext() == false) goto L7;
            r0.add(fromQueueItem(r1.next()));
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

        public long getQueueId() {
            return this.mId;
        }

        public Object getQueueItem() {
            if (this.mItemFwk != null) goto L7;
            this.mItemFwk = Api21Impl.createQueueItem((MediaDescription) this.mDescription.getMediaDescription(), this.mId);
            return this.mItemFwk;
        L7:
            return this.mItemFwk;
        }

        public String toString() {
            return "MediaSession.QueueItem {Description=" + this.mDescription + ", Id=" + this.mId + " }";
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel r3, int r4) {
            this.mDescription.writeToParcel(r3, r4);
            r3.writeLong(this.mId);
        }
    }

    public interface RegistrationCallback {
        void onCallbackRegistered(int r1, int r2);

        void onCallbackUnregistered(int r1, int r2);
    }

    static final class RegistrationCallbackHandler extends Handler {
        private static final int MSG_CALLBACK_REGISTERED = 1001;
        private static final int MSG_CALLBACK_UNREGISTERED = 1002;
        private final RegistrationCallback mCallback;

        RegistrationCallbackHandler(Looper r1, RegistrationCallback r2) {
            super(r1);
            this.mCallback = r2;
        }

        @Override // android.os.Handler
        public void handleMessage(Message r4) {
            super.handleMessage(r4);
            switch(r4.what) {
                case 1001: goto L6;
                case 1002: goto L5;
                default: goto L9;
            };
        L5:
            this.mCallback.onCallbackUnregistered(r4.arg1, r4.arg2);
            return;
        L6:
            this.mCallback.onCallbackRegistered(r4.arg1, r4.arg2);
            return;
        }

        public void postCallbackRegistered(int r2, int r3) {
            obtainMessage(1001, r2, r3).sendToTarget();
        }

        public void postCallbackUnregistered(int r2, int r3) {
            obtainMessage(1002, r2, r3).sendToTarget();
        }
    }

    static final class ResultReceiverWrapper implements Parcelable {
        public static final Parcelable.Creator<ResultReceiverWrapper> CREATOR = null;
        ResultReceiver mResultReceiver;

        static {
            CREATOR = new C00241();
        }

        ResultReceiverWrapper(Parcel r2) {
            this.mResultReceiver = (ResultReceiver) ResultReceiver.CREATOR.createFromParcel(r2);
        }

        public ResultReceiverWrapper(ResultReceiver r1) {
            this.mResultReceiver = r1;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel r2, int r3) {
            this.mResultReceiver.writeToParcel(r2, r3);
        }
    }

    public static final class Token implements Parcelable {
        public static final Parcelable.Creator<Token> CREATOR = null;
        private IMediaSession mExtraBinder;
        private final Object mInner;
        private final Object mLock;
        private VersionedParcelable mSession2Token;

        static {
            CREATOR = new C00251();
        }

        Token(Object r2) {
            this(r2, null, null);
        }

        Token(Object r2, IMediaSession r3) {
            this(r2, r3, null);
        }

        Token(Object r2, IMediaSession r3, VersionedParcelable r4) {
            this.mLock = new Object();
            this.mInner = r2;
            this.mExtraBinder = r3;
            this.mSession2Token = r4;
        }

        public static Token fromBundle(Bundle r5) {
            if (r5 != null) goto L5;
            return null;
        L5:
            r5.setClassLoader(Token.class.getClassLoader());
            IMediaSession r1 = IMediaSession.Stub.asInterface(BundleCompat.getBinder(r5, MediaSessionCompat.KEY_EXTRA_BINDER));
            VersionedParcelable r2 = ParcelUtils.getVersionedParcelable(r5, MediaSessionCompat.KEY_SESSION2_TOKEN);
            Token r3 = (Token) r5.getParcelable(MediaSessionCompat.KEY_TOKEN);
            if (r3 != null) goto L9;
            return null;
        L9:
            return new Token(r3.mInner, r1, r2);
        }

        public static Token fromToken(Object r1) {
            return fromToken(r1, null);
        }

        public static Token fromToken(Object r2, IMediaSession r3) {
            if (r2 != null) goto L4;
            return null;
        L4:
            if ((r2 instanceof MediaSession.Token) == false) goto L8;
            return new Token(r2, r3);
        L8:
            throw new IllegalArgumentException("token is not a valid MediaSession.Token object");
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public boolean equals(Object r5) {
            if (this != r5) goto L6;
            return true;
        L6:
            if ((r5 instanceof Token) == true) goto L8;
            return false;
        L8:
            Token r1 = (Token) r5;
            if (this.mInner != null) goto L16;
            if (r1.mInner != null) goto L14;
            return true;
        L14:
            return false;
        L16:
            if (r1.mInner != null) goto L19;
            return false;
        L19:
            return this.mInner.equals(r1.mInner);
        }

        public IMediaSession getExtraBinder() {
            Object r0 = this.mLock;
            monitor-enter(r0);
            IMediaSession r1 = this.mExtraBinder;     // Catch: Throwable -> L7
            monitor-exit(r0);     // Catch: Throwable -> L7
            return r1;
        L7:
            th = move-exception;
            throw th;
        }

        public VersionedParcelable getSession2Token() {
            Object r0 = this.mLock;
            monitor-enter(r0);
            VersionedParcelable r1 = this.mSession2Token;     // Catch: Throwable -> L7
            monitor-exit(r0);     // Catch: Throwable -> L7
            return r1;
        L7:
            th = move-exception;
            throw th;
        }

        public Object getToken() {
            return this.mInner;
        }

        public int hashCode() {
            if (this.mInner != null) goto L7;
            return 0;
        L7:
            return this.mInner.hashCode();
        }

        public void setExtraBinder(IMediaSession r3) {
            Object r0 = this.mLock;
            monitor-enter(r0);
            this.mExtraBinder = r3;     // Catch: Throwable -> L7
            monitor-exit(r0);     // Catch: Throwable -> L7
            return;
        L7:
            th = move-exception;
            throw th;
        }

        public void setSession2Token(VersionedParcelable r3) {
            Object r0 = this.mLock;
            monitor-enter(r0);
            this.mSession2Token = r3;     // Catch: Throwable -> L7
            monitor-exit(r0);     // Catch: Throwable -> L7
            return;
        L7:
            th = move-exception;
            throw th;
        }

        public Bundle toBundle() {
            Bundle r0 = new Bundle();
            r0.putParcelable(MediaSessionCompat.KEY_TOKEN, this);
            Object r1 = this.mLock;
            monitor-enter(r1);
        L12:
            th = move-exception;
            throw th;
        L5:
            if (this.mExtraBinder == null) goto L8;
            BundleCompat.putBinder(r0, MediaSessionCompat.KEY_EXTRA_BINDER, this.mExtraBinder.asBinder());     // Catch: Throwable -> L12
        L8:
            if (this.mSession2Token == null) goto L10;
            ParcelUtils.putVersionedParcelable(r0, MediaSessionCompat.KEY_SESSION2_TOKEN, this.mSession2Token);     // Catch: Throwable -> L12
        L10:
            monitor-exit(r1);     // Catch: Throwable -> L12
            return r0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel r2, int r3) {
            r2.writeParcelable((Parcelable) this.mInner, r3);
        }
    }

    private MediaSessionCompat(Context r2, MediaSessionImpl r3) {
        this.mActiveListeners = new ArrayList();
        this.mImpl = r3;
        this.mController = new MediaControllerCompat(r2, this);
    }

    public MediaSessionCompat(Context r2, String r3) {
        this(r2, r3, null, null);
    }

    public MediaSessionCompat(Context r7, String r8, ComponentName r9, PendingIntent r10) {
        this(r7, r8, r9, r10, null);
    }

    public MediaSessionCompat(Context r8, String r9, ComponentName r10, PendingIntent r11, Bundle r12) {
        this(r8, r9, r10, r11, r12, null);
    }

    public MediaSessionCompat(Context r5, String r6, ComponentName r7, PendingIntent r8, Bundle r9, VersionedParcelable r10) {
        this.mActiveListeners = new ArrayList();
        if (r5 == null) goto L35;
        if (TextUtils.isEmpty(r6) == true) goto L33;
        if (r7 != null) goto L10;
        r7 = MediaButtonReceiver.getMediaButtonReceiverComponent(r5);
        if (r7 != null) goto L10;
        Log.w(TAG, "Couldn't find a unique registered media button receiver in the given context.");
    L10:
        if (r7 == null) goto L18;
        if (r8 != null) goto L18;
        Intent r0 = new Intent("android.intent.action.MEDIA_BUTTON");
        r0.setComponent(r7);
        if (Build.VERSION.SDK_INT < 31) goto L15;
        int r1 = 33554432;
    L16:
        r8 = PendingIntent.getBroadcast(r5, 0, r0, r1);
        goto L18
    L15:
        r1 = 0;
    L18:
        if (Build.VERSION.SDK_INT < 29) goto L21;
        this.mImpl = new MediaSessionImplApi29(r5, r6, r10, r9);
    L25:
        if (Looper.myLooper() == null) goto L27;
        Looper r2 = Looper.myLooper();
    L28:
        setCallback(new C00191(this), new Handler(r2));
        this.mImpl.setMediaButtonReceiver(r8);
        this.mController = new MediaControllerCompat(r5, this);
        if (sMaxBitmapSize != 0) goto L36;
        sMaxBitmapSize = (int) (TypedValue.applyDimension(1, 320.0f, r5.getResources().getDisplayMetrics()) + 0.5f);
        return;
    L36:
        return;
    L27:
        r2 = Looper.getMainLooper();
        goto L28
    L21:
        if (Build.VERSION.SDK_INT < 28) goto L23;
        this.mImpl = new MediaSessionImplApi28(r5, r6, r10, r9);
        goto L25
    L23:
        this.mImpl = new MediaSessionImplApi22(r5, r6, r10, r9);
        goto L25
    L33:
        throw new IllegalArgumentException("tag must not be null or empty");
    L35:
        throw new IllegalArgumentException("context must not be null");
    }

    public static void ensureClassLoader(Bundle r1) {
        if (r1 == null) goto L5;
        r1.setClassLoader(MediaSessionCompat.class.getClassLoader());
        return;
    }

    public static MediaSessionCompat fromMediaSession(Context r2, Object r3) {
        if (r2 == null) goto L14;
        if (r3 != null) goto L6;
        return null;
    L6:
        if (Build.VERSION.SDK_INT < 29) goto L9;
        MediaSessionImpl r0 = new MediaSessionImplApi29(r3);
    L13:
        return new MediaSessionCompat(r2, r0);
    L9:
        if (Build.VERSION.SDK_INT < 28) goto L11;
        r0 = new MediaSessionImplApi28(r3);
        goto L13
    L11:
        r0 = new MediaSessionImplApi21(r3);
        goto L13
    L14:
        return null;
    }

    static PlaybackStateCompat getStateWithUpdatedPosition(PlaybackStateCompat r14, MediaMetadataCompat r15) {
        if (r14 != null) goto L4;
    L32:
        return r14;
    L4:
        if (r14.getPosition() == (-1)) goto L32;
        if (r14.getState() != 3) goto L9;
    L12:
        long r0 = r14.getLastPositionUpdateTime();
        if (r0 <= 0) goto L31;
        long r10 = SystemClock.elapsedRealtime();
        long r4 = ((long) (r14.getPlaybackSpeed() * (r10 - r0))) + r14.getPosition();
        if (r15 != null) goto L17;
    L19:
        long r12 = -1;
    L21:
        if (r12 < 0) goto L26;
        if (r4 <= r12) goto L26;
        long r7 = r12;
    L30:
        return new PlaybackStateCompat.Builder(r14).setState(r14.getState(), r7, r14.getPlaybackSpeed(), r10).build();
    L26:
        if (r4 >= 0) goto L28;
        r7 = 0;
        goto L30
    L28:
        r7 = r4;
        goto L30
    L17:
        if (r15.containsKey(MediaMetadataCompat.METADATA_KEY_DURATION) == false) goto L19;
        r12 = r15.getLong(MediaMetadataCompat.METADATA_KEY_DURATION);
    L31:
        return r14;
    L9:
        if (r14.getState() == 4) goto L12;
        if (r14.getState() != 5) goto L31;
        goto L12
    }

    public static Bundle unparcelWithClassLoader(Bundle r4) {
        if (r4 != null) goto L5;
        return null;
    L5:
        ensureClassLoader(r4);
        r4.isEmpty();     // Catch: BadParcelableException -> L8
        return r4;
    L9:
        Log.e(TAG, "Could not unparcel the data.");
        return null;
    }

    public void addOnActiveChangeListener(OnActiveChangeListener r3) {
        if (r3 == null) goto L6;
        this.mActiveListeners.add(r3);
        return;
    L6:
        throw new IllegalArgumentException("Listener may not be null");
    }

    public String getCallingPackage() {
        return this.mImpl.getCallingPackage();
    }

    public MediaControllerCompat getController() {
        return this.mController;
    }

    public final MediaSessionManager.RemoteUserInfo getCurrentControllerInfo() {
        return this.mImpl.getCurrentControllerInfo();
    }

    public Object getMediaSession() {
        return this.mImpl.getMediaSession();
    }

    public Object getRemoteControlClient() {
        return this.mImpl.getRemoteControlClient();
    }

    public Token getSessionToken() {
        return this.mImpl.getSessionToken();
    }

    public boolean isActive() {
        return this.mImpl.isActive();
    }

    public void release() {
        this.mImpl.release();
    }

    public void removeOnActiveChangeListener(OnActiveChangeListener r3) {
        if (r3 == null) goto L6;
        this.mActiveListeners.remove(r3);
        return;
    L6:
        throw new IllegalArgumentException("Listener may not be null");
    }

    public void sendSessionEvent(String r3, Bundle r4) {
        if (TextUtils.isEmpty(r3) == true) goto L7;
        this.mImpl.sendSessionEvent(r3, r4);
        return;
    L7:
        throw new IllegalArgumentException("event cannot be null or empty");
    }

    public void setActive(boolean r3) {
        this.mImpl.setActive(r3);
        Iterator<OnActiveChangeListener> r0 = this.mActiveListeners.iterator();
    L4:
        if (r0.hasNext() == false) goto L6;
        r0.next().onActiveChanged();
        goto L4
    }

    public void setCallback(Callback r2) {
        setCallback(r2, null);
    }

    public void setCallback(Callback r3, Handler r4) {
        if (r3 != null) goto L4;
        this.mImpl.setCallback(null, null);
        return;
    L4:
        MediaSessionImpl r0 = this.mImpl;
        if (r4 == null) goto L7;
        Handler r1 = r4;
    L8:
        r0.setCallback(r3, r1);
        return;
    L7:
        r1 = new Handler();
        goto L8
    }

    public void setCaptioningEnabled(boolean r2) {
        this.mImpl.setCaptioningEnabled(r2);
    }

    public void setExtras(Bundle r2) {
        this.mImpl.setExtras(r2);
    }

    public void setFlags(int r2) {
        this.mImpl.setFlags(r2);
    }

    public void setMediaButtonReceiver(PendingIntent r2) {
        this.mImpl.setMediaButtonReceiver(r2);
    }

    public void setMetadata(MediaMetadataCompat r2) {
        this.mImpl.setMetadata(r2);
    }

    public void setPlaybackState(PlaybackStateCompat r2) {
        this.mImpl.setPlaybackState(r2);
    }

    public void setPlaybackToLocal(int r2) {
        this.mImpl.setPlaybackToLocal(r2);
    }

    public void setPlaybackToRemote(VolumeProviderCompat r3) {
        if (r3 == null) goto L6;
        this.mImpl.setPlaybackToRemote(r3);
        return;
    L6:
        throw new IllegalArgumentException("volumeProvider may not be null!");
    }

    public void setQueue(List<QueueItem> r7) {
        if (r7 == null) goto L14;
        HashSet r0 = new HashSet();
        Iterator<QueueItem> r1 = r7.iterator();
    L5:
        if (r1.hasNext() == false) goto L14;
        QueueItem r2 = r1.next();
        if (r2 == null) goto L13;
        if (r0.contains(Long.valueOf(r2.getQueueId())) == false) goto L11;
        Log.e(TAG, "Found duplicate queue id: " + r2.getQueueId(), new IllegalArgumentException("id of each queue item should be unique"));
    L11:
        r0.add(Long.valueOf(r2.getQueueId()));
        goto L5
    L13:
        throw new IllegalArgumentException("queue shouldn't have null items");
    L14:
        this.mImpl.setQueue(r7);
    }

    public void setQueueTitle(CharSequence r2) {
        this.mImpl.setQueueTitle(r2);
    }

    public void setRatingType(int r2) {
        this.mImpl.setRatingType(r2);
    }

    public void setRegistrationCallback(RegistrationCallback r2, Handler r3) {
        this.mImpl.setRegistrationCallback(r2, r3);
    }

    public void setRepeatMode(int r2) {
        this.mImpl.setRepeatMode(r2);
    }

    public void setSessionActivity(PendingIntent r2) {
        this.mImpl.setSessionActivity(r2);
    }

    public void setShuffleMode(int r2) {
        this.mImpl.setShuffleMode(r2);
    }
}
