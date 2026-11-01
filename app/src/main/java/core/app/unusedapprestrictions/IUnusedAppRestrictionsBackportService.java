package androidx.core.app.unusedapprestrictions;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public interface IUnusedAppRestrictionsBackportService extends IInterface {
    public static final String DESCRIPTOR = "androidx.core.app.unusedapprestrictions.IUnusedAppRestrictionsBackportService";

    public static class Default implements IUnusedAppRestrictionsBackportService {
        public Default() {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // androidx.core.app.unusedapprestrictions.IUnusedAppRestrictionsBackportService
        public void isPermissionRevocationEnabledForApp(IUnusedAppRestrictionsBackportCallback r1) throws RemoteException {
        }
    }

    public static abstract class Stub extends Binder implements IUnusedAppRestrictionsBackportService {
        static final int TRANSACTION_isPermissionRevocationEnabledForApp = 1;

        private static class Proxy implements IUnusedAppRestrictionsBackportService {
            private IBinder mRemote;

            Proxy(IBinder r1) {
                this.mRemote = r1;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IUnusedAppRestrictionsBackportService.DESCRIPTOR;
            }

            @Override // androidx.core.app.unusedapprestrictions.IUnusedAppRestrictionsBackportService
            public void isPermissionRevocationEnabledForApp(IUnusedAppRestrictionsBackportCallback r5) throws RemoteException {
                Parcel r0 = Parcel.obtain();
                r0.writeInterfaceToken(IUnusedAppRestrictionsBackportService.DESCRIPTOR);     // Catch: Throwable -> L6
                r0.writeStrongInterface(r5);     // Catch: Throwable -> L6
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
            attachInterface(this, IUnusedAppRestrictionsBackportService.DESCRIPTOR);
        }

        public static IUnusedAppRestrictionsBackportService asInterface(IBinder r2) {
            if (r2 != null) goto L5;
            return null;
        L5:
            IInterface r0 = r2.queryLocalInterface(IUnusedAppRestrictionsBackportService.DESCRIPTOR);
            if (r0 == null) goto L12;
            if ((r0 instanceof IUnusedAppRestrictionsBackportService) == false) goto L12;
            return (IUnusedAppRestrictionsBackportService) r0;
        L12:
            return new Proxy(r2);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int r4, Parcel r5, Parcel r6, int r7) throws RemoteException {
            if (r4 >= 1) goto L5;
        L7:
            switch(r4) {
                case 1598968902: goto L11;
                default: goto L8;
            };
        L8:
            switch(r4) {
                case 1: goto L13;
                default: goto L10;
            };
        L13:
            isPermissionRevocationEnabledForApp(IUnusedAppRestrictionsBackportCallback.Stub.asInterface(r5.readStrongBinder()));
            return true;
        L10:
            return super.onTransact(r4, r5, r6, r7);
        L11:
            r6.writeString(IUnusedAppRestrictionsBackportService.DESCRIPTOR);
            return true;
        L5:
            if (r4 > 16777215) goto L7;
            r5.enforceInterface(IUnusedAppRestrictionsBackportService.DESCRIPTOR);
            goto L7
        }
    }

    void isPermissionRevocationEnabledForApp(IUnusedAppRestrictionsBackportCallback r1) throws RemoteException;
}
