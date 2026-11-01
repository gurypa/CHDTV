package androidx.core.app.unusedapprestrictions;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public interface IUnusedAppRestrictionsBackportCallback extends IInterface {
    public static final String DESCRIPTOR = "androidx.core.app.unusedapprestrictions.IUnusedAppRestrictionsBackportCallback";

    public static class Default implements IUnusedAppRestrictionsBackportCallback {
        public Default() {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // androidx.core.app.unusedapprestrictions.IUnusedAppRestrictionsBackportCallback
        public void onIsPermissionRevocationEnabledForAppResult(boolean r1, boolean r2) throws RemoteException {
        }
    }

    public static abstract class Stub extends Binder implements IUnusedAppRestrictionsBackportCallback {
        static final int TRANSACTION_onIsPermissionRevocationEnabledForAppResult = 1;

        private static class Proxy implements IUnusedAppRestrictionsBackportCallback {
            private IBinder mRemote;

            Proxy(IBinder r1) {
                this.mRemote = r1;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IUnusedAppRestrictionsBackportCallback.DESCRIPTOR;
            }

            @Override // androidx.core.app.unusedapprestrictions.IUnusedAppRestrictionsBackportCallback
            public void onIsPermissionRevocationEnabledForAppResult(boolean r5, boolean r6) throws RemoteException {
                Parcel r0 = Parcel.obtain();
                r0.writeInterfaceToken(IUnusedAppRestrictionsBackportCallback.DESCRIPTOR);     // Catch: Throwable -> L13
                int r1 = 0;
                if (r5 == false) goto L6;
                int r3 = 1;
            L7:
                r0.writeInt(r3);     // Catch: Throwable -> L13
                if (r6 == false) goto L10;
                r1 = 1;
            L10:
                r0.writeInt(r1);     // Catch: Throwable -> L13
                this.mRemote.transact(1, r0, null, 1);     // Catch: Throwable -> L13
                r0.recycle();
                return;
            L6:
                r3 = 0;
            L13:
                th = move-exception;
                r0.recycle();
                throw th;
            }
        }

        public Stub() {
            attachInterface(this, IUnusedAppRestrictionsBackportCallback.DESCRIPTOR);
        }

        public static IUnusedAppRestrictionsBackportCallback asInterface(IBinder r2) {
            if (r2 != null) goto L5;
            return null;
        L5:
            IInterface r0 = r2.queryLocalInterface(IUnusedAppRestrictionsBackportCallback.DESCRIPTOR);
            if (r0 == null) goto L12;
            if ((r0 instanceof IUnusedAppRestrictionsBackportCallback) == false) goto L12;
            return (IUnusedAppRestrictionsBackportCallback) r0;
        L12:
            return new Proxy(r2);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int r6, Parcel r7, Parcel r8, int r9) throws RemoteException {
            if (r6 >= 1) goto L5;
        L7:
            switch(r6) {
                case 1598968902: goto L11;
                default: goto L8;
            };
        L8:
            switch(r6) {
                case 1: goto L13;
                default: goto L10;
            };
        L13:
            boolean r3 = false;
            if (r7.readInt() == 0) goto L16;
            boolean r2 = true;
        L18:
            if (r7.readInt() == 0) goto L20;
            r3 = true;
        L20:
            onIsPermissionRevocationEnabledForAppResult(r2, r3);
            return true;
        L16:
            r2 = false;
            goto L18
        L10:
            return super.onTransact(r6, r7, r8, r9);
        L11:
            r8.writeString(IUnusedAppRestrictionsBackportCallback.DESCRIPTOR);
            return true;
        L5:
            if (r6 > 16777215) goto L7;
            r7.enforceInterface(IUnusedAppRestrictionsBackportCallback.DESCRIPTOR);
            goto L7
        }
    }

    void onIsPermissionRevocationEnabledForAppResult(boolean r1, boolean r2) throws RemoteException;
}
