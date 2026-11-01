package android.support.v4.app;

import android.app.Notification;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public interface INotificationSideChannel extends IInterface {
    public static final String DESCRIPTOR = "android.support.v4.app.INotificationSideChannel";

    public static class Default implements INotificationSideChannel {
        public Default() {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // android.support.v4.app.INotificationSideChannel
        public void cancel(String r1, int r2, String r3) throws RemoteException {
        }

        @Override // android.support.v4.app.INotificationSideChannel
        public void cancelAll(String r1) throws RemoteException {
        }

        @Override // android.support.v4.app.INotificationSideChannel
        public void notify(String r1, int r2, String r3, Notification r4) throws RemoteException {
        }
    }

    public static abstract class Stub extends Binder implements INotificationSideChannel {
        static final int TRANSACTION_cancel = 2;
        static final int TRANSACTION_cancelAll = 3;
        static final int TRANSACTION_notify = 1;

        private static class Proxy implements INotificationSideChannel {
            private IBinder mRemote;

            Proxy(IBinder r1) {
                this.mRemote = r1;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // android.support.v4.app.INotificationSideChannel
            public void cancel(String r6, int r7, String r8) throws RemoteException {
                Parcel r0 = Parcel.obtain();
                r0.writeInterfaceToken(INotificationSideChannel.DESCRIPTOR);     // Catch: Throwable -> L6
                r0.writeString(r6);     // Catch: Throwable -> L6
                r0.writeInt(r7);     // Catch: Throwable -> L6
                r0.writeString(r8);     // Catch: Throwable -> L6
                this.mRemote.transact(2, r0, null, 1);     // Catch: Throwable -> L6
                r0.recycle();
                return;
            L6:
                th = move-exception;
                r0.recycle();
                throw th;
            }

            @Override // android.support.v4.app.INotificationSideChannel
            public void cancelAll(String r6) throws RemoteException {
                Parcel r0 = Parcel.obtain();
                r0.writeInterfaceToken(INotificationSideChannel.DESCRIPTOR);     // Catch: Throwable -> L6
                r0.writeString(r6);     // Catch: Throwable -> L6
                this.mRemote.transact(3, r0, null, 1);     // Catch: Throwable -> L6
                r0.recycle();
                return;
            L6:
                th = move-exception;
                r0.recycle();
                throw th;
            }

            public String getInterfaceDescriptor() {
                return INotificationSideChannel.DESCRIPTOR;
            }

            @Override // android.support.v4.app.INotificationSideChannel
            public void notify(String r5, int r6, String r7, Notification r8) throws RemoteException {
                Parcel r0 = Parcel.obtain();
                r0.writeInterfaceToken(INotificationSideChannel.DESCRIPTOR);     // Catch: Throwable -> L6
                r0.writeString(r5);     // Catch: Throwable -> L6
                r0.writeInt(r6);     // Catch: Throwable -> L6
                r0.writeString(r7);     // Catch: Throwable -> L6
                _Parcel.access$100(r0, r8, 0);     // Catch: Throwable -> L6
                this.mRemote.transact(1, r0, null, 1);     // Catch: Throwable -> L6
                r0.recycle();
                return;
            L6:
                th = move-exception;
                r0.recycle();
                throw th;
            }
        }

        public Stub() {
            attachInterface(this, INotificationSideChannel.DESCRIPTOR);
        }

        public static INotificationSideChannel asInterface(IBinder r2) {
            if (r2 != null) goto L5;
            return null;
        L5:
            IInterface r0 = r2.queryLocalInterface(INotificationSideChannel.DESCRIPTOR);
            if (r0 == null) goto L12;
            if ((r0 instanceof INotificationSideChannel) == false) goto L12;
            return (INotificationSideChannel) r0;
        L12:
            return new Proxy(r2);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int r7, Parcel r8, Parcel r9, int r10) throws RemoteException {
            if (r7 >= 1) goto L5;
        L7:
            switch(r7) {
                case 1598968902: goto L11;
                default: goto L8;
            };
        L8:
            switch(r7) {
                case 1: goto L15;
                case 2: goto L14;
                case 3: goto L13;
                default: goto L10;
            };
        L13:
            cancelAll(r8.readString());
        L16:
            return true;
        L14:
            cancel(r8.readString(), r8.readInt(), r8.readString());
            goto L16
        L15:
            notify(r8.readString(), r8.readInt(), r8.readString(), (Notification) _Parcel.access$000(r8, Notification.CREATOR));
            goto L16
        L10:
            return super.onTransact(r7, r8, r9, r10);
        L11:
            r9.writeString(INotificationSideChannel.DESCRIPTOR);
            return true;
        L5:
            if (r7 > 16777215) goto L7;
            r8.enforceInterface(INotificationSideChannel.DESCRIPTOR);
            goto L7
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

        private static <T> T readTypedObject(Parcel r1, Parcelable.Creator<T> r2) {
            if (r1.readInt() != 0) goto L5;
            return null;
        L5:
            return r2.createFromParcel(r1);
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

    void cancel(String r1, int r2, String r3) throws RemoteException;

    void cancelAll(String r1) throws RemoteException;

    void notify(String r1, int r2, String r3, Notification r4) throws RemoteException;
}
