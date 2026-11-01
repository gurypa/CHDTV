package android.support.v4.media.session;

import android.app.PendingIntent;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.RatingCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface IMediaSession extends IInterface {
    public static final String DESCRIPTOR = "android.support.v4.media.session.IMediaSession";

    public static class Default implements IMediaSession {
        public Default() {
        }

        @Override // android.support.v4.media.session.IMediaSession
        public void addQueueItem(MediaDescriptionCompat r1) throws RemoteException {
        }

        @Override // android.support.v4.media.session.IMediaSession
        public void addQueueItemAt(MediaDescriptionCompat r1, int r2) throws RemoteException {
        }

        @Override // android.support.v4.media.session.IMediaSession
        public void adjustVolume(int r1, int r2, String r3) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // android.support.v4.media.session.IMediaSession
        public void fastForward() throws RemoteException {
        }

        @Override // android.support.v4.media.session.IMediaSession
        public Bundle getExtras() throws RemoteException {
            return null;
        }

        @Override // android.support.v4.media.session.IMediaSession
        public long getFlags() throws RemoteException {
            return 0;
        }

        @Override // android.support.v4.media.session.IMediaSession
        public PendingIntent getLaunchPendingIntent() throws RemoteException {
            return null;
        }

        @Override // android.support.v4.media.session.IMediaSession
        public MediaMetadataCompat getMetadata() throws RemoteException {
            return null;
        }

        @Override // android.support.v4.media.session.IMediaSession
        public String getPackageName() throws RemoteException {
            return null;
        }

        @Override // android.support.v4.media.session.IMediaSession
        public PlaybackStateCompat getPlaybackState() throws RemoteException {
            return null;
        }

        @Override // android.support.v4.media.session.IMediaSession
        public List<MediaSessionCompat.QueueItem> getQueue() throws RemoteException {
            return null;
        }

        @Override // android.support.v4.media.session.IMediaSession
        public CharSequence getQueueTitle() throws RemoteException {
            return null;
        }

        @Override // android.support.v4.media.session.IMediaSession
        public int getRatingType() throws RemoteException {
            return 0;
        }

        @Override // android.support.v4.media.session.IMediaSession
        public int getRepeatMode() throws RemoteException {
            return 0;
        }

        @Override // android.support.v4.media.session.IMediaSession
        public Bundle getSessionInfo() throws RemoteException {
            return null;
        }

        @Override // android.support.v4.media.session.IMediaSession
        public int getShuffleMode() throws RemoteException {
            return 0;
        }

        @Override // android.support.v4.media.session.IMediaSession
        public String getTag() throws RemoteException {
            return null;
        }

        @Override // android.support.v4.media.session.IMediaSession
        public ParcelableVolumeInfo getVolumeAttributes() throws RemoteException {
            return null;
        }

        @Override // android.support.v4.media.session.IMediaSession
        public boolean isCaptioningEnabled() throws RemoteException {
            return false;
        }

        @Override // android.support.v4.media.session.IMediaSession
        public boolean isShuffleModeEnabledRemoved() throws RemoteException {
            return false;
        }

        @Override // android.support.v4.media.session.IMediaSession
        public boolean isTransportControlEnabled() throws RemoteException {
            return false;
        }

        @Override // android.support.v4.media.session.IMediaSession
        public void next() throws RemoteException {
        }

        @Override // android.support.v4.media.session.IMediaSession
        public void pause() throws RemoteException {
        }

        @Override // android.support.v4.media.session.IMediaSession
        public void play() throws RemoteException {
        }

        @Override // android.support.v4.media.session.IMediaSession
        public void playFromMediaId(String r1, Bundle r2) throws RemoteException {
        }

        @Override // android.support.v4.media.session.IMediaSession
        public void playFromSearch(String r1, Bundle r2) throws RemoteException {
        }

        @Override // android.support.v4.media.session.IMediaSession
        public void playFromUri(Uri r1, Bundle r2) throws RemoteException {
        }

        @Override // android.support.v4.media.session.IMediaSession
        public void prepare() throws RemoteException {
        }

        @Override // android.support.v4.media.session.IMediaSession
        public void prepareFromMediaId(String r1, Bundle r2) throws RemoteException {
        }

        @Override // android.support.v4.media.session.IMediaSession
        public void prepareFromSearch(String r1, Bundle r2) throws RemoteException {
        }

        @Override // android.support.v4.media.session.IMediaSession
        public void prepareFromUri(Uri r1, Bundle r2) throws RemoteException {
        }

        @Override // android.support.v4.media.session.IMediaSession
        public void previous() throws RemoteException {
        }

        @Override // android.support.v4.media.session.IMediaSession
        public void rate(RatingCompat r1) throws RemoteException {
        }

        @Override // android.support.v4.media.session.IMediaSession
        public void rateWithExtras(RatingCompat r1, Bundle r2) throws RemoteException {
        }

        @Override // android.support.v4.media.session.IMediaSession
        public void registerCallbackListener(IMediaControllerCallback r1) throws RemoteException {
        }

        @Override // android.support.v4.media.session.IMediaSession
        public void removeQueueItem(MediaDescriptionCompat r1) throws RemoteException {
        }

        @Override // android.support.v4.media.session.IMediaSession
        public void removeQueueItemAt(int r1) throws RemoteException {
        }

        @Override // android.support.v4.media.session.IMediaSession
        public void rewind() throws RemoteException {
        }

        @Override // android.support.v4.media.session.IMediaSession
        public void seekTo(long r1) throws RemoteException {
        }

        @Override // android.support.v4.media.session.IMediaSession
        public void sendCommand(String r1, Bundle r2, MediaSessionCompat.ResultReceiverWrapper r3) throws RemoteException {
        }

        @Override // android.support.v4.media.session.IMediaSession
        public void sendCustomAction(String r1, Bundle r2) throws RemoteException {
        }

        @Override // android.support.v4.media.session.IMediaSession
        public boolean sendMediaButton(KeyEvent r2) throws RemoteException {
            return false;
        }

        @Override // android.support.v4.media.session.IMediaSession
        public void setCaptioningEnabled(boolean r1) throws RemoteException {
        }

        @Override // android.support.v4.media.session.IMediaSession
        public void setPlaybackSpeed(float r1) throws RemoteException {
        }

        @Override // android.support.v4.media.session.IMediaSession
        public void setRepeatMode(int r1) throws RemoteException {
        }

        @Override // android.support.v4.media.session.IMediaSession
        public void setShuffleMode(int r1) throws RemoteException {
        }

        @Override // android.support.v4.media.session.IMediaSession
        public void setShuffleModeEnabledRemoved(boolean r1) throws RemoteException {
        }

        @Override // android.support.v4.media.session.IMediaSession
        public void setVolumeTo(int r1, int r2, String r3) throws RemoteException {
        }

        @Override // android.support.v4.media.session.IMediaSession
        public void skipToQueueItem(long r1) throws RemoteException {
        }

        @Override // android.support.v4.media.session.IMediaSession
        public void stop() throws RemoteException {
        }

        @Override // android.support.v4.media.session.IMediaSession
        public void unregisterCallbackListener(IMediaControllerCallback r1) throws RemoteException {
        }
    }

    public static abstract class Stub extends Binder implements IMediaSession {
        static final int TRANSACTION_addQueueItem = 41;
        static final int TRANSACTION_addQueueItemAt = 42;
        static final int TRANSACTION_adjustVolume = 11;
        static final int TRANSACTION_fastForward = 22;
        static final int TRANSACTION_getExtras = 31;
        static final int TRANSACTION_getFlags = 9;
        static final int TRANSACTION_getLaunchPendingIntent = 8;
        static final int TRANSACTION_getMetadata = 27;
        static final int TRANSACTION_getPackageName = 6;
        static final int TRANSACTION_getPlaybackState = 28;
        static final int TRANSACTION_getQueue = 29;
        static final int TRANSACTION_getQueueTitle = 30;
        static final int TRANSACTION_getRatingType = 32;
        static final int TRANSACTION_getRepeatMode = 37;
        static final int TRANSACTION_getSessionInfo = 50;
        static final int TRANSACTION_getShuffleMode = 47;
        static final int TRANSACTION_getTag = 7;
        static final int TRANSACTION_getVolumeAttributes = 10;
        static final int TRANSACTION_isCaptioningEnabled = 45;
        static final int TRANSACTION_isShuffleModeEnabledRemoved = 38;
        static final int TRANSACTION_isTransportControlEnabled = 5;
        static final int TRANSACTION_next = 20;
        static final int TRANSACTION_pause = 18;
        static final int TRANSACTION_play = 13;
        static final int TRANSACTION_playFromMediaId = 14;
        static final int TRANSACTION_playFromSearch = 15;
        static final int TRANSACTION_playFromUri = 16;
        static final int TRANSACTION_prepare = 33;
        static final int TRANSACTION_prepareFromMediaId = 34;
        static final int TRANSACTION_prepareFromSearch = 35;
        static final int TRANSACTION_prepareFromUri = 36;
        static final int TRANSACTION_previous = 21;
        static final int TRANSACTION_rate = 25;
        static final int TRANSACTION_rateWithExtras = 51;
        static final int TRANSACTION_registerCallbackListener = 3;
        static final int TRANSACTION_removeQueueItem = 43;
        static final int TRANSACTION_removeQueueItemAt = 44;
        static final int TRANSACTION_rewind = 23;
        static final int TRANSACTION_seekTo = 24;
        static final int TRANSACTION_sendCommand = 1;
        static final int TRANSACTION_sendCustomAction = 26;
        static final int TRANSACTION_sendMediaButton = 2;
        static final int TRANSACTION_setCaptioningEnabled = 46;
        static final int TRANSACTION_setPlaybackSpeed = 49;
        static final int TRANSACTION_setRepeatMode = 39;
        static final int TRANSACTION_setShuffleMode = 48;
        static final int TRANSACTION_setShuffleModeEnabledRemoved = 40;
        static final int TRANSACTION_setVolumeTo = 12;
        static final int TRANSACTION_skipToQueueItem = 17;
        static final int TRANSACTION_stop = 19;
        static final int TRANSACTION_unregisterCallbackListener = 4;

        private static class Proxy implements IMediaSession {
            private IBinder mRemote;

            Proxy(IBinder r1) {
                this.mRemote = r1;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void addQueueItem(MediaDescriptionCompat r6) throws RemoteException {
                Parcel r0 = Parcel.obtain();
                Parcel r1 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaSession.DESCRIPTOR);     // Catch: Throwable -> L6
                _Parcel.access$100(r0, r6, 0);     // Catch: Throwable -> L6
                this.mRemote.transact(41, r0, r1, 0);     // Catch: Throwable -> L6
                r1.readException();     // Catch: Throwable -> L6
                r1.recycle();
                r0.recycle();
                return;
            L6:
                th = move-exception;
                r1.recycle();
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void addQueueItemAt(MediaDescriptionCompat r6, int r7) throws RemoteException {
                Parcel r0 = Parcel.obtain();
                Parcel r1 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaSession.DESCRIPTOR);     // Catch: Throwable -> L6
                _Parcel.access$100(r0, r6, 0);     // Catch: Throwable -> L6
                r0.writeInt(r7);     // Catch: Throwable -> L6
                this.mRemote.transact(42, r0, r1, 0);     // Catch: Throwable -> L6
                r1.readException();     // Catch: Throwable -> L6
                r1.recycle();
                r0.recycle();
                return;
            L6:
                th = move-exception;
                r1.recycle();
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void adjustVolume(int r6, int r7, String r8) throws RemoteException {
                Parcel r0 = Parcel.obtain();
                Parcel r1 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaSession.DESCRIPTOR);     // Catch: Throwable -> L6
                r0.writeInt(r6);     // Catch: Throwable -> L6
                r0.writeInt(r7);     // Catch: Throwable -> L6
                r0.writeString(r8);     // Catch: Throwable -> L6
                this.mRemote.transact(11, r0, r1, 0);     // Catch: Throwable -> L6
                r1.readException();     // Catch: Throwable -> L6
                r1.recycle();
                r0.recycle();
                return;
            L6:
                th = move-exception;
                r1.recycle();
                r0.recycle();
                throw th;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void fastForward() throws RemoteException {
                Parcel r0 = Parcel.obtain();
                Parcel r1 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaSession.DESCRIPTOR);     // Catch: Throwable -> L6
                this.mRemote.transact(22, r0, r1, 0);     // Catch: Throwable -> L6
                r1.readException();     // Catch: Throwable -> L6
                r1.recycle();
                r0.recycle();
                return;
            L6:
                th = move-exception;
                r1.recycle();
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public Bundle getExtras() throws RemoteException {
                Parcel r0 = Parcel.obtain();
                Parcel r1 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaSession.DESCRIPTOR);     // Catch: Throwable -> L6
                this.mRemote.transact(31, r0, r1, 0);     // Catch: Throwable -> L6
                r1.readException();     // Catch: Throwable -> L6
                Bundle r3 = (Bundle) _Parcel.access$000(r1, Bundle.CREATOR);     // Catch: Throwable -> L6
                r1.recycle();
                r0.recycle();
                return r3;
            L6:
                th = move-exception;
                r1.recycle();
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public long getFlags() throws RemoteException {
                Parcel r0 = Parcel.obtain();
                Parcel r1 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaSession.DESCRIPTOR);     // Catch: Throwable -> L6
                this.mRemote.transact(9, r0, r1, 0);     // Catch: Throwable -> L6
                r1.readException();     // Catch: Throwable -> L6
                long r3 = r1.readLong();     // Catch: Throwable -> L6
                r1.recycle();
                r0.recycle();
                return r3;
            L6:
                th = move-exception;
                r1.recycle();
                r0.recycle();
                throw th;
            }

            public String getInterfaceDescriptor() {
                return IMediaSession.DESCRIPTOR;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public PendingIntent getLaunchPendingIntent() throws RemoteException {
                Parcel r0 = Parcel.obtain();
                Parcel r1 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaSession.DESCRIPTOR);     // Catch: Throwable -> L6
                this.mRemote.transact(8, r0, r1, 0);     // Catch: Throwable -> L6
                r1.readException();     // Catch: Throwable -> L6
                PendingIntent r3 = (PendingIntent) _Parcel.access$000(r1, PendingIntent.CREATOR);     // Catch: Throwable -> L6
                r1.recycle();
                r0.recycle();
                return r3;
            L6:
                th = move-exception;
                r1.recycle();
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public MediaMetadataCompat getMetadata() throws RemoteException {
                Parcel r0 = Parcel.obtain();
                Parcel r1 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaSession.DESCRIPTOR);     // Catch: Throwable -> L6
                this.mRemote.transact(27, r0, r1, 0);     // Catch: Throwable -> L6
                r1.readException();     // Catch: Throwable -> L6
                MediaMetadataCompat r3 = (MediaMetadataCompat) _Parcel.access$000(r1, MediaMetadataCompat.CREATOR);     // Catch: Throwable -> L6
                r1.recycle();
                r0.recycle();
                return r3;
            L6:
                th = move-exception;
                r1.recycle();
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public String getPackageName() throws RemoteException {
                Parcel r0 = Parcel.obtain();
                Parcel r1 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaSession.DESCRIPTOR);     // Catch: Throwable -> L6
                this.mRemote.transact(6, r0, r1, 0);     // Catch: Throwable -> L6
                r1.readException();     // Catch: Throwable -> L6
                String r3 = r1.readString();     // Catch: Throwable -> L6
                r1.recycle();
                r0.recycle();
                return r3;
            L6:
                th = move-exception;
                r1.recycle();
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public PlaybackStateCompat getPlaybackState() throws RemoteException {
                Parcel r0 = Parcel.obtain();
                Parcel r1 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaSession.DESCRIPTOR);     // Catch: Throwable -> L6
                this.mRemote.transact(28, r0, r1, 0);     // Catch: Throwable -> L6
                r1.readException();     // Catch: Throwable -> L6
                PlaybackStateCompat r3 = (PlaybackStateCompat) _Parcel.access$000(r1, PlaybackStateCompat.CREATOR);     // Catch: Throwable -> L6
                r1.recycle();
                r0.recycle();
                return r3;
            L6:
                th = move-exception;
                r1.recycle();
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public List<MediaSessionCompat.QueueItem> getQueue() throws RemoteException {
                Parcel r0 = Parcel.obtain();
                Parcel r1 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaSession.DESCRIPTOR);     // Catch: Throwable -> L6
                this.mRemote.transact(29, r0, r1, 0);     // Catch: Throwable -> L6
                r1.readException();     // Catch: Throwable -> L6
                ArrayList r3 = r1.createTypedArrayList(MediaSessionCompat.QueueItem.CREATOR);     // Catch: Throwable -> L6
                r1.recycle();
                r0.recycle();
                return r3;
            L6:
                th = move-exception;
                r1.recycle();
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public CharSequence getQueueTitle() throws RemoteException {
                Parcel r0 = Parcel.obtain();
                Parcel r1 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaSession.DESCRIPTOR);     // Catch: Throwable -> L6
                this.mRemote.transact(30, r0, r1, 0);     // Catch: Throwable -> L6
                r1.readException();     // Catch: Throwable -> L6
                CharSequence r3 = (CharSequence) _Parcel.access$000(r1, TextUtils.CHAR_SEQUENCE_CREATOR);     // Catch: Throwable -> L6
                r1.recycle();
                r0.recycle();
                return r3;
            L6:
                th = move-exception;
                r1.recycle();
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public int getRatingType() throws RemoteException {
                Parcel r0 = Parcel.obtain();
                Parcel r1 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaSession.DESCRIPTOR);     // Catch: Throwable -> L6
                this.mRemote.transact(32, r0, r1, 0);     // Catch: Throwable -> L6
                r1.readException();     // Catch: Throwable -> L6
                int r3 = r1.readInt();     // Catch: Throwable -> L6
                r1.recycle();
                r0.recycle();
                return r3;
            L6:
                th = move-exception;
                r1.recycle();
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public int getRepeatMode() throws RemoteException {
                Parcel r0 = Parcel.obtain();
                Parcel r1 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaSession.DESCRIPTOR);     // Catch: Throwable -> L6
                this.mRemote.transact(37, r0, r1, 0);     // Catch: Throwable -> L6
                r1.readException();     // Catch: Throwable -> L6
                int r3 = r1.readInt();     // Catch: Throwable -> L6
                r1.recycle();
                r0.recycle();
                return r3;
            L6:
                th = move-exception;
                r1.recycle();
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public Bundle getSessionInfo() throws RemoteException {
                Parcel r0 = Parcel.obtain();
                Parcel r1 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaSession.DESCRIPTOR);     // Catch: Throwable -> L6
                this.mRemote.transact(50, r0, r1, 0);     // Catch: Throwable -> L6
                r1.readException();     // Catch: Throwable -> L6
                Bundle r3 = (Bundle) _Parcel.access$000(r1, Bundle.CREATOR);     // Catch: Throwable -> L6
                r1.recycle();
                r0.recycle();
                return r3;
            L6:
                th = move-exception;
                r1.recycle();
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public int getShuffleMode() throws RemoteException {
                Parcel r0 = Parcel.obtain();
                Parcel r1 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaSession.DESCRIPTOR);     // Catch: Throwable -> L6
                this.mRemote.transact(47, r0, r1, 0);     // Catch: Throwable -> L6
                r1.readException();     // Catch: Throwable -> L6
                int r3 = r1.readInt();     // Catch: Throwable -> L6
                r1.recycle();
                r0.recycle();
                return r3;
            L6:
                th = move-exception;
                r1.recycle();
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public String getTag() throws RemoteException {
                Parcel r0 = Parcel.obtain();
                Parcel r1 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaSession.DESCRIPTOR);     // Catch: Throwable -> L6
                this.mRemote.transact(7, r0, r1, 0);     // Catch: Throwable -> L6
                r1.readException();     // Catch: Throwable -> L6
                String r3 = r1.readString();     // Catch: Throwable -> L6
                r1.recycle();
                r0.recycle();
                return r3;
            L6:
                th = move-exception;
                r1.recycle();
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public ParcelableVolumeInfo getVolumeAttributes() throws RemoteException {
                Parcel r0 = Parcel.obtain();
                Parcel r1 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaSession.DESCRIPTOR);     // Catch: Throwable -> L6
                this.mRemote.transact(10, r0, r1, 0);     // Catch: Throwable -> L6
                r1.readException();     // Catch: Throwable -> L6
                ParcelableVolumeInfo r3 = (ParcelableVolumeInfo) _Parcel.access$000(r1, ParcelableVolumeInfo.CREATOR);     // Catch: Throwable -> L6
                r1.recycle();
                r0.recycle();
                return r3;
            L6:
                th = move-exception;
                r1.recycle();
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public boolean isCaptioningEnabled() throws RemoteException {
                Parcel r0 = Parcel.obtain();
                Parcel r1 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaSession.DESCRIPTOR);     // Catch: Throwable -> L8
                boolean r4 = false;
                this.mRemote.transact(45, r0, r1, 0);     // Catch: Throwable -> L8
                r1.readException();     // Catch: Throwable -> L8
                if (r1.readInt() == 0) goto L6;
                r4 = true;
            L6:
                r1.recycle();
                r0.recycle();
                return r4;
            L8:
                th = move-exception;
                r1.recycle();
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public boolean isShuffleModeEnabledRemoved() throws RemoteException {
                Parcel r0 = Parcel.obtain();
                Parcel r1 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaSession.DESCRIPTOR);     // Catch: Throwable -> L8
                boolean r4 = false;
                this.mRemote.transact(38, r0, r1, 0);     // Catch: Throwable -> L8
                r1.readException();     // Catch: Throwable -> L8
                if (r1.readInt() == 0) goto L6;
                r4 = true;
            L6:
                r1.recycle();
                r0.recycle();
                return r4;
            L8:
                th = move-exception;
                r1.recycle();
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public boolean isTransportControlEnabled() throws RemoteException {
                Parcel r0 = Parcel.obtain();
                Parcel r1 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaSession.DESCRIPTOR);     // Catch: Throwable -> L8
                boolean r4 = false;
                this.mRemote.transact(5, r0, r1, 0);     // Catch: Throwable -> L8
                r1.readException();     // Catch: Throwable -> L8
                if (r1.readInt() == 0) goto L6;
                r4 = true;
            L6:
                r1.recycle();
                r0.recycle();
                return r4;
            L8:
                th = move-exception;
                r1.recycle();
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void next() throws RemoteException {
                Parcel r0 = Parcel.obtain();
                Parcel r1 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaSession.DESCRIPTOR);     // Catch: Throwable -> L6
                this.mRemote.transact(20, r0, r1, 0);     // Catch: Throwable -> L6
                r1.readException();     // Catch: Throwable -> L6
                r1.recycle();
                r0.recycle();
                return;
            L6:
                th = move-exception;
                r1.recycle();
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void pause() throws RemoteException {
                Parcel r0 = Parcel.obtain();
                Parcel r1 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaSession.DESCRIPTOR);     // Catch: Throwable -> L6
                this.mRemote.transact(18, r0, r1, 0);     // Catch: Throwable -> L6
                r1.readException();     // Catch: Throwable -> L6
                r1.recycle();
                r0.recycle();
                return;
            L6:
                th = move-exception;
                r1.recycle();
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void play() throws RemoteException {
                Parcel r0 = Parcel.obtain();
                Parcel r1 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaSession.DESCRIPTOR);     // Catch: Throwable -> L6
                this.mRemote.transact(13, r0, r1, 0);     // Catch: Throwable -> L6
                r1.readException();     // Catch: Throwable -> L6
                r1.recycle();
                r0.recycle();
                return;
            L6:
                th = move-exception;
                r1.recycle();
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void playFromMediaId(String r6, Bundle r7) throws RemoteException {
                Parcel r0 = Parcel.obtain();
                Parcel r1 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaSession.DESCRIPTOR);     // Catch: Throwable -> L6
                r0.writeString(r6);     // Catch: Throwable -> L6
                _Parcel.access$100(r0, r7, 0);     // Catch: Throwable -> L6
                this.mRemote.transact(14, r0, r1, 0);     // Catch: Throwable -> L6
                r1.readException();     // Catch: Throwable -> L6
                r1.recycle();
                r0.recycle();
                return;
            L6:
                th = move-exception;
                r1.recycle();
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void playFromSearch(String r6, Bundle r7) throws RemoteException {
                Parcel r0 = Parcel.obtain();
                Parcel r1 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaSession.DESCRIPTOR);     // Catch: Throwable -> L6
                r0.writeString(r6);     // Catch: Throwable -> L6
                _Parcel.access$100(r0, r7, 0);     // Catch: Throwable -> L6
                this.mRemote.transact(15, r0, r1, 0);     // Catch: Throwable -> L6
                r1.readException();     // Catch: Throwable -> L6
                r1.recycle();
                r0.recycle();
                return;
            L6:
                th = move-exception;
                r1.recycle();
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void playFromUri(Uri r6, Bundle r7) throws RemoteException {
                Parcel r0 = Parcel.obtain();
                Parcel r1 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaSession.DESCRIPTOR);     // Catch: Throwable -> L6
                _Parcel.access$100(r0, r6, 0);     // Catch: Throwable -> L6
                _Parcel.access$100(r0, r7, 0);     // Catch: Throwable -> L6
                this.mRemote.transact(16, r0, r1, 0);     // Catch: Throwable -> L6
                r1.readException();     // Catch: Throwable -> L6
                r1.recycle();
                r0.recycle();
                return;
            L6:
                th = move-exception;
                r1.recycle();
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void prepare() throws RemoteException {
                Parcel r0 = Parcel.obtain();
                Parcel r1 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaSession.DESCRIPTOR);     // Catch: Throwable -> L6
                this.mRemote.transact(33, r0, r1, 0);     // Catch: Throwable -> L6
                r1.readException();     // Catch: Throwable -> L6
                r1.recycle();
                r0.recycle();
                return;
            L6:
                th = move-exception;
                r1.recycle();
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void prepareFromMediaId(String r6, Bundle r7) throws RemoteException {
                Parcel r0 = Parcel.obtain();
                Parcel r1 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaSession.DESCRIPTOR);     // Catch: Throwable -> L6
                r0.writeString(r6);     // Catch: Throwable -> L6
                _Parcel.access$100(r0, r7, 0);     // Catch: Throwable -> L6
                this.mRemote.transact(34, r0, r1, 0);     // Catch: Throwable -> L6
                r1.readException();     // Catch: Throwable -> L6
                r1.recycle();
                r0.recycle();
                return;
            L6:
                th = move-exception;
                r1.recycle();
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void prepareFromSearch(String r6, Bundle r7) throws RemoteException {
                Parcel r0 = Parcel.obtain();
                Parcel r1 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaSession.DESCRIPTOR);     // Catch: Throwable -> L6
                r0.writeString(r6);     // Catch: Throwable -> L6
                _Parcel.access$100(r0, r7, 0);     // Catch: Throwable -> L6
                this.mRemote.transact(35, r0, r1, 0);     // Catch: Throwable -> L6
                r1.readException();     // Catch: Throwable -> L6
                r1.recycle();
                r0.recycle();
                return;
            L6:
                th = move-exception;
                r1.recycle();
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void prepareFromUri(Uri r6, Bundle r7) throws RemoteException {
                Parcel r0 = Parcel.obtain();
                Parcel r1 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaSession.DESCRIPTOR);     // Catch: Throwable -> L6
                _Parcel.access$100(r0, r6, 0);     // Catch: Throwable -> L6
                _Parcel.access$100(r0, r7, 0);     // Catch: Throwable -> L6
                this.mRemote.transact(36, r0, r1, 0);     // Catch: Throwable -> L6
                r1.readException();     // Catch: Throwable -> L6
                r1.recycle();
                r0.recycle();
                return;
            L6:
                th = move-exception;
                r1.recycle();
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void previous() throws RemoteException {
                Parcel r0 = Parcel.obtain();
                Parcel r1 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaSession.DESCRIPTOR);     // Catch: Throwable -> L6
                this.mRemote.transact(21, r0, r1, 0);     // Catch: Throwable -> L6
                r1.readException();     // Catch: Throwable -> L6
                r1.recycle();
                r0.recycle();
                return;
            L6:
                th = move-exception;
                r1.recycle();
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void rate(RatingCompat r6) throws RemoteException {
                Parcel r0 = Parcel.obtain();
                Parcel r1 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaSession.DESCRIPTOR);     // Catch: Throwable -> L6
                _Parcel.access$100(r0, r6, 0);     // Catch: Throwable -> L6
                this.mRemote.transact(25, r0, r1, 0);     // Catch: Throwable -> L6
                r1.readException();     // Catch: Throwable -> L6
                r1.recycle();
                r0.recycle();
                return;
            L6:
                th = move-exception;
                r1.recycle();
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void rateWithExtras(RatingCompat r6, Bundle r7) throws RemoteException {
                Parcel r0 = Parcel.obtain();
                Parcel r1 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaSession.DESCRIPTOR);     // Catch: Throwable -> L6
                _Parcel.access$100(r0, r6, 0);     // Catch: Throwable -> L6
                _Parcel.access$100(r0, r7, 0);     // Catch: Throwable -> L6
                this.mRemote.transact(Stub.TRANSACTION_rateWithExtras, r0, r1, 0);     // Catch: Throwable -> L6
                r1.readException();     // Catch: Throwable -> L6
                r1.recycle();
                r0.recycle();
                return;
            L6:
                th = move-exception;
                r1.recycle();
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void registerCallbackListener(IMediaControllerCallback r6) throws RemoteException {
                Parcel r0 = Parcel.obtain();
                Parcel r1 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaSession.DESCRIPTOR);     // Catch: Throwable -> L6
                r0.writeStrongInterface(r6);     // Catch: Throwable -> L6
                this.mRemote.transact(3, r0, r1, 0);     // Catch: Throwable -> L6
                r1.readException();     // Catch: Throwable -> L6
                r1.recycle();
                r0.recycle();
                return;
            L6:
                th = move-exception;
                r1.recycle();
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void removeQueueItem(MediaDescriptionCompat r6) throws RemoteException {
                Parcel r0 = Parcel.obtain();
                Parcel r1 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaSession.DESCRIPTOR);     // Catch: Throwable -> L6
                _Parcel.access$100(r0, r6, 0);     // Catch: Throwable -> L6
                this.mRemote.transact(43, r0, r1, 0);     // Catch: Throwable -> L6
                r1.readException();     // Catch: Throwable -> L6
                r1.recycle();
                r0.recycle();
                return;
            L6:
                th = move-exception;
                r1.recycle();
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void removeQueueItemAt(int r6) throws RemoteException {
                Parcel r0 = Parcel.obtain();
                Parcel r1 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaSession.DESCRIPTOR);     // Catch: Throwable -> L6
                r0.writeInt(r6);     // Catch: Throwable -> L6
                this.mRemote.transact(44, r0, r1, 0);     // Catch: Throwable -> L6
                r1.readException();     // Catch: Throwable -> L6
                r1.recycle();
                r0.recycle();
                return;
            L6:
                th = move-exception;
                r1.recycle();
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void rewind() throws RemoteException {
                Parcel r0 = Parcel.obtain();
                Parcel r1 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaSession.DESCRIPTOR);     // Catch: Throwable -> L6
                this.mRemote.transact(23, r0, r1, 0);     // Catch: Throwable -> L6
                r1.readException();     // Catch: Throwable -> L6
                r1.recycle();
                r0.recycle();
                return;
            L6:
                th = move-exception;
                r1.recycle();
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void seekTo(long r6) throws RemoteException {
                Parcel r0 = Parcel.obtain();
                Parcel r1 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaSession.DESCRIPTOR);     // Catch: Throwable -> L6
                r0.writeLong(r6);     // Catch: Throwable -> L6
                this.mRemote.transact(24, r0, r1, 0);     // Catch: Throwable -> L6
                r1.readException();     // Catch: Throwable -> L6
                r1.recycle();
                r0.recycle();
                return;
            L6:
                th = move-exception;
                r1.recycle();
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void sendCommand(String r6, Bundle r7, MediaSessionCompat.ResultReceiverWrapper r8) throws RemoteException {
                Parcel r0 = Parcel.obtain();
                Parcel r1 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaSession.DESCRIPTOR);     // Catch: Throwable -> L6
                r0.writeString(r6);     // Catch: Throwable -> L6
                _Parcel.access$100(r0, r7, 0);     // Catch: Throwable -> L6
                _Parcel.access$100(r0, r8, 0);     // Catch: Throwable -> L6
                this.mRemote.transact(1, r0, r1, 0);     // Catch: Throwable -> L6
                r1.readException();     // Catch: Throwable -> L6
                r1.recycle();
                r0.recycle();
                return;
            L6:
                th = move-exception;
                r1.recycle();
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void sendCustomAction(String r6, Bundle r7) throws RemoteException {
                Parcel r0 = Parcel.obtain();
                Parcel r1 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaSession.DESCRIPTOR);     // Catch: Throwable -> L6
                r0.writeString(r6);     // Catch: Throwable -> L6
                _Parcel.access$100(r0, r7, 0);     // Catch: Throwable -> L6
                this.mRemote.transact(26, r0, r1, 0);     // Catch: Throwable -> L6
                r1.readException();     // Catch: Throwable -> L6
                r1.recycle();
                r0.recycle();
                return;
            L6:
                th = move-exception;
                r1.recycle();
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public boolean sendMediaButton(KeyEvent r6) throws RemoteException {
                Parcel r0 = Parcel.obtain();
                Parcel r1 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaSession.DESCRIPTOR);     // Catch: Throwable -> L8
                boolean r2 = false;
                _Parcel.access$100(r0, r6, 0);     // Catch: Throwable -> L8
                this.mRemote.transact(2, r0, r1, 0);     // Catch: Throwable -> L8
                r1.readException();     // Catch: Throwable -> L8
                if (r1.readInt() == 0) goto L6;
                r2 = true;
            L6:
                r1.recycle();
                r0.recycle();
                return r2;
            L8:
                th = move-exception;
                r1.recycle();
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void setCaptioningEnabled(boolean r6) throws RemoteException {
                Parcel r0 = Parcel.obtain();
                Parcel r1 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaSession.DESCRIPTOR);     // Catch: Throwable -> L10
                if (r6 == false) goto L6;
                int r3 = 1;
            L7:
                r0.writeInt(r3);     // Catch: Throwable -> L10
                this.mRemote.transact(46, r0, r1, 0);     // Catch: Throwable -> L10
                r1.readException();     // Catch: Throwable -> L10
                r1.recycle();
                r0.recycle();
                return;
            L6:
                r3 = 0;
            L10:
                th = move-exception;
                r1.recycle();
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void setPlaybackSpeed(float r6) throws RemoteException {
                Parcel r0 = Parcel.obtain();
                Parcel r1 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaSession.DESCRIPTOR);     // Catch: Throwable -> L6
                r0.writeFloat(r6);     // Catch: Throwable -> L6
                this.mRemote.transact(Stub.TRANSACTION_setPlaybackSpeed, r0, r1, 0);     // Catch: Throwable -> L6
                r1.readException();     // Catch: Throwable -> L6
                r1.recycle();
                r0.recycle();
                return;
            L6:
                th = move-exception;
                r1.recycle();
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void setRepeatMode(int r6) throws RemoteException {
                Parcel r0 = Parcel.obtain();
                Parcel r1 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaSession.DESCRIPTOR);     // Catch: Throwable -> L6
                r0.writeInt(r6);     // Catch: Throwable -> L6
                this.mRemote.transact(39, r0, r1, 0);     // Catch: Throwable -> L6
                r1.readException();     // Catch: Throwable -> L6
                r1.recycle();
                r0.recycle();
                return;
            L6:
                th = move-exception;
                r1.recycle();
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void setShuffleMode(int r6) throws RemoteException {
                Parcel r0 = Parcel.obtain();
                Parcel r1 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaSession.DESCRIPTOR);     // Catch: Throwable -> L6
                r0.writeInt(r6);     // Catch: Throwable -> L6
                this.mRemote.transact(Stub.TRANSACTION_setShuffleMode, r0, r1, 0);     // Catch: Throwable -> L6
                r1.readException();     // Catch: Throwable -> L6
                r1.recycle();
                r0.recycle();
                return;
            L6:
                th = move-exception;
                r1.recycle();
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void setShuffleModeEnabledRemoved(boolean r6) throws RemoteException {
                Parcel r0 = Parcel.obtain();
                Parcel r1 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaSession.DESCRIPTOR);     // Catch: Throwable -> L10
                if (r6 == false) goto L6;
                int r3 = 1;
            L7:
                r0.writeInt(r3);     // Catch: Throwable -> L10
                this.mRemote.transact(40, r0, r1, 0);     // Catch: Throwable -> L10
                r1.readException();     // Catch: Throwable -> L10
                r1.recycle();
                r0.recycle();
                return;
            L6:
                r3 = 0;
            L10:
                th = move-exception;
                r1.recycle();
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void setVolumeTo(int r6, int r7, String r8) throws RemoteException {
                Parcel r0 = Parcel.obtain();
                Parcel r1 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaSession.DESCRIPTOR);     // Catch: Throwable -> L6
                r0.writeInt(r6);     // Catch: Throwable -> L6
                r0.writeInt(r7);     // Catch: Throwable -> L6
                r0.writeString(r8);     // Catch: Throwable -> L6
                this.mRemote.transact(12, r0, r1, 0);     // Catch: Throwable -> L6
                r1.readException();     // Catch: Throwable -> L6
                r1.recycle();
                r0.recycle();
                return;
            L6:
                th = move-exception;
                r1.recycle();
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void skipToQueueItem(long r6) throws RemoteException {
                Parcel r0 = Parcel.obtain();
                Parcel r1 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaSession.DESCRIPTOR);     // Catch: Throwable -> L6
                r0.writeLong(r6);     // Catch: Throwable -> L6
                this.mRemote.transact(17, r0, r1, 0);     // Catch: Throwable -> L6
                r1.readException();     // Catch: Throwable -> L6
                r1.recycle();
                r0.recycle();
                return;
            L6:
                th = move-exception;
                r1.recycle();
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void stop() throws RemoteException {
                Parcel r0 = Parcel.obtain();
                Parcel r1 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaSession.DESCRIPTOR);     // Catch: Throwable -> L6
                this.mRemote.transact(19, r0, r1, 0);     // Catch: Throwable -> L6
                r1.readException();     // Catch: Throwable -> L6
                r1.recycle();
                r0.recycle();
                return;
            L6:
                th = move-exception;
                r1.recycle();
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaSession
            public void unregisterCallbackListener(IMediaControllerCallback r6) throws RemoteException {
                Parcel r0 = Parcel.obtain();
                Parcel r1 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaSession.DESCRIPTOR);     // Catch: Throwable -> L6
                r0.writeStrongInterface(r6);     // Catch: Throwable -> L6
                this.mRemote.transact(4, r0, r1, 0);     // Catch: Throwable -> L6
                r1.readException();     // Catch: Throwable -> L6
                r1.recycle();
                r0.recycle();
                return;
            L6:
                th = move-exception;
                r1.recycle();
                r0.recycle();
                throw th;
            }
        }

        public Stub() {
            attachInterface(this, IMediaSession.DESCRIPTOR);
        }

        public static IMediaSession asInterface(IBinder r2) {
            if (r2 != null) goto L5;
            return null;
        L5:
            IInterface r0 = r2.queryLocalInterface(IMediaSession.DESCRIPTOR);
            if (r0 == null) goto L12;
            if ((r0 instanceof IMediaSession) == false) goto L12;
            return (IMediaSession) r0;
        L12:
            return new Proxy(r2);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int r6, Parcel r7, Parcel r8, int r9) throws RemoteException {
            if (r6 < 1) goto L8;
            if (r6 > 16777215) goto L8;
            r7.enforceInterface(IMediaSession.DESCRIPTOR);
        L8:
            if (r6 != 1598968902) goto L11;
            r8.writeString(IMediaSession.DESCRIPTOR);
            return true;
        L11:
            boolean r2 = false;
            switch(r6) {
                case 1: goto L74;
                case 2: goto L73;
                case 3: goto L72;
                case 4: goto L71;
                case 5: goto L70;
                case 6: goto L69;
                case 7: goto L68;
                case 8: goto L67;
                case 9: goto L66;
                case 10: goto L65;
                case 11: goto L64;
                case 12: goto L63;
                case 13: goto L62;
                case 14: goto L61;
                case 15: goto L60;
                case 16: goto L59;
                case 17: goto L58;
                case 18: goto L57;
                case 19: goto L56;
                case 20: goto L55;
                case 21: goto L54;
                case 22: goto L53;
                case 23: goto L52;
                case 24: goto L51;
                case 25: goto L50;
                case 26: goto L49;
                case 27: goto L48;
                case 28: goto L47;
                case 29: goto L46;
                case 30: goto L42;
                case 31: goto L41;
                case 32: goto L40;
                case 33: goto L39;
                case 34: goto L38;
                case 35: goto L37;
                case 36: goto L36;
                case 37: goto L35;
                case 38: goto L34;
                case 39: goto L33;
                case 40: goto L30;
                case 41: goto L28;
                case 42: goto L27;
                case 43: goto L26;
                case 44: goto L25;
                case 45: goto L24;
                case 46: goto L21;
                case 47: goto L19;
                case 48: goto L18;
                case 49: goto L17;
                case 50: goto L16;
                case 51: goto L15;
                default: goto L14;
            };
        L15:
            rateWithExtras((RatingCompat) _Parcel.access$000(r7, RatingCompat.CREATOR), (Bundle) _Parcel.access$000(r7, Bundle.CREATOR));
            r8.writeNoException();
        L75:
            return true;
        L16:
            Bundle r3 = getSessionInfo();
            r8.writeNoException();
            _Parcel.access$100(r8, r3, 1);
            goto L75
        L17:
            setPlaybackSpeed(r7.readFloat());
            r8.writeNoException();
            goto L75
        L18:
            setShuffleMode(r7.readInt());
            r8.writeNoException();
            goto L75
        L19:
            int r4 = getShuffleMode();
            r8.writeNoException();
            r8.writeInt(r4);
            goto L75
        L24:
            boolean r5 = isCaptioningEnabled();
            r8.writeNoException();
            r8.writeInt(r5 ? 1 : 0);
            goto L75
        L25:
            removeQueueItemAt(r7.readInt());
            r8.writeNoException();
            goto L75
        L26:
            removeQueueItem((MediaDescriptionCompat) _Parcel.access$000(r7, MediaDescriptionCompat.CREATOR));
            r8.writeNoException();
            goto L75
        L27:
            addQueueItemAt((MediaDescriptionCompat) _Parcel.access$000(r7, MediaDescriptionCompat.CREATOR), r7.readInt());
            r8.writeNoException();
            goto L75
        L28:
            addQueueItem((MediaDescriptionCompat) _Parcel.access$000(r7, MediaDescriptionCompat.CREATOR));
            r8.writeNoException();
            goto L75
        L33:
            setRepeatMode(r7.readInt());
            r8.writeNoException();
            goto L75
        L34:
            boolean r10 = isShuffleModeEnabledRemoved();
            r8.writeNoException();
            r8.writeInt(r10 ? 1 : 0);
            goto L75
        L35:
            int r11 = getRepeatMode();
            r8.writeNoException();
            r8.writeInt(r11);
            goto L75
        L36:
            prepareFromUri((Uri) _Parcel.access$000(r7, Uri.CREATOR), (Bundle) _Parcel.access$000(r7, Bundle.CREATOR));
            r8.writeNoException();
            goto L75
        L37:
            prepareFromSearch(r7.readString(), (Bundle) _Parcel.access$000(r7, Bundle.CREATOR));
            r8.writeNoException();
            goto L75
        L38:
            prepareFromMediaId(r7.readString(), (Bundle) _Parcel.access$000(r7, Bundle.CREATOR));
            r8.writeNoException();
            goto L75
        L39:
            prepare();
            r8.writeNoException();
            goto L75
        L40:
            int r12 = getRatingType();
            r8.writeNoException();
            r8.writeInt(r12);
            goto L75
        L41:
            Bundle r13 = getExtras();
            r8.writeNoException();
            _Parcel.access$100(r8, r13, 1);
            goto L75
        L42:
            CharSequence r14 = getQueueTitle();
            r8.writeNoException();
            if (r14 == null) goto L45;
            r8.writeInt(1);
            TextUtils.writeToParcel(r14, r8, 1);
            goto L75
        L45:
            r8.writeInt(0);
            goto L75
        L46:
            List<MediaSessionCompat.QueueItem> r15 = getQueue();
            r8.writeNoException();
            _Parcel.access$200(r8, r15, 1);
            goto L75
        L47:
            PlaybackStateCompat r16 = getPlaybackState();
            r8.writeNoException();
            _Parcel.access$100(r8, r16, 1);
            goto L75
        L48:
            MediaMetadataCompat r17 = getMetadata();
            r8.writeNoException();
            _Parcel.access$100(r8, r17, 1);
            goto L75
        L49:
            sendCustomAction(r7.readString(), (Bundle) _Parcel.access$000(r7, Bundle.CREATOR));
            r8.writeNoException();
            goto L75
        L50:
            rate((RatingCompat) _Parcel.access$000(r7, RatingCompat.CREATOR));
            r8.writeNoException();
            goto L75
        L51:
            seekTo(r7.readLong());
            r8.writeNoException();
            goto L75
        L52:
            rewind();
            r8.writeNoException();
            goto L75
        L53:
            fastForward();
            r8.writeNoException();
            goto L75
        L54:
            previous();
            r8.writeNoException();
            goto L75
        L55:
            next();
            r8.writeNoException();
            goto L75
        L56:
            stop();
            r8.writeNoException();
            goto L75
        L57:
            pause();
            r8.writeNoException();
            goto L75
        L58:
            skipToQueueItem(r7.readLong());
            r8.writeNoException();
            goto L75
        L59:
            playFromUri((Uri) _Parcel.access$000(r7, Uri.CREATOR), (Bundle) _Parcel.access$000(r7, Bundle.CREATOR));
            r8.writeNoException();
            goto L75
        L60:
            playFromSearch(r7.readString(), (Bundle) _Parcel.access$000(r7, Bundle.CREATOR));
            r8.writeNoException();
            goto L75
        L61:
            playFromMediaId(r7.readString(), (Bundle) _Parcel.access$000(r7, Bundle.CREATOR));
            r8.writeNoException();
            goto L75
        L62:
            play();
            r8.writeNoException();
            goto L75
        L63:
            setVolumeTo(r7.readInt(), r7.readInt(), r7.readString());
            r8.writeNoException();
            goto L75
        L64:
            adjustVolume(r7.readInt(), r7.readInt(), r7.readString());
            r8.writeNoException();
            goto L75
        L65:
            ParcelableVolumeInfo r18 = getVolumeAttributes();
            r8.writeNoException();
            _Parcel.access$100(r8, r18, 1);
            goto L75
        L66:
            long r19 = getFlags();
            r8.writeNoException();
            r8.writeLong(r19);
            goto L75
        L67:
            PendingIntent r20 = getLaunchPendingIntent();
            r8.writeNoException();
            _Parcel.access$100(r8, r20, 1);
            goto L75
        L68:
            String r21 = getTag();
            r8.writeNoException();
            r8.writeString(r21);
            goto L75
        L69:
            String r22 = getPackageName();
            r8.writeNoException();
            r8.writeString(r22);
            goto L75
        L70:
            boolean r23 = isTransportControlEnabled();
            r8.writeNoException();
            r8.writeInt(r23 ? 1 : 0);
            goto L75
        L71:
            unregisterCallbackListener(IMediaControllerCallback.Stub.asInterface(r7.readStrongBinder()));
            r8.writeNoException();
            goto L75
        L72:
            registerCallbackListener(IMediaControllerCallback.Stub.asInterface(r7.readStrongBinder()));
            r8.writeNoException();
            goto L75
        L73:
            boolean r24 = sendMediaButton((KeyEvent) _Parcel.access$000(r7, KeyEvent.CREATOR));
            r8.writeNoException();
            r8.writeInt(r24 ? 1 : 0);
            goto L75
        L74:
            sendCommand(r7.readString(), (Bundle) _Parcel.access$000(r7, Bundle.CREATOR), (MediaSessionCompat.ResultReceiverWrapper) _Parcel.access$000(r7, MediaSessionCompat.ResultReceiverWrapper.CREATOR));
            r8.writeNoException();
            goto L75
        L14:
            return super.onTransact(r6, r7, r8, r9);
        L21:
            if (r7.readInt() == 0) goto L23;
            r2 = true;
        L23:
            setCaptioningEnabled(r2);
            r8.writeNoException();
            goto L75
        L30:
            if (r7.readInt() == 0) goto L32;
            r2 = true;
        L32:
            setShuffleModeEnabledRemoved(r2);
            r8.writeNoException();
            goto L75
        }
    }

    public static class _Parcel {
        public _Parcel() {
        }

        static /* synthetic */ Object access$000(Parcel r1, Parcelable.Creator r2) {
            return readTypedObject(r1, r2);
        }

        static /* synthetic */ void access$100(Parcel r0, Parcelable r1, int r2) {
            writeTypedObject(r0, r1, r2);
        }

        static /* synthetic */ void access$200(Parcel r0, List r1, int r2) {
            writeTypedList(r0, r1, r2);
        }

        private static <T> T readTypedObject(Parcel r1, Parcelable.Creator<T> r2) {
            if (r1.readInt() != 0) goto L5;
            return null;
        L5:
            return r2.createFromParcel(r1);
        }

        private static <T extends Parcelable> void writeTypedList(Parcel r3, List<T> r4, int r5) {
            if (r4 != null) goto L4;
            r3.writeInt(-1);
            return;
        L4:
            int r0 = r4.size();
            int r1 = 0;
            r3.writeInt(r0);
        L5:
            if (r1 >= r0) goto L9;
            writeTypedObject(r3, r4.get(r1), r5);
            r1 = r1 + 1;
            goto L5
        }

        private static <T extends Parcelable> void writeTypedObject(Parcel r1, T r2, int r3) {
            if (r2 == null) goto L4;
            r1.writeInt(1);
            r2.writeToParcel(r1, r3);
            return;
        L4:
            r1.writeInt(0);
        }
    }

    void addQueueItem(MediaDescriptionCompat r1) throws RemoteException;

    void addQueueItemAt(MediaDescriptionCompat r1, int r2) throws RemoteException;

    void adjustVolume(int r1, int r2, String r3) throws RemoteException;

    void fastForward() throws RemoteException;

    Bundle getExtras() throws RemoteException;

    long getFlags() throws RemoteException;

    PendingIntent getLaunchPendingIntent() throws RemoteException;

    MediaMetadataCompat getMetadata() throws RemoteException;

    String getPackageName() throws RemoteException;

    PlaybackStateCompat getPlaybackState() throws RemoteException;

    List<MediaSessionCompat.QueueItem> getQueue() throws RemoteException;

    CharSequence getQueueTitle() throws RemoteException;

    int getRatingType() throws RemoteException;

    int getRepeatMode() throws RemoteException;

    Bundle getSessionInfo() throws RemoteException;

    int getShuffleMode() throws RemoteException;

    String getTag() throws RemoteException;

    ParcelableVolumeInfo getVolumeAttributes() throws RemoteException;

    boolean isCaptioningEnabled() throws RemoteException;

    boolean isShuffleModeEnabledRemoved() throws RemoteException;

    boolean isTransportControlEnabled() throws RemoteException;

    void next() throws RemoteException;

    void pause() throws RemoteException;

    void play() throws RemoteException;

    void playFromMediaId(String r1, Bundle r2) throws RemoteException;

    void playFromSearch(String r1, Bundle r2) throws RemoteException;

    void playFromUri(Uri r1, Bundle r2) throws RemoteException;

    void prepare() throws RemoteException;

    void prepareFromMediaId(String r1, Bundle r2) throws RemoteException;

    void prepareFromSearch(String r1, Bundle r2) throws RemoteException;

    void prepareFromUri(Uri r1, Bundle r2) throws RemoteException;

    void previous() throws RemoteException;

    void rate(RatingCompat r1) throws RemoteException;

    void rateWithExtras(RatingCompat r1, Bundle r2) throws RemoteException;

    void registerCallbackListener(IMediaControllerCallback r1) throws RemoteException;

    void removeQueueItem(MediaDescriptionCompat r1) throws RemoteException;

    void removeQueueItemAt(int r1) throws RemoteException;

    void rewind() throws RemoteException;

    void seekTo(long r1) throws RemoteException;

    void sendCommand(String r1, Bundle r2, MediaSessionCompat.ResultReceiverWrapper r3) throws RemoteException;

    void sendCustomAction(String r1, Bundle r2) throws RemoteException;

    boolean sendMediaButton(KeyEvent r1) throws RemoteException;

    void setCaptioningEnabled(boolean r1) throws RemoteException;

    void setPlaybackSpeed(float r1) throws RemoteException;

    void setRepeatMode(int r1) throws RemoteException;

    void setShuffleMode(int r1) throws RemoteException;

    void setShuffleModeEnabledRemoved(boolean r1) throws RemoteException;

    void setVolumeTo(int r1, int r2, String r3) throws RemoteException;

    void skipToQueueItem(long r1) throws RemoteException;

    void stop() throws RemoteException;

    void unregisterCallbackListener(IMediaControllerCallback r1) throws RemoteException;
}
