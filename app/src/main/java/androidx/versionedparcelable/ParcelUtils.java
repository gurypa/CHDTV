package androidx.versionedparcelable;

import android.os.Bundle;
import android.os.Parcelable;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class ParcelUtils {
    private static final String INNER_BUNDLE_KEY = "a";

    private ParcelUtils() {
    }

    public static <T extends VersionedParcelable> T fromInputStream(InputStream r2) {
        return (T) new VersionedParcelStream(r2, null).readVersionedParcelable();
    }

    public static <T extends VersionedParcelable> T fromParcelable(Parcelable r2) {
        if ((r2 instanceof ParcelImpl) == false) goto L7;
        return (T) ((ParcelImpl) r2).getVersionedParcel();
    L7:
        throw new IllegalArgumentException("Invalid parcel");
    }

    public static <T extends VersionedParcelable> T getVersionedParcelable(Bundle r3, String r4) {
        Bundle r1 = (Bundle) r3.getParcelable(r4);     // Catch: RuntimeException -> L8
        if (r1 != null) goto L6;
        return null;
    L6:
        r1.setClassLoader(ParcelUtils.class.getClassLoader());     // Catch: RuntimeException -> L8
        return (T) fromParcelable(r1.getParcelable("a"));
    L9:
        return null;
    }

    public static <T extends VersionedParcelable> List<T> getVersionedParcelableList(Bundle r6, String r7) {
        ArrayList r0 = new ArrayList();
        Bundle r1 = (Bundle) r6.getParcelable(r7);     // Catch: RuntimeException -> L9
        r1.setClassLoader(ParcelUtils.class.getClassLoader());     // Catch: RuntimeException -> L9
        Iterator r3 = r1.getParcelableArrayList("a").iterator();     // Catch: RuntimeException -> L9
    L5:
        if (r3.hasNext() == false) goto L8;
        r0.add(fromParcelable((Parcelable) r3.next()));     // Catch: RuntimeException -> L9
    L8:
        return r0;
    L10:
        return null;
    }

    public static void putVersionedParcelable(Bundle r3, String r4, VersionedParcelable r5) {
        if (r5 != null) goto L4;
        return;
    L4:
        Bundle r0 = new Bundle();
        r0.putParcelable("a", toParcelable(r5));
        r3.putParcelable(r4, r0);
    }

    public static void putVersionedParcelableList(Bundle r5, String r6, List<? extends VersionedParcelable> r7) {
        Bundle r0 = new Bundle();
        ArrayList<? extends Parcelable> r1 = new ArrayList();
        Iterator<? extends VersionedParcelable> r2 = r7.iterator();
    L4:
        if (r2.hasNext() == false) goto L6;
        r1.add(toParcelable(r2.next()));
        goto L4
    L6:
        r0.putParcelableArrayList("a", r1);
        r5.putParcelable(r6, r0);
    }

    public static void toOutputStream(VersionedParcelable r2, OutputStream r3) {
        VersionedParcelStream r0 = new VersionedParcelStream(null, r3);
        r0.writeVersionedParcelable(r2);
        r0.closeField();
    }

    public static Parcelable toParcelable(VersionedParcelable r1) {
        return new ParcelImpl(r1);
    }
}
