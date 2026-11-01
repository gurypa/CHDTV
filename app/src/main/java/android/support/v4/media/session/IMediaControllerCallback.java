package android.support.v4.media.session;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.support.v4.media.MediaMetadataCompat;
import android.text.TextUtils;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface IMediaControllerCallback extends IInterface {
    public static final String DESCRIPTOR = "android.support.v4.media.session.IMediaControllerCallback";

    public static class Default implements IMediaControllerCallback {
        public Default() {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // android.support.v4.media.session.IMediaControllerCallback
        public void onCaptioningEnabledChanged(boolean r1) throws RemoteException {
        }

        @Override // android.support.v4.media.session.IMediaControllerCallback
        public void onEvent(String r1, Bundle r2) throws RemoteException {
        }

        @Override // android.support.v4.media.session.IMediaControllerCallback
        public void onExtrasChanged(Bundle r1) throws RemoteException {
        }

        @Override // android.support.v4.media.session.IMediaControllerCallback
        public void onMetadataChanged(MediaMetadataCompat r1) throws RemoteException {
        }

        @Override // android.support.v4.media.session.IMediaControllerCallback
        public void onPlaybackStateChanged(PlaybackStateCompat r1) throws RemoteException {
        }

        @Override // android.support.v4.media.session.IMediaControllerCallback
        public void onQueueChanged(List<MediaSessionCompat.QueueItem> r1) throws RemoteException {
        }

        @Override // android.support.v4.media.session.IMediaControllerCallback
        public void onQueueTitleChanged(CharSequence r1) throws RemoteException {
        }

        @Override // android.support.v4.media.session.IMediaControllerCallback
        public void onRepeatModeChanged(int r1) throws RemoteException {
        }

        @Override // android.support.v4.media.session.IMediaControllerCallback
        public void onSessionDestroyed() throws RemoteException {
        }

        @Override // android.support.v4.media.session.IMediaControllerCallback
        public void onSessionReady() throws RemoteException {
        }

        @Override // android.support.v4.media.session.IMediaControllerCallback
        public void onShuffleModeChanged(int r1) throws RemoteException {
        }

        @Override // android.support.v4.media.session.IMediaControllerCallback
        public void onShuffleModeChangedRemoved(boolean r1) throws RemoteException {
        }

        @Override // android.support.v4.media.session.IMediaControllerCallback
        public void onVolumeInfoChanged(ParcelableVolumeInfo r1) throws RemoteException {
        }
    }

    public static abstract class Stub extends Binder implements IMediaControllerCallback {
        static final int TRANSACTION_onCaptioningEnabledChanged = 11;
        static final int TRANSACTION_onEvent = 1;
        static final int TRANSACTION_onExtrasChanged = 7;
        static final int TRANSACTION_onMetadataChanged = 4;
        static final int TRANSACTION_onPlaybackStateChanged = 3;
        static final int TRANSACTION_onQueueChanged = 5;
        static final int TRANSACTION_onQueueTitleChanged = 6;
        static final int TRANSACTION_onRepeatModeChanged = 9;
        static final int TRANSACTION_onSessionDestroyed = 2;
        static final int TRANSACTION_onSessionReady = 13;
        static final int TRANSACTION_onShuffleModeChanged = 12;
        static final int TRANSACTION_onShuffleModeChangedRemoved = 10;
        static final int TRANSACTION_onVolumeInfoChanged = 8;

        private static class Proxy implements IMediaControllerCallback {
            private IBinder mRemote;

            Proxy(IBinder r1) {
                this.mRemote = r1;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IMediaControllerCallback.DESCRIPTOR;
            }

            @Override // android.support.v4.media.session.IMediaControllerCallback
            public void onCaptioningEnabledChanged(boolean r6) throws RemoteException {
                Parcel r0 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaControllerCallback.DESCRIPTOR);     // Catch: Throwable -> L10
                if (r6 == false) goto L6;
                int r2 = 1;
            L7:
                r0.writeInt(r2);     // Catch: Throwable -> L10
                this.mRemote.transact(11, r0, null, 1);     // Catch: Throwable -> L10
                r0.recycle();
                return;
            L6:
                r2 = 0;
            L10:
                th = move-exception;
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaControllerCallback
            public void onEvent(String r5, Bundle r6) throws RemoteException {
                Parcel r0 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaControllerCallback.DESCRIPTOR);     // Catch: Throwable -> L6
                r0.writeString(r5);     // Catch: Throwable -> L6
                _Parcel.access$100(r0, r6, 0);     // Catch: Throwable -> L6
                this.mRemote.transact(1, r0, null, 1);     // Catch: Throwable -> L6
                r0.recycle();
                return;
            L6:
                th = move-exception;
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaControllerCallback
            public void onExtrasChanged(Bundle r6) throws RemoteException {
                Parcel r0 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaControllerCallback.DESCRIPTOR);     // Catch: Throwable -> L6
                _Parcel.access$100(r0, r6, 0);     // Catch: Throwable -> L6
                this.mRemote.transact(7, r0, null, 1);     // Catch: Throwable -> L6
                r0.recycle();
                return;
            L6:
                th = move-exception;
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaControllerCallback
            public void onMetadataChanged(MediaMetadataCompat r6) throws RemoteException {
                Parcel r0 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaControllerCallback.DESCRIPTOR);     // Catch: Throwable -> L6
                _Parcel.access$100(r0, r6, 0);     // Catch: Throwable -> L6
                this.mRemote.transact(4, r0, null, 1);     // Catch: Throwable -> L6
                r0.recycle();
                return;
            L6:
                th = move-exception;
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaControllerCallback
            public void onPlaybackStateChanged(PlaybackStateCompat r6) throws RemoteException {
                Parcel r0 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaControllerCallback.DESCRIPTOR);     // Catch: Throwable -> L6
                _Parcel.access$100(r0, r6, 0);     // Catch: Throwable -> L6
                this.mRemote.transact(3, r0, null, 1);     // Catch: Throwable -> L6
                r0.recycle();
                return;
            L6:
                th = move-exception;
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaControllerCallback
            public void onQueueChanged(List<MediaSessionCompat.QueueItem> r6) throws RemoteException {
                Parcel r0 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaControllerCallback.DESCRIPTOR);     // Catch: Throwable -> L6
                _Parcel.access$200(r0, r6, 0);     // Catch: Throwable -> L6
                this.mRemote.transact(5, r0, null, 1);     // Catch: Throwable -> L6
                r0.recycle();
                return;
            L6:
                th = move-exception;
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaControllerCallback
            public void onQueueTitleChanged(CharSequence r6) throws RemoteException {
                Parcel r0 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaControllerCallback.DESCRIPTOR);     // Catch: Throwable -> L10
                if (r6 == null) goto L6;
                r0.writeInt(1);     // Catch: Throwable -> L10
                TextUtils.writeToParcel(r6, r0, 0);     // Catch: Throwable -> L10
            L7:
                this.mRemote.transact(6, r0, null, 1);     // Catch: Throwable -> L10
                r0.recycle();
                return;
            L6:
                r0.writeInt(0);     // Catch: Throwable -> L10
            L10:
                th = move-exception;
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaControllerCallback
            public void onRepeatModeChanged(int r6) throws RemoteException {
                Parcel r0 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaControllerCallback.DESCRIPTOR);     // Catch: Throwable -> L6
                r0.writeInt(r6);     // Catch: Throwable -> L6
                this.mRemote.transact(9, r0, null, 1);     // Catch: Throwable -> L6
                r0.recycle();
                return;
            L6:
                th = move-exception;
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaControllerCallback
            public void onSessionDestroyed() throws RemoteException {
                Parcel r0 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaControllerCallback.DESCRIPTOR);     // Catch: Throwable -> L6
                this.mRemote.transact(2, r0, null, 1);     // Catch: Throwable -> L6
                r0.recycle();
                return;
            L6:
                th = move-exception;
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaControllerCallback
            public void onSessionReady() throws RemoteException {
                Parcel r0 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaControllerCallback.DESCRIPTOR);     // Catch: Throwable -> L6
                this.mRemote.transact(13, r0, null, 1);     // Catch: Throwable -> L6
                r0.recycle();
                return;
            L6:
                th = move-exception;
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaControllerCallback
            public void onShuffleModeChanged(int r6) throws RemoteException {
                Parcel r0 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaControllerCallback.DESCRIPTOR);     // Catch: Throwable -> L6
                r0.writeInt(r6);     // Catch: Throwable -> L6
                this.mRemote.transact(12, r0, null, 1);     // Catch: Throwable -> L6
                r0.recycle();
                return;
            L6:
                th = move-exception;
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaControllerCallback
            public void onShuffleModeChangedRemoved(boolean r6) throws RemoteException {
                Parcel r0 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaControllerCallback.DESCRIPTOR);     // Catch: Throwable -> L10
                if (r6 == false) goto L6;
                int r2 = 1;
            L7:
                r0.writeInt(r2);     // Catch: Throwable -> L10
                this.mRemote.transact(10, r0, null, 1);     // Catch: Throwable -> L10
                r0.recycle();
                return;
            L6:
                r2 = 0;
            L10:
                th = move-exception;
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.media.session.IMediaControllerCallback
            public void onVolumeInfoChanged(ParcelableVolumeInfo r6) throws RemoteException {
                Parcel r0 = Parcel.obtain();
                r0.writeInterfaceToken(IMediaControllerCallback.DESCRIPTOR);     // Catch: Throwable -> L6
                _Parcel.access$100(r0, r6, 0);     // Catch: Throwable -> L6
                this.mRemote.transact(8, r0, null, 1);     // Catch: Throwable -> L6
                r0.recycle();
                return;
            L6:
                th = move-exception;
                r0.recycle();
                throw th;
            }
        }

        public Stub() {
            attachInterface(this, IMediaControllerCallback.DESCRIPTOR);
        }

        public static IMediaControllerCallback asInterface(IBinder r2) {
            if (r2 != null) goto L5;
            return null;
        L5:
            IInterface r0 = r2.queryLocalInterface(IMediaControllerCallback.DESCRIPTOR);
            if (r0 == null) goto L12;
            if ((r0 instanceof IMediaControllerCallback) == false) goto L12;
            return (IMediaControllerCallback) r0;
        L12:
            return new Proxy(r2);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int r5, Parcel r6, Parcel r7, int r8) throws RemoteException {
            if (r5 < 1) goto L8;
            if (r5 > 16777215) goto L8;
            r6.enforceInterface(IMediaControllerCallback.DESCRIPTOR);
        L8:
            if (r5 != 1598968902) goto L11;
            r7.writeString(IMediaControllerCallback.DESCRIPTOR);
            return true;
        L11:
            boolean r2 = false;
            switch(r5) {
                case 1: goto L33;
                case 2: goto L32;
                case 3: goto L31;
                case 4: goto L30;
                case 5: goto L29;
                case 6: goto L28;
                case 7: goto L27;
                case 8: goto L26;
                case 9: goto L25;
                case 10: goto L22;
                case 11: goto L18;
                case 12: goto L16;
                case 13: goto L15;
                default: goto L14;
            };
        L15:
            onSessionReady();
        L34:
            return true;
        L16:
            onShuffleModeChanged(r6.readInt());
            goto L34
        L25:
            onRepeatModeChanged(r6.readInt());
            goto L34
        L26:
            onVolumeInfoChanged((ParcelableVolumeInfo) _Parcel.access$000(r6, ParcelableVolumeInfo.CREATOR));
            goto L34
        L27:
            onExtrasChanged((Bundle) _Parcel.access$000(r6, Bundle.CREATOR));
            goto L34
        L28:
            onQueueTitleChanged((CharSequence) _Parcel.access$000(r6, TextUtils.CHAR_SEQUENCE_CREATOR));
            goto L34
        L29:
            onQueueChanged(r6.createTypedArrayList(MediaSessionCompat.QueueItem.CREATOR));
            goto L34
        L30:
            onMetadataChanged((MediaMetadataCompat) _Parcel.access$000(r6, MediaMetadataCompat.CREATOR));
            goto L34
        L31:
            onPlaybackStateChanged((PlaybackStateCompat) _Parcel.access$000(r6, PlaybackStateCompat.CREATOR));
            goto L34
        L32:
            onSessionDestroyed();
            goto L34
        L33:
            onEvent(r6.readString(), (Bundle) _Parcel.access$000(r6, Bundle.CREATOR));
            goto L34
        L14:
            return super.onTransact(r5, r6, r7, r8);
        L18:
            if (r6.readInt() == 0) goto L20;
            r2 = true;
        L20:
            onCaptioningEnabledChanged(r2);
            goto L34
        L22:
            if (r6.readInt() == 0) goto L24;
            r2 = true;
        L24:
            onShuffleModeChangedRemoved(r2);
            goto L34
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

    void onCaptioningEnabledChanged(boolean r1) throws RemoteException;

    void onEvent(String r1, Bundle r2) throws RemoteException;

    void onExtrasChanged(Bundle r1) throws RemoteException;

    void onMetadataChanged(MediaMetadataCompat r1) throws RemoteException;

    void onPlaybackStateChanged(PlaybackStateCompat r1) throws RemoteException;

    void onQueueChanged(List<MediaSessionCompat.QueueItem> r1) throws RemoteException;

    void onQueueTitleChanged(CharSequence r1) throws RemoteException;

    void onRepeatModeChanged(int r1) throws RemoteException;

    void onSessionDestroyed() throws RemoteException;

    void onSessionReady() throws RemoteException;

    void onShuffleModeChanged(int r1) throws RemoteException;

    void onShuffleModeChangedRemoved(boolean r1) throws RemoteException;

    void onVolumeInfoChanged(ParcelableVolumeInfo r1) throws RemoteException;
}
