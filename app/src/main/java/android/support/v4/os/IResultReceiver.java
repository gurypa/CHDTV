package android.support.v4.os;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public interface IResultReceiver extends IInterface {
    public static final String DESCRIPTOR = "android.support.v4.os.IResultReceiver";

    public static class Default implements IResultReceiver {
        public Default() {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // android.support.v4.os.IResultReceiver
        public void send(int r1, Bundle r2) throws RemoteException {
        }
    }

    public static abstract class Stub extends Binder implements IResultReceiver {
        static final int TRANSACTION_send = 1;

        private static class Proxy implements IResultReceiver {
            private IBinder mRemote;

            Proxy(IBinder r1) {
                this.mRemote = r1;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IResultReceiver.DESCRIPTOR;
            }

            @Override // android.support.v4.os.IResultReceiver
            public void send(int r5, Bundle r6) throws RemoteException {
                Parcel r0 = Parcel.obtain();
                r0.writeInterfaceToken(IResultReceiver.DESCRIPTOR);     // Catch: Throwable -> L6
                r0.writeInt(r5);     // Catch: Throwable -> L6
                _Parcel.access$100(r0, r6, 0);     // Catch: Throwable -> L6
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
            attachInterface(this, IResultReceiver.DESCRIPTOR);
        }

        public static IResultReceiver asInterface(IBinder r2) {
            if (r2 != null) goto L5;
            return null;
        L5:
            IInterface r0 = r2.queryLocalInterface(IResultReceiver.DESCRIPTOR);
            if (r0 == null) goto L12;
            if ((r0 instanceof IResultReceiver) == false) goto L12;
            return (IResultReceiver) r0;
        L12:
            return new Proxy(r2);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int r5, Parcel r6, Parcel r7, int r8) throws RemoteException {
            if (r5 >= 1) goto L5;
        L7:
            switch(r5) {
                case 1598968902: goto L11;
                default: goto L8;
            };
        L8:
            switch(r5) {
                case 1: goto L13;
                default: goto L10;
            };
        L13:
            send(r6.readInt(), (Bundle) _Parcel.access$000(r6, Bundle.CREATOR));
            return true;
        L10:
            return super.onTransact(r5, r6, r7, r8);
        L11:
            r7.writeString(IResultReceiver.DESCRIPTOR);
            return true;
        L5:
            if (r5 > 16777215) goto L7;
            r6.enforceInterface(IResultReceiver.DESCRIPTOR);
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

    void send(int r1, Bundle r2) throws RemoteException;
}
